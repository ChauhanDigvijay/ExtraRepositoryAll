package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

public class States implements Serializable {
    public int stateID;
    public String stateName,stateCode,countryCode;


    public States(){}
    public States(JSONObject obj)
    {
        try{
            stateID = obj.has("stateID") ? obj.getInt("stateID") : 0;
            stateName =obj.has("stateName")? obj.getString("stateName"):null;
            stateCode =obj.has("stateCode")? obj.getString("stateCode"):null;
            countryCode =obj.has("countryCode")? obj.getString("countryCode"):null;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public int getStateID()
    {
        return stateID;
    }
    public void setStateID(int stateID)
    {
        this.stateID = stateID;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }
    public String getStateName()
    {
        return stateName;
    }

    public void setStateCode(String stateCode)
    {
        this.stateCode = stateCode;
    }
    public String getStateCode()
    {
        return stateCode;
    }

    public void getCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCountryCode()
    {
        return countryCode;
    }

}
