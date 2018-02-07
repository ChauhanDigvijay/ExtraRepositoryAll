package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.BasicApp.BusinessLogic.Interfaces.FBASessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Services.FBRewardService;

import java.lang.ref.WeakReference;


/**
 * Created by digvijay(dj)
 */
public class FBALoyaltyService {

    public static FBALoyaltyService instance=null;
    public static Activity mContext;
    public static FBALoyaltyService sharedInstance(Activity context){

        if(instance==null){
            instance=new FBALoyaltyService(context);
        }

        return  instance;
    }
    public FBALoyaltyService(Activity context)
    {
        if(context == null);
        mContext = context;
    }


    public void getOffer(FBASessionServiceCallback callback) {
        final WeakReference<FBASessionServiceCallback> callbackWeakReference = new WeakReference<FBASessionServiceCallback>(callback);
        FBRewardService.getUserFBOffer(new FBOfferCallback() {
            @Override
            public void onFBOfferCallback(FBOfferListItem fbsessionitem, Exception error) {
                if (fbsessionitem != null)

                {


                    FBASessionServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                       // cb.onUserServiceCallback(fbsessionitem, error);
                    }

                } else {

                }
            }
        });
    }

    private void getReward(FBASessionServiceCallback callback) {
        final WeakReference<FBASessionServiceCallback> callbackWeakReference = new WeakReference<FBASessionServiceCallback>(callback);
        FBRewardService.getUserFBReward(new FBOfferCallback() {

            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null) {

                    FBASessionServiceCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                      //  cb.onUserServiceCallback(fbsessionitem, error);
                    }

                } else {

                }
            }
        });
    }

}
