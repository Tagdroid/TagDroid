package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tagdroid.tagapi.ProgressionInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public abstract class HttpGetTask extends AsyncTask<Void, Integer, Void> {
    protected Context context;
    private String RequestUrl;
    protected String responseString = "";

    protected ProgressionInterface progressionInterface;
    public HttpGetTask(String RequestUrl, ProgressionInterface progressionInterface, Context context) {
        this.context = context;
        this.RequestUrl = RequestUrl;
        this.progressionInterface = progressionInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressionInterface.onDownloadStart();
    }

    @Override
    protected Void doInBackground(Void... params) {
        downloadTask();
        if (responseString.equals("")) {
            return null; // There has been an error to manage.
        }
        readJsonTask();
        return null;
    }

    private void downloadTask() {
        try {
            URL url = new URL(RequestUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // download the file
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String lineBuffer;
            while ((lineBuffer = in.readLine()) != null)
                responseString+=lineBuffer;
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readJsonTask() {
        try {
            JSONObject jsonObj = new JSONObject(responseString);
            String message = jsonObj.getString("Message");
            switch (jsonObj.getInt("StatusCode")) {
                case 200:
                    readData(jsonObj.getJSONArray("Data"));
                    break;
               /* case 300:
                    onVoidResult();
                    break;
                case 400:
                    onBadRequest();
                    break;
                case 500:
                    onRequestError();
                    break;*/
                default:
                    throw new Exception("Response StatusCode is " + jsonObj.getInt("StatusCode")
                            +" with the message : " + message);
                    //onUnknownStatusCode();
            }
        } catch (JSONException e) {
            Log.e("JSonParsing", "Could not correctly parse received JSon");
            Log.e("JSonParsingcontent : ", responseString);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public abstract void readData(JSONArray jsonData);

    @Override
    protected void onPostExecute(Void result) {
        progressionInterface.onDownloadComplete();
    }
}
