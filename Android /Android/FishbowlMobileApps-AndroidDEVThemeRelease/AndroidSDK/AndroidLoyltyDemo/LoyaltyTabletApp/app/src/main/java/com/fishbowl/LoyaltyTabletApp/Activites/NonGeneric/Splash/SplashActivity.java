package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;

import org.json.JSONObject;

/**
 * Created by schaudhary_ic on 24-Nov-16.
 */

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    ProgressBarHandler progressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        progressBarHandler = new ProgressBarHandler(this);
        setContentView(R.layout.activity_splash);
        getThemeMobileSettingsServiceSettings();
    }

//    public void getMobileSettings() {
//
//        final JSONObject object = new JSONObject();
//        FB_LY_MobileSettingService.sharedInstance().getViewSettings(object, new FB_LY_MobileSettingService.FBViewSettingsCallback() {
//            @Override
//            public void onViewSettingsCallback(JSONObject response, Exception error) {
//
//                if (response != null) {
//                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
//                    startActivity(i);
//                    finish();
//                } else {
//
//                }
//            }
//        });
//    }

    public void getThemeMobileSettingsServiceSettings() {

        final JSONObject object = new JSONObject();

        FBThemeMobileSettingsService.sharedInstance().getThemeSettings(object, new FBThemeMobileSettingsService.FBThemeSettingsCallback() {
            @Override
            public void onThemeSettingsCallback(JSONObject response, final Exception error) {

                if (response != null) {
                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                } else {

                }
            }
        });
    }

}

