package com.tagdroid.android.Widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.tagdroid.android.R;

import java.util.Random;

public class UpdateWidgetService extends Service {
  private static final String LOG = "com.tagdroid.android.example";

  @Override
  public void onStart(Intent intent, int startId) {
    Log.i(LOG, "Called");
    // Create some random data

    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
        .getApplicationContext());

    int[] allWidgetIds = intent
        .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

    ComponentName thisWidget = new ComponentName(getApplicationContext(),WidgetActivity.class);
    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

    for (int widgetId : allWidgetIds) {
      int number = (new Random().nextInt(100));
      RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget);
      remoteViews.setTextViewText(R.id.widget_vers, "Random: " + String.valueOf(number));
      Intent clickIntent = new Intent(this.getApplicationContext(),WidgetActivity.class);
      clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.widget_vers, pendingIntent);
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }
    stopSelf();

    super.onStart(intent, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
} 