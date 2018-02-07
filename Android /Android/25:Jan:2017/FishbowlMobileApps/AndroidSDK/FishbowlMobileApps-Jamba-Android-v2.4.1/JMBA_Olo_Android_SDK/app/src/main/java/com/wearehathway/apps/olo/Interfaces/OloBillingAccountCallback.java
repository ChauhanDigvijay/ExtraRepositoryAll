package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloBillingAccount;

/**
 * Created by Ihsanulhaq on 5/29/2015.
 */
public interface OloBillingAccountCallback {
    public void onBillingAccountCallback(OloBillingAccount [] accounts, Exception error);
}
