package com.olo.jambajuice.Views.Generic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp.SignUpDOBActivity;
import com.olo.jambajuice.R;

import java.util.Calendar;

/**
 * Created by vt021 on 28/06/17.
 */

public class CustomDatePicker extends DialogFragment {

    public static final String MONTH_KEY = "monthValue";
    public static final String DAY_KEY = "dayValue";
    public static final String YEAR_KEY = "yearValue";
    private final int minYear = 1900;
    int monthVal = -1, dayVal = -1, yearVal = -1;
    int maxFinalYear = SignUpDOBActivity.BirthdayMaxYear;
    int maxFinalMonth = SignUpDOBActivity.BirthdayMaxMonth;
    int maxFinalDay = SignUpDOBActivity.BirthdayMaxDays;
    private DatePickerDialog.OnDateSetListener listener;
    private int daysOfMonth = 31;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private NumberPicker dayPicker;
    private Button setButton;
    private Button cancelButton;
    private Calendar cal = Calendar.getInstance();

    public static CustomDatePicker newInstance(int monthIndex, int daysIndex, int yearIndex) {
        CustomDatePicker f = new CustomDatePicker();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(MONTH_KEY, monthIndex);
        args.putInt(DAY_KEY, daysIndex);
        args.putInt(YEAR_KEY, yearIndex);
        f.setArguments(args);

        return f;
    }

    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        if (extras != null) {
            monthVal = extras.getInt(MONTH_KEY, -1);
            dayVal = extras.getInt(DAY_KEY, -1);
            yearVal = extras.getInt(YEAR_KEY, -1);
        }
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //getDialog().setTitle("Select your Birthday Date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.custom_date_picker, null);
        monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        dayPicker = (NumberPicker) dialog.findViewById(R.id.picker_day);
        setButton = (Button) dialog.findViewById(R.id.tv_setDate);
        cancelButton = (Button) dialog.findViewById(R.id.tv_cancel);

        setDialogue();
        checkMaxYearSetMonthAndDays();
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkMaxYearSetMonthAndDays();
            }
        });


        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                yearVal = picker.getValue();
                checkMaxYearSetMonthAndDays();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = yearPicker.getValue();
                if (year == (minYear + 1)) {
                    year = 1904;
                }
                listener.onDateSet(null, year, monthPicker.getValue(), dayPicker.getValue());
                CustomDatePicker.this.getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDatePicker.this.getDialog().dismiss();
            }
        });

//        builder.setView(dialog)
//                // Add action buttons
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        int year = yearPicker.getValue();
//                        if(year == (minYear+1)){
//                            year = 1904;
//                        }
//                        listener.onDateSet(null, year, monthPicker.getValue(), dayPicker.getValue());
//                    }
//                })
//                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        CustomDatePicker.this.getDialog().cancel();
//                    }
//                });
        builder.setView(dialog);
        return builder.create();
    }

    private void checkMaxYearSetMonthAndDays() {
        if (yearPicker.getValue() == maxFinalYear && monthPicker.getValue() == maxFinalMonth) {
            daysOfMonth = maxFinalDay;
            dayPicker.setMaxValue(daysOfMonth);
            monthPicker.setMaxValue(maxFinalMonth);
        }
        else {
            try {
                if (isLeapYear(yearPicker.getValue()) && monthPicker.getValue() == 2) {
                    daysOfMonth = 29;
                    dayPicker.setMaxValue(daysOfMonth);
                    monthPicker.setMaxValue(12);
                } else if (!isLeapYear(yearPicker.getValue()) && monthPicker.getValue() == 2) {
                    daysOfMonth = 28;
                    dayPicker.setMaxValue(daysOfMonth);
                    monthPicker.setMaxValue(12);
                } else if (monthPicker.getValue() == 1
                        || monthPicker.getValue() == 3
                        || monthPicker.getValue() == 5
                        || monthPicker.getValue() == 7
                        || monthPicker.getValue() == 8
                        || monthPicker.getValue() == 10
                        || monthPicker.getValue() == 12) {
                    daysOfMonth = 31;
                    dayPicker.setMaxValue(daysOfMonth);
                    monthPicker.setMaxValue(12);
                } else {
                    daysOfMonth = 30;
                    dayPicker.setMaxValue(daysOfMonth);
                    monthPicker.setMaxValue(12);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDialogue() {
        monthPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);


        if (monthVal != -1)// && (monthVal > 0 && monthVal < 13))
            monthPicker.setValue(monthVal);
        else
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        monthPicker.setDisplayedValues(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
                "Aug", "Sep", "Oct", "Nov", "Dec"});


        dayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(daysOfMonth);

        if (dayVal != -1)
            dayPicker.setValue(dayVal);
        else
            dayPicker.setValue(cal.get(Calendar.DAY_OF_MONTH));

        int maxYear = maxFinalYear;
        int arraySize = maxYear - minYear;

        String[] tempArray = new String[arraySize];
        // tempArray[0] = "---";
        int tempYear = minYear + 1;

        for (int i = 0; i < arraySize; i++) {
            tempArray[i] = " " + tempYear + "";
            tempYear++;
        }
        Log.i("", "onCreateDialog: " + tempArray.length);
        yearPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearPicker.setMinValue(minYear + 1);
        yearPicker.setMaxValue(maxYear);
        yearPicker.setDisplayedValues(tempArray);

        if (yearVal != -1)
            yearPicker.setValue(yearVal);
        else
            yearPicker.setValue(cal.get(Calendar.YEAR));
    }
}
