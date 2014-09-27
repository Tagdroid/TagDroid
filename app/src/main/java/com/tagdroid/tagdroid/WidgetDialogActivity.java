package com.tagdroid.tagdroid;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WidgetDialogActivity extends Activity {
	
	private ListView ListViewFavoris;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.widgetdialog_activity);
    
    ListViewFavoris = (ListView) findViewById(R.id.listViewfavoris2);
	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();	    
	
	MainActivity.prefs2 = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);	
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
	    				  
	    	int lignecolor = getResources().getIdentifier(MainActivity.prefs2.getString(titre+"_ligne", "ligneX").toLowerCase().replaceAll(" ", ""), "color",getPackageName());
	        temp.put("color", String.valueOf(lignecolor));
	    	
	    	listItem.add(temp);    	
	    }
		    
	}
		
	SimpleAdapter mSchedule = new SimpleAdapter (this, listItem, R.layout.affichageitemgps,
               new String[] {"titre_court" , null, "ligne1", "color"}, new int[] {R.id.titre, R.id.distance, R.id.station, R.id.fond_color});

    ListViewFavoris.setAdapter(mSchedule);
   	    
    
    if(ListViewFavoris.getCount()==0){
    	TextView text_fav = (TextView) findViewById(R.id.text_fav);
    	Typeface light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");	
    	text_fav.setTypeface(light);
    	text_fav.setText(getResources().getString(R.string.nofavs));
    	ImageView img_fav = (ImageView) findViewById(R.id.img_fav);
    	img_fav.setBackgroundResource(R.drawable.favoris2);
    }
    
    ListViewFavoris.setOnItemClickListener(new OnItemClickListener(){  	
     	public void onItemClick(AdapterView<?> a, View v, int position, long id) {   		
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) ListViewFavoris.getItemAtPosition(position);
    		WidgetActivity.station = map.get("titre_court"); 
    		String ligne = map.get("ligne1");
    		int image_ligne = getResources().getIdentifier(ligne.toLowerCase().replace(" ", "")+"_pressed", "drawable",  getPackageName());       
    		RemoteViews remoteViews = new RemoteViews(getBaseContext().getPackageName(),R.layout.widget);
	        remoteViews.setTextViewText(R.id.widget_station, WidgetActivity.station);
	        remoteViews.setImageViewResource(R.id.widget_ligne, image_ligne);
	        ComponentName widget = new ComponentName(getBaseContext(), WidgetActivity.class);
			AppWidgetManager.getInstance(getBaseContext()).updateAppWidget(widget, remoteViews);
    		finish();	
         }
     });  

  }
}