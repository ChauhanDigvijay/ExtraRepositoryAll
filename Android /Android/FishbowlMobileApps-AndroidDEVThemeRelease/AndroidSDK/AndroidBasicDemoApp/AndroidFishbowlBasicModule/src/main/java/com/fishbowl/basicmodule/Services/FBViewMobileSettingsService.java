package com.fishbowl.basicmodule.Services;
/**
 * Created by digvijay(dj) //hbh
 */

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class FBViewMobileSettingsService implements Serializable {

	public Field activeFields;
	public   String  companyLogoImageUrl;
	public   String  companyName;
	public   String  loginHeaderImageUrl;
	public   String  loginLeftSideImageUrl;
	public   String  loginRightTopImageUrl;
	public   String  loginFooterImageUrl;
	public   String  loginBackgroundImageUrl;
	public   String  webHeaderImageSourceUrl;
	public   String  signUpBackgroundImageUrl;
	public   String  checkInButtonColor;
	public   String  minRewardPoints;
	public   String  maxRewardPoints;
	public   JSONArray registrationArr;
	public   String  privacyContent;
	public   String  termsAndConditions;
	public   String  webFooterImageSourceUrl;
	public   String  themeFontName;
	public   String  themeFontUrl;
	public   String  themeFontSize;
	public   String  themeFontColor;
	public   String  Country;
	public   String  faq;
	public String bonus;
	public String programDetail;
	public  String passwordEnable;
	public String signUpPageTagLine;


	//Constant

	public static  String COMPANY_LOGO_IMAGE_URL= "companyLogoImageUrl";
	public static  String COMPANY_NAME= "companyName";
	public static  String LOGIN_HEADER_IMAGE_URL= "loginHeaderImageUrl";
	public static  String LOGIN_LEFT_SIDE_IMAGE_URL= "loginLeftSideImageUrl";
	public static  String LOGIN_RIGHT_TOP_IMAGE_URL= "loginRightTopImageUrl";
	public static  String LOGIN_FOOTER_IMAGE_IMAGE_URL= "loginFooterImageUrl";
	public static  String LOGIN_BACKGROUND_IMAGE_URL= "loginBackgroundImageUrl";
	public static  String CHECK_IN_BUTTON_COLOR= "checkInButtonColor";
	public static  String MIN_REWARDS_POINTS= "minRewardPoints";
	public static  String MAX_REWARDS_POINTS= "maxRewardPoints";
	public static  String REGISTRATION= "registrationFields";
	public static  String PRIVACY_CONTENT= "privacyContentUrl";
	public static  String TERM_AND_CONDITION= "termsAndConditionsUrl";
	public static  String SIGNUP_BACKGROUND_IMAGE_URL= "signUpBackgroundImageUrl";
	public static String  WEBHEADER_IMAGE_SOURCE_URL  = "webHeaderImageSourceUrl";
	public static String  WEBFOOTER_IMAGE_SOURCE_URL  = "webFooterImageSourceUrl";
	public static String  THEME_FONT_NAME  = "themeFontName";
	public static String  THEME_FONT_URL  = "themeFontUrl";
	public static String  THEME_FONT_SIZE  = "themeFontSize";
	public static String  THEME_FONT_COLOR  = "themeFontColor";
	public static String  COUNTRY  = "Country";
	public static String  FAQ  = "faq";
	public static String  BONUS = "bonus";
	public static String PROGRAM_DETAIL = "programDetail";
	public static String  PASSWORD_ENABLE  = "passwordEnable";
	public static  String SIGNUPPAGETAGLINE= "signUpPageTagLine";



	public static FBViewMobileSettingsService instance ;
	private FBSdk fbSdk;
	public static FBViewMobileSettingsService sharedInstance(){
		if(instance==null){
			instance=new FBViewMobileSettingsService();
		}
		return instance;
	}

	public void init(FBSdk _fbsdk){

		fbSdk =_fbsdk;

	}

	public void initFromJson(JSONObject jsonObj){

		try {
			if (jsonObj.has(COMPANY_LOGO_IMAGE_URL) && !jsonObj.isNull(COMPANY_LOGO_IMAGE_URL)) {
				companyLogoImageUrl=jsonObj.getString(COMPANY_LOGO_IMAGE_URL);
			}
			if (jsonObj.has(PASSWORD_ENABLE) && !jsonObj.isNull(PASSWORD_ENABLE)) {
				passwordEnable=jsonObj.getString(PASSWORD_ENABLE);
			}
			if (jsonObj.has(COMPANY_NAME) && !jsonObj.isNull(COMPANY_NAME)) {
				companyName=jsonObj.getString(COMPANY_NAME);
			}
			if (jsonObj.has(LOGIN_HEADER_IMAGE_URL) && !jsonObj.isNull(LOGIN_HEADER_IMAGE_URL)) {
				loginHeaderImageUrl=jsonObj.getString(LOGIN_HEADER_IMAGE_URL);
			}
			if (jsonObj.has(LOGIN_LEFT_SIDE_IMAGE_URL) && !jsonObj.isNull(LOGIN_LEFT_SIDE_IMAGE_URL)) {
				loginLeftSideImageUrl=jsonObj.getString(LOGIN_LEFT_SIDE_IMAGE_URL);
			}

			if (jsonObj.has(LOGIN_RIGHT_TOP_IMAGE_URL) && !jsonObj.isNull(LOGIN_RIGHT_TOP_IMAGE_URL)) {
				loginRightTopImageUrl=jsonObj.getString(LOGIN_RIGHT_TOP_IMAGE_URL);
			}
			if (jsonObj.has(LOGIN_FOOTER_IMAGE_IMAGE_URL) && !jsonObj.isNull(LOGIN_FOOTER_IMAGE_IMAGE_URL)) {
				loginFooterImageUrl=jsonObj.getString(LOGIN_FOOTER_IMAGE_IMAGE_URL);
			}
			if (jsonObj.has(MAX_REWARDS_POINTS) && !jsonObj.isNull(MAX_REWARDS_POINTS)) {
				maxRewardPoints=jsonObj.getString(MAX_REWARDS_POINTS);
			}

			if (jsonObj.has(LOGIN_BACKGROUND_IMAGE_URL) && !jsonObj.isNull(LOGIN_BACKGROUND_IMAGE_URL)) {
				loginBackgroundImageUrl=jsonObj.getString(LOGIN_BACKGROUND_IMAGE_URL);
			}

			if (jsonObj.has(LOGIN_BACKGROUND_IMAGE_URL) && !jsonObj.isNull(LOGIN_BACKGROUND_IMAGE_URL)) {
				loginBackgroundImageUrl=jsonObj.getString(LOGIN_BACKGROUND_IMAGE_URL);
			}

			if (jsonObj.has(CHECK_IN_BUTTON_COLOR) && !jsonObj.isNull(CHECK_IN_BUTTON_COLOR)) {
				checkInButtonColor=jsonObj.getString(CHECK_IN_BUTTON_COLOR);
			}

			if (jsonObj.has(MIN_REWARDS_POINTS) && !jsonObj.isNull(MIN_REWARDS_POINTS)) {
				minRewardPoints=jsonObj.getString(MIN_REWARDS_POINTS);
			}

			if (jsonObj.has(REGISTRATION) && !jsonObj.isNull(REGISTRATION)) {
				registrationArr=new JSONArray(jsonObj.getString(REGISTRATION));

				activeFields= new Field();
				activeFields.initFromJson(registrationArr);

			}

			if (jsonObj.has(PRIVACY_CONTENT) && !jsonObj.isNull(PRIVACY_CONTENT)) {
				privacyContent=jsonObj.getString(PRIVACY_CONTENT);
			}
			if (jsonObj.has(TERM_AND_CONDITION) && !jsonObj.isNull(TERM_AND_CONDITION)) {
				termsAndConditions=jsonObj.getString(TERM_AND_CONDITION);
			}

			if (jsonObj.has(SIGNUP_BACKGROUND_IMAGE_URL) && !jsonObj.isNull(SIGNUP_BACKGROUND_IMAGE_URL)) {
				signUpBackgroundImageUrl=jsonObj.getString(SIGNUP_BACKGROUND_IMAGE_URL);
			}

			if (jsonObj.has(WEBHEADER_IMAGE_SOURCE_URL) && !jsonObj.isNull(WEBHEADER_IMAGE_SOURCE_URL)) {
				webHeaderImageSourceUrl =jsonObj.getString(WEBHEADER_IMAGE_SOURCE_URL);
			}

			if (jsonObj.has(WEBFOOTER_IMAGE_SOURCE_URL) && !jsonObj.isNull(WEBFOOTER_IMAGE_SOURCE_URL)) {
				webFooterImageSourceUrl=jsonObj.getString(WEBFOOTER_IMAGE_SOURCE_URL);
			}
			if (jsonObj.has(THEME_FONT_NAME) && !jsonObj.isNull(THEME_FONT_NAME)) {
				themeFontName=jsonObj.getString(THEME_FONT_NAME);
			}
			if (jsonObj.has(THEME_FONT_URL) && !jsonObj.isNull(THEME_FONT_URL)) {
				themeFontUrl=jsonObj.getString(THEME_FONT_URL);
			}
			if (jsonObj.has(THEME_FONT_SIZE) && !jsonObj.isNull(THEME_FONT_SIZE)) {
				themeFontSize=jsonObj.getString(THEME_FONT_SIZE);
			}
			if (jsonObj.has(THEME_FONT_COLOR) && !jsonObj.isNull(THEME_FONT_COLOR)) {
				themeFontColor=jsonObj.getString(THEME_FONT_COLOR);

			}
			if (jsonObj.has(COUNTRY) && !jsonObj.isNull(COUNTRY)) {
				Country=jsonObj.getString(COUNTRY);

			}

			if (jsonObj.has(FAQ) && !jsonObj.isNull(FAQ)) {
				faq=jsonObj.getString(FAQ);
			}
			if (jsonObj.has(BONUS) && !jsonObj.isNull(BONUS)){
				bonus = jsonObj.getString(BONUS);
			}
			if (jsonObj.has(PROGRAM_DETAIL) && !jsonObj.isNull(PROGRAM_DETAIL)) {
				programDetail=jsonObj.getString(PROGRAM_DETAIL);
			}
			if (jsonObj.has(SIGNUPPAGETAGLINE) && !jsonObj.isNull(SIGNUPPAGETAGLINE)) {
				signUpPageTagLine=jsonObj.getString(SIGNUPPAGETAGLINE);
			}



		}catch (Exception e){
			e.printStackTrace();
		}}


	public void getViewSettings(JSONObject parameter,final FBViewSettingsCallback callback) {

		if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
			return;
		}
		FBService.getInstance().get(FBConstant.FBViewSettingsApi, null , getHeader3(), new FBServiceCallback(){

			public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
				if(error==null&&response!=null){
					FBViewMobileSettingsService.sharedInstance().initFromJson(response);
					callback.onViewSettingsCallback(response,null);
				}else{
					callback.onViewSettingsCallback(null,error);
				}
			}

		});
	}

	//Login
	HashMap<String,String> getHeader3(){
		HashMap<String,String> header=new HashMap<String, String>();
		header.put("Content-Type","application/json");
		header.put("Application","mobile");
		header.put("client_id", FBConstant.client_id);
		header.put("tenantid", FBConstant.client_tenantid);
		header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
		return header;
	}

	public interface FBViewSettingsCallback{
		public void onViewSettingsCallback(JSONObject response, Exception error);
	}

}
