package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import org.json.JSONObject;

/**
 * Created by mohdvaseem on 17/06/16.
 */
public class RegisterField {
    String field;
    boolean mandatory;
    boolean visible;
    int fieldType;
    int configDisplaySeq;

    RegisterField initWithJSon(JSONObject json) {

        try {
            field = json.getString("field");
            mandatory = json.getBoolean("mandatory");
            visible = json.getBoolean("visible");
            configDisplaySeq = json.getInt("configDisplaySeq");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }
}
