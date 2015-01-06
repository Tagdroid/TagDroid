package com.tagdroid.android.Pages;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;

import java.io.File;
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

        CardView reload = (CardView) view.findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Chargement des données",Toast.LENGTH_LONG).show();
            }
        });

        CardView clear = (CardView) view.findViewById(R.id.clear_data);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Données cache supprimées",Toast.LENGTH_LONG).show();
                clearApplicationData();
            }
        });
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
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, dm);
        langue.setText(Locale.getDefault().getDisplayLanguage().toUpperCase());
    }


    public void clearApplicationData(){
        File cache = getActivity().getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));Log.i("TAG", "**************** File /data/data/com.tagdroid.android/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
    }
        return dir.delete();
    }

}
