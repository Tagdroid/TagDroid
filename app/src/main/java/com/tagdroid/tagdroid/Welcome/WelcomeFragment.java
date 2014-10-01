package com.tagdroid.tagdroid.Welcome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tagdroid.tagdroid.R;

import java.lang.reflect.Field;

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
        try {
            int ViewId = R.layout.class.getField("welcome_" + (position + 1)).getInt(null);
            View view = inflater.inflate(ViewId, container, false);
            if (position == 5)
                view.findViewById(R.id.btn_go_welcome).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) { onButtonClicked.onFinalButtonClicked(); }
                });
            return view;
        }
        catch (Exception e) {
            Log.e("MyTag", "Failure to get drawable id.", e);
            return null;
        }
    }
}