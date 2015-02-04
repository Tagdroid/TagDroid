package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.SQLApi.DAO;

import java.util.ArrayList;

public class LineStopsDAO extends DAO<LineStop> {
    @Override
    protected String ID() {
        return "Id";
    }
    public static final String NAME = "Name", POSITION = "Position", LOGICALID = "LogicalStopId",
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
        return "(" + ID()   + " INTEGER, " +
                NAME        + " TEXT, " +
                POSITION    + " INTEGER, " +
                LOGICALID   + " INTEGER, " +
                LOCALITYID  + " INTEGER, " +
                LATITUDE    + " REAL, " +
                LONGITUDE   + " REAL, " +
                LINE_ID     + " INTEGER, " +
                DIRECTION   + " INTEGER);";
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{ID(), NAME, POSITION, LOGICALID, LOCALITYID, LATITUDE, LONGITUDE, LINE_ID, DIRECTION,};
    }

    @Override
    protected ContentValues createValues(LineStop m) {
        ContentValues values = new ContentValues();
        values.put(ID(), m.getId());
        values.put(NAME, m.getName());
        values.put(POSITION, m.getPosition());
        values.put(LOGICALID, m.getLogicalStopId());
        values.put(LOCALITYID, m.getLocalityId());
        values.put(LATITUDE, m.getLatitude());
        values.put(LONGITUDE, m.getLongitude());
        values.put(LINE_ID, m.getLineId());
        values.put(DIRECTION, m.getDirection());
        return values;
    }

    @Override
    protected LineStop fromCursor(Cursor cursor) {
        int i=0;
        return new LineStop(cursor.getLong(i++),
                cursor.getString(i++),
                cursor.getInt(i++),
                cursor.getLong(i++),
                cursor.getInt(i++),
                cursor.getDouble(i++),
                cursor.getDouble(i++),
                cursor.getInt(i++),
                cursor.getInt(i));
    }

    public ArrayList<LineStop> stopsFromLineAndDirection(long lineId, int directionId) {
        ArrayList<LineStop> stopsFromLineAndDirection = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),
                LINE_ID + " = ? AND " + DIRECTION + " = ?",
                new String[]{String.valueOf(lineId),String.valueOf(directionId)},
                null, null, null);
        while (cursor.moveToNext())
            stopsFromLineAndDirection.add(fromCursor(cursor));
        cursor.close();
        return stopsFromLineAndDirection;
    }

    public ArrayList<LineStop> stopsFromLineAndLogicalAndDirection(long lineId, long logicalStopId, int direction){
        ArrayList<LineStop> stopsFromLineAndLogical = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),
                LINE_ID + " = ? AND " + LOGICALID + " = ? AND " + DIRECTION + " = ?",
                new String[]{String.valueOf(lineId),String.valueOf(logicalStopId),String.valueOf(direction)},
                null,null,null);
        while(cursor.moveToNext())
            stopsFromLineAndLogical.add(fromCursor(cursor));
        cursor.close();

        return stopsFromLineAndLogical;
    }
}
