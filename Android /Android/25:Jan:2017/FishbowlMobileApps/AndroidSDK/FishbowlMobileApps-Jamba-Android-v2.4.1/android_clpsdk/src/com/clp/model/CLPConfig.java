package com.clp.model;

import java.io.Serializable;

public class CLPConfig implements Serializable {
	private static final long serialVersionUID = 3495379597464247812L;
	private String gcmSenderId = "";
	private String clpApiKey = "";
	private int pushIconResource = 0;

	public CLPConfig(String gcmSenderId, String clpApiKey, int pushIconResource) {
		super();
		this.gcmSenderId = gcmSenderId;
		this.clpApiKey = clpApiKey;
		this.pushIconResource = pushIconResource;
	}

	public CLPConfig() {
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
