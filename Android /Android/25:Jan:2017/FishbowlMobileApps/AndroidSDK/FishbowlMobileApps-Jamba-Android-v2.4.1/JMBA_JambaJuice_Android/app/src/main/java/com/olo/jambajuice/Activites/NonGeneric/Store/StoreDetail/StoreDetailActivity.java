package com.olo.jambajuice.Activites.NonGeneric.Store.StoreDetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.ProductFamiliesActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductDetailViewPagerActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.Activites.NonGeneric.Settings.SettingsActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.AllStoreMenuCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductAdDetailsServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductAdsServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreCalendarCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.SwitchBasketServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserUpdateCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.StoreTiming;
import com.olo.jambajuice.BusinessLogic.Models.TimeRange;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.BusinessLogic.Services.RecentOrdersService;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Models.OloBasketProduct;
import com.wearehathway.apps.olo.Models.OloSwitchBasket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.olo.jambajuice.BusinessLogic.Services.ProductService.TwentyFourHourInMiliSeconds;
import static com.olo.jambajuice.Utils.SharedPreferenceHandler.LastProductUpdate;
import static com.wearehathway.apps.olo.Utils.Constants.Server_Time_Format;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */

public class StoreDetailActivity extends BaseActivity implements View.OnClickListener, UserUpdateCallback, OnMapReadyCallback {

    public static StoreDetailActivity storeDetailActivity;
    Store selectedStore;
    Button makePreferred;
    Button startOrder;
    Button changeStore;
    Button selectStore;
    private GoogleMap googleMap;
    private boolean isFromPrefferedStore = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        storeDetailActivity = this;
        isShowBasketIcon = false;
        setUpToolBar(true);
        setBackButton(true, false);
        initilizeMap();
        setUpScrollView();
        fillViewWithData();
        setButtonsState();
        //addMarkerOnMap();
        createToolBar();
        fetchStoreTimingsIfRequired();
        if (selectedStore != null) {
            trackUXEvent("store_view", selectedStore.getName());
        }

    }

    private void createToolBar() {
        setUpToolBar(true);
        if (isFromPrefferedStore) {
            setTitle("Preferred Store");
        } else if (selectedStore != null) {
            String storename = selectedStore.getName();

            if (StringUtilities.isValidString(storename)) {
                if (isChooseStoreFromProductDetail()) {
                    if (DataManager.getInstance().isDebug) {
                        setTitle(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""), getResources().getColor(android.R.color.white));
                    } else {
                        setTitle(selectedStore.getName().replace("Jamba Juice ", ""), getResources().getColor(android.R.color.white));
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
                } else {
                    if (DataManager.getInstance().isDebug) {
                        setTitle(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""));
                    } else {
                        setTitle(selectedStore.getName().replace("Jamba Juice ", ""));
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        int id = v.getId();
        if (id == R.id.directionBtn) {
            //Utils.getDirection(this, selectedStore);
            if (HomeActivity.homeActivity != null) {
                HomeActivity.homeActivity.checkPermissionsforlocation(this);
            }
            if (Utils.checkEnableGPS(this)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.B_STORE, selectedStore);
                TransitionManager.transitFrom(this, StoreDirectionActivity.class, bundle);
            }
        } else if (id == R.id.callBtn) {
            Utils.showDialerConfirmation(this, selectedStore);
        } else if (id == R.id.startOrder) {
            startOrderPressed();
        } else if (id == R.id.makePreferred) {
            makePreferredPressed();
        } else if (id == R.id.changeStore) {
            //  changeStorePressedInBasketScreen();//This button will be visibile only if storedetail screen is from basket screen
            // and this functionality will just switch storedetail screen storelist(storeLocator) screen
            if (isPreferredStoreDetail()) {
                changePreferredStorePressed();
            } else {
                changeStorePressedInBasketScreen();//This button will be visibile only if storedetail screen is from basket screen
                // and this functionality will just switch storedetail screen storelist(storeLocator) screen
            }
        } else if (id == R.id.selectStore) {
            if (DataManager.getInstance().getCurrentBasket() != null && DataManager.getInstance().getCurrentBasket().getAppliedRewards() != null) {
                if (DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0
                        || StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {
                    confirmationAlert(Constants.ADD_PRODUCT_OR_REMOVE_PRODUCT_ALERT_MESSAGE);
                } else {
                    changeStorePressedInStoreSelectionScreen();
                }
            }

            //changeStorePressedInStoreSelectionScreen();//This button will be visibile only if storedetail screen is from storelist(storelocator) screen
            //and this functionality will make switchbasket from old store to new store
        }
    }

    private void confirmationAlert(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(StoreDetailActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                changeStorePressedInStoreSelectionScreen();
            }
        });
        dialog.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void basketTotalChangeAlert(String message, final OloBasket oloBasket) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(StoreDetailActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                enableScreen(false);
                loadMenuForChangedStore(oloBasket);//we are loading menu for new store in the background
            }
        });
        dialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void changePreferredStorePressed() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.B_IS_STORE_DETAIL_ONLY, true);
        TransitionManager.slideUp(StoreDetailActivity.this, StoreLocatorActivity.class, bundle);
    }


    private void changeStorePressedInStoreSelectionScreen() {

        int size = 0;

        if (DataManager.getInstance().getCurrentBasket() != null) {
            if (DataManager.getInstance().getCurrentBasket().getProducts() != null) {
                size = DataManager.getInstance().getCurrentBasket().getProducts().size();
            }
        }

        if (size == 0) {
            selectNewOrder();
        } else {
            enableScreen(false);
            BasketService.switchBasket(this, selectedStore.getRestaurantId(), DataManager.getInstance().getCurrentBasket().getId(), new SwitchBasketServiceCallBack() {
                @Override
                public void onSwitchBasketServiceCallback(OloSwitchBasket switchBasket, Exception e) {
                    enableScreen(true);
                    if (switchBasket != null) {
                        OloBasket basket = switchBasket.getBasket();
                        ArrayList<OloBasketProduct> itemsnottransferred = switchBasket.getItemsnottransferred();
                        if (basket != null && itemsnottransferred.size() == 0) {
                            Basket oldBasket = DataManager.getInstance().getCurrentBasket();
                            float difference = Math.abs(basket.getTotal() - oldBasket.getTotal());
                            if (difference == 0) {
                                enableScreen(false);
                                loadMenuForChangedStore(basket);//we are loading menu for new store in the background
                            } else {
                                if (!storeDetailActivity.isFinishing()) {
                                    basketTotalChangeAlert("The price of one or more items in your basket has changed. The new total is: " + Utils.formatPrice(basket.getTotal()), basket);
                                }
                            }

                        } else {
                            enableScreen(true);
                            if (!storeDetailActivity.isFinishing()) {
                                createNewOrderAlert();
                            }
                        }
                    } else {
                        enableScreen(true);
                        if (!storeDetailActivity.isFinishing()) {
                            createNewOrderAlert();
                        }
                    }

                }
            });
        }
    }

    private void loadMenuForChangedStore(final OloBasket basket) {
        ProductService.startNewOrderForStore(this, selectedStore, new AllStoreMenuCallBack() {
            @Override
            public void onAllStoreMenuCallback(Exception exception) {
                enableScreen(true);
                BasketService.setBasket(basket);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.B_IS_IT_FROM_BASKET_STORE_SCREEN, true);// this bundle is used for clearing previous screens which is from basket screen
                TransitionManager.transitFrom(StoreDetailActivity.this, BasketActivity.class, bundle);
                finish();
            }

            @Override
            public void onAllStoreMenuErrorCallback(Exception exception) {
                enableScreen(true);
                if (exception != null) {
                    Utils.showErrorAlert(StoreDetailActivity.this, exception);
                }
            }
        });
    }

    private void createNewOrderAlert() {
        if(!storeDetailActivity.isFinishing()) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Not all the products in your basket are available in the new store. Do you want to empty your basket and start a new order? " +
                    "or Do you want to keep existing basket and revert store selection?");
            alertDialogBuilder.setTitle("Restart Order");
            alertDialogBuilder.setPositiveButton("Start New Order", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    selectNewOrder();
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel Store Change", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    TransitionManager.transitFrom(StoreDetailActivity.this, BasketActivity.class, true);
                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void changeStorePressedInBasketScreen() {

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.B_IS_IT_FROM_BASKET_STORE_SCREEN, true);
        TransitionManager.transitFrom(StoreDetailActivity.this, StoreLocatorActivity.class, bundle);
        finish();

//        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Please select search and select a store where your order will be fulfilled ");
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(Constants.B_IS_IT_FROM_STORE_DETAIL_SCREEN, true);
//                TransitionManager.transitFrom(StoreDetailActivity.this, StoreLocatorActivity.class, bundle);
//                finish();
//            }
//        });
//        alertDialogBuilder.setNegativeButton("Cancel", null);
//        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }

    private void makePreferredPressed() {
        enableScreen(false);
        if (Integer.parseInt(selectedStore.getStoreCode()) != Integer.parseInt(UserService.getUser().getFavoriteStoreCode())) {

            setMakePreferredEnabled(false);
            if (DataManager.getInstance().isDebug) {
                JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(Integer.parseInt(selectedStore.getStoreCode())), Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""), FBEventSettings.UPDATE_FAVORITE_STORE);
            } else {
                JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(Integer.parseInt(selectedStore.getStoreCode())), selectedStore.getName().replace("Jamba Juice ", ""), FBEventSettings.UPDATE_FAVORITE_STORE);
            }
            UserService.updateFavoriteStore(selectedStore, this);
        } else {

            enableScreen(true);

            Utils.showAlert(this, selectedStore.getName() + " is already your preferred store.");
        }
    }

    private void startOrderPressed() {
        if (!selectedStore.isSupportsOrderAhead()) {
            Utils.showAlert(this, selectedStore.getName() + " does not support order ahead. Please chose a different store.");
        } else {
            if (DataManager.getInstance().isBasketPresent()) {
                showDeleteBasketConfirmation();
            } else {
                selectNewOrder();
            }
        }
    }

    private void showDeleteBasketConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Starting a new order will empty the basket and cancel your current order. Continue?");
        alertDialogBuilder.setTitle("Start New Order");
        alertDialogBuilder.setPositiveButton("Start New Order", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selectNewOrder();
            }
        });
        alertDialogBuilder.setNegativeButton("No", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void selectNewOrder() {
//        DataManager.getInstance().resetDataManager();
//        setOrderButtonEnabled(false);
//        StoreService.startNewOrder(this, selectedStore, new StartOrderCallback() {
//            @Override
//            public void onStartOrderCallback(Exception exception) {
//                if (exception != null) {
//                    setOrderButtonEnabled(true);
//                    Utils.showErrorAlert(StoreDetailActivity.this, exception);
//                } else {
//                    transitToPreviousActivity();
//                }
//            }
//        });
        enableScreen(false);

        if (selectedStore == null) {
            enableScreen(true);
            return;
        }

        ProductService.startNewOrderForStore(this, selectedStore, new AllStoreMenuCallBack() {
            @Override
            public void onAllStoreMenuCallback(Exception exception) {
                Log.i("Callback", "Success");
                enableScreen(true);
                if (exception != null) {
                    setOrderButtonEnabled(true);
                    Utils.showErrorAlert(StoreDetailActivity.this, exception);
                } else {
                    DataManager.getInstance().resetBasket();
                    BasketFlagViewManager.getInstance().removeBasketFlag();
                    transitToPreviousActivity();
                }
            }

            @Override
            public void onAllStoreMenuErrorCallback(Exception exception) {
                enableScreen(true);
                if (exception != null) {
                    setOrderButtonEnabled(true);
                    Utils.showErrorAlert(StoreDetailActivity.this, exception);
                }
            }
        });
    }

    private void transitToPreviousActivity() {
        if (isChooseStoreFromProductDetail()) {
            goToProductDetailScreen();
        } else {
            goToMenuActivity();
        }
    }

    //load ad configuartion
    private void loadAdConfig() {

        ProductService.loadAdsConfig(this, new ProductAdsServiceCallback() {
            @Override
            public void onProductAdsCallback(ArrayList<ProductAd> productAds, Exception exception) {
                DataManager manager = DataManager.getInstance();
                manager.setSelectedStoreProductAd(productAds);// set filtered ads
                for (ProductAd ad : productAds) {
                    loadAdDetails(ad);
                }
            }
        });
    }

    //load ads details
    private void loadAdDetails(ProductAd productAd) {

        ProductService.loadAllAdDetails(this, productAd, new ProductAdDetailsServiceCallback() {
            @Override
            public void onProductAdDetailsCallback(ArrayList<ProductAdDetail> productAdDetailss, Exception exception) {
                DataManager.getInstance().setSelectedStoreProductAdDetail(productAdDetailss);
                List<Product> userProds = RecentOrdersService.getProductsFromRecentOrders();
                DataManager.getInstance().setRecentOrderList(userProds);
                navigateScreen();
            }
        });
    }

    private void goToMenuActivity() {
        preActivityFinishTasks();
        //Get User recent order product
//        List<Product> userProds = RecentOrdersService.getProductsFromRecentOrders();
//        if (userProds == null) {
//            userProds = new ArrayList<>();
//        }
//        List<Product> tempProductList = new ArrayList<>();//temp copy for validation
//        tempProductList.addAll(userProds);
//        //Remove recent products do not in current store menu
//        for (Product product : tempProductList) {
//            StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
//            if (storeMenuProduct == null) {
//                userProds.remove(product);
//            }
//        }
        long lastPullTime = SharedPreferenceHandler.getInstance().getLong(LastProductUpdate, -1);
        if (System.currentTimeMillis() - lastPullTime > TwentyFourHourInMiliSeconds || lastPullTime == -1) {
            enableScreen(false);
        }
        loadAdConfig();

    }

    private void navigateScreen() {
        enableScreen(true);
        if (DataManager.getInstance().getCurrentSelectedStore() != null
                && DataManager.getInstance().getCurrentSelectedStore().getName() != null) {
            if ((((DataManager.getInstance().getSelectedStoreProductAdDetail() != null
                    && DataManager.getInstance().getSelectedStoreProductAdDetail().size() > 0)
                    || (DataManager.getInstance().getSelectedStoreFeaturedProducts() != null
                    && DataManager.getInstance().getSelectedStoreFeaturedProducts().size() > 0)
                    || (DataManager.getInstance().getRecentOrderList() != null
                    && DataManager.getInstance().getRecentOrderList().size() > 0)))) {
                TransitionManager.slideUp(StoreDetailActivity.this, MenuActivity.class);
            } else if (DataManager.getInstance().getAllProductFamily() != null
                    && DataManager.getInstance().getAllProductFamily().size() > 0) {
                TransitionManager.slideUp(StoreDetailActivity.this, ProductFamiliesActivity.class);
            }
        }
        finish();
    }

    private void goToProductDetailScreen() {
        preActivityFinishTasks();
        TransitionManager.transitFrom(this, ProductDetailViewPagerActivity.class, true);
        finish();
    }

    private void preActivityFinishTasks() {
        Intent removeStoreLocatorIntent = new Intent(Constants.BROADCAST_REMOVE_STORE_LOCATOR_ACTIVITY);
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).sendBroadcast(removeStoreLocatorIntent);
        BasketFlagViewManager.getInstance().showBasketFlag(this);
    }

    private void setOrderButtonEnabled(boolean isenabled) {
        setButtonsEnabled(isenabled);
        if (isenabled) {
            startOrder.setText("Start New Order");
        } else {
            startOrder.setText("Please wait...");
        }
    }

    private void setMakePreferredEnabled(boolean isenabled) {
        setButtonsEnabled(isenabled);
        if (isenabled) {
            makePreferred.setText("Make Preferred Store");
        } else {
            makePreferred.setText("Please wait...");
        }
    }

    private void setButtonsEnabled(boolean isEnabled) {
        isBackButtonEnabled = isEnabled;
        startOrder.setEnabled(isEnabled);
        makePreferred.setEnabled(isEnabled);
    }

    private void fillViewWithData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedStore = (Store) bundle.getSerializable(Constants.B_STORE);
            if (isPreferredStoreDetail()) {
                isShowBasketIcon = bundle.getBoolean(Constants.B_IS_SHOW_BASKET);
            }
        }
        if (selectedStore != null) {
            TextView storeAddress = (TextView) findViewById(R.id.storeAddress);
            TextView contactNumber = (TextView) findViewById(R.id.contactNumber);
            ImageButton getDirButton = (ImageButton) findViewById(R.id.directionBtn);
            ImageButton callButton = (ImageButton) findViewById(R.id.callBtn);
            makePreferred = (Button) findViewById(R.id.makePreferred);
            startOrder = (Button) findViewById(R.id.startOrder);
            changeStore = (Button) findViewById(R.id.changeStore);
            selectStore = (Button) findViewById(R.id.selectStore);
            if (isItFromMenuScreen()) {
                startOrder.setText("Select Store");
            }
            startOrder.setOnClickListener(this);
            makePreferred.setOnClickListener(this);
            changeStore.setOnClickListener(this);
            selectStore.setOnClickListener(this);
            getDirButton.setOnClickListener(this);
            callButton.setOnClickListener(this);
            storeAddress.setText(selectedStore.getCompleteAddress());
            if (selectedStore.getTelephone() != null && !selectedStore.getTelephone().equals("")) {
                contactNumber.setText(selectedStore.getTelephone());
            } else {
                contactNumber.setText("Not available");
                callButton.setVisibility(View.GONE);
            }
            //moveMapToLocation(selectedStore.getLatitude(), selectedStore.getLongitude());
        }
    }

    private boolean isItFromMenuScreen() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_IT_FROM_WELCOME_SCREEN);
        }
        return false;
    }

    private void fetchStoreTimingsIfRequired() {
        if (selectedStore != null) {
            StoreTiming storeTiming = selectedStore.getStoreTiming();
            if (selectedStore.isSupportsOrderAhead() && storeTiming == null) {
                StoreService.getStoreCalendarForWeek(this, selectedStore.getRestaurantId(), new StoreCalendarCallback() {
                    @Override
                    public void onStoreCalendarCallback(StoreTiming calendar, Exception exception) {
                        if (calendar != null) {
                            addOpenTimings(calendar);
                        } else {
                            Utils.showErrorAlert(StoreDetailActivity.this, exception);
                            TextView timingsFetchError = (TextView) findViewById(R.id.timingsFetchError);
                            timingsFetchError.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } else if (storeTiming != null) {
                addOpenTimings(selectedStore.getStoreTiming());
            }
        }
    }

    private void setButtonsState() {
        Button makePreferred = (Button) findViewById(R.id.makePreferred);
        Button startOrder = (Button) findViewById(R.id.startOrder);
        Button changeStore = (Button) findViewById(R.id.changeStore);
        Button selectStore = (Button) findViewById(R.id.selectStore);

        if (isItChangeStore()) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) selectStore.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            selectStore.setVisibility(View.VISIBLE);
            changeStore.setVisibility(View.GONE);
            makePreferred.setVisibility(View.GONE);
            startOrder.setVisibility(View.GONE);
        } else if (isPreferredStoreDetail()) {
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) changeStore.getLayoutParams();
//            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            changeStore.setVisibility(View.GONE);
//            selectStore.setVisibility(View.GONE);
//            makePreferred.setVisibility(View.GONE);
//            startOrder.setVisibility(View.GONE);

            if (isFromPrefferedStore) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) changeStore.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                changeStore.setText("Change Preferred Store");
                changeStore.setTextColor(ContextCompat.getColor(this, R.color.red_color));
                changeStore.setBackgroundColor(ContextCompat.getColor(this, R.color.background_white));
                changeStore.setVisibility(View.VISIBLE);
                selectStore.setVisibility(View.GONE);
                makePreferred.setVisibility(View.GONE);
                startOrder.setVisibility(View.GONE);
            } else {

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) changeStore.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                changeStore.setText("Change Store");
                changeStore.setTextColor(ContextCompat.getColor(this, R.color.background_white));
                changeStore.setBackgroundColor(ContextCompat.getColor(this, R.color.red_color));
                changeStore.setVisibility(View.GONE);
                selectStore.setVisibility(View.GONE);
                try {
                    if (Integer.parseInt(selectedStore.getStoreCode()) != Integer.parseInt(UserService.getUser().getFavoriteStoreCode())) {
                        makePreferred.setVisibility(View.VISIBLE);
                    } else {
                        makePreferred.setVisibility(View.GONE);
                    }
                } catch (NumberFormatException e) {
                    Log.e("NUMBER_FORMAT_EXCEPTION", e.toString());
                    makePreferred.setVisibility(View.GONE);
                }
                startOrder.setVisibility(View.GONE);
            }

        } else if (isCheckoutStoreDetail()) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) changeStore.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            changeStore.setText("Change Store");
            changeStore.setTextColor(ContextCompat.getColor(this, R.color.background_white));
            changeStore.setBackgroundColor(ContextCompat.getColor(this, R.color.red_color));
            changeStore.setVisibility(View.VISIBLE);
            selectStore.setVisibility(View.GONE);
            makePreferred.setVisibility(View.GONE);
            startOrder.setVisibility(View.GONE);
        } else {
            if (!UserService.isUserAuthenticated()) {
                if (selectedStore != null && selectedStore.isSupportsOrderAhead()) {
                    makePreferred.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) startOrder.getLayoutParams();
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    startOrder.setVisibility(View.VISIBLE);
                    changeStore.setVisibility(View.GONE);
                    selectStore.setVisibility(View.GONE);
                } else {
                    changeStore.setVisibility(View.GONE);
                    makePreferred.setVisibility(View.GONE);
                    startOrder.setVisibility(View.GONE);
                    selectStore.setVisibility(View.GONE);
                }
            } else {
                if (selectedStore != null && selectedStore.isSupportsOrderAhead()) {
                    startOrder.setVisibility(View.VISIBLE);
                } else {
                    startOrder.setVisibility(View.GONE);
                }
                if (tryParseInt(selectedStore.getStoreCode()) && tryParseInt(UserService.getUser().getFavoriteStoreCode())) {
                    if (Integer.parseInt(selectedStore.getStoreCode()) != Integer.parseInt(UserService.getUser().getFavoriteStoreCode())) {
                        makePreferred.setVisibility(View.VISIBLE);
                    } else {
                        makePreferred.setVisibility(View.GONE);
                    }
                } else {
                    makePreferred.setVisibility(View.GONE);
                }
                changeStore.setVisibility(View.GONE);
                selectStore.setVisibility(View.GONE);
            }
        }
    }

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void addMarkerOnMap() {
        if (googleMap != null && selectedStore != null) {
            MarkerOptions options = new MarkerOptions().position(new LatLng(selectedStore.getLatitude(), selectedStore.getLongitude()));
            if (selectedStore.getName() != null && !selectedStore.getName().equals("")) {
                if (DataManager.getInstance().isDebug) {
                    options.title(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""));
                } else {
                    options.title(selectedStore.getName().replace("Jamba Juice ", ""));
                }

                options.snippet(selectedStore.getCity());
            }
            googleMap.addMarker(options);
        }
    }

    private void addOpenTimings(StoreTiming calendar) {
        selectedStore.setStoreTiming(calendar);
        TextView hours = (TextView) findViewById(R.id.hoursHeading);
        hours.setVisibility(View.VISIBLE);
        LinearLayout openTimings = (LinearLayout) findViewById(R.id.openTimmings);
        openTimings.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        ArrayList<TimeRange> ranges = calendar.getRanges();
        SimpleDateFormat format = new SimpleDateFormat(Server_Time_Format);
        SimpleDateFormat displayformat = Utils.getTimeDisplayFormat(this);
        String[] weekDaysName = getWeekDaysName();
        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int i = 1;
        for (String dayName : weekDaysName) {
            boolean isFound = false;
            View view = inflater.inflate(R.layout.row_store_open_timings, openTimings, false);
            TextView dayText = (TextView) view.findViewById(R.id.day);
            TextView timeText = (TextView) view.findViewById(R.id.timings);
            if (i == currentDayOfWeek) {
                dayText.setTextColor(Color.BLACK);
                timeText.setTextColor(Color.BLACK);
            }
            i++;
            dayText.setText(dayName);
            for (TimeRange timeRange : ranges) {
                if (timeRange.getFullWeekDayName().equalsIgnoreCase(dayName)) {
                    String timeRangeStart = timeRange.getStart();
                    String timeRangeEnd = timeRange.getEnd();
                    try {
                        Date from = format.parse(timeRangeStart);
                        Date to = format.parse(timeRangeEnd);
                        String time = displayformat.format(from) + " - " + displayformat.format(to);
                        timeText.setText(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    isFound = true;
                    break;
                }
            }
            openTimings.addView(view);
            if (!isFound) {
                timeText.setText("Closed");
            }
        }
    }

    private void setUpScrollView() {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        View transparentView = findViewById(R.id.transparentView);
        transparentView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (googleMap != null && selectedStore != null) {
            googleMap.getUiSettings().setAllGesturesEnabled(false);
            //fillViewWithData();
            addMarkerOnMap();
            moveMapToLocation(selectedStore.getLatitude(), selectedStore.getLongitude());
        }
    }

    private void initilizeMap() {
//        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).
        //((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
//        SupportMapFragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
//        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
//        SupportMapFragment mapFragment = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    private void moveMapToLocation(double lat, double lng) {
        if (googleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(15.5f).build();
            CameraUpdate center = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(center);
        }
    }

    //Returns weekdays name from current day onwards.
    private String[] getWeekDaysName() {
        String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return weekdays;
    }

    private boolean isChooseStoreFromProductDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_CHOOOSE_STORE_FROM_PROD_DETAIL);
        }
        return false;
    }

    @Override
    public void onUserUpdateCallback(Exception exception) {
        enableScreen(true);
        setMakePreferredEnabled(true);
        if (exception != null) {
            Utils.showErrorAlert(this, exception);
        } else {
            //Update CLP Customer on updating information
            User user = UserService.getUser();
            ((JambaApplication) this.getApplication()).updateFavouriteStore(user);
            // Utils.showAlert(this, selectedStore.getName() + " has been saved as your preferred store.", "Preferred Store");
            if (isPreferredStoreDetail()) {
                TransitionManager.transitFrom(StoreDetailActivity.this, SettingsActivity.class, true);
            } else {

                Utils.showAlert(this, selectedStore.getName() + " has been saved as your preferred store.", "Preferred Store");
            }
        }
    }

    private boolean isItChangeStore() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_IT_FROM_BASKET_STORE_SCREEN);
        }
        return false;
    }

    private boolean isPreferredStoreDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isFromPrefferedStore = bundle.getBoolean(Constants.B_IS_FROM_SETTINGS, false);

            return bundle.getBoolean(Constants.B_IS_STORE_DETAIL_ONLY);
        }
        return false;
    }

    private boolean isCheckoutStoreDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_CHECKOUT_STORE_DETAIL_ONLY);
        }
        return false;
    }

    @Override
    protected void handleAuthTokenFailure() {
        setButtonsState();
    }
}
