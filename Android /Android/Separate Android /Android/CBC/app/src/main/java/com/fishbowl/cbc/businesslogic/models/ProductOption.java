package com.fishbowl.cbc.businesslogic.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauman Afzaal on 03/06/15.
 */
public class ProductOption {
    private int id;
    private int nestedOptionId;
    private String name;
    private float cost;
    private boolean isSelected;

    private int minSelect;
    private int maxSelect;
    private int minChoiceQuantity;
    private int maxChoiceQuantity;
    private int maxAggregateQuantity;
    private boolean isMandatory;
    private boolean supportschoicequantities;
    private int quantity = 1;
    private List<ProductOption> subOptions;

    private boolean isDefault;

    public ProductOption() {
    }

    public ProductOption(int id, String name, float cost, boolean isSelected) {
        this.id = id;
        this.setName(name);
        this.cost = cost;
        this.isSelected = isSelected;
    }

    public ProductOption(int id, String name, float cost, boolean isSelected, boolean isMandatory) {
        this.id = id;
        this.setName(name);
        this.cost = cost;
        this.isSelected = isSelected;
        this.isMandatory = isMandatory;
    }

    public ProductOption(String name, int id, float cost, boolean isSelected, boolean isMandatory, ArrayList<ProductOption> subOptions) {
        this.name = name;
        this.id = id;
        this.cost = cost;
        this.isSelected = isSelected;
        this.isMandatory = isMandatory;
        this.subOptions = subOptions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getNestedOptionId() {
        return nestedOptionId;
    }

    public void setNestedOptionId(int nestedOptionId) {
        this.nestedOptionId = nestedOptionId;
    }

    public int getMinSelect() {
        return minSelect;
    }

    public void setMinSelect(int minSelect) {
        this.minSelect = minSelect;
    }

    public int getMaxSelect() {
        return maxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public List<ProductOption> getSubOptions() {
        return subOptions;
    }

    public void setSubOptions(List<ProductOption> subOptions) {
        this.subOptions = subOptions;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinChoiceQuantity() {
        return minChoiceQuantity;
    }

    public void setMinChoiceQuantity(int minChoiceQuantity) {
        this.minChoiceQuantity = minChoiceQuantity;
    }

    public int getMaxChoiceQuantity() {
        return maxChoiceQuantity;
    }

    public void setMaxChoiceQuantity(int maxChoiceQuantity) {
        this.maxChoiceQuantity = maxChoiceQuantity;
    }

    public int getMaxAggregateQuantity() {
        return maxAggregateQuantity;
    }

    public void setMaxAggregateQuantity(int maxAggregateQuantity) {
        this.maxAggregateQuantity = maxAggregateQuantity;
    }

    public boolean isSupportschoicequantities() {
        return supportschoicequantities;
    }

    public void setSupportschoicequantities(boolean supportschoicequantities) {
        this.supportschoicequantities = supportschoicequantities;
    }


    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

}
