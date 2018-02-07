package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.Product;
import com.fishbowl.cbc.businesslogic.models.ProductCategory;

import java.util.List;

/**
 * Created by VT027 on 5/22/2017.
 */

public interface ProductServiceCallback {
    public void onProductServiceCallback(List<Product> products, ProductCategory productCategory, Exception exception);
}
