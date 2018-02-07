package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards;

import android.view.View;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.R;

/**
 * Created by Nauman Afzaal on 27/05/15.
 */
public class MyRewardsViewHolder {
    private TextView rewardName;
    private TextView description;

    public MyRewardsViewHolder(View view) {
        rewardName = (TextView) view.findViewById(R.id.tv_name);
        description = (TextView) view.findViewById(R.id.tv_expiry);
    }

    public void invalidate(Reward r) {
        rewardName.setText(r.getRewardTitle());
        description.setText(r.getDescription());
    }

}
