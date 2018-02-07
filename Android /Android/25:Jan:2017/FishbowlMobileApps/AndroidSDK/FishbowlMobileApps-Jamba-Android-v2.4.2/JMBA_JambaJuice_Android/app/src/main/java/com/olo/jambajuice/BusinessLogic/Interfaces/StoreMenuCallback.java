package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProduct;

import java.util.HashMap;

/**
 * Created by Nauman Afzaal on 29/05/15.
 */
public interface StoreMenuCallback {
    public void onStoreMenuCallback(HashMap<Integer, StoreMenuProduct> storeProducts, Exception exception);
}
