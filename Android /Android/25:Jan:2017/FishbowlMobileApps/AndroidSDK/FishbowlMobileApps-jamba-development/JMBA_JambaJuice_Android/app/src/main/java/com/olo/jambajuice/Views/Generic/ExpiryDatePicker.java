package com.olo.jambajuice.Views.Generic;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.olo.jambajuice.BusinessLogic.Interfaces.ExpiryDateListener;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import static com.olo.jambajuice.Utils.Constants.MAX_CREDIT_CARD_EXPIRY_YEARS;

/**
 * Created by Ihsanulhaq on 6/10/2015.
 */
public class ExpiryDatePicker extends Dialog implements View.OnClickListener {

    final int yearNow = Calendar.getInstance().get(Calendar.YEAR);
    final int monthNow = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int resultYear;
    private int resultMonth;
    private Button setButton;
    private NumberPicker yearsPicker;
    private NumberPicker monthsPicker;
    private ExpiryDateListener listener;
    private String[] filteredMonths;

    public ExpiryDatePicker(Context context, ExpiryDateListener listener) {
        super(context);
        this.setContentView(R.layout.layout_expiry_date_dialog);
        initComponents();

        resultYear = yearNow;
        resultMonth = monthNow;

        setDialogValues(resultYear);
        setListeners(listener);

        setTitleDate();
    }

    private void initComponents() {
        monthsPicker = (NumberPicker) findViewById(R.id.months_picker);
        yearsPicker = (NumberPicker) findViewById(R.id.years_picker);
        setButton = (Button) findViewById(R.id.tv_setDate);
    }

    private void setTitleDate() {
        int monthValue = getMonthNumber(filteredMonths[monthsPicker.getValue()-1]);
        String month = monthValue > 9 ? monthValue + "" : "0" + monthValue;
        setTitle(month + "/" + resultYear);
    }

    private void setDialogValues(int selectedYear) {
        monthsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        String months[] = new DateFormatSymbols().getMonths();
        if (resultYear != 0 && resultYear == yearNow ) {
            filteredMonths = new String[(12 - monthNow) + 1];
            int size = filteredMonths.length;
            for (int i = 0; i < size; i++) {
                filteredMonths[i] = months[(monthNow-1)+i];
            }
            if(monthsPicker.getDisplayedValues() != null){
                monthsPicker.setDisplayedValues(null);
            }
            monthsPicker.setMinValue(1);
            monthsPicker.setMaxValue(filteredMonths.length);
            monthsPicker.setDisplayedValues(filteredMonths);
            monthsPicker.setValue(1);
        } else {
            filteredMonths = months;
            if(monthsPicker.getDisplayedValues() != null){
                monthsPicker.setDisplayedValues(null);
            }
            monthsPicker.setMinValue(1);
            monthsPicker.setMaxValue(filteredMonths.length);
            monthsPicker.setDisplayedValues(filteredMonths);
            monthsPicker.setValue((monthsPicker.getValue()+monthNow)-1);
        }


        yearsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        int yearCount = yearNow;
        String years[] = new String[MAX_CREDIT_CARD_EXPIRY_YEARS];
        for (int i = 0; i < years.length; i++) {
            years[i] = yearCount++ + "";
        }
        yearsPicker.setDisplayedValues(years);
        yearsPicker.setMinValue(1);
        yearsPicker.setMaxValue(MAX_CREDIT_CARD_EXPIRY_YEARS);
        yearsPicker.setWrapSelectorWheel(false);
        yearsPicker.setValue(getIndexOfYear(selectedYear,years));

    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
//            if (resultYear == yearNow) {
//                if (resultMonth < monthNow) {
//                    Utils.showAlert(getContext(), "Invalid Expiration Month", "Error");
//                    return;
//                }
//            }
            listener.onDateChangeListener(getMonthNumber(filteredMonths[monthsPicker.getValue()-1]), resultYear);
        }
        this.dismiss();
    }

    private void setListeners(ExpiryDateListener listener) {
        this.listener = listener;
        setButton.setOnClickListener(this);
        monthsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                resultMonth = getMonthNumber(filteredMonths[newVal-1]);
                setTitleDate();
            }
        });
        yearsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                resultYear = yearNow + newVal - 1;
                if(resultYear != yearNow){
                    if(filteredMonths.length != 12){
                        setDialogValues(resultYear);
                    }
                }
                if(resultYear == yearNow){
                    setDialogValues(resultYear);
                }
                setTitleDate();
            }
        });
    }

    private int getMonthNumber(String monthName) {

        int month = 0;
        if(monthName.equalsIgnoreCase("January")){
            month =  1;
        }
        if(monthName.equalsIgnoreCase("February")){
            month =  2;
        }
        if(monthName.equalsIgnoreCase("March")){
            month =  3;
        }
        if(monthName.equalsIgnoreCase("April")){
            month =  4;
        }
        if(monthName.equalsIgnoreCase("May")){
            month =  5;
        }
        if(monthName.equalsIgnoreCase("June")){
            month =  6;
        }
        if(monthName.equalsIgnoreCase("July")){
            month =  7;
        }
        if(monthName.equalsIgnoreCase("August")){
            month =  8;
        }
        if(monthName.equalsIgnoreCase("September")){
            month =  9;
        }
        if(monthName.equalsIgnoreCase("October")){
            month =  10;
        }
        if(monthName.equalsIgnoreCase("November")){
            month =  11;
        }
        if(monthName.equalsIgnoreCase("December")){
            month =  12;
        }

        return month;
    }

    private int getIndexOfYear(int year,String[] years){
        int index = 0;
        for (int i=0;i<years.length;i++) {
            if (years[i].equalsIgnoreCase(String.valueOf(year))) {
                index = i;
                break;
            }
        }
        return index+1;
    }
}
