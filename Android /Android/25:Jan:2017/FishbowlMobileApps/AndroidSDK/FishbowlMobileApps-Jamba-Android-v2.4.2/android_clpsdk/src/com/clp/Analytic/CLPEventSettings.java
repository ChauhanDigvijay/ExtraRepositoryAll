package com.clp.Analytic;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
  Created By mohd Vaseem
 */

@SuppressWarnings("serial")
public class CLPEventSettings implements Serializable {
	
	int id;
    String message;
    String successFlag;
    
   public ArrayList<CLPEventModel> modelList=new ArrayList<CLPEventModel>();
 
	
	public static final String PASS_OPENED="PASS_OPENED"; //yes
	public static final String PASS_CLICKED="PASS_CLICKED";//yes We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String PASS_ACCEPTED="PASS_ACCEPTED";////yes // We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String AD_OPENED="";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String AD_CLICKED="";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String SMS_VIEWED="";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String EMAIL_OPEN="EmailOpen";
	public static final String REWARDS_REDEEMED="";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String PASS_REDEEMED="";////yes We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String APP_OPEN="Opened";
	public static final String APP_CLOSE="Closed";
	public static final String LOGIN="SignedIn";
	public static final String LOGOUT="SignedOut";
	public static final String STORE_SEARCH="StoreSearch";
	public static final String MENU_SEARCH="MenuSearch";
	public static final String OPEN_APP_OFFER="OpenOffer";
	public static final String ACCEPT_APP_OFFER="AcceptOffer";
	public static final String MENU_CLICK="MenuOpened";
	public static final String PRODUCT_CLICK="ProductViewed";
	public static final String OPEN_PRODUCT="";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String ADD_PRODUCT="ProductAdded";
	public static final String MODIFY_PRODUCT="ProductQuantityModified";
	public static final String REMOVE_PRODUCT="ProductRemoved";
	public static final String SCAN_PRODUCT="ProductScanned";
	public static final String CHECKOUT="ListSubmitted";
	public static final String PUSH_OPEN="PUSH_OPEN";
	public static final String ACCEPT_OFFER="ACCEPT_OFFER";// We have to add in database in CLYP_EVENT_TYPE_MAPPING
	public static final String DEPARTMENT_CLICK="DeptViewed";
	public static final String SUB_DEPT_CLICK="Sub-DeptViewed";
	public static final String CATEGORY_CLICK="CategoryViewed";
	public static final String SUB_CATEGORY_CLICK="Sub-CategViewed";
	public static final String CLICK_EVENT="ClickEvent";
	public static final String TAB_CLICK="TabClicked";
	public static final String FAV_STORE="FavoriteStoreSelected";
	public static final String LOCATION_UPDATE="LocationChange";
	public static final String USER_CONVERSION = "UserConversion";
	public static final String FIRST_TIME_LAUNCH = "FirstTimeLaunch";
	public static final String  SIGN_UP_START="SignUpStart";
	public static final String  SIGN_UP_COMPLETE="SignUpComplete";
	public static final String  FEATURE_PRODUCT_CLICK="FeatureProductClick";
	public static final String  BASKET_DELETE="BasketDelete";
	public static final String  ADD_BOOST="AddBoost";
	public static final String  APPLY_COUPAN="ApplyCoupon";
	public static final String  APPLY_REWARD="ApplyReward";
	public static final String  CREATE_BASKET="CreateBasket";
	public static final String  ORDER_AGAIN="OrderAgain";
	public static final String  REMOVE_BOOST="RemoveBoost";
	public static final String  SUBMIT_ORDER="SubmitOrder";
	public static final String  REMOVE_COUPAN="RemoveCoupon";
	public static final String  REMOVE_REWARD="RemoveReward";
	public static final String  PICK_UP_TIME="PickUpTime";	
	public static final String  START_NEW_ORDER="StartNewOrder";
	public static final String  UPDATE_PHONE="UpdatePhone";
	public static final String  UPDATE_EMAIL="UpdateEmail";
	public static final String  UPDATE_Password="UpdatePassword";
	public static final String  FORGOT_PASSWORD="ForgotPassword";
	public static final String  UPDATE_PROFILE_IMAGE="UpdateProfileImage";
	public static final String  UPDATE_FAVORITE_STORE="UpdateFavoriteStore";
	public static final String  UPDATE_PROFILE="UpdateProfile";
	public static final String  PUSH_OFFER_RECIEVE="PushOfferRecieve";	
	public static final String  MODIFIER_INTERESTED="ModifierInterested";
	public static final String  CHECKOUT_WITHMODIFIERS="CheckoutWithModifiers";
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
			 id=jsonObj.getInt("id");
			 message=jsonObj.getString("message");
			 successFlag=jsonObj.getString("successFlag");
			 JSONArray eventArray=jsonObj.getJSONArray("digitalEvents");

           for (int i = 0; i < eventArray.length(); i++) {
        	   JSONObject jsonEvent=(JSONObject) eventArray.get(i);
        	   
        	   CLPEventModel evntModel=new CLPEventModel();
        	   evntModel.initFromJson(jsonEvent); 
        	   modelList.add(evntModel); 
		}

 	 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	}
	
	public CLPEventSettings() {
		
		
	}
	
	public boolean isAvailableEvent(String eventName){
		
		
		for(int i=0; i<modelList.size(); i++ ){
			CLPEventModel model=modelList.get(i);
			if (model.eventTypeName.equals(eventName)){
				
				return true;
			}
			
		}
		
		return false;
		
	}
 
    
}
