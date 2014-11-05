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
import com.tagdroid.tagdroid.Pages.AboutFragment;
import com.tagdroid.tagdroid.Pages.ActualitesFragment;
import com.tagdroid.tagdroid.Pages.LignesFragment;
import com.tagdroid.tagdroid.Pages.TraficInfosFragment;

public class MainActivity extends Activity implements ChangeFragmentInterface{
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
            selectItem(0, false);
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

        //Onglets principaux avec icones
        TypedArray titlesArray = res.obtainTypedArray(R.array.drawer_items_titles);
        TypedArray iconesArray = res.obtainTypedArray(R.array.drawer_items_icons);
        for (int i = 0; i < titlesArray.length(); i++)
            drawerAdapter.newSubItemIcon(titlesArray.getString(i),iconesArray.getResourceId(i, 0));

        // Divider
        drawerAdapter.newSection("");

        //Onglets secondaires sans icones
        titlesArray = res.obtainTypedArray(R.array.drawer_items_plus_titles);
        for (int i = 0; i < titlesArray.length(); i++)
            drawerAdapter.newSubItem(titlesArray.getString(i));


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
            case 0:
                activePage = new LignesFragment();
                break;
            case 1:
                //activePage = new FavorisFragment();
                break;
            case 2:
                //activePage = new ProximiteFragment();
                break;
            case 3:
               // activePage = new GMapFragment();
                break;
            case 4:
                activePage = new TraficInfosFragment();
                break;
            case 5:
                activePage = new ActualitesFragment();
                break;
            case 6:
                //activePage = new TarifsFragment();
                break;
            case 7:
                //divider
                break;
            case 8:
                //activePage = new SettingsFragment();
                break;
            case 9:
                activePage = new AboutFragment();
                break;
            case 10:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tagdroid.android")));
                break;
            default:
                return;
        }
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
        Log.d("Actual Position Fragment", actualPosition+"");
        transaction.commit();
    }

    // Manages the ActionBar touches. TODO manage the fragment menu touches…
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return activePage.onOptionsItemSelected(item);
    }



    // Recreate the ActionBar : Title and menu (called on invalidate or onCreate)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (drawer.isDrawerOpen(drawerList)) {
            actionBar.setTitle(R.string.app_name);
        } else {
            actionBar.setTitle(getFragmentTitle());
            getMenuInflater().inflate(getFragmentMenu(), menu);


            // Associate searchable configuration with the SearchView
           /* if(activePage.getTitle().equals("Lignes")) {
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }*/


        }
        return super.onPrepareOptionsMenu(menu);
    }
    Integer getFragmentMenu() {
        return activePage.getMenuId();
    }
    String getFragmentTitle() {
        return activePage.getTitle();
    }

    @Override
    public void onChangeFragment(Page actualPage) {
        activePage = actualPage;
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