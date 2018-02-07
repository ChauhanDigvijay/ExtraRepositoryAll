package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by vt021 on 21/10/17.
 */

public class OloUpsellGroup {
    private String title;
    private ArrayList<OloUpsellItems> items;

    public OloUpsellGroup() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<OloUpsellItems> getUpsellitems() {
        return this.items;
    }

    public void setUpsellitems(ArrayList<OloUpsellItems> upsellitems) {
        this.items = upsellitems;
    }

}
