package com.tagdroid.tagdroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tagdroid.tagdroid.Drawer.CustomAdapter;
import com.tagdroid.tagdroid.Pages.*;

public class NewMainActivity extends Activity {
    private static DrawerLayout drawer;
    private static ListView drawerList;
    private static ActionBarDrawerToggle drawerToggle;
    private static ActionBar actionBar;
    private static Page activePage;
    public static int actualPosition=-1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        if (savedInstanceState == null) {
            selectItem(1, false);
        }
        (new ChangeLog()).init(this, false);
        /*
        Parse.initialize(this, "CdJdR3cRkKAcnHHWcxRXzseLYUPBJdkP0bUzVLFW", "zNLrxOANbZZJi1Brh5P7vyjUkZrpsptFJWKwckcl");

        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseAnalytics.trackAppOpened(getIntent());
         */
    }

    private void initUI() {
        actionBar = getActionBar();
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerToggle = new CustomActionBarDrawerToggle(this, drawer);
        drawer.setDrawerListener(drawerToggle);
        initDrawer();
    }

    private void initDrawer() {
        CustomAdapter drawerAdapter = new CustomAdapter(this, 0);
        Resources res = getResources();
        drawerAdapter.newSection(getString(R.string.ns_menu_reseau_header));
        TypedArray titlesArray = res.obtainTypedArray(R.array.drawer_reseau_items_titles);
        TypedArray iconesArray = res.obtainTypedArray(R.array.drawer_reseau_items_icons);

        for (int i = 0; i < titlesArray.length(); i++)
            drawerAdapter.newSubItem(titlesArray.getString(i),
                    iconesArray.getResourceId(i, 0));

        drawerAdapter.newSection(getString(R.string.ns_menu_main_header2));
        titlesArray = res.obtainTypedArray(R.array.drawer_infos_items_titles);
        iconesArray = res.obtainTypedArray(R.array.drawer_infos_items_icons);

        for (int i = 0; i < titlesArray.length(); i++)
            drawerAdapter.newSubItem(titlesArray.getString(i),
                    iconesArray.getResourceId(i, 0));

        drawerList.setAdapter(drawerAdapter);

        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, actualPosition > 0);
            }
        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void selectItem(int position, boolean isApplicationProgression) {
        switch (position) {
            case 0: // Infos Fragment
                activePage = new AboutFragment();
                break;
            case 1:
                activePage = new LignesFragment();
                break;
            case 2:
                activePage = new LignesFragment();
                break;
            case 3:
                activePage = new LignesFragment();
                break;
            case 4:
                activePage = new LignesFragment();
                break;
            case 6:
                activePage = new LignesFragment();
                break;
            case 7:
                activePage = new ActualitésFragment();
                break;
            case 8:
                activePage = new LignesFragment();
                break;
            default:
                return;
        }
        Bundle args = new Bundle();
        activePage.setArguments(args);

        // update selected item and title, then close the drawer
        drawerList.setItemChecked(position, true);
        drawer.closeDrawer(drawerList);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.pager, activePage)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.fade_in);
        if (isApplicationProgression) {
            transaction.addToBackStack("activePage");
            Log.d("main","add to backstack " + actualPosition + " " + position);
        }
        actualPosition = position;
        transaction.commit();
    }

    // Manages the ActionBar touches. TODO manage the fragment menu touches…
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.menu_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tagdroid.android")));
                drawer.closeDrawer(drawerList);
                return true;
            case R.id.menu_about:
                selectItem(0, actualPosition > 0);
                return true;
            default:
                return activePage.onOptionsItemSelected(item);
        }
    }

    // Recreate the ActionBar : Title and menu (called on invalidate or onCreate)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (drawer.isDrawerOpen(drawerList)) {
            actionBar.setTitle(R.string.app_name);
            getMenuInflater().inflate(R.menu.menu_drawer, menu);
        } else {
            actionBar.setTitle(getFragmentTitle());
            getMenuInflater().inflate(getFragmentMenu(), menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    Integer getFragmentMenu() {
        return activePage.getMenuId();
    }
    String getFragmentTitle() {
        return activePage.getTitle();
    }

    // Manages the Drawer changes
    class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {
        public CustomActionBarDrawerToggle(Activity mActivity, DrawerLayout mDrawerLayout) {
            super(mActivity, mDrawerLayout, R.drawable.ic_drawer,
                    R.string.drawer_open, R.string.drawer_close);
        }
        public void onDrawerClosed(View view) {
            invalidateOptionsMenu();
        }
        public void onDrawerOpened(View drawerView) {
            invalidateOptionsMenu();
        }
    }
}