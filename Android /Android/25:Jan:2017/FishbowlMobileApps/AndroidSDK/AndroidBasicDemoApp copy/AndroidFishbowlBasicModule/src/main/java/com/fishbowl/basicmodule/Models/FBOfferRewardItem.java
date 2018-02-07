package com.fishbowl.basicmodule.Models;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class FBOfferRewardItem {
    private String message;
    private boolean successFlag;
    private int numberOffers;
    private int numberReward;
    private FBOfferRewardDetailItem[] categories;

    public FBOfferRewardItem(String message, boolean successFlag, FBOfferRewardDetailItem[] categories,int numberOffers,int numberReward) {
        this.message = message;
        this.successFlag = successFlag;
        this.categories = categories;
        this.numberOffers=numberOffers;
        this.numberReward=numberReward;

    }

    public FBOfferRewardDetailItem[] getCategories() {
        return categories;
    }


}