package com.tagdroid.tagdroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class LigneFragment extends Fragment {
	private Tracker tracker;
	
	public static LigneFragment newInstance(String ligne, int id_debut, int id_fin) {
		LigneFragment f = new LigneFragment();
		Bundle b = new Bundle();	
		b.putString("ligne", ligne);
		b.putInt("id_debut", id_debut);
		b.putInt("id_fin", id_fin);
		f.setArguments(b);
        return f;
    }
	ProgressDialog mProgressDialog;
	private ListView ListViewLigne;
	private int id_debut, id_fin;	
	
	private static View view;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
			setHasOptionsMenu(true);
	        this.tracker = EasyTracker.getInstance(this.getActivity());

	}
	
	@Override
    public void onResume() {
        super.onResume();
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send( MapBuilder.createAppView().build() );
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,final Bundle savedInstanceState) {
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = inflater.inflate(R.layout.ligne, container, false);
	    } catch (InflateException e) {
	    }
        
        final String ligne = this.getArguments().getString("ligne");
        MainActivity.mTitle = ligne;
        id_debut = this.getArguments().getInt("id_debut", 0);
        id_fin = this.getArguments().getInt("id_fin", 1);
        
        getActivity().getActionBar().setTitle(MainActivity.mTitle);
  
        ListViewLigne = (ListView) view.findViewById(R.id.listviewligne);      
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        
        for(int i=id_debut;i<(id_fin+1);i++){
        	String r="ID"+i;
        	int resID = getResources().getIdentifier(r, "array", getActivity().getPackageName());      	
        	String[] station = getResources().getStringArray(resID);
        	
        	map = new HashMap<String, String>();
        	map.put("longitude", station[3]);
        	map.put("latitude", station[2]);
            map.put("titre", station[1]);
            map.put("id_station", "id="+station[0]);
            if(station.length>=5)map.put("corres1", String.valueOf(getResources().getIdentifier(station[4], "drawable", getActivity().getPackageName())));
            if(station.length>=6)map.put("corres2", String.valueOf(getResources().getIdentifier(station[5], "drawable", getActivity().getPackageName())));
            if(station.length>=7)map.put("corres3", String.valueOf(getResources().getIdentifier(station[6], "drawable", getActivity().getPackageName())));
            if(station.length>=8)map.put("corres4", String.valueOf(getResources().getIdentifier(station[7], "drawable", getActivity().getPackageName())));
            if(station.length>=9)map.put("corres5", String.valueOf(getResources().getIdentifier(station[8], "drawable", getActivity().getPackageName())));
            if(station.length>=10)map.put("corres6", String.valueOf(getResources().getIdentifier(station[9], "drawable", getActivity().getPackageName())));
            if(station.length>=11)map.put("corres7", String.valueOf(getResources().getIdentifier(station[10], "drawable", getActivity().getPackageName())));
            if(station.length>=12)map.put("corres8", String.valueOf(getResources().getIdentifier(station[11], "drawable", getActivity().getPackageName())));
            if(station.length>=13)map.put("corres9", String.valueOf(getResources().getIdentifier(station[12], "drawable", getActivity().getPackageName())));
            if(station.length>=14)map.put("corres10", String.valueOf(getResources().getIdentifier(station[13], "drawable", getActivity().getPackageName())));
            
           
            
            
            if(i==id_debut){
            	 String debut = ligne.toLowerCase().replaceAll(" ", "")+"_debut";
            	map.put("img", String.valueOf(getResources().getIdentifier(debut, "drawable", getActivity().getPackageName())));
            }
            else if(i==id_fin){
            	String fin = ligne.toLowerCase().replaceAll(" ", "")+"_fin";
            	map.put("img", String.valueOf(getResources().getIdentifier(fin, "drawable", getActivity().getPackageName())));
            	
            }
            else{
            	String milieu = ligne.toLowerCase().replaceAll(" ", "")+"_milieu";
            	map.put("img", String.valueOf(getResources().getIdentifier(milieu, "drawable", getActivity().getPackageName())));
            }
            
            listItem.add(map);
        }
  
       SimpleAdapter mSchedule = new SimpleAdapter (getActivity(), listItem, R.layout.affichageitem,
               new String[] {"img", "titre", "corres1", "corres2", "corres3", "corres4", "corres5", "corres6", "corres7", "corres8", "corres9", "corres10"}, 
               new int[] {R.id.img, R.id.titre,R.id.img_corres1, R.id.img_corres2, R.id.img_corres3, R.id.img_corres4, R.id.img_corres5, R.id.img_corres6, R.id.img_corres7,R.id.img_corres8, R.id.img_corres9,R.id.img_corres10 }){
       };
     
        ListViewLigne.setAdapter(mSchedule);
        ListViewLigne.setOnItemClickListener(new OnItemClickListener()
        {  	
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {          		
				HashMap<String, String> map = (HashMap<String, String>) ListViewLigne.getItemAtPosition(position);
        		MainActivity.titre1 = map.get("titre");
        		MainActivity.id_station1 =  map.get("id_station");
        		MainActivity.ligne2 = ligne;
        		MainActivity.latitude1=map.get("latitude");
        		MainActivity.longitude1=map.get("longitude");     		    			
    			MainActivity.TITLES = new String[] { "STATIONDETAIL" };
    			MainActivity.adapter.notifyDataSetChanged();  			
             }
         });
        return view;

    }
  
	
	private class DownloadFile extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }   
        
        protected String doInBackground(String... sUrl) {
            try {
                URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/TAGdroid/PLAN_"+sUrl[1]+".pdf");

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return sUrl[1];
        }
        
        protected void onPostExecute(String result) {
        	 mProgressDialog.dismiss();
	        File file2 = new File(Environment.getExternalStorageDirectory(),"TAGdroid/PLAN_"+result+".pdf");
        	showPdf(file2);
        }
        
        @Override
        protected void onCancelled () {
          if(LigneFragment.this != null)
            Toast.makeText(getActivity(), "Annulation du téléchargement", Toast.LENGTH_SHORT).show();
        }

    }
       

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	menu.clear();
    	inflater.inflate(R.menu.menu_ligne, menu); 
    }
    
    
    
    
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {            
        switch (item.getItemId()) {
    		case R.id.menu_pdf:
    			String[] numero_ligne2=this.getArguments().getString("ligne").split(" ");
    			File folder = new File(Environment.getExternalStorageDirectory().toString(), "TAGdroid");
	        	folder.mkdir();
	        	File file = new File(folder, "PLAN_"+numero_ligne2[1]+".pdf");

	        	if(this.getArguments().getString("ligne").equals("Ligne E"))Toast.makeText(getActivity(), "Pas de plan disponible pour la ligne E", Toast.LENGTH_SHORT).show();	        	
	        	else{
	        		if(!file.exists()){	        		
		        		try {
			        		file.createNewFile();
			        	}
			        	catch (IOException e1) {
			        	    e1.printStackTrace();
			        	}
		        		
		        		mProgressDialog = new ProgressDialog(getActivity());
		        		mProgressDialog.setMessage("PLAN_"+numero_ligne2[1]+".pdf");
		        		mProgressDialog.setTitle("Téléchargement en cours ...");
		        		mProgressDialog.setIndeterminate(false);
		        		mProgressDialog.setMax(100);
		        		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		        		DownloadFile downloadFile = new DownloadFile();
		        		downloadFile.execute("http://www.tag.fr/ftp/fiche_horaires/PLAN_"+numero_ligne2[1]+".pdf",numero_ligne2[1]);     
		        	}
		        	else{
		        		Toast.makeText(getActivity(), "Ouverture du lecteur PDF", Toast.LENGTH_SHORT).show();
		        		showPdf(file);
		        	}
	        		return true;
	        	}	        	
    			
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }

	 public void showPdf(File file){
         Intent testIntent = new Intent(Intent.ACTION_VIEW);
         testIntent.setType("application/pdf");
         Intent intent = new Intent();
         intent.setAction(Intent.ACTION_VIEW);
         Uri uri = Uri.fromFile(file);
         intent.setDataAndType(uri, "application/pdf");
         startActivity(intent);
     }
	 
	 

         	
 }