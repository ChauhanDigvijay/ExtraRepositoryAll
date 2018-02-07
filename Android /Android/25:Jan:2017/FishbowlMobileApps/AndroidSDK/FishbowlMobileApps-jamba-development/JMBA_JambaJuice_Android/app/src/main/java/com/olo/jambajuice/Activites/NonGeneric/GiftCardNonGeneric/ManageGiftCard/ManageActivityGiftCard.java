package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.Generic.WebViewActivity;
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
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSavable;
import com.wearehathway.apps.incomm.Services.InCommCardService;


public class ManageActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    public static ManageActivityGiftCard manageActivityGiftCard;
    int cardId;
    int dataIndex;
    Context context;
    private RelativeLayout ManageRootLayout, selectedImageManageLayout, MFooter;
    private LinearLayout autoReloadManageLayout, paymentAccountManageLayout, transactionHistoryManageLayout, giftCardSupportManageLayout;
    private ImageView selectedImageManage;
    private ImageButton selectedCardNotExpand;
    private int totalWidth, fullImageContentArea;
    private SemiBoldTextView autoReloadSwitchtext, autoReloadErrorText;
    private String barValue;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        context = this;
        manageActivityGiftCard = this;

        isShowBasketIcon = false;

        setUpToolBar(true, true);
        setBackButton(false, false);

        ManageRootLayout = (RelativeLayout) findViewById(R.id.ManageRootLayout);

        selectedImageManageLayout = (RelativeLayout) findViewById(R.id.selectedImageManageLayout);
        MFooter = (RelativeLayout) findViewById(R.id.MFooter);


        autoReloadManageLayout = (LinearLayout) findViewById(R.id.autoReloadManageLayout);
        paymentAccountManageLayout = (LinearLayout) findViewById(R.id.paymentAccountManageLayout);
        transactionHistoryManageLayout = (LinearLayout) findViewById(R.id.transactionHistoryManageLayout);
        giftCardSupportManageLayout = (LinearLayout) findViewById(R.id.giftCardSupportManageLayout);

        selectedCardNotExpand = (ImageButton) findViewById(R.id.selectedCardNotExpand);

        selectedImageManage = (ImageView) findViewById(R.id.selectedImageManage);

        autoReloadSwitchtext = (SemiBoldTextView) findViewById(R.id.autoReloadSwitchtext);
        autoReloadErrorText = (SemiBoldTextView) findViewById(R.id.autoReloadErrorText);

        autoReloadManageLayout.setOnClickListener(this);
        paymentAccountManageLayout.setOnClickListener(this);
        transactionHistoryManageLayout.setOnClickListener(this);
        giftCardSupportManageLayout.setOnClickListener(this);
        selectedCardNotExpand.setOnClickListener(this);
        MFooter.setOnClickListener(this);

        //Get the card id
        cardId = getIntent().getIntExtra("cardId", 0);
        dataIndex = getIntent().getIntExtra("dataIndex", -1);
        if (GiftCardDataManager.getInstance().getUserAllCards() != null
                && GiftCardDataManager.getInstance().getUserAllCards().get(cardId) != null) {
            String imgUrl = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getImageUrl();
            int autoReloadId = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId();
            if (autoReloadId != 0) {
                enableScreen(false);
                checkStateOfAutoReload(autoReloadId, cardId);
            } else {
                GiftCardDataManager.getInstance().setAutoReloadSavable(null);
                autoReloadSwitchtext.setText("");// text will be empty when auto reload was not setting
            }

            if (imgUrl != null) {

                Ion.with(this)
                        .load(imgUrl)
                        .withBitmap()
                        .placeholder(R.drawable.product_placeholder)
                        .error(R.drawable.product_placeholder)
                        .intoImageView(selectedImageManage);
            }

            initToolbar();
            resizeView();
        } else {
            Toast.makeText(context, "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            finish();
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //  client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        if (GiftCardDataManager.getInstance().getAutoReloadSavable() != null) {
            if (GiftCardDataManager.getInstance().getAutoReloadSavable().getActive()) {
                autoReloadSwitchtext.setText("On");
            } else {
                autoReloadSwitchtext.setText("Off");
            }
            if (!GiftCardDataManager.getInstance().getAutoReloadSavable().getLastErrorMessage().equals("") && GiftCardDataManager.getInstance().getAutoReloadSavable().getLastErrorMessage() != null) {
                autoReloadErrorText.setText(GiftCardDataManager.getInstance().getAutoReloadSavable().getLastErrorMessage());
            } else {
                autoReloadErrorText.setText("");
            }
        } else {
            autoReloadSwitchtext.setText("");// text will be empty when auto reload was not setting
            autoReloadErrorText.setText("");
        }
    }

    //Check the status of auto reload
    private void checkStateOfAutoReload(final int autoReloadId, final int cardId) {
        loaderText("Retrieving gift card details...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommCardService.getAutoReloadInfo(userId, cardId, autoReloadId, new InCommCardAutoReloadServiceCallBack() {
            @Override
            public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception) {
                enableScreen(true);
                if (inCommAutoReloadSavable != null && exception == null) {
                    GiftCardDataManager.getInstance().setAutoReloadSavable(inCommAutoReloadSavable);
                    Boolean isActive = inCommAutoReloadSavable.getActive();
                    String Errormessage = inCommAutoReloadSavable.getLastErrorMessage();
                    if (isActive) {
                        autoReloadSwitchtext.setText("On");
                    } else {
                        autoReloadSwitchtext.setText("Off");
                    }
                    if (!Errormessage.equals("") && Errormessage != null) {
                        autoReloadErrorText.setText(Errormessage);
                    } else {
                        autoReloadErrorText.setText("");
                    }
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
                                checkStateOfAutoReload(autoReloadId, cardId);
                            }
                        }
                    });
                } else {
                    alertWithTryAgain(context, "Failure", "Failed to fetch your gift card information. Please try again.");
                }
            }
        });
    }

    private void resizeView() {

        ViewTreeObserver observer = ManageRootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ManageRootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ManageRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                totalWidth = ManageRootLayout.getMeasuredWidth();

                fullImageContentArea = (int) (0.70 * totalWidth);


                RelativeLayout.LayoutParams fullImageParams = (RelativeLayout.LayoutParams) selectedImageManageLayout.getLayoutParams();
                fullImageParams.height = fullImageContentArea;
                selectedImageManageLayout.setLayoutParams(fullImageParams);

            }

        });
    }

    private void initToolbar() {
        setTitle("Manage Card");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MFooter:
                Bundle reload = new Bundle();
                reload.putInt("cardId", cardId);
                TransitionManager.slideUp(ManageActivityGiftCard.this, ReloadActivityGiftCard.class, reload);

                break;
            case R.id.paymentAccountManageLayout:
                redirectToCardDetail();
                break;

            case R.id.selectedCardNotExpand:
                redirectToCardDetail();
                break;

            case R.id.autoReloadManageLayout:
                redirectToAutoReloadScreen();
                break;

            case R.id.transactionHistoryManageLayout:
                Bundle bundle = new Bundle();
                bundle.putInt("cardId", cardId);
                TransitionManager.transitFrom(ManageActivityGiftCard.this, TransactionHistory.class, bundle);
                break;

            case R.id.giftCardSupportManageLayout:
                WebViewActivity.show(this, "Gift Card Support", "https://premium.vcdelivery.com/V2/jambajuice/GiftCardFAQ.aspx");
                break;


        }
    }

    private void redirectToAutoReloadScreen() {
        Bundle bundle = new Bundle();
        bundle.putInt("cardId", cardId);
        TransitionManager.transitFrom(ManageActivityGiftCard.this, AutoReloadActivityGiftCard.class, bundle);
    }

    private void redirectToCardDetail() {

        Bundle myBundle = new Bundle();
        myBundle.putInt("cardId", cardId);
        myBundle.putInt("dataIndex", dataIndex);
        TransitionManager.transitFrom(ManageActivityGiftCard.this, CardDetailsActivityGiftCard.class, myBundle);

    }

    private void alertWithTryAgain(final Context context, String Title, final String Message) {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableScreen(false);
                checkStateOfAutoReload(GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId(), cardId);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


}
