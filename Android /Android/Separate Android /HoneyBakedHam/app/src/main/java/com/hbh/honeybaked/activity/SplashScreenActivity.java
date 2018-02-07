package com.hbh.honeybaked.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseActivity;

public class SplashScreenActivity extends BaseActivity {
    Context context = null;
    String notificationType = "";
    String offerUrl = "";
    String push_alert_msg = "";




    private static int SPLASH_TIME_OUT = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (SplashScreenActivity.this.hbha_pref_helper.getIntValue("login_flag") == 1) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    i.putExtra("offerUrl", SplashScreenActivity.this.offerUrl);
                    i.putExtra("notificationType", SplashScreenActivity.this.notificationType);
                    SplashScreenActivity.this.startActivity(i);
                    SplashScreenActivity.this.finish();
                    return;
                }
                SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, LoginMainActivity.class));
                SplashScreenActivity.this.finish();

            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onClick(View v) {

    }
}

