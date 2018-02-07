package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardCreate;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardReload;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;

/*
    Created by jeeva
 */

public class SelectAmountActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    private Button tenDollar, twoFiveDollar, fiftyDollar, fiveDollar, sevenFiveDollar, oneHundredDollar;
    private EditText customAmount;
    private TextView selectAmountDollor;
    private ViewGroup.LayoutParams tdParams, tfParams, ffParams, fdParams, ohdParams, sfdParams, cdParams;
    private Context context;
    private InputMethodManager inputMethodManager;
    private RelativeLayout SAFooter,customAmountBorder;
    private String selectedAmount = "0.00";
    private SemiBoldTextView infoText;
    private boolean isFromReload = false;
    private boolean isItFromAutoReload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_amount);

        context = this;
        isShowBasketIcon = false;
        setUpToolBar(true);
        setBackButton(true,false);

        //Getting bundle
        isFromReload = getIntent().getBooleanExtra("isFromReload", false);
        isItFromAutoReload = getIntent().getBooleanExtra("FromAutoReload", false);

        tenDollar = (Button) findViewById(R.id.tenDollar);
        twoFiveDollar = (Button) findViewById(R.id.twoFiveDollar);
        fiftyDollar = (Button) findViewById(R.id.fiftyDollar);
        fiveDollar = (Button) findViewById(R.id.fiveDollar);
        oneHundredDollar = (Button) findViewById(R.id.oneHundredDollar);
        sevenFiveDollar = (Button) findViewById(R.id.sevenFiveDollar);

        customAmount = (EditText) findViewById(R.id.customAmount);
        selectAmountDollor= (TextView) findViewById(R.id.selectAmountDollor);

        SAFooter = (RelativeLayout) findViewById(R.id.SAFooter);
        customAmountBorder= (RelativeLayout) findViewById(R.id.customAmountBorder);

        infoText = (SemiBoldTextView) findViewById(R.id.infoText);
        infoText.setText("eGift Card Price Range $" + (int) GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMinimumValue() + " - $" + (int) GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMaximumValue());


        fdParams = fiveDollar.getLayoutParams();
        tdParams = tenDollar.getLayoutParams();
        tfParams = twoFiveDollar.getLayoutParams();
        ffParams = fiftyDollar.getLayoutParams();
        ohdParams = oneHundredDollar.getLayoutParams();
        sfdParams = sevenFiveDollar.getLayoutParams();
        cdParams = customAmount.getLayoutParams();

        tenDollar.setOnClickListener(this);
        twoFiveDollar.setOnClickListener(this);
        fiftyDollar.setOnClickListener(this);
        fiveDollar.setOnClickListener(this);
        oneHundredDollar.setOnClickListener(this);
        sevenFiveDollar.setOnClickListener(this);
        customAmount.setOnClickListener(this);
        SAFooter.setOnClickListener(this);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //if amount already picked or entered
        setAmountPosition();
        initToolbar();

        customAmount.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString();
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                    customAmount.setText(text.substring(0, text.length() - 1));
                    customAmount.setSelection(customAmount.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {

            }
        });

    }

    private void initToolbar() {
        setTitle("Select Amount");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setAmountPosition() {
        if (isFromReload) {
            if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
                String amount = String.valueOf(GiftCardDataManager.getInstance().getGiftCardReload().getAmount());
                if (amount.equalsIgnoreCase("5.0")) {
                    selectedAmount = "5.00";
                    fiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    fiveDollar.setTextColor(Color.WHITE);
                    fiveDollar.setTypeface(null, Typeface.BOLD);
                    fiveDollar.setLayoutParams(fdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (amount.equalsIgnoreCase("10.0")) {
                    selectedAmount = "10.00";
                    tenDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    tenDollar.setTextColor(Color.WHITE);
                    tenDollar.setTypeface(null, Typeface.BOLD);
                    tenDollar.setLayoutParams(tdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (amount.equalsIgnoreCase("25.0")) {
                    selectedAmount = "25.00";
                    twoFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    twoFiveDollar.setTextColor(Color.WHITE);
                    twoFiveDollar.setTypeface(null, Typeface.BOLD);
                    twoFiveDollar.setLayoutParams(tfParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (amount.equalsIgnoreCase("50.0")) {
                    selectedAmount = "50.00";
                    fiftyDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    fiftyDollar.setTextColor(Color.WHITE);
                    fiftyDollar.setTypeface(null, Typeface.BOLD);
                    fiftyDollar.setLayoutParams(ffParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (amount.equalsIgnoreCase("100.0")) {
                    selectedAmount = "100.00";
                    oneHundredDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    oneHundredDollar.setTextColor(Color.WHITE);
                    oneHundredDollar.setTypeface(null, Typeface.BOLD);
                    oneHundredDollar.setLayoutParams(ohdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (amount.equalsIgnoreCase("75.0")) {
                    selectedAmount = "75.00";
                    sevenFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    sevenFiveDollar.setTextColor(Color.WHITE);
                    sevenFiveDollar.setTypeface(null, Typeface.BOLD);
                    sevenFiveDollar.setLayoutParams(sfdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (amount.equalsIgnoreCase("0.0")) {
                    customAmount.setHint("(Or) Enter the custom amount");
                    customAmount.getText().clear();
                } else {
                    customAmount.setText(String.format("%.2f", Double.parseDouble(amount)));
                    customAmountBorder.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_line));
                    selectAmountDollor.setVisibility(View.VISIBLE);
                    customAmount.setTextColor(Color.BLACK);
                }
            }
        } else if (isItFromAutoReload) {
            if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() != null) {
                String autoReloadAmount = String.valueOf(GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getAmount());
                if (autoReloadAmount.equalsIgnoreCase("5.0")) {
                    selectedAmount = "5.00";
                    fiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    fiveDollar.setTextColor(Color.WHITE);
                    fiveDollar.setTypeface(null, Typeface.BOLD);
                    fiveDollar.setLayoutParams(fdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (autoReloadAmount.equalsIgnoreCase("10.0")) {
                    selectedAmount = "10.00";
                    tenDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    tenDollar.setTextColor(Color.WHITE);
                    tenDollar.setTypeface(null, Typeface.BOLD);
                    tenDollar.setLayoutParams(tdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (autoReloadAmount.equalsIgnoreCase("25.0")) {
                    selectedAmount = "25.00";
                    twoFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    twoFiveDollar.setTextColor(Color.WHITE);
                    twoFiveDollar.setTypeface(null, Typeface.BOLD);
                    twoFiveDollar.setLayoutParams(tfParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (autoReloadAmount.equalsIgnoreCase("50.0")) {
                    selectedAmount = "50.00";
                    fiftyDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    fiftyDollar.setTextColor(Color.WHITE);
                    fiftyDollar.setTypeface(null, Typeface.BOLD);
                    fiftyDollar.setLayoutParams(ffParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (autoReloadAmount.equalsIgnoreCase("100.0")) {
                    selectedAmount = "100.00";
                    oneHundredDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    oneHundredDollar.setTextColor(Color.WHITE);
                    oneHundredDollar.setTypeface(null, Typeface.BOLD);
                    oneHundredDollar.setLayoutParams(ohdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (autoReloadAmount.equalsIgnoreCase("75.0")) {
                    selectedAmount = "75.00";
                    sevenFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    sevenFiveDollar.setTextColor(Color.WHITE);
                    sevenFiveDollar.setTypeface(null, Typeface.BOLD);
                    sevenFiveDollar.setLayoutParams(sfdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (autoReloadAmount.equalsIgnoreCase("0.0")) {
                    customAmount.setHint("(Or) Enter the custom amount");
                    customAmount.getText().clear();
                } else {
                    customAmount.setText(String.format("%.2f", Double.parseDouble(autoReloadAmount)));
                    customAmountBorder.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_line));
                    selectAmountDollor.setVisibility(View.VISIBLE);
                    customAmount.setTextColor(Color.BLACK);
                }
            }
        } else {
            if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount().equalsIgnoreCase("5.0")) {
                    selectedAmount = "5.00";
                    fiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    fiveDollar.setTextColor(Color.WHITE);
                    fiveDollar.setTypeface(null, Typeface.BOLD);
                    fiveDollar.setLayoutParams(fdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount().equalsIgnoreCase("10.0")) {
                    selectedAmount = "10.00";
                    tenDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    tenDollar.setTextColor(Color.WHITE);
                    tenDollar.setTypeface(null, Typeface.BOLD);
                    tenDollar.setLayoutParams(tdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount().equalsIgnoreCase("25.0")) {
                    selectedAmount = "25.00";
                    twoFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    twoFiveDollar.setTextColor(Color.WHITE);
                    twoFiveDollar.setTypeface(null, Typeface.BOLD);
                    twoFiveDollar.setLayoutParams(tfParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount().equalsIgnoreCase("50.0")) {
                    selectedAmount = "50.00";
                    fiftyDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    fiftyDollar.setTextColor(Color.WHITE);
                    fiftyDollar.setTypeface(null, Typeface.BOLD);
                    fiftyDollar.setLayoutParams(ffParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount().equalsIgnoreCase("100.0")) {
                    selectedAmount = "100.00";
                    oneHundredDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    oneHundredDollar.setTextColor(Color.WHITE);
                    oneHundredDollar.setTypeface(null, Typeface.BOLD);
                    oneHundredDollar.setLayoutParams(ohdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount().equalsIgnoreCase("75.0")) {
                    selectedAmount = "75.00";
                    sevenFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                    sevenFiveDollar.setTextColor(Color.WHITE);
                    sevenFiveDollar.setTypeface(null, Typeface.BOLD);
                    sevenFiveDollar.setLayoutParams(sfdParams);
                    customAmount.setLayoutParams(cdParams);
                } else if (GiftCardDataManager.getInstance().getGiftCardCreate().getAmount().equalsIgnoreCase("0.0")) {
                    customAmount.setHint("(Or) Enter the custom amount");
                    customAmount.getText().clear();
                } else {
                    customAmount.setText(String.format("%.2f", Double.parseDouble(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount())));
                    customAmountBorder.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_line));
                    selectAmountDollor.setVisibility(View.VISIBLE);
                    customAmount.setTextColor(Color.BLACK);
                }
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {

        View currentFocus = this.getCurrentFocus();

        switch (view.getId()) {

            case R.id.fiveDollar:
                selectedAmount = "5.00";
                fiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                fiveDollar.setTextColor(Color.WHITE);
                fiveDollar.setTypeface(null, Typeface.BOLD);
                fiveDollar.setLayoutParams(fdParams);

                tenDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                tenDollar.setTypeface(null, Typeface.NORMAL);
                tenDollar.setTextColor(Color.BLACK);
                tenDollar.setLayoutParams(tdParams);

                twoFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                twoFiveDollar.setTypeface(null, Typeface.NORMAL);
                twoFiveDollar.setTextColor(Color.BLACK);
                twoFiveDollar.setLayoutParams(tfParams);

                fiftyDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiftyDollar.setTypeface(null, Typeface.NORMAL);
                fiftyDollar.setTextColor(Color.BLACK);
                fiftyDollar.setLayoutParams(ffParams);

                oneHundredDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                oneHundredDollar.setTypeface(null, Typeface.NORMAL);
                oneHundredDollar.setTextColor(Color.BLACK);
                oneHundredDollar.setLayoutParams(ohdParams);

                sevenFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                sevenFiveDollar.setTypeface(null, Typeface.NORMAL);
                sevenFiveDollar.setTextColor(Color.BLACK);
                sevenFiveDollar.setLayoutParams(sfdParams);

                customAmount.setHint("(Or) Enter the custom amount");
                customAmount.getText().clear();
                customAmountBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                selectAmountDollor.setVisibility(View.INVISIBLE);
                customAmount.setCursorVisible(false);


                if (currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                break;

            case R.id.tenDollar:
                selectedAmount = "10.00";
                fiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiveDollar.setTextColor(Color.BLACK);
                fiveDollar.setTypeface(null, Typeface.NORMAL);
                fiveDollar.setLayoutParams(fdParams);

                tenDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                tenDollar.setTextColor(Color.WHITE);
                tenDollar.setTypeface(null, Typeface.BOLD);
                tenDollar.setLayoutParams(tdParams);

                twoFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                twoFiveDollar.setTypeface(null, Typeface.NORMAL);
                twoFiveDollar.setTextColor(Color.BLACK);
                twoFiveDollar.setLayoutParams(tfParams);

                fiftyDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiftyDollar.setTypeface(null, Typeface.NORMAL);
                fiftyDollar.setTextColor(Color.BLACK);
                fiftyDollar.setLayoutParams(ffParams);

                oneHundredDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                oneHundredDollar.setTypeface(null, Typeface.NORMAL);
                oneHundredDollar.setTextColor(Color.BLACK);
                oneHundredDollar.setLayoutParams(ohdParams);

                sevenFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                sevenFiveDollar.setTypeface(null, Typeface.NORMAL);
                sevenFiveDollar.setTextColor(Color.BLACK);
                sevenFiveDollar.setLayoutParams(sfdParams);

                customAmount.setHint("(Or) Enter the custom amount");
                customAmount.getText().clear();
                customAmountBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                selectAmountDollor.setVisibility(View.INVISIBLE);
                customAmount.setCursorVisible(false);


                if (currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                break;

            case R.id.twoFiveDollar:
                selectedAmount = "25.00";
                fiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiveDollar.setTextColor(Color.BLACK);
                fiveDollar.setTypeface(null, Typeface.NORMAL);
                fiveDollar.setLayoutParams(fdParams);

                tenDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                tenDollar.setTypeface(null, Typeface.NORMAL);
                tenDollar.setTextColor(Color.BLACK);
                tenDollar.setLayoutParams(tdParams);

                twoFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                twoFiveDollar.setTextColor(Color.WHITE);
                twoFiveDollar.setTypeface(null, Typeface.BOLD);
                twoFiveDollar.setLayoutParams(tfParams);

                fiftyDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiftyDollar.setTypeface(null, Typeface.NORMAL);
                fiftyDollar.setTextColor(Color.BLACK);
                fiftyDollar.setLayoutParams(ffParams);

                oneHundredDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                oneHundredDollar.setTypeface(null, Typeface.NORMAL);
                oneHundredDollar.setTextColor(Color.BLACK);
                oneHundredDollar.setLayoutParams(ohdParams);

                sevenFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                sevenFiveDollar.setTypeface(null, Typeface.NORMAL);
                sevenFiveDollar.setTextColor(Color.BLACK);
                sevenFiveDollar.setLayoutParams(sfdParams);

                customAmount.setHint("(Or) Enter the custom amount");
                customAmount.getText().clear();
                customAmountBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                selectAmountDollor.setVisibility(View.INVISIBLE);
                customAmount.setCursorVisible(false);

                if (currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;

            case R.id.fiftyDollar:
                selectedAmount = "50.00";
                fiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiveDollar.setTextColor(Color.BLACK);
                fiveDollar.setTypeface(null, Typeface.NORMAL);
                fiveDollar.setLayoutParams(fdParams);

                tenDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                tenDollar.setTypeface(null, Typeface.NORMAL);
                tenDollar.setTextColor(Color.BLACK);
                tenDollar.setLayoutParams(tdParams);

                twoFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                twoFiveDollar.setTypeface(null, Typeface.NORMAL);
                twoFiveDollar.setTextColor(Color.BLACK);
                twoFiveDollar.setLayoutParams(tfParams);

                fiftyDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                fiftyDollar.setTextColor(Color.WHITE);
                fiftyDollar.setTypeface(null, Typeface.BOLD);
                fiftyDollar.setLayoutParams(ffParams);

                oneHundredDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                oneHundredDollar.setTypeface(null, Typeface.NORMAL);
                oneHundredDollar.setTextColor(Color.BLACK);
                oneHundredDollar.setLayoutParams(ohdParams);

                sevenFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                sevenFiveDollar.setTypeface(null, Typeface.NORMAL);
                sevenFiveDollar.setTextColor(Color.BLACK);
                sevenFiveDollar.setLayoutParams(sfdParams);

                customAmount.setHint("(Or) Enter the custom amount");
                customAmount.getText().clear();
                customAmountBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                selectAmountDollor.setVisibility(View.INVISIBLE);
                customAmount.setCursorVisible(false);

                if (currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
            case R.id.oneHundredDollar:
                selectedAmount = "100.00";
                fiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiveDollar.setTextColor(Color.BLACK);
                fiveDollar.setTypeface(null, Typeface.NORMAL);
                fiveDollar.setLayoutParams(fdParams);

                tenDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                tenDollar.setTypeface(null, Typeface.NORMAL);
                tenDollar.setTextColor(Color.BLACK);
                tenDollar.setLayoutParams(tdParams);

                twoFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                twoFiveDollar.setTypeface(null, Typeface.NORMAL);
                twoFiveDollar.setTextColor(Color.BLACK);
                twoFiveDollar.setLayoutParams(tfParams);

                fiftyDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiftyDollar.setTypeface(null, Typeface.NORMAL);
                fiftyDollar.setTextColor(Color.BLACK);
                fiftyDollar.setLayoutParams(ffParams);

                oneHundredDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                oneHundredDollar.setTextColor(Color.WHITE);
                oneHundredDollar.setTypeface(null, Typeface.BOLD);
                oneHundredDollar.setLayoutParams(ohdParams);

                sevenFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                sevenFiveDollar.setTypeface(null, Typeface.NORMAL);
                sevenFiveDollar.setTextColor(Color.BLACK);
                sevenFiveDollar.setLayoutParams(sfdParams);

                customAmount.setHint("(Or) Enter the custom amount");
                customAmount.getText().clear();
                customAmountBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                selectAmountDollor.setVisibility(View.INVISIBLE);
                customAmount.setCursorVisible(false);

                if (currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;

            case R.id.sevenFiveDollar:
                selectedAmount = "75.00";
                fiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiveDollar.setTextColor(Color.BLACK);
                fiveDollar.setTypeface(null, Typeface.NORMAL);
                fiveDollar.setLayoutParams(fdParams);

                tenDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                tenDollar.setTypeface(null, Typeface.NORMAL);
                tenDollar.setTextColor(Color.BLACK);
                tenDollar.setLayoutParams(tdParams);

                twoFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                twoFiveDollar.setTypeface(null, Typeface.NORMAL);
                twoFiveDollar.setTextColor(Color.BLACK);
                twoFiveDollar.setLayoutParams(tfParams);

                fiftyDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiftyDollar.setTypeface(null, Typeface.NORMAL);
                fiftyDollar.setTextColor(Color.BLACK);
                fiftyDollar.setLayoutParams(ffParams);

                oneHundredDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                oneHundredDollar.setTypeface(null, Typeface.NORMAL);
                oneHundredDollar.setTextColor(Color.BLACK);
                oneHundredDollar.setLayoutParams(ohdParams);

                sevenFiveDollar.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_margin_gray_orange));
                sevenFiveDollar.setTextColor(Color.WHITE);
                sevenFiveDollar.setTypeface(null, Typeface.BOLD);
                sevenFiveDollar.setLayoutParams(sfdParams);

                customAmount.setHint("(Or) Enter the custom amount");
                customAmount.getText().clear();
                customAmountBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                selectAmountDollor.setVisibility(View.INVISIBLE);
                customAmount.setCursorVisible(false);

                if (currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;


            case R.id.customAmount:
                selectedAmount = "0.00";
                customAmount.setLayoutParams(cdParams);
                customAmount.setFocusableInTouchMode(true);
                customAmount.requestFocus();
                customAmountBorder.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_line));
                selectAmountDollor.setVisibility(View.VISIBLE);
                customAmount.setTextColor(Color.BLACK);
                customAmount.setCursorVisible(true);

                fiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiveDollar.setTextColor(Color.BLACK);
                fiveDollar.setTypeface(null, Typeface.NORMAL);
                fiveDollar.setLayoutParams(fdParams);

                tenDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                tenDollar.setTypeface(null, Typeface.NORMAL);
                tenDollar.setTextColor(Color.BLACK);
                tenDollar.setLayoutParams(tdParams);

                twoFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                twoFiveDollar.setTypeface(null, Typeface.NORMAL);
                twoFiveDollar.setTextColor(Color.BLACK);
                twoFiveDollar.setLayoutParams(tfParams);

                fiftyDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                fiftyDollar.setTypeface(null, Typeface.NORMAL);
                fiftyDollar.setTextColor(Color.BLACK);
                fiftyDollar.setLayoutParams(ffParams);

                oneHundredDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                oneHundredDollar.setTypeface(null, Typeface.NORMAL);
                oneHundredDollar.setTextColor(Color.BLACK);
                oneHundredDollar.setLayoutParams(ohdParams);

                sevenFiveDollar.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_margin));
                sevenFiveDollar.setTypeface(null, Typeface.NORMAL);
                sevenFiveDollar.setTextColor(Color.BLACK);
                sevenFiveDollar.setLayoutParams(sfdParams);

                break;

            case R.id.SAFooter:
                float tAmount;
                String amount = null;

                if (customAmount.getText().toString().equalsIgnoreCase("")) {
                    if (selectedAmount.equalsIgnoreCase("0.00")) {
                        amount = "";
                    } else {
                        amount = selectedAmount;

                    }
                } else {
                    amount = customAmount.getText().toString();
                }

                if (amount.equalsIgnoreCase("")) {
                    Utils.alert(context, "Alert", "Please enter valid amount");
                } else {
                    tAmount = Float.parseFloat(amount);

                    if (tAmount > GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMaximumValue() || tAmount < GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMinimumValue()) {
                        Utils.alert(context, "Alert", "Please enter an amount between $" + String.valueOf((int) GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMinimumValue()) + " - $" + String.valueOf((int) GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMaximumValue()));
                    } else {
                        //setting values based on bundle
                        if (isFromReload) {
                            if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
                                GiftCardDataManager.getInstance().getGiftCardReload().setAmount(Double.parseDouble(amount));
                                onBackPressed();
                            } else {
                                GiftCardReload giftCardReload = new GiftCardReload();
                                giftCardReload.setAmount(Double.parseDouble(amount));
                                GiftCardDataManager.getInstance().setGiftCardReload(giftCardReload);
                                onBackPressed();
                            }
                        } else if (isItFromAutoReload) {
                            if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() != null) {
                                GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setAmount(Double.parseDouble(amount));
                                onBackPressed();
                            }
                        } else {
                            if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
                                GiftCardDataManager.getInstance().getGiftCardCreate().setAmount(Double.parseDouble(amount));
                                onBackPressed();
                            } else {
                                GiftCardCreate giftCardCreate = new GiftCardCreate();
                                giftCardCreate.setAmount(Double.parseDouble(amount));
                                GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);
                                onBackPressed();
                            }
                        }
                    }
                }
                break;
        }
    }
}
