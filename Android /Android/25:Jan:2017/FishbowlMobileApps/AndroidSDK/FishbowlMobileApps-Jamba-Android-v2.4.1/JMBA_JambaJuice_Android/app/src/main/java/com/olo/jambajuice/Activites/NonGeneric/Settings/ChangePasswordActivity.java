package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserUpdateCallback;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, UserUpdateCallback {
    Button changepasswordBtn;
    private EditText newPassword;
    private EditText confirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setUpToolBar(true);
        setTitle("Update Password");
        setBackButton(true,false);
        changepasswordBtn = (Button) findViewById(R.id.changePasswordBtn);
        changepasswordBtn.setOnClickListener(this);
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
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmNewPassword = (EditText) findViewById(R.id.confirmNewpassword);

        String newPass = newPassword.getText().toString();
        String confirmPass = confirmNewPassword.getText().toString();

        if (!newPass.equals(confirmPass)) {
            Utils.showAlert(this, "Please ensure the password and confirmation are same.", "Passwords do not match");
        } else if (Utils.isEmptyString(newPass) || Utils.isEmptyString(confirmPass)) {
            Utils.showAlert(this, "Password cannot be empty.", "Error");
        } else if (!Utils.isValidPassword(newPass)) {
            String error = Constants.INVALID_PASSWORD_ERROR;
            Utils.showAlert(this, error, "Invalid Passwords");
            newPassword.requestFocus();
        } else {
            Utils.hideSoftKeyboard(this);
            sendPasswordChangeCall(newPass);
        }
    }

    @Override
    public void onUserUpdateCallback(Exception exception) {
        isBackButtonEnabled = true;
        setButtonEnabled(true);
        if (exception != null) {
            changepasswordBtn.setText("Change Password");
            Utils.showErrorAlert(this, exception);
        } else {
            new AlertDialog.Builder(ChangePasswordActivity.this).setMessage("Your password has been changed successfully.").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TransitionManager.transitFrom(ChangePasswordActivity.this, SettingsActivity.class, true);
                }
            }).create().show();
        }
    }

    private void sendPasswordChangeCall(String newPassword) {
        setButtonEnabled(false);
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_Password);
        UserService.changeUserPassword(newPassword, this);
    }

    private void setButtonEnabled(boolean isenabled) {
        isBackButtonEnabled = isenabled;
        changepasswordBtn.setEnabled(isenabled);
        newPassword.setEnabled(isenabled);
        confirmNewPassword.setEnabled(isenabled);
        if (isenabled) {
            changepasswordBtn.setText("Update Password");
        } else {
            changepasswordBtn.setText("Please wait...");
        }
    }
}
