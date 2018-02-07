package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.Generic.WebViewActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;

public class TermsAndPrivacyActivity extends BaseActivity implements View.OnClickListener {
    private static final String jambaPrivacyUrl = "http://www.jambajuice.com/company-info/privacy";
    private static final String jambaLegalUrl = "http://www.jambajuice.com/company-info/legal";
    private static final String spendGoPrivacyUrl = "https://jamba.spendgo.com/index.html#/privacy";
    private static final String spendGoTermsUrl = "https://jamba.spendgo.com/index.html#/terms";
    private static final String oloPrivacyUrl = "http://order.jambajuice.com/help/privacypolicy";
    private static final String oloTermsUrl = "http://order.jambajuice.com/help/useragreement";
    private static final String fishbowlPrivacyUrl = "http://order.jambajuice.com/help/privacypolicy";
    private static final String fishbowlTermsUrl = "http://order.jambajuice.com/help/useragreement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_privacy);
        setToolBar();
        setUpView();
    }

    private void setToolBar() {
        setUpToolBar(true);
        setTitle("Terms & Privacy");
        if (!isOpeningFromSettingScreen()) {
            isSlideDown = true;
        }
        setBackButton(true,false);
    }

    private boolean isOpeningFromSettingScreen() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getBoolean(Constants.B_IS_OPENING_FROM_SETTING);
        }
        return false;
    }

    private void setUpView() {
        findViewById(R.id.jambaJuicePrivacy).setOnClickListener(this);
        findViewById(R.id.jambaJuiceLegal).setOnClickListener(this);
        findViewById(R.id.spendGoPrivacy).setOnClickListener(this);
        findViewById(R.id.spendGoTermsAndConditions).setOnClickListener(this);
        findViewById(R.id.oloPrivacy).setOnClickListener(this);
        findViewById(R.id.oloUserAgreement).setOnClickListener(this);
        findViewById(R.id.fishbowlPrivacy).setOnClickListener(this);
        findViewById(R.id.fishbowlUserAgreement).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        String heading = ((Button) v).getText().toString();
        switch (v.getId()) {
            case R.id.jambaJuicePrivacy:
                WebViewActivity.show(this, heading, jambaPrivacyUrl);
                break;
            case R.id.jambaJuiceLegal:
                WebViewActivity.show(this, heading, jambaLegalUrl);
                break;
            case R.id.spendGoPrivacy:
                WebViewActivity.show(this, heading, spendGoPrivacyUrl);
                break;
            case R.id.spendGoTermsAndConditions:
                WebViewActivity.show(this, heading, spendGoTermsUrl);
                break;
            case R.id.oloPrivacy:
                WebViewActivity.show(this, heading, oloPrivacyUrl);
                break;
            case R.id.oloUserAgreement:
                WebViewActivity.show(this, heading, oloTermsUrl);
                break;
            case R.id.fishbowlPrivacy:
                WebViewActivity.show(this, heading, fishbowlPrivacyUrl);
                break;
            case R.id.fishbowlUserAgreement:
                WebViewActivity.show(this, heading, fishbowlTermsUrl);
                break;
        }
    }
}
