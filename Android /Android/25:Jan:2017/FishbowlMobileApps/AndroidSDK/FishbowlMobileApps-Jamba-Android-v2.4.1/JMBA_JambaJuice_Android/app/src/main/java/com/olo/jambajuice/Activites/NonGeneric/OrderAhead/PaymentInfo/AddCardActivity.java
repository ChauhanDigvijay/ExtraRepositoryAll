package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo;


import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Confirmation.OrderConfirmationActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.ExpiryDateListener;
import com.olo.jambajuice.BusinessLogic.Interfaces.PlaceOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RedeemedServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
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
import com.olo.jambajuice.Views.Generic.ExpiryDatePicker;
import com.wearehathway.apps.olo.Models.OloOrderInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.card.payment.CameraUnavailableException;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AddCardActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private static int MY_SCAN_REQUEST_CODE = 100;
    private int yearInt;
    private int monthInt;
    private EditText zip;
    private EditText expiry;
    private EditText cardNum;
    private EditText securityCode;
    private Button tv_place_order;
    private ImageButton credit_card;
    private ImageView card_image;
    private ExpiryDatePicker expiryDatePicker;
    private static int prevStatus = 0;
    String cardtype;
    Integer[] imageArray = {R.drawable.visa_card_icon, R.drawable.master_card_icon, R.drawable.discover_icon, R.drawable.american_exp_icon};
    private LinearLayout chooseEGiftCardView, giftCardView;

    public static ArrayList<String> listOfPattern() {
        ArrayList<String> listOfPattern = new ArrayList<String>();

        String ptVisa = "^4[0-9]$";

        listOfPattern.add(ptVisa);

        String ptMasterCard = "^5[1-5]$";

        listOfPattern.add(ptMasterCard);

        String ptDiscover = "^6(?:011|5[0-9]{2})$";

        listOfPattern.add(ptDiscover);

        String ptAmeExp = "^3[47]$";

        listOfPattern.add(ptAmeExp);

        return listOfPattern;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        chooseEGiftCardView = (LinearLayout) findViewById(R.id.chooseEGiftCardView);
        chooseEGiftCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.transitFrom(AddCardActivity.this, EGiftCard.class);
            }
        });
        if (DataManager.getInstance().getCurrentBasket() == null) {
            //Incase the data is not available and activity is recreating.
            finish();
            return;
        }

        setUpView();
        expiry.setOnClickListener(this);
        expiry.setCursorVisible(false);
        expiry.setTextIsSelectable(false);
        tv_place_order.setOnClickListener(this);
        credit_card.setOnClickListener(this);
        initDatePicker();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_BASKET_UI));
        //setUpDummyData();
    }

    private void showDatePicker() {
        expiryDatePicker.show();
    }

    private void initDatePicker() {
        expiryDatePicker = new ExpiryDatePicker(this, new ExpiryDateListener() {
            @Override
            public void onDateChangeListener(int month, int year) {
                monthInt = month;
                yearInt = year;
                if (monthInt <= 9) {
                    expiry.setText("0" + monthInt + "/" + yearInt);
                } else {
                    expiry.setText(monthInt + "/" + yearInt);
                }
                expiry.setError(null);
            }
        });

        expiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });

        /*
            Disabling copy/Paste/Edit
         */
        expiry.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_place_order:
                tv_place_order.setEnabled(false);
                tv_place_order.setClickable(false);

                verifyAndPlaceOrder();
                break;
            case R.id.et_expiryDate:
                showDatePicker();
                break;
            case R.id.scan_credit_card:
                try {
                    if (!isCameraOpened()) {
                        Toast.makeText(getApplicationContext(), "Device's Primary Camera not available", Toast.LENGTH_SHORT).show();
                    } else {
                        scanCard();
                    }
                } catch (CameraUnavailableException e) {
                    Toast.makeText(getApplicationContext(), "Device had an unexpected error opening the camera", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }


    private boolean isCameraOpened() {

        // Camera needs to open
        Camera c = null;
        try {
            c = Camera.open();
        } catch (RuntimeException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return true;
            } else {
                throw new CameraUnavailableException();
            }
        }
        if (c == null) {
            return false;
        } else {
            List<Camera.Size> list = c.getParameters().getSupportedPreviewSizes();
            c.release();

            boolean supportsVGA = false;

            for (Camera.Size s : list) {
                if (s.width == 640 && s.height == 480) {
                    supportsVGA = true;
                    break;
                }
            }

            if (!supportsVGA) {
                return false;
            }
        }
        return true;
    }

    private void scanCard() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        try {
            startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            //resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
            if (scanResult.getFormattedCardNumber() != null) {
                cardNum.setText(scanResult.cardNumber);
            }
            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                expiry.setText(scanResult.expiryMonth + "/" + scanResult.expiryYear);
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                securityCode.setText(scanResult.cvv);
            }

            if (scanResult.postalCode != null) {
                zip.setText(scanResult.postalCode);
            }

        } else {
            Log.e("fail", "Scan is cancelled");
        }

    }

//    public void redeemedservices() {
//
//
//        try {
//
//            RequestQueue queue = Volley.newRequestQueue(AddCardActivity.this);
//
//            JSONObject userJSON = new JSONObject();
//            //userJSON.put("CLP-API-KEY","91225258ddb5c8503dce33719c5deda7");
//
//            JSONObject requestObj = new JSONObject();
//            requestObj.put("data", userJSON);
//
//            JsonObjectRequest jsObjRequest = new JsonObjectRequest(
//                    Request.Method.GET, "http://jamba.clpqa.com/clpapi/mobile/redeemed/12448704/1577", requestObj,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//
//                                if (response.has("categories")) {
//
//                                    JSONArray jsonArray = response.getJSONArray("categories");
//
//                                    if (jsonArray != null && jsonArray.length() > 0) {
//
//
//                                    }
//
//                                }
//
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (error != null) {
//
//                        error.printStackTrace(System.out);
//
//                    }
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Connection", "Keep-Alive");
//                    params.put("Content-Type", "application/json");
//                    params.put("CLP-API-KEY", "91225258ddb5c8503dce33719c5deda7");
//                    return params;
//                }
//            };
//            int socketTimeout = 60000;//30 seconds - change to what you want
//            // RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    socketTimeout,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(jsObjRequest);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//
//    }

//    private void CallRedeemedservices() {
//        final String offerId = DataManager.getInstance().getCurrentBasket().getOfferId();
//        if (StringUtilities.isValidString(offerId)) {
//            RedeemedService.getRedeemedservices(this, offerId, new RedeemedServiceCallback() {
//                @Override
//                public void onRedeemedServiceCallback(JSONObject offerSummary, String error) {
//
//
//                    if (offerSummary != null) {
//                        //setDataOffer(offerSummary);
//                    } else {
//                        //   Utils.showErrorAlert(MyRewardsActivity.this, exception);
//                    }
//                }
//            });
//        }
//    }

    private void orderSuccessfullyPlaced() {

        // redeemedservices();
        //CallRedeemedservices();
        Utils.notifyRemoveBasketUI(this);
        TransitionManager.transitFrom(AddCardActivity.this, OrderConfirmationActivity.class);
        finish();
    }

    private String isSaveInformation() {
        SwitchCompat saveInformationSwitch = (SwitchCompat) findViewById(R.id.saveInformationSwitch);
        return saveInformationSwitch.isChecked() ? "true" : "false";
    }

    private void setUpView() {
        isShowBasketIcon = false;
        createToolBar();
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(android.R.color.white));
        tv_place_order = (Button) findViewById(R.id.tv_place_order);
        credit_card = (ImageButton) findViewById(R.id.scan_credit_card);
        card_image = (ImageView) findViewById(R.id.card_image);
        cardNum = (EditText) findViewById(R.id.cardNumber);
        cardNum.setError(null);
        //cardNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        card_image.setImageResource(0);

        securityCode = (EditText) findViewById(R.id.securityCode);
        zip = (EditText) findViewById(R.id.zip);
        expiry = (EditText) findViewById(R.id.et_expiryDate);
        giftCardView = (LinearLayout) findViewById(R.id.giftCardView);

        if (!UserService.isUserAuthenticated()) {
            LinearLayout saveInformationView = (LinearLayout) findViewById(R.id.saveInformationView);
            saveInformationView.setVisibility(View.GONE);
            giftCardView.setVisibility(View.GONE);
        }

        if (UserService.isUserAuthenticated()) {
            if (DataManager.getInstance().isCurrentBasketSupportGiftCard()) {
                giftCardView.setVisibility(View.VISIBLE);
            } else {
                giftCardView.setVisibility(View.GONE);
            }
        }
        cardNum.addTextChangedListener(this);

    }

    private void verifyAndPlaceOrder() {
        trackButtonWithName("Place Order");
        String cardNumberStr = cardNum.getText().toString().replaceAll("-", "");
        String securityCodeStr = securityCode.getText().toString();
        String zipStr = zip.getText().toString();


        boolean isTransitionAllowed = true;
        if (!Utils.isValidCardNumber(cardNumberStr)) {
            cardNum.requestFocus();
            cardNum.setError("Enter valid card number.");
            //cardNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            card_image.setImageResource(0);
            isTransitionAllowed = false;
        }
        if (!Utils.isValidCVV(securityCodeStr)) {
            if (isTransitionAllowed) {
                securityCode.requestFocus();
            }
            securityCode.setError("Enter valid security code.");
            isTransitionAllowed = false;
        }
        if (!Utils.isValidZip(zipStr)) {
            if (isTransitionAllowed) {
                zip.requestFocus();
            }
            zip.setError("Enter valid zip code.");
            isTransitionAllowed = false;
        }
        if (expiry.getText().toString().equals("")) {
            if (isTransitionAllowed) {
                expiry.requestFocus();
            }
            expiry.setError("Select a valid expiration date.");
            isTransitionAllowed = false;
        }

        if (isTransitionAllowed) {
            OloOrderInfo orderInfo = DataManager.getInstance().getOrderInfo();
            orderInfo.setCardnumber(cardNumberStr);
            orderInfo.setCvv(securityCodeStr);
            orderInfo.setZip(zipStr);
            orderInfo.setExpirymonth(monthInt);
            orderInfo.setExpiryyear(yearInt);
            orderInfo.setSaveonfile(isSaveInformation());
            orderInfo.setBillingAccountId(0);// Billing account Id to 0 incase call fails and user try to add another card.
            orderInfo.setBillingMethod("creditcard"); // Placing order using credit card.
            enableScreen(false);

            BasketService.placeOrder(this, new PlaceOrderCallback() {
                @Override
                public void onPlaceOrderCallback(OrderStatus status, Exception e) {
                    if (e == null) {
                        orderSuccessfullyPlaced();
                    } else {
                        Utils.showErrorAlert(AddCardActivity.this, e);
                    }
                    enableScreen(true);
                    tv_place_order.setEnabled(true);
                    tv_place_order.setClickable(true);


                }
            });
        } else {
            tv_place_order.setEnabled(true);
            tv_place_order.setClickable(true);
        }
    }

    private void createToolBar() {
        setUpToolBar(true);
        setTitle("Enter Credit Card", getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        setBackButton(true,true);
    }

    @Override
    protected void handleBroadCastReceiver(Intent intent) {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        System.out.println(charSequence + "called");
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        String ccNum = s.toString();

        if (ccNum.length() >= 2) {
            for (int j = 0; j < listOfPattern().size(); j++) {
                if (ccNum.substring(0, 2).matches(listOfPattern().get(j))) {
                    cardNum.setError(null);
                    //cardNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[j], 0);
                    card_image.setImageResource(imageArray[j]);

                    cardtype = String.valueOf(j);
                    return;
                }
            }
        } else {
            cardNum.setError(null);
            //cardNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            card_image.setImageResource(0);

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        cardNum.removeTextChangedListener(this);
        int currentPos = cardNum.getSelectionStart();
        String formattedString = "";
        String originalString = editable.toString();
        String cardType = cardNum.getText().toString();

        if (originalString.contains("-")) {
            originalString = originalString.replaceAll("-", "");
        }
        for (int i = 4; i < originalString.length(); i += 4) {
            formattedString += originalString.substring(i - 4, i) + "-";
        }
        if (originalString.length() > 0) {
            if (originalString.length() % 4 != 0) {
                formattedString += originalString.substring(originalString.length() - originalString.length() % 4);
            } else {
                formattedString += originalString.substring(originalString.length() - 4);
            }
        }
        cardNum.setText(formattedString);
        int length = formattedString.length();
        if (length - prevStatus == 1) {
            cardNum.setSelection(currentPos);
        }
        if (length - prevStatus == 2) {
            cardNum.setSelection(currentPos + 1);
        }
        if (length - prevStatus == -1) {
            cardNum.setSelection(currentPos);
        }
        if (length - prevStatus == -2) {
            if (currentPos != 0) {
                cardNum.setSelection(currentPos - 1);
            }
        }
        if (length - prevStatus == 0) {
            cardNum.setSelection(currentPos);
        }
        prevStatus = length;
        cardNum.addTextChangedListener(this);


        if (!cardNum.getText().toString().equalsIgnoreCase("")) {
            for (int i = 0; i < listOfPattern().size(); i++) {
                if (cardNum.getText().toString().matches(listOfPattern().get(i))) {
                    cardNum.setError(null);
                    //cardNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[i], 0);
                    card_image.setImageResource(imageArray[i]);

                    cardtype = String.valueOf(i);
                }

            }
        } else {
            cardNum.setError(null);
            //cardNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            card_image.setImageResource(0);
        }

    }


}
