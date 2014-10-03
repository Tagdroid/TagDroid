package com.tagdroid.tagdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.tagdroid.tagdroid.ImageLoader.LazyAdapter;
import com.tagdroid.tagdroid.RssReader.RssFeed;
import com.tagdroid.tagdroid.RssReader.RssItem;
import com.tagdroid.tagdroid.RssReader.RssReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ActuFragment extends Fragment implements OnItemClickListener {
    public static String KEY_TITRE = "titre";
    public static String KEY_DESCRIPTION = "description";
    public static String KEY_IMAGE = "";
    private static View view;
    public final String RSSURL_TAG = "http://www.tag.fr/rss_evenement.php";
    public final String RSSURL_SMTC = "http://www.smtc-grenoble.org/actualites-rss";
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    private Tracker tracker;
    private Activity mActivity;
    private RssFeed feed = null;
    private ProgressDialog progressDial;
    private String actual_flux = "tag";
    private Bundle mBundle;

    public static ActuFragment newInstance() {
        return new ActuFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.actu, container, false);
        } catch (InflateException e) {
        }

        MainActivity.mTitle = getActivity().getResources().getString(R.string.news);
        getActivity().getActionBar().setTitle(MainActivity.mTitle);

        Affichage_RSS rss = new Affichage_RSS();
        rss.execute(RSSURL_TAG);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        setHasOptionsMenu(true);
        mActivity = getActivity();
        this.tracker = EasyTracker.getInstance(this.getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        this.tracker.send(MapBuilder.createAppView().build());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_menu, menu);
        inflater.inflate(R.menu.menu_actu, menu);
    }

    private void displayRSS() {
        ListView itemlist = (ListView) view.findViewById(R.id.itemlist);
        HashMap<String, String> map;
        String[] url_image;
        listItem.clear();

        ArrayList<RssItem> rssItems = feed.getRssItems();
        for (RssItem rssItem : rssItems) {
            if (actual_flux.contains("tag")) {
                String[] description1 = rssItem.getDescription().split(">");
                map = new HashMap<String, String>();
                map.put(KEY_TITRE, rssItem.getTitle());
                map.put(KEY_DESCRIPTION, description1[1]);

                if (description1[0].substring(10).contains("jpg")) {
                    url_image = description1[0].substring(10).replace("IMF_VIGNETTEALAUNE", "IMF_LARGE").split("jpg");
                    map.put(KEY_IMAGE, "http://www.tag.fr" + url_image[0] + "jpg");
                } else {
                    url_image = (description1[0].substring(10).replace("IMF_VIGNETTEALAUNE", "IMF_LARGE").split("png"));
                    map.put(KEY_IMAGE, "http://www.tag.fr" + url_image[0] + "png");
                }
                listItem.add(map);
            } else if (actual_flux.contains("smtc")) {
                String[] description2;
                description2 = rssItem.getDescription().substring(142).split("\" width");
                map = new HashMap<String, String>();
                map.put(KEY_TITRE, rssItem.getTitle());
                map.put(KEY_DESCRIPTION, description2[1].substring(180)
                        .replace("<strong>", "")
                        .replace("</strong>", "")
                        .replace("<br />", "\n")
                        .replace("<span style=\"font-size:12px;\">", "")
                        .replace("</span>", "")
                        .replace("</p>", ""));
                map.put(KEY_IMAGE, description2[0]);

                listItem.add(map);
            }
        }

        adapter = new LazyAdapter(mActivity, listItem);

        itemlist.setDivider(null);
        itemlist.setAdapter(adapter);
        itemlist.setOnItemClickListener(this);
        itemlist.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final CharSequence[] items_rss = {"TAG", "SMTC"};

        AlertDialog.Builder builder_rss = new AlertDialog.Builder(getActivity());
        builder_rss.setTitle(R.string.fluxchoice);
        builder_rss.setSingleChoiceItems(items_rss, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        actual_flux = "tag";
                        Affichage_RSS rss2 = new Affichage_RSS();
                        rss2.execute(RSSURL_TAG);
                        dialog.cancel();
                        break;
                    case 1:
                        actual_flux = "smtc";
                        Affichage_RSS rss3 = new Affichage_RSS();
                        rss3.execute(RSSURL_SMTC);
                        dialog.cancel();

                        break;
                }
            }
        });
        final AlertDialog alert_rss = builder_rss.create();
        switch (item.getItemId()) {
            case R.id.menu_rss:
                alert_rss.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressWarnings("rawtypes")
    public void onItemClick(AdapterView parent, View v, int position, long id) {
        MainActivity.flux = actual_flux;
        MainActivity.title = feed.getRssItems().get(position).getTitle();
        MainActivity.description = feed.getRssItems().get(position).getDescription();
        MainActivity.link = feed.getRssItems().get(position).getLink();
        MainActivity.intent = "actu";
        MainActivity.TITLES = new String[]{"RSS_DetailsFragment"};
        MainActivity.adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    private class Affichage_RSS extends AsyncTask<String, Void, RssFeed> {
        protected void onPreExecute() {
            super.onPreExecute();
            progressDial = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading), true);
        }

        protected void onProgressUpdate() {
        }

        @Override
        protected RssFeed doInBackground(String... params) {
            Bundle bundle = new Bundle();
            try {
                return RssReader.read(new URL(params[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(RssFeed result) {
            progressDial.dismiss();
            feed = result;
            if (feed != null) displayRSS();
            else {
                Toast.makeText(getActivity(), "Connexion internet indisponible", Toast.LENGTH_LONG).show();
            }
        }
    }


}
  
 

