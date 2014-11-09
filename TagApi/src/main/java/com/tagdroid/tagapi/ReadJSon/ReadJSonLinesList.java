package com.tagdroid.tagapi.ReadJSon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadJSonTask;
import com.tagdroid.tagapi.SQLApi.MySQLiteHelper;
import com.tagdroid.tagapi.SQLApi.Transport.LinesDAO;

import org.json.JSONArray;
import org.json.JSONException;

public class ReadJSonLinesList extends ReadJSonTask {
    private Context context;
    public ReadJSonLinesList(String jsonString, ProgressionInterface progressionInterface, Context context) {
        super(jsonString, progressionInterface);
        this.context = context;
    }

    @Override
    public void readData(JSONArray jsonData) {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        LinesDAO linesDAO = new LinesDAO(daTAGase,
                dbHelper.isCreating, dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);

        Line line;

        Integer length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                line = new Line(jsonData.getJSONObject(i));
                linesDAO.add(line);
                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage de ligne", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        dbHelper.close();

    }
}
