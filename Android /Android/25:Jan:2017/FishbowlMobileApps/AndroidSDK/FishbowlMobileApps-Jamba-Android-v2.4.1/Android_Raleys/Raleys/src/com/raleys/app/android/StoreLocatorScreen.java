package com.raleys.app.android;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.AccountRequest;
import com.raleys.app.android.models.ChangeStoreRequest;
import com.raleys.app.android.models.ListRequest;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.ShoppingList;
import com.raleys.app.android.models.Store;
import com.raleys.libandroid.LocationTracker;
import com.raleys.libandroid.SizedImageButton;
import com.raleys.libandroid.SizedImageTextButton;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SizedTextView;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceError;
import com.raleys.libandroid.WebServiceListener;

public class StoreLocatorScreen extends BaseScreen implements
		WebServiceListener {
	private Context _context;
	private RelativeLayout _locatorLayout;
	private ListView _listView;
	private LocatorListAdapter _listAdapter;
	private LinearLayout _mapView;
	private GoogleMap _map;
	private Marker _marker;
	private LatLng _currentLatLng;
	private ArrayList<Store> _nearestStoresList;
	private Hashtable<String, Store> _markers;
	private SizedImageButton _mapViewButton;
	private SizedImageButton _listViewButton;
	private Login _login;
	private Store _storeToChange;
	private WebService _service;
	private Gson _gson;
	SizedImageTextButton _accountButton;
	SizedImageTextButton _signOutButton;
	MoreMenu _moreMenu;
	SizedImageButton _moreButton;
	RelativeLayout.LayoutParams _menuHiddenLayoutParams;
	RelativeLayout.LayoutParams _menuVisibleLayoutParams;
	private static final int MENU_ANIMATION_DURATION = 100;
	Boolean _registrationPage;
	Boolean dialogOpen = false;
	SharedPreferences _checkPreference;
	Boolean _checkfirstLogin;
	private LocationTracker _locationTracker;

	private Bitmap mapButtonBitmap, listButtonBitmap, mapButtonSelectedBitmap,
			listButtonSelectedBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			_CreateAccountButton.setVisibility(View.GONE); // Hiding Create
															// button
			_login = _app.getLogin();
			_context = this;
			_markers = new Hashtable<String, Store>();
			_gson = new GsonBuilder().disableHtmlEscaping().create();
			_locationTracker = new LocationTracker(getApplicationContext());
			if (_locationTracker != null)
				Raleys.shared(getApplication()).latestLocation = _locationTracker
						.getLocation();
			else {
				Raleys.shared(getApplication()).latestLocation = _app
						.getCachedLocation();
			}
			Intent i = getIntent();
			if (i.hasExtra("registration")) {
				_registrationPage = i.getExtras().getBoolean("registration");
			}
			_mainLayout.setBackgroundColor(Color.LTGRAY);
			// don't use base class content area
			_locatorLayout = new RelativeLayout(_context);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = _headerHeight;
			layoutParams.leftMargin = 0;
			layoutParams.height = _contentViewHeight;
			layoutParams.width = _contentViewWidth;
			_locatorLayout.setBackgroundColor(Color.LTGRAY);
			_mainLayout.addView(_locatorLayout, layoutParams);

			// set up the navigation bar
			Typeface buttonFont = _normalFont;
			setNavBarButtonAppearance(2, buttonFont);
			mapButtonBitmap = _app.getAppBitmap("locator_map_button",
					R.drawable.locator_tab_left, _navBarButtonWidth,
					_navBarStoreLocatorButtonHeight);
			listButtonBitmap = _app.getAppBitmap("locator_list_button",
					R.drawable.locator_tab_right_n, _navBarButtonWidth,
					_navBarStoreLocatorButtonHeight);
			mapButtonSelectedBitmap = _app.getAppBitmap(
					"locator_map_button_selected",
					R.drawable.locator_tab_left_n, _navBarButtonWidth,
					_navBarStoreLocatorButtonHeight);
			listButtonSelectedBitmap = _app.getAppBitmap(
					"locator_list_button_selected",
					R.drawable.locator_tab_right, _navBarButtonWidth,
					_navBarStoreLocatorButtonHeight);

			_mapViewButton = new SizedImageButton(_context, mapButtonBitmap,
					mapButtonSelectedBitmap);

			_mapViewButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mapViewButtonPressed();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 0;
			layoutParams.leftMargin = 0;
			_locatorLayout.addView(_mapViewButton, layoutParams);

			_listViewButton = new SizedImageButton(_context, listButtonBitmap,
					listButtonSelectedBitmap);

			_listViewButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					_checkPreference = getSharedPreferences(_login.accountId,
							Context.MODE_PRIVATE);
					_checkfirstLogin = _checkPreference.getBoolean(
							"STORE_FIRST_LOGIN", true);

					if (_checkfirstLogin == false) {
						listViewButtonPressed();
					} else if (_checkfirstLogin == true) {
						_checkPreference.edit()
								.putBoolean("STORE_FIRST_LOGIN", false)
								.commit();
						if (_registrationPage == false) {
							openInstructionPopUp();
						}
						listViewButtonPressed();
					}
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 0;
			layoutParams.leftMargin = _navBarButtonWidth;
			_locatorLayout.addView(_listViewButton, layoutParams);

			int menuItemHeight = _app.getHeaderHeight();
			// more menu
			int menuItems = 2;
			int menuHeight = menuItems * menuItemHeight;
			int menuWidth = (int) (_contentViewWidth * .375);
			int menuXOrigin = _contentViewWidth - menuWidth;

			_menuHiddenLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			_menuHiddenLayoutParams.topMargin = _headerHeight - menuHeight;
			_menuHiddenLayoutParams.leftMargin = menuXOrigin;

			_menuVisibleLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			_menuVisibleLayoutParams.topMargin = _headerHeight;
			_menuVisibleLayoutParams.leftMargin = menuXOrigin;

			_moreMenu = new MoreMenu(_context, menuWidth, menuItemHeight);
			_moreMenu.setVisibility(View.GONE);
			_mainLayout.addView(_moreMenu, _menuHiddenLayoutParams);

			Handler _newHandler = new Handler();
			_newHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// map
					LayoutInflater inflater = LayoutInflater.from(_context);
					_mapView = (LinearLayout) inflater.inflate(R.layout.map,
							null, false);
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = _navBarStoreLocatorButtonHeight;
					layoutParams.leftMargin = 0;
					layoutParams.height = _contentViewHeight
							- _navBarStoreLocatorButtonHeight;
					layoutParams.width = _contentViewWidth;
					_locatorLayout.addView(_mapView, layoutParams);

					_listView = new ListView(_context);
					if (Raleys.shared(getApplication()).latestLocation != null
							&& (Raleys.shared(getApplication()).latestLocation
									.getLatitude() != _app.DEFAULT_LATITUDE && Raleys
									.shared(getApplication()).latestLocation
									.getLongitude() != _app.DEFAULT_LONGITUDE)) {
						_nearestStoresList = _app.getNearestStores(Raleys
								.shared(getApplication()).latestLocation);
					} else {
						Location cachedLocation = _app.getCachedLocation();
						_nearestStoresList = _app
								.getNearestStores(cachedLocation);// use
																	// cahced
																	// location
																	// for
																	// calculating
																	// the
																	// store
																	// distance

					}
					setMyStoreonTop();

					_locatorLayout.addView(_listView, layoutParams); // uses
																		// same
																		// layout
					// as the map;
					_listView.setVisibility(View.GONE);
					_map = ((MapFragment) getFragmentManager()
							.findFragmentById(R.id.map)).getMap();

					showUserLocation();
					_mapView.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							hideMoreMenu();
							return false;
						}
					});
				}
			}, 1000);

			setContentView(_mainLayout);

			// more button, add to header after content is loaded so
			// menus have
			// slide underneath effect
			int moreButtonSize = (int) (_headerHeight * .5);
			Bitmap moreButtonBitmap = _app.getAppBitmap(
					"shopping_screen_more_button", R.drawable.more_button,
					moreButtonSize * 2, moreButtonSize);
			Bitmap moreButtonSelectedBitmap = _app.getAppBitmap(
					"shopping_screen_more_button_selected",
					R.drawable.more_button_selected, moreButtonSize,
					moreButtonSize);
			_moreButton = new SizedImageButton(_context, moreButtonBitmap,
					moreButtonSelectedBitmap);
			_moreButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					moreButtonPressed();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = _screenWidth - moreButtonSize
			// - (_headerButtonPad * 5);
			layoutParams.leftMargin = _screenWidth - moreButtonSize
					- (_headerButtonPad * 12);
			layoutParams.topMargin = (int) ((_headerHeight - moreButtonSize) * .6);
			// layoutParams.rightMargin = moreButtonSize / _headerButtonPad;
			layoutParams.rightMargin = 0;
			_mainLayout.addView(_moreButton, layoutParams);

			if (_registrationPage == false) {
				_moreButton.setVisibility(View.VISIBLE);
			} else {
				_moreButton.setVisibility(View.GONE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	void moreButtonPressed() {
		if (_moreMenu._isAnimating == true)
			return;

		if (_moreMenu.getVisibility() == View.GONE) {
			clearMenus();
			showMoreMenu();
		} else {
			hideMoreMenu();
		}
	}

	void clearMenus() {
		hideMoreMenu();
	}

	void showMoreMenu() {

		_moreMenu._isAnimating = true;
		_moreMenu.setVisibility(View.VISIBLE);
		TranslateAnimation slideDown = new TranslateAnimation(0, 0, 0,
				_moreMenu.getHeight());
		slideDown.setDuration(MENU_ANIMATION_DURATION);
		slideDown.setFillEnabled(true);
		slideDown.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						_moreMenu.getWidth(), _moreMenu.getHeight());
				layoutParams.setMargins(_menuVisibleLayoutParams.leftMargin,
						_menuVisibleLayoutParams.topMargin, 0, 0);
				_moreMenu.setLayoutParams(layoutParams);
			}
		});

		_moreMenu.startAnimation(slideDown);
	}

	public void showUserLocation() {

		try {

			LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			boolean providerAvailable = false;
			final List<String> providers = manager.getProviders(true);
			for (final String provider : providers) {
				if (manager.isProviderEnabled(provider)
						&& provider.equals(LocationManager.GPS_PROVIDER)) {
					providerAvailable = true;
				}
			}

			if (providerAvailable
					&& (Raleys.shared(getApplication()).latestLocation
							.getLatitude() != _app.DEFAULT_LATITUDE && Raleys
							.shared(getApplication()).latestLocation
							.getLongitude() != _app.DEFAULT_LONGITUDE)) {
				LatLng latlong = new LatLng(
						Raleys.shared(getApplication()).latestLocation
								.getLatitude(),
						Raleys.shared(getApplication()).latestLocation
								.getLongitude());
				CameraUpdate locate = CameraUpdateFactory.newLatLngZoom(
						latlong, 15);
				_map.animateCamera(locate);

			} else {
				// showTextDialog(_context, "",
				// "GPS is needed to calculate your distance from the nearest stores");
				if (!providerAvailable) {
					showTextDialog(
							_context,
							"",
							"GPS with High Accuracy Mode is needed to calculate your distance from the nearest stores",
							"turnGPSOn");
				}
				Location cachedLocation = _app.getCachedLocation();
				if (Raleys.shared(getApplication()).latestLocation != null
						&& (Raleys.shared(getApplication()).latestLocation
								.getLatitude() != _app.DEFAULT_LATITUDE && Raleys
								.shared(getApplication()).latestLocation
								.getLongitude() != _app.DEFAULT_LONGITUDE)) {
					LatLng latlong = new LatLng(
							Raleys.shared(getApplication()).latestLocation
									.getLatitude(),
							Raleys.shared(getApplication()).latestLocation
									.getLongitude());
					CameraUpdate locate = CameraUpdateFactory.newLatLngZoom(
							latlong, 15);
					_map.animateCamera(locate);

				} else if (cachedLocation != null) {
					LatLng latlong = new LatLng(cachedLocation.getLatitude(),
							cachedLocation.getLongitude());
					CameraUpdate locate = CameraUpdateFactory.newLatLngZoom(
							latlong, 15);
					_map.animateCamera(locate);
				} else {
					for (Store store : _nearestStoresList) {
						if (_login.storeNumber == store.storeNumber) {
							LatLng latlong = new LatLng(
									Double.valueOf(store.latitude),
									Double.valueOf(store.longitude));
							CameraUpdate locate = CameraUpdateFactory
									.newLatLngZoom(latlong, 15);
							_map.animateCamera(locate);
							break;
						}
					}
				}
				// Location cachedGPSLocation = manager
				// .getLastKnownLocation(LocationManager.GPS_PROVIDER);//cached
				// gps location
				// Location cachedPASSIVELocation = manager
				// .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);//cached
				// gps/wifi/cellular location
				// Location cachedNETWORKLocation = manager
				// .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//cached
				// wifi/cellular location
				//
				// if (cachedGPSLocation == null && cachedGPSLocation ==null &&
				// cachedNETWORKLocation==null) {
				// //if all the provider donot has cached locations
				// for (Store store : _nearestStoresList) {
				// if (_login.storeNumber == store.storeNumber) {
				// LatLng latlong = new LatLng(
				// Double.valueOf(store.latitude),
				// Double.valueOf(store.longitude));
				// CameraUpdate locate = CameraUpdateFactory
				// .newLatLngZoom(latlong, 15);
				// _map.animateCamera(locate);
				// break;
				// }
				// }
				// } else if(cachedGPSLocation!=null){
				// //show cached gps location
				// LatLng latlong = new LatLng(cachedGPSLocation.getLatitude(),
				// cachedGPSLocation.getLongitude());
				// CameraUpdate locate = CameraUpdateFactory.newLatLngZoom(
				// latlong, 15);
				// _map.animateCamera(locate);
				// }else if(cachedPASSIVELocation!=null){
				// //show cached gps/wifi/cellular location
				// LatLng latlong = new
				// LatLng(cachedPASSIVELocation.getLatitude(),
				// cachedPASSIVELocation.getLongitude());
				// CameraUpdate locate = CameraUpdateFactory.newLatLngZoom(
				// latlong, 15);
				// _map.animateCamera(locate);
				// }else if(cachedNETWORKLocation!=null){
				// //show cached wifi/cellular location
				// LatLng latlong = new
				// LatLng(cachedNETWORKLocation.getLatitude(),
				// cachedNETWORKLocation.getLongitude());
				// CameraUpdate locate = CameraUpdateFactory.newLatLngZoom(
				// latlong, 15);
				// _map.animateCamera(locate);
				// }

			}

		} catch (Exception ex) {

		}
	}

	public void turnGPSOn() {
		dismissTextDialog();
		_app.turnGPSOn();
	}

	private void setMyStoreonTop() {

		Login login = _app.getLogin();
		ArrayList<Store> _nearestTempStoresList1 = new ArrayList<Store>();
		ArrayList<Store> _nearestTempStoresList2 = new ArrayList<Store>();

		int newVar = 0;
		for (int i = 0; i < _nearestStoresList.size(); i++) {
			if (login.storeNumber == _nearestStoresList.get(i).storeNumber) {
				_nearestTempStoresList2.add(0, _nearestStoresList.get(i));
			} else {
				_nearestTempStoresList1.add(newVar, _nearestStoresList.get(i));
				newVar++;
			}
		}

		for (int j = 0; j < _nearestTempStoresList1.size(); j++) {
			_nearestTempStoresList2.add(_nearestTempStoresList1.get(j));
		}

		// _nearestStoresList = new ArrayList<Store>(_nearestTempStoresList2);
		_nearestStoresList = _nearestTempStoresList2;

		_listAdapter = new LocatorListAdapter(this, _nearestStoresList,
				_contentViewWidth, (int) (_contentViewHeight * .1),
				"changeStoreDialog");
		_listView.setAdapter(_listAdapter);

	}

	public void callBackFunction() {

		try {
			BitmapDescriptor redPin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_red_large);
			BitmapDescriptor bluePin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_blue_large);
			BitmapDescriptor goldPin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_gold_large);
			BitmapDescriptor greenPin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_green_large);

			if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
				redPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_red_small);
				bluePin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_blue_small);
				goldPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_gold_small);
				greenPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_green_small);

			} else {
				redPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_red_large);
				bluePin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_blue_large);
				goldPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_gold_large);
				greenPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_green_large);
			}

			if (Raleys.shared(getApplication()).latestLocation != null
					&& (Raleys.shared(getApplication()).latestLocation
							.getLatitude() != _app.DEFAULT_LATITUDE && Raleys
							.shared(getApplication()).latestLocation
							.getLongitude() != _app.DEFAULT_LONGITUDE)) {
				_currentLatLng = new LatLng(
						Raleys.shared(getApplication()).latestLocation
								.getLatitude(),
						Raleys.shared(getApplication()).latestLocation
								.getLongitude());
			} else {
				Location cachedLocation = _app.getCachedLocation();
				if (cachedLocation != null) {
					_currentLatLng = new LatLng(cachedLocation.getLatitude(),
							cachedLocation.getLongitude());// use
					// cahced
					// location
					// for
					// calculating
					// the
					// store
					// distance
				} else {
					_currentLatLng = new LatLng(
							Raleys.shared(getApplication()).latestLocation
									.getLatitude(),
							Raleys.shared(getApplication()).latestLocation
									.getLongitude());// use
														// default
														// location
														// if
														// cached
														// location
														// is
														// not
														// available
				}
			}

			_map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

			_map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker marker) {
					marker.hideInfoWindow();
					Store store = _markers.get(marker.getId());

					if (_login.storeNumber == store.storeNumber)
						return;
					changeStoreDialog(store);
				}
			});

			if (Raleys.shared(getApplication()).latestLocation != null
					&& Raleys.shared(getApplication()).latestLocation
							.getLatitude() != _app.DEFAULT_LATITUDE
					&& Raleys.shared(getApplication()).latestLocation
							.getLongitude() != _app.DEFAULT_LONGITUDE)
				_map.addMarker(new MarkerOptions().position(_currentLatLng)
						.title("Current Location"));

			BitmapDescriptor pin;

			for (Store store : _nearestStoresList) {
				try // separate try block so a single store issue doesn't kill
					// mapping altogether
				{
					if (store.chain.equalsIgnoreCase("Raley's"))
						pin = redPin;
					else if (store.chain.equalsIgnoreCase("Bel Air"))
						pin = bluePin;
					else if (store.chain.equalsIgnoreCase("Nob Hill Foods"))
						pin = goldPin;
					else
						pin = redPin;

					if (_login.storeNumber == store.storeNumber) {
						pin = greenPin;

					}

					if (store.latitude.isEmpty() || store.longitude.isEmpty())
						continue;

					LatLng location = new LatLng(
							Double.valueOf(store.latitude),
							Double.valueOf(store.longitude));
					Marker marker = _map.addMarker(new MarkerOptions()
							.position(location)
							.title(store.chain)
							.snippet(
									store.address + " " + store.city + " "
											+ store.state).icon(pin));
					_markers.put(marker.getId(), store);
				} catch (NumberFormatException nfex) {
					nfex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			showProgressDialog("");
			Handler _stHandler = new Handler();
			_stHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					callBackFunction();
					dismissActiveDialog();
					_app._storeLocatorActivityopen = false;

					if (_listView != null && _listAdapter != null) {
						if (Raleys.shared(getApplication()).latestLocation != null
								&& (Raleys.shared(getApplication()).latestLocation
										.getLatitude() != _app.DEFAULT_LATITUDE && Raleys
										.shared(getApplication()).latestLocation
										.getLongitude() != _app.DEFAULT_LONGITUDE)) {
							_nearestStoresList = _app.getNearestStores(Raleys
									.shared(getApplication()).latestLocation);
						} else {
							Location cachedLocation = _app.getCachedLocation();
							_nearestStoresList = _app
									.getNearestStores(cachedLocation);// use
																		// cahced
																		// location
																		// for
																		// calculating
																		// the
																		// store
																		// distance
						}
						setMyStoreonTop();
						_listAdapter.notifyDataSetChanged();
					}
				}
			}, 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void map_marker() {
		try {
			BitmapDescriptor redPin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_red_large);
			BitmapDescriptor bluePin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_blue_large);
			BitmapDescriptor goldPin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_gold_large);
			BitmapDescriptor greenPin = BitmapDescriptorFactory
					.fromResource(R.drawable.pin_green_large);

			if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
				redPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_red_small);
				bluePin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_blue_small);
				goldPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_gold_small);
				greenPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_green_small);

			} else {
				redPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_red_large);
				bluePin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_blue_large);
				goldPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_gold_large);
				greenPin = BitmapDescriptorFactory
						.fromResource(R.drawable.pin_green_large);
			}

			if (Raleys.shared(getApplication()).latestLocation != null) {
				_currentLatLng = new LatLng(
						Raleys.shared(getApplication()).latestLocation
								.getLatitude(),
						Raleys.shared(getApplication()).latestLocation
								.getLongitude());
			} else {
				_currentLatLng = new LatLng(_app.getCachedLocation()
						.getLatitude(), _app.getCachedLocation().getLongitude());
			}
			_map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

			_map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker marker) {
					marker.hideInfoWindow();
					Store store = _markers.get(marker.getId());

					if (_login.storeNumber == store.storeNumber)
						return;
					changeStoreDialog(store);

				}
			});

			if (Raleys.shared(getApplication()).latestLocation != null
					&& Raleys.shared(getApplication()).latestLocation
							.getLatitude() != _app.DEFAULT_LATITUDE
					&& Raleys.shared(getApplication()).latestLocation
							.getLongitude() != _app.DEFAULT_LONGITUDE)
				_map.addMarker(new MarkerOptions().position(_currentLatLng)
						.title("Current Location"));

			BitmapDescriptor pin;
			_map.clear();

			for (Store store : _nearestStoresList) {
				try // separate try block so a single store issue doesn't kill
					// mapping altogether
				{
					if (store.chain.equalsIgnoreCase("Raley's"))
						pin = redPin;
					else if (store.chain.equalsIgnoreCase("Bel Air"))
						pin = bluePin;
					else if (store.chain.equalsIgnoreCase("Nob Hill Foods"))
						pin = goldPin;
					else
						pin = redPin;

					if (_login.storeNumber == store.storeNumber) {
						pin = greenPin;
					}

					if (store.latitude.isEmpty() || store.longitude.isEmpty())
						continue;

					LatLng location = new LatLng(
							Double.valueOf(store.latitude),
							Double.valueOf(store.longitude));
					Marker marker = _map.addMarker(new MarkerOptions()
							.position(location)
							.title(store.chain)
							.snippet(
									store.address + " " + store.city + " "
											+ store.state).icon(pin));
					_markers.put(marker.getId(), store);
				} catch (NumberFormatException nfex) {
					nfex.printStackTrace();
				}
			}

			// _map.setOnCameraChangeListener(new OnCameraChangeListener() {
			// @Override
			// public void onCameraChange(CameraPosition arg0) {
			// try {
			// LatLng currentLatLng = new LatLng(Raleys
			// .shared(getApplication()).latestLocation
			// .getLatitude(),
			// Raleys.shared(getApplication()).latestLocation
			// .getLongitude());
			// Store store = _nearestStoresList.get(1); // second
			// // closest
			// // store
			// Location closestStoreLocation = new Location("");
			// closestStoreLocation.setLatitude(Double
			// .valueOf(store.latitude));
			// closestStoreLocation.setLongitude(Double
			// .valueOf(store.longitude));
			// int miles = (int) (closestStoreLocation
			// .distanceTo(Raleys.shared(getApplication()).latestLocation) /
			// _app.METERS_PER_MILE);
			//
			// if (miles >= 160) // when this far away just make sure
			// // that the current/default location
			// // and the two closest stores are
			// // visible...
			// {
			// LatLng closestStore = new LatLng(Double
			// .valueOf(store.latitude), Double
			// .valueOf(store.longitude));
			// LatLngBounds.Builder builder = new LatLngBounds.Builder();
			// builder.include(_currentLatLng);
			// builder.include(closestStore);
			//
			// if (_cameraInitialized == false)
			// _map.moveCamera(CameraUpdateFactory
			// .newLatLngBounds(builder.build(),
			// (int) (_contentViewWidth * .8)));
			//
			// return;
			// } else {
			// int zoom;
			//
			// if (miles < 10)
			// zoom = 12;
			// else if (miles < 20)
			// zoom = 11;
			// else if (miles < 40)
			// zoom = 10;
			// else if (miles < 80)
			// zoom = 9;
			// else
			// // < 160
			// zoom = 8;
			//
			// if (_cameraInitialized == false)
			// _map.moveCamera(CameraUpdateFactory
			// .newLatLngZoom(currentLatLng, zoom));
			// }
			//
			// _cameraInitialized = true;
			// } catch (Exception ex) {
			// ex.printStackTrace();
			// }
			// }
			// });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(com.raleys.libandroid.R.anim.slide_in_left,
				com.raleys.libandroid.R.anim.slide_out_right);
	}

	public void mapViewButtonPressed() {

		hideMoreMenu();
		_listView.setVisibility(View.GONE);
		_mapView.setVisibility(View.VISIBLE);
		_listViewButton.setImageBitmap(listButtonBitmap);
		_mapViewButton.setImageBitmap(mapButtonBitmap);
	}

	public void listViewButtonPressed() {
		try {
			hideMoreMenu();
			if (_mapView != null) {
				_mapView.setVisibility(View.GONE);
				_listView.setVisibility(View.VISIBLE);
				_listViewButton.setImageBitmap(listButtonSelectedBitmap);
				_mapViewButton.setImageBitmap(mapButtonSelectedBitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void changeStoreDialog(Store store) {
		hideMoreMenu();
		if (_app._locateForAccount == true) {
			_app._storeForAccount = store;

			finish();
			return;
		}

		_storeToChange = store;
		if (dialogOpen == true) {
			return;
		} else if (dialogOpen == false) {
			dialogOpen = true;
			showTextDialog(_context, "", "Change My Store To "
					+ _storeToChange.chain + " at " + _storeToChange.address
					+ " " + _storeToChange.city + ", " + _storeToChange.state,
					"changeStore", "cancelChange");
		}
	}

	public void changeStore() {

		dialogOpen = false;
		ChangeStoreRequest request = new ChangeStoreRequest();
		request.accountId = _login.accountId;
		request.storeNumber = _storeToChange.storeNumber;
		String requestBody = _gson.toJson(request);

		dismissTextDialog();

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Changing store...");

		_service = new WebService(this, ChangeStoreRequest.class, requestBody,
				_login.authKey);
		_service.execute(_app.CHANGE_STORE_URL);

		// handled by handleChangeStoreServiceResponse() directly below
	}

	public void handleChangeStoreServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {

				_login.storeNumber = _storeToChange.storeNumber;
				_app.setLogin(_login);

				if (Raleys.shared(getApplication()).latestLocation != null
						&& (Raleys.shared(getApplication()).latestLocation
								.getLatitude() != _app.DEFAULT_LATITUDE && Raleys
								.shared(getApplication()).latestLocation
								.getLongitude() != _app.DEFAULT_LONGITUDE)) {
					_nearestStoresList = _app.getNearestStores(Raleys
							.shared(getApplication()).latestLocation);
				} else {
					Location cachedLocation = _app.getCachedLocation();
					_nearestStoresList = _app.getNearestStores(cachedLocation);// use
																				// cahced
																				// location
																				// for
																				// calculating
																				// the
																				// store
																				// distance

				}
				setMyStoreonTop();
				_listAdapter.notifyDataSetChanged();

				ListRequest request = new ListRequest();
				request.accountId = _login.accountId;
				request.listId = _app.getCurrentListId();
				map_marker();
				// CLP SDK - FAV_STORE
				// Change Store - Analytics
				JSONObject data = new JSONObject();
				data.put("store", String.valueOf(_storeToChange.storeNumber));
				data.put("event_name", "FavoriteStoreSelected");
				data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
				_app.clpsdkObj.updateAppEvent(data);
				// CLP SDK End

				if (request.listId == null || request.listId.length() == 0)
					return;

				request.appListUpdateTime = 0;
				request.returnCurrentList = true;
				Log.i("SHOPPING_LIST", "STORE CHANGE: setting list "
						+ request.listId
						+ ", returnCurrentList = true, appListUpdateTime = "
						+ request.appListUpdateTime);
				String requestBody = _gson.toJson(request);

				if (!Utils.isNetworkAvailable(this)) {
					showNetworkUnavailableDialog(this);
					return;
				}

				showProgressDialog("Updating Current List...");
				_service = new WebService(this, ShoppingList.class,
						requestBody, _login.authKey);
				_service.execute(_app.LIST_GET_BY_ID_URL);
				// handled by handleListServiceResponse() below
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Change Store Request Failed",
							error.errorMessage);
				else
					showTextDialog(this, " ", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error", "Http Status code: "
				// + status);

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void handleListServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				_app._currentShoppingList = (ShoppingList) _service
						.getResponseObject();

				if (_app._currentShoppingList == null) {

					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					return;
				}

				_app.setCurrentListId(_app._currentShoppingList.listId);
				_app._shoppingListChanged = true;
				// setFooterDetails();

				if (_listView.getVisibility() == View.VISIBLE)
					_listAdapter.notifyDataSetChanged();
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "List Retrieve Failed",
							error.errorMessage);
				else
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error", "Http Status code: "
				// + status);

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void cancelChange() {
		dialogOpen = false;
		dismissTextDialog();
	}

	// custom class to handle the map's pin overlay
	private class CustomInfoWindowAdapter implements InfoWindowAdapter {
		private Context _context;
		private FrameLayout _view;
		private RelativeLayout _layout;
		private int _width;
		private int _height;

		// SizedTextView _chainTextView;
		SizedTextView _addressTextView;
		SizedTextView _cityStateZipTextView;
		// SizedTextView _ecartTextView;
		// SizedTextView _myStoreTextView;
		SizedImageView _myStoreImageView, _myStoreImageLogo, _ecartImageView;
		// SizedTextView _myStoreImageViewText;
		// protected Object store;
		Bitmap bitmap, bitmap1;

		@SuppressWarnings("deprecation")
		public CustomInfoWindowAdapter(Context context) {
			try {
				_width = (int) (_screenWidth * .7);
				_height = (int) (_screenHeight * .25);
				_context = context;
				_view = new FrameLayout(_context); // frame layout is needed so
													// the relative layout can
													// use alpha, doesn't seem

				_layout = new RelativeLayout(_context);
				_layout.setMinimumWidth(_width);
				_layout.setMinimumHeight(_height);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 0;
				layoutParams.topMargin = 0;
				layoutParams.width = _width;
				layoutParams.height = (int) (_height * 0.5);
				_view.addView(_layout, layoutParams);

				_layout.setBackgroundDrawable(new BitmapDrawable(_app
						.getAppBitmap("store_locator_adapter_background",
								R.drawable.map_dialog, _width, _height)));
				_layout.setAlpha(.9f);

				int textHeight = (int) (_height * .10);

				_addressTextView = new SizedTextView(_context,
						(int) (_width * .7), textHeight, Color.TRANSPARENT,
						Color.WHITE, _boldFont, "  ", Align.LEFT);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int) (_width * .2);
				layoutParams.topMargin = (int) (_height * .09);
				_layout.addView(_addressTextView, layoutParams);

				_cityStateZipTextView = new SizedTextView(_context,
						(int) (_width * .6), textHeight, Color.TRANSPARENT,
						Color.WHITE, _normalFont, "", Align.LEFT);

				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				layoutParams.leftMargin = (int) (_width * .2);
				layoutParams.topMargin = (int) (_height * .20);
				_layout.addView(_cityStateZipTextView, layoutParams);

				int buttonWidth = (int) (_width * .12);
				int buttonHeight = (int) (_width * .12);
				int buttonWidth1 = (int) (_width * .12);

				int buttonHeight1 = (int) (_width * .12);
				_myStoreImageView = new SizedImageView(_context, buttonWidth,
						buttonHeight);
				_myStoreImageView.setImageBitmap(bitmap);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int) (_width * (.86));
				layoutParams.topMargin = (int) (_height * .09);
				_layout.addView(_myStoreImageView, layoutParams);

				// Left Side Logo Place Functions
				_myStoreImageLogo = new SizedImageView(_context, buttonWidth1,
						buttonHeight1);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int) (_width * .15) - buttonWidth1;
				layoutParams.topMargin = (int) (_height * .09);
				_layout.addView(_myStoreImageLogo, layoutParams);

				// Ecart logo
				int ImageSize = buttonWidth1;
				_ecartImageView = new SizedImageView(_context, ImageSize,
						(int) (ImageSize * .5));
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int) (_width * .14)
						- (int) (ImageSize * .5);
				layoutParams.topMargin = (int) (_height * .05);
				_layout.addView(_ecartImageView, layoutParams);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private OnClickListener CustomInfoWindowAdapter(
				CustomInfoWindowAdapter customInfoWindowAdapter) {
			return null;
		}

		@Override
		public View getInfoContents(Marker marker) {
			if (marker.getId().equalsIgnoreCase("m0"))
				return null;

			if (_marker != null && _marker.isInfoWindowShown()) {
				_marker.hideInfoWindow();
				_marker.showInfoWindow();
			}

			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {

			// if (marker.getId().equalsIgnoreCase("m0"))
			// return null;

			_marker = marker;

			try {
				if (marker.getId() != null) {
					final Store store = _markers.get(marker.getId());
					if (store == null) {
						return null;
					}
					String eCartEnabled = store.ecart;
					_addressTextView.changeText(store.address);
					_cityStateZipTextView.changeText(store.city + " "
							+ store.state + " " + store.zip);

					int buttonWidth = (int) (_width * .12);
					int buttonHeight = (int) (_width * .12);

					if (_login.storeNumber == store.storeNumber) {
						_myStoreImageView.setVisibility(View.VISIBLE);
						bitmap1 = _app.getAppBitmap("My_Store",
								R.drawable.product_checked_box, buttonWidth,
								buttonHeight);
						_myStoreImageView.setImageBitmap(bitmap1);

					} else {
						_myStoreImageView.setVisibility(View.VISIBLE);
						bitmap = _app.getAppBitmap("locator_my_store_button",
								R.drawable.map_forward_button, buttonWidth,
								buttonHeight);
						_myStoreImageView.setImageBitmap(bitmap);
					}

					if (store.chain.equalsIgnoreCase("Raley's")) {
						_myStoreImageLogo
								.setBackgroundResource(R.drawable.raley_logo);
					} else if (store.chain.equalsIgnoreCase("Bel Air")) {
						_myStoreImageLogo
								.setBackgroundResource(R.drawable.bel_logo);
					} else if (store.chain.equalsIgnoreCase("Nob Hill Foods")) {
						_myStoreImageLogo
								.setBackgroundResource(R.drawable.nob_logo);
					}

					// E-Cart Logo
					if (eCartEnabled.equals("Yes")) {// E-cart Stores
						_ecartImageView.setVisibility(View.VISIBLE);
						_ecartImageView
								.setBackgroundResource(R.drawable.ecart_button);
					} else if (eCartEnabled.equals("No")) {// Non-Ecart Stores
						_ecartImageView.setVisibility(View.GONE);
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return _view;
		}
	}

	@Override
	public void onServiceResponse(Object responseObject) {
		if (responseObject instanceof ChangeStoreRequest)
			handleChangeStoreServiceResponse();
		else if (responseObject instanceof ShoppingList)
			handleListServiceResponse();
		else if (responseObject instanceof AccountRequest)
			handleAccountServiceResponse();

	}

	@Override
	public void onBackPressed() {
		finish();
		// do nothing, make the user hit the home button to background the app
	}

	public class MoreMenu extends RelativeLayout {
		public boolean _isAnimating;

		public MoreMenu(Context context, int menuWidth, int cellHeight) {
			super(context);

			try {

				Typeface menuFont = _app.getNormalFont();
				int menuViewWidth = menuWidth;
				int menuButtonWidth = menuViewWidth;
				int menuButtonHeight = cellHeight;
				int baseLineHeight = (int) (menuButtonHeight * .05);
				int menuButtonFontSize = Utils.getFontSize(
						(int) (menuButtonWidth * .8),
						(int) (menuButtonHeight * .8), menuFont,
						"Accepted Offers"); // longest text currently used in
											// drop menus
				Log.i("size : ", "" + menuButtonFontSize);
				Bitmap menuButtonTopBitmap = _app.getAppBitmap(
						"more_menu_top_button",
						R.drawable.drop_menu_top_unselected, menuButtonWidth,
						menuButtonHeight);
				Bitmap menuButtonTopSelectedBitmap = _app.getAppBitmap(
						"more_menu_top_selected_button",
						R.drawable.drop_menu_top_selected, menuButtonWidth,
						menuButtonHeight);
				Bitmap menuButtonBottomBitmap = _app.getAppBitmap(
						"more_menu_bottom_button",
						R.drawable.drop_menu_unselected, menuButtonWidth,
						menuButtonHeight);
				Bitmap menuButtonBottomSelectedBitmap = _app.getAppBitmap(
						"more_menu_bottom_selected_button",
						R.drawable.drop_menu_selected, menuButtonWidth,
						menuButtonHeight);

				// recipe button menu
				_accountButton = new SizedImageTextButton(context,
						menuButtonTopBitmap, menuButtonTopSelectedBitmap,
						Color.BLACK, menuFont, menuButtonFontSize, "My Account");
				_accountButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						accountButtonPressed();
					}
				});
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = 0;
				layoutParams.leftMargin = 0;
				addView(_accountButton, layoutParams);

				// _storeLocatorButton = new SizedImageTextButton(context,
				// menuButtonBottomBitmap, menuButtonBottomSelectedBitmap,
				// Color.BLACK, menuFont, menuButtonFontSize,
				// "Store Locator");
				// _storeLocatorButton
				// .setOnClickListener(new View.OnClickListener() {
				// public void onClick(View v) {
				// storeLocatorButtonPressed();
				// }
				// });
				// layoutParams = new RelativeLayout.LayoutParams(
				// RelativeLayout.LayoutParams.WRAP_CONTENT,
				// RelativeLayout.LayoutParams.WRAP_CONTENT);
				// layoutParams.topMargin = menuButtonHeight;
				// layoutParams.leftMargin = 0;
				// addView(_storeLocatorButton, layoutParams);

				_signOutButton = new SizedImageTextButton(context,
						menuButtonBottomBitmap, menuButtonBottomSelectedBitmap,
						Color.BLACK, menuFont, menuButtonFontSize, "Sign Out");
				_signOutButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						signOutButtonPressed();
					}
				});
				layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = menuButtonHeight;
				layoutParams.leftMargin = 0;
				addView(_signOutButton, layoutParams);

				SizedImageView baselineImage = new SizedImageView(context,
						menuViewWidth, baseLineHeight);
				baselineImage.setImageBitmap(_app.getAppBitmap(
						"more_menu_baseline", R.drawable.drop_menu_baseline,
						menuViewWidth, baseLineHeight));
				layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = menuButtonHeight * 2;
				layoutParams.leftMargin = 0;
				addView(baselineImage, layoutParams);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		protected void onAnimationEnd() {
			super.onAnimationEnd();
			_isAnimating = false;
		}
	}

	void accountButtonPressed() {

		if (_service != null && _service._isActive == true)
			return;

		hideMoreMenu();
		Login login = _app.getLogin();
		AccountRequest request = new AccountRequest();
		request.accountId = login.accountId;
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}
		// newsdk
		/*
		 * // CLP SDK try { ArrayList<NameValuePair> data = new
		 * ArrayList<NameValuePair>(); data.add(new BasicNameValuePair("time",
		 * _app.getCurrentTime())); data.add(new
		 * BasicNameValuePair("Event_Name", "My_Account")); } catch (Exception
		 * e) { Log.e(_app.ERROR, "EVENT_CLICK:" + e.getMessage()); } // CLP
		 */showProgressDialog("Retrieving account data...");

		_service = new WebService(this, AccountRequest.class, requestBody,
				_login.authKey);
		_service.execute(_app.ACCOUNT_GET_URL);
		// handled by handleAccountServiceResponse() directly below
	}

	public void handleAccountServiceResponse() {
		try {
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				_app._currentAccountRequest = (AccountRequest) _service
						.getResponseObject();

				if (_app._currentAccountRequest == null) {
					dismissActiveDialog();
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					return;
				}

				Intent intent = new Intent(this, AccountScreen_new.class);
				intent.putExtra("registrationPage", false);
				startActivity(intent);
				dismissActiveDialog();

			} else {
				dismissActiveDialog();
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Account Change Failed",
							error.errorMessage);
				else
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error", "Http Status code: "
				// + status);

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void signOutButtonPressed() {
		hideMoreMenu();
		showTextDialog(this, "Sign Out", "Are you sure you want to sign out?",
				"logoutYes", "logoutNo");
	}

	void hideMoreMenu() {

		try {
			if (_moreMenu.getVisibility() == (View.VISIBLE)) {
				_moreMenu._isAnimating = true;
				TranslateAnimation slideUp = new TranslateAnimation(0, 0, 0,
						-_moreMenu.getHeight());
				slideUp.setDuration(MENU_ANIMATION_DURATION);
				slideUp.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
								_moreMenu.getWidth(), _moreMenu.getHeight());
						layoutParams.setMargins(
								_menuHiddenLayoutParams.leftMargin,
								_menuHiddenLayoutParams.topMargin, 0, 0);
						_moreMenu.setLayoutParams(layoutParams);
					}
				});

				_moreMenu.startAnimation(slideUp);
				_moreMenu.setVisibility(View.GONE);
			}
		} catch (Exception ex) {

		}

	}

	public void logoutYes() {
		dismissTextDialog();
		_app.logout();
		// setFooterDetails();
		Intent intent = new Intent(StoreLocatorScreen.this, LoginScreen.class);
		StoreLocatorScreen.this.startActivity(intent);
		StoreLocatorScreen.this.finish();
	}

	public void logoutNo() {
		dismissTextDialog();
	}

	public void openInstructionPopUp() {

		try {

			// custom dialog
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.MATCH_PARENT;
			dialog.getWindow().setBackgroundDrawableResource(
					R.drawable.map_overlay);
			dialog.getWindow().setAttributes(lp);

			ImageView _mainDialog = new ImageView(this);
			_mainDialog.setBackgroundResource(R.drawable.map_overlay);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = 0;
			layoutParams.height = _app.getScreenHeight();
			layoutParams.width = _app.getScreenWidth();
			dialog.addContentView(_mainDialog, layoutParams);

			dialog.show();

			_mainDialog.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		_locationTracker.stopUsingGPS();
		super.onDestroy();
	}
}
