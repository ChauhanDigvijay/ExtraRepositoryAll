package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 07-Feb-17.
 */

public class GetAllRewardOfferPointBank implements Serializable {


    public int loyaltyPoints,tenantId,offerId;
    public JSONObject fishBowlPromotion;
public  String publicname;




    public GetAllRewardOfferPointBank(JSONObject jsonObj){

        try {
            loyaltyPoints = jsonObj.has("loyaltyPoints") ? jsonObj.getInt("loyaltyPoints") : 0;
            tenantId = jsonObj.has("tenantId") ? jsonObj.getInt("tenantId") : 0;
            offerId = jsonObj.has("offerId") ? jsonObj.getInt("offerId") : 0;

            fishBowlPromotion = jsonObj.has("fishBowlPromotion") ? jsonObj.getJSONObject("fishBowlPromotion"):null;
            publicname = fishBowlPromotion.has("name")?fishBowlPromotion.getString("name"):null;


        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void setPublicname(String publicname)
    {
        this.publicname = publicname;
    }
    public String getPublicname()
    {
        return publicname;
    }

    public void setTenantId(int tenantId)
    {
        this.tenantId = tenantId;
    }
    public int getTenantId()
    {
        return tenantId;
    }


    public void setOfferId(int offerId)
    {
        this.offerId = offerId;
    }
    public int getOfferId()
    {
        return offerId;
    }




    public int getLoyaltyPoints()
    {
        return loyaltyPoints;
    }
    public void setLoyaltyPoints(int loyaltyPoints)
    {
        this.loyaltyPoints = loyaltyPoints;
    }

}