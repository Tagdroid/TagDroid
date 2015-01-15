package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.TimeTable.Time;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;

/* Will download the whole Database at first start or at update */

public class HttpGetDatabaseTime implements ProgressionInterface {
    private final  Context context;
    private final long stopId;
    public  static boolean isFinished = false;
    private static ProgressionInterface progressionInterface;
    private static ArrayList<Time> timesList;
    private static boolean isHttpGetTimesListFinished = false;
    private static int timesCount,timesProgression = 0;

    public HttpGetDatabaseTime(long stopId,Context context, ProgressionInterface progressionInterface) {
        this.context = context;
        this.stopId = stopId;
        HttpGetDatabaseTime.progressionInterface = progressionInterface;
    }

    public void execute() {
        progressionInterface.onDownloadStart();
        HttpGetNextStopTimes httpGetNextStopTimes = new HttpGetNextStopTimes(stopId,this, context);
        httpGetNextStopTimes.execute();
    }

    @Override
    public void onDownloadStart() {
    }
    @Override
    public void onDownloadFailed(Exception e) {
    }
    @Override
    public void onDownloadProgression(int progression, int total) {
    }

    @Override
    public void onDownloadComplete() {
        Log.d("ondowloadcomplete", "ondownloadcomplete");
        if (isHttpGetTimesListFinished) {
            progressionInterface.onDownloadProgression(timesProgression, timesCount);
        } else {
            isHttpGetTimesListFinished = true;
            timesList = ReadSQL.getAllTimes(context);
            timesCount = timesList.size();
            isFinished = true;
            progressionInterface.onDownloadComplete();
        }
    }
}
