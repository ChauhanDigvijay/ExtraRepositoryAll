package com.olo.jambajuice.Activites.NonGeneric.Store.StoreDetail;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fishbowl.basicmodule.Analytics.FBToastService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationUpdatesCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.Location.LocationManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.GMapV2GetRouteDirection;
import com.olo.jambajuice.Utils.Utils;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.view.ContextThemeWrapper;


public class StoreDirectionActivity extends BaseActivity implements LocationUpdatesCallback, View.OnClickListener, OnCameraMoveStartedListener,
        OnCameraMoveListener, OnMapReadyCallback {

    Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    LatLng currentPosition;
    MarkerOptions markerOptions1;
    MarkerOptions markerOptions2;
    Store selectedStore;
    AlertDialog.Builder gpsAlertBuilder;
    Timer timer;
    private GoogleMap mGoogleMap;
    private GoogleMap googleMap;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    private ImageButton btnGpsActive;
    private Button openApp;
    private boolean isGPSActive;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_direction);
        setUpToolBar(true);
        setTitle("Store Location");
        setBackButton(true, false);
        initData();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedStore = (Store) bundle.getSerializable(Constants.B_STORE);
        }

        if (HomeActivity.homeActivity != null) {
            HomeActivity.homeActivity.checkPermissionsforlocation(this);
        }

        if (Utils.checkEnableGPS(this)) {
            checkLocationServicesEnabled();
        }

    }

    private void loadingDialog() {
        dialog = new ProgressDialog(StoreDirectionActivity.this);
        dialog.setMessage("Fetching Location...");
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void initData() {
        isGPSActive = false;
        openApp = (Button) findViewById(R.id.openApp);
        openApp.getBackground().setAlpha(204); //80% transparent
        openApp.setOnClickListener(this);

        btnGpsActive = (ImageButton) findViewById(R.id.button2);
        btnGpsActive.setVisibility(View.VISIBLE);
        btnGpsActive.setBackgroundResource(R.drawable.gps_pointer_blue);
        btnGpsActive.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.directionMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        int id = v.getId();
        if (id == R.id.button2) {
            isGPSActive = true;
            HomeActivity.homeActivity.checkPermissionsforlocation(this);
            if (Utils.checkEnableGPS(this)) {
                checkLocationServicesEnabled();
                updateGpsPointerImage();
            }
            centerScreen();
        } else if (id == R.id.openApp) {
            Double latitude = 0.0;
            Double longitude = 0.0;

            if (fromPosition != null) {
                latitude = fromPosition.latitude;
                longitude = fromPosition.longitude;
            }
            if (selectedStore != null) {
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitude + "," + longitude + "&daddr=" + selectedStore.getLatitude() + "," + selectedStore.getLongitude();
                if (isGoogleMapsInstalled()) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(Intent.createChooser(intent, "Select an application"));
                }
            }
        }
    }

    private void updateGpsPointerImage() {
        if (isGPSActive) {
            btnGpsActive.setBackgroundResource(R.drawable.gps_pointer_blue);
        } else {
            btnGpsActive.setBackgroundResource(R.drawable.gps_pointer_grey);
        }
    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void checkLocationServicesEnabled() {
        LocationManager locationManager = LocationManager.getInstance(this);
        if (locationManager.isLocationServicesEnabled()) {
            loadingDialog();
            startTimer();
            LocationManager.getInstance(this).startLocationUpdates(this);
        } else {
            //Notify user that location service is not enabled
            showLocationServiceNotAvailableAlert();
        }
    }

    public void showLocationServiceNotAvailableAlert() {
        gpsAlertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
                this, android.R.style.Theme_DeviceDefault_Light_Dialog));
        gpsAlertBuilder.setTitle("Jamba");
        gpsAlertBuilder
                .setMessage("Your location will be used to find stores near you and location based store offers.");
        gpsAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Turn On GPS if Yes Clicked
                    gpsAlertBuilder = null;
                    turnGPSOn(dialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gpsAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Show Some toast Message
                gpsAlertBuilder = null;
                dialog.dismiss();
            }
        });
        gpsAlertBuilder.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void turnGPSOn(DialogInterface dialog) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        dialog.dismiss();

        if (currentapiVersion > 18) {

            Intent mIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle mBundle = new Bundle();
            startActivity(mIntent, mBundle);
        } else {

            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            this.sendBroadcast(intent);
            String provider = Settings.Secure.getString(
                    this.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                this.sendBroadcast(poke);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        //setUi();
//                 mGoogleMap.setOnCameraIdleListener(this);
//                 mGoogleMap.setOnCameraMoveStartedListener(this);
//                 mGoogleMap.setOnCameraMoveListener(this);
//                 mGoogleMap.setOnCameraMoveCanceledListener(this);
//
//                 // We will provide our own zoom controls.
//                 mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
//                 mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//                 // Show Sydney
//                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.87365, 151.20689), 10));
    }

    private void setUi() {
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        if (mGoogleMap == null) {
            return;
//            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.directionMap)).getMap();
//            ((MapFragment) getFragmentManager().findFragmentById(R.id.directionMap)).
        }

        builder = new LatLngBounds.Builder();
        if (mGoogleMap != null) {
            // Enabling MyLocation in Google Map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnCameraMoveStartedListener(this);
//            mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                @Override
//                public boolean onMyLocationButtonClick() {
//                    checkAndSet();
//                    return false;
//                }
//            });
            mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

            markerOptions1 = new MarkerOptions();
            markerOptions2 = new MarkerOptions();

//            Double lat = JambaApplication.getAppContext().fbsdkObj.getmCurrentLocation().getLatitude();
//            Double lng = JambaApplication.getAppContext().fbsdkObj.getmCurrentLocation().getLongitude();
//
//            fromPosition = new LatLng(lat, lng);
            toPosition = new LatLng(selectedStore.getLatitude(), selectedStore.getLongitude());
            //loadRoute();
        }

    }

    public void loadRoute() {
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //isGPSActive = true;
        updateGpsPointerImage();
    }

    @Override
    public void onLocationCallback(Location location) {
        dismissDialog();
        if (timer != null) {
            timer.cancel();
        }
        if (fromPosition == null) {
            fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
            if (fromPosition != null) {
                setUi();
                loadRoute();
            }
        }
        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        if (isGPSActive) {
            centerScreen();
        }
    }

    @Override
    public void onConnectionFailedCallback() {

        Utils.showAlert(this, "connection failed", "Error");
    }

    private void centerScreen() {

//        // Showing the current location in Google Map
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentPosition.latitude, currentPosition.longitude)));//currentLocation
//        // Zoom in the Google Map
//        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        if (currentPosition != null) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(currentPosition.latitude, currentPosition.longitude), 15);
            mGoogleMap.animateCamera(cu);
        }
    }


    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {
        switch (i) {
            case OnCameraMoveStartedListener.REASON_GESTURE:
                Log.d("5555", "REASON_GESTURE");
                isGPSActive = false;
                updateGpsPointerImage();
                break;
            case OnCameraMoveStartedListener.REASON_API_ANIMATION:
                Log.d("5555", "REASON_API_ANIMATION");
                break;
            case OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                Log.d("5555", "REASON_DEVELOPER_ANIMATION");
                break;
        }
    }

    public void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer(); // At this line a new Thread will be created
        timer.schedule(new RemindTask(), 30 * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {
                    dismissDialog();
                    if (timer != null) {
                        timer.cancel();
                    }
                    FBToastService.sharedInstance().show("Unable to find your location");
                }
            });

        }
    }

    private class GetRouteTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(StoreDirectionActivity.this);
            dialog.setMessage("Loading route...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            mGoogleMap.clear();
            ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
            if (directionPoint.size() <= 0) {
                Toast.makeText(getBaseContext(), "No Route Found", Toast.LENGTH_SHORT).show();
            }
            PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.BLUE);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            // Adding route on the map
            mGoogleMap.addPolyline(rectLine);
            if (fromPosition.longitude != 0.0
                    && fromPosition.latitude != 0.0) {
                markerOptions1.position(fromPosition);
                markerOptions1.draggable(true);
                markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_pin_marker));
                markerOptions1.title("Your Starting Location");
                mGoogleMap.addMarker(markerOptions1);
                builder.include(markerOptions1.getPosition());
            } else {
                if (toPosition != null) {
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(toPosition.latitude, toPosition.longitude), 15);
                    mGoogleMap.animateCamera(cu);
                }
            }

            markerOptions2.position(toPosition);
            markerOptions2.draggable(true);
            if (selectedStore != null) {
                if (DataManager.getInstance().isDebug) {
                    if (Utils.setDemoStoreName(selectedStore).getName() != null) {
                        markerOptions2.title(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""));
                    }
                } else {
                    if (selectedStore.getName() != null) {
                        markerOptions2.title(selectedStore.getName().replace("Jamba Juice ", ""));
                    }
                }
            }
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin_marker));
            mGoogleMap.addMarker(markerOptions2);
            builder.include(markerOptions2.getPosition());

            bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            mGoogleMap.animateCamera(cu);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }
}

