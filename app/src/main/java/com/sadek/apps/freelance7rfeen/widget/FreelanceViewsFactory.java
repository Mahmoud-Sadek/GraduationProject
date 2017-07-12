package com.sadek.apps.freelance7rfeen.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

/**
 * Created by Mahmoud Sadek on 7/12/2017.
 */
public class FreelanceViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String[] items= ConstantsFreelance.SPECIALTY_NAME;

    private Context ctxt=null;
    private int appWidgetId;

    public FreelanceViewsFactory(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(items.length);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_item);

        row.setTextViewText(android.R.id.text1, items[position]);

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putInt(FreelanceWidgetProvider.EXTRA_WORD, position);
        i.putExtras(extras);
        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }
}