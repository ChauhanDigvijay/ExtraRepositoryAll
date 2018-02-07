package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloOrdersSatusProduct;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ihsanulhaq on 6/18/2015.
 */
public class RecentOrderDetails implements Serializable {

    private String name;
    private String specialInstructions;
    private float price;
    private String lastOrderedInfo;
    private List<BasketChoice> choices;

    public RecentOrderDetails(OloOrdersSatusProduct product) {
        this.name = product.getName();
        this.specialInstructions = product.getSpecialinstructions();
        this.price = product.getTotalcost();
    }

    public RecentOrderDetails(JambaOrderSatusProduct product) {
        this.name = product.getName();
        this.specialInstructions = product.getSpecialinstructions();
        this.price = product.getTotalcost();
        this.choices = product.getChoices();
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getLastOrderedInfo() {
        return lastOrderedInfo;
    }

    public void setLastOrderedInfo(String lastOrderedInfo) {
        this.lastOrderedInfo = lastOrderedInfo;
    }

    public List<BasketChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<BasketChoice> choices) {
        this.choices = choices;
    }
}