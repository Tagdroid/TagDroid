package com.tagdroid.tagdroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.tagdroid.tagapi.JSonApi.Favori;
import com.tagdroid.tagapi.SQLApi.FavorisDAO;
import com.tagdroid.tagapi.SQLApi.Transport.MySQLiteHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

public class StationDetailFragment extends Fragment {
    protected static String titre;
    protected static String id_jour1 = "null";
    private static View view, view2;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15;
    Calcul_ID_Jour id_jour = new Calcul_ID_Jour();
    Calcul_Horaire ligne = new Calcul_Horaire();
    private Tracker tracker;
    private Activity mActivity;
    private String ligne1;
    //private Menu menu;
    private boolean alert = false;
    private String id_station = null;
    private Boolean prefName;
    private Button alert_button;
    private String url_alert = null;
    private GoogleMap mMap;
    private LatLng place;
    private LinearLayout load_layout, data_layout, no_horaires_layout;
    private LinearLayout case1, case1_suivant, case2, case2_suivant, case3, case3_suivant, case4, case4_suivant, case5, case5_suivant;

    public static StationDetailFragment newInstance(String titre, String id_station, String ligne1, String latitude, String longitude) {
        StationDetailFragment f = new StationDetailFragment();
        Bundle b = new Bundle();
        b.putString("titre", titre);
        b.putString("id_station", id_station);
        b.putString("ligne1", ligne1);
        b.putString("latitude", latitude);
        b.putString("longitude", longitude);
        f.setArguments(b);
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.station_detail, container, false);
        } catch (InflateException e) {

        }


        view2 = inflater.inflate(R.layout.alert_trafic_dialog, null);

        load_layout = (LinearLayout) view.findViewById(R.id.load_layout);
        data_layout = (LinearLayout) view.findViewById(R.id.horaires_body);
        no_horaires_layout = (LinearLayout) view.findViewById(R.id.no_horaires_layout);

        //Recuperation des variables
        titre = this.getArguments().getString("titre");
        ligne1 = this.getArguments().getString("ligne1");
        id_station = this.getArguments().getString("id_station");

        alert_button = (Button) view.findViewById(R.id.alert_button);

        MainActivity.mTitle = ligne1;
        getActivity().getActionBar().setTitle(MainActivity.mTitle);


        LinearLayout travaux = (LinearLayout) view.findViewById(R.id.travaux);
        travaux.setVisibility(View.GONE);


        case1 = (LinearLayout) view.findViewById(R.id.case1);
        case1_suivant = (LinearLayout) view.findViewById(R.id.case1_suivant);
        case2 = (LinearLayout) view.findViewById(R.id.case2);
        case2_suivant = (LinearLayout) view.findViewById(R.id.case2_suivant);
        case3 = (LinearLayout) view.findViewById(R.id.case3);
        case3_suivant = (LinearLayout) view.findViewById(R.id.case3_suivant);
        case4 = (LinearLayout) view.findViewById(R.id.case4);
        case4_suivant = (LinearLayout) view.findViewById(R.id.case4_suivant);
        case5 = (LinearLayout) view.findViewById(R.id.case5);
        case5_suivant = (LinearLayout) view.findViewById(R.id.case5_suivant);

        case1.setVisibility(View.GONE);
        case2.setVisibility(View.GONE);
        case3.setVisibility(View.GONE);
        case4.setVisibility(View.GONE);
        case5.setVisibility(View.GONE);

        LinearLayout map_click_layout = (LinearLayout) view.findViewById(R.id.map_click_layout);
        map_click_layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.latitude = place.latitude;
                MainActivity.longitude = place.longitude;
                MainActivity.TITLES = new String[]{"MAP"};
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        tv1 = (TextView) view.findViewById(R.id.vers1);
        tv2 = (TextView) view.findViewById(R.id.tps11);
        tv3 = (TextView) view.findViewById(R.id.tps12);
        tv4 = (TextView) view.findViewById(R.id.vers2);
        tv5 = (TextView) view.findViewById(R.id.tps21);
        tv6 = (TextView) view.findViewById(R.id.tps22);
        tv7 = (TextView) view.findViewById(R.id.vers3);
        tv8 = (TextView) view.findViewById(R.id.tps31);
        tv9 = (TextView) view.findViewById(R.id.tps32);
        tv10 = (TextView) view.findViewById(R.id.vers4);
        tv11 = (TextView) view.findViewById(R.id.tps41);
        tv12 = (TextView) view.findViewById(R.id.tps42);
        tv13 = (TextView) view.findViewById(R.id.vers5);
        tv14 = (TextView) view.findViewById(R.id.tps51);
        tv15 = (TextView) view.findViewById(R.id.tps52);


        int image_ligne = this.getResources().getIdentifier(ligne1.toLowerCase().replace(" ", "") + "_default", "drawable", mActivity.getPackageName());
        ((ImageView) view.findViewById(R.id.logo_ligne)).setImageResource(image_ligne);

        place = new LatLng(Float.parseFloat(getArguments().getString("latitude")), Float.parseFloat(getArguments().getString("longitude")));
        setUpMapIfNeeded();

        // Mise en forme de la page
        TextView tv_station = (TextView) view.findViewById(R.id.station_name2);
        tv_station.setText(titre.toUpperCase());

        if (ligne1.equals("Ligne E")) travaux.setVisibility(View.VISIBLE);
        else getData();

        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view2).create();
        alert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calcul_Alert_Info_Trafic alert = new Calcul_Alert_Info_Trafic();
                alert.execute(url_alert);
                if (id_jour.getStatus() == AsyncTask.Status.FINISHED) dialog.show();
            }
        });

        //Gestion des favoris
        Log.d("id_station", id_station);
        prefName = FavorisHelper.isFavori(getActivity(), Integer.valueOf(id_station.split("_")[0]));
        return view;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mActivity = getActivity();
        this.tracker = EasyTracker.getInstance(this.getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        prefName = FavorisHelper.isFavori(getActivity(), Integer.valueOf(id_station.split("_")[0]));
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send(MapBuilder.createAppView().build());
    }


    @SuppressLint("NewApi")
    private void setUpMapIfNeeded() {
        SupportMapFragment fm = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map2);
        mMap = fm.getMap();
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.latitude + 0.001, place.longitude), 15));
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            //mMap.addMarker(new MarkerOptions().position(place));
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_station, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(0).setChecked(prefName);
        if (prefName) menu.getItem(0).setIcon(R.drawable.menu_favoris_checked);
        else menu.getItem(0).setIcon(R.drawable.menu_favoris);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MySQLiteHelper dbHelper = new MySQLiteHelper("Favoris.db", getActivity(), null);
        FavorisDAO favorisDAO = new FavorisDAO(dbHelper, false, false, -1, -1);


        switch (item.getItemId()) {
            case R.id.menu_refresh:
                getData();
                return true;
            case R.id.menu_favoris:
                if (item.isChecked()) {
                    item.setChecked(false);
                    item.setIcon(R.drawable.menu_favoris);
                    Toast.makeText(getActivity(), R.string.delfavoris, Toast.LENGTH_SHORT).show();
                    favorisDAO.delete(Integer.valueOf(id_station.split("=")[1]));
                    MainActivity.favoris_check = false;

                } else {
                    item.setChecked(true);
                    item.setIcon(R.drawable.menu_favoris_checked);
                    Toast.makeText(getActivity(), R.string.addfavoris, Toast.LENGTH_SHORT).show();
                    favorisDAO.add(new Favori(
                            Integer.valueOf(id_station.split("=")[1]), titre, ligne1,
                            Double.valueOf(getArguments().getString("latitude")),
                            Double.valueOf(getArguments().getString("longitude"))));
                    MainActivity.favoris_check = true;
                }

                NsMenuAdapter mAdapter = new NsMenuAdapter(getActivity());
                mAdapter.addHeader(R.string.ns_menu_main_header);
                String[] menuItems = getResources().getStringArray(R.array.ns_menu_items);
                String[] menuItemsIcon = getResources().getStringArray(R.array.ns_menu_items_icon);

                int res = 0;
                for (String item3 : menuItems) {

                    int id_title = getResources().getIdentifier(item3, "string", this.getActivity().getPackageName());
                    int id_icon = getResources().getIdentifier(menuItemsIcon[res], "drawable", this.getActivity().getPackageName());

                    NsMenuItemModel mItem = new NsMenuItemModel(id_title, id_icon);
                    mItem.counter = FavorisHelper.getFavorisNumber(getActivity());
                    mAdapter.addItem(mItem);
                    res++;
                }

                mAdapter.addHeader(R.string.ns_menu_main_header2);

                menuItems = getResources().getStringArray(R.array.ns_menu_items2);
                String[] menuItemsIcon2 = getResources().getStringArray(R.array.ns_menu_items_icon2);

                int res2 = 0;
                for (String item4 : menuItems) {

                    int id_title = getResources().getIdentifier(item4, "string", this.getActivity().getPackageName());
                    int id_icon = getResources().getIdentifier(menuItemsIcon2[res2], "drawable", this.getActivity().getPackageName());

                    mAdapter.addItem(new NsMenuItemModel(id_title, id_icon));
                    res2++;
                }
                MainActivity.mDrawerList.setAdapter(mAdapter);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getData() {
        if (id_jour.getStatus() == AsyncTask.Status.FINISHED) {
            id_jour = new Calcul_ID_Jour();
        }

        if (id_jour.getStatus() != AsyncTask.Status.RUNNING) {
            load_layout.setVisibility(View.VISIBLE);
            data_layout.setVisibility(View.GONE);
            no_horaires_layout.setVisibility(View.GONE);
            id_jour.execute();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class Calcul_ID_Jour extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("unused")
        protected void onProgressUpdate() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String id_jour = "";
            try {
                URL url = new URL("http://preprod.transinfoservice.ws.cityway.fr/TAG/api/disruption/v1/GetDisruptedLines/json?key=TAGDEV");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        id_jour = line;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return id_jour;
        }

        protected void onPostExecute(String result) {
            id_jour1 = result;
            if (id_jour1.equals("null")) {
                Toast.makeText(mActivity, R.string.noconnexion, Toast.LENGTH_SHORT).show();
                load_layout.setVisibility(View.GONE);
                no_horaires_layout.setVisibility(View.VISIBLE);
            } else {
                if (ligne.getStatus() == Status.FINISHED) ligne = new Calcul_Horaire();
                if (ligne.getStatus() != Status.RUNNING) ligne.execute();
            }
        }
    }

    private class Calcul_Alert_Info_Trafic extends AsyncTask<String, Void, String[]> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {
            return new String[3];
        }

        protected void onPostExecute(String[] result) {
            TextView titre_alert2 = (TextView) view2.findViewById(R.id.titre_alert2);
            TextView date_alert = (TextView) view2.findViewById(R.id.date_alert);
            TextView texte_alert = (TextView) view2.findViewById(R.id.texte_alert);

            titre_alert2.setText(Html.fromHtml(result[0]));
            date_alert.setText(Html.fromHtml(result[1]));
            texte_alert.setText(Html.fromHtml(result[2]));
        }
    }

    private class Calcul_Horaire extends AsyncTask<String, Void, Boolean> {
        String vers1 = null, tps11, tps12;
        String vers2, tps21, tps22;
        String vers3, tps31, tps32;
        String vers4, tps41, tps42;
        String vers5, tps51, tps52;
        boolean full = false;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @SuppressWarnings("unused")
        protected void onProgressUpdate() {
        }

        @SuppressLint("NewApi")
        @Override
        protected Boolean doInBackground(String... url) {
            String url_station = "http://tag.bdigital.prod.mobivillage.com/index.php?p=49&" + id_station + "&m=1&I=" + id_jour1;
            try {
                Document doc = Jsoup.connect(url_station)
                        .userAgent("Mozilla")
                        .get();

                Element var_1 = doc.getElementsByClass("corpsL").first();
                var_1.select("span").remove();
                Element var_infotrafic = var_1.getElementsByClass("infoTrafic").first();
                if (var_infotrafic != null) {
                    url_alert = "http://tag.bdigital.prod.mobivillage.com/index.php"
                            + var_infotrafic.select("a[href^=?p]").first().attr("href");
                    alert = true;
                }
                var_1.select("div").remove();
                var_1.select("a").remove();
                var_1.select("br").remove();
                var_1.select("b:contains(!)").remove();

                int nbr_stations = (var_1.getAllElements().size()) - 1;

                if (nbr_stations != 0 && nbr_stations <= 6) {
                    String[][] stations = new String[nbr_stations][3];

                    for (int i = 0; i < nbr_stations; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (j == 0 && var_1.getAllElements().get(i + 1).text() != null) {
                                stations[i][j] = var_1.getAllElements().get(i + 1).text().substring(5);
                            } else stations[i][j] = "";
                        }
                    }

                    String var_2 = var_1.toString().replace("\n", "")
                            .replace("<div class=\"corpsL\">", "")
                            .replace("</div>", "");
                    String[] var_3 = var_2.split("<b>");
                    for (int i = 1; i <= nbr_stations; i++) {
                        String[] var_4 = var_3[i].split("</b>");
                        var_4[1] = var_4[1].substring(2);
                        String[] var_5 = var_4[1].split("Prochain passage : ");
                        for (int j = 1; j < var_5.length; j++) {
                            if (var_5[j].contains("proche")) var_5[j] = "Départ proche";
                            stations[i - 1][j] = var_5[j];
                        }
                    }

						/* Tri des stations par ordre alphabetique pour conserver l'ordre au refresh */
                    Arrays.sort(stations, new Comparator<String[]>() {
                        @Override
                        public int compare(final String[] entry1, final String[] entry2) {
                            return entry1[0].compareTo(entry2[0]);
                        }
                    });


                    if (stations.length >= 1) {
                        full = true;
                        vers1 = stations[0][0].trim();
                        tps11 = stations[0][1].trim();
                        tps12 = stations[0][2].trim();
                    }
                    if (stations.length >= 2) {
                        if (stations[1][0].trim().compareTo(vers1) > 0) {
                            vers2 = stations[1][0].trim();
                            tps21 = stations[1][1].trim();
                            tps22 = stations[1][2].trim();
                        } else {
                            vers1 = stations[1][0].trim();
                            tps11 = stations[1][1].trim();
                            tps12 = stations[1][2].trim();
                            vers2 = stations[0][0].trim();
                            tps21 = stations[0][1].trim();
                            tps22 = stations[0][2].trim();
                        }

                    }
                    if (stations.length >= 3) {
                        vers3 = stations[2][0].trim();
                        tps31 = stations[2][1].trim();
                        tps32 = stations[2][2].trim();
                    }
                    if (stations.length >= 4) {
                        vers4 = stations[3][0].trim();
                        tps41 = stations[3][1].trim();
                        tps42 = stations[3][2].trim();
                    }
                    if (stations.length >= 5) {
                        vers5 = stations[4][0].trim();
                        tps51 = stations[4][1].trim();
                        tps52 = stations[4][2].trim();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                vers1 = "Problème de connexion";
            }
            return full;
        }


        @Override
        protected void onPostExecute(Boolean data) {
            load_layout.setVisibility(View.GONE);
            if (data) {
                no_horaires_layout.setVisibility(View.GONE);
                data_layout.setVisibility(View.VISIBLE);
            } else {
                no_horaires_layout.setVisibility(View.VISIBLE);
                data_layout.setVisibility(View.GONE);
            }


            if (alert) alert_button.setVisibility(View.VISIBLE);


            if (vers1 != null) {
                load_layout.setVisibility(View.GONE);
                case1.setVisibility(View.VISIBLE);
                if (tps12 == null) case1_suivant.setVisibility(View.GONE);
            }
            if (vers2 != null) {
                case2.setVisibility(View.VISIBLE);
                if (tps22.isEmpty()) case2_suivant.setVisibility(View.GONE);
            }
            if (vers3 != null) {
                case3.setVisibility(View.VISIBLE);
                if (tps32.isEmpty()) case3_suivant.setVisibility(View.GONE);
            }
            if (vers4 != null) {
                case4.setVisibility(View.VISIBLE);
                if (tps42.isEmpty()) case4_suivant.setVisibility(View.GONE);
            }
            if (vers5 != null) {
                case5.setVisibility(View.VISIBLE);
                if (tps52.isEmpty()) case5_suivant.setVisibility(View.GONE);
            }

            tv1.setText(vers1);
            tv2.setText(tps11);
            tv3.setText(tps12);
            tv4.setText(vers2);
            tv5.setText(tps21);
            tv6.setText(tps22);
            tv7.setText(vers3);
            tv8.setText(tps31);
            tv9.setText(tps32);
            tv10.setText(vers4);
            tv11.setText(tps41);
            tv12.setText(tps42);
            tv13.setText(vers5);
            tv14.setText(tps51);
            tv15.setText(tps52);

        }
    }
}