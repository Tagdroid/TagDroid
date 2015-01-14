package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.util.Log;

import com.tagdroid.tagapi.ProgressionInterface;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpGetNextStopTimes extends HttpGetTask {
    public HttpGetNextStopTimes(long stopId, ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/TimeTables/v1/GetNextStopHours/json?key=TAGDEV"
                        + "&StopId=" + stopId
                        + "&Date=" + formatedDate(), //Pas besoin du Time vu que c'est les derniers horaires de la journée
                progressionInterface, context);
    }

    public static String formatedDate() {;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date(System.currentTimeMillis()));

        Log.d("date", date);
        return date;
    }

    @Override
    public void readData(JSONArray jsonData) {

    }
}
