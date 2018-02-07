package com.wearehathway.apps.incomm.Models;

import java.util.Date;

/**
 * Created by Ananad on 06-Sep-16.
 */
public class InCommTransactionHistory {
    private String TransactionDescription;
    private Date TransactionDate;
    private double TransactionAmount;
    private double CardBalance;

    public double getTransactionAmount() {
        return TransactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        TransactionAmount = transactionAmount;
    }

    private InCommTransactionType inCommTransactionType;

    public InCommTransactionType getInCommTransactionType() {
        return inCommTransactionType;
    }

    public void setInCommTransactionType(InCommTransactionType inCommTransactionType) {
        this.inCommTransactionType = inCommTransactionType;
    }

    public double getCardBalance() {
        return CardBalance;
    }

    public void setCardBalance(double cardBalance) {
        CardBalance = cardBalance;
    }

    public String getTransactionDescription() {
        return TransactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        TransactionDescription = transactionDescription;
    }

    public Date getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        TransactionDate = transactionDate;
    }

}
