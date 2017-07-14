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
import com.piotrek.customspinner.CustomSpinner;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.model.Client;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import java.util.HashMap;
import java.util.Map;

public class CompleteRegistrationActivity extends AppCompatActivity {

    EditText inputNotes;
    CustomSpinner inputMale, inputSpecialty, inputSubSpecialty;
    Button mRegisterBtn, mLoginBtn;
    String male, specialty, subSpeciality;
    public static String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_complete_registration);
        mRegisterBtn = (Button) findViewById(R.id.btn_sign_up);
        mLoginBtn = (Button) findViewById(R.id.btn_sign_in);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        });

        inputNotes = (EditText) findViewById(R.id.resume);
        inputMale = (CustomSpinner) findViewById(R.id.male_spinner);
        inputSpecialty = (CustomSpinner) findViewById(R.id.specialty_spinner);
        inputSubSpecialty = (CustomSpinner) findViewById(R.id.sub_specialty_spinner);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeRegister();
            }
        });

        inputSpecialty.initializeStringValues(ConstantsFreelance.SPECIALTY_NAME, getString(R.string.main_job));
        inputSpecialty.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                specialty = null;
            }

            @Override
            public void onSpinnerClosed() {
                specialty = ConstantsFreelance.SPECIALTY_ID[inputSpecialty.getSelectedItemPosition()] + "";
                inputSubSpecialty.initializeStringValues(ConstantsFreelance.JOBS_NAMES[inputSpecialty.getSelectedItemPosition()], getString(R.string.job_));
            }
        });
        inputSubSpecialty.initializeStringValues(ConstantsFreelance.JOBS_NAMES[0], getString(R.string.job_));
        inputSubSpecialty.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                subSpeciality = null;
            }

            @Override
            public void onSpinnerClosed() {
                subSpeciality = ConstantsFreelance.JOBS_ID1[inputSpecialty.getSelectedItemPosition()][inputSubSpecialty.getSelectedItemPosition()] + "";
            }
        });
        inputMale.initializeStringValues(getResources().getStringArray(R.array.male_array), getString(R.string.gender));
        inputMale.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                male = null;
            }

            @Override
            public void onSpinnerClosed() {
                male = inputMale.getSelectedItemPosition() + "";
            }
        });
    }

    private void completeRegister() {
        String notes = inputNotes.getText().toString().trim();
        if (validateRegister())
            addClient(notes);
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    Client client;

    private boolean validateRegister() {
        boolean valid = true;

        String notes = inputNotes.getText().toString().trim();

        if (notes.isEmpty() || notes.length() < 7) {
            inputNotes.setError(getString(R.string.valid_notes));
            Toast.makeText(CompleteRegistrationActivity.this, R.string.notes_error, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputNotes.setError(null);
        }

        if (male.equals(null)) {
            Toast.makeText(CompleteRegistrationActivity.this, R.string.gender_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (specialty.equals(null)) {
            Toast.makeText(CompleteRegistrationActivity.this, R.string.specialty_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (subSpeciality.equals(null)) {
            Toast.makeText(CompleteRegistrationActivity.this, R.string.specialt_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/worker/workerregister2?";
    ProgressDialog progressDialog;

    private void addClient(final String notes) {
        progressDialog = new ProgressDialog(CompleteRegistrationActivity.this);
        progressDialog.setMessage(getString(R.string.register));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseSaveRegister2();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor spe = sp.edit();
                        spe.putInt(ConstantsFreelance.USER_TYPE, 1).apply();
                        spe.putInt(ConstantsFreelance.USE_ID, ParseJSON.id).apply();
                        spe.commit();
                        Intent intent = new Intent(getApplicationContext(), ChooseLocationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompleteRegistrationActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(mRegisterBtn, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction(R.string.action, null).show();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("main_id", specialty);
                params.put("major_id", subSpeciality);
                params.put("gender", male);
                params.put("about", notes);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
