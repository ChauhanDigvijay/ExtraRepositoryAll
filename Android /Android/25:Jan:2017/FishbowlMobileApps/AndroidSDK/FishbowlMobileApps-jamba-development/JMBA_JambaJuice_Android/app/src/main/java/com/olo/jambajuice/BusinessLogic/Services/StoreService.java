package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.google.gson.Gson;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.PreferredStoreCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductDetailCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StartOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreCalendarCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreDetailCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreMenuCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProduct;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifier;
import com.olo.jambajuice.BusinessLogic.Models.StoreTiming;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.wearehathway.apps.olo.Interfaces.OloMenuServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloProductModifierCallback;
import com.wearehathway.apps.olo.Interfaces.OloRestaurantCalendarCallback;
import com.wearehathway.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.wearehathway.apps.olo.Models.OloCalendar;
import com.wearehathway.apps.olo.Models.OloCategory;
import com.wearehathway.apps.olo.Models.OloMenu;
import com.wearehathway.apps.olo.Models.OloModifier;
import com.wearehathway.apps.olo.Models.OloProduct;
import com.wearehathway.apps.olo.Models.OloRestaurant;
import com.wearehathway.apps.olo.Models.OloRestaurantCalenders;
import com.wearehathway.apps.olo.Services.OloMenuService;
import com.wearehathway.apps.olo.Services.OloRestaurantService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nauman Afzaal on 20/05/15.
 */
public class StoreService {

    public static void startNewOrderFromPreferredStore(final Activity context, final StartOrderCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(context);
        final User user = UserService.getUser();
        if (user.isPreferredStoreSupportsOrderAHead()) {
            StoreService.getStoreInformation(user.getFavoriteStoreCode(), new StoreDetailCallback() {
                @Override
                public void onStoreDetailCallback(Store store, Exception exception) {
                    if (exception == null) {
                        store.setSpendGoStoreId(user.getSpendGoFavoriteStoreId());// May not use this id.
                        startNewOrder(callbackWeakReference.get(), store, callback);
                    } else {
                        notifyStartOrderCallback(exception, callbackWeakReference, callback);
                    }
                }
            });
        } else {
            callback.onStartOrderCallback(new Exception(user.getStoreName() + " does not support order ahead."));
        }
    }

    public static void startNewOrder(final Activity context, final Store store, final StartOrderCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(context);
        getStoreMenu(context, store.getRestaurantId(), new StoreMenuCallback()
//                getStoreMenu(context, store.getRestaurantId(), new StoreMenuCallback()
        {
            @Override
            public void onStoreMenuCallback(final HashMap<Integer, StoreMenuProduct> storeProducts, Exception exception) {
                if (storeProducts != null) {
                    BasketService.createBasket(callbackWeakReference.get(), store.getRestaurantId(), new BasketServiceCallback() {
                        @Override
                        public void onBasketServiceCallback(Basket basket, Exception error) {
                            if (basket != null) {
                                //Store menu as we have successfully created basket.
                                DataManager manager = DataManager.getInstance();
                                manager.setCurrentSelectedStore(store);

                                //put current selected store in persistent file
                                Gson gson = new Gson();
                                String json = gson.toJson(store);
                                SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, json);

                                String lastSelectedStore = SharedPreferenceHandler.getString(SharedPreferenceHandler.LastSelectedStore, "");
                                Store obj = gson.fromJson(json, Store.class);


                                manager.setStoreMenu(storeProducts);
                                JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(store.getId()), store.getName(), FBEventSettings.START_NEW_ORDER);


                                AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.STORES.value, "start_new_order", store.getName());
                            }
                            notifyStartOrderCallback(error, callbackWeakReference, callback);
                        }
                    });
                } else {
                    notifyStartOrderCallback(exception, callbackWeakReference, callback);
                }
            }
        });
    }

    private static void notifyStartOrderCallback(Exception exception, WeakReference<Activity> callbackWeakReference, StartOrderCallback callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onStartOrderCallback(exception);
        }
    }

    private static void notifyPreferredStoreCallback(Exception exception, WeakReference<Activity> callbackWeakReference, PreferredStoreCallBack callback, Store store) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onPreferredStoreCallback(exception, store);
        }
    }

    private static void notifyPreferredStoreErrorCallback(Exception exception, WeakReference<Activity> callbackWeakReference, PreferredStoreCallBack callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onPreferredStoreErrorCallback(exception);
        }
    }

    public static void getStoreCalendarForWeek(Activity context, int storeId, final StoreCalendarCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(context);
        Calendar calendar = Calendar.getInstance();
        Date from = calendar.getTime();
        int advanceOrderDays = 7;
        if(DataManager.getInstance().getCurrentSelectedStore() != null) {
            advanceOrderDays = DataManager.getInstance().getCurrentSelectedStore().getAdvanceorderdays();
        }
        calendar.add(Calendar.DAY_OF_MONTH, advanceOrderDays);// Get time for next advance days as well.
        Date to = calendar.getTime();
        fetchRetaurantCalendarFromOlo(callbackWeakReference, storeId, from, to, callback);
    }

    public static void getStoreMenu(final Activity context, final int storeId, final StoreMenuCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(context);
        OloMenuService.getRestaurantMenu(storeId, new OloMenuServiceCallback() {
            @Override
            public void onRestaurantMenuCallback(OloMenu menu, Exception exception) {
                HashMap<Integer, StoreMenuProduct> storeProducts = null;
                if (exception == null) {
                    storeProducts = new HashMap<Integer, StoreMenuProduct>();
                    OloCategory[] categories = menu.getCategories();
                    if (categories != null) {
                        for (OloCategory category : categories) {
                            List<OloProduct> oloProducts = category.getProducts();
                            for (OloProduct product : oloProducts) {
                                //ChainProductId is globally unique for each product.
                                storeProducts.put(product.getChainproductid(), new StoreMenuProduct(product));
                            }
                        }
                    }
                    final HashMap<Integer, StoreMenuProduct> finalStoreProducts = storeProducts;
                    final OloCategory[] finalStoreCategories = menu.getCategories();
                    StoreService.getStoreCalendarForWeek(callbackWeakReference.get(), storeId, new StoreCalendarCallback() {
                        @Override
                        public void onStoreCalendarCallback(StoreTiming calendar, Exception ex) {
                            if (calendar != null) {
                                DataManager.getInstance().setSelectedStoreTiming(calendar);
                            }
                            notifyStoreMenuCallback(callbackWeakReference, finalStoreProducts, ex, callback);
                        }
                    });
                } else {
                    notifyStoreMenuCallback(callbackWeakReference, storeProducts, exception, callback);
                }
            }
        });
    }

    public static void getStoreInformation(String storeCode, final StoreDetailCallback callback) {
        OloRestaurantService.getRestaurantByRef(storeCode, new OloRestaurantServiceCallback() {
            @Override
            public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception) {
                Store store = null;
                if (restaurants != null && restaurants.length > 0) {
                    store = new Store(restaurants[0]);
                }
                callback.onStoreDetailCallback(store, exception);
            }
        });
    }

    //Fetch product modifier and cache locally
    public static void fetchProductModifiers(Activity activity, final StoreMenuProduct product, final ProductDetailCallback callback) {

        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        OloMenuService.getProductModifiers(product.getId(), new OloProductModifierCallback() {
            @Override
            public void onProductModifierCallback(OloModifier[] modifiers, Exception exception) {
                List<StoreMenuProductModifier> storeMenuProductModifiers = null;
                if (exception == null) {
                    storeMenuProductModifiers = new ArrayList<StoreMenuProductModifier>();
                    for (OloModifier modifier : modifiers) {
                        storeMenuProductModifiers.add(new StoreMenuProductModifier(modifier));
                    }
                }
                DataManager.getInstance().setProductModifier(product.getChainProductId(), storeMenuProductModifiers);
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callback.onProductDetailCallback(storeMenuProductModifiers, exception);
                }
            }
        });
    }

    private static void notifyStoreMenuCallback(WeakReference<Activity> callbackWeakReference, HashMap<Integer, StoreMenuProduct> storeProducts, Exception exception, final StoreMenuCallback callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onStoreMenuCallback(storeProducts, exception);
        }
    }

    private static void fetchRetaurantCalendarFromOlo(final WeakReference<Activity> callbackWeakReference, int storeId, Date from, Date to, final StoreCalendarCallback callback) {
        OloRestaurantService.getRestaurantCalendar(storeId, from, to, new OloRestaurantCalendarCallback() {
            @Override
            public void onCalendarCallback(OloRestaurantCalenders calendars, Exception exception) {
                StoreTiming storeTiming = null;
                if (calendars != null) {
                    boolean isFound = false;
                    ArrayList<OloCalendar> oloCalendars = calendars.getCalendar();
                    if (oloCalendars.size() > 0) {
                        for (OloCalendar calendar : oloCalendars) {
                            if (calendar.getType().equals("business")) {
                                storeTiming = new StoreTiming(calendar);
                                isFound = true;
                                break;
                            }
                        }
                    }
                    if (!isFound) {
                        exception = new Exception("Unable to get restaurant time");
                    }
                }
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callback.onStoreCalendarCallback(storeTiming, exception);
                }
            }
        });
    }

    public static void getPreferredStore(final Activity context, final PreferredStoreCallBack callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(context);
        final User user = UserService.getUser();
        if (user.isPreferredStoreSupportsOrderAHead()) {
            StoreService.getStoreInformation(user.getFavoriteStoreCode(), new StoreDetailCallback() {
                @Override
                public void onStoreDetailCallback(Store store, Exception exception) {
                    if (exception == null) {
                        store.setSpendGoStoreId(user.getSpendGoFavoriteStoreId());// May not use this id.
                        notifyPreferredStoreCallback(exception, callbackWeakReference, callback, store);
                    } else {
                        notifyPreferredStoreErrorCallback(exception, callbackWeakReference, callback);
                    }
                }
            });
        } else {
            callback.onPreferredStoreErrorCallback(new Exception(user.getStoreName() + " does not support order ahead."));
        }
    }
}
