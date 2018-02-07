package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardCreate;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.incomm.Models.InCommOrderItem;
import com.wearehathway.apps.incomm.Models.InCommOrderRecipientDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Created by Jeeva
 */
@SuppressWarnings("deprecation")
public class RecipientActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    private RelativeLayout yesLayout, noLayout, SRFooter,dateToSendLayout;
    private ImageButton yesCheckBtn, noCheckBtn;
    private EditText message, recipientEmailAddress, repFirstName, repLastName;
    private LinearLayout msgAndREAlayout;
    private Boolean isSelf;
    private Context context;
    private TextView dateToSend;
    private int yearNow, monthNow, dayNow;
    private boolean isSelected;
    private String selectedDate;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDateForSOD(arg1, arg2 + 1, arg3);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true);
        setBackButton(true,false);

        yesLayout = (RelativeLayout) findViewById(R.id.yesLayout);
        noLayout = (RelativeLayout) findViewById(R.id.noLayout);
        SRFooter = (RelativeLayout) findViewById(R.id.SRFooter);
        dateToSendLayout = (RelativeLayout) findViewById(R.id.dateToSendLayout);

        msgAndREAlayout = (LinearLayout) findViewById(R.id.msgAndREAlayout);

        yesCheckBtn = (ImageButton) findViewById(R.id.yesCheckBtn);
        noCheckBtn = (ImageButton) findViewById(R.id.noCheckBtn);

        message = (EditText) findViewById(R.id.message);
        recipientEmailAddress = (EditText) findViewById(R.id.recipientEmailAddress);
        repFirstName = (EditText) findViewById(R.id.repFirstName);
        repLastName = (EditText) findViewById(R.id.repLastName);

        dateToSend = (TextView) findViewById(R.id.dateToSend);

        yesLayout.setOnClickListener(this);
        noLayout.setOnClickListener(this);
        SRFooter.setOnClickListener(this);
        dateToSendLayout.setOnClickListener(this);

        msgAndREAlayout.setVisibility(View.GONE);

        initToolbar();
        setRecipient();

        Calendar calendar = Calendar.getInstance();
        yearNow = calendar.get(Calendar.YEAR);
        monthNow = calendar.get(Calendar.MONTH);
        dayNow = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressWarnings("deprecation")
    public void setDateAndTime(View view) {
        showDialog(view.getId());
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.dateToSendLayout) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, myDateListener, yearNow, monthNow, dayNow);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }
        return null;
    }

    private void showDateForSOD(int year, int month, int day) {

        isSelected = true;

        if (String.valueOf(month).length() == 1 && String.valueOf(day).length() == 1) {
            selectedDate = "0" + month + "/" + "0" + day + "/" + year;
        } else if (String.valueOf(month).length() == 1) {
            selectedDate = "0" + month + "/" + day + "/" + year;
        } else if (String.valueOf(day).length() == 1) {
            selectedDate = month + "/" + "0" + day + "/" + year;
        } else {
            selectedDate = month + "/" + day + "/" + year;
        }

        if(Utils.isToday(selectedDate)){
            dateToSend.setText("Today");
        }else if(Utils.isTomorrowDate(selectedDate)){
            dateToSend.setText("Tomorrow");
        }else{
            dateToSend.setText(selectedDate);
        }
    }

    private void initToolbar() {
        setTitle("Personalize");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    private void setRecipient() {
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            if (GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientEmailAddress() != null) {
                    if (!GiftCardDataManager.getInstance().getIsSelf()) {
                        yesCheckBtn.setVisibility(View.VISIBLE);
                        noCheckBtn.setVisibility(View.GONE);
                        msgAndREAlayout.setVisibility(View.VISIBLE);
                        isSelf = false;
                        isSelected = true;
                        GiftCardDataManager.getInstance().setIsSelf(isSelf);
                        repFirstName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getFirstName());
                        repLastName.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getLastName());
                        message.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getItems().get(0).getMessageText());
                        recipientEmailAddress.setText(GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getEmailAddress());
                     //   selectedDate = Utils.convertDateToString(GiftCardDataManager.getInstance().getGiftCardCreate().getRecipientInfo().get(0).getDeliverOn());

//                        if(Utils.isToday(selectedDate)){
//                            dateToSend.setText("Today");
//                        }else if(Utils.isTomorrowDate(selectedDate)){
//                            dateToSend.setText("Tomorrow");
//                        }else{
//                            dateToSend.setText(selectedDate);
//                        }


                    } else {
                        noCheckBtn.setVisibility(View.VISIBLE);
                        yesCheckBtn.setVisibility(View.GONE);
                        msgAndREAlayout.setVisibility(View.GONE);
                        isSelf = true;
                        GiftCardDataManager.getInstance().setIsSelf(isSelf);
                    }
                }
            }
        }
    }


    //If user select Yes
    private void setRecipientValues() {
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            InCommOrderItem inCommOrderItem = new InCommOrderItem();
            List<InCommOrderItem> inCommOrderItemsList = new ArrayList<>();
            inCommOrderItem.setAmount(Double.parseDouble(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount()));
            inCommOrderItem.setBrandId(getResources().getString(R.string.incomm_brand_id));
            inCommOrderItem.setQuantity(GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity());
            inCommOrderItem.setImageCode(GiftCardDataManager.getInstance().getGiftCardCreate().getSelectedImageCode());
            inCommOrderItem.setMessageText(message.getText().toString());
            inCommOrderItemsList.add(inCommOrderItem);

            InCommOrderRecipientDetails inCommOrderRecipientDetails = new InCommOrderRecipientDetails();
            List<InCommOrderRecipientDetails> inCommOrderRecipientDetailses = new ArrayList<>();
            inCommOrderRecipientDetails.setEmailAddress(recipientEmailAddress.getText().toString());
            inCommOrderRecipientDetails.setFirstName(repFirstName.getText().toString());
            inCommOrderRecipientDetails.setLastName(repLastName.getText().toString());
           // inCommOrderRecipientDetails.setDeliverOn(Utils.convertStringToDate(selectedDate));
            inCommOrderRecipientDetails.setDeliverOn(new Date("Thu Jun 08 23:30:00 GMT 2017"));
//            if(Utils.isToday(selectedDate)){
//                inCommOrderRecipientDetails.setDeliverOn(null);
//            }
            inCommOrderRecipientDetails.setItems(inCommOrderItemsList);
            inCommOrderRecipientDetailses.add(inCommOrderRecipientDetails);

            GiftCardDataManager.getInstance().getGiftCardCreate().setRecipientInfo(inCommOrderRecipientDetailses);
        } else {
            GiftCardCreate giftCardCreate = new GiftCardCreate();

            InCommOrderItem inCommOrderItem = new InCommOrderItem();
            List<InCommOrderItem> inCommOrderItemsList = new ArrayList<>();
            giftCardCreate.setAmount(0.00);
            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);

            inCommOrderItem.setAmount(Double.parseDouble(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount()));
            inCommOrderItem.setBrandId(getResources().getString(R.string.incomm_brand_id));
            inCommOrderItem.setQuantity(GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity());
            inCommOrderItem.setImageCode(GiftCardDataManager.getInstance().getGiftCardCreate().getSelectedImageCode());
            inCommOrderItem.setMessageText(message.getText().toString());
            inCommOrderItemsList.add(inCommOrderItem);

            InCommOrderRecipientDetails inCommOrderRecipientDetails = new InCommOrderRecipientDetails();
            List<InCommOrderRecipientDetails> inCommOrderRecipientDetailses = new ArrayList<>();
            inCommOrderRecipientDetails.setEmailAddress(recipientEmailAddress.getText().toString());
            inCommOrderRecipientDetails.setFirstName(repFirstName.getText().toString());
            inCommOrderRecipientDetails.setLastName(repLastName.getText().toString());
          //  inCommOrderRecipientDetails.setDeliverOn(Utils.convertStringToDate(selectedDate));
            inCommOrderRecipientDetails.setItems(inCommOrderItemsList);
            inCommOrderRecipientDetailses.add(inCommOrderRecipientDetails);

            giftCardCreate.setRecipientInfo(inCommOrderRecipientDetailses);

            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);
        }
    }


    //Setting purchaser info as recipient info if user select NO
    private void setPurAsRecipientValues() {
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            InCommOrderItem inCommOrderItem = new InCommOrderItem();
            List<InCommOrderItem> inCommOrderItemsList = new ArrayList<>();
            inCommOrderItem.setAmount(Double.parseDouble(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount()));
            inCommOrderItem.setBrandId(getResources().getString(R.string.incomm_brand_id));
            inCommOrderItem.setQuantity(GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity());
            inCommOrderItem.setImageCode(GiftCardDataManager.getInstance().getGiftCardCreate().getSelectedImageCode());
            inCommOrderItem.setMessageText("");
            inCommOrderItemsList.add(inCommOrderItem);

            InCommOrderRecipientDetails inCommOrderRecipientDetails = new InCommOrderRecipientDetails();
            List<InCommOrderRecipientDetails> inCommOrderRecipientDetailses = new ArrayList<>();
            inCommOrderRecipientDetails.setEmailAddress(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
            inCommOrderRecipientDetails.setFirstName(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName());
            inCommOrderRecipientDetails.setLastName(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getLastName());
            inCommOrderRecipientDetails.setItems(inCommOrderItemsList);
            inCommOrderRecipientDetailses.add(inCommOrderRecipientDetails);

            GiftCardDataManager.getInstance().getGiftCardCreate().setRecipientInfo(inCommOrderRecipientDetailses);
        } else {
            GiftCardCreate giftCardCreate = new GiftCardCreate();

            InCommOrderItem inCommOrderItem = new InCommOrderItem();
            List<InCommOrderItem> inCommOrderItemsList = new ArrayList<>();
            giftCardCreate.setAmount(0.00);
            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);

            inCommOrderItem.setAmount(Double.parseDouble(GiftCardDataManager.getInstance().getGiftCardCreate().getAmount()));
            inCommOrderItem.setBrandId(getResources().getString(R.string.incomm_brand_id));
            inCommOrderItem.setQuantity(GiftCardDataManager.getInstance().getGiftCardCreate().getQuantity());
            inCommOrderItem.setImageCode(GiftCardDataManager.getInstance().getGiftCardCreate().getSelectedImageCode());
            inCommOrderItem.setMessageText("");
            inCommOrderItemsList.add(inCommOrderItem);

            InCommOrderRecipientDetails inCommOrderRecipientDetails = new InCommOrderRecipientDetails();
            List<InCommOrderRecipientDetails> inCommOrderRecipientDetailses = new ArrayList<>();
            inCommOrderRecipientDetails.setEmailAddress(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getEmailAddress());
            inCommOrderRecipientDetails.setFirstName(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getFirstName());
            inCommOrderRecipientDetails.setLastName(GiftCardDataManager.getInstance().getGiftCardCreate().getPurchaserInfo().getLastName());
            inCommOrderRecipientDetails.setItems(inCommOrderItemsList);
            inCommOrderRecipientDetailses.add(inCommOrderRecipientDetails);

            giftCardCreate.setRecipientInfo(inCommOrderRecipientDetailses);

            GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            case R.id.yesLayout:
                yesCheckBtn.setVisibility(View.VISIBLE);
                noCheckBtn.setVisibility(View.GONE);
                msgAndREAlayout.setVisibility(View.VISIBLE);
                isSelf = false;
                break;

            case R.id.noLayout:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                noCheckBtn.setVisibility(View.VISIBLE);
                yesCheckBtn.setVisibility(View.GONE);
                msgAndREAlayout.setVisibility(View.GONE);
                isSelf = true;
                break;

            case R.id.dateToSendLayout:
                setDateAndTime(view);
                break;

            case R.id.SRFooter:
                if (yesCheckBtn.getVisibility() != View.VISIBLE && noCheckBtn.getVisibility() != View.VISIBLE) {
                    Utils.alert(context, "Alert", "please Select Yes or No");
                    return;
                }
                if (GiftCardDataManager.getInstance().getGiftCardCreate() == null) {
                    return;
                }
                GiftCardDataManager.getInstance().setIsSelf(isSelf);
                if ((yesCheckBtn.getVisibility() == View.VISIBLE) && (repFirstName.getText().toString().equalsIgnoreCase(""))) {
                    repFirstName.setError("Enter your first name");
                    repFirstName.requestFocus();
                    return;
                }
                if ((yesCheckBtn.getVisibility() == View.VISIBLE) && (repLastName.getText().toString().equalsIgnoreCase(""))) {
                    repLastName.setError("Enter your last name");
                    repLastName.requestFocus();
                    return;
                }

                if ((yesCheckBtn.getVisibility() == View.VISIBLE) && (recipientEmailAddress.getText().toString().equalsIgnoreCase(""))) {
                    recipientEmailAddress.setError("Enter your email address");
                    recipientEmailAddress.requestFocus();
                    return;
                }
                if ((yesCheckBtn.getVisibility() == View.VISIBLE) && !isValidEmail(recipientEmailAddress.getText().toString())) {
                    recipientEmailAddress.setError("Enter a valid email address");
                    recipientEmailAddress.requestFocus();
                    return;
                }
//                if ((yesCheckBtn.getVisibility() == View.VISIBLE) && !isSelected) {
//                    Utils.alert(context, "Alert", "Please select date to send");
//                    return;
//                }
                if (noCheckBtn.getVisibility() == View.VISIBLE) {
                    setPurAsRecipientValues();
                    onBackPressed();
                    return;
                }
                setRecipientValues();
                onBackPressed();
        }
    }

}
