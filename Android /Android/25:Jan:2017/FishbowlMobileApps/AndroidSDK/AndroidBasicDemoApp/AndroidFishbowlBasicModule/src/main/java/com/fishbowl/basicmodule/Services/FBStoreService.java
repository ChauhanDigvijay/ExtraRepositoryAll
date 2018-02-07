package com.fishbowl.basicmodule.Services;

import android.location.Location;
import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Models.FBStoresSearchItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StoreHourList;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by digvijay(dj)
 */
public class FBStoreService {
    public static FBStoreService instance;
    public List<StoreHourList> allStoreHourListFromServer = new ArrayList<StoreHourList>();
    public Map<Integer, FBStoresItem> mapIdToStore = new HashMap<Integer, FBStoresItem>();//storesMap
    public Map<String, Integer> mapNumToId = new HashMap<String, Integer>();//storesMapforId
    public List<FBStoresItem> allStoreFromServer;
    public List<FBStoresItem> allStoreFromServerAfterSearch;
    public List<FBStoresSearchItem> allStoreFromServerAfterSearch1;
    public List<FBStoresItem> allStoresAfterSort;
    public List<FBStoresItem> allStoreFromServerAfterSearch2;
    private FBSdk fbSdk;
    // public boolean isGetAllStoreDownload;

    public static FBStoreService sharedInstance() {
        if (instance == null) {
            instance = new FBStoreService();
        }
        return instance;
    }

    public void init(FBSdk _clpsdk) {
        fbSdk = _clpsdk;
    }

    public void getAllStore(final FBAllStoreCallback callback) {


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.OnAllStoreCallback(null, FBUtility.getNetworkError());
        } else {
            final FBSdkData FBSdkData = fbSdk.getFBSdkData();
            FBService.getInstance().get(FBConstant.StoreApi, null, getHeader(), new FBServiceCallback() {

                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                    try {
                        if (error == null && response != null) {
                            if (!response.has("stores"))
                                return;

                            JSONArray getArrayStores = response.getJSONArray("stores");
                            if (getArrayStores != null) {

                                for (int i = 0; i < getArrayStores.length(); i++) {
                                    JSONObject myStoresObj = getArrayStores.getJSONObject(i);
                                    FBStoresItem getStoresObj = new FBStoresItem(myStoresObj);
                                    mapIdToStore.put(getStoresObj.getStoreID(), getStoresObj);
                                    mapNumToId.put(getStoresObj.getStoreNumber(), getStoresObj.getStoreID());
                                    if (!myStoresObj.has("storeHourList"))
                                        System.out.println("Null Store Hours");
                                    else {

                                        JSONArray getArrayStoreHourList = myStoresObj.getJSONArray("storeHourList");
                                        if (getArrayStoreHourList != null) {
                                            for (int j = 0; j < getArrayStoreHourList.length(); j++) {
                                                JSONObject myStoreHourListObj = getArrayStoreHourList.getJSONObject(j);
                                                StoreHourList getStoreHourListObj = new StoreHourList(myStoreHourListObj);
                                                allStoreHourListFromServer.add(getStoreHourListObj);
                                                //        Log.d("Saved Stores", String.valueOf(allStoreHourListFromServer.add(getStoreHourListObj)));

                                            }


                                        }
                                    }


                                }
                                allStoreFromServer = new ArrayList<FBStoresItem>(mapIdToStore.values());
                                callback.OnAllStoreCallback(response, null);
                            }
                        } else {

                            callback.OnAllStoreCallback(null, error);
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }

            });
        }
    }


    public void getSearchAllStore1(final JSONObject store, final String query, final FBAllSearchStorejsonCallback callback) {
        final FBSdkData FBSdkData = fbSdk.getFBSdkData();


        String json = store.toString();
        FBService.getInstance().post(FBConstant.StoreSearchApi, json, getHeaderforsearchstoreonly(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    callback.OnAllSearchStorejsonCallback(response, error);
                } else {
                    callback.OnAllSearchStorejsonCallback(null, error);
                }
            }

        });

    }


    //dont used this storesearch
    public void getSearchAllStore(final JSONObject store, final String query, final String count, final FBAllSearchStoreCallback callback) {

        try {


            if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
                callback.OnAllSearchStoreCallback(null, FBUtility.getNetworkError());
            } else {
                String json = store.toString();
                FBService.getInstance().post(FBConstant.StoreSearchApi, json, getHeaderforsearchstoreonly(), new FBServiceCallback() {


                    @Override
                    public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                        try {
                            if (error == null && response != null) {

                                {
                                    if (response != null) {
                                        allStoreFromServerAfterSearch1 = new ArrayList<FBStoresSearchItem>();

                                        JSONArray getArrayStoreHourList = response.getJSONArray("storeList");
                                        if (getArrayStoreHourList != null) {
                                            for (int j = 0; j < getArrayStoreHourList.length(); j++) {
                                                JSONObject myStoreHourListObj = getArrayStoreHourList.getJSONObject(j);
                                                FBStoresSearchItem getStoresObj = new FBStoresSearchItem(myStoreHourListObj);
                                                allStoreFromServerAfterSearch1.add(getStoresObj);

                                            }


                                        }

                                        callback.OnAllSearchStoreCallback(allStoreFromServerAfterSearch1, error);

                                    } else {
                                        Log.d("allstore error", "Download error");

                                    }
                                }
                            } else {
                                callback.OnAllSearchStoreCallback(null, error);
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void getStoreDetails(final String query, final FBStoreDetailCallback callback) {


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.OnFBStoreDetailCallback(null, FBUtility.getNetworkError());
        } else {
            if (StringUtilities.isValidString(query)) {
                final FBSdkData FBSdkData = fbSdk.getFBSdkData();

                String url = "/mobile/stores/getStoreDetails" + "/" + query;

                FBService.getInstance().get(url, null, getHeaderforsearchstore(), new FBServiceCallback() {

                    public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                        try {

                            if (response != null) {
                                callback.OnFBStoreDetailCallback(response, error);
                            } else {
                                callback.OnFBStoreDetailCallback(null, error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                });

            }
        }
    }


    //dont used this service
    public void getSearchAllStore(final JSONObject store, final String query, final FBAllNewSearchStoreCallback callback) {

        try {


            if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
                callback.OnAllSearchStoreCallback(null, FBUtility.getNetworkError());
            } else {
                String json = store.toString();
                FBService.getInstance().post(FBConstant.StoreSearchApi, json, getHeaderforsearchstoreonly(), new FBServiceCallback() {


                    @Override
                    public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                        try {
                            if (error == null && response != null) {

                                {
                                    if (response != null) {
                                        allStoreFromServerAfterSearch2 = new ArrayList<FBStoresItem>();

                                        JSONArray getArrayStoreHourList = response.getJSONArray("storeList");
                                        if (getArrayStoreHourList != null) {
                                            for (int j = 0; j < getArrayStoreHourList.length(); j++) {
                                                JSONObject myStoreHourListObj = getArrayStoreHourList.getJSONObject(j);
                                                FBStoresItem getStoresObj = new FBStoresItem(myStoreHourListObj);
                                                allStoreFromServerAfterSearch2.add(getStoresObj);

                                            }


                                        }

                                        callback.OnAllSearchStoreCallback(allStoreFromServerAfterSearch2, error);

                                    } else {
                                        Log.d("allstore error", "Download error");

                                    }
                                }
                            } else {
                                callback.OnAllSearchStoreCallback(null, error);
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void getNearestBeaconStores(Location currentLocation) {
        Log.d("currentLocation : ", currentLocation.getLatitude() + ","
                + currentLocation.getLongitude());

        ArrayList<FBStoresItem> storesDistance = new ArrayList<FBStoresItem>();
        if (allStoreFromServer == null)
            return;

        int i = 0;

        for (FBStoresItem store : allStoreFromServer) {

            if (store == null || store.latitude == null
                    || store.latitude.isEmpty() || store.longitude.isEmpty())
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

        if (noOfStores > FBMobileSettingService.sharedInstance().mobileSettings.MAX_STORE_COUNT_ANDROID)
            noOfStores = FBMobileSettingService.sharedInstance().mobileSettings.MAX_STORE_COUNT_ANDROID;


        allStoresAfterSort = new ArrayList<FBStoresItem>(storesDistance.subList(0, noOfStores));
        //clpSdkData.setAllStoresList(curStores);
    }

    HashMap<String, String> getHeaderwithsecurityforerror() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
        header.put("client_id", "201969E1BFD242E189FE7B6297B1B5A5");
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("tenantName", "fishbowl");
        return header;
    }

    HashMap<String, String> getHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String, String> getHeaderwithsecurity() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", FBConstant.client_Application);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("client_id", FBConstant.client_id);
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("tenantName", FBConstant.client_tenantName);
        return header;
    }

    HashMap<String, String> getHeaderforsearchstore() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobile");
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));

        return header;
    }

    HashMap<String, String> getHeaderforsearchstoreonly() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));

        return header;
    }

    //Interface for
    public interface FBAllStoreCallback {
        public void OnAllStoreCallback(JSONObject response, Exception error);
    }


    //Interface for
    public interface FBAllSearchStoreCallback {
        public void OnAllSearchStoreCallback(List<FBStoresSearchItem> response, Exception error);
    }


    //Interface for Detail store
    public interface FBStoreDetailCallback {
        public void OnFBStoreDetailCallback(JSONObject response, Exception error);
    }


    public interface FBAllSearchStorejsonCallback {
        public void OnAllSearchStorejsonCallback(JSONObject response, Exception error);
    }

    //Interface for demo app dont used it
    public interface FBAllNewSearchStoreCallback {
        public void OnAllSearchStoreCallback(List<FBStoresItem> response, Exception error);
    }

    private class StoreDistanceComparator implements Comparator<FBStoresItem> {
        @Override
        public int compare(FBStoresItem store1, FBStoresItem store2) {
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
}
