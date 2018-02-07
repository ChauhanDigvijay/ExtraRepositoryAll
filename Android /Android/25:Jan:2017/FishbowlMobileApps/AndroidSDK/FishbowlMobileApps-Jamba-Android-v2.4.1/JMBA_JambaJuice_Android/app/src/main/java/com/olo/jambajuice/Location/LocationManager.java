package com.olo.jambajuice.Location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationUpdatesCallback;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.Utils.Constants;
import com.wearehathway.apps.olo.Utils.Logger;

import java.lang.ref.WeakReference;


/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    static final int Reconnect_Interval = 3000;
    // Location updates intervals in meters
    static final int Smallest_Location_Displacement = 50;
    public static Location currentLocation;
    static LocationManager instance;
    // Location updates intervals in milliseconds(10 sec)
    private static int UPDATE_INTERVAL = 10 * 1000;
    // Location updates intervals in milliseconds(10 sec)
    private static int FATEST_INTERVAL = 10 * 1000;
    private static Context context;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    WeakReference<Activity> callbackWeakRef;
    Handler handler = new Handler();
    LocationUpdatesCallback callback;
//    private Runnable runnable = new Runnable() {
//        public void run() {
//            if (googleApiClient != null && !googleApiClient.isConnected()) {
//                build();
//            }
//        }
//    };

    public static LocationManager getInstance(Activity activity) {
        context = activity;
        if (instance == null) {
            instance = new LocationManager();
        }
        instance.setCallback(activity);

        return instance;
    }

    private void setCallback(Activity activity) {
        callbackWeakRef = new WeakReference<Activity>(activity);
    }

    public void stopLocationUpdates() {
        this.callback = null;
        this.callbackWeakRef = null;
        if (googleApiClient != null && googleApiClient.isConnected()) {

            if (JambaApplication.getAppContext().fbsdkObj != null) {
                if (JambaApplication.getAppContext().fbsdkObj.getIsLocationService() == true)
                    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

            }

        }
        this.googleApiClient = null;
        this.locationRequest = null;
    }

    public void startLocationUpdates(LocationUpdatesCallback callback) {
        if (callback == null) {
            return;
        }
        this.callback = callback;
        if (isLocationServicesEnabled()) {
            if (googleApiClient != null && googleApiClient.isConnected()) {
                if (JambaApplication.getAppContext().fbsdkObj != null) {
                    if (JambaApplication.getAppContext().fbsdkObj.getIsLocationService() == true)
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }
            } else {
                build();
            }
        } else if (!isLocationServicesEnabled()) {
            showLocationServiceNotAvailableAlert();
        }
    }

    // Callbacks
    @Override
    public void onConnected(Bundle bundle) {
        if (googleApiClient != null) {
            if (JambaApplication.getAppContext().fbsdkObj != null && JambaApplication.getAppContext().fbsdkObj.getIsLocationService() == true)

                currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (currentLocation != null) {
                Logger.i("onConnected Current Location: " + currentLocation.getLatitude() + " == " + currentLocation.getLongitude());
            }
            setUpLocationRequest();
            startLocationUpdates(this.callback);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.e("Error onConnectionSuspended: " + i);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        if (callback != null && !((Activity) callback).isFinishing()) {
            callback.onLocationCallback(location);
        }
        Logger.i("onLocationChanged Current Location: " + currentLocation.getLatitude() + " == " + currentLocation.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        if (callback != null && !((Activity) callback).isFinishing())
//        {
//            callback.onConnectionFailedCallback();
//        }
        Logger.e("Error ConnectionResult: " + connectionResult.getErrorCode());
        if (connectionResult.getErrorCode() == ConnectionResult.TIMEOUT) {
            build();
        }
    }

    //Private Functions
    private void build() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        if (isLocationServicesEnabled()) {
            googleApiClient = new GoogleApiClient.Builder(JambaApplication.getAppContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            googleApiClient.connect();
            //handler.postDelayed(runnable, Reconnect_Interval);
        } else {
            showLocationServiceNotAvailableAlert();
        }
    }

    private void setUpLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(Smallest_Location_Displacement);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FATEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public boolean isLocationServicesEnabled() {
        android.location.LocationManager lm = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        boolean network_enabled = lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
        return gps_enabled || network_enabled;
    }

    public void showLocationServiceNotAvailableAlert() {
        Intent intent = new Intent(Constants.BROADCAST_LOCATION_SERVICE_NOT_AVAILABLE);
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).sendBroadcast(intent);
    }
}
