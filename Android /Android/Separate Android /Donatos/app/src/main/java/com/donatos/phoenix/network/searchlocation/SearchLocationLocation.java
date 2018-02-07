package com.donatos.phoenix.network.searchlocation;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SearchLocationLocation {
    @JsonField(name = {"lat"})
    private Double latitude = null;
    @JsonField(name = {"lng"})
    private Double longitude = null;

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public SearchLocationLocation latitude(Double d) {
        this.latitude = d;
        return this;
    }

    public SearchLocationLocation longitude(Double d) {
        this.longitude = d;
        return this;
    }

    public void setLatitude(Double d) {
        this.latitude = d;
    }

    public void setLongitude(Double d) {
        this.longitude = d;
    }
}
