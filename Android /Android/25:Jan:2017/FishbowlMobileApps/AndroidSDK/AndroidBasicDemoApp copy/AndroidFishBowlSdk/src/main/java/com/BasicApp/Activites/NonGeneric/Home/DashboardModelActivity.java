package com.BasicApp.Activites.NonGeneric.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.NonGeneric.LoyaltyCard.LoyaltyModelActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.NewMenuModelActivity;
import com.BasicApp.Activites.NonGeneric.Offer.RewardModelActivity;
import com.BasicApp.Activites.NonGeneric.Store.StoreListModelActivity;
import com.BasicApp.BusinessLogic.Interfaces.FBAOfferCallback;
import com.BasicApp.BusinessLogic.Interfaces.FBARewardPointCallback;
import com.BasicApp.BusinessLogic.Services.FBARewardService;
import com.BasicApp.Fragments.Image_Fragment;
import com.BasicApp.Utils.TransitionManager;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Models.FBRewardPointDetailItem;
import com.fishbowl.basicmodule.Services.FBRewardService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by digvijay(dj)
 */
public class DashboardModelActivity extends BaseActivity implements View.OnClickListener, FBARewardPointCallback, FBAOfferCallback {
    public CirclePageIndicator titleIndicator;
    public MyAdapter mAdapter;
    public ViewPager mPager;
    public ImageLoader mImageLoader;
    LinearLayout mlayout;

    RelativeLayout loyalty_Layout, menu_Layout, location_Layout, lrewardsoffer_Layout;
    Timer timer;
    NetworkImageView imbackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_bistro);

        mlayout = (LinearLayout) findViewById(R.id.bottom_toolbar);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        titleIndicator.setViewPager(mPager);
        titleIndicator.setPageColor(Color.GRAY);
        titleIndicator.setFillColor(Color.RED);
        imbackground = (NetworkImageView) findViewById(R.id.background);
        setUpToolBar(true, true);
        setTitle("Dashboard");
        setBackButton(true, false);


        loyalty_Layout = (RelativeLayout) findViewById(R.id.loyalty_layout);
        loyalty_Layout.setOnClickListener(this);
        menu_Layout = (RelativeLayout) findViewById(R.id.menu_layout);
        menu_Layout.setOnClickListener(this);
        location_Layout = (RelativeLayout) findViewById(R.id.location_layout);
        location_Layout.setOnClickListener(this);
        lrewardsoffer_Layout = (RelativeLayout) findViewById(R.id.lrewardsoffer_layout);
        lrewardsoffer_Layout.setOnClickListener(this);
       // enableScreen(false);
       // getOffer();

       // FBARewardService.sharedInstance(this).fetchRewardPoint(DashboardModelActivity.this);
    }


    @Override
    protected void onStart() {
        super.onStart();


        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, 3 * 1000);

        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                    .getImageLoader();
            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.menu_layout) {
            TransitionManager.transitFrom(DashboardModelActivity.this, NewMenuModelActivity.class);
        }
        if (v.getId() == R.id.location_layout) {

            TransitionManager.transitFrom(DashboardModelActivity.this, StoreListModelActivity.class);
        }

        if (v.getId() == R.id.lrewardsoffer_layout) {
            TransitionManager.transitFrom(DashboardModelActivity.this, RewardModelActivity.class);
        }


        if (v.getId() == R.id.loyalty_layout) {
            TransitionManager.transitFrom(DashboardModelActivity.this, LoyaltyModelActivity.class);
        }
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
    }


    public void onCustomBackPressed() {
        DashboardModelActivity.this.finish();
    }


    private void getRewards() {

        fetchModelReward();
    }


    private void getOffer() {

        FBARewardService.sharedInstance(this).getOffer(DashboardModelActivity.this);


    }


    private void fetchModelReward() {

        FBRewardService.getUserFBReward(new FBOfferCallback() {

            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null) {

                    enableScreen(true);
                    updateRewardModelInfo(response);

                } else {

                }
            }
        });
    }


    private void updateRewardModelInfo(FBOfferListItem response) {

        int offerCount = FBPreferences.sharedInstance(this).getOfferCount();
        int rewardCount = FBPreferences.sharedInstance(this).getRewardCount();
        int sum = offerCount + rewardCount;
        if (sum > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);
            rewardsValue.setText(String.valueOf(sum));


        } else {
            {
                TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);

                rewardsValue.setText("0");

            }
        }

    }


    private void updateOfferRewardPoint(FBRewardPointDetailItem response) {
        int EarnedPoints = response.earnedPoints;
        if (EarnedPoints > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

            rewardsValue.setText(String.valueOf(EarnedPoints));

        } else {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

            rewardsValue.setText("0");
        }
    }

    @Override
    public void onFBRewardPointCallback(FBRewardPointDetailItem response, Exception error) {
        if (response != null) {

            updateOfferRewardPoint(response);

        } else {

        }
    }

    @Override
    public void onFBOfferCallback(FBOfferListItem response, Exception error) {
        if (response != null) {
            enableScreen(true);
            updateRewardModelInfo(response);


        } else {
            enableScreen(true);
            updateRewardModelInfo(response);

        }
    }


    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Image_Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Image_Fragment(R.drawable.happyhour);
                case 1:
                    return new Image_Fragment(R.drawable.banner_two);
                case 2:
                    return new Image_Fragment(R.drawable.banner_three);

                default:
                    return null;
            }
        }
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                public void run() {

                    int page = mPager.getCurrentItem();
                    if (page == 0) { // In my case the number of pages are 5
                        titleIndicator.setCurrentItem(1);
                    }
                    if (page == 1) { // In my case the number of pages are 5
                        titleIndicator.setCurrentItem(2);
                    }
                    if (page == 2) { // In my case the number of pages are 5
                        titleIndicator.setCurrentItem(0);
                    }
                }
            });

        }
    }
}
