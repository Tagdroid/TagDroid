package com.tagdroid.tagapi.SQLApi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public abstract class DAO<T> {
    protected SQLiteDatabase bdd;
    protected abstract String TABLE_NAME();
    protected String TABLE_DROP() {
        return "DROP TABLE IF EXISTS "+TABLE_NAME();
    }
    protected abstract String COLUMNS();
    protected abstract String[] AllColumns();
    protected abstract String ID();

    public DAO(SQLiteDatabase bdd) {
        this.bdd = bdd;
    }
    public DAO<T> create() {
        bdd.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME() + " " + COLUMNS());
        return this;
    }
    public DAO<T> update(int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            bdd.execSQL(TABLE_DROP());
            bdd.execSQL("CREATE TABLE " + TABLE_NAME() + " " + COLUMNS());
        }
        return this;
    }

    public long add(T m) {
        return bdd.insert(TABLE_NAME(), null, createValues(m));
    }

    public long update(T m) {
        return bdd.insertWithOnConflict(TABLE_NAME(), null, createValues(m), SQLiteDatabase.CONFLICT_IGNORE);
    }
    protected abstract ContentValues createValues(T m);
    protected abstract T fromCursor(Cursor cursor);

    public ArrayList<T> selectAll() {
        ArrayList<T> all = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(), null, null, null, null, null);
        while (cursor.moveToNext())
            all.add(fromCursor(cursor));
        cursor.close();
        return all;
    }
}
