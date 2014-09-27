package com.tagdroid.tagdroid;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavorisFragment extends Fragment {
	private Tracker tracker;
	private ListView ListViewFavoris;
	private static View view;
	private Bundle mBundle;

	public static FavorisFragment newInstance() {
		FavorisFragment f = new FavorisFragment();
		return f;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (view != null) {
		    ViewGroup parent = (ViewGroup) view.getParent();
		    if (parent != null) parent.removeView(view);
		}
		    try {
		    	view = inflater.inflate(R.layout.favorislist, container, false);	
		    } catch (InflateException e) {
		    }
		  			
			MainActivity.mTitle = getActivity().getResources().getString(R.string.favoris);
			getActivity().getActionBar().setTitle(MainActivity.mTitle);
			
			
			
		return view;
	}
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mBundle = savedInstanceState;
	        setRetainInstance(true);
			setHasOptionsMenu(true);
	        this.tracker = EasyTracker.getInstance(this.getActivity());

	    }
	
	  @Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    	super.onCreateOptionsMenu(menu, inflater);
	    	menu.clear();
	    	inflater.inflate(R.menu.menu_menu, menu);
	    	
	    }  
	  
	public void onResume(){
	    super.onResume();
	    
	    this.tracker.set(Fields.SCREEN_NAME, getClass().getSimpleName());
	    this.tracker.send( MapBuilder.createAppView().build() );
	    
    
		ListViewFavoris = (ListView) view.findViewById(R.id.listViewfavoris2);
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();	    
		
		MainActivity.prefs2 = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);	
		Map<String, ?> items = MainActivity.prefs2.getAll();
		
		for(String titre : items.keySet()){
		    HashMap<String,String> temp = new HashMap<String,String>();
		    
		    if(titre.contains("_"));	    
		    else{			  			    	
		    	temp.put("titre", titre);
		    	String titre_court[] = titre.split("&");
		    	temp.put("titre_court", titre_court[0]);
		    	temp.put("ligne1", MainActivity.prefs2.getString(titre+"_ligne", "ligneX"));
		    	temp.put("latitude", MainActivity.prefs2.getString(titre+"_latitude", "0"));
		    	temp.put("longitude", MainActivity.prefs2.getString(titre+"_longitude", "0"));
		    				  
		    	int lignecolor = getResources().getIdentifier(MainActivity.prefs2.getString(titre+"_ligne", "ligneX").toLowerCase().replaceAll(" ", ""), "color",getActivity().getPackageName());
    	        temp.put("color", String.valueOf(lignecolor));
		    	
		    	listItem.add(temp);    	
		    }
			    
		}
			
		SimpleAdapter mSchedule = new SimpleAdapter (getActivity(), listItem, R.layout.affichageitemgps,
	               new String[] {"titre_court" , null, "ligne1", "color"}, new int[] {R.id.titre, R.id.distance, R.id.station, R.id.fond_color});

	    ListViewFavoris.setAdapter(mSchedule);
	   	    
	    
	    if(ListViewFavoris.getCount()==0){
	    	TextView text_fav = (TextView) view.findViewById(R.id.text_fav);
	    	Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");	
	    	text_fav.setTypeface(light);
	    	text_fav.setText(getResources().getString(R.string.nofavs));
	    	ImageView img_fav = (ImageView) view.findViewById(R.id.img_fav);
	    	img_fav.setBackgroundResource(R.drawable.favoris2);
	    }
	    
	    
	    ListViewFavoris.setOnItemClickListener(new OnItemClickListener(){  	
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {   		
				HashMap<String, String> map = (HashMap<String, String>) ListViewFavoris.getItemAtPosition(position);
        		MainActivity.titre1 = map.get("titre_court");
        		MainActivity.id_station1 = MainActivity.prefs2.getString(map.get("titre")+"_id_station", "null");
        		MainActivity.ligne2 = MainActivity.prefs2.getString(map.get("titre")+"_ligne", "ligneX");
        		MainActivity.latitude1= MainActivity.prefs2.getString(map.get("titre")+"_latitude", "null");	
        		MainActivity.longitude1= MainActivity.prefs2.getString(map.get("titre")+"_longitude", "null");				
    			MainActivity.TITLES = new String[] { "STATIONDETAIL" };
    			MainActivity.adapter.notifyDataSetChanged();  	
             }
         });  
	}

}

    
