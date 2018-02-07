package com.fishbowl.basicmodule.Models;

import java.io.Serializable;

/**
 **
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class FBOfferItem implements Serializable
{
	private long id;
	String offerIName;



	String datetime;
	String OfferId;
	private boolean isPMOffer;

	String htmlBody;
	String couponURL;

	public String getCouponURL(){return couponURL;}
	public void setCouponURL(String couponURL){ this.couponURL = couponURL;}

	public String getHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	String offerIUrl;
	String customLogoURL;
	String passCustomStripUrl;
	String promotioncode;

	public String getPromotioncode() {
		return promotioncode;
	}

	public void setPromotioncode(String promotioncode) {
		this.promotioncode = promotioncode;
	}

	public String getCustomLogoURL() {
		return customLogoURL;
	}

	public String getPassCustomStripUrl() {
		return passCustomStripUrl;
	}

	String offerIOther;
	String offerIItem;
	public int channelID = 0;



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

	public int pmPromotionID = 0;
	public int getChannelID() {
		return channelID;
	}

	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}

	public Double offerIPrice = 0.00;


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
	}public void setcustomLogoURL(String customLogoURL) {
	this.customLogoURL = customLogoURL;
}public void setpassCustomStripUrl(String passCustomStripUrl) {
	this.passCustomStripUrl = passCustomStripUrl;
}
	public void setOfferIUrl(String offerIUrl) {
		this.offerIUrl = offerIUrl;
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



}