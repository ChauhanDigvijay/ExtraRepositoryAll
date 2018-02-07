package com.donatos.phoenix.network;

import android.location.Location;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.HashMap;
import java.util.Map;

@JsonObject
public class MyLocation {
    @JsonField(name = {"accuracy"})
    private Float accuracy;
    private Map<String, Object> additionalProperties = new HashMap();
    @JsonField(name = {"altitude"})
    private Double altitude;
    @JsonField(name = {"bearing"})
    private Float bearing;
    @JsonField(name = {"complete"})
    private Boolean complete;
    @JsonField(name = {"elapsedRealtimeNanos"})
    private Long elapsedRealtimeNanos;
    private Object extras;
    @JsonField(name = {"fromMockProvider"})
    private Boolean fromMockProvider;
    @JsonField(name = {"latitude"})
    private Double latitude;
    @JsonField(name = {"locationId"})
    private int locationId;
    @JsonField(name = {"longitude"})
    private Double longitude;
    @JsonField(name = {"provider"})
    private String provider;
    @JsonField(name = {"speed"})
    private Float speed;
    @JsonField(name = {"time"})
    private Long time;

    public MyLocation(Location location) {
        updateLocation(location);
    }

    public Float getAccuracy() {
        return this.accuracy;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public Double getAltitude() {
        return this.altitude;
    }

    public Float getBearing() {
        return this.bearing;
    }

    public Boolean getComplete() {
        return this.complete;
    }

    public Long getElapsedRealtimeNanos() {
        return this.elapsedRealtimeNanos;
    }

    public Object getExtras() {
        return this.extras;
    }

    public Boolean getFromMockProvider() {
        return this.fromMockProvider;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public int getLocationId() {
        return this.locationId;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public String getProvider() {
        return this.provider;
    }

    public Float getSpeed() {
        return this.speed;
    }

    public Long getTime() {
        return this.time;
    }

    public void setAccuracy(Float f) {
        this.accuracy = f;
    }

    public void setAdditionalProperty(String str, Object obj) {
        this.additionalProperties.put(str, obj);
    }

    public void setAltitude(Double d) {
        this.altitude = d;
    }

    public void setBearing(Float f) {
        this.bearing = f;
    }

    public void setComplete(Boolean bool) {
        this.complete = bool;
    }

    public void setElapsedRealtimeNanos(Long l) {
        this.elapsedRealtimeNanos = l;
    }

    public void setExtras(Object obj) {
        this.extras = obj;
    }

    public void setFromMockProvider(Boolean bool) {
        this.fromMockProvider = bool;
    }

    public void setLatitude(Double d) {
        this.latitude = d;
    }

    public void setLocationId(int i) {
        this.locationId = i;
    }

    public void setLongitude(Double d) {
        this.longitude = d;
    }

    public void setProvider(String str) {
        this.provider = str;
    }

    public void setSpeed(Float f) {
        this.speed = f;
    }

    public void setTime(Long l) {
        this.time = l;
    }

    public void updateLocation(Location location) {
        this.accuracy = Float.valueOf(location.getAccuracy());
        this.altitude = Double.valueOf(location.getAltitude());
        this.bearing = Float.valueOf(location.getBearing());
        this.extras = location.getExtras();
        this.latitude = Double.valueOf(location.getLatitude());
        this.longitude = Double.valueOf(location.getLongitude());
        this.provider = location.getProvider();
        this.speed = Float.valueOf(location.getSpeed());
        this.time = Long.valueOf(location.getTime());
    }
}
