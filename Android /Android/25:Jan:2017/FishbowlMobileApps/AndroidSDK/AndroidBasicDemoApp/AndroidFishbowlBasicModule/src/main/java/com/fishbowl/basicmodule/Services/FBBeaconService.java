package com.fishbowl.basicmodule.Services;

import android.util.Log;

import com.Preferences.FBPreferences;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.BeaconManager.RangingListener;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils.Proximity;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.FBBeaconRegionItem;
import com.fishbowl.basicmodule.Models.FBBeaconsItem;
import com.fishbowl.basicmodule.Models.FBMobileEventsItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by digvijay(dj)
 */
public class FBBeaconService {
	public BeaconManager beaconManager;
	public boolean beaconStarted;
	private FBSdk fbSdk;
	String TAG="FBBeaconService";
	public Date lastBeaconCall = null;//from in LocationService

	public static FBBeaconService instance ;
	ArrayList<FBBeaconRegionItem> beaconMonitorRegions = new ArrayList<FBBeaconRegionItem>();
	ArrayList<FBBeaconRegionItem> beaconRangingRegions = new ArrayList<FBBeaconRegionItem>();
	public List<FBBeaconsItem> beaconListInGeofence = new ArrayList<FBBeaconsItem>(); //old firstname allBeaconsForStores
	
	
	public static FBBeaconService sharedInstance(){
		if(instance==null){
			instance=new FBBeaconService();
		} 
		return instance;
	}
	
	public void init(FBSdk _fbsdk){
		
		fbSdk =_fbsdk;
		this.beaconManager = new BeaconManager(fbSdk.context);
        this.beaconStarted = false; 
	}
	
	public void beaconInRange(FBMobileEventsItem mobileEvents, FBBeaconsInRangeCallback callback) {
		
			final FBSdkData FBSdkData = fbSdk.getFBSdkData();
			
	        if (FBSdkData.currCustomer == null|| FBSdkData.currCustomer.memberid <= 0|| !FBUtility.isNetworkAvailable(fbSdk.context)) {
	            return;
	        }  
	        try {
	            mobileEvents.company = FBSdkData.currCustomer.tenantid;
	            mobileEvents.customerid = FBSdkData.currCustomer.memberid;
	            mobileEvents.action = "BeaconInRange";
	            mobileEvents.device_type = FBConstant.DEVICE_TYPE;
	            mobileEvents.device_os_ver = FBUtility.getAndroidOs();
	            mobileEvents.device_carrier = FBUtility.getDeviceCarier(fbSdk.context);
	            
	            if (FBSdkData.mobileSettings.TRIGGER_BEACON > 0) {
				    Gson gson = new Gson();
				    String json = gson.toJson(mobileEvents);
				    String entity = new String(json);
				    //entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				    String url = FBConstant.BeconInRange;
				    
				    FBService.getInstance().post(url, entity,getHeaderwithsecurity(),  new FBServiceCallback(){
				    	
				  	   public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
				  		   		if(error==null&&response!=null){   
				                    // JSONObject getAnObject = response.get(0);
				                    // String BC = getAnObject.getString("Values");
				                    Log.d("beaconInRange", "SUCC + " + response  + "");
				               
				  		   		}else{
				  		   		 Log.d("beaconInRange", "Fail + " + response  + ""); 
				  		   		}
				  		  }

				               
				            });
				} else {
				    Log.d("beaconInRange", "TRIGGER_BEACON is off from server");

				}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }


    public void getBeaconsForStoreNo(ArrayList<String> stores,final FBBeaconsForStoreNoCallback callback) {
    	 
        for (String store : stores) {
            int nearestStore = Integer.valueOf(store);
            String url = FBConstant.BeaconApi + nearestStore;
            
            FBService.getInstance().get(url, null,getHeaderwithsecurity(),  new FBServiceCallback(){
            	
         	   public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
         		  if(error==null&&response!=null){
                        try {
							if (response.getBoolean("successFlag")) {
								
								JSONArray getArrayBeacons = response.getJSONArray("beacons");
								
							    for (int i = 0; i < getArrayBeacons.length(); i++) {
							    	
							        JSONObject myBeaconsObj = getArrayBeacons.getJSONObject(i);
							        
							        FBBeaconsItem getBeaconsObj = new FBBeaconsItem(myBeaconsObj);
							        
							        Log.d("beacons", getBeaconsObj.toString());
							        
							        beaconListInGeofence.add(getBeaconsObj);
							        
							        //Remove Beacon from Location One If beaconListInGeofence is more then in mobile settings
							        if (beaconListInGeofence.size() >= fbSdk.getMAX_BEACON_COUNT() && beaconListInGeofence.size() > 0) {
							        	beaconListInGeofence.remove(1);// remove
							            // first
							            // beacon
							        }
							    }
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        callback.OnFBBeaconsForStoreNoCallback(response,null);
                      
         		  }else{ 
         			  Log.d("Error","Somthing Wrong"); 
         		  }    
                }  
            });
        }  
        
    } 
    
    /////////////////////////////////////////////////////////////////////
    /////////////////   0.Start Beacon Method     //////////////////////
    /////////////////////////////////////////////////////////////////////
	public synchronized void startBeaconForStore() {

		if (beaconListInGeofence != null) {
			if (beaconListInGeofence != null) {
				if (beaconManager != null) {
					stopBeaconMonitoring();
				}
				// Start_Beacon_Monitoring(_clpSdk.allBeaconsForStores);
				Start_Beacon_Ranging(beaconListInGeofence);

			}
		}


	}
    
    /////////////////////////////////////////////////////////////////////
       /////////////////   1.Start Becon Ranging      ////////////////
    /////////////////////////////////////////////////////////////////////
   
    public void Start_Beacon_Ranging(final List<FBBeaconsItem> lstBeaconRegions){

		if (beaconManager == null)
			beaconManager = new BeaconManager(fbSdk.context);
		
		   //1
		    beaconManager.setRangingListener(new RangingListener() {
			@Override
			public void onBeaconsDiscovered(Region region, List<Beacon> regionList) {
				
				if (regionList != null && !regionList.isEmpty()) {
					
					Beacon beacon = regionList.get(0);
					
					Proximity proximity = computeProximity(beacon);
					Log.d("TAG", "entered in bregion " + region.getIdentifier()+ " " + region.getProximityUUID());

					if (proximity == Proximity.IMMEDIATE|| proximity == Proximity.NEAR|| proximity == Proximity.FAR) {
							float interval = fbSdk.getBEACON_PING_FREQUENCY(); //
							Boolean call = false;
							Date currentCall = new Date();
							int beaconpingInterval = 0;
							if (lastBeaconCall != null) {
								beaconpingInterval = (int) (currentCall.getTime() - lastBeaconCall.getTime()) / 1000;
							if (beaconpingInterval >= interval) {
								call = true;
							}
						} else {
							call = true;
						}

						if (call) {
							FBSdk.sharedInstance(fbSdk.context).localLog(TAG,"beaconpingInterval : "+ beaconpingInterval);
							lastBeaconCall = currentCall;
							FBMobileEventsItem clpMobileEvent = new FBMobileEventsItem();
							clpMobileEvent.lat = fbSdk.mCurrentLocation.getLatitude();
							clpMobileEvent.lon = fbSdk.mCurrentLocation.getLongitude();
							clpMobileEvent.beaconid = Integer.parseInt(region
									.getIdentifier());
							fbSdk.beaconInRange(fbSdk.context, clpMobileEvent);
						}
					}
				}
			}
		});
        //2
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					Region region;
					FBSdk.sharedInstance(fbSdk.context).localLog("beacon"," beaconRegionList: " + lstBeaconRegions);
					
					beaconRangingRegions = new ArrayList<FBBeaconRegionItem>();

					for (FBBeaconsItem beacon : lstBeaconRegions) {
						
						//Region
						region = new Region(String.valueOf(beacon.beaconId), UUID.fromString(beacon.udid), beacon.major, beacon.minor);
						
						if (FBUtility.isBluetoothEnabled(fbSdk.context) == false&& fbSdk.getBluetoothPermission()) {
							
							FBUtility.enableBLEDevice(fbSdk.context);
						}
						
						beaconManager.startRanging(region);
						
						FBBeaconRegionItem beaconRegion = new FBBeaconRegionItem(region, beacon.storeID);
						
						beaconRangingRegions.add(beaconRegion);// add region to stop monitor
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
    
    /////////////////////////////////////////////////////////////////////
    /////////////////   2 Stop all Becon Ranging       ////////////////
    /////////////////////////////////////////////////////////////////////
   
	public void stopBeaconRanging() {
		try {
			if (beaconManager != null && beaconRangingRegions != null) {

				for (FBBeaconRegionItem beacon : beaconRangingRegions) {
					beaconManager.stopRanging(beacon.region);
				}
				beaconRangingRegions.clear();
			}
		} catch (Exception e) {
			FBSdk.sharedInstance(fbSdk.context).localLog("Exception StopBeacon : ",e.getMessage());
		}
	}
	
    /////////////////////////////////////////////////////////////////////
       //////  3. Stop  Becon Ranging for perticular Stores    ///////
    /////////////////////////////////////////////////////////////////////
 
	public void stopBeaconRanging(List<String> storeIds) {
		try {
			if (beaconManager != null && beaconRangingRegions != null) {

				for (FBBeaconRegionItem beacon : beaconRangingRegions) {
					for (String storeId : storeIds) {
						if (beacon.StoreId == Integer.parseInt(storeId)) {
							beaconManager.stopRanging(beacon.region);
						}
					}
				}
			}
		} catch (Exception e) {
			FBSdk.sharedInstance(fbSdk.context).localLog("Exception StopBeacon : ",
					e.getMessage());
		}
	}

    /////////////////////////////////////////////////////////////////////
    /////////  4. Start   Becon Monitoring     /////////////////
	/////////////////////////////////////////////////////////////////////
	 public void Start_Beacon_Monitoring(final List<FBBeaconsItem>ListBeaconRegions) {
	
	 if (beaconManager == null)
		 beaconManager = new BeaconManager(fbSdk.context);
	 
	 //Monitor Listener
	 beaconManager.setMonitoringListener(new MonitoringListener() {
	
		 @Override
		 public void onExitedRegion(Region region) {
		 			stopBeaconRanging();
		 }
	
		 @Override
		 public void onEnteredRegion(Region region, List<Beacon> beaconList) { //
			 Start_Beacon_Ranging(beaconListInGeofence);
		 }
	 });
	 
	 
	 //Monitor Connect
	 beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
		 @Override
		 public void onServiceReady() {
			 try {
				 //com.estimote.sdk.utils.enableDebugLogging(true);// enable

				 // log
				 Region region;
				 FBSdk.sharedInstance(fbSdk.context).localLog("", " beaconRegionList: " +ListBeaconRegions);
				 beaconMonitorRegions = new ArrayList<FBBeaconRegionItem>();

				 for (FBBeaconsItem beacon : ListBeaconRegions) {

					 	region = new Region(String.valueOf(beacon.beaconId), UUID.fromString(beacon.udid), beacon.major, beacon.minor);

					 if (FBUtility.isBluetoothEnabled(fbSdk.context) == false && fbSdk.getBluetoothPermission()) {
					 			FBUtility.enableBLEDevice(fbSdk.context);
					 	}

					 	beaconManager.startMonitoring(region);
					 	FBBeaconRegionItem beaconRegion = new FBBeaconRegionItem(region, beacon.storeID);
					 	beaconMonitorRegions.add(beaconRegion);// add region to
					 	// stop
					 	// monitor
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
	 	});
	 
	 
	 }
	 
    /////////////////////////////////////////////////////////////////////
       ///////// 5. Stop  all Becon Monitoring     /////////////////
   /////////////////////////////////////////////////////////////////////
	public void stopBeaconMonitoring() {
		try {
			if (beaconManager != null && beaconMonitorRegions != null) {
				for (FBBeaconRegionItem beacon : beaconMonitorRegions) {
					beaconManager.stopMonitoring(beacon.region);
				}
				beaconMonitorRegions.clear();
			}
		} catch (Exception e) {
			FBSdk.sharedInstance(fbSdk.context).localLog("Exception StopBeacon : ",
					e.getMessage());
		}
	}
	
    /////////////////////////////////////////////////////////////////////
    /////////  6. Stop   Becon Monitoring perticular Store    /////////
    /////////////////////////////////////////////////////////////////////

	public void stopBeaconMonitoring(List<String> storeIds) {
		try {
			if (beaconManager != null && beaconMonitorRegions != null) {
				for (FBBeaconRegionItem beacon : beaconMonitorRegions) {
					for (String storeId : storeIds) {
						if (beacon.StoreId == Integer.parseInt(storeId)) {
							beaconManager.stopMonitoring(beacon.region);
						}
					}
				}
			}
		} catch (Exception e) {
			FBSdk.sharedInstance(fbSdk.context).localLog("Exception StopBeacon : ",
					e.getMessage());
		}
	}
 


	// compute accuracy
	public static double computeAccuracy(Beacon beacon) {
		if (beacon.getRssi() == 0) {
			return -1.0D;
		}

		double ratio = beacon.getRssi() / beacon.getMeasuredPower();
		double rssiCorrection = 0.96D + Math.pow(Math.abs(beacon.getRssi()),
				3.0D) % 10.0D / 150.0D;

		if (ratio <= 1.0D) {
			return Math.pow(ratio, 9.98D) * rssiCorrection;
		}
		return (0.103D + 0.89978D * Math.pow(ratio, 7.71D)) * rssiCorrection;
	}

	public static Proximity proximityFromAccuracy(double accuracy) {
		if (accuracy < 0.0D) {
			return Proximity.UNKNOWN;
		}
		if (accuracy < 0.5D) {
			return Proximity.IMMEDIATE;
		}
		if (accuracy <= 3.0D) {
			return Proximity.NEAR;
		}
		return Proximity.FAR;
	}
	HashMap<String,String> getHeaderwithsecurity(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application", FBConstant.client_Application);
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
		header.put("client_id", FBConstant.client_id);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("tenantName", FBConstant.client_tenantName);
		return header;
	}


	public static Proximity computeProximity(Beacon beacon) {
		return proximityFromAccuracy(computeAccuracy(beacon));
	} 
	
	
    //Interface for 
 	public interface FBBeaconsForStoreNoCallback {
 		public void OnFBBeaconsForStoreNoCallback(JSONObject response, String error);
 	}    
 	 
 	public interface FBBeaconsInRangeCallback {
 	 		public void OnFBBeaconsInRangeCallback(JSONObject response, String error);
 	}    
    
	
}
