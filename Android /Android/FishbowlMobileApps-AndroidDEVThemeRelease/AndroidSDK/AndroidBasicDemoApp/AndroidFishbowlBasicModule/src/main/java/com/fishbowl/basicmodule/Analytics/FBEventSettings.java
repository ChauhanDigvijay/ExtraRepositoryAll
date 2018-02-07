package com.fishbowl.basicmodule.Analytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by digvijay(dj)
 */
@SuppressWarnings("serial")
public class FBEventSettings implements Serializable {
	
	int id;
    String message;
    String successFlag;
    
   public ArrayList<FBEventModel> modelList=new ArrayList<FBEventModel>();
 
	
	public static final String PASS_OPENED="PASS_OPENED"; //yes
	public static final String PASS_CLICKED="PASS_CLICKED";//yes We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String PASS_ACCEPTED="PASS_ACCEPTED";////yes // We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String AD_OPENED="AD_OPENED";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String AD_CLICKED="AD_CLICKED";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String SMS_VIEWED="SMS_VIEWED";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String EMAIL_OPEN="EMAIL_OPEN";
	public static final String REWARDS_REDEEMED="REWARDS_REDEEMED";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String PASS_REDEEMED="PASS_REDEEMED";////yes We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String APP_OPEN="APP_OPEN";
	public static final String APP_CLOSE="APP_CLOSE";
	public static final String LOGIN="LOGIN";
	public static final String LOGOUT="LOGOUT";
	public static final String STORE_SEARCH="STORE_SEARCH";
	public static final String MENU_SEARCH="MENU_SEARCH";
	public static final String OPEN_APP_OFFER="OPEN_APP_OFFER";
	public static final String ACCEPT_APP_OFFER="ACCEPT_APP_OFFER";
	public static final String MENU_CLICK="MENU_CLICK";
	public static final String PRODUCT_CLICK="PRODUCT_CLICK";
	public static final String OPEN_PRODUCT="OPEN_PRODUCT";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String ADD_PRODUCT="ADD_PRODUCT";
	public static final String MODIFY_PRODUCT="MODIFY_PRODUCT";
	public static final String REMOVE_PRODUCT="REMOVE_PRODUCT";
	public static final String SCAN_PRODUCT="SCAN_PRODUCT";
	public static final String CHECKOUT="CHECKOUT";
	public static final String PUSH_OPEN="PUSH_OPEN";
	public static final String ACCEPT_OFFER="ACCEPT_OFFER";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String DEPARTMENT_CLICK="DEPARTMENT_CLICK";
	public static final String SUB_DEPT_CLICK="SUB_DEPT_CLICK";
	public static final String CATEGORY_CLICK="CATEGORY_CLICK";
	public static final String SUB_CATEGORY_CLICK="SUB_CATEGORY_CLICK";
	public static final String CLICK_EVENT="CLICK_EVENT";
	public static final String TAB_CLICK="TAB_CLICK";
	public static final String FAV_STORE="FAV_STORE";
	public static final String LOCATION_UPDATE="LOCATION_UPDATE";
	public static final String USER_CONVERSION = "USER_CONVERSION";
	public static final String FIRST_TIME_LAUNCH = "FIRST_TIME_LAUNCH";
	public static final String  SIGN_UP_START="SIGN_UP_START";
	public static final String  SIGN_UP_COMPLETE="SIGN_UP_COMPLETE";
	public static final String  FEATURE_PRODUCT_CLICK="FEATURE_PRODUCT_CLICK";
	public static final String  BASKET_DELETE="BASKET_DELETE";
	public static final String  ADD_BOOST="ADD_BOOST";
	public static final String  APPLY_COUPAN="APPLY_COUPAN";
	public static final String  APPLY_OFFER="APPLY_OFFER";
	public static final String  APPLY_REWARD="APPLY_REWARD";
	public static final String  CREATE_BASKET="CREATE_BASKET";
	public static final String  ORDER_AGAIN="ORDER_AGAIN";
	public static final String  REMOVE_BOOST="REMOVE_BOOST";
	public static final String  SUBMIT_ORDER="SUBMIT_ORDER";
	public static final String  REMOVE_COUPAN="REMOVE_COUPON";
	public static final String  REMOVE_OFFER="REMOVE_OFFER";
	public static final String  REMOVE_REWARD="REMOVE_REWARD";
	public static final String  PICK_UP_TIME="PICK_UP_TIME";
	
	public static final String  START_NEW_ORDER="START_NEW_ORDER";
	public static final String  UPDATE_PHONE="UPDATE_PHONE";
	public static final String  UPDATE_EMAIL="UPDATE_EMAIL";
	public static final String  UPDATE_Password="UPDATE_PASSWORD";
	public static final String  FORGOT_PASSWORD="FORGOT_PASSWORD";
	public static final String  UPDATE_PROFILE_IMAGE="UPDATE_PROFILE_IMAGE";
	public static final String  UPDATE_FAVORITE_STORE="UPDATE_FAVORITE_STORE";
	public static final String  UPDATE_PROFILE="UPDATE_PROFILE";
	public static final String  PUSH_OFFER_RECIEVE="PUSH_OFFER_RECIEVE";
	public static final String  MODIFIER_INTERESTED="MODIFIER_INTERESTED";
	public static final String  CHECKOUT_WITHMODIFIERS="CHECKOUT_WITHMODIFIERS";
	private   int   EVENT_QUEUE_SIZE_THRESHOLD=10;
	private   int   TIMER_DELAY_IN_SECONDS=60;
	
	
	public static String getPushOfferRecieve() {
		return PUSH_OFFER_RECIEVE;
	}

	
	
	public long getEVENT_QUEUE_SIZE_THRESHOLD() {
		return EVENT_QUEUE_SIZE_THRESHOLD;
	}

	public void setEVENT_QUEUE_SIZE_THRESHOLD(int eVENT_QUEUE_SIZE_THRESHOLD) {
		EVENT_QUEUE_SIZE_THRESHOLD = eVENT_QUEUE_SIZE_THRESHOLD;
	}

	public long getTIMER_DELAY_IN_SECONDS() {
		return TIMER_DELAY_IN_SECONDS;
	}

	public void setTIMER_DELAY_IN_SECONDS(int tIMER_DELAY_IN_SECONDS) {
		TIMER_DELAY_IN_SECONDS = tIMER_DELAY_IN_SECONDS;
	}

	public static final String  CALL_STORE="CallStore";

	public void initFromJson(JSONObject jsonObj){
		 try {
			 if(jsonObj.has("id") && !jsonObj.isNull("id") && (jsonObj.get("id") instanceof Integer)){
				 id=jsonObj.getInt("id");
			 }
			 if(jsonObj.has("message") && !jsonObj.isNull("message") && (jsonObj.get("message") instanceof String)){
				 message=jsonObj.getString("message");
			 }

			 if(jsonObj.has("successFlag") && !jsonObj.isNull("successFlag") && (jsonObj.get("successFlag") instanceof String)){
				 successFlag=jsonObj.getString("successFlag");
			 }

			 JSONArray eventArray = null;

			 if(jsonObj.has("digitalEvents") && !jsonObj.isNull("digitalEvents") && (jsonObj.get("digitalEvents") instanceof JSONArray)){
				 eventArray=jsonObj.getJSONArray("digitalEvents");

				 for (int i = 0; i < eventArray.length(); i++) {
					 JSONObject jsonEvent=(JSONObject) eventArray.get(i);

					 FBEventModel evntModel=new FBEventModel();
					 evntModel.initFromJson(jsonEvent);
					 modelList.add(evntModel);
				 }
			 }
 	 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	}

	public FBEventSettings() {
		
		
	}
	
	public boolean isAvailableEvent(String eventName){
		
		
		for(int i=0; i<modelList.size(); i++ ){
			FBEventModel model=modelList.get(i);
			if (model.eventTypeName.equals(eventName)){
				
				return true;
			}
			
		}
		return true;
		
	}
 
    
}
