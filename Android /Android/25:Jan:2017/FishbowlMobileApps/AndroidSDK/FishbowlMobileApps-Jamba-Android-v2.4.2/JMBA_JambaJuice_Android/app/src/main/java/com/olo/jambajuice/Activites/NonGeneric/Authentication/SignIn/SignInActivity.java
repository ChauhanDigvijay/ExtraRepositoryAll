package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignIn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;

import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.FBSDKLoginServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserLogoutCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.RecentOrdersService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

public class SignInActivity extends BaseActivity implements View.OnClickListener, UserServiceCallback, UserLogoutCallback {

    Button signIn;
    TextView forgotPass;
    private EditText email;
    private EditText password;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setUpToolBar(true, true);
        setTitle("Log In");
        setBackButton(false,false);
        signIn = (Button) findViewById(R.id.signIn);
        forgotPass = (TextView) findViewById(R.id.forgotPassword);
        signIn.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
    }

    @Override
    public void setContentView(int resId) {
        super.setContentView(resId);
        ImageView view = (ImageView) findViewById(R.id.splash_bg_layout);
        try {
            BitmapUtils.loadBitmapResource(view, R.drawable.splash_bg, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        trackButton(v);
        int id = v.getId();
        if (id == R.id.signIn) {
            signInPressed();
        } else if (id == R.id.forgotPassword) {
            forgotPasswordPressed();
        }
    }

    private void signInPressed() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        boolean isValid = true;

        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        if (!(Utils.isValidEmail(emailStr) || Utils.isValidCellPhone(emailStr))) {
            isValid = false;
            email.setError("Please enter valid email or phone number.");
        }
        if (!Utils.isValidPassword(passwordStr)) {
            isValid = false;
            password.setError("Invalid password.");
        }
        if (isValid) {
            setButtonEnabled(false);
            UserService.signInUser(emailStr, passwordStr, this);
        }
    }

    private void forgotPasswordPressed() {
        TransitionManager.transitFrom(SignInActivity.this, ForgotPasswordActivity.class);
    }

    private void setButtonEnabled(boolean isenabled) {
        isBackButtonEnabled = isenabled;
        signIn.setEnabled(isenabled);
        email.setEnabled(isenabled);
        password.setEnabled(isenabled);
        if (isenabled) {
            signIn.setText("Log In");
        } else {
            signIn.setText("Please wait...");
        }
    }

    @Override
    public void onUserServiceCallback(User user, Exception exception) {
        setButtonEnabled(true);
        if (user != null) {
            DataManager manager = DataManager.getInstance();
            manager.resetDataManager();//reset existing local instances (store selection,basket...)
            manager.setAllProductFamily(null);
            manager.setAllCategories(null);

            //onBackPressed();
            if(user.getEmailaddress() != null) {
                AnalyticsManager.getInstance().setUserId(user.getEmailaddress().toString().substring(0, user.getEmailaddress().toString().indexOf("@")));
            }

            AnalyticsManager.getInstance().trackEvent(
                    Constants.GA_CATEGORY.USER_ACCOUNT.value, "login_complete");

            //login CLP Customer
            fbLogin(user);

            RecentOrdersService.clearAllData();//clear recently ordered product during guest session

        } else {
            Utils.showErrorAlert(this, exception);
        }
    }

    private void logout() {
        UserService.logout(this);
    }


    private void fbLogin(final User user){
        setButtonEnabled(false);
        ((JambaApplication) this.getApplication()).loginFb(user, new FBSDKLoginServiceCallBack() {
            @Override
            public void fbSdkLoginCallBack(Boolean success) {
                setButtonEnabled(true);
                JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.LOGIN);
                if (success) {
                    SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, "");
                    Intent intent = new Intent(Constants.BROADCAST_UPDATE_HOME_ACTIVITY);
                    LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).sendBroadcast(intent);
                    UserService.getUser().setTotalOffers(-1);
                    onBackPressed();
                } else {
                    SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, "");
                    Intent intent = new Intent(Constants.BROADCAST_UPDATE_HOME_ACTIVITY);
                    LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).sendBroadcast(intent);
                    UserService.getUser().setTotalOffers(-1);
                    onBackPressed();
                }
            }
        });
    }
    @Override
    public void onUserLogoutCallback(Exception exception) {
        DataManager.getInstance().resetDataManager();
        SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, "");
        BasketFlagViewManager.getInstance().removeBasketFlag();
        Utils.showErrorAlert(SignInActivity.this, "Something wrong in server,Please try again.");
    }
}
