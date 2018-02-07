package com.wearehathway.apps.spendgo;

import com.wearehathway.apps.spendgo.Interfaces.ISpendGoForgotPasswordService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoRewardSummary;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoSessionService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoUserService;
import com.wearehathway.apps.spendgo.Models.SpendGoRewardSummary;
import com.wearehathway.apps.spendgo.Models.SpendGoSession;
import com.wearehathway.apps.spendgo.Models.SpendGoUser;
import com.wearehathway.apps.spendgo.Services.SpendGoSessionService;
import com.wearehathway.apps.spendgo.Services.SpendGoUserService;

import junit.framework.Assert;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public class SpendGoUserServiceTests extends SpendGoBaseTests {
    final String phoneOrUsername = "3454551752";
    String password = "12345678";

    //    public void testUpdateUser()
    //    {
    //        SpendGoSessionService.getInstance().signIn(phoneOrUsername, password, new ISpendGoSessionService()
    //        {
    //            @Override
    //            public void onSessionServiceCallback(SpendGoSession user, Exception error)
    //            {
    //                boolean smsOptIn = true;
    //                String email = "nauman.afzaal@wearehathway.com";
    //                boolean emailOptIn = true;
    //                String firstName = "Android";
    //                String lastName = "Tester";
    //                String dob = "19910511";
    //                String gender = "M";
    //                String maritalStatus = "Single";
    //                final String street = "Street1";
    //                String city = "City1";
    //                String state = "State";
    //                String zip = "54000";
    //                int favoriteStoreId = 123;
    //                final ArrayList<SpendGoOptional> additionalInfo = new ArrayList<>();
    //                additionalInfo.add(new SpendGoOptional("Key11", "Value11"));
    //                additionalInfo.add(new SpendGoOptional("Key2", "Value2"));
    //
    //                SpendGoUser updatedUser = new SpendGoUser();
    //                updatedUser.setPhone(phoneOrUsername);
    //                updatedUser.setSms_opt_in(smsOptIn);
    //                updatedUser.setEmail(email);
    //                updatedUser.setEmail_opt_in(emailOptIn);
    //                updatedUser.setFirst_name(firstName);
    //                updatedUser.setLast_name(lastName);
    //                updatedUser.setDob(dob);
    //                updatedUser.setGender(gender);
    //                updatedUser.setMarital_status(maritalStatus);
    //                updatedUser.setStreet(street);
    //                updatedUser.setCity(city);
    //                updatedUser.setState(state);
    //                updatedUser.setZip(zip);
    //                updatedUser.setAddtl_info(additionalInfo);
    //
    //
    //                SpendGoUserService.getInstance().updateUser(updatedUser, password, favoriteStoreId, new ISpendGoUserService()
    //                {
    //                    @Override
    //                    public void onUserServiceCallback(SpendGoUser user, Exception error)
    //                    {
    //                        Assert.assertNull("Error", error);
    //                        Assert.assertNotNull("Request Failed", user);
    //                        Assert.assertNotNull("SpengGoId is invalid", user.getSpendgo_id());
    //                        Assert.assertEquals("Phone is invalid", user.getPhone(), phoneOrUsername);
    //                        Assert.assertEquals("Street is invalid", user.getStreet(), street);
    //                        ArrayList<SpendGoOptional> addInfo = user.getAddtl_info();
    //                        Assert.assertTrue("Invalid additional Info", addInfo.size() == additionalInfo.size());
    //                        latch.countDown();
    //                    }
    //                });
    //            }
    //        });
    //        waitForCompletion();
    //    }

    public void testGetMemberWithSpendGoId()
    {
        SpendGoSessionService.signIn(phoneOrUsername, password, new ISpendGoSessionService()
        {
            @Override
            public void onSessionServiceCallback(SpendGoSession user, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Exception", user);
                SpendGoUserService.getMemberWithId(user.getSpendGoId(), new ISpendGoUserService()
                {
                    @Override
                    public void onUserServiceCallback(SpendGoUser user, Exception error)
                    {
                        Assert.assertNull("Error", error);
                        Assert.assertNotNull("Request Failed", user);
                        Assert.assertNotNull("SpengGoId is invalid", user.getSpendgo_id());
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testForgotPassword() {
        String email = "naumanafzaal1@gmail.com";
        SpendGoUserService.forgotPassword(email, new ISpendGoForgotPasswordService() {
            @Override
            public void onForgotPasswordCallback(Exception error) {
                Assert.assertNull("ForgotPassword Failed", error);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testRewardsSummary() {
        SpendGoSessionService.signIn(phoneOrUsername, password, new ISpendGoSessionService() {
            @Override
            public void onSessionServiceCallback(SpendGoSession user, Exception error) {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Request Failed", user);
                String accountId = "";
                SpendGoUserService.getUserRewardSummary(user.getSpendGoId(), accountId, new ISpendGoRewardSummary() {
                    @Override
                    public void onRewardSummaryCallback(SpendGoRewardSummary rewards, Exception error) {
                        Assert.assertNull("Error", error);
                        Assert.assertNotNull("Request Failed", rewards);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }
}
