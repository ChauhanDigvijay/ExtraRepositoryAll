package com.fishbowl.basicmodule.Models;

import android.content.Context;

import com.fishbowl.basicmodule.Preferences.FBPreferences;

import org.json.JSONObject;

/**
 * Created by digvijay(dj)
 */
public class Member {
    public static  String CUSTOMER_ID="customerID";
    public static  String EMAIL="emailID";
    public static  String SMS_OPTIN="smsOpted";
    public static  String MAIL_OPTIN="emailOpted";
    public static  String ADDRESSS_STATE="addressState";
    public static  String ADDRESS_CITY="addressCity";
    public static  String ADDRESS_STREET="addressLine1";
    public static  String PHONE= "cellPhone";
    public static  String FIRST_NAME="firstName";
    public static  String LAST_NAME="lastName";
    public static  String GENDER="customerGender";
    public static  String FAVOURITE_DEPARTMENT="favoriteDepartment";
    public static  String ZIP="addressZip";
    public static  String BIRTH_DATE="dateOfBirth";
    public static  String PASSWORD="loginPassword";
    public static  String HOME_STORE_ID="homeStoreID";
    public static  String Loyality_No="loyalityNo";
    /**
     * Created by digvijay(dj)
     */

    public  int offercount;
    public  int rewardcount;

    public int getRewardcount() {
        return rewardcount;
    }

    public void setRewardcount(int rewardcount) {
        this.rewardcount = rewardcount;
    }

    public int getOffercount() {
        return offercount;
    }

    public void setOffercount(int offercount) {
        this.offercount = offercount;
    }

    public double getRewardpoint() {
        return rewardpoint;
    }

    public void setRewardpoint(double rewardpoint) {
        this.rewardpoint = rewardpoint;
    }

    public  double rewardpoint;

    public  String customerID;
    public String emailID;
    public String smsOpted;
    public  String emailOpted;
    public String addressState;
    public String addressCity;
    public String addressLine1;
    public String   cellPhone;
    public String   firstName;
    public String   lastName;
    public String   customerGender;
    public String   favoriteDepartment;
    public  String   addressZip;
    public String   dateOfBirth;
    public String   loginPassword;
    public String homeStoreID;
    public String loyalityNo;



    public void initWithJson(JSONObject jsonObj, Context context){

        try {



            if (jsonObj.has(CUSTOMER_ID) && !jsonObj.isNull(CUSTOMER_ID)) {
                customerID=jsonObj.getString(CUSTOMER_ID);
                FBPreferences.sharedInstance(context).setUserMemberforAppId(customerID);
            }

            if (jsonObj.has(EMAIL) && !jsonObj.isNull(EMAIL)) {
                emailID=jsonObj.getString(EMAIL);
            }
            if (jsonObj.has(SMS_OPTIN) && !jsonObj.isNull(SMS_OPTIN)) {
                smsOpted=jsonObj.getString(SMS_OPTIN);
            }

            if (jsonObj.has(MAIL_OPTIN) && !jsonObj.isNull(MAIL_OPTIN)) {
                emailOpted=jsonObj.getString(MAIL_OPTIN);
            }
            if (jsonObj.has(ADDRESSS_STATE) && !jsonObj.isNull(ADDRESSS_STATE)) {
                addressState=jsonObj.getString(ADDRESSS_STATE);
            }

            if (jsonObj.has(ADDRESS_CITY) && !jsonObj.isNull(ADDRESS_CITY)) {
                addressCity=jsonObj.getString(ADDRESS_CITY);
            }
            if (jsonObj.has(ADDRESS_STREET) && !jsonObj.isNull(ADDRESS_STREET)) {
                addressLine1=jsonObj.getString(ADDRESS_STREET);
            }
            if (jsonObj.has(PHONE) && !jsonObj.isNull(PHONE)) {
                cellPhone=jsonObj.getString(PHONE);
            }

            if (jsonObj.has(FIRST_NAME) && !jsonObj.isNull(FIRST_NAME)) {
                firstName=jsonObj.getString(FIRST_NAME);
            }
            if (jsonObj.has(LAST_NAME) && !jsonObj.isNull(LAST_NAME)) {
                lastName=jsonObj.getString(LAST_NAME);
            }

            if (jsonObj.has(GENDER) && !jsonObj.isNull(GENDER)) {
                customerGender=jsonObj.getString(GENDER);
            }

            if (jsonObj.has(FAVOURITE_DEPARTMENT) && !jsonObj.isNull(FAVOURITE_DEPARTMENT)) {
                favoriteDepartment=jsonObj.getString(FAVOURITE_DEPARTMENT);
            }

            if (jsonObj.has(ZIP) && !jsonObj.isNull(ZIP)) {
                addressZip=jsonObj.getString(ZIP);
            }

            if (jsonObj.has(BIRTH_DATE) && !jsonObj.isNull(BIRTH_DATE)) {
                dateOfBirth=jsonObj.getString(BIRTH_DATE);
            }

            if (jsonObj.has(PASSWORD) && !jsonObj.isNull(PASSWORD)) {
                loginPassword=jsonObj.getString(PASSWORD);
            }
            if (jsonObj.has(HOME_STORE_ID) && !jsonObj.isNull(HOME_STORE_ID)&&Integer.valueOf(jsonObj.getString(HOME_STORE_ID))>0  ) {
                homeStoreID=jsonObj.getString(HOME_STORE_ID);
            }


            if (jsonObj.has(Loyality_No) && !jsonObj.isNull(Loyality_No)) {
                loyalityNo=jsonObj.getString(Loyality_No);
            }

        }catch (Exception e){
            e.printStackTrace();

        }

    }


}
