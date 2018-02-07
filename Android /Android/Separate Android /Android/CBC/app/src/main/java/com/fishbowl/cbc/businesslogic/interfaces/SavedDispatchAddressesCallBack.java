package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.DeliveryAddress;

import java.util.ArrayList;

/**
 * Created by VT027 on 5/23/2017.
 */

public interface SavedDispatchAddressesCallBack {
    public void onSavedDispatchAddresses(ArrayList<DeliveryAddress> deliveryAddresses, Exception error);
}
