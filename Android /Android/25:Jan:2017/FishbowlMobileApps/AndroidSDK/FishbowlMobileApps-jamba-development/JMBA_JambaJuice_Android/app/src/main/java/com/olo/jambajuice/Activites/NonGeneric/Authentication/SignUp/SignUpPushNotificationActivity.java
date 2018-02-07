package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;

import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

public class SignUpPushNotificationActivity extends SignUpBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_push_notification);
        setUpToolBar(true);
        isShowBasketIcon = false;
        setTitle("Push Notification");
        setBackButton(true,false);
        initializeCircle(6);
        Button continueBtn = (Button) findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SwitchCompat pushSwitch = (SwitchCompat) findViewById(R.id.pushSwitch);
        if (pushSwitch.isChecked()) {
            showConfirmationAlert();
        } else {
            transmitToNextScreen();
        }

    }

    private void showConfirmationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("I agree to receive personalized updates and offers via push notifications.");
        alertDialogBuilder.setTitle("Push Notification");
        alertDialogBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                transmitToNextScreen();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void transmitToNextScreen(){
        SwitchCompat pushSwitch = (SwitchCompat) findViewById(R.id.pushSwitch);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.putBoolean(Constants.B_PUSH_OPT_IN, pushSwitch.isChecked());
            TransitionManager.transitFrom(SignUpPushNotificationActivity.this, SignUpDOBActivity.class, bundle);
        }
    }
}
