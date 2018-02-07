package com.fishbowl.basicmodule.Services;


import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by digvijay(dj)
 */
public class FBCustomerService {
	String TAG = "FBCustomerService";
	private static FBSdk fbSdk;
	public static FBCustomerService instance;

	public static FBCustomerService sharedInstance() {
		if (instance == null) {
			instance = new FBCustomerService();
		}
		return instance;
	}

	public void init(FBSdk _fbsdk) {
		fbSdk = _fbsdk;
	}


	public void registerGuest(final FBGuestRegisterCallback callback) {
		final FBSdkData FBSdkData = this.fbSdk.getFBSdkData();
		if (!FBUtility.isNetworkAvailable(this.fbSdk.context)) {
			return;
		}

		FBCustomerItem customer = new FBCustomerItem();
		customer.deviceId = FBUtility.getAndroidDeviceID(this.fbSdk.context);
		customer.deviceType = FBConstant.DEVICE_TYPE;// assign android device id for
		customer.deviceOsVersion = FBUtility.getAndroidOs();
		customer.loginPassword = "password";
		// Validate Guest User and Pass required parameters
		if (customer.firstName == null || customer.firstName.equalsIgnoreCase("")) {
			customer.firstName = "Guest";
			customer.lastName = "";
			customer.loginID = customer.deviceId;
			customer.homeStore = "0";
			customer.pushOpted = "true";//"Y";
			customer.smsOpted = "false";//"N";
			customer.emailOpted = "false";//"N";
			customer.phoneOpted = "false";//"N";
			customer.adOpted = "false";//"N";
		}

		customer.pushToken = this.fbSdk.mPushToken;
		if (FBUtility.isLocationServiceProviderAvailable(this.fbSdk.context)) {
			customer.locationEnabled = "true";//"Y";
		} else {
			customer.locationEnabled = "false";//"N";
		}

		Gson gson = new Gson();
		String json = gson.toJson(customer);
		//send this json as parameter
		Log.d("", json);
		//entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
		FBService.getInstance().post(FBConstant.CustomerRegisterApi, json, getHeaderwithsecurity(), new FBServiceCallback() {
			public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
				if (error == null && response != null) {

					Log.d(TAG, response.toString());

					try {
						if (response.has("successFlag") && response.getBoolean("successFlag") == false) {
							return;
						}
						FBSdkData.currCustomer = new FBCustomerItem();
						FBSdkData.currCustomer.tenantid = response.has("tenantid") ? Long.parseLong(response.getString("tenantid")) : 0;
						FBSdkData.currCustomer.memberid = response.has("memberid") ? Long.parseLong(response.getString("memberid")) : 0;
						FBSdkData.currCustomer.firstName = response.has("firstName") ? response.getString("firstName") : "";
						FBSdkData.currCustomer.lastName = response.has("lastName") ? response.getString("lastName") : "";
						FBSdkData.currCustomer.emailID = response.has("emailID") ? response.getString("emailID") : "";
						FBSdkData.currCustomer.loginID = response.has("loginID") ? response.getString("loginID") : "";
						FBSdkData.currCustomer.cellPhone = response.has("cellPhone") ? response.getString("cellPhone") : "";
						FBSdkData.currCustomer.pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
						FBSdkData.currCustomer.loginPassword = response.has("loginPassword") ? response.getString("loginPassword") : "";

						// Send callback to App
						//Log.d("fbSdk save cust persis :",FBSdkData.currCustomer.toString());
						FBSdkData.setCustomer(FBSdkData.currCustomer);
						callback.onFBGuestRegisterCallback(response, null);

					} catch (Exception e) {
						e.printStackTrace();
					}

					Log.d("Customer registration", "SUCCESS + " + response + "");
				} else {
					callback.onFBGuestRegisterCallback(null, error.getLocalizedMessage());
				}
			}
		});
	}


	public void registerCustomer(final FBCustomerItem customer, final FBSdk.OnFBSdkRegisterListener sdkRegListener, final FBCustomerRegisterCallback callback) {

		final FBSdkData FBSdkData = this.fbSdk.getFBSdkData();
		//if guest is register already return
		if (FBPreferences.sharedInstance(this.fbSdk.context).getGuestRegister()) {
			return;
		}
		if (!FBUtility.isNetworkAvailable(this.fbSdk.context)) {
			return;
		}
		if (sdkRegListener == null) {
			return;
		}

		customer.deviceId = FBUtility.getAndroidDeviceID(this.fbSdk.context);
		customer.deviceType = FBConstant.DEVICE_TYPE;// assign android device id for
		customer.deviceOsVersion = FBUtility.getAndroidOs();
		customer.loginPassword = "password";
		// Validate Guest User and Pass required parameters
		if (customer.firstName == null || customer.firstName.equalsIgnoreCase("")) {
			customer.firstName = "Guest";
			customer.lastName = "";
			customer.loginID = "";//customer.deviceId;
			customer.homeStore = "0";
			customer.pushOpted = "true";//"Y";
			customer.smsOpted = "false";//"N";
			//	customer.emailOpted = "false";//"N";
			customer.phoneOpted = "false";//"N";
			customer.adOpted = "false";//"N";

		}
		customer.appid = "com.olo.jambajuiceapp";
		customer.pushToken = this.fbSdk.mPushToken;
		if (FBUtility.isLocationServiceProviderAvailable(this.fbSdk.context)) {
			customer.locationEnabled = "true";//"Y";
		} else {
			customer.locationEnabled = "false";//"N";
		}


		Gson gson = new Gson();
		String json = gson.toJson(customer);
		//entity = new StringEntity(json);
		//send this json as parameter
		//Log.d("", json);
		//entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
		FBService.getInstance().post(FBConstant.CustomerRegisterApi, json, getHeaderwithsecurity(), new FBServiceCallback() {
			public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
				if (error == null && response != null) {
					Log.d(TAG, response.toString());
					try {
						if (response.has("successFlag") && response.getBoolean("successFlag") == false) {
							sdkRegListener.onFBRegistrationError(response.getString("message"));
							return;
						}

						FBSdkData.currCustomer = new FBCustomerItem();
						FBSdkData.currCustomer.tenantid = response.has("tenantid") ? Long.parseLong(response.getString("tenantid")) : 0;
						FBSdkData.currCustomer.memberid = response.has("memberid") ? Long.parseLong(response.getString("memberid")) : 0;
						FBSdkData.currCustomer.firstName = response.has("firstName") ? response.getString("firstName") : "";
						FBSdkData.currCustomer.lastName = response.has("lastName") ? response.getString("lastName") : "";
						FBSdkData.currCustomer.emailID = response.has("emailID") ? response.getString("emailID") : "";
						FBSdkData.currCustomer.loginID = response.has("loginID") ? response.getString("loginID") : "";
						FBSdkData.currCustomer.cellPhone = response.has("cellPhone") ? response.getString("cellPhone") : "";
						FBSdkData.currCustomer.pushToken = response.has("pushToken") ? response.getString("pushToken") : "";

						// Send callback to App
					//	sdkRegListener.onFBRegistrationSuccess(FBSdkData.currCustomer);
						//Log.d("fbSdk save cust persis :",FBSdkData.currCustomer.toString());
						FBSdkData.setCustomer(FBSdkData.currCustomer);

						FBPreferences.sharedInstance(fbSdk.context).setGuestRegister(true);
						FBPreferences.sharedInstance(fbSdk.context).setGuestMemberId(FBSdkData.currCustomer.memberid);

					} catch (Exception e) {
						e.printStackTrace();
					}

					Log.d("Customer registration", "SUCCESS + " + response + "");
				}
//				else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//				{
//
//					FBTokenService.sharedInstance(fbSdk).getTokenRegisterCustomer(customer, sdkRegListener);
//				}
				else {
					callback.OnFBCustomerRegisterCallback(null, error.getLocalizedMessage());
					sdkRegListener.onFBRegistrationError(error.getLocalizedMessage());
				}
			}
		});
	}

	public void saveCustomer(FBCustomerItem customer, final FBSdk.OnFBCustomerRegisterListener custRegListener, final FBSaveCustomerCallback callback) {
		final FBSdkData FBSdkData = this.fbSdk.getFBSdkData();
		if (!FBUtility.isNetworkAvailable(this.fbSdk.context)) {
			return;
		}
		if (custRegListener == null) {
			return;
		}
		if (FBSdkData.currCustomer != null)
			customer.memberid = FBSdkData.currCustomer.memberid;
		customer.appid = "com.olo.jambajuiceapp";
		customer.deviceId = FBUtility.getAndroidDeviceID(this.fbSdk.context);
		customer.deviceType = FBConstant.DEVICE_TYPE;// assign android device id for
		customer.deviceOsVersion = FBUtility.getAndroidOs();
		customer.loginPassword = "password";

		if (FBUtility.isLocationServiceProviderAvailable(this.fbSdk.context)) {
			customer.locationEnabled = "true";//"Y";
		} else {
			customer.locationEnabled = "false";//"N";
		}
		// Validate Guest User and Pass required parameters
		if (customer.firstName == null || customer.firstName.equalsIgnoreCase("")) {
			customer.firstName = "Guest";
			customer.lastName = "";
			customer.loginID = customer.deviceId;
			customer.homeStore = "0";
			customer.pushOpted = "true";//"Y";
			customer.smsOpted = "false";//"N";
			//customer.emailOpted = "false";//"N";
			customer.phoneOpted = "false";//"N";
			customer.adOpted = "false";//"N";
		}

		customer.pushToken = this.fbSdk.mPushToken;
		if (FBUtility.isLocationServiceProviderAvailable(this.fbSdk.context)) {
			customer.locationEnabled = "true";//"Y";
		} else {
			customer.locationEnabled = "false";//"N";
		}

		Gson gson = new Gson();
		createMember(customer, custRegListener);
	}

	public void createMember(final FBCustomerItem customer, final FBSdk.OnFBCustomerRegisterListener custRegListener) {
		final FBSdkData FBSdkData = fbSdk.getFBSdkData();
		JSONObject object = new JSONObject();
		try {
			//	object.put("memberid", customer.memberid);
			object.put("firstName", customer.getFirstName());
			object.put("lastName", customer.getLastName());
			object.put("email", customer.getEmailID());
			//object.put("emailOptIn", customer.getEmailOpted());
			object.put("phone", customer.getCellPhone());
			object.put("smsOptIn", "false");
			object.put("addressStreet", customer.getAddressLine1());
			object.put("addressCity", customer.addressCity);
			object.put("addressZipCode", customer.getAddressZip());
			object.put("favoriteStore", customer.homeStore);// change jjdkksdlfsldjflsd kfjsdlknfl
			object.put("dob", customer.getDateOfBirth());
			object.put("gender", customer.getCustomerGender());
			object.put("username", "");
			//	object.put("password", customer.getLoginPassword());
			object.put("deviceId", customer.getDeviceID());
			object.put("sendWelcomeEmail", "ss");
			object.put("loginID", customer.getLoginID());
			object.put("storeCode", customer.homeStore);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
			return;
		}

		String str = object.toString();

		FBService.getInstance().post(FBConstant.FBCreateMemberApi, str, getHeaderwithsecurity(), new FBServiceCallback() {

			public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


				if (response != null) {
					try {

						FBSdkData.currCustomer = new FBCustomerItem();
						FBSdkData.currCustomer.tenantid = response.has("tenantid") ? Long.parseLong(response.getString("tenantid")) : 0;
						FBSdkData.currCustomer.memberid = response.has("memberid") ? Long.parseLong(response.getString("memberid")) : 0;
						FBSdkData.currCustomer.firstName = response.has("firstName") ? response.getString("firstName") : "";
						FBSdkData.currCustomer.lastName = response.has("lastName") ? response.getString("lastName") : "";
						FBSdkData.currCustomer.emailID = response.has("emailID") ? response.getString("emailID") : "";
						FBSdkData.currCustomer.loginID = response.has("loginID") ? response.getString("loginID") : "";
						FBSdkData.currCustomer.cellPhone = response.has("cellPhone") ? response.getString("cellPhone") : "";
						String pushToken = response.has("pushToken") ? response.getString("pushToken") : "";
						String deviceId = response.has("deviceId") ? response.getString("deviceId") : "";


						FBSdkData.setCustomer(FBSdkData.currCustomer);
						if (StringUtilities.isValidString(pushToken) && StringUtilities.isValidString(deviceId)) {
							FBPreferences.sharedInstance(fbSdk.context).setDeviceId(deviceId);
							FBPreferences.sharedInstance(fbSdk.context).setPushToken(pushToken);
							//FBPreferences.sharedInstance(fbSdk.context).setUserMemberId(FBSdkData.currCustomer.memberid);
						}

						FBPreferences.sharedInstance(fbSdk.context).setUserMemberId(FBSdkData.currCustomer.memberid);

						if ((!StringUtilities.isValidString(pushToken)) || !StringUtilities.isValidString(deviceId)) {
							deviceUpdate(customer);
						}

						if (!pushToken.equals(customer.pushToken) && !deviceId.equals(customer.deviceId)) {
							deviceUpdate(customer);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//				} else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//				{
//					FBTokenService.sharedInstance(fbSdk).getTokenSaveCustomer(customer, custRegListener);
//				}

			}

		});


	}


	public void deviceUpdate(final FBCustomerItem customer) {
		final FBSdkData FBSdkData = this.fbSdk.getFBSdkData();
		JSONObject object = new JSONObject();
		try {

			object.put("memberid", FBPreferences.sharedInstance(fbSdk.context).getUserMemberId());
			object.put("deviceId", customer.getDeviceID());
			object.put("deviceOsVersion", customer.deviceOsVersion);
			object.put("deviceType", customer.deviceType);
			object.put("pushToken", customer.pushToken);
			object.put("appId", customer.appid);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
			return;
		}
		String str = object.toString();
		FBService.getInstance().post(FBConstant.DeviceUpdateApi, str, getHeaderwithsecurity(), new FBServiceCallback() {

			public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {


				if (response != null) {
					try {


					} catch (Exception e) {
						e.printStackTrace();
					}


				}
//				else if((FBUtility.getErrorDescription(error).equalsIgnoreCase( FBConstant.SERVER_ERROR_INVALID_ACCESS_TOKEN)))
//				{
//					FBTokenService.sharedInstance(fbSdk).getTokendeviceUpdate(customer);
//				}


			}
		});
	}


	HashMap<String, String> getHeaderwithsecurityforerror() {
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Application", "mobilesdk");
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
		header.put("client_id", FBConstant.client_id);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("tenantName", "fishbowl");
		return header;
	}



	//dj
	HashMap<String, String> getHeaderwithsecurity() {
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Application", FBConstant.client_Application);
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
		header.put("client_id", FBConstant.client_id);
		header.put("client_secret", FBConstant.client_secret);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		//header.put("scope","READ");
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("tenantName", FBConstant.client_tenantName);
		return header;
	}


	HashMap<String, String> getHeaderwithsecurityforcreate() {
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Application", FBConstant.client_Application);
		header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
		header.put("client_id", FBConstant.client_id);
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		return header;
	}


	//Login
	HashMap<String,String> getHeader2(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application","mobile");
		header.put("client_id", FBConstant.client_id);
		header.put("client_secret", FBConstant.client_secret);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		header.put("response_type","token");
		header.put("scope","read");
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("state","stateless");
		return header;
	}


	//Create Member
	HashMap<String,String> getHeader1(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application","mobile");
		header.put("client_id", FBConstant.client_id);
		header.put("client_secret", FBConstant.client_secret);
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("deviceId", FBUtility.getAndroidDeviceID(this.fbSdk.context));
		return header;
	}

	//Interface for
	public interface FBGuestRegisterCallback {
		public void onFBGuestRegisterCallback(JSONObject response, String error);
	}

	public interface FBCustomerRegisterCallback {
		public void OnFBCustomerRegisterCallback(JSONObject response, String error);
	}

	public interface FBSaveCustomerCallback {
		public void OnFBSaveCustomerCallback(JSONObject response, String error);
	}

}
