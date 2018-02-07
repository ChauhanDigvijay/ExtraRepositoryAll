package com.BasicApp.Activites.NonGeneric.Authentication.SignIn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.Utils.FBUtils;
import com.basicmodule.sdk.R;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBSessionService;

/**
 * Created by digvijay(dj)
 */
public class ChangePasswordModelActivity extends BaseActivity implements View.OnClickListener {
    EditText old, neww, confirmnew;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);

        old = (EditText) findViewById(R.id.oldpass);
        neww = (EditText) findViewById(R.id.newpass);
        confirmnew = (EditText) findViewById(R.id.confirmnewpass);
        change = (Button) findViewById(R.id.changepass);
        change.setOnClickListener(this);

        setUpToolBar(true,true);
        setTitle("ChangePassword");
        setBackButton(false,false);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void onCustomBackPressed() {
        ChangePasswordModelActivity.this.finish();
    }


    @Override
    public void onClick(View v) {
        FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_Password);
        if (checkValidation()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("Change password");

            alertDialog.setMessage("Do you want to change password");

            alertDialog.setIcon(R.drawable.logo);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Bundle extras = new Bundle();

                    changePassword();

                }
            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }


    public boolean checkValidation() {

        if (!(FBUtils.isValidString(neww.getText().toString()))) {
            FBUtils.showAlert(this, "Empty password");
            return false;
        }
        if (!(FBUtils.isValidString(old.getText().toString()))) {
            FBUtils.showAlert(this, "Empty Old password");
            return false;
        }

        if (neww.getText().toString().length() < 6) {
            FBUtils.showAlert(this, "Minimum digit required six");
            return false;
        }
        if (old.getText().toString().length() < 6) {
            FBUtils.showAlert(this, "Minimum digit required six");
            return false;
        }

        return true;
    }

    public void changePassword() {
        enableScreen(false);
        changePassword(old.getText().toString(), neww.getText().toString());
    }


    public void changePassword(String oldpassword, String newpassword) {

        FBSessionService.changePassword(oldpassword, newpassword, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem fbsessionItem, Exception error) {
                if (fbsessionItem != null)

                {
                    enableScreen(true);
                    ChangePasswordModelActivity.this.finish();


                } else {
                    enableScreen(true);
                    FBUtils.showErrorAlert(ChangePasswordModelActivity.this, error);
                }
            }
        });
    }


}