package com.tagdroid.tagapi;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class HttpGetTask extends AsyncTask<Void, Integer, Void> {
    private String RequestUrl;
    protected String responseString;

    protected ProgressionInterface progressionInterface;
    public HttpGetTask(String RequestUrl, ProgressionInterface progressionInterface) {
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
        try {
            URL url = new URL(RequestUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte data[] = new byte[1024];
            Integer total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress(total);
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            responseString = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
    }

    @Override
    protected void onPostExecute(Void result) {
        progressionInterface.onDownloadComplete(responseString);
    }
}
