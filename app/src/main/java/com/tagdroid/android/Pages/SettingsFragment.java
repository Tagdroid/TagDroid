package com.tagdroid.android.Pages;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;

import java.io.File;

public class SettingsFragment extends Page{
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

        CardView reload = (CardView) view.findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),getString(R.string.database_download),Toast.LENGTH_LONG).show();
            }
        });

        CardView clear = (CardView) view.findViewById(R.id.clear_data);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),getString(R.string.cache_cleared),Toast.LENGTH_LONG).show();
                clearApplicationData();
            }
        });
        return view;
    }

    public void clearApplicationData(){
        File cache = getActivity().getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/com.tagdroid.android/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
        String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
    }
        return dir.delete();
    }

}
