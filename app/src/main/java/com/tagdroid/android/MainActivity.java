package com.tagdroid.android;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;
import com.tagdroid.android.Drawer.CustomAdapter;
import com.tagdroid.android.Pages.AboutFragment;
import com.tagdroid.android.Pages.ActualitesFragment;
import com.tagdroid.android.Pages.LignesFragment;
import com.tagdroid.android.Pages.MapFragment;
import com.tagdroid.android.Pages.TraficInfosFragment;

public class MainActivity extends ActionBarActivity implements ChangeFragmentInterface{
    private static DrawerLayout drawer;
    private static ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private static Page activePage;
    public static int actualPosition=-1;
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceivers(); //Register receivers for push notifications
        PushManager pushManager = PushManager.getInstance(this);  //Create and start push manager
        try {
            pushManager.onStartup(this);
        } catch (Exception e) {
            Log.d("Push", "Error on Startup");
        }
        pushManager.registerForPushNotifications(); //Register for push!
        checkMessage(getIntent());

        initUI();
        if (savedInstanceState == null) {
            selectItem(1, false);
        }
        (new ChangeLog()).init(this, false);

    }

    private void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0); //Sinon shadow sous Android L


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_list);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(mDrawerToggle);



        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        });

        initDrawer();


    }

    private void initDrawer() {
        CustomAdapter drawerAdapter = new CustomAdapter(this, 0);
        Resources res = getResources();


       drawerAdapter.newHeader(R.drawable.tag);
        //Onglets principaux avec icones
        TypedArray titlesArray = res.obtainTypedArray(R.array.drawer_items_titles);
        TypedArray iconesArray = res.obtainTypedArray(R.array.drawer_items_icons);
        for (int i = 0; i < titlesArray.length(); i++){
            if(i==1) drawerAdapter.newSubItemIconCounter(titlesArray.getString(i),iconesArray.getResourceId(i, 0), 5);
            else drawerAdapter.newSubItemIcon(titlesArray.getString(i),iconesArray.getResourceId(i, 0));
        }

        drawerAdapter.newDivider();

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
        mDrawerToggle.syncState();
    }


    private void selectItem(int position, boolean isApplicationProgression) {
        switch (position) {
            case 0:
                //Header
                break;
            case 1:
                activePage = new LignesFragment();
                break;
            case 2:
                //activePage = new FavorisFragment();
                break;
            case 3:
                //activePage = new ProximiteFragment();
                break;
            case 4:
                activePage = new MapFragment();
                break;
            case 5:
                activePage = new TraficInfosFragment();
                break;
            case 6:
                activePage = new ActualitesFragment();
                break;
            case 7:
                //activePage = new TarifsFragment();
                break;
            case 8:
                //divider
                break;
            case 9:
                //activePage = new SettingsFragment();
                break;
            case 10:
                activePage = new AboutFragment();
                break;
            case 11:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tagdroid.android")));
                break;
            default:
                return;
        }
        // update selected item and title, then close the drawer



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

        drawerList.setItemChecked(position, true);
        drawer.closeDrawer(drawerList);
        //
    }

    // Manages the ActionBar touches. TODO manage the fragment menu touches…

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || activePage.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    // Recreate the ActionBar : Title and menu (called on invalidate or onCreate)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (drawer.isDrawerOpen(drawerList)) {
            toolbar.setTitle(R.string.app_name);
        } else {
            toolbar.setTitle(getFragmentTitle());
            toolbar.inflateMenu(getFragmentMenu());
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

    /************************************************/
    /***** PUSH NOTIFICATION PART with Pushwoosh ****/
    /**
     * ********************************************
     */

    @Override
    public void onResume() {
        super.onResume();
        registerReceivers();//Re-register receivers on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceivers();//Unregister receivers on pause
    }

    //Registration receiver
    BroadcastReceiver mBroadcastReceiver = new RegisterBroadcastReceiver() {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent) {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver() {
        @Override
        protected void onMessageReceive(Intent intent) {
            Log.d("PushWoosh", "push message is " + intent.getExtras().getString(JSON_DATA_KEY));
        }
    };

    //Registration of the receivers
    public void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");
        registerReceiver(mReceiver, intentFilter);
        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers() {
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            Log.d("Push", "Error on unRegisterReceiver mReceiver");
        }

        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            Log.d("Push", "Error on unRegisterReceiver mBroadcastReceiver");
        }
    }

    private void checkMessage(Intent intent) {
        if (null != intent) {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT)) {
                Log.d("PushWoosh", "push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            } else if (intent.hasExtra(PushManager.REGISTER_EVENT)) {
                Log.d("PushWoosh", "register");
            } else if (intent.hasExtra(PushManager.UNREGISTER_EVENT)) {
                Log.d("PushWoosh", "unregister");
            } else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT)) {
                Log.d("PushWoosh", "register error");
            } else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT)) {
                Log.d("PushWoosh", "unregister error");
            }
            resetIntentValues();
        }
    }

    private void resetIntentValues() {
        Intent mainAppIntent = getIntent();
        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT)) {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT)) {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT)) {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT)) {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT)) {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }
        setIntent(mainAppIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        checkMessage(intent);
    }
}