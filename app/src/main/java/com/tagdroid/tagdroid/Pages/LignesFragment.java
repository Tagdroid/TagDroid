package com.tagdroid.tagdroid.Pages;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tagdroid.tagdroid.Page;
import com.tagdroid.tagdroid.R;

public class LignesFragment extends Page {
    public LignesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_lignes_grid, container, false);
        GridView tramGridView = (GridView) view.findViewById(R.id.tramGrid);
        Ligne[] lignesdetram = new Ligne[5];
        lignesdetram[0] = new Ligne(R.drawable.lignea_style, "Ligne A", 0);
        lignesdetram[1] = new Ligne(R.drawable.ligneb_style, "Ligne B", 0);
        lignesdetram[2] = new Ligne(R.drawable.lignec_style, "Ligne C", 0);
        lignesdetram[3] = new Ligne(R.drawable.ligned_style, "Ligne D", 0);
        lignesdetram[4] = new Ligne(R.drawable.lignee_style, "Ligne E", 0);
        GridView busGridView = (GridView) view.findViewById(R.id.busGrid);
        Ligne[] lignesdebus = new Ligne[22];
        lignesdebus[0] = new Ligne(R.drawable.ligneco_style, "Ligne CO", 0);
        lignesdebus[1] = new Ligne(R.drawable.lignen1_style, "Ligne B", 0);
        lignesdebus[2] = new Ligne(R.drawable.lignen3_style, "Ligne C", 0);
        lignesdebus[3] = new Ligne(R.drawable.lignen4_style, "Ligne D", 0);
        lignesdebus[4] = new Ligne(R.drawable.ligne1_style, "Ligne E", 0);
        lignesdebus[5] = new Ligne(R.drawable.ligne11_style, "Ligne E", 0);
        lignesdebus[6] = new Ligne(R.drawable.ligne13_style, "Ligne E", 0);
        lignesdebus[7] = new Ligne(R.drawable.ligne16_style, "Ligne E", 0);
        lignesdebus[8] = new Ligne(R.drawable.ligne21_style, "Ligne E", 0);
        lignesdebus[9] = new Ligne(R.drawable.ligne23_style, "Ligne E", 0);
        lignesdebus[10] = new Ligne(R.drawable.ligne26_style, "Ligne E", 0);
        lignesdebus[11] = new Ligne(R.drawable.ligne30_style, "Ligne E", 0);
        lignesdebus[12] = new Ligne(R.drawable.ligne31_style, "Ligne E", 0);
        lignesdebus[13] = new Ligne(R.drawable.ligne32_style, "Ligne E", 0);
        lignesdebus[14] = new Ligne(R.drawable.ligne33_style, "Ligne E", 0);
        lignesdebus[15] = new Ligne(R.drawable.ligne34_style, "Ligne E", 0);
        lignesdebus[16] = new Ligne(R.drawable.ligne41_style, "Ligne E", 0);
        lignesdebus[17] = new Ligne(R.drawable.ligne51_style, "Ligne E", 0);
        lignesdebus[18] = new Ligne(R.drawable.ligne54_style, "Ligne E", 0);
        lignesdebus[19] = new Ligne(R.drawable.ligne55_style, "Ligne E", 0);
        lignesdebus[20] = new Ligne(R.drawable.ligne56_style, "Ligne E", 0);
        lignesdebus[21] = new Ligne(R.drawable.ligne58_style, "Ligne E", 0);

        tramGridView.setAdapter(new LigneAdapter(getActivity(), lignesdetram));
        tramGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("auie", position + "auie");
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        busGridView.setAdapter(new LigneAdapter(getActivity(), lignesdebus));
        busGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            ImageButton view;
            if (oldView == null) {
                view = new ImageButton(context);
                view.setLayoutParams(new GridView.LayoutParams(113, 113));
                view.setBackgroundResource(lignes[position].drawableId);
            } else
                view = (ImageButton) oldView;
            return view;
        }
    }

    private class Ligne {
        int drawableId;
        String name;
        int ligneId;
        public Ligne(int drawableId, String name, int ligneId) {
            this.drawableId = drawableId;
            this.name = name;
            this.ligneId = ligneId;
        }
    }
}

