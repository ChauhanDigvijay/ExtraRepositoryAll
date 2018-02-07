package com.fishbowl.BasicApp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.SignInModelActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardModelActivity;
import com.BasicApp.Utils.FBUtils;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by digvijay(dj)
 */
public class BasicMainActivity extends BaseActivity implements View.OnClickListener, FBSdk.OnFBSdkRegisterListener {


    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_main);


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getMobileSettings();
    }

    @Override
    public void onClick(View v) {

    }




    public void getMobileSettings() {

        final JSONObject object = new JSONObject();

        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewMobileSettingsService.FBViewSettingsCallback() {
            @Override
            public void onViewSettingsCallback(FBViewMobileSettingsService instance, final Exception error) {

                if (instance != null) {


                    if (FBPreferences.sharedInstance(AuthApplication.instance).IsSignin()) {

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                //   GetMember();

                                Intent i = new Intent(BasicMainActivity.this, DashboardModelActivity.class);
                                startActivity(i);
                                BasicMainActivity.this.finish();

                            }
                        }, 1500);


                    } else {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Intent i = new Intent(BasicMainActivity.this, SignInModelActivity.class);
                                startActivity(i);
                                BasicMainActivity.this.finish();
                            }
                        }, 1500);

                    }
                } else {

                    FBUtils.tryHandleTokenExpiry(BasicMainActivity.this, error);
                }
            }
        });
    }


    @Override
    public void onFBRegistrationSuccess(FBCustomerItem FBCustomerItem) {

    }

    @Override
    public void onFBRegistrationError(String error) {

    }
}
