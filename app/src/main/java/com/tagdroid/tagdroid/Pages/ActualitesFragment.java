package com.tagdroid.tagdroid.Pages;

import android.app.AlertDialog;
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

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ActualitesFragment extends Page {
    private static ArrayList<HashMap<String, String>> listItem;
    public final String[] RSSURLS = {
            "http://www.tag.fr/rss_evenement.php",
            "http://www.smtc-grenoble.org/actualites-rss"};
    ListView RSSView;

    @Override
    public String getTitle() {
        return getString(R.string.news);
    }

    @Override
    public Integer getMenuId() {
        return R.menu.menu_actu;
    }

    private int getRSSChannel() {
        return getActivity().getSharedPreferences("RSS", 0).getInt("RSSChannel", 1);
    }

    private void setRSSChannel(int RSSChannel) {
        getActivity().getSharedPreferences("RSS", 0).edit().putInt("RSSChannel", RSSChannel).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualites, container, false);
        RSSView = (ListView) view.findViewById(R.id.rss_view);
        int RSSChannel = getActivity().getSharedPreferences("RSS", 0).getInt("RSSChannel", 1);
        new displayRSSTask(RSSChannel, false).execute();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.fluxchoice)
                .setSingleChoiceItems(new CharSequence[]{"TAG", "SMTC"}, getRSSChannel(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int RSSChannel) {
                                setRSSChannel(RSSChannel);
                                dialog.cancel();
                                new displayRSSTask(RSSChannel, true).execute();
                            }
                        }).create().show();
        return true;
    }

    public class displayRSSTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog progression;
        private int RSSChannel;
        private boolean needToReload;
        private ArrayList<HashMap<String, String>> rssItemsList;
        private String stateMessage;

        displayRSSTask(int RSSFeeder, boolean forceReload) {
            this.RSSChannel = RSSFeeder;
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
                }
            } catch (IOException e) {
                stateMessage = e.getLocalizedMessage();
                return -1;
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return 0;
        }

        protected void onPostExecute(Integer resultState) {
            if (needToReload)
                progression.dismiss();

            if (resultState > 0) {
                listItem = rssItemsList;
                LazyAdapter adapter = new LazyAdapter(getActivity(), listItem);

                RSSView.setDivider(null);
                RSSView.setAdapter(adapter);
                RSSView.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //  selectItem(position, actualPosition > 0);
                    }
                });
                RSSView.setSelection(0);
            } else {
                Toast.makeText(getActivity(), "Erreur de récupération :\n" + stateMessage,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
