package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloDeliveryAddress;

import java.io.Serializable;

/**
 * Created by VT027 on 5/22/2017.
 */

public class DeliveryAddress implements Serializable {
    private int id;
    private String building;
    private String city;
    private String phonenumber;
    private String specialinstructions;
    private String streetaddress;
    private String zipcode;


    public DeliveryAddress() {

    }

    public DeliveryAddress(OloDeliveryAddress oloDeliveryAddress) {
        if (oloDeliveryAddress != null) {
            this.id = oloDeliveryAddress.getId();
            this.building = oloDeliveryAddress.getBuilding();
            this.city = oloDeliveryAddress.getCity();
            this.phonenumber = oloDeliveryAddress.getPhonenumber();
            this.specialinstructions = oloDeliveryAddress.getSpecialinstructions();
            this.streetaddress = oloDeliveryAddress.getStreetaddress();
            this.zipcode = oloDeliveryAddress.getZipcode();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSpecialinstructions() {
        return specialinstructions;
    }

    public void setSpecialinstructions(String specialinstructions) {
        this.specialinstructions = specialinstructions;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
