package com.tagdroid.tagdroid.Welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tagdroid.tagdroid.LoadDBActivity;
import com.tagdroid.tagdroid.R;
import com.viewpagerindicator.LinePageIndicator;

public class WelcomeActivity extends FragmentActivity implements WelcomeFragment.OnButtonClicked{
    public static final String PREFS_NAME_2 = "Welcome";
    public static String PACKAGE_NAME;
    LinePageIndicator indicator;
    ViewPager mPager;
    WelcomeAdapter welcomePager;

    //TODO download database meanwhile…

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        setContentView(R.layout.welcome);


        if (Build.VERSION.SDK_INT >= 14) {
            getActionBar().setIcon(R.drawable.tag);
            getActionBar().setTitle("");
        } else
            getActionBar().setTitle(R.string.welcome_bienvenue);

        indicator = (LinePageIndicator) findViewById(R.id.indicator);
        mPager = (ViewPager) findViewById(R.id.pager);
        welcomePager = new WelcomeAdapter(getSupportFragmentManager());

        float density = getResources().getDisplayMetrics().density;
        indicator.setSelectedColor(0xff00b4f8);
        indicator.setUnselectedColor(0x44888888);
        indicator.setStrokeWidth(5 * density);
        indicator.setLineWidth(40 * density);

        mPager.setAdapter(welcomePager);

        indicator.setViewPager(mPager);
    }

    public class WelcomeAdapter extends FragmentStatePagerAdapter {
        public WelcomeAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public WelcomeFragment getItem(int position) {
            Log.d("Adapter", "newInstance " + position);
            WelcomeFragment fragment =  new WelcomeFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public int getCount() {
            return 6;
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
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME_2, 0).edit();
        editor.putBoolean("firstAppLaunch", false);
        editor.apply();
        Intent intent = new Intent(WelcomeActivity.this, LoadDBActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pageItem", mPager.getCurrentItem());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}