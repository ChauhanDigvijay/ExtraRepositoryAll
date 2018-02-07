package com.fishbowl.apps.olo;

import com.fishbowl.apps.olo.Interfaces.OloSessionServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserContactDetailCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserOrderServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserPostServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloUserServiceCallback;
import com.fishbowl.apps.olo.Models.OloOrderStatus;
import com.fishbowl.apps.olo.Models.OloOrdersSatusProduct;
import com.fishbowl.apps.olo.Models.OloUser;
import com.fishbowl.apps.olo.Services.OloSessionService;
import com.fishbowl.apps.olo.Services.OloUserService;

import junit.framework.Assert;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public class OloUserServiceTests extends BaseTests
{
    String email = "jambaandroidtester@email.com";
    String password = "password";

    public void testChangeUserPassword()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("Session Request failed", exception);
                Assert.assertNotNull("Invalid session response", user);
                OloUserService.changePassword(password, password, new OloUserPostServiceCallback()
                {
                    @Override
                    public void onUserPostCallback(boolean isSuccess, Exception exception)
                    {
                        Assert.assertNull("Password change request failed", exception);
                        Assert.assertTrue("Error occurred while changing password", isSuccess);
                        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
                        {
                            @Override
                            public void onSessionServiceCallback(OloUser user, Exception exception)
                            {
                                Assert.assertNull("Session Request failed after password change", exception);
                                Assert.assertNotNull("Invalid session response after password change", user);
                                latch.countDown();
                            }
                        });
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testUserForgotPassword()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                OloUserService.forgotPassword("jambaandroidtester@email.com", new OloUserPostServiceCallback()
                {
                    @Override
                    public void onUserPostCallback(boolean isSuccess, Exception exception)
                    {
                        Assert.assertNull("Password forgot request failed", exception);
                        Assert.assertTrue("Unable to send password change request", isSuccess);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testUserInformation()
    {
        OloUserService.getUserInformation(new OloUserServiceCallback()
        {
            @Override
            public void onUserServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("User information request failed", exception);
                Assert.assertNotNull("Invalid user information response", user);
                Assert.assertTrue("Invalid Token", !user.getAuthtoken().equals(""));
                Assert.assertTrue("Invalid Email", !user.getEmailaddress().equals(""));
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testUserUpdateInformation()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                final String newFirstName = "JambaNew";
                final String newLastName = "AndroidNew";
                final OloUser updatedUser = new OloUser(newFirstName, newLastName, user.getEmailaddress(), user.getCardsuffix());
                OloUserService.updateUserInformation(updatedUser, new OloUserServiceCallback()
                {
                    @Override
                    public void onUserServiceCallback(OloUser user, Exception exception)
                    {
                        Assert.assertNull("User information request failed", exception);
                        Assert.assertNotNull("Invalid user information response", user);
                        Assert.assertFalse("Invalid Token", user.getAuthtoken().equals(""));
                        Assert.assertFalse("Invalid Email", user.getEmailaddress().equals(""));
                        Assert.assertTrue("Invalid first name", user.getFirstname().equals(newFirstName));
                        Assert.assertTrue("Invalid last name", user.getLastname().equals(newLastName));
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testAddUserCreditCard()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("User request failed", exception);
                Assert.assertNotNull("Invalid user response", user);

                String cardNumber = "4556892528054091";
                int expMonth = 12;
                int expYear = 2025;
                String cvv = "334";
                String zip = "541029";
                OloUserService.addCreditCard(cardNumber, expMonth, expYear, cvv, zip, new OloUserPostServiceCallback()
                {
                    @Override
                    public void onUserPostCallback(boolean isSuccess, Exception exception)
                    {
                        Assert.assertNull("Credit card request failed", exception);
                        Assert.assertTrue("Unable to add credit card", isSuccess);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testDeleteCreditCard()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("User request failed", exception);
                Assert.assertNotNull("Invalid user response", user);

                String cardNumber = "4556892528054091";

                OloUserService.deleteCreditCard(cardNumber, new OloUserPostServiceCallback()
                {
                    @Override
                    public void onUserPostCallback(boolean isSuccess, Exception exception)
                    {
                        Assert.assertNull("Credit card delete request failed", exception);
                        Assert.assertTrue("Unable to delete credit card", isSuccess);
                        latch.countDown();
                    }
                });

            }
        });
        waitForCompletion();
    }

    public void testUserRecentOrders()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("User request failed", exception);
                Assert.assertNotNull("Invalid user response", user);
                OloUserService.getRecentOrders(new OloUserOrderServiceCallback()
                {
                    @Override
                    public void onUserOrderServiceCallback(OloOrderStatus[] orderStatuses, Exception exception)
                    {
                        Assert.assertNull("OloOrderStatus request failed", exception);
                        Assert.assertNotNull("Invalid order statuses response", orderStatuses);
                        int size = orderStatuses.length;
                        for (int i = 0; i < size; i++)
                        {
                            OloOrderStatus order = orderStatuses[i];
                            Assert.assertNotSame("Order Id cannot be null", order.getId(), "");
                            Assert.assertNotSame("Order total cannot be null", order.getTotal(), "");
                            Assert.assertNotNull("Products cannot be null", order.getProducts());
                            for (OloOrdersSatusProduct product : order.getProducts())
                            {
                                Assert.assertNotSame("Product Id cannot be null", product.getName(), "");
                                Assert.assertNotSame("Product total cost cannot be null", product.getTotalcost(), "");
                                Assert.assertNotSame("Product quantity cannot be null", product.getQuantity(), "");
                            }
                        }
                        latch.countDown();

                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testUserConactDetail()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("User request failed", exception);
                Assert.assertNotNull("Invalid user response", user);
                OloUserService.getUserContactDetail(new OloUserContactDetailCallback()
                {
                    @Override
                    public void onUserContactDetailCallback(String contactDetail, Exception exception)
                    {
                        Assert.assertNull("User Contact Request failed", exception);
                        Assert.assertNotNull("Invalid contact response", contactDetail);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testUpdateUserContactDetail()
    {
        OloSessionService.authenticateUser(email, password, new OloSessionServiceCallback()
        {
            @Override
            public void onSessionServiceCallback(OloUser user, Exception exception)
            {
                Assert.assertNull("User request failed", exception);
                Assert.assertNotNull("Invalid user response", user);
                final String number = "1234567890";
                OloUserService.updateUserContactDetail(number, new OloUserContactDetailCallback()
                {
                    @Override
                    public void onUserContactDetailCallback(String contactDetail, Exception exception)
                    {
                        Assert.assertNull("User Contact Request failed", exception);
                        Assert.assertNotNull("Invalid contact response", contactDetail);
                        Assert.assertEquals("Invalid Update", number, contactDetail);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }
}
