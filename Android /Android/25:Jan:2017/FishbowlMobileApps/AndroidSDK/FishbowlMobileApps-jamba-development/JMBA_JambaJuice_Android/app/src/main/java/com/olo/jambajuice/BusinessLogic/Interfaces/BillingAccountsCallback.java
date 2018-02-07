package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 18/06/15.
 */
public interface BillingAccountsCallback {
    public void onBillingAccountsCallback(ArrayList<BillingAccount> billingAccounts, Exception error);
}
