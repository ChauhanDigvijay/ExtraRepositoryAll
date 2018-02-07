package com.BasicApp.BusinessLogic.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by digvijaychauhan on 25/11/16.
 */

public class LoyaltyActivityList {
    public static ArrayList<LoyaltyActivityListItem> activitylist;
    public int offerId;
    public Integer checkNumber;
    public String balance;
    public Integer PointsEarned;
    String EventTime;
    String ActivityType;
    String desc;
    String name;
    String description;
    private int ActivityListCount;


    public LoyaltyActivityList(JSONObject response) {
        try {
            activitylist = new ArrayList<LoyaltyActivityListItem>();
            if (response.has("loyaltyActivityList")) {
                JSONArray jsonArray = response.getJSONArray("loyaltyActivityList");


                if (jsonArray != null && jsonArray.length() > 0) {
                    ActivityListCount = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject wallObj = jsonArray.getJSONObject(i);

                        if (wallObj != null) {
                            LoyaltyActivityListItem rewarditem = new LoyaltyActivityListItem();

                            if (wallObj.has("activityType") && !wallObj.isNull("activityType")) {
                                ActivityType = (String) wallObj.get("activityType");
                                rewarditem.setActivityType(ActivityType);
                            }

                            if (wallObj.has("desc") && !wallObj.isNull("desc")) {
                                desc = (String) wallObj.get("desc");
                                rewarditem.setDesc(desc);
                            }

                            if (wallObj.has("eventDate") && !wallObj.isNull("eventDate")) {
                                EventTime = (String) wallObj.get("eventDate");
                                rewarditem.setEventTime(EventTime);
                            }


                            if (wallObj.has("checkNumber") && !wallObj.isNull("checkNumber")) {
                                checkNumber = (Integer) wallObj.get("checkNumber");
                                rewarditem.setCheckNumber(checkNumber);
                            }

                            if (wallObj.has("balance") && !wallObj.isNull("balance")) {
                                balance = (String) wallObj.get("balance");
                                rewarditem.setBalance(balance);
                            }

                            if (wallObj.has("pointsEarned") && !wallObj.isNull("pointsEarned")) {
                                PointsEarned = (Integer) wallObj.get("pointsEarned");
                                rewarditem.setPointsEarned(PointsEarned);
                            }

                            if (wallObj.has("name") && !wallObj.isNull("name")) {
                                name = (String) wallObj.get("name");
                                rewarditem.setName(name);
                            }
                            if (wallObj.has("description") && !wallObj.isNull("description")) {
                                description = (String) wallObj.get("description");
                                rewarditem.setDescription(description);
                            }
                            if (wallObj.has("offerId") && !wallObj.isNull("offerId")) {
                                offerId = (Integer) wallObj.get("offerId");
                                rewarditem.setOfferId(offerId);
                            }

                            activitylist.add(rewarditem);
                        }
                    }

                    setActivityListCount(RewardSummary.rewardList.size());
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<LoyaltyActivityListItem> getLoyaltyList() {
        return activitylist;

    }

    public int getActivityListCount() {
        return ActivityListCount;
    }

    public void setActivityListCount(int activityListCount) {
        ActivityListCount = activityListCount;
    }
}
