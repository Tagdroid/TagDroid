package com.tagdroid.tagdroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class HomeActivity extends Activity {
	private InterstitialAd interstitialAd;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences welcome = getSharedPreferences(WelcomeActivity.PREFS_NAME_2, 0);	
		boolean skipWelcome = welcome.getBoolean("skipWelcome", false);
		int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if (statusCode != ConnectionResult.SUCCESS) {
		    Toast.makeText(this, "Google Play Service non détecté. Dysfonctionnement de l'application possible.", Toast.LENGTH_LONG);
		}
		
	
		if (skipWelcome){
			/*interstitialAd = new InterstitialAd(this);
		    interstitialAd.setAdUnitId("ca-app-pub-7205304382231139/5017304000");
		    AdRequest adRequest = new AdRequest.Builder()
		    							//.addTestDevice("9D3B7660BC88DFE747A91F1ECC3782CC") // Nexus 4 Test Phone
		    							.build();
		    interstitialAd.loadAd(adRequest);
		    interstitialAd.setAdListener(new AdListener() {
		    	  public void onAdLoaded() {displayInterstitial();}
		    	  public void onAdFailedToLoad(int errorcode) {}
		    });*/
			Intent intent = new Intent(HomeActivity.this, LoadDBActivity.class);
			startActivity(intent);
			finish();
		}else{
			Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
			startActivity(intent);
			finish();
		}		
	}
	public void displayInterstitial() {
		   if (interstitialAd.isLoaded()) {
		    interstitialAd.show();
		   }
		}  
}