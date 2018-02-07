package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.olo.jambajuice.BusinessLogic.Interfaces.LookUpCallback;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.ACTIVATED;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.INVALID_EMAIL;

public class SignUpPhoneNumberActivity extends SignUpBaseActivity implements View.OnClickListener {

    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_phonenumber);
        setUpToolBar(true);
        isShowBasketIcon = false;
        setTitle("Phone Number");
        setBackButton(true,false);
        initializeCircle(5);
        Button continueBtn = (Button) findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        final String phNumber = phoneNumber.getText().toString();
        boolean isTransitionAllowed = true;
        if (!Utils.isValidCellPhone(phNumber)) {
            phoneNumber.requestFocus();
            isTransitionAllowed = false;
            phoneNumber.setError("Enter a valid " + Constants.PHONE_NUMBER_LENGTH + " digit phone number");
        }
        if (isTransitionAllowed) {
            SwitchCompat smsSwitch = (SwitchCompat) findViewById(R.id.phoneSwitch);
            if (smsSwitch.isChecked()) {
                showConfirmationAlert();
            } else {
                checkPhoneNumberAndTransitScreen();
            }

        }
    }

    private void showConfirmationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("I agree to receive personalized offers by text message. Standard carrier charges may apply.");
        alertDialogBuilder.setTitle("Personalized Offers");
        alertDialogBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                checkPhoneNumberAndTransitScreen();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void checkPhoneNumberAndTransitScreen() {
        final String phNumber = phoneNumber.getText().toString();
        enableButton(false);
        UserService.lookUpUserPhone(SignUpPhoneNumberActivity.this, phNumber, new LookUpCallback() {
            @Override
            public void onLookUpCallback(String status, Exception error) {
                enableButton(true);
                if (status != null) {
                    if (ACTIVATED.value.equals(status)) {
                        Utils.showAlert(SignUpPhoneNumberActivity.this, "This phone number is associated with another account. Please enter a different phone number or proceed to Sign In.");
                    } else if (INVALID_EMAIL.value.equals(status)) {
                        Utils.showAlert(SignUpPhoneNumberActivity.this, "Please enter a valid phone number.");
                    } else {
                        SwitchCompat smsSwitch = (SwitchCompat) findViewById(R.id.phoneSwitch);
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            bundle.putString(Constants.B_CONTACT_NUMBER, phNumber);
                            bundle.putBoolean(Constants.B_SMS_OPT_IN, smsSwitch.isChecked());
                            TransitionManager.transitFrom(SignUpPhoneNumberActivity.this, SignUpPushNotificationActivity.class, bundle);
                        }
                    }
                }
            }
        });
    }

    private void enableButton(boolean isEnabled) {
        isBackButtonEnabled = isEnabled;
        Button continueBtn = (Button) findViewById(R.id.continueBtn);
        continueBtn.setEnabled(isEnabled);
        phoneNumber.setEnabled(isEnabled);
        if (isEnabled) {
            continueBtn.setText("Continue");
        } else {
            continueBtn.setText("Please wait...");
        }
    }
}
