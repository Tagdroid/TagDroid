package com.tagdroid.tagapi.ReadJSon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.Locality;
import com.tagdroid.tagapi.JSonApi.Transport.LogicalStop;
import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadJSonTask;
import com.tagdroid.tagapi.SQLApi.Transport.LocalityDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LogicalStopDAO;
import com.tagdroid.tagapi.SQLApi.Transport.MySQLiteHelper;
import com.tagdroid.tagapi.SQLApi.Transport.PhysicalStopDAO;

import org.json.JSONArray;
import org.json.JSONException;

public class ReadJSonStops extends ReadJSonTask {
    private Context context;
    public ReadJSonStops(String jsonString, ProgressionInterface progressionInterface, Context context) {
        super(jsonString, progressionInterface);
        this.context = context;
    }
    @Override
    public void readData(JSONArray jsonData) {
        readDataNow(jsonData);
    }

    private void readDataNow(JSONArray jsonData) {
        MySQLiteHelper dbHelper = new MySQLiteHelper("TagDatabase.db",context, null);
        SQLiteDatabase bdd = dbHelper.getWritableDatabase();
        bdd.beginTransaction();

        PhysicalStopDAO physicalStopDAO = new PhysicalStopDAO(dbHelper, dbHelper.isCreating,
                dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);
        LogicalStopDAO logicalStopDAO = new LogicalStopDAO(bdd, dbHelper.isCreating,
                dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);
        LocalityDAO localityDAO = new LocalityDAO(bdd, dbHelper.isCreating,
                dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);

        PhysicalStop physicalStop;
        LogicalStop logicalStop;
        Locality locality;
        Line line;

        Integer length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                physicalStop = new PhysicalStop(jsonData.getJSONObject(i));
                physicalStopDAO.add(physicalStop);
                logicalStop = physicalStop.getLogicalStop();
                logicalStopDAO.add(logicalStop);
                locality = physicalStop.getLocality();
                localityDAO.add(locality);
                locality = logicalStop.getLocality();
                localityDAO.add(locality);
                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage de station", i + " / " + length);
                // e.printStackTrace();
            }

        bdd.setTransactionSuccessful();
        bdd.endTransaction();
        dbHelper.close();
    }

}