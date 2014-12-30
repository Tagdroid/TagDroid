package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Transport.PhysicalStopDAO;

import org.json.JSONArray;
import org.json.JSONException;

public class HttpGetLineStops extends HttpGetTask {
    private long lineId;
    private int direction;
    public HttpGetLineStops(long lineId, int direction, ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLineStops/json?key=TAGDEV"
                        +"&LineId=" + lineId + "&Direction=" + direction,
                progressionInterface, context);
        this.lineId = lineId;
        this.direction = direction;
    }

    @Override
    public void readData(JSONArray jsonData) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        PhysicalStopDAO physicalStopDAO = (PhysicalStopDAO)new PhysicalStopDAO(daTAGase).update(0,0);

        PhysicalStop physicalStop;

        int length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                physicalStop = new PhysicalStop(jsonData.getJSONObject(i), lineId, direction);
                physicalStopDAO.add(physicalStop);
                publishProgress(i, length);
                //Log.d("parsage de stops", i + " / " + length);
            } catch (JSONException e) {
                Log.e("parsage de stops", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
