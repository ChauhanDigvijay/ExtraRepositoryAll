package com.BasicApp.Activites.NonGeneric.Contact;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.BusinessLogic.Models.AreaType;
import com.BasicApp.BusinessLogic.Models.MessageType;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBLoyaltyAreaTypeCallback;
import com.fishbowl.basicmodule.Interfaces.FBLoyaltyMessageTypeCallback;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBLoyaltyAreaTypeItem;
import com.fishbowl.basicmodule.Models.FBLoyaltyAreaTypeListItem;
import com.fishbowl.basicmodule.Models.FBLoyaltyMessageTypeItem;
import com.fishbowl.basicmodule.Models.FBLoyaltyMessageTypeListItem;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBLoyaltyService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by schaudhary_ic on 01-Dec-16.
 */

public class ContactUsModelActivity extends BaseActivity {

    public static Spinner messageSpinner, areaSpinner;
    public List<AreaType> allAreaListfromServer = new ArrayList<AreaType>();
    public List<MessageType> allMessageListfromServer = new ArrayList<MessageType>();
    public Map<String, Integer> allmessageoId = new HashMap<String, Integer>();
    public Map<String, Integer> allareaToId = new HashMap<String, Integer>();
    public List<FBLoyaltyMessageTypeItem> messagetype = new ArrayList<>();
    public List<FBLoyaltyAreaTypeItem> areatype = new ArrayList<>();
    EditText subject, message;
    Button submit, reset;
    ProgressBarHandler p;
    int areaPosition, messagePosition;
    ArrayList<String> messagelist = new ArrayList<String>();
    ArrayList<String> arealist = new ArrayList<String>();
    ArrayAdapter<String> messageadapter;
    ArrayAdapter<String> areaadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        p = new ProgressBarHandler(this);
        messageSpinner = (Spinner) findViewById(R.id.spinner1);
        areaSpinner = (Spinner) findViewById(R.id.spinner2);
        subject = (EditText) findViewById(R.id.et_subject);
        message = (EditText) findViewById(R.id.et_message);
        submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUs();
            }
        });
        reset = (Button) findViewById(R.id.btn_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.getText().clear();
                message.getText().clear();
                messageSpinner.setSelection(0);
                areaSpinner.setSelection(0);
            }
        });
        setUpToolBar(true, true);
        setTitle("Contact Us");
        setBackButton(false, false);


        getLoyaltyMessageType();
        getLoyaltyAreaType();

        messagelist.add(0, "Select Option");
        messageadapter = new ArrayAdapter<String>(this, R.layout.spinner_item, messagelist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                return view;
            }
        };
        messageadapter.setDropDownViewResource(R.layout.spinner_item);
        messageSpinner.setAdapter(messageadapter);


        messageadapter.notifyDataSetChanged();

        messageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // Toast.makeText(getActivity(), "Selected", Toast.LENGTH_SHORT).show();
                // parentView.setBackgroundColor(Color.BLACK);
                //    ((TextView) selectedItemView).setTextColor(Color.parseColor("#E3170D"));

                //  messageadapter.getDropDownView()
                TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(ContextCompat.getColor(ContactUsModelActivity.this, R.color.black));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
//                TextView selectedText = (TextView) parentView.getChildAt(0);
//                if (selectedText != null) {
//                    selectedText.setTextColor(Color.RED);
//                }
            }

        });

        arealist.add(0, "Select Option");
        areaadapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arealist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                return view;
            }
        };
        areaSpinner.setAdapter(areaadapter);
        areaadapter.setDropDownViewResource(R.layout.spinner_item);
        areaadapter.notifyDataSetChanged();
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(getActivity(), "Selected", Toast.LENGTH_SHORT).show();
                TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(ContextCompat.getColor(ContactUsModelActivity.this, R.color.black));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        areaSpinner.setPrompt("Area");
    }

    @Override
    protected void onStart() {
        super.onStart();

        String buttoncolor = FBViewMobileSettingsService.sharedInstance().checkInButtonColor;
        if (buttoncolor != null) {
            //    toolbarTittle.setTextColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));
        }
    }


    public void getLoyaltyAreaType() {
        p.show();

        FBLoyaltyService.getLoyaltyAreaType(new FBLoyaltyAreaTypeCallback() {
            public void onFBLoyaltyTypeCallback(FBLoyaltyAreaTypeListItem loyaltytype, Exception error) {
                try {
                    if (loyaltytype != null) {

                        p.hide();

                        FBLoyaltyAreaTypeItem[] categories = loyaltytype.getCategories();
                        areatype = Arrays.asList(categories);
                        for(FBLoyaltyAreaTypeItem loyaltyareatype:areatype)
                        {
                            arealist.add(loyaltyareatype.getAreaType());
                        }
                        areaadapter.notifyDataSetChanged();

                    } else {
                        FBUtils.tryHandleTokenExpiry(ContactUsModelActivity.this, error);

                    }
                } catch (Exception e) {
                }
            }
        });
    }


    public void getLoyaltyMessageType() {
        p.show();

        FBLoyaltyService.getLoyaltyMessageType(new FBLoyaltyMessageTypeCallback() {
            public void onFBLoyaltyMessageTypeCallback(FBLoyaltyMessageTypeListItem loyaltytype, Exception error) {
                try {
                    if (loyaltytype != null) {
                        p.hide();
                        FBLoyaltyMessageTypeItem[] categories = loyaltytype.getCategories();
                        messagetype = Arrays.asList(categories);
                        for(FBLoyaltyMessageTypeItem loyaltyareatype:messagetype)
                        {
                            messagelist.add(loyaltyareatype.getMessageType());
                        }
                        messageadapter.notifyDataSetChanged();
                    } else {
                        FBUtils.tryHandleTokenExpiry(ContactUsModelActivity.this, error);

                    }
                } catch (Exception e) {
                }
            }
        });

    }

    public boolean checkValidation() {
        //  fname, lname, email, phone, address,password;
        if (!FBUtils.isValidString(subject.getText().toString())) {
            FBUtils.showAlert(this, "Message Subject is Empty ");
            return false;
        }
        if (!FBUtils.isValidString(messageSpinner.getSelectedItem().toString()) || messageSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Option")) {
            FBUtils.showAlert(this, "Message Type is Empty ");
            return false;
        }
        if (!FBUtils.isValidString(areaSpinner.getSelectedItem().toString()) || areaSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Option")) {
            FBUtils.showAlert(this, "Area Type is Empty ");
            return false;
        }
        if (!FBUtils.isValidString(message.getText().toString())) {
            FBUtils.showAlert(this, "Message is Empty");
            return false;
        }

        return true;
    }

    public void contactUs() {
        String sub = subject.getText().toString();
        String msg = message.getText().toString();
        String area = areaSpinner.getSelectedItem().toString();
        String msgType = messageSpinner.getSelectedItem().toString();

        p.show();

        FBLoyaltyService.contactUs(sub, msg, area, msgType, new FBSessionServiceCallback() {
            public void onSessionServiceCallback(FBSessionItem user, Exception error) {
                // progressBarHandler.hide();
                if (user != null) {

                    finish();
                    subject.getText().clear();
                    message.getText().clear();
                    messageSpinner.setSelection(0);
                    areaSpinner.setSelection(0);
                    p.dismiss();
                } else {
                    p.hide();
                   // alertmessage(user.getString("message"));
                }
            }


        });
    }

//
//    public void alertmessage(String message) {
//        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage(message);
//        alertDialog.setIcon(R.drawable.logo);
//        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        alertDialog.show();
//    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }


}
