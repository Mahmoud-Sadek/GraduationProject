package com.sadek.apps.freelance7rfeen.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sadek.apps.freelance7rfeen.adapter.RequestsAdapter;
import com.sadek.apps.freelance7rfeen.fragments.FilterDialogFragment;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.utils.Drawer;

import java.util.HashMap;
import java.util.Map;

public class RecivedRequestActivity extends AppCompatActivity {

    private FlowingDrawer mDrawer;
    static RecyclerView mRequestsRecyclerView;
    static Context mContext;
    View dataView, noConnectionView;
    Button reloadBtn;
    TextView stateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recived_request);
        mContext = getBaseContext();
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, RecivedRequestActivity.this, "RecivedRequestActivity");
        drawer.makeDrawer();
        menuAction();
        mRequestsRecyclerView = (RecyclerView) findViewById(R.id.sent_request_recyclerview);
        mRequestsRecyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        mRequestsRecyclerView.setLayoutManager(new GridLayoutManager(RecivedRequestActivity.this, 1));

        dataView = findViewById(R.id.data);
        noConnectionView = findViewById(R.id.no_connection);
        reloadBtn = (Button) findViewById(R.id.reload_btn);
        stateTxt = (TextView) findViewById(R.id.state_txt);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequestData();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRequestData();
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/getrecivedrequest?";
    ProgressDialog progressDialog;

    public void getRequestData() {
        progressDialog = new ProgressDialog(RecivedRequestActivity.this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseRequests();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        if (ParseJSON.status_rqst) {
                            setData();
                        } else {
                            dataView.setVisibility(View.GONE);
                            noConnectionView.setVisibility(View.VISIBLE);
                            stateTxt.setText(getString(R.string.norqst));
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecivedRequestActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(mDrawer, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
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
                params.put("mehani_id", sp.getInt(ConstantsFreelance.USE_ID, 0) + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void setData() {
        dataView.setVisibility(View.VISIBLE);
        noConnectionView.setVisibility(View.GONE);
        RequestsAdapter requestsAdapter = new RequestsAdapter(getBaseContext(), ParseJSON.requestsList);
        mRequestsRecyclerView.setAdapter(requestsAdapter);
    }

    private void menuAction() {
        ImageView drawerBtn = (ImageView) findViewById(R.id.drawer_btn);
        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openMenu();
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