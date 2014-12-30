package com.tagdroid.android.Pages.Actualites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.Actualites.Actualite;
import com.tagdroid.tagapi.Actualites.Flux;
import com.tagdroid.tagapi.Actualites.HttpGetActualites;
import com.tagdroid.tagapi.ProgressionInterface;

import java.util.ArrayList;
import java.util.List;

public class ActualitesFragment extends Page implements ProgressionInterface, ActualiteAdapter.OnItemClickListener {
    private List<Actualite> actualitésList;

    private int RSSChannel;
    private ProgressDialog progression;

    private HttpGetActualites httpGetActualites;
    RecyclerView actuCardList;

    @Override
    public String getTitle() {
        return getString(R.string.news);
    }

    @Override
    public Integer getMenuId() {
        return R.menu.menu_actu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actualites_fragment, container, false);
        RSSChannel = getRSSChannel();
        httpGetActualites = new HttpGetActualites(RSSChannel, this);

        actuCardList = (RecyclerView) view.findViewById(R.id.actuCardList);
        actuCardList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (actualitésList == null) {
            actuCardList.setAdapter(new ActualiteAdapter(new ArrayList<Actualite>(),getActivity()));
            httpGetActualites.execute();
        }
        return view;
    }

    private int getRSSChannel() {
        return getActivity().getSharedPreferences("RSS", 0).getInt("RSSChannel", 0);
    }

    private void setRSSChannel(int RSSChannel) {
        getActivity().getSharedPreferences("RSS", 0).edit().putInt("RSSChannel", RSSChannel).apply();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rss_reload:
                httpGetActualites.execute();
                break;
            case R.id.menu_rss:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.fluxchoice)
                        .setSingleChoiceItems(Flux.getRssNames(), getRSSChannel(),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int NewRSSChannel) {
                                        dialog.cancel();
                                        if (NewRSSChannel != RSSChannel) {
                                            RSSChannel = NewRSSChannel;
                                            setRSSChannel(RSSChannel);
                                            httpGetActualites.execute();
                                        }
                                    }
                                }).create().show();
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
        actuCardList.setAdapter(new ActualiteAdapter(new ArrayList<Actualite>(),getActivity()));
        getActivity().findViewById(R.id.noNews).setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "Erreur de chargement :\n" + e.getLocalizedMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDownloadComplete() {
        progression.dismiss();
        actualitésList = httpGetActualites.getResult();
        ActualiteAdapter actualiteAdapter = new ActualiteAdapter(actualitésList, getActivity());
        actualiteAdapter.setOnItemClickListener(this);
        actuCardList.setAdapter(actualiteAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Actualite actualité = actualitésList.get(position);

        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();

        ActualitesDetailsFragment actualitesDetailsFragment = ActualitesDetailsFragment.newInstance(RSSChannel,
                actualité.titre,
                actualité.description,
                actualité.url,
                actualité.image);

        fragmentTransaction.replace(R.id.pager, actualitesDetailsFragment);
        changeFragmentInterface.onChangeFragment(actualitesDetailsFragment);

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack("activePage");
        fragmentTransaction.commit();
    }
}
