package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloRestaurant;
import com.fishbowl.apps.olo.Utils.Utils;

import java.io.Serializable;

/**
 * Created by VT027 on 5/20/2017.
 */

public class Store implements Serializable {
    private int id;
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

    public Store() {

    }

    public Store(String storeName, String city, String state) {
        this.name = storeName;
        this.city = city;
        this.state = state;
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

    public void populateFieldsWithOloRestaurant(OloRestaurant restaurant) {
        if (restaurant != null) {
            this.supportsOrderAhead = true;
            this.restaurantId = restaurant.getId();
            this.isSupportsSpecialInstructions = restaurant.isSupportsSpecialInstructions();
            this.latitude = restaurant.getLatitude();
            this.longitude = restaurant.getLongitude();
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

    public String getStreetAddress() {
        return streetAddress;
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

    public double getLongitude() {
        return longitude;
    }

    public boolean isSupportsOrderAhead() {
        return supportsOrderAhead;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getCompleteAddress() {
        return Utils.getFormatedAddress(streetAddress, city, state, zip);
    }

    public void setSupportsOrderAhead(boolean supportsOrderAhead) {
        this.supportsOrderAhead = supportsOrderAhead;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
}
