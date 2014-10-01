package com.tagdroid.tagdroid;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class AboutFragment extends Fragment {
	private Tracker tracker;
	
	public static AboutFragment newInstance() {
        return new AboutFragment();
    }
	
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		this.tracker = EasyTracker.getInstance(this.getActivity());
	}
	
	@Override
    public void onResume() {
        super.onResume();
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send( MapBuilder.createAppView().build() );
    }
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	menu.clear();
    	inflater.inflate(R.menu.menu_menu, menu);
    } 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about, container, false);		
	
		MainActivity.mTitle = getResources().getString(R.string.about);
		getActivity().getActionBar().setTitle(MainActivity.mTitle);
		
		Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");	
		
	    TextView textview4 = (TextView) view.findViewById(R.id.textView4);
	    TextView textview5 = (TextView) view.findViewById(R.id.textView5);
	    textview4.setTypeface(light); 
	    textview5.setTypeface(light);
	    
	    TextView description_about = (TextView) view.findViewById(R.id.description_about);
	    description_about.setTypeface(light);
	    TextView quentin = (TextView) view.findViewById(R.id.quentin);
	    quentin.setTypeface(light);
	    TextView alexandre = (TextView) view.findViewById(R.id.alexandre);
	    alexandre.setTypeface(light);
	    
        final ImageButton mail = (ImageButton) view.findViewById(R.id.logo_mail);
	    mail.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(Intent.ACTION_SEND);
	        	intent.setType("plain/text");
	        	intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "tagdroid.grenoble@gmail.com"});
	        	startActivity(Intent.createChooser(intent, getResources().getString(R.string.mail)));
	        }
	    });
         
	    final Button showlog = (Button) view.findViewById(R.id.showlog);
	    final ChangeLog cl = new ChangeLog(getActivity());
	    showlog.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {	
	        	cl.getFullLogDialog().show();
	        }
	    });
	    
	    PackageInfo pinfo;
		try {
			pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			showlog.setText("Version "+pinfo.versionName);
			showlog.setTypeface(light);
		}catch (NameNotFoundException e) {
		}
		return view;
	}
}
    