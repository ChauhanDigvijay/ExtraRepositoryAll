package com.clp.sdk;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.clp.model.CLPBeacons;
import com.clp.model.CLPCustomer;
import com.clp.model.CLPMobileEvents;
import com.clp.model.CLPStores;
import com.estimote.sdk.BeaconManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

@SuppressLint("UseSparseArrays")
public class CLPSdk {
	// public static String SERVER_URL = "https://dev.clpcloud.com/clpapi/";//
	// dev
	public static String SERVER_URL = "https://raleys.clyptechs.com/clpapi/";// production

	public static String mobileAPIKey = "";
	private static String DEVICE_TYPE = "Android";
	public static String device_os_ver = DEVICE_TYPE.concat(" ").concat(
			android.os.Build.VERSION.RELEASE);
	// List<Beacons> allBeaconsForStores = new ArrayList<Beacons>();
	public static String PERSISTENT_ALL_DATA = "PERSISTENT_ALL_DATA";
	public static String PERSISTENT_CONTEXT = "PERSISTENT_CONTEXT";
	public static String PERSISTENT_FILE_NAME = "PERSISTENT_FILE_NAME";
	public static String PERSISTENT_CUSTOMER = "PERSISTENT_CUSTOMER";
	public static String PERSISTENT_ALL_BEACON_STORE_LIST = "PERSISTENT_ALL_BEACON_STORE_LIST";
	public static String PERSISTENT_ALL_SORTED_STORE_LIST = "PERSISTENT_ALL_SORTED_STORE_LIST";
	public static String PERSISTENT_MOBILE_SETTINGS = "PERSISTENT_MOBILE_SETTINGS";
	public static String PERSISTENT_BLUETOOTH_PERMISSION = "PERSISTENT_BLUETOOTH_PERMISSION";
	public static String PERSISTENT_GPS_PERMISSION = "PERSISTENT_GPS_PERMISSION";
	public static String PERSISTENT_LATITUDE = "PERSISTENT_LATITUDE";
	public static String PERSISTENT_LONGITUDE = "PERSISTENT_LONGITUDE";

	// current gps priority
	public String currentLocationRequest;
	public static String OUTSIDE_REGION = "OUTSIDE_REGION";
	public static String INSIDE_REGION = "INSIDE_REGION";
	public static String INSIDE_STORE = "INSIDE_STORE";

	public ArrayList<Geofence> mGeofenceList;
	public Map<Integer, CLPStores> storesMap = new HashMap<Integer, CLPStores>();
	protected static final String TAG = "CLPSdk";
	public boolean beaconStarted;
	Context context;
	public BeaconManager beaconManager;
	private static CLPSdk instance;
	// public Customer currCustomer;
	protected Location mCurrentLocation;

	// /persistant
	private File clpSdkDataFileDir;
	private File clpSdkDataFile;
	private CLPSdkData clpSdkData;
	private static String CLP_SDK_FILE_NAME = "clpsdk.data";

	// /Mobile Settings
	public Date lastGeofenceStartCall;
	public Date lastLocationUpdateCall;

	// callback
	public static String CLP_SETTINGS_UPDATE_CALLBACK = "CLP_SETTINGS_UPDATE_CALLBACK";
	public static String CLP_GEOFENCE_CALLBACK = "CLP_GEOFENCE_CALLBACK";
	public static String CLP_STARTBEACON_CALLBACK = "CLP_STARTBEACON_CALLBACK";
	public static String CLP_CALLBACK = "CLP_CALLBACK";

	// use default
	public final double DEFAULT_LATITUDE = 38.577160;
	public final double DEFAULT_LONGITUDE = -121.495560;
	List<CLPBeacons> allBeaconsForStores = new ArrayList<CLPBeacons>();
	public static AsyncHttpClient aClient = new AsyncHttpClient();
	public static AsyncHttpClient sClient = new SyncHttpClient();

	public static CLPSdk sharedInstanceWithKey(Context ctx, String key) {
		if (instance == null) {
			instance = new CLPSdk(ctx, key);
		}
		return instance;
	}

	public static CLPSdk sharedInstance(Context ctx) {
		return instance;
	}

	public CLPSdk() {
	}

	public CLPSdk(Context context, String key) {
		try {
			this.context = context;
			beaconManager = new BeaconManager(context);
			beaconStarted = false;
			mobileAPIKey = key;

			// check for GPS status
			checkGPSEnabledByUser();

			// //PERSISTENT FILE///
			clpSdkDataFileDir = context.getApplicationContext().getFilesDir();
			clpSdkData = new CLPSdkData(context.getApplicationContext(),
					CLP_SDK_FILE_NAME);
			clpSdkDataFile = new File(clpSdkDataFileDir.getAbsolutePath() + "/"
					+ CLP_SDK_FILE_NAME);
			if (clpSdkDataFile.exists() == false) {
				clpSdkDataFile.createNewFile();
				clpSdkData.currCustomer = new CLPCustomer();
				clpSdkData.setAllCLPBeaconStoreList(null);
				clpSdkData.setAllStoresList(null);
				clpSdkData.save(); // init
			} else {
				clpSdkData.refresh();
			}

			// /get mobile settings
			// timer.schedule(updateTask, 0, TimeUnit.MINUTES.toMillis(60));

			// get allstores
			// if (clpSdkData.allCLPBeaconStoreList == null
			// || clpSdkData.allCLPBeaconStoreList.size() == 0) {
			getAllStores();
			mobileSettings();
			// }

			// Location service kill check
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(context, LocationServiceCheck.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent, PendingIntent.FLAG_ONE_SHOT);
			am.cancel(pendingIntent);
			am.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void mobileSettings() {
		try {
			// serialClient = new AsyncHttpClient();
			if (!isNetworkAvailable(context)) {
				return;
			}
			getClient().addHeader("CLP-API-KEY", mobileAPIKey);
			String cusId = "0";
			if (clpSdkData.currCustomer != null)
				cusId = String.valueOf(clpSdkData.currCustomer.customerID);
			getClient().get(
					SERVER_URL
							+ "mobile/settings/getmobilesettings?customerID="
							+ cusId, null, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							JSONArray getArrayMobileSettings;
							try {
								if (response.has("mobileSettings")) {
									getArrayMobileSettings = response
											.getJSONArray("mobileSettings");
									for (int i = 0; i < getArrayMobileSettings
											.length(); i++) {
										JSONObject myMobileSettingObj = getArrayMobileSettings
												.getJSONObject(i);
										if (myMobileSettingObj.getString(
												"settingName").equals(
												("DISTANCE_FILTER"))) {
											clpSdkData.mobileSettings.DISTANCE_FILTER = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"DISTANCE_STORE")) {
											clpSdkData.mobileSettings.DISTANCE_STORE = Float
													.parseFloat(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"GEOFENCE_RADIUS")) {
											clpSdkData.mobileSettings.GEOFENCE_RADIUS = Float
													.parseFloat(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"IN_REGION_SLAB_TIME")) {
											clpSdkData.mobileSettings.IN_REGION_SLAB_TIME = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"STORE_REFRESH_TIME")) {
											clpSdkData.mobileSettings.STORE_REFRESH_TIME = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"MAX_STORE_COUNT_ANDROID")) {
											clpSdkData.mobileSettings.MAX_STORE_COUNT_ANDROID = Integer
													.parseInt(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"MAX_BEACON_COUNT")) {
											clpSdkData.mobileSettings.MAX_BEACON_COUNT = Integer
													.parseInt(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"ENABLE_LOCAL_NOTIFICATION")) {
											clpSdkData.mobileSettings.ENABLE_LOCAL_NOTIFICATION = Integer
													.parseInt(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"OUT_SIDE_UPDATE_INTERVAL")) {
											clpSdkData.mobileSettings.OUT_SIDE_UPDATE_INTERVAL = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj
												.getString("settingName")
												.equals("OUT_SIDE_FASTEST_UPDATE_INTERVAL")) {
											clpSdkData.mobileSettings.OUT_SIDE_FASTEST_UPDATE_INTERVAL = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"IN_SIDE_UPDATE_INTERVAL")) {
											clpSdkData.mobileSettings.IN_SIDE_UPDATE_INTERVAL = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj
												.getString("settingName")
												.equals("IN_SIDE_FASTEST_UPDATE_INTERVAL")) {
											clpSdkData.mobileSettings.IN_SIDE_FASTEST_UPDATE_INTERVAL = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"GEOFENCE_CHECK_FREQUENCY")) {
											clpSdkData.mobileSettings.GEOFENCE_CHECK_FREQUENCY = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj
												.getString("settingName")
												.equals("LOCATION_UPDATE_PING_FREQUENCY")) {
											clpSdkData.mobileSettings.LOCATION_UPDATE_PING_FREQUENCY = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"BEACON_PING_FREQUENCY")) {
											clpSdkData.mobileSettings.BEACON_PING_FREQUENCY = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"GEOFENCE_EXPIRY_TIME")) {
											clpSdkData.mobileSettings.GEOFENCE_EXPIRY_TIME = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"GEOFENCE_MIN_RADIUS")) {
											clpSdkData.mobileSettings.GEOFENCE_MIN_RADIUS = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"IN_STORE_UPDATE_INTERVAL")) {
											clpSdkData.mobileSettings.IN_STORE_UPDATE_INTERVAL = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj
												.getString("settingName")
												.equals("LOCATION_SERVICE_REFRESH_TIME")) {
											clpSdkData.mobileSettings.LOCATION_SERVICE_REFRESH_TIME = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}

										if (myMobileSettingObj
												.getString("settingName")
												.equals("GEOFENCE_CHECK_DISTANCE_MOVED")) {
											clpSdkData.mobileSettings.GEOFENCE_CHECK_DISTANCE_MOVED = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}
										if (myMobileSettingObj.getString(
												"settingName").equals(
												"BEACON_SLAB_TIME")) {
											clpSdkData.mobileSettings.BEACON_SLAB_TIME = Long
													.parseLong(myMobileSettingObj
															.getString("settingValue"));
										}

									}

									clpSdkData
											.setMobileSettings(clpSdkData.mobileSettings);
									// clpSdkData.save(PERSISTENT_MOBILE_SETTINGS,
									// clpSdkData.mobileSettings);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

							localLog("mobileSettings", "SUCC12 + " + response
									+ "");
						}

						@Override
						public void onFailure(int id, Header[] header,
								Throwable error, JSONObject jsonObj) {
							localLog("mobileSettings error",
									"failed to retrieve");

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String str, Throwable throwable) {
							localLog("mobileSettings error",
									"failed to retrieve");
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /services
	public void getAllStores() {

		try {
			// serialClient = new AsyncHttpClient();
			localLog("getAllStores", "Called");

			if (!isNetworkAvailable(context)) {
				return;
			}
			getClient().addHeader("CLP-API-KEY", CLPSdk.mobileAPIKey);
			getClient().get(CLPSdk.SERVER_URL + "mobile/stores/getstores",
					null, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							JSONArray getArrayStores;
							try {
								getArrayStores = response
										.getJSONArray("stores");
								if (getArrayStores != null) {
									for (int i = 0; i < getArrayStores.length(); i++) {
										JSONObject myStoresObj = getArrayStores
												.getJSONObject(i);
										CLPStores getStoresObj = new CLPStores(
												myStoresObj);
										storesMap.put(
												getStoresObj.getStoreID(),
												getStoresObj);
									}
									// Remove it - ramarajan
									// CLPStores vthinkStore = new CLPStores();
									// vthinkStore.companyID = 1;
									// vthinkStore.enableGeoConquest = "N";
									// vthinkStore.latitude = "12.929600";
									// vthinkStore.longitude = "80.161370";
									// vthinkStore.storeID = 777;
									// vthinkStore.storeName = "Vtechstore";
									//
									// // clpSdkData.allCLPBeaconStoreList
									// // .add(vthinkStore);
									// storesMap.put(vthinkStore.getStoreID(),
									// vthinkStore);
									// ///////////

									clpSdkData
											.setAllCLPBeaconStoreList(new ArrayList<CLPStores>(
													storesMap.values()));

									getNearestBeaconStores(getmCurrentLocation());// always
																					// sort

									localLog("clpsdk ls :", "Store downloaded");
									Intent intent = new Intent();
									if (!isMyServiceRunning(
											LocationService.class, context)) {
										intent = new Intent(context,
												LocationService.class);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										context.startService(intent);
									} else {
										intent.setAction(CLP_CALLBACK);
										intent.putExtra(CLP_CALLBACK,
												CLP_SETTINGS_UPDATE_CALLBACK);
										context.sendBroadcast(intent);
									}
								} else {
									localLog("allstore error", "Download error");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int id, Header[] header,
								Throwable error, JSONObject jsonObj) {
							localLog("allstore error",
									jsonObj + " " + error.getMessage());

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String str, Throwable throwable) {
							localLog("allstore error",
									str + " " + throwable.getMessage());
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public void getNearestBeaconStores(Location currentLocation) {
		localLog("currentLocation : ", currentLocation.getLatitude() + ","
				+ currentLocation.getLongitude());
		ArrayList<CLPStores> storesDistance = new ArrayList<CLPStores>();
		if (clpSdkData.getAllCLPBeaconStoreList() == null)
			return;
		int i = 0;
		for (CLPStores store : clpSdkData.getAllCLPBeaconStoreList()) {
			if (store.latitude.isEmpty() || store.longitude.isEmpty())
				continue;

			try {
				Location storeLocation = new Location("");
				storeLocation.setLatitude(Double.valueOf(store.latitude));
				storeLocation.setLongitude(Double.valueOf(store.longitude));
				store._distanceFromLocation = storeLocation
						.distanceTo(currentLocation);
				storesDistance.add(i, store);
				i++;
			} catch (NumberFormatException nfex) {
				nfex.printStackTrace();
			}
		}

		Collections.sort(storesDistance, new StoreDistanceComparator());
		int noOfStores = storesDistance.size();
		if (noOfStores > clpSdkData.mobileSettings.MAX_STORE_COUNT_ANDROID)
			noOfStores = clpSdkData.mobileSettings.MAX_STORE_COUNT_ANDROID;
		List<CLPStores> curStores = new ArrayList<CLPStores>(
				storesDistance.subList(0, noOfStores));
		clpSdkData.setAllStoresList(curStores);

		if (clpSdkData.getAllStoresList() != null
				&& clpSdkData.getAllStoresList().size() > 0
				&& clpSdkData.getAllStoresList().get(0).storeName != null)
			displayLocalPushNotification("Nearest store is ".concat(clpSdkData
					.getAllStoresList().get(0).storeName), context.getClass(),
					R.drawable.ic_launcher, context);
		// localLog("_allStoresList sorted",
		// clpSdkData.getAllStoresList().toString());
	}

	private class StoreDistanceComparator implements Comparator<CLPStores> {
		@Override
		public int compare(CLPStores store1, CLPStores store2) {
			// stores without valid locations are treated as being farthest
			if (store1._distanceFromLocation == 0.0)
				return 1;
			else if (store2._distanceFromLocation == 0.0)
				return -1;

			return (store1._distanceFromLocation > store2._distanceFromLocation ? 1
					: (store1._distanceFromLocation == store2._distanceFromLocation ? 0
							: -1));
		}
	}

	public void getBeaconsForStoreNo(ArrayList<String> stores) {
		// AsyncHttpClient client = new AsyncHttpClient();
		getClient().addHeader("CLP-API-KEY", CLPSdk.mobileAPIKey);
		for (String store : stores) {
			int nearestStore = Integer.valueOf(store);
			String url = CLPSdk.SERVER_URL + "mobile/beacons/" + nearestStore;
			localLog("getBeaconsForStoreNo", url);
			getClient().get(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					JSONArray getArrayBeacons;
					try {
						if (response.getBoolean("successFlag")) {
							getArrayBeacons = response.getJSONArray("beacons");
							for (int i = 0; i < getArrayBeacons.length(); i++) {
								JSONObject myBeaconsObj = getArrayBeacons
										.getJSONObject(i);
								CLPBeacons getBeaconsObj = new CLPBeacons(
										myBeaconsObj);
								localLog("beacons", getBeaconsObj.toString());
								allBeaconsForStores.add(getBeaconsObj);
								if (allBeaconsForStores.size() >= getMAX_BEACON_COUNT()
										&& allBeaconsForStores.size() > 0) {
									allBeaconsForStores.remove(1);// remove
																	// first
																	// beacon
								}
							}
						}

						// remove it - ramarajan
						// CLPBeacons vthinkBeacons = new CLPBeacons();
						// vthinkBeacons.storeID = 777;
						// vthinkBeacons.beaconId = 777;
						// vthinkBeacons.companyId = 1;
						// vthinkBeacons.udid =
						// "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
						// vthinkBeacons.major = 35478;
						// vthinkBeacons.minor = 57817;
						// allBeaconsForStores.add(vthinkBeacons);

						if (!allBeaconsForStores.isEmpty()
								&& isMyServiceRunning(LocationService.class,
										context)) {

							Intent intent = new Intent();
							intent.setAction(CLP_CALLBACK);
							intent.putExtra(CLP_CALLBACK,
									CLP_STARTBEACON_CALLBACK);
							context.sendBroadcast(intent);
							// startBeaconForStore(allBeaconsForStores);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					localLog(
							"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
							throwable.getMessage());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String str, Throwable throwable) {
					localLog(
							"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
							throwable.getMessage());
				}
			});
		}
	}

	public void saveCustomer(CLPCustomer customer) {
		if (!isNetworkAvailable(context)) {
			return;
		}
		customer.deviceID = getAndroidDeviceID();// assign android device id for
													// customer
		customer.deviceType = DEVICE_TYPE;// assign android device id for
		customer.deviceOsVersion = getAndroidOs();
		customer.loginPassword = "password";
		if (isLocationServiceProviderAvailable()) {
			customer.locationEnabled = "Y";
		} else {
			customer.locationEnabled = "N";
		}
		getClient().addHeader("CLP-API-KEY", mobileAPIKey);
		StringEntity entity;
		try {
			Gson gson = new Gson();
			String json = gson.toJson(customer);
			entity = new StringEntity(json);
			localLog(TAG, json);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			getClient().post(context, SERVER_URL + "mobile/customer", entity,
					"application/json", new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							localLog(TAG, response.toString());
							try {
								clpSdkData.currCustomer = new CLPCustomer();
								clpSdkData.currCustomer.companyId = response
										.has("companyId") ? Integer
										.parseInt(response
												.getString("companyId")) : 0;
								clpSdkData.currCustomer.customerID = response
										.has("customerID") ? Integer
										.parseInt(response
												.getString("customerID")) : 0;
								clpSdkData.currCustomer.firstName = response
										.has("firstName") ? response
										.getString("firstName") : "";
								clpSdkData.currCustomer.lastName = response
										.has("lastName") ? response
										.getString("lastName") : "";
								clpSdkData.currCustomer.emailID = response
										.has("emailID") ? response
										.getString("emailID") : "";
								clpSdkData.currCustomer.loginID = response
										.has("loginID") ? response
										.getString("loginID") : "";
								clpSdkData.currCustomer.cellPhone = response
										.has("cellPhone") ? response
										.getString("cellPhone") : "";
								clpSdkData.currCustomer.pushToken = response
										.has("pushToken") ? response
										.getString("pushToken") : "";

								localLog("clpsdk save cust persis :",
										clpSdkData.currCustomer.toString());
								clpSdkData.setCustomer(clpSdkData.currCustomer);
							} catch (Exception e) {
								e.printStackTrace();
							}

							localLog("Customer registration", "SUCCESS + "
									+ response + "");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							localLog(
									"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
									throwable.getMessage());
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String str, Throwable throwable) {
							localLog(
									"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
									throwable.getMessage());
						}
					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void locationUpdateDevice(Context contx, CLPMobileEvents mobileEvents) {
		if (clpSdkData.currCustomer == null
				|| clpSdkData.currCustomer.customerID <= 0
				|| !isNetworkAvailable(context)) {
			return;
		}
		// AsyncHttpClient client = new AsyncHttpClient();
		getClient().addHeader("CLP-API-KEY", mobileAPIKey);
		mobileEvents.company = clpSdkData.currCustomer.companyId;
		mobileEvents.customerid = clpSdkData.currCustomer.customerID;
		mobileEvents.action = "LocationChange";
		mobileEvents.device_type = DEVICE_TYPE;
		mobileEvents.device_os_ver = getAndroidOs();
		mobileEvents.device_carrier = getDeviceCarier();

		try {
			Gson gson = new Gson();
			String json = gson.toJson(mobileEvents);
			StringEntity entity;
			entity = new StringEntity(json);
			localLog("locationUpdateDevice", "locationUpdateDevice + " + json
					+ "");
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			getClient().post(contx, SERVER_URL + "mobile/locationchanged",
					entity, "application/json", new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							localLog("locationUpdateDevice", "SUCC + "
									+ response + "");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							if (errorResponse != null)
								localLog(
										"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
										errorResponse.toString());
						}
					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void beaconInRange(Context contx, CLPMobileEvents mobileEvents) {
		if (clpSdkData.currCustomer == null
				|| clpSdkData.currCustomer.customerID <= 0
				|| !isNetworkAvailable(context)) {
			return;
		}
		// AsyncHttpClient client = new AsyncHttpClient();
		getClient().addHeader("CLP-API-KEY", mobileAPIKey);
		try {
			mobileEvents.company = clpSdkData.currCustomer.companyId;
			mobileEvents.customerid = clpSdkData.currCustomer.customerID;
			mobileEvents.action = "BeaconInRange";
			mobileEvents.device_type = DEVICE_TYPE;
			mobileEvents.device_os_ver = getAndroidOs();
			mobileEvents.device_carrier = getDeviceCarier();

			StringEntity entity;
			try {
				Gson gson = new Gson();
				String json = gson.toJson(mobileEvents);
				entity = new StringEntity(json);
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				getClient().post(contx, SERVER_URL + "mobile/beaconsinrange",
						entity, "application/json",
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								// JSONObject getAnObject = response.get(0);
								// String BC = getAnObject.getString("Values");
								localLog("beaconInRange", "SUCC + " + response
										+ "");
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								if (errorResponse != null)
									localLog(
											"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
											errorResponse.toString());
							}
						});
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendOfferEvent(JSONObject offerEvent) {
		if (clpSdkData.currCustomer == null
				|| clpSdkData.currCustomer.customerID <= 0
				|| !isNetworkAvailable(context)) {
			return;
		}
		// AsyncHttpClient client = new AsyncHttpClient();
		getClient().addHeader("CLP-API-KEY", mobileAPIKey);
		StringEntity entity;
		try {
			try {
				offerEvent.put("action", "CLPEvent");
				offerEvent.put("companyid",
						clpSdkData.currCustomer.getCompanyId());
				offerEvent.put("customerid",
						clpSdkData.currCustomer.getCustomerID());
				offerEvent.put("lat",
						String.valueOf(mCurrentLocation.getLatitude()));
				offerEvent.put("lon",
						String.valueOf(mCurrentLocation.getLongitude()));
				offerEvent.put("device_type", DEVICE_TYPE);
				offerEvent.put("device_os_ver", getAndroidOs());
			}

			catch (JSONException e) {
				e.printStackTrace();
			}
			entity = new StringEntity(offerEvent.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			getClient().post(context, SERVER_URL + "mobile/submitclpevents",
					entity, "application/json", new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							localLog("sendOfferEvent", "SUCC + " + response
									+ "");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							if (errorResponse != null)
								localLog(
										"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
										errorResponse.toString());
						}
					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void updateAppEvent(JSONObject appEvent) {
		if (clpSdkData.getCurrCustomer() == null
				|| clpSdkData.getCurrCustomer().customerID <= 0
				|| !isNetworkAvailable(context)) {
			localLog("clpsdk : ", "getCurrCustomer is null");
			return;
		}
		localLog("clpsdk : ", "sendAppEvent");
		if (mCurrentLocation == null) {
			Location curLoc = new Location("");
			curLoc.setLatitude(0);
			curLoc.setLongitude(0);
			mCurrentLocation = curLoc;
		}
		// AsyncHttpClient client = new AsyncHttpClient();
		getClient().addHeader("CLP-API-KEY", mobileAPIKey);

		StringEntity entity;
		try {
			try {
				appEvent.put("action", "AppEvent");
				appEvent.put("companyid",
						clpSdkData.currCustomer.getCompanyId());
				appEvent.put("customerid",
						clpSdkData.currCustomer.getCustomerID());
				appEvent.put("lat",
						String.valueOf(mCurrentLocation.getLatitude()));
				appEvent.put("lon",
						String.valueOf(mCurrentLocation.getLongitude()));
				appEvent.put("device_type", DEVICE_TYPE);
				appEvent.put("device_os_ver", getAndroidOs());
			}

			catch (JSONException e) {
				e.printStackTrace();
			}

			entity = new StringEntity(appEvent.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			getClient().post(context, SERVER_URL + "mobile/submitappevents",
					entity, "application/json", new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							localLog("sendAppEvent SUCCESS", "Response + "
									+ response + "");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							if (errorResponse != null)
								localLog(
										"onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received",
										errorResponse.toString());
						}
					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// ////////////LOCATION SERVICE
	public void startLocationService() {
		// if (!isMyServiceRunning(LocationService.class, context)) {
		Intent intent = new Intent(context, LocationService.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(intent);
		// } else {
		// Intent intent = new Intent();
		// intent.setAction(CLP_CALLBACK);
		// intent.putExtra(CLP_CALLBACK, CLP_SETTINGS_UPDATE_CALLBACK);
		// context.sendBroadcast(intent);
		// }
	}

	public Location getmCurrentLocation() {
		if (mCurrentLocation == null) {
			return getCachedLocation();
		} else {
			return mCurrentLocation;
		}
	}

	public void setmCurrentLocation(Location mCurrentLocation) {
		this.mCurrentLocation = mCurrentLocation;
	}

	public void processPushMessage(Intent intent) {
		localLog("processPushMessage :", intent.getExtras().toString());
		try {
			String alert = "";
			String url = intent.getStringExtra("url");// passbook url
			String ntype = intent.getStringExtra("ntype");
			String apsStr = intent.getExtras().getString("aps");
			JSONObject apsJson = new JSONObject(apsStr);
			if (apsJson.has("alert")) {
				alert = apsJson.getString("alert");// message
			}
			String couponid = "";
			// String eoid = "";
			// String custid = "";
			String clpnid = "";

			String sound_url = "";
			if (apsJson.has("sound")) {
				sound_url = apsJson.getString("sound");
				if (sound_url != null) {
					sound_url = sound_url.toLowerCase(Locale.US);
					sound_url = sound_url.replace(".wma", "");
					sound_url = sound_url.replace(".wav", "");
					sound_url = sound_url.replace(".caf", "");
				}
			}

			// playSound(sound_url)
			if (intent.hasExtra("clpnid")) {
				clpnid = intent.getStringExtra("clpnid");// clp Notification Id
			}
			// if (intent.hasExtra("custid")) {
			// custid = intent.getStringExtra("custid");
			// }
			if (intent.hasExtra("offerid")) {
				couponid = intent.getStringExtra("offerid");
			}

			// url = "http://10.0.0.20/passbook/pass.pkpass";
			if (url != null && url.length() != 0) {
				// ntype = "";
				// url = "http://www.google.com";
				if (ntype == null || ntype.equals("pass") || ntype.equals(""))
					openPassBook(Uri.parse(url));
				else if (ntype.equals("web"))
					openWeb(Uri.parse(url));

			}
			if (alert != null && alert.length() != 0) {
				// do not do anything
			}

			JSONObject obj = new JSONObject();
			obj.put("notifid", clpnid);
			obj.put("offerid", couponid);
			obj.put("customerid", clpSdkData.getCurrCustomer());
			obj.put("device_type", DEVICE_TYPE);
			obj.put("device_os_ver", getAndroidOs());
			obj.put("device_carrier", getDeviceCarier());
			obj.put("event_time", formatedCurrentDate());

			obj.put("event_name", "PUSH_OPEN");
			localLog("PushOpen : ", obj.toString());
			sendOfferEvent(obj);
			obj.put("event_name", "ACCEPT_OFFER");
			localLog("AcceptOffer : ", obj.toString());
			sendOfferEvent(obj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// Pass book start

	protected boolean openPassBook(Uri uri) {
		if (null != context && isNetworkAvailable(context)) {
			PackageManager packageManager = context.getPackageManager();
			if (null != packageManager) {
				final String strPackageName = "com.attidomobile.passwallet";
				Intent startIntent = new Intent();
				startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startIntent.setAction(Intent.ACTION_VIEW);
				Intent passWalletLaunchIntent = packageManager
						.getLaunchIntentForPackage(strPackageName);
				if (null == passWalletLaunchIntent) { // PassWallet isn't
					// installed, open
					// Google Play:
					if (checkPlayServices()) {
						String strReferrer = "";
						try {
							final String strEncodedURL = URLEncoder.encode(
									uri.toString(), "UTF-8");
							strReferrer = "&referrer=" + strEncodedURL;
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							strReferrer = "";
						}
						try {
							startIntent.setData(Uri
									.parse("market://details?id="
											+ strPackageName + strReferrer));
							context.startActivity(startIntent);
						} catch (android.content.ActivityNotFoundException anfe) {
							// Google Play not installed, open via website
							startIntent
									.setData(Uri
											.parse("http://play.google.com/store/apps/details?id="
													+ strPackageName
													+ strReferrer));
							context.startActivity(startIntent);
						}
					}
				} else {
					final String strClassName = "com.attidomobile.passwallet.activity.TicketDetailActivity";
					startIntent.setClassName(strPackageName, strClassName);
					startIntent.addCategory(Intent.CATEGORY_BROWSABLE);
					startIntent.setDataAndType(uri,
							"application/vnd.apple.pkpass");
					context.startActivity(startIntent);
					return true;
				}
			}
		}
		return false;
	}

	public void openWeb(Uri uri) {
		Intent browserOpenIntent = new Intent(Intent.ACTION_VIEW).setData(uri);
		browserOpenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(browserOpenIntent);
	}

	protected boolean checkPlayServices() {
		// Getting status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		// Showing status
		if (status == ConnectionResult.SUCCESS)
			return true;
		else {
			return false;
		}
	}

	/*
	 * Android Device id
	 */
	protected String getAndroidDeviceID() {
		String android_id = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return android_id;
	}

	public static String getAndroidDeviceName() {
		String deviceName = "";

		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			deviceName = capitalize(model);
		} else {
			deviceName = capitalize(manufacturer) + " " + model;
		}
		if (deviceName != null && !deviceName.equalsIgnoreCase("null")) {
			return deviceName;
		} else {
			return "";
		}
	}

	private static String capitalize(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		char first = str.charAt(0);
		if (Character.isUpperCase(first)) {
			return str;
		} else {
			return Character.toUpperCase(first) + str.substring(1);
		}
	}

	public String formatedCurrentDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);// "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String formatedDate = formatter.format(date);
		return formatedDate;
	}

	public String getAndroidOs() {
		if (device_os_ver != null && device_os_ver.length() != 0) {
			return device_os_ver;
		} else {
			return "";
		}
	}

	public String getDeviceCarier() {
		String device_carrier = "";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getNetworkOperatorName() != null) {
			String provider = tm.getNetworkOperatorName();
			if (provider == null) {
			} else {
				if (!provider.equalsIgnoreCase("null")) {
					device_carrier = tm.getNetworkOperatorName();// getNetworkOperatorName()
				}
			}
		}
		return device_carrier;
	}

	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo netInfo = null;

		try {
			ConnectivityManager connec = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			netInfo = connec.getActiveNetworkInfo();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (netInfo != null && netInfo.isAvailable() == true) {
			return true;
		} else {
			// Toast.makeText(context, "Internet connection unavailable.",
			// Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private static AsyncHttpClient getClient() {
		// Return the synchronous HTTP client when the thread is not prepared
		if (Looper.myLooper() == null)
			return sClient;
		return aClient;
	}

	public boolean isLocationServiceProviderAvailable() {
		LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean providerAvailable = false;
		final List<String> providers = manager.getProviders(true);
		for (final String provider : providers) {
			if (manager.isProviderEnabled(provider)
					&& provider.equals(LocationManager.GPS_PROVIDER)) {
				providerAvailable = true;
			}
		}
		return providerAvailable;
	}

	public Location getCachedLocation() {
		LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Location cachedGPSLocation = manager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);// cached
																	// gps
																	// location
		Location cachedPASSIVELocation = manager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);// cached
																		// gps/wifi/cellular
																		// location
		Location cachedNETWORKLocation = manager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);// cached
																		// wifi/cellular
																		// location

		if (cachedGPSLocation != null) {
			// show cached gps location
			return cachedGPSLocation;
		} else if (cachedPASSIVELocation != null) {
			// show cached gps/wifi/cellular location
			return cachedPASSIVELocation;
		} else if (cachedNETWORKLocation != null) {
			// show cached wifi/cellular location
			return cachedNETWORKLocation;
		} else {
			// set default location
			Location defaultLoc = new Location("DEFAULT");
			defaultLoc.setLatitude(DEFAULT_LATITUDE);
			defaultLoc.setLongitude(DEFAULT_LONGITUDE);
			return defaultLoc;
		}
	}

	private void checkGPSEnabledByUser() {
		if (!isLocationServiceProviderAvailable()) {
			// Toast.makeText(
			// context,
			// "Please switch on the GPS with High Accuracy Mode then only you can receive the offer notifications",
			// Toast.LENGTH_LONG).show();
		}
	}

	public boolean isBluetoothEnabled() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()
				|| !isBLESupportedDevice()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isBLESupportedDevice() {
		// GEt current API level of device
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		// Check API level is > than 18
		if ((currentapiVersion > 18)
				&& (context.getPackageManager()
						.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))) {
			return true;
		}
		return false;
	}

	public void enableBLEDevice() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if ((mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
				&& isBLESupportedDevice()) {
			mBluetoothAdapter.enable();
		}
	}

	public boolean getBluetoothPermission() {
		return clpSdkData.isBluetoothPermission();
	}

	public boolean getGpsPermission() {
		// return clpSdkData.isGpsPermission();
		return true;
	}

	public void setBluetoothPermission(boolean bluetoothPermission) {
		clpSdkData.setBluetoothPermission(bluetoothPermission);
		// clpSdkData.save();
	}

	public void setGpsPermission(boolean gpsPermission) {
		clpSdkData.setGpsPermission(true);
		// clpSdkData.save();
	}

	public List<CLPStores> getAllCLPBeaconStoreList() {
		return clpSdkData.getAllCLPBeaconStoreList();
	}

	public List<CLPStores> getAllStoreList() {
		return clpSdkData.getAllStoresList();
	}

	public long getDISTANCE_FILTER() {
		return clpSdkData.mobileSettings.getDISTANCE_FILTER();
	}

	public float getDISTANCE_STORE() {
		return clpSdkData.mobileSettings.getDISTANCE_STORE();
	}

	public float getGEOFENCE_RADIUS() {
		return clpSdkData.mobileSettings.getGEOFENCE_RADIUS();
	}

	public float getGEOFENCE_MIN_RADIUS() {
		return clpSdkData.mobileSettings.getGEOFENCE_MIN_RADIUS();
	}

	public long getSTORE_REFRESH_TIME() {
		return clpSdkData.mobileSettings.getSTORE_REFRESH_TIME();
	}

	public int getMAX_STORE_COUNT() {
		return clpSdkData.mobileSettings.getMAX_STORE_COUNT_ANDROID();
	}

	public int getMAX_BEACON_COUNT() {
		return clpSdkData.mobileSettings.getMAX_STORE_COUNT_ANDROID();
	}

	public int getENABLE_LOCAL_NOTIFICATION() {
		return clpSdkData.mobileSettings.ENABLE_LOCAL_NOTIFICATION;
	}

	public long getOUT_SIDE_UPDATE_INTERVAL() {
		return clpSdkData.mobileSettings.getOUT_SIDE_UPDATE_INTERVAL();
	}

	public long getOUT_SIDE_FASTEST_UPDATE_INTERVAL() {
		return clpSdkData.mobileSettings.getOUT_SIDE_FASTEST_UPDATE_INTERVAL();
	}

	public long getIN_SIDE_UPDATE_INTERVAL() {
		return clpSdkData.mobileSettings.getIN_SIDE_UPDATE_INTERVAL();
	}

	public long getIN_SIDE_FASTEST_UPDATE_INTERVAL() {
		return clpSdkData.mobileSettings.getIN_SIDE_FASTEST_UPDATE_INTERVAL();
	}

	public long getGEOFENCE_CHECK_FREQUENCY() {
		return clpSdkData.mobileSettings.getGEOFENCE_CHECK_FREQUENCY();
	}

	public long getLOCATION_UPDATE_PING_FREQUENCY() {
		return clpSdkData.mobileSettings.getLOCATION_UPDATE_PING_FREQUENCY();
	}

	public long getBEACON_PING_FREQUENCY() {
		return clpSdkData.mobileSettings.getBEACON_PING_FREQUENCY();
	}

	public long getGEOFENCE_EXPIRY_TIME() {
		return clpSdkData.mobileSettings.getGEOFENCE_EXPIRY_TIME();
	}

	public long getIN_REGION_SLAB_TIME() {
		return clpSdkData.mobileSettings.getIN_REGION_SLAB_TIME();
	}

	public long getLOCATION_SERVICE_REFRESH_TIME() {
		return clpSdkData.mobileSettings.getLOCATION_SERVICE_REFRESH_TIME();
	}

	public long getIN_STORE_UPDATE_INTERVAL() {
		return clpSdkData.mobileSettings.getIN_STORE_UPDATE_INTERVAL();
	}

	public long getGEOFENCE_CHECK_DISTANCE_MOVED() {
		return clpSdkData.mobileSettings.getGEOFENCE_CHECK_DISTANCE_MOVED();
	}

	public long getBEACON_SLAB_TIME() {
		return clpSdkData.mobileSettings.getBEACON_SLAB_TIME();
	}

	public void playSound(String fileName) {
		try {
			Uri notificationuri = getNotificationURI(fileName);
			// //Play ringtone when pass book is open
			if (notificationuri != null) {
				Ringtone ringtone;
				ringtone = RingtoneManager
						.getRingtone(context, notificationuri);
				ringtone.play();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Get ring tone file names
	public Uri getNotificationURI(String soundfileNameFn) {

		Uri notificationuri = null;
		if (soundfileNameFn == null || soundfileNameFn.length() == 0
				|| soundfileNameFn.contains("nosound")) {
			return null;// nosound
		}
		if (soundfileNameFn.equals("chime")) {
			notificationuri = Uri.parse("android.resource://"
					+ context.getPackageName() + "/raw/chime");
		} else if (soundfileNameFn.equals("bell")) {
			notificationuri = Uri.parse("android.resource://"
					+ context.getPackageName() + "/raw/bell");
		} else if (soundfileNameFn.equals("crickets")) {
			notificationuri = Uri.parse("android.resource://"
					+ context.getPackageName() + "/raw/crickets");
		} else if (soundfileNameFn.equals("vibrate")) {
			notificationuri = Uri.parse("android.resource://"
					+ context.getPackageName() + "/raw/vibrate");
		} else if (soundfileNameFn.equals("tring")) {
			notificationuri = Uri.parse("android.resource://"
					+ context.getPackageName() + "/raw/tring");
		} else if (soundfileNameFn.equals("double bell")) {
			notificationuri = Uri.parse("android.resource://"
					+ context.getPackageName() + "/raw/double_bell");
		} else {
			notificationuri = null;// default
		}
		return notificationuri;
	}

	@SuppressWarnings("rawtypes")
	public void displayLocalPushNotification(String message, Class cls, int id,
			Context ctx) {
		if (getENABLE_LOCAL_NOTIFICATION() == 0)
			return;
		Context context = ctx;
		if (id == 0) {
			id = R.drawable.ic_launcher;
		}
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(context, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(id)
				.setContentTitle("Local Notification")
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message).setAutoCancel(true);
		mBuilder.setContentIntent(contentIntent);
		// unique notification id
		// Tag Name of Notification
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
		int hourofday = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int millisecond = cal.get(Calendar.MILLISECOND);
		int NOTIFICATION_ID = 1;
		StringBuilder MY_NOTIFICATION_ID = new StringBuilder(500);
		MY_NOTIFICATION_ID.append(year).append(month).append(dayofmonth)
				.append(hourofday).append(minute).append(minute).append(second)
				.append(millisecond);
		localLog("ID : ", "" + MY_NOTIFICATION_ID);
		String CURRENT_NOTIFICATION_STRING = MY_NOTIFICATION_ID.toString();
		mNotificationManager.notify(CURRENT_NOTIFICATION_STRING,
				NOTIFICATION_ID, mBuilder.build());
	}

	public void localLog(String label, String msg) {
		if (getENABLE_LOCAL_NOTIFICATION() == 0)
			return;
		Log.i(label, msg);
	}

	public void logoutClpSdk() {
		clpSdkData.setCustomer(new CLPCustomer());
	}

}
