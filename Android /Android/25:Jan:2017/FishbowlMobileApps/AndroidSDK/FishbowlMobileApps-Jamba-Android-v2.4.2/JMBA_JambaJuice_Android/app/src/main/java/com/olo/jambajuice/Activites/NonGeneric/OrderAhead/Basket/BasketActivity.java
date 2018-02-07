package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.ProductFamiliesActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductUpdateActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.OrderType.AddDeliveryAddressActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.OrderType.SavedDeliveryAddressesActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo.AddCardActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo.SelectExistingCardActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo.UserInfoActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Promotions.PromotionalCodeActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.BasketRewardsActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.BasketRewardsAndOffersActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreDetail.StoreDetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.Adapters.BasketAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketValidationCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.BillingAccountsCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.BasketChoice;
import com.olo.jambajuice.BusinessLogic.Models.BasketProduct;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.BusinessLogic.Models.DeliveryAddress;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProduct;
import com.olo.jambajuice.BusinessLogic.Models.StoreTiming;
import com.olo.jambajuice.BusinessLogic.Models.TimeRange;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.RecentOrdersService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Models.OloBasketValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.OrderType.DELIVERY;
import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.OrderType.PICKUP;
import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.PickUpDay.ASAP;
import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.PickUpDay.LATER;
import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.PickUpDay.TODAY;
import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.PickUpDay.TOMORROW;
import static com.olo.jambajuice.Utils.Constants.GA_CATEGORY.ORDER_AHEAD;
import static com.wearehathway.apps.olo.Utils.Constants.Server_Time_Format;

public class BasketActivity extends BaseActivity implements View.OnClickListener {
    // fifteen minutes in milliseconds
    private static final double FIFTEEN_MIN_MILLI_SEC = 900000;
    public static int checkoffer = 0;
    public static boolean check_offer = false;
    public JambaApplication _app;
    DataManager dataManager;
    //private Integer todaysTimebarProgress = null;
    //private Integer tomorrowTimebarProgress = null;
    PickUpDay pickUpDay;
    OrderType orderType;
    Button addAnotherItem;
    DatePickerDialog datePickerDialog, pickUpFutureDatePickerDialog;
    private SeekBar timeBar;
    private Button btnToday, pickUpBtn, deliveryBtn, delivery_btn_today;
    private Button btnTomorrow, delivery_btn_later;
    private Button btnLater;
    private RelativeLayout btnAsap, delivery_btn_asap;
    private TextView openTime, sAddress1;
    private TextView closeTime, asapBelowtext, pickUptext;
    private TextView selectedTime;
    private TextView checkout;
    private TextView tax;
    private TextView subtotal;
    private TextView total;
    private TextView tv_discount;
    private TextView tv_deliveryEstimate;
    private TextView storeNotAvailable;
    //private ListView basketList;
    private TextView tv_total_amount;
    private ScrollView scrollView;
    private View promotionsView;
    private BasketRewardViewHolder rewardPromoViewHolder;
    private Date openDate;
    private Date closeDate;
    private BasketAdapter basketAdapter;
    private TimeRange today;
    private TimeRange tomorrow;
    private TimeRange monday;
    private TimeRange tuesday;
    private TimeRange wednesday;
    private TimeRange thursday;
    private TimeRange friday;
    private TimeRange satday;
    private TimeRange sunday;
    private TextView storeAddress;
    private TextView storeName;
    //private DatePicker.OnDateChangedListener DateChangeListener;
    private RelativeLayout storeLayout, dALayout;
    private LinearLayout orderTypeMainLayout, deliveryMainLayout, pickUpMainLayout;
    private ImageButton deliveryEstBtn;
    private RelativeLayout deliveryEstLayout;
    private int year, month, day;
    private int pufYear, pufMonth, pufDay;
    private Date startDate, endDate;
    private Date pickupFutureStartDate, pickupFutureEndDate;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        if (DataManager.getInstance().getCurrentBasket() == null) {
            //In case the data is not available and activity is recreating.
            finish();
            return;
        }
        context = this;
        dataManager = DataManager.getInstance();
        initComponents();
        setListeners();
        setDates();
        setPickUpFutureDates();
        isShowBasketIcon = false;
        orderType = PICKUP;
        pickUpDay = ASAP;
        enableTimebar(false);
        setUpToolBar(true);
        prepareTimeBar();
        prepareOrderType();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_BASKET_UI));
    }

    private void setDates() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) - 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        c.add(Calendar.DATE, 1);
        startDate = c.getTime();

        int advanceOrderDays = 1;
        if (dataManager.getCurrentSelectedStore() != null) {
            advanceOrderDays = dataManager.getCurrentSelectedStore().getAdvanceorderdays() - 1;
        }
        c.add(Calendar.DATE, advanceOrderDays);  // number of days to add
        endDate = c.getTime();

    }

    private void setPickUpFutureDates() {
        Calendar c = Calendar.getInstance();
        pufYear = c.get(Calendar.YEAR);
        pufMonth = c.get(Calendar.MONTH) - 1;
        pufDay = c.get(Calendar.DAY_OF_MONTH);


        c.add(Calendar.DATE, 1);
        pickupFutureStartDate = c.getTime();

        int advanceOrderDays = 1;
        if (dataManager.getCurrentSelectedStore() != null) {
            advanceOrderDays = dataManager.getCurrentSelectedStore().getAdvanceorderdays() - 1;
        }
        c.add(Calendar.DATE, advanceOrderDays);  // number of days to add for pickupFutureEndDate
        pickupFutureEndDate = c.getTime();
    }


    private void resetCalender(){
        setDates();
        setPickUpFutureDates();
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        switch (v.getId()) {
            case R.id.tv_checkout:
                if ((pickUpBtn.isSelected() && DataManager.getInstance().getCurrentBasket().getDeliveryAddress().getId() == 0)
                        || (deliveryBtn.isSelected() && DataManager.getInstance().getCurrentBasket().getDeliveryAddress().getId() != 0)) {
                    String storeName = null;
                    if(DataManager.getInstance().getCurrentSelectedStore() != null) {
                        if (DataManager.getInstance().isDebug) {
                            storeName = Utils.setDemoStoreName(DataManager.getInstance().getCurrentSelectedStore()).getName().replace("Jamba Juice ", "");
                        } else {
                            storeName = DataManager.getInstance().getCurrentSelectedStore().getName().replace("Jamba Juice ", "");
                        }
                    }

                    List<BasketProduct> basketProductList = dataManager.getCurrentBasket().getProducts();
                    JambaAnalyticsManager.sharedInstance().track_ItemWith(dataManager.getCurrentBasket().getId()
                            , "PRODUCTS_COUNT = " + basketProductList.size() + ";TOTAL_COST = " + dataManager.getCurrentBasket().getTotal()
                            , FBEventSettings.CHECKOUT);
                    if (storeName != null) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setMessage("Please confirm your order will be placed at the " + storeName + " " +
                                "store.");
                        alertDialogBuilder.setTitle("Checkout Confirmation");
                        alertDialogBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                verifyAndCheckout();
                            }
                        });

                        alertDialogBuilder.setNeutralButton("Change Store", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(Constants.B_IS_IT_FROM_BASKET_STORE_SCREEN, true);
                                TransitionManager.transitFrom(BasketActivity.this, StoreLocatorActivity.class, bundle);
                            }
                        });
                        alertDialogBuilder.setNegativeButton("Cancel", null);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } else {
                    Utils.showAlert(this, "Choose Delivery Address", "Error");
                }

                break;

            case R.id.btn_today:
                todaySelected();
                break;

            case R.id.btn_tomorrow:
                tomorrowSelected();
                break;

            case R.id.btn_asap:
                asapSelected();
                break;

            case R.id.btn_later:
                laterSelected();
                break;

            case R.id.promotions:
                showRewardsAndPromos();
                break;

            case R.id.addAnotherItem:
                addAnotherItem();
                break;

            case R.id.store_layout:
                if (StoreDetailActivity.storeDetailActivity != null) {
                    StoreDetailActivity.storeDetailActivity.finish();
                }
                showStoreView();
                break;

            case R.id.deleteButton:
                trackButtonWithName("DeleteBasket");
                deleteBasketTapped();
                break;


            case R.id.dialog_back:
                //timedialog.cancel();
                break;

            case R.id.pickUpBtn:
                pickUpSelected();
                break;

            case R.id.deliveryBtn:
                deliverySelected();
                break;

            case R.id.dALayout:
                if (UserService.isUserAuthenticated()) {
                    TransitionManager.transitFrom(BasketActivity.this, SavedDeliveryAddressesActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("IsAddNew", true);
                    TransitionManager.transitFrom(BasketActivity.this, AddDeliveryAddressActivity.class, bundle);
                }
                break;
        }
    }


    private void openDatePicker() {

        if (datePickerDialog != null) {
            datePickerDialog.cancel();
        }
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setViews(year, monthOfYear, dayOfMonth);

                datePickerDialog = null;
            }
        }, year, month - 1, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                asapSelected();
                resetCalender();
            }
        });
        datePickerDialog.getDatePicker().setMinDate(startDate.getTime());
        datePickerDialog.getDatePicker().setMaxDate(endDate.getTime());
        datePickerDialog.show();
    }

    private void openDatePickerForPickupFuture() {

        if (pickUpFutureDatePickerDialog != null) {
            pickUpFutureDatePickerDialog.cancel();
        }
        pickUpFutureDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setPufViews(year, monthOfYear, dayOfMonth);

                pickUpFutureDatePickerDialog = null;
            }
        }, pufYear, pufMonth - 1, pufDay);

        pickUpFutureDatePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                asapSelected();
                resetCalender();
            }
        });
        pickUpFutureDatePickerDialog.getDatePicker().setMinDate(pickupFutureStartDate.getTime());
        pickUpFutureDatePickerDialog.getDatePicker().setMaxDate(pickupFutureEndDate.getTime());
        pickUpFutureDatePickerDialog.show();
    }

    private void setViews(int yearValue, int monthOfYear, int dayOfMonth) {
        year = yearValue;
        month = monthOfYear + 1;
        day = dayOfMonth;

        adjustTimeBar();
        setPickUpTime();
        setLastSelectedTimeIfPresent();
        if (datePickerDialog != null) {
            datePickerDialog.cancel();
        }
    }

    private void setPufViews(int yearValue, int monthOfYear, int dayOfMonth) {
        pufYear = yearValue;
        pufMonth = monthOfYear + 1;
        pufDay = dayOfMonth;

        adjustTimeBar();
        setPickUpTime();
        setLastSelectedTimeIfPresent();
        if (pickUpFutureDatePickerDialog != null) {
            pickUpFutureDatePickerDialog.cancel();
        }

    }


    private void addAnotherItem() {
        //Get User recent order product
        List<Product> userProds = RecentOrdersService.getProductsFromRecentOrders();
        if (userProds == null) {
            userProds = new ArrayList<>();
        }
        List<Product> tempProductList = new ArrayList<>();//temp copy for validation
        tempProductList.addAll(userProds);
        //Remove recent products do not in current store menu
        for (Product product : tempProductList) {
            StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
            if (storeMenuProduct == null) {
                userProds.remove(product);
            }
        }
        if (((DataManager.getInstance().getSelectedStoreFeaturedProducts() != null && DataManager.getInstance().getSelectedStoreFeaturedProducts().size() > 0) || userProds.size() > 0)) {
            TransitionManager.transitFrom(this, MenuActivity.class, true);
        } else if (DataManager.getInstance().getAllProductFamily() != null && DataManager.getInstance().getAllProductFamily().size() > 0) {
            TransitionManager.transitFrom(this, ProductFamiliesActivity.class, true);
        }
        finish();
    }

    private void showRewardsAndPromos() {
        if (rewardPromoViewHolder.isClickAllowed()) {
            if (UserService.isUserAuthenticated()) {
                _app = JambaApplication.getAppContext();
                if (_app != null && _app.fbsdkObj != null) {
                    if (_app.fbsdkObj.getFBSdkData() != null) {
                        checkoffer = _app.fbsdkObj.getFBSdkData().mobileSettings.INAPPOFFER_ENABLED;
                        check_offer = checkoffer == 1;
                        if (check_offer) {
                            if (DataManager.getInstance().getCurrentBasket().getProducts().size() > 0) {
                                TransitionManager.transitFrom(this, BasketRewardsAndOffersActivity.class);
                            } else {
                                Utils.showAlert(this, "Please add at least one product to the basket in order to apply an offer/reward.", "Error");
                            }
                        } else {
                            TransitionManager.transitFrom(this, BasketRewardsActivity.class);

                        }
                    }
                } else {
                    TransitionManager.transitFrom(this, BasketRewardsActivity.class);
                }
            } else {
                TransitionManager.transitFrom(this, PromotionalCodeActivity.class);
            }
        }
    }

    private void todaySelected() {
        pickUpDay = TODAY;
        dataManager.getCurrentBasket().setPickUpDay(pickUpDay);
        setDaySelection();
        adjustTimeBar();
        setLastSelectedTimeIfPresent();
    }

    private void tomorrowSelected() {
        pickUpDay = TOMORROW;
        dataManager.getCurrentBasket().setPickUpDay(pickUpDay);
        setDaySelection();
//        adjustTimeBar();
//        setLastSelectedTimeIfPresent();
        openDatePickerForPickupFuture();
    }

    private void asapSelected() {
        pickUpDay = ASAP;
        dataManager.getCurrentBasket().setPickUpDay(pickUpDay);
        dataManager.getCurrentBasket().setTimewanted(null);
        enableTimebar(false);
        setDaySelection();
    }

    private void laterSelected() {
        pickUpDay = LATER;
        dataManager.getCurrentBasket().setPickUpDay(pickUpDay);
        setDaySelection();
        openDatePicker();
    }

    private void pickUpSelected() {
        if (dataManager.getCurrentBasket().getDeliveryCost() != 0) {
            enableScreen(false);
            BasketService.deliveryMode(this, "pickup", dataManager.getCurrentBasket().getId(), new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    if (e != null) {
                        Utils.showErrorAlert(BasketActivity.this, e);
                    } else {
                        if (dataManager.getCurrentBasket().getPickUpDay() == LATER
                                || dataManager.getCurrentBasket().getPickUpDay() == ASAP) {
                            asapSelected();
                        }
                        pickUptext.setText("Pickup Date & Time");
                        asapBelowtext.setText("in " + dataManager.getCurrentBasket().getLeadtimeestimateminutes() + " Mins");
                        btnTomorrow.setVisibility(View.VISIBLE);
                        btnLater.setVisibility(View.GONE);
                        refreshData();
                        orderType = PICKUP;
                        dataManager.getCurrentBasket().setOrderType(orderType);
                        setOrderTypeSelection();
                    }
                }
            });
        } else {
            if (dataManager.getCurrentBasket().getPickUpDay() == LATER
                    || dataManager.getCurrentBasket().getPickUpDay() == ASAP) {
                asapSelected();
            }
            pickUptext.setText("Pickup Date & Time");
            asapBelowtext.setText("in " + dataManager.getCurrentBasket().getLeadtimeestimateminutes() + " Mins");
            btnTomorrow.setVisibility(View.VISIBLE);
            btnLater.setVisibility(View.GONE);
            refreshData();
            orderType = PICKUP;
            dataManager.getCurrentBasket().setOrderType(orderType);
            setOrderTypeSelection();
        }

    }

    private void deliverySelected() {

        if (dataManager.getCurrentBasket().getPickUpDay() == TOMORROW) {
            asapSelected();
        }
        pickUptext.setText("Delivery Date & Time");
        asapBelowtext.setText("in " + dataManager.getCurrentBasket().getLeadtimeestimateminutes() + " Mins");
        btnTomorrow.setVisibility(View.GONE);
        btnLater.setVisibility(View.VISIBLE);
        refreshData();
        orderType = DELIVERY;
        dataManager.getCurrentBasket().setOrderType(orderType);
        setOrderTypeSelection();

    }

    private void verifyAndCheckout() {
        //Track Data
        List<BasketProduct> basketProductList = dataManager.getCurrentBasket().getProducts();

        ArrayList<Integer> rewardIds = new ArrayList<>();
        ArrayList<String> rewardTitles = new ArrayList<>();
        if (dataManager.getCurrentBasket().getAppliedRewards() != null
                && dataManager.getCurrentBasket().getAppliedRewards().size() > 0) {
            for (Reward reward : dataManager.getCurrentBasket().getAppliedRewards()) {
                rewardIds.add(reward.getRewardId());
                rewardTitles.add(reward.getRewardTitle());
            }

            JambaAnalyticsManager.sharedInstance().track_ItemWith(
                    String.valueOf(rewardIds),
                    String.valueOf(rewardTitles),
                    FBEventSettings.REWARDS_REDEEMED);
        }

        ArrayList<Integer> ids = DataManager.getInstance().getModifiersId();//local instances maintains modifier ids
        ArrayList<Integer> selectedIds = new ArrayList<>();
        ArrayList<String> selectedModifierNames = new ArrayList<>();
        for (BasketProduct product : basketProductList) {
            float modifierTotalPrice = 0;
            boolean productHavingModifier = false;
            List<BasketChoice> choices = new ArrayList<>(product.getChoices());
            for (BasketChoice choice : choices) {
                for (Integer id : ids) {
                    if (choice.getOptionid() == id) {
                        selectedIds.add(choice.getOptionid());
                        selectedModifierNames.add(choice.getName());
                        modifierTotalPrice = modifierTotalPrice + choice.getCost();
                        productHavingModifier = true;
                    }
                }
            }
            if (productHavingModifier) {
                //Product has modifers
                JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(selectedIds)
                        , String.valueOf(selectedModifierNames)
                        , FBEventSettings.CHECKOUT_WITHMODIFIERS);

                //     Log.i(product.getName(), "Total Modifer cost:" + String.valueOf(modifierTotalPrice * product.getQuantity()) + "Total Cost : " + product.getTotalcost());
            } else {
                //Product not having modifier or has only Add boost
                //   Log.i(product.getName(), "Total Cost : " + product.getTotalcost());

            }
        }

        int size = dataManager.getCurrentBasket().getProducts().size();
        if (size == 0) {
            Utils.showAlert(this, "Please add a product to basket before checking out.");
            return;
        }
        if (verifyAndSetPickUpTime()) {
            enableScreen(false);
            BasketService.setTimeWantedAndValidateBasket(new BasketValidationCallback() {
                @Override
                public void onBasketValidated(OloBasketValidation oloBasketValidation, Exception exception) {
                    if (exception == null) {
                        if (oloBasketValidation != null
                                && oloBasketValidation.getUpsellgroups() != null
                                && oloBasketValidation.getUpsellgroups().size() > 0
                                && oloBasketValidation.getUpsellgroups().get(0).getUpsellitems() != null
                                && oloBasketValidation.getUpsellgroups().get(0).getUpsellitems().size() > 0) {
                            enableScreen(true);
                            DataManager.getInstance().setOloUpsellGroups(oloBasketValidation.getUpsellgroups());
                            TransitionManager.slideUp(BasketActivity.this, UpsellActivity.class);
                        } else if (UserService.isUserAuthenticated()) {
                            enableScreen(false);
                            BasketService.getBillingAccountsForCurrentBasket(BasketActivity.this, new BillingAccountsCallback() {
                                @Override
                                public void onBillingAccountsCallback(ArrayList<BillingAccount> billingAccounts, Exception error) {
                                    enableScreen(true);
                                    Basket basket = DataManager.getInstance().getCurrentBasket();
                                    if (billingAccounts != null && billingAccounts.size() > 0) {
                                        TransitionManager.transitFrom(BasketActivity.this, SelectExistingCardActivity.class);
                                    } else {
                                        TransitionManager.transitFrom(BasketActivity.this, AddCardActivity.class);
                                    }
                                }
                            });
                        } else {
                            enableScreen(true);
                            TransitionManager.transitFrom(BasketActivity.this, UserInfoActivity.class);
                        }
                    } else {
                        enableScreen(true);
                        Utils.showErrorAlert(BasketActivity.this, exception);
                    }
                }
            });
        }
    }

    private boolean setPickUpTime() {
        if (pickUpDay == ASAP) {
            DataManager.getInstance().getCurrentBasket().setTimewanted(null);
            return true;
        } else if (pickUpDay == LATER) {
            try {
                //SimpleDateFormat serverFormat = new SimpleDateFormat(Server_Time_Format);

                SimpleDateFormat laterFormat = new SimpleDateFormat("EEE dd MMM hh:mm a");
                String selectedTimeString = selectedTime.getText().toString();
                Date date = laterFormat.parse(selectedTimeString);

                Calendar timeWantedCalendar = Calendar.getInstance();
                timeWantedCalendar.setTime(date);
                timeWantedCalendar.set(Calendar.YEAR, year);
                timeWantedCalendar.set(Calendar.MONTH, month - 1);
                timeWantedCalendar.set(Calendar.DAY_OF_MONTH, day);


                Date timeWanted = timeWantedCalendar.getTime();
                Log.e("Later time: ", String.valueOf(timeWanted));
                DataManager.getInstance().getCurrentBasket().setTimewanted(timeWanted);
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (pickUpDay == TOMORROW) {
            try {
                //SimpleDateFormat serverFormat = new SimpleDateFormat(Server_Time_Format);

                SimpleDateFormat laterFormat = new SimpleDateFormat("EEE dd MMM hh:mm a");
                String selectedTimeString = selectedTime.getText().toString();
                Date date = laterFormat.parse(selectedTimeString);

                Calendar timeWantedCalendar = Calendar.getInstance();
                timeWantedCalendar.setTime(date);
                timeWantedCalendar.set(Calendar.YEAR, pufYear);
                timeWantedCalendar.set(Calendar.MONTH, pufMonth - 1);
                timeWantedCalendar.set(Calendar.DAY_OF_MONTH, pufDay);


                Date timeWanted = timeWantedCalendar.getTime();
                Log.e("Future time: ", String.valueOf(timeWanted));
                DataManager.getInstance().getCurrentBasket().setTimewanted(timeWanted);
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {

            TimeRange range = null;
            Calendar calendar = null;
            if (btnToday.isSelected()) {
                calendar = Calendar.getInstance();
            }
//            else if (btnTomorrow.isSelected()) {
//                calendar = Calendar.getInstance();
//                calendar.add(Calendar.DATE, 1);
//            }

//            int result = calendar.get(Calendar.DAY_OF_WEEK);
//            switch (result) {
//                case Calendar.MONDAY:
//                    range = monday;
//                    break;
//
//                case Calendar.TUESDAY:
//                    range = tuesday;
//                    break;
//
//                case Calendar.WEDNESDAY:
//                    range = wednesday;
//                    break;
//
//                case Calendar.THURSDAY:
//                    range = thursday;
//                    break;
//
//                case Calendar.FRIDAY:
//                    range = friday;
//                    break;
//
//                case Calendar.SATURDAY:
//                    range = satday;
//                    break;
//
//                case Calendar.SUNDAY:
//                    range = sunday;
//                    break;
//            }

            SimpleDateFormat rangeDateFormat = new SimpleDateFormat(Server_Time_Format);
            SimpleDateFormat convertFormat = new SimpleDateFormat("yyyyMMdd");
            String selectedDate = convertFormat.format(calendar.getTime());

            StoreTiming storeTimings = dataManager.getSelectedStoreTiming();
            if (storeTimings != null
                    && storeTimings.getRanges() != null
                    && storeTimings.getRanges().size() > 0) {
                for (TimeRange timeRange : storeTimings.getRanges()) {
                    Date timeRangeDate = null;
                    try {
                        timeRangeDate = rangeDateFormat.parse(timeRange.getStart());
                        String convertedTimeRangeDate = convertFormat.format(timeRangeDate);
                        if (selectedDate.equalsIgnoreCase(convertedTimeRangeDate)) {
                            range = timeRange;
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (range != null) {
                try {
                    SimpleDateFormat serverFormat = new SimpleDateFormat(Server_Time_Format);
                    Date currentDaySelected = serverFormat.parse(range.getStart());
                    SimpleDateFormat format = Utils.getTimeDisplayFormat(this);
                    String selectedTimeString = selectedTime.getText().toString();

                    Calendar timeWantedCalendar = Calendar.getInstance();
                    Calendar currenDaySelectedCalendar = Calendar.getInstance();
                    currenDaySelectedCalendar.setTime(currentDaySelected);
                    timeWantedCalendar.setTime(format.parse(selectedTimeString));
                    timeWantedCalendar.set(Calendar.YEAR, currenDaySelectedCalendar.get(Calendar.YEAR));
                    timeWantedCalendar.set(Calendar.MONTH, currenDaySelectedCalendar.get(Calendar.MONTH));
                    timeWantedCalendar.set(Calendar.DAY_OF_MONTH, currenDaySelectedCalendar.get(Calendar.DAY_OF_MONTH));

                    Date timeWanted = timeWantedCalendar.getTime();
                    Log.e("Wanted time: ", String.valueOf(timeWanted));

                    DataManager.getInstance().getCurrentBasket().setTimewanted(timeWanted);
                    return true;

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean verifyAndSetPickUpTime() {
        String storeName = null;
        if (DataManager.getInstance().isDebug) {
            storeName = Utils.setDemoStoreName(DataManager.getInstance().getCurrentSelectedStore()).getName().replace("Jamba Juice ", "");
        } else {
            storeName = DataManager.getInstance().getCurrentSelectedStore().getName().replace("Jamba Juice ", "");
        }

        if (!setPickUpTime() && pickUpDay != ASAP) {
            if (storeName != null) {
                if ((btnTomorrow.isSelected() && Utils.isToday(pufYear, pufMonth, pufDay))
                        || (btnToday.isSelected())
                        || (btnLater.isSelected() && Utils.isToday(year, month, day))) {
                    Utils.showAlert(this, storeName + " is currently closed.");
                } else {
                    Utils.showAlert(this, storeName + " will be closed.");
                }
            } else {
                if ((btnTomorrow.isSelected() && Utils.isToday(pufYear, pufMonth, pufDay))
                        || (btnToday.isSelected())
                        || (btnLater.isSelected() && Utils.isToday(year, month, day))) {
                    Utils.showAlert(this, "Store is currently closed.");
                } else {
                    Utils.showAlert(this, "Store will be closed.");
                }
            }
            return false;
        }
        return true;
    }

    private void deleteBasketTapped() {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Emptying your basket will cancel this order. Proceed?");
            alertDialogBuilder.setTitle("Empty Basket");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "delete_basket");
                    if (dataManager.getCurrentBasket() != null) {
                        JambaAnalyticsManager.sharedInstance().track_ItemWith(dataManager.getCurrentBasket().getId(), "", FBEventSettings.BASKET_DELETE);
                    }
                    ArrayList<BasketProduct> products = dataManager.getCurrentBasket().getProducts();
                    if (products != null) {
                        int countProuduct = products.size();
                        StringBuilder ids = new StringBuilder();
                        StringBuilder name = new StringBuilder();
                        for (int i = 0; i < countProuduct; i++) {
                            BasketProduct product = products.get(i);
                            ids.append(product.getProductId());
                            ids.append(",");
                            name.append(product.getName());
                            name.append(",");
                        }

                        dataManager.resetBasket();
                        onBackPressed();
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("No", null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void setListeners() {
        checkout.setOnClickListener(this);
        btnToday.setOnClickListener(this);
        btnTomorrow.setOnClickListener(this);
        btnLater.setOnClickListener(this);
        btnAsap.setOnClickListener(this);
        promotionsView.setOnClickListener(this);
        addAnotherItem.setOnClickListener(this);
        storeLayout.setOnClickListener(this);
        dALayout.setOnClickListener(this);
        pickUpBtn.setOnClickListener(this);
        deliveryBtn.setOnClickListener(this);
        findViewById(R.id.deleteButton).setOnClickListener(this);
    }

    private void initComponents() {
        openTime = (TextView) findViewById(R.id.open_time);
        closeTime = (TextView) findViewById(R.id.close_time);
        checkout = (TextView) findViewById(R.id.tv_checkout);
        selectedTime = (TextView) findViewById(R.id.selected_time);
        btnToday = (Button) findViewById(R.id.btn_today);
        btnTomorrow = (Button) findViewById(R.id.btn_tomorrow);
        btnLater = (Button) findViewById(R.id.btn_later);
        btnAsap = (RelativeLayout) findViewById(R.id.btn_asap);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        storeNotAvailable = (TextView) findViewById(R.id.tv_store_not_available);
        addAnotherItem = (Button) findViewById(R.id.addAnotherItem);
        storeName = (TextView) findViewById(R.id.store_title);
        storeAddress = (TextView) findViewById(R.id.store_detail);
        storeLayout = (RelativeLayout) findViewById(R.id.store_layout);
        dALayout = (RelativeLayout) findViewById(R.id.dALayout);
        asapBelowtext = (TextView) findViewById(R.id.asapBelowtext);
        pickUptext = (TextView) findViewById(R.id.pickText);

        orderTypeMainLayout = (LinearLayout) findViewById(R.id.orderTypeMainLayout);
        deliveryMainLayout = (LinearLayout) findViewById(R.id.deliveryMainLayout);
        pickUpMainLayout = (LinearLayout) findViewById(R.id.pickUpMainLayout);
        pickUpBtn = (Button) findViewById(R.id.pickUpBtn);
        deliveryBtn = (Button) findViewById(R.id.deliveryBtn);
        sAddress1 = (TextView) findViewById(R.id.sAddress1);
        delivery_btn_today = (Button) findViewById(R.id.delivery_btn_today);
        delivery_btn_later = (Button) findViewById(R.id.delivery_btn_later);
        delivery_btn_asap = (RelativeLayout) findViewById(R.id.delivery_btn_asap);


        btnToday.setEnabled(false);
        btnTomorrow.setEnabled(false);
        btnLater.setEnabled(false);


        ViewTreeObserver vto = btnAsap.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    btnAsap.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    btnAsap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                int btnAsapHeight = btnAsap.getHeight();

                LinearLayout.LayoutParams todayButtonParams = (LinearLayout.LayoutParams) btnToday.getLayoutParams();
                todayButtonParams.height = btnAsapHeight;
                btnToday.setLayoutParams(todayButtonParams);

                LinearLayout.LayoutParams tomButtonParams = (LinearLayout.LayoutParams) btnTomorrow.getLayoutParams();
                tomButtonParams.height = btnAsapHeight;
                btnTomorrow.setLayoutParams(tomButtonParams);

                LinearLayout.LayoutParams latButtonParams = (LinearLayout.LayoutParams) btnLater.getLayoutParams();
                latButtonParams.height = btnAsapHeight;
                btnLater.setLayoutParams(latButtonParams);

                LinearLayout.LayoutParams pickButtonParams = (LinearLayout.LayoutParams) pickUpBtn.getLayoutParams();
                pickButtonParams.height = btnAsapHeight;
                pickUpBtn.setLayoutParams(pickButtonParams);

                LinearLayout.LayoutParams deliButtonParams = (LinearLayout.LayoutParams) deliveryBtn.getLayoutParams();
                deliButtonParams.height = btnAsapHeight;
                deliveryBtn.setLayoutParams(deliButtonParams);


            }
        });

        if (dataManager.getCurrentBasket().totalProductsCount() == Constants.TOTAL_BASKET_PRODUCTS) {
            addAnotherItem.setVisibility(View.GONE);
        }
        setOrderType();
        setTimeBar();
        initToolbar();
        setStoreInfo();
        setDeliveryAddress();
        initBasketItemsList();
        initScrollView();
        initPromotionsOrRewards();
        initSubtotal();
    }

    private void setDeliveryAddress() {

        DeliveryAddress deliveryAddress = dataManager.getCurrentBasket().getDeliveryAddress();
        if (deliveryAddress != null) {
            String completeAddress = Utils.getFormatedAddress(deliveryAddress.getStreetaddress(), deliveryAddress.getCity(), deliveryAddress.getBuilding(), deliveryAddress.getZipcode());
            if (StringUtilities.isValidString(completeAddress)) {
                sAddress1.setText(completeAddress);
            } else {
                sAddress1.setText("Select/Enter your delivery address");
            }
        } else {
            sAddress1.setText("Select/Enter your delivery address");
        }
    }

    private void setOrderType() {
        if (dataManager.getCurrentSelectedStore() != null) {
            if (dataManager.getCurrentSelectedStore().isSupportDelivery()) {
                orderTypeMainLayout.setVisibility(View.VISIBLE);
                deliveryMainLayout.setVisibility(View.VISIBLE);
            } else {
                orderTypeMainLayout.setVisibility(View.GONE);
                deliveryMainLayout.setVisibility(View.GONE);
            }
        }
    }

    private void setStoreInfo() {
        if (dataManager.getCurrentSelectedStore() != null) {
            if (dataManager.getCurrentSelectedStore().getName() != null) {
                if (DataManager.getInstance().isDebug) {
                    storeName.setText(Utils.setDemoStoreName(dataManager.getCurrentSelectedStore()).getName().replace("Jamba Juice ", ""));
                } else {
                    storeName.setText(dataManager.getCurrentSelectedStore().getName().replace("Jamba Juice ", ""));
                }
            }
            if (dataManager.getCurrentSelectedStore().getCompleteAddress() != null) {
                storeAddress.setText(dataManager.getCurrentSelectedStore().getCompleteAddress());
            }
        }
    }

    private void showStoreView() {
        Store selectedStore = dataManager.getCurrentSelectedStore();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.B_IS_CHECKOUT_STORE_DETAIL_ONLY, true);
        bundle.putSerializable(Constants.B_STORE, selectedStore);
        bundle.putBoolean(Constants.B_IS_SHOW_BASKET, false);
        TransitionManager.transitFrom(this, StoreDetailActivity.class, bundle);
    }

    private void initScrollView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        //basketList.setOnTouchListener(new OnSwipeTouchListener(this, scrollView, basketList));
    }

    private void initSubtotal() {
        View view = findViewById(R.id.total);
        total = (TextView) view.findViewById(R.id.tv_amount_total);
        subtotal = (TextView) view.findViewById(R.id.tv_amount_subtotal);
        tax = (TextView) view.findViewById(R.id.tv_amount_tax);
        tv_deliveryEstimate = (TextView) view.findViewById(R.id.tv_deliveryEstimate);
        tv_discount = (TextView) view.findViewById(R.id.tv_discount);
        deliveryEstBtn = (ImageButton) view.findViewById(R.id.deliveryEstBtn);
        deliveryEstLayout = (RelativeLayout) view.findViewById(R.id.deliveryEstLayout);
    }

    private void initPromotionsOrRewards() {
        promotionsView = findViewById(R.id.promotions);
        rewardPromoViewHolder = new BasketRewardViewHolder(this, promotionsView);
        if (UserService.isUserAuthenticated()) {
            rewardPromoViewHolder.setTitle("Select Reward or Promotion");
        } else {
            rewardPromoViewHolder.setTitle("Enter Promotion code");
        }
        setUpRewardsOrPromotionalCode();
    }

    private void initBasketItemsList() {
        List<BasketProduct> productsData = dataManager.getCurrentBasket().getProducts();
        LinearLayout basketProductContainer = (LinearLayout) findViewById(R.id.basketProductContainer);
        basketAdapter = new BasketAdapter(this, basketProductContainer);
        basketAdapter.updateData(productsData);
    }

    private void initToolbar() {
        setUpToolBar(true);
        setTitle("Basket");
        setBackButton(true, true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(android.R.color.white));
    }

    //SeekBar Related Methods
    private void setTimeBar() {
        timeBar = (SeekBar) findViewById(R.id.seekbar);
        timeBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange_color), PorterDuff.Mode.SRC_IN);
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setTimeWithProgress(progress);
                    setPickUpTime();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        enableTimebar(false);
    }

    private void prepareTimeBar() {
        Store store = dataManager.getCurrentSelectedStore();
        if (store != null) {
            btnToday.setEnabled(true);
            btnTomorrow.setEnabled(true);
            btnLater.setEnabled(true);
            parseTimes(store.getStoreTiming());
            //setProgressAheadCurrentTime();
            if (dataManager.getCurrentBasket() != null) {
                pickUpDay = dataManager.getCurrentBasket().getPickUpDay();
            }
            setDaySelection();
            adjustTimeBar();
            setTimeWithProgress(0);//Set initial time
            setLastSelectedTimeIfPresent();
        }
    }

    private void prepareOrderType() {
        Store store = dataManager.getCurrentSelectedStore();
        if (store != null) {
            if (store.isSupportDelivery()) {
                pickUpBtn.setEnabled(true);
                deliveryBtn.setEnabled(true);

                orderType = dataManager.getCurrentBasket().getOrderType();

                if (orderType == PICKUP) {
                    pickUpSelected();
                }

                if (orderType == DELIVERY) {
                    deliverySelected();
                }

                setOrderTypeSelection();
            } else {
                orderType = dataManager.getCurrentBasket().getOrderType();

                if (orderType == PICKUP) {
                    pickUpSelected();
                }

                if (orderType == DELIVERY) {
                    deliverySelected();
                }

            }

        }
    }

    private void setLastSelectedTimeIfPresent() {
        Date timewanted = dataManager.getCurrentBasket().getTimewanted();
        int progress = 0;
        if (openDate != null && timewanted != null) {
            //Now map previous selected timewanted to selected day
            Calendar openDateCalendar = Calendar.getInstance();
            openDateCalendar.setTime(openDate);
            Calendar timewantedCalendar = Calendar.getInstance();
            timewantedCalendar.setTime(timewanted);
            timewantedCalendar.set(Calendar.YEAR, openDateCalendar.get(Calendar.YEAR));
            timewantedCalendar.set(Calendar.MONTH, openDateCalendar.get(Calendar.MONTH));
            timewantedCalendar.set(Calendar.DAY_OF_MONTH, openDateCalendar.get(Calendar.DAY_OF_MONTH));


            Calendar resetFutureAndLaterCalendar = Calendar.getInstance();
            resetFutureAndLaterCalendar.setTime(timewanted);
            if (btnLater.isSelected()) {
                year = resetFutureAndLaterCalendar.get(Calendar.YEAR);
                month = resetFutureAndLaterCalendar.get(Calendar.MONTH) + 1;
                day = resetFutureAndLaterCalendar.get(Calendar.DAY_OF_MONTH);
            }

            if (btnTomorrow.isSelected()) {
                pufYear = resetFutureAndLaterCalendar.get(Calendar.YEAR);
                pufMonth = resetFutureAndLaterCalendar.get(Calendar.MONTH) + 1;
                pufDay = resetFutureAndLaterCalendar.get(Calendar.DAY_OF_MONTH);
            }

            long difference = timewantedCalendar.getTime().getTime() - openDate.getTime();
            if (difference > 0) {
                progress = (int) Math.ceil(difference / FIFTEEN_MIN_MILLI_SEC);
                timeBar.setProgress(progress);
                setTimeWithProgress(progress);
            }
        }
        if (progress == 0) {
            timeBar.setProgress(0);
            setTimeWithProgress(0);
        }
    }

    private void parseTimes(StoreTiming storeTiming) {
        if (storeTiming != null && storeTiming.getRanges() != null) {
            ArrayList<TimeRange> ranges = storeTiming.getRanges();
            for (TimeRange range : ranges) {

                if (sunday == null || range.getFullWeekDayName().equalsIgnoreCase("Sunday")) {
                    sunday = range;
                }
                if (monday == null || range.getFullWeekDayName().equalsIgnoreCase("Monday")) {
                    monday = range;
                }
                if (tuesday == null || range.getFullWeekDayName().equalsIgnoreCase("Tuesday")) {
                    tuesday = range;
                }
                if (wednesday == null || range.getFullWeekDayName().equalsIgnoreCase("Wednesday")) {
                    wednesday = range;
                }
                if (thursday == null || range.getFullWeekDayName().equalsIgnoreCase("Thursday")) {
                    thursday = range;
                }
                if (friday == null || range.getFullWeekDayName().equalsIgnoreCase("Friday")) {
                    friday = range;
                }
                if (satday == null || range.getFullWeekDayName().equalsIgnoreCase("Saturday")) {
                    satday = range;
                }
            }
        }
    }

    private long adjustTimeBar() {
        long difference = 0;
        if (pickUpDay == ASAP) {
            return 0;
        }

        try {

            TimeRange range = null;

            Calendar calendar = null;

            if (btnLater.isSelected()) {
                calendar = new GregorianCalendar(year, month - 1, day);
            } else if (btnToday.isSelected()) {
                calendar = Calendar.getInstance();
            } else if (btnTomorrow.isSelected()) {
                calendar = new GregorianCalendar(pufYear, pufMonth - 1, pufDay);
            }

//            int result = calendar.get(Calendar.DAY_OF_WEEK);
//            switch (result) {
//                case Calendar.MONDAY:
//                    range = monday;
//                    break;
//
//                case Calendar.TUESDAY:
//                    range = tuesday;
//                    break;
//
//                case Calendar.WEDNESDAY:
//                    range = wednesday;
//                    break;
//
//                case Calendar.THURSDAY:
//                    range = thursday;
//                    break;
//
//                case Calendar.FRIDAY:
//                    range = friday;
//                    break;
//
//                case Calendar.SATURDAY:
//                    range = satday;
//                    break;
//
//                case Calendar.SUNDAY:
//                    range = sunday;
//                    break;
//            }

            SimpleDateFormat rangeDateFormat = new SimpleDateFormat(Server_Time_Format);
            SimpleDateFormat convertFormat = new SimpleDateFormat("yyyyMMdd");
            String selectedDate = convertFormat.format(calendar.getTime());

            StoreTiming storeTimings = dataManager.getSelectedStoreTiming();
            if (storeTimings != null
                    && storeTimings.getRanges() != null
                    && storeTimings.getRanges().size() > 0) {
                for (TimeRange timeRange : storeTimings.getRanges()) {
                    Date timeRangeDate = rangeDateFormat.parse(timeRange.getStart());
                    String convertedTimeRangeDate = convertFormat.format(timeRangeDate);
                    if (selectedDate.equalsIgnoreCase(convertedTimeRangeDate)) {
                        range = timeRange;
                        break;
                    }
                }
            }


            if (range == null) {
                enableTimebar(false);
                return 0;
            }

            enableTimebar(true);
            //Start time of progressbar should be earliest read time.
            String startTimeString = dataManager.getCurrentBasket().getEarliestreadytime(); //range.getStart();
            String endTimeString = range.getEnd();

            SimpleDateFormat serverFormat = new SimpleDateFormat(Server_Time_Format);
            SimpleDateFormat targetFormat = Utils.getTimeDisplayFormat(this);

            if (!btnToday.isSelected()) {
                startTimeString = range.getStart();
                openDate = serverFormat.parse(startTimeString);
            } else if (btnToday.isSelected()) {
                // openDate = Calendar.getInstance().getTime();
                openDate = Utils.getDateFromString(dataManager.getCurrentBasket().getEarliestreadytime(), "yyyyMMdd HH:mm");
            }

            //Open date should be of format 0,15,30,45
            Calendar cal = Calendar.getInstance();
            cal.setTime(openDate);
            if (!btnToday.isSelected()) {
                //Commented because we started to use earliestreadytime
                if (pickUpBtn.isSelected()) {
                    cal.add(Calendar.MINUTE, 15); // Add fifteen minutes for tomorrow order, else there will be an error.
                }
                if (deliveryBtn.isSelected()) {
                    cal.add(Calendar.MINUTE, 45); // Add fourty five minutes for tomorrow or later delivery, else there will be an error.
                }
            }

            int minutes = cal.get(Calendar.MINUTE);
            int remaining = minutes % 15;
            if (remaining > 0) {
                int minToAdd = 15 - remaining;
                cal.add(Calendar.MINUTE, minToAdd);
            }
            openDate = cal.getTime();


            closeDate = serverFormat.parse(endTimeString);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(closeDate);
            cal1.add(Calendar.MINUTE, -15);

            closeDate = cal1.getTime();

            if (openDate.after(closeDate)) {
                enableTimebar(false);
                return 0;
            }

            openTime.setText(targetFormat.format(openDate));
            closeTime.setText(targetFormat.format(closeDate));


            difference = Math.abs(closeDate.getTime() - openDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return difference;
    }

    private void enableDeliveryAddress() {
        if (deliveryBtn.isSelected()) {
            deliveryMainLayout.setVisibility(View.VISIBLE);
            deliveryEstLayout.setVisibility(View.VISIBLE);
        } else {
            deliveryMainLayout.setVisibility(View.GONE);
            deliveryEstLayout.setVisibility(View.GONE);
        }
    }


    private void enableTimebar(boolean enabled) {
        String storename = null;
        if (!enabled) {
            openTime.setVisibility(View.GONE);
            closeTime.setVisibility(View.GONE);
            selectedTime.setVisibility(View.GONE);
            timeBar.setVisibility(View.GONE);
            if (pickUpDay != ASAP) {
                if (DataManager.getInstance().getCurrentSelectedStore() != null) {
                    if (DataManager.getInstance().isDebug) {
                        storename = Utils.setDemoStoreName(DataManager.getInstance().getCurrentSelectedStore()).getName().replace("Jamba Juice ", "");
                    } else {
                        storename = DataManager.getInstance().getCurrentSelectedStore().getName().replace("Jamba Juice ", "");
                    }
                }
                if (storename != null) {
                    if ((btnTomorrow.isSelected() && Utils.isToday(pufYear, pufMonth, pufDay))
                            || (btnToday.isSelected())
                            || (btnLater.isSelected() && Utils.isToday(year, month, day))) {
                        storeNotAvailable.setText(storename + " is currently closed.");
                    } else {
                        storeNotAvailable.setText(storename + " will be closed.");
                    }
                } else {
                    if ((btnTomorrow.isSelected() && Utils.isToday(pufYear, pufMonth, pufDay))
                            || (btnToday.isSelected())
                            || (btnLater.isSelected() && Utils.isToday(year, month, day))) {
                        storeNotAvailable.setText("Store is currently closed.");
                    } else {
                        storeNotAvailable.setText("Store will be closed.");
                    }
                }
                storeNotAvailable.setVisibility(View.VISIBLE);
            } else {
                storeNotAvailable.setVisibility(View.GONE);
            }

        } else {
            openTime.setVisibility(View.VISIBLE);
            closeTime.setVisibility(View.VISIBLE);
            selectedTime.setVisibility(View.VISIBLE);
            timeBar.setVisibility(View.VISIBLE);
            storeNotAvailable.setVisibility(View.GONE);
        }
        timeBar.setEnabled(enabled);
    }

    private void setTimeWithProgress(int progress) {
        // time diff between store opening time and closing time
        final long diff = adjustTimeBar();
        if (diff == 0) {
            return;
        }
        // maximum number of time values the user can specify
        // between store opening and closing times that
        // user can specify that are fifteen minutes a part.
        int max = (int) Math.ceil(diff / FIFTEEN_MIN_MILLI_SEC);
        timeBar.setMax(max);

        int currProgress = (int) (progress * FIFTEEN_MIN_MILLI_SEC);
        if (currProgress > diff) {
            currProgress = (int) diff;
        }
        timeBar.setProgress(progress);
        Date diffDate = new Date(openDate.getTime() + currProgress);
        if (btnLater.isSelected()) {
            SimpleDateFormat laterFormat = new SimpleDateFormat("EEE dd MMM hh:mm a");

            Calendar timeWantedCalendar = Calendar.getInstance();
            timeWantedCalendar.setTime(diffDate);
            timeWantedCalendar.set(Calendar.YEAR, year);
            timeWantedCalendar.set(Calendar.MONTH, month - 1);
            timeWantedCalendar.set(Calendar.DAY_OF_MONTH, day);

            selectedTime.setText(laterFormat.format(timeWantedCalendar.getTime()));
        } else if (btnTomorrow.isSelected()) {
            SimpleDateFormat laterFormat = new SimpleDateFormat("EEE dd MMM hh:mm a");

            Calendar timeWantedCalendar = Calendar.getInstance();
            timeWantedCalendar.setTime(diffDate);
            timeWantedCalendar.set(Calendar.YEAR, pufYear);
            timeWantedCalendar.set(Calendar.MONTH, pufMonth - 1);
            timeWantedCalendar.set(Calendar.DAY_OF_MONTH, pufDay);

            selectedTime.setText(laterFormat.format(timeWantedCalendar.getTime()));
        } else {
            SimpleDateFormat format = Utils.getTimeDisplayFormat(this);
            Log.e("current: ", String.valueOf(diffDate));
            selectedTime.setText(format.format(diffDate));
        }
    }

    private void setDaySelection() {
        btnAsap.setSelected(false);
        btnToday.setSelected(false);
        btnTomorrow.setSelected(false);
        btnLater.setSelected(false);

        if (this.pickUpDay == ASAP) {
            btnAsap.setSelected(true);
        } else if (pickUpDay == TODAY) {
            btnToday.setSelected(true);
        } else if (pickUpDay == TOMORROW) {
            btnTomorrow.setSelected(true);
        } else if (pickUpDay == LATER) {
            btnLater.setSelected(true);
        }
    }

    private void setOrderTypeSelection() {
        pickUpBtn.setSelected(false);
        deliveryBtn.setSelected(false);

        if (this.orderType == PICKUP) {
            pickUpBtn.setSelected(true);
        } else if (orderType == DELIVERY) {
            deliveryBtn.setSelected(true);
        }

        enableDeliveryAddress();
    }

    private void setUpRewardsOrPromotionalCode() {
        Basket basket = dataManager.getCurrentBasket();
        String couponCode = basket.getPromotionCode();
        if (basket.getDiscount() > 0 && rewardPromoViewHolder != null && StringUtilities.isValidString(couponCode)) {
            rewardPromoViewHolder.setSwipeEnabled(true);
            rewardPromoViewHolder.setDetail(couponCode);
            rewardPromoViewHolder.setAmount(basket.getDiscount());
        } else if (basket.getAppliedRewards().size() > 0 && rewardPromoViewHolder != null) {
            String desc = "";
            for (Reward reward : basket.getAppliedRewards()) {
                if (desc.equals("")) {
                    desc = reward.getRewardTitle();
                } else {
                    desc = ", " + reward.getRewardTitle();
                }
            }
            rewardPromoViewHolder.setSwipeEnabled(true);
            rewardPromoViewHolder.setDetail(desc);
            rewardPromoViewHolder.setAmount(basket.getDiscount());
        } else {
            rewardPromoViewHolder.setSwipeEnabled(false);
            rewardPromoViewHolder.setDetail("NOTHING SELECTED");
            rewardPromoViewHolder.setAmount(0);
        }
    }

    //Update product Related Methods
    public void refreshData() {
        if (dataManager.getCurrentBasket().totalProductsCount() == Constants.TOTAL_BASKET_PRODUCTS) {
            addAnotherItem.setVisibility(View.GONE);
        } else {
            addAnotherItem.setVisibility(View.VISIBLE);
        }
        Basket currentBasket = dataManager.getCurrentBasket();
        if (currentBasket != null) {
            tax.setText(Utils.formatPrice(currentBasket.getSalestax()));
            subtotal.setText(Utils.formatPrice(currentBasket.getSubtotal()));
            total.setText(Utils.formatPrice(currentBasket.getTotal()));
            tv_total_amount.setText(Utils.formatPrice(currentBasket.getTotal()));
            tv_deliveryEstimate.setText(Utils.formatPrice(currentBasket.getDeliveryCost()));
            tv_discount.setText("-" + Utils.formatPrice(currentBasket.getDiscount()));
            basketAdapter.updateData(currentBasket.getProducts());
            deliveryEstBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.showAlert(BasketActivity.this, "We use multiple providers to find you the most efficient delivery option. Delivery charges reflect current availability and may change.", "Info");
                }
            });
            setUpRewardsOrPromotionalCode();
            setStoreInfo();
            setDeliveryAddress();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOrderType();
        setTimeBar();
        prepareTimeBar();
        prepareOrderType();
        refreshData();
        Intent intent = getIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null)
            setIntent(intent);
    }

    private void showProductUpdateScreen(int position) {
        BasketProduct basketProduct = DataManager.getInstance().getCurrentBasket().getProducts().get(position);
        Product product = DataManager.getInstance().getParseProductWithProductId(basketProduct.getProductId());
        dataLoaded(product, basketProduct);
    }

    private void dataLoaded(Product product, BasketProduct basketProduct) {
        if (product != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.B_PRODUCT, product);
            bundle.putSerializable(Constants.B_BASKET_PRODUCT, basketProduct);
            TransitionManager.transitFrom(this, ProductUpdateActivity.class, bundle);
        }
    }

    private boolean isItBasketStoreDetailScreen() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getBoolean(Constants.B_IS_IT_FROM_BASKET_STORE_SCREEN);
        }
        return false;
    }

    @Override
    protected void handleBroadCastReceiver(Intent intent) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isItBasketStoreDetailScreen()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                TransitionManager.transitFrom(this, HomeActivity.class);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public enum PickUpDay {
        ASAP(1), TODAY(2), TOMORROW(3), LATER(4);
        public int val;

        PickUpDay(int val) {
            this.val = val;
        }

    }

    public enum OrderType {
        PICKUP(1), DELIVERY(2);
        public int val;

        private OrderType(int val) {
            this.val = val;
        }

    }
}