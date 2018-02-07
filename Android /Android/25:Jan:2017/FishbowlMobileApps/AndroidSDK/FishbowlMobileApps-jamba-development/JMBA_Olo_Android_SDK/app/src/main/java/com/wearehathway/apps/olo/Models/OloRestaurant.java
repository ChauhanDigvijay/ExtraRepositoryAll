package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class OloRestaurant {
    private int id;
    private String name;
    private String storename;
    private String telephone;
    private String streetaddress;
    private String crossstreet;
    private String city;
    private String state;
    private String zip;
    private float latitude;
    private float longitude;
    private String url;
    private String mobileurl;
    private float distance;
    private String extref;
    private boolean advanceonly;
    private int advanceorderdays;
    private int specialinstructionsmaxlength;
    private boolean supportscoupons;
    private String supportedcardtypes;
    private boolean hasolopass;
    private boolean isavailable;
    private boolean supportsspecialinstructions;
    private boolean supportsdispatch;
    private double deliveryfee;
    private ArrayList<OloRestaurantCustomField> customfields;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorename() {
        return storename;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public String getCrossstreet() {
        return crossstreet;
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

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getUrl() {
        return url;
    }

    public String getMobileurl() {
        return mobileurl;
    }

    public float getDistance() {
        return distance;
    }

    public String getStoreCode() {
        return extref;
    }

    public boolean isAdvanceonly() {
        return advanceonly;
    }

    public int getAdvanceorderdays() {
        return advanceorderdays;
    }

    public boolean isSupportscoupons() {
        return supportscoupons;
    }

    public String getSupportedcardtypes() {
        return supportedcardtypes;
    }

    public boolean isHasolopass() {
        return hasolopass;
    }

    public ArrayList<OloRestaurantCustomField> getCustomfields() {
        return customfields;
    }

    public boolean isAvailable() {
        return isavailable;
    }

    public boolean isSupportdispatch() {
        return supportsdispatch;
    }

    public double getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(double deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public boolean isSupportsSpecialInstructions() {
        return supportsspecialinstructions;
    }

    public boolean isSupportsdispatch() {
        return supportsdispatch;
    }

    public void setSupportsdispatch(boolean supportsdispatch) {
        this.supportsdispatch = supportsdispatch;
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
