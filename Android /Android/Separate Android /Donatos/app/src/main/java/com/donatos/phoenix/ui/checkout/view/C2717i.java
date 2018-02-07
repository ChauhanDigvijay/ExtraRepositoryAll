package com.donatos.phoenix.ui.checkout.view;

import com.donatos.phoenix.network.common.MenuSize;
import p027b.p041c.p042d.C1212p;

public final /* synthetic */ class C2717i implements C1212p {
    private final Integer f8525a;

    public C2717i(Integer num) {
        this.f8525a = num;
    }

    public final boolean test(Object obj) {
        return ((MenuSize) obj).getId().equals(this.f8525a);
    }
}
