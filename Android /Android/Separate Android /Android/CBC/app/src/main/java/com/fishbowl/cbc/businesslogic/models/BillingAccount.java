package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloBillingAccount;
import com.fishbowl.basicmodule.Utils.StringUtilities;

/**
 * Created by VT027 on 5/23/2017.
 */

public class BillingAccount {
    private int accountid;
    private String accounttype;
    private String cardtype;
    private String cardSuffix;
    private String description;
    private String expiration;

    public BillingAccount(OloBillingAccount oloBillingAccount)
    {
        accountid = oloBillingAccount.getAccountid();
        cardtype = oloBillingAccount.getCardtype();
        cardSuffix = oloBillingAccount.getCardsuffix();
        accounttype = oloBillingAccount.getAccounttype();
        description = oloBillingAccount.getDescription();
        expiration = oloBillingAccount.getExpiration();
    }

    public int getAccountid()
    {
        return accountid;
    }

    public String getCardtype()
    {
        String cardtypee = "";
        if(StringUtilities.isValidString(cardtype)){
            cardtypee = cardtype;
        }else if(accounttype.equalsIgnoreCase("payinstore")){
            cardtypee = "Pay Restaurant Directly";
        }
        return cardtypee;
    }

    public String getCardSuffix()
    {
        String cardSuffixx = "";
        if(StringUtilities.isValidString(cardSuffix)) {
            cardSuffixx = "XXXX XXXX XXXX " + cardSuffix;
        }else if(accounttype.equalsIgnoreCase("payinstore")){
            cardSuffixx = "payinstore";

        }
        return cardSuffixx;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }
}
