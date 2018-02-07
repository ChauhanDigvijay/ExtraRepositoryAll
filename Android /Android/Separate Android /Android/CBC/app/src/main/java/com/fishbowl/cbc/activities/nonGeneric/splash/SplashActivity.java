package com.fishbowl.cbc.activities.nonGeneric.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.fishbowl.cbc.MainActivity;
import com.fishbowl.cbc.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(1000, 2000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                //TransitionManager.slideUp(SplashActivity.this, ProductFamilyActivity.class,false);
                finish();
            }
        }.start();
    }
}
