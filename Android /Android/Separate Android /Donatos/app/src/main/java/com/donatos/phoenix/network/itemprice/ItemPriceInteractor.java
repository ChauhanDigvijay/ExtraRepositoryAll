package com.donatos.phoenix.network.itemprice;

import com.donatos.phoenix.network.common.CartItem;
import p027b.p040b.C1151b;
import p027b.p040b.C1167n;
import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class ItemPriceInteractor {
    private final ItempriceApi mItemPriceApi;
    private final C1151b<ItemPriceResponse> mItemPriceResponseProvider;

    public ItemPriceInteractor(ItempriceApi itempriceApi, C1167n c1167n) {
        this.mItemPriceApi = itempriceApi;
        this.mItemPriceResponseProvider = c1167n.m4651a().m4629a("itemprice");
    }

    public C1295k<ItemPriceResponse> fetchItemPrice(CartItem cartItem) {
        return this.mItemPriceApi.fetchItemPrice(cartItem).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }
}
