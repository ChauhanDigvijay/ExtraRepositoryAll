package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardSummary;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.RewardsItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.RewardService;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;

import java.util.ArrayList;
import java.util.List;

public class Rewards_Activity extends FragmentActivity implements View.OnClickListener
{
    private List<RewardsItem> rewardList= new ArrayList<RewardsItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        getRewards();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void getRewards()
    {
        if (RewardService.offerSummary != null)
        {
            setReward(RewardService.offerSummary);
        }
        else
        {
            fetchReward();
        }
    }


    private void fetchReward()
    {
        RewardService.getUserReward(this, new RewardSummaryCallback()
        {
            @Override
            public void onRewardSummaryCallback(RewardSummary rewardSummary, Exception error)
            {
                if (rewardSummary != null)
                {
                    setReward(rewardSummary);
                }
                else
                {
                    FBUtils.tryHandleTokenExpiry(Rewards_Activity.this,error);
                }
            }
        });
    }

    private void setReward(RewardSummary rewardSummary)
    {
        if(rewardSummary.getRewardList()!=null)
        {
            rewardList= rewardSummary.getRewardList();
        }

        if(rewardList!=null&&rewardList.size()>0)
        {
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    public void onClick(View v)
    {
    }
}