package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard.GiftCardHomeListActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.PurchaserActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.SelectAmountActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreditCard.ExistingSavedCreditCard;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardReload;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.squareup.picasso.Picasso;
import com.wearehathway.apps.incomm.Interfaces.InCommOrderServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommSaveUserPaymentAccountCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommVestaWebSessionDataCallback;
import com.wearehathway.apps.incomm.Models.InCommOrder;
import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;
import com.wearehathway.apps.incomm.Models.InCommReloadOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Models.InCommVestaWebSessionData;
import com.wearehathway.apps.incomm.Services.InCommOrderService;
import com.wearehathway.apps.incomm.Services.InCommUserPaymentAccountService;
import com.wearehathway.apps.incomm.Services.InCommVestaWebSessionService;

public class ReloadActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    Context context;
    int cardId;
    String cardPin;
    int dataIndex;
    private RelativeLayout reloadRootLayout;
    private RelativeLayout selectedImageReloadLayout;
    private ImageView selectedImageReload, selectedReloadCardImage;
    private int totalWidth, fullImageContentArea;
    private SemiBoldTextView RLBalance, RLCardNumber, selectReloadAmount, reloadCardNumber, reloadPurchaserMailAddress;
    private GiftCardDataManager giftCardDataManager;
    RelativeLayout selectReloadCardAmountLayout,reloadPaymentAccountLayout,reloadPurchaserLayout,RFooter;
    private double reloadAmount = 0.00;
    private String VestaOrgId;
    private String VestaWebSessionId;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true,true);
        setBackButton(false,false);

        giftCardDataManager = GiftCardDataManager.getInstance();

        reloadRootLayout = (RelativeLayout) findViewById(R.id.reloadRootLayout);
        RFooter = (RelativeLayout) findViewById(R.id.RFooter);
        selectedImageReloadLayout = (RelativeLayout) findViewById(R.id.selectedImageReloadLayout);
        selectReloadCardAmountLayout = (RelativeLayout) findViewById(R.id.selectReloadCardAmountLayout);
        reloadPaymentAccountLayout = (RelativeLayout) findViewById(R.id.reloadPaymentAccountLayout);
        reloadPurchaserLayout = (RelativeLayout) findViewById(R.id.reloadPurchaserLayout);

        selectedImageReload = (ImageView) findViewById(R.id.selectedImageReload);
        selectedReloadCardImage = (ImageView) findViewById(R.id.selectedReloadCardImage);

        RLBalance = (SemiBoldTextView) findViewById(R.id.RLBalance);
        RLCardNumber = (SemiBoldTextView) findViewById(R.id.RLCardNumber);
        selectReloadAmount = (SemiBoldTextView) findViewById(R.id.selectReloadAmount);
        reloadCardNumber = (SemiBoldTextView) findViewById(R.id.reloadCardNumber);
        reloadPurchaserMailAddress = (SemiBoldTextView) findViewById(R.id.reloadPurchaserMailAddress);

        webView= (WebView) findViewById(R.id.webView);

        selectReloadCardAmountLayout.setOnClickListener(this);
        reloadPaymentAccountLayout.setOnClickListener(this);
        reloadPurchaserLayout.setOnClickListener(this);
        RFooter.setOnClickListener(this);

        GiftCardReload giftCardReload=new GiftCardReload();
        giftCardReload.setAmount(5.00);
        GiftCardDataManager.getInstance().setGiftCardReload(giftCardReload);

        initToolbar();
        resizeView();
    }

    private void initToolbar() {
        setTitle("Reload");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
    }

    private void resizeView() {

        ViewTreeObserver observer = reloadRootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                //Set height for Gift Card Image.
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    reloadRootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    reloadRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                totalWidth = reloadRootLayout.getMeasuredWidth();

                fullImageContentArea = (int) (0.70 * totalWidth);

                LinearLayout.LayoutParams fullImageParams = (LinearLayout.LayoutParams) selectedImageReloadLayout.getLayoutParams();
                fullImageParams.height = fullImageContentArea;
                selectedImageReloadLayout.setLayoutParams(fullImageParams);

            }
        });
        setData();
    }

    private void setData() {
        //Set the Data to appropriate field if it is available.
        cardId = getIntent().getIntExtra("cardId", 0);
        dataIndex = getIntent().getIntExtra("dataIndex", -1);
        String imgUrl = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getImageUrl();
        cardPin = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getCardPin();

        //Set Image if URL is available
        if (imgUrl != null) {

            Ion.with(this)
                    .load(imgUrl)
                    .withBitmap()
                    .placeholder(R.drawable.product_placeholder)
                    .error(R.drawable.product_placeholder)
                    .intoImageView(selectedImageReload);
        }
        //Set Card Number
        if (giftCardDataManager.getUserAllCards().get(cardId).getCardNumber() != null) {
            RLCardNumber.setText(giftCardDataManager.getUserAllCards().get(cardId).getCardNumber());
        }

        //Set Card Balance
        if (String.valueOf(giftCardDataManager.getUserAllCards().get(cardId).getBalance()) != null) {
            RLBalance.setText("$" + String.format("%.2f", giftCardDataManager.getUserAllCards().get(cardId).getBalance()));
        }

        if (giftCardDataManager.getGiftCardReload() != null) {

            //Set Reload Amount
            if (String.valueOf(giftCardDataManager.getGiftCardReload().getAmount()) != null) {
                giftCardDataManager.getGiftCardReload().setAmount(GiftCardDataManager.getInstance().getGiftCardReload().getAmount());
                selectReloadAmount.setText("$" + String.format("%.2f", GiftCardDataManager.getInstance().getGiftCardReload().getAmount()));
                reloadAmount = GiftCardDataManager.getInstance().getGiftCardReload().getAmount();
            } else {
                selectReloadAmount.setText("$0.00");
            }

            //Set payment account info
            if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo() != null) {
                String cardNumber = GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getCreditCardNumber();
                if (cardNumber != null) {
                    selectedReloadCardImage.setVisibility(View.VISIBLE);
                    reloadCardNumber.setVisibility(View.VISIBLE);
                    String strLastFourDi = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : "";
                    reloadCardNumber.setText(strLastFourDi);
                    for (int i = 0; i < GiftCardDataManager.getInstance().getCreditCardTypes().size(); i++) {
                        if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getCreditCardType().equalsIgnoreCase(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getCreditCardType())) {
                            Picasso.with(context)
                                    .load(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getThumbnailImageUrl())
                                    .placeholder(R.drawable.product_placeholder)
                                    .error(R.drawable.product_placeholder)
                                    .into(selectedReloadCardImage);
                        }

                    }

                } else {
                    selectedReloadCardImage.setVisibility(View.GONE);
                  //  reloadCardNumber.setVisibility(View.GONE);
                }
            }

            //Set purchaser info details
            if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo() != null) {
                String purFirstname = GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getFirstName();
                if (purFirstname != null) {
                    reloadPurchaserMailAddress.setVisibility(View.VISIBLE);
                   // reloadPurchaserMailAddress.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getEmailAddress());
                    reloadPurchaserMailAddress.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getFirstName()+" "+GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getLastName().substring(0,1).toUpperCase()+".");
                }
            } else {
               // reloadPurchaserMailAddress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GiftCardDataManager.getInstance().resetGiftCardReload();//Reset Gift Card reload info.
        GiftCardDataManager.getInstance().resetReloadLocalPaymentInfo();//Reset Gift card reload payment info.
        GiftCardDataManager.getInstance().setIsChecked(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectReloadCardAmountLayout:
                Bundle amount = new Bundle();
                amount.putBoolean("isFromReload", true);
                TransitionManager.transitFrom(ReloadActivityGiftCard.this, SelectAmountActivityGiftCard.class, amount);
                break;
            case R.id.reloadPaymentAccountLayout:
                Bundle reloadPayment = new Bundle();
                reloadPayment.putBoolean("isFromReload", true);
                TransitionManager.transitFrom(ReloadActivityGiftCard.this, ExistingSavedCreditCard.class, reloadPayment);
                break;
            case R.id.reloadPurchaserLayout:
                Bundle purchase = new Bundle();
                purchase.putBoolean("isFromReload", true);
                if (GiftCardDataManager.getInstance().getGiftCardReload() == null) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo() == null) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getFirstName() == null) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                TransitionManager.transitFrom(ReloadActivityGiftCard.this, PurchaserActivityGiftCard.class, purchase);
                break;
            case R.id.RFooter:
                if (selectReloadAmount.getText().toString().equalsIgnoreCase("$0.00")) {
                    Utils.alert(context, "Alert", "Please enter amount");
                    return;
                }
                if (reloadAmount + giftCardDataManager.getUserAllCards().get(cardId).getBalance() > GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMaximumValue()) {
                    Utils.alert(context, "Alert", "Amount that you entered exceeds the maximum allowable Card balance of $500.00 .");
                    return;
                }
                if ((reloadCardNumber.getVisibility() != View.VISIBLE)
                        || (selectedReloadCardImage.getVisibility() != View.VISIBLE)
                        || (reloadCardNumber.getText().toString().equalsIgnoreCase(""))) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                if (reloadPurchaserMailAddress.getVisibility() != View.VISIBLE
                        || (reloadPurchaserMailAddress.getText().toString().equalsIgnoreCase(""))) {
                    Utils.alert(context, "Alert", "Please enter purchaser details");
                    return;
                }
                getVestaWebSessionData();
                break;
        }
    }


    private void getVestaWebSessionData(){
        enableScreen(false);
        loaderText("Reloading gift card...");
        InCommVestaWebSessionService.getVestaWebSessionData(new InCommVestaWebSessionDataCallback() {
            @Override
            public void onVestaWebSessionDataCallback(InCommVestaWebSessionData inCommVestaWebSessionData, Exception error) {
                if(inCommVestaWebSessionData != null){
                    VestaOrgId = inCommVestaWebSessionData.getVestaOrgId();
                    VestaWebSessionId = inCommVestaWebSessionData.getVestaWebSessionId();
                    GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().setVestaWebSessionId(VestaWebSessionId);
                    GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().setVestaOrgId(VestaOrgId);
                    webView();
                }else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                getVestaWebSessionData();
                            }
                        }
                    });
                } else if (error != null){
                    if (Utils.getErrorDescription(error) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(error), "genToken");
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Please check details and retry.", "genToken");
                    }
                }
            }
        });
    }

    private void webView() {
        final String content = "<html><body><p style='background:url(https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/clear.png?" +
                "org_id=" + VestaOrgId + "&session_id=" + VestaWebSessionId + "&m=1)'></p>\n" +
                "<img src='https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/clear.png?org_id=" + VestaOrgId + "&" +
                "session_id=" + VestaWebSessionId + "&m=2' alt='' />\n" +
                "<script src='https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/check.js?org_id=" + VestaOrgId + "&" +
                "session_id=" + VestaWebSessionId + "' type='text/javascript'></script>\n" +
                "<object data='https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/fp.swf?org_id=" + VestaOrgId + "&" +
                "session_id=" + VestaWebSessionId + "' type='application/x-shockwave-flash' width='1' height='1' id='obj_id'><param " +
                "value='https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/fp.swf?org_id=" + VestaOrgId + "&" +
                "session_id=" + VestaWebSessionId + "' name='movie'/></object></body></html>";

        webView.loadData(content, "text/html", null);
        submitOrderReload();
    }

    private void submitOrderReload() {
        final InCommOrderPurchaser inCommOrderPurchaser = GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo();
        InCommSubmitPayment inCommSubmitPayment = GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo();

        //Set info for Submit order.
        InCommReloadOrder inCommReloadOrder = new InCommReloadOrder();
        inCommReloadOrder.setCardId(cardId);
        inCommReloadOrder.setAmount(reloadAmount);
        inCommReloadOrder.setIsTestMode(true);
        inCommReloadOrder.setCardPin(cardPin);
        inCommReloadOrder.setPurchaser(inCommOrderPurchaser);
        inCommReloadOrder.setPayment(inCommSubmitPayment);
        enableScreen(false);

        loaderText("Reloading...");
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_RELOAD);//tracking successful reload
        InCommOrderService.submitOrderReload(inCommReloadOrder, new InCommOrderServiceCallback() {

            @Override
            public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error) {
                if (inCommOrderResponse != null && error == null) {
                    //If Save new payment is true, it goes to savePayment else it goes movePrevious.
                    if (GiftCardDataManager.getInstance().getIsSaveNewPayment() != null && GiftCardDataManager.getInstance().getIsSaveNewPayment()) {
                        savePayment();
                    } else {
                        alertSuccessBack();
                    }
                }else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized|| Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                submitOrderReload();
                            }
                        }
                    });
                }
                else if (error != null) {
                    if (Utils.getErrorDescription(error) != null) {
                        //If error description is available in Utils.
                        enableScreen(true);
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(error), "submitorder");
                    } else {
                        enableScreen(true);
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Please try again or check whether the entered details are correct.", "submitorder");
                    }
                } else {
                    enableScreen(true);
                    alertWithTryAgain(context, "Failure", Utils.responseErrorNull(), "submitorder");
                }

            }
        });

    }

    private void savePayment() {
        loaderText("Saving payment info...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommSubmitPayment inCommSubmitPayment = GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo();
        InCommUserPaymentAccount inCommUserPaymentAccount = new InCommUserPaymentAccount(inCommSubmitPayment, userId);
        InCommUserPaymentAccountService.saveUserPaymentAccount(userId, inCommUserPaymentAccount, new InCommSaveUserPaymentAccountCallback() {
            @Override
            public void onPaymentAccountSaveServiceCallback(InCommUserPaymentAccount paymentAccount, Exception exception) {
                if (paymentAccount != null) {
                    //After successfully payment saved reset the payment details.
                    GiftCardDataManager.getInstance().setAccountList(null);
                    GiftCardDataManager.getInstance().setIsSaveNewPayment(null);
                    alertSuccessBack();
                } else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        enableScreen(true);
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(exception), "payment");
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Therefore your payment information could not be saved in your account. Do you want to try again?", "payment");
                    }
                } else {
                    alertWithTryAgain(context, "Failure", Utils.responseErrorNull(), "payment");
                }
            }
        });
    }

    private void movePrevious() {
        enableScreen(true);
        Bundle homeList = new Bundle();
        homeList.putBoolean("isFromReload", true);
        TransitionManager.slideUp(ReloadActivityGiftCard.this, GiftCardHomeListActivityGiftCard.class, true, homeList);
    }

private void alertSuccessBack(){
    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
    alertDialogBuilder.setTitle("Success");
    alertDialogBuilder.setMessage("Gift Card Reloaded Successfully");
    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            movePrevious();
        }
    });
    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.setCancelable(false);
    alertDialog.setCanceledOnTouchOutside(false);
    alertDialog.show();
}
    private void alertWithTryAgain(final Context context, String Title, final String Message, final String whichFailed) {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (whichFailed.equalsIgnoreCase("payment")) {
                    savePayment();
                } else if (whichFailed.equalsIgnoreCase("submitorder")) {
                    webView();
                }else if(whichFailed.equalsIgnoreCase("genToken")){
                    getVestaWebSessionData();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (whichFailed.equalsIgnoreCase("payment")) {
                    enableScreen(true);
                    dialogInterface.dismiss();
                } else if (whichFailed.equalsIgnoreCase("submitorder")) {
                    enableScreen(true);
                    dialogInterface.dismiss();
                }else if(whichFailed.equalsIgnoreCase("genToken")){
                    enableScreen(true);
                    dialogInterface.dismiss();
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!(selectReloadAmount.getText().toString().equalsIgnoreCase("$0.00"))
                || ((reloadCardNumber.getVisibility() == View.VISIBLE) && (selectedReloadCardImage.getVisibility() == View.VISIBLE) &&  (!reloadCardNumber.getText().toString().equalsIgnoreCase("")))
                || ((reloadPurchaserMailAddress.getVisibility() == View.VISIBLE) &&  (!reloadPurchaserMailAddress.getText().toString().equalsIgnoreCase("")))) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Confirm");
            alertDialogBuilder.setMessage("Cancel gift card reload?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    navigateUp();
                }
            });
            alertDialogBuilder.setNegativeButton("No", null);
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            navigateUp();
        }
    }
}
