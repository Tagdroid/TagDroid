package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Network;

public class NetworkDAO {
    public static final String TABLE_NAME = "Network",
            ID = "Id",
            NAME = "Name";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            NAME + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase bdd;

    public NetworkDAO(SQLiteDatabase bdd, boolean isCreating,
                       boolean isUpdating, int oldVersion, int newVersion) {
        this.bdd = bdd;
        if (isCreating){
            // On créé la table
            bdd.execSQL(TABLE_CREATE);
        }
        else if (isUpdating) {
            bdd.execSQL(TABLE_DROP);
            bdd.execSQL(TABLE_CREATE);
        }
    }

    private ContentValues createValues(Network m) {
        ContentValues values = new ContentValues();
        values.put(ID, m.getId());
        values.put(NAME, m.getName());
        return values;
    }

    public long add(Network m) {
        if (existsNetworkOfId(m.getId()))
            return 0;
        else
            return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    public int modify(Network m) {
        return bdd.update(TABLE_NAME, createValues(m), ID + " = " + m.getId(), null);
    }

    public int delete(long id) {
        return bdd.delete(TABLE_NAME, ID + " = " + id, null);
    }

    public Network select(long id) {
        Cursor c = bdd.query(TABLE_NAME, new String[]{ ID, NAME},
                ID + " = \"" + id + "\"", null, null, null, null);
        c.moveToFirst();
        Network network = new Network(c.getLong(0), c.getString(4));
        c.close();
        return network;
    }

    public Boolean existsNetworkOfId(Long id){
        Cursor c = bdd.query(TABLE_NAME, new String[]{ID, NAME},
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
