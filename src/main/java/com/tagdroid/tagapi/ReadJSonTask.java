package com.tagdroid.tagapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.Locality;
import com.tagdroid.tagapi.JSonApi.Transport.LogicalStop;
import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;
import com.tagdroid.tagapi.SQLApi.Transport.LocalityDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LogicalStopDAO;
import com.tagdroid.tagapi.SQLApi.Transport.MySQLiteHelper;
import com.tagdroid.tagapi.SQLApi.Transport.PhysicalStopDAO;

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
            progressionInterface.onJSonParsingFailed("Void JSon data received…");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            String message = jsonObj.getString("Message");
            Log.d("parsage", "message : " + message);
            switch (jsonObj.getInt("StatusCode")) {
                case 200:
                    Log.d("parsage", jsonString);
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