package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Line;

import org.json.JSONException;

public class LinesDAO {
    public static final String TABLE_NAME = "Lines",
            ACCESSIBILITY = "Accessibility",
            COMPANY = "Company",
            COMPANYID = "CompanyID",
            DELETED = "Deleted",
            ID = "Id",
            NAME = "Name",
            NETWORKID = "NetworkID",
            NETWORKNAME = "NetworkName",
            NUMBER = "Number",
            OPERATORID = "OperatorID",
            ORDER = "Order",
            PUBLISHED = "Published",
            TRANSPORTMODE = "TransportMode";


    public static final String TABLE_CREATE = "CREATE TABLE  " + TABLE_NAME + " (" +
            ACCESSIBILITY + " INTEGER, " +
            COMPANY + " INTEGER, " +
            COMPANYID + " INTEGER, " +
            DELETED + " INTEGER, " +
            ID + " INTEGER PRIMARY KEY, " +
            NAME + " INTEGER, " +
            NETWORKID + " INTEGER, " +
            NETWORKNAME + " INTEGER, " +
            NUMBER + " INTEGER, " +
            OPERATORID + " INTEGER, " +
            ORDER + " INTEGER, " +
            PUBLISHED + " INTEGER, " +
            TRANSPORTMODE + " INTEGER);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase bdd;

    public LinesDAO(MySQLiteHelper dbHelper, boolean isCreating,
                    boolean isUpdating, int oldVersion, int newVersion) {
        this.bdd = dbHelper.getWritableDatabase();
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

    private ContentValues createValues(Line m) {
        ContentValues values = new ContentValues();

        values.put(ACCESSIBILITY, m.getAccessibility());
        values.put(COMPANY, m.getCompany());
        values.put(COMPANYID, m.getCompanyId());
        values.put(DELETED, m.getDeleted());
        values.put(ID, m.getId());
        values.put(NAME, m.getName());
        values.put(NETWORKID, m.getNetworkId());
        values.put(NETWORKNAME, m.getNetworkName());
        values.put(NUMBER, m.getNumber());
        values.put(OPERATORID, m.getOperatorId());
        values.put(ORDER, m.getOrder());
        values.put(PUBLISHED, m.getPublished());
        values.put(TRANSPORTMODE, m.getTransportMode());
        return values;
    }

    public long add(Line m) {
        if (existsLineOfId(m.getId()))
            return 0;
        else
            return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    public int modify(Line m) {
        return bdd.update(TABLE_NAME, createValues(m), ID + " = " + m.getId(), null);
    }

    public int delete(long id) {
        return bdd.delete(TABLE_NAME, ID + " = " + id, null);
    }

    public Line select(long id) throws JSONException {
        Cursor c = bdd.query(TABLE_NAME, new String[]{
                ACCESSIBILITY,
                COMPANY,
                COMPANYID,
                DELETED,
                ID,
                NAME,
                NETWORKID,
                NETWORKNAME,
                NUMBER,
                OPERATORID,
                ORDER,
                PUBLISHED,
                TRANSPORTMODE}, ID + " = \"" + id + "\"", null, null, null, null);
        if (!c.moveToFirst())
            return null;

        Line line = new Line(c.getInt(0),
                c.getString(1),
                c.getLong(2),
                c.getInt(3),
                c.getLong(4),
                c.getString(5),
                c.getLong(6),
                c.getString(7),
                c.getString(8),
                c.getLong(9),
                c.getInt(10),
                c.getInt(12),
                c.getInt(13));
        c.close();
        return line;
    }

    public Boolean existsLineOfId(Long id){
        Cursor c = bdd.query(TABLE_NAME, new String[]{ACCESSIBILITY, COMPANY,COMPANYID, DELETED,
                ID, NAME, NETWORKID, NETWORKNAME, NUMBER, OPERATORID, ORDER, PUBLISHED, TRANSPORTMODE},
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
