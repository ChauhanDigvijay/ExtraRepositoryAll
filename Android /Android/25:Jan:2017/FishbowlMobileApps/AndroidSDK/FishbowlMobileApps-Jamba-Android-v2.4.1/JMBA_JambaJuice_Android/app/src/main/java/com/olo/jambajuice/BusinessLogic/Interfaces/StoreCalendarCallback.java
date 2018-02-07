package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.StoreTiming;

/**
 * Created by Nauman Afzaal on 18/05/15.
 */
public interface StoreCalendarCallback {
    public void onStoreCalendarCallback(StoreTiming calendar, Exception exception);
}
