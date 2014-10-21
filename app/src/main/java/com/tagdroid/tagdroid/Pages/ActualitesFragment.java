package com.tagdroid.tagdroid.Pages;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tagdroid.tagdroid.ImageLoader.LazyAdapter;
import com.tagdroid.tagdroid.Page;
import com.tagdroid.tagdroid.R;
import com.tagdroid.tagdroid.RssReader.RssFeed;
import com.tagdroid.tagdroid.RssReader.RssItem;
import com.tagdroid.tagdroid.RssReader.RssReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ActualitesFragment extends Page {
    private static ArrayList<HashMap<String, String>> listItem;
    public final String[] RSSURLS = {
            "http://www.tag.fr/rss_evenement.php", "http://www.smtc-grenoble.org/actualites-rss"};
    private ListView RSSView;
    private int RSSChannel;

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
        View view = inflater.inflate(R.layout.fragment_actualites, container, false);

        RSSView = (ListView) view.findViewById(R.id.rss_view);

        RSSChannel = getRSSChannel();
        new displayRSSTask(false).execute();

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
                new displayRSSTask(true).execute();
                break;
            case R.id.menu_rss:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.fluxchoice)
                        .setSingleChoiceItems(new CharSequence[]{"TAG", "SMTC"}, getRSSChannel(),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int NewRSSChannel) {
                                        dialog.cancel();
                                        if (NewRSSChannel != RSSChannel) {
                                            RSSChannel = NewRSSChannel;
                                            setRSSChannel(RSSChannel);
                                            new displayRSSTask(true).execute();
                                        }
                                    }
                                }).create().show();
                break;
        }
        return true;
    }

    public class displayRSSTask extends AsyncTask<Void, Void, Integer> implements AdapterView.OnItemClickListener {
        private ProgressDialog progression;
        private boolean needToReload;
        private ArrayList<HashMap<String, String>> rssItemsList;
        private String stateMessage;

        displayRSSTask(boolean forceReload) {
            this.needToReload = (forceReload || listItem == null);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (needToReload)
                progression = ProgressDialog.show(getActivity(), "",
                        getResources().getString(R.string.loading), true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (!needToReload)
                return 1;

            try {
                rssItemsList = new ArrayList<HashMap<String, String>>();
                RssFeed rssFeed = RssReader.read(new URL(RSSURLS[RSSChannel]));
                HashMap<String, String> map;
                switch (RSSChannel) {
                    case 0:
                        for (RssItem rssItem : rssFeed.getRssItems()) {
                            map = new HashMap<String, String>();
                            map.put("titre", rssItem.getTitle());
                            map.put("url", rssItem.getLink());
                            String[] RSSStrings = rssItem.getDescription().split("> ");
                            map.put("description", RSSStrings[1]);
                            map.put("image", "http://www.tag.fr" + RSSStrings[0]
                                            .split("src=\"")[1]
                                            .split("\"")[0]
                                            .replace("IMF_VIGNETTEALAUNE", "IMF_LARGE")
                            );
                            rssItemsList.add(map);
                        }
                        break;
                    case 1:
                        for (RssItem rssItem : rssFeed.getRssItems()) {
                            map = new HashMap<String, String>();
                            map.put("titre", rssItem.getTitle());
                            map.put("url", rssItem.getLink());
                            map.put("description", rssItem.getDescription()
                                            .split("<span[^>]*>")[1]
                                            .split("</span")[0]
                                            .replace("<em>", "").replace("</em>", "")
                                            .replace("<strong>", "").replace("</strong>", "")
                                            .replace("<br />", "\n")
                                            .replace("<span style=\"font-size:12px;\">", "").replace("</span>", "")
                                            .replace("</p>", "")
                            );
                            map.put("image", rssItem.getDescription()
                                            .split("img src=\"")[1]
                                            .split("\"")[0]
                            );
                            rssItemsList.add(map);
                        }
                        break;
                }
                return 1;
            } catch (Exception e) {
                stateMessage += e.getLocalizedMessage();
                e.printStackTrace();
                return -1;
            }
        }

        protected void onPostExecute(Integer resultState) {
            if (needToReload) {
                progression.dismiss();
                listItem = rssItemsList;
            }

            if (resultState > 0) {
                LazyAdapter adapter = new LazyAdapter(getActivity(), listItem);
                RSSView.setDivider(null);
                RSSView.setAdapter(adapter);
                RSSView.setOnItemClickListener(this);
                RSSView.setSelection(0);


            } else {
                Toast.makeText(getActivity(), "Erreur de récupération :\n" + stateMessage,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> selectedItem = listItem.get(position);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            ActualitesDetailsFragment actualitesDetailsFragment = ActualitesDetailsFragment.newInstance(RSSChannel,
                    selectedItem.get("titre"),
                    selectedItem.get("description"),
                    selectedItem.get("url"),
                    selectedItem.get("image"));


            fragmentTransaction.replace(R.id.pager,actualitesDetailsFragment);
            changeFragmentInterface.onChangeFragment(actualitesDetailsFragment);

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack("activePage");
            fragmentTransaction.commit();
        }
    }
}
