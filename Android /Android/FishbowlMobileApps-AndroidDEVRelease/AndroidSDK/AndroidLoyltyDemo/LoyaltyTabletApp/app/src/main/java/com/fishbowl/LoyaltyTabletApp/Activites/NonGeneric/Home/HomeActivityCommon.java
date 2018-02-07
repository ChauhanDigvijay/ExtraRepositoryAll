package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardsItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.OfferService;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.RewardService;
import com.fishbowl.LoyaltyTabletApp.Fragments.FragmentRewardsAndOffers;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityCommon extends FragmentActivity implements OnClickListener {

    Button log;
    TextView title, title_welcome;
    RelativeLayout toolbar;
    LinearLayout profile_way;
    LinearLayout layout_button, layout_logout, layout_loyalty;
    ProgressBarHandler p;
    ArrayList comboList = new ArrayList();
    private List<OfferItem> offerList = new ArrayList<OfferItem>();
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage, titlebackground;
    private List<RewardsItem> rewardList = new ArrayList<RewardsItem>();

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_common);
        p = new ProgressBarHandler(this);
      //  getOffer();
     //   getRewards();
       // inititateui();
    }


    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void getRewards() {
        p.show();
        if (RewardSummary.rewardList != null) {
            if (RewardSummary.rewardList.size() > 0) {
                setReward(RewardService.offerSummary);

            } else {
                fetchReward();
            }


        } else {
            fetchReward();

        }
    }

    private void fetchReward() {
        RewardService.getUserReward(this, new RewardSummaryCallback() {
            @Override
            public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception error) {
                if (rewardSummary != null) {
                    setReward(rewardSummary);
                    p.dismiss();
                } else {
                    p.dismiss();
                    FBUtils.tryHandleTokenExpiry(HomeActivityCommon.this, error);
                }
            }
        });
    }

    private void setReward(RewardSummary rewardSummary) {
        if (rewardSummary.getRewardList() != null) {
            rewardList = rewardSummary.getRewardList();

            FragmentRewardsAndOffers fragment = (FragmentRewardsAndOffers) getSupportFragmentManager().findFragmentById(R.id.image_frag);
            fragment.viewInflater();
            p.dismiss();
        } else if ((rewardSummary.getRewardList() == null)) {
            FragmentRewardsAndOffers fragment = (FragmentRewardsAndOffers) getSupportFragmentManager().findFragmentById(R.id.image_frag);
            fragment.viewInflater();
            p.dismiss();

        }

        if (rewardList != null && rewardList.size() > 0) {

            p.dismiss();
        }
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
        layout_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivityCommon.this, MenuActivity.class);
                startActivity(i);
            }
        });

        layout_loyalty = (LinearLayout) toolbar.findViewById(R.id.layout_loyalty);
        layout_loyalty.setVisibility(View.GONE);
        mImageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();
        final String url = "http://" + FB_LY_MobileSettingService.sharedInstance().loginRightTopImageUrl;
        mImageLoader.get(url, ImageLoader.getImageListener(titlebackground, R.color.white, R.color.white));
        titlebackground.setImageUrl(url, mImageLoader);

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
                Intent i = new Intent(this, MenuActivity.class);
                startActivity(i);
                break;
        }

    }


    private void getOffer() {
        p.show();
        if (OfferSummary.offerList != null) {

            if (OfferSummary.offerList.size() > 0) {
                setDataOffer(OfferService.offerSummary);

            } else {
                fetchDataOffer();
            }


        } else {
            fetchDataOffer();
            //   p.hide();
        }
    }

    public void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivityCommon.this).create();
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
                Intent i = new Intent(HomeActivityCommon.this, SignInActivity.class);
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
                HomeActivityCommon.this.finish();
            }
        });
        alertDialog.show();
    }

    private void fetchDataOffer() {//p.show();
        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(OfferSummary offerSummary, Exception error) {
                if (offerSummary != null) {
                    setDataOffer(offerSummary);
                    p.dismiss();
                } else {
                    FBUtils.tryHandleTokenExpiry(HomeActivityCommon.this, error);
                    p.dismiss();
                }
            }
        });
    }

    private void setDataOffer(OfferSummary offerSummary) {//p.show();
        if (offerSummary.getOfferList() != null) {
            offerList = offerSummary.getOfferList();
            FragmentRewardsAndOffers fragment = (FragmentRewardsAndOffers) getSupportFragmentManager().findFragmentById(R.id.image_frag);
            fragment.viewInflater();
            p.dismiss();
        }
        if (offerList != null && offerList.size() > 0) {
            p.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


}

