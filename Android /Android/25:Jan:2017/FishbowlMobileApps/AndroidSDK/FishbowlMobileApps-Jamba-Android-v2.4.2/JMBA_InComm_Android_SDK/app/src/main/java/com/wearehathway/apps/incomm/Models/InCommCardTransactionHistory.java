package com.wearehathway.apps.incomm.Models;

import java.util.Date;
import java.util.List;

/**
 * Created by vt010 on 9/16/16.
 */
public class InCommCardTransactionHistory {
    private Date TransactionHistoryDate;
    private List<InCommTransactionHistory> TransactionHistory;

    public Date getTransactionHistoryDate() {
        return TransactionHistoryDate;
    }

    public void setTransactionHistoryDate(Date transactionHistoryDate) {
        TransactionHistoryDate = transactionHistoryDate;
    }

    public List<InCommTransactionHistory> getInCommTransactionHistory() {
        return TransactionHistory;
    }

    public void setInCommTransactionHistory(List<InCommTransactionHistory> TransactionHistory) {
        this.TransactionHistory = TransactionHistory;
    }
}
