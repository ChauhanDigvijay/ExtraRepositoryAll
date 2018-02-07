package com.android.societysolutionregistration.Activities.NonGeneric.Authentication.SignIn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.societysolutionregistration.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

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

    }
}
