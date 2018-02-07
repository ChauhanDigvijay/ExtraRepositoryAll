package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijaychauhan on 26/10/17.
 */

public class FBRewardPointDetailItem
{
    private String message;
    private boolean successFlag;
    public static int earnedPoints;
    public static int pointsToNextReward;

    public FBRewardPointDetailItem(String message, boolean successFlag,int earnedPoints,int pointsToNextReward) {
        this.message = message;
        this.successFlag = successFlag;
        this.earnedPoints=earnedPoints;
        this.pointsToNextReward=pointsToNextReward;

    }

}
