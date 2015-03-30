package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.SQLApi.DAO;

import java.util.ArrayList;

public class LinesDAO extends DAO<Line> {
    @Override
    protected String ID() {
        return "Id";
    }
    public static final String NUMBER = "Number", NAME = "Name", IS_ACTIVE = "IsActive",
            TYPE = "TYPE", COLOR = "COLOR";


    public LinesDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected String TABLE_NAME() {
        return "Lines";
    }

    @Override
    protected String COLUMNS() {
        return "(" + ID()   + " INTEGER PRIMARY KEY, "
                + NUMBER    + " TEXT, "
                + NAME      + " TEXT, "
                + TYPE      + " INTEGER, "
                + COLOR     + " INTEGER, "
                + IS_ACTIVE + " INTEGER);";
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{ID(), NUMBER, NAME, IS_ACTIVE, TYPE, COLOR};
    }


    @Override
    protected ContentValues createValues(Line m) {
        ContentValues values = new ContentValues();
        values.put(ID(), m.getId());
        values.put(NUMBER, m.getNumber());
        values.put(NAME, m.getName());
        values.put(IS_ACTIVE, m.getIsActive());
        values.put(TYPE, m.getLineType());
        values.put(COLOR, m.getColor());
        return values;
    }

    @Override
    protected Line fromCursor(Cursor cursor) {
        return new Line(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3) != 0,
                cursor.getInt(4),
                cursor.getInt(5));
    }

    public ArrayList<Line> selectByType(int LineType) {
        ArrayList<Line> thisType = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),
                TYPE + " = ?",
                new String[]{String.valueOf(LineType)},
                null, null, null);
        while (cursor.moveToNext())
            thisType.add(fromCursor(cursor));
        cursor.close();
        return thisType;
    }
}
