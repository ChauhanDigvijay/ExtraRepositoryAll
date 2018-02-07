package com.android.Jcenter;

public class Constants {


    public final static int QA = 1;
    public final static int Stagging  = 2;



    public static String sdkPointingUrl(int pointingServerUrl) {
        switch (pointingServerUrl) {
            case QA:
                return "https://qa-jamba.fishbowlcloud.com/clpapi/";
            case Stagging:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";


            default:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";
        }
    }
}
