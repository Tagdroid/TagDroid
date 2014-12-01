package com.tagdroid.android.Welcome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.android.R;

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

        View welcomeFragment = inflater.inflate(R.layout.welcome_fragment, container, false);

        welcomeFragment.setBackgroundColor(
                args.getInt("color"));
        ((ImageView)welcomeFragment.findViewById(R.id.image)).setImageResource(
                args.getInt("image"));
        ((TextView)welcomeFragment.findViewById(R.id.title)).setText(
                args.getString("title"));
        ((TextView)welcomeFragment.findViewById(R.id.description)).setText(
                args.getString("descr"));

        if (position == 5) {
            welcomeFragment.findViewById(R.id.next).setVisibility(View.GONE);
            View finalButton = welcomeFragment.findViewById(R.id.final_button);
            finalButton.setVisibility(View.VISIBLE);
            finalButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) { onButtonClicked.onFinalButtonClicked(); }
                });
        }
        return welcomeFragment;
    }
}