package com.donatos.phoenix.network.validateaddress;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class ValidateAddressRequest {
    @JsonField(name = {"address"})
    private ValidateAddressAddress address = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public ValidateAddressRequest address(ValidateAddressAddress validateAddressAddress) {
        this.address = validateAddressAddress;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return C2507k.m7346a(this.address, ((ValidateAddressRequest) obj).address);
    }

    public ValidateAddressAddress getAddress() {
        return this.address;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.address});
    }

    public void setAddress(ValidateAddressAddress validateAddressAddress) {
        this.address = validateAddressAddress;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Body {\n");
        stringBuilder.append("    address: ").append(toIndentedString(this.address)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
