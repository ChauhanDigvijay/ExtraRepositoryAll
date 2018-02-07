package com.BasicApp.ActivityModel.Home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.ActivityModel.LoyaltyCard.LoyaltyModelActivity;
import com.BasicApp.ActivityModel.Menus.MenusLanding.NewMenuModelActivity;
import com.BasicApp.ActivityModel.Offer.RewardModelActivity;
import com.BasicApp.ActivityModel.Store.StoreListModelActivity;
import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummaryPointCallback;
import com.BasicApp.BusinessLogic.Services.RewardPointService;
import com.BasicApp.Fragments.Image_Fragment;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Services.FBRewardService;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by digvijay(dj)
 */
public class DashboardModelActivity extends FragmentActivity implements View.OnClickListener {
    public CirclePageIndicator titleIndicator;
    public MyAdapter mAdapter;
    public ViewPager mPager;
    public ImageLoader mImageLoader;
    LinearLayout mlayout;
    RelativeLayout mtoolbar;
    RelativeLayout loyalty_Layout, menu_Layout, location_Layout, lrewardsoffer_Layout;
    Timer timer;
    NetworkImageView imbackground;
    ProgressBarHandler progressBarHandler;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_bistro);
        progressBarHandler = new ProgressBarHandler(this);
        mlayout = (LinearLayout) findViewById(R.id.bottom_toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawerModel(DashboardModelActivity.this);
        loyalty_Layout = (RelativeLayout) findViewById(R.id.loyalty_layout);
        loyalty_Layout.setOnClickListener(this);
        menu_Layout = (RelativeLayout) findViewById(R.id.menu_layout);
        menu_Layout.setOnClickListener(this);
        location_Layout = (RelativeLayout) findViewById(R.id.location_layout);
        location_Layout.setOnClickListener(this);
        lrewardsoffer_Layout = (RelativeLayout) findViewById(R.id.lrewardsoffer_layout);
        lrewardsoffer_Layout.setOnClickListener(this);


        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        titleIndicator.setViewPager(mPager);
        titleIndicator.setPageColor(Color.GRAY);
        titleIndicator.setFillColor(Color.RED);
        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        mtoolbar.findViewById(R.id.backbutton).setVisibility(View.GONE);
        mtoolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);
        imbackground = (NetworkImageView) findViewById(R.id.background);

    }


    @Override
    protected void onStart() {
        super.onStart();
        getOffer();

        fetchRewardPoint();
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
        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();
        int value = FBUserService.sharedInstance().member.getOffercount();
        int value1 = FBUserService.sharedInstance().member.getRewardcount();
        int sum = value + value1;
        if (sum > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);
            rewardsValue.setText(String.valueOf(sum));
        } else {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);
            rewardsValue.setText("0");
        }

        int EarnedPoints = RewardPointSummary.earnedPoints;
        if (EarnedPoints > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);
            rewardsValue.setText(String.valueOf(EarnedPoints));

        } else {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

            rewardsValue.setText("0");
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_navigator) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else
                drawerLayout.openDrawer(GravityCompat.END);
        }

        if (v.getId() == R.id.menu_layout) {
            Intent i = new Intent(this, NewMenuModelActivity.class);
            //  FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.MENU_CLICK);
            startActivity(i);
        }
        if (v.getId() == R.id.location_layout) {
            Intent i = new Intent(this, StoreListModelActivity.class);
            startActivity(i);
        }

        if (v.getId() == R.id.lrewardsoffer_layout) {
            Intent i = new Intent(this, RewardModelActivity.class);
            startActivity(i);
        }


        if (v.getId() == R.id.loyalty_layout) {
            Intent i = new Intent(this, LoyaltyModelActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
    }


    public void onCustomBackPressed()
    {
        DashboardModelActivity.this.finish();
    }


    private void getRewards() {

        fetchModelReward();
    }


    private void getOffer() {


        fetchModelOffer();

    }

    private void fetchRewardPoint() {
        RewardPointService.getUserRewardPoint(this, new RewardSummaryPointCallback() {


            @Override
            public void onRewardSummaryPointCallback(RewardPointSummary rewardSummary, Exception error) {


                if (rewardSummary != null) {
                    updateOfferRewardPoint(rewardSummary);

                } else {

                    FBUtils.tryHandleTokenExpiry(DashboardModelActivity.this, error);
                }
            }
        });
    }





    private void fetchModelReward() {

        FBRewardService.getUserFBReward(new FBOfferCallback() {

            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null) {

                    updateRewardModelInfo(response);

                } else {

                }
            }
        });
    }


    public void fetchModelOffer() {

        FBRewardService.getUserFBOffer(new FBOfferCallback() {
            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null)

                {
                    updateOfferModelInfo(response);

                } else {

                }
            }
        });
    }


    private void updateRewardModelInfo(FBOfferListItem response) {
        int value = FBUserService.sharedInstance().member.getOffercount();
        int value1 = response.getCategories().length;
        int sum = value + value1;
        if (sum > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);

            rewardsValue.setText(String.valueOf(sum));
            FBSessionService.member.setRewardcount(value1);

        } else {
            {
                TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);

                rewardsValue.setText("0");

            }
        }
    }



    private void updateOfferModelInfo(FBOfferListItem response) {
        int value = response.getCategories().length;
        if (value > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);
            FBSessionService.member.setOffercount(value);

        }
        getRewards();

    }



    private void updateOfferRewardPoint(RewardPointSummary rewardSummary) {
        int EarnedPoints = RewardPointSummary.earnedPoints;
        if (EarnedPoints > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

            rewardsValue.setText(String.valueOf(EarnedPoints));

        } else {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);

            rewardsValue.setText("0");
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
