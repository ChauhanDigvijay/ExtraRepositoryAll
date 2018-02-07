package com.fishbowl.loyaltymodule.Models;

import org.json.JSONObject;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class FBAreaTypeItem {

    public static  String AREA_TYPE="areaType";
    public static String  ID = "id";

    public String areaType;
    public String id;

    public void initWithJson(JSONObject jsonObj){

        try {
            if (jsonObj.has(AREA_TYPE) && !jsonObj.isNull(AREA_TYPE)) {
                areaType=jsonObj.getString(AREA_TYPE);
            }

        }catch (Exception e){
            e.printStackTrace();

        }
}
}