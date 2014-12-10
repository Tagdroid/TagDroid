package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Locality;

public class LocalityDAO {
    public static final String TABLE_NAME = "Locality",
            ID = "Id",
            INSEECODE = "InseeCode",
            LATITUDE = "Latitude",
            LONGITUDE = "Longitude",
            NAME = "Name";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            INSEECODE + " INTEGER, " +
            LATITUDE + " INTEGER, " +
            LONGITUDE + " INTEGER, " +
            NAME + " INTEGER);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase bdd;

    public LocalityDAO(SQLiteDatabase bdd, boolean isCreating,
                           boolean isUpdating, int oldVersion, int newVersion) {
        this.bdd = bdd;
        if (isCreating){
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

    private ContentValues createValues(Locality m) {
        ContentValues values = new ContentValues();
        values.put(ID, m.getId());
        values.put(INSEECODE, m.getInseeCode());
        values.put(LATITUDE, m.getLatitude());
        values.put(LONGITUDE, m.getLongitude());
        values.put(NAME, m.getName());
        return values;
    }

    public long add(Locality m) {
        if (existsPhysicalStopOfId(m.getId()))
            return 0;
        else
            return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    public int modify(Locality m) {
        return bdd.update(TABLE_NAME, createValues(m), ID + " = " + m.getId(), null);
    }

    public int delete(long id) {
        return bdd.delete(TABLE_NAME, ID + " = " + id, null);
    }

    public Locality select(long id) {
        Cursor c = bdd.query(TABLE_NAME, new String[]{
                ID,
                INSEECODE,
                LATITUDE,
                LONGITUDE,
                NAME}, ID + " = \"" + id + "\"", null, null, null, null);
        c.moveToFirst();
        Locality locality = new Locality(c.getLong(0),
                c.getInt(1),
                c.getInt(2),
                c.getInt(3),
                c.getString(4));
        c.close();
        return locality;
    }

    public Boolean existsPhysicalStopOfId(Long id){
        Cursor c = bdd.query(TABLE_NAME, new String[]{ID, INSEECODE, LATITUDE, LONGITUDE, NAME},
                ID + " = \"" + id +"\"", null, null, null, null);
        c.moveToFirst();
        if(c.getCount()>0){
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }
}
