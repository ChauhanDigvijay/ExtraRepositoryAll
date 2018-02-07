package com.donatos.phoenix.network.searchlocation;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AddressComponent {
    @JsonField(name = {"long_name"})
    private String longName = null;
    @JsonField(name = {"short_name"})
    private String shortName = null;
    @JsonField(name = {"types"})
    private String[] types = null;

    public String getLongName() {
        return this.longName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String[] getTypes() {
        return this.types;
    }

    public AddressComponent longName(String str) {
        this.longName = str;
        return this;
    }

    public void setLongName(String str) {
        this.longName = str;
    }

    public void setShortName(String str) {
        this.shortName = str;
    }

    public void setTypes(String[] strArr) {
        this.types = strArr;
    }

    public AddressComponent shortName(String str) {
        this.shortName = str;
        return this;
    }

    public AddressComponent types(String[] strArr) {
        this.types = strArr;
        return this;
    }
}
