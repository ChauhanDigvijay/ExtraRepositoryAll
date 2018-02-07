package com.wearehathway.apps.incomm.Interfaces;


import com.wearehathway.apps.incomm.Models.InCommCard;

/**
 * Created by Nauman Afzaal on 07/08/15.
 */
public interface InCommCardServiceCallback
{
    public void onCardServiceCallback(InCommCard card, Exception exception);
}
