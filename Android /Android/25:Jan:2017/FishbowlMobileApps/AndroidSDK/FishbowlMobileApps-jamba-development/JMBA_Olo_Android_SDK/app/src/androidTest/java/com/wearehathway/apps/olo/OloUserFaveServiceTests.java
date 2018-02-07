package com.wearehathway.apps.olo;

import com.wearehathway.apps.olo.Interfaces.OloDeleteUserFaveCallback;
import com.wearehathway.apps.olo.Interfaces.OloSessionServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloUserFavesCallback;
import com.wearehathway.apps.olo.Models.OloFave;
import com.wearehathway.apps.olo.Models.OloUser;
import com.wearehathway.apps.olo.Services.OloFavesService;
import com.wearehathway.apps.olo.Services.OloSessionService;

import junit.framework.Assert;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloUserFaveServiceTests extends BaseTests {

    private final String email = "jambaandroidtester@email.com";
    final String password = "password";
    private final String basketId = "8267B41B-3B64-406A-9A73-1CCE42BD2506";

    public void testGetUserFaves() {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception) {
                Assert.assertNull("Session Request failed", exception);
                Assert.assertNotNull("Invalid session response", user);
                OloFavesService.getUserFaves(new OloUserFavesCallback() {
                    @Override
                    public void onUserFavesCallback(OloFave[] faves, Exception exception) {
                        Assert.assertNull("Faves Request failed", exception);
                        Assert.assertNotNull("Invalid faves response", faves);
                        Assert.assertTrue("Faves should be greater than zero", faves.length > 0);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testAddDeleteUserFave() {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception) {
                Assert.assertNull("Session Request failed", exception);
                Assert.assertNotNull("Invalid session response", user);
                final String description = "Description" + System.currentTimeMillis();
                OloFavesService.addUserFaves(basketId, description, new OloUserFavesCallback() {
                    @Override
                    public void onUserFavesCallback(OloFave[] faves, Exception exception) {
                        Assert.assertNull("Faves Request failed", exception);
                        Assert.assertNotNull("Invalid faves response", faves);
                        OloFave foundFav = helperReturnFaveWithDescription(faves, description);
                        Assert.assertTrue("Fave not added", foundFav != null);
                        OloFavesService.deleteUserFaves(foundFav.getId(), new OloDeleteUserFaveCallback() {
                            @Override
                            public void onDeleteUserFaveCallback(Exception exception) {
                                Assert.assertNull("Faves Delete Request failed", exception);
                                OloFavesService.getUserFaves(new OloUserFavesCallback() {
                                    @Override
                                    public void onUserFavesCallback(OloFave[] faves, Exception exception) {
                                        Assert.assertNull("Faves Request failed", exception);
                                        Assert.assertNotNull("Invalid faves response", faves);
                                        OloFave foundFav = helperReturnFaveWithDescription(faves, description);
                                        Assert.assertTrue("Fave not deleted", foundFav == null);
                                        latch.countDown();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        waitForCompletion();
    }

    private OloFave helperReturnFaveWithDescription(OloFave[] faves, String description) {
        OloFave foundFav = null;
        for (int i = 0; i < faves.length; i++) {
            OloFave fave = faves[i];
            if (fave.getName().equals(description)) {
                foundFav = fave;
                break;
            }
        }
        return foundFav;
    }
}
