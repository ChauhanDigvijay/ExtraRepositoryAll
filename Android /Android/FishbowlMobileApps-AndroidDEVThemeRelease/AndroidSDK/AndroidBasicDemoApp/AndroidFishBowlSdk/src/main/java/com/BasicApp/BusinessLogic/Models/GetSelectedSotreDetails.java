package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 10-Feb-17.
 */

public class GetSelectedSotreDetails implements Serializable {
    public int storeId;
    public int id;
    public int dayOfTheWeek;
    public String openingTime;
    public String closingTime;
    public String startPickupHours;
    public String endPickupHours;
    public String startDeliveryHours;
    public String endDeliveryHours;
    public String startHolidayHours;
    public String endHolidayHours;
    public boolean closed;


    public static GetSelectedSotreDetails instance;

    public GetSelectedSotreDetails() {

    }

    public static GetSelectedSotreDetails sharedInstance() {
        if (instance == null) {
            instance = new GetSelectedSotreDetails();
        }
        return instance;
    }

    public GetSelectedSotreDetails(JSONObject obj) {
        try {
            storeId = obj.has("storeId") ? obj.getInt("storeId") : 0;
            id = obj.has("id") ? obj.getInt("id") : 0;
            dayOfTheWeek = obj.has("dayOfTheWeek") ? obj.getInt("dayOfTheWeek") : 0;
            openingTime = obj.has("openingTime") ? obj.getString("openingTime") : null;
            closingTime = obj.has("closingTime") ? obj.getString("closingTime") : null;
            startPickupHours = obj.has("startPickupHours") ? obj.getString("startPickupHours") : null;
            endPickupHours = obj.has("endPickupHours") ? obj.getString("endPickupHours") : null;
            startDeliveryHours = obj.has("startDeliveryHours") ? obj.getString("startDeliveryHours") : null;
            endDeliveryHours = obj.has("endDeliveryHours") ? obj.getString("endDeliveryHours") : null;
            startHolidayHours = obj.has("startHolidayHours") ? obj.getString("startHolidayHours") : null;
            endHolidayHours = obj.has("endHolidayHours") ? obj.getString("endHolidayHours") : null;
            closed = obj.has("closed") ? obj.getBoolean("closed") : false;
        } catch (Exception e) {
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
    public void setDayofTheWeek(int dayOfTheWeek) {this.dayOfTheWeek = dayOfTheWeek;}

    public int getid() {
        return id;
    }
    public void setid(int id) {this.id = id;}

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

    public void setStartPickupHours(String startPickupHours) {this.startPickupHours = startPickupHours;}
    public String getStartPickupHours() {
        return startPickupHours;
    }

    public void setEndPickupHours(String endPickupHours) {this.endPickupHours = endPickupHours;}
    public String getEndPickupHours() {
        return endPickupHours;
    }

    public void setStartDeliveryHours(String startDeliveryHours) {this.startDeliveryHours = startDeliveryHours;}
    public String getStartDeliveryHours() {
        return startDeliveryHours;
    }

    public void setEndDeliveryHours(String endDeliveryHours) {this.endDeliveryHours = endDeliveryHours;}
    public String getEndDeliveryHours() {
        return endDeliveryHours;
    }

    public void setStartHolidayHours(String startHolidayHours) {this.startHolidayHours = startHolidayHours;}
    public String getStartHolidayHours() {
        return startHolidayHours;
    }

    public void setEndHolidayHours(String endHolidayHours) {this.endHolidayHours = endHolidayHours;}
    public String getEndHolidayHours() {return endHolidayHours;}

    public void setClosed(boolean closed) {this.closed = closed;}
    public Boolean getClosed() {return closed;}

}
