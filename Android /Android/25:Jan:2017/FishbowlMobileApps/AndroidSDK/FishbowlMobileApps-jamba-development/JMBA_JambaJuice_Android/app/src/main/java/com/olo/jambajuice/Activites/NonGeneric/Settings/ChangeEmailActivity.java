package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Authentication.SignIn.ConfirmationSentActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.LookUpCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserUpdateCallback;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.ACTIVATED;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.INVALID_EMAIL;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.STARTED_ACCOUNT;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.SERVER_ERROR.EMAIL_NOT_VALIDATED;

/**
 * Created by Nauman Afzaal on 19/06/15.
 */
public class ChangeEmailActivity extends BaseActivity implements View.OnClickListener, UserUpdateCallback {
    Button changeEmailBtn;
    private EditText newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        setUpToolBar(true);
        setTitle("Update Email Address");
        setBackButton(true,false);
        changeEmailBtn = (Button) findViewById(R.id.changeEmailBtn);
        changeEmailBtn.setOnClickListener(this);
    }

    @Override
    public void setContentView(int resId) {
        super.setContentView(resId);
        ImageView view = (ImageView) findViewById(R.id.splash_bg_layout);
        BitmapUtils.loadBitmapResource(view, R.drawable.splash_bg, false);
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        newEmail = (EditText) findViewById(R.id.newEmail);
        final String emailStr = newEmail.getText().toString();
        if (!Utils.isValidEmail(emailStr)) {
            Utils.showAlert(this, "Please enter a valid email address.", "Error");
            return;
        }
        Utils.hideSoftKeyboard(this);
        sendEmailChangeCall(emailStr);
    }

    private void sendEmailChangeCall(final String newEmail) {
        enableScreen(false);
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_EMAIL);
        UserService.lookUpUserEmail(this, newEmail, new LookUpCallback() {
            @Override
            public void onLookUpCallback(String status, Exception error) {

                if (ACTIVATED.value.equals(status) || STARTED_ACCOUNT.value.equals(status)) {
                    enableScreen(true);
                    Utils.showAlert(ChangeEmailActivity.this, "This email address is associated with another account. Please enter a different email address or proceed to Sign In.");
                } else if (INVALID_EMAIL.value.equals(status)) {
                    enableScreen(true);
                    Utils.showAlert(ChangeEmailActivity.this, "Please enter a valid email address");
                } else {
                    UserService.changeEmail(newEmail, ChangeEmailActivity.this);
                }
            }
        });
    }

    @Override
    public void enableScreen(boolean isEnabled) {
        changeEmailBtn.setEnabled(isEnabled);
        super.enableScreen(isEnabled);
        if (!isEnabled) {
            changeEmailBtn.setText("Please wait...");
        } else {
            changeEmailBtn.setText("Update Email Address");
        }
    }

    @Override
    public void onUserUpdateCallback(Exception error) {
        enableScreen(true);
        int errorCode = Utils.getErrorCode(error);
        if (error == null || errorCode == EMAIL_NOT_VALIDATED.value) {
            //Update CLP Customer on updating information
//            User user = UserService.getUser();
//            ((JambaApplication) this.getApplication()).updateCustomerInfo(user);
            TransitionManager.transitFrom(this, ConfirmationSentActivity.class);
        } else {
            Utils.showErrorAlert(this, error);
        }
    }
}
