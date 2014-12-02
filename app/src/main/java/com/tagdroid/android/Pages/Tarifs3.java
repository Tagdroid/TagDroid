package com.tagdroid.android.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;

public class Tarifs3 extends Page {
    @Override
    public String getTitle() {
        return "Page3";
    }

    @Override
    public Integer getMenuId() {
        return R.menu.menu_about;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tarif3, container, false);
        return view;
    }

}
