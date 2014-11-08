package com.tagdroid.tagapi;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpGetLinesList extends AsyncTask<Void, Integer, Void> {
    private final String RequestUrl =
            "http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLines/json?key=TAGDEV&OperatorId=1";
    private String responseString;

    private ProgressionInterface progressionInterface;

    public HttpGetLinesList(ProgressionInterface progressionInterface) {
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
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        catch (IOException e) {
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
