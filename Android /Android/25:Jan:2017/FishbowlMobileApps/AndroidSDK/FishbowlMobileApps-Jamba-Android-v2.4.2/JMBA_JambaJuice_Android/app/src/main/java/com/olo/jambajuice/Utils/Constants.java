package com.olo.jambajuice.Utils;

import com.fishbowl.basicmodule.Models.FBConfig;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;

/**
 * Created by Nauman Afzaal on 22/04/15.
 */
public class Constants {
    // Splash screen timer
    public final static int SPLASH_TIME_OUT = 1000;
    public final static int SPLASH_TIME_OUT1 = 100000;
    //Alert Message
    public final static String ADD_PRODUCT_OR_REMOVE_PRODUCT_ALERT_MESSAGE = "Changing basket contents will remove applied discounts. Coupon/reward will need to be re-applied manually. Proceed with changes?";
    public final static String APPLY_REWARD_OR_APPLY_COUPON_MESSAGE = "Only one coupon/reward may be applied at a time. Do you want to remove the existing coupon/reward and apply a new one?";
    //Bundle Constants
    public final static String B_FIRST_NAME = "B_FIRST_NAME";
    public final static String B_LAST_NAME = "B_LAST_NAME";
    public final static String B_EMAIL = "B_EMAIL";
    public final static String B_EMAIL_OPT_IN = "B_EMAIL_OPT_IN";
    public final static String B_SMS_OPT_IN = "B_SMS_OPT_IN";
    public final static String B_PUSH_OPT_IN = "B_PUSH_OPT_IN";
    public final static String B_IS_REFRESH_OFFER = "B_IS_REFRESH_OFFER";
    public final static String B_CONTACT_NUMBER = "B_CONTACT_NUMBER";
    public final static String B_DOB = "B_DOB";
    public final static String B_IS_CHOOOSE_STORE_FROM_PROD_DETAIL = "B_IS_CHOOOSE_STORE_FROM_PROD_DETAIL";
    public final static String B_IS_CREATED_FROM_PASSWORD = "B_IS_CREATED_FROM_PASSWORD";
    public final static String B_IS_CHOOOSE_STORE_FROM_SIGN_UP = "B_IS_CHOOOSE_STORE_FROM_SIGN_UP";
    public final static String B_URL = "B_URL";
    public final static String B_TITLE = "B_TITLE";
    public final static String B_PRODUCT = "B_PRODUCT";
    public final static String B_PRODUCT_CATEGORY = "B_PRODUCT_CATEGORY";
    public final static String B_PRODUCTS = "B_PRODUCTS";
    public final static String B_PRODUCT_DETAIL_POSITION = "B_PRODUCT_DETAIL_POSITION";
    public final static String B_BASKET_PRODUCT = "B_BASKET_PRODUCT";
    public final static String B_PREFERRED_STORE_ID = "B_PREFERRED_STORE_ID";
    public final static String B_ORDER_DETAIL = "B_ORDER_DETAIL";
    public final static String B_RECENT_ORDER = "B_RECENT_ORDER";
    public final static String B_RECENT_FAVORITE = "B_RECENT_FAVORITE";
    public final static String B_NEW_FAVORITE = "B_NEW_FAVORITE";
    public final static String B_IS_STORE_DETAIL_ONLY = "B_IS_STORE_DETAIL_ONLY";
    public static final String B_IS_FROM_SETTINGS = "B_IS_FROM_SETTINGS";
    public final static String B_IS_CHECKOUT_STORE_DETAIL_ONLY = "B_IS_CHECKOUT_STORE_DETAIL_ONLY";
    public final static String B_STORE = "B_STORE";
    public static final String B_IS_SHOW_BASKET = "B_IS_SHOW_BASKET";
    public static final String B_OPEN_MENU = "B_OPEN_MENU";
    public static final String B_IS_OPENING_FROM_SETTING = "B_IS_OPENING_FROM_SETTING";
    public static final String B_FEEDBACK_TYPE = "B_FEEDBACK_TYPE";
    public static final String B_MODIFIER_GROUP = "B_MODIFIER_GROUP";
    public static final String B_MODIFIER_GROUP_CHILD = "B_MODIFIER_GROUP_CHILD";
    public static final String B_MODIFIER_SUBSTITUTE = "B_MODIFIER_SUBSTITUTE";
    public static final String B_MODIFIER_PRODUCT_ID = "B_MODIFIER_PRODUCT_ID";
    public static final String B_IS_IT_FROM_WELCOME_SCREEN = "B_IS_IT_FROM_WELCOME_SCREEN";
    public static final String B_IS_IT_FROM_WELCOME_SCREEN_NO_PREFERRED_STORE = "B_IS_IT_FROM_WELCOME_SCREEN_NO_PREFERRED_STORE";

    public static final String B_IS_IT_FROM_BASKET_STORE_SCREEN = "B_IS_IT_FROM_BASKET_STORE_SCREEN";
    public static final String B_IS_IT_FROM_SETTINGS_SCREEN = "B_IS_IT_FROM_SETTINGS_SCREEN";

    public final static int PHONE_NUMBER_LENGTH = 10;
    public final static int PASSWORD_MIN_LENGTH = 6;
    public final static int PASSWORD_MAX_LENGTH = 15;
    public final static int MaxStores = 20;
    public final static double DefaultRadiusInMiles = 60;
    public final static int TOTAL_BASKET_PRODUCTS = 10;
    public final static int SPENDGO_SIGNUP_AGE_LIMIT = 18;// Jamba Insiders must be at least 18 years old
    public static final int ERROR_US_LOCATIONS = 999;
    public final static String DEFAULT_SEARCH_LOCATION = "San Luis Obispo, California";


    //UTC Time Constansts
    public final static String UTC_TIME_1AM = " 1:00:00";


    public static final int[] AVATAR_ICONS = new int[]{R.drawable.apple, R.drawable.blueberry, R.drawable.orange, R.drawable.strawberry};
    public static final int[] AVATAR_ICONS_BG = new int[]{R.color.apple, R.color.blueberry, R.color.avatar_orange_bg, R.color.strawberry};
    //Broadcast Constants
    public final static String BROADCAST_UPDATE_HOME_ACTIVITY = "BROADCAST_UPDATE_HOME_ACTIVITY";
    public final static String BROADCAST_UPDATE_GF_HOME_ACTIVITY = "BROADCAST_UPDATE_GF_HOME_ACTIVITY";
    public final static String BROADCAST_GF_HOME_ACTIVITY_REFRESH_UI = "BROADCAST_GF_HOME_ACTIVITY_REFRESH_UI";
    public final static String BROADCAST_REMOVE_STORE_LOCATOR_ACTIVITY = "BROADCAST_REMOVE_STORE_LOCATOR_ACTIVITY";
    public final static String BROADCAST_LOCATION_SERVICE_NOT_AVAILABLE = "BROADCAST_LOCATION_SERVICE_NOT_AVAILABLE";
    public final static String BROADCAST_REMOVE_BASKET_UI = "BROADCAST_REMOVE_BASKET_UI";
    public final static String BROADCAST_AUTH_TOKEN_FAILURE = "BROADCAST_AUTH_TOKEN_FAILURE";
    public final static String BROADCAST_UPDATE_RECENT_ORDER = "BROADCAST_UPDATE_RECENT_ORDER";
    public final static String INVALID_PASSWORD_ERROR = "Your password must be at least " + Constants.PASSWORD_MIN_LENGTH + " and at max " + Constants.PASSWORD_MAX_LENGTH + " characters long.";
    public final static int QA = 1;
    public final static int DEMO = 2;
    public final static int SALESDEMO = 3;
    public final static int STAGING = 4;
    public final static int Production = 5;
    public final static int MainProduction = 6;
    public final static int FBSdkNewDev = 7;
    public final static int FBSdkNewStagging = 8;
    public final static int FBSdkNewDemo = 9;
    public final static int FBSdkNewQA = 10;
    public final static int FBSdkNewProduction = 11;
    public final static int FBSdkNewHotfix = 12;
    //Transaction Status
    public final static int STARTED = 1;
    public final static int CANCELLED = 2;
    public final static int COMPLETED = 3;
    public final static int FAILED = 4;
    //App Permission Codes
    public final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    //Incomm Error Codes
    public final static int InCommFailure_Unauthorized = 401000;
    public final static int InCommFailure_GiftCardProcessorFailure = 500202;
    public final static String NO_INTERNET_CONNECTION = "Please check your network connection and try again.";
    //Volley message constants
    public final static String VolleyFailure_UnAuthorizedMessage = "No authentication challenges found";
    public final static String VolleyFailure_ServerErrorMessage = "com.android.volley.servererror";
    public final static String IncommTokenExpired = "Token Expired";
    //App Permission Codes
    public static int MAX_CREDIT_CARD_EXPIRY_YEARS = 100;            // At max the credit card should expire this many years from current year

    //Development
    public final static int OloDemoVendorStoreId = 24872;
    public final static int OloDemoLabVendorStoreId = 8687;

    public final static String FBApiKey = "91225258ddb5c8503dce33719c5deda7";

    public final static String DEFAULT_ERROR_MESSAGE = "There was a problem connecting to the server. Please try again later.";

    public static String sdkPointingUrl(int pointingServerUrl) {

        switch (pointingServerUrl) {
            case QA:
                return "http://jamba.clpqa.com/clpapi/";
            case DEMO:
                return "http://demo.clpdemo.com/clpapi/";
            case SALESDEMO:
                return "http://salesfb.clpdemo.com/clpapi/";
            case STAGING:
                return "http://jamba.clpstaging.com/clpapi/";
            case Production:
                return "https://test.clyptechs.com/clpapi/";

            case MainProduction:
                return "https://jamba.clyptechs.com/clpapi/";

            case FBSdkNewDev:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
            case FBSdkNewStagging:
//                return "https://stg-hbh.fishbowlcloud.com/clpapi/";
                return "https://stg-jamba.fishbowlcloud.com/clpapi";
            case FBSdkNewDemo:
                return "https://demo-fb.fishbowlcloud.com/clpapi/";
            case FBSdkNewQA:
                return "https://qa-jamba.fishbowlcloud.com/clpapi/";
            case FBSdkNewProduction:
                return "https://jamba.fishbowlcloud.com/clpapi/";
            case FBSdkNewHotfix:
                return "https://hotfix-jamba.fishbowlcloud.com/clpapi";
            default:
                return "http://jamba.clpqa.com/clpapi/";

        }
    }

    public static FBConfig getFBConfig(int pointingServerUrl) {
        FBConfig fbconfig = new FBConfig();
        fbconfig.setClpApiKey(FBApiKey);

        switch (pointingServerUrl) {

            case FBSdkNewQA:
                fbconfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
                fbconfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A6");
                fbconfig.setGcmSenderId(JambaApplication.getAppContext().getString(R.string.gcm_sender_id_development));
                fbconfig.setSmallpushIconResource(R.drawable.jamba_header_logo);
                break;

            case FBSdkNewStagging:
                fbconfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
                fbconfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A9");
                fbconfig.setGcmSenderId(JambaApplication.getAppContext().getString(R.string.gcm_sender_id_development));
                fbconfig.setSmallpushIconResource(R.drawable.jamba_header_logo);
                break;

            case FBSdkNewProduction:
                fbconfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB8");
                fbconfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A5");
                fbconfig.setGcmSenderId(JambaApplication.getAppContext().getString(R.string.gcm_sender_id_production));
                fbconfig.setSmallpushIconResource(R.drawable.jamba_header_logo);
                break;

            default:break;

        }
        fbconfig.setClient_tenantid("1173");
        fbconfig.setPushIconResource(R.drawable.jamba_header_logo);
        return fbconfig;
    }

    public static int getEnvironment(){
        return FBSdkNewQA;
    }

    //Google Analytics Constants
    public enum GA_CATEGORY {
        UX("ux"), USER_ACCOUNT("user_account"), ORDER_AHEAD("order_ahead"), SEARCH("search"), STORES("stores");
        public String value;

        private GA_CATEGORY(String val) {
            this.value = val;
        }
    }

    //Notification Tags
    public enum NotificationTag {
        FirstPurchase("first_purchase"), NoPurchase("no_purchases");
        public String value;

        private NotificationTag(String val) {
            this.value = val;
        }
    }
}
