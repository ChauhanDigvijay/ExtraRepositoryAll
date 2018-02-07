package com.fishbowl.basicmodule.Models;

import java.io.Serializable;

/**
 * Created by digvijay(dj)
 */
public class FBConfig implements Serializable {
	private static final long serialVersionUID = 3495379597464247812L;
	private String gcmSenderId = "";
	private String clpApiKey = "";
	private int pushIconResource = 0;


	public static String client_id = "";
	public static String client_secret = "";

	public static String getClient_id() {
		return client_id;
	}

	public static void setClient_id(String client_id) {
		FBConfig.client_id = client_id;
	}

	public static String getClient_secret() {
		return client_secret;
	}

	public static void setClient_secret(String client_secret) {
		FBConfig.client_secret = client_secret;
	}

	public static String getClient_tenantid() {
		return client_tenantid;
	}

	public static void setClient_tenantid(String client_tenantid) {
		FBConfig.client_tenantid = client_tenantid;
	}

	public static String client_tenantid = "";
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
