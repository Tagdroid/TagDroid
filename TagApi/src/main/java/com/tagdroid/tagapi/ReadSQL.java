package com.tagdroid.tagapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Transport.DirectionDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LinesDAO;

import java.util.ArrayList;

public class ReadSQL {
    private Context context;
    public ReadSQL(Context context) {
        this.context = context;
    }

    public ArrayList<Line> getAllLines() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase daTAGase = dbHelper.getReadableDatabase();
        daTAGase.beginTransaction();

        LinesDAO linesDAO = new LinesDAO(daTAGase);
        DirectionDAO directionDAO = new DirectionDAO(daTAGase);

        ArrayList<Line> allLinesArrayList = linesDAO.selectAll();
        for (Line i : allLinesArrayList) {
            ArrayList<Direction> directions = directionDAO.getDirectionsOfLine(i.getId());
            i.setDirectionList(directions.toArray(new Direction[directions.size()]));
        }
        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        return allLinesArrayList;
    }
}
