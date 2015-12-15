package com.tagdroid.android.Pages;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.HttpGet.HttpGetInterface;
import com.tagdroid.tagapi.HttpGet.HttpGetStopHours;
import com.tagdroid.tagapi.JSonApi.TimeTable.Time;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.ReadSQL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;


public class StationDetailFragment extends Page implements SwipeRefreshLayout.OnRefreshListener,
        HttpGetInterface, OnMapReadyCallback {
    // Général
    private Line selectedLine;
    private SwipeRefreshLayout swipeLayout;

    // Direct
    private LineStop selectedLineStop;
    private Direction directionA;
    private TextView horaire1Direct;
    private LinearLayout horairesDirects;

    // Reverse
    private boolean is_reverse_lineStop_existing = true;
    private LineStop reverse_lineStop;      // Le LineStop correspondant à la direction opposée (si existe)
    private Direction directionB;
    private TextView horaire1Reverse;
    private LinearLayout horairesReverses;

    HttpGetStopHours httpGetStopHours;
    private boolean download_is_primary_stop = true;

    @Override
    public String getTitle() {
        return selectedLineStop.getName();
    }
    @Override
    public Integer getMenuId() {
        return null;
    }

    public StationDetailFragment() {
        getDetails();
    }
    private void getDetails() {
        selectedLine = ReadSQL.getSelectedLine();

        directionA = ReadSQL.getSelectedDirection();
        directionB = selectedLine.getDirectionList()[2-directionA.getDirectionId()];

        selectedLineStop = ReadSQL.getSelectedLineStop();

        int reverse_direction = 3-selectedLineStop.getDirection();

        ArrayList<LineStop> reverseDirectionStopsList = ReadSQL.getStopsOfLineAndLogicalAndDirection(
                selectedLine.getId(),selectedLineStop.getLogicalStopId(), reverse_direction,getActivity());
        if (reverseDirectionStopsList.size()>=1)
            reverse_lineStop = reverseDirectionStopsList.get(0);
        else
            is_reverse_lineStop_existing = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_detail, container, false);

        ((TextView) view.findViewById(R.id.directionA)).setText(directionA.getName());
        ((TextView) view.findViewById(R.id.directionB)).setText(directionB.getName());
        view.findViewById(R.id.LigneIndicateur1).getBackground()
                .setColorFilter(selectedLine.color, PorterDuff.Mode.SRC_OVER);
        ((Button)view.findViewById(R.id.LigneIndicateur1)).setText(selectedLine.getNumber());

        horaire1Direct = (TextView)view.findViewById(R.id.horaire1Direct);
        horairesDirects = (LinearLayout)view.findViewById(R.id.horairesDirects);

        horaire1Reverse = (TextView) view.findViewById(R.id.horaireB_1);
        horairesReverses = (LinearLayout)view.findViewById(R.id.horairesReverses);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshTimes);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                onRefresh();
            }
        });

        if (!is_reverse_lineStop_existing)
            view.findViewById(R.id.secondaryStopCardView).setVisibility(View.GONE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.StationMap);
            //mapFragment.getMapAsync(this);
        }

        return view;
    }


    @Override
    public void onRefresh() {
        startDownloadTask();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(selectedLineStop.getLatitude(), selectedLineStop.getLongitude()))
                .title("Marker"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(selectedLineStop.getLatitude(), selectedLineStop.getLongitude()), 15));
    }

    public void startDownloadTask() {
        // We check if we are able to download DB
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        horaire1Direct.setText(R.string.default_horaires_string);
        horaire1Reverse.setText(R.string.default_horaires_string);
        horairesDirects.removeAllViews();
        horairesReverses.removeAllViews();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting())
            Log.d("StationDetail", "Internet connection problem");
        else {
            httpGetStopHours = new HttpGetStopHours(selectedLineStop.getId(),
                    selectedLine.getId(), directionA.getDirectionId(), this, getActivity());
            httpGetStopHours.execute();
        }
    }

    private void setTime(boolean direct, boolean principal, int timeMinutes) {
        TextView horaire;

        if (principal)
            if (direct)
                horaire = horaire1Direct;
            else
                horaire = horaire1Reverse;
        else {
            LinearLayout others;
            if (direct)
                others = horairesDirects;
            else
                others = horairesReverses;
            horaire = new TextView(getActivity());
            horaire.setTextColor(getResources().getColor(R.color.fbutton_color_turquoise));
            horaire.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            others.addView(horaire);
        }

        // L'heure actuelle
        Calendar cal = Calendar.getInstance();
        int now = 60 * cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE);


        switch (timeMinutes) {
            case -1:
                horaire.setText("Service terminé !");
                break;
            case 0:
                horaire.setText("À l'approche !");
                break;
            default:
                if (timeMinutes-now < 20)
                    horaire.setText(" "+(timeMinutes-now) + " " + getString(R.string.minutes_abr)+" ");
                else {
                    int minutes = timeMinutes%60;
                    int heures  = (timeMinutes - minutes)/60;
                    String time = new Formatter().format("%d:%2d",heures, minutes).toString();
                    horaire.setText(" " + time + " ("+(timeMinutes-now) + " " + getString(R.string.minutes_abr)+") ");
                }
        }
    }

    @Override
    public void onHttpGetStart() {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void onHttpGetDownloadFinished() {
    }

    @Override
    public void onHttpGetDownloadFailed() {
    }

    @Override
    public void onHttpGetReadJSonFinished() {
        swipeLayout.setRefreshing(false);
        ArrayList<Time> horaires = httpGetStopHours.StopPassingTimeList;

        int horairesCount = horaires.size();

        if (horairesCount == 0 )
            setTime(download_is_primary_stop, true, -1);
        else
            for (int i = 0; i < horaires.size(); i++)
                setTime(download_is_primary_stop, (i==0), horaires.get(i).getPassingTime());

        if (download_is_primary_stop) {
            if (is_reverse_lineStop_existing) {
                httpGetStopHours = new HttpGetStopHours(reverse_lineStop.getId(),
                        selectedLine.getId(), directionB.getDirectionId(), this, getActivity());
                httpGetStopHours.execute();
            }
        }

        download_is_primary_stop = !download_is_primary_stop;
    }

    @Override
    public void onHttpGetReadJSonFailed(Exception e) {
        swipeLayout.setRefreshing(false);
        Log.d("stationdetailfragment", "ondownloadfailed");
        Toast.makeText(getActivity(), "Erreur de chargement :\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onHttpGetBadStatusCode(int statusCode, String message) {
    }
}
