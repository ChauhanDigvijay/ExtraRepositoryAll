package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard.GiftCardHomeListActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.NewCardActivityGiftCard;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.wearehathway.apps.incomm.Interfaces.InCommCardAutoReloadServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommCardDeleteServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommCardUpdateServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSavable;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Services.InCommCardService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CardDetailsActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    int cardId,dataIndex;
    String userId;
    String myText;
    String oldCardName;
    private LinearLayout  CIFooter;
    private RelativeLayout CIRootLayout, CIHeader, selectImageCDLayout, cardRLayout;
    private int totalHeight, headerHeight, footerHeight, fullImageContentArea,totalWidth;
    private ImageView CICardImg;
    private SemiBoldTextView CICardName, CICardNumber, CIPinNumber, CIPurchaseDate, CIInitBalance, CIBalance, CICardNameText;
    private GiftCardDataManager giftCardDataManager;
    private Context context;
    private EditText etCardName;
    private ImageButton imgBtnReloadNotExpand, imgBtnClear;
    private Button footerButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true);
        setBackButton(true, false);

        giftCardDataManager = GiftCardDataManager.getInstance();


        CIRootLayout = (RelativeLayout) findViewById(R.id.CIRootLayout);
        CIHeader = (RelativeLayout) findViewById(R.id.CIHeader);
        CIFooter = (LinearLayout) findViewById(R.id.CIFooter);
        selectImageCDLayout = (RelativeLayout) findViewById(R.id.selectImageCDLayout);
        cardRLayout = (RelativeLayout) findViewById(R.id.cardRLayout);

        CICardImg = (ImageView) findViewById(R.id.CICardImg);

        CICardName = (SemiBoldTextView) findViewById(R.id.CICardName);
        CICardNameText = (SemiBoldTextView) findViewById(R.id.CICardNameText);
        CICardNumber = (SemiBoldTextView) findViewById(R.id.CICardNumber);
        CIPinNumber = (SemiBoldTextView) findViewById(R.id.CIPinNumber);
        CIPurchaseDate = (SemiBoldTextView) findViewById(R.id.CIPurchaseDate);
        CIInitBalance = (SemiBoldTextView) findViewById(R.id.CIInitBalance);
        CIBalance = (SemiBoldTextView) findViewById(R.id.CIBalance);
        etCardName = (EditText) findViewById(R.id.etCardName);
        footerButton = (Button) findViewById(R.id.footerButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        imgBtnReloadNotExpand = (ImageButton) findViewById(R.id.imgBtnReloadNotExpand);
        imgBtnClear = (ImageButton) findViewById(R.id.imgBtnClear);

        if (CICardName.getVisibility() == View.VISIBLE) {
            etCardName.setVisibility(View.GONE);
        }

        CIPinNumber.setOnClickListener(this);
        footerButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        cardRLayout.setOnClickListener(this);
        imgBtnReloadNotExpand.setOnClickListener(this);

        initToolbar();

        setData();

        resizeView();

    }

    private void initToolbar() {
        setTitle("Card Details");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    //Set all data in corresponding view
    private void setData() {
        cardId = getIntent().getIntExtra("cardId", 0);
        dataIndex = getIntent().getIntExtra("dataIndex",-1);
        if(giftCardDataManager.getUserAllCards() != null && giftCardDataManager.getUserAllCards().get(cardId) != null) {
            String imgUrl = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getImageUrl();
            String balance = String.format("%.2f", giftCardDataManager.getUserAllCards().get(cardId).getBalance());
            if (imgUrl != null) {
                Ion.with(this)
                        .load(imgUrl)
                        .withBitmap()
                        .placeholder(R.drawable.product_placeholder)
                        .error(R.drawable.product_placeholder)
                        .intoImageView(CICardImg);
            }
            if (giftCardDataManager.getUserAllCards().get(cardId).getCardName() != null) {
                CICardName.setText(giftCardDataManager.getUserAllCards().get(cardId).getCardName());
            }
            if (giftCardDataManager.getUserAllCards().get(cardId).getCardNumber() != null) {
                CICardNumber.setText(giftCardDataManager.getUserAllCards().get(cardId).getCardNumber());
            }
            if (giftCardDataManager.getUserAllCards().get(cardId).getLastModifiedDate() != null) {
                CIPurchaseDate.setText(convertDate(giftCardDataManager.getUserAllCards().get(cardId).getLastModifiedDate()));
            }
            if (giftCardDataManager.getUserAllCards().get(cardId).getExpirationDate() != null) {
                CIInitBalance.setText(giftCardDataManager.getUserAllCards().get(cardId).getExpirationDate().toString());
            }
            if (String.valueOf(giftCardDataManager.getUserAllCards().get(cardId).getBalance()) != null) {
                CIBalance.setText("$" + balance);
            }
        }

    }

    private String convertDate(Date dateString) {
        SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
        String formattedTime = null;
        formattedTime = output.format(dateString);

        return formattedTime;
    }

    //Set UI
    private void resizeView() {

        ViewTreeObserver observer = CIRootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    CIRootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    CIRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
//                totalHeight = CIRootLayout.getMeasuredHeight();
//
//                headerHeight = CIHeader.getHeight();
//                footerHeight = CIFooter.getHeight();
//
//                int contentArea = totalHeight - (headerHeight + footerHeight);
//                fullImageContentArea = (int) (contentArea * 0.5);

                totalWidth = CIRootLayout.getMeasuredWidth();

                fullImageContentArea = (int) (0.70 * totalWidth);

                LinearLayout.LayoutParams fullImageParams = (LinearLayout.LayoutParams) selectImageCDLayout.getLayoutParams();
                fullImageParams.height = fullImageContentArea;
                selectImageCDLayout.setLayoutParams(fullImageParams);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CIPinNumber:
                if (CIPinNumber.getText().toString().equalsIgnoreCase("show")) {
                    if(giftCardDataManager.getUserAllCards() != null) {
                        CIPinNumber.setText(giftCardDataManager.getUserAllCards().get(cardId).getCardPin());
                    }else{
                        CIPinNumber.setText("xxxx (unable to retrieve your card pin)");
                    }
                    CIPinNumber.setTextColor(Color.BLACK);

                } else {
                    CIPinNumber.setText("show");
                    CIPinNumber.setTextColor(Color.parseColor("#12A5F4"));
                }
                break;
            case R.id.imgBtnReloadNotExpand:
                editCardName();
                break;
            case R.id.cardRLayout:
                editCardName();

                break;

            case R.id.footerButton:
                if (footerButton.getText().toString().equals("Edit Card Name")) {
                    editCardName();
                } else {
                    confirmDetails();
                }
                break;
            case R.id.deleteButton:
                //Remove selected Gift card
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Remove Card");
                if (GiftCardDataManager.getInstance().getUserAllCards().get(cardId) != null
                        && GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId() != 0) {
                    alertDialogBuilder.setMessage("Auto reload rules is set for this card. Are you sure you want to remove this card from your account?");
                } else {
                    alertDialogBuilder.setMessage("Are you sure you want to remove this card from your account?");
                }
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        enableScreen(false);
                        loaderText("Deleting gift card...");
                        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_REMOVED);//tracking adding gift card
                        if (GiftCardDataManager.getInstance().getUserAllCards().get(cardId) != null
                                && GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId() != 0) {
                            JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_AUTORELOAD_REMOVED);//tracking successful auto_reload removed
                            deleteAutoReloadService();
                        } else {
                            deleteGiftCard();
                        }

                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    private void deleteAutoReloadService() {
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        int autoReloadId = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId();
        InCommCardService.deleteAutoReload(userId, cardId, autoReloadId, new InCommCardAutoReloadServiceCallBack() {
            @Override
            public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception) {
                enableScreen(true);
                //refreshGiftCardListActivity();
                if (inCommAutoReloadSavable == null && exception == null) {
                    GiftCardDataManager.getInstance().setAutoReloadSavable(inCommAutoReloadSavable);
                    GiftCardDataManager.getInstance().setAutoReloadLocalPaymentInfo(null);
                   // refreshGiftCardListActivity();
                    deleteGiftCard();
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
                                deleteAutoReloadService();
                            }
                        }
                    });
                } else {
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        Utils.alert(context, "Failure", Utils.responseErrorNull());
                    }
                }
            }
        });
    }


    private void deleteGiftCard() {
        enableScreen(false);
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        int myCardId = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getCardId();
        InCommCardService.deleteCard(userId, myCardId, new InCommCardDeleteServiceCallback() {
            @Override
            public void onCardDeleteServiceCallback(Exception exception) {
                enableScreen(true);
                if (exception == null) {
                    //Remove card in local array
//                    if(GiftCardHomeListActivityGiftCard.giftCardHomeListActivity != null) {
//                        GiftCardHomeListActivityGiftCard.giftCardHomeListActivity.finish();
//                    }
                    if(ManageActivityGiftCard.manageActivityGiftCard != null) {
                        ManageActivityGiftCard.manageActivityGiftCard.finish();
                    }
                    InCommCard inCommCard = GiftCardDataManager.getInstance().getInCommCards().get(dataIndex);
                    alert(context,"Removed","Gift card "+inCommCard.getCardName()+" removed successfully",inCommCard);
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
                                deleteGiftCard();
                            }
                        }
                    });
                } else {
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                        GiftCardDataManager.getInstance().setRefreshGiftCards(true);
                    } else {
                        Utils.alert(context, "Failure", "Your card not deleted please try again");
                    }
                }
            }
        });
    }

    public void alert(Context context,String Title,String Message,final InCommCard inCommCard){
        if(Message == null){
            Message = "Error";
        }
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (inCommCard != null && inCommCard.getCardId() == cardId) {
                    GiftCardDataManager.getInstance().setDoNotifyDataSetChangedOnce(true);
                    GiftCardDataManager.getInstance().getUserAllCards().remove(cardId);//remove array data
                    GiftCardDataManager.getInstance().getInCommCards().remove(inCommCard);//remove hashmap data
                }
                if (GiftCardDataManager.getInstance().getInCommCards() != null && GiftCardDataManager.getInstance().getInCommCards().size() > 0) {
                    GiftCardDataManager.getInstance().setRefreshGiftCards(true);
                    finish();
                   // TransitionManager.slideUp(CardDetailsActivityGiftCard.this, GiftCardHomeListActivityGiftCard.class);
                } else {
                    if(GiftCardHomeListActivityGiftCard.giftCardHomeListActivity != null) {
                        GiftCardHomeListActivityGiftCard.giftCardHomeListActivity.finish();
                    }
                    finish();
                    TransitionManager.slideUp(CardDetailsActivityGiftCard.this, NewCardActivityGiftCard.class);
                }
                //refreshGiftCardListActivity();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    //Show the edit card functionality
    private void editCardName() {
        if (CICardName.getVisibility() == View.VISIBLE) {
            CICardNameText.setVisibility(View.GONE);
            CICardName.setVisibility(View.GONE);
            etCardName.setVisibility(View.VISIBLE);
            oldCardName = etCardName.getText().toString();
            RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            etCardName.setLayoutParams(mRparams);
            imgBtnReloadNotExpand.setVisibility(View.GONE);
            imgBtnClear.setVisibility(View.VISIBLE);
            etCardName.requestFocus();
            footerButton.setText("Confirm Details");
            //Click Cancel button in edit text
            imgBtnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etCardName.setVisibility(View.GONE);
                    CICardNameText.setVisibility(View.VISIBLE);
                    CICardName.setVisibility(View.VISIBLE);
                    imgBtnReloadNotExpand.setVisibility(View.VISIBLE);
                    imgBtnClear.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    footerButton.setText("Edit Card Name");
                }
            });
        }

        etCardName.setText(giftCardDataManager.getUserAllCards().get(cardId).getCardName());

    }

    //Confirm details click confirm button
    private void confirmDetails() {
        if (etCardName.getVisibility() == View.VISIBLE) {
            userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
            myText = etCardName.getText().toString();
            oldCardName = giftCardDataManager.getUserAllCards().get(cardId).getCardName();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etCardName.getWindowToken(), 0);
            if (myText.matches("")) {
                etCardName.setError("Not a valid name");
            } else if (oldCardName.equalsIgnoreCase(myText)) {
                etCardName.setVisibility(View.GONE);
                CICardNameText.setVisibility(View.VISIBLE);
                CICardName.setVisibility(View.VISIBLE);                 //Check card name edited or not
                imgBtnReloadNotExpand.setVisibility(View.VISIBLE);
                imgBtnClear.setVisibility(View.GONE);
                footerButton.setText("Edit Card Name");
            } else {
                enableScreen(false);
                loaderText("Updating...");
                InCommCardService.updateCard(userId, cardId, myText, new InCommCardUpdateServiceCallback() {
                    @Override
                    public void onCardUpdateServiceCallback(InCommCard inCommCard, Exception exception) {
                        enableScreen(true);

                        if (inCommCard != null && exception == null) {
                            if (etCardName.getVisibility() == View.VISIBLE) {
                                giftCardDataManager.getUserAllCards().get(inCommCard.getCardId()).setCardName(myText);
                                etCardName.setVisibility(View.GONE);
                                CICardNameText.setVisibility(View.VISIBLE);         //Update the card name
                                CICardName.setVisibility(View.VISIBLE);
                                imgBtnReloadNotExpand.setVisibility(View.VISIBLE);
                                imgBtnClear.setVisibility(View.GONE);
                                footerButton.setText("Edit Card Name");
                            }
                            CICardName.setText(giftCardDataManager.getUserAllCards().get(inCommCard.getCardId()).getCardName());
                            GiftCardDataManager.getInstance().getUserAllCards().put(inCommCard.getCardId(), inCommCard);
                            onBackPressed();
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
                                        confirmDetails();
                                    }
                                }
                            });
                        } else if (exception != null) {
                            if (Utils.getErrorDescription(exception) != null) {
                                Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                            } else {
                                Utils.alert(context, "Failure", "There is some problem in Network Connection. Please Try Again.");
                            }
                        } else {
                            Utils.alert(context, null, Utils.responseErrorNull());
                        }
                    }
                });
            }
        } else {
            enableScreen(true);
        }
    }

    //Press Device back button
    @Override
    public void onBackPressed() {
        if (etCardName.getVisibility() == View.VISIBLE) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Confirm");
            alertDialogBuilder.setMessage("Cancel card name change?");
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

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent(Constants.BROADCAST_UPDATE_GF_HOME_ACTIVITY);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
