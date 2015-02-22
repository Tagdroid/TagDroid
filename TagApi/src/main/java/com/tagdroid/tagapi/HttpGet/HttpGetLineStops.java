package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.JSonApi.Transport.Locality;
import com.tagdroid.tagapi.JSonApi.Transport.LogicalStop;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Transport.LineStopsDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LocalityDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LogicalStopDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpGetLineStops extends HttpGetTask {
    private long lineId;
    private int direction;
    public HttpGetLineStops(long lineId, int direction, HttpGetInterface httpGetInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLineStops/json?key=TAGDEV"
                        +"&LineId=" + lineId + "&Direction=" + direction,
                httpGetInterface, context);
        this.lineId = lineId;
        this.direction = direction;
    }

    @Override
    public void readData(JSONObject jsonObject) throws JSONException {
        JSONArray jsonData = jsonObject.getJSONArray("Data");
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        LineStopsDAO lineStopsDAO = (LineStopsDAO)new LineStopsDAO(daTAGase).create();
        LogicalStopDAO logicalStopDAO = (LogicalStopDAO)new LogicalStopDAO(daTAGase).create();
        LocalityDAO localityDAO = (LocalityDAO)new LocalityDAO(daTAGase).create();

        LineStop lineStop;
        LogicalStop logicalStop;
        Locality locality;

        int length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                lineStop = new LineStop(jsonData.getJSONObject(i), lineId, direction, i);
                lineStopsDAO.update(lineStop);
                logicalStop = lineStop.getLogicalStop();
                logicalStopDAO.update(logicalStop);
                locality = lineStop.getLocality();
                localityDAO.update(locality);
                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage de stops", i + " / " + length);
                e.printStackTrace();
            }

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
    }
}
