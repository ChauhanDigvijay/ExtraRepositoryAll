package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
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
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommUserServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Models.InCommUser;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.incomm.Services.InCommUserService;

import android.text.InputFilter;

/**
 * Created by Jeeva
 */
public class AddExistingCardActivityGiftCard extends GiftCardBaseActivity {

    private EditText giftCardNumber, securityPin;
    private String cardNumber, cardPin;
    private Context context;
    public static AddExistingCardActivityGiftCard addExistingCardActivityGiftCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_existing_card);

        context = this;
        addExistingCardActivityGiftCard = this;
        isShowBasketIcon = false;

        setUpToolBar(true, true);
        setBackButton(false, false);

        RelativeLayout AEFooter = (RelativeLayout) findViewById(R.id.AEFooter);

        giftCardNumber = (EditText) findViewById(R.id.giftCardNumber);

        giftCardNumber.setFilters(new InputFilter[]{
                new InputFilter() {

                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z0-9]+")) { // here no space character
                            return cs;
                        }
                        return "";
                    }
                }
        });
        securityPin = (EditText) findViewById(R.id.securityPin);

        initToolbar();

        if (AEFooter != null) {
            AEFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(giftCardNumber.getText().length() > 0)) {
                        giftCardNumber.setError("Enter gift card number");
                        giftCardNumber.requestFocus();
                        return;
                    }
//                    if (!(giftCardNumber.getText().length() > 10)) {
//                        giftCardNumber.setError("Please enter valid gift card number");
//                        giftCardNumber.requestFocus();
//                        return;
//                    }
                    if (!(securityPin.getText().length() > 0)) {
                        securityPin.setError("Enter gift card pin");
                        securityPin.requestFocus();
                        return;
                    }
//                    if (!(securityPin.getText().length() > 4)) {
//                        securityPin.setError("Please enter valid pin number");
//                        securityPin.requestFocus();
//                        return;
//                    }
                    cardNumber = giftCardNumber.getText().toString().trim();
                    cardPin = securityPin.getText().toString();
                   // loaderText("Retrieving gift card info...");

                    /*
                       Getting Card Information using card number and card pin service
                    */
                    addCardToList();
                }
            });
        }
    }

    private void addCardToList() {
        if (GiftCardDataManager.getInstance().getInCommUser() != null) {
            String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
            enableScreen(false);
            loaderText("Associating gift card...");
            JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_ADDED);//tracking adding gift card
            InCommCardService.addCardToList(userId, cardNumber, cardPin, new InCommCardServiceCallback() {
                @Override
                public void onCardServiceCallback(InCommCard addedCardResponse, Exception error) {
                    enableScreen(true);
                    if (addedCardResponse != null && error == null) {
                        //Navigate to Gift card list screen after it is successfully added into user account
                        alert(context, "Success", "New card added to account successfully");
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
        } else {
            enableScreen(false);
            InCommUserService.getAccessTokenWithUserId(new InCommUserServiceCallBack() {
                @Override
                public void onUserServiceCallback(InCommUser inCommUser, Exception exception) {
                    enableScreen(true);
                    if (inCommUser != null) {
                        GiftCardDataManager.getInstance().setInCommUser(inCommUser);
                        addCardToList();
                    } else {
                        Utils.showErrorAlert(AddExistingCardActivityGiftCard.this, exception);
                    }
                }
            });
        }
    }

    private void alert(Context context, String title, String message) {
        if (!((Activity) context).isFinishing()) {
            if (message == null) {
                message = "Error";
            }
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TransitionManager.slideUp(AddExistingCardActivityGiftCard.this, GiftCardHomeListActivityGiftCard.class);
                    if (GiftCardDataManager.getInstance().getInCommCards() != null) {
                        if (GiftCardDataManager.getInstance().getInCommCards().size() > 0) {
                            refreshGiftCardListActivity();//refreshing Gift card home list screen using broadcast manager
                        }
                    }
                    //Closing all screens after navigate to List screen and reseting datas
                    finish();
                    if (NewCardActivityGiftCard.newCardActivity != null) {
                        NewCardActivityGiftCard.newCardActivity.finish();
                    }

                    GiftCardDataManager.getInstance().resetGiftCardCreate();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent(Constants.BROADCAST_UPDATE_GF_HOME_ACTIVITY);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void initToolbar() {
        setTitle("Add Existing Card");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.background_white));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, R.color.darker_gray));
    }
}
