package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloBasketChoice;
import com.fishbowl.apps.olo.Models.OloBasketProduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VT027 on 5/22/2017.
 */

public class BasketProduct implements Serializable {
    private int id;
    private int productId;
    private String name;
    private String options;
    private int quantity;
    private float basecost;
    private float totalcost;
    private String specialinstructions;
    private List<BasketChoice> choices;

    public BasketProduct() {

    }

    public BasketProduct(OloBasketProduct oloBasketProduct) {
        setId(oloBasketProduct.getId());
        setProductId(oloBasketProduct.getProductId());
        setName(oloBasketProduct.getName());
        setOptions(oloBasketProduct.getOptions());
        setQuantity(oloBasketProduct.getQuantity());
        setBasecost(oloBasketProduct.getBaseCost());
        setTotalcost(oloBasketProduct.getTotalCost());
        setSpecialinstructions(oloBasketProduct.getSpecialInstructions());
        if (oloBasketProduct.getChoices() != null) {
            this.choices = new ArrayList<>();
            for (OloBasketChoice oloBasketChoice : oloBasketProduct.getChoices()) {
                choices.add(new BasketChoice(oloBasketChoice));
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getOptions() {
        return options;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getBasecost() {
        return basecost;
    }

    public float getTotalcost() {
        return totalcost;
    }

    public String getSpecialinstructions() {
        return specialinstructions;
    }

    public List<BasketChoice> getChoices() {
        return choices;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBasecost(float basecost) {
        this.basecost = basecost;
    }

    public void setTotalcost(float totalcost) {
        this.totalcost = totalcost;
    }

    public void setSpecialinstructions(String specialinstructions) {
        this.specialinstructions = specialinstructions;
    }

    public void setChoices(List<BasketChoice> choices) {
        this.choices = choices;
    }
}
