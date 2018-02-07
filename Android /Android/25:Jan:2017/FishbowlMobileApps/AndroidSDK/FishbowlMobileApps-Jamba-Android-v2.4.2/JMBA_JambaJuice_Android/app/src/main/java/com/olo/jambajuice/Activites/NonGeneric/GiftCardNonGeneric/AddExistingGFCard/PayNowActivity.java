package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.squareup.picasso.Picasso;
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommUserServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Models.InCommUser;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.incomm.Services.InCommUserService;

public class PayNowActivity extends GiftCardBaseActivity {
    int cardId;
    boolean isFirstTimeAlert = true;
    int userCurrentBrightness = 255;
    int userCurrentBrightnessMode = 0;
    Context context;
    String imgUrl;
    String userId;
    private RelativeLayout payNowRootLayout, PNHeader, PNFooter, contentLayout;
    private SemiBoldTextView tvCardNo, tvBalanceAmount;
    private ImageView imgBarCode;
    private GiftCardDataManager giftCardDataManager;
    private int totalHeight, headerHeight, footerHeight, fullImageContentArea, count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);
        giftCardDataManager = GiftCardDataManager.getInstance();
        isShowBasketIcon = false;
        setUpToolBar(true, true);
        setBackButton(false, false);
        context = this;
        payNowRootLayout = (RelativeLayout) findViewById(R.id.payNowRootLayout);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        PNHeader = (RelativeLayout) findViewById(R.id.PNHeader);
        PNFooter = (RelativeLayout) findViewById(R.id.PNFooter);
        tvCardNo = (SemiBoldTextView) findViewById(R.id.tvCardNo);
        tvBalanceAmount = (SemiBoldTextView) findViewById(R.id.tvBalanceAmount);
        imgBarCode = (ImageView) findViewById(R.id.imgBarCode);

        initToolbar();
        resizeView();

        PNFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initToolbar() {
        setTitle("Pay Now");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    private void setPay() {
        if (GiftCardDataManager.getInstance().getInCommUser() != null) {
            userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
            cardId = getIntent().getIntExtra("cardId", 0);
            enableScreen(false);
            InCommCardService.getCardInfo(userId, cardId, new InCommCardServiceCallback() {
                @Override
                public void onCardServiceCallback(InCommCard card, Exception exception) {
                    if (card != null && exception == null) {
                        imgUrl = giftCardDataManager.getUserAllCards().get(cardId).getBarcodeImageUrl();
                        if (imgUrl != null) {
                            Picasso.with(getApplicationContext())
                                    .load(imgUrl)
                                    .into(imgBarCode, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            enableScreen(true);
                                            checkPermissionAndIncreaseBrightness();
                                        }

                                        @Override
                                        public void onError() {
                                            enableScreen(true);
                                            netWorkAlert();
                                        }
                                    });
                        }
                        tvBalanceAmount.setText("$" + String.format("%.2f", card.getBalance()));
                        if (giftCardDataManager.getUserAllCards().get(cardId).getCardNumber() != null) {
                            tvCardNo.setText(giftCardDataManager.getUserAllCards().get(cardId).getCardNumber());
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
                                    setPay();
                                }
                            }
                        });
                    } else if (exception != null) {
                        if (Utils.getErrorDescription(exception) != null) {
                            enableScreen(true);
                            alertWithTryAgain(context, "Failure", Utils.getErrorDescription(exception));
                        } else {
                            enableScreen(true);
                            alertWithTryAgain(context, "Failure", "Failed to fetch your gift card information. Please try again.");
                        }
                    }
                }
            });
        } else {
            enableScreen(false);
            InCommUserService.getAccessTokenWithUserId(new InCommUserServiceCallBack() {
                @Override
                public void onUserServiceCallback(InCommUser inCommUser, Exception exception) {
                    enableScreen(true);
                    if (inCommUser != null) {
                        GiftCardDataManager.getInstance().setInCommUser(inCommUser);
                        setPay();
                    }else{
                        Utils.showErrorAlert(PayNowActivity.this,exception);
                    }
                }
            });
        }
    }

    private void netWorkAlert() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Network Error");
        alertDialogBuilder.setMessage("Please check your network connection and try again.");
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableScreen(false);
                setPay();
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

    private void alertWithTryAgain(final Context context, String Title, final String Message) {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableScreen(false);
                setPay();
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

    private void resizeView() {

        ViewTreeObserver observer = payNowRootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    payNowRootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    payNowRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                totalHeight = payNowRootLayout.getMeasuredHeight();

                headerHeight = PNHeader.getHeight();
                footerHeight = PNFooter.getHeight();

                int contentArea = totalHeight - (headerHeight + footerHeight);
                fullImageContentArea = (int) (contentArea * 0.55);


                RelativeLayout.LayoutParams fullImageParams = (RelativeLayout.LayoutParams) contentLayout.getLayoutParams();
                fullImageParams.height = fullImageContentArea;
                contentLayout.setLayoutParams(fullImageParams);

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetBrightness();
    }

    @Override
    protected void onStop() {
        super.onStop();
        resetBrightness();
    }

    protected void resetBrightness() {
        if (brightnessPermissionAvailable() && imgUrl != null) {
            if (userCurrentBrightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, userCurrentBrightness);
                Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            } else {
                Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, userCurrentBrightness);
            }
        }
    }

    //This method will give brightness mode
//if brigthnessmode=0 means Auto mode is currently off
//if brightnessmode=1 means Auto mode is currently on
    protected int getBrightMode() {
        try {
            return Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception e) {
            return 0;
        }
    }

    private void checkPermissionAndIncreaseBrightness() {
        final Context context = this;
        if (brightnessPermissionAvailable()) {
            changeToMaxBrightness();
        } else {
            if (isFirstTimeAlert) {
                if (!((Activity) context).isFinishing()) {
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                    alertdialog.setCancelable(false);
                    alertdialog.setTitle("Permission Request");
                    alertdialog.setNegativeButton("Cancel", null);
                    alertdialog.setMessage("Do you want to increase this screen brightness? If so please give access to increase screen brightness.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                    alertdialog.show();
                    isFirstTimeAlert = false;
                }
            }
        }
    }

    private boolean brightnessPermissionAvailable() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            if (retVal) {
                return retVal;
            } else {
                return false;
            }
        }
        return retVal;
    }

    private void changeToMaxBrightness() {
        userCurrentBrightnessMode = getBrightMode();
        try {
            userCurrentBrightness = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (userCurrentBrightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);//Set Screen in High brightness
        } else {
            Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);//Set Screen in High brightness
        }
    }

    @Override
    public void onBackPressed() {
        userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        cardId = getIntent().getIntExtra("cardId", 0);
        enableScreen(false);
        InCommCardService.getCardInfo(userId, cardId, new InCommCardServiceCallback() {
            @Override
            public void onCardServiceCallback(InCommCard card, Exception exception) {
                enableScreen(true);
                refreshGiftCardListActivity();
                navigateUp();
            }
        });
    }

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent(Constants.BROADCAST_GF_HOME_ACTIVITY_REFRESH_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
