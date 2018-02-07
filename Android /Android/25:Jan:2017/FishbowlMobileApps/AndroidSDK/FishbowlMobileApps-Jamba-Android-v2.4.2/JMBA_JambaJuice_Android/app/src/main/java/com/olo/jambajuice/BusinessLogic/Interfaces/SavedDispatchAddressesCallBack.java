package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.DeliveryAddress;
import com.wearehathway.apps.olo.Models.OloDeliveryAddress;

import java.util.ArrayList;

/**
 * Created by vthink on 13/03/17.
 */

public interface SavedDispatchAddressesCallBack {
    public void onSavedDispatchAddresses(ArrayList<DeliveryAddress> deliveryAddresses, Exception error);
}
