package com.sadek.apps.freelance7rfeen.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import java.util.HashMap;
import java.util.Map;

public class RegisterAgentActivity extends AppCompatActivity {
    EditText inputName, inputEmail, inputPhone, inputAddress, inputPass, inputConfPass;
    CustomSpinner inputCountry, inputCity;
    Button mRegisterBtn, mLoginBtn;
    String country = "", city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_register_agent);

        mRegisterBtn = (Button) findViewById(R.id.btn_sign_up);
        mLoginBtn = (Button) findViewById(R.id.btn_sign_in);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });

        inputName = (EditText) findViewById(R.id.txt_user_name);
        inputEmail = (EditText) findViewById(R.id.txt_email);
        inputPhone = (EditText) findViewById(R.id.txt_user_phone);
        inputAddress = (EditText) findViewById(R.id.txt_user_address);
        inputPass = (EditText) findViewById(R.id.txt_user_pass);
        inputConfPass = (EditText) findViewById(R.id.txt_confirm_pass);
        inputCountry = (CustomSpinner) findViewById(R.id.country_spinner);
        inputCity = (CustomSpinner) findViewById(R.id.city_spinner);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
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
    }

    private void Register() {
        if (validateRegister())
            addClient();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    Client client;

    private boolean validateRegister() {
        boolean valid = true;
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPass.getText().toString().trim();
        String repassword = inputConfPass.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        client = new Client(name, email, password, phone, address, country, city);

        if (name.isEmpty() || name.length() < 3) {
            inputName.setError("at least 3 characters");
            Toast.makeText(RegisterAgentActivity.this, "User Name is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputName.setError(null);
        }
        if (address.isEmpty() || address.length() < 3) {
            inputAddress.setError("at least 3 characters");
            Toast.makeText(RegisterAgentActivity.this, "Address is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputAddress.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            Toast.makeText(RegisterAgentActivity.this, "Email is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 7 || password.length() > 14) {
            inputPass.setError("between 7 and 14 alphanumeric characters");
            Toast.makeText(RegisterAgentActivity.this, "Password is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputPass.setError(null);
        }
        if (!repassword.equals(password)) {
            inputPass.setError("password not match!");
            Toast.makeText(RegisterAgentActivity.this, "password not match", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputPass.setError(null);
        }
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            inputPhone.setError("enter valid Phone Number");
            Toast.makeText(RegisterAgentActivity.this, "Phone Number is not right", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputPhone.setError(null);
        }
        if (country.equals("")) {
            Toast.makeText(RegisterAgentActivity.this, "Choose Your Country", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (city.equals("")) {
            Toast.makeText(RegisterAgentActivity.this, "Choose Your City", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/worker/register?";
    ProgressDialog progressDialog;

    private void addClient() {
        progressDialog = new ProgressDialog(RegisterAgentActivity.this);
        progressDialog.setMessage(getString(R.string.register));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseRegister2();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        if (ParseJSON.status_register) {
                            /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor spe = sp.edit();
                            spe.putInt(ConstantsFreelance.USER_TYPE, ParseJSON.type).apply();
                            spe.putInt(ConstantsFreelance.USE_ID, ParseJSON.id).apply();
                            spe.commit();*/
                            CompleteRegistrationActivity.user_id = ParseJSON.id + "";
                            Intent intent = new Intent(getApplicationContext(), CompleteRegistrationActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(RegisterAgentActivity.this, "This User Already has Account!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterAgentActivity.this,   getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(mRegisterBtn, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("full_name", client.getName());
                params.put("user_email", client.getEmail());
                params.put("phone", client.getPhone());
                params.put("password", client.getPassword());
                params.put("address1", client.getAddress());
                params.put("country_id", country);
                params.put("city_id", city);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
