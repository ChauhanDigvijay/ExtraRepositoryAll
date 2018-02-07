package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.Activites.NonGeneric.Settings.TermsAndPrivacyActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

/**
 * Created by Ihsanulhaq on 6/16/2015.
 */
public class SignUpJambaInsiderActivity extends SignUpBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_jamba_insider);
        setUpToolBar(true, true);
        isShowBasketIcon = false;
        setTitle("Sign Up");
        setBackButton(false, false);
        setPrivacyText();
        findViewById(R.id.continueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity();
            }
        });
        findViewById(R.id.TermAndConditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackButtonWithName("Terms & Privacy");
                TransitionManager.slideUp(SignUpJambaInsiderActivity.this, TermsAndPrivacyActivity.class);
            }
        });
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "signup_start");
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.SIGN_UP_START);

    }

    private void setPrivacyText() {
        TextView textView = (TextView) findViewById(R.id.TermAndConditions);
        textView.setText(Html.fromHtml("<u>By tapping on the Continue button, you agree to the<br/>Terms & Conditions</u>"));
    }

    private void showActivity() {
        TransitionManager.transitFrom(this, SignUpFindStoresActivity.class);
    }

}
