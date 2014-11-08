package com.tagdroid.android.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.tagdroid.android.Legacy.MainActivityOLD;
import com.tagdroid.android.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WidgetActivity extends AppWidgetProvider{
	
	public static final String ACTION_SHOW_NEXT = "com.tagdroid.android.SHOW_NEXT";	
	public static final String ACTION_SHOW_PREVIOUS = "com.tagdroid.android.SHOW_PREVIOUS";	
	public static final String ACTION_SHOW_CADRE = "com.tagdroid.android.SHOW_CADRE";	
	public static final String ACTION_SHOW_HEAD = "com.tagdroid.android.SHOW_HEAD";
	private static int number = 1;
	static String station;
	private String id_station=null;
	protected static String id_jour1="null";
	Calcul_ID_Jour id_jour=new Calcul_ID_Jour();
	//Calcul_Horaire ligne=new Calcul_Horaire();
	

	 public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		 final int N = appWidgetIds.length;
         for (int appWidgetId : appWidgetIds) {
             updateAppWidget(context, appWidgetManager, appWidgetId);
         }
	}
	 
	 static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		 RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		 ComponentName widget = new ComponentName(context, WidgetActivity.class);
		 AppWidgetManager.getInstance(context).updateAppWidget(widget, remoteViews);
	     
	     
	     /*First Intent - NEXT*/
	     Intent intent = new Intent(context, WidgetActivity.class);
	     intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	     intent.setAction(ACTION_SHOW_NEXT);
	     PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	     remoteViews.setOnClickPendingIntent(R.id.next, pendingIntent);
	     
	     /*Second Intent - PREVIOUS*/
	     Intent intent2 = new Intent(context, WidgetActivity.class);
	     intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	     intent2.setAction(ACTION_SHOW_PREVIOUS);
	     PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, 0);
	     remoteViews.setOnClickPendingIntent(R.id.previous, pendingIntent2);
	     
	     /*Third Intent - CADRE*/
	     Intent intent3 = new Intent(context, WidgetActivity.class);
	     intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	     intent3.setAction(ACTION_SHOW_CADRE);
	     PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 0, intent3, 0);
	     remoteViews.setOnClickPendingIntent(R.id.cadre, pendingIntent3);
	     
	     /*Fourth Intent - HEAD*/
	     Intent intent4 = new Intent(context, WidgetActivity.class);
	     intent4.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	     intent4.setAction(ACTION_SHOW_HEAD);
	     PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, 0, intent4, 0);
	     remoteViews.setOnClickPendingIntent(R.id.head, pendingIntent4);

	     appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	 }
	 
	 
	    @Override
	    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
	        super.onReceive(context, intent);
	        if (intent.getAction().equals(ACTION_SHOW_NEXT)) Action_Next(context);
	        else if (intent.getAction().equals(ACTION_SHOW_PREVIOUS)) Action_Previous(context);	 
	        else if (intent.getAction().equals(ACTION_SHOW_CADRE))Action_Cadre(context);  
	        else if (intent.getAction().equals(ACTION_SHOW_HEAD))Action_Head(context);   
	        else super.onReceive(context, intent);  
	    }
	 
	    protected void Action_Next(Context context) {
	    	number++;
	    	RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget);
		    remoteViews.setTextViewText(R.id.widget_vers,String.valueOf(number));
		    ComponentName widget = new ComponentName(context, WidgetActivity.class);
		    AppWidgetManager.getInstance(context).updateAppWidget(widget, remoteViews);
	    }
	    
	    protected void Action_Previous(Context context) {
	    	number--;
	    	RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget);
		    remoteViews.setTextViewText(R.id.widget_vers, String.valueOf(number));
		    ComponentName widget = new ComponentName(context, WidgetActivity.class);
		    AppWidgetManager.getInstance(context).updateAppWidget(widget, remoteViews);
	    }
	    
	    protected void Action_Cadre(Context context) {
	    	 context.startActivity(new Intent(context, WidgetDialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));	   
	    	//id_jour.execute();
	    }
	    
	    protected void Action_Head(Context context) {
	        context.startActivity(new Intent(context, MainActivityOLD.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	    }
	    
	    private class Calcul_ID_Jour extends AsyncTask<Void, Void, String>{			
			protected void onPreExecute() {				
				super.onPreExecute();
			}
			
			@SuppressWarnings("unused")
			protected void onProgressUpdate(){
			}
			
			@Override
			protected String doInBackground(Void... arg0) {
				String id_jour="null";
				try{     		
					Document doc = Jsoup.connect("http://tag.mobitrans.fr/").userAgent("Mozilla").get();
	     			
	     			if(doc.title().contains("Mobitrans")){
	     				Elements div = doc.select("div.corpsC");
		     			Element ahref = div.select("a[href^=?p]").first();
		     			id_jour= ahref.attr("href").substring(12, 19);
	     			}	
	     		}catch (IOException e) {	     			
	     			e.getStackTrace();
	            }		
				return id_jour;	
			}
			
			protected void onPostExecute(String result){		
				id_jour1=result;	
				if(id_jour1.equals("null")){
					//Toast.makeText(context , "Connexion internet indisponible, Réessayer", Toast.LENGTH_SHORT).show();
				}else{	
					Log.e("123",result+"");
					//if (ligne.getStatus() == AsyncTask.Status.FINISHED) ligne = new Calcul_Horaire();				
					//if (ligne.getStatus() != AsyncTask.Status.RUNNING) ligne.execute();										
				}
			}
	}
/*
	    private class Calcul_Horaire extends AsyncTask<String, Void , Boolean>{   	
	    	String vers1=null,tps11,tps12;
	    	String vers2,tps21,tps22;
	    	String vers3,tps31,tps32;
	    	String vers4,tps41,tps42;
	    	String vers5,tps51,tps52;
	    	boolean full=false;
	    	
			@Override
			protected void onPreExecute() {
				
				super.onPreExecute();			
			}	
			@SuppressWarnings("unused")
			protected void onProgressUpdate(){
			}
			@SuppressLint("NewApi")
			@Override
			protected Boolean doInBackground(String ... url) {	
				String url_station = "http://tag.mobitrans.fr/index.php?p=49&"+id_station+"&m=1&I="+id_jour1;
					try {
						Document doc = Jsoup.connect(url_station)
											.userAgent("Mozilla")
											.get();	
									
						Element var_1 = doc.getElementsByClass("corpsL").first();					
						var_1.select("span").remove();			
						Element var_infotrafic = var_1.getElementsByClass("infoTrafic").first();
						if (var_infotrafic != null){
							url_alert="http://tag.mobitrans.fr/index.php"+var_infotrafic.select("a[href^=?p]").first().attr("href").toString();
							alert=true;									
						}
						var_1.select("div").remove();
						var_1.select("a").remove();
						var_1.select("br").remove();
						var_1.select("b:contains(!)").remove();
						
						int nbr_stations = (var_1.getAllElements().size())-1; 					
						
						if (nbr_stations!=0 && nbr_stations<=6){
							String[][] stations = new String[nbr_stations][3];
							
							for(int i=0;i<nbr_stations;i++){
								for(int j=0;j<3;j++){
									if(j==0 && var_1.getAllElements().get(i+1).text()!=null){
										stations[i][j]=var_1.getAllElements().get(i+1).text().substring(5);		
									}		
									else stations[i][j]="";
								}
							}
							
							String var_2= var_1.toString().replace("\n", "")
									  .replace("<div class=\"corpsL\">","")
									  .replace("</div>","");				
							String[] var_3 = var_2.split("<b>");
							for(int i=1;i<=nbr_stations;i++){
								String[] var_4 = var_3[i].split("</b>");
								var_4[1]=var_4[1].substring(2);	
								String[] var_5 = var_4[1].split("Prochain passage : ");
								for(int j=1;j<var_5.length;j++){
									if(var_5[j].contains("proche")) var_5[j] = "Départ proche";
									stations[i-1][j]=var_5[j];					
								}
							}
							
							// Tri des stations par ordre alphabetique pour conserver l'ordre au refresh 
							Arrays.sort(stations, new Comparator<String[]>() {
						         @Override
						         public int compare(final String[] entry1, final String[] entry2) {
						              return entry1[0].compareTo(entry2[0]);
						         }
						    });
							
							
							
							
																			
							if(stations.length>=1){	
								full=true;
								vers1=stations[0][0].trim();
								tps11=stations[0][1].trim();
								tps12=stations[0][2].trim();
							}if(stations.length>=2){
								if(stations[1][0].trim().compareTo(vers1)>0){
									vers2=stations[1][0].trim();
									tps21=stations[1][1].trim();
									tps22=stations[1][2].trim();
								}else{
									vers1=stations[1][0].trim();
									tps11=stations[1][1].trim();
									tps12=stations[1][2].trim();
									vers2=stations[0][0].trim();
									tps21=stations[0][1].trim();
									tps22=stations[0][2].trim();
								}
								
							}if(stations.length>=3){
								vers3=stations[2][0].trim();
								tps31=stations[2][1].trim();
								tps32=stations[2][2].trim();
							}if(stations.length>=4){
								vers4=stations[3][0].trim();
								tps41=stations[3][1].trim();
								tps42=stations[3][2].trim();
							}if(stations.length>=5){
								vers5=stations[4][0].trim();
								tps51=stations[4][1].trim();
								tps52=stations[4][2].trim();
							}						
						}														
					}
			    
					catch (IOException e) {
						e.printStackTrace();
						vers1="Problème de connexion";
					}
					return full;			
			}
			
			@SuppressLint("ResourceAsColor")
			@Override
			protected void onPostExecute(Boolean data) {
				load_layout.setVisibility(View.GONE);
				if(data){			
					no_horaires_layout.setVisibility(View.GONE);
					data_layout.setVisibility(View.VISIBLE);
				}
				else{				
					no_horaires_layout.setVisibility(View.VISIBLE);
					data_layout.setVisibility(View.GONE);
				}
				
				
				if(alert){ 
		            alert_button.setVisibility(View.VISIBLE);	
				}
											
				  		
				if(vers1!=null){				
					load_layout.setVisibility(View.GONE);
					case1.setVisibility(View.VISIBLE);	
					if(tps12==null){
						case1_suivant.setVisibility(View.GONE);
					}
				}if(vers2!=null){			
					case2.setVisibility(View.VISIBLE);
					if(tps22.isEmpty()){
						case2_suivant.setVisibility(View.GONE);
					}
				}if(vers3!=null){
					case3.setVisibility(View.VISIBLE);	
					if(tps32.isEmpty()){
						case3_suivant.setVisibility(View.GONE);
					}
				}if(vers4!=null){			
					case4.setVisibility(View.VISIBLE);	
					if(tps42.isEmpty()){
						case4_suivant.setVisibility(View.GONE);
					}
				}if(vers5!=null){			
					case5.setVisibility(View.VISIBLE);		
					if(tps52.isEmpty()){
						case5_suivant.setVisibility(View.GONE);
					}
				}
				
				tv1.setText(vers1);
				tv2.setText(tps11);
				tv3.setText(tps12);
				tv4.setText(vers2);
				tv5.setText(tps21);
				tv6.setText(tps22);	
				tv7.setText(vers3);
				tv8.setText(tps31);
				tv9.setText(tps32);	
				tv10.setText(vers4);
				tv11.setText(tps41);
				tv12.setText(tps42);	
				tv13.setText(vers5);
				tv14.setText(tps51);
				tv15.setText(tps52);	

			}		
		}	
		*/
}