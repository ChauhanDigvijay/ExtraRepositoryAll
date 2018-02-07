package com.clp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CLPListBeaconRegions implements Serializable{
	
	private static final long serialVersionUID = -6193466922565614670L;
	public String NAME;
	 public String ID;
	 public String UDID;
	 public String MAJOR;
	 public String DESC;
	 public String ENABLED;
	 public String STORE_NO;
	 public String MINOR;
	 public ArrayList<CLPListBeaconRegions> beacons;

}
