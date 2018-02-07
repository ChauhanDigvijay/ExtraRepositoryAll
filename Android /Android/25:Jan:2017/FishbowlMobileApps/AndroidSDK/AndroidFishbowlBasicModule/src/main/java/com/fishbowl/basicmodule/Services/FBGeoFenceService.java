package com.fishbowl.basicmodule.Services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.LocationService;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by digvijay(dj)
 */
public class FBGeoFenceService {
	public static FBGeoFenceService instance ;
	private static FBSdk fbSdk;
	public Timer timer = null;
	ArrayList<String> currentActiveRegions;
	private static String ENTER_REGION = "ENTER";
	private static String EXIT_REGION = "EXIT";
	public boolean isInsideStore = false;
	LocationService locationService;//Reference from LocationService
	private PendingIntent mGeofencePendingIntent;
	public Date insideRegionTime = null;
	private static String REGIONS = "REGIONS";
	public boolean isInsideRegion = false;
	String TAG="FBGeoFenceService";


	public static FBGeoFenceService sharedInstance(){
		if(instance==null){
			instance=new FBGeoFenceService();
		}
		return instance;
	}
	public void init(FBSdk _fbsdk){
		fbSdk =_fbsdk;
	}

	public void setLocationService(LocationService locationService2){
		this.locationService=locationService2;
	}

	TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			try {
				if (FBLocationService.sharedInstance().latestGLocation == null|| (fbSdk.getmCurrentLocation().distanceTo(FBLocationService.sharedInstance().latestGLocation) >= fbSdk.getGEOFENCE_CHECK_DISTANCE_MOVED())) {

					FBSdk.sharedInstance(fbSdk.context).localLog("Scheduled - Geofence refresh", "Called");

					if (locationService.mGoogleApiClient == null|| !locationService.mGoogleApiClient.isConnected()) {
						initGeofence();
					} else {
						startGeoFencing(false);
					}
				}
				FBLocationService.sharedInstance().latestGLocation = fbSdk.getmCurrentLocation();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void  startGeoFenceTimer(Context context){
		if (timer == null) {
			timer = new Timer();
			timer.schedule(updateTask, TimeUnit.SECONDS.toMillis(fbSdk.getGEOFENCE_CHECK_FREQUENCY()), TimeUnit.SECONDS.toMillis(fbSdk.getGEOFENCE_CHECK_FREQUENCY()));
		}
		currentActiveRegions = new ArrayList<String>();
	}

	public void initGeofence() {

		if(locationService!=null) {
			locationService.mRequestingLocationUpdates = false;
			FBLocationService.sharedInstance().mLastUpdateTime = "";
			locationService.buildGoogleApiClient();
			locationService.mGoogleApiClient.connect();
		}
	}


	// START GEOFENCING
	public void startGeoFencing(boolean isDynamicRadius) {

		FBSdk.sharedInstance(fbSdk.context).localLog("GeofenceCall", "Called");

		if (FBStoreService.sharedInstance().allStoreFromServer != null)
		{

			//	FBStoreService.sharedInstance().getNearestBeaconStores(fbSdk.getmCurrentLocation());// nearest store

			fbSdk.lastGeofenceStartCall = new Date();
			insideRegionTime = new Date();
			isInsideStore = false;
			// Empty list for storing geofences.
			fbSdk.mGeofenceList = new ArrayList<Geofence>();
			mGeofencePendingIntent = null;
			populateGeofenceList(isDynamicRadius);
			addGeofences();
		} else {
			// Toast.makeText(this, "GeoFence not setup empty stores",
			// Toast.LENGTH_SHORT).show();
			//_clpSdk.displayLocalPushNotification(
			//		"GeoFence not setup empty stores", this.getClass(),
			//		R.drawable.ic_launcher, this);
		}
	}
	public void addGeofences() {
		if(locationService != null) {
			if (locationService.mGoogleApiClient != null && !locationService.mGoogleApiClient.isConnected()) {
				FBSdk.sharedInstance(fbSdk.context).localLog("Addgeofence: ", "GoogleApiClient not connected: Reconnecting..");
				locationService.mGoogleApiClient.connect();
			}
			try {
				if (locationService.mGoogleApiClient != null && locationService.mGoogleApiClient.isConnected()) {

					LocationServices.GeofencingApi.removeGeofences(
							locationService.mGoogleApiClient,
							// This is the same pending intent that was used in
							// addGeofences().
							createRequestPendingIntent()).setResultCallback(locationService);

					LocationServices.GeofencingApi.addGeofences(locationService.mGoogleApiClient,
							getGeofencingRequest(), createRequestPendingIntent())
							.setResultCallback(locationService); // Result
					// processed
					// in
					// onResult().
				}
			} catch (SecurityException securityException) {
				FBSdk.sharedInstance(fbSdk.context).localLog(TAG, "Invalid location permission. " + "You need to use ACCESS_FINE_LOCATION with geofences");
			}
		}
		//_clpSdk.displayLocalPushNotification("GeoFence enabled",
		//this.getClass(), R.drawable.ic_launcher, this);
	}


	public void populateGeofenceList(boolean isDynamicRadius) {
		if (FBStoreService.sharedInstance().allStoreFromServer != null) {

			for (FBStoresItem geoStores : FBStoreService.sharedInstance().allStoreFromServer) {
				Double lat = Double.valueOf(geoStores.latitude);
				Double lon = Double.valueOf(geoStores.longitude);
				float radi = fbSdk.getGEOFENCE_RADIUS();
				if (isDynamicRadius) {
					if (geoStores.getDistanceFromCLocation() >= fbSdk.getGEOFENCE_RADIUS()) {
						radi = fbSdk.getGEOFENCE_RADIUS();
					} else if (geoStores.getDistanceFromCLocation() <= fbSdk.getGEOFENCE_MIN_RADIUS()) {
						radi = fbSdk.getGEOFENCE_MIN_RADIUS();
					}
					else {
						float dist = (float) (geoStores.getDistanceFromCLocation() - fbSdk
								.getGEOFENCE_MIN_RADIUS());
						if (dist <= fbSdk.getGEOFENCE_MIN_RADIUS())
							radi = fbSdk.getGEOFENCE_MIN_RADIUS();
						else
							radi = dist;
					}
				}
				FBSdk.sharedInstance(fbSdk.context).localLog("fbSdk : ","Geo rad : " + geoStores.storeID + " - " + radi);

				FBLocationService.sharedInstance().gfRadiusMap.put(geoStores.storeID, radi);

				fbSdk.mGeofenceList.add(new Geofence.Builder()
						.setRequestId(Integer.toString(geoStores.storeID))
						.setCircularRegion(lat, lon, radi)
						.setExpirationDuration(
								fbSdk.getGEOFENCE_EXPIRY_TIME())
						.setLoiteringDelay(300000)
						.setTransitionTypes(
								Geofence.GEOFENCE_TRANSITION_ENTER
										| Geofence.GEOFENCE_TRANSITION_DWELL
										| Geofence.GEOFENCE_TRANSITION_EXIT)

						// Create the geofence.
						.build());
			}
		}

	}

	private GeofencingRequest getGeofencingRequest() {
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
		// Add the geofences to be monitored by geofencing service.
		builder.addGeofences(fbSdk.mGeofenceList);
		FBSdk.sharedInstance(fbSdk.context).localLog("fbSdk ls :",
				"no of geo :" + fbSdk.mGeofenceList.size());
		// Return a GeofencingRequest.
		return builder.build();
	}



	public void registerGeofenceReciever(Context context){
		if (geoFencingCallbackReceiver != null) {
			IntentFilter intentFilter = new IntentFilter(FBSdk.CLP_GEOFENCE_CALLBACK);
			context.registerReceiver(geoFencingCallbackReceiver, intentFilter);
		}
	}

	public void unRegisterGeofenceReciever(Context context){
		if (geoFencingCallbackReceiver != null) {
			context.unregisterReceiver(geoFencingCallbackReceiver);
		}
	}

	//broad cast reciever  to listen geogence
	public PendingIntent createRequestPendingIntent() {

		// If the PendingIntent already exists
		if (null != mGeofencePendingIntent) {
			return mGeofencePendingIntent;
		} else {
			Intent intent = new Intent("com.clp.sdk.ACTION_RECEIVE_GEOFENCE");
			return PendingIntent.getBroadcast(locationService, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		}
	}

	private BroadcastReceiver geoFencingCallbackReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				FBSdk.sharedInstance(fbSdk.context).localLog("LS", intent.getExtras().toString());
				if (intent.hasExtra("EVENT") && intent.hasExtra("REGIONS")) {

					ArrayList<String> regions = intent.getStringArrayListExtra(REGIONS);
					isInsideStore = false;

					// do If TRIGGER_GEOFENCE is on
					if(fbSdk.getFBSdkData().mobileSettings.TRIGGER_GEOFENCE>0){

						if (intent.getStringExtra("EVENT").equals(ENTER_REGION)) {

							for (String region : regions) {
								currentActiveRegions.add(region);
							}
							isInsideRegion = true;
							insideRegionTime = new Date();

							locationService.mLocationRequest= FBLocationService.sharedInstance().createInSideLocationRequest(locationService.mLocationRequest);

							if (FBUtility.isBLESupportedDevice(fbSdk.context)) {
								locationService.beaconMonitorStarts(regions);
							}

						} else if (intent.getStringExtra("EVENT").equals(
								EXIT_REGION)) {
							for (String region : regions) {
								if (currentActiveRegions.contains(region)) {
									currentActiveRegions.remove(region);
								}
							}
							// stop beacon for particular stores
							FBBeaconService.sharedInstance().stopBeaconMonitoring(regions);
							FBBeaconService.sharedInstance().stopBeaconRanging(regions);
							insideRegionTime = new Date();
							if (currentActiveRegions.size() == 0) {
								isInsideRegion = false;
								locationService.mLocationRequest= FBLocationService.sharedInstance().createOutSideLocationRequest(locationService.mLocationRequest);
								// stop all beacon
								FBBeaconService.sharedInstance().stopBeaconMonitoring();
								FBBeaconService.sharedInstance().stopBeaconRanging();
								// start all regions
								FBSdk.sharedInstance(fbSdk.context).localLog("Location service Geofence refresh","Called");
								//_clpSdk.displayLocalPushNotification(
								//		"GeoFence refreshed on exit",
								//		context.getClass(), R.drawable.ic_launcher,
								//		context);
								startGeoFencing(false);// start or restart
								// geofencing
							}
						}
					}else{

						FBSdk.sharedInstance(fbSdk.context).localLog(TAG, "createOutSideLocationRequest");

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};


	HashMap<String,String> getHeaderwithsecurity(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application", FBConstant.client_Application);
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
		header.put("client_id", FBConstant.client_id);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("tenantName", FBConstant.client_tenantName);
		return header;
	}


}
