package com.clp.sdk;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.clp.model.CLPBeaconRegion;
import com.clp.model.CLPBeacons;
import com.clp.model.CLPMobileEvents;
import com.clp.model.CLPStores;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.RangingListener;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils.Proximity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service
		implements
		ConnectionCallbacks,
		OnConnectionFailedListener,
		LocationListener,
		ResultCallback<com.google.android.gms.common.api.Status>,
		com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks {
	private boolean isInsideRegion = false;
	private boolean isInsideStore = false;
	private boolean isFirstTime = true;
	private Date insideRegionTime = null;
	private PendingIntent mGeofencePendingIntent;
	protected static final String TAG = "CLP-SDK-Location-Service";
	public BeaconManager beaconManager;
	ArrayList<CLPBeaconRegion> beaconMonitorRegions = new ArrayList<CLPBeaconRegion>();
	ArrayList<CLPBeaconRegion> beaconRangingRegions = new ArrayList<CLPBeaconRegion>();
	@SuppressLint("UseSparseArrays")
	Map<Integer, Float> gfRadiusMap = new HashMap<Integer, Float>();
	private CLPSdk _clpSdk;
	private Context ctx;
	private static String ENTER_REGION = "ENTER";
	private static String EXIT_REGION = "EXIT";
	private static String REGIONS = "REGIONS";
	private Date lastBeaconCall = null;
	// Keys for storing activity state in the Bundle.
	protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
	protected final static String LOCATION_KEY = "location-key";
	protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

	protected GoogleApiClient mGoogleApiClient;
	protected LocationRequest mLocationRequest;

	protected Boolean mRequestingLocationUpdates;
	protected String mLastUpdateTime;
	private Location latestGLocation;

	ArrayList<String> currentActiveRegions;
	protected Timer timer = new Timer();
	TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			try {
				if (latestGLocation == null
						|| (_clpSdk.getmCurrentLocation().distanceTo(
								latestGLocation) >= _clpSdk
								.getGEOFENCE_CHECK_DISTANCE_MOVED())) {
					CLPSdk.sharedInstance(ctx).localLog(
							"Scheduled - Geofence refresh", "Called");
					if (mGoogleApiClient == null
							|| !mGoogleApiClient.isConnected()) {
						initGeofence();
					} else {
						startGeoFencing(false);
					}
				}
				latestGLocation = _clpSdk.getmCurrentLocation();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private BroadcastReceiver clpCallbackReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {

				if (intent.hasExtra(CLPSdk.CLP_CALLBACK)
						&& intent.getStringExtra(CLPSdk.CLP_CALLBACK).equals(
								CLPSdk.CLP_SETTINGS_UPDATE_CALLBACK)) {
					// if geofence refresh
					CLPSdk.sharedInstance(context).localLog(
							"All store update - Geofence refresh", "Called");
					if (mGoogleApiClient == null
							|| !mGoogleApiClient.isConnected()) {
						initGeofence();
					}
					startGeoFencing(false);

				} else if (intent.hasExtra(CLPSdk.CLP_CALLBACK)
						&& intent.getStringExtra(CLPSdk.CLP_CALLBACK).equals(
								CLPSdk.CLP_STARTBEACON_CALLBACK)) {
					// else bluetooth
					if (_clpSdk.isBLESupportedDevice()) {
						startBeaconForStore();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private BroadcastReceiver geoFencingCallbackReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				CLPSdk.sharedInstance(ctx).localLog("LS",
						intent.getExtras().toString());
				if (intent.hasExtra("EVENT") && intent.hasExtra("REGIONS")) {
					ArrayList<String> regions = intent
							.getStringArrayListExtra(REGIONS);
					isInsideStore = false;
					if (intent.getStringExtra("EVENT").equals(ENTER_REGION)) {
						for (String region : regions) {
							currentActiveRegions.add(region);
						}
						isInsideRegion = true;
						insideRegionTime = new Date();
						createInSideLocationRequest();
						if (_clpSdk.isBLESupportedDevice()) {
							beaconMonitorStarts(regions);
						}
					} else if (intent.getStringExtra("EVENT").equals(
							EXIT_REGION)) {
						for (String region : regions) {
							if (currentActiveRegions.contains(region)) {
								currentActiveRegions.remove(region);
							}
						}
						// stop beacon for particular stores
						stopBeaconMonitoring(regions);
						stopBeaconRanging(regions);
						insideRegionTime = new Date();
						if (currentActiveRegions.size() == 0) {
							isInsideRegion = false;
							createOutSideLocationRequest();
							// stop all beacon
							stopBeaconMonitoring();
							stopBeaconRanging();
							// start all regions
							CLPSdk.sharedInstance(ctx).localLog(
									"Location service Geofence refresh",
									"Called");
							_clpSdk.displayLocalPushNotification(
									"GeoFence refreshed on exit",
									context.getClass(), R.drawable.ic_launcher,
									context);
							startGeoFencing(false);// start or restart
													// geofencing
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			_clpSdk = CLPSdk.sharedInstance(this);
			currentActiveRegions = new ArrayList<String>();

			timer.schedule(updateTask, TimeUnit.SECONDS.toMillis(_clpSdk
					.getGEOFENCE_CHECK_FREQUENCY()), TimeUnit.SECONDS
					.toMillis(_clpSdk.getGEOFENCE_CHECK_FREQUENCY()));

			if (geoFencingCallbackReceiver != null) {
				IntentFilter intentFilter = new IntentFilter(
						CLPSdk.CLP_GEOFENCE_CALLBACK);
				registerReceiver(geoFencingCallbackReceiver, intentFilter);
			}
			if (clpCallbackReceiver != null) {
				IntentFilter intentFilter = new IntentFilter(
						CLPSdk.CLP_CALLBACK);
				registerReceiver(clpCallbackReceiver, intentFilter);
			}

			if (_clpSdk.getAllStoreList() == null
					|| _clpSdk.getAllStoreList().size() == 0) {
				_clpSdk.getAllStores();// download storelist if location
										// sevice
										// started before
			} else {
				initGeofence();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		try {
			if (mGoogleApiClient != null) {
				mGoogleApiClient.disconnect();
			}
			if (geoFencingCallbackReceiver != null) {
				unregisterReceiver(geoFencingCallbackReceiver);
			}
			if (clpCallbackReceiver != null) {
				unregisterReceiver(clpCallbackReceiver);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void initGeofence() {
		mRequestingLocationUpdates = false;
		mLastUpdateTime = "";
		buildGoogleApiClient();
		mGoogleApiClient.connect();
	}

	/**
	 * Runs when a GoogleApiClient object successfully connects.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {

		CLPSdk.sharedInstance(ctx)
				.localLog(TAG, "Connected to GoogleApiClient");
		_clpSdk.displayLocalPushNotification("GeoFence connected",
				this.getClass(), R.drawable.ic_launcher, this);
		if (isInsideRegion || isFirstTime) {
			createInSideLocationRequest();
			isFirstTime = false;
		} else {
			createOutSideLocationRequest();
			isFirstTime = false;
		}
		startGeoFencing(false);
	}

	protected void createOutSideLocationRequest() {
		CLPSdk.sharedInstance(ctx)
				.localLog(TAG, "createOutSideLocationRequest");
		if (_clpSdk.currentLocationRequest == null
				|| !_clpSdk.currentLocationRequest
						.contains(CLPSdk.OUTSIDE_REGION)) {
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(_clpSdk
					.getOUT_SIDE_UPDATE_INTERVAL()));
			mLocationRequest.setFastestInterval(TimeUnit.SECONDS
					.toMillis(_clpSdk.getOUT_SIDE_FASTEST_UPDATE_INTERVAL()));
			mLocationRequest
					.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			// mLocationRequest.setSmallestDisplacement(5f);
			startUpdatesHandler();
			_clpSdk.currentLocationRequest = CLPSdk.OUTSIDE_REGION;
		} else {
			CLPSdk.sharedInstance(ctx).localLog(TAG,
					"already in OutSideLocationRequest");
		}
	}

	protected void createInSideLocationRequest() {
		CLPSdk.sharedInstance(ctx).localLog(TAG, "createInSideLocationRequest");
		if (_clpSdk.currentLocationRequest == null
				|| !_clpSdk.currentLocationRequest
						.contains(CLPSdk.INSIDE_REGION)) {
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(_clpSdk
					.getIN_SIDE_UPDATE_INTERVAL()));
			mLocationRequest.setFastestInterval(TimeUnit.SECONDS
					.toMillis(_clpSdk.getIN_SIDE_FASTEST_UPDATE_INTERVAL()));
			mLocationRequest
					.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			// mLocationRequest.setSmallestDisplacement(5f);
			startUpdatesHandler();
			_clpSdk.currentLocationRequest = CLPSdk.INSIDE_REGION;
		} else {
			CLPSdk.sharedInstance(ctx).localLog(TAG,
					"already in InSideLocationRequest");
		}

	}

	protected void createInStoreLocationRequest() {
		CLPSdk.sharedInstance(ctx)
				.localLog(TAG, "createInStoreLocationRequest");
		if (_clpSdk.currentLocationRequest == null
				|| !_clpSdk.currentLocationRequest
						.contains(CLPSdk.INSIDE_STORE)) {
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(_clpSdk
					.getIN_STORE_UPDATE_INTERVAL()));
			mLocationRequest.setFastestInterval(TimeUnit.SECONDS
					.toMillis(_clpSdk.getIN_STORE_UPDATE_INTERVAL()));
			mLocationRequest
					.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			mLocationRequest.setSmallestDisplacement(15f);
			startUpdatesHandler();
			_clpSdk.currentLocationRequest = CLPSdk.INSIDE_STORE;
		} else {
			CLPSdk.sharedInstance(ctx).localLog(TAG,
					"already in InStoreLocationRequest");
		}
	}

	/**
	 * Handles the Start Updates button and requests start of location updates.
	 * Does nothing if updates have already been requested.
	 */
	public void startUpdatesHandler() {
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

	/**
	 * Requests location updates from the FusedLocationApi.
	 */
	protected void startLocationUpdates() {
		if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
			CLPSdk.sharedInstance(ctx).localLog("clpsdk ls: ",
					"startLocationUpdates not connected: Reconnecting..");
			buildGoogleApiClient();
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	/**
	 * Removes location updates from the FusedLocationApi.
	 */
	protected void stopLocationUpdates() {
		if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
			CLPSdk.sharedInstance(ctx).localLog("clpsdk ls: ",
					"stopLocationUpdates not connected: Reconnecting..");
			buildGoogleApiClient();
		}
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
	}

	protected synchronized void buildGoogleApiClient() {
		CLPSdk.sharedInstance(ctx).localLog(TAG, "Building GoogleApiClient");
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API).build();
			mGoogleApiClient.connect();
		}
	}

	protected void checkInsideNearestStoreRegion(Location userLocation) {
		isInsideRegion = false;
		for (CLPStores geoStores : _clpSdk.getAllStoreList()) {
			Location storeLocation = new Location("storeLocation");
			storeLocation.setLatitude(Double.valueOf(geoStores.getLatitude()));
			storeLocation
					.setLongitude(Double.valueOf(geoStores.getLongitude()));
			if (gfRadiusMap.containsKey(geoStores.storeID)) {
				float checkRadii = gfRadiusMap.get(geoStores.storeID);
				if ((storeLocation.distanceTo(userLocation)) <= checkRadii) {
					isInsideRegion = true;
					break;
				}
			}
		}
	}

	/**
	 * Callback that fires when the location changes.
	 */
	@Override
	public void onLocationChanged(Location location) {
		try {
			_clpSdk.mCurrentLocation = location;
			mLastUpdateTime = DateFormat.getTimeInstance().format(
					new java.util.Date());
			_clpSdk.localLog("onLocationChanged", "Location update received");
			// Toast.makeText(this, "GPS received speed:" + location.getSpeed(),
			// Toast.LENGTH_SHORT).show();
			if (_clpSdk.getAllStoreList() != null
					&& _clpSdk.getAllStoreList().size() != 0) {
				checkInsideNearestStoreRegion(location);// check in region
														// manually

				// if (isInsideRegion) {
				// createInSideLocationRequest();
				// } else {
				// createOutSideLocationRequest();
				// }
			}
			if (isInsideRegion) {
				float interval = Math
						.abs((int) (TimeUnit.SECONDS.toMillis(_clpSdk
								.getLOCATION_UPDATE_PING_FREQUENCY()))); // check
				Boolean call = false;
				Date currentCall = new Date();
				if (_clpSdk.lastLocationUpdateCall != null) {
					int locationInterval = (int) (currentCall.getTime() - _clpSdk.lastLocationUpdateCall
							.getTime()) / 1000;
					if (locationInterval > interval) {
						call = true;
					}
				} else {
					call = true;
				}
				if (call) {
					// location update
					CLPMobileEvents clpMobileEvent = new CLPMobileEvents();
					clpMobileEvent.lat = location.getLatitude();
					clpMobileEvent.lon = location.getLongitude();
					_clpSdk.locationUpdateDevice(this, clpMobileEvent);
				}
				if (insideRegionTime != null) {
					int regionEnteredtime = (int) (currentCall.getTime() - insideRegionTime
							.getTime()) / 1000;
					CLPSdk.sharedInstance(ctx)
							.localLog(
									"clpsdk : ",
									"regionEnteredtime : " + regionEnteredtime
											+ " sec");
					CLPSdk.sharedInstance(ctx).localLog("onLocationChanged",
							"slabtime check");
					if (regionEnteredtime >= (_clpSdk.getIN_REGION_SLAB_TIME())
							&& !isInsideStore) {
						// stopBeaconRanging();
						String inStore = isInStore(location);
						if (inStore != null) {
							isInsideStore = true;
							createInStoreLocationRequest();
						} else {
							startGeoFencing(true);
						}
					}
					if (regionEnteredtime >= (_clpSdk.getBEACON_SLAB_TIME()) && isInsideStore) {
						String inStore = isInStore(location);
						if (inStore != null) {
							List<String> stores = new ArrayList<String>();
							stores.add(inStore);
							stopBeaconMonitoring(stores);
							stopBeaconRanging(stores);// newly added
							CLPSdk.sharedInstance(ctx).localLog(
									"onLocationChanged",
									"Beacon stopped after slabtime");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String isInStore(Location location) {
		CLPStores thisStore;
		for (String cRegion : currentActiveRegions) {
			try {
				if (_clpSdk.storesMap.containsKey(Integer.valueOf(cRegion))) {
					thisStore = _clpSdk.storesMap.get(Integer.valueOf(cRegion));
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			float dist = location.distanceTo(thisStore.getLocation());
			CLPSdk.sharedInstance(ctx).localLog(TAG, "dist to store :" + dist);
			if (dist < _clpSdk.getGEOFENCE_MIN_RADIUS()) {
				return cRegion;
			}
		}
		return null;
	}

	@Override
	public void onConnectionSuspended(int cause) {
		CLPSdk.sharedInstance(ctx).localLog(TAG, "Connection suspended");
		initGeofence();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		CLPSdk.sharedInstance(ctx).localLog(
				TAG,
				"Connection failed: ConnectionResult.getErrorCode() = "
						+ result.getErrorCode());
		initGeofence();
	}

	// START GEOFENCING
	public void startGeoFencing(boolean isDynamicRadius) {
		CLPSdk.sharedInstance(ctx).localLog("GeofenceCall", "Called");
		if (_clpSdk.getAllStoreList() != null
				&& _clpSdk.getAllStoreList().size() > 0) {
			_clpSdk.getNearestBeaconStores(_clpSdk.getmCurrentLocation());// nearest
																			// store
			_clpSdk.lastGeofenceStartCall = new Date();
			insideRegionTime = new Date();
			isInsideStore = false;
			// Empty list for storing geofences.
			_clpSdk.mGeofenceList = new ArrayList<Geofence>();
			mGeofencePendingIntent = null;
			populateGeofenceList(isDynamicRadius);
			addGeofences();
		} else {
			// Toast.makeText(this, "GeoFence not setup empty stores",
			// Toast.LENGTH_SHORT).show();
			_clpSdk.displayLocalPushNotification(
					"GeoFence not setup empty stores", this.getClass(),
					R.drawable.ic_launcher, this);
		}

	}

	public void populateGeofenceList(boolean isDynamicRadius) {
		if (_clpSdk.getAllStoreList() != null
				&& _clpSdk.getAllStoreList().size() > 0) {

			for (CLPStores geoStores : _clpSdk.getAllStoreList()) {
				Double lat = Double.valueOf(geoStores.latitude);
				Double lon = Double.valueOf(geoStores.longitude);
				float radi = _clpSdk.getGEOFENCE_RADIUS();
				if (isDynamicRadius) {
					if (geoStores.getDistanceFromCLocation() >= _clpSdk
							.getGEOFENCE_RADIUS()) {
						radi = _clpSdk.getGEOFENCE_RADIUS();
					} else if (geoStores.getDistanceFromCLocation() <= _clpSdk
							.getGEOFENCE_MIN_RADIUS()) {
						radi = _clpSdk.getGEOFENCE_MIN_RADIUS();
					} else {
						float dist = (float) (geoStores
								.getDistanceFromCLocation() - _clpSdk
								.getGEOFENCE_MIN_RADIUS());
						if (dist <= _clpSdk.getGEOFENCE_MIN_RADIUS())
							radi = _clpSdk.getGEOFENCE_MIN_RADIUS();
						else
							radi = dist;
					}
				}
				CLPSdk.sharedInstance(ctx).localLog("clpsdk : ",
						"Geo rad : " + geoStores.storeID + " - " + radi);
				gfRadiusMap.put(geoStores.storeID, radi);
				_clpSdk.mGeofenceList.add(new Geofence.Builder()
						.setRequestId(Integer.toString(geoStores.storeID))
						.setCircularRegion(lat, lon, radi)
						.setExpirationDuration(
								_clpSdk.getGEOFENCE_EXPIRY_TIME())
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

	public void addGeofences() {
		if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
			CLPSdk.sharedInstance(ctx).localLog("Addgeofence: ",
					"GoogleApiClient not connected: Reconnecting..");
			mGoogleApiClient.connect();
		}

		try {
			if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
				LocationServices.GeofencingApi.removeGeofences(
						mGoogleApiClient,
						// This is the same pending intent that was used in
						// addGeofences().
						createRequestPendingIntent()).setResultCallback(this);

				LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
						getGeofencingRequest(), createRequestPendingIntent())
						.setResultCallback(this); // Result
													// processed
													// in
													// onResult().
			}
		} catch (SecurityException securityException) {
			logSecurityException(securityException);
		}
		_clpSdk.displayLocalPushNotification("GeoFence enabled",
				this.getClass(), R.drawable.ic_launcher, this);
	}

	private void logSecurityException(SecurityException securityException) {
		CLPSdk.sharedInstance(ctx)
				.localLog(
						TAG,
						"Invalid location permission. "
								+ "You need to use ACCESS_FINE_LOCATION with geofences");
	}

	private GeofencingRequest getGeofencingRequest() {
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
		// Add the geofences to be monitored by geofencing service.
		builder.addGeofences(_clpSdk.mGeofenceList);
		CLPSdk.sharedInstance(ctx).localLog("clpsdk ls :",
				"no of geo :" + _clpSdk.mGeofenceList.size());
		// Return a GeofencingRequest.
		return builder.build();
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

	private PendingIntent createRequestPendingIntent() {

		// If the PendingIntent already exists
		if (null != mGeofencePendingIntent) {
			return mGeofencePendingIntent;
		} else {
			Intent intent = new Intent("com.clp.sdk.ACTION_RECEIVE_GEOFENCE");
			return PendingIntent.getBroadcast(this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
		}
	}

	@Override
	public void onResult(com.google.android.gms.common.api.Status status) {
	}

	@Override
	public void onDisconnected() {
	}

	// Beacon start
	public void beaconMonitorStarts(ArrayList<String> stores) {
		_clpSdk.getBeaconsForStoreNo(stores);
	}

	protected synchronized void startBeaconForStore() {
		if (_clpSdk.allBeaconsForStores != null) {
			if (_clpSdk.allBeaconsForStores != null) {
				if (beaconManager != null) {
					stopBeaconMonitoring();
				}
				// Start_Beacon_Monitoring(_clpSdk.allBeaconsForStores);
				Start_Beacon_Ranging(_clpSdk.allBeaconsForStores);

			}
		}
	}

	// public void Start_Beacon_Monitoring(final List<CLPBeacons>
	// ListBeaconRegions) {
	//
	// if (beaconManager == null)
	// beaconManager = new BeaconManager(this);
	// beaconManager.setMonitoringListener(new MonitoringListener() {
	//
	// @Override
	// public void onExitedRegion(Region region) {
	// stopBeaconRanging();
	// }
	//
	// @Override
	// public void onEnteredRegion(Region region, List<Beacon> beaconList) { //
	// Start_Beacon_Ranging(_clpSdk.allBeaconsForStores);
	// }
	// });
	// beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
	// @Override
	// public void onServiceReady() {
	// try {
	// com.estimote.sdk.utils.L.enableDebugLogging(true);// enable
	// // log
	// Region region;
	// CLPSdk.sharedInstance(ctx).localLog("", " beaconRegionList: " +
	// ListBeaconRegions);
	// beaconMonitorRegions = new ArrayList<CLPBeaconRegion>();
	// for (CLPBeacons beacon : ListBeaconRegions) {
	// region = new Region(String.valueOf(beacon.beaconId),
	// beacon.udid, beacon.major, beacon.minor);
	// if (_clpSdk.isBluetoothEnabled() == false
	// && _clpSdk.getBluetoothPermission()) {
	// _clpSdk.enableBLEDevice();
	// }
	// beaconManager.startMonitoring(region);
	// CLPBeaconRegion beaconRegion = new CLPBeaconRegion(
	// region, beacon.storeID);
	// beaconMonitorRegions.add(beaconRegion);// add region to
	// // stop
	// // monitor
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	public void Start_Beacon_Ranging(final List<CLPBeacons> lstBeaconRegions)//
	{

		if (beaconManager == null)
			beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(new RangingListener() {
			@Override
			public void onBeaconsDiscovered(Region region,
					List<Beacon> regionList) {
				if (regionList != null && !regionList.isEmpty()) {
					Beacon beacon = regionList.get(0);
					Proximity proximity = computeProximity(beacon);
					Log.d("TAG", "entered in bregion " + region.getIdentifier()
							+ " " + region.getProximityUUID());

					if (proximity == Proximity.IMMEDIATE
							|| proximity == Proximity.NEAR
							|| proximity == Proximity.FAR) {

						float interval = _clpSdk.getBEACON_PING_FREQUENCY(); //
						Boolean call = false;
						Date currentCall = new Date();
						int beaconpingInterval = 0;
						if (lastBeaconCall != null) {
							beaconpingInterval = (int) (currentCall.getTime() - lastBeaconCall
									.getTime()) / 1000;
							if (beaconpingInterval >= interval) {
								call = true;
							}
						} else {
							call = true;
						}

						if (call) {
							CLPSdk.sharedInstance(ctx).localLog(
									TAG,
									"beaconpingInterval : "
											+ beaconpingInterval);
							lastBeaconCall = currentCall;
							CLPMobileEvents clpMobileEvent = new CLPMobileEvents();
							clpMobileEvent.lat = _clpSdk.mCurrentLocation
									.getLatitude();
							clpMobileEvent.lon = _clpSdk.mCurrentLocation
									.getLongitude();
							clpMobileEvent.beaconid = Integer.parseInt(region
									.getIdentifier());
							_clpSdk.beaconInRange(ctx, clpMobileEvent);
						}
					}
				}
			}
		});

		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					Region region;
					CLPSdk.sharedInstance(ctx).localLog("beacon",
							" beaconRegionList: " + lstBeaconRegions);
					beaconRangingRegions = new ArrayList<CLPBeaconRegion>();

					for (CLPBeacons beacon : lstBeaconRegions) {
						region = new Region(String.valueOf(beacon.beaconId),
								beacon.udid, beacon.major, beacon.minor);
						if (_clpSdk.isBluetoothEnabled() == false
								&& _clpSdk.getBluetoothPermission()) {
							_clpSdk.enableBLEDevice();
						}
						beaconManager.startRanging(region);
						CLPBeaconRegion beaconRegion = new CLPBeaconRegion(
								region, beacon.storeID);
						beaconRangingRegions.add(beaconRegion);// add region to
																// stop
						// monitor
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	public void stopBeaconRanging() {
		try {
			if (beaconManager != null && beaconRangingRegions != null) {

				for (CLPBeaconRegion beacon : beaconRangingRegions) {
					beaconManager.stopRanging(beacon.region);
				}
				beaconRangingRegions.clear();
			}
		} catch (Exception e) {
			CLPSdk.sharedInstance(ctx).localLog("Exception StopBeacon : ",
					e.getMessage());
		}
	}

	public void stopBeaconRanging(List<String> storeIds) {
		try {
			if (beaconManager != null && beaconRangingRegions != null) {

				for (CLPBeaconRegion beacon : beaconRangingRegions) {
					for (String storeId : storeIds) {
						if (beacon.StoreId == Integer.parseInt(storeId)) {
							beaconManager.stopRanging(beacon.region);
						}
					}
				}
			}
		} catch (Exception e) {
			CLPSdk.sharedInstance(ctx).localLog("Exception StopBeacon : ",
					e.getMessage());
		}
	}

	public void stopBeaconMonitoring() {
		try {
			if (beaconManager != null && beaconMonitorRegions != null) {
				for (CLPBeaconRegion beacon : beaconMonitorRegions) {
					beaconManager.stopMonitoring(beacon.region);
				}
				beaconMonitorRegions.clear();
			}
		} catch (Exception e) {
			CLPSdk.sharedInstance(ctx).localLog("Exception StopBeacon : ",
					e.getMessage());
		}
	}

	public void stopBeaconMonitoring(List<String> storeIds) {
		try {
			if (beaconManager != null && beaconMonitorRegions != null) {
				for (CLPBeaconRegion beacon : beaconMonitorRegions) {
					for (String storeId : storeIds) {
						if (beacon.StoreId == Integer.parseInt(storeId)) {
							beaconManager.stopMonitoring(beacon.region);
						}
					}
				}
			}
		} catch (Exception e) {
			CLPSdk.sharedInstance(ctx).localLog("Exception StopBeacon : ",
					e.getMessage());
		}
	}

	// compute accuracy
	public static double computeAccuracy(Beacon beacon) {
		if (beacon.getRssi() == 0) {
			return -1.0D;
		}

		double ratio = beacon.getRssi() / beacon.getMeasuredPower();
		double rssiCorrection = 0.96D + Math.pow(Math.abs(beacon.getRssi()),
				3.0D) % 10.0D / 150.0D;

		if (ratio <= 1.0D) {
			return Math.pow(ratio, 9.98D) * rssiCorrection;
		}
		return (0.103D + 0.89978D * Math.pow(ratio, 7.71D)) * rssiCorrection;
	}

	public static Proximity proximityFromAccuracy(double accuracy) {
		if (accuracy < 0.0D) {
			return Proximity.UNKNOWN;
		}
		if (accuracy < 0.5D) {
			return Proximity.IMMEDIATE;
		}
		if (accuracy <= 3.0D) {
			return Proximity.NEAR;
		}
		return Proximity.FAR;
	}

	public static Proximity computeProximity(Beacon beacon) {
		return proximityFromAccuracy(computeAccuracy(beacon));
	}

}