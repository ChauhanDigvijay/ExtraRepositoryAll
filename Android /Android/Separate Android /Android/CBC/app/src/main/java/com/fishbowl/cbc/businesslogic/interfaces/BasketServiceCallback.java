package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.Basket;

/**
 * Created by VT027 on 5/22/2017.
 */

public interface BasketServiceCallback {
    public void onBasketServiceCallback(Basket basket, Exception e);
}
