package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.LookUpCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserUpdateCallback;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;

import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.ACTIVATED;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.INVALID_EMAIL;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.STARTED_ACCOUNT;

/**
 * Created by Nauman Afzaal on 19/06/15.
 */
public class ChangePhoneNumberActivity extends BaseActivity implements View.OnClickListener, UserUpdateCallback {
    Button changePhoneNumber;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phonenumber);
        setUpToolBar(true);
        setTitle("Update Phone Number");
        setBackButton(true,false);
        changePhoneNumber = (Button) findViewById(R.id.changePhone);
        changePhoneNumber.setOnClickListener(this);
    }

    @Override
    public void setContentView(int resId) {
        super.setContentView(resId);
        ImageView view = (ImageView) findViewById(R.id.glow_layout);
        BitmapUtils.loadBitmapResource(view, R.drawable.glow_sign_up);
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        final String phNumber = phoneNumber.getText().toString();
        boolean isTransitionAllowed = true;
        if (!Utils.isValidCellPhone(phNumber)) {
            isTransitionAllowed = false;
            phoneNumber.setError("Enter a valid " + Constants.PHONE_NUMBER_LENGTH + " digit phone number");
        }
        if (isTransitionAllowed) {
            enableButton(false);
            JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PHONE);
            Utils.hideSoftKeyboard(this);
            UserService.lookUpUserPhone(this, phNumber, new LookUpCallback() {
                @Override
                public void onLookUpCallback(String status, Exception error) {
                    if (status != null) {
                        if (ACTIVATED.value.equals(status) || STARTED_ACCOUNT.value.equals(status)) {
                            enableButton(true);
                            Utils.showAlert(ChangePhoneNumberActivity.this, "This phone number is associated with another account. Please enter a different phone number.");
                        } else if (INVALID_EMAIL.value.equals(status)) {
                            enableButton(true);
                            Utils.showAlert(ChangePhoneNumberActivity.this, "Please enter a valid phone number.");
                        } else {
                            enableButton(false);
                            UserService.changeContactNumber(phNumber, ChangePhoneNumberActivity.this);
                        }
                    } else {
                        enableButton(true);
                        Utils.showErrorAlert(ChangePhoneNumberActivity.this, error);
                    }
                }
            });
        }
    }

    private void enableButton(boolean isEnabled) {
        isBackButtonEnabled = isEnabled;
        Button continueBtn = (Button) findViewById(R.id.changePhone);
        continueBtn.setEnabled(isEnabled);
        phoneNumber.setEnabled(isEnabled);
        if (isEnabled) {
            continueBtn.setText("Update Phone Number");
        } else {
            continueBtn.setText("Please wait...");
        }
    }

    @Override
    public void onUserUpdateCallback(Exception error) {
        enableScreen(true);
        if (error == null) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
            String message = "Your phone number has been successfully changed.";
            alertdialog.setMessage(message).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            }).show();
            //Update CLP Customer on updating information
            User user = UserService.getUser();
            ((JambaApplication) this.getApplication()).updateCustomerInfo(user);
        } else {
            Utils.showErrorAlert(this, error);
        }
    }
}
