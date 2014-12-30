package com.tagdroid.tagapi.SQLApi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "TagDatabase.db";
    private static final String DATABASE_TABLE = "table_name";
    private static final int DATABASE_VERSION = 4;

    public static DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new DatabaseHelper(context);
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    public boolean isUpgrading = false,
            isCreating = false;
    public int oldVersion = -1,
            newVersion = -1;

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
