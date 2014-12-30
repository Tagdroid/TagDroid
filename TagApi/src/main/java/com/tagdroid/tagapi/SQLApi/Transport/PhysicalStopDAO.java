package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;
import com.tagdroid.tagapi.SQLApi.DAO;

public class PhysicalStopDAO extends DAO<PhysicalStop> {
    public static final String CATEGORY = "CATEGORY",
            DIRECTION = "Direction",
            ID = "Id",
            NAME = "Name",
            LATITUDE = "Latitude",
            LINE_ID = "LineId",
            LONGITUDE = "Longitude",
            POINTTYPE = "PointType",
            LOGICALSTOPID = "LogicalStopId",
            LOCALITYID = "LocalityId",
            OPERATORID = "OperatorId",
            ACCESSIBILITY = "Accessibility";
    public static final String[] AllColumns = new String[]{CATEGORY, DIRECTION, ID, NAME,
            LATITUDE, LINE_ID, LONGITUDE, POINTTYPE, LOGICALSTOPID, LOCALITYID, OPERATORID, ACCESSIBILITY};

    public PhysicalStopDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected String TABLE_NAME() {
        return "PhysicalStops";
    }

    @Override
    protected String COLUMNS() {
        return "(" + CATEGORY + " INTEGER, " +
                DIRECTION + " INTEGER, " +
                ID + " INTEGER PRIMARY KEY, " +
                NAME + " INTEGER, " +
                LATITUDE + " REAL, " +
                LINE_ID + " INTEGER, " +
                LONGITUDE + " REAL, " +
                POINTTYPE + " INTEGER, " +
                LOGICALSTOPID + " INTEGER, " +
                LOCALITYID + " INTEGER, " +
                OPERATORID + " INTEGER, " +
                ACCESSIBILITY + " INTEGER);";
    }

    @Override
    protected ContentValues createValues(PhysicalStop m) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY, m.getCategory());
        values.put(DIRECTION, m.getDirection());
        values.put(ID, m.getId());
        values.put(NAME, m.getName());
        values.put(LATITUDE, m.getLatitude());
        values.put(LINE_ID, m.getLineId());
        values.put(LONGITUDE, m.getLongitude());
        values.put(POINTTYPE, m.getPointType());
        values.put(LOGICALSTOPID, m.getLogicalStopId());
        values.put(LOCALITYID, m.getLocalityId());
        values.put(OPERATORID, m.getOperatorId());
        values.put(ACCESSIBILITY, m.getAccessibility());
        return values;
    }

    @Override
    protected PhysicalStop fromCursor(Cursor cursor) {
        return new PhysicalStop(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getLong(2),
                cursor.getString(3),
                cursor.getDouble(4),
                cursor.getInt(5),
                cursor.getDouble(6),
                cursor.getInt(7),
                cursor.getInt(8),
                cursor.getInt(9),
                cursor.getInt(10),
                0);
    }

    public PhysicalStop select(long id) {
        Cursor c = bdd.query(TABLE_NAME(), AllColumns, ID + " = \"" + id + "\"", null, null, null, null);
        if (!c.moveToFirst())
            return null;
        return fromCursor(c);
    }
}
