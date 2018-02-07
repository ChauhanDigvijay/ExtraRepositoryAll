package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.RedeemReward;

import android.os.Bundle;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.R;

public class RedeemRewardActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_reward);
        setUpToolBar(true);
        setBackButton(true,false);
        setTitle("Redeem Instructions");
    }
}
