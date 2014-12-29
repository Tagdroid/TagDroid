package com.tagdroid.tagapi.HttpGet;

import android.content.Context;

import com.tagdroid.tagapi.ProgressionInterface;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpGetNextStopTimes extends HttpGetTask {
    public HttpGetNextStopTimes(int stopId, int lineId, int direction,
                                ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/TimeTables/v1/GetNextStopHours/json?key=TAGDEV"
                        +"&StopId=" + stopId
                        +"&DateTime=" + formatedDate(),
                progressionInterface, context);
    }

    public static String formatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    @Override
    public void readData(JSONArray jsonData) {

    }
}
