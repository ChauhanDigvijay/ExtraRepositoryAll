package com.wearehathway.apps.spendgo;

import android.test.AndroidTestCase;

import com.wearehathway.apps.spendgo.Services.SpendGoService;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nauman on 25/04/15.
 */
public class SpendGoBaseTests extends AndroidTestCase
{
    protected CountDownLatch latch;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        SpendGoService.initialize(getContext(),"https://my.skuped.com", "/mobile/gen/jamba/v1/","jambamobile","ZLT4end4MY6TYj28GXVzAyE62SW0uKuXLHQLqu8gja0=");
    }

    protected void waitForCompletion()
    {
        latch = new CountDownLatch(1);
        try
        {
            Assert.assertTrue(latch.await(30, TimeUnit.SECONDS));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
