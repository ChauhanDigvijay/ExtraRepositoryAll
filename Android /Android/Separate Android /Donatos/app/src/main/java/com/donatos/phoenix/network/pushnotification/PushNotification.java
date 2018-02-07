package com.donatos.phoenix.network.pushnotification;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class PushNotification {
    @JsonField(name = {"coupon"})
    private String coupon = null;
    @JsonField(name = {"google.message_id"})
    private String id = null;
    @JsonField(name = {"menuItem"})
    private String menuItem = null;

    public String getCoupon() {
        return this.coupon;
    }

    public String getId() {
        return this.id;
    }

    public String getMenuItem() {
        return this.menuItem;
    }

    public void setCoupon(String str) {
        this.coupon = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setMenuItem(String str) {
        this.menuItem = str;
    }
}
