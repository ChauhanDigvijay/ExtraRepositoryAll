package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Field {

    public boolean FirstName;
    public boolean LastName;
    public boolean Address;
    public boolean ZipCode;
    public boolean FavoriteStore;
    public boolean City;
    public boolean Gender;
    public boolean EmailAddress;
    public boolean SMSOptIn;
    public boolean EmailOptIn;
    public boolean DOB;
    public boolean State;
    public boolean PhoneNumber;
    public boolean Password;
    public boolean EmailVerify;
    public boolean PushOptin;
    public boolean Country;
    ArrayList<RegisterField> registerFields = new ArrayList<RegisterField>();

    public void initFromJson(JSONArray jsonArr) {

        try {

            int lenth = jsonArr.length();

            for (int i = 0; i < lenth; i++) {

                RegisterField field = new RegisterField();

                JSONObject jsonObj = (JSONObject) jsonArr.get(i);

                field.initWithJSon(jsonObj);
                registerFields.add(field);

                // I need to improve it
                if (jsonObj.getString("field").equals("FirstName")) {
                    FirstName = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }
                if (jsonObj.getString("field").equals("LastName")) {
                    LastName = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("Address")) {
                    Address = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("ZipCode")) {
                    ZipCode = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("FavoriteStore")) {
                    FavoriteStore = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("City")) {
                    City = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("Gender")) {
                    Gender = jsonObj.getBoolean("visible");
                    field.fieldType = 1;
                }

                if (jsonObj.getString("field").equals("EmailAddress")) {
                    EmailAddress = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("SMSOptIn")) {
                    SMSOptIn = jsonObj.getBoolean("visible");
                    field.fieldType = 2;
                }

                if (jsonObj.getString("field").equals("EmailOptIn")) {
                    EmailOptIn = jsonObj.getBoolean("visible");
                    field.fieldType = 2;
                }

                if (jsonObj.getString("field").equals("DOB")) {
                    DOB = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("PhoneNumber")) {
                    PhoneNumber = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }


                if (jsonObj.getString("field").equals("Country")) {
                    Country = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("State")) {
                    State = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }


                if (jsonObj.getString("field").equals("Password")) {
                    Password = jsonObj.getBoolean("visible");
                    field.fieldType = 0;
                }

                if (jsonObj.getString("field").equals("EmailVerify")) {
                    EmailVerify = jsonObj.getBoolean("visible");
                    field.fieldType = 2;
                }

                if (jsonObj.getString("field").equals("pushOptIn")) {
                    PushOptin = jsonObj.getBoolean("visible");
                    field.fieldType = 2;
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
