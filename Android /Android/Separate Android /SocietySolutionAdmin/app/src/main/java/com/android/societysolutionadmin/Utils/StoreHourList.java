package com.android.societysolutionadmin.Utils;

import org.json.JSONObject;

/**
 * Created by schaudhary_ic on 12-Oct-16.
 */

public class StoreHourList {
    public int storeId;
    public int id;
    public int dayOfTheWeek;
    public String openingTime;
    public String closingTime;


    public StoreHourList(){}
    public StoreHourList(JSONObject obj){
        try{
        storeId = obj.has("storeId") ? obj.getInt("storeId") : 0;
            id = obj.has("id") ? obj.getInt("id") : 0;
            dayOfTheWeek = obj.has("dayOfTheWeek") ? obj.getInt("dayOfTheWeek") : 0;
            openingTime =obj.has("openingTime")? obj.getString("openingTime"):null;
            closingTime =obj.has("closingTime")? obj.getString("closingTime"):null;

        }


        catch (Exception e){
            e.printStackTrace();
        }
    }
    public int getStoreId() {
        return storeId;
    }
    public void setStoreId(int storeID) {
        this.storeId = storeID;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }
    public void setDayofTheWeek(int dayOfTheWeek) {  this.dayOfTheWeek = dayOfTheWeek; }

    public int getid() {
        return id;
    }
    public void setid(int id) {  this.id = id; }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }
    public String getOpeningTime() {
        return openingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
    public String getClosingTime() {
        return closingTime;
    }

}
