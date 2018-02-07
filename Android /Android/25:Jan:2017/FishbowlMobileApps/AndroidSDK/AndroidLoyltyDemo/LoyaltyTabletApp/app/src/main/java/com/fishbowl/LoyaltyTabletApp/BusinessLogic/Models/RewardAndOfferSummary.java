package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import java.util.ArrayList;

/**
 * Created by schaudhary_ic on 17-Nov-16.
 */

public class RewardAndOfferSummary {

    public ArrayList<RewardAndOfferItem> list = new ArrayList<RewardAndOfferItem>();
    private int RewardOfferCount;

    public ArrayList<RewardAndOfferItem> getRewardAndOfferSummaryList() {
        return list;
    }

    public void setRewardAndOfferSummaryList(ArrayList<RewardAndOfferItem> list) {
        this.list = list;
    }

    public ArrayList<RewardAndOfferItem> populate() {
        if (OfferSummary.offerList != null || RewardSummary.rewardList != null) {


            if (RewardSummary.rewardList != null) {
                for (int i = 0; i < RewardSummary.rewardList.size(); i++) {
                    RewardAndOfferItem s = RewardSummary.rewardList.get(i);
                    list.add(s);
                }
            }
            if (OfferSummary.offerList != null) {
                for (int i = 0; i < OfferSummary.offerList.size(); i++) {
                    RewardAndOfferItem s = OfferSummary.offerList.get(i);
                    list.add(s);

                }
            }
            for (int j = 0; j < list.size(); j++) {

                if (list.get(j) instanceof RewardsItem) {
                    RewardsItem s = (RewardsItem) list.get(j);
                }
                if (list.get(j) instanceof OfferItem)

                {
                    OfferItem s = (OfferItem) list.get(j);


                }
            }

            setRewardAndOfferCount(list.size());
        }

        return list;
    }

    public int getRewardAndOfferSummaryCount() {
        return RewardOfferCount;
    }

    public void setRewardAndOfferCount(int rewardofferCount) {
        RewardOfferCount = rewardofferCount;
    }
}
