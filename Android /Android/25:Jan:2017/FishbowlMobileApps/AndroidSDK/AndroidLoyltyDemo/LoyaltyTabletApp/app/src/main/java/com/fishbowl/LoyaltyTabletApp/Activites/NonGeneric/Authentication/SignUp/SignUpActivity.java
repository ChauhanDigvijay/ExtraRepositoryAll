package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignUp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;


public class SignUpActivity extends FragmentActivity {
    private NetworkImageView background;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    }

    public void initailui() {
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        background = (NetworkImageView) findViewById(R.id.img_Back);



        final String signUpBackgroundImageUrl = FBThemeMobileSettingsService.sharedInstance().registermapsetting.get("SignUpBackgroundImageUrl");
        //signup image/color
        if(StringUtilities.isValidString(signUpBackgroundImageUrl)) {
            final String signupbgurl = "http://" + signUpBackgroundImageUrl;
            mImageLoader.get(signupbgurl, ImageLoader.getImageListener(background, R.color.white, R.color.white));
            background.setImageUrl(signupbgurl, mImageLoader);
        }

        else if(FBThemeMobileSettingsService.sharedInstance().registermapsetting.get("SignUpBackgroundColor")!=null)
        {
            final String signUpBackgroundColor =  FBThemeMobileSettingsService.sharedInstance().registermapsetting.get("SignUpBackgroundColor");
            if(StringUtilities.isValidString(signUpBackgroundColor))
                background.setBackgroundColor(Color.parseColor(signUpBackgroundColor));
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        initailui();
    }


    @Override
    public void onStart() {
        super.onStart();

    }
}