package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by schaudhary_ic on 05-Dec-16.
 */

public class ProgramDeetailActivity extends FragmentActivity implements View.OnClickListener {
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
        setContentView(R.layout.activity_program_detail);
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
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar2);
        titleimage = (NetworkImageView) toolbar.findViewById(R.id.backbutton);
        final String url2 = "http://" + FB_LY_MobileSettingService.sharedInstance().companyLogoImageUrl;
        mImageLoader.get(url2, ImageLoader.getImageListener(titleimage, R.drawable.signup, R.drawable.signup));
        titlebackground = (NetworkImageView) toolbar.findViewById(R.id.img_Back);
        titleimage.setImageUrl(url2, mImageLoader);
        title_welcome = (TextView) toolbar.findViewById(R.id.title_welcome);
        profile_way = (LinearLayout) toolbar.findViewById(R.id.profile_way);
        profile_way.setOnClickListener(this);
        title = (TextView) toolbar.findViewById(R.id.title_textb);
        layout_logout = (LinearLayout) toolbar.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(this);
        backgroundImage = (NetworkImageView) findViewById(R.id.img_Back);
        final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().signUpBackgroundImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(backgroundImage, R.drawable.signup, R.drawable.signup));
        backgroundImage.setImageUrl(url, mImageLoader);
        layout_button = (LinearLayout) toolbar.findViewById(R.id.layout_button);
        layout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProgramDeetailActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });
        layout_loyalty = (LinearLayout) toolbar.findViewById(R.id.layout_loyalty);
        layout_loyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProgramDeetailActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
        mImageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();
        final String url3 = "http://" + FB_LY_MobileSettingService.sharedInstance().loginRightTopImageUrl;
        mImageLoader.get(url3, ImageLoader.getImageListener(titlebackground, R.color.white, R.color.white));
        titlebackground.setImageUrl(url3, mImageLoader);
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
        AlertDialog alertDialog = new AlertDialog.Builder(ProgramDeetailActivity.this).create();
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
                Intent i = new Intent(ProgramDeetailActivity.this, SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(extras);
                startActivity(i);
                ArrayList<RewardAndOfferSummary> l = new ArrayList<RewardAndOfferSummary>();
                l.clear();
                RewardSummary.rewardList.clear();
                OfferSummary.offerList.clear();

                ProgramDeetailActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
}
