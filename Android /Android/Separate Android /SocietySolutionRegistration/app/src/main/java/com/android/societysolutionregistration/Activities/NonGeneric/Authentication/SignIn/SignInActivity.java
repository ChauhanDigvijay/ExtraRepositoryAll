package com.android.societysolutionregistration.Activities.NonGeneric.Authentication.SignIn;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.societysolutionregistration.Activities.Generic.BaseActivity;
import com.android.societysolutionregistration.R;
import com.android.societysolutionregistration.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class SignInActivity extends BaseActivity {
    TextView title;
    RelativeLayout toolbar;
    ProgressBarHandler progressBarHandler;
    String fbm;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage, titlebackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolBar(true, false);
        setToolbarConfig(true, true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }


}


