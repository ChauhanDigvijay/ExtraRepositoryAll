package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Confirmation.OrderConfirmationActivity;
import com.olo.jambajuice.Adapters.GiftCardAdapters.PayeGiftCardListAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.FBSDKLoginServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.TemplateSelectionInterfaces;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.PlaceOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RedeemedServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.PayGiftCard;
import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.BusinessLogic.Services.RedeemedService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommGetAllCardServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.olo.Models.OloBillingField;
import com.wearehathway.apps.olo.Models.OloOrderInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EGiftCard extends GiftCardBaseActivity implements TemplateSelectionInterfaces {

    Context mContext;
    private RelativeLayout eGiftCardRootLayout, eGiftHeader;
    private RecyclerView eGiftRecyclerListView;
    private TextView noGiftCardListText;
    private ArrayList<PayGiftCard> rowListItem;
    private List<InCommCard> localInCommCardList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter payeGiftCardListAdapter;
    private TemplateSelectionInterfaces templateSelectionInterfaces;
    private int currentPos = 0;
    private int selectedCardPos = -1;
    private SemiBoldButton tv_place_order_giftcard;
    private int logincount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egift_card);
        isShowBasketIcon = false;
        mContext = this;
        eGiftRecyclerListView = (RecyclerView) findViewById(R.id.eGiftRecyclerListView);
        noGiftCardListText = (TextView) findViewById(R.id.noGiftCardListText);
        eGiftCardRootLayout = (RelativeLayout) findViewById(R.id.eGiftCardRootLayout);
        eGiftHeader = (RelativeLayout) findViewById(R.id.eGiftHeader);
        tv_place_order_giftcard = (SemiBoldButton) findViewById(R.id.tv_place_order_giftcard);
        templateSelectionInterfaces = this;
        rowListItem = new ArrayList<>();
        initToolbar();
        getAllGiftCards();

        tv_place_order_giftcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rowListItem != null && rowListItem.size() > 0) {
                    verifyAndPlaceOrder();
                } else {
                    tv_place_order_giftcard.setOnClickListener(null);
                }
            }
        });
    }

    private void setUI() {
        for (int i = 0; i < localInCommCardList.size(); i++) {
            PayGiftCard payGiftCard = new PayGiftCard(false, localInCommCardList.get(i));
            rowListItem.add(payGiftCard);
        }
        if (rowListItem != null && rowListItem.size() > 0) {
            // rowListItem.get(currentPos).setSelected(true);
            mLayoutManager = new LinearLayoutManager(mContext);
            eGiftRecyclerListView.setLayoutManager(mLayoutManager);
            payeGiftCardListAdapter = new PayeGiftCardListAdapter(mContext, rowListItem, templateSelectionInterfaces);
            eGiftRecyclerListView.setAdapter(payeGiftCardListAdapter);
        } else {

            noGiftCardListText.setVisibility(View.VISIBLE);
        }
    }

    //Refresh viewed gift cards in list
    private void getAllGiftCards() {
        if (GiftCardDataManager.getInstance().getInCommUser() != null) {
            enableScreen(false);
            String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
            InCommCardService.getAllCard(userId, new InCommGetAllCardServiceCallBack() {
                @Override
                public void onGetAllCardServiceCallback(List<InCommCard> allCards, Exception exception) {
                    if (exception == null) {
                        logincount = 0;
                        if (allCards != null && allCards.size() > 0) {
                            HashMap<Integer, InCommCard> inCommCardHashMap = new HashMap<Integer, InCommCard>();
                            for (int i = 0; i < allCards.size(); i++) {
                                if (allCards.get(i) != null) {
                                    inCommCardHashMap.put(allCards.get(i).getCardId(), allCards.get(i));
                                }
                            }
                            GiftCardDataManager.getInstance().setUserAllCards(new HashMap<Integer, InCommCard>(inCommCardHashMap));
                            GiftCardDataManager.getInstance().setInCommCards(new ArrayList<InCommCard>(allCards));
                            localInCommCardList = new ArrayList<>(GiftCardDataManager.getInstance().getInCommCards());
                            setUI();
                        } else {
                            noGiftCardListText.setVisibility(View.VISIBLE);
                        }
                        enableScreen(true);
                    } else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized
                            || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)
                            || Utils.getErrorDescription(exception).contains(Constants.IncommTokenExpired)) {
                        enableScreen(false);
                        IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                            @Override
                            public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                                enableScreen(true);
                                if (successFlag) {
                                    DataManager.getInstance().setInCommToken(tokenSummary);
                                    ((JambaApplication) mContext.getApplicationContext()).initializeInCommSDK();
                                    enableScreen(false);
                                    getAllGiftCards();
                                } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                    Utils.alert(mContext, "Failure", Constants.NO_INTERNET_CONNECTION);
                                } else {
                                    // Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                                    logincount++;
                                    if (logincount < 3) {
                                        enableScreen(false);
                                        JambaApplication.getAppContext().loginFb(UserService.getUser(), new FBSDKLoginServiceCallBack() {
                                            @Override
                                            public void fbSdkLoginCallBack(Boolean success) {
                                                enableScreen(true);
                                                if (FBPreferences.sharedInstance(mContext).getAccessTokenforapp() != null) {
                                                    getAllGiftCards();
                                                } else {
                                                    Utils.alert(mContext, "Failure", "Unable to proceed gift card service");
                                                }
                                            }
                                        });
                                    } else {
                                        enableScreen(true);
                                        logincount = 0;
                                        Utils.alert(mContext, "Failure", "Unable to proceed gift card service");
                                    }
                                }
                            }

                        });
                    } else if (exception != null) {
                        logincount = 0;
                        enableScreen(true);
                        if (Utils.getErrorDescription(exception) != null) {
                            netWorkAlert();
                        } else {
                            Utils.alert(mContext, null, Utils.responseErrorNull());
                        }
                    } else {
                        logincount = 0;
                        enableScreen(true);
                        Utils.alert(mContext, "Failure", "Unable to proceed gift card service");
                    }
                }
            });
        } else {
            enableScreen(true);
            Utils.alert(mContext, "Failure", "Unable to proceed gift card service");
        }
    }

    private void netWorkAlert() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Failure");
        alertDialogBuilder.setMessage("Please check your network connection and try again.");
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableScreen(false);
                getAllGiftCards();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    //Place order using pay via eGift card
    private void verifyAndPlaceOrder() {
        trackButtonWithName("Place Order");

        if (selectedCardPos == -1) {
            Utils.showAlert(this, "Please select any one jamba card", "Alert");
            return;
        }
        OloOrderInfo oloOrderInfo = DataManager.getInstance().getOrderInfo();
        oloOrderInfo.setBillingMethod("storedvalue");
        oloOrderInfo.setUserType("user");
        oloOrderInfo.setBillingSchemeId(Integer.parseInt(getResources().getString(R.string.olo_billing_scheme_id)));

        List<OloBillingField> oloBillingFields = new ArrayList<>();

        OloBillingField oloBillingField1 = new OloBillingField();
        oloBillingField1.setName("pin");
        oloBillingField1.setValue(rowListItem.get(selectedCardPos).geteGiftCardList().getCardPin());

        OloBillingField oloBillingField2 = new OloBillingField();
        oloBillingField2.setName("number");
        oloBillingField2.setValue(rowListItem.get(selectedCardPos).geteGiftCardList().getCardNumber());

        oloBillingFields.add(oloBillingField1);
        oloBillingFields.add(oloBillingField2);

        oloOrderInfo.setBillingFields(oloBillingFields);
        enableScreen(false);
        BasketService.placeOrder(this, new PlaceOrderCallback() {
            @Override
            public void onPlaceOrderCallback(OrderStatus status, Exception e) {
                if (e == null) {
                    JambaAnalyticsManager.sharedInstance().track_ItemWith(status.getId(), "ProductCount=" + String.valueOf(status.getProductsCount()) + " Total=" + String.valueOf(status.getTotal()), FBEventSettings.CHECKOUT);
                    orderSuccessfullyPlaced();
                } else {
                    Utils.showErrorAlert(EGiftCard.this, e);
                }
                enableScreen(true);
                tv_place_order_giftcard.setEnabled(true);
                tv_place_order_giftcard.setClickable(true);
            }
        });
    }

    //Select position to adapter
    @Override
    public void onSelection(int pos) {
        currentPos = pos;
        if (rowListItem.get(currentPos).getSelected()) {
            rowListItem.get(currentPos).setSelected(false);
            selectedCardPos = -1;
        } else {
            rowListItem.get(currentPos).setSelected(true);
            selectedCardPos = currentPos;
        }
        for (int i = 0; i < rowListItem.size(); i++) {
            if (i != currentPos) {
                rowListItem.get(i).setSelected(false);
            }
        }
        //rowListItem.get(currentPos).setSelected(true);
        eGiftRecyclerListView.getAdapter().notifyDataSetChanged();
    }

    private void initToolbar() {
        setUpToolBar(true, false);
        setBackButton(true, true);
        setTitle("Pay with Jamba Card");
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(R.color.background_white));
    }

    private void orderSuccessfullyPlaced() {
        //  CallRedeemedservices();
        Utils.notifyRemoveBasketUI(this);
        TransitionManager.transitFrom(EGiftCard.this, OrderConfirmationActivity.class);
        refreshGiftCard();
        finish();
    }

    private void CallRedeemedservices() {
        final String offerId = DataManager.getInstance().getCurrentBasket().getOfferId();
        if (StringUtilities.isValidString(offerId)) {
//            RedeemedService.getRedeemedservices(this, offerId, new RedeemedServiceCallback() {
//                @Override
//                public void onRedeemedServiceCallback(JSONObject offerSummary, String error) {
//
//                }
//            });
        }
    }

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent("BROADCAST_UPDATE_GF_HOME_ACTIVITY");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void refreshGiftCard() {
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        int cardId = rowListItem.get(currentPos).geteGiftCardList().getCardId();
        InCommCardService.getCardInfo(userId, cardId, new InCommCardServiceCallback() {
            @Override
            public void onCardServiceCallback(InCommCard card, Exception exception) {
                refreshGiftCardListActivity();
            }
        });
    }
}
