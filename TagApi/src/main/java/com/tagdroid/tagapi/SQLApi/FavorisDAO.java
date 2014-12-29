package com.tagdroid.tagapi.SQLApi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Favori;

public class FavorisDAO {
    public static final String TABLE_NAME = "Favoris",
            ID = "Id",
            NAME = "Name",
            LINE = "Line",
            LATITUDE = "Latitude",
            LONGITUDE = "Longitude",
            FAVORI = "Favori";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            NAME + " STRING, " +
            LINE + " STRING, " +
            LATITUDE + " INTEGER, " +
            LONGITUDE + " INTEGER, " +
            FAVORI + " BOOLEAN)";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase bdd;

    public FavorisDAO(SQLiteDatabase bdd, boolean isCreating,
                      boolean isUpdating, int oldVersion, int newVersion) {
        this.bdd = bdd;
        bdd.execSQL(TABLE_CREATE);
        if (isCreating){
            // On créé la table
            bdd.execSQL(TABLE_CREATE);
        }
        else if (isUpdating) {
            bdd.execSQL(TABLE_DROP);
            bdd.execSQL(TABLE_CREATE);
        }
    }

    public long add(Favori favori) {
        if (existsFavoriOfId(favori.Id))
            return 0;
        else {
            ContentValues values = new ContentValues();
            values.put(ID, favori.Id);
            values.put(NAME, favori.Name);
            values.put(LINE, favori.Ligne);
            values.put(LATITUDE, favori.Latitude);
            values.put(LONGITUDE, favori.Longitude);
            return bdd.insert(TABLE_NAME, null, values);

        }
    }

    public long modify(Favori favori) {
        ContentValues values = new ContentValues();
        values.put(ID, favori.Id);
        values.put(NAME, favori.Name);
        values.put(LINE, favori.Ligne);
        values.put(LATITUDE, favori.Latitude);
        values.put(LONGITUDE, favori.Longitude);
        return bdd.update(TABLE_NAME, values, ID + " = " + favori.Id, null);
    }

    public int delete(long id) {
        return bdd.delete(TABLE_NAME, ID + " = " + id, null);
    }

    public Favori select(long Id) {
        Cursor c = bdd.query(TABLE_NAME, new String[]{
                ID,
                NAME,
                LINE,
                LATITUDE,
                LONGITUDE}, ID + " = \"" + Id + "\"", null, null, null, null);
        c.moveToFirst();
        Favori favori = new Favori(c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getDouble(2),
                c.getDouble(3));
        c.close();
        return favori;
    }

    public Favori[] list() {
        Cursor c = bdd.query(TABLE_NAME, new String[]{ID, NAME, LINE, LATITUDE, LONGITUDE},
                null, null, null, null, null);
        int nombreFavoris = c.getCount();

        Favori[] favoris = new Favori[nombreFavoris];

        c.moveToFirst();
        for(int i=0; i<nombreFavoris; i++)
            favoris[i]=new Favori(c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getDouble(2),
                c.getDouble(3));
        return favoris;
    }

    public int getFavorisNumber() {
        return bdd.query(TABLE_NAME, new String[]{ID, NAME, LINE, LATITUDE, LONGITUDE},
                null, null, null, null, null).getCount();
    }

    public Boolean existsFavoriOfId(Long id){
        Cursor c = bdd.query(TABLE_NAME, new String[]{ID, NAME, LINE, LATITUDE, LONGITUDE},
                ID + " = \"" + id +"\"", null, null, null, null);
        c.moveToFirst();
        if(c.getCount()>0){
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }
}
