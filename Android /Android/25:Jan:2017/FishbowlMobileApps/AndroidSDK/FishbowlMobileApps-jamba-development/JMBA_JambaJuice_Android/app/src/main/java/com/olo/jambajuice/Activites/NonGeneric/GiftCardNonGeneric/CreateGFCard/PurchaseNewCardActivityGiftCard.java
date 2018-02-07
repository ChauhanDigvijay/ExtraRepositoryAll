package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard.GiftCardHomeListActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreditCard.ExistingSavedCreditCard;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.IncommOrderIdCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.IncommUpdateTransaction;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.SubmitPromoOfferServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardCreate;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.BusinessLogic.Services.OrderIdService;
import com.olo.jambajuice.BusinessLogic.Services.SubmitPromoOfferService;
import com.olo.jambajuice.BusinessLogic.Services.UpdateTransactionService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.squareup.picasso.Picasso;
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommOrderServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommSaveUserPaymentAccountCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommVestaWebSessionDataCallback;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Models.InCommOrder;
import com.wearehathway.apps.incomm.Models.InCommOrderItem;
import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;
import com.wearehathway.apps.incomm.Models.InCommOrderRecipientDetails;
import com.wearehathway.apps.incomm.Models.InCommSubmitOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;
import com.wearehathway.apps.incomm.Models.InCommSubmittedOrderItemGiftCards;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Models.InCommVestaWebSessionData;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.incomm.Services.InCommOrderService;
import com.wearehathway.apps.incomm.Services.InCommUserPaymentAccountService;
import com.wearehathway.apps.incomm.Services.InCommVestaWebSessionService;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
    Created by Jeeva
 */
@SuppressWarnings("deprecation")
public class PurchaseNewCardActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    private static int status;
    private static String currentUpdate;
    private static String sucessTitle, sucessMsg;
    private static boolean sucessisSelf;
    private RelativeLayout PNCRootLayout, selectGFDesignLayout, PNCHeader, PNCFooter, recepientLayout, selectQuantityLayout, paymentAccountLayout, purchaserLayout;
    private LinearLayout selectCardAmountLayout;
    private SemiBoldTextView cardAmount, totalOrderAmount, SGFDesignText, recipientMailAddress, givenCardNumber, purchaserMailAddress, changeDesignText;
    private Context context;
    private int totalHeight, headerHeight, footerHeight, textViewContentArea;
    private ImageView PNCImage, creditCardImg;
    private Spinner quantity;
    private String[] items;
    private ArrayList<String> quantityList = new ArrayList<>();
    private int queueSize = 0;
    private int totalCardSize = 0;
    private int nextIndex;
    private String[] token;
    private Boolean isSelf;
    private List<InCommSubmittedOrderItemGiftCards> createdGiftCards;
    private List<InCommSubmittedOrderItemGiftCards> failureGiftCards;
    private TextView proBarText;
    private String VestaOrgId;
    private String VestaWebSessionId;
    private WebView webView;
    private boolean isBackButtonEnabled = true;
    private String TransactionOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_new_card);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true, true);
        setBackButton(false, false);

        PNCRootLayout = (RelativeLayout) findViewById(R.id.PNCRootLayout);
        selectGFDesignLayout = (RelativeLayout) findViewById(R.id.selectGFDesignLayout);
        PNCHeader = (RelativeLayout) findViewById(R.id.PNCHeader);
        PNCFooter = (RelativeLayout) findViewById(R.id.PNCFooter);
        recepientLayout = (RelativeLayout) findViewById(R.id.recepientLayout);
        purchaserLayout = (RelativeLayout) findViewById(R.id.purchaserLayout);

        selectCardAmountLayout = (LinearLayout) findViewById(R.id.selectCardAmountLayout);
        selectQuantityLayout = (RelativeLayout) findViewById(R.id.selectQuantityLayout);
        paymentAccountLayout = (RelativeLayout) findViewById(R.id.paymentAccountLayout);

        cardAmount = (SemiBoldTextView) findViewById(R.id.cardAmount);
        totalOrderAmount = (SemiBoldTextView) findViewById(R.id.totalOrderAmount);
        SGFDesignText = (SemiBoldTextView) findViewById(R.id.SGFDesignText);
        recipientMailAddress = (SemiBoldTextView) findViewById(R.id.recipientMailAddress);
        givenCardNumber = (SemiBoldTextView) findViewById(R.id.givenCardNumber);
        purchaserMailAddress = (SemiBoldTextView) findViewById(R.id.purchaserMailAddress);
        changeDesignText = (SemiBoldTextView) findViewById(R.id.changeDesignText);

        PNCImage = (ImageView) findViewById(R.id.PNCImage);
        creditCardImg = (ImageView) findViewById(R.id.creditCardImg);

        proBarText = (TextView) findViewById(R.id.proBarText);

        webView = (WebView) findViewById(R.id.webView);

        quantity = (Spinner) findViewById(R.id.quantity);
        int size = 0;
        if (GiftCardDataManager.getInstance().getBrands() != null
                && GiftCardDataManager.getInstance().getBrands().get(0) != null) {
            int[] quantities = GiftCardDataManager.getInstance().getBrands().get(0).getQuantities();
            size = quantities.length;
            items = new String[size];
            for (int i = 0; i < size; i++) {
                items[i] = String.valueOf(quantities[i]);
                quantityList.add(String.valueOf(quantities[i]));
            }
        } else {
            Toast.makeText(context, "Something went wrong,please try again later", Toast.LENGTH_SHORT).show();
            finish();
        }
     //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, quantityList);
        quantity.setAdapter(adapter);

        selectGFDesignLayout.setOnClickListener(this);
        changeDesignText.setOnClickListener(this);
        selectCardAmountLayout.setOnClickListener(this);
        selectQuantityLayout.setOnClickListener(this);
        paymentAccountLayout.setOnClickListener(this);
        purchaserLayout.setOnClickListener(this);
        recepientLayout.setOnClickListener(this);
        PNCFooter.setOnClickListener(this);

        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
                    GiftCardDataManager.getInstance().getGiftCardCreate().setQuantity(Integer.parseInt(quantity.getSelectedItem().toString()));
                    if (GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity() != 0 && GiftCardDataManager.getInstance().getGiftCardCreate().getAmount() != null) {
                        float amount = Float.parseFloat(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount());
                        float quantity = Float.parseFloat(String.valueOf(GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity()));
                        Double totalAmount = Double.parseDouble(String.valueOf(amount * quantity));
                        GiftCardDataManager.getInstance().getGiftCardCreate().setTotalOrderAmount(totalAmount);
                        totalOrderAmount.setText(String.format("%.2f", totalAmount));
                    } else {
                        totalOrderAmount.setText("00.00");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Default template and amount
        GiftCardCreate giftCardCreate = new GiftCardCreate();
        giftCardCreate.setImageCode(GiftCardDataManager.getInstance().getBrands().get(0).getCardImages().get(0));
        giftCardCreate.setAmount(5.00);
        GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);


        initToolbar();
        resizeView();

    }

    private void initToolbar() {
        setTitle("Purchase New Card");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.background_white));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GiftCardDataManager.getInstance().resetGiftCardCreate();
        GiftCardDataManager.getInstance().resetLocalPaymentInfo();
        GiftCardDataManager.getInstance().setIsChecked(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void resizeView() {

        ViewTreeObserver observer = PNCRootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    PNCRootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    PNCRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                totalHeight = PNCRootLayout.getMeasuredHeight();

                headerHeight = PNCHeader.getHeight();
                footerHeight = PNCFooter.getHeight();


                int contentArea = totalHeight - (headerHeight + footerHeight);
                textViewContentArea = (int) (contentArea * 0.5);

                RelativeLayout.LayoutParams textAreaParams = (RelativeLayout.LayoutParams) selectGFDesignLayout.getLayoutParams();
                textAreaParams.height = textViewContentArea;
                selectGFDesignLayout.setLayoutParams(textAreaParams);

                int paddingCommon = getResources().getDimensionPixelSize(R.dimen.new_card_padding);

                selectGFDesignLayout.setPadding(paddingCommon, paddingCommon, paddingCommon, paddingCommon / 4);

                setData();

            }

        });
    }

    private void setData() {

        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {

            GiftCardDataManager.getInstance().getGiftCardCreate().setQuantity(Integer.parseInt(quantity.getSelectedItem().toString()));

            //Showing selected design from select design screen
            if (GiftCardDataManager.getInstance().getGiftCardCreate().getSelectedCardImageUrl() != null) {
                SGFDesignText.setVisibility(View.GONE);
                PNCImage.setVisibility(View.VISIBLE);
                changeDesignText.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(GiftCardDataManager.getInstance().getGiftCardCreate().getSelectedCardImageUrl())
                        .placeholder(R.drawable.product_placeholder)
                        .error(R.drawable.product_placeholder)
                        .into(PNCImage);
            } else {
                SGFDesignText.setVisibility(View.VISIBLE);
                PNCImage.setVisibility(View.GONE);
                changeDesignText.setVisibility(View.GONE);
            }

            //Showing selected or entered amount from select amount screen
            if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount() != null) {
                cardAmount.setText(String.format("%.2f", Double.parseDouble(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount())));
            } else {
                cardAmount.setText("0.00");
            }

            //showing selected payment info or entered payment info from payment details or add new credit card screen
            if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo() != null) {
                String cardNumber = GiftCardDataManager.getInstance().getLocalPaymentInfo().getCreditCardNumber();
                if (cardNumber != null) {
                    creditCardImg.setVisibility(View.VISIBLE);
                    givenCardNumber.setVisibility(View.VISIBLE);
                    String strLastFourDi = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : "";
                    givenCardNumber.setText(strLastFourDi);
                    for (int i = 0; i < GiftCardDataManager.getInstance().getCreditCardTypes().size(); i++) {
                        if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getCreditCardType().equalsIgnoreCase(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getCreditCardType())) {
                            Picasso.with(context)
                                    .load(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getThumbnailImageUrl())
                                    .placeholder(R.drawable.product_placeholder)
                                    .error(R.drawable.product_placeholder)
                                    .into(creditCardImg);
                        }

                    }

                } else {
                    creditCardImg.setVisibility(View.GONE);
                  //  givenCardNumber.setVisibility(View.GONE);
                }

                //Showing entered purchasing mail address from purchaser screen
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo() != null) {
                    String purFirstname = GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName();
                    if (purFirstname != null) {
                        purchaserMailAddress.setVisibility(View.VISIBLE);
                       // purchaserMailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
                        purchaserMailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName()+" "+GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getLastName().substring(0,1).toUpperCase()+".");
                    }
                } else {
                   // purchaserMailAddress.setVisibility(View.GONE);
                }

                //Showing entered recipient mail address from recipient screen
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientEmailAddress() == null || GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientEmailAddress().equalsIgnoreCase("")) {
                   // recipientMailAddress.setVisibility(View.GONE);
                } else {
                    if (GiftCardDataManager.getInstance().getIsSelf()) {
                        GiftCardDataManager.getInstance().getGiftCardCreate().setRecipientEmailAddress(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
                        recipientMailAddress.setVisibility(View.VISIBLE);
                        recipientMailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientEmailAddress());
                    } else {
                        recipientMailAddress.setVisibility(View.VISIBLE);
                        recipientMailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientEmailAddress());
                    }
                }

                //Updating total order amount for every amount from amount screen
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity() != 0 && GiftCardDataManager.getInstance().getGiftCardCreate().getAmount() != null) {
                    float amount = Float.parseFloat(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount());
                    float quantity = Float.parseFloat(String.valueOf(GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity()));
                    Double totalAmount = Double.parseDouble(String.valueOf(amount * quantity));
                    GiftCardDataManager.getInstance().getGiftCardCreate().setTotalOrderAmount(totalAmount);
                    totalOrderAmount.setText(String.format("%.2f", totalAmount));
                } else {
                    totalOrderAmount.setText("00.00");
                }
            }
        }

    }

    private void openDialogue() {
        quantity.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectGFDesignLayout:
                TransitionManager.slideUp(PurchaseNewCardActivityGiftCard.this, SelectDesignActivityGiftCard.class);
                break;
            case R.id.changeDesignText:
                TransitionManager.slideUp(PurchaseNewCardActivityGiftCard.this, SelectDesignActivityGiftCard.class);
                break;
            case R.id.selectCardAmountLayout:
                TransitionManager.transitFrom(PurchaseNewCardActivityGiftCard.this, SelectAmountActivityGiftCard.class);
                break;
            case R.id.selectQuantityLayout:
                openDialogue();
                break;
            case R.id.paymentAccountLayout:
                TransitionManager.transitFrom(PurchaseNewCardActivityGiftCard.this, ExistingSavedCreditCard.class);
                break;
            case R.id.purchaserLayout:
                if (GiftCardDataManager.getInstance().getGiftCardCreate() == null) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo() == null) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getFirstName() == null) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                TransitionManager.transitFrom(PurchaseNewCardActivityGiftCard.this, PurchaserActivityGiftCard.class);
                break;
            case R.id.recepientLayout:
                if (GiftCardDataManager.getInstance().getGiftCardCreate() == null) {
                    Utils.alert(context, "Alert", "Please enter purchaser details");
                    return;
                }
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo() == null) {
                    Utils.alert(context, "Alert", "Please enter purchaser details");
                    return;
                }
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress() == null) {
                    Utils.alert(context, "Alert", "Please enter purchaser details");
                    return;
                }
                TransitionManager.transitFrom(PurchaseNewCardActivityGiftCard.this, RecipientActivityGiftCard.class);
                break;

            case R.id.PNCFooter:
                if (PNCImage.getVisibility() != View.VISIBLE) {
                    Utils.alert(context, "Alert", "Please choose a gift card design");
                    return;
                }
                if (cardAmount.getText().toString().equalsIgnoreCase("0.00")) {
                    Utils.alert(context, "Alert", "Please enter amount");
                    return;
                }
                if ((givenCardNumber.getVisibility() != View.VISIBLE)
                        || (creditCardImg.getVisibility() != View.VISIBLE)
                        || (givenCardNumber.getText().toString().equalsIgnoreCase(""))) {
                    Utils.alert(context, "Alert", "Please enter payment information");
                    return;
                }
                if (purchaserMailAddress.getVisibility() != View.VISIBLE
                        || (purchaserMailAddress.getText().toString().equalsIgnoreCase(""))) {
                    Utils.alert(context, "Alert", "Please enter purchaser details");
                    return;
                }
                if (recipientMailAddress.getVisibility() != View.VISIBLE
                        || (recipientMailAddress.getText().toString().equalsIgnoreCase(""))) {
                    Utils.alert(context, "Alert", "Please enter recipient details");
                    return;
                }
                if (TransactionOrderId == null) {
                    getOrderId();
                } else {
                    getVestaWebSessionData();
                }
                break;
        }
    }

    private void getOrderId() {
        enableScreen(false);
        isBackButtonEnabled = false;
        loaderText("Purchasing gift card...");
        String userId = UserService.getUser().getSpendGoId();
        OrderIdService.getIncommOrderId(userId, new IncommOrderIdCallback() {
            @Override
            public void onIncommOrderIdCallback(String incommOrderId, Boolean successFlag, Exception error) {
                if (successFlag) {
                    TransactionOrderId = incommOrderId;
                    getVestaWebSessionData();
                } else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized
                        || Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)
                        || Utils.getErrorDescription(error).contains(Constants.IncommTokenExpired)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                getOrderId();
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                            }
                        }
                    });
                } else if (error != null) {
                    if (Utils.getErrorDescription(error) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(error), "orderid", false);
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Please check details and retry.", "orderid", GiftCardDataManager.getInstance().getIsSelf());
                    }
                }
            }
        });

    }

    //Generating session id and org id
    private void getVestaWebSessionData() {
        enableScreen(false);
        isBackButtonEnabled = false;
        loaderText("Purchasing gift card...");
        InCommVestaWebSessionService.getVestaWebSessionData(new InCommVestaWebSessionDataCallback() {
            @Override
            public void onVestaWebSessionDataCallback(InCommVestaWebSessionData inCommVestaWebSessionData, Exception error) {
                if (inCommVestaWebSessionData != null && error == null) {
                    VestaOrgId = inCommVestaWebSessionData.getVestaOrgId();
                    VestaWebSessionId = inCommVestaWebSessionData.getVestaWebSessionId();
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().setVestaWebSessionId(VestaWebSessionId);
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().setVestaOrgId(VestaOrgId);
                    status = Constants.STARTED;
                    currentUpdate = "vestaid";
                    updateTransactionOrder();
                } else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
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
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                            }
                        }
                    });
                } else if (error != null) {
                    if (Utils.getErrorDescription(error) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(error), "genToken", GiftCardDataManager.getInstance().getIsSelf());
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Please check details and retry.", "genToken", GiftCardDataManager.getInstance().getIsSelf());
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
        submitOrder();

    }

    private void submitPromotionalOffer(final Context context, final String Title, final String Message, final Boolean isSelf) {
        enableScreen(false);
        SubmitPromoOfferService.submitPromoOffer(new SubmitPromoOfferServiceCallback() {
            @Override
            public void onSubmitPromoOfferServiceCallback(JSONObject jsonObject, String error) {
                enableScreen(true);
                navigateToNextScreen(context, Title, Message, isSelf);
            }
        });
    }

    //Creating Gift card service
    private void submitOrder() {
        final InCommOrderPurchaser inCommOrderPurchaser = GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo();
        InCommSubmitPayment inCommSubmitPayment = GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo();
        List<InCommOrderRecipientDetails> inCommOrderRecipientDetails = GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo();

        InCommSubmitOrder inCommSubmitOrder = new InCommSubmitOrder();
        inCommSubmitOrder.setPurchaser(inCommOrderPurchaser);
        inCommSubmitOrder.setRecipients(inCommOrderRecipientDetails);
        inCommSubmitOrder.setPayment(inCommSubmitPayment);
        inCommSubmitOrder.setId(TransactionOrderId);

        GiftCardDataManager.getInstance().getGiftCardCreate().setInCommSubmitOrder(inCommSubmitOrder);

        isSelf = GiftCardDataManager.getInstance().getIsSelf();

//        enableScreen(false);
        InCommOrderService.submitOrder(inCommSubmitOrder, new InCommOrderServiceCallback() {
            @Override
            public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error) {
                if (inCommOrderResponse != null && error == null) {
                    status = Constants.COMPLETED;
                    currentUpdate = "submitOrder";
                    updateTransactionOrder();
                    if (GiftCardDataManager.getInstance().getIsSelf()) {
                        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_CREATED_SELF);//tracking self creating gift card
                        createdGiftCards = new ArrayList<InCommSubmittedOrderItemGiftCards>(inCommOrderResponse.getSubmittedOrderItemGiftCards());
                        failureGiftCards = new ArrayList<InCommSubmittedOrderItemGiftCards>(inCommOrderResponse.getSubmittedOrderItemGiftCards());
                        totalCardSize = inCommOrderResponse.getSubmittedOrderItemGiftCards().size();
                        queueSize = inCommOrderResponse.getSubmittedOrderItemGiftCards().size();
                        token = new String[queueSize];
                        for (int i = 0; i < queueSize; i++) {
                            token[i] = inCommOrderResponse.getSubmittedOrderItemGiftCards().get(i).getToken();
                        }
                        nextIndex = 0;
                        addCardToList(token[nextIndex]);
                    } else {
                        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_CREATED_OTHERS);//tracking creating gift card for others
                        if (GiftCardDataManager.getInstance().getIsSaveNewPayment() != null) {
                            if (GiftCardDataManager.getInstance().getIsSaveNewPayment()) {
                                savePayment(isSelf);
                            } else {
                                enableScreen(true);
                                isBackButtonEnabled = true;
                                alertWithAction(context, "Success", "Your Gift Card has been created successfully and sent to " + GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress(), isSelf);
                            }
                        } else {
                            enableScreen(true);
                            isBackButtonEnabled = true;
                            alertWithAction(context, "Success", "Your Gift Card has been created successfully and sent to  " + GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress(), isSelf);
                        }
                    }
                } else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                submitOrder();
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                            }
                        }
                    });
                } else if (error != null) {
                    if (Utils.getErrorDescription(error) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(error), "submitorder", GiftCardDataManager.getInstance().getIsSelf());
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Please check details and retry.", "submitorder", GiftCardDataManager.getInstance().getIsSelf());
                    }
                } else {
                    alertWithTryAgain(context, "Failure", Utils.responseErrorNull(), "submitorder", GiftCardDataManager.getInstance().getIsSelf());
                }
            }
        });

    }


    //Created gift cards will associate to your account service
    private void addCardToList(String indexToken) {
        enableScreen(false);
        loaderText("Associating gift card...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommCardService.addCardToList(userId, indexToken, new InCommCardServiceCallback() {
            @Override
            public void onCardServiceCallback(InCommCard inCommCardAddedResponse, Exception error) {
                if (inCommCardAddedResponse != null) {
                    failureGiftCards.remove(createdGiftCards.get(nextIndex));
                    queueSize--;
                    nextIndex++;
                    if (queueSize > 0) {
                        addCardToList(token[nextIndex]);
                    } else {
                        if (GiftCardDataManager.getInstance().getIsSaveNewPayment() != null) {
                            if (GiftCardDataManager.getInstance().getIsSaveNewPayment()) {
                                savePayment(isSelf);
                            } else {
                                enableScreen(true);
                                isBackButtonEnabled=true;
                                alertWithAction(context, "Success", "Your Gift Card has been created successfully and sent to " + GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress(), isSelf);
                            }
                        } else {
                            enableScreen(true);
                            isBackButtonEnabled=true;
                            alertWithAction(context, "Success", "Your Gift Card has been created successfully and sent to " + GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress(), isSelf);
                        }
                    }
                } else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                addCardToList(token[nextIndex]);
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                            }
                        }
                    });
                } else if (error != null) {
                    if (Utils.getErrorDescription(error) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(error) + queueSize + " out of " + totalCardSize + " Gift card failed to associate to your account. Do you want to try again?", "addtolist", isSelf);
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. " + queueSize + " out of " + totalCardSize + " Gift card failed to associate to your account. Do you want to try again?", "addtolist", isSelf);
                    }
                } else {
                    alertWithTryAgain(context, "Failure", Utils.responseErrorNull(), "addtolist", isSelf);
                }
            }
        });
    }


    //Saving Payment information service
    private void savePayment(final Boolean isSelf) {
        enableScreen(false);
        loaderText("Saving payment info...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommSubmitPayment inCommSubmitPayment = GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo();
        InCommUserPaymentAccount inCommUserPaymentAccount = new InCommUserPaymentAccount(inCommSubmitPayment, userId);
        InCommUserPaymentAccountService.saveUserPaymentAccount(userId, inCommUserPaymentAccount, new InCommSaveUserPaymentAccountCallback() {
            @Override
            public void onPaymentAccountSaveServiceCallback(InCommUserPaymentAccount paymentAccount, Exception exception) {
                if (paymentAccount != null) {
                    enableScreen(true);
                    isBackButtonEnabled = true;
                    GiftCardDataManager.getInstance().setAccountList(null);
                    GiftCardDataManager.getInstance().setIsSaveNewPayment(null);
                    alertWithAction(context, "Success", "Your Gift Card has been created successfully and sent to " + GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress(), isSelf);
                } else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                savePayment(isSelf);
                            } else if (error.equals(Constants.NO_INTERNET_CONNECTION)) {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", Constants.NO_INTERNET_CONNECTION);
                            } else {
                                isBackButtonEnabled = true;
                                Utils.alert(context, "Failure", "Unexpected error occurred while processing your request.");
                            }
                        }
                    });
                } else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(context, "Failure", Utils.getErrorDescription(exception) + "Payment information could not be saved to your account. Retry?", "payment", isSelf);
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request.Payment information could not be saved to your account. Retry?", "payment", isSelf);
                    }
                } else {
                    alertWithTryAgain(context, "Failure", Utils.responseErrorNull(), "payment", isSelf);
                }
            }
        });
    }

    private void alertWithTryAgain(final Context context, final String Title, final String Message, final String whichFailed, final Boolean isSelf) {
        if (!((Activity) context).isFinishing()) {
            final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(Title);
            alertDialogBuilder.setMessage(Message);
            alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (whichFailed.equalsIgnoreCase("payment")) {
                        savePayment(isSelf);
                    } else if (whichFailed.equalsIgnoreCase("addtolist")) {
                        addCardToList(token[nextIndex]);
                    } else if (whichFailed.equalsIgnoreCase("submitorder")) {
                        webView();
                    } else if (whichFailed.equalsIgnoreCase("genToken")) {
                        getVestaWebSessionData();
                    } else if (whichFailed.equalsIgnoreCase("orderid")) {
                        getOrderId();
                    } else if (whichFailed.equalsIgnoreCase("updatetransaction")) {
                        updateTransactionOrder();
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (whichFailed.equalsIgnoreCase("payment")) {
                        enableScreen(true);
                        isBackButtonEnabled = true;
                        alertWithAction(context, "Success", "Your Gift Card has been created successfully and sent to " + GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress(), isSelf);
                    } else if (whichFailed.equalsIgnoreCase("addtolist")) {
                        String[] cardNumber = new String[failureGiftCards.size()];
                        String[] cardPin = new String[failureGiftCards.size()];
                        for (int j = 0; j < failureGiftCards.size(); j++) {
                            cardNumber[j] = failureGiftCards.get(j).getGiftCardNumber();
                            cardPin[j] = failureGiftCards.get(j).getPin();
                        }
                        if (GiftCardDataManager.getInstance().getIsSaveNewPayment()) {
                            savePayment(isSelf);
                        } else {
                            enableScreen(true);
                            isBackButtonEnabled = true;
                            alertWithAction(context, "Success", "Your Gift Card has been created successfully and sent to " + GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress(), isSelf);
                        }

                    } else if (whichFailed.equalsIgnoreCase("submitorder")) {
                        enableScreen(false);
                        loaderText("Cancelling...");
                        status = Constants.FAILED;
                        currentUpdate = "submitordercancel";
                        updateTransactionOrder();
                        dialogInterface.dismiss();
                    } else if (whichFailed.equalsIgnoreCase("genToken")) {
                        enableScreen(true);
                        isBackButtonEnabled = true;
                        dialogInterface.dismiss();
                    } else if (whichFailed.equalsIgnoreCase("orderid")) {
                        enableScreen(true);
                        isBackButtonEnabled = true;
                        dialogInterface.dismiss();
                    } else if (whichFailed.equalsIgnoreCase("updatetransaction")) {
                        enableScreen(true);
                        isBackButtonEnabled = true;
                        dialogInterface.dismiss();
                    }
                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private void navigateToNextScreen(Context context, String Title, String Message, final Boolean isSelf) {
        if (!((Activity) context).isFinishing()) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(Title);
            alertDialogBuilder.setMessage(Message);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (NewCardActivityGiftCard.newCardActivity != null) {
                        NewCardActivityGiftCard.newCardActivity.finish();
                    }
                    finish();
                    if (isSelf) {
                        TransitionManager.transitFrom(PurchaseNewCardActivityGiftCard.this, GiftCardHomeListActivityGiftCard.class);
                        if (GiftCardDataManager.getInstance().getInCommCards() != null) {
                            if (GiftCardDataManager.getInstance().getInCommCards().size() > 0) {
                                refreshGiftCardListActivity();
                            }
                        }
                        GiftCardDataManager.getInstance().resetGiftCardCreate();
                    } else {
                        if (GiftCardDataManager.getInstance().getInCommCards().size() > 0) {
                            TransitionManager.transitFrom(PurchaseNewCardActivityGiftCard.this, GiftCardHomeListActivityGiftCard.class);
                            if (GiftCardDataManager.getInstance().getInCommCards() != null) {
                                if (GiftCardDataManager.getInstance().getInCommCards().size() > 0) {
                                    refreshGiftCardListActivity();
                                }
                            }
                            GiftCardDataManager.getInstance().resetGiftCardCreate();
                        } else {
                            TransitionManager.transitFrom(PurchaseNewCardActivityGiftCard.this, NewCardActivityGiftCard.class);
                            if (GiftCardDataManager.getInstance().getInCommCards() != null) {
                                if (GiftCardDataManager.getInstance().getInCommCards().size() > 0) {
                                    refreshGiftCardListActivity();
                                }
                            }
                            GiftCardDataManager.getInstance().resetGiftCardCreate();
                        }
                    }
                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void alertWithAction(Context context, String Title, String Message, final Boolean isSelf) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        try {
            if (sdf.parse(formattedDate).before(sdf.parse("2017-01-01"))) {
                submitPromotionalOffer(context, Title, Message, isSelf);
            } else {
                enableScreen(true);
                navigateToNextScreen(context, Title, Message, isSelf);
            }
        } catch (ParseException e) {

        }
    }

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent("BROADCAST_UPDATE_GF_HOME_ACTIVITY");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    protected void navigateUp() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        finish();
        overridePendingTransition(R.anim.slide_no_anim, R.anim.slide_down_activity);

    }

    private void updateTransactionOrder() {
        String userId = UserService.getUser().getSpendGoId();
        String errorReason;
        if (currentUpdate.equalsIgnoreCase("submitordercancel")) {
            errorReason = "InComm Submit Order Failed";
        } else {
            errorReason = null;
        }
        UpdateTransactionService.updateTransactionOrder(TransactionOrderId, status, userId, errorReason, new IncommUpdateTransaction() {
            @Override
            public void onIncommUpdateTransactionCallback(Boolean successFlag, String error) {
                if (successFlag) {
                    if (currentUpdate.equalsIgnoreCase("submitOrder")) {

                    } else if (currentUpdate.equalsIgnoreCase("submitordercancel")) {
                        enableScreen(true);
                        isBackButtonEnabled = true;
                    } else if (currentUpdate.equalsIgnoreCase("vestaid")) {
                        webView();
                    }
                } else if (error != null) {
                    alertWithTryAgain(context, "Failure", error, "updatetransaction", false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isBackButtonEnabled) {
            enableScreen(true);
            if (PNCImage.getVisibility() == View.VISIBLE
                    || !(cardAmount.getText().toString().equalsIgnoreCase("0.00"))
                    || ((givenCardNumber.getVisibility() == View.VISIBLE) && (creditCardImg.getVisibility() == View.VISIBLE) &&  (!givenCardNumber.getText().toString().equalsIgnoreCase("")))
                    || ((purchaserMailAddress.getVisibility() == View.VISIBLE) &&  (!purchaserMailAddress.getText().toString().equalsIgnoreCase("")))
                    || ((recipientMailAddress.getVisibility() == View.VISIBLE) &&  (!recipientMailAddress.getText().toString().equalsIgnoreCase("")))) {
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Attention");
                alertDialogBuilder.setMessage("Do you wish to cancel current gift card order?");
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
}
