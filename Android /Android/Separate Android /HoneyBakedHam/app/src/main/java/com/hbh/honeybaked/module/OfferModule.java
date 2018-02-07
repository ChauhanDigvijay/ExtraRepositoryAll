package com.hbh.honeybaked.module;

import java.io.Serializable;

public class OfferModule implements Serializable {
    String campaignDescription = "";
    int campaignId = 0;
    String campaignTitle = "";
    String campaignType = "";
    int channelId = 0;
    int channelTypeID = 0;
    String couponURL = "";
    int isPMOffer = 0;
    int mailingId = 0;
    long offerValidDate;
    int point = 0;
    String promotionCode = "";
    int promotionID = 0;
    int templateId = 0;
    String validityEndDateTime = "";

    public OfferModule(int campaignId, String campaignTitle, String campaignDescription, String validityEndDateTime, int promotionID, int channelTypeID, int isPMOffer, int mailingId, int templateId, int channelId, String campaignType, String couponURL, String promotionCode, long offerValidDate) {
        this.campaignId = campaignId;
        this.campaignTitle = campaignTitle;
        this.campaignDescription = campaignDescription;
        this.validityEndDateTime = validityEndDateTime;
        this.channelTypeID = channelTypeID;
        this.isPMOffer = isPMOffer;
        this.mailingId = mailingId;
        this.templateId = templateId;
        this.channelId = channelId;
        this.campaignType = campaignType;
        this.couponURL = couponURL;
        this.promotionID = promotionID;
        this.promotionCode = promotionCode;
        this.offerValidDate = offerValidDate;
    }

    public int getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignTitle() {
        return this.campaignTitle;
    }

    public String getPromotionCode() {
        return this.promotionCode;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCampaignDescription() {
        return this.campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getValidityEndDateTime() {
        return this.validityEndDateTime;
    }

    public void setValidityEndDateTime(String validityEndDateTime) {
        this.validityEndDateTime = validityEndDateTime;
    }

    public int getPromotionID() {
        return this.promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    public int getChannelTypeID() {
        return this.channelTypeID;
    }

    public void setChannelTypeID(int channelTypeID) {
        this.channelTypeID = channelTypeID;
    }

    public int isPMOffer() {
        return this.isPMOffer;
    }

    public void setPMOffer(int PMOffer) {
        this.isPMOffer = PMOffer;
    }

    public int getMailingId() {
        return this.mailingId;
    }

    public void setMailingId(int mailingId) {
        this.mailingId = mailingId;
    }

    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getCampaignType() {
        return this.campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public String getCouponURL() {
        return this.couponURL;
    }

    public void setCouponURL(String couponURL) {
        this.couponURL = couponURL;
    }

    public int getPoint() {
        return this.point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public long getOfferValidDate() {
        return this.offerValidDate;
    }

    public void setOfferValidDate(long offerValidDate) {
        this.offerValidDate = offerValidDate;
    }
}
