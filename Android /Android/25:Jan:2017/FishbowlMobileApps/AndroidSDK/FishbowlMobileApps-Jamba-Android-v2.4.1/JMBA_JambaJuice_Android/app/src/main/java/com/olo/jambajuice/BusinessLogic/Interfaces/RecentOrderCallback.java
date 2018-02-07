package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;

import java.util.List;

/**
 * Created by Ihsanulhaq on 18/6/2015.
 */
public interface RecentOrderCallback {
    public void onOrderCallback(List<RecentOrder> status, Exception e);
}
