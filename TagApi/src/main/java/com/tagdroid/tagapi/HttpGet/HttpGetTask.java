package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.JSonApi.JSonStatusCodes;

public abstract class HttpGetTask extends AsyncTask<Void, Integer, Void> {
    protected Context context;
    protected String RequestUrl;
    protected HttpGetInterface httpGetInterface;

    public HttpGetTask(String RequestUrl, HttpGetInterface httpGetInterface, Context context) {
        this.context = context;
        this.RequestUrl = RequestUrl;
        this.httpGetInterface = httpGetInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        httpGetInterface.onHttpGetStart();
    }
    @Override
    protected Void doInBackground(Void... params) {
        String responseString = downloadTask();
        if (responseString.equals("")) {
            httpGetInterface.onHttpGetDownloadFailed();
            return null;
        }
        readJSonTask(responseString);
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        httpGetInterface.onHttpGetReadJSonFinished();
    }

    private String downloadTask() {
        String responseString="";
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
        return responseString;
    }

    private void readJSonTask(String responseString) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            int StatusCode = jsonObject.getInt("StatusCode");
            if (StatusCode == JSonStatusCodes.NO_ERROR) {
                readData(jsonObject);
                            } else {
                String message = jsonObject.getString("Message");
                (new Exception("StatusCode "+StatusCode + " and message : "+message)).printStackTrace();
                httpGetInterface.onHttpGetBadStatusCode(StatusCode, message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            httpGetInterface.onHttpGetReadJSonFailed(e);
        }
    }
    public abstract void readData(JSONObject jsonObject) throws JSONException;

}
