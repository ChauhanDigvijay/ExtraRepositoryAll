package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;

import java.util.ArrayList;

/**
 * Created by vthink on 20/01/17.
 */

public interface ProductAdDetailsServiceCallback {
    public void onProductAdDetailsCallback(ArrayList<ProductAdDetail> productAds, Exception exception);
}
