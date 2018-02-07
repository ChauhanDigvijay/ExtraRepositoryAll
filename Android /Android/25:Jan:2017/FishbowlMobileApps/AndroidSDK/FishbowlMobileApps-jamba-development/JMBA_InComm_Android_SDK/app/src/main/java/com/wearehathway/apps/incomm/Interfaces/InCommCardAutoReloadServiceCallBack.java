package com.wearehathway.apps.incomm.Interfaces;


import com.wearehathway.apps.incomm.Models.InCommAutoReloadSavable;

/**
 * Created by vthink on 05/09/16.
 */
public interface InCommCardAutoReloadServiceCallBack {

    public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception);
}
