package com.tagdroid.tagapi.JSon2SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadJSonTask;
import com.tagdroid.tagapi.SQLApi.MySQLiteHelper;
import com.tagdroid.tagapi.SQLApi.Transport.PhysicalStopDAO;

import org.json.JSONArray;
import org.json.JSONException;

public class ReadJSonLineStops extends ReadJSonTask {
    private Context context;
    private Integer lineId, direction;
    public ReadJSonLineStops(String jsonString, Integer lineId, Integer direction,
                             ProgressionInterface progressionInterface, Context context) {
        super(jsonString, progressionInterface);
        this.context = context;
        this.lineId = lineId;
        this.direction = direction;
    }

    @Override
    public void readData(JSONArray jsonData) {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        PhysicalStopDAO physicalStopDAO = new PhysicalStopDAO(daTAGase,
                dbHelper.isCreating, dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);

        PhysicalStop physicalStop;

        Integer length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                physicalStop = new PhysicalStop(jsonData.getJSONObject(i), lineId, direction);
                physicalStopDAO.add(physicalStop);
                publishProgress(i, length);
                Log.d("parsage de ligne", i + " / " + length);
            } catch (JSONException e) {
                Log.e("parsage de ligne", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        dbHelper.close();

    }
}
