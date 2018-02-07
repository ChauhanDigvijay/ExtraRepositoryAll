package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Home;

import android.os.Build;
import android.os.Bundle;
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
import com.fishbowl.LoyaltyTabletApp.Activites.Generic.BaseActivity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.OfferSummaryCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardsItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.OfferService;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.RewardService;
import com.fishbowl.LoyaltyTabletApp.Fragments.FragmentRewardsAndOffers;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements OnClickListener {

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
        setContentView(R.layout.activity_dashboard_tablet);
        p = new ProgressBarHandler(this);
        setUpToolBar(true, false);
        setToolbarConfig(true, true);
        getOffer();
        getRewards();
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
                    FBUtils.tryHandleTokenExpiry(HomeActivity.this, error);
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


    private void fetchDataOffer() {//p.show();
        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(OfferSummary offerSummary, Exception error) {
                if (offerSummary != null) {
                    setDataOffer(offerSummary);
                    p.dismiss();
                } else {
                    FBUtils.tryHandleTokenExpiry(HomeActivity.this, error);
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
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


}

