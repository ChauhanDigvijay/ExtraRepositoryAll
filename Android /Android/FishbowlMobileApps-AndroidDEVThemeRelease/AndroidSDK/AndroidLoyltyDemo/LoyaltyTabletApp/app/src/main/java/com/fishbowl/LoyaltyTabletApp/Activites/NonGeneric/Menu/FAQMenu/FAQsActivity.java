package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.FAQMenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home.HomeActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardAndOfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.fishbowl.LoyaltyTabletApp.R.id.img_Back;

/**
 * Created by schaudhary_ic on 10-Nov-16.
 */

public class FAQsActivity extends FragmentActivity implements View.OnClickListener {
    Button log;
    TextView title, title_welcome;
    RelativeLayout toolbar, toolbar2;
    NetworkImageView backgroundImage;
    LinearLayout profile_way;
    LinearLayout layout_button, layout_logout, layout_loyalty;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage, titlebackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar2);
        profile_way = (LinearLayout) findViewById(R.id.profile_way);
        profile_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        inititateui();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void inititateui() {

        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        backgroundImage = (NetworkImageView) findViewById(img_Back);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar2);
        titleimage = (NetworkImageView) toolbar.findViewById(R.id.backbutton);
        title_welcome = (TextView) toolbar.findViewById(R.id.title_welcome);
        profile_way = (LinearLayout) toolbar.findViewById(R.id.profile_way);
        profile_way.setOnClickListener(this);
        title = (TextView) toolbar.findViewById(R.id.title_textb);
        titlebackground = (NetworkImageView) toolbar.findViewById(img_Back);
        layout_logout = (LinearLayout) toolbar.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(this);


        layout_button = (LinearLayout) toolbar.findViewById(R.id.layout_button);
        layout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FAQsActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        layout_loyalty = (LinearLayout) toolbar.findViewById(R.id.layout_loyalty);
        layout_loyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FAQsActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });




        //header image/color
        final String generalLogoImageUrl =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralHeaderImageUrl");
        if(StringUtilities.isValidString(generalLogoImageUrl))
        {
            final String loginleftbgurl = "http://" + generalLogoImageUrl;
            mImageLoader.get(loginleftbgurl, ImageLoader.getImageListener(titlebackground, R.color.white, R.color.white));
            titlebackground.setImageUrl(loginleftbgurl, mImageLoader);
        }
        else if(FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralHeaderColor")!=null)
        {
            final String generalHeaderColor =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralHeaderColor");
            if(StringUtilities.isValidString(generalHeaderColor))
                titlebackground.setBackgroundColor(Color.parseColor(generalHeaderColor));
        }

        //headerlogo image
        final String generalHeaderImageUrl = "http://" +  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralLogoImageUrl");
        mImageLoader.get(generalHeaderImageUrl, ImageLoader.getImageListener(titleimage, R.color.white, R.color.white));
        titleimage.setImageUrl(generalHeaderImageUrl, mImageLoader);





        String firstName = FB_LY_UserService.sharedInstance().member.firstName;
        title.setText(firstName);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_logout:
                logout();
                break;
            case R.id.profile_way:
                onBackPressed();
                break;
        }

    }

    public void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(FAQsActivity.this).create();
        alertDialog.setTitle("Logout ");
        alertDialog.setMessage("Press ok to Logout ");
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JSONObject object = new JSONObject();
                try {
                    object.put("Application", "mobile");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bundle extras = new Bundle();
                Intent i = new Intent(FAQsActivity.this, SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(extras);
                startActivity(i);
                ArrayList<RewardAndOfferSummary> l = new ArrayList<RewardAndOfferSummary>();
                l.clear();
                RewardSummary.rewardList.clear();
                OfferSummary.offerList.clear();

                FAQsActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
}
