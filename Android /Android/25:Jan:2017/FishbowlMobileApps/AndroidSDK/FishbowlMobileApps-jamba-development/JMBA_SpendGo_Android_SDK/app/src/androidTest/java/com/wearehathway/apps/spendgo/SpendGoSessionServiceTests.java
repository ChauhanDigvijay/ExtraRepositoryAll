package com.wearehathway.apps.spendgo;

import com.wearehathway.apps.spendgo.Interfaces.ISpendGoSessionService;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoSignOffService;
import com.wearehathway.apps.spendgo.Models.SpendGoOptional;
import com.wearehathway.apps.spendgo.Models.SpendGoSession;
import com.wearehathway.apps.spendgo.Models.SpendGoUser;
import com.wearehathway.apps.spendgo.Services.SpendGoSessionService;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoSessionServiceTests extends SpendGoBaseTests
{
    String phoneOrUsername = "3454551752";
    String password = "12345678";

    public void testAddMember()
    {
        SpendGoUser user = new SpendGoUser();
        boolean smsOptIn = true;
        String email = "naumanafzaal1@gmail.com";
        boolean emailOptIn = true;
        String firstName = "Nauman";
        String lastName = "Afzaal";
        String dob = "19890530";
        String gender = "M";
        String maritalStatus = "Single";
        String street = "Street";
        String city = "City";
        String state = "State";
        String zip = "54000";
        String favoriteStoreCode = "123";
        ArrayList<SpendGoOptional> additionalInfo = new ArrayList<>();
        additionalInfo.add(new SpendGoOptional("Key1", "Value1"));
        additionalInfo.add(new SpendGoOptional("Key2", "Value2"));
        boolean sendWelcomeEmail = true;
        boolean emailValidated = false;

        user.setPhone(phoneOrUsername);
        user.setSms_opt_in(smsOptIn);
        user.setEmail(email);
        user.setEmail_opt_in(emailOptIn);
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setDob(dob);
        user.setGender(gender);
        user.setMarital_status(maritalStatus);
        user.setStreet(street);
        user.setCity(city);
        user.setState(state);
        user.setZip(zip);
        user.setAddtl_info(additionalInfo);

        SpendGoSessionService.addUser(user, password, favoriteStoreCode, sendWelcomeEmail, emailValidated, new ISpendGoSessionService()
        {
            @Override
            public void onSessionServiceCallback(SpendGoSession user, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid Response", user);
                Assert.assertTrue("Invalid SpendGo Id", !user.getSpendGoId().equals(""));
                //Assert.assertTrue("Invalid Username", !user.getUserName().equals(""));
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testAuthentication()
    {
        SpendGoSessionService.signIn(phoneOrUsername, password, new ISpendGoSessionService()
        {
            @Override
            public void onSessionServiceCallback(SpendGoSession user, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid Response", user);
                Assert.assertTrue("Invalid auth token", !user.getAuthToken().equals(""));
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testSignOff()
    {
        SpendGoSessionService.signIn(phoneOrUsername, password, new ISpendGoSessionService()
        {
            @Override
            public void onSessionServiceCallback(SpendGoSession user, Exception error)
            {
                Assert.assertNull("Error", error);
                Assert.assertNotNull("Invalid Response", user);
                Assert.assertTrue("Invalid auth token", !user.getAuthToken().equals(""));
                SpendGoSessionService.signOff(user.getAuthToken(), new ISpendGoSignOffService()
                {
                    @Override
                    public void onSignOffCallback(Exception exception)
                    {
                        Assert.assertNull("Error", exception);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }
}
