package com.clp.sdk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;

import com.clp.model.CLPCustomer;
import com.clp.model.CLPMobileSettings;
import com.clp.model.CLPStores;

public class CLPSdkData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1234567890123456789L;
	private transient Context context;
	private String fileName;
	public CLPCustomer currCustomer;
	private List<CLPStores> allCLPBeaconStoreList;
	private List<CLPStores> allStoresList;
	public CLPMobileSettings mobileSettings;
	public boolean bluetoothPermission = false;
	public boolean gpsPermission = false;
	public String latitude;
	public String longitude;

	public CLPSdkData(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
		this.allCLPBeaconStoreList = null;
		this.allStoresList = null;
		this.mobileSettings = new CLPMobileSettings();
		bluetoothPermission = false;
		gpsPermission = false;
		this.latitude = "0.0";
		this.longitude = "0.0";
	}

	public void save() {
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
		} catch (Exception ex) {
			CLPSdk.sharedInstance(context).localLog(getClass().getSimpleName(), "Failed to write " + fileName
					+ ", " + ex.toString());
		}
	}

	private void save(String key, Object dataObj) {
		try {
			if (key == null || dataObj == null)
				return;
			refresh();// refresh before overwriting
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);

			if (key.equals(CLPSdk.PERSISTENT_CONTEXT)
					&& dataObj instanceof Context) {
				Context dataContext = (Context) dataObj;
				if (dataContext != null) {
					this.context = dataContext;// application context
				}
			} else if (key.equals(CLPSdk.PERSISTENT_FILE_NAME)
					&& dataObj instanceof String) {
				String dataString = (String) dataObj;
				if (dataString != null) {
					this.fileName = dataString;// file name
				}
			} else if (key.equals(CLPSdk.PERSISTENT_CUSTOMER)
					&& dataObj instanceof CLPCustomer) {
				CLPCustomer dataCustomer = (CLPCustomer) dataObj;
				if (dataCustomer != null) {
					this.currCustomer = dataCustomer;// customer info
				}
			} else if (key.equals(CLPSdk.PERSISTENT_ALL_BEACON_STORE_LIST)
					&& dataObj instanceof List<?>) {
				List<?> dataList = (List<?>) dataObj;
				if (dataList != null && dataList.size() > 0) {
					this.allCLPBeaconStoreList = new ArrayList<CLPStores>();
					for (int i = 0; i < dataList.size(); i++) {
						this.allCLPBeaconStoreList
								.add((CLPStores) dataList.get(i));// all beacon
																// stores
					}
				}
			} else if (key.equals(CLPSdk.PERSISTENT_ALL_SORTED_STORE_LIST)
					&& dataObj instanceof List<?>) {
				List<?> dataList = (List<?>) dataObj;
				if (dataList != null && dataList.size() > 0) {
					this.allStoresList = new ArrayList<CLPStores>();
					for (int i = 0; i < dataList.size(); i++) {
						this.allStoresList.add((CLPStores) dataList.get(i));// store
																			// store
																			// list
					}
				}

			} else if (key.equals(CLPSdk.PERSISTENT_MOBILE_SETTINGS)
					&& dataObj instanceof CLPMobileSettings) {
				CLPMobileSettings dataMobileSettings = (CLPMobileSettings) dataObj;
				if (dataMobileSettings != null) {
					this.mobileSettings = dataMobileSettings;// mobile settings
				}

			} else if (key.equals(CLPSdk.PERSISTENT_BLUETOOTH_PERMISSION)
					&& dataObj instanceof Boolean) {
				boolean dataBlePersmission = (Boolean) dataObj;
				this.bluetoothPermission = dataBlePersmission;// bluetooth
																// permission
			} else if (key.equals(CLPSdk.PERSISTENT_GPS_PERMISSION)
					&& dataObj instanceof Boolean) {
				boolean dataGpsPersmission = (Boolean) dataObj;
				this.gpsPermission = dataGpsPersmission;// gps permission
			} else if (key.equals(CLPSdk.PERSISTENT_LATITUDE)
					&& dataObj instanceof String) {
				if (dataObj != null) {
					this.latitude = (String) dataObj;// latitude
				}
			} else if (key.equals(CLPSdk.PERSISTENT_LONGITUDE)
					&& dataObj instanceof String) {
				if (dataObj != null) {
					this.longitude = (String) dataObj;// longitude
				}
			} else {
				// No action
			}
			os.writeObject(this);
			os.close();
		} catch (Exception ex) {
			CLPSdk.sharedInstance(context).localLog(getClass().getSimpleName(), "Failed to write " + fileName
					+ ", " + ex.toString());
		}
	}

	public void refresh() {
		CLPSdkData data = null;

		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			data = (CLPSdkData) is.readObject();
			this.currCustomer = data.currCustomer;
			this.allCLPBeaconStoreList = data.allCLPBeaconStoreList;
			this.allStoresList = data.allStoresList;
			this.mobileSettings = data.mobileSettings;
			this.bluetoothPermission = data.bluetoothPermission;
			this.gpsPermission = data.gpsPermission;
			this.latitude = data.latitude;
			this.longitude = data.longitude;
			is.close();
		} catch (Exception ex) {
			CLPSdk.sharedInstance(context).localLog(getClass().getSimpleName(), "Failed to read " + fileName
					+ ", " + ex.toString());
		}
	}

	public CLPCustomer getCurrCustomer() {
		if (this.currCustomer == null) {
			refresh();
		}
		return this.currCustomer;
	}

	public void setCustomer(CLPCustomer customer) {
		save(CLPSdk.PERSISTENT_CUSTOMER, customer);
	}

	public List<CLPStores> getAllCLPBeaconStoreList() {
		if (this.allCLPBeaconStoreList == null) {
			CLPSdk.sharedInstance(context).localLog("Clpdata :", "refreshing from persistence");
			refresh();
		}
		if (this.allCLPBeaconStoreList == null) {
			CLPSdk.sharedInstance(context).localLog("Clpdata :", "get Stores from server");
			CLPSdk.sharedInstance(context).getAllStores();
		}
		return allCLPBeaconStoreList;
	}

	public List<CLPStores> getAllStoresList() {
		if (this.allStoresList == null) {
			CLPSdk.sharedInstance(context).localLog("Clpdata :", "refreshing from persistence");
			refresh();
		}
		// if (this.allStoresList == null) {
		// CLPSdk.localLog("Clpdata :", "get Stores from server");
		// clpsdk.sharedInstance(context).getAllStores();
		// }
		return allStoresList;
	}

	public void setAllCLPBeaconStoreList(List<CLPStores> allCLPBeaconStoreList) {
		// this.allCLPBeaconStoreList = allCLPBeaconStoreList;
		save(CLPSdk.PERSISTENT_ALL_BEACON_STORE_LIST, allCLPBeaconStoreList);
	}

	public void setAllStoresList(List<CLPStores> allStoresList) {
		// this.allStoresList = allStoresList;
		save(CLPSdk.PERSISTENT_ALL_SORTED_STORE_LIST, allStoresList);
	}

	public String getLatitude() {
		if (latitude == null) {
			refresh();
		}
		return latitude;
	}

	public String getLongitude() {
		if (longitude == null) {
			refresh();
		}
		return longitude;
	}

	public boolean isBluetoothPermission() {
		return bluetoothPermission;
	}

	public void setBluetoothPermission(boolean bluetoothPermission) {
		// this.bluetoothPermission = bluetoothPermission;
		save(CLPSdk.PERSISTENT_BLUETOOTH_PERMISSION, bluetoothPermission);
	}

	public boolean isGpsPermission() {
		return gpsPermission;
	}

	public void setGpsPermission(boolean gpsPermission) {
		// this.gpsPermission = gpsPermission;
		save(CLPSdk.PERSISTENT_GPS_PERMISSION, gpsPermission);

	}

	public void setMobileSettings(CLPMobileSettings mobileSettings) {
		save(CLPSdk.PERSISTENT_MOBILE_SETTINGS, mobileSettings);
	}

	public Location getLocation() {
		Location storeLocation = new Location("cached");
		storeLocation.setLatitude(Double.valueOf(this.getLatitude()));
		storeLocation.setLongitude(Double.valueOf(this.getLongitude()));
		return storeLocation;
	}
}
