package com.tagdroid.tagdroid.Welcome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tagdroid.tagdroid.R;

public class WelcomeFragment extends Fragment {
    public static interface OnButtonClicked {
        public void onFinalButtonClicked();
    }
    static OnButtonClicked onButtonClicked;

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
        Bundle args = getArguments();
        int position = args.getInt("position");
        View view = inflater.inflate(
                getResources().getIdentifier("welcome_" + (position + 1), "layout", WelcomeActivity.PACKAGE_NAME),
                container, false);
        if (position == 5)
            view.findViewById(R.id.btn_go_welcome).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onButtonClicked.onFinalButtonClicked();
                }
            });
        return view;
    }
}