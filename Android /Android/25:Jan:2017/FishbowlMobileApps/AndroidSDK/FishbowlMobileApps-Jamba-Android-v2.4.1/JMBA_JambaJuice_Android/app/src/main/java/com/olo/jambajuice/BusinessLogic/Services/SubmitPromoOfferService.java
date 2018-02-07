package com.olo.jambajuice.BusinessLogic.Services;

import com.fishbowl.basicmodule.Interfaces.FBOrderValuesCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.SubmitPromoOfferServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.JambaApplication;
import com.wearehathway.apps.incomm.Models.InCommOrderItem;
import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;
import com.wearehathway.apps.incomm.Models.InCommOrderRecipientDetails;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by vthink on 24/10/16.
 */

public class SubmitPromoOfferService {

    public static void submitPromoOffer(final SubmitPromoOfferServiceCallback callback) {

        JambaApplication _app = JambaApplication.getAppContext();

        _app.fbsdkObj.Ordervalue(getJsonObject(), new FBOrderValuesCallback() {
            @Override
            public void OnFBOrderValuesCallback(JSONObject jsonObject, Exception e) {
                callback.onSubmitPromoOfferServiceCallback(jsonObject, e.getMessage());
            }
        });
    }

    public static JSONObject getJsonObject() {

        JSONObject incommInput = new JSONObject();
        try {
            incommInput.put("key", "jambamobile");
            incommInput.put("authorization", UserService.getUser().getSpendGoAuthToken());
            incommInput.put("spendgoId", UserService.getUser().getSpendGoId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject queryInput = new JSONObject();
        try {
            InCommOrderPurchaser inCommOrderPurchaser = GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo();
            queryInput.put("amount", GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getAmount());
            queryInput.put("messageTo", inCommOrderPurchaser.getFirstName() + " " + inCommOrderPurchaser.getLastName());
            queryInput.put("incommInput", incommInput);
            queryInput.put("incommToken", DataManager.getInstance().getInCommToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject paymentInput = new JSONObject();
        try {
            paymentInput.put("OrderPaymentMethod", "NoFundsCollected");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject purchaserInput = new JSONObject();
        try {
            purchaserInput.put("EmailAddress", GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
            purchaserInput.put("FirstName", GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName());
            purchaserInput.put("LastName", GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getLastName());
            purchaserInput.put("Country", GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getCountry());
            purchaserInput.put("SuppressReceiptEmail", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray recipientArray = new JSONArray();
        JSONObject recipientInput = new JSONObject();
        try {
            recipientInput.put("EmailAddress", GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
            recipientInput.put("FirstName", GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getFirstName());
            recipientInput.put("LastName", GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getLastName());
            recipientInput.put("Country", "US");
            recipientArray.put(recipientInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject resultInput = new JSONObject();
        try {
            resultInput.put("Payment", paymentInput);
            resultInput.put("Purchaser", purchaserInput);
            resultInput.put("Recipients", recipientArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject mainJson = new JSONObject();
        try {
            mainJson.put("query", queryInput);
            mainJson.put("result", resultInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mainJson;
    }
}
