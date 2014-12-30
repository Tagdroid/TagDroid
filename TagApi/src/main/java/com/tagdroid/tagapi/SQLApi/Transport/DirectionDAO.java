package com.tagdroid.tagapi.SQLApi.Transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.SQLApi.DAO;

import java.util.ArrayList;

public class DirectionDAO extends DAO<Direction> {
    @Override
    protected String TABLE_NAME() {
        return "Directions";
    }
    public static final String DIRECTION = "Direction", NAME = "Name", LINE_ID = "LineId";
    @Override
    protected String COLUMNS() {
        return "(" + DIRECTION  + " INTEGER, "
                + NAME          + " TEXT, "
                + LINE_ID       + " INTEGER);";
    }
    public static final String[] AllColumns = new String[]{DIRECTION, NAME, LINE_ID};

    public DirectionDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected ContentValues createValues(Direction m) {
        ContentValues values = new ContentValues();
        values.put(DIRECTION, m.getDirection());
        values.put(NAME, m.getName());
        values.put(LINE_ID, m.getLineId());
        return values;
    }

    @Override
    protected Direction fromCursor(Cursor c) {
        return new Direction(c.getInt(0),
                c.getString(1),
                c.getLong(2));
    }

    public ArrayList<Direction> getDirectionsOfLine(long id) {
        ArrayList<Direction> allDirections = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns, LINE_ID + " = ? ", new String[]{"" + id}, null, null, null);
        while (cursor.moveToNext())
            allDirections.add(fromCursor(cursor));

        cursor.close();
        return allDirections;
    }
}