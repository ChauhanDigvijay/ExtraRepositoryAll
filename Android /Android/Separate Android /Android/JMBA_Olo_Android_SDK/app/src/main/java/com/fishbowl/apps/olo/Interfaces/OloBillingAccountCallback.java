package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloBillingAccount;

/**
 * Created by Ihsanulhaq on 5/29/2015.
 */
public interface OloBillingAccountCallback {
    public void onBillingAccountCallback(OloBillingAccount [] accounts, Exception error);
}
