package com.fishbowl.cbc.businesslogic.models;

/**
 * Created by VT027 on 5/23/2017.
 */

public interface PlaceOrderCallback {
    public void onPlaceOrderCallback(OrderStatus status, Exception e);
}
