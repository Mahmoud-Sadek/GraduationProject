package com.sadek.apps.freelance7rfeen.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sadek.apps.freelance7rfeen.R;
import com.sadek.apps.freelance7rfeen.model.Speciality;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;
import com.sadek.apps.freelance7rfeen.widget.FreelanceWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class SplashActivity extends FragmentActivity {

    private Animation animation;
    private ImageView logo;
    private TextView appTitle;
    private TextView appSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logo_img);
        appTitle = (TextView) findViewById(R.id.track_txt);
        appSlogan = (TextView) findViewById(R.id.pro_txt);

        // Font path
        String fontPath = "font/CircleD_Font_by_CrazyForMusic.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Applying font
        appTitle.setTypeface(tf);
        appSlogan.setTypeface(tf);

        if (savedInstanceState == null) {
            flyIn();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                endSplash();
            }
        }, 3000);
    }

    private void flyIn() {
        animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this,
                R.anim.app_name_animation);
        appTitle.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.pro_animation);
        appSlogan.startAnimation(animation);
    }

    List<Speciality> Specialtie;

    private void endSplash() {
        animation = AnimationUtils.loadAnimation(this,
                R.anim.logo_animation_back);
        logo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this,
                R.anim.app_name_animation_back);
        appTitle.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this,
                R.anim.pro_animation_back);
        appSlogan.startAnimation(animation);

        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                insertData();
                int position = getIntent().getIntExtra(FreelanceWidgetProvider.EXTRA_WORD, 100);
                if (position != 100) {
                    SubSpecializationActivity.categorie = Specialtie.get(position);
                    SubSpecializationActivity.categoryNum = position;
                    Intent n = new Intent(getBaseContext(), SubSpecializationActivity.class);
                    n.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(n);

                } else {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Intent intent;
                    if (sp.getInt(ConstantsFreelance.USER_TYPE, 3) == 0) {
                        intent = new Intent(getApplicationContext(),
                                SpecializationActivity.class);
                    } else if (sp.getInt(ConstantsFreelance.USER_TYPE, 3) == 1) {
                        intent = new Intent(getApplicationContext(),
                                SpecializationActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(),
                                LoginActivity.class);
                    }

                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    private void insertData() {
        Specialtie = new ArrayList<>();
        for (int i = 0; i < ConstantsFreelance.SPECIALTY_NAME.length; i++) {
            Specialtie.add(new Speciality(ConstantsFreelance.SPECIALTY_NAME[i], ConstantsFreelance.SPECIALTY_IMAGE[i]));
        }
    }
}
