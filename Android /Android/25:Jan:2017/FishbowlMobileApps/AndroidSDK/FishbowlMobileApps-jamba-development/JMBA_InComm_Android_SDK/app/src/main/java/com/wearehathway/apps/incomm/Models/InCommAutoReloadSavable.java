package com.wearehathway.apps.incomm.Models;

import java.util.Date;

/**
 * Created by vthink on 05/09/16.
 */
public class InCommAutoReloadSavable {
    private double Amount;
    private Date CreatedOn;
    private String EndsOn;
    private int GiftCardId;
    private int Id;
    private Boolean IsActive;
    private String LastErrorMessage;
    private int LastModifiedByPortalUserId;
    private Date LastModifiedBySystemOn;
    private Date LastModifiedByUserOn;
    private double MinimumBalance;
    private Date NextReloadOn;
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

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        CreatedOn = createdOn;
    }



    public int getGiftCardId() {
        return GiftCardId;
    }

    public void setGiftCardId(int giftCardId) {
        GiftCardId = giftCardId;
    }

    public String getEndsOn() {
        return EndsOn;
    }

    public void setEndsOn(String endsOn) {
        EndsOn = endsOn;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    public String getLastErrorMessage() {
        return LastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        LastErrorMessage = lastErrorMessage;
    }

    public int getLastModifiedByPortalUserId() {
        return LastModifiedByPortalUserId;
    }

    public void setLastModifiedByPortalUserId(int lastModifiedByPortalUserId) {
        LastModifiedByPortalUserId = lastModifiedByPortalUserId;
    }

    public Date getLastModifiedBySystemOn() {
        return LastModifiedBySystemOn;
    }

    public void setLastModifiedBySystemOn(Date lastModifiedBySystemOn) {
        LastModifiedBySystemOn = lastModifiedBySystemOn;
    }

    public Date getLastModifiedByUserOn() {
        return LastModifiedByUserOn;
    }

    public void setLastModifiedByUserOn(Date lastModifiedByUserOn) {
        LastModifiedByUserOn = lastModifiedByUserOn;
    }

    public double getMinimumBalance() {
        return MinimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        MinimumBalance = minimumBalance;
    }

    public Date getNextReloadOn() {
        return NextReloadOn;
    }

    public void setNextReloadOn(Date nextReloadOn) {
        NextReloadOn = nextReloadOn;
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
