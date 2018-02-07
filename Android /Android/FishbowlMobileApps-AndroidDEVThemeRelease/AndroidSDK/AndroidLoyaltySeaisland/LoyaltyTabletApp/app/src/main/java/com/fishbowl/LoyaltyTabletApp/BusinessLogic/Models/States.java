package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class States implements Serializable {
    public static Map<String, String> allmapstorecode = new HashMap<String, String>();
    public int stateID;
    public String stateName, stateCode, countryCode;

    public States() {
    }

    public States(JSONObject obj) {
        try {
            stateID = obj.has("stateID") ? obj.getInt("stateID") : 0;
            stateName = obj.has("stateName") ? obj.getString("stateName") : null;
            stateCode = obj.has("stateCode") ? obj.getString("stateCode") : null;
            countryCode = obj.has("countryCode") ? obj.getString("countryCode") : null;
            allmapstorecode.put(stateCode, stateName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void getCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

}
