package com.tagdroid.android.Pages.Favoris;

import android.content.Context;

import com.tagdroid.android.R;
import com.tagdroid.tagapi.JSonApi.Favori;
import com.tagdroid.tagapi.SQLApi.DatabaseHelper;
import com.tagdroid.tagapi.SQLApi.FavorisDAO;

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
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
            FavorisDAO favorisDAO = new FavorisDAO(dbHelper.getReadableDatabase());
            listeFavoris = favorisDAO.list();
            dbHelper.close();
        }
        return listeFavoris;
    }

    public ArrayList<HashMap<String, String>> getFavorisAdaptedArray() {
        if (favorisAdaptedArray == null) {
            favorisAdaptedArray = new ArrayList<>();
            for (Favori i : getFavoris()) {
                HashMap<String, String> temp = new HashMap<>();
                temp.put("nom", i.Name);
                temp.put("ligne", i.Ligne);
                temp.put("id", i.Id.toString());
                try {
                    temp.put("couleur", String.valueOf(
                            R.color.class.getField(i.Ligne.toLowerCase().replaceAll(" ", "")).getInt(null)));
                } catch (Exception e) {
                    e.printStackTrace();
                    temp.put("couleur", String.valueOf(R.color.ligne_default));
                }
                favorisAdaptedArray.add(temp);
            }
        }
        return favorisAdaptedArray;
    }

    public static Boolean isFavori(Context context, long Id) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        FavorisDAO favorisDAO = new FavorisDAO(dbHelper.getReadableDatabase());
        return favorisDAO.existsFavoriOfId(Id);
    }

    public void newFavori() {

    }

    public static int getFavorisNumber(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        FavorisDAO favorisDAO = new FavorisDAO(dbHelper.getReadableDatabase());
        return favorisDAO.getFavorisNumber();
    }
}
