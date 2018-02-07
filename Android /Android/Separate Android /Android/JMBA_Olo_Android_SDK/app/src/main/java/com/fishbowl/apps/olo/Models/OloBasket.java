package com.fishbowl.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloBasket
{
    private String id; //Guid used to identify this basket,
    private String timewanted; //If placing an advance order, this will be set to a date & time in the format yyyymmdd hh:mm. Otherwise null for ASAP orders,
    private String earliestreadytime; //The earliest time at which this basket can be ready (whether pickup or delivery, asap or advance), yyyymmdd hh:mm,
    private float subtotal; //Subtotal of all products and modifiers in the order,
    private float salestax; //Estimated sales tax on the entire basket,
    private float total; //Estimated order total,
    private float tip;
    private float discount;
    private float coupondiscount;
    private int vendorid;
    private boolean vendoronline ; //Set to True if the vendor is currently accepting orders,
    private String deliverymode; //['pickup' or 'delivery']: ,
    private boolean wasupsold;
    private String timemode;    private ArrayList<OloBasketProduct> products;
    private ArrayList<OloLoyaltyReward> appliedrewards;
    private ArrayList<OloTax> taxes;
    private ArrayList<OloCustomField> customfields;
    private OloDeliveryAddress deliveryaddress;
    private float customerhandoffcharge;
    private String contactnumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeWanted() {
        return timewanted;
    }

    public void setTimeWanted(String timewanted) {
        this.timewanted = timewanted;
    }

    public String getEarliestreadytime() {
        return earliestreadytime;
    }

    public void setEarliestreadytime(String earliestreadytime) {
        this.earliestreadytime = earliestreadytime;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getSalestax() {
        return salestax;
    }

    public void setSalestax(float salestax) {
        this.salestax = salestax;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTip() {
        return tip;
    }

    public void setTip(float tip) {
        this.tip = tip;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getCoupondiscount() {
        return coupondiscount;
    }

    public void setCoupondiscount(float coupondiscount) {
        this.coupondiscount = coupondiscount;
    }

    public int getVendorId() {
        return vendorid;
    }

    public void setVendorId(int vendorid) {
        this.vendorid = vendorid;
    }

    public boolean isVendoronline() {
        return vendoronline;
    }

    public void setVendoronline(boolean vendoronline) {
        this.vendoronline = vendoronline;
    }

    public String getDeliverymode() {
        return deliverymode;
    }

    public void setDeliverymode(String deliverymode) {
        this.deliverymode = deliverymode;
    }

    public boolean isWasupsold() {
        return wasupsold;
    }

    public void setWasupsold(boolean wasupsold) {
        this.wasupsold = wasupsold;
    }

    public String getTimemode() {
        return timemode;
    }

    public void setTimemode(String timemode) {
        this.timemode = timemode;
    }

    public ArrayList<OloBasketProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<OloBasketProduct> products) {
        this.products = products;
    }

    public ArrayList<OloLoyaltyReward> getAppliedrewards() {
        return appliedrewards;
    }

    public void setAppliedrewards(ArrayList<OloLoyaltyReward> appliedrewards) {
        this.appliedrewards = appliedrewards;
    }

    public ArrayList<OloTax> getTaxes() {
        return taxes;
    }

    public void setTaxes(ArrayList<OloTax> taxes) {
        this.taxes = taxes;
    }

    public ArrayList<OloCustomField> getCustomfields() {
        return customfields;
    }

    public void setCustomfields(ArrayList<OloCustomField> customfields) {
        this.customfields = customfields;
    }

    public float getCustomerhandoffcharge() {
        return customerhandoffcharge;
    }

    public void setCustomerhandoffcharge(float customerhandoffcharge) {
        this.customerhandoffcharge = customerhandoffcharge;
    }

    public OloDeliveryAddress getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(OloDeliveryAddress deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
}
