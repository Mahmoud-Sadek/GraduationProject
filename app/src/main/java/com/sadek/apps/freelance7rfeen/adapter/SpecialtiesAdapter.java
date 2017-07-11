package com.sadek.apps.freelance7rfeen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.activities.SubSpecializationActivity;
import com.sadek.apps.freelance7rfeen.model.Speciality;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by Mahmoud Yahia on 3/12/2017.
 */

public class SpecialtiesAdapter extends RecyclerView.Adapter<SpecialtiesAdapter.postViewHolder> {

    private List<Speciality> model;
    Context mContext;
    private int lastPosition = -1;

    public SpecialtiesAdapter(Context mContext, List<Speciality> model) {
        this.model = model;
        this.mContext = mContext;
    }

    @Override
    public SpecialtiesAdapter.postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sapecialization_list_item, parent, false);

        return new SpecialtiesAdapter.postViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecialtiesAdapter.postViewHolder viewHolder, final int position) {

        setAnimation(viewHolder.myView, position);
        viewHolder.setName(model.get(position).getName());
        viewHolder.setImage(model.get(position).getImg());
        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ListItemsActivity.categorie_photo = model.get(position).getImg();
//                ListItemsActivity.categorie = model.get(position).getItem_name();
                SubSpecializationActivity.categorie = model.get(position);
                SubSpecializationActivity.categoryNum = position;
                Intent n = new Intent(mContext, SubSpecializationActivity.class);
                n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(n);
            }
        });
//        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((FoldingCell) v).toggle(false);
//                // register in adapter that state for selected cell is toggled
//                registerToggle(position);
//            }
//        });
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

        public postViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }


        void setName(String Title) {
            TextView txt_title = (TextView) myView.findViewById(R.id.specialty_item_text);
            txt_title.setText(Title);
        }

        void setImage(int profileImage) {
            ImageView img_profile = (ImageView) myView.findViewById(R.id.speciality_image_item);
            Picasso.with(mContext)
                    .load(profileImage)
                    .error(R.drawable.am_worker)         // optional
                    .into(img_profile);
        }

    }

}
