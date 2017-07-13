package com.sadek.apps.freelance7rfeen.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.sadek.apps.freelance7rfeen.fragments.FilterDialogFragment;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.utils.Drawer;

import java.util.HashMap;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {
    private FlowingDrawer drawer;
    static RecyclerView mClientsRecyclerView;
    static Context mContext;
    View dataView, noConnectionView;
    Button reloadBtn;
    TextView stateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_favorite);
        mContext = getBaseContext();
        drawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        drawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, FavoriteActivity.this, "FavoriteActivity");
        drawer.makeDrawer();
        menuAction();

        mClientsRecyclerView = (RecyclerView) findViewById(R.id.clients_recyclerview);
        mClientsRecyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        mClientsRecyclerView.setLayoutManager(new GridLayoutManager(FavoriteActivity.this, 1));
        dataView = findViewById(R.id.data);
        noConnectionView = findViewById(R.id.no_connection);
        reloadBtn = (Button) findViewById(R.id.reload_btn);
        stateTxt = (TextView) findViewById(R.id.state_txt);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavData();
    }

    private void menuAction() {
        ImageView drawerBtn = (ImageView) findViewById(R.id.drawer_btn);
//        ImageView filterBtn = (ImageView) findViewById(R.id.filter_btn);
        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openMenu();
            }
        });

//        filterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogFragment dialog = FilterDialogFragment.newInstance();
//                dialog.show(FavoriteActivity.this.getFragmentManager(), "FilterDialogFragment");
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {

        if (drawer.isMenuVisible()) {
            drawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/user_favorite?";
    ProgressDialog progressDialog;

    public void getFavData() {
        progressDialog = new ProgressDialog(FavoriteActivity.this);
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
                            dataView.setVisibility(View.GONE);
                            noConnectionView.setVisibility(View.VISIBLE);
                            stateTxt.setText(getString(R.string.nofav));
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FavoriteActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(drawer, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction(R.string.action, null).show();
                        dataView.setVisibility(View.GONE);
                        noConnectionView.setVisibility(View.VISIBLE);
                        stateTxt.setText(getString(R.string.noconnection));
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

    private void addDataToLayout() {
        dataView.setVisibility(View.VISIBLE);
        noConnectionView.setVisibility(View.GONE);
        ClientsAdapter clientsAdapter = new ClientsAdapter(getBaseContext(), ParseJSON.favoriteList);
        mClientsRecyclerView.setAdapter(clientsAdapter);
    }
}
