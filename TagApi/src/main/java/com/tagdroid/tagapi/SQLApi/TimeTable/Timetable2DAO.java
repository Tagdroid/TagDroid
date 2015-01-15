package com.tagdroid.tagapi.SQLApi.TimeTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.TimeTable.Time;
import com.tagdroid.tagapi.SQLApi.DAO;

import java.util.ArrayList;

public class Timetable2DAO extends DAO<Time> {
    @Override
    protected String ID() {
        return "TimeTable2";
    }
    public static final String PASSINGTIME = "PassingTime", VEHICLE_JOURNEY_ID = "VehicleJourneyId", VEHICLE_JOURNEY_REF = "VehicleJourneyRef";

    public Timetable2DAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected String TABLE_NAME() {
        return "TimeTable2";
    }

    @Override
    protected String COLUMNS() {
        return "(" + PASSINGTIME + " INTEGER, "
                + VEHICLE_JOURNEY_ID + " INTEGER, "
                + VEHICLE_JOURNEY_REF + " TEXT);";
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{PASSINGTIME, VEHICLE_JOURNEY_ID, VEHICLE_JOURNEY_REF};
    }

    @Override
    protected ContentValues createValues(Time m) {
        ContentValues values = new ContentValues();
        values.put(PASSINGTIME, m.getPassingTime());
        values.put(VEHICLE_JOURNEY_ID, m.getVehicleJourneyId());
        values.put(VEHICLE_JOURNEY_REF, m.getVehicleJourneyRef());
        return values;
    }

    @Override
    protected Time fromCursor(Cursor c) {
        return new Time(c.getInt(0),
                c.getLong(1),
                c.getString(2));
    }

    public ArrayList<Time> getNextTimeFromVehicle(long vehicleId) {
        ArrayList<Time> nextTimeFromVehicle = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_NAME(), AllColumns(),VEHICLE_JOURNEY_ID + " = ?", new String[]{String.valueOf(vehicleId)},null, null, null);
        while (cursor.moveToNext())
            nextTimeFromVehicle.add(fromCursor(cursor));

        cursor.close();
        return nextTimeFromVehicle;
    }
}
