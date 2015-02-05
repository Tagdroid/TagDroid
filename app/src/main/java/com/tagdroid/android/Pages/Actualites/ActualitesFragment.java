package com.tagdroid.android.Pages.Actualites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private ActualiteAdapter actualiteAdapter;

    private int RSSChannel;

    private HttpGetActualites httpGetActualites;
    RecyclerView actuCardList;
    SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.actuCardSwipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActualites();
            }
        });


        actuCardList = (RecyclerView) view.findViewById(R.id.actuCardList);
        actuCardList.setLayoutManager(new LinearLayoutManager(getActivity()));
        actualiteAdapter = new ActualiteAdapter(new ArrayList<Actualite>(),getActivity());
        actuCardList.setAdapter(actualiteAdapter);

        if (actualitésList == null)
            refreshActualites();

        return view;
    }

    private void refreshActualites() {
        swipeRefreshLayout.setRefreshing(true);
        httpGetActualites = new HttpGetActualites(RSSChannel, this);
        httpGetActualites.execute();
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
    }

    @Override
    public void onDownloadFailed(Exception e) {
        actuCardList.setAdapter(new ActualiteAdapter(new ArrayList<Actualite>(),getActivity()));
        getActivity().findViewById(R.id.noNews).setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), getString(R.string.loading_error) + e.getLocalizedMessage(),
                Toast.LENGTH_LONG).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDownloadProgression(int progression, int total) {

    }

    @Override
    public void onDownloadComplete() {
        actualitésList = httpGetActualites.getResult();
        actualiteAdapter = new ActualiteAdapter(actualitésList, getActivity());
        actualiteAdapter.setOnItemClickListener(this);
        actuCardList.removeAllViews();
        actuCardList.removeAllViewsInLayout();
        actuCardList.setAdapter(actualiteAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        Actualite actualité = actualitésList.get(position);

        FragmentTransaction fragmentTransaction = getActivity()
                .getFragmentManager().beginTransaction();

        ActualitesDetailsFragment actualitesDetailsFragment = ActualitesDetailsFragment.newInstance(RSSChannel,
                actualité.titre,
                actualité.description,
                actualité.url,
                actualité.image);

        changeFragmentInterface.onChangeFragment(actualitesDetailsFragment);

        fragmentTransaction.setCustomAnimations(R.anim.fragment_fadein, R.anim.fragment_fadeout, R.anim.fragment_fadein, R.anim.fragment_fadeout);
        fragmentTransaction.replace(R.id.pager, actualitesDetailsFragment);
        fragmentTransaction.commit();
    }
}
