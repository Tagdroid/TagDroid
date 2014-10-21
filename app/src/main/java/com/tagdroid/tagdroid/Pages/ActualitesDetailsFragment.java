package com.tagdroid.tagdroid.Pages;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.tagdroid.Page;
import com.tagdroid.tagdroid.R;

import java.io.InputStream;

public class ActualitesDetailsFragment extends Page { //implements View.OnClickListener {
    private Integer RSSChannel;
    private String title;
    private String description;
    private String url_more;
    private String url_photo;

    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public Integer getMenuId() {
        return R.menu.menu_actu_details;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_actu_share:
                break;
        }
        return true;
    }

    public static ActualitesDetailsFragment newInstance(Integer RSSChannel, String title, String description,
                                                        String url_more, String url_photo) {
        ActualitesDetailsFragment fragment = new ActualitesDetailsFragment();
        fragment.RSSChannel = RSSChannel;
        fragment.title = title;
        fragment.description = description;
        fragment.url_more = url_more;
        fragment.url_photo = url_photo;
        return fragment;
    }

    public ActualitesDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualites_details, container, false);
        Button plus =(Button) view.findViewById(R.id.bouton_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url_more)));
            }
        });

        switch (RSSChannel) {
            case 0:
                url_more = url_more.replace("www.", "m.")
                        .replace("/TPL_EVENEMENT/", "/TPL_EVENEMENTMOBILE/")
                        .replace("3-en-detail.htm","226-actualites.htm");
                break;
            case 1:
                break;
            default:
                break;
        }

        new DownloadImageTask((ImageView) view.findViewById(R.id.photo))
                .execute(url_photo);
        ((TextView) view.findViewById(R.id.description))
                .setText(Html.fromHtml(description));
        return view;
    }


    // Je sais pas pourquoi mais ça marchait pas :p
    /*public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bouton_plus:
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url_more)));
                Log.d("Bouton plus","PRESSED");
                break;
            default:
                break;
        }
    }*/


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


}
