package com.fishbowl.basicmodule.Utils;

import com.fishbowl.basicmodule.Models.FBConfig;

/**
 * Created by digvijay(dj)
 */
public class FBConstant {


	//GENERAL CONSTANT
	public static String DEVICE_TYPE = "Android";
	//public static String device_os_ver = DEVICE_TYPE.concat(" ").concat( android.os.Build.VERSION.RELEASE);
	public static String device_os_ver =  android.os.Build.VERSION.RELEASE;
	//CLP EVENT API
	public static String FBAnalyticApi ="event/submitallappevents";
	public static String FBGuestAnalyticApi ="event/guestappevents";
	public static String MobileSettingApi = "mobile/settings/getmobilesettings?customerID=";
	public static String StoreApi = "mobile/stores/getstores";// QA
	public static String StoreSearchApi = "store/searchStores";// QA
	public static String CustomerRegisterApi ="mobile/appcustomer";
	public static String BeaconApi =  "mobile/beacons/";
	public static String BeconInRange = "mobile/beaconsinrange";
	public static String LocationChangeApi = "mobile/locationchanged";
	public static String DeviceUpdateApi = "member/deviceUpdate";
	public static String DeviceUpdateApiForJamba = "member/deviceUpdate";
	//////Authenticate UI APP API
	public static String FBLoginApi ="member/login";
	public static String FBLoginApiForJamba="member/loginSDK";
	public static String FBCreateMemberApi ="member/create";
	public static String FBViewSettingsApi ="loyalty/viewLoyaltySettings";
	public static String FBGetMemberApi ="member/getMember";
	public static String FBGetMemberApiForJamba ="member/getMember";
	public static String FBMemberUpdateApi ="member/update";
	public static String FBChangePasswordApi ="member/changePassword";
	public static String FBforgetPasswordApi ="member/forgetPassMail";
	public static String FBLogoutApi ="member/logout";
	public static String  gcm_sender_id_dev="503777186107";
	public static String  gcm_sender_id_pro="636426125388";
	public static String FBMenuCategory = "menu/category";
	public static String FBMenuSubCategory="menu/subCategory";
	public static String FBMenuCategorySubCategory="=menu/categorySubCategory";
	public static String FBMenuDrawer = "menu/menuDrawer";
	public static String FBMenuProduct = "menu/product";
	public static String FBMenuCategoryProductList = "menu/categoryProductList";
	public static String FBGetFamilyApi = "menu/family";
	public static String FBCountry ="states/getAllCountry";
	public static String FBStates ="states";
	public static String FBContactUsApi="loyalty/messages";
	public static String FBLoyaltyAreaType = "loyalty/getLoyaltyAreaType";
	public static String FBLoyaltyMessageType = "loyalty/getLoyaltyMessageType";
	public static String FBBonusApi="loyalty/signupRuleList";
	public static String FBUseOffer = "loyalty/useOffer";
	public static String FBGetAllRewardOffer="loyaltyw/getallrewardoffer";
	public static String FBMStoreUpdateApi ="member/updateStore";
	public static String FBGetToken = "mobile/getToken";
	public static String FBLoyaltyMessage = "loyalty/getLoyaltyMessages";
	public static String FBLoyaltyMessageStatus = "loyalty/setMessageStatus";
	public static String  client_id= FBConfig.getClient_id();
	public static String  client_secret= FBConfig.getClient_secret();
	public static String  client_tenantid=FBConfig.getClient_tenantid();
	public static String  client_tenantName="fishbowl";
	public static String  client_Application="mobilesdk";
	public static String  guest_username="testertesting@gmail.com";
	public static String  guest_password="123456";
	public static String   SERVER_ERROR_INVALID_ACCESS_TOKEN="Invalid access token";
	public static String FBGuestUser = "event/guestRegister";
	public static String FBGetActivityApi ="loyalty/getActivity";
	public static String FBMemberLoyaltyCard ="loyaltyCard/getMemberLoyaltyCard";
	public static String FBThemeSettingsApi="theme/getDefaultThemeForAllPages";

}
