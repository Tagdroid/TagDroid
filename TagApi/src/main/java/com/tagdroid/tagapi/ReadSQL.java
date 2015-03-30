package com.tagdroid.tagapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Disruption.Disruption;
import com.tagdroid.tagapi.JSonApi.TimeTable.Time;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.Disruption.DisruptionDAO;
import com.tagdroid.tagapi.SQLApi.TimeTable.Timetable1DAO;
import com.tagdroid.tagapi.SQLApi.Transport.DirectionDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LineStopsDAO;
import com.tagdroid.tagapi.SQLApi.Transport.LinesDAO;

import java.util.ArrayList;

public class ReadSQL {
    private static ArrayList<Line> AllLines;
    private static ArrayList<Time> AllTimes;
    private static ArrayList<Disruption> AllDisruptions;

    private static Line         selectedLine;
    private static Direction    selectedDirection;
    private static LineStop     selectedLineStop;

    public static ArrayList<Line> getAllLines(Context context) {
        if (AllLines == null) {
            SQLiteDatabase daTAGase = DatabaseHelper.getInstance(context).getReadableDatabase();
            daTAGase.beginTransaction();

            AllLines = (new LinesDAO(daTAGase)).selectAll();
            for (Line line : AllLines) {
                ArrayList<Direction> directions = (new DirectionDAO(daTAGase))
                        .getDirectionsOfLine(line.getId());
                line.setDirectionList(directions.toArray(new Direction[directions.size()]));
            }
            daTAGase.setTransactionSuccessful();
            daTAGase.endTransaction();
        }
        return AllLines;
    }

    public static ArrayList<LineStop> getStops(long lineId, int directionId, Context context) {
        SQLiteDatabase daTAGase = DatabaseHelper.getInstance(context).getReadableDatabase();
        daTAGase.beginTransaction();

        ArrayList<LineStop> stopArrayList = (new LineStopsDAO(daTAGase))
                .stopsFromLineAndDirection(lineId, directionId);

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        return stopArrayList;
    }

    public static ArrayList<LineStop> getStopsOfLineAndLogicalAndDirection(
            long lineId, long logicalStopId, int direction, Context context) {
        SQLiteDatabase daTAGase = DatabaseHelper.getInstance(context).getReadableDatabase();
        daTAGase.beginTransaction();

        ArrayList<LineStop> stopArrayList = (new LineStopsDAO(daTAGase))
                .stopsFromLineAndLogicalAndDirection(lineId, logicalStopId, direction);

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        return stopArrayList;
    }

    public static ArrayList<Time> getAllTimes(Context context) {
        if (AllTimes == null) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase daTAGase = dbHelper.getReadableDatabase();
            daTAGase.beginTransaction();

            AllTimes = new Timetable1DAO(daTAGase).selectAll();

            daTAGase.setTransactionSuccessful();
            daTAGase.endTransaction();
        }
        return AllTimes;
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
