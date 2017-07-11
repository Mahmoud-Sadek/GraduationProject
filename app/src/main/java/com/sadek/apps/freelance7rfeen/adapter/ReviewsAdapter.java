package com.sadek.apps.freelance7rfeen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.model.Rating;

import java.util.List;

/**
 * Created by Esraa Hosny on 3/23/2017.
 */


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.postViewHolder> {

    private List<Rating> model;
    Context mContext;
    private int lastPosition = -1;


    public ReviewsAdapter(Context mContext, List<Rating> model) {
        this.model = model;
        this.mContext = mContext;
    }

    @Override
    public ReviewsAdapter.postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);

        return new ReviewsAdapter.postViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.postViewHolder viewHolder, int position) {
        Rating review = model.get(position);
        setAnimation(viewHolder.myView, position);
        viewHolder.date.setText(review.getDate());
        viewHolder.summary.setText(review.getWork_summary());
        viewHolder.time.setRating(review.getCompletion_time());
        viewHolder.deadline.setRating(review.getDeadline());
        viewHolder.clean.setRating(review.getClean_work());
        viewHolder.prefiction.setRating(review.getWork_perfection());
        viewHolder.price.setRating(review.getPrice());
        viewHolder.reviewRate.setRating(review.getReview());
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
        TextView name, summary, date;
        RatingBar reviewRate, time, deadline, prefiction, price, clean;
        public postViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            name = (TextView) myView.findViewById(R.id.reviewer_name);
            date = (TextView) myView.findViewById(R.id.review_date);
            summary = (TextView) myView.findViewById(R.id.reviewer_summary);
            reviewRate = (RatingBar) myView.findViewById(R.id.review);
            time = (RatingBar) myView.findViewById(R.id.rate_work_completion);
            deadline = (RatingBar) myView.findViewById(R.id.rate_work_deadline);
            prefiction = (RatingBar) myView.findViewById(R.id.rate_work_prefiction);
            price = (RatingBar) myView.findViewById(R.id.rate_work_price);
            clean = (RatingBar) myView.findViewById(R.id.rate_work_clean);
        }
    }
}