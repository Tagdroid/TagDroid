package com.tagdroid.tagdroid.Pages;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.tagdroid.tagdroid.Page;
import com.tagdroid.tagdroid.R;

public class LignesFragment_Beta extends Page {
    public LignesFragment_Beta() {
    }
    @Override
    public String getTitle() {
        return getString(R.string.lignes);
    }
    @Override
    public Integer getMenuId() {
        return R.menu.menu_lignes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lignes_grid_beta, container, false);
        GridView tramGridView = (GridView) view.findViewById(R.id.tramGrid);
        Ligne[] lignesTram = new Ligne[5];
        lignesTram[0] = new Ligne(R.color.lignea, "Ligne A", 0);
        lignesTram[1] = new Ligne(R.color.ligneb, "Ligne B", 0);
        lignesTram[2] = new Ligne(R.color.lignec, "Ligne C", 0);
        lignesTram[3] = new Ligne(R.color.ligned, "Ligne D", 0);
        lignesTram[4] = new Ligne(R.color.lignee, "Ligne E", 0);
        GridView ChronoGridView = (GridView) view.findViewById(R.id.ChronoGrid);
        Ligne[] lignesChrono = new Ligne[7];
        lignesChrono[0] = new Ligne(R.color.lignec1, "Ligne C1", 0);
        lignesChrono[1] = new Ligne(R.color.lignec2, "Ligne C2", 0);
        lignesChrono[2] = new Ligne(R.color.lignec3, "Ligne C3", 0);
        lignesChrono[3] = new Ligne(R.color.lignec4, "Ligne C4", 0);
        lignesChrono[4] = new Ligne(R.color.lignec5, "Ligne C5", 0);
        lignesChrono[5] = new Ligne(R.color.lignec6, "Ligne C6", 0);
        lignesChrono[6] = new Ligne(R.color.lignee, "Ligne Ebus", 0);
        GridView ProximoGridView = (GridView) view.findViewById(R.id.ProximoGrid);
        Ligne[] lignesProximo = new Ligne[12];
        lignesProximo[0] = new Ligne(R.color.ligne11, "Ligne 11", 0);
        lignesProximo[1] = new Ligne(R.color.ligne12, "Ligne 12", 0);
        lignesProximo[2] = new Ligne(R.color.ligne13, "Ligne 13", 0);
        lignesProximo[3] = new Ligne(R.color.ligne14, "Ligne 14", 0);
        lignesProximo[4] = new Ligne(R.color.ligne15, "Ligne 15", 0);
        lignesProximo[5] = new Ligne(R.color.ligne16, "Ligne 16", 0);
        lignesProximo[6] = new Ligne(R.color.ligne17, "Ligne 17", 0);
        lignesProximo[7] = new Ligne(R.color.ligne18, "Ligne 18", 0);
        lignesProximo[8] = new Ligne(R.color.ligne19, "Ligne 19", 0);
        lignesProximo[9] = new Ligne(R.color.ligne20, "Ligne 20", 0);
        lignesProximo[10] = new Ligne(R.color.ligne21, "Ligne 21", 0);
        lignesProximo[11] = new Ligne(R.color.ligne22, "Ligne 22", 0);
        GridView FlexoGridView = (GridView) view.findViewById(R.id.FlexoGrid);
        Ligne[] lignesFlexo = new Ligne[3];
        lignesFlexo[0] = new Ligne(R.color.ligne40, "Ligne 40", 0);
        lignesFlexo[1] = new Ligne(R.color.ligne41, "Ligne 41", 0);
        lignesFlexo[2] = new Ligne(R.color.ligne42, "Ligne 42", 0);



        tramGridView.setAdapter(new LigneAdapter(getActivity(), lignesTram));
        tramGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("auie", position + "auie");
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        ChronoGridView.setAdapter(new LigneAdapter(getActivity(), lignesChrono));
        ChronoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("auie", position + "auie");
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        ProximoGridView.setAdapter(new LigneAdapter(getActivity(), lignesProximo));
        ProximoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("auie", position + "auie");
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        FlexoGridView.setAdapter(new LigneAdapter(getActivity(), lignesFlexo));
        FlexoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("auie", position + "auie");
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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
            Button view;
            if (oldView == null) {
                view = new Button(context);
                view.setLayoutParams(new GridView.LayoutParams(113, 113));

                view.setBackgroundResource(R.drawable.button_shape_style); //On applique le style
                view.setText(lignes[position].name); // puis le texte

                //On vient chercher le background du style appliqué sur le shape (A TESTER)
                LayerDrawable bgDrawable = (LayerDrawable)view.getBackground();
                final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape2);
                final GradientDrawable shape2 = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape4);
                shape.setColor(lignes[position].color);
                shape2.setColor(lignes[position].color);
                
                //view.setBackgroundColor(lignes[position].color);
            } else
                view = (Button) oldView;
            return view;
        }
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

