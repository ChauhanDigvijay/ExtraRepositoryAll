package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.BasketRewardsAndOffersActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferPromoCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.OfferAvailableStore;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.OfferPromoService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BarcodeGenerated;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * *
 * Created by Digvijay Chauhan on 12/4/16.
 */
public class PassdetailActivity extends BaseActivity {
    String barcode_data = "Digvijay Chauhan";
    // barcode image
    Bitmap bitmap = null;
    ImageView bar_image;
    String Url;
    String Title;
    String desc;
    String offerId;
    Integer Expire;
    TextView offer_promo;
    Boolean isPMOffer;
    int promotionId;
    String promoCode;
    ArrayList<OfferAvailableStore> storeResList;
    Button apply, remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_passdetail);

        setToolbar();
        Intent intent = getIntent();


        Bundle extras = intent.getExtras();
        if (extras != null) {

            promotionId = extras.getInt("promotionId");
            Title = extras.getString("Title");
            Expire = extras.getInt("Expire");
            offerId = extras.getString("offerId");
            desc = extras.getString("desc");
            promoCode = extras.getString("promoCode");
            isPMOffer = extras.getBoolean("isPMOffer");
            storeResList = (ArrayList<OfferAvailableStore>) extras.getSerializable("storeList");
        }

        isShowBasketIcon = false;
        bar_image = (ImageView) findViewById(R.id.bar_image);
        TextView offertitle = (TextView) findViewById(R.id.offertitle);
        TextView offerdate = (TextView) findViewById(R.id.offerdate);
        TextView offerdesc = (TextView) findViewById(R.id.offerdesc);
        TextView offer_location = (TextView) findViewById(R.id.offer_location_name);
        TextView moreInfo = (TextView) findViewById(R.id.moreStores);
        if (storeResList != null) {
            if (storeResList.size() > 1) {
                moreInfo.setVisibility(View.VISIBLE);
            } else {
                moreInfo.setVisibility(View.GONE);
            }
        } else {
            moreInfo.setVisibility(View.GONE);
        }
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putSerializable("storeList", storeResList);
                TransitionManager.slideUp(PassdetailActivity.this, MoreInfoActivity.class, extras);
            }
        });


        apply = (Button) findViewById(R.id.btn_apply);
        remove = (Button) findViewById(R.id.btn_remove);

        offer_promo = (TextView) findViewById(R.id.offer_promo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);


//        if(StringUtilities.isValidString(promoCode)){
//            hideProgressBar();
//            offer_promo.setText("Promo code: "+covertPromoCode(promoCode));
//            try {
//                bitmap = BarcodeGenerated.encodeAsBitmap(promoCode, BarcodeFormat.QR_CODE, getWidthAndHeight(), getWidthAndHeight());
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//            if (bitmap != null) {
//                bar_image.setImageBitmap(bitmap);
//            }
//        }else if (StringUtilities.isValidString(offerId) && !isPMOffer) {
//            fetchUserOffersPromoWithBar(offerId, isPMOffer);
//        } else {
//            fetchUserOffersPromoWithBar(String.valueOf(promotionId), isPMOffer);
//        }

        if (StringUtilities.isValidString(promoCode)) {
            hideProgressBar();
            offer_promo.setText("Promo code: " + covertPromoCode(promoCode));
            try {
                bitmap = BarcodeGenerated.encodeAsBitmap(promoCode, BarcodeFormat.QR_CODE, getWidthAndHeight(), getWidthAndHeight());
            } catch (WriterException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                bar_image.setImageBitmap(bitmap);
            }
        }


        if (StringUtilities.isValidString(Title)) {
            offertitle.setText(Title);
        }


        if (StringUtilities.isValidString(desc)) {
            String[] separated = desc.split("Promo Code:");
            offerdesc.setText(separated[0]);
        } else {
            offerdesc.setVisibility(View.GONE);
        }
//        if (Expire > 0) {
//            offerdate.setText("Expires in" + " " + Expire + " " + ((Expire == 1)?"day":"days"));
//        }

        if(Expire != null) {
            if (Expire == 400) {
                offerdate.setText("Never expires");
            } else if (Expire > 1) {
                offerdate.setText("Expires in" + " " + Expire + " " + "days");
            } else if (Expire < 0) {
                offerdate.setText("Expired");
            } else if (Expire == 1) {
                offerdate.setText("Expires in" + " " + Expire + " " + "day");
            } else if (Expire == 0) {
                offerdate.setText("Expires today");
            }
        }

        DataManager manager = DataManager.getInstance();
        if (manager != null) {
            Store selectedStore = manager.getCurrentSelectedStore();
            if (selectedStore != null) {
                if (StringUtilities.isValidString(selectedStore.getCompleteAddress())) {
                    if(DataManager.getInstance().isDebug){
                        offer_location.setText(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", "") + "\n" + selectedStore.getCompleteAddress());
                    }else {
                        offer_location.setText(selectedStore.getName().replace("Jamba Juice ", "") + "\n" + selectedStore.getCompleteAddress());
                    }
                }
            }
        }


//        int savedPromoId = DataManager.getInstance().getCurrentBasket().getPromoId();

//        if (DataManager.getInstance().getCurrentBasket() != null) {
//            if (DataManager.getInstance().getCurrentBasket().getOfferId() != null) {
//                if (StringUtilities.isValidString(offerId) && !isPMOffer) {
//                    if (DataManager.getInstance().getCurrentBasket().getOfferId().equalsIgnoreCase(offerId)
//                             && DataManager.getInstance().getCurrentBasket().getDiscount() > 0) {
//                        apply.setVisibility(View.GONE);
//                        remove.setVisibility(View.VISIBLE);
//                    } else {
//                        apply.setVisibility(View.VISIBLE);
//                        remove.setVisibility(View.GONE);
//                    }
//                } else {
//                    if (DataManager.getInstance().getCurrentBasket().getOfferId().equalsIgnoreCase(String.valueOf(promotionId))
//                            && DataManager.getInstance().getCurrentBasket().getDiscount() > 0) {
//                        apply.setVisibility(View.GONE);
//                        remove.setVisibility(View.VISIBLE);
//                    } else {
//                        apply.setVisibility(View.VISIBLE);
//                        remove.setVisibility(View.GONE);
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
                apply.setVisibility(View.GONE);
                remove.setVisibility(View.VISIBLE);
            } else {
                apply.setVisibility(View.VISIBLE);
                remove.setVisibility(View.GONE);
            }

        }

//        apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId,Title, FBEventSettings.APPLY_OFFER);
//                if(DataManager.getInstance().getCurrentBasket().getAppliedRewards().size()>0
//                        || StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {
//                    confirmationAlert(Constants.APPLY_REWARD_OR_APPLY_COUPON_MESSAGE);
//                }else{
//                    applyOffer();
//                }
//            }
//        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, Title, FBEventSettings.APPLY_OFFER);
                if (DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0
                        || StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {
                    confirmationAlert(Constants.APPLY_REWARD_OR_APPLY_COUPON_MESSAGE);
                } else {
                    applyCouponToBasket(promoCode, promotionId);
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JambaAnalyticsManager.sharedInstance().track_ItemWith(offerId, Title, FBEventSettings.REMOVE_OFFER);
                removeCouponFromBasket();
            }
        });


    }

//    private void applyOffer() {
//        if (StringUtilities.isValidString(offerId) && !isPMOffer) {
//
//            fetchUserOffersPromo(offerId, isPMOffer);
//        } else {
//            fetchUserOffersPromo(String.valueOf(promotionId), isPMOffer);
//        }
//
//    }

    private void confirmationAlert(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(PassdetailActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //applyOffer();
                applyCouponToBasket(promoCode, promotionId);
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


    private void removeCouponFromBasket() {
        enableScreen(false);
        BasketService.removeCoupon(new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                enableScreen(true);
                if (error == null) {
                    DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                    DataManager.getInstance().getCurrentBasket().setPromoId(0);
                    TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class, true);
                    finish();
                } else {
                    Utils.showErrorAlert(PassdetailActivity.this, error);
                }
                //postDeletionActions(error,basket);

            }
        });
    }

    private void postDeletionActions(Exception e, OloBasket oloBasket) {
        enableScreen(true);
        if (e != null) {
            if (Utils.getErrorCode(e) == 9) {
                enableScreen(false);
                BasketService.refreshBasket(this, new BasketServiceCallback() {
                    @Override
                    public void onBasketServiceCallback(Basket basket, Exception e) {
                        enableScreen(true);
                        if (e == null) {
                            if (basket.getDiscount() == 0) {
                                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                                DataManager.getInstance().getCurrentBasket().setPromoId(0);
                            }
                            TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class, true);
                            finish();
                        } else {
                            TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class, true);
                            finish();
                        }
                    }
                });
            } else {
                Utils.showErrorAlert(this, e);
            }
        } else {
//            if(oloBasket.getDiscount() == 0) {
//                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
//                DataManager.getInstance().getCurrentBasket().setOfferId("");
//            }
            TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class, true);
            finish();
        }
    }

    private void postDeletionActions(Exception e, Basket mBasket) {
        enableScreen(true);
        if (e != null) {
            if (Utils.getErrorCode(e) == 9) {
                enableScreen(false);
                BasketService.refreshBasket(this, new BasketServiceCallback() {
                    @Override
                    public void onBasketServiceCallback(Basket basket, Exception e) {
                        enableScreen(true);
                        if (e == null) {
                            if (basket.getDiscount() == 0) {
                                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                                DataManager.getInstance().getCurrentBasket().setPromoId(0);
                            }
                            TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class, true);
                            finish();
                        } else {
                            TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class, true);
                            finish();
                        }
                    }
                });
            } else {
                Utils.showErrorAlert(this, e);
            }
        } else {
//            if(mBasket.getDiscount() == 0) {
//                DataManager.getInstance().getCurrentBasket().setPromotionCode("");
//                DataManager.getInstance().getCurrentBasket().setOfferId("");
//            }
            TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class, true);
            finish();
        }
    }

    private void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }

    //digvijay(dj) fetch userofferpromo
//    private void fetchUserOffersPromo(final String offerId, Boolean isPMIntegrated) {
//
//        enableScreen(false);
//        OfferPromoService.getUserOfferPromo(this, offerId, isPMIntegrated, new OfferPromoCallback() {
//            @Override
//            public void onOfferPromoCallback(JSONObject offerjson, String error) {
//
//                enableScreen(true);
//                if (offerjson != null) {
//                    if (offerjson.has("promoCode")) {
//                        try {
//                            String promocode = (String) offerjson.get("promoCode");
//                            //applyCouponToBasket(promocode, offerId);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        if (offerjson.has("message")) {
//                            try {
//                                String offermessage = (String) offerjson.get("message");
//                                Utils.showErrorAlert(PassdetailActivity.this, offermessage);
//                                //  code.setText(offermessage);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                } else {
//                    if (StringUtilities.isValidString(error)) {
//                        Utils.showErrorAlert(PassdetailActivity.this, error);
//                    } else {
//                        Utils.showErrorAlert(PassdetailActivity.this, "Sorry,Could not able to get promo code");
//                    }
//                }
//            }
//        });
//    }

    private void applyCouponToBasket(String promocode, final int promoId) {
        enableScreen(false);
        if (StringUtilities.isValidString(promocode)) {
            final String promotionCode = promocode;
            BasketService.applyCoupon(this, promotionCode, new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    if (e == null) {
                        DataManager.getInstance().getCurrentBasket().setPromotionCode(promotionCode);
                        DataManager.getInstance().getCurrentBasket().setPromoId(promoId);
                        //Navigate back to basket screen.
                        //Intent intent = new Intent(PassdetailActivity.this, BasketActivity.class);
                        //this activity is of AppOffer

                        //JambaAnalyticsManager.sharedInstance().track_OfferItemWith(offerId, promotionCode, FBEventSettings.ACCEPT_APP_OFFER);


                        //PassdetailActivity.this.startActivity(intent);
                        TransitionManager.transitFrom(PassdetailActivity.this, BasketActivity.class);
                        finish();
                    } else {

                        //postDeletionActions(e,basket);
                        Utils.showErrorAlert(PassdetailActivity.this, e);
                        BasketRewardsAndOffersActivity.basketRewardsAndOffersActivity.setUi();
                    }
                }
            });
        } else {
            enableScreen(true);
        }
    }

    private void setToolbar() {
        setUpToolBar(true);
        setTitle("Offer Detail");
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(R.color.text_white));
        setBackButton(true, true);
    }

//    public void enableScreen(boolean isEnabled) {
//
//        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
//        if (screenDisableView != null) {
//            if (!isEnabled) {
//                screenDisableView.setVisibility(View.VISIBLE);
//            } else {
//                screenDisableView.setVisibility(View.GONE);
//            }
//        }
//    }

    private int getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.widthPixels * 0.6);
        return width;

    }

//    private void fetchUserOffersPromoWithBar(final String offerId, Boolean isPMIntegrated) {
//
//
//        OfferPromoService.getUserOfferPromo(PassdetailActivity.this, offerId, isPMIntegrated, new OfferPromoCallback() {
//            @Override
//            public void onOfferPromoCallback(JSONObject offerjson, String error) {
//
//
//                if (offerjson != null) {
//                    try {
//                        if (offerjson.has("successFlag") && offerjson.getBoolean("successFlag")) {
//                            if (offerjson.has("promoCode")) {
//                                try {
//                                    String promocode = (String) offerjson.get("promoCode");
//                                    try {
//
//                                        if (StringUtilities.isValidString(promocode)) {
//                                            offer_promo.setText("Promo code: " + covertPromoCode(promocode));
//                                        }
//                                        hideProgressBar();
//                                        bitmap = BarcodeGenerated.encodeAsBitmap(promocode, BarcodeFormat.QR_CODE, getWidthAndHeight(), getWidthAndHeight());
//                                        if (bitmap != null) {
//                                            bar_image.setImageBitmap(bitmap);
//                                        }
//
//                                    } catch (WriterException e) {
//                                        e.printStackTrace();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                if (offerjson.has("message")) {
//                                    try {
//                                        String offermessage = (String) offerjson.get("message");
//                                        Utils.showErrorAlert(PassdetailActivity.this, offermessage);
//                                        //  code.setText(offermessage);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    hideProgressBar();
//                    showAlert();
//                }
//            }
//        });
//    }

//    private void showAlert() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(PassdetailActivity.this);
//        dialog.setCancelable(false);
//        dialog.setTitle("Error");
//        dialog.setMessage("Unable to get promo code please try again");
//        dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                if (StringUtilities.isValidString(offerId) && !isPMOffer) {
//                    fetchUserOffersPromoWithBar(offerId, isPMOffer);
//                } else {
//                    fetchUserOffersPromoWithBar(String.valueOf(promotionId), isPMOffer);
//                }
//            }
//        });
//        dialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                onBackPressed();
//            }
//        });
//
//        final AlertDialog alert = dialog.create();
//        alert.setCanceledOnTouchOutside(false);
//        alert.show();
//    }

    private String covertPromoCode(String promocode) {
        return promocode.replaceAll("([0-9]{3})([0-9]{4})([0-9]{4})", "$1-$2-$3");
    }

}
