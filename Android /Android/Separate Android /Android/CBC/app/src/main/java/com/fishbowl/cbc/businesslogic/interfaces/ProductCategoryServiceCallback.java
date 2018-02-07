package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.ProductCategory;

import java.util.List;

/**
 * Created by VT027 on 5/22/2017.
 */

public interface ProductCategoryServiceCallback {
    public void onProductCategoryCallback(List<ProductCategory> products, Exception exception);
}
