package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.SelectAmountActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreditCard.ExistingSavedCreditCard;
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
import com.wearehathway.apps.incomm.Interfaces.InCommCardAutoReloadServiceCallBack;
import com.wearehathway.apps.incomm.Interfaces.InCommUserPaymentAccountCallback;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSavable;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSubmitOrder;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.incomm.Services.InCommUserPaymentAccountService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

/*
    Created By jeeva
 */
public class AutoReloadActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RelativeLayout balanceBelowLayout, selectReloadAmountLayout, onSpecificLayout, startDateLayout,
            paymentAccountLayout, ARFooter, endDateLayout;
    private Spinner OSSpinner;
    private SemiBoldTextView startDate, endDate, givenCardNumber, onSpecificText, balanceBelowText, selectAmount;
    private TextView dollarLable;
    private ImageView creditCardImg;
    private SwitchCompat setAutoReload;
    private ScrollView ARScrollView;
    private LinearLayout bottomLayout;
    private EditText belowBalanceAmount;
    private InCommAutoReloadSubmitOrder inCommAutoReloadSubmitOrder;
    private ImageButton selectBtnOnSpecific, selectBtnBalanceBelow, autoReloadDelete;
    private String ReloadFreqId = "None";
    private int year, month, day, cardId;
    private String[] onSpecific;
    private String[] reloadFreqIds;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDateForSOD(arg1, arg2 + 1, arg3);
        }
    };

    private Context context;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reload);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true);
        setBackButton(true,false);

        getBundle();
        onCheckedChangeListener = this;

        onSpecific = new String[]{"End Of Week", "End Of Month", "End Of Year"};
        reloadFreqIds = new String[]{"Weekly", "Monthly", "yearly"};

        balanceBelowLayout = (RelativeLayout) findViewById(R.id.balanceBelowLayout);
        selectReloadAmountLayout = (RelativeLayout) findViewById(R.id.selectReloadAmountLayout);
        onSpecificLayout = (RelativeLayout) findViewById(R.id.onSpecificLayout);
        startDateLayout = (RelativeLayout) findViewById(R.id.startDateLayout);
        paymentAccountLayout = (RelativeLayout) findViewById(R.id.paymentAccountLayout);
        ARFooter = (RelativeLayout) findViewById(R.id.ARFooter);
        endDateLayout = (RelativeLayout) findViewById(R.id.endDateLayout);

        selectAmount = (SemiBoldTextView) findViewById(R.id.selectAmount);

        OSSpinner = (Spinner) findViewById(R.id.OSSpinner);
        OSSpinner.setPrompt("On a specific");
        setOnSpecificSpinner();
        OSSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ReloadFreqId = "Weekly";
                } else if (i == 1) {
                    ReloadFreqId = "Monthly";
                } else if (i == 2) {
                    ReloadFreqId = "Yearly";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startDate = (SemiBoldTextView) findViewById(R.id.startDate);
        endDate = (SemiBoldTextView) findViewById(R.id.endDate);
        givenCardNumber = (SemiBoldTextView) findViewById(R.id.givenCardNumber);
        balanceBelowText = (SemiBoldTextView) findViewById(R.id.balanceBelowText);
        onSpecificText = (SemiBoldTextView) findViewById(R.id.onSpecificText);
        dollarLable= (TextView) findViewById(R.id.dollarLable);

        belowBalanceAmount = (EditText) findViewById(R.id.belowBalanceAmount);

        creditCardImg = (ImageView) findViewById(R.id.creditCardImg);

        setAutoReload = (SwitchCompat) findViewById(R.id.setAutoReload);

        ARScrollView = (ScrollView) findViewById(R.id.ARScrollView);

        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);

        selectBtnBalanceBelow = (ImageButton) findViewById(R.id.selectBtnBalanceBelow);
        selectBtnOnSpecific = (ImageButton) findViewById(R.id.selectBtnOnSpecific);
        autoReloadDelete = (ImageButton) findViewById(R.id.autoReloadDelete);

        if (GiftCardDataManager.getInstance().getAutoReloadSavable() == null) {
            if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() == null) {
                inCommAutoReloadSubmitOrder = new InCommAutoReloadSubmitOrder();
                GiftCardDataManager.getInstance().setInCommAutoReloadSubmitOrder(inCommAutoReloadSubmitOrder);
                GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setAmount(5.00);

                final Calendar c = Calendar.getInstance();
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH) + 1;
                int currentDate = c.get(Calendar.DAY_OF_MONTH);
                showDateForSOD(currentYear,currentMonth,currentDate);
                setData();
            }
        }
        //Setting values if this gift card has auto reload config
        if (GiftCardDataManager.getInstance().getAutoReloadSavable() != null) {
            if (GiftCardDataManager.getInstance().getAutoReloadSavable().getActive()) {
                setAmount();
                setBelowBalOrOnSpecific();
                setStartDate();
                setEndDate();
                setDisableActionForActive();
                setPaymentAccount();
            } else {
                setAmount();
                setBelowBalOrOnSpecific();
                setStartDate();
                setEndDate();
                setDisableActionForInActive();
                setPaymentAccount();
            }
        } else {
            setEnableAction();

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

        }


        selectReloadAmountLayout.setOnClickListener(this);
        onSpecificLayout.setOnClickListener(this);
        balanceBelowLayout.setOnClickListener(this);
        startDateLayout.setOnClickListener(this);
        endDateLayout.setOnClickListener(this);
        paymentAccountLayout.setOnClickListener(this);
        ARFooter.setOnClickListener(this);
        autoReloadDelete.setOnClickListener(this);
        setAutoReload.setOnCheckedChangeListener(onCheckedChangeListener);

        initToolbar();

    }

    //Active and Inactive Auto Reload service
    private void updateStatus(final boolean isChecked) {
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        int autoReloadId = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId();
        InCommCardService.updateAutoReloadState(userId, cardId, autoReloadId, isChecked, new InCommCardAutoReloadServiceCallBack() {
            @Override
            public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception) {
                enableScreen(true);
                if (inCommAutoReloadSavable == null && exception == null) {
                    setAutoReload.setChecked(isChecked);
                    GiftCardDataManager.getInstance().getAutoReloadSavable().setActive(isChecked);
                    if (isChecked) {
                        setDisableActionForActive();
                    } else {
                        setDisableActionForInActive();
                    }
                }else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage) ) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                if(isChecked){
                                    loaderText("Activating...");
                                }else {
                                    loaderText("Deactivating...");
                                }
                                updateStatus(isChecked);
                            }
                        }
                    });
                }
                else {
                    setAutoReload.setOnCheckedChangeListener(null);
                    setAutoReload.setChecked(!isChecked);
                    setAutoReload.setOnCheckedChangeListener(onCheckedChangeListener);
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        Utils.alert(context, "Failure", "Unexpected error occured while updating status");
                    }
                }
            }
        });
    }

    private void setAmount() {
        if (GiftCardDataManager.getInstance().getAutoReloadSavable().getAmount() != 0.00) {
            selectAmount.setText(String.format("%.2f", GiftCardDataManager.getInstance().getAutoReloadSavable().getAmount()));
        }
    }

    private void setBelowBalOrOnSpecific() {
        if (!GiftCardDataManager.getInstance().getAutoReloadSavable().getReloadFrequencyId().equalsIgnoreCase("None")) {
            selectBtnOnSpecific.setImageResource(R.drawable.select_green);
            onSpecificText.setTextColor(Color.BLACK);
            OSSpinner.setVisibility(View.VISIBLE);
            for (int i = 0; i < onSpecific.length; i++) {
                if (reloadFreqIds[i].equalsIgnoreCase(GiftCardDataManager.getInstance().getAutoReloadSavable().getReloadFrequencyId())) {
                    OSSpinner.setSelection(i);
                }
            }
        } else {
            selectBtnBalanceBelow.setImageResource(R.drawable.select_green);
            balanceBelowText.setTextColor(Color.BLACK);
            dollarLable.setVisibility(View.VISIBLE);
            dollarLable.setTextColor(Color.LTGRAY);
            belowBalanceAmount.setText(String.valueOf(GiftCardDataManager.getInstance().getAutoReloadSavable().getMinimumBalance()));
        }
    }

    private void setStartDate() {
        startDate.setVisibility(View.VISIBLE);
        startDate.setText(convertDateToString(GiftCardDataManager.getInstance().getAutoReloadSavable().getStartsOn()));
    }

    private void setEndDate() {
        if (GiftCardDataManager.getInstance().getAutoReloadSavable().getEndsOn() != null) {
            endDate.setVisibility(View.VISIBLE);
            endDate.setText(convertDateToString(GiftCardDataManager.getInstance().getAutoReloadSavable().getEndsOn()));
        } else if (GiftCardDataManager.getInstance().getAutoReloadSavable().getNumberOfOccurancesRemaining() > 0) {
            endDate.setVisibility(View.VISIBLE);
            endDate.setText("After " + String.valueOf(GiftCardDataManager.getInstance().getAutoReloadSavable().getNumberOfOccurancesRemaining()) + " Occurrences");
        } else {
            endDate.setVisibility(View.VISIBLE);
            endDate.setText("Never");
        }
    }


    private void setPaymentAccount() {
        enableScreen(false);
        loaderText("Retrieving auto reload details...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommUserPaymentAccountService.getAllUserPaymentAccount(userId, new InCommUserPaymentAccountCallback() {
            @Override
            public void onPaymentAccountListServiceCallback(ArrayList<InCommUserPaymentAccount> accountList, Exception exception) {
                enableScreen(true);
                String cardNumber = null, cardType = null;
                if (exception == null) {
                    if (accountList != null) {
                        if (accountList.size() > 0) {
                            for (int i = 0; i < accountList.size(); i++) {
                                if (accountList.get(i).getId() == GiftCardDataManager.getInstance().getAutoReloadSavable().getPaymentAccountId()) {
                                    cardNumber = accountList.get(i).getCreditCardNumber();
                                    cardType = accountList.get(i).getCreditCardTypeCode();
                                }
                            }

                            setCreditCardInfo(cardNumber, cardType);
                        }
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
                                setPaymentAccount();
                            }
                        }
                    });
                }else {
                    alertWithTryAgain(context, "Failure", "Failed to fetch your gift card information. Please try again.");
                }
            }
        });
    }

    private void alertWithTryAgain(final Context context, String Title, final String Message) {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableScreen(false);
                setPaymentAccount();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void setCreditCardInfo(String cardNumber, String cardType) {
        if (cardNumber != null) {
            creditCardImg.setVisibility(View.VISIBLE);
            givenCardNumber.setVisibility(View.VISIBLE);
            String strLastFourDi = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : "";
            givenCardNumber.setText(strLastFourDi);
            for (int i = 0; i < GiftCardDataManager.getInstance().getCreditCardTypes().size(); i++) {
                if (cardType.equalsIgnoreCase(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getCreditCardType())) {
                    Picasso.with(context)
                            .load(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getThumbnailImageUrl())
                            .placeholder(R.drawable.product_placeholder)
                            .error(R.drawable.product_placeholder)
                            .into(creditCardImg);
                }

            }

        } else {
            creditCardImg.setVisibility(View.GONE);
            givenCardNumber.setVisibility(View.GONE);
        }
    }

    private void setDisableActionForActive() {
        ARScrollView.setBackgroundColor(Color.WHITE);
        ARScrollView.setAlpha((float) 1.0);
        bottomLayout.setBackgroundColor(Color.WHITE);
        bottomLayout.setAlpha((float) 0.5);
        autoReloadDelete.setVisibility(View.VISIBLE);
        setAutoReload.setChecked(true);
        selectReloadAmountLayout.setEnabled(false);
        selectReloadAmountLayout.setClickable(false);
        OSSpinner.setEnabled(false);
        OSSpinner.setClickable(false);
        balanceBelowLayout.setEnabled(false);
        balanceBelowLayout.setClickable(false);
        belowBalanceAmount.setEnabled(false);
        belowBalanceAmount.setClickable(false);
        onSpecificLayout.setEnabled(false);
        onSpecificLayout.setClickable(false);
        startDateLayout.setEnabled(false);
        startDateLayout.setClickable(false);
        endDateLayout.setEnabled(false);
        endDateLayout.setClickable(false);
        paymentAccountLayout.setEnabled(false);
        paymentAccountLayout.setClickable(false);
        ARFooter.setEnabled(false);
        ARFooter.setClickable(false);
    }

    private void setDisableActionForInActive() {
        ARScrollView.setBackgroundColor(Color.WHITE);
        ARScrollView.setAlpha((float) 0.5);
        bottomLayout.setBackgroundColor(Color.WHITE);
        bottomLayout.setAlpha((float) 0.5);
        autoReloadDelete.setVisibility(View.VISIBLE);
        setAutoReload.setChecked(false);
        selectReloadAmountLayout.setEnabled(false);
        selectReloadAmountLayout.setClickable(false);
        OSSpinner.setEnabled(false);
        OSSpinner.setClickable(false);
        balanceBelowLayout.setEnabled(false);
        balanceBelowLayout.setClickable(false);
        belowBalanceAmount.setEnabled(false);
        belowBalanceAmount.setClickable(false);
        onSpecificLayout.setEnabled(false);
        onSpecificLayout.setClickable(false);
        startDateLayout.setEnabled(false);
        startDateLayout.setClickable(false);
        endDateLayout.setEnabled(false);
        endDateLayout.setClickable(false);
        paymentAccountLayout.setEnabled(false);
        paymentAccountLayout.setClickable(false);
        ARFooter.setEnabled(false);
        ARFooter.setClickable(false);
    }

    private void setEnableAction() {
        ARScrollView.setBackgroundColor(Color.WHITE);
        ARScrollView.setAlpha((float) 0.5);
        bottomLayout.setBackgroundColor(Color.WHITE);
        bottomLayout.setAlpha((float) 0.5);
        autoReloadDelete.setVisibility(View.GONE);
        setAutoReload.setChecked(false);
        selectReloadAmountLayout.setEnabled(false);
        selectReloadAmountLayout.setClickable(false);
        OSSpinner.setEnabled(false);
        OSSpinner.setClickable(false);
        balanceBelowLayout.setEnabled(false);
        balanceBelowLayout.setClickable(false);
        belowBalanceAmount.setEnabled(false);
        belowBalanceAmount.setClickable(false);
        dollarLable.setVisibility(View.INVISIBLE);
        onSpecificLayout.setEnabled(false);
        onSpecificLayout.setClickable(false);
        startDateLayout.setEnabled(false);
        startDateLayout.setClickable(false);
        endDateLayout.setEnabled(false);
        endDateLayout.setClickable(false);
        paymentAccountLayout.setEnabled(false);
        paymentAccountLayout.setClickable(false);
        ARFooter.setEnabled(false);
        ARFooter.setClickable(false);
    }


    private void initToolbar() {
        setTitle("Auto Reload");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
    }

    private void getBundle() {
        cardId = getIntent().getIntExtra("cardId", 0);
    }


    private void setOnSpecificSpinner() {
        ArrayAdapter<String> onSpecAdapter = new ArrayAdapter<String>(this, R.layout.auto_reload_spinner_text, onSpecific);
        OSSpinner.setAdapter(onSpecAdapter);
    }

    private void showDateForSOD(int year, int month, int day) {
        startDate.setVisibility(View.VISIBLE);
        if (String.valueOf(month).length() == 1 && String.valueOf(day).length() == 1) {
            startDate.setText(new StringBuilder().append("0" + month).append("/")
                    .append("0" + day).append("/").append(year));
        } else if (String.valueOf(month).length() == 1) {
            startDate.setText(new StringBuilder().append("0" + month).append("/")
                    .append(day).append("/").append(year));
        } else if (String.valueOf(day).length() == 1) {
            startDate.setText(new StringBuilder().append(month).append("/")
                    .append("0" + day).append("/").append(year));
        } else {
            startDate.setText(new StringBuilder().append(month).append("/")
                    .append(day).append("/").append(year));
        }
    }

    @SuppressWarnings("deprecation")
    public void setDateAndTime(View view) {
        showDialog(view.getId());
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.startDateLayout) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GiftCardDataManager.getInstance().getAutoReloadSavable() == null) {
            if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() == null) {
                inCommAutoReloadSubmitOrder = new InCommAutoReloadSubmitOrder();
                GiftCardDataManager.getInstance().setInCommAutoReloadSubmitOrder(inCommAutoReloadSubmitOrder);
            } else {
                setData();
            }
        }
    }

    private void setData() {
        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getAmount() != 0.00) {
            selectAmount.setText(String.format("%.2f", GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getAmount()));
        } else {
            selectAmount.setText("0.00");
        }

        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getEndsOn() != null) {
            endDate.setVisibility(View.VISIBLE);
            endDate.setText(convertDateToString(GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getEndsOn()));
        } else if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getNumberOfOccurancesRemaining() > 0) {
            endDate.setVisibility(View.VISIBLE);
            endDate.setText("After " + String.valueOf(GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getNumberOfOccurancesRemaining()) + " Occurrences");
        } else {
            endDate.setVisibility(View.VISIBLE);
            endDate.setText("Never");
        }

        if (GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo() != null) {
            String cardNumber = GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().getCreditCardNumber();
            if (cardNumber != null) {
                creditCardImg.setVisibility(View.VISIBLE);
                givenCardNumber.setVisibility(View.VISIBLE);
                String strLastFourDi = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : "";
                givenCardNumber.setText("..." + strLastFourDi);
                for (int i = 0; i < GiftCardDataManager.getInstance().getCreditCardTypes().size(); i++) {
                    if (GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().getCreditCardType().equalsIgnoreCase(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getCreditCardType())) {
                        Picasso.with(context)
                                .load(GiftCardDataManager.getInstance().getCreditCardTypes().get(i).getThumbnailImageUrl())
                                .placeholder(R.drawable.product_placeholder)
                                .error(R.drawable.product_placeholder)
                                .into(creditCardImg);
                    }

                }

            } else {
                creditCardImg.setVisibility(View.GONE);
                givenCardNumber.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectReloadAmountLayout:
                Bundle amountBundle = new Bundle();
                amountBundle.putBoolean("FromAutoReload", true);
                TransitionManager.transitFrom(AutoReloadActivityGiftCard.this, SelectAmountActivityGiftCard.class, amountBundle);
                break;

            case R.id.onSpecificLayout:
                OSSpinner.performClick();

                selectBtnOnSpecific.setImageResource(R.drawable.select_green);
                onSpecificText.setTextColor(Color.BLACK);
                OSSpinner.setVisibility(View.VISIBLE);

                selectBtnBalanceBelow.setImageResource(R.drawable.select_white);
                balanceBelowText.setTextColor(Color.LTGRAY);
                belowBalanceAmount.setEnabled(false);
                belowBalanceAmount.setClickable(false);
                belowBalanceAmount.getText().clear();
                dollarLable.setVisibility(View.INVISIBLE);

                break;

            case R.id.balanceBelowLayout:
                selectBtnBalanceBelow.setImageResource(R.drawable.select_green);
                balanceBelowText.setTextColor(Color.BLACK);
                belowBalanceAmount.setEnabled(true);
                belowBalanceAmount.setClickable(true);
                dollarLable.setTextColor(Color.BLACK);
                dollarLable.setVisibility(View.VISIBLE);

                selectBtnOnSpecific.setImageResource(R.drawable.select_white);
                onSpecificText.setTextColor(Color.LTGRAY);
                OSSpinner.setVisibility(View.GONE);
                ReloadFreqId = "None";
                break;

            case R.id.startDateLayout:
                setDateAndTime(view);
                break;

            case R.id.endDateLayout:
                TransitionManager.transitFrom(AutoReloadActivityGiftCard.this, AutoReloadEndDateActivityGiftCard.class);
                break;

            case R.id.paymentAccountLayout:
                Bundle bundle = new Bundle();
                bundle.putBoolean("FromAutoReload", true);
                TransitionManager.transitFrom(AutoReloadActivityGiftCard.this, ExistingSavedCreditCard.class, bundle);
                break;

            case R.id.ARFooter:
                setAutoReloadValuesAndService();
                break;

            case R.id.autoReloadDelete:
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Warning");
                alertDialogBuilder.setMessage("Are you sure you want to remove this auto reload payment?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        enableScreen(false);
                        loaderText("Deleting...");
                        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_AUTORELOAD_REMOVED);//tracking successful auto_reload removed
                        deleteAutoReloadService();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;


        }
    }

    private String convertDateToString(String date) {
        SimpleDateFormat utcFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = null;
        try {
            Date parsedDate = utcFormat.parse(date);
            formattedDate = df.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    private String convertStringToDate(String startDateString) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat utcFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = null;
        String utcStartDate="";
        try {
            startDate = df.parse(startDateString);
            utcStartDate= utcFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcStartDate;
    }

    private void setAutoReloadValues() {
        InCommAutoReloadSubmitOrder localInCommAutoReloadSubmitOrder = GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder();
        localInCommAutoReloadSubmitOrder.setGiftCardId(cardId);
        if (!belowBalanceAmount.getText().toString().equalsIgnoreCase("")) {
            localInCommAutoReloadSubmitOrder.setMinimumBalance(Double.parseDouble(belowBalanceAmount.getText().toString()));
        }
        localInCommAutoReloadSubmitOrder.setStartsOn(convertStringToDate(startDate.getText().toString()+ Constants.UTC_TIME_1AM));
        localInCommAutoReloadSubmitOrder.setReloadFrequencyId(ReloadFreqId);
        GiftCardDataManager.getInstance().setInCommAutoReloadSubmitOrder(localInCommAutoReloadSubmitOrder);
    }

    private void setService() {
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommAutoReloadSubmitOrder inCommAutoReloadSubmitOrder = GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder();
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_AUTORELOAD_CREATED);//tracking successful auto_reload creating
        InCommCardService.autoReload(userId, String.valueOf(cardId), inCommAutoReloadSubmitOrder, new InCommCardAutoReloadServiceCallBack() {
            @Override
            public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception) {
                enableScreen(true);
                if (inCommAutoReloadSavable != null && exception == null) {
                    enableScreen(false);
                    GiftCardDataManager.getInstance().setAutoReloadSavable(inCommAutoReloadSavable);
                    GiftCardDataManager.getInstance().setInCommAutoReloadSubmitOrder(null);
                    GiftCardDataManager.getInstance().setAutoReloadLocalPaymentInfo(null);
                    refreshGiftCardListActivity();
                    Log.d("","refreshing gift card");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enableScreen(true);
                            alert(context,"Success","Successfully configured auto reload");
                        }
                    }, 5000);
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
                                setService();
                            }
                        }
                    });
                }
                else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        Utils.alert(context, "Failure", Utils.responseErrorNull());
                    }
                } else {
                    Utils.alert(context, "Error", Utils.responseErrorNull());
                }
            }
        });
    }

    private boolean isValidStartAndEndDate(String date) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDate = c.get(Calendar.DAY_OF_MONTH);

        StringTokenizer tokens = new StringTokenizer(date, "/");
        int monthInt = Integer.parseInt(tokens.nextToken());
        int dateInt = Integer.parseInt(tokens.nextToken());
        int yearInt = Integer.parseInt(tokens.nextToken());

        if (yearInt > currentYear) {
            return true;
        } else if (yearInt == currentYear) {
            if (monthInt > currentMonth) {
                return true;
            } else if (monthInt == currentMonth) {
                if (dateInt >= currentDate) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void setAutoReloadValuesAndService() {
        if (selectAmount.getText().toString().equalsIgnoreCase("0.00")) {
            Utils.alert(context, "Auto Reload", "Please enter an auto reload amount.");
            return;
        }

        if (belowBalanceAmount.getText().toString().equalsIgnoreCase("")) {
            if (OSSpinner.getVisibility() == View.GONE) {
                Utils.alert(context, "Auto Reload", "Please enter a minimum balance or select a reload interval.");
                return;
            }
        } else {
            double selectedAmount = Double.parseDouble(selectAmount.getText().toString());
            double minBal = Double.parseDouble(belowBalanceAmount.getText().toString());
            double totalAmount = selectedAmount + minBal;

            if (totalAmount > GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMaximumValue()) {
                // Utils.alert(context, "Auto Reload", "You can enter the amount maximum of $" + String.valueOf((int) (GiftCardDataManager.getInstance().getBrands().get(0).getVariableAmountDenominationMaximumValue() - selectedAmount)));
                Utils.alert(context, "Auto Reload", "Below reload balance greater than maximum amount.");
                return;
            }

            if (Double.compare(minBal,0.0) <=0){
                Utils.alert(context, "Auto Reload", "Minimum balance should be greater than $0");
                return;
            }
        }

        if (startDate.getVisibility() == View.GONE) {
            Utils.alert(context, "Start Date", "Please select start date");
            return;
        }

        if (!isValidStartAndEndDate(startDate.getText().toString())) {
            Utils.alert(context, "Start Date", "Please select a valid start date");
            return;
        }

        if (endDate.getVisibility() == View.GONE) {
            Utils.alert(context, "End Date", "Please select your end date");
            return;
        }

        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getEndsOn() != null) {
            if (!isValidStartAndEndDate(endDate.getText().toString())) {
                Utils.alert(context, "End Date", "Please select a valid end date");
                return;
            }
        }

        if (creditCardImg.getVisibility() == View.GONE || givenCardNumber.getVisibility() == View.GONE) {
            Utils.alert(context, "Payment", "Please select enter payment details");
            ARScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            return;
        }

        enableScreen(false);
        loaderText("Saving auto reload...");
        setAutoReloadValues();
        setService();

    }

    private void alert(Context context,String Title,String Message){
        if(!((Activity) context).isFinishing()) {
            if (Message == null) {
                Message = "Error";
            }
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(Title);
            alertDialogBuilder.setMessage(Message);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private void deleteAutoReloadService() {
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        int autoReloadId = GiftCardDataManager.getInstance().getUserAllCards().get(cardId).getAutoReloadId();
        InCommCardService.deleteAutoReload(userId, cardId, autoReloadId, new InCommCardAutoReloadServiceCallBack() {
            @Override
            public void onCardAutoReloadServiceCallback(InCommAutoReloadSavable inCommAutoReloadSavable, Exception exception) {
                enableScreen(true);
                if (inCommAutoReloadSavable == null && exception == null) {
                    enableScreen(false);
                    GiftCardDataManager.getInstance().setAutoReloadSavable(inCommAutoReloadSavable);
                    GiftCardDataManager.getInstance().setAutoReloadLocalPaymentInfo(null);
                    refreshGiftCardListActivity();
                    Log.d("","refreshing gift card");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enableScreen(true);
                            alert(context,"Success!","Auto reload profile deleted successfully");
                        }
                    }, 5000);
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
                                deleteAutoReloadService();
                            }
                        }
                    });
                }
                else {
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(context, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        Utils.alert(context, "Failure", Utils.responseErrorNull());
                    }
                }
            }
        });
    }

    private void refreshGiftCardListActivity() {
        Intent intent = new Intent("BROADCAST_UPDATE_GF_HOME_ACTIVITY");
        intent.putExtra("pos", true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() != null) {
            if ((GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getPaymentAccountId() != 0)
                    || (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getAmount() != 0.00)
                    || (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getMinimumBalance() != 0.00)
                    || (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getEndsOn() != null)
                    || (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getNumberOfOccurancesRemaining() != 0)
                    || (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getReloadFrequencyId() != null)
                    || (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getStartsOn() != null)) {
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm");
                alertDialogBuilder.setMessage("Do you wish to cancel current auto reload?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GiftCardDataManager.getInstance().setInCommAutoReloadSubmitOrder(null);
                        GiftCardDataManager.getInstance().setAutoReloadLocalPaymentInfo(null);
                        navigateUp();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                GiftCardDataManager.getInstance().setInCommAutoReloadSubmitOrder(null);
                GiftCardDataManager.getInstance().setAutoReloadLocalPaymentInfo(null);
                navigateUp();
            }
        } else {
            navigateUp();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (GiftCardDataManager.getInstance().getAutoReloadSavable() != null) {
            enableScreen(false);
            if(isChecked){
                JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_AUTORELOAD_ENABLED);//tracking successful auto_reload enabling
                loaderText("Activating...");
            }else {
                JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.GIFT_CARD_AUTORELOAD_DISABLED);//tracking successful auto_reload disabling
                loaderText("Deactivating...");
            }
            updateStatus(isChecked);
        } else {
            if (isChecked) {
                ARScrollView.setBackgroundColor(Color.WHITE);
                ARScrollView.setAlpha((float) 1.0);
                bottomLayout.setBackgroundColor(Color.WHITE);
                bottomLayout.setAlpha((float) 1.0);
                selectReloadAmountLayout.setEnabled(true);
                selectReloadAmountLayout.setClickable(true);
                onSpecificLayout.setEnabled(true);
                onSpecificLayout.setClickable(true);
                balanceBelowLayout.setEnabled(true);
                balanceBelowLayout.setClickable(true);
                startDateLayout.setEnabled(true);
                startDateLayout.setClickable(true);
                endDateLayout.setEnabled(true);
                endDateLayout.setClickable(true);
                paymentAccountLayout.setEnabled(true);
                paymentAccountLayout.setClickable(true);
                ARFooter.setEnabled(true);
                ARFooter.setClickable(true);
            } else {
                ARScrollView.setBackgroundColor(Color.WHITE);
                ARScrollView.setAlpha((float) 0.5);
                bottomLayout.setBackgroundColor(Color.WHITE);
                bottomLayout.setAlpha((float) 0.5);
                selectReloadAmountLayout.setEnabled(false);
                selectReloadAmountLayout.setClickable(false);
                OSSpinner.setEnabled(false);
                OSSpinner.setClickable(false);
                balanceBelowLayout.setEnabled(false);
                balanceBelowLayout.setClickable(false);
                belowBalanceAmount.setEnabled(false);
                belowBalanceAmount.setClickable(false);
                dollarLable.setVisibility(View.INVISIBLE);
                selectBtnOnSpecific.setImageResource(R.drawable.select_white);
                selectBtnBalanceBelow.setImageResource(R.drawable.select_white);
                onSpecificText.setTextColor(Color.LTGRAY);
                OSSpinner.setVisibility(View.GONE);
                ReloadFreqId = "None";
                balanceBelowText.setTextColor(Color.LTGRAY);
                belowBalanceAmount.setEnabled(false);
                belowBalanceAmount.setClickable(false);
                belowBalanceAmount.getText().clear();
                dollarLable.setVisibility(View.INVISIBLE);
                onSpecificLayout.setEnabled(false);
                onSpecificLayout.setClickable(false);
                startDateLayout.setEnabled(false);
                startDateLayout.setClickable(false);
                endDateLayout.setEnabled(false);
                endDateLayout.setClickable(false);
                paymentAccountLayout.setEnabled(false);
                paymentAccountLayout.setClickable(false);
                ARFooter.setEnabled(false);
                ARFooter.setClickable(false);
            }
        }
    }
}
