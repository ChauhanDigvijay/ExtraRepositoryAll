package com.donatos.phoenix.network.searchlocation;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class LocationBounds {
    @JsonField(name = {"northeast"})
    private SearchLocationLocation northeast = null;
    @JsonField(name = {"southwest"})
    private SearchLocationLocation southwest = null;

    public SearchLocationLocation getNortheast() {
        return this.northeast;
    }

    public SearchLocationLocation getSouthwest() {
        return this.southwest;
    }

    public LocationBounds northeast(SearchLocationLocation searchLocationLocation) {
        this.northeast = searchLocationLocation;
        return this;
    }

    public void setNortheast(SearchLocationLocation searchLocationLocation) {
        this.northeast = searchLocationLocation;
    }

    public void setSouthwest(SearchLocationLocation searchLocationLocation) {
        this.southwest = searchLocationLocation;
    }

    public LocationBounds southwest(SearchLocationLocation searchLocationLocation) {
        this.southwest = searchLocationLocation;
        return this;
    }
}
