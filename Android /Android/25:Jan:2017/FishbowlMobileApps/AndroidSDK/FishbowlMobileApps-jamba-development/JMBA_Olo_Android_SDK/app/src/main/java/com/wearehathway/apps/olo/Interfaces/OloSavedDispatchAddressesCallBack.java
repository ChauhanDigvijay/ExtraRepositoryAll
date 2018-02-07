package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloBillingScheme;
import com.wearehathway.apps.olo.Models.OloDeliveryAddress;

import java.util.ArrayList;

/**
 * Created by vthink on 13/03/17.
 */

public interface OloSavedDispatchAddressesCallBack {
    public void onSavedDispatchAddresses(ArrayList<OloDeliveryAddress> oloDeliveryAddresses, Exception error);
}
