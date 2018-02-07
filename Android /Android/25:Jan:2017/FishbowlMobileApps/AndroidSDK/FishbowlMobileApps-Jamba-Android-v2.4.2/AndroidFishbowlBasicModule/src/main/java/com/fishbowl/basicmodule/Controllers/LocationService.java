package com.fishbowl.basicmodule.Controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import com.estimote.sdk.BeaconManager;
import com.fishbowl.basicmodule.Models.FBBeaconRegionItem;
import com.fishbowl.basicmodule.Services.FBBeaconService;
import com.fishbowl.basicmodule.Services.FBGeoFenceService;
import com.fishbowl.basicmodule.Services.FBLocationService;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;
/**
 * Created by digvijay(dj)
 */
public class LocationService extends Service implements ConnectionCallbacks,OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
	
	
	
	private boolean isFirstTime = true;
	
	protected static final String TAG = "CLP-SDK-Location-Service";
	public BeaconManager beaconManager;
	ArrayList<FBBeaconRegionItem> beaconMonitorRegions = new ArrayList<FBBeaconRegionItem>();
	ArrayList<FBBeaconRegionItem> beaconRangingRegions = new ArrayList<FBBeaconRegionItem>();
	@SuppressLint("UseSparseArrays")

	private FBSdk _FBSdk;
	private Context ctx;

	private static String REGIONS = "REGIONS";
	private Date lastBeaconCall = null;//add in FBBeaconService
	// Keys for storing activity state in the Bundle.
	protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
	protected final static String LOCATION_KEY = "location-key";
	protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

	public GoogleApiClient mGoogleApiClient;
	public LocationRequest mLocationRequest;

	public Boolean mRequestingLocationUpdates;
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			_FBSdk = FBSdk.sharedInstance(this);
			mLocationRequest=new LocationRequest();
			
			FBGeoFenceService.sharedInstance().startGeoFenceTimer(this);
			FBGeoFenceService.sharedInstance().registerGeofenceReciever(this);
			FBLocationService.sharedInstance().setLocationService(this);
		//	FBGeoFenceService.sharedInstance().setLocationService(this);
			
			if (clpCallbackReceiver != null) {
				IntentFilter intentFilter = new IntentFilter(FBSdk.CLP_CALLBACK);
				registerReceiver(clpCallbackReceiver, intentFilter);
			}

			if (FBStoreService.sharedInstance().allStoreFromServer == null) {
				_FBSdk.getAllStores();// download storelist if location
										// sevice
										// started before
			} else {
				FBGeoFenceService.sharedInstance().initGeofence();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	} 

	@Override
	public void onDestroy() {
		try {
			if (mGoogleApiClient != null) {
				mGoogleApiClient.disconnect();
			} 
			
			FBGeoFenceService.sharedInstance().unRegisterGeofenceReciever(this);
			
			if (clpCallbackReceiver != null) {
				unregisterReceiver(clpCallbackReceiver);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	
	 
	
	/**
	 * Requests location updates from the FusedLocationApi.
	 */
	protected void startLocationUpdates() {
		if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
			FBSdk.sharedInstance(ctx).localLog("fbSdk ls: ", "startLocationUpdates not connected: Reconnecting..");
			buildGoogleApiClient();
		}
		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

		if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}

	}

	/**
	 * Removes location updates from the FusedLocationApi.
	 */
	
	protected void stopLocationUpdates() {
		
		if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
			FBSdk.sharedInstance(ctx).localLog("fbSdk ls: ","stopLocationUpdates not connected: Reconnecting..");
			buildGoogleApiClient();
		}
		
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	} 


	public synchronized void buildGoogleApiClient() {
		FBSdk.sharedInstance(ctx).localLog(TAG, "Building GoogleApiClient");
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
			mGoogleApiClient.connect();
		}
	}
	

	/**
	 * Handles the Start Updates button and requests start of location updates.
	 * Does nothing if updates have already been requested.
	 */
	public void startUpdatesHandler(LocationRequest _mLocationRequest) {
		 mLocationRequest=_mLocationRequest;

		if (!mRequestingLocationUpdates) {
			startLocationUpdates();
		} else {
			stopLocationUpdates();
			startLocationUpdates();
		}
		mRequestingLocationUpdates = true;
	}

	/**
	 * Handles the Stop Updates button, and requests removal of location
	 * updates. Does nothing if updates were not previously requested.
	 */
	public void stopUpdatesHandler() {
		if (mRequestingLocationUpdates) {
			mRequestingLocationUpdates = false;
			stopLocationUpdates();
		}
	}
	



	// Beacon start
	public void beaconMonitorStarts(ArrayList<String> stores) {
		_FBSdk.getBeaconsForStoreNo(stores);
	}



	// private PendingIntent getGeofencePendingIntent() {
	//
	// // Reuse the PendingIntent if we already have it.
	// if (mGeofencePendingIntent != null) {
	// return mGeofencePendingIntent;
	// }
	// Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
	// return PendingIntent.getService(this, 0, intent,
	// PendingIntent.FLAG_UPDATE_CURRENT);
	// }
	
	
	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////	
	/////////////////////////////////////////////////////////////////////////////////////// 
	
	
	private BroadcastReceiver clpCallbackReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {

				if (intent.hasExtra(FBSdk.CLP_CALLBACK)&& intent.getStringExtra(FBSdk.CLP_CALLBACK).equals(FBSdk.CLP_SETTINGS_UPDATE_CALLBACK)) {
					// if geofence refresh
					
					FBSdk.sharedInstance(context).localLog("All store update - Geofence refresh", "Called");
					if (mGoogleApiClient == null|| !mGoogleApiClient.isConnected()) {
						
						FBGeoFenceService.sharedInstance().initGeofence();
						
					}
					FBGeoFenceService.sharedInstance().startGeoFencing(false);

				} else if (intent.hasExtra(FBSdk.CLP_CALLBACK)&& intent.getStringExtra(FBSdk.CLP_CALLBACK).equals(FBSdk.CLP_STARTBEACON_CALLBACK)) {
				 
					if (FBUtility.isBLESupportedDevice(_FBSdk.context)) {
						FBBeaconService.sharedInstance().startBeaconForStore();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
 
 


	
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	/**
	 * Runs when a GoogleApiClient object successfully connects.
	 */
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	
	@Override
	public void onConnected(Bundle connectionHint) {

		FBSdk.sharedInstance(ctx).localLog(TAG, "Connected to GoogleApiClient");
		//_FBSdk.displayLocalPushNotification("GeoFence connected",
		//		this.getClass(), R.drawable.ic_launcher, this);
		if (FBGeoFenceService.sharedInstance().isInsideRegion || isFirstTime) {
			mLocationRequest= FBLocationService.sharedInstance().createInSideLocationRequest(mLocationRequest);
			isFirstTime = false;
		} else {
			mLocationRequest= FBLocationService.sharedInstance().createOutSideLocationRequest(mLocationRequest);
			isFirstTime = false;
		}
		FBGeoFenceService.sharedInstance().startGeoFencing(false);
	}

	
	@Override
	public void onConnectionSuspended(int cause) {
		FBSdk.sharedInstance(ctx).localLog(TAG, "Connection suspended");
		FBGeoFenceService.sharedInstance().initGeofence();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		FBSdk.sharedInstance(ctx).localLog(
				TAG,
				"Connection failed: ConnectionResult.getErrorCode() = "
						+ result.getErrorCode());
		FBGeoFenceService.sharedInstance().initGeofence();
	}

	/**
	 * Callback that fires when the location changes.
	 */
	@Override
	public void onLocationChanged(Location location) {
		FBLocationService.sharedInstance().onLocationChanged(location);
	}

	@Override
	public void onResult(com.google.android.gms.common.api.Status status) {
	}



  
}