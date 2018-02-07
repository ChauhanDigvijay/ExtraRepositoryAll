package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.donatos.phoenix.p134b.C2510n;

public class PhoneTypeConverter extends StringBasedTypeConverter<Phone> {
    private static final int PHONE_NUMBER_WITH_AREA_CODE_LENGTH = 10;

    public String convertToString(Phone phone) {
        return phone != null ? phone.getFullPhoneNumber() : null;
    }

    public Phone getFromString(String str) {
        if (C2510n.m7367a(str)) {
            return null;
        }
        Phone phone = new Phone();
        if (str.length() == 10) {
            phone.setAreaCode(str.substring(0, 3));
            phone.setPhoneNumber(str.substring(3, 10));
            return phone;
        }
        phone.setPhoneNumber(str);
        return phone;
    }
}
