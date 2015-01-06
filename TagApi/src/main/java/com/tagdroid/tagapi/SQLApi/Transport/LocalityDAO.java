package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Locality;
import com.tagdroid.tagapi.SQLApi.DAO;

public class LocalityDAO extends DAO<Locality> {
    @Override
    protected String ID() {
        return "Direction";
    }
    public static final String INSEECODE = "InseeCode", LATITUDE = "Latitude",
            LONGITUDE = "Longitude", NAME = "Name";

    public LocalityDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected String TABLE_NAME() {
        return "Locality";
    }

    @Override
    protected String COLUMNS() {
        return "(" +
                ID() + " INTEGER PRIMARY KEY, " +
                INSEECODE + " INTEGER, " +
                LATITUDE + " INTEGER, " +
                LONGITUDE + " INTEGER, " +
                NAME + " INTEGER);";
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{ID(), INSEECODE, LATITUDE, LONGITUDE, NAME};
    }

    @Override
    protected ContentValues createValues(Locality m) {
        ContentValues values = new ContentValues();
        values.put(ID(), m.getId());
        values.put(INSEECODE, m.getInseeCode());
        values.put(LATITUDE, m.getLatitude());
        values.put(LONGITUDE, m.getLongitude());
        values.put(NAME, m.getName());
        return values;
    }

    @Override
    protected Locality fromCursor(Cursor cursor) {
        return new Locality(cursor.getLong(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getString(4));
    }

    public long update(Locality m) {
        return bdd.insertWithOnConflict(TABLE_NAME(), null, createValues(m),SQLiteDatabase.CONFLICT_IGNORE);
    }
}
