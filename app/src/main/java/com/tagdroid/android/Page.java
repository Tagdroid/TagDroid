package com.tagdroid.android;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class Page extends Fragment {
    public abstract String  getTitle();
    public abstract Integer getMenuId();
    protected static ChangeFragmentInterface changeFragmentInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("page", "onAttach");
        try {
            changeFragmentInterface = (ChangeFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        ((MainActivity) activity).setFragmentTitle(getTitle());
    }

    public interface ChangeFragmentInterface {
        void onChangeFragment(Page actualPage);
    }
}
