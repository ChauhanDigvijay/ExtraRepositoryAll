package com.fishbowl.basicmodule.Models;

import org.json.JSONObject;


/**
 * Created by digvijaychauhan on 29/05/17.
 */

public class FBThemeRegistrationField {



    public  int id;
    public int tenantId;
    public String name;
    public String displayName;
    public String confirmationText;
    public  int displaySeq;
    public boolean required;
    public  boolean locked;
    public boolean indexed;
    public  int maximumLength;
    public  boolean editable;
    public boolean dataEditable;
    public  boolean customField;
    public  int displayType;
    public String profileFieldType;
    public String databaseName;
    public String dataType;
    public String regularExpression;
    public String extendedAttributes;
    public  int themeId;
    public String defaultValue;


    public static  String ID= "id";
    public static  String TENANTID= "tenantId";
    public static  String NAME= "name";
    public static  String DISPLAYNAME= "displayName";
    public static  String DISPLAYSEQ= "displaySeq";
    public static  String REQUIRED= "required";
    public static  String THEMEID= "themeId";
    public static  String DATABASENAME= "databaseName";
    public static  String CUSTOMFIELD= "customField";

    public void initFromJson(JSONObject jsonObj){

        try {
            if (jsonObj.has(ID) && !jsonObj.isNull(ID)) {
               this.id=Integer.parseInt(jsonObj.getString(ID));
            }
            if (jsonObj.has(NAME) && !jsonObj.isNull(NAME)) {
                this.name=jsonObj.getString(NAME);
            }
            if (jsonObj.has(DISPLAYNAME) && !jsonObj.isNull(DISPLAYNAME)) {
                this.displayName=jsonObj.getString(DISPLAYNAME);
            }
            if (jsonObj.has(DISPLAYSEQ) && !jsonObj.isNull(DISPLAYSEQ)) {
                this.displaySeq=Integer.parseInt(jsonObj.getString(DISPLAYSEQ));
            }

            if (jsonObj.has(REQUIRED) && !jsonObj.isNull(REQUIRED)) {
                this.required=jsonObj.getBoolean(REQUIRED);
            }

            if (jsonObj.has(THEMEID) && !jsonObj.isNull(THEMEID)) {
                this.themeId=Integer.parseInt(jsonObj.getString(THEMEID));
            }
            if (jsonObj.has(TENANTID) && !jsonObj.isNull(TENANTID)) {
                this.tenantId=Integer.parseInt(jsonObj.getString(TENANTID));
            }
            if (jsonObj.has(DATABASENAME) && !jsonObj.isNull(DATABASENAME)) {
                this.databaseName=jsonObj.getString(DATABASENAME);
            }
            if (jsonObj.has(CUSTOMFIELD) && !jsonObj.isNull(CUSTOMFIELD)) {
                this.customField=jsonObj.getBoolean(CUSTOMFIELD);
            }

        }catch (Exception e){
            e.printStackTrace();
        }}

    public int getConfigDisplaySeq() {
        return displaySeq;
    }

    public void setConfigDisplaySeq(int displaySeq) {
        this.displaySeq = displaySeq;
    }

}
