package com.tagdroid.tagdroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.astuetz.pagerslidingtabstrip.PagerSlidingTabStrip;
import com.google.analytics.tracking.android.EasyTracker;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

import java.util.Map;


public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	static String mTitle="Stations";
	private String[] menuItems;
	private NsMenuAdapter mAdapter;
	static boolean favoris_check, addbus_check;
	
	
	public static final String PREFS_NAME = "stations_favoris";
	protected static SharedPreferences prefs2;
	
	static String titre1, id_station1, ligne2, latitude1, longitude1;
	static String flux, title, description, link, intent;
	static double latitude, longitude;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	static MyPagerAdapter adapter;
	
	static String[] TITLES ={"STATIONS"};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    ChangeLog cl = new ChangeLog(this);
	 	if (cl.firstRun()) cl.getLogDialog().show();
	 	
	 	prefs2 = this.getSharedPreferences(PREFS_NAME, 0);
	 	
	 	Parse.initialize(this, "CdJdR3cRkKAcnHHWcxRXzseLYUPBJdkP0bUzVLFW", "zNLrxOANbZZJi1Brh5P7vyjUkZrpsptFJWKwckcl"); 
	 	
	 	PushService.setDefaultPushCallback(this, MainActivity.class);
	 	ParseInstallation.getCurrentInstallation().saveInBackground();
	 	ParseAnalytics.trackAppOpened(getIntent());

	 	Map<String, ?> items = MainActivity.prefs2.getAll();
	 	int i=0;
	 	for(String titre : items.keySet()) if(!(titre.contains("_"))) i++;    
		
	 	
	    if(prefs2.getAll().size()!=0) 
	    	if((prefs2.getAll().size())/i != 6){
	    		prefs2.edit().clear();
	    		prefs2.edit().apply();
	    	}

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		TabChange();	
		
		if (android.os.Build.VERSION.SDK_INT >= 11){
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);	
		}
	
		_initMenu();	
		
	
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawerLayout);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setItemChecked(0, true);	
	}
	
	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}
		
		@Override
		public int getItemPosition(Object object){
		    return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {
			if(TITLES[0].equals("STATIONS")) return StationsFragment.newInstance();
			else if(TITLES[0].equals("LIGNE")) return LigneFragment.newInstance(StationsFragment.ligne,StationsFragment.id_debut,StationsFragment.id_fin);
			else if(TITLES[0].equals("STATIONDETAIL")) return StationDetailFragment.newInstance(titre1,id_station1,ligne2,latitude1,longitude1);
			else if(TITLES[0].equals("FAVORIS")) return FavorisFragment.newInstance();
			else if(TITLES[0].equals("PROXIMITE")) return ProxFragment.newInstance();
			else if(TITLES[0].equals("MAP")) return GMapFragment.newInstance(latitude,longitude);
			else if(TITLES[0].equals("ACTU")) return ActuFragment.newInstance();
			else if(TITLES[0].equals("INFO")) return InfoFragment.newInstance();
			else if(TITLES[0].equals("RSS_DetailsFragment")) return RSS_DetailsFragment.newInstance(flux, title, description, link, intent);
			else if(TITLES[0].equals("TICKETS")&&position==0) return TicketFragment.newInstance();
			else if(TITLES[0].equals("TICKETS")&&position==1) return PassFragment.newInstance();
			else if(TITLES[0].equals("TICKETS")&&position==2) return CombinesFragment.newInstance();
			else if(TITLES[0].equals("ABOUT")) return AboutFragment.newInstance();
			else return StationsFragment.newInstance();		
	    }
		
		
	}
	
	private void _initMenu() {
		mAdapter = new NsMenuAdapter(this);
		mAdapter.addHeader(R.string.ns_menu_main_header);
		menuItems = getResources().getStringArray(R.array.ns_menu_items);
		String[] menuItemsIcon = getResources().getStringArray(R.array.ns_menu_items_icon);
		
		int res = 0;
		for (String item : menuItems) {
		
			int id_title = getResources().getIdentifier(item, "string",this.getPackageName());
			int id_icon = getResources().getIdentifier(menuItemsIcon[res],"drawable", this.getPackageName());
		
			NsMenuItemModel mItem = new NsMenuItemModel(id_title, id_icon);
			
			if (res==1 && prefs2.getAll().size()!=0) mItem.counter= (prefs2.getAll().size()/6); 
			else mItem.counter=0;
			
			mAdapter.addItem(mItem);
			res++;
		}
		
		mAdapter.addHeader(R.string.ns_menu_main_header2);
	
		menuItems = getResources().getStringArray(R.array.ns_menu_items2);
		String[] menuItemsIcon2 = getResources().getStringArray(R.array.ns_menu_items_icon2);
		
		int res2 = 0;
		for (String item : menuItems) {
		
			int id_title = getResources().getIdentifier(item, "string",this.getPackageName());
			int id_icon = getResources().getIdentifier(menuItemsIcon2[res2],"drawable", this.getPackageName());
		
			mAdapter.addItem(new NsMenuItemModel(id_title, id_icon));
			res2++;
		}
		
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
		if (mDrawerList != null)
		mDrawerList.setAdapter(mAdapter);
		
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	 @Override
	protected void onPostCreate(Bundle savedInstanceState) {
		 super.onPostCreate(savedInstanceState);	     
	     mDrawerToggle.syncState();	
	 }
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {     		 
		 if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	          }
	      
	      switch (item.getItemId()) {
	        case R.id.menu_rate:
	        	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tagdroid.android")));
	        	mDrawerLayout.closeDrawer(mDrawerList);
	            return true;
	        case R.id.menu_about:
	        	TITLES = new String[] { "ABOUT" };
	        	adapter.notifyDataSetChanged();
	        	mDrawerLayout.closeDrawer(mDrawerList);	
	        	TabChange();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	      }
	    }
	    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {    
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if(menu.findItem(R.id.menu_rate)!=null)menu.findItem(R.id.menu_rate).setVisible(drawerOpen);
        if(menu.findItem(R.id.menu_about)!=null)menu.findItem(R.id.menu_about).setVisible(drawerOpen);
        if(menu.findItem(R.id.menu_pdf)!=null)menu.findItem(R.id.menu_pdf).setVisible(!drawerOpen);
        if(menu.findItem(R.id.menu_addbus)!=null){
        	menu.findItem(R.id.menu_addbus).setVisible(!drawerOpen);
       	 	if(favoris_check) menu.getItem(0).setIcon(R.drawable.menu_addbus_checked);
            else menu.getItem(0).setIcon(R.drawable.menu_addbus);
        }     	
        if(menu.findItem(R.id.menu_favoris)!=null){
        	 menu.findItem(R.id.menu_favoris).setVisible(!drawerOpen);
        	 if(favoris_check) menu.getItem(0).setIcon(R.drawable.menu_favoris_checked);
             else menu.getItem(0).setIcon(R.drawable.menu_favoris);
        }
        if(menu.findItem(R.id.menu_refresh)!=null)menu.findItem(R.id.menu_refresh).setVisible(!drawerOpen);
        if(menu.findItem(R.id.menu_map)!=null)menu.findItem(R.id.menu_map).setVisible(!drawerOpen);
        if(menu.findItem(R.id.menu_tram)!=null)menu.findItem(R.id.menu_tram).setVisible(!drawerOpen);
        if(menu.findItem(R.id.menu_bus)!=null)menu.findItem(R.id.menu_bus).setVisible(!drawerOpen);
        if(menu.findItem(R.id.menu_others)!=null)menu.findItem(R.id.menu_others).setVisible(!drawerOpen);
        if(menu.findItem(R.id.menu_rss)!=null)menu.findItem(R.id.menu_rss).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);       
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.clear();
		getMenuInflater().inflate(R.menu.menu_menu, menu);		
		return true;
	}
	
	@Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
      }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this); 
     }
  
	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

          public CustomActionBarDrawerToggle(Activity mActivity,DrawerLayout mDrawerLayout){
                super(  mActivity, 
						mDrawerLayout, 
						R.drawable.ic_drawer, 
						R.string.drawer_open,
						R.string.drawer_close);
         }

		//* Called when drawer is closed 
		public void onDrawerClosed(View view) {
		getActionBar().setTitle(mTitle);
		invalidateOptionsMenu();
		}
		
		//* Called when a drawer is opened 
		public void onDrawerOpened(View drawerView) {
		getActionBar().setTitle("TAGdroid");
		invalidateOptionsMenu();
		}	
    }
	  
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
			mDrawerList.setItemChecked(position, true);		
			switch(position){
				case 1:TITLES = new String[] { "STATIONS" }; break;
				case 2:TITLES = new String[] { "FAVORIS" };break;
				case 3:TITLES = new String[] { "PROXIMITE" };break;
				case 4:TITLES = new String[] { "MAP" }; latitude = 0 ; longitude = 0;break;
				case 6:TITLES = new String[] { "INFO" };break;
				case 7:TITLES = new String[] { "ACTU" };break;
				case 8:TITLES = new String[] { "TICKETS", "PASS", "COMBINES" };break;
			}	
			TabChange();
			_initMenu();
			mDrawerList.setItemChecked(position, true);		
			mDrawerList.setAdapter(mAdapter);
			mDrawerLayout.closeDrawer(mDrawerList);}			
		}
					
	public boolean onKeyDown(int keyCode, KeyEvent event) {	   
			  if ((keyCode == KeyEvent.KEYCODE_BACK)&& !(TITLES[0].equals("STATIONS"))){
				  if(TITLES[0].equals("STATIONDETAIL") && StationsFragment.ligne!=null){
					  if(StationsFragment.ligne.equals("map"))TITLES = new String[] { "MAP" };
					  else TITLES = new String[] { "LIGNE" };
				  }		 
				  else TITLES = new String[] { "STATIONS" };
		        	TabChange();
		        	mDrawerList.setAdapter(mAdapter);
		        	_initMenu();	
		            return false; 
		        }return super.onKeyDown(keyCode, event);
	  }
		  
	private void TabChange(){
				if(TITLES.length==1)tabs.setVisibility(View.GONE);
				else tabs.setVisibility(View.VISIBLE);
				
	        	adapter.notifyDataSetChanged();
	        	pager.setAdapter(adapter);			
				tabs.setViewPager(pager);
	}
	  
	
	
	  
	  
}
