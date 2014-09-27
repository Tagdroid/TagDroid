package com.tagdroid.tagapi.SQLApi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final int VERSION_BDD = 4;
    public static final String databaseName = "TagDatabase.db";

    public boolean isUpgrading = false,
            isCreating = false;
    public Integer oldVersion = -1,
            newVersion = -1;


    public MySQLiteHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, databaseName, factory, VERSION_BDD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.isCreating = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.isUpgrading = true;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }
}
