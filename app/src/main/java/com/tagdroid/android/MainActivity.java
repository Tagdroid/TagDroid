package com.tagdroid.android;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;
import com.tagdroid.android.Drawer.DrawerFragment;
import com.tagdroid.android.Pages.AboutFragment;
import com.tagdroid.android.Pages.Actualites.ActualitesFragment;
import com.tagdroid.android.Pages.Disruptions.DisruptionsFragment;
import com.tagdroid.android.Pages.LignesGridFragment;
import com.tagdroid.android.Pages.SettingsFragment;
import com.tagdroid.android.Pages.TarifsFragment;
import com.tagdroid.android.Welcome.WelcomeActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class MainActivity extends ActionBarActivity implements DrawerFragment.DrawerCallbacks,
        Page.ChangeFragmentInterface {
    public static boolean firstSee=true;

    // Used to store the last screen title. For use in {@link #restoreActionBar()}.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + getPackageName() + "/databases/TagDatabase.db";
                String backupDBPath = "TagDatabase.db.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception ignored) {

        }

        if ( !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("AppAlreadyLaunched", false)) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        if(firstSee){ //Avoid to display the Changelog at each rotate on first launch…
            new ChangeLog(this).showIfNewVersion(false);
            firstSee=false;
        }
        setContentView(R.layout.main_activity);

        registerReceivers(); //Register receivers for push notifications
        PushManager pushManager = PushManager.getInstance(this);  //Create and start push manager
        try {
            pushManager.onStartup(this);
        } catch (Exception e) {
            Log.d("Push", "Error on Startup");
        }
        pushManager.registerForPushNotifications(); //Register for push!
        checkMessage(getIntent());


        /** Get the Toolbar as the Actionbar */
        Log.d("mainactivity", "oncreateToolbar");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        /** Setup the Drawer */
        DrawerFragment drawerFragment = (DrawerFragment) getFragmentManager()
                .findFragmentById(R.id.left_drawer);
        drawerFragment.setUp(findViewById(R.id.left_drawer),
                (DrawerLayout) findViewById(R.id.drawer_layout));
        if (savedInstanceState == null)
            onDrawerItemSelected(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(int position) {
        Page page;
        switch (position) {
            case  1: page = new LignesGridFragment();
                break;
            // case  2: page = new FavorisFragment();
            //     break;
            // case  3: page = new StationDetailFragment();
            //    break;
            // case  4: page = new MapFragment();
            //     break;
            case  5: page = new DisruptionsFragment();
                 break;
            case  6: page = new ActualitesFragment();
                break;
            case  7: page = new TarifsFragment();
                break;
            case  8: return; //divider
            case  9: page = new SettingsFragment();
                break;
            case 10: page = new AboutFragment();
                break;
            case 11: startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.tagdroid.android")));
                return;
            default:
                return;
        }
        onChangeFragment(page);
        // update the main content by replacing fragments
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.pager, page);
        if(position != 1)//TODO rajouter "si on est sur la page d'accueil"
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDrawerOpenOrClose(boolean isOpen) {
        if (!isOpen)
            supportInvalidateOptionsMenu();
    }

    @Override
    public void onChangeFragment(Page newPage) {
    }

    @Override
    public void setActivityTitle(String title) {
        setTitle(title);
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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}