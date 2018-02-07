package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.os.Bundle;
import android.view.View;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

/**
 * Created by Ihsanulhaq on 6/16/2015.
 */
public class SignUpSuccessfulActivity extends SignUpBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_successful);
        setTitle("Sign Up Complete");
        isShowBasketIcon = false;
        setBackButton(true, false);
        findViewById(R.id.accountCreatedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.notifyHomeScreenUpdateAndTransitBack(SignUpSuccessfulActivity.this);
                TransitionManager.transitFrom(SignUpSuccessfulActivity.this, SignInActivity.class, true);
            }
        });
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "signup_complete");
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.SIGN_UP_COMPLETE);

    }

    @Override
    public void onBackPressed() {
        Utils.notifyHomeScreenUpdateAndTransitBack(SignUpSuccessfulActivity.this);
        TransitionManager.transitFrom(SignUpSuccessfulActivity.this, SignInActivity.class, true);
    }

}
