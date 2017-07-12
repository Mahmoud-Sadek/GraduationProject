package com.sadek.apps.freelance7rfeen.activities;

import android.app.DialogFragment;
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
import com.sadek.apps.freelance7rfeen.fragments.OrderFragment;
import com.sadek.apps.freelance7rfeen.fragments.RateReviewFragment;
import com.sadek.apps.freelance7rfeen.model.Client;
import com.sadek.apps.freelance7rfeen.model.Requests;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import java.util.HashMap;
import java.util.Map;

public class RequestDetailActivity extends AppCompatActivity {

    TextView name, summary, date, phone, address, endDate,statusTxt;
    Button rate, cancel;
    public static String request_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        name = (TextView) findViewById(R.id.request_name);
        date = (TextView) findViewById(R.id.request_date);
        summary = (TextView) findViewById(R.id.request_summary);
        phone = (TextView) findViewById(R.id.txt_phone);
        endDate = (TextView) findViewById(R.id.txt_date);
        address = (TextView) findViewById(R.id.txt_place);
        statusTxt = (TextView) findViewById(R.id.txt_status);
        rate = (Button) findViewById(R.id.btn_rate);
        cancel = (Button) findViewById(R.id.btn_cancel_order);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateOrder();
            }
        });
        getUserData();
    }

    private void rateOrder() {
        if (ParseJSON.request.getStatus().equals("1")){
            RateReviewFragment.meh_id = ParseJSON.request.getRecived_id();
            DialogFragment dialog = RateReviewFragment.newInstance(RequestDetailActivity.this);
            dialog.show(RequestDetailActivity.this.getFragmentManager(), "RateReviewFragment");
        }else {
            Toast.makeText(this, R.string.cant_rate, Toast.LENGTH_SHORT).show();
        }
    }

    private static final String JSON_URL2 = ConstantsFreelance.SERVER + "/request_details?";
    ProgressDialog progressDialog;

    public void getUserData() {
        progressDialog = new ProgressDialog(RequestDetailActivity.this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseRequestDetail();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        if (ParseJSON.status_rqst_detail) {
                            addDataToLayout(ParseJSON.request);
                        } else {
//                            dataView.setVisibility(View.GONE);
//                            noConnectionView.setVisibility(View.VISIBLE);
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RequestDetailActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(name, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
//                        dataView.setVisibility(View.GONE);
//                        noConnectionView.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();

                params.put("request_id", request_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addDataToLayout(Requests userProfile) {
        name.setText(userProfile.getUser_name());
        address.setText(userProfile.getAddress());
        phone.setText(userProfile.getPhone());
        summary.setText(userProfile.getContent());
        endDate.setText(userProfile.getEnd_date());
        date.setText(userProfile.getCreate_date());
        String status = userProfile.getStatus();
        if (status.equals("0")){
            status = "Not Seen";
        }else if (status.equals("1")){
            status = "approved";
        }else if (status.equals("-1")){
            status = "not approved";
        }else if (status.equals("2")){
            status = "finished";
        }else if (status.equals("-2")){
            status = "Canceled";
        }
        statusTxt.setText(status);
    }
}
