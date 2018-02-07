package com.fishbowl.basicmodule.Services;

import android.location.Location;
import android.util.Log;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBPassCallback;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by digvijay(dj)
 */
public class FBOfferService {
	private FBSdk fbSdk;
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
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {



		FBSdkData FBSdkData =this.fbSdk.getFBSdkData();

		if (fbSdk.mCurrentLocation == null) {
			Location curLoc = new Location("");
			curLoc.setLatitude(0);
			curLoc.setLongitude(0);
			fbSdk.mCurrentLocation = curLoc;
		}
		String entity;



				String url = "mobile/getoffers" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberId() + "/" + "0";
				Log.d("sendOfferEvent", "url + " + url);
				FBService.getInstance().get(url, null, getHeaderwithsecurity(), new FBServiceCallback() {
					public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {

//						String jsonstr = "{\"inAppOfferList\":[{\"storeRestriction\":[],\"campaignId\":382109,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"$1.00 Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382109,\"campaignTitle\":\"TEST_FB_02142017_1\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382110,\"mobileNotificationId\":0,\"campaignType\":\"2\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382110,\"campaignTitle\":\"TEST_FB_02142017_2\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382112,\"mobileNotificationId\":0,\"campaignType\":\"3\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"Free Apple 'N Greens Smoothie\",\"onlineinStore\":\"1\",\"promotionID\":382112,\"campaignTitle\":\"TEST_FB_02142017_3\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382113,\"mobileNotificationId\":0,\"campaignType\":\"7\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% off your smoothie, min purchase $10\",\"onlineinStore\":\"1\",\"promotionID\":382113,\"campaignTitle\":\"TEST_FB_02142017_4\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"id\":0,\"storeRestriction\":[{\"storecode\":\"204\",\"storename\":\"Fort Union\",\"storeid\":97642,\"displayname\":null},{\"storecode\":\"481\",\"storename\":\"Union Station / Chicago\",\"storeid\":97774,\"displayname\":null},{\"storecode\":\"1286\",\"storename\":\"Union Station - DC\",\"storeid\":115860,\"displayname\":null},{\"storecode\":\"1316\",\"storename\":\"Dupont Circle\",\"storeid\":115869,\"displayname\":null},{\"storecode\":\"1378\",\"storename\":\"L'Enfant Plaza\",\"storeid\":146253,\"displayname\":null}],\"loyaltyPoints\":0,\"campaignId\":384402,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"channelTypeID\":1,\"campaignDescription\":\"Allowed only at 1316\",\"isPMOffer\":true,\"successFlag\":true,\"onlineinStore\":\"1\",\"promotionID\":384402,\"campaignTitle\":\"TEST_FB_02142017_5\"},{\"storeRestriction\":[],\"campaignId\":382109,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"$1.00 Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382109,\"campaignTitle\":\"TEST_FB_02142017_1\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382110,\"mobileNotificationId\":0,\"campaignType\":\"2\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382110,\"campaignTitle\":\"TEST_FB_02142017_2\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382112,\"mobileNotificationId\":0,\"campaignType\":\"3\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"Free Apple 'N Greens Smoothie\",\"onlineinStore\":\"1\",\"promotionID\":382112,\"campaignTitle\":\"TEST_FB_02142017_3\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382113,\"mobileNotificationId\":0,\"campaignType\":\"7\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% off your smoothie, min purchase $10\",\"onlineinStore\":\"1\",\"promotionID\":382113,\"campaignTitle\":\"TEST_FB_02142017_4\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"id\":0,\"storeRestriction\":[{\"storecode\":\"204\",\"storename\":\"Fort Union\",\"storeid\":97642,\"displayname\":null},{\"storecode\":\"481\",\"storename\":\"Union Station / Chicago\",\"storeid\":97774,\"displayname\":null},{\"storecode\":\"1286\",\"storename\":\"Union Station - DC\",\"storeid\":115860,\"displayname\":null},{\"storecode\":\"1316\",\"storename\":\"Dupont Circle\",\"storeid\":115869,\"displayname\":null},{\"storecode\":\"1378\",\"storename\":\"L'Enfant Plaza\",\"storeid\":146253,\"displayname\":null}],\"loyaltyPoints\":0,\"campaignId\":384402,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"channelTypeID\":1,\"campaignDescription\":\"Allowed only at 1316\",\"isPMOffer\":true,\"successFlag\":true,\"onlineinStore\":\"1\",\"promotionID\":384402,\"campaignTitle\":\"TEST_FB_02142017_5\"},{\"storeRestriction\":[],\"campaignId\":382109,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"$1.00 Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382109,\"campaignTitle\":\"TEST_FB_02142017_1\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382110,\"mobileNotificationId\":0,\"campaignType\":\"2\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382110,\"campaignTitle\":\"TEST_FB_02142017_2\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382112,\"mobileNotificationId\":0,\"campaignType\":\"3\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"Free Apple 'N Greens Smoothie\",\"onlineinStore\":\"1\",\"promotionID\":382112,\"campaignTitle\":\"TEST_FB_02142017_3\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382113,\"mobileNotificationId\":0,\"campaignType\":\"7\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% off your smoothie, min purchase $10\",\"onlineinStore\":\"1\",\"promotionID\":382113,\"campaignTitle\":\"TEST_FB_02142017_4\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"id\":0,\"storeRestriction\":[{\"storecode\":\"204\",\"storename\":\"Fort Union\",\"storeid\":97642,\"displayname\":null},{\"storecode\":\"481\",\"storename\":\"Union Station / Chicago\",\"storeid\":97774,\"displayname\":null},{\"storecode\":\"1286\",\"storename\":\"Union Station - DC\",\"storeid\":115860,\"displayname\":null},{\"storecode\":\"1316\",\"storename\":\"Dupont Circle\",\"storeid\":115869,\"displayname\":null},{\"storecode\":\"1378\",\"storename\":\"L'Enfant Plaza\",\"storeid\":146253,\"displayname\":null}],\"loyaltyPoints\":0,\"campaignId\":384402,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"channelTypeID\":1,\"campaignDescription\":\"Allowed only at 1316\",\"isPMOffer\":true,\"successFlag\":true,\"onlineinStore\":\"1\",\"promotionID\":384402,\"campaignTitle\":\"TEST_FB_02142017_5\"},{\"storeRestriction\":[],\"campaignId\":382109,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"$1.00 Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382109,\"campaignTitle\":\"TEST_FB_02142017_1\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382110,\"mobileNotificationId\":0,\"campaignType\":\"2\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382110,\"campaignTitle\":\"TEST_FB_02142017_2\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382112,\"mobileNotificationId\":0,\"campaignType\":\"3\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"Free Apple 'N Greens Smoothie\",\"onlineinStore\":\"1\",\"promotionID\":382112,\"campaignTitle\":\"TEST_FB_02142017_3\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382113,\"mobileNotificationId\":0,\"campaignType\":\"7\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% off your smoothie, min purchase $10\",\"onlineinStore\":\"1\",\"promotionID\":382113,\"campaignTitle\":\"TEST_FB_02142017_4\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"id\":0,\"storeRestriction\":[{\"storecode\":\"204\",\"storename\":\"Fort Union\",\"storeid\":97642,\"displayname\":null},{\"storecode\":\"481\",\"storename\":\"Union Station / Chicago\",\"storeid\":97774,\"displayname\":null},{\"storecode\":\"1286\",\"storename\":\"Union Station - DC\",\"storeid\":115860,\"displayname\":null},{\"storecode\":\"1316\",\"storename\":\"Dupont Circle\",\"storeid\":115869,\"displayname\":null},{\"storecode\":\"1378\",\"storename\":\"L'Enfant Plaza\",\"storeid\":146253,\"displayname\":null}],\"loyaltyPoints\":0,\"campaignId\":384402,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"channelTypeID\":1,\"campaignDescription\":\"Allowed only at 1316\",\"isPMOffer\":true,\"successFlag\":true,\"onlineinStore\":\"1\",\"promotionID\":384402,\"campaignTitle\":\"TEST_FB_02142017_5\"},{\"storeRestriction\":[],\"campaignId\":382109,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"$1.00 Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382109,\"campaignTitle\":\"TEST_FB_02142017_1\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382110,\"mobileNotificationId\":0,\"campaignType\":\"2\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382110,\"campaignTitle\":\"TEST_FB_02142017_2\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382112,\"mobileNotificationId\":0,\"campaignType\":\"3\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"Free Apple 'N Greens Smoothie\",\"onlineinStore\":\"1\",\"promotionID\":382112,\"campaignTitle\":\"TEST_FB_02142017_3\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382113,\"mobileNotificationId\":0,\"campaignType\":\"7\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% off your smoothie, min purchase $10\",\"onlineinStore\":\"1\",\"promotionID\":382113,\"campaignTitle\":\"TEST_FB_02142017_4\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"id\":0,\"storeRestriction\":[{\"storecode\":\"204\",\"storename\":\"Fort Union\",\"storeid\":97642,\"displayname\":null},{\"storecode\":\"481\",\"storename\":\"Union Station / Chicago\",\"storeid\":97774,\"displayname\":null},{\"storecode\":\"1286\",\"storename\":\"Union Station - DC\",\"storeid\":115860,\"displayname\":null},{\"storecode\":\"1316\",\"storename\":\"Dupont Circle\",\"storeid\":115869,\"displayname\":null},{\"storecode\":\"1378\",\"storename\":\"L'Enfant Plaza\",\"storeid\":146253,\"displayname\":null}],\"loyaltyPoints\":0,\"campaignId\":384402,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"channelTypeID\":1,\"campaignDescription\":\"Allowed only at 1316\",\"isPMOffer\":true,\"successFlag\":true,\"onlineinStore\":\"1\",\"promotionID\":384402,\"campaignTitle\":\"TEST_FB_02142017_5\"},{\"storeRestriction\":[],\"campaignId\":382109,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"$1.00 Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382109,\"campaignTitle\":\"TEST_FB_02142017_1\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382110,\"mobileNotificationId\":0,\"campaignType\":\"2\",\"validityEndDateTime\":\"2017-03-31 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% Off Your Check\",\"onlineinStore\":\"1\",\"promotionID\":382110,\"campaignTitle\":\"TEST_FB_02142017_2\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382112,\"mobileNotificationId\":0,\"campaignType\":\"3\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"Free Apple 'N Greens Smoothie\",\"onlineinStore\":\"1\",\"promotionID\":382112,\"campaignTitle\":\"TEST_FB_02142017_3\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"storeRestriction\":[],\"campaignId\":382113,\"mobileNotificationId\":0,\"campaignType\":\"7\",\"validityEndDateTime\":\"2017-03-17 04:00:00\",\"channelTypeID\":1,\"campaignDescription\":\"10% off your smoothie, min purchase $10\",\"onlineinStore\":\"1\",\"promotionID\":382113,\"campaignTitle\":\"TEST_FB_02142017_4\",\"id\":0,\"loyaltyPoints\":0,\"isPMOffer\":true,\"successFlag\":true},{\"id\":0,\"storeRestriction\":[{\"storecode\":\"204\",\"storename\":\"Fort Union\",\"storeid\":97642,\"displayname\":null},{\"storecode\":\"481\",\"storename\":\"Union Station / Chicago\",\"storeid\":97774,\"displayname\":null},{\"storecode\":\"1286\",\"storename\":\"Union Station - DC\",\"storeid\":115860,\"displayname\":null},{\"storecode\":\"1316\",\"storename\":\"Dupont Circle\",\"storeid\":115869,\"displayname\":null},{\"storecode\":\"1378\",\"storename\":\"L'Enfant Plaza\",\"storeid\":146253,\"displayname\":null}],\"loyaltyPoints\":0,\"campaignId\":384402,\"mobileNotificationId\":0,\"campaignType\":\"1\",\"channelTypeID\":1,\"campaignDescription\":\"Allowed only at 1316\",\"isPMOffer\":true,\"successFlag\":true,\"onlineinStore\":\"1\",\"promotionID\":384402,\"campaignTitle\":\"TEST_FB_02142017_5\"}],\"message\":\"Success\",\"successFlag\":true}";
//						try {
//							response = new JSONObject(jsonstr);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
						if (response != null) {
							try {
								if (!response.has("stores")) {
									Log.d("sendOfferEvent", "SUCC + " + response + "");

									if (callback != null) {
										callback.OnFBOfferCallback(response, "");
									}
								}

							} catch (Exception e) {

							}

						}
						else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
						{
							Log.d("sendOfferEvent", "failure + " + error + "error message" + errorMessage);
							FBTokenService.sharedInstance(fbSdk).getTokenUserClypOffer(offer,customId,callback);
						}

						else
						{
							Log.d("sendOfferEvent", "failure + " + error + "error message" + errorMessage);
							callback.OnFBOfferCallback(response,"");
						}
					}
				});
			}









	public void getFBOfferByOfferId(final JSONObject offer, final String offerId, final ClypOfferPushCallback callback) {
//		  	       // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {



		FBSdkData FBSdkData =this.fbSdk.getFBSdkData();

		String entity;



				String url = "mobile/getOfferByOfferId" + "/" + offerId ;
				FBService.getInstance().get(url, null, getHeader(), new FBServiceCallback() {
					public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
						if (response != null) {
							try {
								if (!response.has("stores")) {
									Log.d("sendOfferEvent", "SUCC + " + response + "");

									if (callback != null) {
										callback.onClypOfferPushCallback(response, "");
									}
								}
								else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
								{
									FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferByOfferId(offer,offerId,callback);
								}

							}

							catch (Exception e) {

							}

						}
						else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
						{
							FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferByOfferId(offer,offerId,callback);
						}

						else
						{
							callback.onClypOfferPushCallback(response, "");
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
						offer.put("memberid",FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
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

					String url= "mobile/redeemed" +"/"+FBPreferences.sharedInstance(fbSdk.context).getUserMemberId()+"/"+itemId;

					FBService.getInstance().post(url, json,null, new FBServiceCallback(){

						public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
							if (response != null) {
								if (callback != null) {
									callback.OnFBRedeemedCallback(response, "");
								}

							}
							else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
							{
								FBTokenService.sharedInstance(fbSdk).getTokenClypRedeemedOffer(offer,itemId,callback);
							}
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

		FBSdkData FBSdkData = this.fbSdk.getFBSdkData();


		String entity = null;

		// if (FBSdkData.currCustomer != null) {


		try {
			JSONObject object = new JSONObject();
			object.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
			object.put("campaignId", offerId);
			object.put("deviceName", "ANDROID");
			entity = object.toString();
		} catch (Exception e) {
		}


		//      String url=  "mobile/getPass" +"/"+FBUserManager.sharedInstance().member.customerID+"/" + offerId;
		String url = "mobile/getPass/";


		FBService.getInstance().makeCustomPassRequest(url, entity, getHeader(), new FBPassCallback() {
			public void onServicePassCallback(byte[] response, Exception error, String errorMessage) {
				if (response != null) {
					try {

						if (callback != null) {
							callback.OnFBOfferPassCallback(response, null);
						}

					} catch (Exception e) {
						callback.OnFBOfferPassCallback(null, e.getLocalizedMessage());
					}
				}else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
				{

					FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferPass(offerpromo, offerId, isPMIntegrated, callback);
				}
				else
				{
					callback.OnFBOfferPassCallback(response, null);
				}
			}
		});


	}


	public void getFBOfferPromo(final JSONObject offerpromo, final String itemId, final Boolean isPMIntegrated, final FBOfferPromoCallback callback) {

		FBSdkData fbSdkDataObj = fbSdk.getFBSdkData();

		String  entity;

				try {

					offerpromo.put("tenantid", ((fbSdkDataObj.currCustomer!=null)?fbSdkDataObj.currCustomer.getCompanyId():0));
					offerpromo.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
					offerpromo.put("lat", String.valueOf(fbSdk.mCurrentLocation.getLatitude()));
					offerpromo.put("lon", String.valueOf(fbSdk.mCurrentLocation.getLongitude()));
					offerpromo.put("device_type", FBConstant.DEVICE_TYPE);
					offerpromo.put("device_os_ver", FBUtility.getAndroidOs());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				entity = offerpromo.toString();

				String url="mobile/getPromo" + "/" + FBPreferences.sharedInstance(fbSdk.context).getUserMemberId() + "/" + itemId +"/" + isPMIntegrated;

				FBService.getInstance().get(url, null,getHeader(), new FBServiceCallback(){
					public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
						if (response != null) {
							try {
								if (!response.has("stores")) {
									Log.d("sendOfferEvent", "SUCC + " + response + "");

									if (callback != null) {
										callback.OnFBOfferPromoCallback(response, "");
									}
								}
							} catch (Exception e) {

							}
						}
						else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
						{
							FBTokenService.sharedInstance(fbSdk).getTokenUserClypOfferPromo(offerpromo,itemId,isPMIntegrated,callback);
						}

						else
						{
							callback.OnFBOfferPromoCallback(response, "");
						}
					}
				});

			}


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

	public interface ClypOfferPushCallback{
		public void onClypOfferPushCallback(JSONObject response, String error);
	}

	public interface FBOfferEventCallback {
		public void OnFBOfferEventCallback(JSONObject response, String error);
	}
	public interface FBRedeemedCallback {
		public void OnFBRedeemedCallback(JSONObject response, String error);
	}



	//	HashMap<String,String> getHeader(){
//		HashMap<String,String> header=new HashMap<String, String>();
//		header.put("Content-Type","application/json");
//		header.put("Application","mobilesdk");
//
//		//header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
//		//header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
//		//header.put("scope","READ");
//		header.put("tenantid","1173");
//		return header;
//	}

	HashMap<String,String> getHeader(){
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Application", "mobilesdk");
		header.put("client_id", FBConstant.client_id);
		header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
		header.put("tenantid", FBConstant.client_tenantid);
		return header;
	}


	HashMap<String,String> getHeaderwithsecurity(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application", FBConstant.client_Application);
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
		header.put("client_id", FBConstant.client_id);
		header.put("client_secret", FBConstant.client_secret);
		header.put("deviceId",FBUtility.getAndroidDeviceID(this.fbSdk.context));
		//header.put("scope","READ");
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("tenantName", FBConstant.client_tenantName);
		return header;
	}


}
