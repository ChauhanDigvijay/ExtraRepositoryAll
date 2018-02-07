package com.fishbowl.apps.olo.Models;

/**
 * Created by Ihsanulhaq on 5/29/2015.
 */
public class OloBillingAccount {

    private int accountid;
    private String accounttype;
    private String cardtype;
    private String cardsuffix;
    private String description;
    private String expiration;

    public int getAccountid() {
        return accountid;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public String getCardtype() {
        return cardtype;
    }

    public String getCardsuffix() {
        return cardsuffix;
    }

    public String getDescription() {
        return description;
    }

    public String getExpiration() {
        return expiration;
    }
}
