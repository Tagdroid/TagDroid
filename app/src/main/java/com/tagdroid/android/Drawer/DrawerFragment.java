package com.tagdroid.android.Drawer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tagdroid.android.R;


public class DrawerFragment extends Fragment {
    /** Remember the position of the selected item. */
    private static final String STATE_SELECTED_POSITION = "selected_drawer_position";
    private int mCurrentSelectedPosition = 1;

    /** Pour vérifier que l'utilisateur a "appris" à ouvrir le drawer */
    private static final String PREF_USER_LEARNED_DRAWER = "drawer_learned";
    private boolean mUserLearnedDrawer;

    /** A pointer to the current callbacks instance (the Activity). */
    private DrawerCallbacks mCallbacks;

    /** La "Layout" générale */
    private DrawerLayout drawerLayout;
    /** La View contenant la "Layout" du drawer : un fragment */
    private View drawerContainerView;
    /** La "Layout" du drawer : une ListView. */
    private ListView drawerListView;

    /** La Toolbar qui nous sert d'ActionBar et le DrawerToggle ! */
    private ActionBarDrawerToggle drawerToggle;


    private boolean mFromSavedInstanceState;

    public DrawerFragment() {
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (DrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On regarde si l'utilisateur a "appris" à ouvrir le Drawer
        mUserLearnedDrawer = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        drawerContainerView = getActivity().findViewById(R.id.left_drawer);

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        drawerListView = (ListView) inflater
                .inflate(R.layout.main_drawer, container, false);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        initDrawerLayout();
        return drawerListView;
    }
    /** Va générer la Layout du Drawer */
    public void initDrawerLayout() {
        DrawerAdapter drawerAdapter = new DrawerAdapter(getActivity(), R.id.drawer_list);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setItemChecked(mCurrentSelectedPosition, true);
    }

    /** Derniers ajustements, notamment le DrawerToggle */
    public void setUp(View drawerContainerView,
                      DrawerLayout drawerLayout) {
        this.drawerContainerView = drawerContainerView;
        this.drawerLayout = drawerLayout;
        this.drawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded())
                    return;
                mCallbacks.onDrawerOpenOrClose(false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded())
                    return;
                if (!mUserLearnedDrawer) {
                    // L'utilisateur a appris à ouvrir le drawer
                    mUserLearnedDrawer = true;
                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                mCallbacks.onDrawerOpenOrClose(true);
            }
        };
        this.drawerToggle.setDrawerIndicatorEnabled(true);

        // Defer code dependent on restoration of previous instance state.
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        this.drawerLayout.setDrawerListener(drawerToggle);

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            drawerLayout.openDrawer(drawerContainerView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    private void selectItem(int position) {
        // Le Header aura le même effet que le premier élément
        if (position== 0)
            position = 1;

        mCurrentSelectedPosition = position;
        if (drawerListView != null)
            drawerListView.setItemChecked(position, true);

        if (drawerLayout != null)
                    drawerLayout.closeDrawer(drawerContainerView);

        if (mCallbacks != null) {
            final int finalPosition = position;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCallbacks.onDrawerItemSelected(finalPosition);
                }
            }, 100);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Callbacks interface that all activities using this fragment must implement. */
    public static interface DrawerCallbacks {
        // Called when an item in the navigation drawer is selected.
        void onDrawerItemSelected(int position);
        void onDrawerOpenOrClose(boolean isOpen);
    }
}


