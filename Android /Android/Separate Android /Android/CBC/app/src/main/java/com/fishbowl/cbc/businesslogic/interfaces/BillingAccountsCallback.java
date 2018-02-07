package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.BillingAccount;

import java.util.ArrayList;

/**
 * Created by VT027 on 5/23/2017.
 */

public interface BillingAccountsCallback {
    public void onBillingAccountsCallback(ArrayList<BillingAccount> billingAccounts, Exception error);
}
