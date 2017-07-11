package com.sadek.apps.freelance7rfeen.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.activities.ProfileClientActivity;
import com.sadek.apps.freelance7rfeen.activities.RequestDetailActivity;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mahmoud Sadek on 6/28/2017.
 */

public class OrderFragment extends DialogFragment {

    public static String btnTxt, message;
    static Context mcontext;
    public static int meh_id;
    public static Activity mActivity;
    EditText comment, addrss, phone;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static OrderFragment newInstance(Context context) {
        mcontext = context;
        OrderFragment addListDialogFragment = new OrderFragment();
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
                getActivity(), R.anim.slide_down));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.order_dialog, null);


        TextView mssage = (TextView) rootView.findViewById(R.id.message);
        mssage.setText(message);

        comment = (EditText) rootView.findViewById(R.id.txt_comment);
        addrss = (EditText) rootView.findViewById(R.id.txt_user_address);
        phone = (EditText) rootView.findViewById(R.id.txt_user_phone);
        addrss.setText(ParseJSON.clientProfile.getAddress());
        phone.setText(ParseJSON.clientProfile.getPhone());

        if (btnTxt.equals("إطلب")){
            rootView.findViewById(R.id.liner_order).setVisibility(View.VISIBLE);
        }

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (btnTxt.equals("إطلب")) {
                            order();
                        } else if (btnTxt.equals("تم")) {
                            OrderFragment.this.getDialog().cancel();
                        } else if (btnTxt.equals("تقييم")) {
                            startActivity(new Intent(mcontext, RequestDetailActivity.class));
                            RequestDetailActivity.request_id = meh_id+"";
                            mActivity.finish();
                        }
                    }
                });

        return builder.create();
    }

    private static final String JSON_URL_order = ConstantsFreelance.SERVER + "/send_request?";
    ProgressDialog progressDialog;

    private void order() {
        final String cmnt = comment.getText().toString().trim();
        final String phn = phone.getText().toString().trim();
        final String addrs = addrss.getText().toString().trim();

        if (TextUtils.isEmpty(cmnt) && TextUtils.isEmpty(phn) && TextUtils.isEmpty(addrs)) {
            Toast.makeText(mcontext, "اكمل البيانات اولا", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(mActivity);
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
                            String state = jsonObject.getString("requests");
                            if (state.equals("done")) {
                                Toast.makeText(mcontext, "تم طلب المهني بنجاح", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mActivity, "افحص الانترنت", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mcontext);
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sp.getInt(ConstantsFreelance.USE_ID, 0) + "");
                params.put("mehani_id", meh_id + "");
                params.put("request_description", cmnt);
                params.put("request_city", ParseJSON.clientProfile.getCity() + "");
                params.put("request_address", addrs);
                params.put("request_phone", phn);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(stringRequest);
    }

    /**
     * Add new active list
     */


    public void addShoppingList() {


            /* Close the dialog fragment */
        OrderFragment.this.getDialog().cancel();

    }

}
