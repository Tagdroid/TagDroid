package com.tagdroid.tagdroid;

import android.content.Context;

import com.tagdroid.tagapi.JSonApi.Favori;
import com.tagdroid.tagapi.SQLApi.FavorisDAO;
import com.tagdroid.tagapi.SQLApi.Transport.MySQLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class FavorisHelper {
    private Context context;
    private static Favori[] listeFavoris;
    private static ArrayList<HashMap<String, String>> favorisAdaptedArray;

    public FavorisHelper(Context context) {
        this.context = context;
    }

    public Favori[] getFavoris() {
        if (listeFavoris == null) {
            MySQLiteHelper dbHelper = new MySQLiteHelper("Favoris.db", context, null);
            FavorisDAO favorisDAO = new FavorisDAO(dbHelper, false, false, -1, -1);
            listeFavoris = favorisDAO.list();
            dbHelper.close();
        }
        return listeFavoris;
    }

    public ArrayList<HashMap<String, String>> getFavorisAdaptedArray() {
        if (favorisAdaptedArray == null) {
            favorisAdaptedArray = new ArrayList<HashMap<String, String>>();
            for (Favori i : getFavoris()) {
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put("nom", i.Name);
                temp.put("ligne", i.Ligne);
                temp.put("id", i.Id.toString());
                try {
                    temp.put("couleur", String.valueOf(
                            R.color.class.getField(i.Ligne.toLowerCase().replaceAll(" ", "")).getInt(null)));
                } catch (Exception e) {
                    e.printStackTrace();
                    temp.put("couleur", String.valueOf(R.color.lignea));
                }
                favorisAdaptedArray.add(temp);
            }
        }
        return favorisAdaptedArray;
    }

    public static Boolean isFavori(Context context, long Id) {
        MySQLiteHelper dbHelper = new MySQLiteHelper("Favoris.db", context, null);
        FavorisDAO favorisDAO = new FavorisDAO(dbHelper, false, false, -1, -1);
        return favorisDAO.existsFavoriOfId(Id);
    }

    public void newFavori() {

    }

    public static int getFavorisNumber(Context context) {
        MySQLiteHelper dbHelper = new MySQLiteHelper("Favoris.db", context, null);
        FavorisDAO favorisDAO = new FavorisDAO(dbHelper, false, false, -1, -1);
        return favorisDAO.getFavorisNumber();
    }
}
