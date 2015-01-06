package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.SQLApi.DAO;

public class LinesDAO extends DAO<Line> {
    @Override
    protected String ID() {
        return "Id";
    }
    public static final String NUMBER = "Number", NAME = "Name", IS_ACTIVE = "IsActive";


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
                + IS_ACTIVE + " INTEGER);";
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{ID(), NUMBER, NAME, IS_ACTIVE};
    }


    @Override
    protected ContentValues createValues(Line m) {
        ContentValues values = new ContentValues();
        values.put(ID(), m.getId());
        values.put(NUMBER, m.getNumber());
        values.put(NAME, m.getName());
        values.put(IS_ACTIVE, m.getIsActive());
        return values;
    }

    @Override
    protected Line fromCursor(Cursor cursor) {
        return new Line(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3) != 0);
    }
}
