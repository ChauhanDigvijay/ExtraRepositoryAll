package com.fishbowl.loyaltymodule.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */

public class Field {

   public  ArrayList<RegisterField> registerFields=new  ArrayList<RegisterField>();

    public boolean  FirstName;

    public boolean  LastName;

    public boolean  Address;

    public boolean  ZipCode;

    public boolean  FavoriteStore;

    public boolean  City;

    public boolean  Gender;

    public boolean  EmailAddress;

    public boolean  SMSOptIn;

    public boolean  EmailOptIn;

    public boolean  DOB;

    public boolean  State;

    public boolean  PhoneNumber;

    public boolean Password;

    public boolean  EmailVerify;

    public boolean  PushOptin;
    public boolean  Country;
    public boolean Bonus;

    public void initFromJson(JSONArray jsonArr) {

            try {

                int lenth=jsonArr.length();

                for (int i=0; i<lenth; i++){

                    RegisterField field=new RegisterField();

                    JSONObject jsonObj= (JSONObject)jsonArr.get(i);

                    field.initWithJSon(jsonObj);
                    registerFields.add(field);
                    Collections.sort(registerFields,Collections.reverseOrder(new MySalaryComp()));

                    // I need to improve it
                    if (jsonObj.getString("field").equals("FirstName")  ) {
                        FirstName=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }
                    if (jsonObj.getString("field").equals("LastName") ) {
                        LastName=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("Address") ) {
                        Address=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("ZipCode") ) {
                        ZipCode=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("FavoriteStore")  ) {
                        FavoriteStore=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("City")  ) {
                        City=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("Gender")  ) {
                        Gender=jsonObj.getBoolean("visible");
                        field.fieldType=1;
                    }

                    if (jsonObj.getString("field").equals("EmailAddress") ) {
                        EmailAddress=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("SMSOptIn")  ) {
                        SMSOptIn=jsonObj.getBoolean("visible");
                        field.fieldType=2;
                    }

                    if (jsonObj.getString("field").equals("EmailOptIn") ) {
                        EmailOptIn=jsonObj.getBoolean("visible");
                        field.fieldType=2;
                    }

                    if (jsonObj.getString("field").equals("DOB")  ) {
                        DOB=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("PhoneNumber")  ) {
                        PhoneNumber=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("Country")  ) {
                        Country=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("State")  ) {
                        State=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("Password")  ) {
                        Password=jsonObj.getBoolean("visible");
                        field.fieldType=0;
                    }

                    if (jsonObj.getString("field").equals("EmailVerify") ) {
                        EmailVerify=jsonObj.getBoolean("visible");
                        field.fieldType=2;
                    }

                    if (jsonObj.getString("field").equals("pushOptIn") ) {
                        PushOptin=jsonObj.getBoolean("visible");
                        field.fieldType=2;
                    }

                    if (jsonObj.getString("field").equals("Bonus") ) {
                        Bonus=jsonObj.getBoolean("visible");
                        field.fieldType=2;

                    }

                }

            }catch (Exception e){

                e.printStackTrace();
            }
    }


    class MySalaryComp implements Comparator<RegisterField> {

        @Override
        public int compare(RegisterField e1, RegisterField e2) {
            if(e1.getConfigDisplaySeq() < e2.getConfigDisplaySeq()){
                return 1;
            } else {
                return -1;
            }
        }
    }

}
