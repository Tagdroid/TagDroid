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

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tagdroid.android.MainActivity;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.HttpGet.HttpGetDatabase;
import com.tagdroid.tagapi.ProgressionInterface;
import com.viewpagerindicator.CirclePageIndicator;

public class WelcomeActivity extends FragmentActivity implements WelcomeFragment.OnButtonClicked, ProgressionInterface {
    ViewPager mPager;
    HttpGetDatabase httpGetDatabase = new HttpGetDatabase(this, this);
    private ProgressBar progressBar;
    private ArcProgress arcProgress;
    private TextView load_value;
    private boolean finalbutton=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We check for Google Play Services… CyanogenMod without GApps for example
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != 0) {
            Toast.makeText(this, getString(R.string.googleplay_not_detected),
                    Toast.LENGTH_LONG).show();
            Log.d("WelcomeActivity", getString(R.string.googleplay_not_detected));
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
                    .setTitle(getString(R.string.welcome_internet_connexion_problem_alerttitle))
                    .setMessage(getString(R.string.welcome_internet_connexion_problem_alertdialog))

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
        if (HttpGetDatabase.isFinished) {
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean("AppAlreadyLaunched", true).apply();
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        } else{
            setContentView(R.layout.welcome_activity_load);
            arcProgress = (ArcProgress)findViewById(R.id.arc_progress);
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
    public void onDownloadStart() {
        Log.d("WelcomeActivity", "onDownloadStart");
    }

    @Override
    public void onDownloadFailed(Exception e) {
        Log.d("WelcomeActivity", "onDownloadFailed : " +e.getMessage());
    }

    @Override
    public void onDownloadProgression(int progression, int total) {
        int pourcent = (progression*100)/total;

        Log.d("WelcomeActivity", "downloadline " + progression + "/" + total);
        Log.d("WelcomeActivity", "downloadline% " + pourcent + " %");

        if(progressBar!=null){
            progressBar.setMax(total);
            progressBar.setProgress(progression);
            progressBar.setSecondaryProgress(progression);
        }


        if(arcProgress!=null){
            arcProgress.setProgress(pourcent);
            load_value.setText(getString(R.string.loading_in_progress));
            load_value.setAllCaps(true);
        }
    }

    @Override
    public void onDownloadComplete() {
        load_value.setText(getString(R.string.welcome_load_finished));

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