package com.olo.jambajuice.BusinessLogic.Models;

import android.util.Log;

import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Models.OloRestaurant;
import com.wearehathway.apps.spendgo.Models.SpendGoStore;

import java.io.Serializable;

/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class Store implements Serializable {
    private int id; // SpendGo Store Id
    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
    private String storeCode;
    private String telephone;

    private double latitude;
    private double longitude;
    //For Olo restaurants only
    private boolean supportsOrderAhead;
    private boolean isSupportsSpecialInstructions;
    private int restaurantId;
    private StoreTiming storeTiming;
    private boolean isSupportDelivery;


    // When user location is known, distance can be calculated
    private double distanceToUser;
    // Restaurants - Delivery option
    private boolean supportsDeliveryOption;
    private double deliveryfee;

    private int advanceorderdays;
    private int specialinstructionsmaxlength;

    public Store() {

    }

    public Store(String storeName, String city, String state) {
        this.name = storeName;
        this.city = city;
        this.state = state;
    }

    public Store(SpendGoStore restaurant) {
        this.id = (restaurant.getId());
        this.name = restaurant.getName();
        name = name.substring(5, name.length());
        this.city = restaurant.getCity();
        this.streetAddress = restaurant.getStreet();
        this.state = restaurant.getState();
        this.zip = restaurant.getZip();
        this.telephone = restaurant.getPhone();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.storeCode = restaurant.getCode();
        this.supportsOrderAhead = false;
        this.distanceToUser = -1;
    }

    public Store(OloRestaurant restaurant) {
        this.restaurantId = restaurant.getId();
        this.name = restaurant.getName();
        this.city = restaurant.getCity();
        this.streetAddress = restaurant.getStreetaddress();
        this.state = restaurant.getState();
        this.zip = restaurant.getZip();
        this.telephone = restaurant.getTelephone();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.storeCode = restaurant.getStoreCode();
        this.deliveryfee = restaurant.getDeliveryfee();
        this.advanceorderdays = restaurant.getAdvanceorderdays();
        this.specialinstructionsmaxlength = restaurant.getSpecialinstructionsmaxlength();
        if (restaurant.isAvailable()) {
            this.supportsOrderAhead = true;
        } else {
            this.supportsOrderAhead = false;
        }
        if (restaurant.isSupportdispatch()) {
            this.supportsDeliveryOption = true;
        } else {
            this.supportsDeliveryOption = false;
        }
        this.isSupportsSpecialInstructions = restaurant.isSupportsSpecialInstructions();
        this.isSupportDelivery = restaurant.isSupportsdispatch();
    }

    public void setDemoStore() {

        //Spendgo Info
        this.id = 9999;
        this.name = "9999 Demo vendor";
        name = name.substring(5, name.length());
        this.city = "Emeryville";
        this.streetAddress = "6450 Christie Ave. # 150";
        this.state = "CA";
        this.zip = "94608";
        this.telephone = "(000) 000-0000";
        this.latitude = 37.844775;
        this.longitude = -122.2961095;
        this.storeCode = "9999";
        this.distanceToUser = -1;

        //Olo Info
        this.supportsOrderAhead = true;
        this.supportsDeliveryOption = true;
        this.restaurantId = 24872;
        this.isSupportsSpecialInstructions = false;
        this.isSupportDelivery = true;
        this.advanceorderdays = 7;
        this.specialinstructionsmaxlength = 40;
    }

    public void setDemoLabStore() {

        //Spendgo Info
        this.id = 9997;
        this.name = "9997 Demo Lab vendor";
        name = name.substring(5, name.length());
        this.city = "Emeryville";
        this.streetAddress = "26 Broadway\n" +
                "24th Floor";
        this.state = "NY";
        this.zip = "94608";
        this.telephone = "(555) 555-5555";
        this.latitude = 37.844775;
        this.longitude = -122.2961095;
        this.storeCode = "9997";
        this.distanceToUser = -1;

        //Olo Info
        this.supportsOrderAhead = true;
        this.supportsDeliveryOption = true;
        this.restaurantId = 8687;
        this.isSupportsSpecialInstructions = false;
        this.isSupportDelivery = true;
        this.advanceorderdays = 7;
        this.specialinstructionsmaxlength = 40;
    }

    public void populateFieldsWithOloRestaurant(OloRestaurant restaurant) {
        if (restaurant != null) {
//            this.supportsOrderAhead = true;
            this.restaurantId = restaurant.getId();
            this.isSupportsSpecialInstructions = restaurant.isSupportsSpecialInstructions();
            this.latitude = restaurant.getLatitude();
            this.longitude = restaurant.getLongitude();
            this.advanceorderdays = restaurant.getAdvanceorderdays();
            this.specialinstructionsmaxlength = restaurant.getSpecialinstructionsmaxlength();
        }
    }

    public boolean isSupportsDeliveryOption() {
        return supportsDeliveryOption;
    }

    public void setSupportsDeliveryOption(boolean supportsDeliveryOption) {
        this.supportsDeliveryOption = supportsDeliveryOption;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSupportsOrderAhead() {
        return supportsOrderAhead;
    }

    public void setSupportsOrderAhead(boolean supportsOrderAhead) {
        this.supportsOrderAhead = supportsOrderAhead;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCompleteAddress() {
        return Utils.getFormatedAddress(streetAddress, city, state, zip);
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    // Hard Coded Distance for Demo Vendor.
    public double getDistanceToUser() {
        if (distanceToUser != -1) {
            return distanceToUser;
        } else {
            return 1.1;
        }
    }

    public void setDistanceToUser(double distanceToUser) {
        this.distanceToUser = distanceToUser;
    }

    public String getStreetAndDistance() {
        if (distanceToUser != -1) {
            return streetAddress + " - " + distanceToUser;
        } else {
            return streetAddress;
        }
    }

    public StoreTiming getStoreTiming() {
        return storeTiming;
    }

    public void setStoreTiming(StoreTiming storeTiming) {
        this.storeTiming = storeTiming;
    }

    public void setSpendGoStoreId(int id) {
        this.id = id;
    }

    public boolean isSupportsSpecialInstructions() {
        return isSupportsSpecialInstructions;
    }

    public double getDeliveryfee() {
        return deliveryfee;
    }

    public boolean isSupportDelivery() {
        return isSupportDelivery;
    }

    public void setSupportDelivery(boolean supportDelivery) {
        isSupportDelivery = supportDelivery;
    }

    public int getAdvanceorderdays() {
        return advanceorderdays;
    }

    public void setAdvanceorderdays(int advanceorderdays) {
        this.advanceorderdays = advanceorderdays;
    }

    public int getSpecialinstructionsmaxlength() {
        return specialinstructionsmaxlength;
    }

    public void setSpecialinstructionsmaxlength(int specialinstructionsmaxlength) {
        this.specialinstructionsmaxlength = specialinstructionsmaxlength;
    }
}

