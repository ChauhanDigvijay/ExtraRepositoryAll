package com.fishbowl.loyaltymodule.Controllers;//package com.loyalty.sdk;


import android.content.Context;

import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserOfferService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class FBSdk {
    public static String SERVER_URL = "";// QA
    protected static final String TAG = "CLPSdk";
    public Context context;
    private static FBSdk instance;
    Boolean sdk=false;

    public static FBSdk sharedInstance(Context ctx, String sdkPointingUrl) {
        if (instance == null) {
            instance = new FBSdk(ctx,sdkPointingUrl);
        }
        return instance;
    }
    public FBSdk(Context context, String sdkPointingUrl) {
        try {
            SERVER_URL = sdkPointingUrl;
            this.context = context;
            initloyaltyManager();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static FBSdk sharedInstance(Context ctx) {

        return instance;
    }

    public void initloyaltyManager(){

       // FB_LY_MobileSettingService.sharedInstance().init(this);
        FB_LY_UserService.sharedInstance().init(this);
        FB_LY_UserOfferService.sharedInstance().init(this);
        FBThemeMobileSettingsService.sharedInstance().init(this);
    }







}