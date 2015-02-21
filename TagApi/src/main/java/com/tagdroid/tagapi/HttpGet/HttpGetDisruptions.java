package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Disruption.Disruption;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Disruption.DisruptionDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpGetDisruptions extends HttpGetTask {
    public HttpGetDisruptions(ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/disruption/v1/GetActualAndFutureDisruptions/json?key=TAGDEV",
                progressionInterface, context);
    }


    @Override
    public void readData(JSONObject jsonObject) throws JSONException {
        JSONArray jsonData = jsonObject.getJSONArray("Data");
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        DisruptionDAO disruptionDAO = (DisruptionDAO)new DisruptionDAO(daTAGase).create();
        Disruption disruption;

        int length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                disruption = new Disruption(jsonData.getJSONObject(i));
                disruptionDAO.update(disruption);

                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage des disruptions", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
    }
}
