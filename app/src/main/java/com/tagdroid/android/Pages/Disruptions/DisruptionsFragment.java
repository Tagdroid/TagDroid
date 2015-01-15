package com.tagdroid.android.Pages.Disruptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.HttpGet.HttpGetDatabaseDisruption;
import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.ReadSQL;

public class DisruptionsFragment extends Page implements ProgressionInterface {
    private ProgressDialog progression;
    HttpGetDatabaseDisruption httpGetDatabaseDisruption;

    @Override
    public String getTitle() {
        return getString(R.string.trafic);
    }

    @Override
    public Integer getMenuId() {
        return R.menu.menu_actu;
    }


    public DisruptionsFragment() {
        getDetailsFromSQL();
    }

    private void getDetailsFromSQL() {
        Log.d("Disruptions", "getDetailsFromSQL");
        httpGetDatabaseDisruption = new HttpGetDatabaseDisruption(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disruptions_fragment, container, false);

        startDownloadTask();
        return view;
    }

    public void startDownloadTask() {
        // We check if we are able to download DB
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            Log.d("Disruptions", "Internet connection problem");

        } else {
            Log.d("Disruptions", "Start of Downloading database");
            httpGetDatabaseDisruption.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rss_reload:
                break;
            case R.id.menu_rss:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onDownloadStart() {
        progression = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.loading), true);
    }

    @Override
    public void onDownloadFailed(Exception e) {
        progression.dismiss();
    }

    @Override
    public void onDownloadProgression(int progression, int total) {

    }

    @Override
    public void onDownloadComplete() {
        progression.dismiss();
        String text = ReadSQL.getAllDisruptions(getActivity()).size()+"";
        Log.d("### DISRUPTIONS !!!!!!", text);
    }


}
