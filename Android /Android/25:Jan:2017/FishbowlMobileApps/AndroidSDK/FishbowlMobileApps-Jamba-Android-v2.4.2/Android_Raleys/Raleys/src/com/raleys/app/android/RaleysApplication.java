package com.raleys.app.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.clp.model.CLPCustomer;
import com.clp.sdk.CLPSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.AccountRequest;
import com.raleys.app.android.models.EcartPreOrderResponse;
import com.raleys.app.android.models.ListRequest;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Offer;
import com.raleys.app.android.models.OfferRequest;
import com.raleys.app.android.models.Product;
import com.raleys.app.android.models.ProductCategoriesRequest;
import com.raleys.app.android.models.ProductCategory;
import com.raleys.app.android.models.ShoppingList;
import com.raleys.app.android.models.ShoppingListName;
import com.raleys.app.android.models.Store;
import com.raleys.gcm.RegisterApp;
import com.raleys.libandroid.LocationTracker;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceError;

public class RaleysApplication extends Application {

	// public static final String SENDER_ID = "955738816549";// Raleys dev
	// public static final String SENDER_ID = "489474086263";// Raleys webqa
	// test
	public static final String SENDER_ID = "1095249547277"; // Raleys prod
	boolean DEBUG = false; // 1 - Debug , 0 - Production

	public final String DOMAIN = "https://www.raleys.com/";
	public final String BASE_URL = "https://www.raleys.com/www-ws/services/";

	// public final String DOMAIN = "https://webqa.vs.raleys.com/";
	// public final String BASE_URL =
	// "https://webqa.vs.raleys.com/www-ws/services/";

	// public final String DOMAIN = "https://webdev.vs.raleys.com/";
	// public final String BASE_URL =
	// "https://webdev.vs.raleys.com/www-ws/services/";

	// public final String DOMAIN = "https://webqa2.vs.raleys.com/";
	// public final String BASE_URL =
	// "https://webqa2.vs.raleys.com/www-ws/services/";

	public final String LOGIN_URL = BASE_URL + "customer/login";
	public final String CHANGE_STORE_URL = BASE_URL
			+ "customer/changeDefaultStore";
	public final String GET_STORES_URL = BASE_URL + "store/getStoreList";

	public final String ACCOUNT_GET_URL = BASE_URL + "customer/getCustomer";
	public final String ACCOUNT_REGISTRATION_URL = BASE_URL
			+ "customer/customerSignUp";
	public final String ACCOUNT_UPDATE_URL = BASE_URL
			+ "customer/customerUpdate";

	public final String ACCEPT_OFFER_URL = BASE_URL + "offer/acceptOffer";
	public final String OFFERS_ACCEPTED_URL = BASE_URL
			+ "offer/getAcceptedOffers";
	public final String OFFERS_PERSONALIZED_URL = BASE_URL
			+ "offer/getPersonalizedOffers";
	public final String OFFERS_EXTRA_FRIENDZY_URL = BASE_URL
			+ "offer/getExtraFriendzyOffers";
	public final String OFFERS_MORE_FOR_YOU_URL = BASE_URL
			+ "offer/getMoreForYouOffers";

	public final String PRODUCT_FOR_UPC_URL = BASE_URL + "item/findByUpc";
	public final String PRODUCT_CATEGORIES_URL = BASE_URL
			+ "category/getMainCategories";
	public final String PROMO_CATEGORIES_URL = BASE_URL
			+ "category/getPromoCategories";
	public final String PRODUCTS_FOR_CATEGORY_URL = BASE_URL
			+ "item/findItemsForCategory";

	public final String PRODUCTS_FOR_PROMO_CATEGORY_URL = BASE_URL
			+ "item/findItemsForPromoCategory";
	public final String PRODUCTS_BY_SEARCH_CATEGORY_URL = BASE_URL
			+ "item/findBySearchText";

	public final String LIST_CREATE_URL = BASE_URL + "list/createList";
	public final String LIST_DELETE_URL = BASE_URL + "list/deleteList";
	public final String LIST_GET_ALL_URL = BASE_URL
			+ "list/findListNamesForCustomer";
	public final String LIST_GET_BY_ID_URL = BASE_URL
			+ "list/getListByAccountIdAndListId";
	public final String LIST_ADD_ITEM_URL = BASE_URL + "list/addItemToList";
	public final String LIST_DELETE_ITEM_URL = BASE_URL
			+ "list/deleteItemFromList";

	public final String ECART_PREORDER_URL = BASE_URL + "order/ecartPreOrder";
	public final String ECART_ORDER_URL = BASE_URL + "order/ecartOrder";

	private static final String PERSISTENT_DATA_FILE_NAME = "persistent.data";
	private static final String PRODUCT_CATEGORIES_FILE_NAME = "product_categories.data";
	private static final String PROMO_CATEGORIES_FILE_NAME = "promo_categories.data";
	private static final int MAX_CACHE_FILES = 3000;
	public final double METERS_PER_MILE = 1609.344;

	// use CA State Capitol as default location when GPS isn't available
	public final double DEFAULT_LATITUDE = 38.577160;
	public final double DEFAULT_LONGITUDE = -121.495560;

	// UI members
	private int _screenWidth;
	private int _screenHeight;
	private int _headerHeight;
	private int _footerHeight;
	private int _navBarHeight;
	private int _deviceType;
	private int _offerThreadCount;
	private int _imageThreadCount;
	private File _productImageDir;
	private File _appBitmapDir;
	private File _contextDir;
	private File _persistentDataFile;
	private File _productCategoriesFile;
	private File _promoCategoriesFile;
	private Typeface _boldFont;
	private Typeface _regularFont;
	private Typeface _fixedFont;

	public ArrayList<Store> _allStoresList;
	public ArrayList<Offer> _acceptedOffersList;
	public ArrayList<Offer> _personalizedOffersList;
	public ArrayList<Offer> _extraFriendzyOffersList;
	public ArrayList<Offer> _moreForYouOffersList;

	private ProductCategory _productCategories;
	private ProductCategory _promoCategories;
	private LocationTracker _locationTracker;
	private PersistentData _persistentData;
	private Object _offerThreadCountLock = new Object();
	// public Location _location;
	public ShoppingList _currentShoppingList;
	public EcartPreOrderResponse _currentEcartPreOrderResponse;
	public Product _currentProductDetail;
	public AccountRequest _currentAccountRequest;
	public Store _storeForAccount;
	public boolean _newLogin;
	public boolean _offersShown;
	public boolean _showShoppingList;
	public boolean _shoppingListChanged;
	public boolean _showProductList;
	public boolean _retrievingShoppingList;
	public boolean _locateForAccount;

	// the following Bitmap objects are scaled from the app's resource directory
	// and stored here to prevent repeated scaling
	public Bitmap _dialogBackground;
	public Bitmap _progressBackground;

	// public Bitmap _categoryPlusBitmap;
	// public Bitmap _categoryMinusBitmap;
	public Bitmap _categoryGoBitmap;
	public Bitmap _categoryForwardBitmap;
	public Bitmap _categoryDownBitmap;

	public Bitmap _productGridCellBitmap;
	public Bitmap _offerGridCellBitmap;
	public Bitmap _offerGridCellAcceptBitmap;
	public Bitmap _offerGridHeaderBitmap;
	public Bitmap _shoppingListGridCellBitmap;
	public Bitmap _shoppingListGridHeaderBitmap;
	public Bitmap _shoppingListGridPromoBitmap;
	public Bitmap _productCategoryButtonBitmap;
	public Bitmap _productCategorySelectedButtonBitmap;
	public Bitmap _productGridPromoBitmap;
	public Bitmap _productListDefaultBitmap;
	public Bitmap _shoppingListDefaultBitmap;
	public Bitmap _locatorListButtonBitmap;
	public Bitmap _storeButtonBitmap;
	public Bitmap _listNamesCellBitmap;
	public Bitmap _listNamesCellSelectedBitmap;

	// Notification variables

	int localNotificationCount = 1000;
	String CURRENT_LOCAL_NOTIFICATION_STRING;

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "GCMRelated";

	public ProductCategory _currentProductCategory;

	Runnable mStatusChecker;

	// Get public context
	public Context _shoppingScreenContext;
	public Context _ProductDetailsScreenContext;

	public Boolean _listItemClick = false;
	// Current Active List Name
	ShoppingListName _currentActiveShoppingListName;

	//
	// Show Item details page variables
	public Boolean _checkChange = false;
	public Context _showItemsPageContext;
	//
	AlertDialog.Builder blueToothAlertBuilder;
	AlertDialog.Builder gpsAlertBuilder;
	public LocationManager locationManager;
	public boolean stopLocationUpdate;
	Thread getAllStoresThread;
	// Newly added for multiple open
	Boolean _offerItemsActivityopen = false;
	Boolean _productItemsActivityopen = false;

	// Push registration
	GoogleCloudMessaging gcm;
	public String regid;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public final String CLP_TRACK_ERROR = "TRACK_ERR";

	// newly added 10.10
	public Boolean _storeLocatorActivityopen = false;
	public Boolean _registerActivityopen = false;

	// newly added in v2.3
	int instructionTextLength = 60;

	public CLPSdk clpsdkObj;

	@Override
	public void onCreate() {
		super.onCreate();

		try {
			// //PERSISTENT FILE///
			_contextDir = getApplicationContext().getFilesDir();
			Log.i(getClass().getSimpleName(), "Context file directory is "
					+ _contextDir.getAbsolutePath());
			_persistentData = new PersistentData(getApplicationContext(),
					PERSISTENT_DATA_FILE_NAME);
			_persistentDataFile = new File(_contextDir.getAbsolutePath()
					+ PERSISTENT_DATA_FILE_NAME);

			if (_persistentDataFile.exists() == false) {
				_persistentDataFile.createNewFile();
				_persistentData._login = new Login();
				_persistentData._currentListId = "";
				_persistentData.save(); // initialiaze the file data
			} else {
				_persistentData.refresh();
			}

			DisplayMetrics displayMetrics = getApplicationContext()
					.getResources().getDisplayMetrics(); // metrics calculated
															// for portrait mode
			_screenWidth = displayMetrics.widthPixels;
			_screenHeight = displayMetrics.heightPixels;
			_deviceType = Utils.getDeviceType(displayMetrics);
			_offerThreadCount = 0;
			_imageThreadCount = 0;
			Log.i(getClass().getSimpleName(), "Initial screen: _screenWidth = "
					+ _screenWidth + ", _screenHeight = " + _screenHeight);
			/*
			 * _boldFont = Typeface.createFromAsset(getAssets(),
			 * "fonts/HelveticaNeueLTStd-Bd.otf"); _regularFont =
			 * Typeface.createFromAsset(getAssets(),
			 * "fonts/HelveticaNeueLTStd-Roman.otf");
			 */

			_boldFont = Typeface.createFromAsset(getAssets(),
					"fonts/Verdana_bold.otf");
			_regularFont = Typeface.createFromAsset(getAssets(),
					"fonts/Verdana.otf");
			_fixedFont = Typeface.createFromAsset(getAssets(),
					"fonts/DroidSansMono.ttf");

			_productImageDir = new File(_contextDir.getAbsolutePath()
					+ "/productImages");

			if (_productImageDir.exists() == false)
				_productImageDir.mkdirs();

			_appBitmapDir = new File(_contextDir.getAbsolutePath()
					+ "/appBitmaps");

			if (_appBitmapDir.exists() == false)
				_appBitmapDir.mkdirs();

			_newLogin = false;
			_offersShown = false;
			_allStoresList = new ArrayList<Store>();
			_acceptedOffersList = new ArrayList<Offer>();
			_personalizedOffersList = new ArrayList<Offer>();
			_extraFriendzyOffersList = new ArrayList<Offer>();
			_moreForYouOffersList = new ArrayList<Offer>();
			_currentProductCategory = new ProductCategory();

			_productCategoriesFile = new File(_contextDir.getAbsolutePath()
					+ PRODUCT_CATEGORIES_FILE_NAME);
			_promoCategoriesFile = new File(_contextDir.getAbsolutePath()
					+ PROMO_CATEGORIES_FILE_NAME);

			if (_productCategoriesFile.exists())
				_productCategories = getCategoriesFromCache(PRODUCT_CATEGORIES_FILE_NAME);

			if (_promoCategoriesFile.exists())
				_promoCategories = getCategoriesFromCache(PROMO_CATEGORIES_FILE_NAME);

			if (_persistentData._storeList != null)
				_allStoresList.addAll(_persistentData._storeList);

			if (!Utils.isNetworkAvailable(getApplicationContext())) {
				Log.e(getClass().getSimpleName(),
						"Internet connection unavailable");
				setCurrentListId("");
				// return;
			}

			if (_productCategoriesFile.exists() == false) {
				_productCategoriesFile.createNewFile();
				_productCategories = new ProductCategory();
				downloadProductCategories(PRODUCT_CATEGORIES_URL);
			}

			if (_promoCategoriesFile.exists() == false) {
				_promoCategoriesFile.createNewFile();
				_promoCategories = new ProductCategory();
			}

			if (isLoggedIn()) {
				getAvailableOffers();
				downloadProductCategories(PROMO_CATEGORIES_URL);

				if (_persistentData._currentListId != null
						&& _persistentData._currentListId.length() > 0)
					getCurrentShoppingList();
			}

			_locationTracker = new LocationTracker(getApplicationContext());
			Raleys.shared(getApplication()).latestLocation = _locationTracker
					.getLocation();

			Raleys.shared(getApplication()).latestLocation = _locationTracker
					.getLocation();
			startAppManagerThread();// periodically download latest offers &
			// products category list
			// clpsdkObj = CLPSdk.sharedInstanceWithKey(
			// this.getApplicationContext(),
			// "5bccfcdc00b2639232feaa75ab73ba1e");// dev
			clpsdkObj = CLPSdk.sharedInstanceWithKey(
					this.getApplicationContext(),
					"ea6635d9e06c5b61fb479dc728f9801a");// production
			clpsdkObj.startLocationService();

			//
			// CLP SDK Opened
			JSONObject data = new JSONObject();
			data.put("event_name", "Opened");
			data.put("event_time", clpsdkObj.formatedCurrentDate());
			clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void enableGPS(boolean isFirstAlert, Context con) {
		if (!isFirstAlert)
			return;
		else {
			LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			boolean providerAvailable = false;
			final List<String> providers = manager.getProviders(true);
			for (final String provider : providers) {
				if (manager.isProviderEnabled(provider)
						&& provider.equals(LocationManager.GPS_PROVIDER)) {
					providerAvailable = true;
				}
			}
			if (providerAvailable) {
				return;
			}
		}
		// showTextDialog(this, getNormalFont(), getNormalFont(),
		// "GPS Settings",
		//
		// "Please switch on the GPS then only you can receive the offer notifications",
		// "yesEnableGPS", "noDonotEnableGPS");

		// ask user to enable gps
		if (_shoppingScreenContext == null || gpsAlertBuilder != null)
			return;
		gpsAlertBuilder = new AlertDialog.Builder(this._shoppingScreenContext,
				AlertDialog.THEME_TRADITIONAL);
		gpsAlertBuilder.setTitle("Alert");
		gpsAlertBuilder
				.setMessage("Please switch on the GPS with High Accuracy Mode then only you can receive the offer notifications");
		gpsAlertBuilder.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					clpsdkObj.setGpsPermission(true);
					clpsdkObj.startLocationService();
					// Turn On GPS if Yes Clicked
					gpsAlertBuilder = null;
					turnGPSOn(dialog);
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					customerRegistration(regid);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		gpsAlertBuilder.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Show Some toast Message
				gpsAlertBuilder = null;
				dialog.dismiss();
			}
		});
		gpsAlertBuilder.show();
	}

	@SuppressWarnings("deprecation")
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void turnGPSOn() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion > 18) {

			Intent mIntent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle mBundle = new Bundle();
			int sdk = android.os.Build.VERSION.SDK_INT;

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

	public Location getCachedLocation() {
		LocationManager manager = (LocationManager) this
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

	/*
	 * public void stopBeacon(){ //beaconManager.stopMonitoring(V15_1);
	 * 
	 * try{ if(beaconManager != null){
	 * 
	 * for (Region beacon : beaconRegionList_ary) {
	 * beaconManager.stopMonitoring(beacon); } } } catch(Exception e){
	 * Log.e("Exception StopBeacon : ", e.getMessage()); } }
	 */

	// simple getters
	public RaleysApplication getApplication() {
		return this;
	}

	public int getScreenWidth() {
		return _screenWidth;
	}

	public int getScreenHeight() {
		return _screenHeight;
	}

	public int getHeaderHeight() {
		return _headerHeight;
	}

	public int getFooterHeight() {
		return _footerHeight;
	}

	public int getNavBarHeight() {
		return _navBarHeight;
	}

	public int getDeviceType() {
		return _deviceType;
	}

	public Typeface getBoldFont() {
		return _boldFont;
	};

	public Typeface getNormalFont() {
		return _regularFont;
	};

	public Typeface getFixedFont() {
		return _fixedFont;
	};

	public File getImageDir() {
		return _productImageDir;
	}

	public List<Store> getStoresList() {
		return _allStoresList;
	}

	public ProductCategory getProductCategories() {
		return _productCategories;
	}

	public ProductCategory getPromoCategories() {
		return _promoCategories;
	}

	// Reset Screen height and Width
	public void resetScreenHeight(Activity activity) {

		DisplayMetrics displayMetrics = getApplicationContext().getResources()
				.getDisplayMetrics(); // metrics calculated
										// for portrait mode
		_screenWidth = displayMetrics.widthPixels;
		_screenHeight = displayMetrics.heightPixels;

	}

	// Check Application is foreground or background
	public boolean isInForeground() {
		try {
			ActivityManager activityManager = (ActivityManager) getApplicationContext()
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> services = activityManager
					.getRunningTasks(Integer.MAX_VALUE);

			if (services.get(0).topActivity
					.getPackageName()
					.toString()
					.equalsIgnoreCase(
							getApplicationContext().getPackageName().toString()))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Uri getNotificationURI(String soundfileNameFn) {

		Uri notificationuri = null;
		if (soundfileNameFn == null || soundfileNameFn == ""
				|| soundfileNameFn.contains("nosound")) {
			return null;// nosound
		}
		if (soundfileNameFn.equals("chime")) {
			notificationuri = Uri.parse("android.resource://"
					+ getPackageName() + "/" + R.raw.chime);
		} else if (soundfileNameFn.equals("bell")) {
			notificationuri = Uri.parse("android.resource://"
					+ getPackageName() + "/" + R.raw.bell);
		} else if (soundfileNameFn.equals("crickets")) {
			notificationuri = Uri.parse("android.resource://"
					+ getPackageName() + "/" + R.raw.crickets);
		} else if (soundfileNameFn.equals("vibrate")) {
			notificationuri = Uri.parse("android.resource://"
					+ getPackageName() + "/" + R.raw.vibrate);
		} else if (soundfileNameFn.equals("tring")) {
			notificationuri = Uri.parse("android.resource://"
					+ getPackageName() + "/" + R.raw.tring);
		} else if (soundfileNameFn.equals("double bell")) {
			notificationuri = Uri.parse("android.resource://"
					+ getPackageName() + "/" + R.raw.double_bell);
		} else {
			notificationuri = null;// default
		}
		return notificationuri;
	}

	public String getCurrentActivity() {

		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager
				.getRunningTasks(1);
		ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
		return ar.topActivity.getClassName().toString();

	}

	public void adjustScreenHeight(Activity activity) {
		int dynamic_width = _screenWidth / 4;
		int org_height = 42;
		int org_width = 82;
		_screenHeight -= Utils.getStatusBarHeight(activity);
		Log.i(getClass().getSimpleName(), "Adjusted screen: _screenWidth = "
				+ _screenWidth + ", _screenHeight = " + _screenHeight);

		if (_deviceType == Utils.DEVICE_PHONE) {

			// _headerHeight=(dynamic_width * org_height )/org_width;
			_headerHeight = (int) (_screenHeight * .09);
			_footerHeight = (int) (_screenHeight * .09);

		} else {
			_headerHeight = (int) (_screenHeight * .08);
			_footerHeight = (int) (_screenHeight * .08);

		}
		_navBarHeight = (dynamic_width * org_height) / org_width;
		// _navBarHeight = (int) (_headerHeight * .8);

	}

	/**
	 * Raleys All Stores list
	 */
	public void getAllStores() {
		try {
			if (getAllStoresThread != null && getAllStoresThread.isAlive()) {
				// thread is already running
				return;
			}
			getAllStoresThread = new Thread() {
				@Override
				public void run() {

					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					HttpConnectionParams.setSoTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					final DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParameters);

					try {
						HttpPost httpPost = new HttpPost(GET_STORES_URL);
						HttpResponse response = httpClient.execute(httpPost);
						StatusLine statusLine = response.getStatusLine();

						if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson gson = new GsonBuilder().disableHtmlEscaping()
									.create();
							Store stores = gson.fromJson(responseString,
									Store.class);

							if (stores != null && stores.storeList.size() > 0)
								_allStoresList.clear();

							for (Store store : stores.storeList) {
								if (store.chain.equalsIgnoreCase("Raley's")
										|| store.chain
												.equalsIgnoreCase("Bel Air")
										|| store.chain
												.equalsIgnoreCase("Nob Hill Foods"))
									_allStoresList.add(store);
							}

							setStoreList(_allStoresList);
							Log.i("getAllStores", "_allStoresList count: "
									+ _allStoresList.size());
						} else if (statusLine.getStatusCode() == 422) // service
																		// error
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson responseGson = new Gson();
							WebServiceError error = responseGson.fromJson(
									responseString, WebServiceError.class);
							Log.e("getAllStores", "Service error: "
									+ error.errorCode + ", "
									+ error.errorMessage);
						} else if (statusLine.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) // 408
						{
							Log.e("getAllStores", "HttpRequest timed out");
						} else {
							Log.e("getAllStores",
									"HttpRequest failed: status = "
											+ statusLine.getStatusCode());
						}
					} catch (Exception ex) {
						Log.e("getAllStores", "makeHttpRequest Exception: "
								+ ex.toString());
					}
				}
			};

			getAllStoresThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private class StoreDistanceComparator implements Comparator<Store> {
		@Override
		public int compare(Store store1, Store store2) {
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

	public ArrayList<Store> getNearestStores(Location currentLocation) {
		if (currentLocation == null)
			return _allStoresList;

		for (Store store : _allStoresList) {
			if (store.latitude.isEmpty() || store.longitude.isEmpty())
				continue;

			try {
				Location storeLocation = new Location("");
				storeLocation.setLatitude(Double.valueOf(store.latitude));
				storeLocation.setLongitude(Double.valueOf(store.longitude));
				store._distanceFromLocation = storeLocation
						.distanceTo(currentLocation);
			} catch (NumberFormatException nfex) {
				nfex.printStackTrace();
			}
		}

		Collections.sort(_allStoresList, new StoreDistanceComparator());
		return _allStoresList;
	}

	/**
	 * This method implements a disk cache for bitmaps scaled from the app's
	 * resource bundle. The purpose for this is to minimize the number of
	 * resource files(ideally one .png file scales to fit all devices). This
	 * method will scale resource files to the device requirements just one time
	 * at app install/update. This alleviates having to repeatedly scale bitmaps
	 * as would be the case when using Android methods like
	 * setBackgroundResource()...
	 * 
	 * @param name
	 *            - the file name assigned to the stored bitmap, ".png" will
	 *            automatically be appended
	 * @param resourceId
	 *            - the resource id from the app's resource bundle
	 * @param width
	 *            - the scaled width of the bitmap
	 * @param height
	 *            - the scaled height of the bitmap
	 */
	public Bitmap getAppBitmap(String name, int resourceId, int width,
			int height) {
		File bitmapFile = new File(_appBitmapDir, name + ".png");
		String imagePath = bitmapFile.getAbsolutePath();
		Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());

		if (bitmap != null) {
			// if an app update changes the dimensions create a new bitmap and
			// overwrite the existing file
			if (bitmap.getWidth() == width && bitmap.getHeight() == height)
				return bitmap;
			else
				Log.i(getClass().getSimpleName(),
						"cached app bitmap " + imagePath
								+ " is wrong size, old = " + bitmap.getWidth()
								+ "x" + bitmap.getHeight() + ", new = " + width
								+ "x" + height);
		}

		try {
			bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			FileOutputStream fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // quality
																	// arg(100)
																	// is
																	// ignored
																	// for PNG
																	// format
			fos.close();
			Log.i(getClass().getSimpleName(), "Created app bitmap file: "
					+ imagePath);
			return bitmap;
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(),
					"Failed to create app bitmap to file: " + imagePath
							+ ": exception: " + ex.toString());
			return null;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// app manager begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void startAppManagerThread() {
		try {
			Thread thread = new Thread() {
				@Override
				public void run() {
					Date now;
					long timeDiff = 0;
					long days = 0;
					long hours = 0;
					long minutes = 0;

					while (true) {
						try {
							Thread.sleep(3600000);// 60 minutes sleep
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						// /download all stores
						if (isLoggedIn() == true) {
							// getAllStores();
						}
						now = new Date();
						Log.d("AppManager", "AppManager thread executing "
								+ DateFormat.format("M/d/yy hh:mm:ss aa", now)
										.toString());

						// check product category cache, update weekly
						if (_productCategories == null
								|| _productCategories.categoryList == null
								|| _productCategories.categoryList.size() == 0) // should
																				// have
																				// this
																				// at
																				// install
						{
							Log.d("AppManager",
									"Initializing product category cache");
							downloadProductCategories(PRODUCT_CATEGORIES_URL);
						} else {
							timeDiff = now.getTime()
									- _persistentData._lastProductCategoryCacheTime;
							days = timeDiff / (24 * 60 * 60 * 1000);
							hours = timeDiff / (60 * 60 * 1000) % 24;
							minutes = timeDiff / (60 * 1000) % 60;
							Log.d("AppManager", "product category cache is "
									+ days + " days, " + hours + " hours, "
									+ minutes + " minutes old");

							if (days >= 7) {
								Log.d("AppManager",
										"Updating product category cache");
								downloadProductCategories(PRODUCT_CATEGORIES_URL);
							}
						}

						// check promo category cache, update daily
						if (_promoCategories == null
								|| _promoCategories.categoryList == null
								|| _promoCategories.categoryList.size() == 0) // should
																				// have
																				// this
																				// at
																				// install
						{
							Log.d("AppManager",
									"Initializing promo category cache");
							downloadProductCategories(PROMO_CATEGORIES_URL);
						} else {
							timeDiff = now.getTime()
									- _persistentData._lastPromoCategoryCacheTime;
							days = timeDiff / (24 * 60 * 60 * 1000);
							hours = timeDiff / (60 * 60 * 1000) % 24;
							minutes = timeDiff / (60 * 1000) % 60;
							Log.d("AppManager", "promo category cache is "
									+ days + " days, " + hours + " hours, "
									+ minutes + " minutes old");

							if (days >= 1) {
								Log.d("AppManager",
										"Updating promo category cache");
								downloadProductCategories(PROMO_CATEGORIES_URL);
							}
						}

						// check offers, update daily
						timeDiff = now.getTime()
								- _persistentData._lastOfferUpdateTime;
						days = timeDiff / (24 * 60 * 60 * 1000);
						hours = timeDiff / (60 * 60 * 1000) % 24;
						minutes = timeDiff / (60 * 1000) % 60;
						Log.d("AppManager", "last offer update was " + days
								+ " days, " + hours + " hours, " + minutes
								+ " minutes ago");

						if (days >= 1 && isLoggedIn()) {
							Log.d("AppManager", "Updating offers");
							getAvailableOffers();
						}

						// check product image cache
						try {
							int productFileCount = 0;
							float productImageBytes = 0;

							File fileList[] = _productImageDir.listFiles();

							if (fileList != null) {
								for (int i = 0; i < fileList.length; i++) {
									File file = fileList[i];

									if (file.length() == 0) {
										file.delete();
										Log.e("AppManager",
												"Deleting zero length file: "
														+ file.getName());
										continue;
									}

									productFileCount++;
									productImageBytes += file.length();
								}
							}

							Arrays.sort(fileList, new Comparator<File>() {
								@Override
								public int compare(File f1, File f2) {
									return Long.valueOf(f1.lastModified())
											.compareTo(f2.lastModified());
								}
							});

							if (productFileCount > MAX_CACHE_FILES) {
								int filesToDelete = productFileCount
										- MAX_CACHE_FILES;
								Log.d("AppManager", "Removing " + filesToDelete
										+ " files from image cache");

								for (int i = 0; i < filesToDelete; i++) {
									File deleteFile = fileList[i];
									// Log.d("AppManager", "Deleting file " +
									// deleteFile.getName() +
									// ", LAST MODIFIED: " +
									// DateFormat.format("M/d/yy hh:mm:ss aa",
									// deleteFile.lastModified()).toString());
									deleteFile.delete();
								}
							}

							int appBitmapCount = 0;
							float appImageBytes = 0;

							File appBitmapList[] = _appBitmapDir.listFiles();

							if (appBitmapList != null) {
								for (int i = 0; i < appBitmapList.length; i++) {
									File file = appBitmapList[i];

									if (file.length() == 0) {
										file.delete();
										Log.e("AppManager",
												"Deleting zero length file: "
														+ file.getName());
										continue;
									}

									appBitmapCount++;
									appImageBytes += file.length();
								}
							}

							productImageBytes = productImageBytes / 1000000;
							appImageBytes = appImageBytes / 1000000;
							Log.d("AppManager",
									"Filesystem: "
											+ (_contextDir.getFreeSpace() / 1000000)
											+ " MBytes available, "
											+ productFileCount
											+ " product bitmap files: "
											+ String.format("%.1f",
													productImageBytes)
											+ " MBytes, "
											+ appBitmapCount
											+ " app bitmap files: "
											+ String.format("%.1f",
													appImageBytes) + " MBytes");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

				}
			};

			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// app manager end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// persistent stuff start
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void logout() {
		_offersShown = false;
		_persistentData._login = new Login();
		_persistentData._currentListId = "";
		_persistentData._Raleys = null;
		_persistentData.account = new AccountRequest();
		_persistentData.save();
		_personalizedOffersList.clear();
		_acceptedOffersList.clear();
		_extraFriendzyOffersList.clear();
		_moreForYouOffersList.clear();
		_currentShoppingList = null;

		try {
			// CLP SDK Opened
			JSONObject data = new JSONObject();
			data.put("event_name", "SignedOut");
			data.put("event_time", clpsdkObj.formatedCurrentDate());
			clpsdkObj.updateAppEvent(data);
			clpsdkObj.logoutClpSdk();
			//
		} catch (Exception e) {
			e.printStackTrace();
		}

		Raleys.shared(getApplication()).reset();

	}

	// tracking functions

	public ArrayList<NameValuePair> getTimeParams() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		String current_time = getCurrentTime();
		params.add(new BasicNameValuePair("time", current_time));
		return params;
	}

	public ArrayList<NameValuePair> getParams(String key, String value) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(key, value));
		return params;
	}

	// Get current time
	public String getCurrentTime() {
		// getting current time

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
		int hourofday = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		// current_time String
		String current_time = "";
		current_time += String.valueOf(month) + "/";
		current_time += String.valueOf(dayofmonth) + "/";
		current_time += String.valueOf(year) + " ";
		current_time += String.valueOf(hourofday) + ":";
		current_time += String.valueOf(minute) + ":";
		current_time += String.valueOf(second);

		return current_time;
	}

	// -

	public void setLogin(Login login) {
		_persistentData._login = login;
		_persistentData.save();
	}

	public boolean isLoggedIn() {
		if (_persistentData._login == null
				|| _persistentData._login.accountId == null
				|| _persistentData._login.crmNumber == null)
			return false;
		else
			return true;
	}

	public Login getLogin() {
		return _persistentData._login;
	}

	public void setStoreList(ArrayList<Store> storeList) {
		if (_persistentData._storeList != null)
			_persistentData._storeList.clear();
		else
			_persistentData._storeList = new ArrayList<Store>();

		_persistentData._storeList = storeList;
		_persistentData.save();
	}

	public ArrayList<Store> getStoreList() {
		return _persistentData._storeList;
	}

	public void setCurrentListId(String listId) {
		_persistentData._currentListId = listId;
		_persistentData.save();
	}

	public String getCurrentListId() {
		return _persistentData._currentListId;
	}

	public void setActiveListId(String listId) {
		_persistentData._activeListId = listId;
		_persistentData.save();
	}

	public String getActiveListId() {
		if (_persistentData._activeListId == null)
			return "";
		return _persistentData._activeListId;
	}

	public void setActiveShoppingList(ShoppingList list) {
		_persistentData._activeShoppingList = list;
		_persistentData.save();
	}

	public ShoppingList getActiveShoppingList() {
		return _persistentData._activeShoppingList;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// persistent stuff end
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// product category stuff start
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void storeProductCategoriesInCache(ProductCategory data, String url) {
		try {
			FileOutputStream fos;
			String fileName;

			if (url.compareTo(PRODUCT_CATEGORIES_URL) == 0) {
				_productCategories = data;
				fileName = PRODUCT_CATEGORIES_FILE_NAME;
			} else // PROMO_CATEGORIES_URL
			{
				_promoCategories = data;
				fileName = PROMO_CATEGORIES_FILE_NAME;
			}

			fos = getApplicationContext().openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(data);
			os.close();
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), ex.toString());
		}
	}

	private ProductCategory getCategoriesFromCache(String fileName) {
		try {
			FileInputStream fis = getApplicationContext().openFileInput(
					fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			ProductCategory productCategory = new ProductCategory();
			productCategory = (ProductCategory) is.readObject();
			is.close();
			return productCategory;
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), ex.toString());
			;
			return null;
		}
	}

	private void downloadProductCategories(String downloadUrl) {
		final String url = downloadUrl;

		try {

			Thread thread = new Thread() {
				@Override
				public void run() {
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					HttpConnectionParams.setSoTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					final DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParameters);
					// final DefaultHttpClient httpClient =
					// Utils.getNewHttpClient(WebService.SERVICE_TIMEOUT); //
					// use this to ignore certificates and comment out the four
					// lines above it

					try {
						HttpPost httpPost = new HttpPost(url);

						if (url.compareTo(PROMO_CATEGORIES_URL) == 0) {
							Login login = getLogin();
							ProductCategoriesRequest request = new ProductCategoriesRequest();
							request.storeNumber = login.storeNumber;
							Gson requestGson = new Gson();
							String requestBody = requestGson.toJson(request);
							httpPost.addHeader("Content-Type",
									"application/json");
							StringEntity stringEntity = new StringEntity(
									requestBody);
							stringEntity.setContentType("application/json");
							httpPost.setEntity(stringEntity);
						}

						HttpResponse response = httpClient.execute(httpPost);
						StatusLine statusLine = response.getStatusLine();

						if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson gson = new GsonBuilder().disableHtmlEscaping()
									.create();
							if (responseString != null) {
								ProductCategory category = gson.fromJson(
										responseString, ProductCategory.class);
								if (category != null) {
									if (url.compareTo(PRODUCT_CATEGORIES_URL) == 0) {
										// Log.i("downloadProductCategories",
										// "category list size = "
										// + category.categoryList
										// .size());
										storeProductCategoriesInCache(category,
												url);
										_persistentData._lastProductCategoryCacheTime = new Date()
												.getTime();
									} else // PROMO_CATEGORIES_URL
									{
										// Log.i("downloadPromoCategories",
										// "category list size = "
										// + category.categoryList
										// .size());
										storeProductCategoriesInCache(category,
												url);
										_persistentData._lastPromoCategoryCacheTime = new Date()
												.getTime();
									}
								}

								_persistentData.save();
							}
						} else if (statusLine.getStatusCode() == 422) // service
																		// error
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson responseGson = new Gson();
							WebServiceError error = responseGson.fromJson(
									responseString, WebServiceError.class);
							Log.e("downloadProductCategories",
									"Service error: " + error.errorCode + ", "
											+ error.errorMessage);
						} else if (statusLine.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) // 408
						{
							Log.e("downloadProductCategories",
									"HttpRequest timed out");
						} else {
							Log.e("downloadProductCategories",
									"HttpRequest failed: status = "
											+ statusLine.getStatusCode());
						}
					} catch (Exception ex) {
						Log.e("downloadProductCategories",
								"makeHttpRequest Exception: " + ex.toString());
					}
				}
			};

			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// product category stuff end
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// offers stuff begin
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void getAvailableOffers() {
		_personalizedOffersList.clear();
		_extraFriendzyOffersList.clear();
		_moreForYouOffersList.clear();
		downloadOffers(OFFERS_PERSONALIZED_URL);
		downloadOffers(OFFERS_EXTRA_FRIENDZY_URL);
		downloadOffers(OFFERS_MORE_FOR_YOU_URL);
		_persistentData._lastOfferUpdateTime = new Date().getTime();
		_persistentData.save();
	}

	public void getAcceptedOffers() {
		_acceptedOffersList.clear();
		downloadOffers(OFFERS_ACCEPTED_URL);
	}

	private void downloadOffers(String offerUrl) {
		final String offerurl = offerUrl;

		try {
			Thread thread = new Thread() {
				@Override
				public void run() {
					synchronized (_offerThreadCountLock) {
						_offerThreadCount++;
					}
					// Log.i(getClass().getSimpleName(), "_offerThreadCount = "
					// + _offerThreadCount);
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					HttpConnectionParams.setSoTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					final DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParameters);
					// final DefaultHttpClient httpClient =
					// Utils.getNewHttpClient(WebService.SERVICE_TIMEOUT); //
					// use this to ignore certificates and comment out the four
					// lines above it

					try {
						OfferRequest request = new OfferRequest();
						request.crmNumber = _persistentData._login.crmNumber;
						Gson requestGson = new Gson();
						String requestBody = requestGson.toJson(request);

						HttpPost httpPost = new HttpPost(offerurl);
						httpPost.addHeader("Content-Type", "application/json");
						StringEntity stringEntity = new StringEntity(
								requestBody);
						stringEntity.setContentType("application/json");
						httpPost.setEntity(stringEntity);
						HttpResponse response = httpClient.execute(httpPost);
						StatusLine statusLine = response.getStatusLine();

						if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
						{

							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Log.i("Request URL: ", offerurl);
							Gson responseGson = new Gson();
							Offer data = responseGson.fromJson(responseString,
									Offer.class);

							if (data != null) {
								synchronized (data) {
									if (offerurl
											.equalsIgnoreCase(OFFERS_ACCEPTED_URL)) {
										_acceptedOffersList.clear();
										Log.i("downloadOffers",
												"_acceptedOffersList size:"
														+ data.offerList.size());
									} else if (offerurl
											.equalsIgnoreCase(OFFERS_PERSONALIZED_URL)) {
										_personalizedOffersList.clear();
										Log.i("downloadOffers",
												"_personalizedOffersList size:"
														+ data.offerList.size());
									} else if (offerurl
											.equalsIgnoreCase(OFFERS_EXTRA_FRIENDZY_URL)) {
										_extraFriendzyOffersList.clear();
										Log.i("downloadOffers",
												"_extraFriendzyOffersList size:"
														+ data.offerList.size());

									} else if (offerurl
											.equalsIgnoreCase(OFFERS_MORE_FOR_YOU_URL)) {
										Log.i("downloadOffers",
												"_specialOffersList size:"
														+ data.offerList.size());
										_moreForYouOffersList.clear();
									}

									for (Offer offer : data.offerList) {
										offer._acceptableOffer = false;
										offer._acceptedOffer = false;

										if (offerurl
												.equalsIgnoreCase(OFFERS_ACCEPTED_URL)) {
											offer._acceptedOffer = true;
											_acceptedOffersList.add(offer);
										} else if (offerurl
												.equalsIgnoreCase(OFFERS_PERSONALIZED_URL)) {
											offer._acceptableOffer = true;
											_personalizedOffersList.add(offer);
										} else if (offerurl
												.equalsIgnoreCase(OFFERS_EXTRA_FRIENDZY_URL)) {
											offer._acceptableOffer = true;
											_extraFriendzyOffersList.add(offer);
										} else if (offerurl
												.equalsIgnoreCase(OFFERS_MORE_FOR_YOU_URL)) {
											_moreForYouOffersList.add(offer);
										}

										getImageAsync(DOMAIN
												+ "images/imagescoupons/"
												+ offer.offerProductImageFile);
									}
								}
							}
						} else if (statusLine.getStatusCode() == 422) // service
																		// error
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Log.i("Request URL: ", offerurl);
							Log.i("Response : ", responseString);
							Gson responseGson = new Gson();
							WebServiceError error = responseGson.fromJson(
									responseString, WebServiceError.class);

							if (error.errorCode != 504)
								Log.e("downloadOffers", offerurl
										+ ": Service error: " + error.errorCode
										+ ", " + error.errorMessage);
						} else if (statusLine.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) // 408
						{
							Log.e("downloadOffers", offerurl
									+ ": HttpRequest timed out");
						} else {
							response.getEntity().getContent().close();
							Log.e("downloadOffers", offerurl
									+ ": HttpRequest failed: status = "
									+ statusLine.getStatusCode());
						}
					} catch (Exception ex) {
						Log.e("downloadOffers", offerurl
								+ ": HttpRequest Exception: " + ex.toString());
					}

					synchronized (_offerThreadCountLock) {
						_offerThreadCount--;
					}
					// Log.i("downloadOffers", "_offerThreadCount = " +
					// _offerThreadCount);
				}
			};

			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean offerThreadsDone() {
		// Log.i(getClass().getSimpleName(), "_offerThreadCount = " +
		// _offerThreadCount + ", _imageThreadCount = " + _imageThreadCount);

		if (_offerThreadCount == 0 && _imageThreadCount == 0)
			return true;
		else
			return false;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// offers stuff end
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// image threading/caching begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Bitmap getCachedImage(String imageURL) {
		final String url = imageURL;
		Bitmap imageBitmap = null;

		try {
			String fileName = url.substring(url.lastIndexOf('/') + 1);
			if (fileName.equalsIgnoreCase("product-image-default-bag-90px.png")) {
				return null;
			}
			File imageFile = new File(_productImageDir, fileName);

			String imagePath = imageFile.getAbsolutePath();

			if (imageFile.exists()) {
				// Log.i("SATAN", "found cached image " + imageURL);
				imageFile.setLastModified(new Date().getTime()); // update the
																	// file's
																	// timestamp
																	// so that
																	// the cache
																	// manager
																	// knows
																	// what
																	// files
																	// have been
																	// used most
																	// recently
				imageBitmap = BitmapFactory.decodeFile(imageFile
						.getAbsolutePath());

				if (imageBitmap == null)
					Log.e(getClass().getSimpleName(),
							"Failed to decode bitmap file: " + imagePath
									+ ", size:" + imageFile.length());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return imageBitmap;
	}

	public void getImageAsync(String imageURL) {
		final String url = imageURL;

		while (_imageThreadCount > 200) {
			// Log.i("RALEYS" + getClass().getSimpleName(),
			// "WAITING: _imageThreadCount = " + _imageThreadCount);
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		try {
			Thread thread = new Thread() {
				@Override
				public void run() {
					synchronized (_offerThreadCountLock) {
						_imageThreadCount++;
					}

					getImageSync(url);

					synchronized (_offerThreadCountLock) {
						_imageThreadCount--;
					}
				}
			};

			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Bitmap getImageSync(String imageURL) {
		Bitmap imageBitmap = getCachedImage(imageURL);
		String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);

		if (imageBitmap != null)
			return imageBitmap;

		final String url = imageURL;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				WebService.SERVICE_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters,
				WebService.SERVICE_TIMEOUT);
		final DefaultHttpClient httpClient = new DefaultHttpClient(
				httpParameters);
		// final DefaultHttpClient httpClient =
		// Utils.getNewHttpClient(WebService.SERVICE_TIMEOUT); // use this to
		// ignore certificates and comment out the four lines above it

		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
			{
				File imageFile = new File(_productImageDir, fileName);
				String imagePath = imageFile.getAbsolutePath();

				try {
					FileOutputStream fos = new FileOutputStream(imageFile);
					response.getEntity().writeTo(fos);
					fos.close();
					// Log.i("getImageSync", "Wrote downloaded bitmap to file: "
					// + imagePath);
				} catch (Exception ex) {
					Log.e("getImageSync", "Failed to write bitmap to file: "
							+ imagePath + ": exception: " + ex.toString());
					return null;
				}

				imageBitmap = BitmapFactory.decodeFile(imageFile
						.getAbsolutePath());

				if (imageBitmap == null)
					Log.e("getImageSync", "Failed to decode bitmap file: "
							+ imagePath + ", size:" + imageFile.length());
			} else if (statusLine.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) // 408
			{
				Log.e("getImageSync", fileName + ". HttpRequest timed out");
			} else if (statusLine.getStatusCode() == HttpStatus.SC_NOT_FOUND) // 404
			{
				// (statusLine.getStatusCode())
				Log.e("getImageSync", fileName + ". Image not found");
			} else {
				response.getEntity().getContent().close();
				Log.e("getImageSync",
						fileName + ". HttpRequest failed: status = "
								+ statusLine.getStatusCode());
			}
		} catch (Exception ex) {
			Log.e("getImageSync", fileName + ". makeHttpRequest Exception: "
					+ ex.toString());
		}

		return imageBitmap;
	}

	public void getProductImages(ArrayList<Product> productList) {
		_imageThreadCount = 0;

		if (productList != null) {
			for (Product product : productList)
				getImageAsync(product.imagePath);
		}
	}

	public boolean imageThreadsDone() {
		// Log.i(getClass().getSimpleName(), "_imageThreadCount = " +
		// _imageThreadCount);

		if (_imageThreadCount == 0)
			return true;
		else
			return false;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// image threading/caching end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void getCurrentShoppingList() {
		try {
			Thread thread = new Thread() {
				@Override
				public void run() {
					_retrievingShoppingList = true;
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					HttpConnectionParams.setSoTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					final DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParameters);

					try {
						ListRequest request = new ListRequest();
						request.accountId = _persistentData._login.accountId;
						request.listId = _persistentData._currentListId;
						request.appListUpdateTime = 0;
						request.returnCurrentList = true;
						Gson requestGson = new Gson();
						String requestBody = requestGson.toJson(request);

						HttpPost httpPost = new HttpPost(LIST_GET_BY_ID_URL);
						httpPost.addHeader("Content-Type", "application/json");
						StringEntity stringEntity = new StringEntity(
								requestBody);
						stringEntity.setContentType("application/json");
						httpPost.setEntity(stringEntity);
						HttpResponse response = httpClient.execute(httpPost);
						StatusLine statusLine = response.getStatusLine();

						if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson gson = new GsonBuilder().disableHtmlEscaping()
									.create();
							_currentShoppingList = gson.fromJson(
									responseString, ShoppingList.class);

							if (_currentShoppingList != null) {
								Log.i("SHOPPING_LIST",
										"_currentShoppingList count: "
												+ _currentShoppingList.productList
														.size()
												+ ", serverUpdateTime = "
												+ _currentShoppingList.serverUpdateTime);
								_persistentData._currentListId = _currentShoppingList.listId;
								_persistentData.save();
							} else {
								Log.i("SHOPPING_LIST",
										"response.productList is null");
								Log.e("getCurrentShoppingList",
										"response.productList is null");
							}
						} else if (statusLine.getStatusCode() == 422) // service
																		// error
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson responseGson = new Gson();
							WebServiceError error = responseGson.fromJson(
									responseString, WebServiceError.class);
							Log.e("getCurrentShoppingList", "Service error: "
									+ error.errorCode + ", "
									+ error.errorMessage);
						} else if (statusLine.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) // 408
						{
							Log.e("getCurrentShoppingList",
									"HttpRequest timed out");
						} else {
							Log.e("getCurrentShoppingList",
									"HttpRequest failed: status = "
											+ statusLine.getStatusCode());
						}
					} catch (Exception ex) {
						Log.e("getCurrentShoppingList",
								"makeHttpRequest Exception: " + ex.toString());
					}

					_retrievingShoppingList = false;
				}
			};

			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getActiveShoppingListAndUpdate(final Context _shopcontext,
			final ShoppingListName listName) {
		try {
			Thread thread = new Thread() {
				@Override
				public void run() {
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					HttpConnectionParams.setSoTimeout(httpParameters,
							WebService.SERVICE_TIMEOUT);
					final DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParameters);

					try {
						ListRequest request = new ListRequest();
						request.accountId = _persistentData._login.accountId;
						request.listId = listName.listId;
						request.appListUpdateTime = 0;
						request.returnCurrentList = true;
						Gson requestGson = new Gson();
						String requestBody = requestGson.toJson(request);

						HttpPost httpPost = new HttpPost(LIST_GET_BY_ID_URL);
						httpPost.addHeader("Content-Type", "application/json");
						StringEntity stringEntity = new StringEntity(
								requestBody);
						stringEntity.setContentType("application/json");
						httpPost.setEntity(stringEntity);
						HttpResponse response = httpClient.execute(httpPost);
						StatusLine statusLine = response.getStatusLine();

						if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson gson = new GsonBuilder().disableHtmlEscaping()
									.create();

							ShoppingList tempShoppingList = gson.fromJson(
									responseString, ShoppingList.class);

							if (tempShoppingList != null) {
								setActiveShoppingList(tempShoppingList);
								_persistentData.save();
							}

							((ShoppingScreen) _shopcontext)
									.confirmChangeActiveList(listName);
						} else if (statusLine.getStatusCode() == 422) // service
																		// error
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson responseGson = new Gson();
							WebServiceError error = responseGson.fromJson(
									responseString, WebServiceError.class);
							Log.e("getCurrentShoppingList", "Service error: "
									+ error.errorCode + ", "
									+ error.errorMessage);
						} else if (statusLine.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) // 408
						{
							Log.e("getCurrentShoppingList",
									"HttpRequest timed out");
						} else {
							Log.e("getCurrentShoppingList",
									"HttpRequest failed: status = "
											+ statusLine.getStatusCode());
						}

					} catch (Exception ex) {
						Log.e("getCurrentShoppingList",
								"makeHttpRequest Exception: " + ex.toString());
					}
					((ShoppingScreen) _shopcontext).changeActiveListUpdate();

				}
			};

			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Beacon methods Block Start
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean isBLESupportedDevice() {

		// GEt current API level of device
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		// Check API level is > than 18
		if ((currentapiVersion > 18)
				&& (getPackageManager()
						.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))) {
			return true;
		}

		return false;
	}

	class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Context... params) {
			final Context context = params[0].getApplicationContext();
			return isAppOnForeground(context);
		}

		private boolean isAppOnForeground(Context context) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}
			final String packageName = context.getPackageName();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& appProcess.processName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
	}

	public void userRegister() {
		if (_persistentData.account == null) {
			getAccountDetail();
		}
	}

	public void getAccountDetail() {
		final Login login = getLogin();
		if (login == null)
			return;

		try {
			Thread thread = new Thread() {
				@Override
				public void run() {
					HttpParams httpParamters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParamters,
							WebService.SERVICE_TIMEOUT);
					HttpConnectionParams.setSoTimeout(httpParamters,
							WebService.SERVICE_TIMEOUT);
					final DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParamters);
					// http method
					HttpPost httpPost = new HttpPost(ACCOUNT_GET_URL);
					httpPost.addHeader("Content-Type", "application/json");
					// request
					AccountRequest request = new AccountRequest();
					request.accountId = login.accountId;
					try {
						Gson requestGson = new Gson();
						String requestBody = requestGson.toJson(request);
						StringEntity stringEntity = new StringEntity(
								requestBody);
						stringEntity.setContentType("application/json");
						httpPost.setEntity(stringEntity);
						HttpResponse response = httpClient.execute(httpPost);
						StatusLine statusLine = response.getStatusLine();
						if (statusLine.getStatusCode() == HttpStatus.SC_OK) // 200
						{
							String responseString = Utils
									.stringFromHttpEntity(response.getEntity());
							Gson gson = new GsonBuilder().disableHtmlEscaping()
									.create();
							AccountRequest responseObject = gson.fromJson(
									responseString, AccountRequest.class);
							_currentAccountRequest = responseObject;
							if (_currentAccountRequest == null)
								return;
							_persistentData.account = _currentAccountRequest;
							// storePersistentData();
						}
					} catch (Exception e) {

					}
				}
			};
			thread.start();
		} catch (Exception e) {
		}
		// userLoginRegister();

	}

	// Persistant methods
	// {

	public Raleys getRaleys() {
		try {
			if (_persistentData._Raleys == null) {
				return null;
			} else {
				return _persistentData._Raleys;
			}
		} catch (Exception e) {
			return null;
		}

	}

	// Store raleys object into persistant storage

	public void storeRaleys() {
		if (Raleys.shared(getApplication()) == null) {
			// retry
		} else {
			_persistentData._Raleys = Raleys.shared(getApplication());
		}
		_persistentData.save();

	}

	// Get all objects stored in persistant

	public PersistentData getPersistentData() {

		return _persistentData;

	}

	// get persistantdata account

	public AccountRequest getPersistendDataAccount() {

		return _persistentData.account;

	}

	// save dataaccount into persistant storage

	public void setPersistentDataAccount(AccountRequest req) {

		_persistentData.account = req;

	}

	// save local server into persistant file

	public void savePersistentData() {

		_persistentData.save();

	}

	// }

	// Check play service

	public boolean checkPlayServices(Activity objActivity) {

		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, objActivity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				objActivity.finish();
			}
			return false;
		}
		return true;
	}

	// Ask permission from user before enabling bluetooth
	public void enableBluetoothIfDisabled(
			final BluetoothAdapter mBluetoothAdapter, boolean firstRequest) {
		if (!isBLESupportedDevice())
			return;
		if (blueToothAlertBuilder != null)
			return;

		blueToothAlertBuilder = new AlertDialog.Builder(
				this._shoppingScreenContext, AlertDialog.THEME_TRADITIONAL);
		blueToothAlertBuilder.setTitle("Alert");
		blueToothAlertBuilder
				.setMessage("Please switch on the Bluetooth then only you can receive the offer notifications");
		blueToothAlertBuilder.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clpsdkObj.setBluetoothPermission(true);
				mBluetoothAdapter.enable();
				blueToothAlertBuilder = null;
			}

		});
		blueToothAlertBuilder.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Show Some toast Message
				dialog.dismiss();
				blueToothAlertBuilder = null;
			}
		});
		blueToothAlertBuilder.show();
	}

	public void registerPushNotification(Activity objActivity, String simpleName) {
		//
		boolean isGooglePlay = checkPlayServices(objActivity);
		if (isGooglePlay) {
			gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
			regid = getRegistrationId(getApplicationContext(), simpleName);

			if (!regid.isEmpty()) {

				Log.i("GCM - REGID", regid);
				// customerRegistration(regid);
			}
			new RegisterApp(getApplicationContext(), gcm, this,
					getAppVersion(getApplicationContext())).execute();

		} else {
			Log.i("Google Play : ", "No valid Google Play Services APK found.");
		}

	}

	// Get Registration / Token ID
	private String getRegistrationId(Context context, String simpleName) {

		final SharedPreferences prefs = getGCMPreferences(context, simpleName);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		Log.i("RegisterID", registrationId);

		if (registrationId.isEmpty()) {
			Log.i("Error: getRegistrationId", "Registration not found.");

			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(getApplicationContext());
		if (registeredVersion != currentVersion) {
			Log.i("Error: getRegistration", "App version changed.");
			return "";
		}
		return registrationId;

	}

	//
	// // Get GCM preferences
	public SharedPreferences getGCMPreferences(Context context,
			String simpleName) {

		return getSharedPreferences(simpleName, Context.MODE_PRIVATE);

	}

	//
	// Get Application Version
	public int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public void customerRegistration(String regid) {
		try {
			CLPCustomer customer = new CLPCustomer();

			AccountRequest account = getPersistendDataAccount();
			if (account != null && account.email != null) {
				customer.firstName = account.firstName;
				customer.lastName = account.lastName;
				customer.emailID = account.email;
				customer.loginID = account.email;
				customer.customerAge = "";
				customer.customerGender = "";
				customer.cellPhone = account.phone;
				customer.loyalityNo = account.loyaltyNumber;
				customer.addressLine1 = account.address;
				customer.addressLine2 = account.address;
				customer.addressCity = account.city;
				customer.addressState = account.state;
				customer.addressZip = account.zip;
				customer.loyalityNo = account.loyaltyNumber;
				customer.favoriteDepartment = account.favoriteDept;
				customer.customerTenantID = account.crmNumber;
				customer.statusCode = 1;
				customer.homeStore = String.valueOf(account.storeNumber);

				if (regid != null)
					customer.pushOpted = "Y";
				else
					customer.pushOpted = "N";
				if (account.sendTextsFlag)
					customer.smsOpted = "Y";
				else
					customer.smsOpted = "N";
				if (account.sendEmailsFlag)
					customer.emailOpted = "Y";
				else
					customer.emailOpted = "N";

				customer.phoneOpted = "N";
				customer.adOpted = "N";
				customer.loyalityRewards = "0";
				customer.pushToken = regid;
				customer.createdBy = account.firstName;
				customer.modifiedBy = account.firstName;

				clpsdkObj.saveCustomer(customer);
			} else {
				Raleys.shared(getApplication()).userRegister();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
