package com.tagdroid.tagapi.Actualites;

import android.os.AsyncTask;

import com.tagdroid.tagapi.ProgressionInterface;
import com.tagdroid.tagapi.RssReader.RssFeed;
import com.tagdroid.tagapi.RssReader.RssItem;
import com.tagdroid.tagapi.RssReader.RssReader;

import java.util.ArrayList;
import java.util.List;

public class HttpGetActualites extends AsyncTask<Void, Void, Integer> {
    protected ProgressionInterface progressionInterface;
    private int FluxId;

    private Exception exception;

    private List<Actualite> ActualitésList = new ArrayList<>();

    public HttpGetActualites(int FluxId, ProgressionInterface progressionInterface) {
        this.progressionInterface = progressionInterface;
        this.FluxId = FluxId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressionInterface.onDownloadStart();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        RssFeed rssFeed;

        try {
            rssFeed = RssReader.read(Flux.getRSSUrl(FluxId));
        } catch (Exception e) {
            exception = e;
            return -1;
        }

        switch (FluxId) {
            case 0:
                for (RssItem rssItem : rssFeed.getRssItems()) {
                    String[] descriptions = rssItem.getDescription().split(">");
                    String description = descriptions[1];
                    String image = "http://www.tag.fr"
                            +descriptions[0].split("src=\"")[1].split("\"")[0]
                            .replace("IMF_VIGNETTEALAUNE", "IMF_LARGE");

                    ActualitésList.add(
                            new Actualite(rssItem.getTitle(), rssItem.getLink(), description, image, FluxId)
                    );
                }
                break;
            case 1:
                for (RssItem rssItem : rssFeed.getRssItems()) {
                    String description = rssItem.getDescription();
                    //Log.d("description ", description);
                    /*description = description
                            .split("<span")[1]
                            .split("&lt;/span")[0]
                            .replace("<em>", "").replace("</em>", "")
                            .replace("<strong>", "").replace("</strong>", "")
                            .replace("<br />", "\n")
                            .replace("<span style=\"font-size:12px;\">", "").replace("</span>", "")
                            .replace("</p>", "");*/

                    String image = rssItem.getDescription().split("img src=\"")[1].split("\"")[0];

                    ActualitésList.add(
                            new Actualite(rssItem.getTitle(), rssItem.getLink(), description, image, FluxId)
                    );
                }
                break;
        }
        return FluxId;
    }

    @Override
    protected void onPostExecute(Integer resultState) {
        if (resultState >= 0)
            progressionInterface.onDownloadComplete("");
        else
            progressionInterface.onDownloadFailed(exception);
    }

    public List<Actualite> getResult() {
        return ActualitésList;
    }
}
