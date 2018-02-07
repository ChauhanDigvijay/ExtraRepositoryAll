package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

/**
 * Created by schaudhary_ic on 26-Nov-16.
 */

public class MessageType {

    public String  messageType;
    public int id;




    public  MessageType(JSONObject jsonObj){

        try {
            id = jsonObj.has("id") ? jsonObj.getInt("id") : 0;
            messageType =jsonObj.has("messageType")? jsonObj.getString("messageType"):null;

        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void setMessageType(String messageType)
    {
        this.messageType = messageType;

    }
    public String getMessageType()
    {
        return messageType;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

}
