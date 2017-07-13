package com.sadek.apps.freelance7rfeen.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.adapter.ClientsAdapter;
import com.sadek.apps.freelance7rfeen.database.FreelanceContract;
import com.sadek.apps.freelance7rfeen.database.FreelanceDbHelper;
import com.sadek.apps.freelance7rfeen.model.ClientProfile;
import com.sadek.apps.freelance7rfeen.model.Favorite;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.utils.Drawer;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListClientsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static String categorie_id;
    private FlowingDrawer mDrawer;
    static RecyclerView mClientsRecyclerView;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_list_clients);

        initLayout();
    }

    private void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        initCollapsingToolbar();
        mContext = getBaseContext();
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, ListClientsActivity.this, "");
        drawer.makeDrawer();
        mClientsRecyclerView = (RecyclerView) findViewById(R.id.clients_recyclerview);
        mClientsRecyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        mClientsRecyclerView.setLayoutManager(new GridLayoutManager(ListClientsActivity.this, 1));
        getWorkersData();
        List<Favorite> clientList = new ArrayList<>();
        ClientsAdapter clientsAdapter = new ClientsAdapter(getBaseContext(), clientList);
        mClientsRecyclerView.setAdapter(clientsAdapter);
    }

    public void initCollapsingToolbar() {
        try {
            Picasso.with(this).load(R.drawable.am_worker).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    collapsingToolbar.setTitle("نجار");
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

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/get_workers?";
    ProgressDialog progressDialog;

    public void getWorkersData() {
        progressDialog = new ProgressDialog(ListClientsActivity.this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseFavorite();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        if (ParseJSON.status_fav) {
                            addDataToLayout();
                            progressDialog.dismiss();
                        } else {
//                            dataView.setVisibility(View.GONE);
//                            noConnectionView.setVisibility(View.VISIBLE);
//                            stateTxt.setText(getString(R.string.nofav));
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListClientsActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(mDrawer, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction(R.string.action, null).show();
//                        dataView.setVisibility(View.GONE);
//                        noConnectionView.setVisibility(View.VISIBLE);
//                        stateTxt.setText(getString(R.string.noconnection));
                        getStoredData();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("majors[0]", categorie_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getStoredData() {
        getLoaderManager().initLoader(1, null, this);

    }

    private void addDataToLayout() {
//        dataView.setVisibility(View.VISIBLE);
//        noConnectionView.setVisibility(View.GONE);
        ClientsAdapter clientsAdapter = new ClientsAdapter(getBaseContext(), ParseJSON.favoriteList);
        mClientsRecyclerView.setAdapter(clientsAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] column = {FreelanceDbHelper.UID, FreelanceDbHelper.NAME, FreelanceDbHelper.ADDRESS, FreelanceDbHelper.SKILLS,
                FreelanceDbHelper.EDUCATION, FreelanceDbHelper.EXPERIENCE, FreelanceDbHelper.PHONE, FreelanceDbHelper.EX_YEARS,
                FreelanceDbHelper.EVALUATION, FreelanceDbHelper.AVAILABLE, FreelanceDbHelper.GOVERNMENT, FreelanceDbHelper.CITY,
                FreelanceDbHelper.CARRIER, FreelanceDbHelper.RATE};
        if (id == 1) {
            return new CursorLoader(ListClientsActivity.this, FreelanceContract.FreelanceEntry.CONTENT_URI,
                    column, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        List<Favorite> clientProfileList = new ArrayList<Favorite>();
        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder stringBuilderQueryResult = new StringBuilder("");
            //cursor.moveToFirst();

            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(11) == Integer.parseInt(categorie_id)) {

                        String job_name = "", government = "", city = "";
                        for (int j = 0; j < ConstantsFreelance.JOBS_ID1.length; j++) {
                            for (int k = 0; k < ConstantsFreelance.JOBS_ID1[j].length; k++) {
                                if (cursor.getInt(11) == ConstantsFreelance.JOBS_ID1[j][k]) {
                                    job_name = ConstantsFreelance.JOBS_NAMES[j][k];
                                    break;
                                }
                            }
                        }
                        int k;
                        for (k = 0; k < ConstantsFreelance.GOVERNMENT_NAMES.length; k++) {
                            if (cursor.getInt(9) == ConstantsFreelance.GOVERNMENT_IDS[k]) {
                                government = ConstantsFreelance.GOVERNMENT_NAMES[k];
                                for (int h = 0; h < ConstantsFreelance.CITY_IDS[k].length; h++) {
                                    if (cursor.getInt(10) == ConstantsFreelance.CITY_IDS[k][h]) {
                                        city = ConstantsFreelance.CITY_NAMES[k][h];
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        Favorite clientProfile = new Favorite(cursor.getString(1), job_name, government, city, "", Float.parseFloat(cursor.getString(12)));

                        clientProfileList.add(clientProfile);
                    }
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(mContext, R.string.noconnection, Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        ClientsAdapter clientsAdapter = new ClientsAdapter(getBaseContext(), clientProfileList);
        mClientsRecyclerView.setAdapter(clientsAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
