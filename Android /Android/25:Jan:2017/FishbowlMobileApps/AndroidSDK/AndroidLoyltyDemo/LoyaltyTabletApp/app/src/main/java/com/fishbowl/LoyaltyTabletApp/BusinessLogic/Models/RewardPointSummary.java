package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import org.json.JSONObject;

public class RewardPointSummary {

    public static int earnedPoints;
    public static int pointsToNextReward;

    public RewardPointSummary(JSONObject response) {
        try {
            if (response.has("earnedPoints")) {
                earnedPoints = (Integer) response.get("earnedPoints");
                setEarnedPoints(earnedPoints);
            }

            if (response.has("pointsToNextReward")) {
                pointsToNextReward = (Integer) response.get("pointsToNextReward");
                setPointsToNextReward(pointsToNextReward);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public double getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public int getPointsToNextReward() {
        return pointsToNextReward;
    }

    public void setPointsToNextReward(int pointsToNextReward) {
        this.pointsToNextReward = pointsToNextReward;
    }

}