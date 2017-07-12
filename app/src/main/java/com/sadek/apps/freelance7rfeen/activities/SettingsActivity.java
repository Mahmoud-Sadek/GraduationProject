package com.sadek.apps.freelance7rfeen.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piotrek.customspinner.CustomSpinner;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.model.Client;
import com.sadek.apps.freelance7rfeen.model.User;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    EditText inputName, inputAddress, inputPhone;
    CustomSpinner inputCountry, inputCity;
    Button mUpdateBtn;
    String country = "1", city = "1";
    View dataView, noConnectionView;
    Button reloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_settings);

        mUpdateBtn = (Button) findViewById(R.id.btn_update);

        inputName = (EditText) findViewById(R.id.txt_user_name);
        inputPhone = (EditText) findViewById(R.id.txt_user_phone);
        inputAddress = (EditText) findViewById(R.id.txt_user_address);
        inputCountry = (CustomSpinner) findViewById(R.id.country_spinner);
        inputCity = (CustomSpinner) findViewById(R.id.city_spinner);
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        inputCity.initializeStringValues(ConstantsFreelance.CITY_NAMES[0], "المدينة");
        inputCity.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                city = null;
            }

            @Override
            public void onSpinnerClosed() {
                city = String.valueOf(inputCity.getSelectedItem());
                city = ConstantsFreelance.CITY_IDS[inputCountry.getSelectedItemPosition()][inputCity.getSelectedItemPosition()]+"";
            }
        });
        inputCountry.initializeStringValues(ConstantsFreelance.GOVERNMENT_NAMES, "المحافظة");
        inputCountry.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                country = "";
            }

            @Override
            public void onSpinnerClosed() {
                country = ConstantsFreelance.GOVERNMENT_IDS[inputCountry.getSelectedItemPosition()]+"";
                inputCity.initializeStringValues(ConstantsFreelance.CITY_NAMES[inputCountry.getSelectedItemPosition()], "المدينة");
            }
        });

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

    private void update() {
        if (validateUpdate())
            updateData();

    }


    User user;

    private boolean validateUpdate() {
        boolean valid = true;
        String name = inputName.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        user = new User(phone, address, country, city, name);

        if (name.isEmpty() || name.length() < 3) {
            inputName.setError(getString(R.string.error_chars));
            Toast.makeText(SettingsActivity.this, R.string.name_error, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputName.setError(null);
        }
        if (address.isEmpty() || address.length() < 3) {
            inputAddress.setError(getString(R.string.error_chars));
            Toast.makeText(SettingsActivity.this, R.string.address_error, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputAddress.setError(null);
        }
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            inputPhone.setError(getString(R.string.phone_valid));
            Toast.makeText(SettingsActivity.this, R.string.phone_erro, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputPhone.setError(null);
        }
        if (country.equals("")) {
            Toast.makeText(SettingsActivity.this, R.string.contry_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (city.equals("")) {
            Toast.makeText(SettingsActivity.this, R.string.city_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/user_save_settings?";
    ProgressDialog progressDialog;

    private void updateData() {
        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage(getString(R.string.update));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(SettingsActivity.this, jsonObject.getString(getString(R.string.message)), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SettingsActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(mUpdateBtn, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sp.getInt(ConstantsFreelance.USE_ID, 0) + "");
                params.put("type", sp.getInt(ConstantsFreelance.USER_TYPE, 0) + "");
                params.put("full_name", user.getName());
                params.put("username", ParseJSON.userProfile.getPassword());
                params.put("phone1", user.getPhone());
                params.put("address1", user.getAddress());
//                params.put("country_id", user.getCountry());
                params.put("city_id", user.getCity());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private static final String JSON_URL2 = ConstantsFreelance.SERVER + "/exist_profile?";

    public void getUserData() {
        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL2,
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
                        Toast.makeText(SettingsActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(mUpdateBtn, getString(R.string.check_network), Snackbar.LENGTH_LONG)
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
        inputName.setText(userProfile.getName());
        inputPhone.setText(userProfile.getPhone());
        inputAddress.setText(userProfile.getAddress());
    }
}
