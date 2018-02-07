package com.fishbowl.basicmodule.Models;

import com.google.android.gms.location.Geofence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by digvijay(dj)
 */
public class FBMobileSettingsItem implements Serializable {
	static final String TEAM_IDENTIFIER = "TEAM_IDENTIFIER";
	static final String ORGANIZATION_NAME = "ORGANIZATION_NAME";
	static final String Test11 = "Test11";
	static final String BRAND_ID = "BRAND_ID";
	static final String PASS_IDENTIFIER = "PASS_IDENTIFIER";
	static final String CONFIG_SHORTCODE = "CONFIG_SHORTCODE";
	static final String FISHBOWL_PM_API_OAUTH_CLIENT_SECRET = "FISHBOWL_PM_API_OAUTH_CLIENT_SECRET";
	static final String WORKFLOW_AUTO_APPROVE = "WORKFLOW_AUTO_APPROVE";
	static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";
	static final String CONFIG_VENDOR = "CONFIG_VENDOR";
	static final String PROMO_CODE_FORMAT = "PROMO_CODE_FORMAT";
	static final String WORKFLOW_ENABLED = "WORKFLOW_ENABLED";
	static final String OPTOUT_TINY_URL = "OPTOUT_TINY_URL";
	static final String SMSREVIEWAUTOAPPROVAL = "SMSREVIEWAUTOAPPROVAL";
	static final String TWILIO_PHONE_NO = "TWILIO_PHONE_NO";
	static final String SMSREVIEWAUTOREJECTION = "SMSREVIEWAUTOREJECTION";
	static final String MAX_SEND_COUNT = "MAX_SEND_COUNT";
	static final String FISHBOWL_PM_API_OAUTH_PASSWORD = "FISHBOWL_PM_API_OAUTH_PASSWORD";
	static final String AWS_KEY_ID = "AWS_KEY_ID";
	static final String OPEN_TINY_URL = "OPEN_TINY_URL";
	static final String FISHBOWL_PM_API_OAUTH_USER = "FISHBOWL_PM_API_OAUTH_USER";
	static final String TWILIO_SID = "TWILIO_SID";
	static final String FISHBOWL_PM_API_OAUTH_CLIENT_ID = "FISHBOWL_PM_API_OAUTH_CLIENT_ID";
	static final String TWILIO_AUTH_TOKEN = "TWILIO_AUTH_TOKEN";
	static final String FISHBOWL_PM_API_OAUTH_SITE = "FISHBOWL_PM_API_OAUTH_SITE";
	//Old Mobile Setting Field Remove them
	private static final long serialVersionUID = 8138136422103486837L;
	public long DISTANCE_FILTER = 50;// in meters
	public float DISTANCE_STORE = 10000.0f;// in meters
	public float GEOFENCE_RADIUS = 750.0f;// in meters
	public float GEOFENCE_MIN_RADIUS = 150f;// in meters
	public long STORE_REFRESH_TIME = 900;// in seconds
	public int MAX_STORE_COUNT_ANDROID = 70;
	public int MAX_BEACON_COUNT = 15;
	public int ENABLE_LOCAL_NOTIFICATION = 0;// 1- yes 0- No
	public long OUT_SIDE_UPDATE_INTERVAL = 500;// in seconds
	public long OUT_SIDE_FASTEST_UPDATE_INTERVAL = 300;//in seconds
	public long IN_SIDE_UPDATE_INTERVAL = 60;//in seconds
	public long IN_SIDE_FASTEST_UPDATE_INTERVAL = 30;//in seconds
	public long IN_STORE_UPDATE_INTERVAL = 300;//in seconds
	public long GEOFENCE_CHECK_FREQUENCY = 1800;//in seconds
	public long LOCATION_UPDATE_PING_FREQUENCY = 15;
	public long BEACON_PING_FREQUENCY = 30;//in seconds
	public long GEOFENCE_EXPIRY_TIME = Geofence.NEVER_EXPIRE;//
	public long IN_REGION_SLAB_TIME = 500;//in seconds
	public long LOCATION_SERVICE_REFRESH_TIME = 900;
	public long GEOFENCE_CHECK_DISTANCE_MOVED = 300;//in meters
	public long BEACON_SLAB_TIME = 2500;//in sec
	public int TRIGGER_APP_EVENTS = 1;// 1 on / 0 off
	public int TRIGGER_GEOFENCE = 1;//1 on / 0 off
	public int TRIGGER_BEACON = 1;//1 on / 0 off
	public int TRIGGER_ERROR_EVENT = 1;//1 on / 0 off
	public int TRIGGER_PUSH_NOTIFICATION = 1;//1 on / 0 off
	public int EVENT_QUEUE_SIZE_THRESHOLD = 10;
	public int TIMER_DELAY_IN_SECONDS = 5;
	public String VERSION = "";
	public int INAPPOFFER_ENABLED = 1;//1 on / 0 off
	public String INFORMATION ;
	public String FORCEUPDATE ;
	public String VERSIONCODE ;
	public int FREQUENCY = 2;
	//New Api
	HashMap<String, FBMobileSettingObjectItem> mobileSettingHashMap = new HashMap<String, FBMobileSettingObjectItem>();


	public FBMobileSettingsItem() {
	}


	public FBMobileSettingsItem(long dISTANCE_FILTER, float dISTANCE_STORE,
							   float gEOFENCE_RADIUS, float gEOFENCE_MIN_RADIUS,
							   long sTORE_REFRESH_TIME, int mAX_STORE_COUNT_ANDROID, int mAX_BEACON_COUNT,
							   int eNABLE_LOCAL_NOTIFICATION, long oUT_SIDE_UPDATE_INTERVAL,
							   long oUT_SIDE_FASTEST_UPDATE_INTERVAL,
							   long iN_SIDE_UPDATE_INTERVAL, long iN_SIDE_FASTEST_UPDATE_INTERVAL,
							   long gEOFENCE_CHECK_FREQUENCY, long lOCATION_UPDATE_PING_FREQUENCY,
							   long bEACON_PING_FREQUENCY, long gEOFENCE_EXPIRY_TIME,
							   long iN_REGION_SLAB_TIME, long lOCATION_SERVICE_REFRESH_TIME,
							   long iIN_STORE_UPDATE_INTERVAL, long gEOFENCE_CHECK_DISTANCE_MOVED, long bEACON_SLAB_TIME,
							   int tRIGGER_APP_EVENTS,
							   int tRIGGER_GEOFENCE,
							   int tRIGGER_BEACON,
							   int tRIGGER_ERROR_EVENT,
							   int tRIGGER_PUSH_NOTIFICATION,
							   int tRIGGER_INAPPOFFER_ENABLED,
							   int eVENT_QUEUE_SIZE_THRESHOLD,
							   int tIMER_DELAY_IN_SECONDS) {
		super();
		this.DISTANCE_FILTER = dISTANCE_FILTER;
		this.DISTANCE_STORE = dISTANCE_STORE;
		this.GEOFENCE_RADIUS = gEOFENCE_RADIUS;
		this.GEOFENCE_MIN_RADIUS = gEOFENCE_MIN_RADIUS;
		this.STORE_REFRESH_TIME = sTORE_REFRESH_TIME;
		this.MAX_STORE_COUNT_ANDROID = mAX_STORE_COUNT_ANDROID;
		this.MAX_BEACON_COUNT = mAX_BEACON_COUNT;
		this.ENABLE_LOCAL_NOTIFICATION = eNABLE_LOCAL_NOTIFICATION;
		this.OUT_SIDE_UPDATE_INTERVAL = oUT_SIDE_UPDATE_INTERVAL;
		this.OUT_SIDE_FASTEST_UPDATE_INTERVAL = oUT_SIDE_FASTEST_UPDATE_INTERVAL;
		this.IN_SIDE_UPDATE_INTERVAL = iN_SIDE_UPDATE_INTERVAL;
		this.IN_SIDE_FASTEST_UPDATE_INTERVAL = iN_SIDE_FASTEST_UPDATE_INTERVAL;
		this.GEOFENCE_CHECK_FREQUENCY = gEOFENCE_CHECK_FREQUENCY;
		this.LOCATION_UPDATE_PING_FREQUENCY = lOCATION_UPDATE_PING_FREQUENCY;
		this.BEACON_PING_FREQUENCY = bEACON_PING_FREQUENCY;
		this.GEOFENCE_EXPIRY_TIME = gEOFENCE_EXPIRY_TIME;
		this.IN_REGION_SLAB_TIME = iN_REGION_SLAB_TIME;
		this.LOCATION_SERVICE_REFRESH_TIME = lOCATION_SERVICE_REFRESH_TIME;
		this.IN_STORE_UPDATE_INTERVAL = iIN_STORE_UPDATE_INTERVAL;
		this.GEOFENCE_CHECK_DISTANCE_MOVED = gEOFENCE_CHECK_DISTANCE_MOVED;
		this.BEACON_SLAB_TIME = bEACON_SLAB_TIME;
		this.TRIGGER_APP_EVENTS = tRIGGER_APP_EVENTS;
		this.TRIGGER_BEACON = tRIGGER_BEACON;
		this.TRIGGER_ERROR_EVENT = tRIGGER_ERROR_EVENT;
		this.TRIGGER_GEOFENCE = tRIGGER_GEOFENCE;
		this.TRIGGER_PUSH_NOTIFICATION = tRIGGER_PUSH_NOTIFICATION;
		this.INAPPOFFER_ENABLED = tRIGGER_INAPPOFFER_ENABLED;
		this.TIMER_DELAY_IN_SECONDS = tIMER_DELAY_IN_SECONDS;
		this.EVENT_QUEUE_SIZE_THRESHOLD = eVENT_QUEUE_SIZE_THRESHOLD;

	}


	public String getVERSIONCODE() {
		return VERSIONCODE;
	}

	public void setVERSIONCODE(String VERSIONCODE) {
		this.VERSIONCODE = VERSIONCODE;
	}

	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

	public String getINFORMATION() {
		return INFORMATION;
	}

	public void setINFORMATION(String iNFORMATION) {
		INFORMATION = iNFORMATION;
	}

	public String getFORCEUPDATE() {
		return FORCEUPDATE;
	}

	public void setFORCEUPDATE(String fORCEUPDATE) {
		FORCEUPDATE = fORCEUPDATE;
	}

	public int getFREQUENCY() {
		return FREQUENCY;
	}

	public void setFREQUENCY(int fREQUENCY) {
		FREQUENCY = fREQUENCY;
	}

	public void initFromJson(JSONObject jsonObj) {
		try {

			// OLD API DATA
			JSONArray getArrayMobileSettings = jsonObj.getJSONArray("mobileSettings");

			for (int i = 0; i < getArrayMobileSettings.length(); i++) {

				JSONObject myMobileSettingObj = getArrayMobileSettings.getJSONObject(i);

				if (myMobileSettingObj.getString("settingName").equals(("DISTANCE_FILTER"))) {
					this.DISTANCE_FILTER = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("DISTANCE_STORE")) {
					this.DISTANCE_STORE = Float.parseFloat(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("GEOFENCE_RADIUS")) {
					this.GEOFENCE_RADIUS = Float.parseFloat(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("IN_REGION_SLAB_TIME")) {
					this.IN_REGION_SLAB_TIME = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("STORE_REFRESH_TIME")) {
					this.STORE_REFRESH_TIME = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("MAX_STORE_COUNT_ANDROID")) {
					this.MAX_STORE_COUNT_ANDROID = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("MAX_BEACON_COUNT")) {
					this.MAX_BEACON_COUNT = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("ENABLE_LOCAL_NOTIFICATION")) {
					this.ENABLE_LOCAL_NOTIFICATION = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("OUT_SIDE_UPDATE_INTERVAL")) {
					this.OUT_SIDE_UPDATE_INTERVAL = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("OUT_SIDE_FASTEST_UPDATE_INTERVAL")) {
					this.OUT_SIDE_FASTEST_UPDATE_INTERVAL = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("IN_SIDE_UPDATE_INTERVAL")) {
					this.IN_SIDE_UPDATE_INTERVAL = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("IN_SIDE_FASTEST_UPDATE_INTERVAL")) {
					this.IN_SIDE_FASTEST_UPDATE_INTERVAL = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("GEOFENCE_CHECK_FREQUENCY")) {
					this.GEOFENCE_CHECK_FREQUENCY = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("LOCATION_UPDATE_PING_FREQUENCY")) {
					this.LOCATION_UPDATE_PING_FREQUENCY = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("BEACON_PING_FREQUENCY")) {
					this.BEACON_PING_FREQUENCY = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("GEOFENCE_EXPIRY_TIME")) {
					this.GEOFENCE_EXPIRY_TIME = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("GEOFENCE_MIN_RADIUS")) {
					this.GEOFENCE_MIN_RADIUS = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("IN_STORE_UPDATE_INTERVAL")) {
					this.IN_STORE_UPDATE_INTERVAL = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("LOCATION_SERVICE_REFRESH_TIME")) {
					this.LOCATION_SERVICE_REFRESH_TIME = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("GEOFENCE_CHECK_DISTANCE_MOVED")) {
					this.GEOFENCE_CHECK_DISTANCE_MOVED = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("BEACON_SLAB_TIME")) {
					this.BEACON_SLAB_TIME = Long.parseLong(myMobileSettingObj.getString("settingValue"));
				}

				//TRIGGER_APP_EVENTS=1;// 1 on / 0 off
				if (myMobileSettingObj.getString("settingName").equals("TRIGGER_APP_EVENTS")) {
					this.TRIGGER_APP_EVENTS = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				//TRIGGER_GEOFENCE=1;//1 on / 0 off
				if (myMobileSettingObj.getString("settingName").equals("TRIGGER_GEOFENCE")) {
					this.TRIGGER_GEOFENCE = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				//TRIGGER_BEACON
				if (myMobileSettingObj.getString("settingName").equals("TRIGGER_BEACON")) {
					this.TRIGGER_BEACON = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				//TRIGGER_ERROR_EVENT=1;//1 on / 0 off
				if (myMobileSettingObj.getString("settingName").equals("TRIGGER_ERROR_EVENT")) {
					this.TRIGGER_ERROR_EVENT = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				//TRIGGER_PUSH_NOTIFICATION=1;//1 on / 0 off
				if (myMobileSettingObj.getString("settingName").equals("TRIGGER_PUSH_NOTIFICATION")) {
					this.TRIGGER_PUSH_NOTIFICATION = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
					//FBPreferences.sharedInstance(context).setTrigger_Push_Notification(myMobileSettingObj.getString("settingValue"));

				}

				if (myMobileSettingObj.getString("settingName").equals("EVENT_QUEUE_SIZE_THRESHOLD")) {
					this.EVENT_QUEUE_SIZE_THRESHOLD = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}

				if (myMobileSettingObj.getString("settingName").equals("TIMER_DELAY_IN_SECONDS")) {
					this.TIMER_DELAY_IN_SECONDS = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}
				if (myMobileSettingObj.getString("settingName").equals("ANDROID_BUILD_VERSION")) {
					this.VERSION = myMobileSettingObj.getString("settingValue");
				}

				if (myMobileSettingObj.getString("settingName").equals("INAPPOFFER_ENABLED")) {
					this.INAPPOFFER_ENABLED = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}
				if (myMobileSettingObj.getString("settingName").equals("ANDROID_BUILD_UPDATE_MESSAGE")) {
					this.INFORMATION = myMobileSettingObj.getString("settingValue");
				}

				if (myMobileSettingObj.getString("settingName").equals("ANDROID_BUILD_FORCE_UPDATE")) {
					this.FORCEUPDATE = myMobileSettingObj.getString("settingValue");
				}

				if (myMobileSettingObj.getString("settingName").equals("ANDROID_BUILD_CODE")) {
					this.VERSIONCODE = myMobileSettingObj.getString("settingValue");
				}

				if (myMobileSettingObj.getString("settingName").equals("FREQUENCY")) {
					this.FREQUENCY = Integer.parseInt(myMobileSettingObj.getString("settingValue"));
				}


			}


/*
             if (jsonObj.getJSONObject("TEAM_IDENTIFIER")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("TEAM_IDENTIFIER"));
				 mobileSettingHashMap.put(TEAM_IDENTIFIER,mobSet);
			 }
			 if (jsonObj.getJSONObject("ORGANIZATION_NAME")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("ORGANIZATION_NAME"));
				 mobileSettingHashMap.put(ORGANIZATION_NAME,mobSet);
			 }
			 if (jsonObj.getJSONObject("BRAND_ID")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("BRAND_ID"));
				 mobileSettingHashMap.put(BRAND_ID,mobSet);
			 }
			 if (jsonObj.getJSONObject("PASS_IDENTIFIER")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("PASS_IDENTIFIER"));
				 mobileSettingHashMap.put(PASS_IDENTIFIER,mobSet);
			 }
			 if (jsonObj.getJSONObject("CONFIG_SHORTCODE")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("CONFIG_SHORTCODE"));
				 mobileSettingHashMap.put(CONFIG_SHORTCODE,mobSet);
			 }

			 if (jsonObj.getJSONObject("FISHBOWL_PM_API_OAUTH_CLIENT_SECRET")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("FISHBOWL_PM_API_OAUTH_CLIENT_SECRET"));
				 mobileSettingHashMap.put(FISHBOWL_PM_API_OAUTH_CLIENT_SECRET,mobSet);
			 }
			 if (jsonObj.getJSONObject("WORKFLOW_AUTO_APPROVE")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("WORKFLOW_AUTO_APPROVE"));
				 mobileSettingHashMap.put(WORKFLOW_AUTO_APPROVE,mobSet);
			 }
			 if (jsonObj.getJSONObject("AWS_SECRET_KEY")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("AWS_SECRET_KEY"));
				 mobileSettingHashMap.put(AWS_SECRET_KEY,mobSet);
			 }
			 if (jsonObj.getJSONObject("CONFIG_VENDOR")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("CONFIG_VENDOR"));
				 mobileSettingHashMap.put(CONFIG_VENDOR,mobSet);
			 }
			 if (jsonObj.getJSONObject("PROMO_CODE_FORMAT")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("PROMO_CODE_FORMAT"));
				 mobileSettingHashMap.put(PROMO_CODE_FORMAT,mobSet);
			 }

			 if (jsonObj.getJSONObject("PROMO_CODE_FORMAT")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("PROMO_CODE_FORMAT"));
				 mobileSettingHashMap.put(PROMO_CODE_FORMAT,mobSet);
			 }
			 if (jsonObj.getJSONObject("PROMO_CODE_FORMAT")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("PROMO_CODE_FORMAT"));
				 mobileSettingHashMap.put(PROMO_CODE_FORMAT,mobSet);
			 }
			 if (jsonObj.getJSONObject("PROMO_CODE_FORMAT")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("PROMO_CODE_FORMAT"));
				 mobileSettingHashMap.put(PROMO_CODE_FORMAT,mobSet);
			 }
			 if (jsonObj.getJSONObject("PROMO_CODE_FORMAT")!=null) {
				 FBMobileSettingObjectItem mobSet=new FBMobileSettingObjectItem();
				 mobSet.initWithJsonObject(jsonObj.getJSONObject("PROMO_CODE_FORMAT"));
				 mobileSettingHashMap.put(PROMO_CODE_FORMAT,mobSet);
			 }
*/


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getTRIGGER_INAPPOFFER_ENABLED() {
		return INAPPOFFER_ENABLED;
	}


	public void setTRIGGER_INAPPOFFER_ENABLED(int tRIGGER_INAPPOFFER_ENABLED) {
		INAPPOFFER_ENABLED = tRIGGER_INAPPOFFER_ENABLED;
	}

	public int getEVENT_QUEUE_SIZE_THRESHOLD() {
		return EVENT_QUEUE_SIZE_THRESHOLD;
	}


	public void setEVENT_QUEUE_SIZE_THRESHOLD(int eVENT_QUEUE_SIZE_THRESHOLD) {
		EVENT_QUEUE_SIZE_THRESHOLD = eVENT_QUEUE_SIZE_THRESHOLD;
	}


	public int getTIMER_DELAY_IN_SECONDS() {
		return TIMER_DELAY_IN_SECONDS;
	}


	public void setTIMER_DELAY_IN_SECONDS(int tIMER_DELAY_IN_SECONDS) {
		TIMER_DELAY_IN_SECONDS = tIMER_DELAY_IN_SECONDS;
	}


	public int getTRIGGER_GEOFENCE() {
		return TRIGGER_GEOFENCE;
	}


	public void setTRIGGER_GEOFENCE(int tRIGGER_GEOFENCE) {
		TRIGGER_GEOFENCE = tRIGGER_GEOFENCE;
	}


	public int getTRIGGER_BEACON() {
		return TRIGGER_BEACON;
	}


	public void setTRIGGER_BEACON(int tRIGGER_BEACON) {
		TRIGGER_BEACON = tRIGGER_BEACON;
	}


	public int getTRIGGER_ERROR_EVENT() {
		return TRIGGER_ERROR_EVENT;
	}


	public void setTRIGGER_ERROR_EVENT(int tRIGGER_ERROR_EVENT) {
		TRIGGER_ERROR_EVENT = tRIGGER_ERROR_EVENT;
	}


	public int getTRIGGER_PUSH_NOTIFICATION() {
		return TRIGGER_PUSH_NOTIFICATION;
	}


	public void setTRIGGER_PUSH_NOTIFICATION(int tRIGGER_PUSH_NOTIFICATION) {
		TRIGGER_PUSH_NOTIFICATION = tRIGGER_PUSH_NOTIFICATION;
	}

	public int getTRIGGER_APP_EVENTS() {
		return TRIGGER_APP_EVENTS;
	}

	public void setTRIGGER_APP_EVENTS(int tRIGGER_APP_EVENTS) {
		TRIGGER_APP_EVENTS = tRIGGER_APP_EVENTS;
	}

	public long getDISTANCE_FILTER() {
		return DISTANCE_FILTER;
	}

	public float getDISTANCE_STORE() {
		return DISTANCE_STORE;
	}

	public float getGEOFENCE_RADIUS() {
		return GEOFENCE_RADIUS;
	}

	public float getGEOFENCE_MIN_RADIUS() {
		return GEOFENCE_MIN_RADIUS;
	}

	public long getSTORE_REFRESH_TIME() {
		return STORE_REFRESH_TIME;
	}

	public int getMAX_STORE_COUNT_ANDROID() {
		return MAX_STORE_COUNT_ANDROID;
	}

	public int getMAX_BEACON_COUNT() {
		return MAX_BEACON_COUNT;
	}

	public int getENABLE_LOCAL_NOTIFICATION() {
		return ENABLE_LOCAL_NOTIFICATION;
	}

	public long getOUT_SIDE_UPDATE_INTERVAL() {
		return OUT_SIDE_UPDATE_INTERVAL;
	}

	public long getOUT_SIDE_FASTEST_UPDATE_INTERVAL() {
		return OUT_SIDE_FASTEST_UPDATE_INTERVAL;
	}

	public long getIN_SIDE_UPDATE_INTERVAL() {
		return IN_SIDE_UPDATE_INTERVAL;
	}

	public long getIN_SIDE_FASTEST_UPDATE_INTERVAL() {
		return IN_SIDE_FASTEST_UPDATE_INTERVAL;
	}

	public long getGEOFENCE_CHECK_FREQUENCY() {
		return GEOFENCE_CHECK_FREQUENCY;
	}

	public long getLOCATION_UPDATE_PING_FREQUENCY() {
		return LOCATION_UPDATE_PING_FREQUENCY;
	}

	public long getBEACON_PING_FREQUENCY() {
		return BEACON_PING_FREQUENCY;
	}

	public long getGEOFENCE_EXPIRY_TIME() {
		return GEOFENCE_EXPIRY_TIME;
	}

	public long getIN_REGION_SLAB_TIME() {
		return IN_REGION_SLAB_TIME;
	}

	public long getLOCATION_SERVICE_REFRESH_TIME() {
		return LOCATION_SERVICE_REFRESH_TIME;
	}

	public long getIN_STORE_UPDATE_INTERVAL() {
		return IN_STORE_UPDATE_INTERVAL;
	}

	public long getGEOFENCE_CHECK_DISTANCE_MOVED() {
		return GEOFENCE_CHECK_DISTANCE_MOVED;
	}

	public long getBEACON_SLAB_TIME() {
		return BEACON_SLAB_TIME;
	}

	public void setBEACON_SLAB_TIME(long bEACON_SLAB_TIME) {
		BEACON_SLAB_TIME = bEACON_SLAB_TIME;
	}

}
