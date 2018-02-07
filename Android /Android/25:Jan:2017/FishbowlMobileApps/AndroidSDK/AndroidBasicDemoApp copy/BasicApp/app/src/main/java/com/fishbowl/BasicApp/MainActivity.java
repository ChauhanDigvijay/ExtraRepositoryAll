package com.fishbowl.BasicApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Services.FBService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;

import org.json.JSONObject;
/**
 * Created by digvijay(dj)
 */
public class MainActivity extends AppCompatActivity implements FBSdk.OnFBSdkRegisterListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeCLPSDK();
    }

    private void initializeCLPSDK() {
        try {

            FBService.initialize(this, Constants.sdkPointingUrl(Constants.QA),"null");
            FBCustomerItem customer = new FBCustomerItem();

            //if (FBUtility.checkPlayServices(this))
            {
                //check user is already login
                /*if (USer.isUserAuthenticated()) {
                    User user = UserService.getUser();
                    customer = collectCustomerData(user);
                }*/
                // CLP Registration
                FBConfig FBConfig = new FBConfig();
                FBConfig.setClpApiKey("91225258ddb5c8503dce33719c5deda7");
               // FBConfig.setGcmSenderId(FBConstant.gcm_sender_id_dev);
                FBConfig.setPushIconResource(R.drawable.ic_launcher);
                //clpsdkObj = FBSdk.sharedInstance(this, Constants.sdkPointingUrl(Constants.portalstagingdeltaco));

            FBSdk.sharedInstanceWithKey(this, Constants.sdkPointingUrl(Constants.QA), MainActivity.this, FBConfig, customer,false);
                getMobileSettings();
            }
        } catch (Exception e) {
            Log.i("Error ", "error");
        }
    }
    public void getMobileSettings() {

        final JSONObject object = new JSONObject();

        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewMobileSettingsService.FBViewSettingsCallback() {
            @Override
            public void onViewSettingsCallback(FBViewMobileSettingsService instance, final Exception error) {

                if(instance !=null) {
                    Intent i = new Intent(MainActivity.this, BasicMainActivity.class);
                    startActivity(i);
                   MainActivity.this.finish();
                }
            }
        });
    }
    @Override
    public void onFBRegistrationSuccess(FBCustomerItem fbCustomerItem) {

    }

    @Override
    public void onFBRegistrationError(String s) {

    }
}
