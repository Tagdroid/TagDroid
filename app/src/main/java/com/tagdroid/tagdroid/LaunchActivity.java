package com.tagdroid.tagdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tagdroid.tagdroid.Welcome.WelcomeActivity;

public class LaunchActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // We check for Google Play Services… CyanogenMod without GApps for example
    	if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != 0)
		    Toast.makeText(this, "Google Play Service non détecté. Dysfonctionnement de l'application possible.",
                    Toast.LENGTH_LONG).show();

	    // We check if it's the first app launch…
		if (true | getSharedPreferences(WelcomeActivity.PREFS_NAME_2, 0).getBoolean("firstAppLaunch", true)){
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
            startActivity(new Intent(LaunchActivity.this, WelcomeActivity.class));
		}
        else
            startActivity(new Intent(LaunchActivity.this, LoadDBActivity.class));
	}

    private InterstitialAd interstitialAd;
	public void displayInterstitial() {
            Log.d("Home", "displayInterstitial");
        if (interstitialAd.isLoaded())
            interstitialAd.show();
    }
}
