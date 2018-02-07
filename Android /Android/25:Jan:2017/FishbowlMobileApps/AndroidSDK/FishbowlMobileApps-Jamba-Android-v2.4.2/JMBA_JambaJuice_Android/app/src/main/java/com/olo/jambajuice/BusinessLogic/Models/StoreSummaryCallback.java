package com.olo.jambajuice.BusinessLogic.Models;

import java.util.Map;

/**
 * Created by digvijaychauhan on 13/05/16.
 */
public interface StoreSummaryCallback {
    public void onStoreSummaryCallback(Map<Integer, Integer> storesMapforId, String error);
}
