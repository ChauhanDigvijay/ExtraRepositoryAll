package com.donatos.phoenix.network.searchlocation;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Geometry {
    @JsonField(name = {"bounds"})
    private LocationBounds bounds = null;
    @JsonField(name = {"location"})
    private SearchLocationLocation location = null;
    @JsonField(name = {"location_type"})
    private String locationType = null;
    @JsonField(name = {"viewport"})
    private LocationBounds viewport = null;

    public Geometry bounds(LocationBounds locationBounds) {
        this.bounds = locationBounds;
        return this;
    }

    public LocationBounds getBounds() {
        return this.bounds;
    }

    public SearchLocationLocation getLocation() {
        return this.location;
    }

    public String getLocationType() {
        return this.locationType;
    }

    public LocationBounds getViewport() {
        return this.viewport;
    }

    public Geometry location(SearchLocationLocation searchLocationLocation) {
        this.location = searchLocationLocation;
        return this;
    }

    public Geometry locationType(String str) {
        this.locationType = str;
        return this;
    }

    public void setBounds(LocationBounds locationBounds) {
        this.bounds = locationBounds;
    }

    public void setLocation(SearchLocationLocation searchLocationLocation) {
        this.location = searchLocationLocation;
    }

    public void setLocationType(String str) {
        this.locationType = str;
    }

    public void setViewport(LocationBounds locationBounds) {
        this.viewport = locationBounds;
    }

    public Geometry viewport(LocationBounds locationBounds) {
        this.viewport = locationBounds;
        return this;
    }
}
