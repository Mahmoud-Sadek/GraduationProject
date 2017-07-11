package com.sadek.apps.freelance7rfeen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.activities.ProfileClientActivity;
import com.sadek.apps.freelance7rfeen.model.Favorite;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Mahmoud Sadek on 3/19/2017.
 */

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.postViewHolder> {

    private List<Favorite> model;
    Context mContext;
    private int lastPosition = -1;


    public ClientsAdapter(Context mContext, List<Favorite> model) {
        this.model = model;
        this.mContext = mContext;
    }

    @Override
    public postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_client, parent, false);

        return new postViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(postViewHolder viewHolder, final int position) {

        setAnimation(viewHolder.myView, position);
        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(mContext, ProfileClientActivity.class);
                ProfileClientActivity.mehani_id = model.get(position).getId();
                n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(n);
            }
        });
        viewHolder.address.setText(model.get(position).getGovernment() + "-" + model.get(position).getCity());
        viewHolder.job.setText(model.get(position).getJob());
        viewHolder.name.setText(model.get(position).getUserName());
        viewHolder.reviewRate.setRating(model.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.slide_down : R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }

    class postViewHolder extends RecyclerView.ViewHolder {
        View myView;

        TextView name, job, address;
        RatingBar reviewRate;

        public postViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            reviewRate = (RatingBar) myView.findViewById(R.id.profile_rate);
            name = (TextView) myView.findViewById(R.id.txt_client_name);
            job = (TextView) myView.findViewById(R.id.txt_job);
            address = (TextView) myView.findViewById(R.id.txt_place);
        }
    }
}