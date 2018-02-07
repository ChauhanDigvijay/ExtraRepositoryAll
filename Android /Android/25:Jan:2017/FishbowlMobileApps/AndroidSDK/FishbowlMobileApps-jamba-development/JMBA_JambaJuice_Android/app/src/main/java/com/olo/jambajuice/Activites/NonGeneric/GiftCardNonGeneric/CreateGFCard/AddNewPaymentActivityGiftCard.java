package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.Generic.WebViewActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard.AutoReloadActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard.ReloadActivityGiftCard;
import com.olo.jambajuice.BusinessLogic.Interfaces.ExpiryDateListener;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardCreate;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardReload;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.AddrTextWatcher;
import com.olo.jambajuice.Views.Generic.CapitalizeTextWatcher;
import com.olo.jambajuice.Views.Generic.ExpiryDatePicker;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.squareup.picasso.Picasso;
import com.wearehathway.apps.incomm.Interfaces.InCommSaveUserPaymentAccountCallback;
import com.wearehathway.apps.incomm.Models.InCommBrandCreditCardType;
import com.wearehathway.apps.incomm.Models.InCommCountry;
import com.wearehathway.apps.incomm.Models.InCommStates;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Services.InCommUserPaymentAccountService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.card.payment.CameraUnavailableException;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/*
 * Created By jeeva
 */

public class AddNewPaymentActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,TextWatcher {

    private static int MY_SCAN_REQUEST_CODE = 100;
    List<InCommStates> states;
    List<InCommStates> filteredStates;
    private RelativeLayout expirationDateLayout, selectCardTypeLayout, savePaymentLayout, TermsAndConditions;
    private SemiBoldTextView expirationDate;
    private SemiBoldButton submitButton;
    private String countryCode, stateCode, creditCardName;
    private EditText firstName, lastName, streetAddress1, streetAddress2, city, zipCode,
            cardNumber, ccvCode;
    private ImageView selectCardImage;
    private SwitchCompat checkSaveBtn;
    private Spinner country, state, selectCardType;
    private ArrayAdapter<String> countryAdapter, stateAdapter, creditCardAdapter;
    private String[] creCardImg;
    private Context context;
    private boolean isFromReload = false;
    private boolean islocalChecked = false;
    private boolean isitFromAutoReload = false;
    private InCommSubmitPayment inCommAutoReloadSubmitPayment;
    private ExpiryDatePicker expiryDatePicker;
    private int yearInt;
    private int monthInt;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private ImageButton scanCreateCard;
    private static int prevStatus = 0;

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
        setContentView(R.layout.activity_add_new_payment);

        context = this;

        isShowBasketIcon = false;

        onCheckedChangeListener = this;

        setUpToolBar(true);
        setBackButton(true,false);

        /*
           Getting bundle from Reload or AutoReload Screen .if both bundle false mean it is from purchase gift card screen
         */
        isFromReload = getIntent().getBooleanExtra("isFromReload", false);
        isitFromAutoReload = getIntent().getBooleanExtra("isItFromAutoReload", false);

        expirationDateLayout = (RelativeLayout) findViewById(R.id.expirationDateLayout);
        selectCardTypeLayout = (RelativeLayout) findViewById(R.id.selectCardTypeLayout);
        savePaymentLayout = (RelativeLayout) findViewById(R.id.savePaymentLayout);
        TermsAndConditions = (RelativeLayout) findViewById(R.id.TermsAndConditions);

        expirationDate = (SemiBoldTextView) findViewById(R.id.expirationDate);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        firstName.addTextChangedListener(new CapitalizeTextWatcher(firstName));
        lastName.addTextChangedListener(new CapitalizeTextWatcher(lastName));
        streetAddress1 = (EditText) findViewById(R.id.streetAddress1);
        streetAddress2 = (EditText) findViewById(R.id.streetAddress2);
        city = (EditText) findViewById(R.id.city);
        streetAddress1.addTextChangedListener(new AddrTextWatcher(streetAddress1));
        streetAddress2.addTextChangedListener(new AddrTextWatcher(streetAddress2));
        city.addTextChangedListener(new AddrTextWatcher(city));
        zipCode = (EditText) findViewById(R.id.zipCode);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        cardNumber.addTextChangedListener(this);
        ccvCode = (EditText) findViewById(R.id.ccvCode);
        scanCreateCard = (ImageButton) findViewById(R.id.scan_credit_card);
        scanCreateCard.setOnClickListener(this);

        country = (Spinner) findViewById(R.id.country);
        country.setPrompt("Country");
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                countryCode = GiftCardDataManager.getInstance().getInCommCountries().get(position).getCode();
                states = new ArrayList<InCommStates>(GiftCardDataManager.getInstance().getInCommStates());

                filteredStates = new ArrayList<InCommStates>();

                /*
                    Filtering states based on selected country and setting in to state adapter
                 */
                for (int i = 0; i < states.size(); i++) {
                    if (countryCode.equalsIgnoreCase(states.get(i).getCountryCode())) {
                        filteredStates.add(states.get(i));
                    }
                }


                final String[] stateName = new String[filteredStates.size()];
                for (int j = 0; j < filteredStates.size(); j++) {
                    stateName[j] = filteredStates.get(j).getName();
                }

                stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_textview) {

                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };

                stateAdapter.setDropDownViewResource(R.layout.spinner_textview);
                stateAdapter.add("State");
                stateAdapter.addAll(stateName);
                state.setAdapter(stateAdapter);
                stateCode=null;
                if (isFromReload) {
                    if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo() != null) {
                        if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getStateCode() != null) {
                            for (int j = 0; j < filteredStates.size(); j++) {
                                if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getStateCode().equalsIgnoreCase(filteredStates.get(j).getCode())) {
                                    state.setSelection(j + 1);
                                }
                            }
                        }
                    }
                } else {
                    if (GiftCardDataManager.getInstance().getLocalPaymentInfo() != null) {
                        if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getStateCode() != null) {
                            for (int j = 0; j < filteredStates.size(); j++) {
                                if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getStateCode().equalsIgnoreCase(filteredStates.get(j).getCode())) {
                                    state.setSelection(j + 1);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        state = (Spinner) findViewById(R.id.state);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    stateCode = filteredStates.get(position - 1).getCode();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setCountryStateSpinner();

        selectCardType = (Spinner) findViewById(R.id.selectCardType);
        selectCardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                creditCardName = GiftCardDataManager.getInstance().getCreditCardTypes().get(position).getCreditCardType();
                Picasso.with(context)
                        .load(creCardImg[position])
                        .placeholder(R.drawable.product_placeholder)
                        .error(R.drawable.product_placeholder)
                        .into(selectCardImage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setCreditCardTypeSpinner();

        checkSaveBtn = (SwitchCompat) findViewById(R.id.checkSaveBtn);
        checkSaveBtn.setOnCheckedChangeListener(onCheckedChangeListener);

        submitButton = (SemiBoldButton) findViewById(R.id.submitButton);

        selectCardImage = (ImageView) findViewById(R.id.selectCardImage);

        /*
            Enabling and disabling save payment toggle based on bundle
         */
        if (isitFromAutoReload) {
            savePaymentLayout.setBackgroundColor(Color.WHITE);
            checkSaveBtn.setOnCheckedChangeListener(null);
            checkSaveBtn.setChecked(true);
            checkSaveBtn.setOnCheckedChangeListener(onCheckedChangeListener);
            savePaymentLayout.setAlpha((float) 0.5);
        } else {
            savePaymentLayout.setBackgroundColor(Color.WHITE);
            checkSaveBtn.setChecked(false);
            savePaymentLayout.setAlpha((float) 1.0);
        }


        submitButton.setOnClickListener(this);
        expirationDateLayout.setOnClickListener(this);
        savePaymentLayout.setOnClickListener(this);
        selectCardTypeLayout.setOnClickListener(this);
        TermsAndConditions.setOnClickListener(this);

        initDatePicker();
        initToolbar();
        autoFillUserDetails();
        setData();


    }

    private void autoFillUserDetails(){
        if(UserService.isUserAuthenticated()){
            if(UserService.getUser() != null){
                if(UserService.getUser().getFirstname() != null){
                    firstName.setText(UserService.getUser().getFirstname());
                }
                if(UserService.getUser().getLastname() != null){
                    lastName.setText(UserService.getUser().getLastname());
                }
            }
        }
    }

    private void initToolbar() {
        setTitle("Add New Card");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    private void initDatePicker() {
        expiryDatePicker = new ExpiryDatePicker(this, new ExpiryDateListener() {
            @Override
            public void onDateChangeListener(int month, int year) {
                monthInt = month;
                yearInt = year;
                if (monthInt <= 9) {
                    expirationDate.setText("0" + monthInt + "/" + yearInt);
                } else {
                    expirationDate.setText(monthInt + "/" + yearInt);

                }
            }
        });

        expirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        expirationDate.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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

    private void showDatePicker() {
        expiryDatePicker.show();
    }

    /*
        Setting the data based on bundle
     */
    private void setData() {
        if (isFromReload) {
            if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo() != null) {
                    if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getFirstName() != null) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        firstName.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getFirstName());
                        lastName.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getLastName());
                        streetAddress1.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getStreetAddress1());
                        streetAddress2.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getStreetAddress2());
                        city.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCity());
                        zipCode.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getZipPostalCode());
                        cardNumber.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCreditCardNumber());
                        ccvCode.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCreditCardVerificationCode());
                        monthInt = GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCreditCardExpirationMonth();
                        yearInt = GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCreditCardExpirationYear();
                        expirationDate.setText(new StringBuilder().append(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCreditCardExpirationMonth()).append("/").append(GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCreditCardExpirationYear()));
                        checkSaveBtn.setChecked(GiftCardDataManager.getInstance().getIsSaveNewPayment());

                        List<InCommCountry> countries = GiftCardDataManager.getInstance().getInCommCountries();
                        for (int i = 0; i < countries.size(); i++) {
                            if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCountry().equalsIgnoreCase(countries.get(i).getCode())) {
                                country.setSelection(i);
                            }
                        }


                        List<InCommBrandCreditCardType> creditCardTypes = GiftCardDataManager.getInstance().getCreditCardTypes();
                        for (int i = 0; i < creditCardTypes.size(); i++) {
                            if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getCreditCardType().equalsIgnoreCase(creditCardTypes.get(i).getCreditCardType())) {
                                selectCardType.setSelection(i);
                                Picasso.with(context)
                                        .load(creditCardTypes.get(i).getThumbnailImageUrl())
                                        .placeholder(R.drawable.product_placeholder)
                                        .error(R.drawable.product_placeholder)
                                        .into(selectCardImage);
                            }
                        }
                    }
                }
            }
        } else {
            if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo() != null) {
                    if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getFirstName() != null) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        firstName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getFirstName());
                        lastName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getLastName());
                        streetAddress1.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getStreetAddress1());
                        streetAddress2.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getStreetAddress2());
                        city.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCity());
                        zipCode.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getZipPostalCode());
                        cardNumber.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCreditCardNumber());
                        ccvCode.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCreditCardVerificationCode());
                        monthInt = GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCreditCardExpirationMonth();
                        yearInt = GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCreditCardExpirationYear();
                        expirationDate.setText(new StringBuilder().append(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCreditCardExpirationMonth()).append("/").append(GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCreditCardExpirationYear()));
                        checkSaveBtn.setChecked(GiftCardDataManager.getInstance().getIsSaveNewPayment());

                        List<InCommCountry> countries = GiftCardDataManager.getInstance().getInCommCountries();
                        for (int i = 0; i < countries.size(); i++) {
                            if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCountry().equalsIgnoreCase(countries.get(i).getCode())) {
                                country.setSelection(i);
                            }
                        }


                        List<InCommBrandCreditCardType> creditCardTypes = GiftCardDataManager.getInstance().getCreditCardTypes();
                        for (int i = 0; i < creditCardTypes.size(); i++) {
                            if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getCreditCardType().equalsIgnoreCase(creditCardTypes.get(i).getCreditCardType())) {
                                selectCardType.setSelection(i);
                                Picasso.with(context)
                                        .load(creditCardTypes.get(i).getThumbnailImageUrl())
                                        .placeholder(R.drawable.product_placeholder)
                                        .error(R.drawable.product_placeholder)
                                        .into(selectCardImage);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setCreditCardTypeSpinner() {
        List<InCommBrandCreditCardType> creditCardTypes = GiftCardDataManager.getInstance().getCreditCardTypes();
        String[] creditCardName = new String[creditCardTypes.size()];
        creCardImg = new String[creditCardTypes.size()];
        for (int i = 0; i < creditCardTypes.size(); i++) {
            creditCardName[i] = creditCardTypes.get(i).getCreditCardType();
            creCardImg[i] = creditCardTypes.get(i).getThumbnailImageUrl();
        }
        creditCardAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, creditCardName);
        selectCardType.setAdapter(creditCardAdapter);
    }

    private void setCountryStateSpinner() {
        List<InCommCountry> countries = GiftCardDataManager.getInstance().getInCommCountries();
        String[] countryName = new String[countries.size()];
        for (int i = 0; i < countries.size(); i++) {
            countryName[i] = countries.get(i).getName();
        }
        countryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, countryName);
        country.setAdapter(countryAdapter);

        countryCode = countries.get(0).getCode();

        states = GiftCardDataManager.getInstance().getInCommStates();

        /*
             Filtering states based on selected country and setting in to state adapter
          */
        filteredStates = new ArrayList<InCommStates>();
        for (int i = 0; i < states.size(); i++) {
            if (countryCode.equalsIgnoreCase(states.get(i).getCountryCode())) {
                filteredStates.add(states.get(i));
            }
        }

        final String[] stateName = new String[filteredStates.size()];
        for (int j = 0; j < filteredStates.size(); j++) {
            stateName[j] = filteredStates.get(j).getName();
        }

        stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };

        stateAdapter.setDropDownViewResource(R.layout.spinner_textview);
        stateAdapter.add("State");
        stateAdapter.addAll(stateName);
        state.setAdapter(stateAdapter);
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
                cardNumber.setText(scanResult.cardNumber);
            }
            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                expirationDate.setText(scanResult.expiryMonth + "/" + scanResult.expiryYear);
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                ccvCode.setText(scanResult.cvv);
            }

            if (scanResult.postalCode != null) {
                zipCode.setText(scanResult.postalCode);
            }

        } else {
            Log.e("fail", "Scan is cancelled");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expirationDateLayout:
                showDatePicker();
                break;
            case R.id.selectCardTypeLayout:
                selectCardType.performClick();
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

            case R.id.TermsAndConditions:
                WebViewActivity.show(this, "Terms and Conditions", "http://www.jambajuice.com/company-info/JambaCardTermsandConditions");
                break;
            case R.id.submitButton:
                if (!Utils.isValidName(firstName.getText().toString())) {
                    firstName.setError("Enter your first name");
                    firstName.requestFocus();
                    return;
                }
                if (!Utils.isValidName(lastName.getText().toString())) {
                    lastName.setError("Enter your last name");
                    lastName.requestFocus();
                    return;
                }
                if (streetAddress1.getText().toString().equalsIgnoreCase("")) {
                    streetAddress1.setError("Enter your street address1");
                    streetAddress1.requestFocus();
                    return;
                }
                if (streetAddress1.getText().length() < 4 || streetAddress1.getText().length() > 255) {
                    streetAddress1.setError("Street address should have 4 - 255 characters");
                    streetAddress1.requestFocus();
                    return;
                }
                if (stateCode == null) {
                    Utils.showAlert(context, "Please select state", "Alert");
                    return;
                }
                if (!Utils.isValidName(city.getText().toString())) {
                    city.setError("Enter your city");
                    city.requestFocus();
                    return;
                }
                if (zipCode.getText().toString().equalsIgnoreCase("")) {
                    zipCode.setError("Enter your zip code");
                    zipCode.requestFocus();
                    return;
                }
                if (!Utils.isValidZip(zipCode.getText().toString())) {
                    zipCode.setError("Enter a valid zip code");
                    zipCode.requestFocus();
                    return;
                }
                if (cardNumber.getText().toString().equalsIgnoreCase("")) {
                    cardNumber.setError("Enter your card Number");
                    cardNumber.requestFocus();
                    return;
                }
                if (!Utils.isValidCardNumber(cardNumber.getText().toString())) {
                    cardNumber.setError("Enter a valid card number");
                    cardNumber.requestFocus();
                    return;
                }
                if (ccvCode.getText().toString().equalsIgnoreCase("")) {
                    ccvCode.setError("Enter CCV code");
                    ccvCode.requestFocus();
                    return;
                }
                if (!Utils.isValidCVV(ccvCode.getText().toString())) {
                    ccvCode.setError("Enter a valid CCV code");
                    ccvCode.requestFocus();
                    return;
                }
                if (expirationDate.getText().toString().equalsIgnoreCase("")) {
                    Utils.alert(context, "Alert", "Select expiration date");
                    expirationDate.requestFocus();
                    return;
                }

                if (!isValidExpirationDate()) {
                    Utils.alert(context, "Alert", "Select a valid expiration date");
                    expirationDate.requestFocus();
                    return;
                }

                //Setting values and navigate to screens based on bundle
                if (isFromReload) {
                    setReloadPaymentValues();
                    TransitionManager.transitFrom(AddNewPaymentActivityGiftCard.this, ReloadActivityGiftCard.class, true);
                } else if (isitFromAutoReload) {
                    setAutoReloadPaymentValues();
                } else {
                    setCreatePaymentValues();
                    TransitionManager.transitFrom(AddNewPaymentActivityGiftCard.this, PurchaseNewCardActivityGiftCard.class, true);
                }
                break;
        }
    }


    private boolean isValidExpirationDate() {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;

        if (yearInt > currentYear) {
            return true;
        } else if (yearInt == currentYear) {
            if (monthInt >= currentMonth) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void setAutoReloadPaymentValues() {
        inCommAutoReloadSubmitPayment = new InCommSubmitPayment();
        inCommAutoReloadSubmitPayment.setCity(city.getText().toString());
        inCommAutoReloadSubmitPayment.setCountry(countryCode);
        inCommAutoReloadSubmitPayment.setCreditCardExpirationMonth(monthInt);
        inCommAutoReloadSubmitPayment.setCreditCardExpirationYear(yearInt);
        inCommAutoReloadSubmitPayment.setCreditCardNumber(cardNumber.getText().toString().replace("-",""));
        inCommAutoReloadSubmitPayment.setCreditCardVerificationCode(ccvCode.getText().toString());
        inCommAutoReloadSubmitPayment.setCreditCardType(creditCardName);
        inCommAutoReloadSubmitPayment.setFirstName(firstName.getText().toString());
        inCommAutoReloadSubmitPayment.setLastName(lastName.getText().toString());
        String payStateCode = stateCode.replace(countryCode + "-", "");
        inCommAutoReloadSubmitPayment.setStateProvince(payStateCode);
        inCommAutoReloadSubmitPayment.setStreetAddress1(streetAddress1.getText().toString());
        inCommAutoReloadSubmitPayment.setStreetAddress2(streetAddress2.getText().toString());
        inCommAutoReloadSubmitPayment.setZipPostalCode(zipCode.getText().toString());
        inCommAutoReloadSubmitPayment.setOrderPaymentMethod("CreditCard");

        /*
            For Auto Reload purpose payment information should be saved
         */
        savePayment();
    }

    private void savePayment() {
        enableScreen(false);
        loaderText("Saving payment info...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommUserPaymentAccount inCommUserPaymentAccount = new InCommUserPaymentAccount(inCommAutoReloadSubmitPayment, userId);
        InCommUserPaymentAccountService.saveUserPaymentAccount(userId, inCommUserPaymentAccount, new InCommSaveUserPaymentAccountCallback() {
            @Override
            public void onPaymentAccountSaveServiceCallback(InCommUserPaymentAccount paymentAccount, Exception exception) {
                enableScreen(true);
                if (paymentAccount != null && exception == null) {
                    GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setPaymentAccountId(paymentAccount.getId());
                    GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().setCreditCardNumber(paymentAccount.getCreditCardNumber());
                    GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().setCreditCardType(paymentAccount.getCreditCardTypeCode());
                    TransitionManager.transitFrom(AddNewPaymentActivityGiftCard.this, AutoReloadActivityGiftCard.class, true);
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
                                savePayment();
                            }
                        }
                    });
                }else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Therefore your payment information could not be saved in your account. Do you want to try again?", "payment");
                    }
                } else {
                    alertWithTryAgain(context, "Failure", "Unexpected error occurred while processing your request. Therefore your payment information could not be saved in your account. Do you want to try again?", "payment");
                }
            }
        });
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
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (whichFailed.equalsIgnoreCase("payment")) {
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

    private void setCreatePaymentValues() {
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            if (GiftCardDataManager.getInstance().getIsChecked()) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo() != null) {
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().setFirstName(firstName.getText().toString());
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().setLastName(lastName.getText().toString());
                    GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().setCountry(countryCode);
                   /* if ((!GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName().equals(firstName.getText().toString()) ||
                            !GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getLastName().equals(lastName.getText().toString()) ||
                            !GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getCountry().equals(countryCode))) {
                        GiftCardDataManager.getInstance().getGiftCardCreate().resetPurchaserInfo();
                    }*/
                }
            }

            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardCreate().getTotalOrderAmount());
            inCommOrderPayment.setCity(city.getText().toString());
            inCommOrderPayment.setCountry(countryCode);
            inCommOrderPayment.setCreditCardExpirationMonth(monthInt);
            inCommOrderPayment.setCreditCardExpirationYear(yearInt);
            inCommOrderPayment.setCreditCardNumber(cardNumber.getText().toString().replace("-",""));
            inCommOrderPayment.setCreditCardVerificationCode(ccvCode.getText().toString());
            inCommOrderPayment.setCreditCardType(creditCardName);
            inCommOrderPayment.setFirstName(firstName.getText().toString());
            inCommOrderPayment.setLastName(lastName.getText().toString());
            String payStateCode = stateCode.replace(countryCode + "-", "");
            inCommOrderPayment.setStateProvince(payStateCode);
            inCommOrderPayment.setStreetAddress1(streetAddress1.getText().toString());
            inCommOrderPayment.setStreetAddress2(streetAddress2.getText().toString());
            inCommOrderPayment.setZipPostalCode(zipCode.getText().toString());
            inCommOrderPayment.setOrderPaymentMethod("CreditCard");

            GiftCardDataManager.getInstance().getGiftCardCreate().setPaymentInfo(inCommOrderPayment);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(islocalChecked);

            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardNumber(cardNumber.getText().toString());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardType(creditCardName);
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setFirstName(firstName.getText().toString());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setLastName(lastName.getText().toString());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCountry(countryCode);
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setStateCode(stateCode);
        } else {
            GiftCardCreate giftCardCreate = new GiftCardCreate();
            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            giftCardCreate.setTotalOrderAmount(0.00);
            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);

            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardCreate().getTotalOrderAmount());
            inCommOrderPayment.setCity(city.getText().toString());
            inCommOrderPayment.setCountry(countryCode);
            inCommOrderPayment.setCreditCardExpirationMonth(monthInt);
            inCommOrderPayment.setCreditCardExpirationYear(yearInt);
            inCommOrderPayment.setCreditCardNumber(cardNumber.getText().toString().replace("-",""));
            inCommOrderPayment.setCreditCardVerificationCode(ccvCode.getText().toString());
            inCommOrderPayment.setCreditCardType(creditCardName);
            inCommOrderPayment.setFirstName(firstName.getText().toString());
            inCommOrderPayment.setLastName(lastName.getText().toString());
            String payStateCode = stateCode.replace(countryCode + "-", "");
            inCommOrderPayment.setStateProvince(payStateCode);
            inCommOrderPayment.setStreetAddress1(streetAddress1.getText().toString());
            inCommOrderPayment.setStreetAddress2(streetAddress2.getText().toString());
            inCommOrderPayment.setZipPostalCode(zipCode.getText().toString());
            inCommOrderPayment.setOrderPaymentMethod("CreditCard");


            giftCardCreate.setPaymentInfo(inCommOrderPayment);
            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(islocalChecked);

            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardNumber(cardNumber.getText().toString());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCreditCardType(creditCardName);
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setFirstName(firstName.getText().toString());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setLastName(lastName.getText().toString());
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setCountry(countryCode);
            GiftCardDataManager.getInstance().getLocalPaymentInfo().setStateCode(stateCode);
        }
    }


    private void setReloadPaymentValues() {
        if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            if (GiftCardDataManager.getInstance().getIsChecked()) {
                if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo() != null) {
                    GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().setFirstName(firstName.getText().toString());
                    GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().setLastName(lastName.getText().toString());
                    GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().setCountry(countryCode);
                }
            }
            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardReload().getAmount());
            inCommOrderPayment.setCity(city.getText().toString());
            inCommOrderPayment.setCountry(countryCode);
            inCommOrderPayment.setCreditCardExpirationMonth(monthInt);
            inCommOrderPayment.setCreditCardExpirationYear(yearInt);
            inCommOrderPayment.setCreditCardNumber(cardNumber.getText().toString().replace("-",""));
            inCommOrderPayment.setCreditCardVerificationCode(ccvCode.getText().toString());
            inCommOrderPayment.setCreditCardType(creditCardName);
            inCommOrderPayment.setFirstName(firstName.getText().toString());
            inCommOrderPayment.setLastName(lastName.getText().toString());
            String payStateCode = stateCode.replace(countryCode + "-", "");
            inCommOrderPayment.setStateProvince(payStateCode);
            inCommOrderPayment.setStreetAddress1(streetAddress1.getText().toString());
            inCommOrderPayment.setStreetAddress2(streetAddress2.getText().toString());
            inCommOrderPayment.setZipPostalCode(zipCode.getText().toString());
            inCommOrderPayment.setOrderPaymentMethod("CreditCard");

            GiftCardDataManager.getInstance().getGiftCardReload().setPaymentInfo(inCommOrderPayment);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(islocalChecked);

            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardNumber(cardNumber.getText().toString());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardType(creditCardName);
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setFirstName(firstName.getText().toString());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setLastName(lastName.getText().toString());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCountry(countryCode);
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setStateCode(stateCode);
        } else {
            GiftCardReload giftCardReload = new GiftCardReload();
            InCommSubmitPayment inCommOrderPayment = new InCommSubmitPayment();
            giftCardReload.setAmount(0.00);
            GiftCardDataManager.getInstance().setGiftCardReload(giftCardReload);

            inCommOrderPayment.setAmount(GiftCardDataManager.getInstance().getGiftCardReload().getAmount());
            inCommOrderPayment.setCity(city.getText().toString());
            inCommOrderPayment.setCountry(countryCode);
            inCommOrderPayment.setCreditCardExpirationMonth(monthInt);
            inCommOrderPayment.setCreditCardExpirationYear(yearInt);
            inCommOrderPayment.setCreditCardNumber(cardNumber.getText().toString().replace("-",""));
            inCommOrderPayment.setCreditCardVerificationCode(ccvCode.getText().toString());
            inCommOrderPayment.setCreditCardType(creditCardName);
            inCommOrderPayment.setFirstName(firstName.getText().toString());
            inCommOrderPayment.setLastName(lastName.getText().toString());
            String payStateCode = stateCode.replace(countryCode + "-", "");
            inCommOrderPayment.setStateProvince(payStateCode);
            inCommOrderPayment.setStreetAddress1(streetAddress1.getText().toString());
            inCommOrderPayment.setStreetAddress2(streetAddress2.getText().toString());
            inCommOrderPayment.setZipPostalCode(zipCode.getText().toString());
            inCommOrderPayment.setOrderPaymentMethod("CreditCard");


            giftCardReload.setPaymentInfo(inCommOrderPayment);
            GiftCardDataManager.getInstance().setGiftCardReload(giftCardReload);
            GiftCardDataManager.getInstance().setIsSaveNewPayment(islocalChecked);

            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardNumber(cardNumber.getText().toString());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCreditCardType(creditCardName);
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setFirstName(firstName.getText().toString());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setLastName(lastName.getText().toString());
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setCountry(countryCode);
            GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().setStateCode(stateCode);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isitFromAutoReload) {
            checkSaveBtn.setOnCheckedChangeListener(null);
            checkSaveBtn.setChecked(!isChecked);
            checkSaveBtn.setOnCheckedChangeListener(this);
            Utils.alert(context, "Alert", "Inorder to configure auto reload setting you need to save your payment account");
        } else {
            if (isChecked) {
                islocalChecked = isChecked;
            } else {
                islocalChecked = isChecked;
            }
        }
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
                    cardNumber.setError(null);
                    selectCardType.setSelection(j);
                    return;
                }
            }
        } else {
            cardNumber.setError(null);
            selectCardType.setSelection(0);

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        cardNumber.removeTextChangedListener(this);
        int currentPos = cardNumber.getSelectionStart();
        String formattedString = "";
        String originalString = editable.toString();
        String cardType = cardNumber.getText().toString();

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
        cardNumber.setText(formattedString);
        int length = formattedString.length();
        if (length - prevStatus == 1) {
            cardNumber.setSelection(currentPos);
        }
        if (length - prevStatus == 2) {
            cardNumber.setSelection(currentPos + 1);
        }
        if (length - prevStatus == -1) {
            cardNumber.setSelection(currentPos);
        }
        if (length - prevStatus == -2) {
            if (currentPos != 0) {
                cardNumber.setSelection(currentPos - 1);
            }
        }
        if (length - prevStatus == 0) {
            cardNumber.setSelection(currentPos);
        }
        prevStatus = length;
        cardNumber.addTextChangedListener(this);


        if (!cardNumber.getText().toString().equalsIgnoreCase("")) {
            for (int i = 0; i < listOfPattern().size(); i++) {
                if (cardNumber.getText().toString().matches(listOfPattern().get(i))) {
                    cardNumber.setError(null);
                    selectCardType.setSelection(i);
                }

            }
        } else {
            cardNumber.setError(null);
            selectCardType.setSelection(0);
        }

    }
}
