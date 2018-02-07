package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vthink on 12/01/17.
 */

public interface ProductAdsServiceCallback {
    public void onProductAdsCallback(ArrayList<ProductAd> productAds, Exception exception);
}
