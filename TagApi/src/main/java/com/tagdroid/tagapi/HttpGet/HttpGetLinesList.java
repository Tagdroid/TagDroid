package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.util.Log;

import com.tagdroid.tagapi.JSon2SQL.ReadJSonLinesList;
import com.tagdroid.tagapi.ProgressionInterface;

public class HttpGetLinesList extends HttpGetTask {
    public HttpGetLinesList(ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLines/json?key=TAGDEV&OperatorId=1",
                progressionInterface, context);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.d("httpgetlineslist", "this one finished");
        new ReadJSonLinesList(responseString, progressionInterface, context).execute();
    }
}