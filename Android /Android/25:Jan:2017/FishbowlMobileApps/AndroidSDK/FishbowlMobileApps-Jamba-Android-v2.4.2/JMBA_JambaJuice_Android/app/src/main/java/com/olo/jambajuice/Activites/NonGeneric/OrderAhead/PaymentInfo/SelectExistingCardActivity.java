package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketProductViewHolder;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Confirmation.OrderConfirmationActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.PlaceOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RedeemedServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.RedeemedService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Models.OloOrderInfo;
import com.wearehathway.apps.olo.Services.OloBasketService;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class SelectExistingCardActivity extends BaseActivity implements View.OnClickListener {
    List<CardViewHolder> cardViewHolderList;
    ArrayList<BillingAccount> billingAccounts;
    int selectedCardAccountId = -1;
    private int payInstoreAccountId;

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    Button placeOrderBtn;
    Button addAnotherCard, usingeGiftCard;
    LinearLayout giftCardView;
    LinearLayout listCardContainer, otherPaymentLayout, payInStoreLayout;
    RelativeLayout tickImgPayInstore;
    TextView payDetailText, payTypeText,noSavedCardsText;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exisiting_card);
        context = this;
        if (DataManager.getInstance().getCurrentBasket() == null) {
            //Incase the data is not available and activity is recreating.
            finish();
            return;
        }
        initToolbar();
        setUpView();
        ArrayList<BillingAccount> billingAccountts = new ArrayList<>(DataManager.getInstance().getBillingAccount());
        ArrayList<BillingAccount> tempBillingAcc = new ArrayList<>(DataManager.getInstance().getBillingAccount());
        if (DataManager.getInstance().getCurrentBasket().getDeliverymode().equalsIgnoreCase("dispatch")) {
            for (BillingAccount billingAccount : billingAccountts) {
                if (billingAccount.getAccounttype().equalsIgnoreCase("payinstore")) {
                    tempBillingAcc.remove(billingAccount);
                }
            }
        }

        DataManager.getInstance().setBillingAccount(tempBillingAcc);
        billingAccounts = DataManager.getInstance().getBillingAccount();
        listCardContainer = (LinearLayout) findViewById(R.id.list_cards_info);
        addExistingCards();
        setUpPayWithAnotherCCandGCText();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_BASKET_UI));
    }

    private void setUpPayWithAnotherCCandGCText() {
        boolean isHavingSavedCC = false;
        boolean isHavingSavedGC = false;
        for (BillingAccount billingAccount : billingAccounts) {
            if (billingAccount.getAccounttype().equalsIgnoreCase("creditcard")) {
                isHavingSavedCC = true;
            }

            if (billingAccount.getAccounttype().equalsIgnoreCase("Jamba Cards")) {
                isHavingSavedGC = true;
            }
        }

        if (isHavingSavedCC) {
            addAnotherCard.setText("Pay with another Credit Card");
        } else {
            addAnotherCard.setText("Pay with Credit Card");
        }

        if (isHavingSavedGC) {
            usingeGiftCard.setText("Pay with another Jamba Card");
        } else {
            usingeGiftCard.setText("Pay with Jamba Card");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_place_order:
                placeOrderPressed();
                break;
            case R.id.addAnotherCard:
                TransitionManager.transitFrom(this, AddCardActivity.class);
                break;
            case R.id.usingeGiftCard:
                TransitionManager.transitFrom(this, EGiftCard.class);
                break;
            case R.id.payInStoreLayout:
                selectPayInStore();
                break;
        }
    }

    private void selectPayInStore() {
        if (tickImgPayInstore.getVisibility() == View.VISIBLE) {
            tickImgPayInstore.setVisibility(View.GONE);
            selectedCardAccountId = -1;
        } else {
            tickImgPayInstore.setVisibility(View.VISIBLE);
            selectedCardAccountId = payInstoreAccountId;
            resetSavedCards();
        }
    }

//    private void CallRedeemedservices() {
//        int offerId = DataManager.getInstance().getCurrentBasket().getPromoId();
//        if (StringUtilities.isValidString(offerId)) {
//            RedeemedService.getRedeemedservices(this, offerId, new RedeemedServiceCallback() {
//                @Override
//                public void onRedeemedServiceCallback(JSONObject offerSummary, String error) {
//
//
//                    if (offerSummary != null) {
//                        //  setDataOffer(offerSummary);
//                    } else {
//                        //   Utils.showErrorAlert(MyRewardsActivity.this, exception);
//                    }
//                }
//            });
//        }
//    }

    private void placeOrderPressed() {

        // Utils.alert(context,"Alert","Selected card account id is = "+selectedCardAccountId);

        if (selectedCardAccountId == -1) {
            Utils.showAlert(this, "Please select a payment method", "Payment Method");
            return;
        }


        OloOrderInfo oloOrderInfo = DataManager.getInstance().getOrderInfo();
        oloOrderInfo.setBillingMethod("billingaccount");
        oloOrderInfo.setBillingAccountId(selectedCardAccountId);
        enableScreen(false);
        BasketService.placeOrder(this, new PlaceOrderCallback() {
            @Override
            public void onPlaceOrderCallback(OrderStatus status, Exception e) {
                if (e == null) {
                    orderSuccessfullyPlaced();

                } else {
                    Utils.showErrorAlert(SelectExistingCardActivity.this, e);
                }
                enableScreen(true);
            }
        });
    }

    public void cardSelectedWithAccountId(int accountId) {
        if (selectedCardAccountId == accountId) {
            accountId = -1; // If same cell is pressed again then deselect that card.
        }
        for (CardViewHolder cardViewHolder : cardViewHolderList) {
            cardViewHolder.setSelected(accountId);
            resetPayInstoreLayout();
            if (cardViewHolder.billingAccount.getAccountid() != 0) {
                cardViewHolder.resetSwipe();
            }
        }
        selectedCardAccountId = accountId;
    }

    private void resetPayInstoreLayout() {
        if (tickImgPayInstore.getVisibility() == View.VISIBLE) {
            tickImgPayInstore.setVisibility(View.GONE);
            selectedCardAccountId = -1;
        }
    }

    public void deleteCard(final BillingAccount billingAccount) {
        enableScreen(false);
        String successMessage = "Card removed successfully.";
        if (billingAccount.getAccounttype().equalsIgnoreCase("creditcard")) {
            successMessage = "Credit card removed successfully.";
        } else if (billingAccount.getAccounttype().equalsIgnoreCase("Jamba Cards")) {
            successMessage = "Jamba card removed successfully.";
        }
        final String finalSuccessMessage = successMessage;
        OloBasketService.deleteSavedBillingAccount(billingAccount.getAccountid(), new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                enableScreen(true);
                if (error == null && response == null) {
                    if (cardViewHolderList != null) {
                        cardViewHolderList.clear();
                    }
                    listCardContainer.removeAllViews();
                    billingAccounts.remove(billingAccount);
                    addExistingCards();
                    Utils.showAlert(context, finalSuccessMessage, "Success!");
                } else {
                    Utils.showErrorAlert(SelectExistingCardActivity.this, error);
                }
                setUpPayWithAnotherCCandGCText();
            }
        });
    }

    private void orderSuccessfullyPlaced() {

        Utils.notifyRemoveBasketUI(this);
        //CallRedeemedservices();

        TransitionManager.transitFrom(SelectExistingCardActivity.this, OrderConfirmationActivity.class);
        finish();
    }

    public void showDeleteCardConfirmationAlert(final BillingAccount billingAccount) {
        String title = "Remove Card";
        String message = "Are you sure you want to delete this saved card?";
        if (billingAccount.getAccounttype().equalsIgnoreCase("creditcard")) {
            title = "Remove Credit Card";
            message = "Are you sure you want to delete this saved credit card?";
        } else if (billingAccount.getAccounttype().equalsIgnoreCase("Jamba Cards")) {
            title = "Remove Jamba Card";
            message = "Are you sure you want to delete this saved jamba card?";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deleteCard(billingAccount);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void addExistingCards() {
        boolean isSavedCardsAvailable = false;

        LayoutInflater layoutInflater = getLayoutInflater();
        cardViewHolderList = new ArrayList<>();

            for (BillingAccount billingAccount : billingAccounts) {

                if (billingAccount.getAccounttype().equalsIgnoreCase("payinstore")) {
                    payInStoreLayout.setVisibility(View.VISIBLE);
                    payInstoreAccountId = billingAccount.getAccountid();
                    payDetailText.setText(billingAccount.getCardSuffix());
                    payTypeText.setText(billingAccount.getCardtype());
                } else {
                    isSavedCardsAvailable = true;
                    View cardDetailView = layoutInflater.inflate(R.layout.row_payment_card, listCardContainer, false);
                    CardViewHolder cardViewHolder = new CardViewHolder(this, cardDetailView, billingAccount);
                    cardViewHolderList.add(cardViewHolder);
                    listCardContainer.addView(cardDetailView);
                }
            }


        if(!isSavedCardsAvailable){
            noSavedCardsText.setVisibility(View.VISIBLE);
        }else{
            noSavedCardsText.setVisibility(View.GONE);
        }
    }

    private void resetSavedCards() {
        if (cardViewHolderList != null) {
            cardViewHolderList.clear();
        }
        listCardContainer.removeAllViews();
        addExistingCards();
    }

    private void initToolbar() {
        isShowBasketIcon = false;
        setUpToolBar(true);
        setTitle("Select Payment Method");
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(android.R.color.white));
        setBackButton(true, true);
    }

    private void setUpView() {
        placeOrderBtn = (Button) findViewById(R.id.tv_place_order);
        addAnotherCard = (Button) findViewById(R.id.addAnotherCard);
        usingeGiftCard = (Button) findViewById(R.id.usingeGiftCard);
        giftCardView = (LinearLayout) findViewById(R.id.giftCardView);
        otherPaymentLayout = (LinearLayout) findViewById(R.id.otherPaymentLayout);
        payInStoreLayout = (LinearLayout) findViewById(R.id.payInStoreLayout);
        tickImgPayInstore = (RelativeLayout) findViewById(R.id.tickImgPayInstore);
        payDetailText = (TextView) findViewById(R.id.payDetailText);
        payTypeText = (TextView) findViewById(R.id.payTypeText);
        noSavedCardsText = (TextView) findViewById(R.id.noSavedCardsText);
        placeOrderBtn.setOnClickListener(this);
        addAnotherCard.setOnClickListener(this);
        usingeGiftCard.setOnClickListener(this);
        payInStoreLayout.setOnClickListener(this);

        if (UserService.isUserAuthenticated()) {
            if (DataManager.getInstance().isCurrentBasketSupportGiftCard()) {
                giftCardView.setVisibility(View.VISIBLE);
            } else {
                giftCardView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void enableScreen(boolean isEnabled) {
        super.enableScreen(isEnabled);
        placeOrderBtn.setEnabled(isEnabled);
    }

}
