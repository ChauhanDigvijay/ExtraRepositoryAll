package com.fishbowl.basicmodule.Services;

import android.location.Location;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceArrayCallback;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.FBStoreItem;
import com.fishbowl.basicmodule.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

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

    private FBSdk fbSdk;
    public static FBStoreService instance ;
    public Map<Integer, FBStoreItem> mapIdToStore = new HashMap<Integer, FBStoreItem>();//storesMap
    public Map<String, Integer> mapNumToId = new HashMap<String, Integer>();//storesMapforId

    public List<FBStoreItem> allStoreFromServer;
    public List<FBStoreItem> allStoreFromServerAfterSearch;
    public List<FBStoreItem> allStoresAfterSort;
    public boolean isGetAllStoreDownload;

    public static FBStoreService sharedInstance(){
        if(instance==null){
            instance=new FBStoreService();
        }
        return instance;
    }

    public void init(FBSdk _fbsdk){
        fbSdk =_fbsdk;
    }

    public  void getAllStore(final FBStoreCallback callback){
        final FBSdkData FBSdkData = fbSdk.getFBSdkData();

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        isGetAllStoreDownload=false;
        FBService.getInstance().get(FBConstant.StoreApi, null, getHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                try {
                    if(error==null&&response!=null){
                        if (!response.has("stores"))
                            return;

                        JSONArray getArrayStores = response.getJSONArray("stores");
                        if (getArrayStores != null) {

                            for (int i = 0; i < getArrayStores.length(); i++) {
                                JSONObject myStoresObj = getArrayStores.getJSONObject(i);
                                FBStoreItem getStoresObj = new FBStoreItem(myStoresObj);
                                mapIdToStore.put(getStoresObj.getStoreID(),getStoresObj);
                                mapNumToId.put(getStoresObj.getStoreNumber(), getStoresObj.getStoreID());
                                //  mapIdToStore.put(getStoresObj.getStoreID(),getStoresObj.getStoreID());
                            }

                            allStoreFromServer=new ArrayList<FBStoreItem>(mapIdToStore.values());
                            //FBSdkData.setAllCLPBeaconStoreList(new ArrayList<FBStoreItem>(storesMap.values()));
                            //  getNearestBeaconStores(fbSdk.getmCurrentLocation());// always
                            callback.OnFBStoreCallback(response,null);
                            isGetAllStoreDownload=true;

                        } else {
                            Log.d("allstore error", "Download error");
                            //  callback.OnFBStoreCallback(null,error.getLocalizedMessage());
                        }
                    }
                    else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
                    {
                        FBTokenService.sharedInstance(fbSdk).getTokenAllStore();
                    }

                }
                catch (JSONException e) {
                    //	callback.OnFBStoreCallback(response,error.getLocalizedMessage());
                    e.printStackTrace();
                }

            }

        });

    }



    public  void getSearchAllStore1(final JSONObject store,final String query,final FBSearchStoreCallback callback){
        final FBSdkData FBSdkData = fbSdk.getFBSdkData();

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        if(FBSdkData.currCustomer.getCustomerID()!=0 && FBSdkData.currCustomer.getCustomerID()>0) {
            try {
                store.put("query", query);
                store.put("radius", "1000");
                store.put("count", "10");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String json = store.toString();
            FBService.getInstance().post(FBConstant.StoreSearchApi, json, getHeaderforsearchstore(), new FBServiceCallback() {

                public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                    try {
                        if (error == null && response != null) {

                            {
                                if (!response.has("stores"))
                                    return;

                                JSONArray getArrayStores = response.getJSONArray("stores");
                                if (getArrayStores != null) {
                                    allStoreFromServerAfterSearch=new ArrayList<FBStoreItem>();
                                    for (int i = 0; i < getArrayStores.length(); i++) {
                                        JSONObject myStoresObj = getArrayStores.getJSONObject(i);
                                        FBStoreItem getStoresObj = new FBStoreItem(myStoresObj);
                                        allStoreFromServerAfterSearch.add(getStoresObj);

                                    }


                                    callback.OnFBSearchStoreCallback(allStoreFromServerAfterSearch,null);

                                } else {
                                    Log.d("allstore error", "Download error");

                                }
                            }

                        } else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
                        {
                            FBTokenService.sharedInstance(fbSdk).getTokenAllStore();
                        } else {
                            callback.OnFBSearchStoreCallback(null, errorMessage);
                        }
                    }
                    catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

            });

        }}


    //redeemedservices
    public void getSearchAllStore(final JSONObject store,final String query,final FBSearchStoreCallback callback) {


        try {

                    try {
                        store.put("query", query);
                        store.put("radius", "1000");
                        store.put("count", "10");
                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                    String json = store.toString();

                    String url= "store/searchStores";

                    FBService.getInstance().makeCustomArrayRequest(FBConstant.StoreSearchApi, json,getHeaderforsearchstore(), new FBServiceArrayCallback(){


                        @Override
                        public void onCLPServiceArrayCallback(JSONArray response, Exception error, String errorMessage) {
                            try {
                                if (error == null && response != null) {

                                    {



                                        if (response != null) {
                                            allStoreFromServerAfterSearch=new ArrayList<FBStoreItem>();
                                            for (int i = 0; i < response.length(); i++) {
                                                JSONObject myStoresObj = response.getJSONObject(i);
                                                FBStoreItem getStoresObj = new FBStoreItem(myStoresObj);
                                                allStoreFromServerAfterSearch.add(getStoresObj);

                                            }


                                            callback.OnFBSearchStoreCallback(allStoreFromServerAfterSearch,null);

                                        } else {
                                            Log.d("allstore error", "Download error");

                                        }
                                    }

                                } else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
                                {
                                    callback.OnFBSearchStoreCallback(null, errorMessage);
                                } else {
                                    callback.OnFBSearchStoreCallback(null, errorMessage);
                                }
                            }
                            catch (JSONException e) {

                                e.printStackTrace();
                            }

                        }
                    });


        }catch(Exception e){
            e.printStackTrace();

        }

    }




    public void getNearestBeaconStores(Location currentLocation) {
        Log.d("currentLocation : ", currentLocation.getLatitude() + ","
                + currentLocation.getLongitude());

        ArrayList<FBStoreItem> storesDistance = new ArrayList<FBStoreItem>();
        if (allStoreFromServer == null)
            return;

        int i = 0;

        for (FBStoreItem store : allStoreFromServer) {

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


        allStoresAfterSort = new ArrayList<FBStoreItem>( storesDistance.subList(0, noOfStores));
        //clpSdkData.setAllStoresList(curStores);
    }

    private class StoreDistanceComparator implements Comparator<FBStoreItem> {
        @Override
        public int compare(FBStoreItem store1, FBStoreItem store2) {
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




    HashMap<String,String> getHeaderwithsecurityforerror(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        //header.put("scope","READ");
        header.put("tenantid","1173");
        header.put("tenantName","fishbowl");
        return header;
    }



    HashMap<String,String> getHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        //header.put("X-XSRF-TOKEN","1234");
        //header.put("REDIS","true");
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        //header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        //header.put("scope","READ");
        header.put("tenantid","1173");
        return header;
    }



    HashMap<String,String> getHeaderwithsecurity(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application", FBConstant.client_Application);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
        header.put("client_id", FBConstant.client_id);
        header.put("deviceId",FBUtility.getAndroidDeviceID(this.fbSdk.context));
        //header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        //header.put("scope","READ");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("tenantName", FBConstant.client_tenantName);
        return header;
    }

    HashMap<String,String> getHeaderforsearchstore(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application", FBConstant.client_Application);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId",FBUtility.getAndroidDeviceID(this.fbSdk.context));
        header.put("client_id", FBConstant.client_id);
       header.put("client_secret", FBConstant.client_secret);
        header.put("tenantid", FBConstant.client_tenantid);

        return header;
    }



    //Interface for
    public interface FBStoreCallback {
        public void OnFBStoreCallback(JSONObject response, String error);
    }


    //Interface for
    public interface FBSearchStoreCallback {
        public void OnFBSearchStoreCallback(List<FBStoreItem> response, String error);
    }

}
