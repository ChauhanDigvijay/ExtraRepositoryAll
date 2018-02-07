package com.wearehathway.apps.incomm.Interfaces;

import com.wearehathway.apps.incomm.Models.InCommCardTransactionHistory;
import com.wearehathway.apps.incomm.Models.InCommTransactionHistory;

import java.util.ArrayList;

/**
 * Created by vt010 on 9/16/16.
 */
public interface InCommTransactionHistoryCallback {
    public void onTransactionHistoryServiceCallback(InCommCardTransactionHistory accountList, Exception exception);
}
