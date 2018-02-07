package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Promotions;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.PassdetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.BasketRewardsAndOffersActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

/**
 * Created by Ihsanulhaq on 6/8/2015.
 */
public class PromotionalCodeActivity extends BaseActivity implements View.OnClickListener {
    Button continueBtn;
    String offermessage;
    String promocode;
    private EditText code;
    private static int prevStatus = 0;
    private boolean isFormatting;
    private boolean deletingHyphen;
    private int hyphenStart;
    private boolean deletingBackward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotional_code);
        isShowBasketIcon = false;
        initComponents();
        initToolbar();
        //   fetchUserOffersPromo("295");
    }


    @Override
    public void onClick(View v) {
        if (!StringUtilities.isValidString(code.getText().toString())) {
            code.setError("Promotional code cannot be empty");
        } else {
            trackButton(v);
            if(DataManager.getInstance().getCurrentBasket() != null && DataManager.getInstance().getCurrentBasket().getAppliedRewards() != null) {
                if (DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0
                        || StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {
                    confirmationAlert(Constants.APPLY_REWARD_OR_APPLY_COUPON_MESSAGE);
                } else {
                    applyCouponToBasket();
                }
            }
        }
    }


    private void initComponents() {
        code = (EditText) findViewById(R.id.code);
        continueBtn = (Button) findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(this);
    }

    private void applyCouponToBasket() {
        enableScreen(false);
        final String promotionCode = code.getText().toString();
        BasketService.applyCoupon(this, promotionCode, new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                enableScreen(true);
                if (e == null) {
                    DataManager.getInstance().getCurrentBasket().setPromotionCode(promotionCode);
                    //Navigate back to basket screen.
                    TransitionManager.transitFrom(PromotionalCodeActivity.this, BasketActivity.class, true);
                } else {
                    //postDeletionActions(e,basket);
                    Utils.showErrorAlert(PromotionalCodeActivity.this, e);
                    if(BasketRewardsAndOffersActivity.basketRewardsAndOffersActivity != null) {
                        BasketRewardsAndOffersActivity.basketRewardsAndOffersActivity.setUi();
                    }
                }
            }
        });
    }

    private void postDeletionActions(Exception e,Basket mBasket) {
        enableScreen(true);
        if (e != null) {
            if(Utils.getErrorCode(e) == 9){
                enableScreen(false);
                BasketService.refreshBasket(this, new BasketServiceCallback() {
                    @Override
                    public void onBasketServiceCallback(Basket basket, Exception e) {
                        enableScreen(true);
                        if(e == null){
                            if(basket.getDiscount() == 0) {
                                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                                DataManager.getInstance().getCurrentBasket().setPromoId(0);
                            }
                            TransitionManager.transitFrom(PromotionalCodeActivity.this, BasketActivity.class, true);
                            finish();
                        }else{
                            TransitionManager.transitFrom(PromotionalCodeActivity.this, BasketActivity.class, true);
                            finish();
                        }
                    }
                });
            }else{
                Utils.showErrorAlert(this, e);
            }
        } else {
//            if(mBasket.getDiscount() == 0) {
//                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
//                DataManager.getInstance().getCurrentBasket().setOfferId("");
//            }
            TransitionManager.transitFrom(PromotionalCodeActivity.this, BasketActivity.class, true);
            finish();
        }
    }

    private void confirmationAlert(String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(PromotionalCodeActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                applyCouponToBasket();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void initToolbar() {
        setUpToolBar(true);
        setTitle("Promotional Code");
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(android.R.color.white));
        setBackButton(true,true);
    }

    @Override
    public void enableScreen(boolean isEnabled) {
        super.enableScreen(isEnabled);
        continueBtn.setEnabled(isEnabled);
    }
}
