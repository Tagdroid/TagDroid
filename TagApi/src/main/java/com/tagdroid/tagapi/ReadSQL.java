package com.tagdroid.tagapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.SQLApi.MySQLiteHelper;
import com.tagdroid.tagapi.SQLApi.Transport.LinesDAO;

import java.util.ArrayList;

public class ReadSQL {
    private Context context;
    public ReadSQL(Context context) {
        this.context = context;
    }

    public ArrayList<Line> getAllLines() {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase daTAGase = dbHelper.getWritableDatabase();
        daTAGase.beginTransaction();

        LinesDAO linesDAO = new LinesDAO(daTAGase,
                dbHelper.isCreating, dbHelper.isUpgrading, dbHelper.oldVersion, dbHelper.newVersion);

        ArrayList<Line> allLinesArrayList = linesDAO.getAllLines();

        daTAGase.setTransactionSuccessful();
        daTAGase.endTransaction();
        dbHelper.close();
        return allLinesArrayList;
    }
}
