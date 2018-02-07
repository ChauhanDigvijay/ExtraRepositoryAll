package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.olo.jambajuice.BusinessLogic.Interfaces.UserServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.spendgo.Utils.SpendGoConstants;

import static com.olo.jambajuice.Utils.Constants.B_CONTACT_NUMBER;
import static com.olo.jambajuice.Utils.Constants.B_DOB;
import static com.olo.jambajuice.Utils.Constants.B_EMAIL;
import static com.olo.jambajuice.Utils.Constants.B_EMAIL_OPT_IN;
import static com.olo.jambajuice.Utils.Constants.B_FIRST_NAME;
import static com.olo.jambajuice.Utils.Constants.B_LAST_NAME;
import static com.olo.jambajuice.Utils.Constants.B_PREFERRED_STORE_ID;
import static com.olo.jambajuice.Utils.Constants.B_PUSH_OPT_IN;
import static com.olo.jambajuice.Utils.Constants.B_SMS_OPT_IN;

public class SignUpPasswordActivity extends SignUpBaseActivity implements View.OnClickListener, UserServiceCallback {
    private Button createAccountBtn;
    private EditText password;
    private EditText confirmPassword;
    User users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password);
        setUpToolBar(true);
        isShowBasketIcon = false;
        setTitle("Password");
        setBackButton(true,false);
       initializeCircle(8);
        createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmpassword);

        String pass = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();

        if (!pass.equals(confirmPass)) {
            Utils.showAlert(this, "Please ensure the password and confirmation are same.", "Passwords do not match");
        } else if (!Utils.isValidPassword(pass) || Utils.isEmptyString(pass) || Utils.isEmptyString(confirmPass)) {
            Utils.showAlert(this, Constants.INVALID_PASSWORD_ERROR, "Invalid Password");
            password.requestFocus();
        } else {
            sendSignUpCall(pass);
        }
    }

    private void sendSignUpCall(String password) {
        setButtonEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String fName = bundle.getString(B_FIRST_NAME);
            String lName = bundle.getString(B_LAST_NAME);
            String email = bundle.getString(B_EMAIL);
            String dob = bundle.getString(B_DOB);
            boolean email_opt_in = bundle.getBoolean(B_EMAIL_OPT_IN);
            boolean sms_opt_in = bundle.getBoolean(B_SMS_OPT_IN);
            boolean push_opt_in = bundle.getBoolean(B_PUSH_OPT_IN);
            String contactNumber = bundle.getString(B_CONTACT_NUMBER);
            int favoriteStoreId = Integer.parseInt(bundle.getString(B_PREFERRED_STORE_ID));
            String favoriteStoreCode = bundle.getString(B_PREFERRED_STORE_ID);
            users=new User();
            users.setFirstname(fName);
            users.setLastname(lName);
            users.setEmailaddress(email);
            users.setDob(dob);
            users.setEnableEmailOpt(email_opt_in);
            users.setEnableSmsOpt(sms_opt_in);
            users.setEnablePushOpt(push_opt_in);
            users.setContactnumber(contactNumber);
            users.setSpendGoFavoriteStoreId(favoriteStoreId);
            UserService.signUpUser(fName, lName, email, dob, email_opt_in, sms_opt_in, contactNumber, password, favoriteStoreCode, this);
        }
    }

    @Override
    public void onUserServiceCallback(User user, Exception exception) {
        setButtonEnabled(true);
        if (user != null) {
            signUpValidated();
        }
        // Incase exchanging authToken with olo is failed both params will be null or if email is not validated then error will be returned. Hence show user signUpSuccessful screen.
        else if (Utils.getErrorCode(exception) == SpendGoConstants.SERVER_ERROR.EMAIL_NOT_VALIDATED.value || (user == null && exception == null)) {
            signUpSuccessful();
            ((JambaApplication) this.getApplication()).registerCustomer(users);
        }
        else
        {
            Utils.showErrorAlert(this, exception);
        }

    }

    private void signUpValidated() {
        Utils.notifyHomeScreenUpdateAndTransitBack(this);
    }

    private void signUpSuccessful() {
        TransitionManager.transitFrom(SignUpPasswordActivity.this, SignUpSuccessfulActivity.class);
    }



    private void setButtonEnabled(boolean isenabled)
    {
        isBackButtonEnabled = isenabled;
        createAccountBtn.setEnabled(isenabled);
        password.setEnabled(isenabled);
        confirmPassword.setEnabled(isenabled);
        if (isenabled) {
            createAccountBtn.setText("Create Account");
        } else {
            createAccountBtn.setText("Creating Account...");
        }
    }

}
