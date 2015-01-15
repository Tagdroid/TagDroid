package com.tagdroid.tagapi.SQLApi.Disruption;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Disruption.Disruption;
import com.tagdroid.tagapi.SQLApi.DAO;

public class DisruptionDAO extends DAO<Disruption> {
    @Override
    protected String ID() {
        return "Id";
    }
    public static final String NAME = "Name", DESCRIPTION = "Description", CREATEDATE = "CreateDate",
            BEGINVALIDITYDATE = "BeginValidityDate", ENDVALIDITYDATE = "EndValidityDate", TYPENAME = "TypeName";
            //LINE_ID = "LineId", DIRECTION = "Direction";

    public DisruptionDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected String TABLE_NAME() {
        return "Disruption";
    }

    @Override
    protected String COLUMNS() {
        return "(" + ID()   + " INTEGER, " +
                NAME        + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                CREATEDATE  + " TEXT, " +
                BEGINVALIDITYDATE  + " TEXT, " +
                ENDVALIDITYDATE    + " TEXT, " +
                TYPENAME   + " TEXT);";
                /*LINE_ID     + " INTEGER, " +
                DIRECTION   + " INTEGER);";*/
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{ID(), NAME, DESCRIPTION, CREATEDATE, BEGINVALIDITYDATE, ENDVALIDITYDATE, TYPENAME};// LINE_ID, DIRECTION};
    }

    @Override
    protected ContentValues createValues(Disruption m) {
        ContentValues values = new ContentValues();
        values.put(ID(), m.getId());
        values.put(NAME, m.getName());
        values.put(DESCRIPTION, m.getDescription());
        values.put(CREATEDATE, m.getCreateDateString());
        values.put(BEGINVALIDITYDATE, m.getBeginValidityDateString());
        values.put(ENDVALIDITYDATE, m.getEndValidityDateString());
        values.put(TYPENAME, m.getDisruptionType().getName());
       /*
        values.put(LINE_ID, m.getDisruptedLines()[0].getLineId());
        values.put(DIRECTION, m.getDisruptedLines()[0].getDirection());*/
        return values;
    }

    @Override
    protected Disruption fromCursor(Cursor cursor) {
        return new Disruption(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getLong(5),
                cursor.getInt(6),
                cursor.getInt(7),
                cursor.getString(8),
                cursor.getString(9));
    }


    /*public ArrayList<Disruption> DisruptionFromLineAndDirection(long lineId, int directionId) {
        ArrayList<Disruption> DisruptionFromLineAndDirection = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),
                LINE_ID + " = ? AND " + DIRECTION + " = ?",
                new String[]{String.valueOf(lineId),String.valueOf(directionId)},
                null, null, null);
        while (cursor.moveToNext())
            DisruptionFromLineAndDirection.add(fromCursor(cursor));
        cursor.close();
        return DisruptionFromLineAndDirection;
    }*/

}
