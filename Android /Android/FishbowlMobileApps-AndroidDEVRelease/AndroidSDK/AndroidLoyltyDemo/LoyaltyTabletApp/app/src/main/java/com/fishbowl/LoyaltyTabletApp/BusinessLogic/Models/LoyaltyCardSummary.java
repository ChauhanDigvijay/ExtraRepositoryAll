package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

/**
 * Created by digvijaychauhan on 13/02/17.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoyaltyCardSummary {
    public static ArrayList<RewardsItem> rewardList;
    public int channelTypeID;
    public int promotionID;
    public int channelID = 0;
    String passPreviewImageURL;
    Boolean ispmOffer;
    String message;
    String campaignTitle;
    String compaignDescription;
    String validityEndDateTime;
    String htmlBody;
    String promotionCode;
    String passCustomStripUrl;
    String customLogoURL;
    String notificationContent;
    private int RewardCount, count;


    public LoyaltyCardSummary(JSONObject response) {
        try {
            if (response.has("inAppOfferList")) {
                JSONArray jsonArray = response.getJSONArray("inAppOfferList");
                rewardList = new ArrayList<RewardsItem>();

                if (jsonArray != null && jsonArray.length() > 0) {
                    RewardCount = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject wallObj = jsonArray.getJSONObject(i);

                        if (wallObj != null) {
                            RewardsItem rewarditem = new RewardsItem();

                            if (wallObj.has("message")) {
                                message = (String) wallObj.get("message");
                                rewarditem.setMessage(message);
                            }

                            if (wallObj.has("campaignTitle")) {
                                campaignTitle = (String) wallObj.get("campaignTitle");
                                rewarditem.setCompaignTitle(campaignTitle);
                            }

                            if (wallObj.has("campaignDescription")) {
                                compaignDescription = (String) wallObj.get("campaignDescription");
                                rewarditem.setCompaignDescription(compaignDescription);
                            }

                            if (wallObj.has("validityEndDateTime")) {
                                validityEndDateTime = (String) wallObj.get("validityEndDateTime");
                                rewarditem.setValidityEndDateTime(validityEndDateTime);
                            }

                            if (wallObj.has("isPMOffer")) {
                                ispmOffer = wallObj.getBoolean("isPMOffer");
                                rewarditem.setIspmOffer(ispmOffer);
                            }

                            if (wallObj.has("passPreviewImageURL")) {
                                passPreviewImageURL = (String) wallObj.get("passPreviewImageURL");
                                rewarditem.setPassPreviewImageURL(passPreviewImageURL);
                            }

                            if (wallObj.has("passCustomStripUrl")) {
                                passCustomStripUrl = (String) wallObj.get("passCustomStripUrl");
                                rewarditem.setPassCustomStripUrl(passCustomStripUrl);
                            }

                            if (wallObj.has("customLogoURL")) {
                                customLogoURL = (String) wallObj.get("customLogoURL");
                                rewarditem.setCustomLogoURL(customLogoURL);
                            }

                            if (wallObj.has("campaignId")) {
                                channelTypeID = (Integer) wallObj.get("campaignId");
                                rewarditem.setCompaignId(channelTypeID);
                            }

                            if (wallObj.has("channelTypeID")) {
                                channelID = (Integer) wallObj.get("channelTypeID");
                                rewarditem.setChannelID(channelID);
                            }

                            if (wallObj.has("promotionID")) {
                                promotionID = (Integer) wallObj.get("promotionID");
                                rewarditem.setPromotionID(promotionID);

                            }

                            if (wallObj.has("htmlBody")) {
                                htmlBody = (String) wallObj.get("htmlBody");
                                rewarditem.setHtmlBody(htmlBody);
                            }

                            if (wallObj.has("promotionCode")) {
                                promotionCode = (String) wallObj.get("promotionCode");
                                rewarditem.setPromotioncode(promotionCode);
                            }
                            if (wallObj.has("couponURL")) {
                                notificationContent = (String) wallObj.get("couponURL");
                                rewarditem.setNotificationContent(notificationContent);
                            }

                            rewardList.add(rewarditem);
                        }
                    }

                    setRewardCount(rewardList.size());
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<RewardsItem> getRewardList() {
        return rewardList;
    }

    public static void setRewardList(ArrayList<RewardsItem> rewardList) {
        RewardSummary.rewardList = rewardList;
    }

    public int getRewardCount() {
        return RewardCount;
    }

    public void setRewardCount(int rewardCount) {
        RewardCount = rewardCount;
    }


}
