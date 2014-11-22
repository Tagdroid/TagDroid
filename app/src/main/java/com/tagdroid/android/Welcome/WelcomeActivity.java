package com.tagdroid.android.Welcome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tagdroid.android.MainActivity;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.HttpGet.HttpGetLineStops;
import com.tagdroid.tagapi.JSon2SQL.ReadJSonLineStops;
import com.tagdroid.tagapi.ProgressionInterface;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class WelcomeActivity extends FragmentActivity implements WelcomeFragment.OnButtonClicked, ProgressionInterface {
    ViewPager mPager;
    PageIndicator mIndicator;
    boolean db_downloading = false,
            db_OK,
            skip = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We check for Google Play Services… CyanoFgenMod without GApps for example
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != 0) {
            Toast.makeText(this, "Google Play Service non détecté. Dysfonctionnement de l'application possible.",
                    Toast.LENGTH_LONG).show();
            Log.d("Welcome Status", "Google Play Service undetected");
        }

        db_OK = this.getDatabasePath("TagDatabase.db").exists();

        // We check if it's the first app launch…
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("AppAlreadyLaunched", false)) {
            Log.d("Welcome Status", "App already launched");
            // …and if DB exists
            if (db_OK) {
                Log.d("Welcome Status", "Database already exists");
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                setContentView(R.layout.activity_welcome);
                startWaitingScreen();
            }
        } else {
            Log.d("Welcome Status", "First App Launch");
            startWelcomeScreen();
        }
    }

    private void startWaitingScreen() {
        if (!db_downloading)
            startDownloadTask();
    }

    public void startWelcomeScreen() {
        setContentView(R.layout.activity_welcome);

        /*if (Build.VERSION.SDK_INT >= 14) {
            getActionBar().setIcon(R.drawable.tag);
            getActionBar().setTitle("");
        } else getActionBar().setTitle(R.string.welcome_bienvenue);*/

        WelcomeAdapter mAdapter = new WelcomeAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        indicator.setRadius(4 * density);
        indicator.setPageColor(0x22000000);
        indicator.setFillColor(0xCC000000);
        indicator.setStrokeWidth(0);

        startDownloadTask();
    }

    public void startDownloadTask() {
        // We check if we are able to download DB
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            Log.d("Welcome Status", "Internet connection problem");
            new AlertDialog.Builder(this)
                    .setTitle("Pas de connexion Internet")
                    .setMessage("L'application n'est pas en mesure de télécharger la base de données "
                            + "nécessaire au bon fonctionnement de l'application.\n\n"
                            + "TAGdroid va se fermer.\n\nActivez votre connexion Internet "
                            + "par WIFI ou données mobiles (4G, 3G, Edge) et relancez l'application.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(R.drawable.ic_report)
                    .show();
        } else {
            //HttpGetLinesList httpGetLinesList = new HttpGetLinesList(this);
            Log.d("Welcome Status", "Start of Downloading database");
            //httpGetLinesList.setProgressBar((ProgressBar) findViewById(R.id.loadJSON_bar));
            //httpGetLinesList.execute();
            new HttpGetLineStops(27, 1, this).execute();
            db_downloading = true;
        }
    }

    @Override
    public void onFinalButtonClicked() {
        skip = true;
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("AppAlreadyLaunched", true)
                .apply();

        if (db_OK) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        } else
            startWaitingScreen();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pageItem", mPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void readJSon(String jsonQueryResult) {
        //ReadJSonLinesList readJSonLinesList = new ReadJSonLinesList(jsonQueryResult, this, this);
        //readJSonLinesList.setProgressBar((ProgressBar) findViewById(R.id.parseJSON_bar));
        //readJSonLinesList.execute();
        new ReadJSonLineStops(jsonQueryResult, 27, 1, this, this).execute();
    }

    public void onDownloadStart() {
        Toast.makeText(this, "Début du téléchargement…", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Début du téléchargement…");
    }

    public void onDownloadFailed(Integer e) {
        Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Failed to download file");
        Log.e("Download", "StatusCode : " + e);
    }

    public void onDownloadFailed(Exception e) {
        Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Failed to download file");
        e.printStackTrace();
    }

    public void onDownloadComplete(String resultString) {
        Toast.makeText(this, "Le téléchargement est terminé.", Toast.LENGTH_SHORT).show();
        Log.d("Download", "Download finished !");
        readJSon(resultString);
    }

    public void onJSonParsingStarted() {
    }

    public void onJSonParsingFailed(Exception e) {
    }

    public void onJSonParsingFailed(String e) {
        Log.e("JSonParsing", e);
    }

    public void onJSonParsingComplete() {
        db_OK = true;
        Log.d("JSonParsing", "Finished !");
        if (skip) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }

    public class WelcomeAdapter extends FragmentStatePagerAdapter {
        public WelcomeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public WelcomeFragment getItem(int position) {
            WelcomeFragment fragment = new WelcomeFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);
            return fragment;
        }
    }
}