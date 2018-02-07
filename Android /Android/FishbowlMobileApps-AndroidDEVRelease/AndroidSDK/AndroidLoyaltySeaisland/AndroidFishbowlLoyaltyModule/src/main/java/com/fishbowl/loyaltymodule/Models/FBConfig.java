package com.fishbowl.loyaltymodule.Models;

import java.io.Serializable;
/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class FBConfig implements Serializable {
	private static final long serialVersionUID = 3495379597464247812L;
	private String gcmSenderId = "";
	private String clpApiKey = "";
	private int pushIconResource = 0;


private String secrateKey = "";


	public String getSecrateKey() {
		return secrateKey;
	}

	public void setSecrateKey(String secrateKey) {
		this.secrateKey = secrateKey;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public FBConfig(String gcmSenderId, String clpApiKey, int pushIconResource) {
		super();
		this.gcmSenderId = gcmSenderId;
		this.clpApiKey = clpApiKey;
		this.pushIconResource = pushIconResource;
	}

	public FBConfig() {
	}

	public String getGcmSenderId() {
		return gcmSenderId;
	}

	public void setGcmSenderId(String gcmSenderId) {
		this.gcmSenderId = gcmSenderId;
	}

	public String getClpApiKey() {
		return clpApiKey;
	}

	public void setClpApiKey(String clpApiKey) {
		this.clpApiKey = clpApiKey;
	}

	public int getPushIconResource() {
		return pushIconResource;
	}

	public void setPushIconResource(int pushIconResource) {
		this.pushIconResource = pushIconResource;
	}
}
