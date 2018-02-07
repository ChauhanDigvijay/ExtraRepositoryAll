package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
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
import com.squareup.picasso.Picasso;
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Services.InCommCardService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jeeva
 */
public class CardInfoActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    private RelativeLayout CIRootLayout, selectImageCILayout, CIHeader, CIFooter;
    private int totalHeight, headerHeight, footerHeight, cardImgContentArea, totalWidth;
    private Context context;
    private SemiBoldTextView CICardName, CICardNumber, CIBalance;
    private ImageView CICardImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true,true);
        setBackButton(false,false);

        CIRootLayout = (RelativeLayout) findViewById(R.id.CIRootLayout);
        selectImageCILayout = (RelativeLayout) findViewById(R.id.selectImageCILayout);
        CIHeader = (RelativeLayout) findViewById(R.id.CIHeader);
        CIFooter = (RelativeLayout) findViewById(R.id.CIFooter);

        CICardName = (SemiBoldTextView) findViewById(R.id.CICardName);
        CICardNumber = (SemiBoldTextView) findViewById(R.id.CICardNumber);
        CIBalance = (SemiBoldTextView) findViewById(R.id.CIBalance);

        CICardImg = (ImageView) findViewById(R.id.CICardImg);


        CIFooter.setOnClickListener(this);

        initToolbar();
        resizeView();

    }

    private void resizeView() {

        ViewTreeObserver observer = CIRootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    CIRootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    CIRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                totalHeight = CIRootLayout.getMeasuredHeight();
                totalWidth = CIRootLayout.getMeasuredWidth();

                headerHeight = CIHeader.getHeight();
                footerHeight = CIFooter.getHeight();

                int contentArea = (totalHeight - (headerHeight + footerHeight));
                cardImgContentArea = (int) (contentArea * 0.5);

                LinearLayout.LayoutParams cardParams = (LinearLayout.LayoutParams) selectImageCILayout.getLayoutParams();
                cardParams.height = cardImgContentArea;
                selectImageCILayout.setLayoutParams(cardParams);

                RelativeLayout.LayoutParams ciNameParams = (RelativeLayout.LayoutParams) CICardName.getLayoutParams();
                ciNameParams.width = (int) (totalWidth / 2.5);
                CICardName.setLayoutParams(ciNameParams);

                RelativeLayout.LayoutParams ciNumberParams = (RelativeLayout.LayoutParams) CICardNumber.getLayoutParams();
                ciNumberParams.width = (int) (totalWidth / 2.5);
                CICardNumber.setLayoutParams(ciNumberParams);

                setData();

            }

        });
    }

    private void initToolbar() {
        setTitle("Card Information");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }


    /*
        Setting Card information into this screen from Gift Card Manager what we have stored in it.
     */
    private void setData() {

        if (GiftCardDataManager.getInstance().getInCommCard() != null) {
            if (GiftCardDataManager.getInstance().getInCommCard().getImageUrl() != null) {
                Picasso.with(context).load(GiftCardDataManager.getInstance().getInCommCard().getImageUrl()).into(CICardImg);
            } else {
                Picasso.with(context).load(R.drawable.product_placeholder).into(CICardImg);
            }
            if (GiftCardDataManager.getInstance().getInCommCard().getCardName() != null) {
                CICardName.setText(GiftCardDataManager.getInstance().getInCommCard().getCardName());
            } else {
                CICardName.setText("No Card Name Available");
            }

            CICardNumber.setText(GiftCardDataManager.getInstance().getInCommCard().getCardNumber());
            CIBalance.setText(String.format("%.2f", GiftCardDataManager.getInstance().getInCommCard().getBalance()));

        }
    }

    /*
         Associating Gift Card to personal account using card number and card pin service
     */
    private void addCardToList() {

        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        String cardNumber = GiftCardDataManager.getInstance().getInCommCard().getCardNumber();
        String cardPin = GiftCardDataManager.getInstance().getInCommCard().getCardPin();
        enableScreen(false);
        loaderText("Associating gift card...");
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_ADDED);//tracking adding gift card
        InCommCardService.addCardToList(userId, cardNumber, cardPin, new InCommCardServiceCallback() {
            @Override
            public void onCardServiceCallback(InCommCard addedCardResponse, Exception error) {
                enableScreen(true);
                if (addedCardResponse != null && error == null) {
                    //Navigate to Gift card list screen after it is successfully added into user account
                    TransitionManager.slideUp(CardInfoActivityGiftCard.this, GiftCardHomeListActivityGiftCard.class);
                    if (GiftCardDataManager.getInstance().getInCommCards() != null) {
                        if (GiftCardDataManager.getInstance().getInCommCards().size() > 0) {
                            refreshGiftCardListActivity();//refreshing Gift card home list screen using broadcast manager
                        }
                    }
                    //Closing all screens after navigate to List screen and reseting datas
                    finish();
                    if(NewCardActivityGiftCard.newCardActivity != null){
                        NewCardActivityGiftCard.newCardActivity.finish();
                    }
                    if(AddExistingCardActivityGiftCard.addExistingCardActivityGiftCard != null) {
                        AddExistingCardActivityGiftCard.addExistingCardActivityGiftCard.finish();
                    }

                    GiftCardDataManager.getInstance().resetGiftCardCreate();
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
                                addCardToList();
                            }
                        }
                    });
                } else if (error != null) {
                    if (Utils.getErrorDescription(error) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(error));
                    } else {
                        Utils.alert(context, "Failure", "Error in Loading");
                    }
                } else {
                    Utils.alert(context, "Failure", Utils.responseErrorNull());
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CIFooter:
                addCardToList();
                break;
        }
    }

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent("BROADCAST_UPDATE_GF_HOME_ACTIVITY");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
