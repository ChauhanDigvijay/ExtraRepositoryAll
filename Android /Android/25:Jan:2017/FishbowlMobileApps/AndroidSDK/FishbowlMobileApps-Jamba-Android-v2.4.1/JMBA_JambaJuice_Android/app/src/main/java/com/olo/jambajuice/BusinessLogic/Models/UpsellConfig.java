package com.olo.jambajuice.BusinessLogic.Models;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by vt021 on 21/10/17.
 */

public class UpsellConfig implements Serializable {

    private String objectId;
    private int rotationInterval;

    public UpsellConfig(ParseObject parseObject) {
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
