package com.olo.jambajuice.BusinessLogic.Models;

/**
 * Created by vt010 on 10/22/16.
 */

public class ProductSearch {
    private Product product;
    private String productCategory;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
