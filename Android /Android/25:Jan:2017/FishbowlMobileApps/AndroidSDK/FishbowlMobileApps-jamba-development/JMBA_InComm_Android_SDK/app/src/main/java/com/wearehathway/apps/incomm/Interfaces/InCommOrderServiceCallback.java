package com.wearehathway.apps.incomm.Interfaces;


import com.wearehathway.apps.incomm.Models.InCommOrder;

/**
 * Created by Nauman Afzaal on 07/08/15.
 */
public interface InCommOrderServiceCallback
{
    public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error);
}
