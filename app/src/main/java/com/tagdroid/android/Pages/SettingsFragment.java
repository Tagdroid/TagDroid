package com.tagdroid.android.Pages;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;

import java.util.Locale;

public class SettingsFragment extends Page implements RadioGroup.OnCheckedChangeListener{
    private TextView langue;
    @Override
    public String getTitle() {
        return getString(R.string.settings);
    }

    @Override
    public Integer getMenuId() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("page", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        langue = (TextView) view.findViewById(R.id.language);
        langue.setText(Locale.getDefault().getDisplayLanguage().toUpperCase());

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroupe);
        radioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Locale locale;
        switch(checkedId) {
            case R.id.radioAuto:
                locale = Locale.FRANCE;
                break;
            case R.id.radioFR:
                locale = Locale.FRANCE;
                break;
            case R.id.radioEN:
                locale = Locale.ENGLISH;
                break;
            case R.id.radioES:
                locale = new Locale("es","ES");
                break;
            case R.id.radioIT:
                locale = Locale.ITALIAN;
                break;
            default:
                locale = new Locale("fr_FR");
                break;
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getApplicationContext().getResources().updateConfiguration(config, null);
        langue.setText(Locale.getDefault().getDisplayLanguage().toUpperCase());
    }


}
