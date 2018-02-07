package com.fishbowl.basicmodule.Services;

import android.location.Location;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Controllers.LocationService;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.FBMobileEventItem;
import com.fishbowl.basicmodule.Models.FBStoreItem;
import com.fishbowl.basicmodule.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by digvijay(dj)
 */
public class FBLocationService {
	private FBSdk fbSdk;
	public static FBLocationService instance ;
	public String mLastUpdateTime;
	

	
	String TAG="FBLocationService";
	public Map<Integer, Float> gfRadiusMap = new HashMap<Integer, Float>();
	ArrayList<String> currentActiveRegions;  
	LocationService locationService;
	public Location latestGLocation;
	
	
	public static FBLocationService sharedInstance(){
		if(instance==null){
			instance=new FBLocationService();
		} 
		return instance;
	} 
	
	public void init(FBSdk _fbsdk){
		fbSdk =_fbsdk;
	}
	
	public void setLocationService(LocationService locationService2){
		this.locationService=locationService2;
	}
	
    public void locationUpdateDevice(FBMobileEventItem mobileEvents) {

    	FBSdkData FBSdkData = fbSdk.getFBSdkData();

        if (FBSdkData.currCustomer == null || FBSdkData.currCustomer.memberid <= 0|| !FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        mobileEvents.company = FBSdkData.currCustomer.tenantid;
        mobileEvents.customerid = FBSdkData.currCustomer.memberid;
        mobileEvents.action = "LocationChange";
        mobileEvents.device_type = FBConstant.DEVICE_TYPE;
        mobileEvents.device_os_ver = FBUtility.getAndroidOs();
        mobileEvents.device_carrier = FBUtility.getDeviceCarier(fbSdk.context);

        Gson gson = new Gson();
		String json = gson.toJson(mobileEvents); 
		
		Log.d("locationUpdateDevice", "locationUpdateDevice + " + json+ "");
         
		String url= FBConstant.LocationChangeApi;
		
		FBService.getInstance().get(url, null,getHeader(),  new FBServiceCallback(){
			@Override
			public void onServiceCallback(JSONObject response,Exception error,String errorMessage) {
				
				  Log.d("locationUpdateDevice", "SUCCESS + "+ response + "");
				
			} 
		
		});
    }

	
	public void onLocationChanged(Location location) {
		try {
			fbSdk.mCurrentLocation = location;
			mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
			
			fbSdk.localLog("onLocationChanged", "Location update received");
		 
			if (fbSdk.getAllStoreList() != null && fbSdk.getAllStoreList().size() != 0) {
				checkInsideNearestStoreRegion(location);// check in region
														// manually

				// if (isInsideRegion) {
				// createInSideLocationRequest();
				// } else {
				// createOutSideLocationRequest();
				// }
			}
			if (FBGeoFenceService.sharedInstance().isInsideRegion) {
				float interval = Math
						.abs((int) (TimeUnit.SECONDS.toMillis(fbSdk
								.getLOCATION_UPDATE_PING_FREQUENCY()))); // check
				Boolean call = false;
				Date currentCall = new Date();
				if (fbSdk.lastLocationUpdateCall != null) {
					int locationInterval = (int) (currentCall.getTime() - fbSdk.lastLocationUpdateCall
							.getTime()) / 1000;
					if (locationInterval > interval) {
						call = true;
					}
				} else {
					call = true;
				}
				if (call) {
					// location update
					FBMobileEventItem clpMobileEvent = new FBMobileEventItem();
					clpMobileEvent.lat = location.getLatitude();
					clpMobileEvent.lon = location.getLongitude();
					locationUpdateDevice(clpMobileEvent);
				}
				if (FBGeoFenceService.sharedInstance().insideRegionTime != null) {
					int regionEnteredtime = (int) (currentCall.getTime() - FBGeoFenceService.sharedInstance().insideRegionTime.getTime()) / 1000;
					 
				   FBSdk.sharedInstance(fbSdk.context).localLog("onLocationChanged","slabtime check");
					if (regionEnteredtime >= (fbSdk.getIN_REGION_SLAB_TIME())&& !FBGeoFenceService.sharedInstance().isInsideStore) {
						// stopBeaconRanging();
						String inStore = isInStore(location);
						if (inStore != null) {
							FBGeoFenceService.sharedInstance().isInsideStore = true;
							this.locationService.mLocationRequest=createInStoreLocationRequest(this.locationService.mLocationRequest);
						} else {
							FBGeoFenceService.sharedInstance().startGeoFencing(true);
						}
					}
					
					if (regionEnteredtime >= (fbSdk.getBEACON_SLAB_TIME())&& FBGeoFenceService.sharedInstance().isInsideStore) {
						String inStore = isInStore(location);
						if (inStore != null) {
							List<String> stores = new ArrayList<String>();
							stores.add(inStore);
							FBBeaconService.sharedInstance().stopBeaconMonitoring(stores);
							FBBeaconService.sharedInstance().stopBeaconRanging(stores);// newly added
							FBSdk.sharedInstance(fbSdk.context).localLog("onLocationChanged","Beacon stopped after slabtime");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String isInStore(Location location) {
		FBStoreItem thisStore;
		for (String cRegion : currentActiveRegions) {
			try {
				if (fbSdk.storesMap.containsKey(Integer.valueOf(cRegion))) {
					thisStore = fbSdk.storesMap.get(Integer.valueOf(cRegion));
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			float dist = location.distanceTo(thisStore.getLocation());
			FBSdk.sharedInstance(fbSdk.context).localLog(TAG, "dist to store :" + dist);
			if (dist < fbSdk.getGEOFENCE_MIN_RADIUS()) {
				return cRegion;
			}
		}
		return null;
	}

	

	protected void checkInsideNearestStoreRegion(Location userLocation) {
		FBGeoFenceService.sharedInstance().isInsideRegion = false;
		for (FBStoreItem geoStores : fbSdk.getAllStoreList()) {
			Location storeLocation = new Location("storeLocation");
			storeLocation.setLatitude(Double.valueOf(geoStores.getLatitude()));
			storeLocation
					.setLongitude(Double.valueOf(geoStores.getLongitude()));
			if (gfRadiusMap.containsKey(geoStores.storeID)) {
				float checkRadii = gfRadiusMap.get(geoStores.storeID);
				Log.i("Store distance :", geoStores.getStoreName() + " "
						+ storeLocation.distanceTo(userLocation) + " LA:"+ geoStores.getLatitude() +" LO:"+ geoStores.getLongitude());
				if ((storeLocation.distanceTo(userLocation)) <= checkRadii) {
					FBGeoFenceService.sharedInstance().isInsideRegion = true;
					break;
				}
			}
		}
	}
	
	
	public LocationRequest createOutSideLocationRequest(LocationRequest mLocationRequest) {
		FBSdk.sharedInstance(fbSdk.context).localLog(TAG, "createOutSideLocationRequest");
		if (fbSdk.currentLocationRequest == null
				|| !fbSdk.currentLocationRequest
						.contains(FBSdk.OUTSIDE_REGION)) {
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(fbSdk.getOUT_SIDE_UPDATE_INTERVAL()));
			mLocationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(fbSdk.getOUT_SIDE_FASTEST_UPDATE_INTERVAL()));
			mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			// mLocationRequest.setSmallestDisplacement(5f);
			locationService.startUpdatesHandler(mLocationRequest);
			fbSdk.currentLocationRequest = FBSdk.OUTSIDE_REGION;
		} else {
			FBSdk.sharedInstance(fbSdk.context).localLog(TAG,
					"already in OutSideLocationRequest");
		}
		return mLocationRequest;
	}

	public LocationRequest createInSideLocationRequest(LocationRequest mLocationRequest) {
		FBSdk.sharedInstance(fbSdk.context).localLog(TAG, "createInSideLocationRequest");
		if (fbSdk.currentLocationRequest == null
				|| !fbSdk.currentLocationRequest
						.contains(FBSdk.INSIDE_REGION)) {
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(fbSdk.getIN_SIDE_UPDATE_INTERVAL()));
			mLocationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(fbSdk.getIN_SIDE_FASTEST_UPDATE_INTERVAL()));
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			// mLocationRequest.setSmallestDisplacement(5f);
			locationService.startUpdatesHandler(mLocationRequest);
			fbSdk.currentLocationRequest = FBSdk.INSIDE_REGION;
		} else {
			FBSdk.sharedInstance(fbSdk.context).localLog(TAG,"already in InSideLocationRequest");
		}

		return mLocationRequest;
	}

	protected LocationRequest createInStoreLocationRequest(LocationRequest mLocationRequest) {
		FBSdk.sharedInstance(fbSdk.context).localLog(TAG, "createInStoreLocationRequest");
		if (fbSdk.currentLocationRequest == null|| !fbSdk.currentLocationRequest.contains(FBSdk.INSIDE_STORE)) {
			
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(fbSdk.getIN_STORE_UPDATE_INTERVAL()));
			mLocationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(fbSdk.getIN_STORE_UPDATE_INTERVAL()));
			mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			mLocationRequest.setSmallestDisplacement(15f);
			locationService.startUpdatesHandler(mLocationRequest);
			
			fbSdk.currentLocationRequest = FBSdk.INSIDE_STORE;
		} else {
			FBSdk.sharedInstance(fbSdk.context).localLog(TAG,"already in InStoreLocationRequest");
		}
		return mLocationRequest;
	}  
	
	
	
	//
	 
 	public interface locationUpdateDeviceCallback{
 	 		public void onlocationUpdateDevice(JSONObject response, String error);
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



	HashMap<String,String> getHeader(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application","mobilesdk");
		header.put("deviceId",FBUtility.getAndroidDeviceID(this.fbSdk.context));
	//	header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
		//header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
		//header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
		//header.put("scope","READ");
		header.put("tenantid","1173");
		return header;
	}


}
