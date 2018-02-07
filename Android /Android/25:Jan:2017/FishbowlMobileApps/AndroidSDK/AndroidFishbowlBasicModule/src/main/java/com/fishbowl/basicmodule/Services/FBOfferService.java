package com.fishbowl.basicmodule.Services;

import android.location.Location;
import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBServicePassCallback;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by digvijay(dj)
 */
public class FBOfferService {
	private  static FBSdk fbSdk;
	String TAG="FBOfferService";
	public static FBOfferService instance ;

	public static FBOfferService sharedInstance(){
		if(instance==null){
			instance=new FBOfferService();
		}
		return instance;
	}

	public void init(FBSdk _fbsdk){
		fbSdk =_fbsdk;
	}

	public void getFBOffer(final JSONObject offer, final String customId, final FBOfferCallback callback) {

		String url = "mobile/getoffers" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberId() + "/" + "0";
		FBService.getInstance().get(url, null, getHeaderwithsecurity(), new FBServiceCallback() {
			public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {

				if(error==null&&response!=null){
					try {


							if (callback != null) {
								callback.OnFBOfferCallback(response, "");
							}


					} catch (Exception e) {

					}

				}
//				else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//				{
//					FBTokenService.sharedInstance(fbSdk).getTokenUserClypOffer(offer,customId,callback);
//				}

				else
				{
					callback.OnFBOfferCallback(response,"");
				}
			}
		});
	}



	public void getFBOfferByOfferId(final JSONObject offer, final String offerId, final FBOfferByIdCallback callback) {

		String url = "mobile/getOfferByOfferId" + "/" + offerId ;
		FBService.getInstance().get(url, null, getHeaderwithsecurity(), new FBServiceCallback() {
			public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
				if(error==null&&response!=null){
					try {

							if (callback != null) {
								callback.onFBOfferByIdCallback(response, error);
							}

//						else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//						{
//							FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferByOfferId(offer,offerId,callback);
//						}

					}

					catch (Exception e) {

					}

				}
//				else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//				{
//					FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferByOfferId(offer,offerId,callback);
//				}

				else
				{
					callback.onFBOfferByIdCallback(response,error);
				}
			}
		});
	}


	//redeemedservices
	public void getFBRedeemedOffer(final JSONObject offer, final String itemId, final FBRedeemedCallback callback) {
		FBSdkData FBSdkData = fbSdk.getFBSdkData();

		try {

			try {
				offer.put("tenantid", FBSdkData.currCustomer.getCompanyId());
				offer.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
				offer.put("lat",String.valueOf(fbSdk.mCurrentLocation.getLatitude()));
				offer.put("lon",String.valueOf(fbSdk.mCurrentLocation.getLongitude()));
				offer.put("device_type", FBConstant.DEVICE_TYPE);
				offer.put("device_os_ver", FBUtility.getAndroidOs());
			}

			catch (JSONException e)
			{
				e.printStackTrace();
			}


			String json = offer.toString();

			String url= "mobile/redeemed" +"/"+ FBPreferences.sharedInstance(fbSdk.context).getUserMemberId()+"/"+itemId;

			FBService.getInstance().post(url, json,null, new FBServiceCallback(){

				public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
					if (response != null) {
						if (callback != null) {
							callback.OnFBRedeemedCallback(response, "");
						}

					}
//					else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//					{
//						FBTokenService.sharedInstance(fbSdk).getTokenClypRedeemedOffer(offer,itemId,callback);
//					}
					else
					{
						callback.OnFBRedeemedCallback(response, "");
					}

				}
			});

		}catch(Exception e){
			e.printStackTrace();

		}

	}


	public void getFBOfferPass(final JSONObject offerpromo, final String offerId, final boolean isPMIntegrated, final FBOfferPassCallback callback) {


		String entity = null;

		try {
			JSONObject object = new JSONObject();
			object.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
			object.put("campaignId", offerId);
			object.put("deviceName", "ANDROID");
			entity = object.toString();
		} catch (Exception e) {
		}

		String url = "mobile/getPass";
		FBService.getInstance().makeCustomPassRequest(url, entity, getHeaderwithsecurity(), new FBServicePassCallback() {
			public void onServicePassCallback(byte[] response, Exception error, String errorMessage) {
				if (response != null) {
					try {

						if (callback != null) {
							callback.OnFBOfferPassCallback(response, null);
						}

					} catch (Exception e) {
						callback.OnFBOfferPassCallback(null, e.getLocalizedMessage());
					}
				}
//				else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//				{
//
//					FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferPass(offerpromo, offerId, isPMIntegrated, callback);
//				}
				else
				{
					callback.OnFBOfferPassCallback(response, null);
				}
			}
		});


	}

//
//	public void getFBOfferPromo(final JSONObject offerpromo, final String itemId, final Boolean isPMIntegrated, final FBOfferPromoCallback callback) {
//
//		FBSdkData FBSdkData = fbSdk.getFBSdkData();
//
//		String  entity;
//
//		try {
//
//			offerpromo.put("tenantid", FBSdkData.currCustomer.getCompanyId());
//			offerpromo.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
//			offerpromo.put("lat", String.valueOf(fbSdk.mCurrentLocation.getLatitude()));
//			offerpromo.put("lon", String.valueOf(fbSdk.mCurrentLocation.getLongitude()));
//			offerpromo.put("device_type", FBConstant.DEVICE_TYPE);
//			offerpromo.put("device_os_ver", FBUtility.getAndroidOs());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		entity = offerpromo.toString();
//
//		String url="mobile/getPromo" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberId() + "/" + itemId +"/" + isPMIntegrated;
//
//		FBService.getInstance().get(url, null,getHeaderwithsecurity(), new FBServiceCallback(){
//			public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
//				if (response != null) {
//					try {
//						if (!response.has("stores")) {
//							Log.d("sendOfferEvent", "SUCC + " + response + "");
//
//							if (callback != null) {
//								callback.OnFBOfferPromoCallback(response, "");
//							}
//						}
//					} catch (Exception e) {
//
//					}
//				}
////				else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
////				{
////					FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferPromo(offerpromo,itemId,isPMIntegrated,callback);
////				}
//
//				else
//				{
//					callback.OnFBOfferPromoCallback(response, "");
//				}
//			}
//		});
//
//	}


	public void sendOfferEvent(JSONObject offerEvent) {
		FBSdkData FBSdkData = fbSdk.getFBSdkData();


		if (fbSdk.mCurrentLocation == null) {
			Location curLoc = new Location("");
			curLoc.setLatitude(0);
			curLoc.setLongitude(0);
			fbSdk.mCurrentLocation = curLoc;
		}
		String  entity;
		try {
			offerEvent.put("action", "CLPEvent");
			offerEvent.put("tenantid",
					FBSdkData.currCustomer.getCompanyId());
			offerEvent.put("memberid",
					FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
			offerEvent.put("lat",
					String.valueOf(fbSdk.mCurrentLocation.getLatitude()));
			offerEvent.put("lon",
					String.valueOf(fbSdk.mCurrentLocation.getLongitude()));
			offerEvent.put("device_type", FBConstant.DEVICE_TYPE);
			offerEvent.put("device_os_ver", FBUtility.getAndroidOs());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		entity = offerEvent.toString();


		String url= "mobile/submitclpevents";

		FBService.getInstance().post(url, entity,null, new FBServiceCallback(){
			public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

				Log.d("sendOfferEvent", "SUCC + " + response + "");

			}
		});
	}


	//Interface for
	public interface FBOfferCallback {
		public void OnFBOfferCallback(JSONObject response, String error);
	}

	public interface FBOfferPassCallback {
		public void OnFBOfferPassCallback(byte[] response, String error);
	}


	public interface FBOfferPromoCallback {
		public void OnFBOfferPromoCallback(JSONObject response, String error);
	}

	public interface FBOfferPushCallback {
		public void onFBOfferPushCallback(JSONObject response, String error);
	}

	public interface FBOfferByIdCallback {
		public void onFBOfferByIdCallback(JSONObject response, Exception error);
	}



	public interface FBOfferEventCallback {
		public void OnFBOfferEventCallback(JSONObject response, String error);
	}
	public interface FBRedeemedCallback {
		public void OnFBRedeemedCallback(JSONObject response, String error);
	}



	HashMap<String,String> getHeaderwithsecurity(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application", FBConstant.client_Application);
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
		header.put("client_id", FBConstant.client_id);
		header.put("client_secret", FBConstant.client_secret);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		//header.put("scope","READ");
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("tenantName", FBConstant.client_tenantName);
		return header;
	}


}
