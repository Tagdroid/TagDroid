package com.tagdroid.tagdroid.Welcome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tagdroid.tagdroid.MainActivity;
import com.tagdroid.tagdroid.R;
import com.viewpagerindicator.LinePageIndicator;

public class WelcomeActivity extends FragmentActivity implements WelcomeFragment.OnButtonClicked {
    public static String PACKAGE_NAME;
    ViewPager mPager;

    //TODO download database meanwhile…

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We check for Google Play Services… CyanogenMod without GApps for example
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != 0)
            Toast.makeText(this, "Google Play Service non détecté. Dysfonctionnement de l'application possible.",
                    Toast.LENGTH_LONG).show();

        // We check if it's the first app launch…
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("AppAlreadyLaunched", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
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
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
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
}