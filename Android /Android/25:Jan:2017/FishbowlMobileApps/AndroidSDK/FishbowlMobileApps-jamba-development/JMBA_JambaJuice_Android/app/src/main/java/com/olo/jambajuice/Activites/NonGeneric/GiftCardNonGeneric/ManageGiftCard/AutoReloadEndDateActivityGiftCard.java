package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.BoldTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
    Created by jeeva
 */
public class AutoReloadEndDateActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    private RelativeLayout neverLayout, afterLayout, onLayout, EDFooter, afterEditTextLayout;
    private ImageView SRASelect, ASelect, OnSelect;
    private BoldTextView onDate;
    private EditText noOcc;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDateForSOD(arg1, arg2 + 1, arg3);
        }
    };
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reload_end_date);

        context = this;

        isShowBasketIcon = false;

        setUpToolBar(true);
        setBackButton(true,false);

        neverLayout = (RelativeLayout) findViewById(R.id.neverLayout);
        onLayout = (RelativeLayout) findViewById(R.id.onLayout);
        afterLayout = (RelativeLayout) findViewById(R.id.afterLayout);
        EDFooter = (RelativeLayout) findViewById(R.id.EDFooter);
        afterEditTextLayout = (RelativeLayout) findViewById(R.id.afterEditTextLayout);

        SRASelect = (ImageView) findViewById(R.id.SRASelect);
        ASelect = (ImageView) findViewById(R.id.ASelect);
        OnSelect = (ImageView) findViewById(R.id.OnSelect);

        onDate = (BoldTextView) findViewById(R.id.onDate);

        noOcc = (EditText) findViewById(R.id.noOcc);

        neverLayout.setOnClickListener(this);
        afterLayout.setOnClickListener(this);
        onLayout.setOnClickListener(this);
        EDFooter.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        initToolbar();
        setData();

    }

    private void initToolbar() {
        setTitle("End Date");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
    }

    private void setData() {
        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() != null) {
            if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getEndsOn() != null) {
                SRASelect.setVisibility(View.GONE);
                ASelect.setVisibility(View.GONE);
                afterEditTextLayout.setVisibility(View.GONE);
                OnSelect.setVisibility(View.VISIBLE);
                onDate.setVisibility(View.VISIBLE);
                onDate.setText(convertDateToString(GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getEndsOn()));
            } else if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getNumberOfOccurancesRemaining() > 0) {
                SRASelect.setVisibility(View.GONE);
                OnSelect.setVisibility(View.GONE);
                onDate.setVisibility(View.GONE);
                ASelect.setVisibility(View.VISIBLE);
                afterEditTextLayout.setVisibility(View.VISIBLE);
                noOcc.setText(String.valueOf(GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getNumberOfOccurancesRemaining()));
                noOcc.setSelection(noOcc.getText().length());
            } else {
                SRASelect.setVisibility(View.VISIBLE);
                onDate.setVisibility(View.GONE);
                OnSelect.setVisibility(View.GONE);
                ASelect.setVisibility(View.GONE);
                afterEditTextLayout.setVisibility(View.GONE);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void setDateAndTime(View view) {
        showDialog(view.getId());
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.onLayout) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }
        return null;
    }

    private void showDateForSOD(int year, int month, int day) {
        onDate.setVisibility(View.VISIBLE);
        if (String.valueOf(month).length() == 1 && String.valueOf(day).length() == 1) {
            onDate.setText(new StringBuilder().append("0" + month).append("/")
                    .append("0" + day).append("/").append(year));
        } else if (String.valueOf(month).length() == 1) {
            onDate.setText(new StringBuilder().append("0" + month).append("/")
                    .append(day).append("/").append(year));
        } else if (String.valueOf(day).length() == 1) {
            onDate.setText(new StringBuilder().append(month).append("/")
                    .append("0" + day).append("/").append(year));
        } else {
            onDate.setText(new StringBuilder().append(month).append("/")
                    .append(day).append("/").append(year));
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

    private String convertStringToDate(String endDateString) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat utcFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = null;
        String utcEndDate="";
        try {
            startDate = df.parse(endDateString);
            utcEndDate= utcFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcEndDate;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.neverLayout:
                SRASelect.setVisibility(View.VISIBLE);
                onDate.setVisibility(View.GONE);
                ASelect.setVisibility(View.GONE);
                OnSelect.setVisibility(View.GONE);
                afterEditTextLayout.setVisibility(View.GONE);
                break;

            case R.id.afterLayout:
                SRASelect.setVisibility(View.GONE);
                onDate.setVisibility(View.GONE);
                OnSelect.setVisibility(View.GONE);
                ASelect.setVisibility(View.VISIBLE);
                afterEditTextLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.onLayout:
                SRASelect.setVisibility(View.GONE);
                ASelect.setVisibility(View.GONE);
                afterEditTextLayout.setVisibility(View.GONE);
                OnSelect.setVisibility(View.VISIBLE);
                setDateAndTime(view);
                break;

            case R.id.EDFooter:

                if (SRASelect.getVisibility() == View.VISIBLE) {
                    GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setEndsOn(null);
                    GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setNumberOfOccurancesRemaining(0);
                    onBackPressed();
                    return;

                }

                if (onDate.getVisibility() == View.VISIBLE) {
                    GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setEndsOn(convertStringToDate(onDate.getText().toString()+ Constants.UTC_TIME_1AM));
                    GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setNumberOfOccurancesRemaining(0);
                    onBackPressed();
                    return;
                }


                if (ASelect.getVisibility() == View.VISIBLE) {
                    if (noOcc.getText().toString().length() > 0) {
                        if (Integer.parseInt(noOcc.getText().toString()) > 0 && Integer.parseInt(noOcc.getText().toString()) <= 100) {
                            GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setNumberOfOccurancesRemaining(Integer.parseInt(noOcc.getText().toString()));
                            GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().setEndsOn(null);
                            onBackPressed();
                            return;
                        } else {
                            Utils.alert(context, "Auto Reload", "Please enter no of occurrences between 1 to 100");
                            return;
                        }
                    } else {
                        Utils.alert(context, "Auto Reload", "Please enter no of occurrences between 1 to 100");
                        return;
                    }
                }


                Utils.alert(context, "Alert", "Please choose any end type");

                break;

        }
    }
}
