package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.Store;

/**
 * Created by VT027 on 5/23/2017.
 */

public interface PreferredStoreCallBack {
    public void onPreferredStoreCallback(Exception exception, Store store);

    public void onPreferredStoreErrorCallback(Exception exception);
}

