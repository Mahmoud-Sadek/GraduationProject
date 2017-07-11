package com.sadek.apps.freelance7rfeen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.adapter.ReviewsAdapter;
import com.sadek.apps.freelance7rfeen.model.ClientProfile;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;

import static com.sadek.apps.freelance7rfeen.parse.ParseJSON.clientProfile;

/**
 * Created by Mahmoud Sadek on 4/28/2017.
 */

public class DetailProfileClientFragment extends Fragment {

    static Context mcontext;
    static RecyclerView mClientsRecyclerView;
    static View view;
    public static DetailProfileClientFragment getInstance(Context context) {
        DetailProfileClientFragment myFragment = new DetailProfileClientFragment();
        mcontext = context;
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail_profile_client, container, false);

        mClientsRecyclerView =(RecyclerView) view.findViewById(R.id.reviews_recyclerView);
        RecyclerView.LayoutManager layoutManager=new StaggeredGridLayoutManager(1,1);
        mClientsRecyclerView.setLayoutManager(layoutManager);
        mClientsRecyclerView.setHasFixedSize(true);
        DetailProfileClientFragment.setData(clientProfile);
        return view;
    }

    static TextView yearEx, detail_job, place, education, experience, skills;
    static RatingBar review ;

    public static void setData(ClientProfile clientProfile) {
        yearEx = (TextView) view.findViewById(R.id.txt_client_year);
        detail_job = (TextView) view.findViewById(R.id.detail_job);
        place = (TextView) view.findViewById(R.id.txt_client_place);
        education = (TextView) view.findViewById(R.id.txt_client_education);
        experience = (TextView) view.findViewById(R.id.txt_client_khebra);
        skills = (TextView) view.findViewById(R.id.txt_client_talent);

        yearEx.setText(clientProfile.getYears_experience()+"");
        skills.setText(clientProfile.getSkills()+"");
        education.setText(clientProfile.getEducation()+"");
        experience.setText(clientProfile.getExperience()+"");
        place.setText(clientProfile.getAddress()+"");
        ReviewsAdapter adapter=new ReviewsAdapter(mcontext, ParseJSON.ratingList);
        mClientsRecyclerView.setAdapter(adapter);
    }
}