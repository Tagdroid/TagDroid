package com.tagdroid.tagdroid.Welcome;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tagdroid.tagapi.HttpApiTask;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadJSonTask;
import com.tagdroid.tagdroid.MainActivity;
import com.tagdroid.tagdroid.R;
import com.viewpagerindicator.LinePageIndicator;

import java.io.File;

import rosenpin.androidL.dialog.AndroidLDialog;

public class WelcomeActivity extends FragmentActivity implements WelcomeFragment.OnButtonClicked, ProgressionInterface {
    public static String PACKAGE_NAME;
    ViewPager mPager;
    boolean db_OK;

    //TODO download database meanwhile… WORK IN PROGRESS !!!!

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We check for Google Play Services… CyanogenMod without GApps for example
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != 0)
            Toast.makeText(this, "Google Play Service non détecté. Dysfonctionnement de l'application possible.",
                    Toast.LENGTH_LONG).show();

        // We check if it's the first app launch and DB exists
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("AppAlreadyLaunched", false)
                && doesDatabaseExist(this,"TagDatabase.db")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        // We check if we are able to download DB else App Exit.
        }else if(!(isNetworkAvailable())){
            AndroidLDialog dialog = new AndroidLDialog.Builder(this)
                    .Title("Pas de connexion Internet")
                    .Message("L'application n'est pas en mesure de télécharger la base de données nécessaire au bon fonctionnement de l'application.\n\nTAGdroid va se fermer.\n\nActivez votre connexion Internet par WIFI ou données mobiles (4G, 3G, Edge) et relancez l'application.")
                    .setPositiveButton("OK" , new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .show();
            /*AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setCancelable(false);
            alert.setTitle("Pas de connexion Internet");
            alert.setIcon(getResources().getDrawable(R.drawable.ic_report));
            alert.setMessage("L'application n'est pas en mesure de télécharger la base de données nécessaire au bon fonctionnement de l'application.\n\nTAGdroid va se fermer.\n\nActivez votre connexion Internet par WIFI ou données mobiles (4G, 3G, Edge) et relancez l'application.");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            });
            alert.show();*/
        }else {
            db_OK=false;
            HttpApiTask httpApiTask = new HttpApiTask(this,"transport/v2/GetPhysicalStops/json?key=TAGDEV");
            //httpApiTask.setProgressBar((ProgressBar) findViewById(R.id.dlProgress));
            httpApiTask.execute();

            PACKAGE_NAME = getApplicationContext().getPackageName();
            setContentView(R.layout.activity_welcome);

            if (Build.VERSION.SDK_INT >= 14) {
                getActionBar().setIcon(R.drawable.tag);
                getActionBar().setTitle("");
            } else
                getActionBar().setTitle(R.string.welcome_bienvenue);

            LinePageIndicator indicator = (LinePageIndicator) findViewById(R.id.indicator);
            WelcomeAdapter welcomePager = new WelcomeAdapter(getSupportFragmentManager());
            mPager = (ViewPager) findViewById(R.id.pager);

            float density = getResources().getDisplayMetrics().density;
            indicator.setSelectedColor(0xff00b4f8);
            indicator.setUnselectedColor(0x44888888);
            indicator.setStrokeWidth(5 * density);
            indicator.setLineWidth(40 * density);
            mPager.setAdapter(welcomePager);
            indicator.setViewPager(mPager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_skip) {
            onFinalButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinalButtonClicked() {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("AppAlreadyLaunched", true)
                .apply();
        if(db_OK&&doesDatabaseExist(this,"TagDatabase.db")){
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            //TODO Ouvrir le fragment qui termine la DL de la BDD
        }


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

    private void readJSon(String jsonQueryResult) {
        ReadJSonTask readJSonTask = new ReadJSonTask(jsonQueryResult, this, this);
        //readJSonTask.setProgressBar((ProgressBar)findViewById(R.id.sqlProgress));
        readJSonTask.execute();
    }

    private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
    public void onDownloadStart() {
        Toast.makeText(this, "Début du téléchargement…", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Début du téléchargement…");
    }
    public void onDownloadFailed(Integer e) {
        Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();
        Log.e("Download", "Failed to download file");
        Log.e("Download", "StatusCode : "+e);
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
    public void onJSonParsingStarted() {}
    public void onJSonParsingFailed(Exception e) {}
    public void onJSonParsingFailed(String e) {
        Log.e("JSonParsing", e);
    }
    public void onJSonParsingComplete() {
        db_OK = true;
        Log.d("JSonParsing", "Finished !");
    }

    // Check if connect to network
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}