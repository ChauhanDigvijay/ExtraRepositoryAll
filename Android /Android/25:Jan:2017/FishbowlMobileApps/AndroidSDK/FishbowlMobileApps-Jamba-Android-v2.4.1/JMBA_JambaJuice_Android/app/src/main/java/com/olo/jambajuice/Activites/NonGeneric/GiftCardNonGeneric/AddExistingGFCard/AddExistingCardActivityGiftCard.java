package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
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
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Services.InCommCardService;
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

        setUpToolBar(true,true);
        setBackButton(false,false);

        RelativeLayout AEFooter = (RelativeLayout) findViewById(R.id.AEFooter);

        giftCardNumber = (EditText) findViewById(R.id.giftCardNumber);

        giftCardNumber.setFilters(new InputFilter[] {
                new InputFilter() {

                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-zA-Z0-9]+")){ // here no space character
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
                    if (!(securityPin.getText().length() > 0)) {
                        securityPin.setError("Enter 4 digit pin");
                        securityPin.requestFocus();
                        return;
                    }
                    cardNumber = giftCardNumber.getText().toString().trim();
                    cardPin = securityPin.getText().toString();
                    enableScreen(false);
                    loaderText("Retrieving gift card info...");

                    /*
                       Getting Card Information using card number and card pin service
                    */
                    getCardInformation();
                }
            });
        }
    }

    private void getCardInformation(){
        InCommCardService.getCardByNumber(cardNumber, cardPin, true, new InCommCardServiceCallback() {
            @Override
            public void onCardServiceCallback(InCommCard card, Exception exception) {
                enableScreen(true);
                if (card != null && exception == null) {
                    GiftCardDataManager.getInstance().setInCommCard(card);
                    TransitionManager.slideUp(AddExistingCardActivityGiftCard.this, CardInfoActivityGiftCard.class);
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
                                getCardInformation();
                            }
                        }
                    });
                }
                else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        Utils.alert(context, "Failure", "There is some problem in Network Connection. Please Try Again.");
                    }
                } else {
                    Utils.alert(context, "Failure", Utils.responseErrorNull());
                }
            }
        });
    }
    private void initToolbar() {
        setTitle("Add Existing Card");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.background_white));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, R.color.darker_gray));
    }
}
