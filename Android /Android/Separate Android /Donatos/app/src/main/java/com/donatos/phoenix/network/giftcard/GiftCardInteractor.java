package com.donatos.phoenix.network.giftcard;

import p027b.p040b.C1151b;
import p027b.p040b.C1167n;
import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class GiftCardInteractor {
    private final GiftcardApi mGiftcardApi;
    private final C1151b<GiftCardResponse> mGiftcardResponseProvider;

    public GiftCardInteractor(GiftcardApi giftcardApi, C1167n c1167n) {
        this.mGiftcardApi = giftcardApi;
        this.mGiftcardResponseProvider = c1167n.m4651a().m4629a("giftcard");
    }

    public C1295k<GiftCardResponse> getGiftcard(String str) {
        return this.mGiftcardApi.getGiftcard(str).m4870a(this.mGiftcardResponseProvider.m4634a()).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }
}
