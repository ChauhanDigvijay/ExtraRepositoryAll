package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CapitalizeTextWatcher;
import com.wearehathway.apps.incomm.Models.InCommCountry;
import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Created by Jeeva
 */
public class PurchaserActivityGiftCard extends GiftCardBaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private EditText purFirstName, purLastName, purEmailAddress;
    private Spinner purCountry;
    private SwitchCompat setPurchaserInfo;
    private RelativeLayout PDFooter;
    private String countryCode;
    private Boolean isItChecked;
    private Boolean isFromReload = false;
    private Context context;
    private String DEFAULT_COUNTRY = "US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchaser);

        isItChecked = false;
        isShowBasketIcon = false;

        setUpToolBar(true);
        setBackButton(true,false);

        context=this;
        //Getting bundle from Reload Screen
        isFromReload = getIntent().getBooleanExtra("isFromReload", false);

        purFirstName = (EditText) findViewById(R.id.purFirstName);
        purLastName = (EditText) findViewById(R.id.purLastName);
        purFirstName.addTextChangedListener(new CapitalizeTextWatcher(purFirstName));
        purLastName.addTextChangedListener(new CapitalizeTextWatcher(purLastName));
        purEmailAddress = (EditText) findViewById(R.id.purEmailAddress);
        purCountry = (Spinner) findViewById(R.id.purCountry);
        purCountry.setPrompt("Select Country");
        setCountrySpinner();

        purCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryCode = GiftCardDataManager.getInstance().getInCommCountries().get(i).getCode();
                if (isItChecked != null) {
                    if (isItChecked) {
                        adapterView.setSelection(i);
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.GRAY);
                    } else {
                        adapterView.setSelection(i);
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setPurchaserInfo = (SwitchCompat) findViewById(R.id.setPurchaserInfo);

        PDFooter = (RelativeLayout) findViewById(R.id.PDFooter);

        setPurchaserInfo.setOnCheckedChangeListener(this);
        PDFooter.setOnClickListener(this);

        initToolbar();

        ///Setting data based on Reload screen bundle
        if (isFromReload) {
            setReloadData();
        } else {
            setCreateData();
        }

    }

    private void initToolbar() {
        setTitle("Purchaser Details");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
    }

    private void setDisableButtons() {
        purFirstName.setEnabled(false);
        purFirstName.setCursorVisible(false);
        purFirstName.setError(null);
        purLastName.setEnabled(false);
        purLastName.setCursorVisible(false);
        purLastName.setError(null);
        purCountry.setEnabled(false);
    }

    private void setEnableButtons() {
        purFirstName.setEnabled(true);
        purFirstName.setCursorVisible(true);
        purLastName.setEnabled(true);
        purLastName.setCursorVisible(true);
        purEmailAddress.setEnabled(true);
        purEmailAddress.setCursorVisible(true);
        purCountry.setEnabled(true);
    }


    private void setReloadData() {
        if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
            if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getFirstName() != null) {
                    if (GiftCardDataManager.getInstance().getIsChecked()) {
                        setPurchaserInfo.setChecked(true);
                        setDisableButtons();
                        purFirstName.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getFirstName());
                        purLastName.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getLastName());
                        purEmailAddress.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getEmailAddress());
                        if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getCountry().equalsIgnoreCase("US")) {
                            purCountry.setSelection(0);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
                            }
                        } else {
                            purCountry.setSelection(1);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
                            }
                        }

                        isItChecked = true;
                        GiftCardDataManager.getInstance().setIsChecked(isItChecked);

                    } else {
                        setPurchaserInfo.setChecked(false);
                        setEnableButtons();
                        purFirstName.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getFirstName());
                        purLastName.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getLastName());
                        purEmailAddress.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getEmailAddress());
                        if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getCountry().equalsIgnoreCase("US")) {
                            purCountry.setSelection(0);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.BLACK);
                            }

                        } else {
                            purCountry.setSelection(1);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.BLACK);
                            }
                        }
                        isItChecked = false;
                        GiftCardDataManager.getInstance().setIsChecked(isItChecked);
                    }
                }
            }
        }
    }

    private void setCreateData() {
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName() != null) {
                    if (GiftCardDataManager.getInstance().getIsChecked()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        setPurchaserInfo.setChecked(true);
                        setDisableButtons();
                        purFirstName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName());
                        purLastName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getLastName());
                        purEmailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
                        if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getCountry().equalsIgnoreCase("US")) {
                            purCountry.setSelection(0);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
                            }
                        } else {
                            purCountry.setSelection(1);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
                            }
                        }

                        isItChecked = true;
                        GiftCardDataManager.getInstance().setIsChecked(isItChecked);

                    } else {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        setPurchaserInfo.setChecked(false);
                        setEnableButtons();
                        purFirstName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName());
                        purLastName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getLastName());
                        purEmailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
                        if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getCountry().equalsIgnoreCase("US")) {
                            purCountry.setSelection(0);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.BLACK);
                            }

                        } else {
                            purCountry.setSelection(1);
                            if (purCountry.getChildAt(0) != null) {
                                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.BLACK);
                            }
                        }
                        isItChecked = false;
                        GiftCardDataManager.getInstance().setIsChecked(isItChecked);
                    }
                }
            }
        }
    }

    private void setCountrySpinner() {
        List<InCommCountry> countries = GiftCardDataManager.getInstance().getInCommCountries();
        String[] countryName = new String[countries.size()];
        for (int i = 0; i < countries.size(); i++) {
            countryName[i] = countries.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, countryName);
        purCountry.setAdapter(adapter);
    }

    private void setPurchaserForReload() {
        purFirstName.setText(GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getFirstName());
        purLastName.setText(GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getLastName());
        if (GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo() != null
                && GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getEmailAddress() != null) {
                purEmailAddress.setText(GiftCardDataManager.getInstance().getGiftCardReload().getPurchaserInfo().getEmailAddress());
        }else{
            if(UserService.getUser().getEmailaddress() != null) {
                purEmailAddress.setText(UserService.getUser().getEmailaddress());
            }
        }
        if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getCountry().equalsIgnoreCase("US")) {
            purCountry.setSelection(0);
            if (purCountry.getChildAt(0) != null) {
                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
            }
        } else {
            purCountry.setSelection(1);
            if (purCountry.getChildAt(0) != null) {
                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
            }
        }
    }

    private void setPurchaserForCreate() {
        purFirstName.setText(GiftCardDataManager.getInstance().getLocalPaymentInfo().getFirstName());
        purLastName.setText(GiftCardDataManager.getInstance().getLocalPaymentInfo().getLastName());
        if (GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo() != null
                && GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress() != null) {
                purEmailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
        }else{
            if(UserService.getUser().getEmailaddress() != null) {
                purEmailAddress.setText(UserService.getUser().getEmailaddress());
            }
        }
        if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getCountry().equalsIgnoreCase("US")) {
            purCountry.setSelection(0);
            if (purCountry.getChildAt(0) != null) {
                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
            }
        } else {
            purCountry.setSelection(1);
            if (purCountry.getChildAt(0) != null) {
                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.GRAY);
            }
        }
    }

    private void setPurchaserValuesForReload() {
        if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
            InCommOrderPurchaser inCommOrderPurchaser = new InCommOrderPurchaser();
            inCommOrderPurchaser.setEmailAddress(purEmailAddress.getText().toString());
            inCommOrderPurchaser.setFirstName(purFirstName.getText().toString());
            inCommOrderPurchaser.setLastName(purLastName.getText().toString());
            //inCommOrderPurchaser.setCountry(countryCode);
            inCommOrderPurchaser.setCountry(DEFAULT_COUNTRY);

            GiftCardDataManager.getInstance().getGiftCardReload().setPurchaserInfo(inCommOrderPurchaser);
            GiftCardDataManager.getInstance().setIsChecked(isItChecked);
        }
    }

    private void setPurchaserValuesForCreate() {
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            InCommOrderPurchaser inCommOrderPurchaser = new InCommOrderPurchaser();
            inCommOrderPurchaser.setEmailAddress(purEmailAddress.getText().toString());
            inCommOrderPurchaser.setFirstName(purFirstName.getText().toString());
            inCommOrderPurchaser.setLastName(purLastName.getText().toString());
            //inCommOrderPurchaser.setCountry(countryCode);
            inCommOrderPurchaser.setCountry(DEFAULT_COUNTRY);

            GiftCardDataManager.getInstance().getGiftCardCreate().setPurchaserInfo(inCommOrderPurchaser);
            GiftCardDataManager.getInstance().setIsChecked(isItChecked);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            isItChecked = isChecked;
            setDisableButtons();
            if (isFromReload) {
                setPurchaserForReload();
            } else {
                setPurchaserForCreate();
            }
        } else {
            isItChecked = isChecked;
            setEnableButtons();
            purFirstName.getText().clear();
            purLastName.getText().clear();
            purEmailAddress.getText().clear();
            purCountry.setSelection(0);
            if (purCountry.getChildAt(0) != null) {
                ((TextView) purCountry.getChildAt(0)).setTextColor(Color.BLACK);
            }
        }

    }

    private boolean isValidEmail(String email) {
        boolean isValidEmail = false;

        String emailExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(emailExpression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValidEmail = true;
        }
        return isValidEmail;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PDFooter:
                if (!Utils.isValidName(purFirstName.getText().toString())) {
                    purFirstName.setError("Enter your first name");
                    purFirstName.requestFocus();
                    return;
                }
                if (!Utils.isValidName(purLastName.getText().toString())) {
                    purLastName.setError("Enter your last name");
                    purLastName.requestFocus();
                    return;
                }
                if (purEmailAddress.getText().toString().equalsIgnoreCase("")) {
                    purEmailAddress.setError("Enter your email address");
                    purEmailAddress.requestFocus();
                    return;
                }
                if (!isValidEmail(purEmailAddress.getText().toString())) {
                    purEmailAddress.setError("Enter a valid email address");
                    purEmailAddress.requestFocus();
                    return;
                }

                //Setting purchaser values based on bundle
                if (isFromReload) {
                    setPurchaserValuesForReload();
                } else {
                    setPurchaserValuesForCreate();
                }
                onBackPressed();

                break;
        }
    }
}
