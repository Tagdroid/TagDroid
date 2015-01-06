package com.tagdroid.android.Welcome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tagdroid.android.MainActivity;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.HttpGet.HttpGetDatabase;
import com.tagdroid.tagapi.ProgressionInterface;
import com.viewpagerindicator.CirclePageIndicator;

public class WelcomeActivity extends FragmentActivity implements WelcomeFragment.OnButtonClicked, ProgressionInterface {
    ViewPager mPager;
    HttpGetDatabase httpGetDatabase = new HttpGetDatabase(this, this);
    private int total=0;
    private ProgressBar progressBar;
    private TextView load_value;
    private boolean finalbutton=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We check for Google Play Services… CyanogenMod without GApps for example
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != 0) {
            Toast.makeText(this, "Google Play Service non détecté. Dysfonctionnement de l'application possible.",
                    Toast.LENGTH_LONG).show();
            Log.d("WelcomeActivity", "Google Play Service undetected");
        }

        setContentView(R.layout.welcome_activity);

        WelcomeAdapter mAdapter = new WelcomeAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        indicator.setRadius(4 * density);
        indicator.setPageColor(0x22000000);
        indicator.setFillColor(0xCC000000);
        indicator.setStrokeWidth(0);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        load_value = (TextView) findViewById(R.id.load_value);
        startDownloadTask();
    }

    public void startDownloadTask() {
        // We check if we are able to download DB
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            Log.d("WelcomeActivity", "Internet connection problem");
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
            Log.d("WelcomeActivity", "Start of Downloading database");
            httpGetDatabase.execute();

        }
    }

    @Override
    public void onFinalButtonClicked() {
        finalbutton = true;
        if (httpGetDatabase.isFinished) {
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean("AppAlreadyLaunched", true).apply();
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        } else{//TODO Need to implement some "waiting" window
            setContentView(R.layout.welcome_activity_load);
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            load_value = (TextView) findViewById(R.id.load_value);
            Toast.makeText(this,R.string.wait_for_download_to_finish, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pageItem", mPager.getCurrentItem()); //TODO useful ?
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDownloadStart() {
        Log.d("WelcomeActivity", "onDownloadStart");
    }

    @Override
    public void onDownloadFailed(Exception e) {
        Log.d("WelcomeActivity", "onDownloadFailed : " +e.getMessage());
    }

    @Override
    public void onDownloadProgression(int progression, int total) {
        Log.d("WelcomeActivity", "downloadline " + progression + "/" + total);

        int pourcent = (progression*100)/total;
        Log.d("WelcomeActivity", "downloadline% " + pourcent + " %");

        load_value.setText(pourcent+ " %");
        this.total = total;
        progressBar.setMax(total);

        progressBar.setProgress(progression);
        progressBar.setSecondaryProgress(progression);
    }

    @Override
    public void onDownloadComplete() {
        Log.d("WelcomeActivity", "onDownloadComplete");
        progressBar.setProgress(total);
        load_value.setText("Chargement terminé");

        if(finalbutton){
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean("AppAlreadyLaunched", true).apply();
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }

    public class WelcomeAdapter extends FragmentStatePagerAdapter {
        private int[] colors;
        private TypedArray images;
        private String[] titles;
        private String[] descrs;

        public WelcomeAdapter(FragmentManager fm) {
            super(fm);
            setItems();
        }

        private void setItems() {
            Resources res = getResources();
            colors = res.getIntArray(R.array.welcome_colors);
            images = res.obtainTypedArray(R.array.welcome_images);
            titles = res.getStringArray(R.array.welcome_titles);
            descrs = res.getStringArray(R.array.welcome_descriptions);
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
            args.putInt("color",    colors[position]);
            args.putInt("image",    images.getResourceId(position, -1));
            args.putString("title", titles[position]);
            args.putString("descr", descrs[position]);
            fragment.setArguments(args);
            return fragment;
        }
    }
}