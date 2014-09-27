package com.tagdroid.tagdroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import java.io.InputStream;

public class RSS_DetailsFragment extends Fragment {
	private Tracker tracker;
	private static View view;
	private Bundle mBundle;
	
	public static RSS_DetailsFragment newInstance(String flux, String title, String description, String link, String intent) {
		RSS_DetailsFragment f = new RSS_DetailsFragment();
		Bundle b = new Bundle();	
		b.putString("flux", flux);
		b.putString("title", title);  			
		b.putString("description", description);
		b.putString("link", link);
		b.putString("intent", intent);
		f.setArguments(b);
		return f;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (view != null) {
		    ViewGroup parent = (ViewGroup) view.getParent();
		    if (parent != null) parent.removeView(view);
		}
		    try {
		    	view = inflater.inflate(R.layout.showrss, container, false);	
		    } catch (InflateException e) {
		    }
		    
		    String myFeed2, link=null;
 
		    MainActivity.mTitle = this.getArguments().getString("title");
		    getActivity().getActionBar().setTitle(MainActivity.mTitle);
		    
	        TextView db2= (TextView) view.findViewById(R.id.storybox2);
	        ImageView iv1 = (ImageView) view.findViewById(R.id.iv1);
	        Button btn_more = (Button) view.findViewById(R.id.more);
      	
	        	if(this.getArguments().getString("intent").equals("actu")){
	        		if(this.getArguments().getString("flux").contains("tag")){
	        			if(this.getArguments().getString("description") !=null){       		
			        		String[] description1 = this.getArguments().getString("description").split(">");
			        		if(description1[1] != null){
			        			link = this.getArguments().getString("link").replace("evenement", "TPL_CODE/TPL_EVENEMENTMOBILE/PAR_TPL_IDENTIFIANT")
										   .replace("www", "m")
										   .replace("3-en-detail","226-actualites");
			        			myFeed2 = description1[1];
			        			
			        		}else{
			        			myFeed2 = description1[0];
			        			link = this.getArguments().getString("link");
			        		}  
		
			        		String url_iv;
			        		
			        		if(description1[0].substring(10).contains("jpg")){
			        			String[] url_image = description1[0].substring(10).replace("IMF_VIGNETTEALAUNE", "IMF_LARGE").split("jpg");	
			        			url_iv =  "http://www.tag.fr"+url_image[0]+"jpg";
			        		}else{
			        			String[] url_image = (description1[0].substring(10).replace("IMF_VIGNETTEALAUNE", "IMF_LARGE").split("png"));
			        			url_iv = "http://www.tag.fr"+url_image[0]+"png";
			        		}	
			        		        		
			        		new DownloadImageTask(iv1).execute(url_iv);	        		
			    	        
			        	}else{	        		
			        		myFeed2 = "Aucune information trouvée";
			        	}
	        		}
	        		else{
	        			String[] description2 = new String[2];
						description2 = this.getArguments().getString("description").substring(142).split("\" width");
	        			myFeed2 = description2[1].substring(177).replace("</div>", "");
	        			link = this.getArguments().getString("link");
	        			new DownloadImageTask(iv1).execute(description2[0]);
	        		}
		        	
	        	}else{	        			      	        		
	        		link = this.getArguments().getString("link").replace("info_trafic", "TPL_CODE/TPL_INFOTRAFICFICHEMOBILE/PAR_TPL_IDENTIFIANT")
							  .replace("www", "m")
							  .replace("89","227");
	        		
	        		myFeed2 = this.getArguments().getString("description");
	        	}		        

	        final String url = link;
	        btn_more.setOnClickListener(new View.OnClickListener() {			
				public void onClick(View view) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				}
			});
	        
	        db2.setText(Html.fromHtml(myFeed2));
			
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
		setHasOptionsMenu(true);
        this.tracker = EasyTracker.getInstance(this.getActivity());

	    }
	
	@Override
    public void onResume() {
        super.onResume();
        this.tracker.set(Fields.SCREEN_NAME, getClass().getSimpleName());
        this.tracker.send( MapBuilder.createAppView().build() );
    }
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	menu.clear();
    	inflater.inflate(R.menu.menu_menu, menu);
    } 
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }

		  protected Bitmap doInBackground(String... urls) {
		      String urldisplay = urls[0];
		      Bitmap mIcon11 = null;
		      try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        mIcon11 = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		      }
		      return mIcon11;
		  }

		  protected void onPostExecute(Bitmap result) {
		      bmImage.setImageBitmap(result);
		  }
		}
}
