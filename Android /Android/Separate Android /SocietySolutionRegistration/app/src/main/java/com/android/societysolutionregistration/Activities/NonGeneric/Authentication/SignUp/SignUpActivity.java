package com.android.societysolutionregistration.Activities.NonGeneric.Authentication.SignUp;

import android.os.Bundle;

import com.android.societysolutionregistration.Activities.Generic.BaseActivity;
import com.android.societysolutionregistration.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


public class SignUpActivity extends BaseActivity {
    private NetworkImageView background;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    }

    @Override
    protected void onResume() {
        super.onResume();
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