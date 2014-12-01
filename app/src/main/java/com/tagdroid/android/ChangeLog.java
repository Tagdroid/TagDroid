package com.tagdroid.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChangeLog {
    private static final String VERSIONNAME_KEY = "PREFS_VERSIONNAME_KEY";
    private static Context context;
    private static boolean initialized = false;
    private static String
            oldVersionName,
            nowVersionName;

    private boolean fullLog;
    private static Listmode listMode = Listmode.NONE;

    public ChangeLog(Context context) {
        ChangeLog.context = context;
        if (!initialized) {
            // Si le Changelog n'est pas initialisé on va regarder si l'appli a changé de version
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            oldVersionName = preferences.getString(VERSIONNAME_KEY, "NO_OLD_VERSION_NAME");
            try {
                nowVersionName = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
                // save new version number to preferences
                preferences.edit().putString(VERSIONNAME_KEY, nowVersionName).apply();
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                nowVersionName = "NO_VERSION_NAME";
            }
            initialized = true;
        }
        Log.d("changelog", oldVersionName + " to " + nowVersionName);
    }

    public void showIfNewVersion(boolean fullLog) {
        if (isAppNewVersion())
            show(fullLog);
    }

    public void show(boolean fullLog) {
        this.fullLog = fullLog;
        getLogDialog().show();
    }

    private static boolean isAppNewVersion() {
        return (!nowVersionName.equals(oldVersionName));
    }

    private AlertDialog getLogDialog() {
        WebView webView = new WebView(context);
        webView.loadDataWithBaseURL(null, getLog(), "text/html", "UTF-8", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(
                        fullLog ? R.string.changelog_full_title : R.string.changelog_title))
                .setView(webView)
                .setPositiveButton(context.getResources().getString(R.string.changelog_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        if (!fullLog)
            builder.setNegativeButton(context.getResources().getString(R.string.changelog_show_full),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            show(true);
                        }
                    });
        return builder.create();
    }

    private String getLog() {
        StringBuilder changeLogString = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(context.getResources().openRawResource(R.raw.changelog)));
            String line, content;
            char marker;
            boolean EndOfLog = false;
            while (!EndOfLog && (line = br.readLine()) != null) {
                line = line.trim();
                if (line.length()==0)
                    continue;
                marker = line.charAt(0);
                content = line.substring(1).trim();
                switch (marker) {
                    case '$':
                        String version = line.substring(1).trim();
                        if (!fullLog && (
                                version.equals(oldVersionName)) )
                            EndOfLog = true;
                        break;
                    case '%': // line contains version title
                        changeLogString.append(closeList())
                                .append("<div class='title'>")
                                .append(content).append("</div>\n");
                        break;
                    case '_': // line contains version description
                        changeLogString.append(closeList())
                                .append("<div class='subtitle'>")
                                .append(content).append("</div>\n");
                        break;
                    case '!': // line contains free text
                        changeLogString.append(closeList())
                                .append("<div class='freetext'>")
                                .append(content).append("</div>\n");
                        break;
                    case '#': // line contains numbered list item
                        changeLogString.append(openList(Listmode.ORDERED))
                                .append("<li>")
                                .append(content).append("</li>\n");
                        break;
                    case '*': // line contains bullet list item
                        changeLogString.append(openList(Listmode.UNORDERED))
                                .append("<li>")
                                .append(content).append("</li>\n");
                        break;
                    default: // no special character: just use line as is
                        changeLogString.append(closeList())
                                .append(line).append("\n");
                }
            }
            changeLogString.append(closeList())
                    .append("</body></html>");
            br.close();
            return changeLogString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String openList(Listmode newListMode) {
        if (newListMode.equals(listMode) ||
            newListMode.equals(Listmode.NONE))
            return "";
        String returnString = closeList().concat("<div class='list'>");
        switch (newListMode) {
            case ORDERED:
                returnString+="<ol>\n";
                break;
            case UNORDERED:
                returnString+="<ul>\n";
                break;
        }
        listMode = newListMode;
        return returnString;
    }

    private static String closeList() {
        String returnString = "";
        switch (listMode) {
            case ORDERED:
                returnString = "</ol></div>\n";
                break;
            case UNORDERED:
                returnString = "</ul></div>\n";
                break;
        }
        listMode = Listmode.NONE;
        return returnString;
    }

    private static enum Listmode {
        NONE,
        ORDERED,
        UNORDERED,
    }
}