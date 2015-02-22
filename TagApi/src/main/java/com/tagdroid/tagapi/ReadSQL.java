package com.tagdroid.tagapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Disruption.Disruption;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Disruption.DisruptionDAO;
import com.tagdroid.tagapi.SQLApi.Transport.DirectionDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LineStopsDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LinesDAO;

import java.util.ArrayList;

public class ReadSQL {
    private static ArrayList<Line> AllLines;
    private static ArrayList<Disruption> AllDisruptions;

    private static Line         selectedLine;
    private static Direction    selectedDirection;
    private static LineStop     selectedLineStop;

    public static ArrayList<Line> getAllLines(Context context) {
        if (AllLines == null) {
            SQLiteDatabase daTAGase = DatabaseHelper.getInstance(context).getReadableDatabase();
            daTAGase.beginTransaction();

            LinesDAO linesDAO = new LinesDAO(daTAGase);
            DirectionDAO directionDAO = new DirectionDAO(daTAGase);

            AllLines = linesDAO.selectAll();
            for (Line i : AllLines) {
                ArrayList<Direction> directions = directionDAO.getDirectionsOfLine(i.getId());
                i.setDirectionList(directions.toArray(new Direction[directions.size()]));
            }
            daTAGase.setTransactionSuccessful();
            daTAGase.endTransaction();
        }
        return AllLines;
    }
    public static ArrayList<Line> getLinesByType(Context context, int LineType) {
        SQLiteDatabase daTAGase = DatabaseHelper.getInstance(context).getReadableDatabase();
        daTAGase.beginTransaction();

        LinesDAO linesDAO = new LinesDAO(daTAGase);
        DirectionDAO directionDAO = new DirectionDAO(daTAGase);

        ArrayList<Line> ThisType = linesDAO.selectByType(LineType);
        for (Line i : ThisType) {
            ArrayList<Direction> directions = directionDAO.getDirectionsOfLine(i.getId());
            i.setDirectionList(directions.toArray(new Direction[directions.size()]));
        }
        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();

        return ThisType;

    }

    public static ArrayList<LineStop> getStops(long lineId, int directionId, Context context) {
        SQLiteDatabase daTAGase = DatabaseHelper.getInstance(context).getReadableDatabase();
        daTAGase.beginTransaction();

        LineStopsDAO lineStopsDAO = new LineStopsDAO(daTAGase);

        ArrayList<LineStop> stopsArrayList = lineStopsDAO
                .stopsFromLineAndDirection(lineId, directionId);

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        return stopsArrayList;
    }

    public static ArrayList<LineStop> getStopsOfLineAndLogicalAndDirection(long lineId, long logicalStopId,
                                                                           int direction,
                                                                           Context context) {
        SQLiteDatabase daTAGase = DatabaseHelper.getInstance(context).getReadableDatabase();
        daTAGase.beginTransaction();
        LineStopsDAO lineStopsDAO = new LineStopsDAO(daTAGase);

        ArrayList<LineStop> lineStopsOfLogicalAndLine = lineStopsDAO
                .stopsFromLineAndLogicalAndDirection(lineId, logicalStopId, direction);

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        return lineStopsOfLogicalAndLine;
    }

    public static ArrayList<Disruption> getAllDisruptions(Context context) {
        if (AllDisruptions == null) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase daTAGase = dbHelper.getReadableDatabase();
            daTAGase.beginTransaction();

            AllDisruptions = new DisruptionDAO(daTAGase).selectAll();

            daTAGase.setTransactionSuccessful();
            daTAGase.endTransaction();
        }
        return AllDisruptions;
    }



    public static void setSelectedLineAndDirection(Line line, Direction direction) {
        selectedLine = line;
        selectedDirection = direction;
    }
    public static void setSelectedLineStop(LineStop lineStop) {
        selectedLineStop = lineStop;
    }

    public static Line      getSelectedLine() {
        return selectedLine;
    }
    public static Direction getSelectedDirection() {
        return selectedDirection;
    }
    public static LineStop  getSelectedLineStop() {
        return selectedLineStop;
    }
}
