package com.tagdroid.tagapi.SQLApi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.LogicalStop;

public class LogicalStopDAO {
    public static final String TABLE_NAME = "LogicalStops",
            ID = "Id",
            LOCALITYID = "LocalityId",
            NAME = "Name",
            POINTTYPE = "PointType";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            NAME + " INTEGER, " +
            POINTTYPE + " INTEGER, " +
            LOCALITYID + " INTEGER);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase bdd;

    public LogicalStopDAO(SQLiteDatabase bdd, boolean isCreating,
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


    private ContentValues createValues(LogicalStop m) {
        ContentValues values = new ContentValues();
        values.put(ID, m.getId());
        values.put(LOCALITYID, m.getLocalityId());
        values.put(NAME, m.getName());
        values.put(POINTTYPE, m.getPointType());
        return values;
    }

    public long add(LogicalStop m) {
        if (existsPhysicalStopOfId(m.getId()))
            return 0;
        else
            return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    public int modify(LogicalStop m) {
        return bdd.update(TABLE_NAME, createValues(m), ID + " = " + m.getId(), null);
    }

    public int delete(long id) {
        return bdd.delete(TABLE_NAME, ID + " = " + id, null);
    }

    public LogicalStop select(long id) {
        Cursor c = bdd.query(TABLE_NAME, new String[]{ ID, LOCALITYID, NAME, POINTTYPE},
                ID + " = \"" + id + "\"", null, null, null, null);
        c.moveToFirst();
        LogicalStop physicalStop = new LogicalStop(c.getLong(0), c.getInt(1),
                c.getString(2), c.getInt(3));
        c.close();
        return physicalStop;
    }

    public Boolean existsPhysicalStopOfId(Long id){
        Cursor c = bdd.query(TABLE_NAME, new String[]{ID, LOCALITYID, NAME, POINTTYPE},
                ID + " = \"" + id +"\"", null, null, null, null);
        if(c.getCount()>0){
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

}
