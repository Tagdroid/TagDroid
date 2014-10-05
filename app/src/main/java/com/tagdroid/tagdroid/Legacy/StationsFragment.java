package com.tagdroid.tagdroid.Legacy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.tagdroid.tagdroid.R;

public class StationsFragment extends Fragment {
    public static String ligne;
    public static int id_debut;
    public static int id_fin;
    private Tracker tracker;

    public static StationsFragment newInstance() {
        return new StationsFragment();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recherche, container, false);

        MainActivity.mTitle = getActivity().getResources().getString(R.string.stations);
        getActivity().getActionBar().setTitle(MainActivity.mTitle);


        final ImageButton lignea = (ImageButton) view.findViewById(R.id.lignea);
        lignea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne A";
                id_debut = 0;
                id_fin = 28;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligneb = (ImageButton) view.findViewById(R.id.ligneb);
        ligneb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne B";
                id_debut = 29;
                id_fin = 48;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton lignec = (ImageButton) view.findViewById(R.id.lignec);
        lignec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne C";
                id_debut = 49;
                id_fin = 67;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligned = (ImageButton) view.findViewById(R.id.ligned);
        ligned.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne D";
                id_debut = 68;
                id_fin = 73;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton lignee = (ImageButton) view.findViewById(R.id.lignee);
        lignee.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne E";
                id_debut = 4000;
                id_fin = 4017;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligneco = (ImageButton) view.findViewById(R.id.ligneco);
        ligneco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne CO";
                id_debut = 720;
                id_fin = 736;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton lignen1 = (ImageButton) view.findViewById(R.id.lignen1);
        lignen1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne N1";
                id_debut = 638;
                id_fin = 666;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton lignen3 = (ImageButton) view.findViewById(R.id.lignen3);
        lignen3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne N3";
                id_debut = 667;
                id_fin = 690;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton lignen4 = (ImageButton) view.findViewById(R.id.lignen4);
        lignen4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne N4";
                id_debut = 691;
                id_fin = 712;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne1 = (ImageButton) view.findViewById(R.id.ligne1);
        ligne1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 1";
                id_debut = 1085;
                id_fin = 1142;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne11 = (ImageButton) view.findViewById(R.id.ligne11);
        ligne11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 11";
                id_debut = 2142;
                id_fin = 2176;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne13 = (ImageButton) view.findViewById(R.id.ligne13);
        ligne13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 13";
                id_debut = 177;
                id_fin = 208;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne16 = (ImageButton) view.findViewById(R.id.ligne16);
        ligne16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 16";
                id_debut = 211;
                id_fin = 256;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne21 = (ImageButton) view.findViewById(R.id.ligne21);
        ligne21.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 21";
                id_debut = 257;
                id_fin = 274;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne23 = (ImageButton) view.findViewById(R.id.ligne23);
        ligne23.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 23";
                id_debut = 1275;
                id_fin = 1301;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne26 = (ImageButton) view.findViewById(R.id.ligne26);
        ligne26.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 26";
                id_debut = 301;
                id_fin = 340;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne30 = (ImageButton) view.findViewById(R.id.ligne30);
        ligne30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 30";
                id_debut = 3341;
                id_fin = 3366;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne31 = (ImageButton) view.findViewById(R.id.ligne31);
        ligne31.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 31";
                id_debut = 367;
                id_fin = 401;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne32 = (ImageButton) view.findViewById(R.id.ligne32);
        ligne32.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 32";
                id_debut = 402;
                id_fin = 430;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne33 = (ImageButton) view.findViewById(R.id.ligne33);
        ligne33.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 33";
                id_debut = 431;
                id_fin = 446;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne34 = (ImageButton) view.findViewById(R.id.ligne34);
        ligne34.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 34";
                id_debut = 448;
                id_fin = 484;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne41 = (ImageButton) view.findViewById(R.id.ligne41);
        ligne41.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 41";
                id_debut = 1483;
                id_fin = 1511;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne43 = (ImageButton) view.findViewById(R.id.ligne43);
        ligne43.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 43";
                id_debut = 513;
                id_fin = 522;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne51 = (ImageButton) view.findViewById(R.id.ligne51);
        ligne51.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 51";
                id_debut = 523;
                id_fin = 551;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne54 = (ImageButton) view.findViewById(R.id.ligne54);
        ligne54.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 54";
                id_debut = 1552;
                id_fin = 1568;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne55 = (ImageButton) view.findViewById(R.id.ligne55);
        ligne55.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 55";
                id_debut = 564;
                id_fin = 584;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne56 = (ImageButton) view.findViewById(R.id.ligne56);
        ligne56.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 56";
                id_debut = 588;
                id_fin = 613;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        final ImageButton ligne58 = (ImageButton) view.findViewById(R.id.ligne58);
        ligne58.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ligne = "Ligne 58";
                id_debut = 614;
                id_fin = 637;
                MainActivity.TITLES = new String[]{"LIGNE"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.tracker = EasyTracker.getInstance(this.getActivity());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send(MapBuilder.createAppView().build());
    }

}