package com.fishbowl.basicmodule.Services;


import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.FBMobileSettingItem;
import com.fishbowl.basicmodule.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by digvijay(dj)
 */
public class FBMobileSettingService {

	public FBSdk fbSdk;
	public static FBMobileSettingService instance;
	public FBMobileSettingItem mobileSettings;

	public static FBMobileSettingService sharedInstance(){
		if(instance==null){
			instance=new FBMobileSettingService();
		}
		return instance;
	}

	public void init(FBSdk _fbsdk){
		fbSdk =_fbsdk;
	}

	public  void getMobileSetting(final FBSdk fbsdk, String cusId, final FBMobileSettingCallback callback){

		final FBSdkData FBSdkData = fbsdk.getFBSdkData();

		mobileSettings=new FBMobileSettingItem();
		if (!FBUtility.isNetworkAvailable(fbsdk.context)) {
			return;
		}

		FBService.getInstance().get(FBConstant.MobileSettingApi + cusId, null,getHeaderwithsecurity(), new FBServiceCallback(){

			public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

				try {

					if(error==null&&response!=null){

						if (response.has("mobSettingList")) {
							JSONObject mobSettingList= response.getJSONObject("mobSettingList");
							mobileSettings.initFromJson(mobSettingList);
							FBSdkData.setMobileSettings(mobileSettings);
						}

						////add By Mohd Vaseem for Clyp Analytic Settings///

						if (response.has("digitalEventList")) {
							JSONObject digitalEventList= response.getJSONObject("digitalEventList");
							FBAnalytic.sharedInstance().initEventSettings(digitalEventList);
							FBEventSettings evtSettings= FBAnalytic.sharedInstance().getFBEventSettings();
							//set value from Mobile setting to event setting
							FBAnalytic.sharedInstance().EVENT_QUEUE_SIZE_THRESHOLD= FBSdkData.getMobileSettings().getEVENT_QUEUE_SIZE_THRESHOLD();
							FBAnalytic.sharedInstance().TIMER_DELAY_IN_SECONDS= FBSdkData.getMobileSettings().getTIMER_DELAY_IN_SECONDS();
							//evtSettings.setEVENT_QUEUE_SIZE_THRESHOLD(FBSdkData.mobileSettings.getEVENT_QUEUE_SIZE_THRESHOLD());
							//evtSettings.setTIMER_DELAY_IN_SECONDS(FBSdkData.mobileSettings.getTIMER_DELAY_IN_SECONDS());
							FBSdkData.setEventSetting(evtSettings);
						}
						callback.OnFBMobileSettingCallback(true, error);
					}
					else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
					{
						FBTokenService.sharedInstance(fbsdk).getTokenMobileSetting();
					}


				}
				catch (JSONException e) {
					callback.OnFBMobileSettingCallback(false, e);
					e.printStackTrace();
				}

			}

		});
	}


	HashMap<String,String> getHeader(){
		HashMap<String,String> header=new HashMap<String, String>();
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
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
		header.put("client_id", FBConstant.client_id);
		header.put("deviceId",FBUtility.getAndroidDeviceID(this.fbSdk.context));
		//header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
		//header.put("scope","READ");
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("tenantName", FBConstant.client_tenantName);
		return header;
	}


	//Interface for
	public interface FBMobileSettingCallback {
		public void OnFBMobileSettingCallback(boolean state, Exception error);
	}

}
