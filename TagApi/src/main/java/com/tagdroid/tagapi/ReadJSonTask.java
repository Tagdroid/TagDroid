package com.tagdroid.tagapi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
    Je lis chaque Token du stream en l'interprétant comme il se doit.
    Pas spécifique aux PhysicalStop, mais à toute l'API.
 */

public abstract class ReadJSonTask extends AsyncTask<Void, Integer, Void> {
    private String jsonString;
    private ProgressionInterface progressionInterface;
    private ProgressBar progressBar;

    public ReadJSonTask(String jsonString, ProgressionInterface progressionInterface) {
        super();
        this.progressionInterface = progressionInterface;
        this.jsonString = jsonString;
    }
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressionInterface.onJSonParsingStarted();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        if (jsonString == null) {
            progressionInterface.onJSonParsingFailed(new JSONException("Void JSON"));
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            String message = jsonObj.getString("Message");
            switch (jsonObj.getInt("StatusCode")) {
                case 200:
                    readData(jsonObj.getJSONArray("Data"));
                case 300:
                    onVoidResult();
                case 400:
                    onBadRequest();
                case 500:
                    onRequestError();
                default:
                    onUnknownStatusCode();
            }
        } catch (JSONException e) {
            Log.e("JSonParsing", "Could not correctly parse received JSon");
            Log.e("JSonParsingcontent : ", jsonString);
            e.printStackTrace();
        }
        return null;
    }

    public abstract void readData(JSONArray jsonData);

    @Override
    protected void onProgressUpdate(Integer... values){
       if (progressBar != null)
            progressBar.setProgress(values[0] * 100 /values[1]);
    }

    private static void onVoidResult() {
    }
    private static void onBadRequest() {

    }
    private static void onRequestError() {
    }
    private static void onUnknownStatusCode() {
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        progressionInterface.onJSonParsingComplete();
    }
}