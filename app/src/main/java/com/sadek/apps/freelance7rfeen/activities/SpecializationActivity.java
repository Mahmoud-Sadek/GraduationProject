package com.sadek.apps.freelance7rfeen.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.adapter.SpecialtiesAdapter;
import com.sadek.apps.freelance7rfeen.fragments.FilterDialogFragment;
import com.sadek.apps.freelance7rfeen.model.Client;
import com.sadek.apps.freelance7rfeen.model.Speciality;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.utils.Drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecializationActivity extends AppCompatActivity {

    private FlowingDrawer drawer;
    List<Speciality> Specialtie;
    RecyclerView SpecialtiesrecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_specializations);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        drawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        drawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, SpecializationActivity.this, "SpecializationActivity");
        drawer.makeDrawer();

        insertData();

        SpecialtiesrecyclerView = (RecyclerView) findViewById(R.id.specialties_recyclerView);
        layoutManager = new StaggeredGridLayoutManager(calculateNoOfColumns(getBaseContext()), 1);
        SpecialtiesrecyclerView.setLayoutManager(layoutManager);
        SpecialtiesrecyclerView.setHasFixedSize(true);
        SpecialtiesAdapter adapter = new SpecialtiesAdapter(getBaseContext(), Specialtie);
        SpecialtiesrecyclerView.setAdapter(adapter);
        menuAction();

        checkUser();
    }

    private void checkUser() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sp.getString(ConstantsFreelance.USER_NAME, "").equals("")) {
            getUserData();
        }
    }

    private void insertData() {
        Specialtie = new ArrayList<>();
        for (int i = 0; i < ConstantsFreelance.SPECIALTY_NAME.length; i++) {
            Specialtie.add(new Speciality(ConstantsFreelance.SPECIALTY_NAME[i], ConstantsFreelance.SPECIALTY_IMAGE[i]));
        }
    }

    private void menuAction() {
        ImageView drawerBtn = (ImageView) findViewById(R.id.drawer_btn);
        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openMenu();
            }
        });

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns >= 2 ? noOfColumns : 2;

    }

    @Override
    public void onBackPressed() {

        if (drawer.isMenuVisible()) {
            drawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/exist_profile?";
    ProgressDialog progressDialog;

    public void getUserData() {
        progressDialog = new ProgressDialog(SpecializationActivity.this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseProfileUser();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        if (ParseJSON.status_user_profile) {
                            addDataToLayout(ParseJSON.userProfile);
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SpecializationActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(drawer, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction(R.string.action, null).show();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sp.getInt(ConstantsFreelance.USE_ID, 0) + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addDataToLayout(Client userProfile) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(ConstantsFreelance.USER_NAME, userProfile.getName()).apply();
        spe.putString(ConstantsFreelance.USER_EMAIL, userProfile.getEmail()).apply();
        spe.commit();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
