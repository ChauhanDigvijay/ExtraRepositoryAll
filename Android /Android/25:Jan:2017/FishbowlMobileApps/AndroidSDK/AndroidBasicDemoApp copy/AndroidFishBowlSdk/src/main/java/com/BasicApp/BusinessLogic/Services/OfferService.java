//package com.BasicApp.BusinessLogic.Services;
//
//import android.app.Activity;
//
//import com.fishbowl.basicmodule.Models.FBOfferSummaryItem;
//import com.fishbowl.basicmodule.Interfaces.OfferSummaryCallback;
//import com.fishbowl.basicmodule.Services.FBUserOfferService;
//
//import org.json.JSONObject;
//
//public class OfferService
//{
//    public static FBOfferSummaryItem FBOfferSummaryItem = null;
//    public static void getUserOffer(Activity activity, final OfferSummaryCallback callback)
//    {
//
//
//        JSONObject data = new JSONObject();
//        FBUserOfferService.sharedInstance().getUserFBOffer(data, " ", new FBUserOfferService.FBOfferCallback() {
//            @Override
//            public void OnFBOfferCallback(JSONObject response, Exception error) {
//
//                FBOfferSummaryItem = new FBOfferSummaryItem(response);
//
//
//                callback.onOfferSummaryCallback(FBOfferSummaryItem, error);
//
//            }
//        });
//    }
//}
