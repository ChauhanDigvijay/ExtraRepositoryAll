package com.raleys.app.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.AccountRequest;
import com.raleys.app.android.models.EcartPreOrderRequest;
import com.raleys.app.android.models.EcartPreOrderResponse;
import com.raleys.app.android.models.ListAddItemRequest;
import com.raleys.app.android.models.ListCreateRequest;
import com.raleys.app.android.models.ListDeleteItemRequest;
import com.raleys.app.android.models.ListDeleteRequest;
import com.raleys.app.android.models.ListRequest;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Offer;
import com.raleys.app.android.models.OfferAcceptRequest;
import com.raleys.app.android.models.Product;
import com.raleys.app.android.models.ProductCategoriesRequest;
import com.raleys.app.android.models.ProductCategory;
import com.raleys.app.android.models.ProductRequest;
import com.raleys.app.android.models.ProductsForCategoryRequest;
import com.raleys.app.android.models.ProductsForCategoryResponse;
import com.raleys.app.android.models.ShoppingList;
import com.raleys.app.android.models.ShoppingListName;
import com.raleys.app.android.models.Store;
import com.raleys.libandroid.SizedImageButton;
import com.raleys.libandroid.SizedImageTextButton;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SizedTextView;
import com.raleys.libandroid.SmartScrollView;
import com.raleys.libandroid.SmartTextView;
import com.raleys.libandroid.TickDialog;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceError;
import com.raleys.libandroid.WebServiceListener;

public class ShoppingScreen extends BaseScreen implements OnTouchListener,
		OnGestureListener, WebServiceListener, Camera.PreviewCallback {
	private RelativeLayout _categoryLayout;
	private RelativeLayout _productViewLayout;
	private RelativeLayout _categoryMenuLayout;
	private RelativeLayout _scannerLayout;
	private RelativeLayout.LayoutParams _menuHiddenLayoutParams;
	private RelativeLayout.LayoutParams _menuVisibleLayoutParams;
	SmartTextView _productCountTopLabel;
	SmartTextView _productCountBottomLabel;
	private SizedImageButton _moreButton;
	private SizedImageButton _productsButton;
	private SizedImageButton _offersButton;
	private SizedImageButton _listsButton;
	private SizedImageButton _mapButton;
	// private SizedImageButton _showListButton;
	// private SizedImageButton _forwardButton;
	// private SizedImageButton _reverseButton;
	private SizedImageButton _ecartButton;
	private SizedImageTextButton _accountButton;
	private SizedImageTextButton _signOutButton;
	private ListView _offerGridView;
	private OfferGridAdapter _offerGridAdapter;
	private GridView _productGridView;
	private ProductGridAdapter _productGridAdapter;
	private ListView _shoppingListGridView;
	private ShoppingListGridAdapter _shoppingListGridAdapter;
	private SmartScrollView _categoryScrollView;
	private ViewTreeObserver _viewTreeObserver;
	private OnGlobalLayoutListener _layoutListener;
	private WebService _service;
	private Gson _gson;
	private ShoppingMenu _productMenu;
	private ShoppingMenu _offerMenu;
	private ShoppingMenu _listMenu;
	private ShoppingMenu _mapMenu;
	protected ArrayList<ShoppingMenu> _menuList;
	private ArrayList<Product> _currentPageProductList;
	private ArrayList<Product> _multiplePageProductList;
	private ProductCategoryButton _selectedButton;
	// private ProductCategoryButton level2CategoryButton;// new
	// private ProductCategoryButton level3CategoryButton;// new
	private ListCreateView _listCreateView;
	private ListChangeView _listChangeView;
	// private SizedImageView _logButton;
	private Login _login;
	private ProductCategory _currentProductCategory;
	private Product _productToDelete;
	private Product _productToAdd;
	private MoreMenu _moreMenu;
	private EditText _searchText;
	RelativeLayout searchBar;
	private GestureDetector _gestureDetector;
	private Bitmap _productsNormalBitmap;
	private Bitmap _productsSelectedBitmap;
	private Bitmap _offersNormalBitmap;
	private Bitmap _offersSelectedBitmap;
	private Bitmap _listsNormalBitmap;
	private Bitmap _listsSelectedBitmap;
	private Bitmap _mapNormalBitmap;
	private Bitmap _mapSelectedBitmap;
	private String _currentSearchText;
	String _currentCategoryName;
	private int _categoryType;
	private int _productUpdateType;
	private int _scrollViewYOffset;
	private int _selectedButtonYOrigin;
	private int _categoryButtonHeight;
	private int _searchBarHeight;
	private int _currentProductIndex;
	int _currentProductPage;
	int _productPageCount;
	private int _productsInCategoryCount;
	// private long _downTime;
	// private long _upTime;
	private boolean _cancelProductImageUpdate;
	private boolean _validatingEcartList;
	private boolean _showingActiveList;

	private Button _Checkout_button;
	private TextView _ErrorMSG_Text;
	private SizedTextView _No_Accept_Offer_Message;

	private static final int PRODUCT_CATEGORIES = 0;
	private static final int PROMO_CATEGORIES = 1;
	private static final int MENU_ANIMATION_DURATION = 100;
	private static final int PRODUCT_FETCH_LIMIT = 0;// 200(like iOS)
	private static final String SHOW_PRODUCT_DETAIL_CALLBACK = "showProductDetail";
	private static final String SET_ACTIVE_LIST_TEXT = "No active list to add into. Goto lists and make one active";

	// this if for the temporary scanner software
	private Camera _camera;
	private CameraPreview _cameraPreview;
	private Handler _autoFocusHandler;
	private ImageScanner _scanner;
	private boolean _previewing = true;
	private boolean _restartScanner = false;

	// Product Detail View

	int detailType;
	String currentPage;
	Runnable _productrunnable;
	Handler _productHandler;

	static {
		System.loadLibrary("iconv");
	} // for ZBar scanning software

	Context context;
	// GCM
	GoogleCloudMessaging gcm;
	String regid;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	AtomicInteger msgId = new AtomicInteger();
	public static boolean active = true;
	LocationManager locationManager;

	Raleys _raleys;
	String _barcode;

	// shoppingname list
	private RelativeLayout _ShoppingNameListContainer;
	private ListView _listView;
	private ListNameAdapter _listNameAdapter;
	private ShoppingListName _deleteListName;
	RelativeLayout _CreateLayoutContainer;

	public LinearLayout _ShoppingList_Footer_container; // newly added
	public Context maincontext;

	// -- for search functionality{
	private Runnable runnable;
	private Handler handler;

	private boolean forwardButtonPressed;
	Intent intent;
	// -- for search functionality}

	RelativeLayout.LayoutParams layoutParams;

	// menu layout
	RelativeLayout menuBar;
	int menuItemHeight;

	private RelativeLayout ShoppingListTitleContainer;

	// Newly Added for scanner
	Boolean checkFirstTime = true;
	Date firstCall;
	Date intervalCall;
	int firstCallTime;
	int intervalCallTime;
	Boolean scanAlertOpen = true;
	int currInterval;
	String error_message = "";
	Boolean checkkeyboardVisibility = false;

	// for tracking add/update product qty
	float _productQuantity;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			InputMethodManager _inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			_app = (RaleysApplication) getApplication();
			_app.registerPushNotification(this,
					ShoppingScreen.class.getSimpleName());// register for push
															// notification
			_raleys = Raleys.shared(getApplication());
			context = getApplicationContext();
			maincontext = this;
			_CreateAccountButton.setVisibility(View.GONE); // Hiding
															// Create
															// button
			intent = getIntent();
			_multiplePageProductList = new ArrayList<Product>();
			_gestureDetector = new GestureDetector(this, this);
			_gson = new GsonBuilder().disableHtmlEscaping().create();
			_login = _app.getLogin();
			_app._shoppingScreenContext = maincontext;
			_app.enableGPS(true, this);// enable gps
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
				_app.enableBluetoothIfDisabled(mBluetoothAdapter, true);
			}

			if (_app.getStoreList() == null || _app.getStoreList().size() == 0) {
				_app.getAllStores();
			}

			// we don't use the base navigation bar but do use it's
			// button setup
			_menuList = new ArrayList<ShoppingMenu>();
			setNavBarButtonAppearance(4, _normalFont);
			// menu layout
			menuBar = new RelativeLayout(maincontext);
			menuItemHeight = _app.getHeaderHeight();

			// last nav button scales wider to fill the width
			_productsNormalBitmap = _app.getAppBitmap("tab_shop_unselected",
					R.drawable.tab_shop_unselected, _navBarButtonWidth,
					_navBarButtonHeight);
			_productsSelectedBitmap = _app.getAppBitmap("tab_shop_selected",
					R.drawable.tab_shop_selected, _navBarButtonWidth,
					_navBarButtonHeight);
			_offersNormalBitmap = _app.getAppBitmap("tab_offers_unselected",
					R.drawable.tab_offers_unselected, _navBarButtonWidth,
					_navBarButtonHeight);
			_offersSelectedBitmap = _app.getAppBitmap("tab_offers_selected",
					R.drawable.tab_offers_selected, _navBarButtonWidth,
					_navBarButtonHeight);
			_listsNormalBitmap = _app.getAppBitmap("tab_lists_unselected",
					R.drawable.tab_lists_unselected, _navBarLastButtonWidth,
					_navBarButtonHeight);
			_listsSelectedBitmap = _app.getAppBitmap("tab_lists_selected",
					R.drawable.tab_lists_selected, _navBarLastButtonWidth,
					_navBarButtonHeight);
			_mapNormalBitmap = _app.getAppBitmap("tab_map_unselect",
					R.drawable.tab_map_unselect, _navBarLastButtonWidth,
					_navBarButtonHeight);
			_mapSelectedBitmap = _app.getAppBitmap("tab_map_select",
					R.drawable.tab_map_select, _navBarLastButtonWidth,
					_navBarButtonHeight);

			// product menu
			ArrayList<String> productItems = new ArrayList<String>();
			productItems.add("Sale Products:saleButtonPressed");
			productItems.add("All Products:searchButtonPressed");
			productItems.add("Scan:barcodeButtonPressed");

			// sub menu height and width
			int _subMenuWidth = (int) (_navBarButtonWidth * 1.5);
			int _subMenuHeight = menuItemHeight;

			_productMenu = new ShoppingMenu(maincontext, _subMenuWidth,
					_subMenuHeight, productItems, MENU_ANIMATION_DURATION,
					_inputManager);
			_productMenu._hiddenLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_productMenu._hiddenLayoutParams.topMargin = _headerHeight
					+ _navBarHeight - (menuItemHeight * productItems.size());
			_productMenu._hiddenLayoutParams.leftMargin = 0;
			_productMenu._visibleLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_productMenu._visibleLayoutParams.topMargin = _headerHeight
					+ _navBarHeight;
			_productMenu._visibleLayoutParams.leftMargin = 0;
			_mainLayout.addView(_productMenu, _productMenu._hiddenLayoutParams);
			addMenu(_productMenu);
			_productMenu.setVisibility(View.GONE);

			// offer menu
			ArrayList<String> offerItems = new ArrayList<String>();
			offerItems.add("Available Offers:availableOffersButtonPressed");
			offerItems.add("Accepted Offers:acceptedOffersButtonPressed");

			// newsdk
			/*
			 * if (_app.beaconSchedule != null &&
			 * _app.beaconSchedule.enableGoogleOffer == true) {
			 * offerItems.add("SE By Google:googleButtonPressed"); // Google //
			 * SE // disable }
			 */

			_offerMenu = new ShoppingMenu(maincontext, _subMenuWidth,
					_subMenuHeight, offerItems, MENU_ANIMATION_DURATION,
					_inputManager);
			_offerMenu._hiddenLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_offerMenu._hiddenLayoutParams.topMargin = _headerHeight
					+ _navBarHeight - (menuItemHeight * offerItems.size());
			_offerMenu._hiddenLayoutParams.leftMargin = _navBarButtonWidth;
			_offerMenu._visibleLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_offerMenu._visibleLayoutParams.topMargin = _headerHeight
					+ _navBarHeight;
			_offerMenu._visibleLayoutParams.leftMargin = _navBarButtonWidth;
			_mainLayout.addView(_offerMenu, _offerMenu._hiddenLayoutParams);
			addMenu(_offerMenu);
			_offerMenu.setVisibility(View.GONE);

			// list menu
			ArrayList<String> listItems = new ArrayList<String>();
			// listItems.add("Show Active:showActiveList");
			// listItems.add("Set Active:setActiveButtonPressed");
			// listItems.add("Delete Active:deleteActiveButtonPressed");
			// listItems.add("Create New:createNewButtonPressed");
			_listMenu = new ShoppingMenu(maincontext, _subMenuWidth,
					_subMenuHeight, listItems, MENU_ANIMATION_DURATION,
					_inputManager);
			_listMenu._hiddenLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_listMenu._hiddenLayoutParams.topMargin = _headerHeight
					+ _navBarHeight - (menuItemHeight * listItems.size());
			_listMenu._hiddenLayoutParams.leftMargin = _navBarButtonWidth * 2;
			_listMenu._visibleLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_listMenu._visibleLayoutParams.topMargin = _headerHeight
					+ _navBarHeight;
			_listMenu._visibleLayoutParams.leftMargin = _navBarButtonWidth * 2;
			_mainLayout.addView(_listMenu, _listMenu._hiddenLayoutParams);
			addMenu(_listMenu);
			_listMenu.setVisibility(View.GONE);

			// Map menu
			ArrayList<String> mapItems = new ArrayList<String>();

			_mapMenu = new ShoppingMenu(maincontext, _subMenuWidth,
					_subMenuHeight, mapItems, MENU_ANIMATION_DURATION,
					_inputManager);
			_mapMenu._hiddenLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_mapMenu._hiddenLayoutParams.topMargin = _headerHeight
					+ _navBarHeight - (menuItemHeight * mapItems.size());
			_mapMenu._hiddenLayoutParams.leftMargin = _navBarButtonWidth * 3;
			_mapMenu._visibleLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_mapMenu._visibleLayoutParams.topMargin = _headerHeight
					+ _navBarHeight;
			_mapMenu._visibleLayoutParams.leftMargin = _navBarButtonWidth * 3;
			_mainLayout.addView(_mapMenu, _mapMenu._hiddenLayoutParams);
			addMenu(_mapMenu);
			_mapMenu.setVisibility(View.GONE);

			// menu buttons
			// Shop menu
			_productsButton = new SizedImageButton(maincontext,
					_productsNormalBitmap, _productsSelectedBitmap);
			_productsButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (checkkeyboardVisibility == true) {
						hideSoftInputMode();
					}
					productMenuButtonPressed();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 0;
			layoutParams.leftMargin = 0;
			menuBar.addView(_productsButton, layoutParams);

			// Offer Menu
			_offersButton = new SizedImageButton(maincontext,
					_offersNormalBitmap, _offersSelectedBitmap);
			_offersButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (checkkeyboardVisibility == true) {
						hideSoftInputMode();
					}
					offerMenuButtonPressed();
				}
			});
			_offersButton.changeBitmap(_offersSelectedBitmap);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 0;
			layoutParams.leftMargin = _navBarButtonWidth;
			menuBar.addView(_offersButton, layoutParams);

			// List Menu
			_listsButton = new SizedImageButton(maincontext,
					_listsNormalBitmap, _listsSelectedBitmap);
			_listsButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// listMenuButtonPressed();
					if (checkkeyboardVisibility == true) {
						hideSoftInputMode();
					}
					setActiveButtonPressed();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 0;
			layoutParams.leftMargin = _navBarButtonWidth * 2;
			menuBar.setBackgroundColor(Color.rgb(235, 235, 235));
			menuBar.addView(_listsButton, layoutParams);

			// Map Menu
			_mapButton = new SizedImageButton(maincontext, _mapNormalBitmap,
					_mapSelectedBitmap);
			_mapButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// listMenuButtonPressed();
					if (checkkeyboardVisibility == true) {
						hideSoftInputMode();
					}
					storeLocatorButtonPressed();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 0;
			layoutParams.leftMargin = _navBarButtonWidth * 3;
			menuBar.addView(_mapButton, layoutParams);

			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = _headerHeight;
			layoutParams.leftMargin = 0;
			layoutParams.height = _navBarHeight;
			layoutParams.width = _screenWidth;
			_mainLayout.addView(menuBar, layoutParams);

			// more menu
			int menuItems = 2;
			int menuHeight = menuItems * menuItemHeight;
			int menuWidth = (int) (_navBarButtonWidth * 1.5);
			int menuXOrigin = _contentViewWidth - menuWidth;

			_menuHiddenLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_menuHiddenLayoutParams.topMargin = _headerHeight - menuHeight;
			_menuHiddenLayoutParams.leftMargin = menuXOrigin;

			_menuVisibleLayoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_menuVisibleLayoutParams.topMargin = _headerHeight;
			_menuVisibleLayoutParams.leftMargin = menuXOrigin;

			_moreMenu = new MoreMenu(maincontext, menuWidth, menuItemHeight);
			_moreMenu.setVisibility(View.GONE);
			_mainLayout.addView(_moreMenu, _menuHiddenLayoutParams);
			_layoutListener = new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					_categoryScrollView.scrollTo(0, _scrollViewYOffset);
				}
			};
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// dismissActiveDialog();
		// // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//
		// }
		// },0);

		setContentView(_mainLayout);

		try {
			// more button, add to header after content is loaded so menus have
			// slide underneath effect
			int moreButtonSize = (int) (_headerHeight * .5);
			Bitmap moreButtonBitmap = _app.getAppBitmap(
					"shopping_screen_more_button", R.drawable.more_button,
					moreButtonSize * 2, moreButtonSize);
			Bitmap moreButtonSelectedBitmap = _app.getAppBitmap(
					"shopping_screen_more_button_selected",
					R.drawable.more_button_selected, moreButtonSize * 2,
					moreButtonSize);
			_moreButton = new SizedImageButton(maincontext, moreButtonBitmap,
					moreButtonSelectedBitmap);
			_moreButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (checkkeyboardVisibility == true) {
						hideSoftInputMode();
					}
					moreButtonPressed();
				}
			});
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = _screenWidth - moreButtonSize
			// - (_headerButtonPad * 5);
			layoutParams.leftMargin = _screenWidth - moreButtonSize
					- (_headerButtonPad * 12);
			layoutParams.topMargin = (int) ((_headerHeight - moreButtonSize) * .6);
			// layoutParams.rightMargin = moreButtonSize / _headerButtonPad;
			layoutParams.rightMargin = 0;

			_mainLayout.addView(_moreButton, layoutParams);

			// For Back Button
			int backButtonSize = (int) (_headerHeight * .6);

			Bitmap ecartButtonBitmap = _app.getAppBitmap(
					"base_screen_back_button", R.drawable.back_arrow,
					backButtonSize * 2, backButtonSize);

			_ecartButton = new SizedImageButton(maincontext, ecartButtonBitmap);
			_ecartButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// ecartButtonPressed();
					setActiveButtonPressed();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = _headerButtonPad;
			// layoutParams.topMargin = (_headerHeight - ecartButtonHeight) / 2;
			layoutParams.topMargin = (_headerHeight - backButtonSize) / 2;
			layoutParams.leftMargin = _headerButtonPad;
			_mainLayout.addView(_ecartButton, layoutParams);
			_ecartButton.setVisibility(View.GONE);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// When Notification came this block call
		try {

			if (getIntent().hasExtra("clpnid")) {

				JSONObject jsonEvent = new JSONObject();
				jsonEvent.put("event_name", "Notification Opened");
				jsonEvent.put("event_time",
						_app.clpsdkObj.formatedCurrentDate());

				// _app.clpsdkObj.sendAppEvent(jsonEvent);
				_app.clpsdkObj.processPushMessage(intent);

			}
			hideBackButton();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Product double click
		_productHandler = new Handler();
		_productrunnable = new Runnable() {

			@Override
			public void run() {
				if (_app._productItemsActivityopen == true) {
					return;
				} else if (_app._productItemsActivityopen == false) {
					_app._productItemsActivityopen = true;
					hide_product_click();
				}
			}
		};

	}

	public void enableGPS(boolean isFirstAlert, Context con) {
		if (!isFirstAlert) {
			return;
		} else {
			LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				checkBluetoothNeeded();
				return;
			}
		}
		showTextDialog(
				this,
				_normalFont,
				_normalFont,
				"GPS Settings",
				"Please switch on the GPS then only you can receive the offer notifications",
				"yesEnableGPS", "noDoNotEnableGPS");

	}

	private void checkBluetoothNeeded() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()
				&& _app.isBLESupportedDevice()) {
			enableBluetoothIfDisabled();
		}
	}

	public void yesEnableGPS() {
		_app.clpsdkObj.setGpsPermission(true);
		dismissTextDialog();
		checkBluetoothNeeded();
		_app.turnGPSOn();
	}

	public void noDoNotEnableGPS() {
		_app.clpsdkObj.setGpsPermission(false);
		dismissTextDialog();
		checkBluetoothNeeded();
	}

	// Ask permission from user before enabling bluetooth
	public void enableBluetoothIfDisabled() {
		showTextDialog(
				this,
				_normalFont,
				_normalFont,
				"Bluetooth Settings",
				"Please switch on the Bluetooth then only you can receive the offer notifications",
				"yesEnableBluetooth", "noDoNotEnableBluetooth");
	}

	public void yesEnableBluetooth() {
		_app.clpsdkObj.setBluetoothPermission(true);
		dismissTextDialog();
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
	}

	public void noDoNotEnableBluetooth() {
		// _app.clpsdkObj.setBluetoothPermission(false);
		dismissTextDialog();
	}

	public void hide_product_click() {

		_app._shoppingScreenContext = maincontext;
		Intent new_intent = new Intent(this, ShowItemDetails.class);
		new_intent.putExtra("productDetaiType", detailType);
		new_intent.putExtra("currentpage", currentPage);
		this.startActivity(new_intent);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		hideMoreMenu();
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return _gestureDetector.onTouchEvent(event);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (_scannerLayout != null) {
			releaseCamera();
			_scannerLayout.removeView(_cameraPreview);
			_cameraPreview = null;
			if (_scanner != null) {
				_scanner.destroy();
			}
			_scanner = null;
			_contentLayout.removeView(_scannerLayout);
			restoreBackground();
			_scannerLayout = null;
			_restartScanner = true;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// hideBackButton();

		if (_app._retrievingShoppingList == true)
			new WaitForShoppingList().execute();

		// setFooterDetails();

		if (!Utils.isNetworkAvailable(this))
			showNetworkUnavailableDialog(this);

		if (_shoppingListGridView != null && _app._shoppingListChanged == true) // reload
																				// list
																				// when
																				// store
																				// is
																				// changed
																				// in
																				// StoreLocatorScreen
		{
			_app._shoppingListChanged = false;
			showShoppingList();
		}

		if (_app.offerThreadsDone() == false) // should get here when app
												// starts, user is already
												// logged in, and app
												// _newLoginoffer threads are
												// still executing
		{
			showProgressDialog("Searching for your offers...");
			new WaitForOffers(_app.OFFERS_PERSONALIZED_URL).execute();
		} else if (_app._newLogin == true) // should get here right after user
											// logs in
		{
			_app._newLogin = false;
			_app.getAvailableOffers();
			showProgressDialog("Searching for your offers...");
			new WaitForOffers(_app.OFFERS_PERSONALIZED_URL).execute();
		} else if (_app._offersShown == false) {
			showAvailableOffers();
		}

		if (_restartScanner == true) {
			_restartScanner = false;
			showScannerView();
		}
	}

	// This method adds a menu to the menu list. It should be called any time a
	// new menu is instantiated.
	void addMenu(ShoppingMenu menu) {
		boolean found = false;

		synchronized (_menuList) {
			if (_menuList.size() == 0) {
				_menuList.add(menu);
				return;
			}

			for (RelativeLayout layout : _menuList) {
				if (menu == layout) {
					found = true;
					break;
				}
			}

			if (found == false)
				_menuList.add(menu);
		}
	}

	// This method will clear any visible menus.
	void clearMenus() {
		hideMoreMenu();

		for (ShoppingMenu menu : _menuList)
			menu.hideMenu();
	}

	public void setActiveMenu(SizedImageButton button) {
		if (button == _productsButton) {
			button.setImageBitmap(_productsSelectedBitmap);
			_offersButton.setImageBitmap(_offersNormalBitmap);
			_listsButton.setImageBitmap(_listsNormalBitmap);
			_mapButton.setImageBitmap(_mapNormalBitmap);
		} else if (button == _offersButton) {
			button.setImageBitmap(_offersSelectedBitmap);
			_productsButton.setImageBitmap(_productsNormalBitmap);
			_listsButton.setImageBitmap(_listsNormalBitmap);
			_mapButton.setImageBitmap(_mapNormalBitmap);
		} else if (button == _listsButton) {
			button.setImageBitmap(_listsSelectedBitmap);
			_productsButton.setImageBitmap(_productsNormalBitmap);
			_offersButton.setImageBitmap(_offersNormalBitmap);
			_mapButton.setImageBitmap(_mapNormalBitmap);
		} else if (button == _mapButton) {
			button.setImageBitmap(_mapSelectedBitmap);
			_productsButton.setImageBitmap(_productsNormalBitmap);
			_offersButton.setImageBitmap(_offersNormalBitmap);
			_listsButton.setImageBitmap(_listsNormalBitmap);
		}
	}

	@SuppressWarnings("deprecation")
	public void removeViews() {
		try {
			if (_productViewLayout != null) {
				_contentLayout.removeView(_productViewLayout);
				_productViewLayout = null;
			}

			if (_offerGridView != null) {
				_contentLayout.removeView(_offerGridView);
				_offerGridView = null;
			}

			if (_shoppingListGridView != null) {
				_contentLayout.removeView(_shoppingListGridView);
				_shoppingListGridView = null;
			}

			if (_categoryLayout != null) {

				if (checkkeyboardVisibility == true) {
					hideSoftInputMode();
				}
				if (_viewTreeObserver != null && _layoutListener != null) {
					_viewTreeObserver
							.removeGlobalOnLayoutListener(_layoutListener);
				}
				_contentLayout.removeView(_categoryLayout);
				_categoryLayout = null;
			}

			if (_scannerLayout != null) {
				releaseCamera();
				_scannerLayout.removeView(_cameraPreview);
				_cameraPreview = null;
				if (_scanner != null) {
					_scanner.destroy();
				}
				_scanner = null;
				_scannerLayout.removeAllViews();
				_contentLayout.removeView(_scannerLayout);
				restoreBackground();
				_scannerLayout = null;
			}

			// remove shopping name list
			if (_ShoppingNameListContainer != null) {
				_ShoppingNameListContainer.removeAllViews();
				_contentLayout.removeView(_ShoppingNameListContainer);
				_ShoppingNameListContainer = null;
			}

			// remove shopping List Title
			if (ShoppingListTitleContainer != null) {
				ShoppingListTitleContainer.removeAllViews();
				_contentLayout.removeView(ShoppingListTitleContainer);
				ShoppingListTitleContainer = null;
			}

			if (_ShoppingList_Footer_container != null) {
				_ShoppingList_Footer_container.removeAllViews();
				_contentLayout.removeView(_ShoppingList_Footer_container);
				_ShoppingList_Footer_container = null;
			}
			if (_ErrorMSG_Text != null) {
				_contentLayout.removeView(_ErrorMSG_Text);
				_ErrorMSG_Text = null;
			}

			//
			if (_CreateLayoutContainer != null) {
				_CreateLayoutContainer.removeAllViews();
				_contentLayout.removeView(_CreateLayoutContainer);
				_CreateLayoutContainer = null;
			}

			if (_ecartButton != null) {
				_ecartButton.setVisibility(View.GONE);
			}

			if (_No_Accept_Offer_Message != null) { // Accepted offer message
				_contentLayout.removeView(_No_Accept_Offer_Message);
				_No_Accept_Offer_Message = null;
			}
			hideBackButton();

		} catch (Exception ex) {

		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// more menu buttons begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

	void accountButtonPressed() {

		// multiple click avoid
		if (_service != null && _service._isActive == true)
			return;

		hideBackButton();
		hideMoreMenu();
		Login login = _app.getLogin();
		AccountRequest request = new AccountRequest();
		request.accountId = login.accountId;
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}
		try {
			// CLP SDK Menu opened - My Account
			JSONObject data = new JSONObject();
			data.put("event_name", "MenuOpened");
			data.put("link_clicked", "My Account");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		showProgressDialog("Retrieving account data...");

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

	void storeLocatorButtonPressed() {

		hideBackButton();
		hideMoreMenu();
		dismiss_offermenu();

		if (_app.getStoresList() == null || _app.getStoresList().size() == 0) {
			_app.getAllStores();
			showTextDialog(this, "Server Error",
					"Sorry, the store locator is not available at this time. Please try again.");
		} else {
			if (_app._storeLocatorActivityopen == true) {
				return;
			} else if (_app._storeLocatorActivityopen == false) {
				_app._storeLocatorActivityopen = true;
				Intent intent = new Intent(this, StoreLocatorScreen.class);
				intent.putExtra("registration", false);
				startActivity(intent);
			}
		}
		try {
			// CLP SDK Store tab click
			JSONObject data = new JSONObject();
			data.put("event_name", "TabClicked");
			data.put("link_clicked", "Store Locator");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void signOutButtonPressed() {
		hideMoreMenu();
		showTextDialog(this, "Sign Out", "Are you sure you want to sign out?",
				"logoutYes", "logoutNo");
	}

	public void logoutYes() {
		dismissTextDialog();
		_app.logout();
		Intent intent = new Intent(this, LoginScreen.class);
		startActivity(intent);
		finish();
	}

	public void logoutNo() {
		dismissTextDialog();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// more menu buttons end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// product menu buttons begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void productMenuButtonPressed() {

		dismiss_offermenu();

		if (_productMenu._isAnimating == true)
			return;

		if (_productMenu.getVisibility() == View.VISIBLE) {
			_productMenu.hideMenu();
		} else {
			clearMenus();
			_productMenu.showMenu();
		}

		try {
			// CLP SDK Shop tab click
			JSONObject data = new JSONObject();
			data.put("event_name", "TabClicked");
			data.put("link_clicked", "Shop");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saleButtonPressed() {

		try {
			_productMenu.hideMenu();

			// if (_categoryLayout != null && _categoryType == PROMO_CATEGORIES)
			// return;

			removeViews();
			_selectedButton = null;
			setActiveMenu(_productsButton);

			ProductCategory categories = _app.getPromoCategories();

			try {
				// CLP SDK Menu opened - Sales Products
				JSONObject data = new JSONObject();
				data.put("event_name", "MenuOpened");
				data.put("link_clicked", "Sale Products");
				data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
				_app.clpsdkObj.updateAppEvent(data);
				//
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (categories != null && categories.categoryList != null
					&& categories.categoryList.size() != 0) {
				_categoryType = PROMO_CATEGORIES;
				showCategoryView();
			} else {

				_categoryType = PROMO_CATEGORIES;

				Login login = _app.getLogin();
				ProductCategoriesRequest request = new ProductCategoriesRequest();
				request.storeNumber = login.storeNumber;
				String requestBody = _gson.toJson(request);

				if (!Utils.isNetworkAvailable(this)) {
					showNetworkUnavailableDialog(this);
					return;
				}

				showProgressDialog("Retrieving promo categories...");

				_service = new WebService(this, ProductCategory.class,
						requestBody, _login.authKey);
				_service.execute(_app.PROMO_CATEGORIES_URL);
				// handled by handleProductCategoriesServiceResponse() directly
				// below
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleProductCategoriesServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {

				if (_categoryType == PROMO_CATEGORIES) {

					_app.storeProductCategoriesInCache(
							(ProductCategory) _service.getResponseObject(),
							_app.PROMO_CATEGORIES_URL);
					_categoryType = PROMO_CATEGORIES;

				} else if (_categoryType == PRODUCT_CATEGORIES) {

					_app.storeProductCategoriesInCache(
							(ProductCategory) _service.getResponseObject(),
							_app.PRODUCT_CATEGORIES_URL);
					_categoryType = PRODUCT_CATEGORIES;
				}

				showCategoryView();
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Category List Retrieve Failed",
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

	public void searchButtonPressed() {

		try {
			dismiss_offermenu();
			_productMenu.hideMenu();

			// if (_categoryLayout != null && _categoryType ==
			// PRODUCT_CATEGORIES)
			// return;

			removeViews();
			_selectedButton = null;
			setActiveMenu(_productsButton);
			ProductCategory categories = _app.getProductCategories();
			// categories.categoryList.clear();
			if (categories != null && categories.categoryList != null
					&& categories.categoryList.size() != 0) {
				_categoryType = PRODUCT_CATEGORIES;
				showCategoryView();
			} else {

				_categoryType = PRODUCT_CATEGORIES;
				Login login = _app.getLogin();
				ProductCategoriesRequest request = new ProductCategoriesRequest();
				request.storeNumber = login.storeNumber;
				String requestBody = _gson.toJson(request);

				if (!Utils.isNetworkAvailable(this)) {
					showNetworkUnavailableDialog(this);
					return;
				}

				showProgressDialog("Retrieving promo categories...");

				_service = new WebService(this, ProductCategory.class,
						requestBody, _login.authKey);
				_service.execute(_app.PRODUCT_CATEGORIES_URL);
				// handled by handleProductCategoriesServiceResponse() directly
				// below

				try {
					// CLP SDK Menu opened - My Account
					JSONObject data = new JSONObject();
					data.put("event_name", "MenuOpened");
					data.put("link_clicked", "All Products");
					data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
					_app.clpsdkObj.updateAppEvent(data);
					//
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void barcodeButtonPressed() {
		_productMenu.hideMenu();
		removeViews();
		setActiveMenu(_productsButton);
		// newsdk - scan and search menu
		showScannerView();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// product menu buttons end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// offer menu buttons begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void offerMenuButtonPressed() {
		if (_offerMenu._isAnimating == true)
			return;

		if (_offerMenu.getVisibility() == View.VISIBLE) {
			_offerMenu.hideMenu();
		} else {
			clearMenus();
			_offerMenu.showMenu();
		}
		try {
			// CLP SDK Offer tab click
			JSONObject data = new JSONObject();
			data.put("event_name", "TabClicked");
			data.put("link_clicked", "Offer");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Offer Menu Dismiss Function
	public void dismiss_offermenu() {
		_offerMenu.hideMenu();
		_productMenu.hideMenu();
		hideMoreMenu();
	}

	public void availableOffersButtonPressed() {

		try {
			hideBackButton();
			_offerMenu.hideMenu();
			removeViews();
			setActiveMenu(_offersButton);

			if (!Utils.isNetworkAvailable(this)) {
				showNetworkUnavailableDialog(this);
				return;
			}

			// newsdk

			// check that the offers were retrieved on startup
			if (_app._moreForYouOffersList.size() == 0
					|| _app._extraFriendzyOffersList.size() == 0) {
				_app.getAvailableOffers();
				showProgressDialog("Searching for your offers...");
				new WaitForOffers(_app.OFFERS_PERSONALIZED_URL).execute();
			} else {
				showAvailableOffers();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void acceptedOffersButtonPressed() {
		_offerMenu.hideMenu();
		hideBackButton();
		removeViews();
		setActiveMenu(_offersButton);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		// newsdk
		_app.getAcceptedOffers();
		showProgressDialog("Retrieving accepted offers...");
		new WaitForOffers(_app.OFFERS_ACCEPTED_URL).execute();
	}

	public void googleButtonPressed() {
		_offerMenu.hideMenu();
		showTextDialog(this, "Under Construction",
				"Google Offers page coming soon...");
		// removeViews();
		// setActiveMenu(_offersButton);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// offer menu buttons nd
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// shopping list menu buttons begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void listMenuButtonPressed() {
		if (_listMenu._isAnimating == true)
			return;

		if (_listMenu.getVisibility() == View.VISIBLE) {
			_listMenu.hideMenu();
		} else {
			clearMenus();
			_listMenu.showMenu();
		}

	}

	public void setActiveButtonPressed() {
		hideBackButton();
		removeViews();
		_listMenu.hideMenu();
		dismiss_offermenu();
		setActiveMenu(_listsButton);
		setActiveList();
		try {
			// CLP SDK Lists tab click
			JSONObject data = new JSONObject();
			data.put("event_name", "TabClicked");
			data.put("link_clicked", "Lists");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteActiveButtonPressed() {
		_listMenu.hideMenu();
		setActiveMenu(_listsButton);

		String listId = _app.getCurrentListId();

		if (listId == null || listId.length() == 0) {
			showTextDialog(this, "List Error",
					"You do not have a current list to delete.");
			return;
		}

		showTextDialog(this, "Delete List",
				"Are you sure you want to delete list "
						+ _app._currentShoppingList.name + "?", "deleteList",
				"cancelListDelete");
	}

	public void createNewButtonPressed() {
		_listMenu.hideMenu();
		dismiss_offermenu();
		setActiveMenu(_listsButton);
		// createList();
		// newsdk - Create NewList

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// shopping list menu buttons end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// products stuff begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void showCategoryView() {
		try {

			// restoreBackground();

			_categoryLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = _navBarHeight;
			layoutParams.width = _contentViewWidth;
			layoutParams.height = _contentViewHeight - _navBarHeight;
			_contentLayout.addView(_categoryLayout, layoutParams);

			_contentLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (checkkeyboardVisibility == true) {
						hideSoftInputMode();
					}
					dismiss_offermenu();
				}
			});
			_categoryLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (checkkeyboardVisibility == true) {
						hideSoftInputMode();
					}
					dismiss_offermenu();
				}
			});

			_searchBarHeight = _headerHeight;
			int searchTextHeight = (int) (_searchBarHeight * .7);

			// search bar background
			searchBar = new RelativeLayout(this);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = 0;
			layoutParams.width = _contentViewWidth;
			layoutParams.height = _searchBarHeight;
			_categoryLayout.addView(searchBar, layoutParams);

			// search text field
			_searchText = new EditText(this);

			// search - hide menu
			_searchText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					checkkeyboardVisibility = true;
					dismiss_offermenu();
				}
			});

			_searchText.setTextColor(Color.BLACK);
			_searchText.setTypeface(_normalFont);
			_searchText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (searchTextHeight * .42));
			_searchText.setInputType(InputType.TYPE_CLASS_TEXT);
			_searchText.setHint("Search");
			_searchText.setPadding(20, 0, 0, 0);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = (int) (_searchBarHeight * .15);
			layoutParams.leftMargin = (int) (_contentViewWidth * .02);
			layoutParams.rightMargin = (int) (_contentViewWidth * .02);
			layoutParams.width = _contentViewWidth;
			layoutParams.height = (searchTextHeight);
			_searchText.setPadding((int) (searchTextHeight * .1),
					(int) (searchTextHeight * .05), 0, 0);
			_searchText.setBackgroundResource(R.drawable.round_corners);
			_searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
			_searchText.setOnEditorActionListener(new OnEditorActionListener() {
				// Serach Fixes
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						checkkeyboardVisibility = false;
						productSearchButtonPressed(v.getText().toString());
						return true;
					}
					return false;
				}
			});
			searchBar.addView(_searchText, layoutParams);
			searchBar.setBackgroundColor(Color.rgb(178, 178, 178));

			showCategoryMenu();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void showCategoryMenu() {

		int categoryMenuHeight = _contentViewHeight - _navBarHeight
				- _searchBarHeight;
		int categoryMenuYOrigin = _searchBarHeight;
		int yOrigin = 0;
		int level1ProductCategoryId = 0;
		int level2ProductCategoryId = 0;
		boolean level1Expanded = false;
		boolean level2Expanded = false;

		if (_app.getDeviceType() == Utils.DEVICE_PHONE)
			_categoryButtonHeight = (int) (_contentViewHeight * .1);
		else
			_categoryButtonHeight = (int) (_contentViewHeight * .08);

		if (_selectedButton != null) {
			// LogInfo(@"Scroll offset = %d, %d",
			// (int)_categoryScrollView.contentOffset.x,
			// (int)_categoryScrollView.contentOffset.y);
			if (_selectedButton._category.subCategoryList == null
					|| _selectedButton._category.subCategoryList.size() == 0) // the
																				// lowest
																				// category
																				// level,
																				// need
																				// to
																				// display
																				// or
																				// hide
																				// the
																				// table
																				// here
			{
				hideBackButton();
				_currentProductCategory = _selectedButton._category;
				_currentSearchText = null;
				_currentProductIndex = 0;
				_multiplePageProductList.clear();
				showProductsForCategory(_selectedButton._category,
						_currentSearchText, _currentProductIndex, false);
				return;
			}

			// [_categoryMenu.subviews makeObjectsPerformSelector:
			// @selector(removeFromSuperview)]; // remove the previous scroll
			// view's buttons

			if (_selectedButton._category.level == 1) {
				level1ProductCategoryId = _selectedButton._category.productCategoryId;

				if (_selectedButton._expanded == true)
					level1Expanded = true;
			} else if (_selectedButton._category.level == 2) {
				level1ProductCategoryId = _selectedButton._category.parentCategoryId;
				level2ProductCategoryId = _selectedButton._category.productCategoryId;

				if (_selectedButton._expanded == true) {
					level1Expanded = true;
					level2Expanded = true;
				} else {
					level1Expanded = true;
				}
			} else if (_selectedButton._category.level == 3) {
				level1ProductCategoryId = _selectedButton._category.grandParentCategoryId;
				level2ProductCategoryId = _selectedButton._category.parentCategoryId;
				level1Expanded = true;
				level2Expanded = true;
			}
		}

		if (_viewTreeObserver != null) {
			_viewTreeObserver.removeGlobalOnLayoutListener(_layoutListener);
			_viewTreeObserver = null;
		}

		// clear existing menu and start over here
		if (_categoryScrollView != null)
			_categoryScrollView.removeAllViews();

		_categoryLayout.removeView(_categoryScrollView);
		_categoryScrollView = null;
		_categoryScrollView = new SmartScrollView(this);
		_categoryScrollView.setSmoothScrollingEnabled(true);
		_categoryMenuLayout = new RelativeLayout(this);

		_categoryMenuLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkkeyboardVisibility == true) {
					hideSoftInputMode();
				}
				dismiss_offermenu();
			}
		});

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = 0;
		layoutParams.topMargin = categoryMenuYOrigin;
		layoutParams.width = _contentViewWidth;
		layoutParams.height = categoryMenuHeight;
		_categoryScrollView.addView(_categoryMenuLayout, layoutParams);

		ProductCategory categories;

		// Get Product Categories
		if (_categoryType == PRODUCT_CATEGORIES)
			categories = _app.getProductCategories();
		else
			// PROMO_CATEGORIES
			categories = _app.getPromoCategories();

		if (categories.categoryList != null) {// fix added in v2.3
			for (final ProductCategory level1ProductCategory : categories.categoryList) {
				// always create level 1 buttons
				final ProductCategoryButton level1CategoryButton = new ProductCategoryButton(
						this, _contentViewWidth, _categoryButtonHeight,
						_normalFont, level1ProductCategory);
				level1CategoryButton
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								categoryButtonPressed(level1CategoryButton);
							}
						});
				layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = yOrigin;
				layoutParams.leftMargin = 0;
				_categoryMenuLayout.addView(level1CategoryButton, layoutParams);

				// save the location of this button so we can scroll it
				// appropriately after the whole scroll view is constructed
				if (_selectedButton != null
						&& _selectedButton._category.level == 1
						&& level1ProductCategory.productCategoryId == _selectedButton._category.productCategoryId) {
					_selectedButtonYOrigin = yOrigin;
					level1CategoryButton
							.setExpandedState(_selectedButton._expanded);
					level1CategoryButton.invalidate();
				}

				if (level1Expanded == true
						&& level1ProductCategory.productCategoryId == _selectedButton._category.parentCategoryId) {
					level1CategoryButton.setExpandedState(true);
					level1CategoryButton.invalidate();
				}

				yOrigin += _categoryButtonHeight;

				if (_selectedButton != null
						&& level1CategoryButton._expanded == true) {
					if (level1ProductCategoryId == level1ProductCategory.productCategoryId) {
						// create buttons for the second level
						if (level1ProductCategory.subCategoryList != null
								&& level1ProductCategory.subCategoryList.size() > 0) {
							for (final ProductCategory level2ProductCategory : level1ProductCategory.subCategoryList) {
								final ProductCategoryButton level2CategoryButton = new ProductCategoryButton(
										this, _contentViewWidth,
										_categoryButtonHeight, _normalFont,
										level2ProductCategory);

								// //place new
								// // new code{
								// level2CategoryButton._category.parentCategoryId
								// =
								// level1ProductCategoryId;// onsale
								// // fix
								// level2CategoryButton._category.parentCategoryName
								// = level1ProductCategory.name;// onsale
								// // fix
								// // }
								level2CategoryButton
										.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												categoryButtonPressed(level2CategoryButton);
											}
										});
								layoutParams = new RelativeLayout.LayoutParams(
										android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
										android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
								layoutParams.topMargin = yOrigin;
								layoutParams.leftMargin = 0;
								_categoryMenuLayout.addView(
										level2CategoryButton, layoutParams);

								// save the location of this button so we can
								// scroll
								// it appropriately after the whole scroll view
								// is
								// constructed
								if (_selectedButton._category.level == 2
										&& level2ProductCategory.productCategoryId == _selectedButton._category.productCategoryId) {
									_selectedButtonYOrigin = yOrigin;
									level2CategoryButton
											.setExpandedState(_selectedButton._expanded);
									level2CategoryButton.invalidate();
								}
								// place old
								// new code{
								level2CategoryButton._category.parentCategoryId = level1ProductCategoryId;// onsale
								// fix
								level2CategoryButton._category.parentCategoryName = level1ProductCategory.name;// onsale
								// fix
								// }

								yOrigin += _categoryButtonHeight;

								if (level2ProductCategoryId == level2ProductCategory.productCategoryId) {
									if (level2ProductCategory.subCategoryList != null
											&& level2ProductCategory.subCategoryList
													.size() > 0
											&& level2Expanded == true) {
										for (final ProductCategory level3ProductCategory : level2ProductCategory.subCategoryList) {
											final ProductCategoryButton level3CategoryButton = new ProductCategoryButton(
													this, _contentViewWidth,
													_categoryButtonHeight,
													_normalFont,
													level3ProductCategory);
											// //place new
											// // new code{
											// level3CategoryButton._category.parentCategoryId
											// = level2ProductCategoryId;// on
											// // sale
											// // fix
											// level3CategoryButton._category.grandParentCategoryId
											// = level1ProductCategoryId;// on
											// // sale
											// // fix
											// level3CategoryButton._category.grandParentCategoryName
											// = level1ProductCategory.name;//
											// onsale
											// // fix
											// level3CategoryButton._category.parentCategoryName
											// = level2ProductCategory.name;//
											// onsale
											// // fix
											// // new code}
											level3CategoryButton
													.setOnClickListener(new View.OnClickListener() {
														@Override
														public void onClick(
																View v) {
															categoryButtonPressed(level3CategoryButton);
														}
													});
											// place old
											// new code{
											level3CategoryButton._category.parentCategoryId = level2ProductCategoryId;// on
											// sale
											// fix
											level3CategoryButton._category.grandParentCategoryId = level1ProductCategoryId;// on
											// sale
											// fix
											level3CategoryButton._category.grandParentCategoryName = level1ProductCategory.name;
											// onsale
											// fix
											level3CategoryButton._category.parentCategoryName = level2ProductCategory.name;
											// onsale
											// fix
											// new code}
											layoutParams = new RelativeLayout.LayoutParams(
													android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
													android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
											layoutParams.topMargin = yOrigin;
											layoutParams.leftMargin = 0;
											_categoryMenuLayout.addView(
													level3CategoryButton,
													layoutParams);
											yOrigin += _categoryButtonHeight;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (_scrollViewYOffset > _selectedButtonYOrigin)
			_scrollViewYOffset = _selectedButtonYOrigin;

		layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = 0;
		layoutParams.topMargin = categoryMenuYOrigin;
		layoutParams.bottomMargin = 0;
		layoutParams.width = _contentViewWidth;
		layoutParams.height = categoryMenuHeight;
		_categoryScrollView.setBackgroundColor(Color.LTGRAY);
		_categoryLayout.addView(_categoryScrollView, layoutParams);

		_viewTreeObserver = _categoryScrollView.getViewTreeObserver();
		_viewTreeObserver.addOnGlobalLayoutListener(_layoutListener);
	}

	private void categoryButtonPressed(ProductCategoryButton button) {

		dismiss_offermenu();
		if (checkkeyboardVisibility == true) {
			hideSoftInputMode();
		}
		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		_scrollViewYOffset = _categoryScrollView.getYOffset();
		_selectedButton = button;
		_selectedButton.toggleExpandedState();
		showCategoryMenu();

		// CLP SDK Category/Department Click
		try {
			JSONObject data = new JSONObject();
			if (_selectedButton._category.parentCategoryId == 0) {
				if (_categoryType == PROMO_CATEGORIES) {
					data.put("event_name", "CategoryViewed");
				} else {
					data.put("event_name", "DeptViewed");
				}
			} else {
				data.put("event_name", "CategoryViewed");
				data.put("Parent_item_ID", String
						.valueOf(_selectedButton._category.parentCategoryId));
				data.put(
						"Grand_Parent_item_ID",
						String.valueOf(_selectedButton._category.grandParentCategoryId));
				data.put("Parent_item_Name",
						_selectedButton._category.parentCategoryName);
				data.put("Grand_Parent_item_Name",
						_selectedButton._category.grandParentCategoryName);
			}
			// data.put("Click", _selectedButton._category.name);
			// data.put("Category_ID",
			// String.valueOf(_selectedButton._category.productCategoryId));
			// data.put("Parent_Category_ID",
			// String.valueOf(_selectedButton._category.parentCategoryId));
			// data.put("Grand_Parent_Category_ID", String
			// .valueOf(_selectedButton._category.grandParentCategoryId));
			// data.put("Category_Name", _selectedButton._category.name);
			// data.put("Parent_Category_Name",
			// _selectedButton._category.parentCategoryName);
			// data.put("Grand_Parent_Category_Name",
			// _selectedButton._category.parentCategoryName));

			data.put("item_id",
					String.valueOf(_selectedButton._category.productCategoryId));
			data.put("item_name", _selectedButton._category.name);
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);

		} catch (Exception e) {
			Log.e(_app.CLP_TRACK_ERROR, "CATEGORY_CLICK:" + e.getMessage());
		}
		// CLP
	}

	private void productSearchButtonPressed(String com_text) {

		try {
			// CLP SDK Product Searched
			JSONObject data = new JSONObject();
			data.put("search_keyword", com_text);
			data.put("event_name", "Filter");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		_currentSearchText = com_text;
		_currentProductCategory = null;
		_currentProductIndex = 0;
		_multiplePageProductList.clear();

		if (_currentSearchText == null
				|| _currentSearchText.equalsIgnoreCase("")) {
			showTextDialog(this, "Input Error", "Search field can't be blank");
			return;
		}
		showProductsForCategory(_currentProductCategory, _currentSearchText,
				_currentProductIndex, true);

	}

	private void showProductsForCategory(ProductCategory category,
			String searchText, int startIndex, Boolean isSearch) {
		// Tab Double Click avoid
		if (_service != null && _service._isActive == true)
			return;

		if (startIndex == 0) {
			_currentPageProductList = new ArrayList<Product>();
		}
		if (_productsInCategoryCount != 0
				&& startIndex >= _productsInCategoryCount) {
			return;
		}
		ProductsForCategoryRequest request = new ProductsForCategoryRequest();
		request.storeNumber = _login.storeNumber;
		request.startIndex = startIndex;
		request.count = PRODUCT_FETCH_LIMIT;

		if (category != null) {
			_currentCategoryName = category.name;

			if (category.level == 1) {
				request.cat1Id = category.productCategoryId;
			} else if (category.level == 2) {
				request.cat1Id = category.parentCategoryId;
				request.cat2Id = category.productCategoryId;
			} else if (category.level == 3) {
				request.cat1Id = category.grandParentCategoryId;
				request.cat2Id = category.parentCategoryId;
				request.cat3Id = category.productCategoryId;
			}
		} else if (searchText != null) {
			_currentCategoryName = searchText;
			request.searchText = searchText;
		} else {
			Log.e(getClass().getSimpleName(),
					"showProductsForCategory received no parameters");
			return;
		}

		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		if (!isSearch) {
			//
			// try {
			// JSONObject data = new JSONObject();
			// data.put("store", String.valueOf(request.storeNumber));
			// // data.put("PC_Category1ID", String.valueOf(request.cat1Id));
			// // data.put("PC_Category2ID", String.valueOf(request.cat2Id));
			// // data.put("PC_Category3ID", String.valueOf(request.cat3Id));
			// // data.put("PC_StartIndex",
			// // String.valueOf(request.startIndex));
			//
			// // CLP SDK Category Viewed
			// data.put("event_name", "Dept Viewed");
			// data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			// _app.clpsdkObj.sendAppEvent(data);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// // /CLP ANALYTICS END
		}

		showProgressDialog("Retrieving product list...");

		_service = new WebService(this, ProductsForCategoryResponse.class,
				requestBody, _login.authKey);

		// newly edited code{
		String url = new String();
		// for search purpose
		if (_currentSearchText != null
				&& !_currentSearchText.equalsIgnoreCase("")) {
			url = _app.PRODUCTS_BY_SEARCH_CATEGORY_URL;
			// _searchText.setText("");
		} else if (_categoryType == PRODUCT_CATEGORIES) {
			url = _app.PRODUCTS_FOR_CATEGORY_URL;

		} else // PROMO_CATEGORIES
		{
			url = _app.PRODUCTS_FOR_PROMO_CATEGORY_URL;
		}
		_service.execute(url);// _app.PRODUCTS_FOR_CATEGORY_URL
		// new edited code}

		// _service.execute(_app.PRODUCTS_FOR_CATEGORY_URL);
		// handled by handleProductServiceResponse() directly below
	}

	public void handleProductServiceResponse() {
		try {
			int status = _service.getHttpStatusCode();
			_searchText.setText("");
			if (status != 200) {

				WebServiceError error = _service.getError();

				// backend or http error
				if (status == 422) {
					showTextDialog(this, "Product List Retrieve Failed",
							error.errorMessage);
					dismissActiveDialog();
					return;

				} else {
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					dismissActiveDialog();
					return;
				}

			}

			ProductsForCategoryResponse response = (ProductsForCategoryResponse) _service
					.getResponseObject();

			if (response == null || response.productList == null) {
				showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error",
				// "Unable to parse data returned from server.");
				dismissActiveDialog();
				return;
			}

			_productsInCategoryCount = response.productsInCategoryCount;
			_productPageCount = _productsInCategoryCount;
			_currentProductPage = _productPageCount;
			_multiplePageProductList.addAll(_currentPageProductList);
			_currentPageProductList.addAll(response.productList);
			_app._showProductList = true;
			changeProgressText("Retrieving product data...");
			_app.getProductImages(_currentPageProductList);
			new WaitForProductImages().execute();

		} catch (Exception ex) {
			ex.printStackTrace();
			dismissActiveDialog();
		}

	}

	private final void focusOnGridView() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				try {
					_productGridView.setSelection(_currentProductIndex);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// //////////////----------------------//////////////////////
	// this displays the product grid
	public void showProducts() {
		try {
			_categoryLayout.setVisibility(View.GONE);

			if (_productViewLayout != null) {
				_contentLayout.removeView(_productViewLayout);
				_productViewLayout = null;
			}

			// int productHeaderHeight = _categoryButtonHeight;
			int columns;
			float productHeightRatio;

			if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
				columns = 2;
				productHeightRatio = 1.6f;
			} else {
				columns = 2;
				productHeightRatio = 1.6f;
			}

			_productViewLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = _navBarHeight;
			layoutParams.width = _contentViewWidth;
			layoutParams.height = _contentViewHeight - _navBarHeight;
			_productViewLayout.setBackgroundColor(Color.WHITE);
			_contentLayout.addView(_productViewLayout, layoutParams);

			_searchBarHeight = _headerHeight;
			int searchTextHeight = (int) (_searchBarHeight * .7);

			// search bar background
			searchBar = new RelativeLayout(this);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = 0;
			layoutParams.width = _contentViewWidth;
			layoutParams.height = _searchBarHeight;
			_productViewLayout.addView(searchBar, layoutParams);

			// search text field
			_searchText = new EditText(this);

			// search - hide menu
			_searchText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					checkkeyboardVisibility = true;
					dismiss_offermenu();
				}
			});

			_searchText.setTextColor(Color.BLACK);
			_searchText.setTypeface(_normalFont);
			_searchText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (searchTextHeight * .4));
			_searchText.setInputType(InputType.TYPE_CLASS_TEXT);
			_searchText.setHint("Search");
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = (int) (_searchBarHeight * .15);
			layoutParams.leftMargin = (int) (_contentViewWidth * .02);
			layoutParams.rightMargin = (int) (_contentViewWidth * .02);
			layoutParams.width = _contentViewWidth;
			layoutParams.height = (searchTextHeight);
			_searchText.setPadding((int) (searchTextHeight * .1),
					(int) (searchTextHeight * .05), 0, 0);
			_searchText.setBackgroundResource(R.drawable.round_corners);
			_searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
			_searchText.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						checkkeyboardVisibility = false;
						productSearchButtonPressed(v.getText().toString());
						return true;
					}
					return false;
				}
			});
			searchBar.addView(_searchText, layoutParams);

			// // category back button
			try {

				int backButtonSize = (int) (_headerHeight * .6);
				Bitmap backButtonBitmap = _app.getAppBitmap(
						"base_screen_back_button", R.drawable.back_arrow,
						backButtonSize * 2, backButtonSize);
				Bitmap backButtonSelectedBitmap = _app.getAppBitmap(
						"base_screen_back_button_selected",
						R.drawable.back_arrow_selected, backButtonSize * 2,
						backButtonSize);

				if (_backButton != null) {
					_backButton.setVisibility(View.GONE);

				}
				_backButton = new SizedImageButton(this, backButtonBitmap,
						backButtonSelectedBitmap);
				_backButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						removeProductView();
					}
				});

				layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = (_headerHeight - backButtonSize) / 2;
				layoutParams.leftMargin = 0;
				layoutParams.height = _contentViewHeight;
				layoutParams.width = _contentViewWidth;

				_mainLayout.addView(_backButton, layoutParams);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			int cellSpacing = (int) (_contentViewWidth * .005);
			int cellSize = (_contentViewWidth - (cellSpacing * (columns + 1)))
					/ columns;
			int productGridCellHeight = (int) (cellSize * productHeightRatio);

			_productGridView = new GridView(this);
			_productGridView.setNumColumns(columns);
			_productGridView.setColumnWidth((int) (_contentViewWidth * .5));
			_productGridView.setHorizontalSpacing(cellSpacing * 4);
			_productGridView.setVerticalSpacing(cellSpacing * 4);
			_productGridAdapter = new ProductGridAdapter(this,
					_currentPageProductList, _currentProductCategory,
					(cellSize - (cellSpacing * 3)), productGridCellHeight,
					"showProductDetail");
			_productGridView.setAdapter(_productGridAdapter);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = cellSpacing * 3;
			layoutParams.rightMargin = cellSpacing * 3;
			layoutParams.topMargin = _searchBarHeight;
			layoutParams.width = _contentViewWidth;
			layoutParams.height = _contentViewHeight - _searchBarHeight;
			_productGridView.setVerticalScrollBarEnabled(false);
			_productViewLayout.addView(_productGridView, layoutParams);
			searchBar.setBackgroundColor(Color.rgb(178, 178, 178));
			_productGridView.setBackgroundColor(Color.LTGRAY);

			_productGridView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {

					if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
						// Log.i("","End is reached");
						// End is reached

						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, 1000);
					}

				}
			});

			_productViewLayout.setBackgroundColor(Color.rgb(206, 204, 204));

			handler = new Handler();
			runnable = new Runnable() {
				@Override
				public void run() {
					forwardButtonPressed();
				}
			};

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void reverseButtonPressed() {
		_currentProductIndex -= PRODUCT_FETCH_LIMIT;
		showProductsForCategory(_currentProductCategory, _currentSearchText,
				_currentProductIndex, false);
	}

	void forwardButtonPressed() {
		// if (!_globalSearchText.getText().toString().equalsIgnoreCase("")) {
		if (_currentSearchText != null
				&& !_currentSearchText.equalsIgnoreCase("")) {
			Log.i("", "Forward Button is pressed...");
			_currentProductIndex = _currentPageProductList.size();// PRODUCT_FETCH_LIMIT
			showProductsForCategory(_currentProductCategory,
					_currentSearchText, _currentProductIndex, false);
			forwardButtonPressed = true;
		}
	}

	// Back Button
	void removeProductView() {
		try {
			_contentLayout.removeView(_productViewLayout);
			_productViewLayout = null;
			if (_categoryLayout != null) {
				_categoryLayout.setVisibility(View.VISIBLE);
			}
			if (_backButton != null) {
				_backButton.setVisibility(View.GONE);
				_searchText.setText("");
			}
			System.gc();
		} catch (Exception ex) {
		}
	}

	public void hide_soft() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
				.getWindowToken(), 0);
	}

	public void showProductDetail(final int _detailType, String _currentPage) {
		// _productDetailView = new ProductDetailView(this,
		// _app._currentProductDetail, _currentProductCategory,
		// detailType, "updateShoppingList");
		// _productDetailView.show();
		try {
			// _app.resetScreenHeight(this);
			// _app.adjustScreenHeight(this);
			// _app._shoppingScreenContext = maincontext;
			// Intent new_intent = new Intent(this, ShowItemDetails.class);
			// new_intent.putExtra("productDetaiType", detailType);
			// new_intent.putExtra("currentpage", currentPage);
			// this.startActivity(new_intent);
			detailType = _detailType;
			currentPage = _currentPage;
			_productHandler.removeCallbacks(_productrunnable);
			_productHandler.postDelayed(_productrunnable, 1000);
			dismiss_offermenu();

			hide_soft();
		} catch (Exception ex) {
			Log.e("showProductDetail", ex.getMessage());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// products stuff end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// offers stuff begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void showAvailableOffers() {
		try {
			if (_offerGridView != null) {
				_contentLayout.removeView(_offerGridView);
				_offerGridView = null;
			}
			removeViews();

			_app._offersShown = true;
			int columns;
			int offerGridHeaderHeight;

			if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
				columns = 2;
				offerGridHeaderHeight = (int) (_contentViewHeight * .08);
			} else {
				columns = 2;
				offerGridHeaderHeight = (int) (_contentViewHeight * .06);
			}

			int cellSpacing = (int) (_contentViewWidth * .01);
			int cellSize = (_contentViewWidth - (cellSpacing * (columns + 1)))
					/ columns;
			int offerGridCellHeight = (int) (cellSize * 1.6);

			ArrayList<ArrayList<Offer>> rows = new ArrayList<ArrayList<Offer>>();
			ArrayList<Offer> cellArray = null;
			ArrayList<Offer> offerListArray = new ArrayList<Offer>();
			String headerText = null;
			int listCount = 0;

			cellArray = new ArrayList<Offer>();
			Offer offer = new Offer();
			offer.consumerTitle = "CONTENT_VIEW_BLANK";
			cellArray.add(offer);
			rows.add(cellArray);

			for (int i = 0; i < 3; i++) {
				if (i == 0) {
					offerListArray = _app._personalizedOffersList;
					listCount = offerListArray.size();
					headerText = "Personalized Offers";
				} else if (i == 1) {
					offerListArray = _app._extraFriendzyOffersList;
					listCount = offerListArray.size();
					headerText = "Extra Friendzy Offers";
				} else if (i == 2) {
					offerListArray = _app._moreForYouOffersList;
					listCount = offerListArray.size();
					headerText = "More For You Offers";
				}

				if (listCount > 0) {
					cellArray = new ArrayList<Offer>();
					offer = new Offer();
					offer.consumerTitle = "GRID_HEADER";
					offer.consumerDesc = headerText;
					cellArray.add(offer);
					rows.add(cellArray);

					cellArray = new ArrayList<Offer>();

					for (Offer listOffer : offerListArray) {
						cellArray.add(listOffer);

						if (cellArray.size() == columns) {
							rows.add(cellArray);
							cellArray = new ArrayList<Offer>();
						}
					}

					if (cellArray.size() > 0)
						rows.add(cellArray);
				}
			}

			if (_app._moreForYouOffersList.size() == 0
					&& _app._extraFriendzyOffersList.size() == 0
					&& _app._personalizedOffersList.size() == 0) { // Error
																	// message
																	// on No
				// offers
				if (_No_Accept_Offer_Message == null) {
					_No_Accept_Offer_Message = new SizedTextView(context,
							_navBarButtonWidth * 3, _navBarButtonHeight * 2,
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							"No offers available at this time");
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = _navBarButtonWidth / 2;
					layoutParams.topMargin = _navBarButtonHeight * 4;
					_contentLayout.addView(_No_Accept_Offer_Message,
							layoutParams);
				}
			} else {

				if (_No_Accept_Offer_Message != null) { // Accepted offer
					// message
					_contentLayout.removeView(_No_Accept_Offer_Message);
					_No_Accept_Offer_Message = null;
				}
				_offerGridView = new ListView(this);
				_offerGridView.setDividerHeight(0);
				_offerGridAdapter = new OfferGridAdapter(this, rows, columns,
						_contentViewWidth, offerGridHeaderHeight,
						offerGridCellHeight, "acceptOffer");
				_offerGridView.setSelector(new ColorDrawable(0));
				_offerGridView.setAdapter(_offerGridAdapter);
				// added in v2.3
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 0;
				layoutParams.topMargin = (int) (_navBarHeight * .35);
				layoutParams.width = _contentViewWidth;
				layoutParams.height = _contentViewHeight;
				_contentLayout.addView(_offerGridView, layoutParams);

			}
			// removed in v2.3
			// RelativeLayout.LayoutParams layoutParams = new
			// RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = 0;
			// layoutParams.topMargin = (int) (_navBarHeight * .35);
			// layoutParams.width = _contentViewWidth;
			// layoutParams.height = _contentViewHeight;
			// _contentLayout.addView(_offerGridView, layoutParams);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			// CLP SDK Menu opened - Available Offers
			JSONObject data = new JSONObject();
			data.put("event_name", "MenuOpened");
			data.put("link_clicked", "Available Offers");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAcceptedOffers() {
		try {

			// added for testing
			// _app._acceptedOffersList = new ArrayList<Offer>(
			// _app._moreForYouOffersList);
			// for (Offer offer : _app._moreForYouOffersList) {
			// offer._acceptedOffer = true;
			// offer._acceptableOffer = false;
			// }

			if (_offerGridView != null) {
				_contentLayout.removeView(_offerGridView);
				_offerGridView = null;
			}
			removeViews();
			_app._offersShown = true;
			int columns;
			int offerGridHeaderHeight;

			if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
				columns = 2;
				offerGridHeaderHeight = (int) (_contentViewHeight * .08);
			} else {
				columns = 2;
				offerGridHeaderHeight = (int) (_contentViewHeight * .06);
			}

			int cellSpacing = (int) (_contentViewWidth * .01);
			int cellSize = (_contentViewWidth - (cellSpacing * (columns + 1)))
					/ columns;
			int offerGridCellHeight = (int) (cellSize * 1.5);

			ArrayList<ArrayList<Offer>> rows = new ArrayList<ArrayList<Offer>>();
			ArrayList<Offer> cellArray = null;
			ArrayList<Offer> offerListArray = new ArrayList<Offer>();
			String headerText = null;
			int listCount = 0;

			cellArray = new ArrayList<Offer>();
			Offer offer = new Offer();
			offer.consumerTitle = "CONTENT_VIEW_BLANK";
			cellArray.add(offer);
			rows.add(cellArray);

			for (int i = 0; i < 1; i++) {
				if (i == 0) {
					offerListArray = _app._acceptedOffersList;
					listCount = offerListArray.size();
					headerText = "Accepted Offers";
				}

				if (listCount > 0) {
					cellArray = new ArrayList<Offer>();
					offer = new Offer();
					offer.consumerTitle = "GRID_HEADER";
					offer.consumerDesc = headerText;
					cellArray.add(offer);
					rows.add(cellArray);

					cellArray = new ArrayList<Offer>();

					for (Offer listOffer : offerListArray) {
						cellArray.add(listOffer);

						if (cellArray.size() == columns) {
							rows.add(cellArray);
							cellArray = new ArrayList<Offer>();
						}
					}

					if (cellArray.size() > 0)
						rows.add(cellArray);
				}
			}
			if (_app._acceptedOffersList.size() == 0) { // Error message on No
														// offers
				if (_No_Accept_Offer_Message == null) {
					_No_Accept_Offer_Message = new SizedTextView(context,
							_navBarButtonWidth * 3, _navBarButtonHeight * 2,
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							"No offers accepted at this time");
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = _navBarButtonWidth / 2;
					layoutParams.topMargin = _navBarButtonHeight * 4;
					_contentLayout.addView(_No_Accept_Offer_Message,
							layoutParams);
				}
			} else {

				if (_No_Accept_Offer_Message != null) { // Accepted offer
														// message
					_contentLayout.removeView(_No_Accept_Offer_Message);
					_No_Accept_Offer_Message = null;
				}

				_offerGridView = new ListView(this);
				_offerGridView.setDividerHeight(0);
				// _offerGridView.setDivider(this.getResources().getDrawable(
				// android.R.color.transparent));
				// _offerGridView.setDividerHeight((int) (_screenHeight *
				// .005));
				// _offerGridView.setSelector(android.R.color.transparent); //
				// disable
				// orange
				// background
				// highlighting
				_offerGridAdapter = new OfferGridAdapter(this, rows, columns,
						_contentViewWidth, offerGridHeaderHeight,
						offerGridCellHeight, "acceptOffer");
				_offerGridView.setSelector(new ColorDrawable(0));
				_offerGridView.setAdapter(_offerGridAdapter);

				// _offerGridView.setOnTouchListener(new OnTouchListener() {
				// @Override
				// public boolean onTouch(View v, MotionEvent event) {
				//
				// return false;
				// }
				//
				// });
			}

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = (int) (_headerHeight * .4);
			layoutParams.width = _contentViewWidth;
			layoutParams.height = _contentViewHeight;
			_contentLayout.addView(_offerGridView, layoutParams);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			// CLP SDK Menu opened - Accepted Offers
			JSONObject data = new JSONObject();
			data.put("event_name", "MenuOpened");
			data.put("link_clicked", "Accepted Offers");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void acceptOffer(Offer offer) {
		Login login = _app.getLogin();
		OfferAcceptRequest request = new OfferAcceptRequest();
		request.crmNumber = login.crmNumber;
		request.acceptGroup = offer.acceptGroup;
		// new for accepting push offer
		request.offerId = offer.offerId;
		if (offer.endDate != null) {
			request.endDate = offer.endDate;
		}
		if (offer.consumerTitle != null) {
			request.title = offer.consumerTitle;
		}
		if (offer.promoCode != null) {
			request.promoCode = offer.promoCode;
		}
		request.dynamicOffer = offer.dynamicOffer;
		//
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Sending accept request...");
		_service = new WebService(this, OfferAcceptRequest.class, requestBody,
				_login.authKey);
		_service.execute(_app.ACCEPT_OFFER_URL);
		// handled by handleOfferAcceptServiceResponse() directly below

		// CLP SDK Offer accept
		try {
			if (offer != null && offer.offerId != null
					&& offer.consumerTitle != null && offer.promoCode != null
					&& offer.consumerDesc != null && offer.acceptGroup != null
					&& offer.startDate != null && offer.endDate != null) {

				JSONObject data = new JSONObject();
				data.put("item_id", offer.offerId);
				data.put("item_name", offer.consumerTitle);
				data.put("promo_code", offer.promoCode);
				data.put("promo_title", offer.consumerDesc);
				data.put("price", String.valueOf(offer.offerPrice));
				data.put("accept_group", offer.acceptGroup);
				data.put("start_date", offer.startDate);
				data.put("end_date", offer.endDate);

				data.put("event_name", "AcceptOffer");
				data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			}
		} catch (Exception e) {
			Log.e(_app.CLP_TRACK_ERROR, "OFFER_OPEN:" + e.getMessage());
		}
		// CLP SDK

	}

	public void handleOfferAcceptServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				OfferAcceptRequest response = (OfferAcceptRequest) _service
						.getResponseObject();

				if (response == null || response.acceptGroup == null) {
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					showAvailableOffers();
					return;
				}

				for (int i = 0; i < _app._personalizedOffersList.size(); i++) {
					Offer offer = _app._personalizedOffersList.get(i);

					if (offer.acceptGroup.compareTo(response.acceptGroup) == 0) {
						_app._acceptedOffersList.add(offer);
						_app._personalizedOffersList.remove(i);
						break;
					}
				}

				for (int i = 0; i < _app._extraFriendzyOffersList.size(); i++) {
					Offer offer = _app._extraFriendzyOffersList.get(i);

					if (offer.acceptGroup.compareTo(response.acceptGroup) == 0) {
						_app._acceptedOffersList.add(offer);
						_app._extraFriendzyOffersList.remove(i);
						break;
					}
				}
				// show tick mark for success
				showTickImage(maincontext);
				showAvailableOffers();
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Accept Offer Failed",
							error.errorMessage);
				else
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error", "Http Status code: "
				// + status);

				showAvailableOffers();
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// offers stuff end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// lists stuff begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	void setEcartButton() {
		// Login login = _app.getLogin();
		// List<Store> storeList = _app.getStoresList();
		//
		// for (Store store : storeList) {
		// if (login.storeNumber == store.storeNumber
		// && store.ecart.equalsIgnoreCase("Yes")) {
		// _ecartButton.setVisibility(View.VISIBLE);
		// return;
		// }
		// }

		// _ecartButton.setVisibility(View.GONE);
		_ecartButton.setVisibility(View.VISIBLE);
	}

	void ecartButtonPressed() {
		_app._showShoppingList = true;
		ListRequest request = new ListRequest();
		request.accountId = _login.accountId;
		request.listId = _app.getCurrentListId();
		request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
		request.returnCurrentList = true;
		Log.i("SHOPPING_LIST", "ecartButtonPressed "
				+ _app._currentShoppingList.listId
				+ ": setting returnCurrentList = true, appListUpdateTime = "
				+ _app._currentShoppingList.serverUpdateTime);
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		// // START
		// try {

		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// // END

		// / showProgressDialog("Syncing Current List...");
		// _validatingEcartList = true;
		// _service = new WebService(this, ShoppingList.class, requestBody,
		// _login.authKey);
		// _service.execute(_app.LIST_GET_BY_ID_URL);

		ArrayList<Store> _nearestStoresList = _app.getNearestStores(Raleys
				.shared(getApplication()).latestLocation);
		if (_nearestStoresList == null || _nearestStoresList.size() == 0) {
			_app.getAllStores();
			showTextDialog(this, "Store List Error", "Please try again later");
			return;
		} else {
			String ecartEnabled = null;
			for (int i = 0; i < _nearestStoresList.size(); i++) {
				if (_login.storeNumber == _nearestStoresList.get(i).storeNumber) {
					ecartEnabled = _nearestStoresList.get(i).ecart;
					if (ecartEnabled.equals("Yes")) {
						showProgressDialog("Syncing Current List...");
						_validatingEcartList = true;
						_service = new WebService(this, ShoppingList.class,
								requestBody, _login.authKey);
						_service.execute(_app.LIST_GET_BY_ID_URL);
						// handled by handleListServiceResponse() below

					} else if (ecartEnabled.equals("No")) {
						showTextDialog(this, "",
								"eCart is not available for the current selected home store");
						return;
					}
					break;
				}
			}
		}
		// handled by handleListServiceResponse() below
	}

	void ecartPreOrder() {
		EcartPreOrderRequest request = new EcartPreOrderRequest();
		request.accountId = _login.accountId;
		request.storeNumber = _login.storeNumber;
		request.listId = _app.getCurrentListId();
		String requestBody = _gson.toJson(request);
		Log.i(getClass().getSimpleName(), "EcartPreOrderRequest: "
				+ requestBody);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Getting available pickup times...");
		_service = new WebService(this, EcartPreOrderResponse.class,
				requestBody, _login.authKey);
		_service.execute(_app.ECART_PREORDER_URL);
		// handled by handleEcartPreOrderServiceResponse() below
	}

	public void handleEcartPreOrderServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				EcartPreOrderResponse response = (EcartPreOrderResponse) _service
						.getResponseObject();

				if (response == null) {
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					return;
				}
				_app._currentEcartPreOrderResponse = response;
				Intent intent = new Intent(ShoppingScreen.this,
						EcartScreen.class);
				startActivity(intent);
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Appointment Retrieve Failed",
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

	public void showActiveList() {
		clearMenus();

		if (_shoppingListGridView != null)
			return;

		setActiveMenu(_listsButton);
		String listId = _app.getCurrentListId();

		if (listId == null || listId.length() == 0) {
			showTextDialog(this, "Show Active List", SET_ACTIVE_LIST_TEXT);
			return;
		}

		_app._showShoppingList = true;
		ListRequest request = new ListRequest();
		request.accountId = _login.accountId;
		request.listId = listId;
		request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
		request.returnCurrentList = true;
		Log.i("SHOPPING_LIST", "showActiveList "
				+ _app._currentShoppingList.listId
				+ ": setting returnCurrentList = true, appListUpdateTime = "
				+ _app._currentShoppingList.serverUpdateTime);
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		_showingActiveList = true;
		showProgressDialog("Retrieving shopping list...");
		_service = new WebService(this, ShoppingList.class, requestBody,
				_login.authKey);
		_service.execute(_app.LIST_GET_BY_ID_URL);
		// handled by handleListServiceResponse() below
	}

	// Called after new item added in list
	public void refreshCurrentShoppingList() {
		setActiveList(_app._currentActiveShoppingListName);
	}

	public void setActiveList(ShoppingListName listName) {
		if (listName == null)
			return;

		// For hide the tick Image
		_app._listItemClick = true;
		_app._currentActiveShoppingListName = listName;
		_app._shoppingScreenContext = maincontext;

		if (_listChangeView != null)
			_listChangeView.dismiss();

		// if (listName.listId.equalsIgnoreCase(_app.getCurrentListId())
		// && _shoppingListGridView != null)
		// return;

		removeViews();
		_app.setCurrentListId(listName.listId);
		_app._showShoppingList = true;
		// refreshShoppingList();
		ListRequest request = new ListRequest();
		request.accountId = _login.accountId;
		request.listId = listName.listId;
		request.appListUpdateTime = 0;
		request.returnCurrentList = true;
		Log.i("SHOPPING_LIST", "setActiveList: setting list " + listName.listId
				+ ", returnCurrentList = true, appListUpdateTime = "
				+ request.appListUpdateTime);
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Retrieving shopping list...");
		_service = new WebService(this, ShoppingList.class, requestBody,
				_login.authKey);
		_service.execute(_app.LIST_GET_BY_ID_URL);
		// handled by handleListServiceResponse() below
	}

	public void changeActiveList(ShoppingListName listName) {
		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}
		showProgressDialog("Get product list");
		_app.getActiveShoppingListAndUpdate(this, listName);
		try {
			// CLP SDK Menu opened - Set Active List
			JSONObject data = new JSONObject();
			data.put("event_name", "MenuOpened");
			data.put("link_clicked", "List - Set Active");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeActiveListUpdate() {
		Handler mainHandler = new Handler(context.getMainLooper());
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				// run code
				dismissActiveDialog();
				_listNameAdapter.moveActiveShopListToTop();
				_listNameAdapter.notifyDataSetChanged();
				int count = _listNameAdapter.getCount();
				if (count > 0) {
					_listView.setSelection(0);
				}
			}
		});

	}

	public void confirmChangeActiveList(final ShoppingListName listName) {
		Handler mainHandler = new Handler(context.getMainLooper());
		mainHandler.post(new Runnable() {

			@Override
			public void run() {
				// run code
				_app.setActiveListId(listName.listId);
				_listNameAdapter.notifyDataSetChanged();
				showTickImage(maincontext);
			}
		});
	}

	public void updateShoppingList(int updateType, Product product,
			String listID) {
		try {

			_productUpdateType = updateType;

			if (product == null) // should be null when the close button in the
									// detail view is used
			{
				if (_scannerLayout != null)
					restartCamera();

				return;
			}
			String _activeList = listID;
			// String ListID = _app.getActiveListId();
			// ShoppingList list = _app.getActiveShoppingList();
			if (_activeList.isEmpty()) {
				showTextDialog(this, "List Error", SET_ACTIVE_LIST_TEXT);
				return;
			}

			// if (_app._currentShoppingList == null) {
			// showTextDialog(this, "List Error", SET_ACTIVE_LIST_TEXT);
			// return;
			// }

			if (_app._currentShoppingList == null) {
				_app._currentShoppingList = _app.getActiveShoppingList();
			}

			if (_productUpdateType == Product.PRODUCT_ADD
					|| _productUpdateType == Product.PRODUCT_MODIFY) {
				_productToAdd = product; // product gets added later if the
											// service
											// is successful and the list is not
											// returned because it is current
				ListAddItemRequest request = new ListAddItemRequest();
				request.accountId = _login.accountId;
				request.listId = listID; // _app.getCurrentListId();
				request.sku = product.sku;
				request.qty = product.qty;
				request.customerComment = product.customerComment;

				if (listID.equalsIgnoreCase(_app.getActiveListId())) {
					request.appListUpdateTime = _app.getActiveShoppingList().serverUpdateTime;
				} else {
					request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
				}

				if (product.weight > 0) {
					request.purchaseBy = "W";
					request.weight = product.weight;
					_productQuantity = product.weight;
				} else {
					request.purchaseBy = "E";
					request.qty = product.qty;
					_productQuantity = product.qty;

				}

				if (_shoppingListGridView != null) {
					request.returnCurrentList = true;
					Log.i("SHOPPING_LIST",
							"ADD: setting returnCurrentList = true, appListUpdateTime = "
									+ request.appListUpdateTime
									+ ", totalPrice = "
									+ _app.getActiveShoppingList().totalPrice);
				} else {
					request.returnCurrentList = false;
					Log.i("SHOPPING_LIST",
							"ADD: setting returnCurrentList = false, appListUpdateTime = "
									+ request.appListUpdateTime
									+ ", totalPrice = "
									+ _app.getActiveShoppingList().totalPrice);
				}

				String requestBody = _gson.toJson(request);

				if (!Utils.isNetworkAvailable(this)) {
					showNetworkUnavailableDialog(this);
					return;
				}

				if (_shoppingListGridView != null)
					_app._showShoppingList = true;
				else
					_app._showShoppingList = false;

				showProgressDialog("Updating your list...");
				_service = new WebService(this, ShoppingList.class,
						requestBody, _login.authKey);
				_service.execute(_app.LIST_ADD_ITEM_URL);
				// handled by handleListServiceResponse() below
			} else if (_productUpdateType == Product.PRODUCT_DELETE) {
				_productToDelete = product; // product gets deleted later if the
											// service is successful and the
											// list is
											// not returned because it is
											// current
				ListDeleteItemRequest request = new ListDeleteItemRequest();
				request.accountId = _login.accountId;
				request.listId = listID; // _app.getCurrentListId();
				request.sku = product.sku;
				request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
				if (product.weight > 0) {
					_productQuantity = product.weight;
				} else {
					_productQuantity = product.qty;

				}
				if (_shoppingListGridView != null) {
					request.returnCurrentList = true;
					Log.i("SHOPPING_LIST",
							"ADD: setting returnCurrentList = true, appListUpdateTime = "
									+ _app._currentShoppingList.serverUpdateTime);
				} else {
					request.returnCurrentList = false;
					Log.i("SHOPPING_LIST",
							"ADD: setting returnCurrentList = false, appListUpdateTime = "
									+ _app._currentShoppingList.serverUpdateTime);
				}

				String requestBody = _gson.toJson(request);

				if (!Utils.isNetworkAvailable(this)) {
					showNetworkUnavailableDialog(this);
					return;
				}

				if (_shoppingListGridView != null)
					_app._showShoppingList = true;
				else
					_app._showShoppingList = false;

				_cancelProductImageUpdate = true;

				showProgressDialog("Updating your list...");

				_service = new WebService(this, ShoppingList.class,
						requestBody, _login.authKey);
				_service.execute(_app.LIST_DELETE_ITEM_URL);
				// handled by handleListServiceResponse() below
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handleListServiceResponse() {
		try {
			int status = _service.getHttpStatusCode();

			if (status == 200) {

				if (_app._listItemClick == true) {
					dismissActiveDialog();
					_app._listItemClick = false;
				} else {
					dismissActiveDialog();
					if (_validatingEcartList == false) {
						showTickImage(maincontext);
					}
				}

				if (_showingActiveList == true) // ok to clear the view that was
												// active when the service call
												// was made
				{
					_showingActiveList = false;
					removeViews();
				}

				ShoppingList response = (ShoppingList) _service
						.getResponseObject();

				if (response == null) {
					dismissActiveDialog();
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					return;
				}

				_app.setCurrentListId(response.listId);

				if (_login.storeNumber != response.storeNumber) {
					_login.storeNumber = response.storeNumber;
					_app.setLogin(_login);
				}

				Log.i("SHOPPING_LIST", "handleListServiceResponse for "
						+ response.listId);

				if (response.productList == null)
					Log.i("SHOPPING_LIST", "response.productList is null");
				else
					Log.i("SHOPPING_LIST", "response.productList contains "
							+ response.productList.size() + " products");

				if (response.productListReturned == true) {
					if (response.productList == null)
						Log.e(getClass().getSimpleName(),
								"response.productList is null");

					_app._currentShoppingList = response;
					// update active list
					if (response.listId
							.equalsIgnoreCase(_app.getActiveListId())) {
						_app.setActiveShoppingList(response);
					}
					//
					Log.i("SHOPPING_LIST",
							"responseList.productListReturned == true, serverUpdateTime = "
									+ response.serverUpdateTime
									+ ", totalPrice = " + response.totalPrice);
				} else // list wasn't returned so check if we need to manually
						// update the list because a product was added, deleted,
						// or modified, or that we were just syncing prior to
						// submitting an ecart order
				{
					Log.i("SHOPPING_LIST",
							"responseList.productListReturned == false, serverUpdateTime = "
									+ response.serverUpdateTime
									+ ", totalPrice = " + response.totalPrice);
					_app._currentShoppingList.serverUpdateTime = response.serverUpdateTime;
					_app._currentShoppingList.name = response.name; // for
																	// footer
																	// details
					_app._currentShoppingList.totalPrice = response.totalPrice; // for
																				// footer
																				// details

					if (_productToDelete != null) {
						Log.i("SHOPPING_LIST", "Deleting product:"
								+ _productToDelete.upc + ", qty:"
								+ _productToDelete.qty + ", description:"
								+ _productToDelete.description);

						// choose list
						ShoppingList list;
						if (_app.getActiveListId().equalsIgnoreCase(
								response.listId)) {
							list = _app.getActiveShoppingList();
						} else {
							list = _app._currentShoppingList;
						}
						if (list != null) {
							for (int i = 0; i < list.productList.size(); i++) {
								Product product = list.productList.get(i);
								if (product.upc.compareTo(_productToDelete.upc) == 0) {
									list.productList.remove(i);
									break;
								}
							}
							// update active list
							if (_app.getActiveListId().equalsIgnoreCase(
									response.listId)) {
								_app.setActiveShoppingList(list);
							}
						}
						try {
							// CLP SDK Product Delete
							JSONObject data = new JSONObject();
							data.put("SKU", _productToDelete.sku);
							data.put("item_id", _productToDelete.upc);
							data.put("brand", _productToDelete.brand);
							data.put("item_name", _productToDelete.description);
							data.put("price",
									String.valueOf(_productToDelete.regPrice));
							data.put("promo_price",
									String.valueOf(_productToDelete.promoPrice));
							data.put("category", _productToDelete.mainCategory);
							if (_productToDelete.weight > 0) {
								data.put("quantity",
										String.valueOf(_productToDelete.weight));
							} else {
								data.put("quantity",
										String.valueOf(_productToDelete.qty));
							}

							data.put("event_name", "ProductRemoved");
							data.put("event_time",
									_app.clpsdkObj.formatedCurrentDate());
							_app.clpsdkObj.updateAppEvent(data);
							// / STOP

							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
						_productToDelete = null;
						dismissActiveDialog();
						restorePreviousView();
						// setFooterDetails();
						return;
					}

					if (_productToAdd != null) {
						// if (_productUpdateType == Product.PRODUCT_ADD) {
						// Log.i("SHOPPING_LIST", "Adding product:"
						// + _productToAdd.upc + ", qty:"
						// + _productToAdd.qty + ", description:"
						// + _productToAdd.description);
						// _app._currentShoppingList.productList
						// .add(_productToAdd);
						// } else // Product.PRODUCT_MODIFY
						// {
						// choose list
						ShoppingList list;
						if (_app.getActiveListId().equalsIgnoreCase(
								response.listId)) {
							list = _app.getActiveShoppingList();
						} else {
							list = _app._currentShoppingList;
						}
						if (list != null) {
							boolean productAlreadyExist = false;
							for (int i = 0; i < list.productList.size(); i++) {
								Product product = list.productList.get(i);

								if (product.upc.compareTo(_productToAdd.upc) == 0) {
									list.productList.remove(i);
									list.productList.add(_productToAdd);
									productAlreadyExist = true;
									break;
								}
							}
							if (productAlreadyExist == false) {
								list.productList.add(_productToAdd);
							}

							// update active list
							if (_app.getActiveListId().equalsIgnoreCase(
									response.listId)) {
								_app.setActiveShoppingList(list);
							}
							// CLP SDK
							JSONObject data = new JSONObject();
							data.put("SKU", _productToAdd.sku);
							data.put("item_id", _productToAdd.upc);
							data.put("brand", _productToAdd.brand);
							data.put("item_name", _productToAdd.description);
							data.put("price",
									String.valueOf(_productToAdd.regPrice));
							data.put("promo_price",
									String.valueOf(_productToAdd.promoPrice));

							data.put("category", _productToAdd.mainCategory);

							if (_productToAdd.weight > 0) {
								data.put("quantity",
										String.valueOf(_productToAdd.weight));
							} else {
								data.put("quantity",
										String.valueOf(_productToAdd.qty));
							}

							if (productAlreadyExist) {
								// CLP SDK Product Modified
								data.put("event_name",
										"ProductQuantityModified");
								data.put("event_time",
										_app.clpsdkObj.formatedCurrentDate());
							} else {
								// CLP SDK Product add
								data.put("event_name", "ProductAdded");
								data.put("event_time",
										_app.clpsdkObj.formatedCurrentDate());
							}
							_app.clpsdkObj.updateAppEvent(data);
							// / STOP
						}

						_productToAdd = null;
						dismissActiveDialog();
						restorePreviousView();
						// setFooterDetails();
						return;
					}

					if (_validatingEcartList == true) {
						_validatingEcartList = false;
						dismissActiveDialog();
						ecartPreOrder();
						return;
					}
				} // end of productListReturned == false

				// setFooterDetails();

				if (_app._currentShoppingList.productList != null) {
					Log.i("SHOPPING_LIST",
							"_app._currentShoppingList contains "
									+ _app._currentShoppingList.productList
											.size() + " products");

					if (_app._showShoppingList == false
							|| (_app._showShoppingList == true && _cancelProductImageUpdate == true)) // no
																										// need
																										// to
																										// clear
																										// cache
																										// if
																										// the
																										// list
																										// isn't
																										// visible
																										// or
																										// we're
																										// deleting
																										// an
																										// item
																										// from
																										// the
																										// list
					{
						_cancelProductImageUpdate = false;
						dismissActiveDialog();
						restorePreviousView();
						return;
					}
					dismissActiveDialog();
					showProgressDialog("Retrieving product data...");
					_app.getProductImages(_app._currentShoppingList.productList);
					new WaitForProductImages().execute(); // screen redraws
															// after image
															// collection
				} else {
					dismissActiveDialog();
				}
			} else // service error
			{
				dismissActiveDialog();
				// WebServiceError error = _service.getError();
				if (status == 422) // backend or http error
					showTextDialog(this, "Appointment Retrieve Failed",
							Raleys.COMMON_ERROR_MSG);
				else
					showTextDialog(this, "Server Error",
							Raleys.COMMON_ERROR_MSG);

				if (_scannerLayout != null)
					restartCamera();

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			dismissActiveDialog();
		}
	}

	public void showShoppingList() {
		if (_shoppingListGridView != null) {
			_contentLayout.removeView(_shoppingListGridView);
			_shoppingListGridView = null;
		}

		if (_ErrorMSG_Text != null) {
			_contentLayout.removeView(_ErrorMSG_Text);
			_ErrorMSG_Text = null;
		}

		if (_ShoppingList_Footer_container != null) {
			_ShoppingList_Footer_container.removeAllViews();
			_contentLayout.removeView(_ShoppingList_Footer_container);
			_contentLayout.removeView(ShoppingListTitleContainer);// new code
			_ShoppingList_Footer_container = null;
			ShoppingListTitleContainer = null;// new code
		}

		if (_app._currentShoppingList.productList == null)
			return;

		Collections.sort(_app._currentShoppingList.productList,
				new AisleComparator()); // sorted by aisleNumber ascending

		int columns;
		int shoppingListHeaderHeight;
		if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
			columns = 1; // Over riding column values
			shoppingListHeaderHeight = (int) (_contentViewHeight * .065); // 0.08
		} else {
			columns = 4;
			shoppingListHeaderHeight = (int) (_contentViewHeight * .06);
		}

		int cellSpacing = (int) (_contentViewWidth * .005);
		int cellSize = (_contentViewWidth - (cellSpacing * (columns + 1)))
				/ columns;
		int shoppingListCellHeight = (int) (cellSize * 0.20); // near to 140

		ArrayList<ArrayList<Product>> rows = new ArrayList<ArrayList<Product>>();

		int listCount = _app._currentShoppingList.productList.size();
		int currentListAisle = 0;
		int currentProductAisle = 0;
		ArrayList<Product> cellArray = null;

		cellArray = new ArrayList<Product>();
		Product blankProduct = new Product();
		blankProduct.mainCategory = "CONTENT_VIEW_BLANK";

		for (int i = 0; i < listCount; i++) {
			// wrap cell building in separate try/catch block so data errors
			// don't wipe every cell out
			try {
				if (i == 0 && listCount == 0) // no need to add anything
					break;

				Product product = _app._currentShoppingList.productList.get(i);
				String headerString = null;
				currentProductAisle = Integer.valueOf(product.aisleNumber);

				if (currentProductAisle == 999)
					headerString = "Not Available At This Store";
				else
					headerString = "Products in Aisle " + product.aisleNumber;

				if (i == 0) // add the first header
				{
					currentListAisle = Integer.valueOf(product.aisleNumber);
					Product headerProduct = new Product(); // add header cell
					headerProduct.mainCategory = "GRID_HEADER";
					headerProduct.description = headerString;
					cellArray = new ArrayList<Product>();
					cellArray.add(headerProduct);
					rows.add(cellArray);
					cellArray = new ArrayList<Product>();
				}

				if (currentProductAisle != currentListAisle) // next aisle
				{
					if (cellArray.size() > 0)
						rows.add(cellArray);

					currentListAisle = Integer.valueOf(product.aisleNumber);
					Product headerProduct = new Product(); // add header cell
					headerProduct.mainCategory = "GRID_HEADER";
					headerProduct.description = headerString;
					cellArray = new ArrayList<Product>();
					cellArray.add(headerProduct);
					rows.add(cellArray);
					cellArray = new ArrayList<Product>();
				}

				cellArray.add(product);

				if (cellArray.size() == columns) {
					rows.add(cellArray);
					cellArray = new ArrayList<Product>();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// new code{
		int _listTitleHeight = (int) (_headerHeight * 0.6);
		ShoppingListTitleContainer = new RelativeLayout(this);
		RelativeLayout.LayoutParams ContainerListlayoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		ContainerListlayoutParams.topMargin = _navBarHeight;
		ContainerListlayoutParams.height = _listTitleHeight;
		ShoppingListTitleContainer.setBackgroundColor(Color.rgb(39, 39, 39)); // normal
																				// black
																				// color
		TextView ShoppingListTitle = new TextView(this);
		ShoppingListTitle.setText(_app._currentShoppingList.name);
		ShoppingListTitle.setTextColor(Color.WHITE);
		ShoppingListTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				(int) (_listTitleHeight * 0.4));
		ShoppingListTitle.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams ListlayoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		ListlayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL
				| RelativeLayout.CENTER_VERTICAL,
				ShoppingListTitleContainer.getId());
		ShoppingListTitleContainer.addView(ShoppingListTitle, ListlayoutParams);
		_contentLayout.addView(ShoppingListTitleContainer,
				ContainerListlayoutParams);

		// new code }

		if (rows.size() > 1 && cellArray.size() > 0)
			rows.add(cellArray);

		_shoppingListGridView = new ListView(this);
		_shoppingListGridAdapter = new ShoppingListGridAdapter(this, rows,
				columns, _contentViewWidth, shoppingListHeaderHeight,
				shoppingListCellHeight, SHOW_PRODUCT_DETAIL_CALLBACK);
		_shoppingListGridView.setAdapter(_shoppingListGridAdapter);
		_shoppingListGridView.setOnTouchListener(this);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = 0;
		layoutParams.topMargin = _navBarHeight + _listTitleHeight;
		layoutParams.width = _contentViewWidth;
		ShoppingList _isEmptylist = _app._currentShoppingList;
		int Error_tot_text_height = (int) (_contentViewWidth * 0.065);
		if (_isEmptylist.productList.size() <= 0) {

			if (_ErrorMSG_Text == null) {

				_ErrorMSG_Text = new SmartTextView(context,
						(_contentViewWidth), Error_tot_text_height, 1,
						Color.TRANSPARENT, Color.DKGRAY, _normalFont,
						"No item added", Align.CENTER);
				RelativeLayout.LayoutParams Errortxt_layoutParams = null;
				Errortxt_layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				Errortxt_layoutParams.topMargin = (int) (_contentViewHeight / 2.1);
				Errortxt_layoutParams.height = Error_tot_text_height; // .42

				_contentLayout.addView(_ErrorMSG_Text, Errortxt_layoutParams);
			} else {
				_contentLayout.removeView(_ErrorMSG_Text);
				_ErrorMSG_Text = null;
			}
		}
		int table_height = _contentViewHeight - (shoppingListCellHeight);
		int tot_text_height = (int) (_contentViewWidth * 0.07);
		layoutParams.height = table_height - tot_text_height
				- ((int) (_headerHeight * 0.5) * 2);
		_contentLayout.addView(_shoppingListGridView, layoutParams);

		// creating Footer Details controls
		_ShoppingList_Footer_container = new LinearLayout(context);
		_ShoppingList_Footer_container.setOrientation(LinearLayout.VERTICAL);
		_ShoppingList_Footer_container.setBackgroundColor(Color.DKGRAY);
		LayoutParams _Footer_container_param = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		_Footer_container_param.height = (shoppingListCellHeight);
		_Footer_container_param.topMargin = table_height;
		_Footer_container_param.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		_Footer_container_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		_ShoppingList_Footer_container.setLayoutParams(_Footer_container_param);

		TextView total_PriceText = new SmartTextView(context,
				(_contentViewWidth), tot_text_height, 1, Color.rgb(187, 0, 0),
				Color.WHITE, _normalFont, "$"
						+ String.format("%.2f", getShoppingListTotal()),
				Align.CENTER);
		RelativeLayout.LayoutParams txt_layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		txt_layoutParams.topMargin = table_height;
		txt_layoutParams.height = tot_text_height; // .42
		_ShoppingList_Footer_container.addView(total_PriceText,
				txt_layoutParams);
		int remainingfooter_height = (shoppingListCellHeight) - tot_text_height;

		_Checkout_button = new Button(context);
		_Checkout_button.setText("Checkout");
		_Checkout_button.setAllCaps(false);//v2.3 fix for Android Lollipop
		_Checkout_button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				(int) (remainingfooter_height * 0.4));
		_Checkout_button.setHeight(remainingfooter_height);
		_Checkout_button.setTextColor(Color.LTGRAY);
		_Checkout_button.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams newlayoutParams1 = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		newlayoutParams1.topMargin = tot_text_height;
		newlayoutParams1.height = remainingfooter_height;
		newlayoutParams1.width = _contentViewWidth;

		if (_isEmptylist.productList.size() > 0) {

			_Checkout_button.setTextColor(Color.BLACK);
			_Checkout_button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ecartButtonPressed();
				}
			});
		}

		_ShoppingList_Footer_container.addView(_Checkout_button,
				newlayoutParams1);
		_contentLayout.addView(_ShoppingList_Footer_container,
				_Footer_container_param);

		setEcartButton();
		if (_validatingEcartList == true) {
			_validatingEcartList = false;
			ecartPreOrder();
		}
	}

	public float getShoppingListTotal() {
		// total payable price
		float total = 0.0f;
		ShoppingList list = _app._currentShoppingList;
		for (int i = 0; i < list.productList.size(); i++) {

			// currentProductAisle = Integer.valueOf(product.aisleNumber);
			// 999 - not available on the store

			Product _product = list.productList.get(i);
			int quantity = 0;// no of items
			float weight = 0;// no of items
			if (_product.weight > 0)
				weight = _product.weight;
			else
				quantity = _product.qty;
			float unitPrice;// item unit price
			if (_product.promoType > 0)
				unitPrice = _product.promoPrice / _product.promoForQty;
			else
				unitPrice = _product.regPrice;
			if (_product.approxAvgWgt > 0 && _product.qty > 0)
				unitPrice *= _product.approxAvgWgt;

			if (_product.weight > 0)
				total = total + (weight * unitPrice);
			else
				total = total + (quantity * unitPrice);

		}
		return total;
	}

	public void setActiveList() {
		ListRequest request = new ListRequest();
		request.accountId = _login.accountId;
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Getting list names...");
		_service = new WebService(this, ShoppingListName.class, requestBody,
				_login.authKey);
		_service.execute(_app.LIST_GET_ALL_URL);
		// handled by handleListNamesServiceResponse() directly below
	}

	public void handleListNamesServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				ShoppingListName response = (ShoppingListName) _service
						.getResponseObject();

				if (response == null || response.nameList == null) {
					showTextDialog(this, "Server Error",
							"Unable to parse data returned from server.");
					return;
				}

				// if (response.nameList.size() == 1) {
				// setActiveList(response.nameList.get(0));
				// return;
				// }

				float width = 0.0f;
				float height = 0.0f;
				float top = 0.0f;

				if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
					width = .90f;
					height = .48f;
					top = .19f;
				} else {
					width = .70f;
					height = .43f;
					top = .24f;
				}

				// _listChangeView = new ListChangeView(this, width, height,
				// top,
				// response.nameList, "setActiveList");
				// _listChangeView.show();
				showShoppingNameList(response.nameList);
			} else {
				// WebServiceError error = _service.getError();
				ArrayList<ShoppingListName> currlistName = new ArrayList<ShoppingListName>();

				if (status == 422) { // back end or http error
					showShoppingNameList(currlistName);
					// showTextDialog(this, "List Names Retrieve Failed",
					// error.errorMessage);
				} else {
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error", "Http Status code: "
					// + status);
				}

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void showShoppingNameList(ArrayList<ShoppingListName> listName) {

		if (_ShoppingNameListContainer != null) {
			_listNameAdapter.changeListName(listName);
			_listNameAdapter.notifyDataSetChanged();
			return;
		}

		removeViews();
		_selectedButton = null;
		setActiveMenu(_listsButton);
		// list
		int listButtonHeight = _listsButton.getHeight();
		_ShoppingNameListContainer = new RelativeLayout(this);
		_ShoppingNameListContainer
				.setLayoutParams(new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						_contentLayout.getHeight() - listButtonHeight));
		RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				_contentLayout.getHeight() - _listsButton.getHeight());
		containerParams.topMargin = listButtonHeight;

		int totalShopListHeight = _contentLayout.getHeight() - listButtonHeight;
		int shopListHeight = totalShopListHeight - listButtonHeight;
		int createListHeight = listButtonHeight;

		_listView = new ListView(context);
		_listView.setBackgroundColor(Color.TRANSPARENT);
		_listView.setDividerHeight(0);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		layoutParams.height = shopListHeight;
		layoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		_ShoppingNameListContainer.addView(_listView, layoutParams);

		_listNameAdapter = new ListNameAdapter(this, listName,
				_contentLayout.getWidth(), listButtonHeight, "setActiveList");
		_listView.setAdapter(_listNameAdapter);
		_listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismiss_offermenu();
				if (event.getAction() == MotionEvent.ACTION_SCROLL) {
					View currentFocus = ((Activity) maincontext)
							.getCurrentFocus();
					if (currentFocus != null) {
						currentFocus.clearFocus();

						_listNameAdapter.hideKeyboard();
					}
				} else {
					_listNameAdapter.checkAddNewIsCompleted();
				}
				return false;
			}
		});
		_contentLayout.addView(_ShoppingNameListContainer, containerParams);

		// create New list Footer Details controls
		// Main Layout

		int sdk = android.os.Build.VERSION.SDK_INT;
		_CreateLayoutContainer = new RelativeLayout(context);
		_CreateLayoutContainer.setBackgroundColor(Color.WHITE);
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			_CreateLayoutContainer.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.list_section_bg));
		} else {
			_CreateLayoutContainer.setBackground(this.getResources()
					.getDrawable(R.drawable.list_section_bg));
		}
		LayoutParams _mainLayout_param = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		_mainLayout_param.height = createListHeight;
		_mainLayout_param.topMargin = shopListHeight;
		_mainLayout_param.width = _contentViewWidth;
		_mainLayout_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		int AddIcon_button_size = (int) (_contentViewWidth * 0.08);

		// Sub layout Layout
		LinearLayout _Create_subCenterLayout = new LinearLayout(context);
		_Create_subCenterLayout.setOrientation(LinearLayout.HORIZONTAL);
		_Create_subCenterLayout.setBackgroundColor(Color.TRANSPARENT);

		LayoutParams _SubLayout_param = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		_SubLayout_param.height = createListHeight;
		// _SubLayout_param.topMargin = shopListHeight;
		_SubLayout_param.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		// _SubLayout_param.leftMargin = (int) ((_contentViewWidth -
		// AddIcon_button_size) / 2);
		_SubLayout_param.addRule(RelativeLayout.CENTER_HORIZONTAL);

		// Add new icon
		ImageView Add_NewIconButton = new ImageView(this);
		// Text Button(this);

		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			Add_NewIconButton.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.add_new));
		} else {
			Add_NewIconButton.setBackground(this.getResources().getDrawable(
					R.drawable.add_new));
		}

		LinearLayout.LayoutParams IconlayoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		IconlayoutParams.width = AddIcon_button_size;
		IconlayoutParams.height = AddIcon_button_size;
		// IconlayoutParams.leftMargin = (int) (_contentViewWidth * 0.28);
		IconlayoutParams.topMargin = (createListHeight - AddIcon_button_size) / 2; // (layout
																					// height
																					// -
																					// buttonsize/2)
		_Create_subCenterLayout.addView(Add_NewIconButton, IconlayoutParams); // Icon
																				// Image

		// create new list Button
		Button createNew = new Button(context);
		createNew.setText("Create New List");
		createNew.setAllCaps(false);//v2.3 fix for Android Lollipop
		createNew.setTextColor(Color.BLACK);
		createNew.setBackgroundColor(Color.TRANSPARENT);
		RelativeLayout.LayoutParams newlayoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		newlayoutParams.height = createListHeight;
		newlayoutParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		_Create_subCenterLayout.addView(createNew, newlayoutParams); // Text//
																		// Button

		_CreateLayoutContainer.addView(_Create_subCenterLayout,
				_SubLayout_param); // Sub layout controls container
		//
		_contentLayout.addView(_CreateLayoutContainer, _mainLayout_param); // Main
																			// BG
																			// Layout

		_CreateLayoutContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss_offermenu();
				createNewList();
			}
		});
		_contentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss_offermenu();
			}
		});

		createNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createNewList();
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInputFromWindow(
						_CreateLayoutContainer.getApplicationWindowToken(),
						InputMethodManager.SHOW_FORCED, 0);
			}
		});
		// show instruction screen if first time user
		SharedPreferences _checkPreference = getSharedPreferences(
				_login.accountId, Context.MODE_PRIVATE);
		Boolean _checkfirstLogin = _checkPreference.getBoolean(
				"LIST_FIRST_LOGIN", true);

		if (_checkfirstLogin == false) {
			// openIstructionPopUp();
		} else if (_checkfirstLogin == true) {
			_checkPreference.edit().putBoolean("LIST_FIRST_LOGIN", false)
					.commit();
			openInstructionPopUp();
		}

	}

	public void openInstructionPopUp() {

		try {

			// custom dialog
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			lp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			dialog.getWindow().setBackgroundDrawableResource(
					R.drawable.list_overlay);
			dialog.getWindow().setAttributes(lp);

			ImageView _mainDialog = new ImageView(this);
			_mainDialog.setBackgroundResource(R.drawable.list_overlay);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
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

	private void createNewList() {
		try {
			createNewButtonPressed();
			_listNameAdapter.createNew = true;
			_listNameAdapter.notifyDataSetChanged();
			int count = _listNameAdapter.getCount();
			if (count > 0) {
				_listView.setSelection(_listNameAdapter.getCount() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteConfirmation(ShoppingListName listName) {
		_deleteListName = listName;
		String message = "Are you sure you want to delete the '".concat(
				listName.name).concat("'?");
		showTextDialog(this, _normalFont, _normalFont, "Delete", message,
				"deleteList", "dismissTextDialog");
	}

	public void deleteList() {
		dismissTextDialog();
		if (_deleteListName != null) {
			deleteList(_deleteListName);
		}
		try {
			// CLP SDK Menu opened - Delete List
			JSONObject data = new JSONObject();
			data.put("event_name", "MenuOpened");
			data.put("link_clicked", "Delete List");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteList(ShoppingListName listName) {
		dismissTextDialog();

		ListDeleteRequest request = new ListDeleteRequest();
		request.accountId = _login.accountId;
		request.listId = listName.listId; // _app._currentShoppingList.listId;
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Deleting list...");
		_service = new WebService(this, ListDeleteRequest.class, requestBody,
				_login.authKey);
		_service.execute(_app.LIST_DELETE_URL);
		// handled by handleDeleteListServiceResponse() directly below
	}

	public void handleDeleteListServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				// removeViews();
				// _app._currentShoppingList = null;
				// _app.setCurrentListId("");
				// setFooterDetails();
				// setActiveMenu(_offersButton);
				// showAvailableOffers();

				String activeListId = _app.getActiveListId();
				if (_deleteListName.listId.equals(activeListId)) {
					_app.setActiveListId(null);
					_app.setActiveShoppingList(null);
				}
				// show tick
				refreshShoppingList();

			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Delete List Failed",
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

	public void cancelListDelete() {
		dismissTextDialog();
	}

	public void createList() {
		float width = 0.0f;
		float height = 0.0f;
		float top = 0.0f;

		if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
			width = .90f;
			height = .48f;
			top = .12f;
		} else {
			width = .70f;
			height = .43f;
			top = .24f;
		}

		_listCreateView = new ListCreateView(this, width, height, top,
				"addNewList");
		_listCreateView.show();
	}

	public void addNewListConfirm() {
		try {
			if (_listNameAdapter != null) {
				_listNameAdapter.addNewList();
				dismissTextDialog();
				_listNameAdapter.hideKeyboard();
				_listNameAdapter.addNewAlertIsPresent = false;
			}
			// addNewCancel();
		} catch (Exception ex) {
			Log.e("Error", "addNewListConfirm");
		}
	}

	public void addNewCancel() {
		try {
			dismissTextDialog();
			_listNameAdapter.cancelAddNew();
		} catch (Exception ex) {
			Log.e("Error", "addNewCancel");
		}
	}

	public void addNewList(String listName) {
		// _listCreateView.dismiss();
		ListCreateRequest request = new ListCreateRequest();
		request.email = _login.email;
		request.accountId = _login.accountId;
		request.listName = listName;
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Creating list...");
		_service = new WebService(this, ListCreateRequest.class, requestBody,
				_login.authKey);
		_service.execute(_app.LIST_CREATE_URL);
		// handled by handleCreateListServiceResponse() directly below
	}

	public void handleCreateListServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				ListCreateRequest response = (ListCreateRequest) _service
						.getResponseObject();

				if (response == null || response.listName == null) {
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					return;
				}

				// showTextDialog(
				// this,
				// "List Added",
				// "List "
				// + response.listName
				// +
				// " has been added. To make this your active list use the 'Set Active' item in 'Manage Lists' menu.",
				// "refreshShoppingList");
				showTickImage(maincontext);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						refreshShoppingList();
					}
				}, TickDialog.animationDelay);
				try {
					// CLP SDK Menu opened - Create new list
					JSONObject data = new JSONObject();
					data.put("event_name", "MenuOpened");
					data.put("link_clicked", "Create New List");
					data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
					_app.clpsdkObj.updateAppEvent(data);
					//
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Add List Failed", error.errorMessage);
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

	public void refreshShoppingList() {
		dismissTextDialog();
		setActiveButtonPressed();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// lists stuff end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// scanner stuff begin
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	private void showScannerView() {
		_scannerLayout = new RelativeLayout(this);
		setBackgroundColor(Color.rgb(255, 255, 255));
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = 0;
		layoutParams.topMargin = _navBarHeight;
		layoutParams.width = _contentViewHeight - _navBarHeight;
		layoutParams.height = _contentViewHeight - _navBarHeight;
		_contentLayout.addView(_scannerLayout, layoutParams);

		_autoFocusHandler = new Handler();
		_camera = getCameraInstance();

		final int rotation = getWindowManager().getDefaultDisplay()
				.getOrientation();

		if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
			if (rotation == Surface.ROTATION_0)
				_camera.setDisplayOrientation(90);
			else if (rotation == Surface.ROTATION_180)
				_camera.setDisplayOrientation(270);
		} else // tablets
		{
			if (rotation == Surface.ROTATION_90)
				_camera.setDisplayOrientation(270);
			else if (rotation == Surface.ROTATION_270)
				_camera.setDisplayOrientation(90);
		}

		_scanner = new ImageScanner();
		_scanner.setConfig(0, Config.X_DENSITY, 3);
		_scanner.setConfig(0, Config.Y_DENSITY, 3);
		_scanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
		_scanner.setConfig(Symbol.EAN13, Config.ENABLE, 1);
		_scanner.setConfig(Symbol.UPCA, Config.ENABLE, 1);

		int backgroundWidth = (int) (_contentViewWidth * .95);
		int backgroundHeight = (int) (_contentViewHeight * .5);
		int backgroundYOrigin = (_contentViewHeight - backgroundHeight) / 2;
		int cameraWidth = (int) (backgroundWidth * .95);
		int cameraHeight = (int) (backgroundHeight * .86);

		layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = backgroundYOrigin
				+ (int) (backgroundHeight * .02);
		layoutParams.leftMargin = (_contentViewWidth - cameraWidth) / 2;
		layoutParams.width = cameraWidth;
		layoutParams.height = cameraHeight;

		_cameraPreview = new CameraPreview(this, this, autoFocusCB);
		_scannerLayout.addView(_cameraPreview, layoutParams);
		_cameraPreview.setCamera(_camera);
		_cameraPreview.showSurfaceView();
		_previewing = true;

		try {
			SizedImageView backgroundView = new SizedImageView(this,
					backgroundWidth, backgroundHeight);
			backgroundView.setBackgroundDrawable(new BitmapDrawable(_app
					.getAppBitmap("scanner_dialog", R.drawable.scan_dialog,
							backgroundWidth, backgroundHeight)));
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = (_contentViewHeight - backgroundYOrigin) / 4;
			layoutParams.leftMargin = (_contentViewWidth - backgroundWidth) / 2;
			_scannerLayout.addView(backgroundView, layoutParams);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SizedTextView titleText = new SizedTextView(this, backgroundWidth,
				(int) (backgroundHeight * .12), Color.TRANSPARENT, Color.BLACK,
				_normalFont, "Please Scan Your Item", Align.CENTER);
		layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = backgroundYOrigin
				- (int) (backgroundHeight * .12);
		layoutParams.leftMargin = (_contentViewWidth - backgroundWidth) / 2;
		_scannerLayout.addView(titleText, layoutParams);

		try {
			// CLP SDK Menu opened - Scan
			JSONObject data = new JSONObject();
			data.put("event_name", "MenuOpened");
			data.put("link_clicked", "Scan");
			data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
			_app.clpsdkObj.updateAppEvent(data);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Camera getCameraInstance() {
		Camera c = null;

		try {
			c = Camera.open();
		} catch (Exception e) {
		}

		return c;
	}

	private void restartCamera() {
		_camera.setPreviewCallback(this);
		_camera.startPreview();
		_previewing = true;
		_camera.autoFocus(autoFocusCB);
	}

	private void releaseCamera() {
		if (_camera != null) {
			if (_cameraPreview != null) {
				_cameraPreview.setCamera(null);
			}
			_camera.cancelAutoFocus();
			_camera.setPreviewCallback(null);
			_camera.stopPreview();
			_camera.release();
			_previewing = false;
			_camera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		@Override
		public void run() {
			if (_previewing)
				_camera.autoFocus(autoFocusCB);
		}
	};

	// Mimic continuous auto-focusing
	Camera.AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			_autoFocusHandler.postDelayed(doAutoFocus, 1000);
			// Toast.makeText(context, "Scan finished", Toast.LENGTH_SHORT)
			// .show();
		}
	};

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		// this method sometimes gets called multiple times for the same scan...
		synchronized (this) {

			Camera.Parameters parameters = camera.getParameters();
			Camera.Size size = parameters.getPreviewSize();
			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);
			final int result = _scanner.scanImage(barcode);

			if (result != 0) {

				checkFirstTime = true;
				_camera.stopPreview();
				_camera.setPreviewCallback(null);
				_previewing = false;
				_camera.cancelAutoFocus();
				SymbolSet syms = _scanner.getResults();

				for (Symbol sym : syms) {
					String symData = sym.getData();

					if (symData.length() > 13) // scanner sends back some weird
												// stuff occasionally
					{
						Log.i(getClass().getSimpleName(),
								"Invalid barcode length " + symData.length());
						return;
					}

					Log.i(getClass().getSimpleName(), "Barcode read: "
							+ symData);

					getProduct(symData);

				}

			} else if (result == 0) {

				if (checkFirstTime == true) {

					checkFirstTime = false;
					firstCall = new Date();
					firstCallTime = (int) firstCall.getTime();

				} else if (checkFirstTime == false) {

					intervalCall = new Date();
					intervalCallTime = (int) intervalCall.getTime();
					currInterval = (intervalCallTime - firstCallTime) / 1000;

					if (currInterval > 20) {

						if (scanAlertOpen == true) {
							scanAlertOpen = false;
							showTextDialog(maincontext, "Scanning Error",
									"Barcode not readable, please try again",
									"hideOpenDialog");

							return;
						}
					}

				}
			}
		}
	}

	public void hideOpenDialog() {

		checkFirstTime = true;
		scanAlertOpen = true;
		dismissTextDialog();
		currInterval = 0;

	}

	public void getProduct(String barcode) {

		if (_service != null && _service._isActive == true)
			return;

		_barcode = barcode;
		ProductRequest request = new ProductRequest();
		request.upc = barcode;
		request.storeNumber = _login.storeNumber;
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		showProgressDialog("Retrieving product data...");
		_service = new WebService(this, Product.class, requestBody,
				_login.authKey);
		_service.execute(_app.PRODUCT_FOR_UPC_URL);
		// handled by handleProductByUPCServiceResponse() directly below
	}

	public void handleProductByUPCServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status != 200) {
				WebServiceError error = _service.getError();
				if (status == 422) { // backend or http error
					showTextDialog(this, "Product Search Failed",
							error.errorMessage, "camerraRestartError");
					error_message = error.errorMessage;
					return;
				} else {
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG,
							"camerraRestartError");
					error_message = "Http Status code: " + status;
					return;
					// showTextDialog(this, "Server Error", "Http Status code: "
					// + status);

				}
			}
			int productDetaiType = 0;
			boolean found = false;
			Product product = (Product) _service.getResponseObject();

			if (product == null) {
				showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error",
				// "Unable to parse data returned from server.");
				return;
			}

			try {
				// CLP SDK Scan Product
				JSONObject eventData = new JSONObject();
				eventData.put("store", product.storeNumber);
				eventData.put("barcode", product.upc);

				eventData.put("SKU", product.sku);
				eventData.put("item_id", product.upc);
				eventData.put("item_name", product.description);
				eventData.put("brand", product.brand);
				eventData.put("price", String.valueOf(product.regPrice));
				eventData
						.put("promo_price", String.valueOf(product.promoPrice));
				eventData.put("category", product.mainCategory);

				eventData.put("event_name", "ProductScanned");
				eventData.put("event_time",
						_app.clpsdkObj.formatedCurrentDate());
				_app.clpsdkObj.updateAppEvent(eventData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// CLP END

			if (_app._currentShoppingList != null
					&& _app._currentShoppingList.productList != null
					&& _app._currentShoppingList.productList.size() > 0) {
				for (Product listProduct : _app._currentShoppingList.productList) {
					if (listProduct.sku.equals(product.sku)) {
						found = true;
						_app._currentProductDetail = listProduct;
						productDetaiType = Product.PRODUCT_MODIFY;
						break;
					}
				}
			}

			if (found == false) {
				_app._currentProductDetail = product;
				productDetaiType = Product.PRODUCT_ADD;
			}

			if (_app._currentProductDetail.imagePath != null)
				_app.getImageAsync(_app._currentProductDetail.imagePath); // checks
																			// for
																			// cached
																			// image
																			// first

			new WaitForProductByUPCImage(this, productDetaiType).execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void camerraRestartError() {

		dismissTextDialog();
		restartCamera();
		// newsdk barcodefail
		/*
		 * // CLP SDK try { ArrayList<NameValuePair> param = new
		 * ArrayList<NameValuePair>(); param.add(new
		 * BasicNameValuePair("Barcode_Value", _barcode)); param.add(new
		 * BasicNameValuePair("Error_Message", error_message)); param = new
		 * 
		 * } catch (Exception e) { Log.e(_app.ERROR, "SCAN_BARCODE_FAIL:" +
		 * e.getMessage()); } // CLP
		 */
	}

	private class WaitForProductByUPCImage extends AsyncTask<Void, Void, Void> {
		private Context _context;
		private int _productDetailType;

		public WaitForProductByUPCImage(Context context, int productDetailType) {
			_context = context;
			_productDetailType = productDetailType;
		}

		@Override
		protected Void doInBackground(Void... params) {
			while (_app.imageThreadsDone() == false) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// _productDetailView = new ProductDetailView(_context,
			// _app._currentProductDetail, _currentProductCategory,
			// _productDetailType, "updateShoppingList");
			// _productDetailView.show();
			try {
				Intent new_intent = new Intent(_context, ShowItemDetails.class);
				new_intent.putExtra("productDetaiType", _productDetailType);
				new_intent.putExtra("currentpage", "productpage");
				_context.startActivity(new_intent);

			} catch (Exception ex) {
				Log.e("showProductDetail", ex.getMessage());
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// scanner stuff end
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private class WaitForShoppingList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			int seconds = 0;

			while (_app._retrievingShoppingList == true
					&& seconds < WebService.SERVICE_TIMEOUT) {
				try {
					Thread.sleep(1000);
					seconds++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// if (_app._currentShoppingList != null)
			// setFooterDetails();
			// else{
			// Log.i(getClass().getSimpleName(),
			// "WaitForShoppingListThread did not get shopping list");

		}
	}

	private class WaitForOffers extends AsyncTask<Void, Void, Void> {
		private String _offerUrl;

		WaitForOffers(String offerUrl) {
			_offerUrl = offerUrl;
		}

		@Override
		protected Void doInBackground(Void... params) {
			while (_app.offerThreadsDone() == false) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dismissActiveDialog();

			if (_offerUrl == _app.OFFERS_PERSONALIZED_URL)
				showAvailableOffers();
			else
				// _app.OFFERS_ACCEPTED_URL
				showAcceptedOffers();
		}
	}

	private class WaitForProductImages extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			int checkInterval = 100;
			int totalWaitTime = 0;

			while (_app.imageThreadsDone() == false) {
				try {
					if (totalWaitTime > 5000) // only wait 5 seconds total
												// before going back to the view
						break;

					totalWaitTime += checkInterval;
					Thread.sleep(checkInterval);
				}

				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dismissActiveDialog();
			restorePreviousView();
			if (forwardButtonPressed) {
				focusOnGridView();// set gridview to last visible position
				forwardButtonPressed = false;
			}
		}
	}

	void restorePreviousView() {
		if (_app._showProductList == true) {
			_app._showProductList = false;
			showProducts();
		} else if (_app._showShoppingList == true) {
			_app._showShoppingList = false;
			showShoppingList();
		} else if (_scannerLayout != null) {
			restartCamera();
		}
	}

	float calculateTotalPrice(Product product) {
		float value = 0f;

		try {
			if (product.promoType > 0) {
				float unitPrice = product.promoPrice / product.promoForQty;
				value = unitPrice * product.qty;
			} else {
				value = product.regPrice * product.qty;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return value;
	}

	private class AisleComparator implements Comparator<Product> {
		@Override
		public int compare(Product product1, Product product2) {
			try {
				return (product1.aisleNumber > product2.aisleNumber ? 1
						: (product1.aisleNumber == product2.aisleNumber ? 0
								: -1));
			} catch (Exception ex) {
				ex.printStackTrace();
				return -1;
			}
		}
	}

	private class MoreMenu extends RelativeLayout {
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
						if (checkkeyboardVisibility == true) {
							hideSoftInputMode();
						}
						accountButtonPressed();
					}
				});
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = 0;
				layoutParams.leftMargin = 0;
				addView(_accountButton, layoutParams);

				_signOutButton = new SizedImageTextButton(context,
						menuButtonBottomBitmap, menuButtonBottomSelectedBitmap,
						Color.BLACK, menuFont, menuButtonFontSize, "Sign Out");
				_signOutButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (checkkeyboardVisibility == true) {
							hideSoftInputMode();
						}
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

	// OnGestureListener methods
	@Override
	public boolean onDown(MotionEvent e) {
		hideMoreMenu();
		clearMenus();
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	// WebServiceListener method
	@Override
	public void onServiceResponse(Object responseObject) {
		if (responseObject instanceof Product) {
			handleProductByUPCServiceResponse();
		} else if (responseObject instanceof ProductsForCategoryResponse) {
			handleProductServiceResponse();
		} else if (responseObject instanceof ShoppingList) {
			handleListServiceResponse();
		} else if (responseObject instanceof ShoppingListName) {
			handleListNamesServiceResponse();
		} else if (responseObject instanceof ListCreateRequest) {
			handleCreateListServiceResponse();
		} else if (responseObject instanceof ListDeleteRequest) {
			handleDeleteListServiceResponse();
		} else if (responseObject instanceof OfferAcceptRequest) {
			handleOfferAcceptServiceResponse();
		} else if (responseObject instanceof EcartPreOrderResponse) {
			handleEcartPreOrderServiceResponse();
		} else if (responseObject instanceof AccountRequest) {
			handleAccountServiceResponse();
		} else if (responseObject instanceof ProductCategory) {
			handleProductCategoriesServiceResponse();
		}
	}

	@Override
	public void onBackPressed() {

		if (_backButton != null && _backButton.getVisibility() == View.VISIBLE) {
			_backButton.performClick();
			// _searchText.setText("");
		} else if (_ecartButton != null
				&& _ecartButton.getVisibility() == View.VISIBLE) {
			_ecartButton.performClick();
		} else if (_backButton != null
				&& _backButton.getVisibility() == View.GONE) {

			showTextDialog(maincontext, "CLOSE_APP", "Do you want to exit?",
					"exitYes", "exitNo");
			return;
		}

	}

	public void exitYes() {
		dismissTextDialog();
		moveTaskToBack(true);
	}

	public void exitNo() {
		dismissTextDialog();
	}

	public void hideSoftInputMode() {

		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				_categoryMenuLayout.getWindowToken(), 0);

	}
}
