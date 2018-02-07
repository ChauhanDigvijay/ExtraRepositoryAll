package com.wearehathway.apps.incomm.Models;

/**
 * Created by vthink on 26/08/16.
 */
public class InCommStates {
    private String Name;
    private String Code;
    private String CountryCode;
    private String Type;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
