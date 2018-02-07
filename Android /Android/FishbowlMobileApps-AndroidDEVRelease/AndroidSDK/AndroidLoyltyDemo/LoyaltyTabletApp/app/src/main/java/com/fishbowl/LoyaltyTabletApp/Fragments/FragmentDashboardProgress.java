package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.Generic.WebViewActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.LoyaltyCard.LoyaltyCardActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer.MultiReward_Activity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer.MultipleOffer_Activity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryPointCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardPointSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.RewardPointService;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.SeekArc;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;

import static com.fishbowl.LoyaltyTabletApp.R.id.button1;
import static com.fishbowl.LoyaltyTabletApp.R.id.footer_image_url;


public class FragmentDashboardProgress extends Fragment implements View.OnClickListener {
    NetworkImageView background, footer_imageurl;
    Button btn1, button2;
    LinearLayout loyalty_layout, program_detail, loyatycardlayout;
    private ImageLoader mImageLoader;
    private SeekArc mSeekArc;
    private TextView mSeekArcProgress, textEnd, txt_program_detail, points_until_rewards;
    final String programlink = "http://" + FB_LY_MobileSettingService.sharedInstance().programDetail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_progress_new, container, false);
        loyalty_layout = (LinearLayout) v.findViewById(R.id.loyalty_layout);
        loyalty_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(getContext(), LoyaltyCardActivity.class);

                startActivity(i2);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });

        program_detail = (LinearLayout) v.findViewById(R.id.program_detail);
        program_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i2 = new Intent(getContext(), ProgramDeetailActivity.class);
//                startActivity(i2);
                WebViewActivity.show(getActivity(), "DJ", programlink);

            }
        });
        btn1 = (Button) v.findViewById(button1);
        btn1.setOnClickListener(this);
        button2 = (Button) v.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        footer_imageurl = (NetworkImageView) v.findViewById(footer_image_url);
        background = (NetworkImageView) v.findViewById(R.id.right_side_image_url);
        mSeekArc = (SeekArc) v.findViewById(R.id.seekArc);

        loyatycardlayout = (LinearLayout) v.findViewById(R.id.loyatycardlayout);
        mSeekArcProgress = (TextView) v.findViewById(R.id.seekArcProgress);
        mSeekArc.setClockwise(true);

        txt_program_detail = (TextView) v.findViewById(R.id.txt_program_detail);
        points_until_rewards = (TextView) v.findViewById(R.id.points_until_rewards);
        int EarnedPoints = RewardPointSummary.earnedPoints;
        int PointsToNextReward = RewardPointSummary.pointsToNextReward;
        textEnd = (TextView) v.findViewById(R.id.textEnd);
        int z = PointsToNextReward + EarnedPoints;
        mSeekArc.setMax(z);

        if (EarnedPoints >= 0) {
            mSeekArc.setProgress(EarnedPoints);
            mSeekArcProgress.setText("" + EarnedPoints);
            if (PointsToNextReward != 0) {
                textEnd.setText("" + z);
            } else {

                textEnd.setText("" + 0);
            }

        } else {

            mSeekArc.setProgress(0);
            mSeekArcProgress.setText("0");
            points_until_rewards.setText("0 Points until next rewards");
        }


        if (PointsToNextReward == 0) {
            points_until_rewards.setText("No more rewards configured");

        } else {
            points_until_rewards.setText(PointsToNextReward + " Points until next rewards");

        }
        fetchRewardPoint();
        return v;
    }

    private void fetchRewardPoint() {

        RewardPointService.getUserRewardPoint((Activity) getContext(), new RewardSummaryPointCallback() {
            @Override
            public void onRewardSummaryPointCallback(RewardPointSummary rewardSummary, Exception error) {
                if (rewardSummary != null) {
                    int EarnedPoints = RewardPointSummary.earnedPoints;
                    int PointsToNextReward = RewardPointSummary.pointsToNextReward;

                    int z = PointsToNextReward + EarnedPoints;
                    mSeekArc.setMax(z);

                    if (EarnedPoints >= 0) {
                        mSeekArc.setProgress(EarnedPoints);
                        mSeekArcProgress.setText("" + EarnedPoints);
                        if (PointsToNextReward != 0) {

                            textEnd.setText("" + z);
                        } else {

                            textEnd.setText("" + 0);
                        }

                    } else {

                        mSeekArc.setProgress(0);
                        mSeekArcProgress.setText("0");
                        points_until_rewards.setText("0 Points until next rewards");
                    }


                    if (PointsToNextReward == 0) {
                        points_until_rewards.setText("No more rewards configured");

                    } else {
                        points_until_rewards.setText(PointsToNextReward + " Points until next rewards");

                    }
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    //   progressBarHandler.hide();
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        final String url1 = "http://" + FB_LY_MobileSettingService.sharedInstance().loginFooterImageUrl;
        mImageLoader.get(url1, ImageLoader.getImageListener(footer_imageurl, R.drawable.bottom_bar, R.drawable.bottom_bar));


        footer_imageurl.setImageUrl(url1, mImageLoader);
        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;

        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            btn1.setBackgroundColor(Color.parseColor(btncolor));
            button2.setBackgroundColor(Color.parseColor(btncolor));

            mSeekArc.setProgressColor(Color.parseColor(btncolor));
            loyatycardlayout.setBackgroundColor(Color.parseColor(btncolor));

        }

        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            btn1.setBackgroundColor(Color.parseColor(btncolor));
            btn1.setBackgroundColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
            txt_program_detail.setTextColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
            btn1.setBackgroundResource(R.drawable.normal);
            GradientDrawable gd = (GradientDrawable) btn1.getBackground().getCurrent();
            button2.setBackgroundColor(Color.parseColor(btncolor));
            button2.setBackgroundColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
            button2.setBackgroundResource(R.drawable.normal);
            GradientDrawable gd1 = (GradientDrawable) button2.getBackground().getCurrent();
            gd.setColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
            gd.setCornerRadii(new float[]{10, 10, 10, 10, 10, 10, 10, 10});
            gd.setStroke(1, Color.parseColor("#444444"), 0, 0);
            gd1.setColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
            gd1.setCornerRadii(new float[]{10, 10, 10, 10, 10, 10, 10, 10});
            gd1.setStroke(1, Color.parseColor("#444444"), 0, 0);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                if (OfferSummary.offerList != null) {
                    if (OfferSummary.offerList.size() > 0) {
                        Intent i2 = new Intent(getContext(), MultipleOffer_Activity.class);
                        this.startActivity(i2);
                        break;
                    } else {
                        showalertoffer();
                    }
                }
            case R.id.button1:
                if (RewardSummary.rewardList != null) {
                    if (RewardSummary.rewardList.size() > 0) {
                        Intent i3 = new Intent(getContext(), MultiReward_Activity.class);
                        this.startActivity(i3);
                        break;
                    } else {
                        showalertreward();
                    }
                }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
        }
    }


    public void showalertreward() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("No reward avaiable for this customer ");
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void showalertoffer() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("No offer avaiable for this customer");
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
