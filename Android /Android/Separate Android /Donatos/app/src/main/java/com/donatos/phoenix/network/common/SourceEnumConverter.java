package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import p027b.p068d.C1775h;

public class SourceEnumConverter extends StringBasedTypeConverter<C1775h> {
    public String convertToString(C1775h c1775h) {
        return c1775h.toString();
    }

    public C1775h getFromString(String str) {
        return C1775h.valueOf(str);
    }
}
