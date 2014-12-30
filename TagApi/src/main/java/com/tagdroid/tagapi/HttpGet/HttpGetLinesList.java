package com.tagdroid.tagapi.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Transport.DirectionDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LinesDAO;

import org.json.JSONArray;
import org.json.JSONException;

public class HttpGetLinesList extends HttpGetTask {
    public HttpGetLinesList(ProgressionInterface progressionInterface, Context context) {
        super("http://transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLines/json?key=TAGDEV&OperatorId=1",
                progressionInterface, context);
    }

    @Override
    public void readData(JSONArray jsonData) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        LinesDAO linesDAO = (LinesDAO)new LinesDAO(daTAGase).update(dbHelper.oldVersion, dbHelper.newVersion);
        DirectionDAO directionDAO = (DirectionDAO)new DirectionDAO(daTAGase).update(dbHelper.oldVersion, dbHelper.newVersion);

        Line line;

        int length = jsonData.length();
        for (int i = 0; i < length; i++)
            try {
                line = new Line(jsonData.getJSONObject(i));
                linesDAO.add(line);

                // Saving the directions
                for (Direction direction : line.getDirectionList()) {
                    directionDAO.add(direction);
                    //Log.d("parsage de lignes", i + " / " + length + ", direction " + direction.getName());
                }

                publishProgress(i, length);
            } catch (JSONException e) {
                Log.e("parsage de lignes", i + " / " + length);
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
