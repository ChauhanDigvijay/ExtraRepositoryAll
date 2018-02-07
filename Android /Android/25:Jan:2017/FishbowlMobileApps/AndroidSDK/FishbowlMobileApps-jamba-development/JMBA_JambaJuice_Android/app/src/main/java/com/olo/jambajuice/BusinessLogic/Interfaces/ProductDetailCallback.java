package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifier;

import java.util.List;

/**
 * Created by Nauman Afzaal on 01/06/15.
 */
public interface ProductDetailCallback {
    public void onProductDetailCallback(List<StoreMenuProductModifier> storeMenuProductModifiers, Exception exception);
}
