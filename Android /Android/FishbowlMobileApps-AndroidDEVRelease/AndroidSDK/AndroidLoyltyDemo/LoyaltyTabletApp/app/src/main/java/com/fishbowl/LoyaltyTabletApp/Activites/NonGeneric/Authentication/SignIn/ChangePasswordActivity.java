package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;

public class ChangePasswordActivity extends FragmentActivity {
    public NetworkImageView backgroundImage;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        backgroundImage = (NetworkImageView) findViewById(R.id.img_Back);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        backgroundImage = (NetworkImageView) findViewById(R.id.img_Back);
        final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().signUpBackgroundImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(backgroundImage, R.drawable.signup, R.drawable.signup));
        backgroundImage.setImageUrl(url, mImageLoader);
    }
}
