package com.donatos.phoenix.network.pushnotification;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class FishbowlProfile {
    @JsonField(name = {"deviceId"})
    private String deviceId = null;
    @JsonField(name = {"email"})
    private String email = null;
    @JsonField(name = {"location"})
    private String location = "1318";
    @JsonField(name = {"phoneNumber"})
    private String phone = null;
    @JsonField(name = {"pushToken"})
    private String pushToken = null;

    public FishbowlProfile deviceId(String str) {
        this.deviceId = str;
        return this;
    }

    public FishbowlProfile email(String str) {
        this.email = str;
        return this;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLocation() {
        return this.location;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPushToken() {
        return this.pushToken;
    }

    public FishbowlProfile location(String str) {
        this.location = str;
        return this;
    }

    public FishbowlProfile phone(String str) {
        this.phone = str;
        return this;
    }

    public FishbowlProfile pushToken(String str) {
        this.pushToken = str;
        return this;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setLocation(String str) {
        this.location = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public void setPushToken(String str) {
        this.pushToken = str;
    }
}
