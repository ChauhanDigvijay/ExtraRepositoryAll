package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by vt021 on 21/10/17.
 */

public class OloUpsellItems {
    private int id;
    private String name;
    private String shortdescription;
    private int minquantity;
    private int maxquantity;
    private int selectedQuantity;
    private String cost;
    private boolean isSelected;

    public OloUpsellItems(OloUpsellItems oloUpsellItems) {
        this.id = oloUpsellItems.getId();
        this.name = oloUpsellItems.getName();
        this.shortdescription = oloUpsellItems.getShortdescription();
        this.minquantity = oloUpsellItems.getMinquantity();
        this.maxquantity = oloUpsellItems.getMaxquantity();
        this.cost = oloUpsellItems.getCost();
        this.selectedQuantity = 1;
        this.isSelected = false;
    }

    public String getCost() {
        return this.cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortdescription() {
        return this.shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public int getMinquantity() {
        return this.minquantity;
    }

    public void setMinquantity(int minquantity) {
        this.minquantity = minquantity;
    }

    public int getMaxquantity() {
        return this.maxquantity;
    }

    public void setMaxquantity(int maxquantity) {
        this.maxquantity = maxquantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }
}
