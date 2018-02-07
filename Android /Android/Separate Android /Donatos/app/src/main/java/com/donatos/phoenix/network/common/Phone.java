package com.donatos.phoenix.network.common;

import android.telephony.PhoneNumberUtils;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.p134b.C2510n;
import java.util.Arrays;

@JsonObject
public class Phone {
    @JsonField(name = {"areaCode"})
    private String areaCode = null;
    @JsonField(name = {"phoneNumber"})
    private String phoneNumber = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Phone areaCode(String str) {
        this.areaCode = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Phone phone = (Phone) obj;
        return C2507k.m7346a(this.areaCode, phone.areaCode) && C2507k.m7346a(this.phoneNumber, phone.phoneNumber);
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public String getFormattedPhoneNumber() {
        return getFullPhoneNumber() == null ? null : PhoneNumberUtils.formatNumber(getFullPhoneNumber());
    }

    public String getFullPhoneNumber() {
        Object obj = "";
        if (!C2510n.m7367a(getAreaCode())) {
            obj = getAreaCode();
        }
        if (!C2510n.m7367a(getPhoneNumber())) {
            obj = obj + getPhoneNumber();
        }
        return !C2510n.m7367a(obj) ? obj : null;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.areaCode, this.phoneNumber});
    }

    public Phone phoneNumber(String str) {
        this.phoneNumber = str;
        return this;
    }

    public void setAreaCode(String str) {
        this.areaCode = str;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Phone {\n");
        stringBuilder.append("    areaCode: ").append(toIndentedString(this.areaCode)).append("\n");
        stringBuilder.append("    phoneNumber: ").append(toIndentedString(this.phoneNumber)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
