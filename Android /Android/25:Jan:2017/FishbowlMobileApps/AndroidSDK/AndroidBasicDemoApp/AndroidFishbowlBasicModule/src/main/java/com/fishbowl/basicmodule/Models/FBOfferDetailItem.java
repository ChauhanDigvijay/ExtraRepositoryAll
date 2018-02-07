package com.fishbowl.basicmodule.Models;

import java.io.Serializable;

/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class FBOfferDetailItem implements Serializable {

    private String offerDescription;
    private String offerTitle;
    private boolean ispmOffer;
    private int pmPromotionID;
    private int offerID;
    private int channelTypeID;
    private int offerCount;
    private String validityEndDateTime;
    private String passPreviewImageURL;
    private String promotionCode;
    private String couponURL;
    private String htmlBody;
    private String campaignTitle;
    private String campaignDescription;
    public int campaignId;
    private String Discount;
    private String createdDate;
    private String promoCodeSentDate;

    public String getOfferDescription() {
        return offerDescription;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public boolean ispmOffer() {
        return ispmOffer;
    }

    public int getPmPromotionID() {
        return pmPromotionID;
    }

    public int getOfferID() {
        return offerID;
    }

    public int getChannelTypeID() {
        return channelTypeID;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public String getValidityEndDateTime() {
        return validityEndDateTime;
    }

    public String getPassPreviewImageURL() {
        return passPreviewImageURL;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public String getCouponURL() {
        return couponURL;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPromoCodeSentDate() {
        return promoCodeSentDate;
    }

}