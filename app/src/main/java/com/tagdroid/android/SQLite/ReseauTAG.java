package com.tagdroid.android.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ReseauTAG extends SQLiteOpenHelper {
 
    public ReseauTAG(Context context){
        super(context, "ReseauTAG.db", null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL("create table ReseauTAG(Id text,LocalityId text,Name text, PointType text)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    
    public void insertData(String Id, String LocalityId, String Name, String PointType){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Id",Id);
        values.put("LocalityId",LocalityId);
        values.put("Name",Name);
        values.put("PointType",PointType);
        sqLiteDatabase.insert("ReseauTAG",null,values);
    }
    
    public ArrayList fetchData(){
        ArrayList<String>stringArrayList=new ArrayList<String>();
        String fetchdata="select * from ReseauTAG";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
           do
            {
                stringArrayList.add(cursor.getString(0));
                stringArrayList.add(cursor.getString(1));
                stringArrayList.add(cursor.getString(2));
                stringArrayList.add(cursor.getString(3));
             } while (cursor.moveToNext());
        }
    return stringArrayList;
    }
}