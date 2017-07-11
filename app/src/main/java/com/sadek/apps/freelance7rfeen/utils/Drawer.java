package com.sadek.apps.freelance7rfeen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.adapter.DrawerAdapter;

/**
 * Created by Mahmoud Sadek on 4/21/2017.
 */

public class Drawer {
    Context context;
    View v;
    public static Activity activity;
    private FlowingDrawer mDrawer;
    public static String activityName = "";

    public Drawer(Context context, View v, Activity activity, String activityName) {
        this.context = context;
        this.v = v;
        this.activity = activity;
        this.activityName = activityName;
    }

    public void makeDrawer() {
        /*v.findViewById(R.id.imageView_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
        ///////////////////////////////
        RecyclerView mDrawerListView = (RecyclerView) v.findViewById(R.id.drawerList);
        DrawerAdapter drawerAdapter = new DrawerAdapter(context);
        mDrawerListView.setHasFixedSize(true);
        mDrawerListView.setLayoutManager(new GridLayoutManager(context, 1));
        mDrawerListView.setAdapter(drawerAdapter);
        TextView name = (TextView) v.findViewById(R.id.userName);
        TextView email = (TextView) v.findViewById(R.id.textView_email);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        name.setText(sp.getString(ConstantsFreelance.USER_NAME, ""));
        email.setText(sp.getString(ConstantsFreelance.USER_EMAIL, ""));

    }
}