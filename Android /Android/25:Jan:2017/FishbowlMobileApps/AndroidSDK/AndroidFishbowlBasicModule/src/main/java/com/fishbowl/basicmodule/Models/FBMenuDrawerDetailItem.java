package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijay(dj)
 */

public class FBMenuDrawerDetailItem {

    private int id;
    private String productName;
    private String productLongDescription;
    private String productShortDescription;
    private String productTagDescription;
    private  boolean productImageEnable;
    private  String productImageUrl;
    private boolean productThumbImageEnable;
    private String productThumbImageUrl;
    private double productBasePrice;
    private double productBaseVolume;
    private double productBaseWeight;
    private boolean active;
    private String created;
    private  String createdBy;
    private  String lastUpdated;
    private String lastUpdatedBy;
    private long quantity = 0;
    private Double ext = 0.00;
    private Double price = 0.00;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLongDescription() {
        return productLongDescription;
    }

    public void setProductLongDescription(String productLongDescription) {
        this.productLongDescription = productLongDescription;
    }

    public String getProductShortDescription() {
        return productShortDescription;
    }

    public void setProductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public String getProductTagDescription() {
        return productTagDescription;
    }

    public void setProductTagDescription(String productTagDescription) {
        this.productTagDescription = productTagDescription;
    }

    public boolean isProductImageEnable() {
        return productImageEnable;
    }

    public void setProductImageEnable(boolean productImageEnable) {
        this.productImageEnable = productImageEnable;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public boolean isProductThumbImageEnable() {
        return productThumbImageEnable;
    }

    public void setProductThumbImageEnable(boolean productThumbImageEnable) {
        this.productThumbImageEnable = productThumbImageEnable;
    }

    public String getProductThumbImageUrl() {
        return productThumbImageUrl;
    }

    public void setProductThumbImageUrl(String productThumbImageUrl) {
        this.productThumbImageUrl = productThumbImageUrl;
    }

    public double getProductBasePrice() {
        return productBasePrice;
    }

    public void setProductBasePrice(double productBasePrice) {
        this.productBasePrice = productBasePrice;
    }

    public double getProductBaseVolume() {
        return productBaseVolume;
    }

    public void setProductBaseVolume(double productBaseVolume) {
        this.productBaseVolume = productBaseVolume;
    }

    public double getProductBaseWeight() {
        return productBaseWeight;
    }

    public void setProductBaseWeight(double productBaseWeight) {
        this.productBaseWeight = productBaseWeight;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Double getExt() {
        return ext;
    }

    public void setExt(Double ext) {
        this.ext = ext;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }





}

