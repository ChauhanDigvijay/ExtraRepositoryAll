package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

public class RewardsItem extends RewardAndOfferItem {
    public int compaignId;
    public int promotionID;
    public int channelID = 0;
    Boolean ispmOffer;
    String htmlBody;
    String message;
    String compaignTitle;
    String compaignDescription;
    String validityEndDateTime;
    String passPreviewImageURL;
    String promotioncode;
    String passCustomStripUrl;
    String customLogoURL;
    String notificationContent;

    public Boolean getIspmOffer() {
        return ispmOffer;
    }

    public void setIspmOffer(Boolean ispmOffer) {
        this.ispmOffer = ispmOffer;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getPassCustomStripUrl() {
        return passCustomStripUrl;
    }

    public void setPassCustomStripUrl(String passCustomStripUrl) {
        this.passCustomStripUrl = passCustomStripUrl;
    }

    public String getCustomLogoURL() {
        return customLogoURL;
    }

    public void setCustomLogoURL(String customLogoURL) {
        this.customLogoURL = customLogoURL;
    }

    public String getPromotioncode() {
        return promotioncode;
    }

    public void setPromotioncode(String promotioncode) {
        this.promotioncode = promotioncode;
    }

    public String getPassPreviewImageURL() {
        return passPreviewImageURL;
    }

    public void setPassPreviewImageURL(String passPreviewImageURL) {
        this.passPreviewImageURL = passPreviewImageURL;
    }

    public int getCompaignId() {
        return compaignId;
    }

    public void setCompaignId(int compaignId) {
        this.compaignId = compaignId;
    }

    public String getCompaignTitle() {
        return compaignTitle;
    }

    public void setCompaignTitle(String compaignTitle) {
        this.compaignTitle = compaignTitle;
    }

    public String getCompaignDescription() {
        return compaignDescription;
    }

    public void setCompaignDescription(String compaignDescription) {
        this.compaignDescription = compaignDescription;
    }

    public String getValidityEndDateTime() {
        return validityEndDateTime;
    }

    public void setValidityEndDateTime(String validityEndDateTime) {
        this.validityEndDateTime = validityEndDateTime;
    }

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}