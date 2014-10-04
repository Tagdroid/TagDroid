package com.tagdroid.tagdroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ChangeLog {
    static final String EOCL = "END_OF_CHANGE_LOG";
    static final String TAG = "ChangeLog";
    private static final String VERSIONNAME_KEY = "PREFS_VERSIONNAME_KEY";
    final Context context;
    String oldVersionName, nowVersionName;
    Listmode listMode = Listmode.NONE;
    StringBuffer sb = null;

    public ChangeLog(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public ChangeLog(Context context, SharedPreferences preferences) {
        this.context = context;
        this.oldVersionName = preferences.getString(VERSIONNAME_KEY, "");
        try {
            nowVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // save new version number to preferences
        preferences.edit().putString(VERSIONNAME_KEY, this.nowVersionName).apply();
    }

    public boolean isAppNewerVersion() {
        return (!nowVersionName.equals(oldVersionName));
    }

    public boolean firstRunEver() {
        return (oldVersionName.equals(""));
    }

    /**
     * @return an AlertDialog displaying the changes since the previous
     * installed version of your app (what's new).
     */
    public AlertDialog getLogDialog() {
        return this.getDialog(false);
    }

    /**
     * @return an AlertDialog with a full change log displayed
     */
    public AlertDialog getFullLogDialog() {
        return this.getDialog(true);
    }

    private AlertDialog getDialog(boolean full) {
        WebView wv = new WebView(this.context);
        wv.setBackgroundColor(0);
        wv.loadDataWithBaseURL(null, this.getLog(full), "text/html", "UTF-8", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(context.getResources().getString(full
                ? R.string.changelog_full_title
                : R.string.changelog_title))
                .setView(wv)
                .setCancelable(false)
                .setPositiveButton(
                        context.getResources().getString(
                                R.string.changelog_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        if (!full) {
            builder.setNegativeButton(R.string.changelog_show_full,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            getFullLogDialog().show();
                        }
                    });
        }

        return builder.create();
    }

    /**
     * @return HTML displaying the changes since the previous
     * installed version of your app (what's new)
     */
    public String getLog() {
        return this.getLog(false);
    }

    /**
     * @return HTML which displays full change log
     */
    public String getFullLog() {
        return this.getLog(true);
    }

    private String getLog(boolean full) {
        sb = new StringBuffer();
        try {
            InputStream ins = context.getResources().openRawResource(R.raw.changelog);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            String line;
            boolean advanceToEOVS = false; // if true: ignore further version sections
            while ((line = br.readLine()) != null) {
                line = line.trim();
                char marker = line.length() > 0 ? line.charAt(0) : 0;
                if (marker == '$') {
                    // begin of a version section
                    this.closeList();
                    String version = line.substring(1).trim();
                    // stop output?
                    if (!full) {
                        if (this.oldVersionName.equals(version)) {
                            advanceToEOVS = true;
                        } else if (version.equals(EOCL)) {
                            advanceToEOVS = false;
                        }
                    }
                } else if (!advanceToEOVS) {
                    switch (marker) {
                        case '%':
                            // line contains version title
                            this.closeList();
                            sb.append("<div class='title'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '_':
                            // line contains version title
                            this.closeList();
                            sb.append("<div class='subtitle'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '!':
                            // line contains free text
                            this.closeList();
                            sb.append("<div class='freetext'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '#':
                            // line contains numbered list item
                            this.openList(Listmode.ORDERED);
                            sb.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        case '*':
                            // line contains bullet list item
                            this.openList(Listmode.UNORDERED);
                            sb.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        default:
                            // no special character: just use line as is
                            this.closeList();
                            sb.append(line).append("\n");
                    }
                }
            }
            this.closeList();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private void openList(Listmode listMode) {
        if (this.listMode != listMode) {
            closeList();
            if (listMode == Listmode.ORDERED) {
                sb.append("<div class='list'><ol>\n");
            } else if (listMode == Listmode.UNORDERED) {
                sb.append("<div class='list'><ul>\n");
            }
            this.listMode = listMode;
        }
    }

    private void closeList() {
        if (this.listMode == Listmode.ORDERED) {
            sb.append("</ol></div>\n");
        } else if (this.listMode == Listmode.UNORDERED) {
            sb.append("</ul></div>\n");
        }
        this.listMode = Listmode.NONE;
    }


    private enum Listmode {
        NONE,
        ORDERED,
        UNORDERED,
    }
}