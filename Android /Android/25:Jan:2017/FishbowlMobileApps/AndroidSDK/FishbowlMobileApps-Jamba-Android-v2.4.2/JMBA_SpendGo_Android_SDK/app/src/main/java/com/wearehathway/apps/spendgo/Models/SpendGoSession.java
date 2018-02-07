package com.wearehathway.apps.spendgo.Models;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public class SpendGoSession
{
    private String spendgo_id;
    private String auth_token;

    public SpendGoSession()
    {
        clearSession();
    }

    public String getSpendGoId()
    {
        return spendgo_id;
    }

    public String getAuthToken()
    {
        return auth_token;
    }

    public void setAuthToken(String authToken)
    {
        this.auth_token = authToken;
    }

    public void setSpendgo_id(String spendgo_id)
    {
        this.spendgo_id = spendgo_id;
    }

    public void clearSession()
    {
        spendgo_id = null;
        auth_token = null;
    }
}
