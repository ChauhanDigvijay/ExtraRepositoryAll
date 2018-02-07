package com.donatos.phoenix.network.validateaddress;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class ValidateAddressAddress {
    @JsonField(name = {"address1"})
    private String address1 = null;
    @JsonField(name = {"address2"})
    private String address2 = null;
    @JsonField(name = {"city"})
    private String city = null;
    @JsonField(name = {"state"})
    private String state = null;
    @JsonField(name = {"zipcode"})
    private String zipcode = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public ValidateAddressAddress address1(String str) {
        this.address1 = str;
        return this;
    }

    public ValidateAddressAddress address2(String str) {
        this.address2 = str;
        return this;
    }

    public ValidateAddressAddress city(String str) {
        this.city = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ValidateAddressAddress validateAddressAddress = (ValidateAddressAddress) obj;
        return C2507k.m7346a(this.address1, validateAddressAddress.address1) && C2507k.m7346a(this.address2, validateAddressAddress.address2) && C2507k.m7346a(this.city, validateAddressAddress.city) && C2507k.m7346a(this.state, validateAddressAddress.state) && C2507k.m7346a(this.zipcode, validateAddressAddress.zipcode);
    }

    public String getAddress1() {
        return this.address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.address1, this.address2, this.city, this.state, this.zipcode});
    }

    public void setAddress1(String str) {
        this.address1 = str;
    }

    public void setAddress2(String str) {
        this.address2 = str;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setState(String str) {
        this.state = str;
    }

    public void setZipcode(String str) {
        this.zipcode = str;
    }

    public ValidateAddressAddress state(String str) {
        this.state = str;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class ValidateAddressAddress {\n");
        stringBuilder.append("    address1: ").append(toIndentedString(this.address1)).append("\n");
        stringBuilder.append("    address2: ").append(toIndentedString(this.address2)).append("\n");
        stringBuilder.append("    city: ").append(toIndentedString(this.city)).append("\n");
        stringBuilder.append("    state: ").append(toIndentedString(this.state)).append("\n");
        stringBuilder.append("    zipcode: ").append(toIndentedString(this.zipcode)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public ValidateAddressAddress zipcode(String str) {
        this.zipcode = str;
        return this;
    }
}
