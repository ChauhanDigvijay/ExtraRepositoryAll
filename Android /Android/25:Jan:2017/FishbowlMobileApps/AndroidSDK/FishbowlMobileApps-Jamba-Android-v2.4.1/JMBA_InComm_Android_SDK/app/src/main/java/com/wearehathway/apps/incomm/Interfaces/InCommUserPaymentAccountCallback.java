package com.wearehathway.apps.incomm.Interfaces;


import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 07/08/15.
 */
public interface InCommUserPaymentAccountCallback
{
    public void onPaymentAccountListServiceCallback(ArrayList<InCommUserPaymentAccount> accountList, Exception exception);
}
