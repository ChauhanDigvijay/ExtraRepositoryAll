package com.BasicApp.BusinessLogic.Models;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class OfferSummary {
    String offerDescription;
    String offerTitle;
    Boolean ispmOffer;
    Integer pmPromotionID ;
    Integer offerID;
    Integer channelID;
    public static ArrayList<OfferItem> offerList;
    private int offerCount;
    String validityEndDateTime;
    String passPreviewImageURL ;
    String htmlBody;
    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public OfferSummary(JSONObject response)
    {
        try
        {

            if(response.has("inAppOfferList"))
            {

                JSONArray jsonArray	 = response.getJSONArray("inAppOfferList");
                offerList = new ArrayList<OfferItem>();
                if(jsonArray != null && jsonArray.length() > 0)
                {

                    offerCount= jsonArray.length();
                    for( int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject wallObj = jsonArray.getJSONObject(i);
                        if(wallObj != null) {
                            OfferItem offeritem = new OfferItem();
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
                            if (wallObj.has("htmlBody")) {
                                htmlBody =  (String)wallObj.get("htmlBody");
                                offeritem.setHtmlBody(htmlBody);

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



    public ArrayList<OfferItem> getOfferList() {
        return offerList;
    }
    public int getOfferCount() {
        return offerCount;
    }

}

