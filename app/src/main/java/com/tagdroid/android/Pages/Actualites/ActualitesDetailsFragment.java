package com.tagdroid.android.Pages.Actualites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.android.Page;
import com.tagdroid.android.R;

import java.io.InputStream;

public class ActualitesDetailsFragment extends Page implements View.OnClickListener {
    public static int RSSChannel;
    public static String title;
    public static String description;
    public static String url_more;
    public static String url_photo;

    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public Integer getMenuId() {
        return R.menu.menu_actu_details;
    }

    public static ActualitesDetailsFragment newInstance(int RSSChannel, String title, String description,
                                                        String url_more, String url_photo) {
        ActualitesDetailsFragment fragment = new ActualitesDetailsFragment();
        ActualitesDetailsFragment.RSSChannel = RSSChannel;
        ActualitesDetailsFragment.title = title;
        ActualitesDetailsFragment.description = description;
        ActualitesDetailsFragment.url_more = url_more;
        ActualitesDetailsFragment.url_photo = url_photo;
        return fragment;
    }

    public ActualitesDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actualites_detail, container, false);

        switch (RSSChannel) {
            case 0:
                if (url_more.contains("TPL_EVENEMENT")) {
                    url_more = url_more.replace("www.", "m.")
                            .replace("/TPL_EVENEMENT/", "/TPL_EVENEMENTMOBILE/")
                            .replace("3-en-detail.htm", "226-actualites.htm");
                } else {
                    url_more = url_more.replace("tag.fr/", "tag.fr/DISABLE_REDIRECT_MOBILE/1/");
                }
                break;
            case 1:
                break;
            default:
                break;
        }

        Log.d("PHOTO",url_photo);

        new DownloadImageTask((ImageView) view.findViewById(R.id.photo)).execute(url_photo);
        ((TextView) view.findViewById(R.id.titre)).setText(Html.fromHtml(title));
        ((TextView) view.findViewById(R.id.description)).setText(Html.fromHtml(description));


        view.findViewById(R.id.button_more).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_more:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url_more)));
                break;
            default:
                break;
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_actu_share:
                break;
        }
        return true;
    }
}
