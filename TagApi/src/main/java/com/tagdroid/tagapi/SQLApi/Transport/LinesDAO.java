package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.SQLApi.DAO;

import org.json.JSONException;

import java.util.ArrayList;

public class LinesDAO extends DAO<Line>{
    @Override
    protected String TABLE_NAME() {
        return "Lines";
    }
    public static final String ID = "Id", NUMBER = "Number", NAME = "Name", IS_ACTIVE = "IsActive";
    @Override
    protected String COLUMNS() {
        return "(" + ID     +" INTEGER PRIMARY KEY, "
                + NUMBER    +" TEXT, "
                + NAME      +" TEXT, "
                + IS_ACTIVE +" INTEGER);";
    }
    public static final String[] AllColumns = new String[]{ID, NUMBER, NAME, IS_ACTIVE};

    public LinesDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected ContentValues createValues(Line m) {
        ContentValues values = new ContentValues();
        values.put(ID, m.getId());
        values.put(NUMBER, m.getNumber());
        values.put(NAME, m.getName());
        values.put(IS_ACTIVE, m.getIsActive());
        return values;
    }

    @Override
    protected Line fromCursor(Cursor c) {
        return new Line(c.getLong(0),
                c.getString(1),
                c.getString(2),
                c.getInt(3) != 0);
    }

    public ArrayList<Line> getAllLines() {
        ArrayList<Line> allLines = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns, null, null, null, null, null);
        while (cursor.moveToNext())
            allLines.add(fromCursor(cursor));

        cursor.close();
        return allLines;
    }

    private Cursor getLineCursor(long id) {
        return bdd.query(TABLE_NAME(), AllColumns, ID + " = \"" + id + "\"", null, null, null, null);
    }

    public Line select(long id) throws JSONException {
        Cursor c = getLineCursor(id);
        if (!c.moveToFirst())
            return null;
        return fromCursor(c);
    }
}
