package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Controllers.MainActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.OfferPromoCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StartOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreDetailCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.OfferAvailableStore;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.OfferPromoService;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BarcodeGenerated;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.wearehathway.apps.olo.Services.OloUserService;
import com.wearehathway.apps.spendgo.Services.SpendGoService;
import com.wearehathway.apps.spendgo.Services.SpendGoStoreService;
import com.wearehathway.apps.spendgo.Services.SpendGoUserService;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * *
 * Created by Digvijay Chauhan on 2/3/16.
 */
public class Passreward extends BaseActivity {
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
    Button btn_try_again;
    ProgressBar progressBar;
    LinearLayout offerBarcodeLayout;
    SemiBoldTextView offerHeading,offerFooterText;
    SemiBoldButton offerFooterButton;
    int onlineInStore;
    HomeActivity homeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_passreward);
        homeActivity = HomeActivity.homeActivity;

        setToolbar();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            promotionId = extras.getInt("promotionId");
            Title = extras.getString("Title");
            Expire = extras.getInt("Expire");
            offerId = extras.getString("offerId");
            desc = extras.getString("desc");
            isPMOffer = extras.getBoolean("isPMOffer");
            promoCode = extras.getString("promoCode");
            storeResList = (ArrayList<OfferAvailableStore>) extras.getSerializable("storeList");
            onlineInStore = extras.getInt("onlineInStore");
        }

        bar_image = (ImageView) findViewById(R.id.bar_image);
        TextView offertitle = (TextView) findViewById(R.id.offertitle);
        TextView offerdate = (TextView) findViewById(R.id.offerdate);
        final TextView offer_location = (TextView) findViewById(R.id.offer_location_name);
        TextView offerdesc = (TextView) findViewById(R.id.offerdesc);
        TextView moreInfo = (TextView) findViewById(R.id.moreStores);
        btn_try_again = (Button) findViewById(R.id.tryAgain);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        offerBarcodeLayout = (LinearLayout) findViewById(R.id.offer_barcode);
        offerHeading = (SemiBoldTextView) findViewById(R.id.offerHeading);
        offerFooterText = (SemiBoldTextView) findViewById(R.id.offerFooterText);
        offerFooterButton = (SemiBoldButton) findViewById(R.id.offerFooterButton);
        offerFooterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlsWaitText();
                homeActivity.orderNowClicked(new StartOrderCallback() {
                    @Override
                    public void onStartOrderCallback(Exception exception) {
                        setOrderNowText();
                    }
                });
            }
        });

        if(onlineInStore == 2){
            offertitle.setTextColor(Color.BLACK);
            offerBarcodeLayout.setVisibility(View.GONE);
            offerHeading.setVisibility(View.VISIBLE);
            offerFooterText.setVisibility(View.VISIBLE);
            offerFooterButton.setVisibility(View.VISIBLE);
        }else if (onlineInStore == 3 || onlineInStore == 1 || onlineInStore == 0){
            offerBarcodeLayout.setVisibility(View.VISIBLE);
            offerHeading.setVisibility(View.GONE);
            offerFooterText.setVisibility(View.GONE);
            offerFooterButton.setVisibility(View.GONE);
        }


//        btn_try_again.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showProgressBar();
//                fetchUserOfferPromo();
//            }
//        });

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
                TransitionManager.slideUp(Passreward.this, MoreInfoActivity.class, extras);
            }
        });
        offer_promo = (TextView) findViewById(R.id.offer_promo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

//        if (StringUtilities.isValidString(promoCode)) {
//            hideProgressBar();
//            offer_promo.setText("Promo code: " + covertPromoCode(promoCode));
//            try {
//                bitmap = BarcodeGenerated.encodeAsBitmap(promoCode, BarcodeFormat.QR_CODE, getWidthAndHeight(), getWidthAndHeight());
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//            if (bitmap != null) {
//                bar_image.setImageBitmap(bitmap);
//            }
//        } else {
//            fetchUserOfferPromo();
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
//        else {
//            fetchUserOfferPromo();
//        }


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

//        DataManager manager = DataManager.getInstance();
//        if (manager != null) {
//            Store selectedStore = manager.getCurrentSelectedStore();
//            if (selectedStore != null) {
//                if (StringUtilities.isValidString(selectedStore.getCompleteAddress())) {
//                    offer_location.setText(selectedStore.getCompleteAddress());
//                }
//            }
//        }

        if (storeResList != null) {
            if (storeResList.size() == 0) {
                if (StringUtilities.isValidString(UserService.getUser().getStoreAddress())) {
                    if (DataManager.getInstance().isDebug) {
                        offer_location.setText(Utils.getDemoStoreName() + "\n" + UserService.getUser().getStoreAddress());
                    } else {
                        offer_location.setText(UserService.getUser().getStoreName().replace("Jamba Juice ", "") + "\n" + UserService.getUser().getStoreAddress());
                    }
                }
            } else {
                for (int i = 0; i < storeResList.size(); i++) {
                    String favCode = UserService.getUser().getFavoriteStoreCode();
                    if (favCode.equalsIgnoreCase(storeResList.get(i).getStoreCode())) {
                        if(DataManager.getInstance().isDebug) {
                            offer_location.setText(Utils.getDemoStoreName() + "\n" + UserService.getUser().getStoreAddress());
                        }else {
                            offer_location.setText(UserService.getUser().getStoreName().replace("Jamba Juice ", "") + "\n" + UserService.getUser().getStoreAddress());
                        }
                        return;
                    }
                }

                if (storeResList.size() > 1) {
                    offer_location.setText("Tap More Info to see stores");
                } else {
                    StoreService.getStoreInformation(storeResList.get(0).getStoreCode(), new StoreDetailCallback() {
                        @Override
                        public void onStoreDetailCallback(Store store, Exception exception) {
                            if (store != null) {
                                offer_location.setText(storeResList.get(0).getStoreName() + "\n" + store.getCompleteAddress());
                            } else {
                                offer_location.setText(storeResList.get(0).getStoreName());
                            }
                        }
                    });
                }

            }
        } else {
            if (StringUtilities.isValidString(UserService.getUser().getStoreAddress())) {
                offer_location.setText(UserService.getUser().getStoreAddress());
            }
        }

    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showTryAgainButton() {
        btn_try_again.setVisibility(View.VISIBLE);
    }

    private void hideTryAgainButton() {
        btn_try_again.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void setOrderNowText(){
        offerFooterButton.setText("ORDER NOW");
    }

    private void setPlsWaitText(){
        offerFooterButton.setText("Please Wait...");
    }
    private void setToolbar() {
        setUpToolBar(true);
        setTitle("Offer Detail");
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(R.color.text_white));
        setBackButton(true, true);
    }

    public void enableScreen(boolean isEnabled) {

        RelativeLayout screenDisableView = (RelativeLayout) findViewById(R.id.screenDisableView);
        if (screenDisableView != null) {
            if (!isEnabled) {
                screenDisableView.setVisibility(View.VISIBLE);
            } else {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }

    private int getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.widthPixels * 0.6);
        return width;

    }

//    private void fetchUserOffersPromo(final String offerId, Boolean isPMIntegrated) {
//
//
//        OfferPromoService.getUserOfferPromo(Passreward.this, offerId, isPMIntegrated, new OfferPromoCallback() {
//            @Override
//            public void onOfferPromoCallback(JSONObject offerjson, String error) {
//
//                if (offerjson != null) {
//                    try {
//                        if (offerjson.has("successFlag") && offerjson.getBoolean("successFlag")) {
//                            if (offerjson.has("promoCode")) {
//                                try {
//                                    String promocode = (String) offerjson.get("promoCode");
//
//                                    try {
//
//                                        if (StringUtilities.isValidString(promocode)) {
//                                            offer_promo.setText("Promo code: " + covertPromoCode(promocode));
//                                        }
//
//                                        hideProgressBar();
//                                        bitmap = BarcodeGenerated.encodeAsBitmap(promocode, BarcodeFormat.QR_CODE, getWidthAndHeight(), getWidthAndHeight());
//                                        if (bitmap != null) {
//                                            bar_image.setImageBitmap(bitmap);
//                                        }
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
//                                        Utils.showErrorAlert(Passreward.this, offermessage);
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
//                    hideTryAgainButton();
//                } else {
//                    hideProgressBar();
//                    //showAlert();
//                    showTryAgainButton();
//                }
//            }
//        });
//    }


//    private void showAlert() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(Passreward.this);
//        dialog.setCancelable(false);
//        dialog.setTitle("Error");
//        dialog.setMessage("Unable to get promo code please try again");
//        dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                if (StringUtilities.isValidString(offerId) && !isPMOffer) {
//                    fetchUserOffersPromo(offerId, isPMOffer);
//                } else {
//                    fetchUserOffersPromo(String.valueOf(promotionId), isPMOffer);
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

//    private void fetchUserOfferPromo(){
//        if (StringUtilities.isValidString(offerId) && !isPMOffer) {
//            fetchUserOffersPromo(offerId, isPMOffer);
//        } else {
//            fetchUserOffersPromo(String.valueOf(promotionId), isPMOffer);
//        }
//    }
}
