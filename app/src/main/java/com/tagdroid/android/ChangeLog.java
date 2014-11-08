package com.tagdroid.android;

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
    private static final String VERSIONNAME_KEY = "PREFS_VERSIONNAME_KEY";
    private static final String EOCL = "END_OF_CHANGE_LOG";
    private static Context context;
    private boolean fullLog;
    private static Listmode listMode = Listmode.NONE;
    private static StringBuffer changeLogString;
    private static String oldVersionName, nowVersionName;

    public void init(Context context, boolean fullLog) {
        ChangeLog.context = context;
        this.fullLog = fullLog;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        oldVersionName = preferences.getString(VERSIONNAME_KEY, "");
        try {
            nowVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // save new version number to preferences
        preferences.edit().putString(VERSIONNAME_KEY, nowVersionName).apply();

        if (isAppNewerVersion())
            getLogDialog().show();
    }

    public void start(boolean fullLog) {
        this.fullLog = fullLog;
        getLogDialog().show();
    }

    private static boolean isAppNewerVersion() {
        return (!nowVersionName.equals(oldVersionName));
    }

    private static boolean firstRunEver() {
        return (oldVersionName.equals(""));
    }

    private AlertDialog getLogDialog() {
        return getDialog();
    }


    private AlertDialog getDialog() {
        WebView webView = new WebView(context);
        webView.setBackgroundColor(0);
        webView.loadDataWithBaseURL(null, getLog(), "text/html", "UTF-8", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources()
                .getString(fullLog ? R.string.changelog_full_title : R.string.changelog_title))
                .setView(webView)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.changelog_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        if (!fullLog)
            builder.setNegativeButton(context.getResources().getString(R.string.changelog_show_full),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            fullLog = true;
                            getLogDialog().show();
                        }
                    });
        return builder.create();
    }

    private String getLog() {
        changeLogString = new StringBuffer();
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
                    closeList();
                    String version = line.substring(1).trim();
                    // stop output?
                    if (!fullLog)
                        if (oldVersionName.equals(version))
                            advanceToEOVS = true;
                        else if (version.equals(EOCL))
                            advanceToEOVS = false;

                } else if (!advanceToEOVS) {
                    switch (marker) {
                        case '%':
                            // line contains version title
                            closeList();
                            changeLogString.append("<div class='title'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '_':
                            // line contains version title
                            closeList();
                            changeLogString.append("<div class='subtitle'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '!':
                            // line contains free text
                            closeList();
                            changeLogString.append("<div class='freetext'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '#':
                            // line contains numbered list item
                            openList(Listmode.ORDERED);
                            changeLogString.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        case '*':
                            // line contains bullet list item
                            openList(Listmode.UNORDERED);
                            changeLogString.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        default:
                            // no special character: just use line as is
                            closeList();
                            changeLogString.append(line).append("\n");
                    }
                }
            }
            closeList();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return changeLogString.toString();
    }

    private static void openList(Listmode newListMode) {
        if (listMode == newListMode)
            return;
        switch (listMode) {
            case ORDERED:
                changeLogString.append("<div class='list'><ol>\n");
                break;
            case UNORDERED:
                changeLogString.append("<div class='list'><ul>\n");
                break;
        }
        listMode = newListMode;
    }

    private static void closeList() {
        switch (listMode) {
            case ORDERED:
                changeLogString.append("</ol></div>\n");
                break;
            case UNORDERED:
                changeLogString.append("</ul></div>\n");
                break;
        }
        listMode = Listmode.NONE;
    }

    private static enum Listmode {
        NONE,
        ORDERED,
        UNORDERED,
    }
}