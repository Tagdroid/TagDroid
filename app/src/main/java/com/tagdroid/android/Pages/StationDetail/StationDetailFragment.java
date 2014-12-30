package com.tagdroid.android.Pages.StationDetail;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.ProgressionInterface;

import java.util.ArrayList;
import java.util.List;


public class StationDetailFragment extends Page implements ProgressionInterface, StationCardAdapter.OnItemClickListener {
    @Override
    public String getTitle() {
        return "Victor Hugo";
    }

    @Override
    public Integer getMenuId() {
        return null;
    }

    private Handler handler = new Handler();
    private SwipeRefreshLayout swipeLayout;
    private GoogleMap map;
    private GoogleMap mMap;
    RecyclerView recycler_view;
    private List<StationCard> StationCardList;
    private GetHoraires gethoraires;
    private ProgressDialog progression;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_detail, container, false);

        gethoraires = new GetHoraires(this);

        recycler_view = (RecyclerView) view.findViewById(R.id.stationCardList);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (StationCardList == null) {
            gethoraires.execute();
            recycler_view.setAdapter(new StationCardAdapter(new ArrayList<StationCard>(),getActivity()));
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
                        //gethoraires.execute();

                        recycler_view.setAdapter(new StationCardAdapter(new ArrayList<StationCard>(), getActivity()));
                        if (gethoraires.getStatus() == AsyncTask.Status.FINISHED)
                            swipeLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext()) == ConnectionResult.SUCCESS) {

            //setUpMapIfNeeded();


//            map = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map2)).getMap();
            /*
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setAllGesturesEnabled(false);
            map.getUiSettings().setCompassEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);

            LatLng place = new LatLng(45.189831, 5.725025);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.latitude + 0.001, place.longitude), 15));*/
        } else {
            Toast.makeText(getActivity(), "Please install google play services", Toast.LENGTH_LONG).show();
        }

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
        recycler_view.setAdapter(new StationCardAdapter(new ArrayList<StationCard>(),getActivity()));
        getActivity().findViewById(R.id.noNews).setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "Erreur de chargement :\n" + e.getLocalizedMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDownloadComplete() {
        progression.dismiss();
        StationCardList = gethoraires.getResult();
        StationCardAdapter stationCardAdapter = new StationCardAdapter(StationCardList, getActivity());
        //StationCardAdapter.setOnItemClickListener(this);
        recycler_view.setAdapter(stationCardAdapter);
    }


    public void onJSonParsingStarted() {}
    public void onJSonParsingFailed(Exception e) {}
    public void onJSonParsingComplete() {}

    @Override
    public void onItemClick(View view, int position) {}

   /* private void setUpMapIfNeeded() {
        mMap = (MapFragment)getFragmentManager().findFragmentById(R.id.map2).getMap();
        if (mMap != null) {
            LatLng place = new LatLng(45.189831, 5.725025);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.latitude + 0.001, place.longitude), 15));
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }*/
}
