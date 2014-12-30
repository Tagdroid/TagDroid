package com.tagdroid.android.Pages.StationDetail;

import android.os.AsyncTask;
import android.util.Log;

import com.tagdroid.tagapi.ProgressionInterface;

import java.util.ArrayList;
import java.util.List;

public class GetHoraires extends AsyncTask<Void, Void, Integer> {
    private List<StationCard> StationCardList = new ArrayList<>();
    protected ProgressionInterface progressionInterface;
    private Exception exception;


    public GetHoraires(ProgressionInterface progressionInterface) {
        this.progressionInterface = progressionInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressionInterface.onDownloadStart();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            StationCardList.add(new StationCard("direction1", "5 min","17 min"));
            StationCardList.add(new StationCard("direction2", "8 min","26 min"));
            StationCardList.add(new StationCard("direction3", "12 min","47 min"));
        } catch (Exception e) {
            exception = e;
            return -1;
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer resultState) {
        Log.d("StationCARD","Fin de L'Async");
        if (resultState >= 0)
            progressionInterface.onDownloadComplete();
        else
            progressionInterface.onDownloadFailed(exception);
    }

    public List<StationCard> getResult() {
        return StationCardList;
    }
}
