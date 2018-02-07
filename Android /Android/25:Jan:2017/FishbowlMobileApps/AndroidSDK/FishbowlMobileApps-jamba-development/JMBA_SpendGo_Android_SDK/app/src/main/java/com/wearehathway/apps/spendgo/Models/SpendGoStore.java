package com.wearehathway.apps.spendgo.Models;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoStore
{
    private int id;
    private String name;
    private String code;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String hours;
    private double latitude;
    private double longitude;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    public String getStreet()
    {
        return street;
    }

    public String getCity()
    {
        return city;
    }

    public String getState()
    {
        return state;
    }

    public String getZip()
    {
        return zip;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getHours()
    {
        return hours;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }
}
