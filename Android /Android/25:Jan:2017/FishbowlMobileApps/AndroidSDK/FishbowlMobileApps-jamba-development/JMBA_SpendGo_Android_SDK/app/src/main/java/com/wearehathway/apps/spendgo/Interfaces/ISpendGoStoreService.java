package com.wearehathway.apps.spendgo.Interfaces;

import com.wearehathway.apps.spendgo.Models.SpendGoStore;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface ISpendGoStoreService
{
    public void onSpendGoStoreServiceCallback(ArrayList<SpendGoStore> stores, Exception error);
}
