package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.StoreTiming;

/**
 * Created by VT027 on 5/22/2017.
 */

public interface StoreCalenderCallback {
    public void onStoreCalendarCallback(StoreTiming calendar, Exception exception);
}
