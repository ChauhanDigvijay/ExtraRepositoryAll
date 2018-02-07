package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ContactUsMenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

/**
 * Created by schaudhary_ic on 10-Nov-16.
 */

public class ContactUsActivity extends FragmentActivity implements View.OnClickListener {
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        inititateui();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void inititateui() {

        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
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

        layout_button = (LinearLayout) toolbar.findViewById(R.id.layout_button);
        layout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContactUsActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        layout_loyalty = (LinearLayout) toolbar.findViewById(R.id.layout_loyalty);
        layout_loyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContactUsActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
        titlebackground.setBackgroundResource(R.drawable.header);
        String firstName = FB_LY_UserService.sharedInstance().member.firstName;
        title.setText(firstName);

        titlebackground = (NetworkImageView) toolbar.findViewById(R.id.img_Back);
        mImageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();
        final String url3 = "http://" + FB_LY_MobileSettingService.sharedInstance().loginRightTopImageUrl;
        mImageLoader.get(url3, ImageLoader.getImageListener(titlebackground, R.color.white, R.color.white));
        titlebackground.setImageUrl(url3, mImageLoader);
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
        AlertDialog alertDialog = new AlertDialog.Builder(ContactUsActivity.this).create();
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
                Intent i = new Intent(ContactUsActivity.this, SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(extras);
                startActivity(i);
                if (RewardSummary.rewardList != null) {
                    if (RewardSummary.rewardList.size() > 0) {
                        RewardSummary.rewardList.clear();

                    }
                }
                if (OfferSummary.offerList != null) {
                    if (OfferSummary.offerList.size() > 0) {

                        OfferSummary.offerList.clear();
                    }
                }
                ContactUsActivity.this.finish();
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
