package com.olo.jambajuice.Activites.NonGeneric.Splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.TransitionManager;

/**
 * Created by Ihsanulhaq on 7/13/2015.
 */
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverage);
        setUpMapImage();
        setClickListeners();
    }

    private void setClickListeners() {
        findViewById(R.id.beginBtn).setOnClickListener(this);
        SharedPreferenceHandler.put(SharedPreferenceHandler.IsFirstSplash, false);
    }

    private void setUpMapImage() {
        ImageView view = (ImageView) findViewById(R.id.map_image);
        ImageView splash_bg_layout = (ImageView) findViewById(R.id.splash_bg_layout);
        BitmapUtils.loadBitmapResourceWithViewSize(view, R.drawable.order_ahead_map, false);
        BitmapUtils.loadBitmapResource(splash_bg_layout, R.drawable.splash_bg);
    }

    @Override
    public void onClick(View v) {

        TransitionManager.transitFrom(this, HomeActivity.class);
        finish();
    }
}
