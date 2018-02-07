package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignUp;

import android.os.Bundle;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.Generic.BaseActivity;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;

public class SignUpActivity extends BaseActivity {
    private NetworkImageView background;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    }

    public void initailui() {
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        background = (NetworkImageView) findViewById(R.id.img_Back);
        final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().signUpBackgroundImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(background, R.color.white, R.color.white));
        background.setImageUrl(url, mImageLoader);
        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initailui();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}