package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.TimeTable.Time;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.TimeTable.Timetable1DAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpGetNextStopTimes extends HttpGetTask {
    public HttpGetNextStopTimes(long stopId, ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/TimeTables/v1/GetNextStopHours/json?key=TAGDEV"
                        + "&StopId=" + stopId
                        + "&Date=" + formatedDate(), //Pas besoin du Time vu que c'est les derniers horaires de la journée
                progressionInterface, context);

        Log.d("URL STOP TIME","http://transinfoservice.ws.cityway.fr/TAG/api/TimeTables/v1/GetNextStopHours/json?key=TAGDEV"
                + "&StopId=" + stopId
                + "&Date=" + formatedDate());
    }

    public static String formatedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date(System.currentTimeMillis()));

        Log.d("date", date);
        return date;
    }

    @Override
    public void readData(JSONObject jsonObject) throws JSONException {
        JSONArray jsonData = jsonObject.getJSONArray("Data");
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        Timetable1DAO timetable1DAO = (Timetable1DAO)new Timetable1DAO(daTAGase).create();

        Time time;

        int length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                time = new Time(jsonData.getJSONObject(i));
                timetable1DAO.update(time);

                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage de stops", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();

    }
}
