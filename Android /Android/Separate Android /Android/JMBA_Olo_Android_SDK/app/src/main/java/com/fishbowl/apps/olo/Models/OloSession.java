package com.fishbowl.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 28/04/15.
 */
public class OloSession
{
    private String authToken;

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public void clearSession()
    {
        authToken = null;
    }
}
