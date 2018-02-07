package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.StoreTiming;

/**
 * Created by VT027 on 5/23/2017.
 */

public interface StoreCalendarCallback {
    public void onStoreCalendarCallback(StoreTiming calendar, Exception exception);
}
