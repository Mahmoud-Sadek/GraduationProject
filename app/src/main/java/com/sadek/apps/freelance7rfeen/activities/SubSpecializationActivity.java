package com.sadek.apps.freelance7rfeen.activities;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.adapter.SubSpecialtiesAdapter;
import com.sadek.apps.freelance7rfeen.model.Speciality;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.utils.Drawer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SubSpecializationActivity extends AppCompatActivity {

    public static Speciality categorie;
    private FlowingDrawer mDrawer;
    static RecyclerView mClientsRecyclerView;
    static Context mContext;
    public static int categoryNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_sub_specialization);
        initLayout();
    }

    private void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("نجار");
        initCollapsingToolbar();
        mContext = getBaseContext();
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, SubSpecializationActivity.this, "");
        drawer.makeDrawer();
        mClientsRecyclerView = (RecyclerView) findViewById(R.id.sub_specialization_recyclerview);
        mClientsRecyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        mClientsRecyclerView.setLayoutManager(new GridLayoutManager(SubSpecializationActivity.this, 1));
        setData();


    }

    private void setData() {
        List<Speciality> SubSpecialityList = new ArrayList<>();
        for (int i = 0; i < ConstantsFreelance.JOBS_NAMES[categoryNum].length; i++) {
            SubSpecialityList.add(new Speciality(ConstantsFreelance.JOBS_NAMES[categoryNum][i], ConstantsFreelance.JOBS_ID1[categoryNum][i]));
        }
        SubSpecialtiesAdapter clientsAdapter = new SubSpecialtiesAdapter(getBaseContext(), SubSpecialityList);
        mClientsRecyclerView.setAdapter(clientsAdapter);
    }

    public void initCollapsingToolbar() {
        try {
            Picasso.with(this).load(categorie.getImg()).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView name = (TextView) findViewById(R.id.name_sepcialization);
        name.setText(categorie.getName());

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(categorie.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }


}
