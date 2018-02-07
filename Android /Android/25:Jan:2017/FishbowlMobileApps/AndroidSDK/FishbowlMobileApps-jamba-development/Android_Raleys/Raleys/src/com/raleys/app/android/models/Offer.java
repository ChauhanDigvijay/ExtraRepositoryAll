package com.raleys.app.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Offer implements Serializable {
	private static final long serialVersionUID = 8714970475455405873L;
	public String offerType;
	public String offerProductImage;
	public String consumerDesc;
	public String consumerDetails;
	public String offerSize;
	public String offerLimit;
	public String offerId;
	public String offeridLive;
	public String offerProductImageFile;
	public float offerPrice;
	public String offerLogoImageFile;
	public String acceptGroup;
	public String consumerTitle;
	public String offerOrderNo;
	public String offerListType;
	public String startDate;
	public String offerProductImageAlt;
	public String candidateGroup;
	public String offerLogoImageAlt;
	public String offerLogoImage;
	public String active;
	public String hideAccept;
	public String endDate;
	public ArrayList<Offer> offerList;

	public boolean _acceptableOffer; // not used by json
	public boolean _acceptedOffer; // not used by json

	public String promoCode;
	public boolean dynamicOffer;// newly added for closed loop fix
}
