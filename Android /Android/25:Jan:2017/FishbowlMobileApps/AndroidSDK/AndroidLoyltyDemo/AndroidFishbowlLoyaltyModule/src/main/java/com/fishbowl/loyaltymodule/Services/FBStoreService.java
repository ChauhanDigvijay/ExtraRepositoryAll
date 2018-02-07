//package com.fishbowl.loyaltymodule.Services;
//
//import android.location.Location;
//import android.util.Log;
//
//import com.Preferences.FBPreferences;
//import com.fishbowl.loyaltymodule.Utils.StringUtilities;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////  Created by HARSH on 17/11/15.
////  Copyright © 2015 HARSH. All rights reserved.
//public class FBStoreService {
//    public  List<StoreHourList> allStoreHourListFromServer = new ArrayList<StoreHourList>();
//    private FBSdk fbSdk;
//    public static FBStoreService instance ;
//    public Map<Integer, FBStoresItem> mapIdToStore = new HashMap<Integer, FBStoresItem>();//storesMap
//    public Map<String, Integer> mapNumToId = new HashMap<String, Integer>();//storesMapforId
//
//    public List<FBStoresItem> allStoreFromServer;
//    public List<FBStoresItem> allStoreFromServerAfterSearch;
//    public List<FBStoresItem> allStoresAfterSort;
//    public boolean isGetAllStoreDownload;
//
//    public static FBStoreService sharedInstance(){
//        if(instance==null){
//            instance=new FBStoreService();
//        }
//        return instance;
//    }
//
//    public void init(FBSdk _clpsdk){
//        fbSdk =_clpsdk;
//    }
//
//    public  void getAllStore(final FBAllStoreCallback callback){
//        final FBSdkData FBSdkData = fbSdk.getClpSdkData();
//
//        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
//            return;
//        }
//        isGetAllStoreDownload=false;
//        FBService.getInstance().get(FBConstant.StoreApi, null, getHeader(), new FBServiceCallback(){
//
//            public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
//
//                try {
//                    if (error == null && response != null) {
//                        if (!response.has("stores"))
//                            return;
//
//                        JSONArray getArrayStores = response.getJSONArray("stores");
//                        if (getArrayStores != null) {
//
//                            for (int i = 0; i < getArrayStores.length(); i++) {
//                                JSONObject myStoresObj = getArrayStores.getJSONObject(i);
//                                FBStoresItem getStoresObj = new FBStoresItem(myStoresObj);
//                                mapIdToStore.put(getStoresObj.getStoreID(), getStoresObj);
//                                mapNumToId.put(getStoresObj.getStoreNumber(), getStoresObj.getStoreID());
//                                //  mapIdToStore.put(getStoresObj.getStoreID(),getStoresObj.getStoreID());
//
//                                if (!myStoresObj.has("storeHourList"))
//                                    System.out.println("Null Store Hours");
//                                else {
//                                    //  System.out.println("Null Store Hours");
//                                    JSONArray getArrayStoreHourList = myStoresObj.getJSONArray("storeHourList");
//                                    if (getArrayStoreHourList != null) {
//                                        for (int j = 0; j < getArrayStoreHourList.length(); j++) {
//                                            JSONObject myStoreHourListObj = getArrayStoreHourList.getJSONObject(j);
//                                            StoreHourList getStoreHourListObj = new StoreHourList(myStoreHourListObj);
//                                            allStoreHourListFromServer.add(getStoreHourListObj);
//                                            //        Log.d("Saved Stores", String.valueOf(allStoreHourListFromServer.add(getStoreHourListObj)));
//
//                                        }
//
//
//                                    }
//                                }
//
//
//                            }
//                            allStoreFromServer = new ArrayList<FBStoresItem>(mapIdToStore.values());
//                            //FBSdkData.setAllCLPBeaconStoreList(new ArrayList<FBStoresItem>(storesMap.values()));
//                            //  getNearestBeaconStores(fbSdk.getmCurrentLocation());// always
//                            callback.OnAllStoreCallback(response, null);
//                            isGetAllStoreDownload = true;
//
//                        } else {
//                            Log.d("allstore error", "Download error");
//                            //  callback.OnAllStoreCallback(null,error.getLocalizedMessage());
//                        }
//                    }  else if (StringUtilities.isValidString(errorMessage)) {
//
//                        if (errorMessage.equalsIgnoreCase("Invalid access token")) {
//                            FBTokenService.sharedInstance(fbSdk).getTokenAllStore();
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    //	callback.OnAllStoreCallback(response,error.getLocalizedMessage());
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            });
//
//    }
//
//
//
//    public  void getSearchAllStore1(final JSONObject store,final String query,final FBAllSearchStoreCallback callback){
//        final FBSdkData FBSdkData = fbSdk.getClpSdkData();
//
//        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
//            return;
//        }
//        if(FBSdkData.currCustomer.getCustomerID()!=0 && FBSdkData.currCustomer.getCustomerID()>0) {
//            try {
//                store.put("query", query);
//                store.put("radius", "1000");
//                store.put("count", "10");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            String json = store.toString();
//            FBService.getInstance().post(FBConstant.StoreSearchApi, json, getHeaderforsearchstore(), new FBServiceCallback() {
//
//                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
//                    try {
//                        if (error == null && response != null) {
//
//                            {
//                                if (!response.has("stores"))
//                                    return;
//
//                                JSONArray getArrayStores = response.getJSONArray("stores");
//                                if (getArrayStores != null) {
//                                    allStoreFromServerAfterSearch=new ArrayList<FBStoresItem>();
//                                    for (int i = 0; i < getArrayStores.length(); i++) {
//                                        JSONObject myStoresObj = getArrayStores.getJSONObject(i);
//                                        FBStoresItem getStoresObj = new FBStoresItem(myStoresObj);
//                                        allStoreFromServerAfterSearch.add(getStoresObj);
//
//                                    }
//
//
//                                    callback.OnAllSearchStoreCallback(allStoreFromServerAfterSearch,null);
//
//                                } else {
//                                    Log.d("allstore error", "Download error");
//
//                                }
//                            }
//
//                        } else if (errorMessage.equalsIgnoreCase("Invalid access token")) {
//                            FBTokenService.sharedInstance(fbSdk).getTokenAllStore();
//                        } else {
//                            callback.OnAllSearchStoreCallback(null, errorMessage);
//                        }
//                    }
//                    catch (JSONException e) {
//
//                        e.printStackTrace();
//                    }
//                }
//
//            });
//
//        }}
//
//
//    //redeemedservices
//    public void getSearchAllStore(final JSONObject store,final String query,final FBAllSearchStoreCallback callback) {
//
//        try {
//             try {
//                        store.put("query", query);
//                        store.put("radius", "1000");
//                        store.put("count", "10");
//                    }
//
//                    catch (JSONException e)
//                    {
//                        e.printStackTrace();
//                    }
//
//
//                    String json = store.toString();
//
//                    String url= "store/searchStores";
//
//                    FBService.getInstance().post(FBConstant.StoreSearchApi, json,getHeaderforsearchstore(), new FBServiceCallback(){
//
//
//                        @Override
//                        public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
//                            try {
//                                if (error == null && response != null) {
//
//                                    {
//
//
//                                        if (response != null) {
//                                            allStoreFromServerAfterSearch = new ArrayList<FBStoresItem>();
//
//                                            JSONArray getArrayStoreHourList = response.getJSONArray("storeList");
//                                            if (getArrayStoreHourList != null) {
//                                                for (int j = 0; j < getArrayStoreHourList.length(); j++) {
//                                                    JSONObject myStoreHourListObj = getArrayStoreHourList.getJSONObject(j);
//                                                    FBStoresItem getStoresObj = new FBStoresItem(myStoreHourListObj);
//                                                    allStoreFromServerAfterSearch.add(getStoresObj);
//                                                    //        Log.d("Saved Stores", String.valueOf(allStoreHourListFromServer.add(getStoreHourListObj)));
//
//                                                }
//
//
//                                            }
//
//                                            callback.OnAllSearchStoreCallback(allStoreFromServerAfterSearch, null);
//
//                                        } else {
//                                            Log.d("allstore error", "Download error");
//
//                                        }
//                                    }
//
////                                } else if (errorMessage.equalsIgnoreCase("Invalid access token")) {
////                                    callback.OnAllSearchStoreCallback(null, errorMessage);
////                                } else {
////                                    callback.OnAllSearchStoreCallback(null, errorMessage);
////                                }
//                                }
//                            }
//                            catch (JSONException e) {
//
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//
//
//        }catch(Exception e){
//            e.printStackTrace();
//
//        }
//
//    }
//
//
//
//
//    public void getNearestBeaconStores(Location currentLocation) {
//        Log.d("currentLocation : ", currentLocation.getLatitude() + ","
//                + currentLocation.getLongitude());
//
//        ArrayList<FBStoresItem> storesDistance = new ArrayList<FBStoresItem>();
//        if (allStoreFromServer == null)
//            return;
//
//        int i = 0;
//
//        for (FBStoresItem store : allStoreFromServer) {
//
//            if (store == null || store.latitude == null
//                    || store.latitude.isEmpty() || store.longitude.isEmpty())
//                continue;
//
//            try {
//                Location storeLocation = new Location("");
//                storeLocation.setLatitude(Double.valueOf(store.latitude));
//                storeLocation.setLongitude(Double.valueOf(store.longitude));
//                store._distanceFromLocation = storeLocation
//                        .distanceTo(currentLocation);
//                storesDistance.add(i, store);
//                i++;
//            } catch (NumberFormatException nfex) {
//                nfex.printStackTrace();
//            }
//        }
//
//        Collections.sort(storesDistance, new StoreDistanceComparator());
//
//        int noOfStores = storesDistance.size();
//
//        if (noOfStores > FB_LY_MobileSettingService.sharedInstance().mobileSettings.MAX_STORE_COUNT_ANDROID)
//            noOfStores = FB_LY_MobileSettingService.sharedInstance().mobileSettings.MAX_STORE_COUNT_ANDROID;
//
//
//        allStoresAfterSort = new ArrayList<FBStoresItem>( storesDistance.subList(0, noOfStores));
//        //clpSdkData.setAllStoresList(curStores);
//    }
//
//    private class StoreDistanceComparator implements Comparator<FBStoresItem> {
//        @Override
//        public int compare(FBStoresItem store1, FBStoresItem store2) {
//            // stores without valid locations are treated as being farthest
//            if (store1._distanceFromLocation == 0.0)
//                return 1;
//            else if (store2._distanceFromLocation == 0.0)
//                return -1;
//
//            return (store1._distanceFromLocation > store2._distanceFromLocation ? 1
//                    : (store1._distanceFromLocation == store2._distanceFromLocation ? 0
//                    : -1));
//        }
//    }
//
//
//
//
//    HashMap<String,String> getHeaderwithsecurityforerror(){
//        HashMap<String,String> header=new HashMap<String, String>();
//        header.put("Content-Type","application/json");
//        header.put("Application","mobilesdk");
//        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
//        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
//        header.put("deviceId",FBUtility.getAndroidDeviceID(fbSdk.context));
//        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
//        //header.put("scope","READ");
//        header.put("tenantid","1173");
//        header.put("tenantName","fishbowl");
//        return header;
//    }
//
//
//
//    HashMap<String,String> getHeader(){
//        HashMap<String,String> header=new HashMap<String, String>();
//        //header.put("X-XSRF-TOKEN","1234");
//        //header.put("REDIS","true");
//        header.put("Content-Type","application/json");
//        header.put("Application","mobilesdk");
//        header.put("deviceId",FBUtility.getAndroidDeviceID(fbSdk.context));
//        //header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
//        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
//        //header.put("scope","READ");
//        header.put("tenantid","1173");
//        return header;
//    }
//
//
//
//    HashMap<String,String> getHeaderwithsecurity(){
//        HashMap<String,String> header=new HashMap<String, String>();
//        header.put("Content-Type","application/json");
//        header.put("Application", FBConstant.client_Application);
//        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
//        header.put("deviceId",FBUtility.getAndroidDeviceID(fbSdk.context));
//        header.put("deviceId",FBUtility.getAndroidDeviceID(fbSdk.context));
//        header.put("client_id", FBConstant.client_id);
//        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
//        //header.put("scope","READ");
//        header.put("tenantid", FBConstant.client_tenantid);
//        header.put("tenantName", FBConstant.client_tenantName);
//        return header;
//    }
//
//    HashMap<String,String> getHeaderforsearchstore(){
//        HashMap<String,String> header=new HashMap<String, String>();
//        header.put("Content-Type","application/json");
//        header.put("Application", FBConstant.client_Application);
//        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
//        header.put("client_id", FBConstant.client_id);
//       header.put("client_secret", FBConstant.client_secret);
//        header.put("tenantid", FBConstant.client_tenantid);
//        header.put("deviceId",FBUtility.getAndroidDeviceID(fbSdk.context));
//
//        return header;
//    }
//
//
//
//    //Interface for
//    public interface FBAllStoreCallback {
//        public void OnAllStoreCallback(JSONObject response, String error);
//    }
//
//
//    //Interface for
//    public interface FBAllSearchStoreCallback {
//        public void OnAllSearchStoreCallback(List<FBStoresItem> response, String error);
//    }
//
//}
