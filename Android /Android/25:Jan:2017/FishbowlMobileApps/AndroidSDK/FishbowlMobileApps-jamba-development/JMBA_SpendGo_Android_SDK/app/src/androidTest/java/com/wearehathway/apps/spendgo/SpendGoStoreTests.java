package com.wearehathway.apps.spendgo;

import com.wearehathway.apps.spendgo.Interfaces.ISpendGoStoreService;
import com.wearehathway.apps.spendgo.Models.SpendGoStore;
import com.wearehathway.apps.spendgo.Services.SpendGoStoreService;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoStoreTests extends SpendGoBaseTests
{
    public void testFindNearestStoresWithInvalidLatLng()
    {
        double lat = 123421;
        double lng = 2232;
        SpendGoStoreService.findNearestStores(lat, lng, 10, new ISpendGoStoreService()
        {
            @Override
            public void onSpendGoStoreServiceCallback(ArrayList<SpendGoStore> stores, Exception error)
            {
                Assert.assertNull("Should return error", error);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testFindStoresNearSanLuisObispo()
    {
        double latitude = 37.51179885864258;
        double longitude = -122.29399871826172;

        SpendGoStoreService.findNearestStores(latitude, longitude, 10, new ISpendGoStoreService()
        {
            @Override
            public void onSpendGoStoreServiceCallback(ArrayList<SpendGoStore> stores, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid response", stores);
                Assert.assertTrue("Invalid number of stores returned", stores.size() > 0);
                SpendGoStore store = stores.get(0);
                Assert.assertTrue("Invalid store Id", store.getId() > 0);
                Assert.assertTrue("Invalid store zip", store.getZip() != null && !store.getZip().equals(""));
                latch.countDown();
            }
        });
        waitForCompletion();
    }

}
