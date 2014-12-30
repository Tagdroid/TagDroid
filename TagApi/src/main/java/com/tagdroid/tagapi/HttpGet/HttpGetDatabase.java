package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;
import com.tagdroid.tagapi.SQLApi.Transport.DirectionDAO;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
/* Will download the whole Database at first start or at update */

public class HttpGetDatabase implements ProgressionInterface {
    private final Context context;
    public boolean isFinished = false;

    ProgressionInterface progressionInterface;

    private boolean isHttpGetLinesListFinished = false;
    ArrayList<Line> linesList;

    public HttpGetDatabase(Context context, ProgressionInterface progressionInterface) {
        this.context = context;
        this.progressionInterface = progressionInterface;
    }

    public void execute() {
        progressionInterface.onDownloadStart();
        HttpGetLinesList httpGetLinesList = new HttpGetLinesList(this, context);
        try {
            httpGetLinesList.execute().get(); //TODO Pas du tout bon…
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        isFinished = true;
        progressionInterface.onDownloadComplete();
    }

    private void downloadAllLines() {
        Log.d("httpgetdatabase", "downloadalllines");
        for (Line line: linesList) {
            if (line.getDirectionList() != null)
                for (Direction direction : line.getDirectionList())
                    try {
                        Log.d("httpgetdatabase", "downloadline " + line.getNumber()+ ", dir " + direction.getDirection());
                        (new HttpGetLineStops(line.getId(), direction.getDirection(), this, context)).execute().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
        }
    }

    @Override
    public void onDownloadStart() {
    }

    @Override
    public void onDownloadFailed(Exception e) {
    }

    @Override
    public void onDownloadComplete() {
        if (!isHttpGetLinesListFinished) {
            isHttpGetLinesListFinished = true;
            linesList = (new ReadSQL(context)).getAllLines();
            //downloadAllLines();
        }
    }
}
