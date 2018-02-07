package com.BasicApp.BusinessLogic.Models;

import java.io.Serializable;

/**
 * Created by digvijaychauhan on 21/09/16.
 */

public class RewardsItem implements Serializable {
    Boolean ispmOffer;

    public Boolean getIspmOffer() {
        return ispmOffer;
    }

    public void setIspmOffer(Boolean ispmOffer) {
        this.ispmOffer = ispmOffer;
    }
    String htmlBody;

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    String message;
    public int compaignId;
    String compaignTitle;
    String compaignDescription;
    String  validityEndDateTime;
    public int promotionID;
    public int channelID = 0;
    String passPreviewImageURL ;
    String promotioncode;
    String couponURL;

    public String getCouponURL(){return couponURL;}
    public void setCouponURL(String couponURL){ this.couponURL = couponURL;}

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
