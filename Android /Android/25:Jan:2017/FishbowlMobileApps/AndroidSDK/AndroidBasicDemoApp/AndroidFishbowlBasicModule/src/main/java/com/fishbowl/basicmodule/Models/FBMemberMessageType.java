package com.fishbowl.basicmodule.Models;

public class FBMemberMessageType {

    private boolean pointsAwardMessages;
    private boolean rewardNotice;
    private boolean otherLoyaltyMessages;

    public FBMemberMessageType() {
        super();
    }

    public FBMemberMessageType(boolean pointsAwardMessages, boolean rewardNotice, boolean otherLoyaltyMessages) {
        super();
        this.pointsAwardMessages = pointsAwardMessages;
        this.rewardNotice = rewardNotice;
        this.otherLoyaltyMessages = otherLoyaltyMessages;
    }

    public boolean isPointsAwardMessages() {
        return pointsAwardMessages;
    }

    public void setPointsAwardMessages(boolean pointsAwardMessages) {
        this.pointsAwardMessages = pointsAwardMessages;
    }

    public boolean isRewardNotice() {
        return rewardNotice;
    }

    public void setRewardNotice(boolean rewardNotice) {
        this.rewardNotice = rewardNotice;
    }

    public boolean isOtherLoyaltyMessages() {
        return otherLoyaltyMessages;
    }

    public void setOtherLoyaltyMessages(boolean otherLoyaltyMessages) {
        this.otherLoyaltyMessages = otherLoyaltyMessages;
    }

    @Override
    public String toString() {
        return "MemberMessageType [pointsAwardMessages=" + pointsAwardMessages + ", rewardNotice=" + rewardNotice
                + ", otherLoyaltyMessages=" + otherLoyaltyMessages + "]";
    }

}
