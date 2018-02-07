package com.olo.jambajuice.BusinessLogic.Interfaces;


import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;

import java.util.List;

/**
 * Created by Nauman on 15/05/15.
 */
public interface ProductCategoryServiceCallback {
    public void onProductCategoryCallback(List<ProductCategory> products, Exception exception);
}
