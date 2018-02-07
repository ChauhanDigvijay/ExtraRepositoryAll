package com.olo.jambajuice.BusinessLogic.Models;

import com.parse.ParseObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by vthink on 12/01/17.
 */

public class ProductAdDetail implements Serializable {
    private String objectId;
    private String imageUrl;
    private String linkUrl;
    private int productId;
    private int orderNo;
    private Boolean status;
    private String adName;
    private String categoryId;

    public ProductAdDetail(ParseObject parseObject) {
        this.objectId = parseObject.getObjectId();
        this.imageUrl = parseObject.getString("image_url");
        this.linkUrl =  parseObject.getString("link_url");
        this.productId = parseObject.getInt("product_id");
        this.orderNo = parseObject.getInt("order_no");
        this.status = parseObject.getBoolean("status");
        this.adName = parseObject.getString("ad_name");
        if(parseObject.getParseObject("category") != null) {
            this.categoryId = parseObject.getParseObject("category").getObjectId();
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public int getProductId() {
        return productId;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getAdName(){ return adName;}

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setAdName(String adName){this.adName = adName;}

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
