package com.sadek.apps.freelance7rfeen.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.activities.RequestDetailActivity;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mahmoud Sadek on 5/2/2017.
 */

public class RateReviewFragment extends DialogFragment {

    static Activity mcontext;
    RatingBar time, deadline, prefiction, price, clean;
    EditText summary;
    public static String meh_id;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static RateReviewFragment newInstance(Activity context) {
        mcontext = context;
        RateReviewFragment addListDialogFragment = new RateReviewFragment();
        Bundle bundle = new Bundle();
        addListDialogFragment.setArguments(bundle);
        return addListDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.anim.logo_animation_back;
        ((ViewGroup) getDialog().getWindow().getDecorView())
                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                getActivity(), R.anim.app_name_animation));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View myView = inflater.inflate(R.layout.fragment_rate_review, null);

        time = (RatingBar) myView.findViewById(R.id.rate_work_completion);
        deadline = (RatingBar) myView.findViewById(R.id.rate_work_deadline);
        prefiction = (RatingBar) myView.findViewById(R.id.rate_work_prefiction);
        price = (RatingBar) myView.findViewById(R.id.rate_work_price);
        clean = (RatingBar) myView.findViewById(R.id.rate_work_clean);
        summary = (EditText) myView.findViewById(R.id.user_comment);
        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
        builder.setView(myView)
                /* Add action buttons */
                .setPositiveButton("تقييم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RateOrder();
                    }
                });

        return builder.create();
    }

    /**
     * Add new active list
     */


    private static final String JSON_URL_order = ConstantsFreelance.SERVER + "/do_rate?";
    ProgressDialog progressDialog;

    public void RateOrder() {
        final String cmnt = summary.getText().toString().trim();
        final String tim = (int) time.getRating() + "";
        final String ddln = (int) deadline.getRating() + "";
        final String prfct = (int) prefiction.getRating() + "";
        final String pric = (int) price.getRating() + "";
        final String cln = (int) clean.getRating() + "";

        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_order,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String state = jsonObject.getString("message");
                            if (state.equals("done")) {
                                Toast.makeText(mcontext, "تم تقييم المهني بنجاح", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mcontext, "حدث خطأ ما", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        progressDialog.setMessage(getString(R.string.check_network));
                        progressDialog.setCancelable(true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mcontext);
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sp.getInt(ConstantsFreelance.USE_ID, 0) + "");
                params.put("mehani_id", meh_id);
                params.put("work_time", tim);
                params.put("request_id", RequestDetailActivity.request_id);
                params.put("work_quality", prfct);
                params.put("work_price", pric);
                params.put("work_appearance", ddln);
                params.put("environment_cleaning", cln);
                params.put("review", cmnt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        requestQueue.add(stringRequest);
            /* Close the dialog fragment */

    }

}
