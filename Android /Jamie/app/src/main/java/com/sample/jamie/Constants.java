package com.sample.jamie;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by digvijay(dj)
 */

public class Constants
{
    // Splash screen timer
    public final static int SPLASH_TIME_OUT = 1000;
    public final static int SPLASH_TIME_OUT1 = 100000;
    //Bundle Constants

    public final static int  DandaVpn = 1;
    public final static int  jamabproduction = 2;
    public final static int  test = 3;
    public final static int  berticussstg = 4;
    public final static int  portalstagingdeltaco=5;
    public final static int  ClpSdkHRHSTG=6;
    public final static int  ClpSdkNewDemo=7;
    public final static int  ClpSdkNewQA=8;
    public final static int  ClpSdkHRH=9;


    public static void alertDialogShow(Context context, String message)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }




    public static  String  sdkPointingUrl(int pointingServerUrl ) {

        switch (pointingServerUrl)
        {
            case DandaVpn:
                return "http://dev-jamba.fishbowlcloud.com:8080/FBAppWSv1/clpapi/";
            case jamabproduction:
                return "https://jamba.fishbowlcloud.com/clpapi/";
            case test:
                return "https://test.fishbowlcloud.com/clpapi/";
            case berticussstg:
                return "https://stg-bertuccis.fishbowlcloud.com/clpapi/";
            case portalstagingdeltaco:
                return "https://stg-seaisland.fishbowlcloud.com/clpapi";
            case ClpSdkHRHSTG:
                return "https://stg-hbh.fishbowlcloud.com/clpapi/";

            case ClpSdkNewDemo:
                return "https://demo-fb.fishbowlcloud.com/clpapi/";
            case ClpSdkNewQA:
                return "https://qa-jamba.fishbowlcloud.com/clpapi/";


            case ClpSdkHRH:
                return " https://demo-live.fishbowlcloud.com/clpapi/";


            default:
                return "http://devdmz-jamba.fishbowlcloud.com/clpapi/";



        }

    }

}
