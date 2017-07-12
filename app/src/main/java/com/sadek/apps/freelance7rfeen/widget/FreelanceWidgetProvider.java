package com.sadek.apps.freelance7rfeen.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.activities.SpecializationActivity;
import com.sadek.apps.freelance7rfeen.activities.SplashActivity;
import com.sadek.apps.freelance7rfeen.activities.SubSpecializationActivity;


public class FreelanceWidgetProvider extends AppWidgetProvider {
    public static String EXTRA_WORD =
            "com.sadek.apps.freelance7rfeen.WORD";

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent svcIntent = new Intent(ctxt, WidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget = new RemoteViews(ctxt.getPackageName(),
                    R.layout.widget);

            widget.setRemoteAdapter(appWidgetIds[i], R.id.words,
                    svcIntent);

            Intent clickIntent = new Intent(ctxt, SplashActivity.class);
            PendingIntent clickPI = PendingIntent
                    .getActivity(ctxt, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setPendingIntentTemplate(R.id.words, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }
}