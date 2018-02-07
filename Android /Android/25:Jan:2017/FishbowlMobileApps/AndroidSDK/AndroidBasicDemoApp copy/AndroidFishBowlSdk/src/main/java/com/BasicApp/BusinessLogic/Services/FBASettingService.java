package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;

import org.json.JSONObject;


/**
 * Created by digvijay(dj)
 */
public class FBASettingService {

    public static FBASettingService instance=null;
    public static Activity mContext;
    public static FBASettingService sharedInstance(Activity context){

        if(instance==null){
            instance=new FBASettingService(context);
        }

        return  instance;
    }
    public FBASettingService(Activity context)
    {
        if(context == null);
        mContext = context;
    }

//    public void getMobileSettings() {
//
//
//        String cusId = "0";
//
//        FBMobileSettingService.sharedInstance().getMobileSetting(cusId, new FBMobileSettingService.FBMobileSettingCallback() {
//            @Override
//            public void OnFBMobileSettingCallback(boolean state, Exception error) {
//                if (state) {
//
//                } else {
//
//
//                }
//            }
//        });
//    }


    public void getViewMobileSettings() {

        final JSONObject object = new JSONObject();

        FBViewMobileSettingsService.sharedInstance().getViewSettings(object, new FBViewMobileSettingsService.FBViewSettingsCallback() {
            @Override
            public void onViewSettingsCallback(FBViewMobileSettingsService instance, final Exception error) {

                if (instance != null) {

                }
            }
        });
    }
}
