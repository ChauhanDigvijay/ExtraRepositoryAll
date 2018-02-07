package com.fishbowl.apps.olo;

import com.fishbowl.apps.olo.Interfaces.OloOrderServiceCallback;
import com.fishbowl.apps.olo.Models.OloOrderStatus;
import com.fishbowl.apps.olo.Services.OloOrderService;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public class OloOrderServiceTests extends BaseTests
{
    public void testGetOrderById()
    {
        OloOrderService.getOrderById("", new OloOrderServiceCallback()
        {
            @Override
            public void onOrderServiceCallback(OloOrderStatus orderStatus, Exception error)
            {
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testGetOrderByRef()
    {
        OloOrderService.getOrderByRef("", new OloOrderServiceCallback()
        {
            @Override
            public void onOrderServiceCallback(OloOrderStatus orderStatus, Exception error)
            {
                latch.countDown();
            }
        });
        waitForCompletion();
    }
}
