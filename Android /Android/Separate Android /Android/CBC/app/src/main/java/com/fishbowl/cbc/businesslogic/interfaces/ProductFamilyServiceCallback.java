package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.ProductFamily;

import java.util.List;

/**
 * Created by VT027 on 5/22/2017.
 */

public interface ProductFamilyServiceCallback {
    public void onProductFamilyCallback(List<ProductFamily> products, Exception exception);
}
