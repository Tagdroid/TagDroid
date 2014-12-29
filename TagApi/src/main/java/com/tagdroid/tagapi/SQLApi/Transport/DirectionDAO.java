package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Direction;

import org.json.JSONException;

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
            Log.d("SQLiteHelper", "Base is being created");
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
        if (existsLineOfId(m.getLineId()))
            return 0;
        else
            return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    public ArrayList<Direction> getAllLines() {
        ArrayList<Direction> allLines = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME, AllColumns, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (!cursor.isLast()) {
                allLines.add(lineFromCursor(cursor));
                cursor.moveToNext();
            }
        cursor.close();
        return allLines;
    }

    private Cursor getLineCursor(long id) {
        return bdd.query(TABLE_NAME, AllColumns, DIRECTION + " = \"" + id + "\"", null, null, null, null);
    }

    public Boolean existsLineOfId(long id) {
        Cursor c = getLineCursor(id);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public Direction lineFromCursor(Cursor c) {
        return new Direction(c.getInt(0),
                c.getString(1),
                c.getLong(2));
    }

    public Direction select(long id) throws JSONException {
        Cursor c = getLineCursor(id);
        if (!c.moveToFirst())
            return null;
        return lineFromCursor(c);
    }
}
