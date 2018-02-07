package com.olo.jambajuice.Activites.NonGeneric.OrderHistory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.NonGeneric.Store.StoreDetail.StoreDetailActivity;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.DeliveryAddress;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderSummary;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

/**
 * Created by Ihsanulhaq on 6/18/2015.
 */
public class OrderSummaryViewHolder implements View.OnClickListener {
    private final TextView detail;
    private final TextView total;
    private final RelativeLayout summaryLayout, detailLayout,orderStatusLayout;
    private final TextView status;
    private final TextView storeAddress;
    private final TextView storeName;
    private final TextView order_head;
    private Store store;
    private Activity context;
    private RelativeLayout delivery,deliveryStatusLayout;
    private TextView sAddress1;
    private TextView del_status;
    private TextView cancelText;

    public OrderSummaryViewHolder(View convertView) {
        delivery = (RelativeLayout) convertView.findViewById(R.id.del_relative);
        deliveryStatusLayout = (RelativeLayout)convertView.findViewById(R.id.del_status_layout);
        detail = (TextView) convertView.findViewById(R.id.tv_detail);
        total = (TextView) convertView.findViewById(R.id.tv_total);
        status = (TextView) convertView.findViewById(R.id.tv_status);
        order_head = (TextView) convertView.findViewById(R.id.ord_addr);
        summaryLayout = (RelativeLayout) convertView.findViewById(R.id.store_layout);
        detailLayout = (RelativeLayout) convertView.findViewById(R.id.detailLayout);
        storeName = (TextView) convertView.findViewById(R.id.store_title);
        storeAddress = (TextView) convertView.findViewById(R.id.store_detail);
        sAddress1 = (TextView) convertView.findViewById(R.id.sAddress1);
        del_status = (TextView) convertView.findViewById(R.id.del_status);
        cancelText = (TextView) convertView.findViewById(R.id.cancelText);
        orderStatusLayout = (RelativeLayout) convertView.findViewById(R.id.orderStatusLayout);

        summaryLayout.setOnClickListener(this);
        delivery.setOnClickListener(this);
    }

    public void invalidate(Context context, RecentOrderSummary summary, Store store, String orderStatus) {
        this.context = (Activity) context;
        String time = "";
        if (summary != null) {
            if (summary.getReadyTime() != null) {
                total.setText(Utils.formatPrice(summary.getAmount()));
                time = summary.getReadyTime();
            } else {
                detailLayout.setVisibility(View.GONE);
                orderStatusLayout.setVisibility(View.GONE);
            }
            if (summary.getDeliverymode().equalsIgnoreCase("pickup")) {
                if(orderStatus != null && orderStatus.equalsIgnoreCase("Canceled")){
                    detail.setText(summary.getOrderTimeStatement());
                    cancelText.setVisibility(View.VISIBLE);
                }else {
                    detail.setText(Utils.getOrderReadyTimeStatement(context, time));
                    cancelText.setVisibility(View.GONE);
                }
                delivery.setVisibility(View.GONE);
                deliveryStatusLayout.setVisibility(View.GONE);
            } else if (summary.getDeliverymode().equalsIgnoreCase("dispatch")) {
                if(orderStatus != null && orderStatus.equalsIgnoreCase("Canceled")){
                    detail.setText(summary.getOrderTimeStatement());
                    cancelText.setVisibility(View.VISIBLE);
                }else {
                    detail.setText(Utils.getOrderDispatchTiming(context, time,summary,false));
                    cancelText.setVisibility(View.GONE);
                }
                if(summary.getDeliverystatus() != null) {
                    del_status.setText(summary.getDeliverystatus());
                    switch (summary.getDeliverystatus()) {
                        case "Canceled":
                            del_status.setTextColor(context.getResources().getColor(R.color.cancel_red));
                            break;
                        case "Failed":
                            del_status.setTextColor(context.getResources().getColor(R.color.cancel_red));
                            break;
                        case "Completed":
                            del_status.setTextColor(context.getResources().getColor(R.color.greenEnd));
                            break;
                        case "Delivered":
                            if(!(orderStatus != null && orderStatus.equalsIgnoreCase("Canceled"))) {
                                detail.setText(Utils.getOrderDispatchTiming(context, time,summary,true));
                            }
                            del_status.setTextColor(context.getResources().getColor(R.color.greenEnd));
                            break;
                        case "Scheduled":
                            del_status.setTextColor(context.getResources().getColor(R.color.dark_gray));
                            break;
                        default:
                            del_status.setTextColor(context.getResources().getColor(R.color.dark_gray));
                            break;
                    }
                }
                delivery.setVisibility(View.VISIBLE);
                deliveryStatusLayout.setVisibility(View.VISIBLE);
            }
            if (summary.getDeliveryaddress() != null) {
                DeliveryAddress deliveryAddress = summary.getDeliveryaddress();
                sAddress1.setText(Utils.getFormatedAddress(deliveryAddress.getStreetaddress(), deliveryAddress.getCity(), deliveryAddress.getBuilding(), deliveryAddress.getZipcode()));
            }
        } else {
            clearVar();
        }
        if (store != null) {
            this.store = store;
            if (orderStatus != null) {
                status.setText(orderStatus);
                switch (orderStatus) {
                    case "Canceled":
                        status.setTextColor(context.getResources().getColor(R.color.cancel_red));
                        break;
                    case "Failed":
                        status.setTextColor(context.getResources().getColor(R.color.cancel_red));
                        detail.setText("Your Order is Not Placed");
                        break;
                    case "Completed":
                        status.setTextColor(context.getResources().getColor(R.color.greenEnd));
                        break;
                    case "Scheduled":
                        status.setTextColor(context.getResources().getColor(R.color.dark_gray));
                        break;
                    default:
                        status.setTextColor(context.getResources().getColor(R.color.dark_gray));
                        break;
                }
            }

            summaryLayout.setVisibility(View.VISIBLE);
            if(DataManager.getInstance().isDebug){
                storeName.setText(Utils.setDemoStoreName(store).getName().replace("Jamba Juice ", ""));
            }else{
                storeName.setText(store.getName().replace("Jamba Juice ", ""));
            }

            storeAddress.setText(store.getCompleteAddress());
        } else {
            order_head.setVisibility(View.GONE);
            delivery.setVisibility(View.GONE);
            summaryLayout.setVisibility(View.GONE);
            orderStatusLayout.setVisibility(View.GONE);
        }
    }

    private void clearVar() {
        delivery.setVisibility(View.GONE);
        orderStatusLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        deliveryStatusLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_layout:
                if (this.store != null && this.context != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.B_IS_STORE_DETAIL_ONLY, true);
                    bundle.putSerializable(Constants.B_STORE, store);
                    bundle.putBoolean(Constants.B_IS_SHOW_BASKET, false);
                    TransitionManager.transitFrom(context, StoreDetailActivity.class, bundle);
                }
                break;
        }

    }
}
