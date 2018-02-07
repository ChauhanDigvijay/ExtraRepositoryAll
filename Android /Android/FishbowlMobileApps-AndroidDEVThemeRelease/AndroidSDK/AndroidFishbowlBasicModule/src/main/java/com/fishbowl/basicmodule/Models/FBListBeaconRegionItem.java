package com.fishbowl.basicmodule.Models;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by digvijay(dj)
 */

public class FBListBeaconRegionItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7491053409611429L;
	public String NAME;
	 public String ID;
	 public String UDID;
	 public String MAJOR;
	 public String DESC;
	 public String ENABLED;
	 public String STORE_NO;
	 public String MINOR;
	 public ArrayList<FBListBeaconRegionItem> beacons;

}
