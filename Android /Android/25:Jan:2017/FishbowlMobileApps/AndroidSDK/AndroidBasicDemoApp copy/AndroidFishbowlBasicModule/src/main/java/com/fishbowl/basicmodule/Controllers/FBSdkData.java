//package com.fishbowl.basicmodule.Controllers;
//
//import android.content.Context;
//import android.location.Location;
//
//import com.fishbowl.basicmodule.Analytics.FBEventSettings;
//import com.fishbowl.basicmodule.Models.FBCustomerItem;
//import com.fishbowl.basicmodule.Models.FBStoresItem;
//
//import java.io.EOFException;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.OptionalDataException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by digvijay(dj)
// */
//
//public class FBSdkData implements Serializable {
//
//    private static final long serialVersionUID = -7804350200144195850L;
//    public FBCustomerItem currCustomer;
//    public List<FBStoresItem> allCLPBeaconStoreList;
//    public List<FBStoresItem> allStoresList;
//    public com.fishbowl.basicmodule.Analytics.FBEventSettings FBEventSettings;
//    public boolean bluetoothPermission = false;
//    public boolean gpsPermission = false;
//    public String latitude;
//    public String longitude;
//    public String gcmToken;
//    private transient Context context;
//    private String fileName;
//
//    public FBSdkData(Context context, String fileName) {
//        this.context = context;
//        this.fileName = fileName;
//        this.allCLPBeaconStoreList = null;
//        this.allStoresList = null;
//        this.FBEventSettings = new FBEventSettings();
//        bluetoothPermission = false;
//        gpsPermission = false;
//        this.latitude = "0.0";
//        this.longitude = "0.0";
//        this.gcmToken = "";
//        currCustomer = new FBCustomerItem();
//    }
//
//
//    public void save() {
//        try {
//            FileOutputStream fos = context.openFileOutput(fileName,
//                    Context.MODE_PRIVATE);
//            ObjectOutputStream os = new ObjectOutputStream(fos);
//            os.writeObject(this);
//            os.close();
//        } catch (Exception ex) {
//
//        }
//    }
//
//    private void save(String key, Object dataObj) {
//        try {
//            if (key == null || dataObj == null)
//                return;
//            refresh();// refresh before overwriting
//            FileOutputStream fos = context.openFileOutput(fileName,
//                    Context.MODE_PRIVATE);
//            ObjectOutputStream os = new ObjectOutputStream(fos);
//
//            if (key.equals(FBSdk.PERSISTENT_CONTEXT)
//                    && dataObj instanceof Context) {
//                Context dataContext = (Context) dataObj;
//                if (dataContext != null) {
//                    this.context = dataContext;// application context
//                }
//            } else if (key.equals(FBSdk.PERSISTENT_FILE_NAME)
//                    && dataObj instanceof String) {
//                String dataString = (String) dataObj;
//                if (dataString != null) {
//                    this.fileName = dataString;// file firstname
//                }
//            } else if (key.equals(FBSdk.PERSISTENT_CUSTOMER)
//                    && dataObj instanceof FBCustomerItem) {
//                FBCustomerItem dataCustomer = (FBCustomerItem) dataObj;
//                if (dataCustomer != null) {
//                    this.currCustomer = dataCustomer;// customer info
//                }
//            } else if (key.equals(FBSdk.PERSISTENT_ALL_BEACON_STORE_LIST)
//                    && dataObj instanceof List<?>) {
//                List<?> dataList = (List<?>) dataObj;
//                if (dataList != null && dataList.size() > 0) {
//                    this.allCLPBeaconStoreList = new ArrayList<FBStoresItem>();
//                    for (int i = 0; i < dataList.size(); i++) {
//                        this.allCLPBeaconStoreList.add((FBStoresItem) dataList
//                                .get(i));// all beacon
//                        // stores
//                    }
//                }
//            } else if (key.equals(FBSdk.PERSISTENT_ALL_SORTED_STORE_LIST)
//                    && dataObj instanceof List<?>) {
//                List<?> dataList = (List<?>) dataObj;
//                if (dataList != null && dataList.size() > 0) {
//                    this.allStoresList = new ArrayList<FBStoresItem>();
//                    for (int i = 0; i < dataList.size(); i++) {
//                        this.allStoresList.add((FBStoresItem) dataList.get(i));// store
//                        // store
//                        // list
//                    }
//                }
//
//            } else if (key.equals(FBSdk.PERSISTENT_EVENTS_SETTINGS)
//                    && dataObj instanceof com.fishbowl.basicmodule.Analytics.FBEventSettings) {
//                com.fishbowl.basicmodule.Analytics.FBEventSettings dataEventSettings = (com.fishbowl.basicmodule.Analytics.FBEventSettings) dataObj;
//                if (dataEventSettings != null) {
//                    this.FBEventSettings = dataEventSettings;// mobile settings
//                }
//
//            } else if (key.equals(FBSdk.PERSISTENT_BLUETOOTH_PERMISSION)
//                    && dataObj instanceof Boolean) {
//                boolean dataBlePersmission = (Boolean) dataObj;
//                this.bluetoothPermission = dataBlePersmission;// bluetooth
//                // permission
//            } else if (key.equals(FBSdk.PERSISTENT_GPS_PERMISSION)
//                    && dataObj instanceof Boolean) {
//                boolean dataGpsPersmission = (Boolean) dataObj;
//                this.gpsPermission = dataGpsPersmission;// gps permission
//            } else if (key.equals(FBSdk.PERSISTENT_LATITUDE)
//                    && dataObj instanceof String) {
//                if (dataObj != null) {
//                    this.latitude = (String) dataObj;// latitude
//                }
//            } else if (key.equals(FBSdk.PERSISTENT_LONGITUDE)
//                    && dataObj instanceof String) {
//                if (dataObj != null) {
//                    this.longitude = (String) dataObj;// longitude
//                }
//            } else if (key.equals(FBSdk.PERSISTENT_GCM_TOKEN)
//                    && dataObj instanceof String) {
//                String dataString = (String) dataObj;
//                if (dataString != null) {
//                    this.gcmToken = dataString;// GCM Token
//                }
//            } else {
//                // No action
//            }
//            os.writeObject(this);
//            os.flush();
//            os.close();
//        } catch (Exception ex) {
//
//        }
//    }
//
//    public void refresh() {
//        FBSdkData data = null;
//
//        try {
//            FileInputStream fis = context.openFileInput(fileName);
//            ObjectInputStream is = new ObjectInputStream(fis);
//            int readSize = is.read();
//            if (readSize == -1) {
//                System.out.println("NO ERRORS!");
//            } else {
//                System.out.println("SOME ERRORS!");
//            }
//            //Log.d("", is.readObject().toString());
//            //s.readObject()==null &&
//            //if (is.available()<=0) {
//            //	return;
//            //}
//            data = (FBSdkData) is.readObject();
//            this.currCustomer = data.currCustomer;
//            this.allCLPBeaconStoreList = data.allCLPBeaconStoreList;
//            this.allStoresList = data.allStoresList;
//            //	this.mobileSettings = data.mobileSettings;
//            this.FBEventSettings = data.FBEventSettings;
//            this.bluetoothPermission = data.bluetoothPermission;
//            this.gpsPermission = data.gpsPermission;
//            this.latitude = data.latitude;
//            this.longitude = data.longitude;
//            this.gcmToken = data.gcmToken;
//            is.close();
//
//        } catch (OptionalDataException e) {
//
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception ex) {
//
//            if (FBSdk.sharedInstance(context) != null) {
//
//            }
//            ex.printStackTrace();
//        }
//    }
//
//    public boolean fileHasContent() {
//        try {
//            FileInputStream fis = context.openFileInput(fileName);
//            ObjectInputStream is = new ObjectInputStream(fis);
//            if (is.readObject() == null && is.available() <= 0) {
//                is.close();
//                return false;
//            } else {
//                is.close();
//                return true;
//            }
//        } catch (EOFException ex) {
//
//            return false;
//        } catch (Exception ex) {
//
//            return false;
//        }
//    }
//
//    public FBCustomerItem getCurrCustomer() {
//        if (this.currCustomer == null) {
//            refresh();
//        }
//        return this.currCustomer;
//    }
//
//    public void setCustomer(FBCustomerItem customer) {
//        this.currCustomer = customer;//added By vaseem
//        save(FBSdk.PERSISTENT_CUSTOMER, customer);
//    }
//
//    public String getGcmToken() {
//        if (this.gcmToken == null) {
//            refresh();
//        }
//        return gcmToken;
//    }
//
//    public void setGcmToken(String gcmToken) {
//        save(FBSdk.PERSISTENT_GCM_TOKEN, gcmToken);
//    }
//
//    public List<FBStoresItem> getAllCLPBeaconStoreList() {
//        if (this.allCLPBeaconStoreList == null) {
//
//            refresh();
//        }
//        if (this.allCLPBeaconStoreList == null) {
//
//            //FBSdk.sharedInstance(context).getAllStores();
//        }
//        return allCLPBeaconStoreList;
//    }
//
//    public void setAllCLPBeaconStoreList(List<FBStoresItem> allCLPBeaconStoreList) {
//        // this.allCLPBeaconStoreList = allCLPBeaconStoreList;
//        save(FBSdk.PERSISTENT_ALL_BEACON_STORE_LIST, allCLPBeaconStoreList);
//    }
//
//    public List<FBStoresItem> getAllStoresList() {
//        if (this.allStoresList == null) {
//
//            refresh();
//        }
//        // if (this.allStoresList == null) {
//        // FBSdk.localLog("Clpdata :", "get Stores from server");
//        // fbSdk.sharedInstance(context).getAllStores();
//        // }
//        return allStoresList;
//    }
//
//    public void setAllStoresList(List<FBStoresItem> allStoresList) {
//        // this.allStoresList = allStoresList;
//        save(FBSdk.PERSISTENT_ALL_SORTED_STORE_LIST, allStoresList);
//    }
////	//public FBMobileSettingsItem getMobileSettings() {
////		return mobileSettings;
////	}
//
//    public String getLatitude() {
//        if (latitude == null) {
//            refresh();
//        }
//        return latitude;
//    }
//
//    public String getLongitude() {
//        if (longitude == null) {
//            refresh();
//        }
//        return longitude;
//    }
//
//    public boolean isBluetoothPermission() {
//        return bluetoothPermission;
//    }
//
//    public void setBluetoothPermission(boolean bluetoothPermission) {
//        // this.bluetoothPermission = bluetoothPermission;
//        save(FBSdk.PERSISTENT_BLUETOOTH_PERMISSION, bluetoothPermission);
//    }
//
//
//    public void setGpsPermission(boolean gpsPermission) {
//        // this.gpsPermission = gpsPermission;
//        save(FBSdk.PERSISTENT_GPS_PERMISSION, gpsPermission);
//
//    }
//
//
//    public com.fishbowl.basicmodule.Analytics.FBEventSettings getEventSetting() {
//        return FBEventSettings;
//    }
//
//    public void setEventSetting(com.fishbowl.basicmodule.Analytics.FBEventSettings eventSetting) {
//        save(FBSdk.PERSISTENT_EVENTS_SETTINGS, eventSetting);
//    }
//
//    public Location getLocation() {
//        Location storeLocation = new Location("cached");
//        storeLocation.setLatitude(Double.valueOf(this.getLatitude()));
//        storeLocation.setLongitude(Double.valueOf(this.getLongitude()));
//        return storeLocation;
//    }
//}
