package com.tagdroid.tagdroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tagdroid.tagdroid.R;
import com.tagdroid.tagdroid.WelcomeActivity;


public class WelcomePager extends FragmentPagerAdapter {
    public WelcomePager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 6;
    }

    public static interface OnButtonClicked {
        public void onFinalButtonClicked();
    }

    public static class PageFragment extends Fragment {
        int position;
        OnButtonClicked onButtonClicked;

        public static PageFragment newInstance(int position) {
            PageFragment pageFragment = new PageFragment();
            pageFragment.position = position;
            return pageFragment;
        }
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                onButtonClicked = (OnButtonClicked) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int layout = getResources().getIdentifier("welcome_" + (position + 1), "layout", WelcomeActivity.PACKAGE_NAME);
            View view = inflater.inflate(layout, container, false);
            if (position == 5)
                view.findViewById(R.id.btn_go_welcome).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onButtonClicked.onFinalButtonClicked();
                    }
                });
            return view;
        }
    }

}
