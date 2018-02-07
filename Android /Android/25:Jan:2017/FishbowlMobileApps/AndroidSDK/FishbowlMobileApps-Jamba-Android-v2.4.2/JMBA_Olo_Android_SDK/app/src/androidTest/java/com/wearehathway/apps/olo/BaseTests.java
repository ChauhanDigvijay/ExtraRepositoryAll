package com.wearehathway.apps.olo;

import android.test.AndroidTestCase;

import com.wearehathway.apps.olo.Services.OloService;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nauman on 25/04/15.
 */
public class BaseTests extends AndroidTestCase
{
    protected CountDownLatch latch;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OloService.initialize(getContext(), "https://api.olostaging.com/v1.1", "kpGgmHgbo3J3A4HaXM3AdnAWtYZB9Nb2");
    }

    protected void waitForCompletion()
    {
        latch = new CountDownLatch(1);
        try
        {
            Assert.assertTrue(latch.await(50, TimeUnit.SECONDS));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
