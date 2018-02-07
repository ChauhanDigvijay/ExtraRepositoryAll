package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;

import java.util.List;

/**
 * Created by Nauman on 15/05/15.
 */
public interface ProductServiceCallback {
    public void onProductServiceCallback(List<Product> products, ProductCategory productCategory, Exception exception);
}
