package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.PhysicalStop;

public class PhysicalStopDAO {
    public static final String TABLE_NAME = "LineStops",
            CATEGORY = "CATEGORY",
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

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            CATEGORY + " INTEGER, " +
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
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static SQLiteDatabase bdd;

    public PhysicalStopDAO(SQLiteDatabase bdd, boolean isCreating,
                           boolean isUpdating, int oldVersion, int newVersion) {
        PhysicalStopDAO.bdd = bdd;
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

    private ContentValues createValues(PhysicalStop m) {
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

    public long add(PhysicalStop m) {
        if (existsPhysicalStopOfId(m.getId())) {
            Log.d("pas d'enregistrement sql", m.getName() + " " + m.getId());
            return 0;
        }
        else {
            Log.d("      enregistrement sql", m.getName() + " " + m.getId());
            return bdd.insert(TABLE_NAME, null, createValues(m));
        }
    }

    public int modify(PhysicalStop m) {
        return bdd.update(TABLE_NAME, createValues(m), ID + " = " + m.getId(), null);
    }

    public int delete(long id) {
        return bdd.delete(TABLE_NAME, ID + " = " + id, null);
    }

    public PhysicalStop select(long id) {
        Cursor c = bdd.query(TABLE_NAME, new String[]{
                CATEGORY,
                DIRECTION,
                ID,
                NAME,
                LATITUDE,
                LINE_ID,
                LONGITUDE,
                POINTTYPE,
                LOGICALSTOPID,
                LOCALITYID,
                OPERATORID,
                ACCESSIBILITY}, ID + " = \"" + id + "\"", null, null, null, null);
        if (!c.moveToFirst())
            return null;

        PhysicalStop physicalStop = new PhysicalStop(c.getInt(0),
                c.getInt(1),
                c.getLong(2),
                c.getString(3),
                c.getDouble(4),
                c.getInt(5),
                c.getDouble(6),
                c.getInt(7),
                c.getInt(8),
                c.getInt(9),
                c.getInt(10),
                0);
        c.close();
        return physicalStop;
    }

    public Boolean existsPhysicalStopOfId(Long id){
        Cursor c = bdd.query(TABLE_NAME, new String[]{CATEGORY, DIRECTION, ID, NAME, LATITUDE, LINE_ID,
                LONGITUDE, POINTTYPE, LOGICALSTOPID, LOCALITYID, OPERATORID, ACCESSIBILITY},
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
