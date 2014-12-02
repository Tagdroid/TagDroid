package com.tagdroid.android.Pages;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.tagdroid.android.Page;
import com.tagdroid.android.R;

public class MapFragment extends Page {
    @Override
    public String getTitle() {
        return getString(R.string.map);
    }
    @Override
    public Integer getMenuId() {
        return R.menu.menu_map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tram:
                return true;
            case R.id.menu_bus:
                return true;
            case R.id.menu_others:
                return true;
            case R.id.menu_map:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    private Activity mActivity;
    private MapView mMapView;

    private Bundle mBundle;
    private static final LatLng GRENOBLE = new LatLng(45.1869,5.72639);
    private GoogleMap mMap;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.legacy_fragment_map, container, false);

        MapsInitializer.initialize(mActivity);
        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);

        return inflatedView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        setHasOptionsMenu(true);
        mActivity = getActivity();

    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }



    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        double latitude_fromStation = 0;//this.getArguments().getDouble("latitude", 0);
        double longitude_fromStation = 0;//this.getArguments().getDouble("longitude", 0);

        if(latitude_fromStation==0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GRENOBLE, 12));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_fromStation,longitude_fromStation), 15));
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_map, menu);
    }



    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    private boolean checkPlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                showErrorDialog(status);
            } else {
                Toast.makeText(mActivity, "This device is not supported.",Toast.LENGTH_LONG).show();
                //TODO : Back to previous fragment
            }
            return false;
        }
        return true;
    }

    void showErrorDialog(int code) {
        GooglePlayServicesUtil.getErrorDialog(code, mActivity, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
    }


}
