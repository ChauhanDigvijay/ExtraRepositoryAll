package com.raleys.app.android.models;

public class OfferAcceptRequest {
	public String crmNumber;
	public String acceptGroup;
	// New
	public String offerId;

	public String endDate;
	public String promoCode;
	public String title;
	public boolean dynamicOffer;// newly added for closed loop fix
}
