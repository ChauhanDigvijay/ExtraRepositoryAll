package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import org.json.JSONObject;




/**
 * Created by Digvijay on 26-Nov-16.
 */

public class LoyaltyMessages {
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(String messageSent) {
        this.messageSent = messageSent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String  messageSent;
    public String  message;

    public Boolean  isRead;

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public  LoyaltyMessages(JSONObject jsonObj){

        try {
            id = jsonObj.has("id") ? jsonObj.getInt("id") : 0;
            message =jsonObj.has("message")? jsonObj.getString("message"):null;
            messageSent =jsonObj.has("messageSent")? jsonObj.getString("messageSent"):null;


            if(jsonObj.has("isRead"))
            {
                isRead =Boolean.valueOf(jsonObj.getString("isRead").equalsIgnoreCase("true"));
            }
            else
            {
                isRead =Boolean.valueOf(jsonObj.getString("isRead").equalsIgnoreCase("false"));
            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }


}
