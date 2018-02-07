package com.fishbowl.apps.olo;

import com.fishbowl.apps.olo.Interfaces.OloAddUserFaveLocationCallback;
import com.fishbowl.apps.olo.Interfaces.OloDeleteUserFaveLocationCallback;
import com.fishbowl.apps.olo.Interfaces.OloSessionServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserFaveLocationsCallback;
import com.fishbowl.apps.olo.Models.OloFaveLocation;
import com.fishbowl.apps.olo.Models.OloUser;
import com.fishbowl.apps.olo.Services.OloFaveLocationService;
import com.fishbowl.apps.olo.Services.OloSessionService;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public class OloFaveLocationServiceTests extends BaseTests
{
    private final String email = "jambaandroidtester@email.com";
    final String password = "password";
    private static final int vendorId1 = 9496;
    private static final int vendorId2 = 9504;

    public void testAddUserFaveLocation()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("Session Request failed", exception);
                Assert.assertNotNull("Invalid session response", user);
                OloFaveLocationService.deleteUserFaveLocation(vendorId1, new OloDeleteUserFaveLocationCallback()
                {
                    @Override
                    public void onDeleteUserFaveLocationCallback(Exception error)
                    {
                        //To be on a safe side delete before adding fave location.
                        OloFaveLocationService.addUserFaveLocation(vendorId1, new OloAddUserFaveLocationCallback()
                        {
                            @Override
                            public void onAddUserFaveLocationCallback(OloFaveLocation location, Exception error)
                            {
                                Assert.assertNull("Fave location request failed", error);
                                Assert.assertNotNull("Invalid fave location response", location);
                                Assert.assertTrue("Invalid location Id", location.getId() > 0);
                                helperDeleteUserFaveLocation();
                            }
                        });
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testGetUserFaveLocations()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("Session request failed", exception);
                Assert.assertNotNull("Invalid session response", user);
                OloFaveLocationService.addUserFaveLocation(vendorId2, new OloAddUserFaveLocationCallback()
                {
                    @Override
                    public void onAddUserFaveLocationCallback(OloFaveLocation location, Exception error)
                    {
                        OloFaveLocationService.getUserFaveLocations(new OloUserFaveLocationsCallback()
                        {
                            @Override
                            public void onUserFaveLocationsCallback(ArrayList<OloFaveLocation> locations, Exception exception)
                            {
                                Assert.assertNull("Get Fave location request failed", exception);
                                Assert.assertNotNull("Invalid fave locations response", locations);
                                Assert.assertTrue("Location should be greater than 0", locations.size() > 0);
                                for (OloFaveLocation location : locations)
                                {
                                    Assert.assertTrue("Invalid Fave Location Id", location.getId() > 0);
                                }
                                latch.countDown();
                            }
                        });

                    }
                });
            }
        });
        waitForCompletion();
    }

    public void helperDeleteUserFaveLocation()
    {
        OloFaveLocationService.deleteUserFaveLocation(vendorId1, new OloDeleteUserFaveLocationCallback()
        {
            @Override
            public void onDeleteUserFaveLocationCallback(Exception error)
            {
                Assert.assertNull("Fave location delete request failed", error);
                latch.countDown();
            }
        });
    }
}
