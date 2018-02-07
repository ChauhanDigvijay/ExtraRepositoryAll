package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloOrderStatus;

import java.io.Serializable;

/**
 * Created by Ihsanulhaq on 6/18/2015.
 */
public class RecentOrderSummary implements Serializable {
    private float amount;
    private String readyTime;
    private String orderTimeStatement;
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

    public RecentOrderSummary(JambaOrderStatus status,String orderTimeStatement) {
        this.amount = status.getTotal();
        this.readyTime = status.getReadytime();
        this.orderTimeStatement = orderTimeStatement;
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

    public String getOrderTimeStatement() {
        return orderTimeStatement;
    }

    public void setOrderTimeStatement(String orderTimeStatement) {
        this.orderTimeStatement = orderTimeStatement;
    }
}
