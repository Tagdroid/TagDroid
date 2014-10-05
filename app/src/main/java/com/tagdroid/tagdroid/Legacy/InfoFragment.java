package com.tagdroid.tagdroid.Legacy;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.tagdroid.tagdroid.R;
import com.tagdroid.tagdroid.SQLite.AlertTAG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InfoFragment extends Fragment {
	private Tracker tracker;
	
	public static InfoFragment newInstance() {
        return new InfoFragment();
	}
	
	private Activity mActivity;

	private static View view;
	private Bundle mBundle;
	
	AlertTAG database;
	ArrayList arrayList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (view != null) {
		    ViewGroup parent = (ViewGroup) view.getParent();
		    if (parent != null) parent.removeView(view);
		}
		
	    try {
	    	view = inflater.inflate(R.layout.info, container, false);
	    }catch (InflateException e) {}
		    

		MainActivity.mTitle = getActivity().getResources().getString(R.string.trafic);
		getActivity().getActionBar().setTitle(MainActivity.mTitle);
		

		database=new AlertTAG(mActivity);
		database.getWritableDatabase();

		if(isNetworkAvailable()){
			/*Parsing des datas au format JSON et fabrication de la bdd en SQLITE*/
			Get_DB_alert database_async_alert=new Get_DB_alert();
			database_async_alert.execute();
		}	
		return view;
	}
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
		setHasOptionsMenu(true);
		mActivity = getActivity();
        this.tracker = EasyTracker.getInstance(this.getActivity());

    }

  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	menu.clear();
    	inflater.inflate(R.menu.menu_menu, menu);
    } 
  
  @Override
  public void onResume() {
      super.onResume();
      this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
      this.tracker.send( MapBuilder.createAppView().build() );
  }	
  
  
  private class Get_DB_alert extends AsyncTask<Void, Void, String>{	
		protected void onPreExecute() {				
			super.onPreExecute();
		}
		
		@SuppressWarnings("unused")
		protected void onProgressUpdate(){
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			String json="";			
			try {
				  URL url = new URL("http://preprod.transinfoservice.ws.cityway.fr/TAG/api/disruption/v1/GetActualAndFutureDisruptions/json?key=TAGDEV");
				  HttpURLConnection con = (HttpURLConnection) url.openConnection();	  					
				  BufferedReader reader = null;
				  try {
				    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				    String line = "";
				    while ((line = reader.readLine()) != null) {
				      json=line;
				    }
				  } catch (IOException e) {
				    e.printStackTrace();
				  } finally {
				    if (reader != null) {
				      try {
				        reader.close();
				      } catch (IOException e) {
				        e.printStackTrace();
				        }
				    }
				  }
			}catch (Exception e) {
				  e.printStackTrace();
			}
			return json;	
		}
		
		protected void onPostExecute(String result){		
			  try
		        {
		          JSONObject jsonObject=new JSONObject(result);
		          JSONArray jsonArray= jsonObject.getJSONArray("Data");
		          String Message = jsonObject.getString("Message");
		          String StatusCode = jsonObject.getString("StatusCode");
		          String Direction = "";
		          String LineId = "";
		          String ServiceLevel = "";
		          for(int i=0;i<jsonArray.length();i++){
		              JSONObject jsonObject1=jsonArray.getJSONObject(i);
		              String BeginValidityDateString= jsonObject1.getString("BeginValidityDateString");
		              String Cause= jsonObject1.getString("Cause");
		              String CreateDateString= jsonObject1.getString("CreateDateString");
		              String Description= jsonObject1.getString("Description");
		              String EndValidityDateString= jsonObject1.getString("EndValidityDateString");
		              String Id= jsonObject1.getString("Id");
		              String Latitude= jsonObject1.getString("Latitude");
		              String Longitude= jsonObject1.getString("Longitude");
		              String Name= jsonObject1.getString("Name");
		              String Source= jsonObject1.getString("Source");
		              JSONArray jsonArray1= jsonObject1.getJSONArray("DisruptedLines");		          
		              JSONArray jsonArray2= jsonObject1.getJSONArray("DisruptedStopPoints");	              
		              JSONObject jsonObject2=jsonObject1.getJSONObject("DisruptionType");
		              String Code= jsonObject2.getString("Code");
		              String Id2= jsonObject2.getString("Id");
		              String Name2= jsonObject2.getString("Name");
		              Log.e("LENGHT",jsonArray1.length()+"");
		              for(int j=0;j<jsonArray1.length();j++){
		            	  //JSONObject jsonObject3=jsonArray.getJSONObject(j);
		            	  Direction="dir"+j;//jsonObject3.getString("Direction").toString();
		            	  LineId="line"+j;//jsonObject3.getString("LineId").toString();
		            	  ServiceLevel="serv"+j;//jsonObject3.getString("ServiceLevel").toString();
		            	  Log.e("FIELD", Id+Source+CreateDateString+Cause+BeginValidityDateString+EndValidityDateString+Code+Id2+Name2+Name+Description+LineId+Direction+ServiceLevel+Latitude+Longitude);
		            	  database.insertData(Id,Source,CreateDateString,Cause,BeginValidityDateString,EndValidityDateString,Code,Id2,Name2,Name,Description,LineId,Direction,ServiceLevel,Latitude,Longitude);
			          }		              
		        }
		    }
		    catch(JSONException e){e.printStackTrace();}
			
			//arrayList=database.fetchData();
		}
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) 
	    mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) return true;
	    return false;
	} 
}
  
 

