//package com.fishbowl.LoyaltyTabletApp.Utils;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.DatePickerDialog.OnDateSetListener;
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.TextView;
//
//import com.fishbowl.LoyaltyTabletApp.R;
//
//import java.util.Calendar;
//
//public class MultiDatePickerActivity extends Activity {
//
//    private TextView startDateDisplay;
//    private TextView endDateDisplay;
//    private Button startPickDate;
//    private Button endPickDate;
//    private Calendar startDate;
//    private Calendar endDate;
//
//    static final int DATE_DIALOG_ID = 0;
//
//    private TextView activeDateDisplay;
//    private Calendar activeDate;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.customtimepicker);
//
//        /*  capture our View elements for the start date function   */
//        startDateDisplay = (TextView) findViewById(R.id.startDateDisplay);
//        startPickDate = (Button) findViewById(R.id.startPickDate);
//
//        /* get the current date */
//        startDate = Calendar.getInstance();
//
//        /* add a click listener to the button   */
//        startPickDate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                showDateDialog(startDateDisplay, startDate);
//            }
//        });
//
//        /* capture our View elements for the end date function */
//        endDateDisplay = (TextView) findViewById(R.id.endDateDisplay);
//        endPickDate = (Button) findViewById(R.id.endPickDate);
//
//        /* get the current date */
//        endDate = Calendar.getInstance();
//
//        /* add a click listener to the button   */
//        endPickDate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                showDateDialog(endDateDisplay, endDate);
//            }
//        });
//
//        /* display the current date (this method is below)  */
//        updateDisplay(startDateDisplay, startDate);
//        updateDisplay(endDateDisplay, endDate);
//    }
//
//    private void updateDisplay(TextView dateDisplay, Calendar date) {
//        dateDisplay.setText(
//                new StringBuilder()
//                        // Month is 0 based so add 1
//                        .append(date.get(Calendar.MONTH) + 1).append("-")
//                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
//                        .append(date.get(Calendar.YEAR)).append(" "));
//
//    }
//
//    public void showDateDialog(TextView dateDisplay, Calendar date) {
//        activeDateDisplay = dateDisplay;
//        activeDate = date;
//        showDialog(DATE_DIALOG_ID);
//    }
//
//    private OnDateSetListener dateSetListener = new OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            activeDate.set(Calendar.YEAR, year);
//            activeDate.set(Calendar.MONTH, monthOfYear);
//            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updateDisplay(activeDateDisplay, activeDate);
//            unregisterDateDisplay();
//        }
//    };
//
//    private void unregisterDateDisplay() {
//        activeDateDisplay = null;
//        activeDate = null;
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DATE_DIALOG_ID:
//                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog) {
//        super.onPrepareDialog(id, dialog);
//        switch (id) {
//            case DATE_DIALOG_ID:
//                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
//                break;
//        }
//    }
//}
