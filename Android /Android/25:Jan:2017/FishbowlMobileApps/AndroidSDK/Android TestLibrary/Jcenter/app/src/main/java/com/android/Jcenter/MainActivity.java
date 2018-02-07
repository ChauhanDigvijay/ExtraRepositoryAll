package com.android.Jcenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.jcenter_projectlibrary.Interfaces.FBSessionServiceCallback;
import com.android.jcenter_projectlibrary.Interfaces.FBUserServiceCallback;
import com.android.jcenter_projectlibrary.Models.FBMember;
import com.android.jcenter_projectlibrary.Models.FBSessionItem;
import com.android.jcenter_projectlibrary.Services.FBSessionService;
import com.android.jcenter_projectlibrary.Utils.FBPreferences;

import static com.android.jcenter_projectlibrary.Services.FBSessionService.fbSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInUser("959-595-9596","password");
       // createUser(collectCustomerData());
    }

    public static void signInUser(String email, String password) {

        FBSessionService.signIn(email, password, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {

                    FBPreferences.sharedInstance(fbSdk.context).setAccessToken(spendGoSession.getAccessToken());
                    FBSessionService.getmember(new FBUserServiceCallback() {
                        @Override
                        public void onUserServiceCallback(FBMember user, Exception error) {

                        }
                    });
                }
        });
    }

    private FBMember collectCustomerData() {

        FBMember customer = new FBMember();
        customer.setFirstName("Dj");
        customer.setLastName("Kumar");
        customer.setEmailAddress("vkumar_ic@fishbowl.com");
        customer.setAddressStreet("East Avenue");
        customer.setAddressState("Noida");
        customer.setAddressCity("Noida");
        customer.setPhoneNumber("956-071-7227");
        customer.setAddressZipCode("201301");
        customer.setDate("08");
        customer.setMonth("08");
        customer.setYear("2017");
        customer.setGender("M");
        customer.setFavoriteStore("0");
        customer.setEmailOptIn("true");
        customer.setPushOptIn("true");
        customer.setsMSOptIn("true");
        customer.setProfileImageUrl("");
        customer.setRequestFromJoinPage("true");
        customer.setCreated("2017-08-08 08:22:00.0");
        customer.setdOB("08/08/2017");
        customer.setSendWelcomeEmail("ss");
        customer.setDeviceId("12345");
        customer.setStoreCode("0");
        customer.setCountry("India");
        customer.setPassword("password");

        return customer;
    }

    public static void createUser(FBMember user) {

        FBSessionService.getRegisterToken( user, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (error != null)

                {
                    //parseResponseAndNotify(error, callbackWeakReference);
                } else {
                    // getOloTokenFromSpendGoToken(true, spendGoSession, callbackWeakReference);
                }
            }
        });
    }

}
