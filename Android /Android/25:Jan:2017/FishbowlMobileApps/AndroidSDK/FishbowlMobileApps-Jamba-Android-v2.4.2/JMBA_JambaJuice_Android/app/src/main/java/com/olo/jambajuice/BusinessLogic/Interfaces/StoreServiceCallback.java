package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.Store;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public interface StoreServiceCallback {
    public void onStoreServiceCallback(ArrayList<Store> stores, Exception exception);
}
