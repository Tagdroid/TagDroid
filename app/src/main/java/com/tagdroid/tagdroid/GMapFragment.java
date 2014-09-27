package com.tagdroid.tagdroid;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMapFragment extends Fragment {
	private Tracker tracker;
	private Activity mActivity;
	static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
	final CharSequence[] items_tram = {"Ligne A","Ligne B","Ligne C","Ligne D","Ligne E"}; 
	final CharSequence[] items_bus = {"Ligne CO","Ligne N1","Ligne N3","Ligne N4","Ligne 1","Ligne 11","Ligne 13","Ligne 16","Ligne 21","Ligne 23","Ligne 26","Ligne 30","Ligne 31","Ligne 32","Ligne 33",
									"Ligne 34","Ligne 41","Ligne 43","Ligne 51","Ligne 54","Ligne 55","Ligne 56","Ligne 58"};
	int selected_map = 0;
	
	final boolean[] selected_tram = new boolean[items_tram.length];  
	final boolean[] selected_bus = new boolean[items_bus.length];  
	
	public static GMapFragment newInstance(Double latitude, Double longitude) {
		GMapFragment f = new GMapFragment();
		Bundle b = new Bundle();	
		b.putDouble("latitude", latitude);
		b.putDouble("longitude", longitude);
		f.setArguments(b);
		return f;
	}
	

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    private static final LatLng GRENOBLE = new LatLng(45.1869,5.72639);
	LatLng latlong;

	
	class CustomInfoWindowAdapter implements InfoWindowAdapter {
    	private final View mContents;
    	
	    CustomInfoWindowAdapter() {
	        mContents = mActivity.getLayoutInflater().inflate(R.layout.custom_info_contents, null);
	    }
	    
	    public View getInfoContents(Marker marker) {
	        render(marker, mContents);
	        return mContents;
	    }
	    
	    private void render(Marker marker, View view) {
	        String title = marker.getTitle();
	        Log.e("123", title);
	        latlong =marker.getPosition();
	        TextView titleUi = ((TextView) view.findViewById(R.id.title));
	        titleUi.setText(title);

	        String snippet0[] = marker.getSnippet().split("\n");   
	        String snippet = snippet0[0];
	        Log.e("456", snippet);
	        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
	        snippetUi.setText(snippet);
	        
	        int image_ligne = getResources().getIdentifier(snippet0[0].toLowerCase().replace(" ", "")+"_default", "drawable",  mActivity.getPackageName());
	        ((ImageView) view.findViewById(R.id.badge)).setImageResource(image_ligne);
	    }

		@Override
		public View getInfoWindow(Marker arg0) {
			return null;
		}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.map, container, false);

        MapsInitializer.initialize(mActivity);

        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);
        
        StationsFragment.ligne="map";
        for(int i=0; i<5;i++) selected_tram[i]=true;
        
        MainActivity.mTitle = mActivity.getResources().getString(R.string.map);
        mActivity.getActionBar().setTitle(MainActivity.mTitle);
        checkPlayServices(); 
        return inflatedView;
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
		setHasOptionsMenu(true);
		mActivity = getActivity();
        this.tracker = EasyTracker.getInstance(this.getActivity());

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
    	 
        double latitude_fromStation = this.getArguments().getDouble("latitude", 0);
        double longitude_fromStation = this.getArguments().getDouble("longitude", 0);
        
        if(latitude_fromStation==0){
        	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GRENOBLE, 12));
        }else{
        	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_fromStation,longitude_fromStation), 15));
        	mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    	
    	addMarkersToMap();
    	
    	mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    	
        mMap.setOnMarkerClickListener(new OnMarkerClickListener(){
        	 public boolean onMarkerClick(final Marker marker) {
        		 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12));
				return false;
             }
        });
        
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
			public void onInfoWindowClick(Marker marker) {
				String snippet[] = marker.getSnippet().split("\n");
				MainActivity.titre1 = marker.getTitle();
	    		if(!(snippet[0].equals("Ligne E"))) MainActivity.id_station1 = "id="+snippet[1];
	    		else MainActivity.id_station1 = "id=0";
	    		MainActivity.ligne2 = snippet[0];
	    		MainActivity.latitude1= latlong.latitude+"";
	    		MainActivity.longitude1= latlong.longitude+"";	    			
				MainActivity.TITLES = new String[] { "STATIONDETAIL" };	
		        startActivity(new Intent(mActivity, MainActivity.class));
		        mActivity.finish();
			}  		
        }); 
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	menu.clear();
    	inflater.inflate(R.menu.menu_map, menu);
    	inflater.inflate(R.menu.menu_menu, menu);	
    }
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
    	
        switch (item.getItemId()) {   
    		case R.id.menu_tram: 			
 			   	DialogFragmentTRAM newFragment1 = new DialogFragmentTRAM();
 			   	newFragment1.show(ft, "dialog");
    			return true;
    		case R.id.menu_bus:  
    			DialogFragmentBUS newFragment2 = new DialogFragmentBUS();
 			   	newFragment2.show(ft, "dialog");
    			return true;
    		case R.id.menu_others:  
 			   	DialogFragmentOTHER newFragment3 = new DialogFragmentOTHER();
 			   	newFragment3.show(ft, "dialog");
    			return true;
    		case R.id.menu_map:  
    			   DialogFragmentMAP newFragment4 = new DialogFragmentMAP();
    		       newFragment4.show(ft, "dialog");
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }

    
	
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();    
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send( MapBuilder.createAppView().build() );
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

    
    private void Ligne(String ligne, int debut, int fin){
  		for(int i=debut;i<fin+1;i++){	
  			int res = getResources().getIdentifier("ID"+i, "array", mActivity.getPackageName());  
  			String[] station = this.getResources().getStringArray(res);
  			String icon=ligne.toLowerCase().replace("ligne ", "station_");
  			int resID = getResources().getIdentifier(icon, "drawable", mActivity.getPackageName());
  			
  	        double latitude = Double.valueOf(station[2]).doubleValue();
  	        double longitude = Double.valueOf(station[3]).doubleValue();	
  	        
  	        LatLng point = new LatLng(latitude,longitude);
  	        mMap.addMarker(new MarkerOptions().position(point)
  	        								  .title(station[1])
  	        								  .snippet(ligne+"\n"+station[0])
  	        								  .icon(BitmapDescriptorFactory.fromResource(resID)));
  		}				
  	}
        
    private void addMarkersToMap() {
    	Ligne("Ligne E",4000,4017);
    	Ligne("Ligne D",68,73);
    	Ligne("Ligne C",49,67);
    	Ligne("Ligne B",29,48);
    	Ligne("Ligne A",0,28);	
    }
    
    private boolean checkPlayServices() {
    	  int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
    	  if (status != ConnectionResult.SUCCESS) {
    	    if (GooglePlayServicesUtil.isUserRecoverableError(status)) showErrorDialog(status);
    	    else Toast.makeText(mActivity, "This device is not supported.", Toast.LENGTH_LONG).show();  	    
    	    return false;
    	  }
    	  return true;  	  
    } 
	 
    void showErrorDialog(int code) {
    	  GooglePlayServicesUtil.getErrorDialog(code, mActivity, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
    }
    
    class DialogFragmentTRAM extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        	
        	
            AlertDialog.Builder builder_tram = new AlertDialog.Builder(getActivity());
            builder_tram.setTitle(R.string.tramlines);
            builder_tram.setMultiChoiceItems(items_tram, selected_tram, new DialogInterface.OnMultiChoiceClickListener() {
        		public void onClick(DialogInterface dialog, int which, boolean isChecked) {	
        			mMap.clear();
        			if (selected_tram[0])Ligne("Ligne A",0,28);
        			if (selected_tram[1])Ligne("Ligne B",29,48);
        			if (selected_tram[2])Ligne("Ligne C",49,67);
        			if (selected_tram[3])Ligne("Ligne D",68,73);	
        			if (selected_tram[4])Ligne("Ligne E",4000,4017);	
        			if (selected_bus[0])Ligne("Ligne CO", 720, 736);
        			if (selected_bus[1])Ligne("Ligne N1", 638, 666);
        			if (selected_bus[2])Ligne("Ligne N3", 667, 690);
        			if (selected_bus[3])Ligne("Ligne N4", 691, 712);
        			if (selected_bus[4])Ligne("Ligne 1", 1085, 1142);
        			if (selected_bus[5])Ligne("Ligne 11", 2141, 2176);	
        			if (selected_bus[6])Ligne("Ligne 13", 177, 208);
        			if (selected_bus[7])Ligne("Ligne 16", 211, 256);
        			if (selected_bus[8])Ligne("Ligne 21", 257, 274);
        			if (selected_bus[9])Ligne("Ligne 23", 1275, 1301);
        			if (selected_bus[10])Ligne("Ligne 26", 301, 340);
        			if (selected_bus[11])Ligne("Ligne 30", 3341, 3366);
        			if (selected_bus[12])Ligne("Ligne 31", 367, 401);
        			if (selected_bus[13])Ligne("Ligne 32", 402, 430);
        			if (selected_bus[14])Ligne("Ligne 33", 431, 446);
        			if (selected_bus[15])Ligne("Ligne 34", 448, 484);
        			if (selected_bus[16])Ligne("Ligne 41", 1483, 1511);
        			if (selected_bus[17])Ligne("Ligne 43", 513, 522);
        			if (selected_bus[18])Ligne("Ligne 51", 523, 551);
        			if (selected_bus[19])Ligne("Ligne 54", 1552, 1568);
        			if (selected_bus[20])Ligne("Ligne 55", 564, 584);
        			if (selected_bus[21])Ligne("Ligne 56", 588, 613);
        			if (selected_bus[22])Ligne("Ligne 58", 614, 637);
        		}                   
        	});
            return builder_tram.create();
        }
    }
    
    class DialogFragmentBUS extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder_bus = new AlertDialog.Builder(getActivity());
            builder_bus.setTitle(R.string.buslines);
            builder_bus.setMultiChoiceItems(items_bus, selected_bus, new DialogInterface.OnMultiChoiceClickListener() {
        		public void onClick(DialogInterface dialog, int which, boolean isChecked) {	 
        			mMap.clear();
        			if (selected_tram[0])Ligne("Ligne A",0,28);
        			if (selected_tram[1])Ligne("Ligne B",29,48);
        			if (selected_tram[2])Ligne("Ligne C",49,67);
        			if (selected_tram[3])Ligne("Ligne D",68,73);
        			if (selected_tram[4])Ligne("Ligne E",4000,4017);
        			if (selected_bus[0])Ligne("Ligne CO", 720, 736);
        			if (selected_bus[1])Ligne("Ligne N1", 638, 666);
        			if (selected_bus[2])Ligne("Ligne N3", 667, 690);
        			if (selected_bus[3])Ligne("Ligne N4", 691, 712);
        			if (selected_bus[4])Ligne("Ligne 1", 1085, 1142);
        			if (selected_bus[5])Ligne("Ligne 11", 2141, 2176);
        			if (selected_bus[6])Ligne("Ligne 13", 177, 208);
        			if (selected_bus[7])Ligne("Ligne 16", 211, 256);
        			if (selected_bus[8])Ligne("Ligne 21", 257, 274);
        			if (selected_bus[9])Ligne("Ligne 23", 1275, 1301);
        			if (selected_bus[10])Ligne("Ligne 26", 301, 340);
        			if (selected_bus[11])Ligne("Ligne 30", 3341, 3366);
        			if (selected_bus[12])Ligne("Ligne 31", 367, 401);
        			if (selected_bus[13])Ligne("Ligne 32", 402, 430);
        			if (selected_bus[14])Ligne("Ligne 33", 431, 446);
        			if (selected_bus[15])Ligne("Ligne 34", 448, 484);
        			if (selected_bus[16])Ligne("Ligne 41", 1483, 1511);
        			if (selected_bus[17])Ligne("Ligne 43", 513, 522);
        			if (selected_bus[18])Ligne("Ligne 51", 523, 551);
        			if (selected_bus[19])Ligne("Ligne 54", 1552, 1568);
        			if (selected_bus[20])Ligne("Ligne 55", 564, 584);
        			if (selected_bus[21])Ligne("Ligne 56", 588, 613);
        			if (selected_bus[22])Ligne("Ligne 58", 614, 637);
        		}                   
        	});
            return builder_bus.create();
        }
    }
    
    class DialogFragmentOTHER extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) { 	
            AlertDialog.Builder builder_others = new AlertDialog.Builder(getActivity());
            builder_others.setTitle(R.string.places);
            builder_others.setMessage(R.string.soon);
            return builder_others.create();
        }
    }

    class DialogFragmentMAP extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        	final CharSequence[] items_map = {"Plan","Satellite","Terrain"}; 
        	
            AlertDialog.Builder builder_map = new AlertDialog.Builder(getActivity());
            builder_map.setTitle(R.string.mapstype);
            builder_map.setSingleChoiceItems(items_map, selected_map, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				if (which==0)mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    				if (which==1)mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    				if (which==2)mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    				selected_map=which;
    				dialog.dismiss();
    			}
    		});
            return builder_map.create();
        }
    }
}