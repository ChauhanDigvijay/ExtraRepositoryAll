package com.olo.jambajuice.Adapters;


/**
 * Created by Digvijay Chauhan on 7/12/15.
 */


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer.PassdetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.BasketRewardsAndOffersActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferPromoCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.OfferItem;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.OfferPromoService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class BasketOfferAdapter extends ArrayAdapter<OfferItem> implements View.OnClickListener {

    BasketRewardsAndOffersActivity context;
    OfferItem rowItem;

    public BasketOfferAdapter(BasketRewardsAndOffersActivity context, int resourceId,
                              List<OfferItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    public void enableScreen(boolean isEnabled) {

        RelativeLayout screenDisableView = (RelativeLayout) context.findViewById(R.id.screenDisableView);
        if (screenDisableView != null) {
            if (!isEnabled) {
                screenDisableView.setVisibility(View.VISIBLE);
            } else {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;


        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_basket_offer_item, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_name);
            holder.txtTitle1 = (TextView) convertView.findViewById(R.id.tv_expiry);
            holder.time = (TextView) convertView.findViewById(R.id.tv_name1);
            holder.B1 = (Button) convertView
                    .findViewById(R.id.btn_apply);
            holder.B2 = (Button) convertView
                    .findViewById(R.id.btn_remove);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        rowItem = getItem(position);
        final String offerId = rowItem.getOfferId();
        final Boolean isPMOffer = rowItem.isPMOffer();
        final int promotionId = rowItem.getPmPromotionID();
        final String promoCode = rowItem.getPromotioncode();

        if (StringUtilities.isValidString(rowItem.getDatetime())) {
            Date d2 = Utils.getDateFromString(rowItem.getDatetime(), null);

            int differ = Utils.daysBetween(new Date(), d2);
            if (differ > 0) {
                holder.time.setText("Expires in" + " " + differ + " " + "days");
            } else if (differ < 0) {
                holder.time.setText("Expired");
            } else if (differ == 1) {
                holder.time.setText("Expires in" + " " + differ + " " + "day");
            } else if (differ == 0) {
                holder.time.setText("Expires today");
            }
        }else{
            holder.time.setText("Never Expires");
        }

        holder.txtTitle.setText(rowItem.getOfferIItem());
        if(StringUtilities.isValidString(rowItem.getOfferIName())){
            String[] separated = rowItem.getOfferIName().split("Promo Code:");
            holder.txtTitle1.setText(separated[0]);
        }else{
            holder.txtTitle1.setText("");
        }

        int savedPromoId = DataManager.getInstance().getCurrentBasket().getPromoId();

//        if (DataManager.getInstance().getCurrentBasket() != null) {
//            if (DataManager.getInstance().getCurrentBasket().getOfferId() != null) {
//                if (StringUtilities.isValidString(offerId) && !isPMOffer) {
//                    if (DataManager.getInstance().getCurrentBasket().getOfferId().equalsIgnoreCase(offerId)
//                            && DataManager.getInstance().getCurrentBasket().getDiscount() > 0) {
//                        holder.B1.setVisibility(View.GONE);
//                        holder.B2.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.B1.setVisibility(View.VISIBLE);
//                        holder.B2.setVisibility(View.GONE);
//                    }
//                } else {
//                    if (DataManager.getInstance().getCurrentBasket().getOfferId().equalsIgnoreCase(String.valueOf(promotionId))
//                             && DataManager.getInstance().getCurrentBasket().getDiscount() > 0) {
//                        holder.B1.setVisibility(View.GONE);
//                        holder.B2.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.B1.setVisibility(View.VISIBLE);
//                        holder.B2.setVisibility(View.GONE);
//                    }
//                }
//
//            }
//        }

        if (DataManager.getInstance().getCurrentBasket() != null) {
            if (DataManager.getInstance().getCurrentBasket().getPromoId() != 0
                    && DataManager.getInstance().getCurrentBasket().getPromoId() == promotionId
                    && DataManager.getInstance().getCurrentBasket().getPromotionCode().equalsIgnoreCase(promoCode)
                    && DataManager.getInstance().getCurrentBasket().getDiscount() > 0) {
                holder.B1.setVisibility(View.GONE);
                holder.B2.setVisibility(View.VISIBLE);
            } else {
                holder.B1.setVisibility(View.VISIBLE);
                holder.B2.setVisibility(View.GONE);
            }
        }


//        holder.B1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, rowItem.getOfferIItem(), FBEventSettings.APPLY_OFFER);
//                if(DataManager.getInstance().getCurrentBasket().getAppliedRewards().size()>0
//                        || StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {
//                    confirmationAlert(Constants.APPLY_REWARD_OR_APPLY_COUPON_MESSAGE,offerId,promotionId,isPMOffer);
//                }else{
//                    applyOffer(offerId,promotionId,isPMOffer);
//                }
//            }
//        });

        holder.B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, rowItem.getOfferIItem(), FBEventSettings.APPLY_OFFER);
                if (DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0
                        || StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {

                    confirmationAlert(Constants.APPLY_REWARD_OR_APPLY_COUPON_MESSAGE, offerId, promotionId, isPMOffer, promoCode);
                } else {
                    //applyOffer(offerId,promotionId,isPMOffer);
                    applyCouponToBasket(promoCode, promotionId);
                }
            }
        });

        holder.B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, rowItem.getOfferIItem(), FBEventSettings.REMOVE_OFFER);
                removeCouponFromBasket();
            }
        });

        return convertView;
    }

//    private void applyOffer(String offerId, int promoId, boolean isPmOffer) {
//        enableScreen(false);
//        if (StringUtilities.isValidString(offerId) && !isPmOffer) {
//
//            fetchUserOffersPromo(offerId, isPmOffer);
//        } else {
//            fetchUserOffersPromo(String.valueOf(promoId), isPmOffer);
//        }
//
//        notifyDataSetChanged();
//    }


    private void confirmationAlert(String message, final String offerId, final int promoId, final boolean isPmOffer, final String promoCode) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // applyOffer(offerId,promoId,isPmOffer);
                applyCouponToBasket(promoCode, promoId);
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

    //digvijay(dj) fetch userofferpromo
//    private void fetchUserOffersPromo(final String offerId, Boolean isPMIntegrated) {
//        context.enableScreen(false);
//        OfferPromoService.getUserOfferPromo(context, offerId, isPMIntegrated, new OfferPromoCallback() {
//            @Override
//            public void onOfferPromoCallback(JSONObject offerjson, String error) {
//
//                context.enableScreen(true);
//                if (offerjson != null) {
//                    if (offerjson.has("promoCode")) {
//                        try {
//                            String promocode = (String) offerjson.get("promoCode");
//                            // applyCouponToBasket(promocode, prmo);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        if (offerjson.has("message")) {
//                            try {
//                                String offermessage = (String) offerjson.get("message");
//                                Utils.showErrorAlert(context, offermessage);
//                                //  code.setText(offermessage);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                } else {
//                    if (StringUtilities.isValidString(error)) {
//                        Utils.showErrorAlert(context, error);
//                    } else {
//                        Utils.showErrorAlert(context, "Sorry,Could not able to get promo code");
//                    }
//                }
//            }
//        });
//    }

    private void removeCouponFromBasket() {
        context.enableScreen(false);
        BasketService.removeCoupon(new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                if (error == null) {
                    DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                    DataManager.getInstance().getCurrentBasket().setPromoId(0);
                    TransitionManager.transitFrom(context, BasketActivity.class, true);
                    context.finish();
                    notifyDataSetChanged();
                } else {
                    Utils.showErrorAlert(context, error);
                    notifyDataSetChanged();
                }
                // postDeletionActions(error,basket);
            }
        });
    }

    private void postDeletionActions(Exception e, OloBasket oloBasket) {
        context.enableScreen(true);
        if (e != null) {
            if (Utils.getErrorCode(e) == 9) {
                enableScreen(false);
                BasketService.refreshBasket(context, new BasketServiceCallback() {
                    @Override
                    public void onBasketServiceCallback(Basket basket, Exception e) {
                        enableScreen(true);
                        if (e == null) {
                            if (basket.getDiscount() == 0) {
                                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                                DataManager.getInstance().getCurrentBasket().setPromoId(0);
                            }
                            TransitionManager.transitFrom(context, BasketActivity.class, true);
                            context.finish();
                        } else {
                            TransitionManager.transitFrom(context, BasketActivity.class, true);
                            context.finish();
                        }
                    }
                });
            } else {
                Utils.showErrorAlert(context, e);
            }
        } else {
//            if(oloBasket.getDiscount() == 0) {
//                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
//                DataManager.getInstance().getCurrentBasket().setOfferId("");
//            }
            TransitionManager.transitFrom(context, BasketActivity.class, true);
            context.finish();
            notifyDataSetChanged();
        }
    }

    private void postDeletionActions(Exception e, Basket mBasket) {
        context.enableScreen(true);
        if (e != null) {
            if (Utils.getErrorCode(e) == 9) {
                enableScreen(false);
                BasketService.refreshBasket(context, new BasketServiceCallback() {
                    @Override
                    public void onBasketServiceCallback(Basket basket, Exception e) {
                        enableScreen(true);
                        if (e == null) {
                            if (basket.getDiscount() == 0) {
                                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                                DataManager.getInstance().getCurrentBasket().setPromoId(0);
                            }
                            TransitionManager.transitFrom(context, BasketActivity.class, true);
                            context.finish();
                        } else {
                            TransitionManager.transitFrom(context, BasketActivity.class, true);
                            context.finish();
                        }
                    }
                });
            } else {
                Utils.showErrorAlert(context, e);
                notifyDataSetChanged();
            }
        } else {
//            if(mBasket.getDiscount() == 0) {
//                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
//                DataManager.getInstance().getCurrentBasket().setOfferId("");
//            }
            TransitionManager.transitFrom(context, BasketActivity.class, true);
            context.finish();
            notifyDataSetChanged();
        }
    }


    private void applyCouponToBasket(String promocode, final int promoId) {
        context.enableScreen(false);
        if (promocode != null && promocode != " ") {
            final String promotionCode = promocode;
            BasketService.applyCoupon(context, promotionCode, new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    context.enableScreen(true);
                    if (e == null) {
                        DataManager.getInstance().getCurrentBasket().setPromotionCode(promotionCode);
                        DataManager.getInstance().getCurrentBasket().setPromoId(promoId);
                        //Navigate back to basket screen.

                        if (rowItem.getChannelID() == 6) {//PASS
                            //JambaAnalyticsManager.sharedInstance().track_OfferItemWith(offerId, promotionCode, FBEventSettings.PASS_ACCEPTED);


                        } else {
                            //JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, promotionCode, FBEventSettings.ACCEPT_APP_OFFER);
                        }

//                        Intent intent = new Intent(context, BasketActivity.class);
//                        context.startActivity(intent);
                        TransitionManager.transitFrom(context, BasketActivity.class, true);
                        context.finish();
                        notifyDataSetChanged();
                    } else {
                        //postDeletionActions(e,basket);
                        Utils.showErrorAlert(context, e);
                        notifyDataSetChanged();
                        context.setUi();
                    }
                }
            });
        }
    }

    public void setOffer(List<OfferItem> rewards) {
        clear();
        addAll(rewards);
        this.notifyDataSetChanged();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle1;
        TextView txtTitle;
        TextView time;
        Button B1;
        Button B2;

    }
}