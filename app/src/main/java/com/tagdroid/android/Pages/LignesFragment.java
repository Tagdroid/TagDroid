package com.tagdroid.android.Pages;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;

import info.hoang8f.widget.FButton;

public class LignesFragment extends Page {
    private Activity mContext = getActivity();
    public LignesFragment() {
    }
    @Override
    public String getTitle() {
        return getString(R.string.stations);
    }
    @Override
    public Integer getMenuId() {
        return R.menu.menu_lignes;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Log.d("Test menu item","SEARCH");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lignes_grid, container, false);
        GridView tramGridView = (GridView) view.findViewById(R.id.tramGrid);

        //Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Black.ttf");
        /*TextView tramways = (TextView) view.findViewById(R.id.tramways);
        TextView chrono = (TextView) view.findViewById(R.id.chrono);
        TextView proximo = (TextView) view.findViewById(R.id.proximo);
        TextView flexo = (TextView) view.findViewById(R.id.flexo);
        tramways.setTypeface(tf2);
        chrono.setTypeface(tf2);
        proximo.setTypeface(tf2);
        flexo.setTypeface(tf2);*/

        final Ligne[] lignesTram = new Ligne[5];
        lignesTram[0] = new Ligne(R.color.ligne_a, "A", 0);
        lignesTram[1] = new Ligne(R.color.ligne_b, "B", 0);
        lignesTram[2] = new Ligne(R.color.ligne_c, "C", 0);
        lignesTram[3] = new Ligne(R.color.ligne_d, "D", 0);
        lignesTram[4] = new Ligne(R.color.ligne_e, "E", 0);

        GridView ChronoGridView = (GridView) view.findViewById(R.id.ChronoGrid);
        final Ligne[] lignesChrono = new Ligne[7];
        lignesChrono[0] = new Ligne(R.color.ligne_c1, "C1", 0);
        lignesChrono[1] = new Ligne(R.color.ligne_c2, "C2", 0);
        lignesChrono[2] = new Ligne(R.color.ligne_c3, "C3", 0);
        lignesChrono[3] = new Ligne(R.color.ligne_c4, "C4", 0);
        lignesChrono[4] = new Ligne(R.color.ligne_c5, "C5", 0);
        lignesChrono[5] = new Ligne(R.color.ligne_c6, "C6", 0);
        lignesChrono[6] = new Ligne(R.color.ligne_e, "Ebus", 0);

        GridView ProximoGridView = (GridView) view.findViewById(R.id.ProximoGrid);
        final Ligne[] lignesProximo = new Ligne[12];
        lignesProximo[0] = new Ligne(R.color.ligne_11, "11", 0);
        lignesProximo[1] = new Ligne(R.color.ligne_12, "12", 0);
        lignesProximo[2] = new Ligne(R.color.ligne_13, "13", 0);
        lignesProximo[3] = new Ligne(R.color.ligne_14, "14", 0);
        lignesProximo[4] = new Ligne(R.color.ligne_15, "15", 0);
        lignesProximo[5] = new Ligne(R.color.ligne_16, "16", 0);
        lignesProximo[6] = new Ligne(R.color.ligne_17, "17", 0);
        lignesProximo[7] = new Ligne(R.color.ligne_18, "18", 0);
        lignesProximo[8] = new Ligne(R.color.ligne_19, "19", 0);
        lignesProximo[9] = new Ligne(R.color.ligne_20, "20", 0);
        lignesProximo[10] = new Ligne(R.color.ligne_21, "21", 0);
        lignesProximo[11] = new Ligne(R.color.ligne_22, "22", 0);

        GridView FlexoGridView = (GridView) view.findViewById(R.id.FlexoGrid);
        final Ligne[] lignesFlexo = new Ligne[3];
        lignesFlexo[0] = new Ligne(R.color.ligne_40, "40", 0);
        lignesFlexo[1] = new Ligne(R.color.ligne_41, "41", 0);
        lignesFlexo[2] = new Ligne(R.color.ligne_42, "42", 0);




        /* L'event OnItemClick ne fonctionne pas ! Des idées ???? */



       tramGridView.setAdapter(new LigneAdapter(getActivity(), lignesTram));
        tramGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("Bouton GridView", position + "");
                Log.d("Bouton GridView name", lignesTram[position].name);
                Toast.makeText(getActivity(), "Ligne "+lignesTram[position].name, Toast.LENGTH_SHORT).show();
            }
        });

        ChronoGridView.setAdapter(new LigneAdapter(getActivity(), lignesChrono));
        ChronoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("Bouton GridView", position + "");
                Log.d("Bouton GridView name", lignesChrono[position].name);
                Toast.makeText(getActivity(), "Ligne "+lignesChrono[position].name, Toast.LENGTH_SHORT).show();
            }
        });
        ProximoGridView.setAdapter(new LigneAdapter(getActivity(), lignesProximo));
        ProximoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("Bouton GridView", position + "");
                Log.d("Bouton GridView name", lignesProximo[position].name);
                Toast.makeText(getActivity(), "Ligne "+lignesProximo[position].name, Toast.LENGTH_SHORT).show();
            }
        });
        FlexoGridView.setAdapter(new LigneAdapter(getActivity(), lignesFlexo));
        FlexoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("Bouton GridView", position + "");
                Log.d("Bouton GridView name", lignesFlexo[position].name);
                Toast.makeText(getActivity(), "Ligne "+lignesFlexo[position].name, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private String[] getLignesfromDB() {

        return null;
    }

    public class LigneAdapter extends BaseAdapter {
        private Context context;
        Ligne[] lignes;
        public LigneAdapter(Context context, Ligne[] lignes) {
            this.context = context;
            this.lignes = lignes;
        }
        public int getCount() {
            return lignes.length;
        }
        public Object getItem(int position) {
            return this;
        }
        public long getItemId(int position) {
            return 0;
        }


        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View oldView, ViewGroup parent) {
            if (oldView != null)
                return oldView;
            FButton view;
            view = new FButton(context);
            view.setText(lignes[position].name);
            if(lignes[position].name.length()>3)
                view.setTextSize(14);
            else
                view.setTextSize(16);
            view.setTextColor(BlackorWhite(getResources().getColor(lignes[position].color)));
            view.setButtonColor(getResources().getColor(lignes[position].color));
            view.setShadowEnabled(true);
            view.setShadowHeight(15);
            view.setCornerRadius(15);
            view.setClickable(false);
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
            return view;
        }
    }

   private static int BlackorWhite(int color) {
        double brightness = (int)Math.sqrt(Color.red(color)*Color.red(color)*.241 +
                                       Color.green(color)*Color.green(color)*.691 +
                                       Color.blue(color)*Color.blue(color)*.068);

        if (brightness<=160) return Color.WHITE;
        else return Color.BLACK;
    }

    private class Ligne {
        int color;
        String name;
        int ligneId;
        public Ligne(int color, String name, int ligneId) {
            this.color = color;
            this.name = name;
            this.ligneId = ligneId;
        }
    }
}

