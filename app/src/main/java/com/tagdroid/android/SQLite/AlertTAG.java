package com.tagdroid.android.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AlertTAG extends SQLiteOpenHelper {
 
    public AlertTAG(Context context){
        super(context, "AlertTAG.db", null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL("create table AlertTAG(Id text,Source text,CreateDateString text,Cause text,"
    			+ "BeginValidityDateString text,EndValidityDateString text,Code text,Id2 text,"
    			+ "Name2 text,Name text,Description text,LineId text,Direction text,"
    			+ "ServiceLevel text,Latitude text,Longitude text)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    
    public void insertData(String Id,String Source,String CreateDateString,String Cause,String BeginValidityDateString,
    					String EndValidityDateString,String Code,String Id2,String Name2,String Name,String Description,
    					String LineId,String Direction,String ServiceLevel,String Latitude,String Longitude){
    	
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Id",Id);
        values.put("Source",Source);
        values.put("CreateDateString",CreateDateString);
        values.put("Cause",Cause);
        values.put("BeginValidityDateString",BeginValidityDateString);
        values.put("EndValidityDateString",EndValidityDateString);
        values.put("Code",Code);
        values.put("Id2",Id2);
        values.put("Name2",Name2);
        values.put("Name",Name);
        values.put("Description",Description);
        values.put("LineId",LineId);
        values.put("Direction",Direction);
        values.put("ServiceLevel",ServiceLevel);
        values.put("Latitude",Latitude);
        values.put("Longitude",Longitude);
        
        sqLiteDatabase.insert("AlertTAG",null,values);
    }
    
    public ArrayList fetchData(){
        ArrayList<String>stringArrayList=new ArrayList<>();
        String fetchdata="select * from AlertTAG";
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