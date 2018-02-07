package com.olo.jambajuice.BusinessLogic.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vthink on 21/02/17.
 */

public class OfferAvailableStore implements Serializable{

    private String storeCode;
    private String storeName;
    private int storeId;
    private String displayName;
    public static ArrayList<OfferAvailableStore> storeList;
    private int storeCount;

    public OfferAvailableStore(String storeCode, String storeName, int storeId, String displayName) {
        this.storeCode = storeCode;
        this.storeName = storeName;
        this.storeId = storeId;
        this.displayName = displayName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public static ArrayList<OfferAvailableStore> getStoreList() {
        return storeList;
    }

    public static void setStoreList(ArrayList<OfferAvailableStore> storeList) {
        OfferAvailableStore.storeList = storeList;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }
}
