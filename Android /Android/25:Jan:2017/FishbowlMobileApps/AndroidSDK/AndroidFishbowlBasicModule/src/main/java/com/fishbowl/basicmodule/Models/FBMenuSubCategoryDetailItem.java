package com.fishbowl.basicmodule.Models;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 28-Dec-16.
 */

public class FBMenuSubCategoryDetailItem implements Serializable {
    private int productSubCategoryId;
    private String productSubCategoryName;
    private String productSubCategoryDescription;
    private Boolean productSubCategoryImageEnable;
    private String productSubCategoryImageUrl;
    private Boolean productSubCategoryThumbImageEnable;
    private String productSubCategoryThumbImageUrl;
    private Boolean active;
    private int displayOrder;
    private String createdDate;
    private String createdBy;
    private String lastUpdatedDate;
    private String lastUpdatedBy;


    public int getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(int productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
    }

    public String getProductSubCategoryName() {
        return productSubCategoryName;
    }

    public void setProductSubCategoryName(String productSubCategoryName) {
        this.productSubCategoryName = productSubCategoryName;
    }

    public String getProductSubCategoryDescription() {
        return productSubCategoryDescription;
    }

    public void setProductSubCategoryDescription(String productSubCategoryDescription) {
        this.productSubCategoryDescription = productSubCategoryDescription;
    }

    public Boolean getProductSubCategoryImageEnable() {
        return productSubCategoryImageEnable;
    }

    public void setProductSubCategoryImageEnable(Boolean productSubCategoryImageEnable) {
        this.productSubCategoryImageEnable = productSubCategoryImageEnable;
    }

    public String getProductSubCategoryImageUrl() {
        return productSubCategoryImageUrl;
    }

    public void setProductSubCategoryImageUrl(String productSubCategoryImageUrl) {
        this.productSubCategoryImageUrl = productSubCategoryImageUrl;
    }

    public Boolean getProductSubCategoryThumbImageEnable() {
        return productSubCategoryThumbImageEnable;
    }

    public void setProductSubCategoryThumbImageEnable(Boolean productSubCategoryThumbImageEnable) {
        this.productSubCategoryThumbImageEnable = productSubCategoryThumbImageEnable;
    }

    public String getProductSubCategoryThumbImageUrl() {
        return productSubCategoryThumbImageUrl;
    }

    public void setProductSubCategoryThumbImageUrl(String productSubCategoryThumbImageUrl) {
        this.productSubCategoryThumbImageUrl = productSubCategoryThumbImageUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }




}
