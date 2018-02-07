package com.olo.jambajuice.BusinessLogic.Models;

import com.olo.jambajuice.Utils.StringUtilities;
import com.wearehathway.apps.olo.Models.OloBillingAccount;

/**
 * Created by Nauman Afzaal on 18/06/15.
 */
public class BillingAccount
{

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

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
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
            cardSuffixx =  cardSuffix;
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
