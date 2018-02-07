package com.wearehathway.apps.incomm;

import android.test.AndroidTestCase;

import com.wearehathway.apps.incomm.Models.InCommSDKConfiguration;
import com.wearehathway.apps.incomm.Services.InCommService;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nauman Afzaal on 10/08/15.
 */


public class BaseTests extends AndroidTestCase
{
    protected interface AuthenticationTestCallback
    {
        public void onAuthenticationCallback();
    }
    protected CountDownLatch latch;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        InCommSDKConfiguration configuration = new InCommSDKConfiguration();
        configuration.baseUrl = "https://api.giftango.com/enterpriseapi/v1";
        configuration.brandId = "711763";
        configuration.clientId = "jambajuicemobileapplication";
        configuration.applicationToken = "shG6CqMLuLDnMG5BPibag1duE5JpX2J97zAsIZQRii8=";
        InCommService.initialize(getContext(),configuration);
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
