package com.wearehathway.apps.incomm.Interfaces;


import com.wearehathway.apps.incomm.Models.InCommCard;

import java.util.List;

/**
 * Created by vthink on 18/08/16.
 */
public interface InCommGetAllCardServiceCallBack {
    public void onGetAllCardServiceCallback(List<InCommCard> allCards, Exception exception);
}
