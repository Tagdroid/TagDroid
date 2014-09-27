package com.tagdroid.tagdroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tagdroid.tagdroid.Pager.MyPagerAdapter;

import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class WelcomeActivity extends FragmentActivity {
	
	protected MyPagerAdapter mAdapter;
    protected ViewPager mPager;
    protected PageIndicator mIndicator;
    
    public static String PACKAGE_NAME;
    public static AssetManager ASSETS;
    
    public static final String PREFS_NAME_2 = "Welcome";
	protected static SharedPreferences prefs;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);


			getActionBar().setIcon(R.drawable.tag);
			getActionBar().setTitle("");
		
		
		   
		
		prefs = getSharedPreferences(PREFS_NAME_2, 0);
			
		PACKAGE_NAME = getApplicationContext().getPackageName();
		ASSETS = getAssets();
	
		mAdapter = new MyPagerAdapter(getSupportFragmentManager());		
		
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        
        LinePageIndicator indicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setSelectedColor(0xff00b4f8);
        indicator.setUnselectedColor(0x44888888);
        indicator.setStrokeWidth(5 * density);
        indicator.setLineWidth(40 * density);    
 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_welcome, menu);
	    return true;
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {  
		 switch (item.getItemId()) {
		 	case R.id.menu_skip:
		 		SharedPreferences.Editor editor = prefs.edit();
	        	editor.putBoolean("skipWelcome", true);
	        	editor.commit();
	        	Intent intent = new Intent(WelcomeActivity.this, LoadDBActivity.class);
				startActivity(intent);
	        	finish();
		 	return true;
		 }  		 
	      return super.onOptionsItemSelected(item);
	    }
	
	
}