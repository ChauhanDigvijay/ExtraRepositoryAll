package com.wearehathway.apps.spendgo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoUser
{
    private String phone;
    private String spendgo_id;
    private boolean sms_opt_in;
    private boolean push_opt_in;
    private String email;
    private boolean email_opt_in;
    private String first_name;
    private String last_name;
    private String dob;
    private String gender;
    private String marital_status;
    private String street;
    private String state;
    private String city;
    private String zip;
    private ArrayList<SpendGoOptional> addtl_info;
    private SpendGoStore favorite_store;

    public String getPhone()
    {
        return phone;
    }

    public String getSpendgo_id()
    {
        return spendgo_id;
    }

    public boolean isSms_opt_in()
    {
        return sms_opt_in;
    }

    public String getEmail()
    {
        return email;
    }

    public boolean isEmail_opt_in()
    {
        return email_opt_in;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public String getDob()
    {
        return dob;
    }

    public String getGender()
    {
        return gender;
    }

    public String getMarital_status()
    {
        return marital_status;
    }

    public String getCity()
    {
        return city;
    }

    public String getZip()
    {
        return zip;
    }

    public ArrayList<SpendGoOptional> getAddtl_info()
    {
        return addtl_info;
    }

    public SpendGoStore getFavorite_store()
    {
        return favorite_store;
    }

    public String getStreet()
    {
        return street;
    }

    public String getState()
    {
        return state;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setSpendgo_id(String spendgo_id)
    {
        this.spendgo_id = spendgo_id;
    }

    public void setSms_opt_in(boolean sms_opt_in)
    {
        this.sms_opt_in = sms_opt_in;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setEmail_opt_in(boolean email_opt_in)
    {
        this.email_opt_in = email_opt_in;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public void setDob(String dob)
    {
        this.dob = dob;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public void setMarital_status(String marital_status)
    {
        this.marital_status = marital_status;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public void setAddtl_info(ArrayList<SpendGoOptional> addtl_info)
    {
        this.addtl_info = addtl_info;
    }

    public void setFavorite_store(SpendGoStore favorite_store)
    {
        this.favorite_store = favorite_store;
    }

    public boolean isPush_opt_in() {
        return push_opt_in;
    }

    public void setPush_opt_in(boolean push_opt_in) {
        this.push_opt_in = push_opt_in;
    }
}
