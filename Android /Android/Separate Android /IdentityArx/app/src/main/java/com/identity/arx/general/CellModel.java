package com.identity.arx.general;

import android.os.Build;
import android.text.TextUtils;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

public class CellModel {
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                if (Character.isWhitespace(c)) {
                    capitalizeNext = true;
                }
                phrase.append(c);
            }
        }
        return phrase.toString();
    }
}
