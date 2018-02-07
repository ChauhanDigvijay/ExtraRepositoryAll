package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.BasicApp.BusinessLogic.Interfaces.FBAOfferCallback;
import com.BasicApp.BusinessLogic.Interfaces.FBARewardPointCallback;
import com.BasicApp.Utils.FBUtils;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Interfaces.FBBonusRuleListCallback;
import com.fishbowl.basicmodule.Interfaces.FBOfferCallback;
import com.fishbowl.basicmodule.Interfaces.FBRewardCallback;
import com.fishbowl.basicmodule.Interfaces.FBRewardPointCallback;
import com.fishbowl.basicmodule.Models.FBBonusItem;
import com.fishbowl.basicmodule.Models.FBOfferListItem;
import com.fishbowl.basicmodule.Models.FBRewardListItem;
import com.fishbowl.basicmodule.Models.FBRewardPointDetailItem;
import com.fishbowl.basicmodule.Services.FBRewardService;

import java.lang.ref.WeakReference;


/**
 * Created by digvijay(dj)
 */
public class FBARewardService {

    public static FBARewardService instance = null;
    public static Activity mContext;

    public FBARewardService(Activity context) {
        if (context == null) ;
        mContext = context;
    }

    public static FBARewardService sharedInstance(Activity context) {

        if (instance == null) {
            instance = new FBARewardService(context);
        }

        return instance;
    }

    public void getOffer(final FBAOfferCallback callback) {
        final WeakReference<FBAOfferCallback> callbackWeakReference = new WeakReference<FBAOfferCallback>(callback);
        FBRewardService.getUserFBOffer(new FBOfferCallback() {
            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null)

                {
                    int offercount = response.getCategories().length;
                    FBPreferences.sharedInstance(mContext).setOfferCount(offercount);

                    getReward(callback);
                } else {

                    getReward(callback);
                }
            }
        });
    }

    private void getReward(final FBAOfferCallback callback) {
        final WeakReference<FBAOfferCallback> callbackWeakReference = new WeakReference<FBAOfferCallback>(callback);
        FBRewardService.getUserFBReward(new FBOfferCallback() {

            @Override
            public void onFBOfferCallback(FBOfferListItem response, Exception error) {
                if (response != null) {
                    int rewardcount = response.getCategories().length;
                    FBPreferences.sharedInstance(mContext).setRewardCount(rewardcount);
                    FBAOfferCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                         cb.onFBOfferCallback(response, error);
                    }

                } else {
                    FBUtils.tryHandleTokenExpiry(mContext, error);
                }
            }
        });
    }

    public void fetchRewardPoint(FBARewardPointCallback callback) {

        final WeakReference<FBARewardPointCallback> callbackWeakReference = new WeakReference<FBARewardPointCallback>(callback);
        FBRewardService.getUserFBRewardPoint(new FBRewardPointCallback() {
            @Override
            public void onFBRewardPointCallback(FBRewardPointDetailItem response, Exception error) {
                if (response != null) {


                    FBARewardPointCallback cb = callbackWeakReference.get();
                    if (cb != null && !((Activity) cb).isFinishing()) {
                        cb.onFBRewardPointCallback(response, error);
                    }
                } else {
                    FBUtils.tryHandleTokenExpiry(mContext, error);
                }
            }
        });
    }


    public void getUserFBPointBankOffer() {

        FBRewardService.getUserFBPointBankOffer(new FBRewardCallback() {
            @Override
            public void onFBOfferCallback(FBRewardListItem restaurants, Exception error) {
                if (restaurants != null)

                {


                } else {

                }
            }
        });
    }

    public void useOffer(String offerId, String claimPoints) {

        FBRewardService.useOffer(offerId, claimPoints, new FBRewardCallback() {
            @Override
            public void onFBOfferCallback(FBRewardListItem restaurants, Exception error) {
                if (restaurants != null)

                {


                } else {

                }
            }
        });
    }

    public void getBonusRuleList() {

        FBRewardService.getBonusRuleList(new FBBonusRuleListCallback() {
            @Override
            public void onBonusRuleListCallback(FBBonusItem restaurants, Exception error) {
                if (restaurants != null)

                {


                } else {

                }
            }
        });
    }


}
