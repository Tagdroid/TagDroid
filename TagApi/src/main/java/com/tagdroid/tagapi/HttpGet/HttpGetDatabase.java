package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HttpGetDatabase implements ProgressionInterface {
    private final Context context;
    public boolean isFinished = false;

    private boolean isHttpGetLinesListFinished = false;

    private HttpGetLineStops httpGetLineStops;

    ArrayList<Line> linesList;
    private int linesCount,
            lineProgress,
            lineDirectionProgress;

    public HttpGetDatabase(Context context) {
        this.context = context;
    }

    public void execute() {
        HttpGetLinesList httpGetLinesList = new HttpGetLinesList(this, context);
        httpGetLinesList.execute();
        Log.d("HttpGetDatabase", "httpgetlineslist just called");
    }

    private void downloadAllLines() {
        for (Line line: linesList) {
            if (line.getDirectionList() != null)
                for (Direction direction : line.getDirectionList())
                    try {
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
    }

    @Override
    public void onJSonParsingStarted() {
    }

    @Override
    public void onJSonParsingFailed(Exception e) {
    }

    @Override
    public void onJSonParsingComplete() {
        if (!isHttpGetLinesListFinished) {
            isHttpGetLinesListFinished = true;
            linesList   = (new ReadSQL(context)).getAllLines();
            linesCount  = linesList.size();
            lineProgress=0;
            downloadAllLines();
        }
    }
}
