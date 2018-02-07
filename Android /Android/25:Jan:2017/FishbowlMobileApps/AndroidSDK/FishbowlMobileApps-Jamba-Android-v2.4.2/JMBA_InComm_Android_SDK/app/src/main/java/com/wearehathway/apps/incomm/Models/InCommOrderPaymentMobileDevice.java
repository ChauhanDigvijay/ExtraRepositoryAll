package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommOrderPaymentMobileDevice
{
    private String Id;
    private double Latitude	;
    private double Longitude;

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public double getLatitude()
    {
        return Latitude;
    }

    public void setLatitude(double latitude)
    {
        Latitude = latitude;
    }

    public double getLongitude()
    {
        return Longitude;
    }

    public void setLongitude(double longitude)
    {
        Longitude = longitude;
    }
}
