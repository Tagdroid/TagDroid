package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tagdroid.tagapi.JSonApi.Transport.Line;

import org.json.JSONException;

import java.util.ArrayList;

public class LinesDAO {
    public static final String TABLE_NAME = "Lines",
            ID = "Id", NUMBER = "Number", NAME = "Name", IS_ACTIVE = "IsActive";
    public static final String[] AllColumns = new String[]{ID, NUMBER, NAME, IS_ACTIVE};

    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME +" ("
            + ID        +" INTEGER PRIMARY KEY, "
            + NUMBER    +" TEXT, "
            + NAME      +" TEXT, "
            + IS_ACTIVE +" INTEGER);";

    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase bdd;

    public LinesDAO(SQLiteDatabase bdd, boolean isCreating,
                    boolean isUpdating, int oldVersion, int newVersion) {
        this.bdd = bdd;
        if (isCreating) {
            // On créé la table
            Log.d("SQLiteHelper", "Base is being created");
            //bdd.execSQL(TABLE_DROP);
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
        values.put(ID, m.getId());
        values.put(NUMBER, m.getNumber());
        values.put(NAME, m.getName());
        values.put(IS_ACTIVE, m.getIsActive());
        return values;
    }

    public long add(Line m) {
        if (existsLineOfId(m.getId()))
            return 0;
        else
            return bdd.insert(TABLE_NAME, null, createValues(m));
    }

    public ArrayList<Line> getAllLines() {
        ArrayList<Line> allLines = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME, AllColumns, null, null, null, null, null);
        if (cursor .moveToFirst())
            while (!cursor.isLast()) {
                allLines.add(lineFromCursor(cursor));
                cursor.moveToNext();
            }
        cursor.close();
        return allLines;
    }

    private Cursor getLineCursor(long id) {
        return bdd.query(TABLE_NAME, AllColumns, ID + " = \"" + id + "\"", null, null, null, null);
    }
    public Boolean existsLineOfId(long id) {
        Cursor c = getLineCursor(id);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public Line lineFromCursor(Cursor c) {
        return new Line(c.getLong(0),
                c.getString(1),
                c.getString(2),
                c.getInt(3) != 0);
    }

    public Line select(long id) throws JSONException {
        Cursor c = getLineCursor(id);
        if (!c.moveToFirst())
            return null;
        return lineFromCursor(c);
    }
}
