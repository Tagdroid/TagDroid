package com.tagdroid.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

public abstract class Page extends Fragment {
    public abstract String  getTitle();
    public abstract Integer getMenuId();
    protected static ChangeFragmentInterface changeFragmentInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            changeFragmentInterface = (ChangeFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeFragmentInterface.setActivityTitle(getTitle());
    }

    public interface ChangeFragmentInterface {
        void onChangeFragment(Page actualPage);
        void setActivityTitle(String title);
    }
}
