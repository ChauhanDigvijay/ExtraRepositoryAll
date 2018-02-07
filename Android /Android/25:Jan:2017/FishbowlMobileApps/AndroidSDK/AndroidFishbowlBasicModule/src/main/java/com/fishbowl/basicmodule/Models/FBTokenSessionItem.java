package com.fishbowl.basicmodule.Models;

/**
 * Created by digvijaychauhan on 28/07/17.
 */

public class FBTokenSessionItem
{


    private String spendgo_id;
    private String auth_token;

    public String getSpendgo_id() {
        return spendgo_id;
    }

    public void setSpendgo_id(String spendgo_id) {
        this.spendgo_id = spendgo_id;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public FBTokenSessionItem()
    {
        clearSession();
    }
    public void clearSession()
    {
        spendgo_id = null;
        auth_token = null;
    }


}
