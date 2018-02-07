package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;


import org.json.JSONObject;

import java.io.Serializable;


public class AreaType implements Serializable {

    public String areaType;
    public int id;


    public AreaType(JSONObject jsonObj) {

        try {
            id = jsonObj.has("id") ? jsonObj.getInt("id") : 0;
            areaType = jsonObj.has("areaType") ? jsonObj.getString("areaType") : null;

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}