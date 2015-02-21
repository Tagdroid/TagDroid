package com.tagdroid.tagapi.HttpGet;

import android.annotation.SuppressLint;
import android.content.Context;

import com.tagdroid.tagapi.JSonApi.TimeTable.Time;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HttpGetStopHours extends HttpGetTask {
    private final boolean horairesPrincipaux;

    public HttpGetStopHours(long StopId, long LineId, long LineDirection, boolean horairesPrincipaux,
                            ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/TimeTables/v1/GetStopHours/json?key=TAGDEV"
                + "&StopId="        + StopId
                + "&Date="          + formatedTAGDate()
                + "&LineId="        + LineId
                + "&LineDirection=" + LineDirection,
                progressionInterface, context);
        this.horairesPrincipaux = horairesPrincipaux;
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
        JSONArray StopPassingTimeList = jsonObject.getJSONObject("Data").getJSONArray("StopPassingTimeList");

        // L'heure actuelle
        Calendar cal = Calendar.getInstance();
        int now = 60 * cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE);

        // On convertit le JSONArray en ArrayList contenant les temps d'attente (et non les heures de passage)
        ArrayList<Time> PassingTimes = new ArrayList<>();
        for (int i = 0; i < StopPassingTimeList.length(); i++) {
            int attente = StopPassingTimeList.getJSONObject(i).getInt("PassingTime") - now;
            if (attente > 0)
                PassingTimes.add(new Time(attente, 0, ""));
        }

        if (horairesPrincipaux)
            ReadSQL.setHorairesPrincipaux(PassingTimes);
        else
            ReadSQL.setHorairesSecondaires(PassingTimes);
    }
}
