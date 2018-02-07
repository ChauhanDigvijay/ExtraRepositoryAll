package com.fishbowl.basicmodule.Models;


import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by digvijay(dj)
 */
public class FBMobileSettingObjectItem {

    /**
     * Created by digvijay(dj)
     */
private String configId;
private String tenantId;
private String configGroup;
private String configName;
private String configValue;
private String description;
private String active;
private String created;
private String lastUpdated;


    public void initWithJsonObject(JSONObject json){

        try {
            configId=json.getString("configId");

            tenantId=json.getString("tenantId");;
            configGroup=json.getString("configGroup");;
             configName=json.getString("configName");;
            configValue=json.getString("configValue");;
            description=json.getString("description");;
            active=json.getString("active");;
            created=json.getString("created");;
            lastUpdated=json.getString("lastUpdated");;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
