package com.wearehathway.apps.incomm.Interfaces;


import com.wearehathway.apps.incomm.Models.InCommUser;

/**
 * Created by vthink on 18/08/16.
 */
public interface InCommUserServiceCallBack {
    public void onUserServiceCallback(InCommUser inCommUser, Exception exception);
}
