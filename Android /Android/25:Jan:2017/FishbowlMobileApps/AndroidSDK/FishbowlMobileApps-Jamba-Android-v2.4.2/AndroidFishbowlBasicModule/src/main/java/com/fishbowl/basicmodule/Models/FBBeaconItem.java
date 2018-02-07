package com.fishbowl.basicmodule.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by digvijay(dj)
 */
public class FBBeaconItem implements Serializable{
	
	
	private static final long serialVersionUID = 5266044751585937275L;
	public int beaconId;
    public int companyId;
    public String name;
    public String udid;
    public int major;
    public int minor;
    public String description;
    public int storeID;
    public String enabled;
    public String createDate;
    public String updateDate;
    
    public FBBeaconItem() {}

    public FBBeaconItem(JSONObject obj) {
        try {
			this.beaconId = obj.has("beaconId") ? obj.getInt("beaconId") : 0;
			this.companyId = obj.has("companyId") ?obj.getInt("companyId"): 0;
	        this.name = obj.has("firstname") ?obj.getString("firstname"): null;
	        this.udid = obj.has("udid") ?obj.getString("udid"): null;
	        this.major = obj.has("major") ?obj.getInt("major"): 0;
	        this.minor = obj.has("minor") ?obj.getInt("minor"): 0;
	        this.description = obj.has("description") ?obj.getString("description"): null;
	        this.storeID = obj.has("storeID") ?obj.getInt("storeID"): 0;
	        this.enabled = obj.has("enabled") ?obj.getString("enabled"): null;
	        this.createDate = obj.has("createDate") ?obj.getString("createDate"): null;
	        this.updateDate = obj.has("updateDate") ?obj.getString("updateDate"): null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }

	@Override
	public String toString() {
		return "Beacons [beaconId=" + beaconId + ", companyId=" + companyId + ", firstname=" + name
				+ ", udid=" + udid + ", major=" + major + ", minor=" + minor + ", storeID="
				+ storeID + "]";
	}

}