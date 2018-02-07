package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.UpSell;
import com.olo.jambajuice.BusinessLogic.Models.UpsellConfig;

import java.util.ArrayList;

/**
 * Created by vt021 on 21/10/17.
 */

public interface UpSellServiceCallBack {
    public void onUpSellServiceCallBack(ArrayList<UpSell> upSells, Exception exception);
}
