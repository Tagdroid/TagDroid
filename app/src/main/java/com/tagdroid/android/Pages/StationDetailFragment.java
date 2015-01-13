package com.tagdroid.android.Pages;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.JSonApi.Transport.Direction;
import com.tagdroid.tagapi.JSonApi.Transport.Line;
import com.tagdroid.tagapi.JSonApi.Transport.LineStop;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;


public class StationDetailFragment extends Page implements ProgressionInterface {
    private LineStop lineStop;
    private Line ligne;
    private Direction direction, direction2;
    private Handler handler = new Handler();
    private SwipeRefreshLayout swipeLayout;
    private CardView cardview1, cardview2;
    private ProgressDialog progression;
    private boolean other_direction;
    private TextView direction_tv1, direction_tv2;

    private GoogleMap map;

    @Override
    public String getTitle() {
        return lineStop.getName();
    }

    @Override
    public Integer getMenuId() {
        return null;
    }

    public StationDetailFragment() {
        getDetailsFromSQL();
    }
    private void getDetailsFromSQL() {
        Log.d("Details","getDetailsFromSQL");
        ligne = ReadSQL.getSelectedLine();
        direction = ReadSQL.getSelectedDirection();
        int dir2;
        if(ReadSQL.getSelectedDirection().getDirection()==1) dir2=1; else dir2=0;
        direction2= ReadSQL.getSelectedLine().getDirectionList()[dir2];
        lineStop = ReadSQL.getSelectedLineStop();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_detail, container, false);

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext()) == ConnectionResult.SUCCESS){
            //setUpMapIfNeeded();
        }else{
            Toast.makeText(getActivity(), "Please install google play services", Toast.LENGTH_LONG).show();
        }

        cardview2 = (CardView)view.findViewById(R.id.card_view2);
        direction_tv1 = (TextView)view.findViewById(R.id.direction);
        direction_tv2 = (TextView)view.findViewById(R.id.direction2);


        other_direction=true;

        direction_tv1.setText(direction.getName());

        //TODO Check if other direction exists
        if(other_direction){
            cardview2.setVisibility(View.VISIBLE);
            direction_tv2.setText(direction2.getName());
        }else{
            cardview2.setVisibility(View.GONE);
        }


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO ne veut pas s'excuter une seconde fois ?!
                        swipeLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        return view;
    }

    @Override
    public void onDownloadStart() {
        progression = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.loading), true);
    }

    @Override
    public void onDownloadFailed(Exception e) {
        progression.dismiss();

        Toast.makeText(getActivity(), "Erreur de chargement :\n" + e.getLocalizedMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDownloadProgression(int progression, int total) {

    }

    @Override
    public void onDownloadComplete() {
        progression.dismiss();
    }

    private void setUpMapIfNeeded() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //if(mapFragment!=null) map = mapFragment.getMap();
        if (map != null) {
            LatLng place = new LatLng(lineStop.getLatitude(), lineStop.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.latitude + 0.001, place.longitude), 15));
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            map.getUiSettings().setAllGesturesEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setCompassEnabled(false);
        }
    }

}
