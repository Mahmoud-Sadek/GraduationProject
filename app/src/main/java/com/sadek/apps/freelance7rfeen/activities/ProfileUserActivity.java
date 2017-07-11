package com.sadek.apps.freelance7rfeen.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.model.Client;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import java.util.HashMap;
import java.util.Map;

public class ProfileUserActivity extends AppCompatActivity {

    TextView name, address, userName, phone, email;
    View dataView, noConnectionView;
    Button reloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_profile_user);

        name = (TextView) findViewById(R.id.userName);
        userName = (TextView) findViewById(R.id.full_name);
        address = (TextView) findViewById(R.id.user_address);
        phone = (TextView) findViewById(R.id.user_phone);
        email = (TextView) findViewById(R.id.user_mail);
        dataView = findViewById(R.id.data);
        noConnectionView = findViewById(R.id.no_connection);
        reloadBtn= (Button) findViewById(R.id.reload_btn);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/exist_profile?";
    ProgressDialog progressDialog;

    public void getUserData() {
        progressDialog = new ProgressDialog(ProfileUserActivity.this);
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
                        } else {
                            dataView.setVisibility(View.GONE);
                            noConnectionView.setVisibility(View.VISIBLE);
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileUserActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(name, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        dataView.setVisibility(View.GONE);
                        noConnectionView.setVisibility(View.VISIBLE);
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
        dataView.setVisibility(View.VISIBLE);
        noConnectionView.setVisibility(View.GONE);
        name.setText(userProfile.getName());
        userName.setText(userProfile.getPassword());
        phone.setText(userProfile.getPhone());
        address.setText(userProfile.getAddress());
        email.setText(userProfile.getEmail());
    }

}
