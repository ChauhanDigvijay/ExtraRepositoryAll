package com.fishbowl.loyaltymodule.Models;

import org.json.JSONObject;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class RegisterField {
    public String field;
    public boolean mandatory;
    public  boolean visible;
    int fieldType;
    public  int configDisplaySeq;
    public String configDisplayLabel;

    public int getConfigDisplaySeq() {
        return configDisplaySeq;
    }

    public void setConfigDisplaySeq(int configDisplaySeq) {
        this.configDisplaySeq = configDisplaySeq;
    }

    RegisterField initWithJSon(JSONObject json){

        try {
            field = json.getString("field");
            mandatory = json.getBoolean("mandatory");
            visible = json.getBoolean("visible");
            configDisplaySeq = json.getInt("configDisplaySeq");
            configDisplayLabel = json.getString("configDisplayLabel");
        }catch (Exception e) {
            e.printStackTrace();
        }

        return  this;
    }
}
