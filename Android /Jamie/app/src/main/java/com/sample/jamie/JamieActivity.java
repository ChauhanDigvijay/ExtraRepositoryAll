package com.sample.jamie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.FBUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class JamieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToken();
    }

    public void getToken() {

        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback() {


            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    //   Constants.alertDialogShow(BasicMainActivity.this,"GetToken Success Message");
                    String secratekey = null;
                    try {
                        secratekey = response.getString("message");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    FBPreferences.sharedInstance(JamieActivity.this).setAccessToken(secratekey);
                    Constants.alertDialogShow(JamieActivity.this, "getTokenApi Success Message" + response);
                    //  mobileSettings();
                    registerMemberUpdatebyEmailApi();

                } else {

                    Constants.alertDialogShow(JamieActivity.this, "getTokenApi Error Message");
                    FBUtils.tryHandleTokenExpiry(JamieActivity.this, error);

                }

            }
        });
    }


    public void registerMemberUpdatebyEmailApi() {

        final JSONObject object = new JSONObject();
        try {

            object.put("email", "vkumarsss_ic@fishbowl.com");// your email id
            object.put("appId", "com.fishbowl.BasicApp"); //  your package id
            object.put("pushToken", FBPreferences.sharedInstance(this).getPushToken());
            object.put("pushOptIn", true);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOSVersion", FBConstant.device_os_ver);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().createMemberUpdatebyEmail(object, new FBUserService.FBCreateMemberCallback() {
            @Override
            public void onCreateMemberCallback(JSONObject response, Exception error) {
                if (error == null && response != null) {

                    Constants.alertDialogShow(JamieActivity.this, "createMemberUpdatebyEmail Success Message" + response);

                } else {
                    Constants.alertDialogShow(JamieActivity.this, "createMemberUpdatebyEmail Error Message");
                    FBUtils.tryHandleTokenExpiry(JamieActivity.this, error);
                }
            }
        });
    }

}
