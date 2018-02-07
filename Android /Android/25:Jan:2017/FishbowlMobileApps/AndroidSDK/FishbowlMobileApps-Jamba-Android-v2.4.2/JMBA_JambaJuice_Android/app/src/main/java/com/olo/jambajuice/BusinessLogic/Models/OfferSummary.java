package com.olo.jambajuice.BusinessLogic.Models;

import com.google.zxing.common.StringUtils;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;


/**
 * Created by Digvijay Chauhan on 3/3/16.
 */
public class OfferSummary  {
    String offerDescription;
    String offerTitle;
    Boolean ispmOffer;
    Integer   pmPromotionID ;
    Integer offerID;
    Integer channelTypeID;
    String  htmlBody;
    public static ArrayList<OfferItem> offerList;
    ArrayList<OfferItem> resultOfferList = new ArrayList<>();
    private int offerCount;
    String validityEndDateTime;
    String passPreviewImageURL ;
    String promotionCode;
    String passCustomStripUrl ;
    String customLogoURL ;
    String  notificationContent;
    Integer onlineInStore;
    Integer id;
    private ArrayList<OfferAvailableStore> availableStoresList;
  //  ProgressBarHandler p;

    public void setOfferCount(int offerCount)
    {
        this.offerCount = offerCount;
    }

    public OfferSummary(JSONObject response)
    {
        try
        {
            if(response.has("successFlag")
                    && response.getBoolean("successFlag")
                    && response.has("inAppOfferList")
                    && !response.isNull("inAppOfferList"))
            {
                JSONArray jsonArray	 = response.getJSONArray("inAppOfferList");
                offerList = new ArrayList<OfferItem>();
                if(jsonArray != null && jsonArray.length() > 0)
                {
                   // offerCount= jsonArray.length();
                    for( int i=0; i<jsonArray.length(); i++) {
                        JSONObject wallObj = jsonArray.getJSONObject(i);

                        if (wallObj != null) {
                            OfferItem offeritem = new OfferItem();

                            if (wallObj.has("campaignTitle")) {
                                offerTitle = (String) wallObj.get("campaignTitle");
                                offeritem.setOfferIItem(offerTitle);
                            }

                            if (wallObj.has("id")) {
                                id = (Integer) wallObj.get("id");
                                offeritem.setId(id);
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

                            if (wallObj.has("passCustomStripUrl")) {
                                passCustomStripUrl = (String) wallObj.get("passCustomStripUrl");
                                offeritem.setpassCustomStripUrl(passCustomStripUrl);
                            }

                            if (wallObj.has("customLogoURL")) {
                                customLogoURL = (String) wallObj.get("customLogoURL");
                                offeritem.setcustomLogoURL(customLogoURL);
                            }

                            if (wallObj.has("campaignId")) {
                                offerID = (Integer) wallObj.get("campaignId");
                                offeritem.setOfferId(offerID.toString());
                            }

                            if (wallObj.has("channelTypeID")) {
                                channelTypeID = (Integer) wallObj.get("channelTypeID");
                                offeritem.setChannelID(channelTypeID);
                            }

                            if (wallObj.has("promotionID")) {
                                pmPromotionID = (Integer) wallObj.get("promotionID");
                                offeritem.setPmPromotionID(pmPromotionID);
                            }

                            if (wallObj.has("htmlBody")) {
                                htmlBody = (String) wallObj.get("htmlBody");
                                offeritem.setHtmlBody(htmlBody);
                            }

                            if (wallObj.has("promotionCode")) {
                                promotionCode = (String) wallObj.get("promotionCode");
                                offeritem.setPromotioncode(promotionCode);
                            }
                            if (wallObj.has("notificationContent")) {
                                notificationContent = (String) wallObj.get("notificationContent");
                                offeritem.setNotificationContent(notificationContent);
                            }

                            if (wallObj.has("onlineinStore")) {
                                onlineInStore = Integer.parseInt(wallObj.getString("onlineinStore"));
                                offeritem.setOnlineInStore(onlineInStore);
                            }

                            if (wallObj.has("storeRestriction")
                                    && !wallObj.isNull("storeRestriction")) {
                                JSONArray storeJsonArray = wallObj.getJSONArray("storeRestriction");
                                availableStoresList = new ArrayList<OfferAvailableStore>();
                                if (storeJsonArray != null && storeJsonArray.length() > 0) {
                                    for (int j = 0; j < storeJsonArray.length(); j++) {
                                        JSONObject obj = storeJsonArray.getJSONObject(j);
                                        OfferAvailableStore offerAvailableStore = null;
                                        if (obj != null) {
                                            String storecode = (obj.has("storecode") && !obj.isNull("storecode")) ? (String) obj.get("storecode") : "";
                                            String storename = (obj.has("storename") && !obj.isNull("storename")) ? (String) obj.get("storename") : "";
                                            int storeid = (obj.has("storeid") && !obj.isNull("storeid")) ? (Integer) obj.get("storeid") : 0;
                                            String displayName = "";
                                            if (obj.has("displayname")
                                                    && !obj.isNull("displayname")) {
                                                displayName = (String) obj.get("displayname");
                                            } else {
                                                displayName = "";
                                            }
                                            //String displayName = (obj.has("displayname") && obj.get("displayname")!= null) ? (String)obj.get("displayname") : "";
                                            offerAvailableStore = new OfferAvailableStore(storecode, storename, storeid, displayName);
                                        }
                                        availableStoresList.add(offerAvailableStore);
                                    }
                                }
                                offeritem.setAvailableStoresList(availableStoresList);
                            }
                            int expire = 0;
                            if (StringUtilities.isValidString(validityEndDateTime)) {
                                Date d2 = Utils.getDateFromString(validityEndDateTime, null);
                                if (d2 == null) {
                                    d2 = Utils.getDateFromStringSlash(validityEndDateTime, null);
                                }
                                expire = Utils.daysBetween(new Date(), d2);
                            }
                            if (expire >= 0) {
                                if (StringUtilities.isValidString(offeritem.getPromotioncode())) {
//                                  //  if (offeritem.getOnlineInStore() == 1 || offeritem.getOnlineInStore() == 2) {
                                        offerList.add(offeritem);
//                                  //  }

                                }

                            }
                        }
                    }
                    if(offerList != null && offerList.size() > 0) {
                        for (int i = 0; i < offerList.size(); i++) {
                            if (offerList.get(i).getPmPromotionID() != 0) {
                                resultOfferList.add(offerList.get(i));
                                break;
                            }
                        }
                        int flag = 0;
                        for (int i = 1; i < offerList.size(); i++) {
                            flag = -1;
                            for (int j = 0; j < resultOfferList.size(); j++) {
                                if (offerList.get(i).getPmPromotionID() == resultOfferList.get(j).getPmPromotionID()) {
                                    if (offerList.get(i).getId() < resultOfferList.get(j).getId()) {
                                        resultOfferList.set(j, offerList.get(i));
                                    }
                                    flag = j;
                                    break;
                                }
                            }
                            if (flag < 0) {
                                if(offerList.get(i).getPmPromotionID() != 0) {
                                    resultOfferList.add(offerList.get(i));
                                }
                            }
                        }
                    }
                    setOfferCount(resultOfferList.size());
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ArrayList<OfferAvailableStore> getAvailableStoresList() {
        return availableStoresList;
    }

    public ArrayList<OfferItem> getOfferList()
    {
        return resultOfferList;
    }



    public int getOfferCount()
    {
        return offerCount;
    }
}


