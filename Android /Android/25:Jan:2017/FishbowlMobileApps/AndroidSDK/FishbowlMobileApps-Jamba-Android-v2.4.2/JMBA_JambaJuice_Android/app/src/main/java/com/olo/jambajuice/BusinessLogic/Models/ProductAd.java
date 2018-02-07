package com.olo.jambajuice.BusinessLogic.Models;

import com.parse.ParseObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by vthink on 20/01/17.
 */

public class ProductAd implements Serializable{

    private String objectId;
    private int rotationInterval;

    public ProductAd(){

    }
    public ProductAd(ParseObject parseObject) {
        this.objectId = parseObject.getObjectId();
        this.rotationInterval = parseObject.getInt("rotation_interval");
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getRotationInterval() {
        return rotationInterval;
    }

    public void setRotationInterval(int rotationInterval) {
        this.rotationInterval = rotationInterval;
    }
}
