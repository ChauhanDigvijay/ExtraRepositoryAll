package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.Store;

/**
 * Created by Nauman Afzaal on 19/06/15.
 */
public interface StoreDetailCallback {
    public void onStoreDetailCallback(Store store, Exception exception);
}
