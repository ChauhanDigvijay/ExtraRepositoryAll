package com.fishbowl.apps.olo;

import com.android.volley.VolleyError;
import com.fishbowl.apps.olo.Interfaces.OloSessionAuthTokenCallback;
import com.fishbowl.apps.olo.Interfaces.OloSessionServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserLogoutCallback;
import com.fishbowl.apps.olo.Models.OloUser;
import com.fishbowl.apps.olo.Services.OloSessionService;
import com.fishbowl.apps.olo.Utils.Logger;

import junit.framework.Assert;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public class OloSessionServiceTests extends BaseTests implements OloSessionServiceCallback
{
    public void testCreateUser()
    {
        String firstName = "Jamba";
        String lastName = "Android";
        String contactNumber = "123456789";
        String email = "jambaandroidtester@email.com";
        final String password = "password";
        OloSessionService.createUser(firstName, lastName, contactNumber, email, password, this);
        waitForCompletion();
    }

    public void testUserAuthentication()
    {
        String email = "jambaandroidtester@email.com";
        String password = "password";
        OloSessionService.authenticateUser(email, password, this);
        waitForCompletion();
    }

    public void testUserLogOut()
    {
        String email = "jambaandroidtester@email.com";
        String password = "password";
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                OloSessionService.logOutUser(new OloUserLogoutCallback()
                {
                    @Override
                    public void onUserLogoutCallback(Exception exception)
                    {
                        Assert.assertNull("User Request failed", exception);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();

    }

    public void testSessionAuthToken()
    {
        String provider = "spendgo";
        String providerAuthToken = "fa5c978cf04c23e3ef4f346abe1d3a51a93f114667fc74af785a54614c83935dcb9bdfd5cdfce27789e218cf10e2bb0be77464130700413c435879aee009d21948c33402237c97e09983c91ad6cf7e3c79442dedb9e23e5446d4946b5e3e9847ee037405626add60d4339d4408b34daed84841d7cf9a5e3d25cf0f0a87df8e01ca45f80e8bf8ae302653216ff7889f11";
        OloSessionService.getOrCreateUser(provider, providerAuthToken, "", "", new OloSessionAuthTokenCallback()
        {
            @Override
            public void onOloSessionAuthTokenCallback(String authToken, Exception error)
            {
                Assert.assertNull("Auth Token Request failed", error);
                Assert.assertNotNull("Invalid auth token response", authToken);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    @Override
    public void onSessionServiceCallback(OloUser user, Exception exception)
    {
        if (exception instanceof VolleyError)
        {
            VolleyError error = (VolleyError) exception;
            if (error.networkResponse.statusCode >= 200 && error.networkResponse.statusCode <= 300)
            {
                Logger.i("User already exists" + error.networkResponse.statusCode);
                latch.countDown();
                return;
            }
        }
        Assert.assertNull("User Request failed", exception);
        Assert.assertNotNull("Invalid user response", user);
        Assert.assertTrue("Invalid Token", !user.getAuthtoken().equals(""));
        Assert.assertTrue("Invalid Email", !user.getEmailaddress().equals(""));
        latch.countDown();
    }
}



