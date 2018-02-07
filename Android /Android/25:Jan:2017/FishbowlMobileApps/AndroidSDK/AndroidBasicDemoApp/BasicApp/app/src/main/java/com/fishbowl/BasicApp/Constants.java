package com.fishbowl.BasicApp;

/**
 * Created by digvijay(dj)
 */
public class Constants
{
    // Splash screen timer
    public final static int SPLASH_TIME_OUT = 1000;
    public final static int SPLASH_TIME_OUT1 = 100000;
    //Bundle Constants

    public final static int  ClpSdkCBC = 1;
    public final static int  Test = 2;
    public final static int  portalStaging = 3;
    public final static int  ClpSdkNewApi = 4;
    public final static int  portalstagingdeltaco=5;
    public final static int  fishbowlStgPushPass=6;
    public final static int  ClpSdkNewDemo=7;
    public final static int  ClpSdkNewQA=8;
    public final static int  ClpSdkHRH=9;

    public static  String  sdkPointingUrl(int pointingServerUrl ) {

        switch (pointingServerUrl)
        {
            //return "http://devdmz-jamba.fishbowlcloud.com:8080/FBAppWSv1/clpapi";
            case ClpSdkCBC:
                return "https://stg-cornerbakerycafe.fishbowlcloud.com/clpapi/";
            case Test:
                return "https://test.fishbowlcloud.com/clpapi/";
            case portalStaging:
                return "https://portalstaging-jamba.fishbowlcloud.com/clpapi/";
            case ClpSdkNewApi:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
            case portalstagingdeltaco:
                return "https://portalstaging-deltaco.fishbowlcloud.com/clpapi/";
            case fishbowlStgPushPass:
                return  "https://stg-jamba.fishbowlcloud.com/clpapi/";

            case ClpSdkNewDemo:
                return "https://demo-fb.fishbowlcloud.com/clpapi/";
            case ClpSdkHRH:
                return "https://stg-hbh.fishbowlcloud.com/clpapi/";
           /* case ClpSdkNewQA:
                return "https://qa-jamba.fishbowlcloud.com/clpapi/";*/

            case ClpSdkNewQA:
                return "https://qa-jamba.fishbowlcloud.com/clpapi/";






            default:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";



        }

    }

}
