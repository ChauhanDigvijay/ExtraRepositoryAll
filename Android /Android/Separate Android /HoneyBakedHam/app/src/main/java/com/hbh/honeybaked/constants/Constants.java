package com.hbh.honeybaked.constants;

public class Constants {
    public static final int ClpSdkHbh = 11;
    public static final int ClpSdkNewApi = 4;
    public static final int ClpSdkNewDemo = 7;
    public static final int ClpSdkNewDemoLive = 9;
    public static final int ClpSdkNewQA = 8;
    public static final int ClpSdkStgHbh = 10;
    public static final int DandaVpn = 1;
    public static final int NoVpn = 2;
    public static final int SPLASH_TIME_OUT = 1000;
    public static final int SPLASH_TIME_OUT1 = 100000;
    public static final int fishbowlStgPushPass = 6;
    public static final int portalStaging = 3;
    public static final int portalstagingdeltaco = 5;

    public static String sdkPointingUrl(int pointingServerUrl) {
        switch (pointingServerUrl) {
            case 1:
                return "http://dev-jamba.fishbowlcloud.com:8080/FBAppWSv1/clpapi/";
            case 2:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
            case 3:
                return "https://portalstaging-jamba.fishbowlcloud.com/clpapi/";
            case 4:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
            case 5:
                return "https://portalstaging-deltaco.fishbowlcloud.com/clpapi/";
            case 6:
                return "https://stg-jamba.fishbowlcloud.com/clpapi/";
            case 7:
                return "https://demo-fb.fishbowlcloud.com/clpapi/";
            case 8:
                return "https://qa-jamba.fishbowlcloud.com/clpapi/";
            case 9:
                return "https://demo-live.fishbowlcloud.com/clpapi/";
            case 10:
                return "https://stg-hbh.fishbowlcloud.com/clpapi/";
            case 11:
                return "https://hbh.fishbowlcloud.com/clpapi/";
            default:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
        }
    }
}
