package com.fishbowl.basicmodule.Models;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 31/05/17.
 */

public class FBThemeRegistrationFieldSetting {

    public  int id;
    public int deviceTypeId;
    public String configGroup;
    public String configName;
    public String configValue;
    public String description;
    public  boolean active;
    public boolean mandatory;




    public static  String ID= "id";
    public static  String DEVICETYPEID= "deviceTypeId";
    public static  String CONFIGGROUP= "configGroup";
    public static  String CONFIGNAME= "configName";
    public static  String CONFIGVALUE= "configValue";
    public static  String DESCRIPTION= "description";
    public static  String ACTIVE= "active";
    public static  String MANDATORY= "mandatory";


    public void initFromJson(JSONObject jsonObj){

        try {
            if (jsonObj.has(ID) && !jsonObj.isNull(ID)) {
                this.id=Integer.parseInt(jsonObj.getString(ID));

                if (jsonObj.has(DEVICETYPEID) && !jsonObj.isNull(DEVICETYPEID)) {
                    this.deviceTypeId=Integer.parseInt(jsonObj.getString(DEVICETYPEID));
                }

            }
            if (jsonObj.has(CONFIGGROUP) && !jsonObj.isNull(CONFIGGROUP)) {
                this.configGroup=jsonObj.getString(CONFIGGROUP);
            }
            if (jsonObj.has(CONFIGNAME) && !jsonObj.isNull(CONFIGNAME)) {
                this.configName=jsonObj.getString(CONFIGNAME);
            }
            if (jsonObj.has(CONFIGVALUE) && !jsonObj.isNull(CONFIGVALUE)) {
                this.configValue=jsonObj.getString(CONFIGVALUE);
            }
            if (jsonObj.has(DESCRIPTION) && !jsonObj.isNull(DESCRIPTION)) {
                this.description=jsonObj.getString(DESCRIPTION);
            }

            if (jsonObj.has(ACTIVE) && !jsonObj.isNull(ACTIVE)) {
                this.active=jsonObj.getBoolean(ACTIVE);
            }

            if (jsonObj.has(MANDATORY) && !jsonObj.isNull(MANDATORY)) {
                this.mandatory=jsonObj.getBoolean(MANDATORY);
            }


        }catch (Exception e){
            e.printStackTrace();
        }}

}



