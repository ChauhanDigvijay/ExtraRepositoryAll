package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.StoreMenuProductModifier;

import java.util.List;

/**
 * Created by Nauman Afzaal on 01/06/15.
 */
public interface ProductDetailCallback {
    public void onProductDetailCallback(List<StoreMenuProductModifier> storeMenuProductModifiers, Exception exception);
}
