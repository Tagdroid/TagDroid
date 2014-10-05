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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ActualitésFragment extends Page implements AdapterView.OnItemClickListener {
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
        new downloadRSSTask(RSSChannel).execute();
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
                                new downloadRSSTask(RSSChannel).execute();
                            }
                        }).create().show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void displayRSS(ArrayList<HashMap<String, String>> RSSList) {
        LazyAdapter adapter = new LazyAdapter(getActivity(), RSSList);

        RSSView.setDivider(null);
        RSSView.setAdapter(adapter);
        RSSView.setOnItemClickListener(this);
        RSSView.setSelection(0);
    }

    public class downloadRSSTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
        int RSSChannel;
        ProgressDialog progression;

        downloadRSSTask(int RSSFeeder) {
            this.RSSChannel = RSSFeeder;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progression = ProgressDialog.show(getActivity(), "",
                    getResources().getString(R.string.loading), true);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            try {
                RssFeed RSSFeed = RssReader.read(new URL(RSSURLS[RSSChannel]));
                ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map;

                switch (RSSChannel) {
                    case 0:
                        for (RssItem rssItem : RSSFeed.getRssItems()) {
                            map = new HashMap<String, String>();
                            map.put("titre", rssItem.getTitle());
                            String[] RSSStrings = rssItem.getDescription().split("> ");
                            map.put("description", RSSStrings[1]);
                            map.put("image", "http://www.tag.fr" + RSSStrings[0]
                                            .split("src=\"")[1]
                                            .split("\"")[0]
                                            .replace("IMF_VIGNETTEALAUNE", "IMF_LARGE")
                            );
                            listItem.add(map);
                        }
                        break;
                    case 1:
                        for (RssItem rssItem : RSSFeed.getRssItems()) {
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
                            listItem.add(map);
                        }
                }
                return listItem;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            progression.dismiss();
            if (result == null)
                Toast.makeText(getActivity(), "Erreur de récupération", Toast.LENGTH_LONG).show();
            else
                displayRSS(result);
        }
    }
}
