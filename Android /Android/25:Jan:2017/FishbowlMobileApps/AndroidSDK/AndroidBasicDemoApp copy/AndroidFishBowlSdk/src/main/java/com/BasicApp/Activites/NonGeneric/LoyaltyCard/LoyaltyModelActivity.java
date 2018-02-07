package com.BasicApp.Activites.NonGeneric.LoyaltyCard;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Activites.Generic.WebViewActivity;
import com.BasicApp.Activites.NonGeneric.Home.UserProfileModelActivity;
import com.BasicApp.Activites.NonGeneric.Offer.RewardModelActivity;
import com.BasicApp.Activites.NonGeneric.Scan.ScanModelActivity;
import com.BasicApp.BusinessLogic.Models.GetAllRewardOfferPointBank;
import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.SeekArc;
import com.BasicApp.Utils.TransitionManager;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by digvijay(dj)
 */
public class LoyaltyModelActivity extends BaseActivity {
    public List<GetAllRewardOfferPointBank> listfromServer = new ArrayList<GetAllRewardOfferPointBank>();
    Button pointbtn;
    LinearLayout loyaltyview, layout_programDetail, loyaltyprofile;
    ImageView barcodeimg, backbutton;
    ListView listView;
    int EarnedPoints;
    int PointsToNextReward;
    TextView next_points, available_points, membername, memberdate;
    FBMember member;
    ProgressBarHandler progressBarHandler;
    Button mButton;
    private RelativeLayout toolbar;
    private SeekArc mSeekArc;
    private TextView mSeekArcProgress, textEnd, line;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);
        mButton = (Button) findViewById(R.id.rewardbtn);

        progressBarHandler = new ProgressBarHandler(this);

        Gson gson = new Gson();
        String json = FBPreferences.sharedInstance(LoyaltyModelActivity.this).mSharedPreferences.getString("FBUser", "");
        member = gson.fromJson(json, FBMember.class);
        EarnedPoints = RewardPointSummary.earnedPoints;
        PointsToNextReward = RewardPointSummary.pointsToNextReward;
        inititateui();

        listView = (ListView) findViewById(R.id.listView);
        next_points = (TextView) findViewById(R.id.next_points);
        available_points = (TextView) findViewById(R.id.available_points);
        membername = (TextView) findViewById(R.id.membername);
        memberdate = (TextView) findViewById(R.id.memberdate);

        if (member != null) {
            membername.setText(member.getFirstName());
        }
        next_points.setText("You need " + PointsToNextReward + " more pts for next offer");
        available_points.setText(EarnedPoints + " PTS Available");

        layout_programDetail = (LinearLayout) findViewById(R.id.layout_programDetail);
        final String programdetail = "http://" + FBViewMobileSettingsService.sharedInstance().programDetail;

        layout_programDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WebViewActivity.show(LoyaltyModelActivity.this, "Failed", programdetail);
            }
        });


        pointbtn = (Button) findViewById(R.id.pointbtn);
        pointbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.transitFrom(LoyaltyModelActivity.this, MyActivityModel.class);
            }
        });


        loyaltyview = (LinearLayout) findViewById(R.id.loyaltyview);
        loyaltyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.transitFrom(LoyaltyModelActivity.this, ViewLoyaltyCardModel.class);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.transitFrom(LoyaltyModelActivity.this, RewardModelActivity.class);
            }
        });
        loyaltyprofile = (LinearLayout) findViewById(R.id.loyaltyprofile);
        loyaltyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.transitFrom(LoyaltyModelActivity.this, UserProfileModelActivity.class);
            }
        });


        barcodeimg = (ImageView) findViewById(R.id.barcodeimg);
        barcodeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.transitFrom(LoyaltyModelActivity.this, ScanModelActivity.class);

            }
        });


        setUpToolBar(true, true);
        setTitle(" My Loyalty");
        setBackButton(true, false);
    }


    @Override
    protected void onResume() {
        super.onResume();


        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();

        int EarnedPoints = RewardPointSummary.earnedPoints;
        int PointsToNextReward = RewardPointSummary.pointsToNextReward;
        next_points.setText("You need " + PointsToNextReward + " more pts for next offer");
        available_points.setText(EarnedPoints + " PTS Available");
    }

    public void inititateui() {


        mSeekArc = (SeekArc) findViewById(R.id.seekArc);
        mSeekArcProgress = (TextView) findViewById(R.id.text_seekArcProgress);

        mSeekArc.setClockwise(true);

        // mSeekArc.setProgress(2);
        //  mSeekArcProgress.setText("" + EarnedPoints);
        line = (TextView) findViewById(R.id.line);
        int z = PointsToNextReward - EarnedPoints;

        if (EarnedPoints >= 0) {
            mSeekArc.setMax(PointsToNextReward);
            mSeekArc.setProgress(EarnedPoints);
            mSeekArcProgress.setText("" + EarnedPoints);
            if (PointsToNextReward == 0) {
                line.setText("No more rewards configured");
            } else {
                line.setText(z + " Points until next rewards");
            }

        } else {
            mSeekArc.setMax(PointsToNextReward);
            mSeekArc.setProgress(0);
            mSeekArcProgress.setText("0");
            line.setText("0 Points until next rewards");
        }

        textEnd = (TextView) findViewById(R.id.textEnd);
        if (PointsToNextReward > 0) {
            textEnd.setText("" + PointsToNextReward);
        } else {
            textEnd.setText("" + 0);
        }
    }


    public void getRewardPoint() {

        int offerCount = FBPreferences.sharedInstance(this).getOfferCount();
        int rewardCount = FBPreferences.sharedInstance(this).getRewardCount();
        int sum = offerCount + rewardCount;
            mButton.setText("You have " + sum + " rewards available");

    }

    @Override
    protected void onStart() {
        super.onStart();
        listfromServer.clear();

        getRewardPoint();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    class MySalaryComp implements Comparator<GetAllRewardOfferPointBank> {

        @Override
        public int compare(GetAllRewardOfferPointBank e1, GetAllRewardOfferPointBank e2) {
            if (e1.getLoyaltyPoints() < e2.getLoyaltyPoints()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}