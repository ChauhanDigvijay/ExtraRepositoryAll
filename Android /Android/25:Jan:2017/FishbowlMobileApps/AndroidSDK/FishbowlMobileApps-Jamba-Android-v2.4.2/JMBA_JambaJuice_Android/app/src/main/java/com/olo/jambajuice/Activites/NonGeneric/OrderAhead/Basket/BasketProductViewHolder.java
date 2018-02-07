package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.daimajia.swipe.SwipeLayout;
import com.olo.jambajuice.Adapters.BasketAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.BasketChoice;
import com.olo.jambajuice.BusinessLogic.Models.BasketProduct;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Models.OloProduct;


/**
 * Created by Ihsanulhaq on 5/25/2015.
 */
public class BasketProductViewHolder implements View.OnClickListener, SwipeLayout.SwipeListener {

    private final RelativeLayout rowView;
    private TextView title;
    private TextView txtDetail;
    private TextView specialInstructions;
    private TextView amount;
    private TextView seeMore;
    private TextView seeLess;

    private BasketAdapter adapter;
    private TextView quantity;
    private TextView delete;
    private SwipeLayout swipe;
    private BasketProduct basketProduct;
    private Activity activity;
    private int position;

    public BasketProductViewHolder(Activity activity, View view, BasketAdapter adapter) {
        this.adapter = adapter;
        this.activity = activity;
        title = (TextView) view.findViewById(R.id.tv_title);
        txtDetail = (TextView) view.findViewById(R.id.tv_detail);
        specialInstructions = (TextView) view.findViewById(R.id.tv_specialInst);
        amount = (TextView) view.findViewById(R.id.tv_amount);
        quantity = (TextView) view.findViewById(R.id.tv_count);
        delete = (TextView) view.findViewById(R.id.tv_delete);
        swipe = (SwipeLayout) view.findViewById(R.id.swipe);
        rowView = (RelativeLayout) view.findViewById(R.id.row_container);
        seeMore = (TextView) view.findViewById(R.id.txtSeeMore);
        seeLess = (TextView) view.findViewById(R.id.txtSeeLess);
        swipe.setClickToClose(true);
        swipe.addSwipeListener(this);
        rowView.setOnClickListener(this);
        delete.setOnClickListener(this);
        seeMore.setOnClickListener(this);
        seeLess.setOnClickListener(this);
    }

    public void initWithValues(BasketProduct item, int position) {
        this.position = position;
        this.basketProduct = item;
        title.setText(item.getName());
        if(item.getSpecialinstructions().isEmpty()){
            specialInstructions.setVisibility(View.GONE);
        }else {
            specialInstructions.setVisibility(View.VISIBLE);
            specialInstructions.setText(item.getSpecialinstructions());
        }
        String detail = "";
        if (item.getChoices() != null) {
            for (BasketChoice basketChoice : item.getChoices()) {
                String choiceName = basketChoice.getName();
                int qty = basketChoice.getQuantity();
                String qtyText;
                //1. Olo is sending extra choice with same name, skip it.
                //2. For steel cut oat meal olo is sending nested product modifier parent in choice so skip it.
//                if (!detail.contains(choiceName) && !choiceName.toLowerCase().contains("click here"))
                if (!choiceName.toLowerCase().contains("click here")) {
                    if (choiceName.contains("Small")) {
                        if (detail.contains("Small")) {
                            continue;
                        }
                    }
                    if (choiceName.contains("Medium")) {
                        if (detail.contains("Medium")) {
                            continue;
                        }
                    }
                    if (choiceName.contains("Large")) {
                        if (detail.contains("Large")) {
                            continue;
                        }
                    }
                    if (qty > 1) {
                        qtyText = String.valueOf(qty) + "x ";
                    } else {
                        qtyText = "";
                    }
                    if (detail.equals("")) {
                        detail = qtyText + choiceName;
                    } else {
                        detail += ", " + qtyText + choiceName;
                    }
                }
            }
        }
        if (TextUtils.isEmpty(detail)) {
            txtDetail.setVisibility(View.INVISIBLE);
        } else {
            txtDetail.setVisibility(View.VISIBLE);
            txtDetail.setText(detail);
            txtDetail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (seeLess.getVisibility() == View.GONE) {
                        if (txtDetail.getLineCount() > 3) {
                            seeMore.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
        amount.setText(Utils.formatPrice(item.getTotalcost()));
        quantity.setText(item.getQuantity() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                if(StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())
                        || DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0){
                    confirmationAlert(Constants.ADD_PRODUCT_OR_REMOVE_PRODUCT_ALERT_MESSAGE);
                }else {
                    deleteBasketProduct();
                }
                break;
            case R.id.row_container:
                showHideDeleteOption();
                break;
            case R.id.txtSeeMore:
                showMoreText();
                break;
            case R.id.txtSeeLess:
                showLessText();
                break;
        }
    }

    private void confirmationAlert(String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                removeOfferOrRewardAndARemoveProducttFromBasket();
            }
        });
        dialog.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void removeOfferOrRewardAndARemoveProducttFromBasket(){
        enableScreen(false);

        if(StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {
            BasketService.removeCoupon(new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket basket, Exception error) {
                    enableScreen(true);
                    if (error == null) {
                        DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                        DataManager.getInstance().getCurrentBasket().setPromoId(0);
                        enableScreen(false);
                        deleteBasketProduct();
                    } else {
                        Utils.showErrorAlert(activity, error);
                    }
                }
            });
        }

        if(DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0){
            BasketService.removeRewards(new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    if (e == null) {
                        enableScreen(false);
                        deleteBasketProduct();
                    } else {
                        Utils.showErrorAlert(activity, e);
                    }
                }
            });
        }
    }

    public void showMoreText() {
        ObjectAnimator animation = ObjectAnimator.ofInt(txtDetail, "maxLines", txtDetail.getLineCount());
        animation.setDuration(100).start();
        seeMore.setVisibility(View.GONE);
        seeLess.setVisibility(View.VISIBLE);
    }

    public void showLessText() {
        ObjectAnimator animation = ObjectAnimator.ofInt(txtDetail, "maxLines", 3);
        animation.setDuration(100).start();
        seeLess.setVisibility(View.GONE);
        seeMore.setVisibility(View.VISIBLE);
    }

    public void closeSwipe() {
        swipe.close();
    }

    private void showHideDeleteOption() {
        if (swipe.getShowMode() == SwipeLayout.ShowMode.PullOut) {
            swipe.open(true);
        }
    }

    private void deleteBasketProduct() {
        if(basketProduct != null) {
            JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(basketProduct.getProductId())
                    , "PRODUCT_NAME = " + basketProduct.getName() + ";TOTAL_QUANTITY = " + basketProduct.getQuantity() + ";TOTAL_COST = " + basketProduct.getTotalcost()
                    , FBEventSettings.REMOVE_PRODUCT);
        }

        enableScreen(false);
        BasketService.deleteProduct(activity, DataManager.getInstance().getCurrentBasket().getId(), basketProduct, new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                if (e == null) {
                    closeSwipe();
                    if(basket.getDiscount() == 0) {
                        adapter.refreshData();
                    }else{
                        adapter.refreshData();
                    }
//                    JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(basketProduct.getProductId())
//                            , "ProductName = "+basketProduct.getName()+" ProductCount = "+basketProduct.getQuantity()+" Total Cost = "+basketProduct.getTotalcost()
//                            , FBEventSettings.REMOVE_PRODUCT);

                } else {
                    Utils.showErrorAlert(activity, e);
                }

                enableScreen(true);
            }
        });

    }

    private void enableScreen(boolean isEnabled) {
        BasketActivity basketActivity = (BasketActivity) this.activity;
        basketActivity.enableScreen(isEnabled);
    }

    @Override
    public void onStartOpen(SwipeLayout swipeLayout) {
        adapter.closeOther(position);
        Log.d("onStartOpen ...", "" + swipeLayout.getDragEdge());
    }

    @Override
    public void onOpen(SwipeLayout swipeLayout) {

    }

    @Override
    public void onStartClose(SwipeLayout swipeLayout) {
        Log.d("onStartClose ...", "" + swipeLayout.getDragEdge());
    }

    @Override
    public void onClose(SwipeLayout swipeLayout) {
        Log.d("onClose ...", "" + swipeLayout.getDragEdge());
    }

    @Override
    public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {


    }

    @Override
    public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {

        Log.d("onHandRelease ...", "" + swipeLayout.getDragEdge());

    }
}
