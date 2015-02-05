package com.tagdroid.android.Legacy;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.tagdroid.android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxFragment extends Fragment implements LocationListener{
	private ListView ListViewProx;
	protected LocationManager lm = null;
	private TextView myadress;
	private static View view;
	private Bundle mBundle;

	
	public static ProxFragment newInstance() {
        return new ProxFragment();
	}
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (view != null) {
		    ViewGroup parent = (ViewGroup) view.getParent();
		    if (parent != null) parent.removeView(view);
		}
		    try {
		    	view = inflater.inflate(R.layout.proxlist, container, false);
		    } catch (InflateException e) {
		    }
		    
		    myadress = (TextView) view.findViewById(R.id.myadress);    
		    ListViewProx = (ListView) view.findViewById(R.id.listViewfavoris2);	
			getLastLocation();

		return view;
	}
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mBundle = savedInstanceState;
			setHasOptionsMenu(true);


	    }
	  
	public void getLastLocation(){
		lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if(lm==null)Log.e("1","lm est null");
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_COARSE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String providerName = lm.getBestProvider(locationCritera, true);

        if (providerName != null && lm.isProviderEnabled(providerName)) {
            lm.requestLocationUpdates(providerName, 20000, 100, this);
          //Recherche Async de la position et affichage
            Double[] lat_long = new Double[] {lm.getLastKnownLocation(getBestProvider()).getLatitude(),
    				  lm.getLastKnownLocation(getBestProvider()).getLongitude()};
            new LoadAddress(getActivity().getBaseContext()).execute(lat_long);

        } else {
            Toast.makeText(getActivity(), R.string.please_turn_on_gps, Toast.LENGTH_LONG).show();
        }
           
            }
	
	public String getBestProvider(){
    	lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    	Criteria criteria = new Criteria();
    	criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
    	criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        return lm.getBestProvider(criteria, true);
    }
	
	public Location convertGpToLoc(LatLng gp){
        Location convertedLocation = new Location("");
	    convertedLocation.setLatitude(gp.latitude / 1e6);
	    convertedLocation.setLongitude(gp.longitude / 1e6);
	    return convertedLocation;
	}
	
	
	public void onResume(){
	    super.onResume();
		Resources res = getResources();

	    
        
        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();
        HashMap<String, String> map;
        double latitude;
    	double longitude;
		LatLng point;
		
		if(getBestProvider()!=null){ 	
			if(lm.getLastKnownLocation(getBestProvider())!=null){
				
				for(int i=0;i<74;i++){
		        	int resID = getResources().getIdentifier("ID"+i, "array",getActivity().getPackageName());      	
		        	String[] station = res.getStringArray(resID);
				
		        	latitude = Double.valueOf(station[2]);
			        longitude = Double.valueOf(station[3]);
					point = new LatLng((int) (latitude*1E6),(int) (longitude*1E6));
			        
					
		        	double distance = (int)lm.getLastKnownLocation(getBestProvider()).distanceTo(convertGpToLoc(point));
		        	           	
	            	map = new HashMap<>();
	                map.put("titre", station[1]);
	                map.put("id_station", "id="+station[0]);
	                map.put("distance", distance+" m");  
	                map.put("latitude", station[2]);
	                map.put("longitude", station[3]);
	                
	                
	    	        if(i<29)map.put("ligne1", "Ligne A");
	    	        else if(i<49&&i>=29)map.put("ligne1", "Ligne B");
	    	        else if(i<68&&i>=49)map.put("ligne1", "Ligne C");
	    	        else map.put("ligne1", "Ligne D");
	    	       
	    	        int lignecolor = getResources().getIdentifier(map.get("ligne1").toLowerCase().replaceAll(" ", ""), "color", getActivity().getPackageName());
	    	        map.put("color", String.valueOf(lignecolor));
	    	        
	    	        listItem.add(map);	    
	    		}

	    		Collections.sort(listItem, new Comparator<Map<String, String>>() {
	    	        public int compare(final Map<String, String> o1, final Map<String, String> o2) {	             
	    	           return Double.valueOf(o1.get("distance").substring(0, o1.get("distance").length()-2)).compareTo(Double.valueOf(o2.get("distance").substring(0, o2.get("distance").length()-2)));
	    	        }
	    	    });
	    		
	            SimpleAdapter mSchedule = new SimpleAdapter (getActivity(), listItem.subList(0, 10),R.layout.legacy_listitem_station,
	                    									new String[] {"titre",  "distance", "ligne1", "color"},
	                    									new int[] { R.id.titre, R.id.distance, R.id.station, R.id.fond_color});
	           
	            ListViewProx.setAdapter(mSchedule);
	                      
	        }else Toast.makeText(getActivity(), "Aucune information de localisation", Toast.LENGTH_LONG).show();    
		}
	
	    
		
        // Aucune Localisation	
		if(ListViewProx.getCount()==0){
	    	TextView text_prox = (TextView) view.findViewById(R.id.text_fav);
	    	text_prox.setText(getResources().getString(R.string.noproxs));
	    	ImageView img_fav = (ImageView) view.findViewById(R.id.img_fav);
	    	img_fav.setBackgroundResource(R.drawable.ic_action_place);
	    }	
		
	    
	/*ListViewProx.setOnItemClickListener(new OnItemClickListener()
        {  	
		@SuppressWarnings("unchecked")
     	public void onItemClick(AdapterView<?> a, View v, int position, long id) {      		
			HashMap<String, String> map = (HashMap<String, String>) ListViewProx.getItemAtPosition(position);
			MainActivityOLD.titre1 = map.get("titre");
    		MainActivityOLD.id_station1 =  map.get("id_station");
    		MainActivityOLD.ligne2 = map.get("ligne1");
    		MainActivityOLD.latitude1=map.get("latitude");
    		MainActivityOLD.longitude1=map.get("longitude");
			MainActivityOLD.TITLES = new String[] { "STATIONDETAIL" };
			MainActivityOLD.adapter.notifyDataSetChanged();
         }
     });*/
	}

	
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private class LoadAddress extends AsyncTask<Double, Void, String> {		
		Context mContext;		
        public LoadAddress(Context context) {
        	super();
            mContext = context;
		}

		@Override
        protected void onPreExecute() {
                super.onPreExecute();
                myadress.setText(R.string.searchaddress);
        }
        

        protected String doInBackground(Double... params) {
            double latitude = params[0];
            double longitude = params[1];
        	String addressText;
    		List<Address> addresses = null;
    		Geocoder geocoder = new Geocoder(mContext);
    		try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
    		if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());
            }
    		else addressText="Aucune adresse trouvée";
    		return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            myadress.setText(addressText);
        }

    }

	
	  @Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    	super.onCreateOptionsMenu(menu, inflater);
	    	menu.clear();
	    	
	    	// TODO inflater.inflate(R.menu.menu_prox, menu);
	    	
	    }  
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {            
        switch (item.getItemId()) {
    		case R.id.menu_addbus:
    			if(item.isChecked()){
					item.setChecked(false);
					item.setIcon(R.drawable.menu_addbus);
					Toast.makeText(getActivity(), "Bus retiré de la liste", Toast.LENGTH_SHORT).show();
			    	//MainActivity.addbus_check=false;
				}
				else{	    		
					item.setChecked(true);
					item.setIcon(R.drawable.menu_addbus_checked);					
					Toast.makeText(getActivity(), "Bus ajouté à la liste", Toast.LENGTH_SHORT).show();
					//MainActivityOLD.addbus_check=true;
				}
	        	return true;

    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
}

