package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloOrderStatus;

import java.io.Serializable;

/**
 * Created by VT027 on 5/20/2017.
 */

class RecentOrderSummary implements Serializable {
    private float amount;
    private String readyTime;
    private DeliveryAddress deliveryaddress;
    private String deliverymode;
    private String deliveryid;
    private String deliverystatus;
    private String drivername;
    private String driverphonenumber;
    private String deliveryservice;

    public RecentOrderSummary(OloOrderStatus status) {
        this.amount = status.getTotal();
        this.readyTime = status.getReadytime();
    }

    public RecentOrderSummary(CbcOrderStatus status) {
        this.amount = status.getTotal();
        this.readyTime = status.getReadytime();
        this.deliverystatus = status.getStatus();
        this.deliverymode = status.getDeliverymode();
        this.deliveryaddress = status.getDeliveryaddress();
        this.deliveryservice = status.getDeliveryservice();
        this.drivername = status.getDrivername();
        this.driverphonenumber = status.getDriverphonenumber();
    }

    public float getAmount() {
        return amount;
    }

    public String getReadyTime() {
        return readyTime;
    }


    public String getDeliverymode() {
        return deliverymode;
    }

    public DeliveryAddress getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(DeliveryAddress deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

    public String getDeliverystatus() {
        return deliverystatus;
    }

    public void setDeliverystatus(String deliverystatus) {
        this.deliverystatus = deliverystatus;
    }

    public String getDeliveryid() {
        return deliveryid;
    }

    public void setDeliveryid(String deliveryid) {
        this.deliveryid = deliveryid;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriverphonenumber() {
        return driverphonenumber;
    }

    public void setDriverphonenumber(String driverphonenumber) {
        this.driverphonenumber = driverphonenumber;
    }

    public String getDeliveryservice() {
        return deliveryservice;
    }

    public void setDeliveryservice(String deliveryservice) {
        this.deliveryservice = deliveryservice;
    }
}
