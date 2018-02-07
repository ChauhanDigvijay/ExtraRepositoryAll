package com.BasicApp.Activites.NonGeneric.Authentication.SignIn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONObject;

/**
 * Created by schaudhary_ic on 31-May-16.
 */
public class ChangePasswordActivity extends Activity implements View.OnClickListener {
    EditText old, neww, confirmnew;
    Button change;
    ProgressBarHandler progressBarHandler = null;
    RelativeLayout mtoolbar;
    private NetworkImageView loginBackground, headerImage, imlogo;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);
        imlogo = (NetworkImageView) findViewById(R.id.iv_logo);
        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });
        old = (EditText) findViewById(R.id.oldpass);
        neww = (EditText) findViewById(R.id.newpass);
        confirmnew = (EditText) findViewById(R.id.confirmnewpass);
        change = (Button) findViewById(R.id.changepass);
        change.setOnClickListener(this);
        progressBarHandler = new ProgressBarHandler(this);
        //headerImage = (NetworkImageView) findViewById(R.id.headerImage);
        loginBackground = (NetworkImageView) findViewById(R.id.backgroundProfile);
    }

    public void onCustomBackPressed() {
        ChangePasswordActivity.this.finish();
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


    @Override
    public void onClick(View v) {
        FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_Password);
        String oldPass = old.getText().toString();
        String newPass = neww.getText().toString();
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


    public void changePassword() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("oldPassword", old.getText().toString());
            obj.put("password", neww.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBarHandler.show();
        FBUserService.sharedInstance().changePassword(obj, new FBUserService.FBChangePasswordCallback() {
            @Override
            public void onChangePasswordCallback(JSONObject response, Exception error) {
                progressBarHandler.hide();
                if (response != null) {
                    ChangePasswordActivity.this.finish();
                } else {
                    FBUtils.showErrorAlert(ChangePasswordActivity.this, error);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
//
//            change.setBackgroundColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));
//
//            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//
//
//        }


    }
}