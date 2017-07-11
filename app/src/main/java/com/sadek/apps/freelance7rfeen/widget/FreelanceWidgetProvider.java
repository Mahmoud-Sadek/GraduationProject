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

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        String description = "FreeLance";

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget;
            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, description);
            }

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(context, SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.back, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.back, description);
    }
}