package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.os.AsyncTask;

import com.tagdroid.tagapi.ProgressionInterface;

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
        // progressionInterface.onDownloadStart();
    }

    @Override
    protected Void doInBackground(Void... params) {
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
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        progressionInterface.onDownloadComplete();
    }
}
