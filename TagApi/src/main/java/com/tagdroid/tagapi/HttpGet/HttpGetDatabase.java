package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;

/* Will download the whole Database at first start or at update */

public class HttpGetDatabase implements ProgressionInterface {
    private final Context context;
    public boolean isFinished = false;
    ProgressionInterface progressionInterface;
    ArrayList<Line> linesList;
    private boolean isHttpGetLinesListFinished = false;
    private int linesCount,
            linesProgression = 0,
            directionsProgression = 0;

    public HttpGetDatabase(Context context, ProgressionInterface progressionInterface) {
        this.context = context;
        this.progressionInterface = progressionInterface;
    }

    public void execute() {
        progressionInterface.onDownloadStart();
        HttpGetLinesList httpGetLinesList = new HttpGetLinesList(this, context);
        httpGetLinesList.execute();
    }

    @Override
    public void onDownloadStart() {
    }

    @Override
    public void onDownloadFailed(Exception e) {
    }

    @Override
    public void onDownloadComplete() {
        Log.d("ondowloadcomplete", "ondownloadcomplete");
        if (isHttpGetLinesListFinished) {
            Log.d("httpgetdatabase", "downloadline " + linesProgression + "/" + linesCount);
            downloadNext();
        } else {
            isHttpGetLinesListFinished = true;
            linesList = (new ReadSQL(context)).getAllLines();
            linesCount = linesList.size();
            downloadNext();
        }
    }

    private void downloadNext() {
        Line actualLine = linesList.get(linesProgression);
        int directionsCount = actualLine.getDirectionList().length;
        if (directionsProgression < directionsCount) {
            // On continue sur la même ligne
            new HttpGetLineStops(actualLine.getId(),
                    actualLine.getDirectionList()[directionsProgression].getDirection(),
                    this, context).execute();
            directionsProgression++;
        } else if (linesProgression < linesCount - 1) {
            // On passe à la ligne suivante, sinon c'est fini
            directionsProgression = 0;
            linesProgression++;
            downloadNext();
        } else {
            isFinished = true;
            progressionInterface.onDownloadComplete();
        }
    }
}
