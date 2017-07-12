package com.sadek.apps.freelance7rfeen.widget;

/**
 * Created by Mahmoud Sadek on 7/12/2017.
 */

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new FreelanceViewsFactory(this.getApplicationContext(),
                intent));
    }
}