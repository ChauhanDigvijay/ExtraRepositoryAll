package com.fishbowl.LoyaltyTabletApp.Utils;

public class Constants {
    // Splash screen timer
    public final static int SPLASH_TIME_OUT = 1000;
    public final static int SPLASH_TIME_OUT11 = 500;

    public final static int SPLASH_TIME_OUT1 = 100000;
    //Bundle Constants
    public final static int DandaVpn = 1;
    public final static int NoVpn = 2;
    public final static int portalStaging = 3;
    public final static int FBSdkDevJamba = 4;
    public final static int portalstagingdeltaco = 5;
    public final static int fishbowlStgPushPass = 6;
    public final static int FBSdkNewDemo = 7;
    public final static int FBSdkNewQA = 8;
    public final static int FBSdkHBH_STG = 9;


    public static String sdkPointingUrl(int pointingServerUrl) {
        switch (pointingServerUrl) {
            case DandaVpn:
                return "http://dev-jamba.fishbowlcloud.com:8080/FBAppWSv1/clpapi/";
            case NoVpn:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
            case portalStaging:
                return "https://stg-seaisland.fishbowlcloud.com/clpapi/";
            case FBSdkDevJamba:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
            case portalstagingdeltaco:
                return "https://portalstaging-deltaco.fishbowlcloud.com/clpapi/";
            case fishbowlStgPushPass:
                return "https://stg-jamba.fishbowlcloud.com/clpapi/";

            case FBSdkNewDemo:
                return "https://demo-live.fishbowlcloud.com/clpapi/";
            case FBSdkNewQA:
                return "https://qa-jamba.fishbowlcloud.com/clpapi";

            case FBSdkHBH_STG:
                return "https://seaisland.fishbowlcloud.com/clpapi/";

            default:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
        }
    }
}
