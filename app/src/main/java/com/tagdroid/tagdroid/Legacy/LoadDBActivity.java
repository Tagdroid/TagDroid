package com.tagdroid.tagdroid.Legacy;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tagdroid.tagdroid.R;
import com.tagdroid.tagdroid.SQLite.ReseauTAG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoadDBActivity extends Activity {
	
	ReseauTAG database;
	ArrayList arrayList;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loaddb);
		
		TextView loaddb_text = (TextView)findViewById(R.id.loaddb_text);
		
		database=new ReseauTAG(this);
		database.getWritableDatabase();

		//Verifie si database existe et si elle est remplie	
		if (doesDatabaseExist(this,"ReseauTAG.db")){
			Log.e("test","DB OK - Taille : "+ DatabaseSize(this,"ReseauTAG.db"));
			Intent intent = new Intent(LoadDBActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}else{
			if(isNetworkAvailable()){
				/*Parsing des datas au format JSON et fabrication de la bdd en SQLITE*/
				Get_DB database_async=new Get_DB();
				database_async.execute();
			}
			else loaddb_text.setText("Problème de connexion internet");
		}		
	}
	
	private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
	    File dbFile=context.getDatabasePath(dbName);
	    return dbFile.exists();
	}
	private static long DatabaseSize(ContextWrapper context, String dbName) {
	    File dbFile=context.getDatabasePath(dbName);    
	    return dbFile.length();
	}
	
	private class Get_DB extends AsyncTask<Void, Void, String>{	
		protected void onPreExecute() {				
			super.onPreExecute();
		}
		
		@SuppressWarnings("unused")
		protected void onProgressUpdate(){
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			String id_jour="";			
			try {
				  URL url = new URL("http://preprod.transinfoservice.ws.cityway.fr/TAG/api/transport/v2/GetLogicalStops/json?key=TAGDEV");
				  HttpURLConnection con = (HttpURLConnection) url.openConnection();	  					
				  BufferedReader reader = null;
				  try {
				    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				    String line;
				    while ((line = reader.readLine()) != null) {
				      id_jour=line;
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
			return id_jour;	
		}
		
		protected void onPostExecute(String result){		
			Log.e("JSON",result);	
			
			  try
		        {
		          JSONObject jsonObject=new JSONObject(result);
		          JSONArray jsonArray= jsonObject.getJSONArray("Data");
		          for(int i=0;i<jsonArray.length();i++)
		          {
		              JSONObject jsonObject1=jsonArray.getJSONObject(i);
		              String Id= jsonObject1.getString("Id");
		              String LocalityId= jsonObject1.getString("LocalityId");
		              String Name= jsonObject1.getString("Name");
		              String PointType= jsonObject1.getString("PointType");
		              database.insertData(Id,LocalityId,Name,PointType);
		        }
		          database.close();
		    }
		    catch(JSONException e){
		    	e.printStackTrace();
		    	database.close();
		    	}
			
			arrayList=database.fetchData();
			 
			Intent intent = new Intent(LoadDBActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) 
	    getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) return true;
	    return false;
	} 
	
}


