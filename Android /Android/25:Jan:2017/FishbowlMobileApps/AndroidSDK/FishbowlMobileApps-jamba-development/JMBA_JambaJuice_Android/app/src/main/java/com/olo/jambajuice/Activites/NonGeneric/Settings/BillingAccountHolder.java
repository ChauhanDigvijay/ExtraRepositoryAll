package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.olo.jambajuice.Activites.NonGeneric.Settings.BillingAccountsActivity;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.R;


/**
 * Created by Nauman Afzaal on 18/06/15.
 */
public class BillingAccountHolder implements View.OnClickListener,SwipeLayout.SwipeListener {
    BillingAccountsActivity activity;
    BillingAccount billingAccount;
    ImageView img_selected;
    ImageView cardImg;
    RelativeLayout saveCardsRootLayout;
    private SwipeLayout swipe;

    public BillingAccountHolder(BillingAccountsActivity activity, View view, BillingAccount billingAccount) {
        this.activity = activity;
        this.billingAccount = billingAccount;
        swipe = (SwipeLayout) view.findViewById(R.id.swipe);
        LinearLayout delLayout = (LinearLayout) view.findViewById(R.id.delLayout);
        saveCardsRootLayout = (RelativeLayout) view.findViewById(R.id.saveCardsRootLayout);
        TextView detail = (TextView) view.findViewById(R.id.tv_detail);
        cardImg = (ImageView) view.findViewById(R.id.tv_type_img);
        cardImg.setVisibility(View.VISIBLE);
        detail.setText(billingAccount.getCardSuffix());
        swipe.setClickToClose(true);
        swipe.addSwipeListener(this);
        setCardImg();
        delLayout.setOnClickListener(this);
    }

    public void setCardImg() {
        if (billingAccount.getCardtype().equalsIgnoreCase("Visa")) {
            cardImg.setImageResource(R.drawable.visa_card_icon);
        } else if (billingAccount.getCardtype().equalsIgnoreCase("Mastercard")) {
            cardImg.setImageResource(R.drawable.master_card_icon);
        } else if (billingAccount.getCardtype().equalsIgnoreCase("Amex")) {
            cardImg.setImageResource(R.drawable.american_exp_icon);
        } else if (billingAccount.getCardtype().equalsIgnoreCase("Discover")) {
            cardImg.setImageResource(R.drawable.discover_icon);
        } else if (billingAccount.getCardtype().equalsIgnoreCase("JambaCard")) {
            cardImg.setImageResource(R.drawable.jambacard);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delLayout) {
            activity.showDeleteCardConfirmationAlert(billingAccount);
        }
    }


    private void showHideDeleteOption() {
        if (swipe.getShowMode() == SwipeLayout.ShowMode.PullOut) {
            swipe.open(true);
        }
    }

    public void resetSwipe() {
        swipe.setSelected(false);
    }

    public void closeSwipe() {
        swipe.close(true);
    }

    @Override
    public void onStartOpen(SwipeLayout layout) {
        layout.setSelected(true);
    }

    @Override
    public void onOpen(SwipeLayout layout) {
    }

    @Override
    public void onStartClose(SwipeLayout layout) {

    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.setSelected(false);
    }

    @Override
    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

    }

    @Override
    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

    }
}
