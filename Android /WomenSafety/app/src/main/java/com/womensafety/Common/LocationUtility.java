package com.womensafety.Common;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

public class LocationUtility {
    private static final String TAG = LocationUtility.class.getSimpleName();
    private Context context;
    private boolean gps_enabled = false;
    private Location lastLocation = null;
    private LocationManager lm;
    LocationListener locationListenerGps = new C06851();
    LocationListener locationListenerNetwork = new C06862();
    private LocationResult locationResult;
    private Object lock = new Object();
    private boolean network_enabled = false;

    class C06851 implements LocationListener {
        C06851() {
        }

        public void onLocationChanged(Location loc) {
            if (this == LocationUtility.this.locationListenerGps) {
            }
            LocationUtility.this.lastLocation = loc;
            if (LocationUtility.this.lm != null) {
                LocationUtility.this.lm.removeUpdates(this);
                if (LocationUtility.this.locationListenerNetwork != null) {
                    LocationUtility.this.lm.removeUpdates(LocationUtility.this.locationListenerNetwork);
                }
            }
            synchronized (LocationUtility.this.lock) {
                LocationUtility.this.lock.notifyAll();
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    class C06862 implements LocationListener {
        C06862() {
        }

        public void onLocationChanged(Location loc) {
            if (this == LocationUtility.this.locationListenerNetwork) {
            }
            LocationUtility.this.lastLocation = loc;
            if (LocationUtility.this.lm != null) {
                LocationUtility.this.lm.removeUpdates(this);
                if (LocationUtility.this.locationListenerGps != null) {
                    LocationUtility.this.lm.removeUpdates(LocationUtility.this.locationListenerGps);
                }
            }
            synchronized (LocationUtility.this.lock) {
                LocationUtility.this.lock.notifyAll();
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    class GetLastLocation implements Runnable {
        GetLastLocation() {
        }

        public void run() {
            try {
                synchronized (LocationUtility.this.lock) {
                    LocationUtility.this.lock.wait(1000);
                }
            } catch (InterruptedException e) {
            }
            if (LocationUtility.this.lastLocation != null) {
                LocationUtility.this.locationResult.gotLocation(LocationUtility.this.lastLocation);
                return;
            }
            Location net_loc = null;
            Location gps_loc = null;
            if (LocationUtility.this.gps_enabled && LocationUtility.this.lm != null) {
                gps_loc = LocationUtility.this.lm.getLastKnownLocation("gps");
                LocationUtility.this.lm.removeUpdates(LocationUtility.this.locationListenerGps);
            }
            if (LocationUtility.this.network_enabled && LocationUtility.this.lm != null) {
                net_loc = LocationUtility.this.lm.getLastKnownLocation("network");
                LocationUtility.this.lm.removeUpdates(LocationUtility.this.locationListenerNetwork);
            }
            if (gps_loc == null || net_loc == null) {
                if (net_loc != null) {
                    LocationUtility.this.locationResult.gotLocation(net_loc);
                } else if (gps_loc != null) {
                    LocationUtility.this.locationResult.gotLocation(gps_loc);
                } else {
                    LocationUtility.this.locationResult.gotLocation(null);
                }
            } else if (gps_loc.getTime() > net_loc.getTime()) {
                LocationUtility.this.locationResult.gotLocation(gps_loc);
            } else {
                LocationUtility.this.locationResult.gotLocation(net_loc);
            }
        }
    }

    public interface LocationResult {
        void gotLocation(Location location);
    }

    public LocationUtility(Context context) {
        this.context = context;
    }

    public boolean getLocation(LocationResult result) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        this.lastLocation = null;
        this.locationResult = result;
        if (this.lm == null) {
            this.lm = (LocationManager) this.context.getSystemService(Param.LOCATION);
        }
        try {
            this.network_enabled = this.lm.isProviderEnabled("network");
            this.gps_enabled = this.lm.isProviderEnabled("gps");
            if (!(this.gps_enabled || this.network_enabled)) {
                return false;
            }
        } catch (Exception ex) {
            Log.w(TAG, "Exception checking location manager status", ex);
        }
        new Thread(new GetLastLocation()).start();
        synchronized (this.lock) {
            if (this.network_enabled) {
                this.lm.requestLocationUpdates("network", 0, 100.0f, this.locationListenerNetwork);
            }
            if (this.gps_enabled) {
                this.lm.requestLocationUpdates("gps", 0, 100.0f, this.locationListenerGps);
            }
        }
        return true;
    }

    public void stopGpsLocUpdation() {
        if (this.lm != null) {
            this.lm.removeUpdates(this.locationListenerNetwork);
            this.lm.removeUpdates(this.locationListenerGps);
        }
        this.lm = null;
    }
}
