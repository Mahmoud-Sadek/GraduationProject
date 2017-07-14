package com.sadek.apps.freelance7rfeen.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.database.FreelanceContract;
import com.sadek.apps.freelance7rfeen.database.FreelanceDbHelper;
import com.sadek.apps.freelance7rfeen.fragments.DetailProfileClientFragment;
import com.sadek.apps.freelance7rfeen.fragments.OrderFragment;
import com.sadek.apps.freelance7rfeen.fragments.RatingProfileClientFragment;
import com.sadek.apps.freelance7rfeen.model.ClientProfile;
import com.sadek.apps.freelance7rfeen.parse.ParseJSON;
import com.sadek.apps.freelance7rfeen.utils.BuilderManager;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.utils.Drawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class ProfileClientActivity extends AppCompatActivity implements MaterialTabListener {

    private MaterialTabHost tabHost;
    private static ViewPager viewPager;
    static Toolbar toolBar;
    private ViewPagerAdapter pagerAdapter;
    private FlowingDrawer mDrawer;
    static Context mContext;
    public static String mehani_id = "";
    TextView name, available;
    View dataView, noConnectionView;
    Button reloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_profile_client);
        initLayout();
        tabs();
        name = (TextView) findViewById(R.id.txt_client_name);
        available = (TextView) findViewById(R.id.txt_client_available);
        dataView = findViewById(R.id.data);
        noConnectionView = findViewById(R.id.no_connection);
        reloadBtn= (Button) findViewById(R.id.reload_btn);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClientProfileData();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getClientProfileData();
    }

    private void tabs() {
        //Tabs
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
//                                .setIcon(pagerAdapter.getIcon(i))
                            .setTabListener(this)
            );
        }
    }

    private void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        initCollapsingToolbar();
        mContext = getBaseContext();
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        View v = findViewById(R.id.drawerlayout);
        Drawer drawer = new Drawer(getBaseContext(), v, ProfileClientActivity.this, "");
        drawer.makeDrawer();

        fabButtons();
    }

    private void fabButtons() {
        BoomMenuButton bmb2 = (BoomMenuButton) findViewById(R.id.fab);
        for (int i = 0; i < 4; i++)
            bmb2.addBuilder(BuilderManager.getSquareTextInsideCircleButtonBuilder());
        bmb2.getBuilder(0).listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                addToFavorite();
            }
        });

        bmb2.getBuilder(1).listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                checkToOrder();
            }
        });
        bmb2.getBuilder(2).listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                String number = ParseJSON.clientProfile.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });
        bmb2.getBuilder(3).listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                MapsActivity.clientLatLng = new LatLng(ParseJSON.clientProfile.getLat(), ParseJSON.clientProfile.getLog());
                startActivity(new Intent(ProfileClientActivity.this, MapsActivity.class));
            }
        });
    }



    private static final String JSON_URL_check_order = ConstantsFreelance.SERVER + "/check_request?";

    private void checkToOrder() {
        progressDialog = new ProgressDialog(ProfileClientActivity.this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_check_order,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String state = jsonObject.getString("show_modal");
                            OrderFragment.mActivity = ProfileClientActivity.this;
                            if (state.equals(getString(R.string.ordr))) {
                                OrderFragment.btnTxt = getString(R.string.order);
                                OrderFragment.message = getString(R.string.fill_details);
                            } else if (state.equals(getString(R.string.his_self)) || state.equals(getString(R.string.not_avalb))) {
                                OrderFragment.message = getString(R.string.cant_order);
                                OrderFragment.btnTxt = getString(R.string.done);
                            } else if (state.equals(getString(R.string.rate_one))) {
                                OrderFragment.message = getString(R.string.rate_first);
                                OrderFragment.btnTxt = getString(R.string.rate);
                                JSONArray request = jsonObject.getJSONArray("requestId");
                                OrderFragment.meh_id = request.getJSONObject(0).getInt("request_id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DialogFragment dialog = OrderFragment.newInstance(getBaseContext());
                        dialog.show(ProfileClientActivity.this.getFragmentManager(), "OrderFragment");
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileClientActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(tabHost, getString(R.string.check_network), Snackbar.LENGTH_LONG)
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
                params.put("mehani_id", mehani_id + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private static final String JSON_URL_FAV = ConstantsFreelance.SERVER + "/make_favorite?";

    private void addToFavorite() {
        progressDialog = new ProgressDialog(ProfileClientActivity.this);
        progressDialog.setMessage(getString(R.string.fav));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_FAV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            Toast.makeText(ProfileClientActivity.this, jsonObject.getString(getString(R.string.message)), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileClientActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(tabHost, getString(R.string.check_network), Snackbar.LENGTH_LONG)
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
                params.put("mehani_id", mehani_id + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void initCollapsingToolbar() {

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
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
    public static void addClientToDatabase(ClientProfile clientProfile) {
        ContentValues values = new ContentValues();
        values.put(FreelanceDbHelper.UID, clientProfile.getId());
        values.put(FreelanceDbHelper.NAME, clientProfile.getName());
        values.put(FreelanceDbHelper.ADDRESS, clientProfile.getAddress());
        values.put(FreelanceDbHelper.SKILLS, clientProfile.getSkills());
        values.put(FreelanceDbHelper.EDUCATION, clientProfile.getEducation());
        values.put(FreelanceDbHelper.EXPERIENCE, clientProfile.getExperience());
        values.put(FreelanceDbHelper.PHONE, clientProfile.getPhone());
        values.put(FreelanceDbHelper.EX_YEARS, clientProfile.getYears_experience());
        values.put(FreelanceDbHelper.EVALUATION, clientProfile.getEducation());
        values.put(FreelanceDbHelper.AVAILABLE, clientProfile.getAvailable());
        values.put(FreelanceDbHelper.GOVERNMENT, clientProfile.getGovernment());
        values.put(FreelanceDbHelper.CITY, clientProfile.getCity());
        values.put(FreelanceDbHelper.CARRIER, clientProfile.getCarrier());
        values.put(FreelanceDbHelper.RATE, clientProfile.getRate());

        Uri uri = mContext.getContentResolver().insert(FreelanceContract.FreelanceEntry.CONTENT_URI, values);
    }

    public static void switchTab(int tab) {
        viewPager.setCurrentItem(tab);
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private static final String JSON_URL = ConstantsFreelance.SERVER + "/worker/profile?";
    ProgressDialog progressDialog;

    public void getClientProfileData() {
        progressDialog = new ProgressDialog(ProfileClientActivity.this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON parseJSON = new ParseJSON(response);
                        parseJSON.parseProfileClient();
                        progressDialog.setCancelable(false);
                        progressDialog.cancel();
                        if (ParseJSON.status_client_profile) {
                            addDataToLayout();
                            addClientToDatabase(ParseJSON.clientProfile);
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
                        Toast.makeText(ProfileClientActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                        Snackbar.make(tabHost, getString(R.string.check_network), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        dataView.setVisibility(View.GONE);
                        noConnectionView.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", mehani_id + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addDataToLayout() {
        dataView.setVisibility(View.VISIBLE);
        noConnectionView.setVisibility(View.GONE);
        ClientProfile clientProfile = ParseJSON.clientProfile;
        name.setText(clientProfile.getName());

        if (clientProfile.getAvailable() == 1) {
            available.setText(R.string.available);
            available.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            available.setText(R.string.not_available);
            available.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        RatingBar review = (RatingBar) findViewById(R.id.profile_rate);
        review.setRating(clientProfile.getRate());
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//        Drawable tabs[] = {getResources().getDrawable(R.drawable.unshipped), (getResources().getDrawable(R.drawable.shipped)), (getResources().getDrawable(R.drawable.profile))};

        String tabs[] = {getString(R.string.detail)};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            /**
             * Set fragment to different fragments depending on position in ViewPager
             */
            switch (position) {
                case 0:
                    fragment = DetailProfileClientFragment.getInstance(getBaseContext());
                    break;
                default:
                    fragment = DetailProfileClientFragment.getInstance(getBaseContext());
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
        //        public Drawable getIcon(int position) {
//            return tabs[position];
//        }
    }
}
