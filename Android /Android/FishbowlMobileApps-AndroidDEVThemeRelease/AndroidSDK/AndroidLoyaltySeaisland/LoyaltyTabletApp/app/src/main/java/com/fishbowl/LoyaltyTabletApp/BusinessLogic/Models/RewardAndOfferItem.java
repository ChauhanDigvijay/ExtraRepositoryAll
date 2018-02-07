package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 21-Nov-16.
 */

public class RewardAndOfferItem implements Serializable {
    public int compaignId;
    public int promotionID;
    public int channelID = 0;
    public int pmPromotionID = 0;
    public Double offerIPrice = 0.00;
    String offerIName;
    String datetime;
    String OfferId;
    String offerIUrl;
    String offerIOther;
    String offerIItem;
    String message;
    String compaignTitle;
    String compaignDescription;
    String validityEndDateTime;
    String passPreviewImageURL;
    String promotioncode;
    String passCustomStripUrl;
    String customLogoURL;
    Boolean ispmOffer;
    String htmlBody;
    private long id;
    private boolean isPMOffer;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isPMOffer() {
        return isPMOffer;
    }

    public void setPMOffer(boolean PMOffer) {
        isPMOffer = PMOffer;
    }

    public int getPmPromotionID() {
        return pmPromotionID;
    }

    public void setPmPromotionID(int pmPromotionID) {
        this.pmPromotionID = pmPromotionID;
    }

    public String getOfferId() {
        return OfferId;
    }

    public void setOfferId(String offerId) {
        OfferId = offerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOfferIItem() {
        return offerIItem;
    }

    public void setOfferIItem(String offerIItem) {
        this.offerIItem = offerIItem;
    }

    public String getOfferIName() {
        return offerIName;
    }

    public void setOfferIName(String offerIName) {
        this.offerIName = offerIName;
    }

    public String getOfferIUrl() {
        return offerIUrl;
    }

    public void setOfferIUrl(String offerIUrl) {
        this.offerIUrl = offerIUrl;
    }

    public void setcustomLogoURL(String customLogoURL) {
        this.customLogoURL = customLogoURL;
    }

    public void setpassCustomStripUrl(String passCustomStripUrl) {
        this.passCustomStripUrl = passCustomStripUrl;
    }

    public String getOfferIOther() {
        return offerIOther;
    }

    public void setOfferIOther(String offerIOther) {
        this.offerIOther = offerIOther;
    }

    public Double getOfferIPrice() {
        return offerIPrice;
    }

    public void setOfferIPrice(Double offerIPrice) {
        this.offerIPrice = offerIPrice;
    }

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
