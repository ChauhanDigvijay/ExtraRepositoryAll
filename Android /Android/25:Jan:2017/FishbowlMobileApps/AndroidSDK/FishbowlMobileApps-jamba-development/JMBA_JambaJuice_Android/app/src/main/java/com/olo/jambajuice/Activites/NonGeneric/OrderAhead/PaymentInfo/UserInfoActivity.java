package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CapitalizeTextWatcher;
import com.wearehathway.apps.olo.Models.OloOrderInfo;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        if (DataManager.getInstance().getCurrentBasket() == null) {
            //Incase the data is not available and activity is recreating.
            finish();
            return;
        }
        isShowBasketIcon = false;
        setUpView();
        //setUpDummyData();
    }

    private void setUpView() {
        createToolBar();
        Button button = (Button) findViewById(R.id.continueBtn);
        button.setOnClickListener(this);
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        firstName.addTextChangedListener(new CapitalizeTextWatcher(firstName));
        lastName.addTextChangedListener(new CapitalizeTextWatcher(lastName));
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_BASKET_UI));
    }

    private void createToolBar() {
        setUpToolBar(true);
        setTitle("Enter Your Information", getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        setBackButton(true,true);
    }

    @Override
    public void onClick(View v) {
        verifyFieldsAndContinue();
    }

    private void verifyFieldsAndContinue() {
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText email = (EditText) findViewById(R.id.email);
        EditText contactNumber = (EditText) findViewById(R.id.contactNumber);

        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String emailStr = email.getText().toString();
        String contactNumberStr = contactNumber.getText().toString();

        boolean isTransitionAllowed = true;
        if (fName.equals("")) {
            firstName.setError("Please enter your first name");
            isTransitionAllowed = false;
        }
        if (lName.equals("")) {
            lastName.setError("Please enter your last name");
            isTransitionAllowed = false;
        }
        if (!Utils.isValidEmail(emailStr)) {
            email.setError("Please enter a valid email address");
            isTransitionAllowed = false;
        }
        if (!Utils.isValidCellPhone(contactNumberStr)) {
            contactNumber.setError("Please enter a valid " + Constants.PHONE_NUMBER_LENGTH + " digit phone number");
            isTransitionAllowed = false;
        }
        if (isTransitionAllowed) {
            OloOrderInfo orderInfo = DataManager.getInstance().getOrderInfo();
            orderInfo.setFirstName(fName);
            orderInfo.setLastName(lName);
            orderInfo.setEmail(emailStr);
            orderInfo.setContactNumber(contactNumberStr);
            TransitionManager.transitFrom(this, AddCardActivity.class);
        }
    }

    private void setUpDummyData() {
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText email = (EditText) findViewById(R.id.email);
        EditText contactNumber = (EditText) findViewById(R.id.contactNumber);

        firstName.setText("Nauman");
        lastName.setText("Afzaal");
        email.setText("nauman@gmail.com");
        contactNumber.setText("1111111111");

    }

    @Override
    protected void handleBroadCastReceiver(Intent intent) {
        finish();
    }
}

