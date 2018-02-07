package com.olo.jambajuice.BusinessLogic.Models;

import java.io.Serializable;

/**
 * Created by VT017 on 3/14/2017.
 */

public class FavoriteOrder implements Serializable {
    private String id;
    private String name = "";
    private boolean online = false;
    private String vendorid = "";
    private String vendorname = "";
    private boolean disabled = false;

    public FavoriteOrder(JambaOrderStatus status) {
        id = status.getId();
        name = status.getName();
        online = status.isOnline();
        vendorid = status.getVendorid();
        vendorname = status.getVendorname();
        disabled = status.isDisabled();
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


}
