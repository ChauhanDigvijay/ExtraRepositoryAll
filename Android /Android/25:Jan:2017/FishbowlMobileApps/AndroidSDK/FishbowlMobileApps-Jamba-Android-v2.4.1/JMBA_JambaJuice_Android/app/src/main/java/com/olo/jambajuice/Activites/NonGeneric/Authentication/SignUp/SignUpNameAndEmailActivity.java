package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.LookUpCallback;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CapitalizeTextWatcher;

import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.ACTIVATED;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.INVALID_EMAIL;
import static com.wearehathway.apps.spendgo.Utils.SpendGoConstants.LOOK_UP_STATUS.STARTED_ACCOUNT;


public class SignUpNameAndEmailActivity extends SignUpBaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private SwitchCompat emailSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_name_email);
        setUpToolBar(true);
        isShowBasketIcon = false;
        setTitle("Name & Email");
        setBackButton(true,false);
        initializeCircle(4);
        Button continueBtn = (Button) findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(this);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        firstName.addTextChangedListener(new CapitalizeTextWatcher(firstName));
        lastName.addTextChangedListener(new CapitalizeTextWatcher(lastName));
        email = (EditText) findViewById(R.id.email);
        emailSwitch = (SwitchCompat) findViewById(R.id.emailSwitch);
        firstName.setOnFocusChangeListener(this);
        lastName.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.USER_ACCOUNT.value, "login_start");

    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        final String fName = firstName.getText().toString();
        final String lName = lastName.getText().toString();
        final String emailStr = email.getText().toString();

        boolean isTransitionAllowed = true;
        if (!Utils.isValidName(fName)) {
            firstName.requestFocus();
            firstName.setError("Enter first name");
            isTransitionAllowed = false;
        }
        if (!Utils.isValidName(lName)) {
            if (isTransitionAllowed) {
                lastName.requestFocus();
            }
            lastName.setError("Enter last name");
            isTransitionAllowed = false;
        }
        if (!Utils.isValidEmail(emailStr)) {
            if (isTransitionAllowed) {
                email.requestFocus();
            }
            email.setError("Enter a valid email address");
            isTransitionAllowed = false;
        }
        if (isTransitionAllowed) {
            if (emailSwitch.isChecked()) {
                showConfirmationAlert();
            } else {
                checkEmailAndTransitToNextScreen();
            }

        }
    }

    private void showConfirmationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("I agree to receive personalized offers by email.");
        alertDialogBuilder.setTitle("Personalized Offers");
        alertDialogBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                checkEmailAndTransitToNextScreen();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                emailSwitch.setChecked(false);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void checkEmailAndTransitToNextScreen() {
        final String fName = firstName.getText().toString();
        final String lName = lastName.getText().toString();
        final String emailStr = email.getText().toString();
        enableButton(false);
        UserService.lookUpUserEmail(SignUpNameAndEmailActivity.this, emailStr, new LookUpCallback() {
            @Override
            public void onLookUpCallback(String status, Exception error) {
                enableButton(true);
                if (status != null) {
                    if (ACTIVATED.value.equals(status) || STARTED_ACCOUNT.value.equals(status)) {
                        Utils.showAlert(SignUpNameAndEmailActivity.this, "This email address is associated with another account. Please enter a different email address or proceed to Sign In.");
                    } else if (INVALID_EMAIL.value.equals(status)) {
                        Utils.showAlert(SignUpNameAndEmailActivity.this, "Please enter a valid email address");
                    } else {
                        SwitchCompat emailSwitch = (SwitchCompat) findViewById(R.id.emailSwitch);
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            bundle.putString(Constants.B_FIRST_NAME, fName);
                            bundle.putString(Constants.B_LAST_NAME, lName);
                            bundle.putString(Constants.B_EMAIL, emailStr);
                            bundle.putBoolean(Constants.B_EMAIL_OPT_IN, emailSwitch.isChecked());
                            TransitionManager.transitFrom(SignUpNameAndEmailActivity.this, SignUpPhoneNumberActivity.class, false, bundle);
                        }
                    }
                } else {
                    Utils.showErrorAlert(SignUpNameAndEmailActivity.this, error);
                }
            }
        });
    }

    private void enableButton(boolean isEnabled) {
        isBackButtonEnabled = isEnabled;
        Button continueBtn = (Button) findViewById(R.id.continueBtn);
        continueBtn.setEnabled(isEnabled);
        email.setEnabled(isEnabled);
        firstName.setEnabled(isEnabled);
        lastName.setEnabled(isEnabled);
        if (isEnabled) {
            continueBtn.setText("Continue");
        } else {
            continueBtn.setText("Please wait...");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            TextView question = (TextView) findViewById(R.id.question);
            switch (v.getId()) {
                case R.id.firstName:
                    question.setText("What is your first name?");
                    break;
                case R.id.lastName:
                    question.setText("What is your last name?");
                    break;
                case R.id.email:
                    question.setText("What is your email?");
                    break;
            }
        }
    }

}
