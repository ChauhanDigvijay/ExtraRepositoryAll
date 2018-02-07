package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ContactUsMenu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.Generic.BaseActivity;
import com.fishbowl.LoyaltyTabletApp.R;

/**
 * Created by schaudhary_ic on 10-Nov-16.
 */

public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
    Button log;
    TextView title, title_welcome;
    RelativeLayout toolbar;
    LinearLayout profile_way;
    LinearLayout layout_button, layout_logout, layout_loyalty;
    NetworkImageView backgroundImage;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage, titlebackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setUpToolBar(true, true);
        setToolbarConfig(true, true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
