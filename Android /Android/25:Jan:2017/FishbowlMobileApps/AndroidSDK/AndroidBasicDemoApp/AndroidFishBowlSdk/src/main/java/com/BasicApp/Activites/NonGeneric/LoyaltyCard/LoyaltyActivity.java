package com.BasicApp.Activites.NonGeneric.LoyaltyCard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Activites.NonGeneric.Offer.RewardActivity;
import com.BasicApp.Activites.NonGeneric.Scan.ScanActivity;
import com.BasicApp.Activites.NonGeneric.Settings.ProgramDetailActvity;
import com.BasicApp.Adapters.PointBankAdapter;
import com.BasicApp.BusinessLogic.Models.GetAllRewardOfferPointBank;
import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.SeekArc;
import com.BasicApp.activity.LoyaltyNewView;
import com.BasicApp.activity.MyActivity;
import com.BasicApp.activity.UserProfileActivity;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by digvijay(dj)
 */
public class LoyaltyActivity extends Activity {
    public List<GetAllRewardOfferPointBank> listfromServer = new ArrayList<GetAllRewardOfferPointBank>();
    Button pointbtn;
    LinearLayout loyaltyview, layout_programDetail, loyaltyprofile;
    ImageView barcodeimg, backbutton;
    ListView listView;
    int EarnedPoints;
    int PointsToNextReward;
    int porgressPosition;
    PointBankAdapter adapter;
    TextView next_points, available_points, membername, memberdate;
    Member member;
    private RelativeLayout toolbar;
    private SeekArc mSeekArc;
    private TextView mSeekArcProgress, textEnd, line;
    private DrawerLayout drawerLayout;
    ProgressBarHandler progressBarHandler;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);
         mButton = (Button)findViewById(R.id.rewardbtn);

        progressBarHandler = new ProgressBarHandler(this);
        member = FBUserService.sharedInstance().member;
        EarnedPoints = RewardPointSummary.earnedPoints;
        PointsToNextReward = RewardPointSummary.pointsToNextReward;
        inititateui();

        listView = (ListView) findViewById(R.id.listView);
        next_points = (TextView) findViewById(R.id.next_points);
        available_points = (TextView) findViewById(R.id.available_points);
        membername = (TextView) findViewById(R.id.membername);
        memberdate = (TextView) findViewById(R.id.memberdate);

        if (member != null) {
            membername.setText(member.firstName);
        }
        next_points.setText("You need " + PointsToNextReward + " more pts for next offer");
        available_points.setText(EarnedPoints + " PTS Available");

        layout_programDetail = (LinearLayout) findViewById(R.id.layout_programDetail);
        layout_programDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoyaltyActivity.this, ProgramDetailActvity.class);
                startActivity(i);
            }
        });
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else
                    drawerLayout.openDrawer(GravityCompat.END);
            }

        });

        backbutton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pointbtn = (Button) findViewById(R.id.pointbtn);
        pointbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoyaltyActivity.this, MyActivity.class);
                startActivity(i);
            }
        });


        loyaltyview = (LinearLayout) findViewById(R.id.loyaltyview);
        loyaltyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoyaltyActivity.this, LoyaltyNewView.class);
                startActivity(i);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoyaltyActivity.this, RewardActivity.class);
                startActivity(i);
            }
        });
        loyaltyprofile = (LinearLayout) findViewById(R.id.loyaltyprofile);
        loyaltyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoyaltyActivity.this, UserProfileActivity.class);
                startActivity(i);
            }
        });


        barcodeimg = (ImageView) findViewById(R.id.barcodeimg);
        barcodeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoyaltyActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });

        FBUtils.setUpNavigationDrawer(LoyaltyActivity.this);
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

    public void getAllRewardOfer() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getAllRewardOfferApi(object, new FBUserService.FBAllRewardOfferCallback() {
            @Override
            public void onAllRewardOfferCallback(JSONObject response, Exception error) {
                try {
                    if (response != null && error == null) {
                        if (!response.has("loyaltyPointRedemptionList"))
                            return;

                        JSONArray jArray = response.getJSONArray("loyaltyPointRedemptionList");
                        if (jArray != null) {

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObj = jArray.getJSONObject(i);
                                GetAllRewardOfferPointBank getStoresObj = new GetAllRewardOfferPointBank(jObj);
                                if (jObj.getBoolean("enabled") == true) {
                                    listfromServer.add(getStoresObj);
                                    Collections.sort(listfromServer, Collections.reverseOrder(new MySalaryComp()));
                                }

                            }
                            for (int i = 0; i < listfromServer.size(); i++) {
                                GetAllRewardOfferPointBank loyaltyitem = listfromServer.get(i);
                                int EarnedPoints = RewardPointSummary.earnedPoints;
                                if (EarnedPoints >= loyaltyitem.getLoyaltyPoints()) {
                                    porgressPosition = i;
                                }

                            }
                            adapter = new PointBankAdapter(LoyaltyActivity.this, (ArrayList<GetAllRewardOfferPointBank>) listfromServer, porgressPosition);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressBarHandler.dismiss();
                            listView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (v.getId() == R.id.listView) {
                                        v.getParent().requestDisallowInterceptTouchEvent(true);
                                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                            case MotionEvent.ACTION_UP:
                                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                                break;
                                        }
                                    }
                                    return false;
                                }
                            });
                        }
                    } else {
                        FBUtils.tryHandleTokenExpiry(LoyaltyActivity.this, error);
                        progressBarHandler.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getRewardPoint() {
        int value1 = FBUserService.sharedInstance().member.getRewardcount();
        mButton.setText("You have "+value1 +" rewards available");
    }
    @Override
    protected void onStart() {
        super.onStart();
     //   listfromServer.clear();
     //  getAllRewardOfer();
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