package com.tagdroid.tagapi.SQLApi.Transport;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.LineStop;

import org.json.JSONException;

public class LineStopsDAO {
    public static final String TABLE_NAME = "LineStops",
            ID = "Id",
            NAME = "Name",
            LOGICALSTOPID = "LogicalStopId",
            LOCALITYID = "LocalityId",
            LATITUDE = "Latitude",
            LONGITUDE = "Longitude",
            LINE_ID = "LineId",
            DIRECTION = "Direction";
    private String[] AllColumns = new String[]{ID, NAME, LOGICALSTOPID, LOCALITYID,
            LATITUDE, LONGITUDE, LINE_ID, DIRECTION,};

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS   " + TABLE_NAME + " (" +
            ID          + " INTEGER PRIMARY KEY, " +
            NAME        + " TEXT, " +
            LOGICALSTOPID + " INTEGER, " +
            LOCALITYID  + " INTEGER, " +
            LATITUDE    + " REAL, " +
            LONGITUDE   + " REAL, " +
            LINE_ID     + " INTEGER, " +
            DIRECTION   + " INTEGER);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static SQLiteDatabase bdd;

    public LineStopsDAO(SQLiteDatabase bdd, boolean isCreating,
                           boolean isUpdating, int oldVersion, int newVersion) {
        LineStopsDAO.bdd = bdd;
        if (isCreating) {
            // On créé la table
            Log.d("SQLiteHelper", "Base is being created");
            bdd.execSQL(TABLE_CREATE);
        }
        else if (isUpdating) {
            Log.d("SQLiteHelper", "Base is being updated");
            bdd.execSQL(TABLE_DROP);
            bdd.execSQL(TABLE_CREATE);
        }
    }

    private ContentValues createValues(LineStop m) {
        ContentValues values = new ContentValues();
        values.put(ID, m.getId());
        values.put(NAME, m.getName());
        values.put(LOGICALSTOPID, m.getLogicalStopId());
        values.put(LOCALITYID, m.getLocalityId());
        values.put(LATITUDE, m.getLatitude());
        values.put(LONGITUDE, m.getLongitude());
        values.put(LINE_ID, m.getLineId());
        values.put(DIRECTION, m.getDirection());
        return values;
    }

    public long add(LineStop m) {
        if (existsLineOfId(m.getId()))
            return 0;
        else
            return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    private Cursor getLineCursor(long id) {
        return bdd.query(TABLE_NAME, AllColumns, ID + " = \"" + id + "\"", null, null, null, null);
    }
    public Boolean existsLineOfId(long id) {
        Cursor c = getLineCursor(id);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public LineStop select(long id) throws JSONException {
        Cursor c = getLineCursor(id);
        if (!c.moveToFirst())
            return null;

        LineStop line = new LineStop(c.getLong(0),
                c.getString(1),
                c.getLong(2),
                c.getInt(3),
                c.getDouble(4),
                c.getDouble(5),
                c.getInt(6),
                c.getInt(7));
        c.close();
        return line;
    }
}
