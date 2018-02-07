package com.BasicApp.BusinessLogic.Models;

/**
 * Created by digvijaychauhan on 25/11/16.
 */

public class LoyaltyActivityListItem {

    public int offerId;
    public int checkNumber;
    public int PointsEarned;
    public String balance;
    String EventTime;
    String ActivityType;
    String Name;
    String description;
    String desc;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    public int getPointsEarned() {
        return PointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        PointsEarned = pointsEarned;
    }
}
