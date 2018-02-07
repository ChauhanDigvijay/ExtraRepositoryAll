package com.BasicApp.Activites.NonGeneric.Authentication.SignIn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBSessionService;

/**
 * Created by digvijay(dj)
 */
public class ForgotPasswordModelActivity extends BaseActivity implements View.OnClickListener {
    NetworkImageView imgBack;
    Button sendEmail;
    EditText email;
    RelativeLayout mtoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword_bistro);
        imgBack = (NetworkImageView) findViewById(R.id.img_Back);
        sendEmail = (Button) findViewById(R.id.btn_forgetpass);
        sendEmail.setOnClickListener(this);
        email = (EditText) findViewById(R.id.edt_forgetpassword);

        setUpToolBar(true,true);
        setTitle("Forget Password");
        setBackButton(false,false);



    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View v) {




        if (v.getId() == R.id.btn_forgetpass) {
            forgetPassword();

        }

    }


    public boolean checkValidation()
    {

        if (!(FBUtils.isValidEmail(email.getText().toString()))) {
            FBUtils.showAlert(this, "Please enter valid email.");
            return false;
        }


        return true;
    }

    public void forgetPassword() {
        if (checkValidation()) {
        forgetPassword(email.getText().toString());
    }
    }


    public void forgetPassword(String email) {

        enableScreen(false);
        FBSessionService.forgetPassword(email, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null)

                {
                    enableScreen(true);
                    ForgotPasswordModelActivity.this.finish();


                } else {
                    enableScreen(true);
                    FBUtils.showErrorAlert(ForgotPasswordModelActivity.this, error);
                }
            }
        });
    }

}
