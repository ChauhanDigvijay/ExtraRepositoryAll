package com.fishbowl.cbc.utils;

import com.fishbowl.cbc.R;

/**
 * Created by VT027 on 5/19/2017.
 */

public class Constants {

    public final static int PHONE_NUMBER_LENGTH = 10;
    public final static int PASSWORD_MIN_LENGTH = 6;
    public final static int PASSWORD_MAX_LENGTH = 15;

    public static final int[] AVATAR_ICONS = new int[]{R.drawable.basket};
    public static final int[] AVATAR_ICONS_BG = new int[]{R.color.gray};

    public final static String BROADCAST_UPDATE_HOME_ACTIVITY = "BROADCAST_UPDATE_HOME_ACTIVITY";
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


    /**
     * TODO: olo demo vendor ids
     */
    public final static int OloDemoVendorStoreId = 0;
    public final static int OloDemoLabVendorStoreId = 0;

    /**
     * TODO: fill: fishbowl api key
     */
    public final static String FBApiKey = "";

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
                return "https://stg-jamba.fishbowlcloud.com/clpapi/";
            case FBSdkNewDemo:
                return "https://demo-fb.fishbowlcloud.com/clpapi/";
            case FBSdkNewQA:
                return "https://qa-jamba.fishbowlcloud.com/clpapi/";
            case FBSdkNewProduction:
                return "https://jamba.fishbowlcloud.com/clpapi/";
            default:
                return "http://jamba.clpqa.com/clpapi/";

        }

    }

    public static int getEnvironment() {
        return FBSdkNewProduction;
    }

    //Google Analytics Constants
    public enum GA_CATEGORY {
        UX("ux"), USER_ACCOUNT("user_account"), ORDER_AHEAD("order_ahead"), SEARCH("search"), STORES("stores");
        public String value;

        private GA_CATEGORY(String val) {
            this.value = val;
        }
    }

}
