package com.tagdroid.tagdroid;

import android.app.Activity;
import android.app.Fragment;

public abstract class Page extends Fragment {
    public abstract String  getTitle();
    public abstract Integer getMenuId();
    protected static ChangeFragmentInterface changeFragmentInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            changeFragmentInterface = (ChangeFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
