package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreditCard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.PurchaseNewCardActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard.AutoReloadActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard.ReloadActivityGiftCard;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardCreate;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardReload;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.wearehathway.apps.incomm.Interfaces.InCommCardAutoReloadServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommCardDeleteServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSavable;
import com.wearehathway.apps.incomm.Models.InCommBrandCreditCardType;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.incomm.Services.InCommUserPaymentAccountService;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    Created by Jeeva
 */
public class PaymentDetailsActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    private SemiBoldTextView cardNumber, expirationDate, fullName, streetAddress1, streetAddress2, country, state, city, zipCode;
    private ImageView cardImg;
    private int pos;
    private Context context;
    private RelativeLayout PAYDFooter;
    private ImageButton PAYDelete;
    private int accountId;
    private ArrayList<InCommUserPaymentAccount> accountList;
    private boolean isFromReload = false;
    private Boolean isItFromAutoReloadScreen = false;
    private int queueSize = 0;
    private int nextIndex = 0;
    private List<InCommAutoReloadSavable> autoReloadList;
    private List<InCommAutoReloadSavable> filteredAutoReloadList;
    private Integer[] autoReloadIds, cardIds;
    private int delAutoReloadTotalSize = 0;
    private int delAutoReloadQueueSize = 0;
    private int delAutoReloadNextIndex = 0;
    private TextView proBarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true);
        setBackButton(true,false);

        getBundle();

        cardImg = (ImageView) findViewById(R.id.cardImg);
        cardNumber = (SemiBoldTextView) findViewById(R.id.cardNumber);
        expirationDate = (SemiBoldTextView) findViewById(R.id.expirationDate);
        fullName = (SemiBoldTextView) findViewById(R.id.fullName);
        streetAddress1 = (SemiBoldTextView) findViewById(R.id.streetAddress1);
        streetAddress2 = (SemiBoldTextView) findViewById(R.id.streetAddress2);
        country = (SemiBoldTextView) findViewById(R.id.country);
        state = (SemiBoldTextView) findViewById(R.id.state);
        city = (SemiBoldTextView) findViewById(R.id.city);
        zipCode = (SemiBoldTextView) findViewById(R.id.zipCode);

        PAYDFooter = (RelativeLayout) findViewById(R.id.PAYDFooter);
        PAYDelete = (ImageButton) findViewById(R.id.PAYDelete);
        proBarText = (TextView)findViewById(R.id.proBarText);
        PAYDelete.setOnClickListener(this);
        PAYDFooter.setOnClickListener(this);

        initToolbar();
        setData();

    }

    private void initToolbar() {
        setTitle("Payment Details");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    private void setData() {
        accountList = new ArrayList<>(GiftCardDataManager.getInstance().getAccountList());
        accountId = accountList.get(pos).getId();
        List<InCommBrandCreditCardType> creditCardTypeList = new ArrayList<>(GiftCardDataManager.getInstance().getCreditCardTypes());
        if (accountList != null && creditCardTypeList != null) {
            for (int i = 0; i < creditCardTypeList.size(); i++) {
                if (creditCardTypeList.get(i).getCreditCardType().equalsIgnoreCase(accountList.get(pos).getCreditCardTypeCode())) {
                    Ion.with(context)
                            .load(creditCardTypeList.get(i).getThumbnailImageUrl())
                            .withBitmap()
                            .placeholder(R.drawable.product_placeholder)
                            .error(R.drawable.product_placeholder)
                            .intoImageView(cardImg);
                }
            }
        }
        if (accountList != null) {
            cardNumber.setText("xxxx xxxx xxxx " + accountList.get(pos).getCreditCardNumber());
            String month = getMonth(accountList.get(pos).getCreditCardExpirationMonth()).substring(0, 3);
            expirationDate.setText(month + " " + accountList.get(pos).getCreditCardExpirationYear());
            fullName.setText(accountList.get(pos).getFirstName() + " " + accountList.get(pos).getLastName());
            streetAddress1.setText(accountList.get(pos).getStreetAddress1());
            streetAddress2.setText(accountList.get(pos).getStreetAddress2());
            country.setText(accountList.get(pos).getCountry());
            state.setText(accountList.get(pos).getStateProvince());
            city.setText(accountList.get(pos).getCity());
            zipCode.setText(accountList.get(pos).getZipPostalCode());
        }
    }

    private void setPaymentValuesForAutoReload() {
        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() != null) {
            GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setPaymentAccountId(accountId);

            GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().setCreditCardNumber(accountList.get(pos).getCreditCardNumber());
            GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().setCreditCardType(accountList.get(pos).getCreditCardTypeCode());
        }
    }

    //Getting bundle
    private void getBundle() {
        pos = getIntent().getIntExtra("position", 0);
        isItFromAutoReloadScreen = getIntent().getBooleanExtra("isItFromAutoReloadScreen", false);
        isFromReload = getIntent().getBooleanExtra("isFromReload", false);

    }

    private void setPaymentValuesForReload() {
        if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
            if (GiftCardDataManager.getInstance().getIsChecked()) {
                if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo() != null) {
                    GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().setFirstName(accountList.get(pos).getFirstName());
                    GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().setLastName(accountList.get(pos).getLastName());
                    GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().setCountry(accountList.get(pos).getCountry());
                }
            }
            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardReload().getAmount());
            inCommOrderPayment.setPaymentAccountId(accountId);
            GiftCardDataManager.getInstance().getGiftCardReload().setPaymentInfo(inCommOrderPayment);

            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardNumber(accountList.get(pos).getCreditCardNumber());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardType(accountList.get(pos).getCreditCardTypeCode());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCountry(accountList.get(pos).getCountry());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setFirstName(accountList.get(pos).getFirstName());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setLastName(accountList.get(pos).getLastName());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setStateCode(null);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(false);

        } else {
            GiftCardReload giftCardReload = new GiftCardReload();
            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            GiftCardDataManager.getInstance().setGiftCardReload(giftCardReload);

            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardReload().getAmount());
            inCommOrderPayment.setPaymentAccountId(accountId);
            GiftCardDataManager.getInstance().getGiftCardReload().setPaymentInfo(inCommOrderPayment);
            GiftCardDataManager.getInstance().setGiftCardReload(giftCardReload);

            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardNumber(accountList.get(pos).getCreditCardNumber());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardType(accountList.get(pos).getCreditCardTypeCode());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCountry(accountList.get(pos).getCountry());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setFirstName(accountList.get(pos).getFirstName());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setLastName(accountList.get(pos).getLastName());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setStateCode(null);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(false);
        }
    }

    private void setPaymentValuesForCreate() {
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            if (GiftCardDataManager.getInstance().getIsChecked()) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo() != null) {
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().setFirstName(accountList.get(pos).getFirstName());
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().setLastName(accountList.get(pos).getLastName());
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().setCountry(accountList.get(pos).getCountry());
                }
            }
            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardCreate().getTotalOrderAmount());
            inCommOrderPayment.setPaymentAccountId(accountId);
            GiftCardDataManager.getInstance().getGiftCardCreate().setPaymentInfo(inCommOrderPayment);

            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardNumber(accountList.get(pos).getCreditCardNumber());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardType(accountList.get(pos).getCreditCardTypeCode());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCountry(accountList.get(pos).getCountry());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setFirstName(accountList.get(pos).getFirstName());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setLastName(accountList.get(pos).getLastName());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setStateCode(null);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(false);

        } else {
            GiftCardCreate giftCardCreate = new GiftCardCreate();
            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            giftCardCreate.setTotalOrderAmount(0.00);
            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);

            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardCreate().getTotalOrderAmount());
            inCommOrderPayment.setPaymentAccountId(accountId);
            GiftCardDataManager.getInstance().getGiftCardCreate().setPaymentInfo(inCommOrderPayment);
            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);

            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardNumber(accountList.get(pos).getCreditCardNumber());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardType(accountList.get(pos).getCreditCardTypeCode());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCountry(accountList.get(pos).getCountry());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setFirstName(accountList.get(pos).getFirstName());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setLastName(accountList.get(pos).getLastName());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setStateCode(null);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(false);
        }
    }

    //Before deleting credit card we have to delete auto reloads which is associate with this credit card
    //Getting all auto reload ids
    private void searchAndDeleteAutoReloads() {
        HashMap<Integer, InCommCard> allGiftCardsHashMap = new HashMap<Integer, InCommCard>(GiftCardDataManager.getInstance().getUserAllCards());
        List<InCommCard> allGiftCardsList = new ArrayList<InCommCard>(GiftCardDataManager.getInstance().getInCommCards());

        HashMap<Integer, InCommCard> allGiftCardsWithAutoReloadHashMap = new HashMap<Integer, InCommCard>();
        List<InCommCard> allGiftCardsWithAutoReloadList = new ArrayList<InCommCard>();

        for (int i = 0; i < allGiftCardsHashMap.size(); i++) {
            if (allGiftCardsHashMap.get(allGiftCardsList.get(i).getCardId()).getAutoReloadId() != 0) {
                allGiftCardsWithAutoReloadHashMap.put(allGiftCardsList.get(i).getCardId(), allGiftCardsHashMap.get(allGiftCardsList.get(i).getCardId()));
                allGiftCardsWithAutoReloadList.add(allGiftCardsList.get(i));
            }
        }

        queueSize = allGiftCardsWithAutoReloadHashMap.size();
        autoReloadIds = new Integer[allGiftCardsWithAutoReloadHashMap.size()];
        cardIds = new Integer[allGiftCardsWithAutoReloadHashMap.size()];

        for (int i = 0; i < allGiftCardsWithAutoReloadHashMap.size(); i++) {
            autoReloadIds[i] = allGiftCardsWithAutoReloadHashMap.get(allGiftCardsWithAutoReloadList.get(i).getCardId()).getAutoReloadId();
            cardIds[i] = allGiftCardsWithAutoReloadList.get(i).getCardId();
        }

        if (allGiftCardsWithAutoReloadHashMap.size() > 0) {
            nextIndex = 0;
            autoReloadList = new ArrayList<InCommAutoReloadSavable>();
            getAutoReloadInfo(autoReloadIds[nextIndex], cardIds[nextIndex]);
        } else {
            deleteCreditCard();
        }

    }


    //Getting auto Reload informations
    private void getAutoReloadInfo(int autoReloadId, int cardId) {
        loaderText("Processing...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommCardService.getAutoReloadInfo(userId, cardId, autoReloadId, new InCommCardAutoReloadServiceCallBack() {
            @Override
            public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception) {
                if (inCommAutoReloadSavable != null && exception == null) {
                    autoReloadList.add(inCommAutoReloadSavable);
                    queueSize--;
                    nextIndex++;
                    if (queueSize > 0) {
                        getAutoReloadInfo(autoReloadIds[nextIndex], cardIds[nextIndex]);
                    } else {
                        //Filtering auto reloads which is associate with this credit card only
                        filteredAutoReloadList = new ArrayList<InCommAutoReloadSavable>();
                        for (int i = 0; i < autoReloadList.size(); i++) {
                            if (accountId == autoReloadList.get(i).getPaymentAccountId()) {
                                filteredAutoReloadList.add(autoReloadList.get(i));
                            }
                        }
                        if (filteredAutoReloadList.size() > 0) {
                            delAutoReloadTotalSize = filteredAutoReloadList.size();
                            delAutoReloadQueueSize = filteredAutoReloadList.size();
                            delAutoReloadNextIndex = 0;
                            deleteAutoReload(filteredAutoReloadList.get(delAutoReloadNextIndex).getGiftCardId());
                        } else {
                            deleteCreditCard();
                        }
                    }
                }else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                getAutoReloadInfo(autoReloadIds[nextIndex], cardIds[nextIndex]);
                            }
                        }
                    });
                } else {
                    refreshGiftCardListActivity();
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(exception), "autoReloadList");
                    } else {
                        alertWithTryAgain(context, "Failure", "Failed to delete credit card. Do you want to try again?", "autoReloadList");
                    }
                }
            }
        });
    }

    //Deleting AUto Reloads which is using by this credit card
    private void deleteAutoReload(int cardId) {
        loaderText("Processing...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        int autoReloadId = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId();
        InCommCardService.deleteAutoReload(userId, cardId, autoReloadId, new InCommCardAutoReloadServiceCallBack() {
            @Override
            public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception) {
                if (inCommAutoReloadSavable == null && exception == null) {
                    delAutoReloadQueueSize--;
                    delAutoReloadNextIndex++;
                    if (delAutoReloadQueueSize > 0) {
                        deleteAutoReload(filteredAutoReloadList.get(delAutoReloadNextIndex).getGiftCardId());
                    } else {
                        deleteCreditCard();
                    }
                }else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                deleteAutoReload(filteredAutoReloadList.get(delAutoReloadNextIndex).getGiftCardId());
                            }
                        }
                    });
                } else {
                    refreshGiftCardListActivity();
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(exception) + String.valueOf((delAutoReloadTotalSize - delAutoReloadQueueSize)) + " out of " + String.valueOf(delAutoReloadTotalSize) + " Auto Reload configuration that are associated with this payment account has been deleted successfully. But failed to delete payment account. Do you want to try again?", "delAutoReload");
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. " + String.valueOf(delAutoReloadTotalSize - delAutoReloadQueueSize) + " out of " + String.valueOf(delAutoReloadTotalSize) + " Auto Reload configuration that are associated with this payment account has been deleted successfully. But failed to delete payment account. Do you want to try again?", "delAutoReload");
                    }
                }
            }
        });

    }

    //Finally Deleting Credit Card and restting values based on bundle
    private void deleteCreditCard() {
        loaderText("Deleting credit card...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        String creditCardId = String.valueOf(accountId);
        InCommUserPaymentAccountService.deleteCard(userId, creditCardId, new InCommCardDeleteServiceCallback() {
            @Override
            public void onCardDeleteServiceCallback(Exception exception) {
                enableScreen(true);
                if (exception == null) {
                    GiftCardDataManager.getInstance().getAccountList().remove(accountList.get(pos));
                    refreshGiftCardListActivity();
                    if (accountId == GiftCardDataManager.getInstance().getPaymentAccountID()) {
                        if (isFromReload) {
                            GiftCardDataManager.getInstance().resetReloadLocalPaymentInfo();
                            if (GiftCardDataManager.getInstance().getIsChecked()) {
                                GiftCardDataManager.getInstance().getGiftCardReload().resetPurchaserInfo();
                            }
                        } else if (isItFromAutoReloadScreen) {
                            GiftCardDataManager.getInstance().setAutoReloadLocalPaymentInfo(null);
                        } else {
                            GiftCardDataManager.getInstance().resetLocalPaymentInfo();
                            if (GiftCardDataManager.getInstance().getIsChecked()) {
                                GiftCardDataManager.getInstance().getGiftCardCreate().resetPurchaserInfo();
                            }
                            GiftCardDataManager.getInstance().getGiftCardCreate().setRecipientEmailAddress(null);
                        }
                    }
                    onBackPressed();
                }else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                deleteCreditCard();
                            }
                        }
                    });
                }
                else {
                    refreshGiftCardListActivity();
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(exception) + "All Auto Reload configuration that are associated with this payment account has been deleted successfully. But failed to delete payment account. Do you want to try again?", "deleteCreditCard");
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. All Auto Reload configuration that are associated with this payment account has been deleted successfully. But failed to delete payment account. Do you want to try again?", "deleteCreditCard");
                    }
                }
            }
        });
    }

    private void alertWithTryAgain(final Context context, String Title, String Message, final String whichFailed) {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableScreen(false);
                if (whichFailed.equalsIgnoreCase("autoReloadList")) {
                    getAutoReloadInfo(autoReloadIds[nextIndex], cardIds[nextIndex]);
                } else if (whichFailed.equalsIgnoreCase("delAutoReload")) {
                    deleteAutoReload(filteredAutoReloadList.get(delAutoReloadNextIndex).getGiftCardId());
                } else if (whichFailed.equalsIgnoreCase("deleteCreditCard")) {
                    deleteCreditCard();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableScreen(true);
                dialogInterface.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PAYDFooter:
                //Setting payment values based on bundle
                if (isItFromAutoReloadScreen) {
                    setPaymentValuesForAutoReload();
                    TransitionManager.transitFrom(PaymentDetailsActivityGiftCard.this, AutoReloadActivityGiftCard.class, true);
                } else if (isFromReload) {
                    setPaymentValuesForReload();
                    TransitionManager.transitFrom(PaymentDetailsActivityGiftCard.this, ReloadActivityGiftCard.class, true);
                } else {
                    setPaymentValuesForCreate();
                    TransitionManager.transitFrom(PaymentDetailsActivityGiftCard.this, PurchaseNewCardActivityGiftCard.class, true);
                }
                break;

            case R.id.PAYDelete:
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm");
                alertDialogBuilder.setMessage("Deleting this payment account will remove auto reload configurations associated with this payment account. Are you sure you want to remove this payment account?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        enableScreen(false);
                        loaderText("Processing...");
                        searchAndDeleteAutoReloads();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent("BROADCAST_UPDATE_GF_HOME_ACTIVITY");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
