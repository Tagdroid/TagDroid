package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Direction;

import java.util.ArrayList;

public class DirectionDAO {
    public static final String TABLE_NAME = "Directions",
            DIRECTION = "Direction", NAME = "Name", LINE_ID = "LineId";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + DIRECTION + " INTEGER, "
            + NAME + " TEXT, "
            + LINE_ID + " INTEGER);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String[] AllColumns = new String[]{DIRECTION, NAME, LINE_ID};
    private SQLiteDatabase bdd;

    public DirectionDAO(SQLiteDatabase bdd, boolean isCreating,
                        boolean isUpdating, int oldVersion, int newVersion) {
        this.bdd = bdd;
        if (isCreating) {
            // On créé la table
            Log.d("SQLiteHelper", "Table Direction is being created");
            //bdd.execSQL(TABLE_DROP);
            bdd.execSQL(TABLE_CREATE);
        } else if (isUpdating) {
            Log.d("SQLiteHelper", "Base is being updated");
            bdd.execSQL(TABLE_DROP);
            bdd.execSQL(TABLE_CREATE);
        }
    }

    private ContentValues createValues(Direction m) {
        ContentValues values = new ContentValues();
        values.put(DIRECTION, m.getDirection());
        values.put(NAME, m.getName());
        values.put(LINE_ID, m.getLineId());
        return values;
    }

    public long add(Direction m) {
        return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    private Cursor getLineDirectionsCursor(long id) {
        return bdd.query(TABLE_NAME, AllColumns, LINE_ID + " = ? ", new String[]{"" + id}, null, null, null);
    }

    public ArrayList<Direction> getDirectionsOfLine(long id) {
        ArrayList<Direction> allLines = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME, AllColumns, LINE_ID + " = ? ", new String[]{"" + id}, null, null, null);
        if (cursor.moveToFirst())
            while (!cursor.isLast()) {
                allLines.add(directionFromCursor(cursor));
                cursor.moveToNext();
            }
        cursor.close();
        return allLines;

    }

    private Direction directionFromCursor(Cursor c) {
        return new Direction(c.getInt(0),
                c.getString(1),
                c.getLong(2));
    }
}
