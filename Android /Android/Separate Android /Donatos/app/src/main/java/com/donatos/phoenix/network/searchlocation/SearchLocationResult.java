package com.donatos.phoenix.network.searchlocation;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SearchLocationResult {
    @JsonField(name = {"address_components"})
    private AddressComponent[] addressComponents = null;
    @JsonField(name = {"formatted_address"})
    private String formattedAddress = null;
    @JsonField(name = {"geometry"})
    private Geometry geometry = null;
    @JsonField(name = {"place_id"})
    private String placeId = null;
    @JsonField(name = {"types"})
    private String[] types = null;

    public SearchLocationResult addressComponents(AddressComponent[] addressComponentArr) {
        this.addressComponents = addressComponentArr;
        return this;
    }

    public SearchLocationResult formattedAddress(String str) {
        this.formattedAddress = str;
        return this;
    }

    public SearchLocationResult geometry(Geometry geometry) {
        this.geometry = geometry;
        return this;
    }

    public AddressComponent[] getAddressComponents() {
        return this.addressComponents;
    }

    public String getFormattedAddress() {
        return this.formattedAddress;
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public String[] getTypes() {
        return this.types;
    }

    public SearchLocationResult placeId(String str) {
        this.placeId = str;
        return this;
    }

    public void setAddressComponents(AddressComponent[] addressComponentArr) {
        this.addressComponents = addressComponentArr;
    }

    public void setFormattedAddress(String str) {
        this.formattedAddress = str;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void setPlaceId(String str) {
        this.placeId = str;
    }

    public void setTypes(String[] strArr) {
        this.types = strArr;
    }

    public SearchLocationResult types(String[] strArr) {
        this.types = strArr;
        return this;
    }
}
