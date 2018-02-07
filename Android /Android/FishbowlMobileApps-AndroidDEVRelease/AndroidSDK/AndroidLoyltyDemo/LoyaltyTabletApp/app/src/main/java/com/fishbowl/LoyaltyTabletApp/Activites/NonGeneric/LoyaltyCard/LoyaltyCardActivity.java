package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.LoyaltyCard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fishbowl.LoyaltyTabletApp.R;

/**
 * Created by schaudhary_ic on 11-Nov-16.
 */

public class LoyaltyCardActivity extends FragmentActivity implements View.OnClickListener {
    ImageView im1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_card);
        im1 = (ImageView) findViewById(R.id.bt3);
        im1.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt3:
                this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                this.finish();
                break;

        }
    }
}
