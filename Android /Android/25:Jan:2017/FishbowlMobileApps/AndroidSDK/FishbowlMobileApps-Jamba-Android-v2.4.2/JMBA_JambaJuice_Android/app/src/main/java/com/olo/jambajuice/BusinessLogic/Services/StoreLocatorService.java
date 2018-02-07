package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;
import android.location.Location;

import com.android.volley.VolleyError;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.LocationSearchCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.wearehathway.apps.olo.Models.OloRestaurant;
import com.wearehathway.apps.olo.Services.OloRestaurantService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoStoreService;
import com.wearehathway.apps.spendgo.Models.SpendGoStore;
import com.wearehathway.apps.spendgo.Services.SpendGoStoreService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static com.olo.jambajuice.Utils.Constants.DefaultRadiusInMiles;
import static com.olo.jambajuice.Utils.Constants.MaxStores;

/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class StoreLocatorService {
//    public static void findStoresNearLocation(final double latitude, final double longitude, final StoreServiceCallback callback, final String locationName) {
//        final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);
//        OloRestaurantService.getAllRestaurantsNear(latitude, longitude, DefaultRadiusInMiles, MaxStores, new OloRestaurantServiceCallback() {
//            @Override
//            public void onRestaurantServiceCallback(final OloRestaurant[] oloRestaurants, Exception exception) {
//                final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);
//                if (exception != null) {
//                    notifyStoreLocator(callbackWeakReference, null, exception);
//                } else {
//                    LocationService.searchZipForLocation(latitude, longitude, new LocationSearchCallback() {
//                        @Override
//                        public void onSearchLocationCallback(JSONObject response) {
//                            final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);
//                            if (response == null) {
//                                notifyStoreLocator(callbackWeakReference, null, new Exception("Cannot find location."));
//                            } else {
//                                if (isUSLocation(response)) {
//                                    String zip = getZipCode(response);
//                                    if (!zip.equals("")) {
//
//                                        SpendGoStoreService.findNearestStores(zip, DefaultRadiusInMiles, new ISpendGoStoreService() {
//                                            @Override
//                                            public void onSpendGoStoreServiceCallback(ArrayList<SpendGoStore> spendGoStores, Exception exception) {
//                                                final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);
//                                                parseRestaurantResponseAndNotify(latitude, longitude, spendGoStores, oloRestaurants, exception, callbackWeakReference, locationName);
//                                            }
//                                        });
//                                    } else {
//                                        notifyStoreLocator(callbackWeakReference, null, new Exception("Cannot find location."));
//                                    }
//                                } else {
//                                    VolleyError error = Utils.getCustomServerError(Constants.ERROR_US_LOCATIONS, "Can only search for US locations.".getBytes());
//                                    notifyStoreLocator(callbackWeakReference, null, error);
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }

    public static void findStoresNearLocation(final double latitude, final double longitude, final StoreServiceCallback callback, final String locationName) {
        final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);
        OloRestaurantService.getAllRestaurantsNear(latitude, longitude, DefaultRadiusInMiles, MaxStores, new OloRestaurantServiceCallback() {
            @Override
            public void onRestaurantServiceCallback(final OloRestaurant[] oloRestaurants, Exception exception) {
                final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);
                if (exception != null) {
                    notifyStoreLocator(callbackWeakReference, null, exception);
                } else {
                    parseRestaurantResponseAndNotify(latitude, longitude, oloRestaurants, exception, callbackWeakReference, locationName);
                }
            }
        });
    }

    public static void findStoresByLocationName(final String searchString, final StoreServiceCallback callback) {
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.SEARCH.value, "store_search", searchString);
        final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);
        LocationService.searchLocationForString(searchString, new LocationSearchCallback() {
            @Override
            public void onSearchLocationCallback(JSONObject address) {
                final WeakReference<StoreServiceCallback> callbackWeakReference = new WeakReference<StoreServiceCallback>(callback);

                if (address == null) {
                    notifyStoreLocator(callbackWeakReference, null, new Exception("Cannot find location."));
                } else {
                    try {
                        if (isUSLocation(address)) {
                            JSONObject geometry = (JSONObject) address.get("geometry");
                            JSONObject location = (JSONObject) geometry.get("location");
                            double lat = location.getDouble("lat");
                            double lng = location.getDouble("lng");
                            findStoresNearLocation(lat, lng, callbackWeakReference.get(), searchString);
                        } else {
                            VolleyError error = Utils.getCustomServerError(Constants.ERROR_US_LOCATIONS, "Can only search for US locations.".getBytes());
                            notifyStoreLocator(callbackWeakReference, null, error);
                        }
                    } catch (JSONException e) {
                        notifyStoreLocator(callbackWeakReference, null, new Exception("Cannot find location."));
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static boolean isUSLocation(JSONObject address) {
        JSONArray address_components = null;
        try {
            address_components = (JSONArray) address.get("address_components");
            for (int i = 0; i < address_components.length(); i++) {
                String shortName = (String) ((JSONObject) address_components.get(i)).get("short_name");
                String longNmae = (String) ((JSONObject) address_components.get(i)).get("long_name");
                ;
                if (shortName.equals("US") || longNmae.contains("United States")) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getZipCode(JSONObject address) {
        String zipCode = "";
        try {
            JSONArray address_components = (JSONArray) address.get("address_components");
            for (int i = 0; i < address_components.length(); i++) {
                JSONObject object = (JSONObject) address_components.get(i);
                JSONArray types = (JSONArray) object.get("types");
                for (int j = 0; j < types.length(); j++) {
                    String type = (String) types.get(j);
                    if (type.equals("postal_code")) {
                        String postalCode = (String) ((JSONObject) address_components.get(i)).get("short_name");
                        return postalCode;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipCode;
    }

    private static void parseRestaurantResponseAndNotify(double latitude, double longitude, OloRestaurant[] oloRestaurants, Exception exception, WeakReference<StoreServiceCallback> callbackWeakReference, String locationName) {
        StoreServiceCallback cb = callbackWeakReference.get();
        HashMap<String, OloRestaurant> oloRestaurantHashMap = new HashMap<>();
        ArrayList<Store> stores = new ArrayList<Store>();
        ArrayList<String> notAvailableStores = new ArrayList<>();
        if (oloRestaurants != null) {
            int size = oloRestaurants.length;
            for (int i = 0; i < size; i++) {
                OloRestaurant restaurant = oloRestaurants[i];
                if (restaurant != null) {
                    // if (restaurant.isAvailable()) {
                    oloRestaurantHashMap.put(restaurant.getStoreCode(), restaurant);
//                    } else {
//                        notAvailableStores.add(restaurant.getStoreCode());
//                    }
                }
            }
        }
        if (oloRestaurantHashMap != null) {
            int size = oloRestaurants.length;
            for (int i = 0; i < size; i++) {
                // if (oloRestaurants[i].isAvailable()) {
                Store store = new Store(oloRestaurantHashMap.get(oloRestaurants[i].getStoreCode()));
                if(DataManager.getInstance().isDebug) {
                    store = Utils.setDemoStoreName(store);
                }

                stores.add(store);
                // }
            }
        }
        //This is for Testing purpose only. Set ShowDemoStore as False during production release
        if (DataManager.getInstance().showDemoStore) {
            Store demoStore = new Store();

            demoStore.setDemoStore();

            stores.add(demoStore);
        }

        //This is for Testing purpose only. Set ShowDemoLabStore as False during production release
        if (DataManager.getInstance().showDemoLabStore) {
            Store demoLabStore = new Store();

            demoLabStore.setDemoLabStore();

            stores.add(demoLabStore);
        }

        for (Store store : stores) {
            if (store != null) {
                if (store.getStoreCode() != null) {
                    OloRestaurant restaurant = oloRestaurantHashMap.get(store.getStoreCode());
                    if (restaurant != null) {
                        store.populateFieldsWithOloRestaurant(restaurant);
                    }
                }
                store.setDistanceToUser(returnDistance(latitude, longitude, store));
            }
        }
        Collections.sort(stores, new Comparator<Store>() {
            @Override
            public int compare(Store lhs, Store rhs) {
                double distance1 = lhs.getDistanceToUser();
                double distance2 = rhs.getDistanceToUser();
                if (distance1 < distance2) {
                    return -1;
                }
                if (distance1 > distance2) {
                    return 1;
                }
                return 0;
            }
        });

        if (locationName != null) {

            AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.SEARCH.value, stores.size() > 0 ? "store_search_no_results" : "store_search_results", locationName + "(" + stores.size() + ")");
        }
        notifyStoreLocator(callbackWeakReference, stores, exception);
    }


//    private static void parseRestaurantResponseAndNotify(double latitude, double longitude, ArrayList<SpendGoStore> spendGoStores, OloRestaurant[] oloRestaurants, Exception exception, WeakReference<StoreServiceCallback> callbackWeakReference, String locationName) {
//        StoreServiceCallback cb = callbackWeakReference.get();
//        HashMap<String, OloRestaurant> oloRestaurantHashMap = new HashMap<>();
//        ArrayList<Store> stores = new ArrayList<Store>();
//        ArrayList<String> notAvailableStores = new ArrayList<>();
//        if (oloRestaurants != null) {
//            int size = oloRestaurants.length;
//            for (int i = 0; i < size; i++) {
//                OloRestaurant restaurant = oloRestaurants[i];
//                if (restaurant.isAvailable()) {
//                    oloRestaurantHashMap.put(restaurant.getStoreCode(), restaurant);
//                } else {
//                    notAvailableStores.add(restaurant.getStoreCode());
//                }
//            }
//        }
//        if (spendGoStores != null) {
//            int size = spendGoStores.size();
//            for (int i = 0; i < size; i++) {
//                SpendGoStore spgStore = spendGoStores.get(i);
//                //Store with isavailable false should not be shown.
//                if (!notAvailableStores.contains(spgStore.getCode())) {
//                    Store store = new Store(spgStore);
//                    stores.add(store);
//                }
//            }
//            //This is for Testing purpose only. Set ShowDemoStore as False during production release
////            if (DataManager.getInstance().showDemoStore) {
////                Store demoStore = new Store();
////
////                demoStore.setDemoStore();
////
////                stores.add(demoStore);
////            }
//
//            for (Store store : stores) {
//                if (store != null) {
//                    if (store.getStoreCode() != null) {
//                        OloRestaurant restaurant = oloRestaurantHashMap.get(store.getStoreCode());
//                        if (restaurant != null) {
//                            store.populateFieldsWithOloRestaurant(restaurant);
//                        }
//                    }
//                    store.setDistanceToUser(returnDistance(latitude, longitude, store));
//                }
//            }
//        }
//        Collections.sort(stores, new Comparator<Store>() {
//            @Override
//            public int compare(Store lhs, Store rhs) {
//                double distance1 = lhs.getDistanceToUser();
//                double distance2 = rhs.getDistanceToUser();
//                if (distance1 < distance2) {
//                    return -1;
//                }
//                if (distance1 > distance2) {
//                    return 1;
//                }
//                return 0;
//            }
//        });
//        if (locationName != null) {
//            AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.SEARCH.value, stores.size() > 0 ? "store_search_no_results" : "store_search_results", locationName + "(" + stores.size() + ")");
//        }
//        notifyStoreLocator(callbackWeakReference, stores, exception);
//    }

    private static void notifyStoreLocator(WeakReference<StoreServiceCallback> callbackWeakReference, ArrayList<Store> stores, Exception exception) {
        StoreServiceCallback cb = callbackWeakReference.get();
        try {
            if (cb != null) {
                if (cb instanceof Activity) {
                    if (!((Activity) cb).isFinishing()) {
                        cb.onStoreServiceCallback(stores, exception);
                    }
                } else {
                    cb.onStoreServiceCallback(stores, exception);
                }
            }
        } catch (Exception e) {
            if (cb != null) {
                cb.onStoreServiceCallback(stores, exception);
            }
            e.printStackTrace();
        }
    }


    //Helper functions
    private static double returnDistance(double latitude, double longitude, Store store) {
        Location currentLocation = new Location("currentLocation");
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);
        Location storeLoc = new Location("store");
        storeLoc.setLatitude(store.getLatitude());
        storeLoc.setLongitude(store.getLongitude());
        double distance = currentLocation.distanceTo(storeLoc);
        return round(convertMetersToMiles(distance), 2);
    }

    private static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static double convertMetersToMiles(double meters) {
        return (meters / 1609.344);
    }
}