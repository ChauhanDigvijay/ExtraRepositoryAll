package com.donatos.phoenix.ui.checkout.view;

import com.donatos.phoenix.network.common.MenuRecipe;
import p027b.p041c.p042d.C1212p;

public final /* synthetic */ class C2716h implements C1212p {
    private final Integer f8524a;

    public C2716h(Integer num) {
        this.f8524a = num;
    }

    public final boolean test(Object obj) {
        return ((MenuRecipe) obj).getId().equals(this.f8524a);
    }
}
