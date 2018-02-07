package com.fishbowl.basicmodule.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by digvijay(dj)
 */
public class FBOfferSummaryItem {
    String offerDescription;
    String offerTitle;
    Boolean ispmOffer;
    Integer   pmPromotionID ;
    Integer offerID;
    Integer channelID;
    public static ArrayList<FBOfferItem> offerList;
    private int offerCount;
    String validityEndDateTime;
    String passPreviewImageURL ;
    String promotionCode;
    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public FBOfferSummaryItem(JSONObject response)
    {
        try
        {

            if(response.has("inAppOfferList"))
            {

                JSONArray jsonArray	 = response.getJSONArray("inAppOfferList");
                offerList = new ArrayList<FBOfferItem>();
                if(jsonArray != null && jsonArray.length() > 0)
                {

                    offerCount= jsonArray.length();
                    for( int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject wallObj = jsonArray.getJSONObject(i);
                        if(wallObj != null) {
                            FBOfferItem offeritem = new FBOfferItem();
                            if (wallObj.has("campaignTitle")) {
                                offerTitle = (String) wallObj.get("campaignTitle");
                                offeritem.setOfferIItem(offerTitle);
                            }
                            if (wallObj.has("campaignDescription")) {
                                offerDescription = (String) wallObj.get("campaignDescription");
                                offeritem.setOfferIName(offerDescription);
                            }
                            if (wallObj.has("validityEndDateTime")) {
                                validityEndDateTime = (String) wallObj.get("validityEndDateTime");
                                offeritem.setDatetime(validityEndDateTime);
                            }
                            if (wallObj.has("isPMOffer")) {
                                ispmOffer = wallObj.getBoolean("isPMOffer");
                                offeritem.setPMOffer(ispmOffer);
                            }

                            if (wallObj.has("passPreviewImageURL")) {
                                passPreviewImageURL = (String) wallObj.get("passPreviewImageURL");
                                offeritem.setOfferIOther(passPreviewImageURL);
                            }
                            if (wallObj.has("campaignId")) {
                                offerID = (Integer) wallObj.get("campaignId");
                                offeritem.setOfferId(offerID.toString());
                            }
                            if (wallObj.has("channelID")) {
                                channelID = (Integer) wallObj.get("channelID");
                                offeritem.setChannelID(channelID);
                            }

                            if (wallObj.has("promotionID")) {
                                pmPromotionID = (Integer) wallObj.get("promotionID");
                                offeritem.setPmPromotionID(pmPromotionID);

                            }

                            if (wallObj.has("promotionCode")) {
                                promotionCode =  (String)wallObj.get("promotionCode");
                                offeritem.setPromotioncode(promotionCode);

                            }

                            offerList.add(offeritem);

                        }


                    }
                    setOfferCount(offerList.size());
                }

            }

        }

        catch(Exception ex){
            ex.printStackTrace();

        }
    }



    public ArrayList<FBOfferItem> getOfferList() {
        return offerList;
    }
    public int getOfferCount() {
        return offerCount;
    }

}

