package com.womensafety.Common;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

public class NetworkUtility {
    public static boolean isNetworkConnectionAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.getState() == State.CONNECTED;
        } else {
            return false;
        }
    }

    public static boolean isWifiConnected(Context context) {
        if (((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isGPSEnable(Context context) {
        try {
            if (!((LocationManager) context.getSystemService(Param.LOCATION)).isProviderEnabled("gps")) {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static boolean isGooglePlayServicesAvailable(AppCompatActivity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status == 0) {
            return true;
        }
        if (googleApiAvailability.isUserResolvableError(status)) {
            googleApiAvailability.getErrorDialog(activity, status, 2404).show();
        }
        return false;
    }
}
