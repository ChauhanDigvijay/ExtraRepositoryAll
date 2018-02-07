package com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp.SignUpStoreDetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreDetail.StoreDetailActivity;
import com.olo.jambajuice.Adapters.StoreLocatorAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationUpdatesCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.StoreLocatorService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.Location.LocationManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.DEFAULT_SEARCH_LOCATION;
import static com.olo.jambajuice.Utils.SharedPreferenceHandler.LastSearchedLocation;

public class StoreLocatorActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, StoreServiceCallback, LocationUpdatesCallback {
    public JambaApplication _app;
    boolean isUserLocation;
    AlertDialog dialog;
    //    private static final String MAP_FRAGMENT_TAG = "MAP";
//    private GoogleMap googleMap;
    private Dialog errorDialog;
    private StoreLocatorAdapter storeLocatorAdapter;
    private List<Store> stores;
    //    private SupportMapFragment mapFragment;
    private HashMap<String, Store> storeLocations;
    private EditText searchEditText;
    private ImageView clearSearchTextImage;
    private ImageView locationImage;
    private ListView listView;
    private RelativeLayout store_selection_jambaLogoLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);

        _app = (JambaApplication) getApplication();
        isShowBasketIcon = false;

        if (isItFromMenuScreen()) {
            createNewOrderBySelectingNewStore();
        } else if (isItFromMenuScreenIfNoPreferredStore()) {
            createNewOrderWhenNoPrefferedStore();
        }

        ImageView store_selection_jambaLogo = (ImageView) findViewById(R.id.store_selection_jambaLogo);
        store_selection_jambaLogo.setImageResource(R.drawable.signed_in_logo);
        store_selection_jambaLogoLayout = (RelativeLayout) findViewById(R.id.store_selection_jambaLogoLayout);

        ImageView storeBack = (ImageView) findViewById(R.id.storeBack);
        createToolBar();
        stores = new ArrayList<>();
        storeLocations = new HashMap<>();
        setUpListView();


//        setUpScrollView();
//        Button menuButton = (Button) findViewById(R.id.menuBtn);
//        menuButton.setOnClickListener(this);
//        Button storeBtn = (Button) findViewById(R.id.storeBtn);
//        storeBtn.setOnClickListener(this);
        setUpSearchView();
        setUpButtonsView();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_STORE_LOCATOR_ACTIVITY));
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_LOCATION_SERVICE_NOT_AVAILABLE));


        //Jamba Logo Action
        store_selection_jambaLogo.setOnClickListener(this);
        storeBack.setOnClickListener(this);
//        new Handler().post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                initilizeMap();
//            }
//        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void createNewOrderBySelectingNewStore() {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Choose Store");
        alertDialogBuilder.setMessage("Please select search and select a store where your order will be fulfilled ");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (searchEditText != null) {
                    searchEditText.requestFocus();
                }
            }
        });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                onBackPressed();
//            }
//        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createNewOrderWhenNoPrefferedStore() {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Choose Store");
        alertDialogBuilder.setMessage("Your preferred store doesn't offer Order Ahead. Please select a different store for your order");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (searchEditText != null) {
                    searchEditText.requestFocus();
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setUpButtonsView() {
//        ImageView orderAheadMap = (ImageView) findViewById(R.id.orderAheadMap);
//        BitmapUtils.loadBitmapResourceWithViewSize(orderAheadMap, R.drawable.order_ahead_map, false);
        Button continueOnMaps = (Button) findViewById(R.id.continueOnMaps);
        TextView cannotFindMessage = (TextView) findViewById(R.id.cannotFindMessage);
//        if (isChooseStoreForSignUp()) {
//            cannotFindMessage.setText("Cannot find your preferred store?");
//            continueOnMaps.setText("Browse Menu");
//            continueOnMaps.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean(Constants.B_OPEN_MENU, true);
//                    TransitionManager.transitFrom(StoreLocatorActivity.this, HomeActivity.class, true, bundle);
//                }
//            });
//        } else

        if (isChooseStoreFromProductDetail()) {
            continueOnMaps.setVisibility(View.GONE);
            cannotFindMessage.setVisibility(View.GONE);
        } else {
            continueOnMaps.setOnClickListener(this);
            if (isChooseStoreForSignUp()) {
                cannotFindMessage.setText("Cannot find your preferred store?");
            }
        }
    }

    private void createToolBar() {
        if (isChooseStoreForSignUp()) {
            setUpToolBar(true, false);
            setBackButton(false, false);
            store_selection_jambaLogoLayout.setVisibility(View.GONE);
        } else {
            setUpToolBar(true, true);
            setBackButton(true, false);
            store_selection_jambaLogoLayout.setVisibility(View.VISIBLE);
        }
        isShowBasketIcon = false;
        if (isChooseStoreFromProductDetail()) {
            setTitle("Select Preferred Store", getResources().getColor(android.R.color.white));
            toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
            viewAdjustmentForStoreChooser();
        } else if (isChooseStoreForSignUp()) {
            setTitle("Select Preferred Store");
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            ViewGroup.LayoutParams params = progressBar.getLayoutParams();
            viewAdjustmentForStoreChooser();
        } else {
            RelativeLayout searchView = (RelativeLayout) findViewById(R.id.searchView);
            searchView.setBackgroundColor(getResources().getColor(R.color.toolbar_bg));
            toolbar.setVisibility(View.GONE);
        }
    }

    private void viewAdjustmentForStoreChooser() {
        View rootView = findViewById(R.id.rootView);
        rootView.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    private void setUpListView() {
        listView = (ListView) findViewById(R.id.listView);
        setListViewHeightBasedOnChildren(listView);
    }

    private void setUpSearchView() {
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchEditText.clearFocus();
        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchStoresAtLocation(searchEditText.getText().toString());
                return false;
            }
        });


        clearSearchTextImage = (ImageView) findViewById(R.id.cancelImage);
        locationImage = (ImageView) findViewById(R.id.locationImage);
        clearSearchTextImage.setOnClickListener(this);
        locationImage.setOnClickListener(this);
        clearSearchTextImage.setVisibility(View.INVISIBLE);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearSearchTextImage.setVisibility(View.VISIBLE);
                    locationImage.setVisibility(View.GONE);
                } else {
                    clearSearchTextImage.setVisibility(View.GONE);
                    locationImage.setImageResource(R.drawable.gps_pointer_grey);
                    locationImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (stores.get(position) != null) {

            startStoreDetailActivity(stores.get(position));

            //Track On select Favourate Store
            //JambaAnalyticsManager.sharedInstance().track_Favourate_Store();

            //dj Store Locator  (StoreLocatorScreen)


        }
    }

    private void startStoreDetailActivity(Store store) {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean(Constants.B_IS_FROM_SETTINGS, false);
        bundle.putSerializable(Constants.B_STORE, store);
        if (isChooseStoreForSignUp()) {
            TransitionManager.transitFrom(this, SignUpStoreDetailActivity.class, bundle);
        } else {
            // TransitionManager.transitFrom(this, StoreDetailActivity.class, bundle);
            if (isPreferredStoreDetail()) {
                this.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                Intent i = new Intent(this, StoreDetailActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            } else {

                TransitionManager.transitFrom(this, StoreDetailActivity.class, bundle);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.menuBtn:
//                trackButtonWithName("Menu");
//                TransitionManager.slideUp(StoreLocatorActivity.this, MenuActivity.class);
//                finish();
//                break;
//            case R.id.storeBtn:
//                trackButtonWithName("Stores");
//                onBackPressed();
//                break;
            case R.id.cancelImage:
                clearSearchText();
                break;
            case R.id.continueOnMaps:
                Utils.openMap(this, searchEditText.getText().toString());
                break;
            case R.id.store_selection_jambaLogo:
                goToDashBoard();
                break;
            case R.id.storeBack:
                onBackPressed();
                break;
            case R.id.locationImage:
                if(HomeActivity.homeActivity != null) {
                    HomeActivity.homeActivity.checkPermissionsforlocation(this);
                }
                if (Utils.checkEnableGPS(this)) {
                    storeSearchBasedOnuserLocation();
                }
                break;
        }
    }

    private void storeSearchBasedOnuserLocation() {
        clearStoreData();


        updateList();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        locationImage.setImageResource(R.drawable.gps_pointer_blue);
        LocationManager locationManager = LocationManager.getInstance(this);
        if (locationManager.isLocationServicesEnabled()) {
            locationManager.startLocationUpdates(this);
        } else {
            //Notify user that location service is not enabled
            locationManager.showLocationServiceNotAvailableAlert();
        }
    }

    private void goToDashBoard() {
        if (UserService.isUserAuthenticated()) {
            TransitionManager.transitFrom(this, HomeActivity.class, true);
        }
    }

    private void clearSearchText() {
        if (searchEditText == null) {
            searchEditText = (EditText) findViewById(R.id.searchEditText);
        }
        searchEditText.setText("");
    }

    //Callbacks
    @Override
    public void onLocationCallback(Location location) {
        if (location != null) {
            LocationManager.getInstance(this).stopLocationUpdates();
            double lat = location.getLatitude();
            double lng = location.getLongitude();
//            moveMapToLocation(lat, lng);
            StoreLocatorService.findStoresNearLocation(lat, lng, this, "user_location");
            //isUserLocation = true;
        }
    }

    @Override
    public void onConnectionFailedCallback() {

    }

    @Override
    public void onStoreServiceCallback(ArrayList<Store> searchedStores, Exception exception) {
        updateStoreData(searchedStores);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        RelativeLayout noStoreAvailableView = (RelativeLayout) findViewById(R.id.noStoreAvailableView);
        noStoreAvailableView.setVisibility(View.VISIBLE);
//        TextView sorryMessage = (TextView) findViewById(R.id.sorryMessage);
//        sorryMessage.setVisibility(View.VISIBLE);
        if (stores != null && stores.size() > 0) {
            if (storeLocatorAdapter == null) {
                storeLocatorAdapter = new StoreLocatorAdapter(this, stores);
                listView.setAdapter(storeLocatorAdapter);
                listView.setOnItemClickListener(this);
                setListViewHeightBasedOnChildren(listView);
            }
//            sorryMessage.setVisibility(View.GONE);
//            addMarkerOnMaps(stores);
//            setMapMarkerAdapter();
            //noStoreAvailableView.setVisibility(View.GONE);
        }
        //        else if (stores != null && stores.size() == 0)
        //        {
        //            noStoreAvailableView.setVisibility(View.VISIBLE);
        //        }
        if (exception != null) {
            //Incase we dont have to show no store available when user is searching location other than US.
            //            if (Utils.getErrorCode(exception) == Constants.ERROR_US_LOCATIONS)
            //            {
            //                noStoreAvailableView.setVisibility(View.GONE);
            //            }
            //            else
            //            {
            //                noStoreAvailableView.setVisibility(View.VISIBLE);
            //            }
            Utils.showErrorAlert(this, exception);
        }
        updateList();
    }

//    private void setMapMarkerAdapter()
//    {
//        if (googleMap != null)
//        {
//            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
//            {
//
//                @Override
//                public View getInfoWindow(Marker arg0)
//                {
//                    return null;
//                }
//
//                @Override
//                public View getInfoContents(Marker arg0)
//                {
//                    View v = getLayoutInflater().inflate(R.layout.layout_marker_info_window, null);
//                    LatLng latLng = arg0.getPosition();
//
//                    Store store = storeLocations.get(latLng.toString());
//                    TextView title = (TextView) v.findViewById(R.id.title);
//                    TextView snippet = (TextView) v.findViewById(R.id.snippet);
//                    ImageView orderAhead = (ImageView) v.findViewById(R.id.order_ahead);
//
//                    //orderAhead.setVisibility(View.GONE);
//                    //TODO uncomment to show order adhead icon.
//                    if (store.isSupportsOrderAhead())
//                    {
//                        orderAhead.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        orderAhead.setVisibility(View.GONE);
//                    }
//                    title.setText(store.getName());
//                    snippet.setText(store.getCity());
//                    return v;
//                }
//            });
//        }
//    }

    private void searchStoresAtLocation(String location) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        if (!location.equals("")) {
            SharedPreferenceHandler.put(LastSearchedLocation, location);
            preSearchOperation();
            if(FBPreferences.sharedInstance(getApplicationContext()).getAccessToken() != null) {
                JambaAnalyticsManager.sharedInstance().track_ItemWith("", location, FBEventSettings.STORE_SEARCH);
            }else{
                Utils.getTokenAndSendEventsWithItem(this,location,FBEventSettings.STORE_SEARCH);
            }
            StoreLocatorService.findStoresByLocationName(location, this);
        }
        searchEditText.clearFocus();
    }

    private void preSearchOperation() {
        clearStoreData();
        updateList();
//        clearMap();
        LocationManager.getInstance(this).stopLocationUpdates();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RelativeLayout noStoreAvailableView = (RelativeLayout) findViewById(R.id.noStoreAvailableView);
        noStoreAvailableView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap)
//    {
//        this.googleMap = googleMap;
//        if (googleMap != null)
//        {
//            googleMap.setMyLocationEnabled(true);
//            googleMap.setOnMyLocationButtonClickListener(this);
//        }
//    }

//    @Override
//    public boolean onMyLocationButtonClick()
//    {
//        isUserLocation = false;
//        SharedPreferenceHandler.put(LastSearchedLocation, "");
//        searchEditText.setText("");
//        preSearchOperation();
//        setUpLocationSearch();
//        return false;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isItFromMenuScreen() == false && isItFromMenuScreenIfNoPreferredStore() == false && isItBasketStoreDetailScreen() == false) {
            setUpLocationSearch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationManager.getInstance(this).stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearStoreData();
    }

    //Private Methods
    private void setUpLocationSearch() {
        if (stores.size() == 0) {
            if (storeLocatorAdapter != null) {
                storeLocatorAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);
            }
            LocationManager locationManager = LocationManager.getInstance(this);
            String lastSearched = SharedPreferenceHandler.getInstance().getString(LastSearchedLocation, null);
            if (lastSearched != null && !lastSearched.isEmpty()) {
                if (searchEditText != null) {
                    searchEditText.setText(lastSearched);
                }
                searchStoresAtLocation(lastSearched);
            } else if (locationManager.isLocationServicesEnabled()) {
                //AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.SEARCH.value, "store_search", "user_location");

                LocationManager.getInstance(this).startLocationUpdates(this);
            } else {
                //Notify user that location service is not enabled
                locationManager.showLocationServiceNotAvailableAlert();
                searchStoresAtLocation(DEFAULT_SEARCH_LOCATION);
            }
        }
    }

//    private void moveMapToLocation(double lat, double lng)
//    {
//        if (googleMap != null)
//        {
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(15.5f).build();
//            CameraUpdate center = CameraUpdateFactory.newCameraPosition(cameraPosition);
//            googleMap.animateCamera(center, 2000, null);
//        }
//    }

//    private void setUpScrollView()
//    {
//        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
//        scrollView.post(new Runnable()
//        {
//            public void run()
//            {
//                scrollView.scrollTo(0, 0);
//            }
//        });
//
//        View transparentView = findViewById(R.id.transparentView);
//        transparentView.setOnTouchListener(new View.OnTouchListener()
//        {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                int action = event.getAction();
//                switch (action)
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        // Disallow ScrollView to intercept touch events.
//                        scrollView.requestDisallowInterceptTouchEvent(true);
//                        // Disable touch on transparent view
//                        return false;
//
//                    case MotionEvent.ACTION_UP:
//                        // Allow ScrollView to intercept touch events.
//                        scrollView.requestDisallowInterceptTouchEvent(false);
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        scrollView.requestDisallowInterceptTouchEvent(true);
//                        return false;
//
//                    default:
//                        return true;
//                }
//            }
//        });
//    }

//    private void initilizeMap()
//    {
//        if (!isChooseStore())
//        {
//            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
//            if (status == ConnectionResult.SUCCESS)
//            {
//                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
//                if (mapFragment == null)
//                {
//                    mapFragment = SupportMapFragment.newInstance();
//                    mapFragment.getMapAsync(this);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.mapContainer, mapFragment, MAP_FRAGMENT_TAG).commit();
//                }
//            }
//            else
//            {
//                dismissErrorDialog();
//                int requestCode = 10; // Show Missing Play Services Dialog
//                errorDialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
//                errorDialog.show();
//            }
//        }
//        else
//        {
//            hideMap();
//        }
//    }

//    private void hideMap()
//    {
//        LinearLayout btns = (LinearLayout) findViewById(R.id.btns);
//        View transparentView = findViewById(R.id.transparentView);
//        View mapContainer = findViewById(R.id.mapContainer);
//        btns.setVisibility(View.GONE);
//        transparentView.setVisibility(View.GONE);
//        mapContainer.setVisibility(View.GONE);
//    }

    private void dismissErrorDialog() {
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }
    }

    private void setListViewHeightBasedOnChildren(final ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        int listHeight = 0;
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        if (myListAdapter != null) {
            int totalHeight = 0;
            for (int size = 0; size < myListAdapter.getCount(); size++) {

                View listItem = myListAdapter.getView(size, null, myListView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                }
                Display display = getWindowManager().getDefaultDisplay();
                Point winSize = new Point();
                display.getSize(winSize);
                int widthSpec = View.MeasureSpec.makeMeasureSpec(winSize.x, View.MeasureSpec.AT_MOST);
                listItem.measure(widthSpec, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            listHeight = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        }
        params.height = listHeight;
        myListView.setLayoutParams(params);
        myListView.requestLayout();
        //makeScrollViewScrollable(listHeight);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    private void makeScrollViewScrollable(final int listHeightFinal) {
        if (!isChooseStore()) {
            final RelativeLayout orderAheadView = (RelativeLayout) findViewById(R.id.orderAheadView);
            final RelativeLayout noStoreAvailableView = (RelativeLayout) findViewById(R.id.noStoreAvailableView);
            Utils.addOnGlobalLayoutListener(noStoreAvailableView, new Runnable() {
                @Override
                public void run() {
                    int[] location = new int[2];
                    orderAheadView.getLocationOnScreen(location);
                    Logger.i("layoutParams: " + location[0] + "::" + location[1]);

                    //Add extra space incase scrollview is not scrollable.
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
                    int totalViewHeight = (int) (location[1] + listHeightFinal + orderAheadView.getHeight() + orderAheadView.getPaddingTop() + orderAheadView.getPaddingBottom() + noStoreAvailableView.getMeasuredHeight() + Utils.dpToPx(5)); //5 for margin above listview
                    if (totalViewHeight < (screenHeight + getResources().getDimension(R.dimen.basket_icon_top_margin))) {
                        int actualListHeight = (int) ((screenHeight - totalViewHeight) + getResources().getDimension(R.dimen.basket_icon_top_margin));
                        ViewGroup.LayoutParams params = listView.getLayoutParams();
                        params.height = actualListHeight;
                        listView.setLayoutParams(params);
                        listView.requestLayout();
                    }
                }
            });
        }
    }

//    private void addMarkerOnMaps(List<Store> stores)
//    {
//        if (googleMap != null)
//        {
//            storeLocations.clear();
//            clearMap();
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (Store store : stores)
//            {
//                LatLng latlng = new LatLng(store.getLatitude(), store.getLongitude());
//                MarkerOptions options = new MarkerOptions().position(latlng);
//                options.title(store.getName());
//                options.snippet(store.getCity());
//                googleMap.addMarker(options);
//                builder.include(options.getPosition());
//                storeLocations.put(latlng.toString(), store);
//            }
//            LatLngBounds bounds = builder.build();
//            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
//            googleMap.animateCamera(cu);
//            googleMap.setOnInfoWindowClickListener(this);
//        }
//    }

    private void updateList() {
        if (storeLocatorAdapter != null) {
            storeLocatorAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(listView);
        }
    }

    //Store Helper Functions
    private void updateStoreData(ArrayList<Store> newStores) {
        clearStoreData();
        if (newStores != null) {
            if (isChooseStoreFromProductDetail()) {
                for (Store newStore : newStores) {
                    if (newStore.isSupportsOrderAhead()) {
                        stores.add(newStore);
                    }
                }
            } else if (isItFromMenuScreen()) {
                for (Store newStore : newStores) {
                    if (newStore.isSupportsOrderAhead()) {
                        stores.add(newStore);
                    }
                }
            } else if (isItBasketStoreDetailScreen()) {
                for (Store newStore : newStores) {
                    if (newStore.isSupportsOrderAhead()) {
                        stores.add(newStore);
                    }
                }
            } else {
                stores.addAll(newStores);
            }
        }
    }

    private void clearStoreData() {
        stores.clear();
    }

//    private void clearMap()
//    {
//        if (googleMap != null)
//        {
//            googleMap.clear();
//        }
//    }

    @Override
    protected void handleBroadCastReceiver(Intent intent) {
        if (intent != null && intent.getAction().equalsIgnoreCase(Constants.BROADCAST_LOCATION_SERVICE_NOT_AVAILABLE)) {
            showLocationServiceNotAvailableAlert();
        } else {
            finish();
        }
    }

    private boolean isChooseStoreFromProductDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_CHOOOSE_STORE_FROM_PROD_DETAIL);
        }
        return false;
    }

    private boolean isItFromMenuScreen() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN);
        }
        return false;
    }

    private boolean isItFromMenuScreenIfNoPreferredStore() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN_NO_PREFERRED_STORE);
        }
        return false;
    }

    private boolean isItBasketStoreDetailScreen() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_IT_FROM_BASKET_STORE_SCREEN);
        }
        return false;
    }

    private boolean isChooseStoreForSignUp() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_CHOOOSE_STORE_FROM_SIGN_UP);
        }
        return false;
    }

    private boolean isPreferredStoreDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_STORE_DETAIL_ONLY);
        }
        return false;
    }

    private boolean isChooseStore() {
        return isChooseStoreFromProductDetail() || isChooseStoreForSignUp();
    }

//    @Override
//    public void onInfoWindowClick(Marker marker)
//    {
//        LatLng latlng = marker.getPosition();
//        Store store = storeLocations.get(latlng.toString());
//        startStoreDetailActivity(store);
//    }

    public void showLocationServiceNotAvailableAlert() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        if (!isFinishing()) {
            dialog = new AlertDialog.Builder(this).create();
            dialog.setMessage("Your current location cannot be determined. Please ensure location services are enabled by clicking Settings.");
            dialog.setButton(Dialog.BUTTON_POSITIVE, "Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!isFinishing()) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                }
            });
            dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
    }

}
