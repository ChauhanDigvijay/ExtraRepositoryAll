package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

/**
 * Created by digvijaychauhan on 09/12/16.
 */


import org.json.JSONObject;

/**
 * Created by mohdvaseem on 17/06/16.
 */
public class EmailOffer {
    public static String message;
    public static String preview;
    public static boolean successFlag;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public EmailOffer initWithJSon(JSONObject json) {

        try {

            message = json.getString("message");
            preview = json.getString("preview");
            successFlag = json.getBoolean("successFlag");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }
}
