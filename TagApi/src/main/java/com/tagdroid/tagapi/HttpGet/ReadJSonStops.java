package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Locality;
import com.tagdroid.tagapi.JSonApi.Transport.LogicalStop;
import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.SQLApi.Transport.LocalityDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LogicalStopDAO;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Transport.PhysicalStopDAO;

import org.json.JSONArray;
import org.json.JSONException;

/* N'est plus utilisé mais on conserve le code "au cas où" il sert */

public class ReadJSonStops {
    private Context context;
    public ReadJSonStops(String jsonString, ProgressionInterface progressionInterface, Context context) {
        this.context = context;
    }

    public void readData(JSONArray jsonData) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        PhysicalStopDAO physicalStopDAO = new PhysicalStopDAO(daTAGase);
        LogicalStopDAO logicalStopDAO = new LogicalStopDAO(daTAGase,
                dbHelper.isCreating, dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);
        LocalityDAO localityDAO = new LocalityDAO(daTAGase,
                dbHelper.isCreating, dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);

        PhysicalStop physicalStop;
        LogicalStop logicalStop;
        Locality locality;

        int length = jsonData.length();
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
            } catch (JSONException e) {
                Log.e("parsage de station", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
    }
}
