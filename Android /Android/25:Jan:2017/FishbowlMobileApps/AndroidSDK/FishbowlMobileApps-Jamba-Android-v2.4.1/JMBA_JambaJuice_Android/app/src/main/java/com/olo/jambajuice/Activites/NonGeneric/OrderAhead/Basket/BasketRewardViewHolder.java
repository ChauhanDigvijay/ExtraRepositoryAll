package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;

/**
 * Created by Ihsanulhaq on 5/26/2015.
 */
public class BasketRewardViewHolder implements View.OnClickListener {

    BasketActivity basketActivity;
    private TextView title;
    private TextView detail;
    private TextView amount;
    private TextView delete;
    private SwipeLayout swipe;

    public BasketRewardViewHolder(BasketActivity activity, View view) {
        basketActivity = activity;
        title = (TextView) view.findViewById(R.id.tv_title);
        detail = (TextView) view.findViewById(R.id.tv_detail);
        amount = (TextView) view.findViewById(R.id.tv_amount);
        delete = (TextView) view.findViewById(R.id.tv_delete);
        delete.setOnClickListener(this);
        swipe = (SwipeLayout) view;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDetail(String detail) {
        if (TextUtils.isEmpty(detail)) {
            this.detail.setVisibility(View.GONE);
        } else {
            this.detail.setVisibility(View.VISIBLE);
            this.detail.setText(detail);
        }
    }

    public void setAmount(float amount) {
        this.amount.setText("-" + Utils.formatPrice(amount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                deleteRewardOrPromotion();
                break;
        }
    }

    private void deleteRewardOrPromotion() {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        basketActivity.enableScreen(false);
        if (basket != null && basket.getAppliedRewards().size() > 0) {
            BasketService.removeRewards(new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    postDeletionActions(e);
                }
            });
        } else {
            BasketService.removeCoupon(new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket basket, Exception error) {
                    postDeletionActions(error);
                }
            });
        }
    }

    private void postDeletionActions(Exception e) {
        basketActivity.enableScreen(true);
        if (e != null) {
            Utils.showErrorAlert(basketActivity, e);
        } else {
            DataManager.getInstance().getCurrentBasket().setPromotionCode("");
            DataManager.getInstance().getCurrentBasket().setPromoId(0);
            basketActivity.refreshData();
        }
    }

    public boolean isClickAllowed() {
        return swipe.getOpenStatus() == SwipeLayout.Status.Close;
    }

    public void setSwipeEnabled(boolean isEnabled) {
        swipe.setSwipeEnabled(isEnabled);
    }
}
