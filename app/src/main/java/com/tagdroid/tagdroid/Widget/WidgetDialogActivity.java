package com.tagdroid.tagdroid.Widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tagdroid.tagapi.JSonApi.Favori;
import com.tagdroid.tagdroid.Favoris.FavorisHelper;
import com.tagdroid.tagdroid.R;

public class WidgetDialogActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.widgetdialog_activity);
        ListView listViewFavoris = (ListView) findViewById(R.id.listViewfavoris2);

        final FavorisHelper favorisHelper = new FavorisHelper(this);

        if (favorisHelper.getFavorisAdaptedArray().isEmpty()) {
            TextView text_fav = (TextView) findViewById(R.id.text_fav);
            text_fav.setText(getResources().getString(R.string.nofavs));
            ImageView img_fav = (ImageView) findViewById(R.id.img_fav);
            img_fav.setBackgroundResource(R.drawable.favoris2);
        } else {
            SimpleAdapter mSchedule = new SimpleAdapter(this,
                    favorisHelper.getFavorisAdaptedArray(),
                    R.layout.listitem_station,
                    new String[]{"nom", null, "ligne", "couleur"},
                    new int[]{R.id.titre, R.id.distance, R.id.station, R.id.fond_color});

            listViewFavoris.setAdapter(mSchedule);
            listViewFavoris.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Favori selectedFavori = favorisHelper.getFavoris()[position];
                    WidgetActivity.station = selectedFavori.Name;
                    String ligne = selectedFavori.Ligne;

                    RemoteViews remoteViews = new RemoteViews(getBaseContext().getPackageName(), R.layout.widget);
                    remoteViews.setTextViewText(R.id.widget_station, WidgetActivity.station);
                    try {
                        int image_ligne = R.drawable.class
                                .getField(ligne.toLowerCase().replace(" ", "") + "_pressed").getInt(null);
                        remoteViews.setImageViewResource(R.id.widget_ligne, image_ligne);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ComponentName widget = new ComponentName(getBaseContext(), WidgetActivity.class);
                    AppWidgetManager.getInstance(getBaseContext()).updateAppWidget(widget, remoteViews);
                    finish();
                }
            });
        }

    }
}