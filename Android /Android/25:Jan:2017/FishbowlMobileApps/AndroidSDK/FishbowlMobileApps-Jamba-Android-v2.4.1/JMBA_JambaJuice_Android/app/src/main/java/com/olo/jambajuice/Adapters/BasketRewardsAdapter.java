package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.Reward;
import com.olo.jambajuice.R;

import java.util.List;

/**
 * Created by Ihsanulhaq on 5/27/2015.
 */
public class BasketRewardsAdapter extends ArrayAdapter<Reward> {
    private final static int SectionHeader = 0;
    private final static int RewardsItem = 1;
    private final static int NoRewardsItem = 2;
    private final static int EnterPromotionCode = 3;
    List<Reward> rewardsList;
    private LayoutInflater mInflater;

    public BasketRewardsAdapter(Activity activity, List<Reward> rewardsList) {
        super(activity, R.layout.row_reward_item, rewardsList);
        this.rewardsList = rewardsList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getCount() {
        int rewardsSize = rewardsList.size() > 0 ? rewardsList.size() : 1;
        return rewardsSize + 3; // 2 headers and 1 for enter promotion code
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (rewardsList.size() == 0 && position == 2) || (rewardsList.size() > 0 && position == rewardsList.size() + 1)) {
            return SectionHeader;
        } else if (rewardsList.size() == 0 && position == 1) {
            return NoRewardsItem;
        } else if (rewardsList.size() > 0 && position > 0 && position <= rewardsList.size()) {
            return RewardsItem;
        } else {
            return EnterPromotionCode;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == RewardsItem) {
                convertView = mInflater.inflate(R.layout.row_basket_reward_item, parent, false);
            } else if (type == SectionHeader) {
                convertView = mInflater.inflate(R.layout.row_reward_section_header, parent, false);
            } else if (type == NoRewardsItem) {
                convertView = mInflater.inflate(R.layout.row_no_reward_available, parent, false);
            } else {
                convertView = mInflater.inflate(R.layout.row_enter_promotion_code, parent, false);
            }
        }
        if (type == RewardsItem) {
            TextView rewardName = (TextView) convertView.findViewById(R.id.rewardName);
            rewardName.setText(getItem(position - 1).getRewardTitle()); // -1 because of first header
        } else if (type == SectionHeader) {
            TextView sectionTitle = (TextView) convertView.findViewById(R.id.sectionTitle);
            if (position == 0) {
                sectionTitle.setText("Available Rewards");
            } else {
                sectionTitle.setText("Promotion Code");
            }
        } else if (type == NoRewardsItem) {
            TextView noReward = (TextView) convertView.findViewById(R.id.noreward);
            noReward.setText("No reward available");
        }
        return convertView;
    }
}
