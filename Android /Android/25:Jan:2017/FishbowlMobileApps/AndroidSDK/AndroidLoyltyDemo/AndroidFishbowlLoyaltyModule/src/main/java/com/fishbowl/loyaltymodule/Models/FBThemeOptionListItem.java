package com.fishbowl.loyaltymodule.Models;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 05/06/17.
 */

public class FBThemeOptionListItem {


    public  int id;
    public String fieldName;
    public String displayName;
    public  boolean active;
    public boolean deleted;


    public static  String ID= "id";
    public static  String FIELDNAME= "fieldName";
    public static  String DISPLAYNAME= "displayName";
    public static  String ACTIVE= "active";
    public static  String DELETED= "deleted";



    public void initFromJson(JSONObject jsonObj){

        try {
            if (jsonObj.has(ID) && !jsonObj.isNull(ID)) {
                this.id=Integer.parseInt(jsonObj.getString(ID));
            }
            if (jsonObj.has(FIELDNAME) && !jsonObj.isNull(FIELDNAME)) {
                this.fieldName=jsonObj.getString(FIELDNAME);
            }
            if (jsonObj.has(DISPLAYNAME) && !jsonObj.isNull(DISPLAYNAME)) {
                this.displayName=jsonObj.getString(DISPLAYNAME);
            }

            if (jsonObj.has(ACTIVE) && !jsonObj.isNull(ACTIVE)) {
                this.active=jsonObj.getBoolean(ACTIVE);
            }
            if (jsonObj.has(DELETED) && !jsonObj.isNull(DELETED)) {
                this.deleted=jsonObj.getBoolean(DELETED);
            }


        }catch (Exception e){
            e.printStackTrace();
        }}


}
