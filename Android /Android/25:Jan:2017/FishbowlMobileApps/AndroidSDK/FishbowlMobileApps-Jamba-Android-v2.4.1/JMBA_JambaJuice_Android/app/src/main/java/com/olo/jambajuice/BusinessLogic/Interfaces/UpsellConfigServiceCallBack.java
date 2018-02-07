package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.UpsellConfig;

import java.util.ArrayList;

/**
 * Created by vt021 on 21/10/17.
 */

public interface UpsellConfigServiceCallBack {
    public void onUpSellConfigServiceCallBack(ArrayList<UpsellConfig> upSellConfigs, Exception exception);
}
