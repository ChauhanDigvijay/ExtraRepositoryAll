package com.fishbowl.loyaltymodule.Utils;

//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class FBConstant {


	//GENERAL CONSTANT
	public static String DEVICE_TYPE = "Android";
	public static String device_os_ver = DEVICE_TYPE.concat(" ").concat( android.os.Build.VERSION.RELEASE);

	//CLP EVENT API
	public static String ClpAnalyticApi ="mobile/submitallappevents";

	public static String MobileSettingApi = "mobile/settings/getmobilesettings?customerID=";

	public static String StoreApi = "mobile/stores/getstores";// QA

	public static String StoreSearchApi = "store/searchStores";// QA


	public static String CustomerRegisterApi ="mobile/appcustomer";

	public static String BeaconApi =  "mobile/beacons/";

	public static String BeconInRange = "mobile/beaconsinrange";

	public static String LocationChangeApi = "mobile/locationchanged";


	public static String DeviceUpdateApi = "member/deviceUpdate";

	//////Authenticate UI APP API
	public static String FBLoginApi ="member/login";
	public static String FBCreateMemberApi ="member/create";
	public static String FBViewSettingsApi ="loyalty/viewLoyaltySettings";
	public static String FBGetMemberApi ="member/getMember";
	public static String FBMemberUpdateApi ="member/update";
	public static String FBChangePasswordApi ="member/changePassword";
	public static String FBforgetPasswordApi ="member/forgetPassMail";
	public static String FBLogoutApi ="member/logout";
	public static String FBContactUsApi="loyalty/messages";
	public static String FBLoyaltyAreaType = "loyalty/getLoyaltyAreaType";
	public static String FBLoyaltyMessageType = "loyalty/getLoyaltyMessageType";
	public static String FBLoyaltyMessage = "loyalty/getLoyaltyMessages";
	public static String FBLoyaltyMessageStatus = "loyalty/setMessageStatus";
	public static String  gcm_sender_id_dev="503777186107";
	public static String  gcm_sender_id_pro="636426125388";
	public static String FBGetActivityApi ="loyalty/getActivity";

	public static String FBMemberLoyaltyCard ="loyaltyCard/getMemberLoyaltyCard";

//qa stuff
//	FBConfig.setClient_secret("C65A0DC0F28C469FB7376F972DEFBCB7");
//	FBConfig.setClient_tenantid("1173");
//	FBConfig.setClient_id("201969E1BFD242E189FE7B6297B1B5A6");

//	public static String client_id ="201969E1BFD242E189FE7B6297B1B590"; //for stgseaisland
	public static String client_id ="201969E1BFD242E189FE7B6297B1B5B2";//selisland - stg
//public static String client_id ="201969E1BFD242E189FE7B6297B1B5A6"; //for finaldemo

	//public static String client_id ="201969E1BFD242E189FE7B6297B1B5A6";

	//public static String client_secret ="C65A0DC0F28C469FB7376F972DEFBC90"; //for stgseaisland
	public static String client_secret ="C65A0DC0F28C469FB7376F972DEFBCA2"; //selisland - stg
//public static String client_secret ="C65A0DC0F28C469FB7376F972DEFBCB7"; //for finaldemo

	//public static String client_secret ="201969E1BFD242E189FE7B6297B1B5A6";

	//public static String  client_secret="C65A0DC0F28C469FB7376F972DEFBCB3";//for hbh stagging

	//public static String  client_secret="C65A0DC0F28C469FB7376F972DEFBCB8";//for hbh stagging

	public static String FBBonusApi="loyalty/signupRuleList";



	public static String  client_tenantid="1702";// for seaisland
	//public static String  client_tenantid="1703";

	public static String  client_tenantidforstagging="581";
	public static String  client_tenantName="fishbowl";
	public static String  client_Application="mobilesdk";
	public static String FBCountry ="states/getAllCountry";
	public static String FBStates ="states";

	public static String FBGetToken = "mobile/getToken";
	public static String FBSendPassEmail ="states";

	public static String  guest_username="testertesting@gmail.com";
	public static String  guest_password="123456";

	public final static String B_URL = "B_URL";
	public final static String B_TITLE = "B_TITLE";

	public static int   SERVER_ERROR_INVALID_ACCESS_TOKEN=400;



}
