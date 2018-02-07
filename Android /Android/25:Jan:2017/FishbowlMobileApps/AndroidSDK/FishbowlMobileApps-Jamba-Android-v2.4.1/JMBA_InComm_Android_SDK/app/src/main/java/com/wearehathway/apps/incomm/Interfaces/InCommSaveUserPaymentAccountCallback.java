package com.wearehathway.apps.incomm.Interfaces;


import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;

/**
 * Created by Nauman Afzaal on 07/08/15.
 */
public interface InCommSaveUserPaymentAccountCallback
{
    public void onPaymentAccountSaveServiceCallback(InCommUserPaymentAccount paymentAccount, Exception exception);
}
