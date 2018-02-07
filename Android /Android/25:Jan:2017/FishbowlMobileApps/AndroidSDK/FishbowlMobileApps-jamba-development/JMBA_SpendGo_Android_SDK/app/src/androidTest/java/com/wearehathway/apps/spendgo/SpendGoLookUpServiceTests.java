package com.wearehathway.apps.spendgo;

import com.wearehathway.apps.spendgo.Interfaces.ISpendGoLookUpService;
import com.wearehathway.apps.spendgo.Services.SpendGoLookUpService;

import junit.framework.Assert;

/**
 * Created by Nauman Afzaal on 12/05/15.
 */
public class SpendGoLookUpServiceTests extends SpendGoBaseTests
{
//    {"status":"Activated"} {"status":"NotFound"} {"status":"InvalidEmail"} {"status":"StarterAccount"}


    public void testLookUpInValidEmail()
    {
        String email = "naumanafzaal1234@wearehathway.com";

        SpendGoLookUpService.lookUp(email, "", new ISpendGoLookUpService()
        {
            @Override
            public void onLookUpServiceCallback(String status, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid Response", status);
                Assert.assertEquals("Invalid status", status, "NotFound");
                latch.countDown();
            }
        });
        waitForCompletion();
    }
    public void testLookUpValidEmail()
    {
        String email = "naumanafzaal1@gmail.com";
        SpendGoLookUpService.lookUp(email, "", new ISpendGoLookUpService()
        {
            @Override
            public void onLookUpServiceCallback(String status, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid Response", status);
                Assert.assertEquals("Invalid status", status, "Activated");
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testLookUpValidPhone()
    {
        String phone = "3454551752";
        SpendGoLookUpService.lookUp("", phone, new ISpendGoLookUpService()
        {
            @Override
            public void onLookUpServiceCallback(String status, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid Response", status);
                Assert.assertEquals("Invalid status", status, "Activated");
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testLookUpInValidPhone()
    {
        String phone = "345455175211";
        SpendGoLookUpService.lookUp("", phone, new ISpendGoLookUpService()
        {
            @Override
            public void onLookUpServiceCallback(String status, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid Response", status);
                Assert.assertEquals("Invalid status", status, "NotFound");
                latch.countDown();
            }
        });
        waitForCompletion();
    }
}
