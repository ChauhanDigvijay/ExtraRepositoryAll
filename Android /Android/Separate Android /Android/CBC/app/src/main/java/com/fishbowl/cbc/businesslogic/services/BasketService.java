package com.fishbowl.cbc.businesslogic.services;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.fishbowl.apps.olo.Interfaces.OloBasketLoyaltyRewardsCallback;
import com.fishbowl.apps.olo.Interfaces.OloBasketLoyaltySchemesCallback;
import com.fishbowl.apps.olo.Interfaces.OloBasketServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloBasketSubmitServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloBasketValidationCallback;
import com.fishbowl.apps.olo.Interfaces.OloBatchProductBasketServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloBillingSchemeServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloErrorCallback;
import com.fishbowl.apps.olo.Interfaces.OloSavedDispatchAddressesCallBack;
import com.fishbowl.apps.olo.Interfaces.OloSwitchBasketServiceCallback;
import com.fishbowl.apps.olo.Models.OloBasket;
import com.fishbowl.apps.olo.Models.OloBasketChoice;
import com.fishbowl.apps.olo.Models.OloBasketProduct;
import com.fishbowl.apps.olo.Models.OloBasketProductBatchResult;
import com.fishbowl.apps.olo.Models.OloBasketValidation;
import com.fishbowl.apps.olo.Models.OloBillingAccount;
import com.fishbowl.apps.olo.Models.OloBillingScheme;
import com.fishbowl.apps.olo.Models.OloDeliveryAddress;
import com.fishbowl.apps.olo.Models.OloLoyaltyReward;
import com.fishbowl.apps.olo.Models.OloLoyaltyScheme;
import com.fishbowl.apps.olo.Models.OloOrderInfo;
import com.fishbowl.apps.olo.Models.OloOrderStatus;
import com.fishbowl.apps.olo.Models.OloSwitchBasket;
import com.fishbowl.apps.olo.Services.OloBasketService;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.cbc.CbcApplication;
import com.fishbowl.cbc.businesslogic.analytics.AnalyticsManager;
import com.fishbowl.cbc.businesslogic.analytics.CbcAnalyticsManager;
import com.fishbowl.cbc.businesslogic.interfaces.AllStoreMenuCallBack;
import com.fishbowl.cbc.businesslogic.interfaces.BasketRewardsCallback;
import com.fishbowl.cbc.businesslogic.interfaces.BasketServiceCallback;
import com.fishbowl.cbc.businesslogic.interfaces.BasketValidationCallback;
import com.fishbowl.cbc.businesslogic.interfaces.BillingAccountsCallback;
import com.fishbowl.cbc.businesslogic.interfaces.SavedDispatchAddressesCallBack;
import com.fishbowl.cbc.businesslogic.interfaces.StoreDetailCallback;
import com.fishbowl.cbc.businesslogic.interfaces.StoreMenuCallback;
import com.fishbowl.cbc.businesslogic.interfaces.SwitchBasketServiceCallBack;
import com.fishbowl.cbc.businesslogic.managers.DataManager;
import com.fishbowl.cbc.businesslogic.models.Basket;
import com.fishbowl.cbc.businesslogic.models.BasketChoice;
import com.fishbowl.cbc.businesslogic.models.BasketProduct;
import com.fishbowl.cbc.businesslogic.models.BillingAccount;
import com.fishbowl.cbc.businesslogic.models.DeliveryAddress;
import com.fishbowl.cbc.businesslogic.models.OrderStatus;
import com.fishbowl.cbc.businesslogic.models.PlaceOrderCallback;
import com.fishbowl.cbc.businesslogic.models.RecentOrder;
import com.fishbowl.cbc.businesslogic.models.Reward;
import com.fishbowl.cbc.businesslogic.models.Store;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProduct;
import com.fishbowl.cbc.utils.Constants;
import com.fishbowl.cbc.utils.SharedPreferenceHandler;
import com.fishbowl.cbc.utils.Utils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.fishbowl.cbc.utils.Constants.GA_CATEGORY.ORDER_AHEAD;

/**
 * Created by VT027 on 5/22/2017.
 */

public class BasketService {
    public static void refreshBasket(Activity activity, final BasketServiceCallback callback) {
        String basketId = DataManager.getInstance().getCurrentBasket().getId();
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketService.getBasketWithId(basketId, new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                updateBasketAndNotify(oloBasket, callbackWeakReference, error, callback);
            }
        });
    }

    public static void createBasket(Activity activity, int vendorId, final BasketServiceCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketService.createBasket(vendorId, new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                if (oloBasket != null) {
                    setBasket(oloBasket);
                    notifyBasketCreation(callbackWeakReference, error, callback);

                    //JambaAnalyticsManager.sharedInstance().track_ItemWith(oloBasket.getId(), String.valueOf(oloBasket.getVendorId()), FBEventSettings.CREATE_BASKET);
                } else if (error != null) {
                    notifyBasketCreation(callbackWeakReference, error, callback);
                }
                AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "create_basket");
            }
        });
    }

    public static void switchBasket(Activity activity, int vendorId, String basketId, final SwitchBasketServiceCallBack callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketService.switchBasket(vendorId, basketId, new OloSwitchBasketServiceCallback() {
            @Override
            public void onSwitchBasketServiceCallback(OloSwitchBasket switchBasket, Exception error) {

                callback.onSwitchBasketServiceCallback(switchBasket, error);
            }
        });
    }

    public static void dispatchAddress(Activity activity, DeliveryAddress deliveryAddress, String basketId, final BasketServiceCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloDeliveryAddress oloDeliveryAddress = convertToOloDeliveryAddress(deliveryAddress);
        OloBasketService.dispatchAddress(basketId, oloDeliveryAddress, new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                updateBasketAndNotify(basket, callbackWeakReference, error, callback);
            }
        });
    }

    public static void deleteAddress(Activity activity, int addressId, final BasketServiceCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        String authToken = UserService.getUser().getOloAuthToken();
        OloBasketService.removeAddress(addressId, authToken, new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callback.onBasketServiceCallback(null, error);
                }
            }
        });
    }

    public static void deliveryMode(Activity activity, String deliveryMode, String basketId, final BasketServiceCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketService.deliveryMode(basketId, deliveryMode, new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                updateBasketAndNotify(basket, callbackWeakReference, error, callback);
            }
        });
    }

    public static void addProductToBasket(Activity activity, String basketId, final BasketProduct product, final BasketServiceCallback callback) {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketServiceCallback(null, new Exception("Basket not found"));
            return;
        }
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketProduct oloProduct = convertToOloBasketProduct(product);
        OloBasketService.addProductToBasketBatch(basketId, oloProduct, new OloBatchProductBasketServiceCallback() {
            @Override
            public void onBatchProductBasketServiceCallback(OloBasketProductBatchResult result, Exception error) {
                OloBasket oloBasket = null;
                if (result != null) {
                    oloBasket = result.getBasket();
                    if (error == null) {
                        AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "add_product", product.getName(), product.getQuantity(), null);
                        AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "special_instructions", product.getSpecialinstructions(), 0, null);
                        if (product.getOptions() != null) {
                            String[] options = product.getOptions().split(",");
                            for (String option : options) {


                                for (int i = 0; i < oloBasket.getProducts().size(); i++) {
                                    OloBasketProduct obp = oloBasket.getProducts().get(i);

                                    if (obp.getProductId() == product.getProductId()) {

//                                        JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(obp.getProductId())
//                                                ,"PRODUCT_NAME = "+obp.getName()+";TOTAL_QUANTITY = "+obp.getQuantity()+";TOTAL_COST = "+obp.getTotalCost()
//                                                , FBEventSettings.ADD_PRODUCT);

                                    }
                                }

                            }
                        }
                    }
                }
                updateBasketAndNotify(oloBasket, callbackWeakReference, error, callback);
            }
        });
    }

    public static void updateProduct(Activity activity, String basketId, BasketProduct product, final BasketServiceCallback callback) {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketServiceCallback(null, new Exception("Basket not found"));
            return;
        }
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketProduct oloProduct = convertToOloBasketProduct(product);
        OloBasketService.updateProduct(basketId, oloProduct, new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                updateBasketAndNotify(oloBasket, callbackWeakReference, error, callback);
            }
        });
    }

    public static void deleteProduct(Activity activity, String basketId, final BasketProduct product, final BasketServiceCallback callback) {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketServiceCallback(null, new Exception("Basket not found"));
            return;
        }
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketService.deleteProduct(basketId, product.getId(), new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                updateBasketAndNotify(oloBasket, callbackWeakReference, error, callback);
                if (error == null) {
//                    JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(obp.getProductId())
//                            ,"PRODUCT_NAME = "+obp.getName()+";TOTAL_QUANTITY = "+obp.getQuantity()+";TOTAL_COST = "+obp.getTotalCost()
//                            , FBEventSettings.ADD_PRODUCT);
                    AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "remove_product", product.getName(), product.getQuantity(), null);
                    if (product.getChoices() != null) {
                        for (BasketChoice choice : product.getChoices()) {
                            //JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(product.getId()), "Optionid=" + String.valueOf(choice.getOptionid()), FBEventSettings.REMOVE_BOOST);
                            AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "remove_boost", choice.getOptionid() + "");
                        }
                    }
                }
            }
        });
    }

    public static void placeOrder(Activity activity, final PlaceOrderCallback callback) {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onPlaceOrderCallback(null, new Exception("Basket not found"));
            return;
        } else {
            CbcAnalyticsManager.sharedInstance().track_ItemWith(basket.getId(), "", FBEventSettings.SUBMIT_ORDER);
        }
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        validateBasket(new BasketValidationCallback() {
            @Override
            public void onBasketValidated(Exception exception) {
                if (exception == null) {
                    OloOrderInfo oloOrderInfo = DataManager.getInstance().getOrderInfo();
                    if (UserService.isUserAuthenticated()) {
                        oloOrderInfo.setUserType("user");
                        oloOrderInfo.setContactNumber(UserService.getUser().getContactnumber());
                    } else {
                        oloOrderInfo.setUserType("guest");
                    }
                    OloBasketService.submitOrder(oloOrderInfo, new OloBasketSubmitServiceCallback() {
                        @Override
                        public void onOloBasketSubmitServiceCallback(OloOrderStatus oloOrderStatus, Exception exception) {
                            OrderStatus orderStatus = null;
                            if (oloOrderStatus != null) {
                                //Order has been successfully placed
                                DataManager dataManager = DataManager.getInstance();
                                orderStatus = new OrderStatus(oloOrderStatus);
                                dataManager.setOrderStatus(orderStatus);
                                RecentOrdersService.cacheRecentProducts();
                                Utils.notifyHomeScreenUpdate(callbackWeakReference.get());
                                Intent intent = new Intent(Constants.BROADCAST_UPDATE_RECENT_ORDER);
                                LocalBroadcastManager.getInstance(CbcApplication.getInstance()).sendBroadcast(intent);


                                ArrayList<BasketProduct> products = dataManager.getCurrentBasket().getProducts();
                                int countProuduct = products.size();
                                StringBuilder ids = new StringBuilder();
                                StringBuilder name = new StringBuilder();
                                for (int i = 0; i < countProuduct; i++) {
                                    BasketProduct product = products.get(i);
                                    ids.append(product.getProductId());
                                    ids.append(",");
                                    //name.append(product.getName());
                                    //name.append(",");
                                }


                                //Analytics
                                AnalyticsManager.getInstance().trackPurchase(orderStatus, dataManager.getCurrentBasket().getProducts());
                                AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "submit_order", "basket_size_" + dataManager.getCurrentBasket().getProducts().size());
                                //Push Notification
                                boolean isFirstPurchase = SharedPreferenceHandler.getBoolean(SharedPreferenceHandler.FirstPurchase, false);
                                //removed urbanairship
//                                if(isFirstPurchase)
//                                {
//                                    NotificationManager.removeTag(FirstPurchase.value);
//                                }
//                                else
//                                {
//                                    NotificationManager.addTag(FirstPurchase.value);
//                                    SharedPreferenceHandler.put(SharedPreferenceHandler.FirstPurchase, true);
//                                }

                                //Audit Log
                                /**
                                 * TODO: uncomment when audit service is available
                                 */
                               // AuditService.trackOrder(dataManager.getCurrentBasket(), orderStatus);
                            }
                            //Notify order status or error
                            notifyOrderStatus(callbackWeakReference, orderStatus, exception, callback);
                        }
                    });
                } else {
                    //Notifiy basket validation error
                    notifyOrderStatus(callbackWeakReference, null, exception, callback);
                }
            }
        });
    }

    public static void getBillingAccountsForCurrentBasket(Activity activity, final BillingAccountsCallback billingAccountsCallback) {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            billingAccountsCallback.onBillingAccountsCallback(null, new Exception("Basket not found"));
            return;
        }
//        ArrayList<BillingAccount> accounts = DataManager.getInstance().getBillingAccount();
//        if (accounts != null) {
//            billingAccountsCallback.onBillingAccountsCallback(accounts, null);
//        } else {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        String basketId = DataManager.getInstance().getCurrentBasket().getId();
        OloBasketService.getBillingSchemes(basketId, new OloBillingSchemeServiceCallback() {
            @Override
            public void onBillingSchemeServiceCallback(ArrayList<OloBillingScheme> oloBillingSchemes, Exception error) {
                ArrayList<BillingAccount> billingAccounts = null;
                if (oloBillingSchemes != null) {
                    billingAccounts = new ArrayList<BillingAccount>();
                    for (OloBillingScheme oloBillingScheme : oloBillingSchemes) {
                        if (oloBillingScheme.getType().equals("creditcard")
                                || oloBillingScheme.getType().equals("payinstore")) {
                            ArrayList<OloBillingAccount> oloBillingAccounts = oloBillingScheme.getAccounts();
                            if (oloBillingAccounts != null) {
                                for (OloBillingAccount oloBillingAccount : oloBillingAccounts) {
                                    BillingAccount billingAccount = new BillingAccount(oloBillingAccount);
                                    billingAccounts.add(billingAccount);
                                }
                            }
                        }
                    }
                    DataManager.getInstance().setBillingAccount(billingAccounts);
                }
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    billingAccountsCallback.onBillingAccountsCallback(billingAccounts, error);
                }
            }
        });
        //}
    }

    public static void getSavedDeliveryAddresses(Activity activity, final SavedDispatchAddressesCallBack callBack) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        String authToken = UserService.getUser().getOloAuthToken();
        OloBasketService.getSavedDispatchAddress(authToken, new OloSavedDispatchAddressesCallBack() {
            @Override
            public void onSavedDispatchAddresses(ArrayList<OloDeliveryAddress> oloDeliveryAddresses, Exception error) {

                ArrayList<DeliveryAddress> deliveryAddresses = new ArrayList<DeliveryAddress>();
                if (oloDeliveryAddresses != null) {
                    for (OloDeliveryAddress oloDeliveryAddress : oloDeliveryAddresses) {
                        DeliveryAddress deliveryAddress = new DeliveryAddress(oloDeliveryAddress);
                        deliveryAddresses.add(deliveryAddress);
                    }
                }
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callBack.onSavedDispatchAddresses(deliveryAddresses, error);
                }
            }
        });
    }

    private static void setTimeWanted(final BasketServiceCallback callback) {
        DataManager manager = DataManager.getInstance();
        final Basket currentBasket = manager.getCurrentBasket();
        Date timeWanted = currentBasket.getTimewanted();
        if (timeWanted == null) {
            CbcAnalyticsManager.sharedInstance().track_ItemWith(currentBasket.getId(), "ASAP", FBEventSettings.PICK_UP_TIME);
            // If Time Wanted is not set, remove any previously times set on server.
            OloBasketService.deleteTimeWanted(currentBasket.getId(), new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                    updateBasket(oloBasket);
                    callback.onBasketServiceCallback(DataManager.getInstance().getCurrentBasket(), error);
                    AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "pickup_time", "ASAP");

                }
            });
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeWanted);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);
            int a = calendar.get(Calendar.AM_PM);
            String AM_PM = "";
            if (a == Calendar.AM) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }

            final String basketId = currentBasket.getId();

            Calendar cal1 = Calendar.getInstance();
            boolean isToday = cal1.get(Calendar.ERA) == calendar.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
            final String todayOrTomorrow = isToday ? "TODAY" : "TOMORROW";
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); //like "HH:mm" or just "mm", whatever you want
            final String time = sdf.format(timeWanted);
            // final String time = hourIn12 + ":" + minute;
            CbcAnalyticsManager.sharedInstance().track_ItemWith(basketId, todayOrTomorrow + ":" + time.replace("am", "AM").replace("pm", "PM"), FBEventSettings.PICK_UP_TIME);

            OloBasketService.setTimeWanted(basketId, false, year, month, day, hour, minute, new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                    updateBasket(oloBasket);
                    callback.onBasketServiceCallback(DataManager.getInstance().getCurrentBasket(), error);
                    if (error == null) {
//                        Calendar cal1 = Calendar.getInstance();
//                        boolean isToday = cal1.get(Calendar.ERA) == calendar.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
//                        String day = isToday ? "TODAY" : "TOMORROW";
//                        String time = hour + ":" + minute;
                        AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "pickup_time", todayOrTomorrow + ":" + time, 0, null);
                        //JambaAnalyticsManager.sharedInstance().track_ItemWith(basketId, day + ":" + time +" "+ finalAM_PM, FBEventSettings.PICK_UP_TIME);
                    }
                }
            });
        }
    }

    public static void validateBasket(final BasketValidationCallback callback) {
        DataManager manager = DataManager.getInstance();
        Basket currentBasket = manager.getCurrentBasket();
        OloBasketService.validateBasket(currentBasket.getId(), new OloBasketValidationCallback() {
            @Override
            public void onOloBasketValidationCallback(OloBasketValidation oloBasketValidation, Exception e) {
                callback.onBasketValidated(e);
            }
        });
    }

    public static void setTimeWantedAndValidateBasket(final BasketValidationCallback callback) {
        setTimeWanted(new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                if (e == null) {
                    validateBasket(callback);
                } else {
                    callback.onBasketValidated(e);
                }
            }
        });
    }

    // Retrieve available rewards for the current basket / restaurant
    // We need to ensure loyalty accounts are linked, which we do by posting the phonenumber to loyaltyschemes
    //  1. Get loyatly schemes, if membership is null, post phone number and get loyalty schemes
    // 2. Get rewards available for that membership id
    public static void availableRewards(Activity activity, final BasketRewardsCallback callback) {
        final Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketRewardsCallback(null, new Exception("Basket not found"));
            return;
        }

        final String phoneNumber = UserService.getUser().getContactnumber();
        if (phoneNumber == null || phoneNumber.equals("")) {
            callback.onBasketRewardsCallback(null, new Exception("User phone number not found."));
            return;
        }
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloBasketService.getLoyaltySchemes(basket.getId(), new OloBasketLoyaltySchemesCallback() {
            @Override
            public void onLoyaltySchemesCallback(ArrayList<OloLoyaltyScheme> oloLoyaltySchemes, Exception error) {
                if (oloLoyaltySchemes != null) {
                    OloLoyaltyScheme jambaInsiderScheme = null;
                    for (OloLoyaltyScheme scheme : oloLoyaltySchemes) {
                        if (scheme.getName() != null && scheme.getName().toLowerCase().equals("Jamba Insider Rewards".toLowerCase())) {
                            jambaInsiderScheme = scheme;
                            break;
                        }
                    }
                    if (jambaInsiderScheme == null) {
                        notifyBasketRewards(new ArrayList<Reward>(), null, callbackWeakReference, callback); // No rewards available for the user
                        return;
                    }
                    // Check if user is member yet, if not, make it a member and try again
                    int membershipId = 0;
                    if (jambaInsiderScheme.getMembership() != null) {
                        membershipId = jambaInsiderScheme.getMembership().getId();
                    }
                    if (membershipId == 0) {
                        OloBasketService.configureLoyaltySchemes(basket.getId(), jambaInsiderScheme.getId(), phoneNumber, new OloErrorCallback() {
                            @Override
                            public void onErrorCallback(Exception error) {
                                if (error != null) {
                                    notifyBasketRewards(null, error, callbackWeakReference, callback);
                                    return;
                                }
                                BasketService.availableRewards(callbackWeakReference.get(), callback);
                            }
                        });
                        return;
                    }

                    OloBasketService.getLoyaltyRewards(basket.getId(), membershipId, new OloBasketLoyaltyRewardsCallback() {
                        @Override
                        public void onBasketLoyaltyRewardsCallback(ArrayList<OloLoyaltyReward> oloLoyaltyReward, Exception error) {
                            ArrayList<Reward> rewards = null;
                            if (oloLoyaltyReward != null) {
                                rewards = new ArrayList<Reward>();
                                for (OloLoyaltyReward loyaltyReward : oloLoyaltyReward) {
                                    rewards.add(new Reward(loyaltyReward));
                                }
                            }
                            notifyBasketRewards(rewards, error, callbackWeakReference, callback);
                        }
                    });
                } else {
                    notifyBasketRewards(null, error, callbackWeakReference, callback);
                }
            }
        });
    }

    //Apply coupon (Promotional code)
    public static void applyCoupon(Activity activity, final String couponCode, final BasketServiceCallback callback) {
        final Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketServiceCallback(null, new Exception("Basket not found"));
            return;
        }
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        String basketId = basket.getId();
        if (basket.getAppliedRewards().size() > 0) {
            removeRewards(new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception error) {
                    if (error != null) {
                        Activity cb = callbackWeakReference.get();
                        if (cb != null && !cb.isFinishing()) {
                            callback.onBasketServiceCallback(DataManager.getInstance().getCurrentBasket(), error);
                        }
                        return;
                    }
                    applyCoupon(callbackWeakReference.get(), couponCode, callback);
                }
            });
            return;
        }

        if (basket.getPromotionCode() != null && !basket.getPromotionCode().equals("")) {
            final String promocod = basket.getPromotionCode();
            removeCoupon(new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket basket, Exception error) {
                    if (error != null) {
                        updateBasketAndNotify(basket, callbackWeakReference, error, callback);
                        return;
                    }
                    applyCoupon(callbackWeakReference.get(), couponCode, callback);
                }
            });
            return;
        }

        OloBasketService.applyCoupon(couponCode, basketId, new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                updateBasketAndNotify(basket, callbackWeakReference, error, callback);
                if (error == null) {
                    AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "apply_coupon");
                }
            }
        });
    }

    //Apply reward
    public static void applyRewards(Activity activity, final Reward reward, final BasketServiceCallback callback) {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketServiceCallback(null, new Exception("Basket not found"));
            return;
        }
        if (reward.getMembershipId() == 0) {
            callback.onBasketServiceCallback(null, new Exception("Missing reward membership Id"));
            return;
        }
        if (reward.getReference() == null || reward.getReference().equals("")) {
            callback.onBasketServiceCallback(null, new Exception("Missing reward reference"));
            return;
        }
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        if (basket.getAppliedRewards().size() > 0) {
            removeRewards(new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception error) {
                    if (error != null) {
                        Activity cb = callbackWeakReference.get();
                        if (cb != null && !cb.isFinishing()) {
                            callback.onBasketServiceCallback(DataManager.getInstance().getCurrentBasket(), error);
                        }
                        return;
                    } else {
                        applyRewards(callbackWeakReference.get(), reward, callback);
                    }
                }
            });
            return;
        }

        if (basket.getPromotionCode() != null && !basket.getPromotionCode().equals("")) {
            removeCoupon(new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket basket, Exception error) {
                    if (error != null) {
                        Activity cb = callbackWeakReference.get();
                        if (cb != null && !cb.isFinishing()) {
                            callback.onBasketServiceCallback(DataManager.getInstance().getCurrentBasket(), error);
                        }
                        return;
                    }
                    applyRewards(callbackWeakReference.get(), reward, callback);
                }
            });
            return;
        }


        OloBasketService.applyReward(basket.getId(), reward.getMembershipId(), reward.getReference(), new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                if (error == null) {
                    //Analytic in BasketRewardAvtivity

                }
                updateBasketAndNotify(basket, callbackWeakReference, error, callback);
            }
        });
    }

    // Remove promotional code
    public static void removeCoupon(final OloBasketServiceCallback callback) {
        final Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketServiceCallback(null, new Exception("Basket not found"));
            return;
        }
        OloBasketService.deleteCoupon(basket.getId(), new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                if (error == null) {
                    AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "remove_coupon", basket.getPromotionCode());
                    updateBasket(oloBasket);
                    DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                }
                callback.onBasketServiceCallback(oloBasket, error);
            }
        });
    }

    /// Remove any applied rewards
    public static void removeRewards(final BasketServiceCallback callback) {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        if (basket == null) {
            callback.onBasketServiceCallback(null, new Exception("Basket not found"));
            return;
        }
        ArrayList<Reward> rewards = basket.getAppliedRewards();
        if (rewards.size() > 0) {
            final Reward appliedReward = rewards.get(0);
            if (appliedReward.getRewardId() == 0) {
                callback.onBasketServiceCallback(null, new Exception("Missing reward id"));
                return;
            } else {
                CbcAnalyticsManager.sharedInstance().track_ItemWith("", "REWARD_TITLE:" + appliedReward.getRewardTitle() + ";MEMBERSHIP_ID:" + appliedReward.getMembershipId() + ";REFERENCE:" + appliedReward.getReference(), FBEventSettings.REMOVE_REWARD);
            }
            OloBasketService.removeReward(basket.getId(), appliedReward.getRewardId(), new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket basket, Exception error) {
                    if (error != null) {
                        callback.onBasketServiceCallback(null, error);
                        return;
                    }
                    AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "remove_reward", appliedReward.getRewardTitle());
                    updateBasket(basket);
                    Basket updatedBasket = DataManager.getInstance().getCurrentBasket();
                    if (updatedBasket.getAppliedRewards().size() > 0) {
                        removeRewards(callback);
                        return;
                    }
                    callback.onBasketServiceCallback(updatedBasket, null);
                }
            });
        } else {
            callback.onBasketServiceCallback(basket, null); // No reward applied to this basket
        }
    }

    private static void notifyBasketRewards(ArrayList<Reward> rewards, Exception error, WeakReference<Activity> callbackWeakReference, BasketRewardsCallback callback) {
        Activity activity = callbackWeakReference.get();
        if (activity != null && !activity.isFinishing()) {
            callback.onBasketRewardsCallback(rewards, error);
        }
    }

    private static void notifyOrderStatus(WeakReference<Activity> callbackWeakReference, OrderStatus orderStatus, Exception exception, final PlaceOrderCallback callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onPlaceOrderCallback(orderStatus, exception);
        }

    }

    private static void updateBasketAndNotify(OloBasket oloBasket, WeakReference<Activity> callbackWeakReference, Exception exception, BasketServiceCallback callback) {
        if (exception == null) {
            updateBasket(oloBasket);
        }
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onBasketServiceCallback(DataManager.getInstance().getCurrentBasket(), exception);
        }
    }

    private static void updateBasket(OloBasket oloBasket) {
        if (oloBasket != null) {
            DataManager manager = DataManager.getInstance();
            manager.updateBasket(oloBasket);
        }
    }

    public static void setBasket(OloBasket oloBasket) {
        if (oloBasket != null) {
            DataManager manager = DataManager.getInstance();
            Basket basket = new Basket(oloBasket);
            manager.setCurrentBasket(basket);
        }
    }

    private static OloBasketProduct convertToOloBasketProduct(BasketProduct basketProduct) {
        OloBasketProduct oloBasketProduct = new OloBasketProduct();
        ArrayList<OloBasketChoice> oloBasketChoices = new ArrayList<>();
        oloBasketProduct.setId(basketProduct.getId());
        oloBasketProduct.setProductId(basketProduct.getProductId());
        oloBasketProduct.setQuantity(basketProduct.getQuantity());
        if (basketProduct.getChoices() != null) {
            for (BasketChoice basketChoice : basketProduct.getChoices()) {
                OloBasketChoice oloBasketChoice = new OloBasketChoice();
                AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "add_boost", String.valueOf(basketChoice.getId()));
                boolean isExist = true;
                for (OloBasketChoice oloBasketChoice1 : oloBasketChoices) {
                    if (oloBasketChoice1.getId() == basketChoice.getId()) {
                        isExist = false;
                    }
                }
                if (isExist) {
                    oloBasketChoice.setId(basketChoice.getId());
                    oloBasketChoice.setQuantity(basketChoice.getQuantity());
                    oloBasketChoices.add(oloBasketChoice);
                }
            }
        }
        oloBasketProduct.setChoices(oloBasketChoices);
        return oloBasketProduct;
    }

    private static OloDeliveryAddress convertToOloDeliveryAddress(DeliveryAddress deliveryAddress) {
        OloDeliveryAddress oloDeliveryAddress = new OloDeliveryAddress();
        oloDeliveryAddress.setStreetaddress(deliveryAddress.getStreetaddress());
        oloDeliveryAddress.setBuilding(deliveryAddress.getBuilding());
        oloDeliveryAddress.setId(deliveryAddress.getId());
        oloDeliveryAddress.setCity(deliveryAddress.getCity());
        oloDeliveryAddress.setPhonenumber(deliveryAddress.getPhonenumber());
        oloDeliveryAddress.setZipcode(deliveryAddress.getZipcode());
        oloDeliveryAddress.setSpecialinstructions(deliveryAddress.getSpecialinstructions());

        return oloDeliveryAddress;
    }

    private static void notifyBasketCreation(WeakReference<Activity> callbackWeakReference, Exception exception, final BasketServiceCallback callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onBasketServiceCallback(DataManager.getInstance().getCurrentBasket(), exception);
        }
    }

//    public static void createBasketFromOrder(Activity activity, final RecentOrder recentOrder, final BasketServiceCallback callback)
//    {
//        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
//        StoreService.getStoreInformation(recentOrder.getVendorextref(), new StoreDetailCallback()
//        {
//            @Override
//            public void onStoreDetailCallback(final Store store, Exception exception)
//            {
//                if (store != null)
//                {
//                    StoreService.getStoreMenu(callbackWeakReference.get(), store.getRestaurantId(), new StoreMenuCallback()
//                    {
//                        @Override
//                        public void onStoreMenuCallback(final HashMap<Integer, StoreMenuProduct> storeProducts,Exception exception)
//                        {
//                            if (storeProducts != null)
//                            {
//                                OloBasketService.createFromOrder(recentOrder.getId(), recentOrder.getReference(), new OloBasketServiceCallback()
//                                {
//                                    @Override
//                                    public void onBasketServiceCallback(OloBasket oloBasket, Exception error)
//                                    {
//                                        if (oloBasket != null)
//                                        {
//                                            setBasket(oloBasket);
//                                            DataManager manager = DataManager.getInstance();
//                                            manager.setCurrentSelectedStore(store);
//                                            manager.setStoreMenu(storeProducts);
//
//                                           // AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "order_again", recentOrder.getReference());
//                                        }
//                                        notifyBasketCreation(callbackWeakReference, error, callback);
//                                    }
//                                });
//
//                            }
//                            else
//                            {
//                                notifyBasketCreation(callbackWeakReference, exception, callback);
//                            }
//                        }
//                    });
//                }
//                else
//                {
//                    notifyBasketCreation(callbackWeakReference, exception, callback);
//                }
//            }
//        });
//
//    }


    public static void createBasketFromOrder(Activity activity, final RecentOrder recentOrder, final AllStoreMenuCallBack callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        StoreService.getStoreInformation(recentOrder.getVendorextref(), new StoreDetailCallback() {
                    @Override
                    public void onStoreDetailCallback(final Store store, Exception exception) {
                        if (store != null) {
                            StoreService.getStoreMenu(callbackWeakReference.get(), store.getRestaurantId(), new StoreMenuCallback() {
                                @Override
                                public void onStoreMenuCallback(final HashMap<Integer, StoreMenuProduct> storeProducts, Exception exception) {
                                    if (storeProducts != null) {
                                        OloBasketService.createFromOrder(recentOrder.getId(), recentOrder.getReference(), new OloBasketServiceCallback() {
                                            @Override
                                            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                                                if (oloBasket != null) {
                                                    setBasket(oloBasket);
//                                            DataManager manager = DataManager.getInstance();
//                                            manager.setCurrentSelectedStore(store);
//                                            manager.setStoreMenu(storeProducts);

                                                    // AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "order_again", recentOrder.getReference());
                                                }
//                                        notifyBasketCreation(callbackWeakReference, error, callback);
//                                        //put current selected store in persistent file
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(store);
                                                //SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, json);

                                                ProductService.loadAllFamily(callbackWeakReference, store, storeProducts, callback);

                                            }
                                        });

                                    } else {
                                        notifyBasketCreationError(callbackWeakReference, new Exception("Basket creation failed. Please try again!"), callback);

                                    }
                                }
                            });
                        } else {
                            notifyBasketCreationError(callbackWeakReference, new Exception("Basket creation failed. Please try again!"), callback);
                        }
                    }
                });

    }

    private static void notifyBasketCreationError(WeakReference<Activity> callbackWeakReference, Exception exception, final AllStoreMenuCallBack callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onAllStoreMenuErrorCallback(exception);
        }
    }

    /**
     * TODO: create basket from favorites
     */
//    public static void createBasketFromFavorite(Activity activity, final FavoriteOrder recentOrder, final AllStoreMenuCallBack callback) {
//        OloBasketService.createFromFavorite(recentOrder.getId(), new OloBasketServiceCallback() {
//            @Override
//            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
//                if (oloBasket != null) {
//                    setBasket(oloBasket);
//                }
//
//                if (callback != null) {
//                    callback.onAllStoreMenuCallback(error);
//                }
//
//            }
//        });
//    }
}
