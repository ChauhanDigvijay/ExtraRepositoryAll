package com.BasicApp.BusinessLogic.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by digvijaychauhan on 21/09/16.
 */

public class RewardSummary {

    private int RewardCount;

    String passPreviewImageURL ;
    Boolean ispmOffer;
    String message;
    public int compaignId;
    String campaignTitle;
    String compaignDescription;
    String  validityEndDateTime;
    public int promotionID;
    public int channelID = 0;
   String  htmlBody;
    String promotionCode;
    String couponURL;
    public static ArrayList<RewardsItem> rewardList;

    public static ArrayList<RewardsItem> getRewardList() {
        return rewardList;
    }

    public static void setRewardList(ArrayList<RewardsItem> rewardList) {
        RewardSummary.rewardList = rewardList;
    }

    public RewardSummary(JSONObject response)
    {
        try
        {

            if(response.has("inAppOfferList"))
            {

                JSONArray jsonArray	 = response.getJSONArray("inAppOfferList");
                rewardList = new ArrayList<RewardsItem>();
                if(jsonArray != null && jsonArray.length() > 0)
                {

                    RewardCount= jsonArray.length();
                    for( int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject wallObj = jsonArray.getJSONObject(i);
                        if(wallObj != null) {
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
                            if (wallObj.has("campaignId")) {
                                compaignId = (Integer) wallObj.get("campaignId");
                                rewarditem.setCompaignId(compaignId);
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
                                htmlBody =  (String)wallObj.get("htmlBody");
                                rewarditem.setHtmlBody(htmlBody);

                            }
                            if (wallObj.has("promotionCode")) {
                                promotionCode =  (String)wallObj.get("promotionCode");
                                rewarditem.setPromotioncode(promotionCode);

                            }
                            if (wallObj.has("couponURL")) {
                                couponURL =  (String)wallObj.get("couponURL");
                                rewarditem.setCouponURL(couponURL);}
                           /* else if (wallObj.has("notificationContent")) {
                                couponURL =  (String)wallObj.get("notificationContent");
                                rewarditem.setCouponURL(couponURL);}*/


                            rewardList.add(rewarditem);

                        }


                    }
                    setRewardCount(rewardList.size());
                }

            }

        }

        catch(Exception ex){
            ex.printStackTrace();

        }
    }

    public int getRewardCount() {
        return RewardCount;
    }

    public void setRewardCount(int rewardCount) {
        RewardCount = rewardCount;
    }
}
