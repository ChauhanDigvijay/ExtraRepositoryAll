package com.fishbowl.cbc.businesslogic.models;

import com.parse.ParseObject;

/**
 * Created by VT027 on 5/22/2017.
 */

public class ProductFamily {
    protected final int TYPE_ITEM = 0;
    protected final int TYPE_SECTION = 1;

    private String imageUrl;
    private int order;
    private String name;
    private String objectID;

    public ProductFamily(ParseObject parseObject) {
        this.name = parseObject.getString("name");
        this.order = parseObject.getInt("order");
        this.objectID = parseObject.getObjectId();
        this.imageUrl = parseObject.getString("imageUrl");
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getObjectID() {
        return objectID;
    }

    public int getType() {
        return TYPE_SECTION;
    }

    public int getOrder() {
        return order;
    }
}
