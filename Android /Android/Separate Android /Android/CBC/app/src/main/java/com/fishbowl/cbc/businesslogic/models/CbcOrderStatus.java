package com.fishbowl.cbc.businesslogic.models;

import java.util.ArrayList;

/**
 * Created by VT027 on 5/20/2017.
 */

public class CbcOrderStatus {
    private String id;
    private String timeplaced;
    private String vendorname;
    private String status;
    private float total;
    private float subtotal;
    private float salestax;
    private String orderref;
    private String readytime;
    private String vendorextref;
    private String deliverymode;
    private String name;
    private boolean online;
    private String vendorid;
    private boolean disabled;
    private String drivername;
    private String driverphonenumber;
    private String deliveryservice;
    private DeliveryAddress deliveryaddress;

    private ArrayList<CbcOrderSatusProduct> products;

    public String getId() {
        return id;
    }

    public String getTimeplaced() {
        return timeplaced;
    }

    public String getVendorname() {
        return vendorname;
    }

    public String getStatus() {
        return status;
    }

    public float getTotal() {
        return total;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public float getSalestax() {
        return salestax;
    }

    public String getOrderref() {
        return orderref;
    }

    public String getReadytime() {
        return readytime;
    }

    public String getVendorextref() {
        return vendorextref;
    }

    public String getDeliverymode() {
        return deliverymode;
    }


    public boolean isOnline() {
        return online;
    }

    public String getName() {
        return name;
    }

    public String getVendorid() {
        return vendorid;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public ArrayList<CbcOrderSatusProduct> getProducts() {
        return products;
    }

    public DeliveryAddress getDeliveryaddress() {
        return deliveryaddress;
    }

    public String getDrivername() {
        return drivername;
    }

    public String getDriverphonenumber() {
        return driverphonenumber;
    }

    public String getDeliveryservice() {
        return deliveryservice;
    }
}
