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

public class ReadJSonTask extends AsyncTask<Void, Integer, Void> {
    private final Context context;
    private String jsonStr;
    private ProgressionInterface progressionInterface;
    private ProgressBar progressBar;

    public ReadJSonTask(String jsonString, ProgressionInterface progressionInterface, Context context) {
        super();
        this.context = context;
        this.progressionInterface = progressionInterface;
        this.jsonStr = jsonString;
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
        if (jsonStr != null) {
            // Log.d("parsage", jsonStr);
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String message = jsonObj.getString("Message");
                Integer statusCode = jsonObj.getInt("StatusCode");
                Log.d("parsage", "message : " + message + " : statuscode : " + statusCode);
                switch (statusCode) {
                    case 200:
                        readDataNow(jsonObj.getJSONArray("Data"));
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
        } else {
            progressionInterface.onJSonParsingFailed("Void JSon data received…");
        }
        return null;
    }


    private void readDataNow(JSONArray jsonData) {
        MySQLiteHelper dbHelper = new MySQLiteHelper("TagDatabase.db",context, null);
        SQLiteDatabase bdd = dbHelper.getWritableDatabase();
        bdd.beginTransaction();

        PhysicalStopDAO physicalStopDAO = new PhysicalStopDAO(dbHelper, dbHelper.isCreating,
                dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);
        LogicalStopDAO logicalStopDAO = new LogicalStopDAO(bdd, dbHelper.isCreating,
                dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);
        LocalityDAO localityDAO = new LocalityDAO(bdd, dbHelper.isCreating,
                dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);

        PhysicalStop physicalStop;
        LogicalStop logicalStop;
        Locality locality;
        Line line;

        Integer length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                physicalStop = new PhysicalStop(jsonData.getJSONObject(i));
                physicalStopDAO.add(physicalStop);
                logicalStop = physicalStop.getLogicalStop();
                logicalStopDAO.add(logicalStop);
                locality = physicalStop.getLocality();
                localityDAO.add(locality);
                locality = logicalStop.getLocality();
                localityDAO.add(locality);
                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage de station", i + " / " + length);
                // e.printStackTrace();
            }

        bdd.setTransactionSuccessful();
        bdd.endTransaction();
        dbHelper.close();
    }
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