package com.sadek.apps.freelance7rfeen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sadek.apps.freelance7rfeen.R;

public class ChooseRegisterActivity extends AppCompatActivity {

    View mRegisterFreelancer, mRegisterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_choose_register);
        mRegisterFreelancer = findViewById(R.id.register_freelancer);
        mRegisterUser = findViewById(R.id.register_user);
        mRegisterFreelancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterAgentActivity.class));
            }
        });
        mRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterUserActivity.class));
            }
        });
    }
}
