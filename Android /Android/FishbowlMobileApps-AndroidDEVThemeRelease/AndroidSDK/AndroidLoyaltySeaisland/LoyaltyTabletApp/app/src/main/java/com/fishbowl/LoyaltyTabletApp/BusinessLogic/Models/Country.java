package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

public class Country implements Serializable {

    public String countryName, countryCode;


    public Country() {
    }

    public Country(JSONObject obj) {
        try {

            countryName = obj.has("countryName") ? obj.getString("countryName") : null;

            countryCode = obj.has("countryCode") ? obj.getString("countryCode") : null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setcountryName(String stateCode) {
        this.countryName = stateCode;
    }

    public String getcountryName() {
        return countryName;
    }

    public void getCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

}
