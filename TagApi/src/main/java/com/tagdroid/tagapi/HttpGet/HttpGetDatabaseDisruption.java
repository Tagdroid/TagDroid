package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Disruption.Disruption;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;

/* Will download the whole Database at first start or at update */

public class HttpGetDatabaseDisruption implements ProgressionInterface {
    private final  Context context;
    public  static boolean isFinished = false;
    private static ProgressionInterface progressionInterface;
    private static ArrayList<Disruption> DisruptionsList;
    private static boolean isHttpGetDisruptionsFinished = false;
    private static int disruptionsCount,disruptionsProgression = 0;

    public HttpGetDatabaseDisruption(Context context, ProgressionInterface progressionInterface) {
        this.context = context;
        HttpGetDatabaseDisruption.progressionInterface = progressionInterface;
    }

    public void execute() {
        progressionInterface.onDownloadStart();
        HttpGetDisruptions httpGetDisruptions = new HttpGetDisruptions(this, context);
        httpGetDisruptions.execute();
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
        if (isHttpGetDisruptionsFinished) {
            progressionInterface.onDownloadProgression(disruptionsProgression, disruptionsCount);
        } else {
            isHttpGetDisruptionsFinished = true;
            DisruptionsList = ReadSQL.getAllDisruptions(context);
            disruptionsCount = DisruptionsList.size();
            isFinished = true;
            disruptionsProgression++;
            progressionInterface.onDownloadComplete();
        }
    }
}
