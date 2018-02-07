package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductFamily;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauman on 15/05/15.
 */
public interface ProductFamilyServiceCallback {
    public void onProductFamilyCallback(List<ProductFamily> products, Exception exception);
}
