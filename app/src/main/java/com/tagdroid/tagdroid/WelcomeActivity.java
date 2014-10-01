package com.tagdroid.tagdroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.viewpagerindicator.LinePageIndicator;

public class WelcomeActivity extends FragmentActivity implements WelcomePager.OnButtonClicked{
    public static final String PREFS_NAME_2 = "Welcome";
    public static String PACKAGE_NAME;
    public static AssetManager ASSETS;

    //TODO download database meanwhile…

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        ASSETS = getAssets();
        setContentView(R.layout.welcome);

        if (Build.VERSION.SDK_INT >= 14) {
            getActionBar().setIcon(R.drawable.tag);
            getActionBar().setTitle("");
        } else
            getActionBar().setTitle(R.string.welcome_bienvenue);

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new WelcomePager(getSupportFragmentManager()));

        LinePageIndicator indicator = (LinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        float density = getResources().getDisplayMetrics().density;
        indicator.setSelectedColor(0xff00b4f8);
        indicator.setUnselectedColor(0x44888888);
        indicator.setStrokeWidth(5 * density);
        indicator.setLineWidth(40 * density);
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
}