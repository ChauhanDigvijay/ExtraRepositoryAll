package com.donatos.phoenix.network.pushnotification;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class FishbowlProfileResponse {
    @JsonField(name = {"message"})
    private String message = null;
    @JsonField(name = {"successFlag"})
    private boolean success;

    public String getMessage() {
        return this.message;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public FishbowlProfileResponse message(String str) {
        this.message = str;
        return this;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setSuccess(boolean z) {
        this.success = z;
    }

    public FishbowlProfileResponse success(boolean z) {
        this.success = z;
        return this;
    }
}
