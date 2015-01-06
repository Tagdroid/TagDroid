package com.tagdroid.tagapi.SQLApi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tagdroid.tagapi.JSonApi.Favori;

public class FavorisDAO extends DAO<Favori>{
    @Override
    protected String TABLE_NAME() {
        return "Favoris";
    }

    @Override
    protected String ID() {
        return "Id";
    }
    public static final String NAME = "Name",
            LINE = "Line",
            LATITUDE = "Latitude",
            LONGITUDE = "Longitude",
            FAVORI = "Favori";
    @Override
    protected String COLUMNS() {
        return "(" + ID() + " INTEGER PRIMARY KEY, " +
                NAME + " STRING, " +
                LINE + " STRING, " +
                LATITUDE + " INTEGER, " +
                LONGITUDE + " INTEGER, " +
                FAVORI + " BOOLEAN)";
    }

    @Override
    protected String[] AllColumns() {
        return new String[]{ID(), NAME, LINE, LATITUDE, LONGITUDE};
    }

    public FavorisDAO(SQLiteDatabase bdd) {
        super(bdd);
    }

    @Override
    protected ContentValues createValues(Favori m) {
        ContentValues values = new ContentValues();
        values.put(ID(), m.Id);
        values.put(NAME, m.Name);
        values.put(LINE, m.Ligne);
        values.put(LATITUDE, m.Latitude);
        values.put(LONGITUDE, m.Longitude);
        return values;
    }

    @Override
    protected Favori fromCursor(Cursor cursor) {
        return null;
    }

    public long modify(Favori favori) {
        ContentValues values = new ContentValues();
        values.put(ID(), favori.Id);
        values.put(NAME, favori.Name);
        values.put(LINE, favori.Ligne);
        values.put(LATITUDE, favori.Latitude);
        values.put(LONGITUDE, favori.Longitude);
        return bdd.update(TABLE_NAME(), values, ID() + " = " + favori.Id, null);
    }

    public int delete(long id) {
        return bdd.delete(TABLE_NAME(), ID() + " = " + id, null);
    }

    public Favori[] list() {
        Cursor c = bdd.query(TABLE_NAME(), AllColumns(), null, null, null, null, null);
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
        return bdd.query(TABLE_NAME(),AllColumns(), null, null, null, null, null).getCount();
    }

    public Boolean existsFavoriOfId(Long id){
        Cursor c = bdd.query(TABLE_NAME(), AllColumns(), ID() + " = \"" + id +"\"", null, null, null, null);
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
