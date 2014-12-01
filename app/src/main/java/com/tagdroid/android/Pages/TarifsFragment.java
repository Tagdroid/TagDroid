package com.tagdroid.android.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;

public class TarifsFragment extends Page {
    @Override
    public String getTitle() {
        return getString(R.string.tarif);
    }

    @Override
    public Integer getMenuId() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tarif, container, false);


        return view;
    }

}
