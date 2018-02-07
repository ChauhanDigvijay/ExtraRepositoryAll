package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CustomDatePicker;

import java.util.Calendar;
import java.util.Date;

public class SignUpDOBActivity extends SignUpBaseActivity implements View.OnClickListener {
    public static int BirthdayMaxYear = 0;
    public static int BirthdayMaxMonth = 0;
    public static int BirthdayMaxDays = 0;
    int yearNow, monthNow, dayNow;
    DatePickerDialog datePickerDialog;
    CustomDatePicker customDatePicker;
    boolean isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_dob);
        setUpToolBar(true);
        isShowBasketIcon = false;
        setTitle("Birthday");
        setBackButton(true, false);
        Button continueBtn = (Button) findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(this);
        initializeCircle(7);
        TextView dobTextView = (TextView) findViewById(R.id.dobTextView);
        dobTextView.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -18);
        Date newDate = calendar.getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(newDate);
        yearNow = calendar1.get(Calendar.YEAR);
        monthNow = calendar1.get(Calendar.MONTH) + 1;
        dayNow = calendar1.get(Calendar.DAY_OF_MONTH);
        BirthdayMaxDays = dayNow;
        BirthdayMaxMonth = monthNow;
        BirthdayMaxYear = yearNow;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.continueBtn) {
            continuePressed();
        } else if (id == R.id.dobTextView) {
            // showDatePicker();
            showCustomDatePicker();
        }
    }

    private void showDatePicker() {
        if (datePickerDialog != null) {
            datePickerDialog.cancel();
        }
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setViews(year, monthOfYear, dayOfMonth);
                datePickerDialog = null;
            }
        }, yearNow, monthNow, dayNow);

        Calendar now = Calendar.getInstance();
        //now.add(Calendar.YEAR, -SPENDGO_SIGNUP_AGE_LIMIT);
        datePickerDialog.getDatePicker().setMaxDate(now.getTimeInMillis());
        datePickerDialog.show();

    }

    private void showCustomDatePicker() {
        if (customDatePicker != null) {
            customDatePicker.dismiss();
        }
        customDatePicker = CustomDatePicker.newInstance(monthNow, dayNow, yearNow);

        customDatePicker.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                setViews(selectedYear, selectedMonth, selectedDay);
                customDatePicker = null;
            }
        });

        customDatePicker.show(getFragmentManager(), "CustomDatePicker");
    }

    private void setViews(int year, int monthOfYear, int dayOfMonth) {
        yearNow = year;
        monthNow = monthOfYear;
        dayNow = dayOfMonth;
        isSelected = true;
        TextView dobTextView = (TextView) findViewById(R.id.dobTextView);
        dobTextView.setText(Utils.getFormattedBirthdayString(yearNow, monthNow - 1, dayNow));
    }

    private void continuePressed() {
        if (!isSelected) {
            Utils.showAlert(this, "Please select your date of birth.");
            return;
        }
//        else if (!isEighteenYearsOld()) {
//            Utils.showAlert(this, "You must be at least 18 years old to sign up.");
//            return;
//        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.putString(Constants.B_DOB, Utils.getBirthdayInSpendGoFormat(yearNow, monthNow -1 , dayNow));
            TransitionManager.transitFrom(SignUpDOBActivity.this, SignUpPasswordActivity.class, bundle);
        }
    }


    private boolean isEighteenYearsOld() {
        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        dob.set(yearNow, monthNow-1, dayNow);
        if (dob.after(now)) {
            return false;
        }
        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);
        int age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }
        return age >= 18;
    }

}
