package com.BasicApp.Activites.NonGeneric.Home;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.BasicApp.Activites.NonGeneric.LoyaltyCard.LoyaltyActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.NewMenuActivity;
import com.BasicApp.Activites.NonGeneric.Offer.RewardActivity;
import com.BasicApp.Activites.NonGeneric.Store.StoreListActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.OfferSummary;
import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummaryCallback;
import com.BasicApp.BusinessLogic.Models.RewardSummaryPointCallback;
import com.BasicApp.BusinessLogic.Services.OfferService;
import com.BasicApp.BusinessLogic.Services.RewardPointService;
import com.BasicApp.BusinessLogic.Services.RewardService;
import com.BasicApp.Fragments.Image_Fragment;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.OfferSummaryCallback;
import com.fishbowl.basicmodule.Models.FBOfferItem;
import com.fishbowl.basicmodule.Models.FBOfferSummaryItem;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by digvijay(dj)
 */
public class DashboardActivity extends FragmentActivity implements View.OnClickListener {
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
    private Toolbar toolbar;
    private ListView rewardsListView1;
    private DrawerLayout drawerLayout;
    private List<FBOfferItem> offerList = new ArrayList<FBOfferItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_bistro);
        progressBarHandler = new ProgressBarHandler(this);
        mlayout = (LinearLayout) findViewById(R.id.bottom_toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawer(DashboardActivity.this);
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


    private void getRewards() {
        if (RewardSummary.rewardList != null) {
            if (RewardSummary.rewardList.size() > 0) {


                updateRewardInfo(RewardService.offerSummary);
            } else {
                fetchReward();
            }
        } else {
            fetchReward();
        }
    }


    private void fetchRewardPoint() {
          //progressBarHandler.show();

        RewardPointService.getUserRewardPoint(this, new RewardSummaryPointCallback() {


            @Override
            public void onRewardSummaryPointCallback(RewardPointSummary rewardSummary, Exception error) {
                 //   progressBarHandler.dismiss();

                if (rewardSummary != null) {
                    updateOfferRewardPoint(rewardSummary);

                } else {
                    //  enableScreen(true);
                    FBUtils.tryHandleTokenExpiry(DashboardActivity.this, error);
                }
            }
        });
    }


    public void onCustomBackPressed() {
        DashboardActivity.this.finish();
    }


    private void fetchReward() {

        RewardService.getUserReward(this, new RewardSummaryCallback() {


            @Override
            public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception error) {


                if (rewardSummary != null) {

                    updateRewardInfo(rewardSummary);
                } else {
                }
            }
        });
    }


    private void updateRewardInfo(RewardSummary rewardSummary) {
        int value = FBUserService.sharedInstance().member.getOffercount();
        int value1 = rewardSummary.getRewardCount();
        int sum = value + value1;
        if (sum > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);

            rewardsValue.setText(String.valueOf(sum));
            FBUserService.sharedInstance().member.setRewardcount(value1);

        } else {
            {
                TextView rewardsValue = (TextView) findViewById(R.id.txt_gift);

                rewardsValue.setText("0");

            }
        }
    }

    private void getOffer() {

        if (OfferSummary.offerList != null) {

            if (OfferSummary.offerList.size() > 0) {
                updateOfferInfo(OfferService.FBOfferSummaryItem);
            } else {
                fetchDataOffer();
            }
        } else {
            fetchDataOffer();
        }


    }


    private void fetchDataOffer() {

        OfferService.getUserOffer(this, new OfferSummaryCallback() {
            @Override
            public void onOfferSummaryCallback(FBOfferSummaryItem FBOfferSummaryItem, Exception error) {


                if (FBOfferSummaryItem != null) {

                    updateOfferInfo(FBOfferSummaryItem);
                } else {
                    FBUtils.tryHandleTokenExpiry(DashboardActivity.this, error);
                }
            }
        });
    }


    private void updateOfferInfo(FBOfferSummaryItem FBOfferSummaryItem) {
        int value = FBOfferSummaryItem.getOfferCount();
        if (value > 0) {
            TextView rewardsValue = (TextView) findViewById(R.id.txt_reward);
            FBUserService.sharedInstance().member.setOffercount(value);

        }
        getRewards();
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
            Intent i = new Intent(this, NewMenuActivity.class);
            FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.MENU_CLICK);
            startActivity(i);
        }
        if (v.getId() == R.id.location_layout) {
            Intent i = new Intent(this, StoreListActivity.class);
            startActivity(i);
        }

        if (v.getId() == R.id.lrewardsoffer_layout) {
            Intent i = new Intent(this, RewardActivity.class);
            startActivity(i);
        }


        if (v.getId() == R.id.loyalty_layout) {
            Intent i = new Intent(this, LoyaltyActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
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


    public void guestUserApi() {

        JSONObject object = new JSONObject();
        try {
            object.put("tenantid", FBConstant.client_tenantid);
            object.put("memberid", "0");
            object.put("deviceid", FBUtility.getAndroidDeviceID(this));
            object.put("deviceOsVersion", FBConstant.device_os_ver);
            object.put("deviceType", FBConstant.DEVICE_TYPE);
            object.put("pushToken", FBPreferences.sharedInstance(this).getPushToken());


        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().guestUserApi(object, new FBUserService.FBGuestUserCallback() {

            @Override
            public void onGuestUserCallback(JSONObject response, Exception error) {
                if (response != null) {

                } else {
                    FBUtils.tryHandleTokenExpiry(DashboardActivity.this, error);

                }
            }
        });
    }

    public void getToken() {
        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(this));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback() {
            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if (response != null) {

                } else {
                    FBUtils.tryHandleTokenExpiry(DashboardActivity.this, error);
                }
            }
        });
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
