package com.sadek.apps.freelance7rfeen.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button mLoginButton, mRegisterButton;
    EditText inputEmail, inputPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.btn_login);
        mRegisterButton = (Button) findViewById(R.id.btn_sign_up);
        inputEmail = (EditText) findViewById(R.id.txt_email);
        inputPass = (EditText) findViewById(R.id.txt_password);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();

            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ChooseRegisterActivity.class));
            }
        });

    }

    private void Login() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPass.getText().toString().trim();
        if (validateLogin())
            checkExistence(email, password);
//        startActivity(new Intent(getBaseContext(), SpecializationActivity.class));
    }

    private boolean validateLogin() {
        boolean valid = true;
        String email = inputEmail.getText().toString().trim();
        String password = inputPass.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError(getString(R.string.valid_email));
            Toast.makeText(LoginActivity.this, R.string.email_error, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 7 || password.length() > 14) {
            inputPass.setError(getString(R.string.valid_pass));
            Toast.makeText(LoginActivity.this, R.string.pass_error, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputPass.setError(null);
        }
        return valid;
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/login?";
    ProgressDialog progressDialog;

    private void checkExistence(final String email, final String password) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getString(R.string.login));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseLogin();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        if (ParseJSON.status_login) {
                            if (ParseJSON.type == 0) {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor spe = sp.edit();
                                spe.putInt(ConstantsFreelance.USER_TYPE, ParseJSON.type).apply();
                                spe.putInt(ConstantsFreelance.USE_ID, ParseJSON.id).apply();
                                spe.commit();
                                Intent intent = new Intent(getApplicationContext(), SpecializationActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (ParseJSON.complete == 1) {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor spe = sp.edit();
                                spe.putInt(ConstantsFreelance.USER_TYPE, ParseJSON.type).apply();
                                spe.putInt(ConstantsFreelance.USE_ID, ParseJSON.id).apply();
                                spe.commit();
                                Intent intent = new Intent(getApplicationContext(), SpecializationActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                CompleteRegistrationActivity.user_id = ParseJSON.id+"";
                                Intent intent = new Intent(getApplicationContext(), CompleteRegistrationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.error_user), Toast.LENGTH_LONG).show();
                            Snackbar.make(mLoginButton, getString(R.string.error_user), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(mLoginButton, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
