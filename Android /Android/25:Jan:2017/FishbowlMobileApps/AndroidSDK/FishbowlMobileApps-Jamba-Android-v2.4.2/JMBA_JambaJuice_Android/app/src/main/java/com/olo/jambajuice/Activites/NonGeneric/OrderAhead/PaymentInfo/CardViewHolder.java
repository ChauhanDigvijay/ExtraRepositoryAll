package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.R;


/**
 * Created by Nauman Afzaal on 18/06/15.
 */
public class CardViewHolder implements View.OnClickListener, SwipeLayout.SwipeListener {
    SelectExistingCardActivity activity;
    BillingAccount billingAccount;
    ImageView img_selected;
    ImageView cardImg;
    RelativeLayout saveCardsRootLayout;
    private SwipeLayout swipe;
    TextView detail;

    public CardViewHolder(SelectExistingCardActivity activity, View view, BillingAccount billingAccount) {
        this.activity = activity;
        this.billingAccount = billingAccount;
        swipe = (SwipeLayout) view.findViewById(R.id.swipe);
        LinearLayout delLayout = (LinearLayout) view.findViewById(R.id.delLayout);
        saveCardsRootLayout = (RelativeLayout) view.findViewById(R.id.saveCardsRootLayout);
        detail = (TextView) view.findViewById(R.id.tv_detail);
        cardImg = (ImageView) view.findViewById(R.id.tv_type_img);
        img_selected = (ImageView) view.findViewById(R.id.img_selected);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) img_selected.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        img_selected.setLayoutParams(layoutParams);
        cardImg.setVisibility(View.VISIBLE);
        detail.setText(billingAccount.getCardSuffix());
        swipe.setClickToClose(true);
        swipe.addSwipeListener(this);
        setCardImg();
        setSelected(-1);
        view.setOnClickListener(this);
        delLayout.setOnClickListener(this);
    }

    public void setSelected(int accountId) {
        if (accountId == billingAccount.getAccountid()) {
            img_selected.setVisibility(View.VISIBLE);
            saveCardsRootLayout.setBackgroundColor(Color.WHITE);
            saveCardsRootLayout.setAlpha(1f);
            detail.setAlpha(1f);
            img_selected.setImageAlpha(255);
            cardImg.setImageAlpha(180);
        } else {
            img_selected.setVisibility(View.GONE);
            saveCardsRootLayout.setBackgroundColor(ContextCompat.getColor(activity,R.color.semi_transparent_white));
            saveCardsRootLayout.setAlpha(0.5f);
            detail.setAlpha(0.5f);
            cardImg.setImageAlpha(128);
        }

        if(accountId == -1){
            img_selected.setVisibility(View.GONE);
            saveCardsRootLayout.setBackgroundColor(Color.WHITE);
            detail.setAlpha(1f);
            saveCardsRootLayout.setAlpha(1f);
            cardImg.setImageAlpha(255);
        }
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
        } else if (billingAccount.getCardtype().equalsIgnoreCase("Jamba Cards")) {
            cardImg.setImageResource(R.drawable.jambacard);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delLayout) {
            activity.showDeleteCardConfirmationAlert(billingAccount);
        } else {
            if (!swipe.isSelected()) {
                activity.cardSelectedWithAccountId(billingAccount.getAccountid());
            }
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
