package com.fishbowl.cbc.businesslogic.models;

import java.io.Serializable;

/**
 * Created by VT027 on 4/24/2017.
 */

public class ProductFamilyModel implements Serializable {

    private String mTitle;
    private String mThumbnail;

    public ProductFamilyModel() {

    }

    public ProductFamilyModel(String title, String thumbnail) {
        this.mTitle = title;
        this.mThumbnail = thumbnail;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }
}
