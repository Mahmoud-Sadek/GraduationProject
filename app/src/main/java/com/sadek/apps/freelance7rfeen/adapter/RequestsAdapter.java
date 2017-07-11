package com.sadek.apps.freelance7rfeen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.activities.ProfileClientActivity;
import com.sadek.apps.freelance7rfeen.activities.RequestDetailActivity;
import com.sadek.apps.freelance7rfeen.model.Requests;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Mahmoud Sadek on 6/27/2017.
 */
public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.postViewHolder> {

    private List<Requests> model;
    Context mContext;
    private int lastPosition = -1;


    public RequestsAdapter(Context mContext, List<Requests> model) {
        this.model = model;
        this.mContext = mContext;
    }

    @Override
    public RequestsAdapter.postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        return new RequestsAdapter.postViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestsAdapter.postViewHolder viewHolder, final int position) {

        setAnimation(viewHolder.myView, position);
        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(mContext, RequestDetailActivity.class);
                RequestDetailActivity.request_id = model.get(position).getId();
                n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(n);
            }
        });
        viewHolder.name.setText(model.get(position).getUser_name());
        viewHolder.address.setText(model.get(position).getAddress());
        viewHolder.phone.setText(model.get(position).getPhone());
        viewHolder.summary.setText(model.get(position).getContent());
        viewHolder.endDate.setText(model.get(position).getEnd_date());
        viewHolder.date.setText(model.get(position).getCreate_date());
        String status = model.get(position).getStatus();
        if (status.equals("0")){
            status = "Not Seen";
        }else if (status.equals("1")){
            status = "approved";
        }else if (status.equals("-1")){
            status = "not approved";
        }else if (status.equals("2")){
            status = "finished";
        }else if (status.equals("-2")){
            status = "Canceled";
        }
        viewHolder.statusTxt.setText(status);
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
        TextView name, summary, date, phone, address, endDate,statusTxt;

        public postViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            name = (TextView) myView.findViewById(R.id.request_name);
            date = (TextView) myView.findViewById(R.id.request_date);
            summary = (TextView) myView.findViewById(R.id.request_summary);
            phone = (TextView) myView.findViewById(R.id.txt_phone);
            endDate = (TextView) myView.findViewById(R.id.txt_date);
            address = (TextView) myView.findViewById(R.id.txt_place);
            statusTxt = (TextView) myView.findViewById(R.id.txt_status);

        }
    }
}