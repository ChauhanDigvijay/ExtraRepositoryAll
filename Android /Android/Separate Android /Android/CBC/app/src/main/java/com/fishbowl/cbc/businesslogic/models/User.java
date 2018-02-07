package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.cbc.utils.Constants;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by VT027 on 5/19/2017.
 */

@ParseClassName("User")
public class User extends ParseObject {
    private static final String OloAuthToken = "OloAuthToken";
    private static final String FirstName = "FirstName";
    private static final String LastName = "LastName";
    private static final String Email = "Email";
    private static final String ContactNumber = "ContactNumber";
    private static final String AvatarIconId = "AvatarIconId";
    private static final String FavoriteStoreCode = "FavoriteStoreCode";
    private static final String OloFavoriteRestaurantId = "OloFavoriteRestaurantId";
    private static final String StoreAddress = "StoreAddress";
    private static final String StoreName = "StoreName";
    private static final String EnableSmsOpt = "EnableSmsOpt";
    private static final String EnableEmailOpt = "EnableEmailOpt";
    private static final String EnablePushOpt = "EnablePushOpt";
    private static final String DOB = "DOB";
    private static final String Longitude = "Longitude";
    private static final String Latitude = "Latitude";
    private static final String TotalPoints = "TotalPoints";
    private static final String Threshold = "Threshold";
    private static final String TotalRewards = "TotalRewards";
    private static final String TotalOffers = "TotalOffers";


    private static final String StoreTelephoneNumber = "StoreTelephoneNumber";

    public User() {
    }

    /* public void updateUserWithSpendGoData(SpendGoUser user)
     {
         if (user != null)
         {
             setFirstname(user.getFirst_name());
             setLastname(user.getLast_name());
             setEmailaddress(user.getEmail());
             setContactnumber(user.getPhone());
             setSpendGoId(user.getSpendgo_id());
             setDob(user.getDob());
             setEnableEmailOpt(user.isEmail_opt_in());
             setEnableSmsOpt(user.isSms_opt_in());
             setEnablePushOpt(user.isPush_opt_in());

             SpendGoStore favoriteStore = user.getFavorite_store();
             if (favoriteStore != null)
             {

                 setSpendGoFavoriteStoreId(favoriteStore.getId());
                 setFavoriteStoreCode(favoriteStore.getCode());
                 setStoreName(favoriteStore.getName().substring(5, favoriteStore.getName().length()));
                 setStoreAddress(Utils.getFormatedAddress(favoriteStore.getStreet(), favoriteStore.getCity(), favoriteStore.getState(), favoriteStore.getZip()));
                 setStoreTelephoneNumber(favoriteStore.getPhone());
                 setLongitude(favoriteStore.getLongitude());
                 setLatitude(favoriteStore.getLatitude());
             }
         }
     }
 */
    @Override
    public void put(String key, Object value) {
        if (value != null) {
            super.put(key, value);
        }
    }

    public String getFirstname() {
        return getString(FirstName);
    }

    public void setFirstname(String firstname) {
        put(FirstName, firstname);
    }

    public String getLastname() {
        return getString(LastName);
    }

    public void setLastname(String lastname) {
        put(LastName, lastname);
    }

    public String getEmailaddress() {
        return getString(Email);
    }

    public void setEmailaddress(String emailaddress) {
        put(Email, emailaddress);
    }

    public String getContactnumber() {
        return getString(ContactNumber);
    }

    public void setContactnumber(String contactnumber) {
        put(ContactNumber, contactnumber);
    }

    public int getAvatarId() {
        return getInt(AvatarIconId);
    }

    public void setAvatarId(int avatarId) {
        if(avatarId >= 0 && avatarId < Constants.AVATAR_ICONS.length)
        {
            put(AvatarIconId, avatarId);
        }
    }


    public String getOloAuthToken() {
        return getString(OloAuthToken);
    }

    public void setOloAuthToken(String oloAuthToken) {
        if (oloAuthToken != null && !oloAuthToken.equals("")) {
            put(OloAuthToken, oloAuthToken);
        }
    }

    public String getFavoriteStoreCode() {
        return getString(FavoriteStoreCode);
    }

    public void setFavoriteStoreCode(String favoriteStoreCode) {
        put(FavoriteStoreCode, favoriteStoreCode);
    }

    public int getOloFavoriteRestaurantId() {
        return getInt(OloFavoriteRestaurantId);
    }

    public void setOloFavoriteRestaurantId(int oloFavoriteRestaurantId) {
        put(OloFavoriteRestaurantId, oloFavoriteRestaurantId);
    }

    public String getDob() {
        return getString(DOB);
    }

    public void setDob(String dob) {
        if (dob != null && !dob.equals("")) {
            put(DOB, dob);
        }
    }

    public boolean isEnableSmsOpt() {
        return getBoolean(EnableSmsOpt);
    }

    public void setEnableSmsOpt(boolean enableSmsOpt) {
        put(EnableSmsOpt, enableSmsOpt);
    }

    public boolean isEnableEmailOpt() {
        return getBoolean(EnableEmailOpt);
    }

    public void setEnableEmailOpt(boolean enableEmailOpt) {
        put(EnableEmailOpt, enableEmailOpt);
    }

    public boolean isEnablePushOpt() {
        return getBoolean(EnablePushOpt);
    }

    public void setEnablePushOpt(boolean enablePushOpt) {
        put(EnablePushOpt, enablePushOpt);
    }

    public String getStoreAddress() {
        return getString(StoreAddress);
    }

    public void setStoreAddress(String storeAddress) {
        put(StoreAddress, storeAddress);
    }

    public String getStoreName() {
        return getString(StoreName);
    }

    public void setStoreName(String storeName) {
        put(StoreName, storeName);
    }

    public double getLatitude() {
        return getDouble(Latitude);
    }

    public void setLatitude(double latitude) {
        put(Latitude, latitude);
    }

    public double getLongitude() {
        return getDouble(Longitude);
    }

    public void setLongitude(double longitude) {
        put(Longitude, longitude);
    }

    public boolean isPreferredStoreSupportsOrderAHead() {
        return getOloFavoriteRestaurantId() > 0;
    }

    public int getTotalPoints() {
        return getInt(TotalPoints);
    }

    public void setTotalPoints(int totalPoints) {
        put(TotalPoints, totalPoints);
    }

    public int getThreshold() {
        return getInt(Threshold);
    }

    public void setThreshold(int spendThreshold) {
        put(Threshold, spendThreshold);
    }

    public int getTotalRewards() {
        return getInt(TotalRewards);
    }

    public void setTotalRewards(int totalRewards) {
        put(TotalRewards, totalRewards);
    }

    public void setTotalOffers(int totalOffers) {
        put(TotalOffers, totalOffers);
    }

    public int getTotalOffers() {
        return getInt(TotalOffers);
    }

    public void setStoreTelephoneNumber(String store_Telephone) {
        if (store_Telephone != null && !store_Telephone.equals("")) {
            put(StoreTelephoneNumber, store_Telephone);
        }
    }

    public String getStoreTelephoneNumber() {
        return getString(StoreTelephoneNumber);
    }
}
