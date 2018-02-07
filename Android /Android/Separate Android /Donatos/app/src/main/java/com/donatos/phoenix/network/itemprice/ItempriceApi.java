package com.donatos.phoenix.network.itemprice;

import com.donatos.phoenix.network.common.CartItem;
import p027b.p041c.C1295k;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ItempriceApi {
    @POST("itemPrice")
    C1295k<ItemPriceResponse> fetchItemPrice(@Body CartItem cartItem);
}
