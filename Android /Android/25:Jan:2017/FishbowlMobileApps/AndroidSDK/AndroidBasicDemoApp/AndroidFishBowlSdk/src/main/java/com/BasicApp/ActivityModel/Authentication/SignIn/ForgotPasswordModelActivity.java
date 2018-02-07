package com.BasicApp.ActivityModel.Authentication.SignIn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBSessionService;

/**
 * Created by schaudhary_ic on 30-May-16.
 */
public class ForgotPasswordModelActivity extends Activity implements View.OnClickListener {
    NetworkImageView imgBack;
    Button sendEmail;
    EditText email;
    ImageView imageView, back;
    RelativeLayout mtoolbar;
    TextView title;
    ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword_bistro);
        imgBack = (NetworkImageView) findViewById(R.id.img_Back);
        sendEmail = (Button) findViewById(R.id.btn_forgetpass);
        sendEmail.setOnClickListener(this);
        email = (EditText) findViewById(R.id.edt_forgetpassword);
        imageView = (ImageView) findViewById(R.id.imageforgot);
        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);

        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });

    }
    public void onCustomBackPressed() {
        ForgotPasswordModelActivity.this.finish();
    }
    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.backbutton) {
            this.finish();

        }

        if (v.getId() == R.id.btn_forgetpass) {
            forgetPassword();

        }

    }

    @Override
    public void onBackPressed() {

        this.finish();
    }

    public void forgetPassword() {

        forgetPassword(email.getText().toString());
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("email", email.getText().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        FBUserService.sharedInstance().forgetPassword(obj, new FBUserService.FBforgetPasswordCallback() {
//            @Override
//            public void onFBforgetPasswordCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.FORGOT_PASSWORD);
//                    ForgotPasswordModelActivity.this.finish();
//                } else {
//                    FBUtils.showErrorAlert(ForgotPasswordModelActivity.this, error);
//                }
//            }
//        });
    }


    public void forgetPassword(String email) {

        FBSessionService.forgetPassword(email, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null)

                {
                    ForgotPasswordModelActivity.this.finish();



                } else {
                    //    Constants.alertDialogShow(SignInModelActivity.this, "loginMember Error Message");
                    FBUtils.showErrorAlert(ForgotPasswordModelActivity.this, error);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
//
//            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().loginBackgroundImageUrl;
//            mImageLoader.get(url, ImageLoader.getImageListener(imgBack, R.drawable.bgimage, R.drawable.bgimage));
//
//        }
    }
}
