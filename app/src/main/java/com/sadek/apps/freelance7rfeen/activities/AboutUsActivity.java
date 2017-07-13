package com.sadek.apps.freelance7rfeen.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sadek.apps.freelance7rfeen.R;

import java.util.Date;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_about_us);
        Date date = new Date();
        String d = date.getDay()+"/"+date.getMonth()+"/"+date.getYear();

        Element adsElement = new Element();
        adsElement.setTitle(getResources().getString(R.string.app_name));

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_us))
                .setImage(R.drawable.am_worker)
                .addItem(new Element().setTitle(getString(R.string.version)))
                .addItem(adsElement)
                .addGroup(getString(R.string.contact_us))
                .addEmail(getString(R.string.mail))
                .addWebsite(getString(R.string.web))
                .addFacebook(getString(R.string.app_name))
//                .addTwitter("medyo80")
//                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore(getString(R.string.packg))
                .addInstagram(getString(R.string.insta))
//                .addGitHub("medyo")
//                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }

//    Element getCopyRightsElement() {
//        Element copyRightsElement = new Element();
//        final String copyrights = String.format("copy_right", Calendar.getInstance().get(Calendar.YEAR));
//        copyRightsElement.setTitle(copyrights);
//        copyRightsElement.setIcon(R.drawable.ashank);
//        copyRightsElement.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
//        copyRightsElement.setGravity(Gravity.CENTER);
//        copyRightsElement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AboutUsActivity.this, copyrights, Toast.LENGTH_SHORT).show();
//            }
//        });
//        return copyRightsElement;
//    }

}
