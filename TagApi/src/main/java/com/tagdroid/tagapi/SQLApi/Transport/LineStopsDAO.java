package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.SQLApi.DAO;

public class LineStopsDAO extends DAO<LineStop> {
    public static final String ID = "Id", NAME = "Name", POSITION = "Position", LOGICALSTOPID = "LogicalStopId",
            LOCALITYID = "LocalityId", LATITUDE = "Latitude", LONGITUDE = "Longitude",
            LINE_ID = "LineId", DIRECTION = "Direction";

    public LineStopsDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected String TABLE_NAME() {
        return "LineStops";
    }

    @Override
    protected String COLUMNS() {
        return "(" + ID     + " INTEGER PRIMARY KEY, " +
                NAME        + " TEXT, " +
                POSITION    + " INTEGER, " +
                LOGICALSTOPID+" INTEGER, " +
                LOCALITYID  + " INTEGER, " +
                LATITUDE    + " REAL, " +
                LONGITUDE   + " REAL, " +
                LINE_ID     + " INTEGER, " +
                DIRECTION   + " INTEGER);";
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{ID, NAME, POSITION, LOGICALSTOPID, LOCALITYID, LATITUDE, LONGITUDE, LINE_ID, DIRECTION,};
    }

    @Override
    protected ContentValues createValues(LineStop m) {
        ContentValues values = new ContentValues();
        values.put(ID, m.getId());
        values.put(NAME, m.getName());
        values.put(POSITION, m.getPosition());
        values.put(LOGICALSTOPID, m.getLogicalStopId());
        values.put(LOCALITYID, m.getLocalityId());
        values.put(LATITUDE, m.getLatitude());
        values.put(LONGITUDE, m.getLongitude());
        values.put(LINE_ID, m.getLineId());
        values.put(DIRECTION, m.getDirection());
        return values;
    }

    @Override
    protected LineStop fromCursor(Cursor cursor) {
        return new LineStop(cursor.getLong(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getLong(2),
                cursor.getInt(3),
                cursor.getDouble(4),
                cursor.getDouble(5),
                cursor.getInt(6),
                cursor.getInt(7));
    }

    public LineStop select(long id) {
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),
                ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        if (!cursor.moveToFirst())
            return null;
        return fromCursor(cursor);
    }
}
