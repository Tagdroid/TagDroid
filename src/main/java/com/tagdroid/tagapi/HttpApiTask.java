package com.tagdroid.tagapi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpApiTask extends AsyncTask<Void, Integer, Void> {
    private String RequestFullUrl;
    private String responseString;

    private ProgressionInterface progressionInterface;
    private ProgressBar progressBar;
    private Integer fileLength;

    public HttpApiTask(ProgressionInterface progressionInterface, String apiPath) {
        this.progressionInterface = progressionInterface;
        RequestFullUrl = "http://transinfoservice.ws.cityway.fr/TAG/api" + apiPath + "/json?key=TAGDEV";
    }
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressionInterface.onDownloadStart();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(RequestFullUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // This could be useful but the server is kinda slow…
            fileLength = connection.getContentLength();
            Log.d("filelength", ""+fileLength);
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
        if (progressBar != null)
            progressBar.setProgress(values[0] * 100 / fileLength);
    }

    @Override
    protected void onPostExecute(Void result) {
        if (progressBar != null)
            progressBar.setProgress(100);
        progressionInterface.onDownloadComplete(responseString);
    }
}
