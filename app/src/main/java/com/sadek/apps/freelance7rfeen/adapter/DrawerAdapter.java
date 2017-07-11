package com.sadek.apps.freelance7rfeen.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.activities.AboutUsActivity;
import com.sadek.apps.freelance7rfeen.activities.ChooseLocationActivity;
import com.sadek.apps.freelance7rfeen.activities.FavoriteActivity;
import com.sadek.apps.freelance7rfeen.activities.LoginActivity;
import com.sadek.apps.freelance7rfeen.activities.ProfileUserActivity;
import com.sadek.apps.freelance7rfeen.activities.RecivedRequestActivity;
import com.sadek.apps.freelance7rfeen.activities.SendRequestActivity;
import com.sadek.apps.freelance7rfeen.activities.SettingsActivity;
import com.sadek.apps.freelance7rfeen.activities.SpecializationActivity;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.utils.Drawer;

import java.util.List;


/**
 * Created by Sadokey on 12/22/2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    int[] photo, text;
    int[] photo1 = {R.drawable.ic_home_white_48dp, R.drawable.profile, android.R.drawable.ic_dialog_email, R.drawable.fav, R.drawable.am_worker, android.R.drawable.ic_menu_manage, R.drawable.logout};
    int[] text1 = {R.string.home, R.string.profile, R.string.sending_requests, R.string.my_favorite, R.string.aboutus, R.string.setting, R.string.logout};
    int[] photo2 = {R.drawable.ic_home_white_48dp, R.drawable.profile, android.R.drawable.ic_dialog_email, android.R.drawable.ic_menu_call, R.drawable.fav, R.drawable.am_worker, android.R.drawable.ic_menu_manage, android.R.drawable.ic_dialog_map, R.drawable.logout};
    int[] text2 = {R.string.home, R.string.profile, R.string.sending_requests, R.string.comming_requests, R.string.my_favorite, R.string.aboutus, R.string.setting,R.string.update_location, R.string.logout};
    private Context mContext;

    int id;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        ImageView img_poster;

        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.mnu_txt);
            img_poster = (ImageView) view.findViewById(R.id.mnu_photo);
        }
    }

    public DrawerAdapter(Context mContext) {
        this.mContext = mContext;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        id = sp.getInt(ConstantsFreelance.USER_TYPE, 3);
        if (id == 0) {
            photo = photo1;
            text = text1;
        } else {
            photo = photo2;
            text = text2;
        }
    }

    View itemView;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.img_poster.setImageResource(photo[position]);
        holder.txt_title.setText(text[position]);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawers(position);
            }
        });
    }

    private void drawers(int position) {
        if (position == 0) {
            if (!Drawer.activityName.equals("SpecializationActivity")) {
                Intent intent = new Intent(mContext, SpecializationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        }
        if (position == 1) {
            Intent intent = new Intent(mContext, ProfileUserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        if (position == 2) {
            if (!Drawer.activityName.equals("SendRequestActivity")) {
                Intent intent = new Intent(mContext, SendRequestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }

        if (id == 0) {
            if (position == 3) {
                if (!Drawer.activityName.equals("FavoriteActivity")) {
                    Intent intent = new Intent(mContext, FavoriteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }

            if (position == 4) {
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

            if (position == 5) {
                Intent intent = new Intent(mContext, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            if (position == 6) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor spe = sp.edit();
                spe.putInt(ConstantsFreelance.USER_TYPE, 3).apply();
                spe.putInt(ConstantsFreelance.USE_ID, 0).apply();
                spe.commit();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                spe.putString(ConstantsFreelance.USER_NAME, "").apply();
                spe.putString(ConstantsFreelance.USER_EMAIL, "").apply();
                mContext.startActivity(intent);
                Drawer.activity.finish();
            }
        } else {
            if (position == 3) {
                if (!Drawer.activityName.equals("RecivedRequestActivity")) {
                    Intent intent = new Intent(mContext, RecivedRequestActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
            if (position == 4) {
                if (!Drawer.activityName.equals("FavoriteActivity")) {
                    Intent intent = new Intent(mContext, FavoriteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }

            if (position == 5) {
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

            if (position == 6) {
                Intent intent = new Intent(mContext, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            if (position == 7) {
                Intent intent = new Intent(mContext, ChooseLocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            if (position == 8) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor spe = sp.edit();
                spe.putInt(ConstantsFreelance.USER_TYPE, 3).apply();
                spe.putInt(ConstantsFreelance.USE_ID, 0).apply();
                spe.putString(ConstantsFreelance.USER_NAME, "").apply();
                spe.putString(ConstantsFreelance.USER_EMAIL, "").apply();
                spe.commit();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                Drawer.activity.finish();
            }
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return photo.length;
    }


}