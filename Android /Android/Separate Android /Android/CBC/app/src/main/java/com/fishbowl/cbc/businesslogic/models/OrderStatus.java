package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloOrderStatus;

/**
 * Created by VT027 on 5/22/2017.
 */

public class OrderStatus {
    private String id;
    private String vendorname;
    private String readytime;

    // Required For GoogleAnalytics
    private String orderRef;
    private String vendorExtRef;
    private float total;
    private float saleTax;

    private String timePlaced;
    private float subTotal;
    private int productsCount;


    public OrderStatus(OloOrderStatus orderStatus) {
        this.id = orderStatus.getId();
        this.vendorname = orderStatus.getVendorname();
        this.readytime = orderStatus.getReadytime();
        this.orderRef = orderStatus.getOrderref();
        this.vendorExtRef = orderStatus.getVendorextref();
        this.total = orderStatus.getTotal();
        this.saleTax = orderStatus.getSalestax();
        this.timePlaced = orderStatus.getTimeplaced();
        this.subTotal = orderStatus.getSubtotal();
        this.productsCount = orderStatus.getProducts().size();
    }

    public String getId() {
        return id;
    }

    public String getVendorname() {
        return vendorname;
    }

    public String getReadytime() {
        return readytime;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public float getTotal() {
        return total;
    }

    public String getVendorExtRef() {
        return vendorExtRef;
    }

    public float getSaleTax() {
        return saleTax;
    }

    public String getTimePlaced() {
        return timePlaced;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public int getProductsCount() {
        return productsCount;
    }
}
