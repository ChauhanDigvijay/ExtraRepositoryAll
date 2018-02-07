package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.JambaOrderStatus;

/**
 * Created by Jeeva on 22-04-2016.
 */
public interface JambaUserOrderServiceCallBack {

    public void onUserOrderServiceCallBack(JambaOrderStatus[] jambaOrderStatuses, Exception e);
}
