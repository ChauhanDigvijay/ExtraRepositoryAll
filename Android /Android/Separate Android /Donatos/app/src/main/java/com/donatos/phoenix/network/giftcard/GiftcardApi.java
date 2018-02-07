package com.donatos.phoenix.network.giftcard;

import p027b.p041c.C1334s;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GiftcardApi {
    @GET("giftCard/{gift_card_number}")
    C1334s<GiftCardResponse> getGiftcard(@Path("gift_card_number") String str);
}
