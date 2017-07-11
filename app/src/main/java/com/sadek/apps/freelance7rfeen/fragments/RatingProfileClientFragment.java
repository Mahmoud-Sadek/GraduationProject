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

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.adapter.ClientsAdapter;
import com.sadek.apps.freelance7rfeen.model.Favorite;

import java.util.ArrayList;
import java.util.List;

import static com.sadek.apps.freelance7rfeen.activities.SpecializationActivity.calculateNoOfColumns;

/**
 * Created by Mahmoud Sadek on 4/28/2017.
 */

public class RatingProfileClientFragment extends Fragment {

    static Context mcontext;
    RecyclerView mClientsRecyclerView;

    public static RatingProfileClientFragment getInstance(Context context) {
        RatingProfileClientFragment myFragment = new RatingProfileClientFragment();
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
        View view = inflater.inflate(R.layout.fragment_rating_profile_client, container, false);
        mClientsRecyclerView = (RecyclerView) view.findViewById(R.id.clients_recyclerview);
        mClientsRecyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        RecyclerView.LayoutManager layoutManager=new StaggeredGridLayoutManager(calculateNoOfColumns(getContext()),1);
        mClientsRecyclerView.setLayoutManager(layoutManager);
        List<Favorite> clientList = new ArrayList<>();
        ClientsAdapter clientsAdapter = new ClientsAdapter(getContext(), clientList);
        mClientsRecyclerView.setAdapter(clientsAdapter);

        return view;
    }
}