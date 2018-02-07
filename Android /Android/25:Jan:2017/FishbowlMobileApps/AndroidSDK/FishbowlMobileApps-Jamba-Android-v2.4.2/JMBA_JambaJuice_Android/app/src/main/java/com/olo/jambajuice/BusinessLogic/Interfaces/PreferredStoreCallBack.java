package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.Store;

/**
 * Created by vt02 on 3/5/16.
 */
public interface PreferredStoreCallBack {
    public void onPreferredStoreCallback(Exception exception, Store store);

    public void onPreferredStoreErrorCallback(Exception exception);
}
