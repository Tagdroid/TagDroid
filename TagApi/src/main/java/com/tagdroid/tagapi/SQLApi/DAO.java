package com.tagdroid.tagapi.SQLApi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class DAO<T> {
    protected SQLiteDatabase bdd;
    protected abstract String TABLE_NAME();
    protected String TABLE_DROP() {
        return "DROP TABLE IF EXISTS "+TABLE_NAME();
    }
    protected abstract String COLUMNS();

    public DAO(SQLiteDatabase bdd) {
        this.bdd = bdd;
    }
    public DAO<T> create() {
        Log.d("SQLiteHelper", "Table "+ TABLE_NAME() +" is being created");
        bdd.execSQL("CREATE TABLE " + TABLE_NAME() + " " + COLUMNS());
        return this;
    }
    public DAO<T> update(int oldVersion, int newVersion) {
        Log.d("SQLiteHelper", "Table "+ TABLE_NAME() +" is being updated from "+oldVersion+" to "+newVersion);
        bdd.execSQL(TABLE_DROP());
        bdd.execSQL("CREATE TABLE " + TABLE_NAME() + " " + COLUMNS());
        return this;
    }

    public long add(T m) {
        return bdd.insert(TABLE_NAME(), null, createValues(m));
    }

    protected abstract ContentValues createValues(T m);
    protected abstract T fromCursor(Cursor cursor);
}
