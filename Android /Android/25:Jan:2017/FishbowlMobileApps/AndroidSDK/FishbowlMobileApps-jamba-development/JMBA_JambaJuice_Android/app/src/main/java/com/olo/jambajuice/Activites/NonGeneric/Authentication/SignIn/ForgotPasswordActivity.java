package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignIn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.ForgotPasswordCallback;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener, ForgotPasswordCallback {
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setUpToolBar(true);
        setBackButton(true,false);
        setTitle("Forgot Password");
        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        trackButton(v);
        sendResetCodePressed();
    }

    private void sendResetCodePressed() {
        EditText email = (EditText) findViewById(R.id.emailOrPhone);
        String emailText = email.getText().toString();
        boolean isValid = true;
        if (!Utils.isValidEmail(emailText)) {
            isValid = false;
            email.setError("Please enter valid email address.");
        }
        if (isValid) {
            setButtonEnabled(false);
            JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.FORGOT_PASSWORD);
            UserService.forgotPassword(emailText, this);
        }
    }

    @Override
    public void onForgotPasswordCallback(Exception exception) {
        if (exception != null) {
            setButtonEnabled(true);
            Utils.showErrorAlert(this, exception);
        } else {
            Bundle b = new Bundle();
            b.putBoolean(Constants.B_IS_CREATED_FROM_PASSWORD, true);
            TransitionManager.transitFrom(ForgotPasswordActivity.this, ConfirmationSentActivity.class, b);
        }
    }

    private void setButtonEnabled(boolean isenabled) {
        isBackButtonEnabled = isenabled;
        sendBtn.setEnabled(isenabled);
        if (isenabled) {
            sendBtn.setText("Send Request");
        } else {
            sendBtn.setText("Sending Request...");
        }
    }

    @Override
    public void setContentView(int resId) {
        super.setContentView(resId);
        ImageView view = (ImageView) findViewById(R.id.splash_bg_layout);
        BitmapUtils.loadBitmapResource(view, R.drawable.splash_bg, true);
    }
}
