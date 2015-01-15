package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.TimeTable.Time;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.TimeTable.Timetable2DAO;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpGetNextStopTimes2 extends HttpGetTask {
    public HttpGetNextStopTimes2(long stopId, ProgressionInterface progressionInterface, Context context) {
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
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        Timetable2DAO timetable2DAO = (Timetable2DAO)new Timetable2DAO(daTAGase).create();

        Time time;

        int length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                time = new Time(jsonData.getJSONObject(i));
                timetable2DAO.update(time);

                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage de stops", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();

    }
}
