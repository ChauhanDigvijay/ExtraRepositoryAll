package com.fishbowl.fbtemplate1.Model;
/**
 **
 * Created by Digvijay Chauhan on 7/1/16.
 */
public class OfferItem  implements java.io.Serializable
{
	private long id;
	String offerIName;
	String OfferId;	
	String offerIUrl;
	String offerIOther;
	String offerIItem;	
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
