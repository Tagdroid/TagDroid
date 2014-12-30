package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.LogicalStop;
import com.tagdroid.tagapi.SQLApi.DAO;

public class LogicalStopDAO extends DAO<LogicalStop> {
    @Override
    protected String TABLE_NAME() {
        return "LogicalStops";
    }

    public static final String ID = "Id", LOCALITYID = "LocalityId", NAME = "Name", POINTTYPE = "PointType";
    @Override protected String[] AllColumns() {
        return new String[]{ID, LOCALITYID, NAME, POINTTYPE};
    }
    public LogicalStopDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected String COLUMNS() {
        return "(" +
                ID          + " INTEGER PRIMARY KEY, " +
                NAME        + " INTEGER, " +
                POINTTYPE   + " INTEGER, " +
                LOCALITYID  + " INTEGER);";
    }

    @Override
    protected ContentValues createValues(LogicalStop m) {
        ContentValues values = new ContentValues();
        values.put(ID, m.getId());
        values.put(LOCALITYID, m.getLocalityId());
        values.put(NAME, m.getName());
        values.put(POINTTYPE, m.getPointType());
        return values;
    }

    @Override
    protected LogicalStop fromCursor(Cursor cursor) {
        return new LogicalStop(cursor.getLong(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3));
    }

    public long update(LogicalStop m) {
        return bdd.insertWithOnConflict(TABLE_NAME(), null, createValues(m), SQLiteDatabase.CONFLICT_IGNORE);
    }

    public LogicalStop select(long id) {
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),
                ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        return fromCursor(cursor);
    }

    public Boolean existsPhysicalStopOfId(Long id) {
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),
                ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        return cursor.moveToFirst();
    }

}
