package com.wearehathway.apps.incomm.Models;

import java.util.Date;

/**
 * Created by vthink on 05/09/16.
 */
public class InCommAutoReloadSubmitOrder {

    private double Amount;
    private String EndsOn;
    private int GiftCardId;
    private double MinimumBalance;
    private int NumberOfOccurancesRemaining;
    private int PaymentAccountId;
    private String StartsOn;
    private String ReloadFrequencyId;

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getEndsOn() {
        return EndsOn;
    }

    public void setEndsOn(String endsOn) {
        EndsOn = endsOn;
    }

    public int getGiftCardId() {
        return GiftCardId;
    }

    public void setGiftCardId(int giftCardId) {
        GiftCardId = giftCardId;
    }

    public double getMinimumBalance() {
        return MinimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        MinimumBalance = minimumBalance;
    }

    public int getNumberOfOccurancesRemaining() {
        return NumberOfOccurancesRemaining;
    }

    public void setNumberOfOccurancesRemaining(int numberOfOccurancesRemaining) {
        NumberOfOccurancesRemaining = numberOfOccurancesRemaining;
    }

    public int getPaymentAccountId() {
        return PaymentAccountId;
    }

    public void setPaymentAccountId(int paymentAccountId) {
        PaymentAccountId = paymentAccountId;
    }

    public String getStartsOn() {
        return StartsOn;
    }

    public void setStartsOn(String startsOn) {
        StartsOn = startsOn;
    }

    public String getReloadFrequencyId() {
        return ReloadFrequencyId;
    }

    public void setReloadFrequencyId(String reloadFrequencyId) {
        ReloadFrequencyId = reloadFrequencyId;
    }
}
