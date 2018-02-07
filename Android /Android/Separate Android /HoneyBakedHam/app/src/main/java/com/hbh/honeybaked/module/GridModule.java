package com.hbh.honeybaked.module;

import java.io.Serializable;

public class GridModule implements Serializable {
    String p_short_desc;
    int productId;
    double productPrize;
    String sImageUrl;
    String sProductDescription;
    String sProductName;

    public GridModule(int productId, String sProductName, String sImageUrl, double productPrize, String sProductDescription, String p_short_desc) {
        this.productId = productId;
        this.sProductName = sProductName;
        this.sImageUrl = sImageUrl;
        this.productPrize = productPrize;
        this.sProductDescription = sProductDescription;
        this.p_short_desc = p_short_desc;
    }

    public String getsProductDescription() {
        return this.sProductDescription;
    }

    public void setsProductDescription(String sProductDescription) {
        this.sProductDescription = sProductDescription;
    }

    public double getProductPrize() {
        return this.productPrize;
    }

    public void setProductPrize(double productPrize) {
        this.productPrize = productPrize;
    }

    public String getsImageUrl() {
        return this.sImageUrl;
    }

    public void setsImageUrl(String sImageUrl) {
        this.sImageUrl = sImageUrl;
    }

    public String getsProductName() {
        return this.sProductName;
    }

    public void setsProductName(String sProductName) {
        this.sProductName = sProductName;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getP_short_desc() {
        return this.p_short_desc;
    }

    public void setP_short_desc(String p_short_desc) {
        this.p_short_desc = p_short_desc;
    }
}
