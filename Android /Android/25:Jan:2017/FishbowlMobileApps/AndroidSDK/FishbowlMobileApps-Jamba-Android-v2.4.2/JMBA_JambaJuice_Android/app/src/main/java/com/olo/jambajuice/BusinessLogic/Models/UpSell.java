package com.olo.jambajuice.BusinessLogic.Models;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by vt021 on 21/10/17.
 */

public class UpSell implements Serializable {

    private String objectId;
    private String name;
    private String default_image_url;
    private String campaign_image_url;

    public UpSell(ParseObject parseObject) {
        this.objectId = parseObject.getObjectId();
        this.name = parseObject.getString("name");
        this.default_image_url =  parseObject.getString("default_image_url");
        this.campaign_image_url = parseObject.getString("campaign_image_url");
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefault_image_url() {
        return default_image_url;
    }

    public void setDefault_image_url(String default_image_url) {
        this.default_image_url = default_image_url;
    }

    public String getCampaign_image_url() {
        return campaign_image_url;
    }

    public void setCampaign_image_url(String campaign_image_url) {
        this.campaign_image_url = campaign_image_url;
    }
}
