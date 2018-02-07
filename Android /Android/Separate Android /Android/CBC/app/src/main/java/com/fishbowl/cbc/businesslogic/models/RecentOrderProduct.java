package com.fishbowl.cbc.businesslogic.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by VT027 on 5/20/2017.
 */
@ParseClassName("RecentOrderProduct")
public class RecentOrderProduct extends ParseObject {
    public static final String Id = "Id";
    public static final String TimePlaced = "TimePlaced";

    public RecentOrderProduct() {
    }

    public RecentOrderProduct(String id, String time) {
        this.setId(id);
        this.setTimePlaced(time);
    }

    public String getId() {
        return getString(Id);
    }

    public void setId(String id) {
        put(Id, id);
    }

    public String getTimePlaced() {
        return getString(TimePlaced);
    }

    public void setTimePlaced(String timePlaced) {
        put(TimePlaced, timePlaced);
    }
}
