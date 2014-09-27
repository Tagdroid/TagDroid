package com.tagdroid.tagdroid.Pager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tagdroid.tagdroid.MainActivity;
import com.tagdroid.tagdroid.R;
import com.tagdroid.tagdroid.WelcomeActivity;

public final class MyFragment extends Fragment {
	private String mContent = "???";
	public static final String PREFS_NAME_2 = "Welcome";
	protected static SharedPreferences prefs;
		
    public static MyFragment newInstance(String content) {
        MyFragment fragment = new MyFragment();        
        StringBuilder builder = new StringBuilder();
        builder.append(content).append(" ");
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();
        
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {   	
    	int layout = getResources().getIdentifier("welcome_"+mContent, "layout",  WelcomeActivity.PACKAGE_NAME);   	
    	View view = inflater.inflate(layout, container,false); 
    	
    	
    	Typeface light = Typeface.createFromAsset(WelcomeActivity.ASSETS, "fonts/Roboto-Light.ttf");	
    	 if(mContent.contains("1")){ 		
     		TextView bienvenue = (TextView) view.findViewById(R.id.welcome_bienvenue);
     		TextView welcome_text1 = (TextView) view.findViewById(R.id.welcome_text1);
     		TextView welcome_text2 = (TextView) view.findViewById(R.id.welcome_text2);
     		TextView welcome_text3 = (TextView) view.findViewById(R.id.welcome_text3);
     		welcome_text1.setTypeface(light);
     		welcome_text2.setTypeface(light);
     		welcome_text3.setTypeface(light);
     		bienvenue.setTypeface(light); 		
     	}if(mContent.contains("2")){
      		TextView navigation = (TextView) view.findViewById(R.id.widget_station);
      		TextView welcome_text5 = (TextView) view.findViewById(R.id.widget_prochain);
      		welcome_text5.setTypeface(light);
      		navigation.setTypeface(light); 		
      	}if(mContent.contains("3")){
       		TextView favoris = (TextView) view.findViewById(R.id.welcome_favoris);
       		TextView welcome_text6 = (TextView) view.findViewById(R.id.welcome_text6);
      		TextView welcome_text7 = (TextView) view.findViewById(R.id.welcome_text7);
      		welcome_text6.setTypeface(light);
      		welcome_text7.setTypeface(light);
       		favoris.setTypeface(light); 		
       	}if(mContent.contains("4")){
       		TextView proximite = (TextView) view.findViewById(R.id.welcome_proximite);
       		TextView welcome_text8 = (TextView) view.findViewById(R.id.welcome_text8);
      		TextView welcome_text9 = (TextView) view.findViewById(R.id.welcome_text9);
      		welcome_text8.setTypeface(light);
      		welcome_text9.setTypeface(light);
       		proximite.setTypeface(light); 		
       	}if(mContent.contains("5")){
      		TextView pdf = (TextView) view.findViewById(R.id.welcome_pdf);
      		TextView welcome_text10 = (TextView) view.findViewById(R.id.welcome_text10);
      		welcome_text10.setTypeface(light);
      		pdf.setTypeface(light); 		
      	}if(mContent.contains("6")){
      		TextView welcome_text12 = (TextView) view.findViewById(R.id.welcome_text12);
      		TextView welcome_text13 = (TextView) view.findViewById(R.id.welcome_text13);
      		TextView welcome_text14 = (TextView) view.findViewById(R.id.welcome_text14);
      		welcome_text12.setTypeface(light);
      		welcome_text13.setTypeface(light);
      		welcome_text14.setTypeface(light);
      		
      		ImageButton btn_go_welcome = (ImageButton)view.findViewById(R.id.btn_go_welcome);
      		btn_go_welcome.setOnClickListener(new View.OnClickListener() {
    	        public void onClick(View v) {
    	        	prefs = getActivity().getSharedPreferences(PREFS_NAME_2, getActivity().MODE_WORLD_READABLE);
    	        	SharedPreferences.Editor editor = prefs.edit();
    	        	editor.putBoolean("skipWelcome", true);
    	        	editor.commit();
    	        	Intent intent = new Intent(getActivity(),MainActivity.class);
    				startActivity(intent);
    				getActivity().finish();
    	        }
    	    });
      	}
      	return view;			
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
