package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfferSummary {
    public static ArrayList<OfferItem> offerList;
    String offerDescription;
    String offerTitle;
    Boolean ispmOffer;
    Integer pmPromotionID;
    Integer offerID;
    Integer channelTypeID;
    String htmlBody;
    String validityEndDateTime;
    String passPreviewImageURL;
    String promotionCode;
    String passCustomStripUrl;
    String customLogoURL;
    String notificationContent;
    ProgressBarHandler p;
    private int offerCount;

    public OfferSummary(JSONObject response) {
        try {
            if (response.has("inAppOfferList")) {
                JSONArray jsonArray = response.getJSONArray("inAppOfferList");
                offerList = new ArrayList<OfferItem>();

                if (jsonArray != null && jsonArray.length() > 0) {
                    offerCount = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject wallObj = jsonArray.getJSONObject(i);

                        if (wallObj != null) {
                            OfferItem offeritem = new OfferItem();

                            if (wallObj.has("campaignTitle")) {
                                offerTitle = (String) wallObj.get("campaignTitle");
                                offeritem.setOfferIItem(offerTitle);
                            }

                            if (wallObj.has("campaignDescription")) {
                                offerDescription = (String) wallObj.get("campaignDescription");
                                offeritem.setOfferIName(offerDescription);
                            }

                            if (wallObj.has("validityEndDateTime")) {
                                validityEndDateTime = (String) wallObj.get("validityEndDateTime");
                                offeritem.setDatetime(validityEndDateTime);
                            }

                            if (wallObj.has("isPMOffer")) {
                                ispmOffer = wallObj.getBoolean("isPMOffer");
                                offeritem.setPMOffer(ispmOffer);
                            }

                            if (wallObj.has("passPreviewImageURL")) {
                                passPreviewImageURL = (String) wallObj.get("passPreviewImageURL");
                                offeritem.setOfferIOther(passPreviewImageURL);
                            }

                            if (wallObj.has("passCustomStripUrl")) {
                                passCustomStripUrl = (String) wallObj.get("passCustomStripUrl");
                                offeritem.setpassCustomStripUrl(passCustomStripUrl);
                            }

                            if (wallObj.has("customLogoURL")) {
                                customLogoURL = (String) wallObj.get("customLogoURL");
                                offeritem.setcustomLogoURL(customLogoURL);
                            }

                            if (wallObj.has("campaignId")) {
                                offerID = (Integer) wallObj.get("campaignId");
                                offeritem.setOfferId(offerID.toString());
                            }

                            if (wallObj.has("channelTypeID")) {
                                channelTypeID = (Integer) wallObj.get("channelTypeID");
                                offeritem.setChannelID(channelTypeID);
                            }

                            if (wallObj.has("promotionID")) {
                                pmPromotionID = (Integer) wallObj.get("promotionID");
                                offeritem.setPmPromotionID(pmPromotionID);
                            }

                            if (wallObj.has("htmlBody")) {
                                htmlBody = (String) wallObj.get("htmlBody");
                                offeritem.setHtmlBody(htmlBody);
                            }

                            if (wallObj.has("promotionCode")) {
                                promotionCode = (String) wallObj.get("promotionCode");
                                offeritem.setPromotioncode(promotionCode);
                            }
                            if (wallObj.has("couponURL")) {
                                notificationContent = (String) wallObj.get("couponURL");
                                offeritem.setNotificationContent(notificationContent);
                            }


                            offerList.add(offeritem);
                        }
                    }
                    setOfferCount(offerList.size());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<OfferItem> getOfferList() {
        return offerList;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }
}
