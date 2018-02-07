package com.wearehathway.apps.olo;

import com.wearehathway.apps.olo.Interfaces.OloOrderServiceCallback;
import com.wearehathway.apps.olo.Models.OloOrderStatus;
import com.wearehathway.apps.olo.Services.OloOrderService;

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
