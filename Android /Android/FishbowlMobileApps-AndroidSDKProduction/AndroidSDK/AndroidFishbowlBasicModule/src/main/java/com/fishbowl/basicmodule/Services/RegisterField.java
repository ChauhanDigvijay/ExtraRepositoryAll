package com.fishbowl.basicmodule.Services;

import org.json.JSONObject;

/**
 * Created by digvijay on 17/06/16.
 */
public class RegisterField {
    String field;
    boolean mandatory;
    boolean visible;
    int fieldType;

    RegisterField initWithJSon(JSONObject json){

        try {
            field = json.getString("field");
            mandatory = json.getBoolean("mandatory");
            visible = json.getBoolean("visible");
        }catch (Exception e) {
            e.printStackTrace();
        }

        return  this;
   }
}
