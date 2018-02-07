package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloDeliveryAddress;

import java.util.ArrayList;

/**
 * Created by vthink on 13/03/17.
 */

public interface OloSavedDispatchAddressesCallBack {
    public void onSavedDispatchAddresses(ArrayList<OloDeliveryAddress> oloDeliveryAddresses, Exception error);
}
