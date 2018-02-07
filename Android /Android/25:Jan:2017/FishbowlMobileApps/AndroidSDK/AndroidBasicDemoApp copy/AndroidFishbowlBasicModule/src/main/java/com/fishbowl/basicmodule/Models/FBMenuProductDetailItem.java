package com.fishbowl.basicmodule.Models;

/**
 * Created by schaudhary_ic on 14-Feb-17.
 */

public class FBMenuProductDetailItem  {


    private String productNutrientList;
    private String productIngredients;
    private String productSizeList;
    private String productModifierList;
    private String productOptionList;
    private String productAllergensList;
    private int minWeightRange;
    private int maxWeightRange;
    private int productPrice;

    public String getProductNutrientList() {
        return productNutrientList;
    }

    public void setProductNutrientList(String productNutrientList) {
        this.productNutrientList = productNutrientList;
    }

    public String getProductIngredients() {
        return productIngredients;
    }

    public void setProductIngredients(String productIngredients) {
        this.productIngredients = productIngredients;
    }

    public String getProductSizeList() {
        return productSizeList;
    }

    public void setProductSizeList(String productSizeList) {
        this.productSizeList = productSizeList;
    }

    public String getProductModifierList() {
        return productModifierList;
    }

    public void setProductModifierList(String productModifierList) {
        this.productModifierList = productModifierList;
    }

    public String getProductOptionList() {
        return productOptionList;
    }

    public void setProductOptionList(String productOptionList) {
        this.productOptionList = productOptionList;
    }

    public String getProductAllergensList() {
        return productAllergensList;
    }

    public void setProductAllergensList(String productAllergensList) {
        this.productAllergensList = productAllergensList;
    }

    public int getMinWeightRange() {
        return minWeightRange;
    }

    public void setMinWeightRange(int minWeightRange) {
        this.minWeightRange = minWeightRange;
    }

    public int getMaxWeightRange() {
        return maxWeightRange;
    }

    public void setMaxWeightRange(int maxWeightRange) {
        this.maxWeightRange = maxWeightRange;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

}