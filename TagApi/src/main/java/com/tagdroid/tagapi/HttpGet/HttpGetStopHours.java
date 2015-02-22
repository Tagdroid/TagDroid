package com.tagdroid.tagapi.HttpGet;

import android.annotation.SuppressLint;
import android.content.Context;

import com.tagdroid.tagapi.JSonApi.TimeTable.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HttpGetStopHours extends HttpGetTask {
    public ArrayList<Time> StopPassingTimeList = new ArrayList<>();

    public HttpGetStopHours(long StopId, long LineId, long LineDirection,
                            HttpGetInterface httpGetInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/TimeTables/v1/GetStopHours/json?key=TAGDEV"
                + "&StopId="        + StopId
                + "&Date="          + formatedTAGDate()
                + "&LineId="        + LineId
                + "&LineDirection=" + LineDirection,
                httpGetInterface, context);
    }

    @SuppressLint("SimpleDateFormat")
    protected static String formatedTAGDate() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY)<3)
            cal.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    @Override
    public void readData(JSONObject jsonObject) throws JSONException {
        ArrayList<Time> TempStopPassingTimeList = Time.StopPassingTimeList(jsonObject.getJSONObject("Data")
                .getJSONArray("StopPassingTimeList"));

        // L'heure actuelle
        Calendar cal = Calendar.getInstance();
        int now = 60 * cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE);

        // On refait toute l'ArrayList pour rajouter les temps d'attente
        for (int i = 0; i < TempStopPassingTimeList.size(); i++)
            if (TempStopPassingTimeList.get(i).getPassingTime()>=now)
                StopPassingTimeList.add(TempStopPassingTimeList.get(i));
    }
}
