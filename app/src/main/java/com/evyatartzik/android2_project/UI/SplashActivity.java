package com.evyatartzik.android2_project.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.evyatartzik.android2_project.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {


    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!


    @Override
    public void initSplash(ConfigSplash configSplash) {

        //Customize Circular Reveal
        configSplash.setAnimCircularRevealDuration(750);
        configSplash.setBackgroundColor(R.color.primary_dark);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.quickfix_logo);
        configSplash.setAnimLogoSplashDuration(500);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("QuickFit");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(40f); //float value
        configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.DropOut);

    }

    @Override
    public void animationsFinished() {
        boolean isFirstTime = this.getSharedPreferences("settings", MODE_PRIVATE).getBoolean("isFirstTime", true);

        if (isFirstTime) {
            Intent intro = new Intent(this, TutorialActivity.class);
            startActivity(intro);
        } else {
            Intent main = new Intent(this, LoginRegister.class);
            startActivity(main);
        }
        finish();
    }
}
