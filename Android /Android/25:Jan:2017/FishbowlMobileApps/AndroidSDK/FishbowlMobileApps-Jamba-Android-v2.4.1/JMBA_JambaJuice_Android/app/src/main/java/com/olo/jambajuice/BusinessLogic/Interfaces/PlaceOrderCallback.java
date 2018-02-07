package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;

/**
 * Created by Ihsanulhaq on 6/1/2015.
 */
public interface PlaceOrderCallback {
    public void onPlaceOrderCallback(OrderStatus status, Exception e);
}
