package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.AreaType;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.MessageType;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FB_LY_MobileSettingService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fishbowl.LoyaltyTabletApp.R.id.btn_submit;
import static com.fishbowl.LoyaltyTabletApp.R.id.spinner2;

/**
 * Created by schaudhary_ic on 09-Nov-16.
 */

public class FragmentContactUs extends Fragment implements View.OnClickListener {
    public static Spinner messageSpinner, areaSpinner;
    public List<AreaType> allAreaListfromServer = new ArrayList<AreaType>();
    public List<MessageType> allMessageListfromServer = new ArrayList<MessageType>();
    public Map<String, Integer> allmessageoId = new HashMap<String, Integer>();
    public Map<String, Integer> allareaToId = new HashMap<String, Integer>();
    EditText subject, message;
    Button submit, reset;
    ProgressBarHandler p;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);
        p = new ProgressBarHandler(getContext());
        getLoyaltyMessageType();
        getLoyaltyAreaType();
        messageSpinner = (Spinner) v.findViewById(R.id.spinner1);
        areaSpinner = (Spinner) v.findViewById(spinner2);
        subject = (EditText) v.findViewById(R.id.et_subject);
        message = (EditText) v.findViewById(R.id.et_message);

        submit = (Button) v.findViewById(btn_submit);
        submit.setOnClickListener(this);
        reset = (Button) v.findViewById(R.id.btn_reset);
        reset.setOnClickListener(this);
        list.add(0, "Select Option");
        adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
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
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                }
                return view;
            }
        };
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        messageSpinner.setAdapter(adapter1);


        adapter1.notifyDataSetChanged();

        messageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        list1.add(0, "Select Option");
        adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list1) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
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
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                }
                return view;
            }
        };
        areaSpinner.setAdapter(adapter2);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        adapter2.notifyDataSetChanged();
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(getActivity(), "Selected", Toast.LENGTH_SHORT).show();
                TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        areaSpinner.setPrompt("Area");
        return v;
    }


    public void contactUs() {

        String sub = subject.getText().toString();
        String msg = message.getText().toString();
        String area = areaSpinner.getSelectedItem().toString();
        String msgType = messageSpinner.getSelectedItem().toString();
        JSONObject obj = new JSONObject();
        try {
            obj.put("subject", sub);
            obj.put("description", msg);
            obj.put("messageType", msgType);
            obj.put("areaType", area);
            obj.put("memberId", FB_LY_UserService.sharedInstance().member.customerID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.show();
        FB_LY_UserService.sharedInstance().contactUs(obj, new FB_LY_UserService.FBContactUsCallback() {
            @Override
            public void onContactUsCallback(JSONObject response, Exception error) {
                if (response != null) {
                    if (response.has("successFlag")) {
                        try {
                            if (response.getString("successFlag").equalsIgnoreCase("true")) {

                                FBUtils.showAlert(getContext(), "Message successfully sent");
                                subject.getText().clear();
                                message.getText().clear();
                                messageSpinner.setSelection(0);
                                areaSpinner.setSelection(0);
                                p.dismiss();
                            } else {
                                p.dismiss();
                                alertmessage(response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    p.dismiss();
                    FBUtils.showErrorAlert((Activity) getContext(), error);
                }
            }
        });
    }


    public void alertmessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.logomain);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                subject.getText().clear();
                message.getText().clear();
                messageSpinner.setSelection(0);
                areaSpinner.setSelection(0);
                break;
            case btn_submit:
                if (checkValidation()) {
                    contactUs();
                    break;
                }
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String buttoncolor = FB_LY_MobileSettingService.sharedInstance().checkInButtonColor;
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            submit.setBackgroundColor(Color.parseColor(btncolor));

        }
    }


    public void getLoyaltyMessageType() {
        p.show();
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getLoyaltyMessageType(object, new FB_LY_UserService.FBLoyaltyMessageTypeCallback() {
            public void onLoyaltyMessageTypeCallback(JSONObject response, Exception error) {
                try {
                    if (response != null && error == null) {
                        if (!response.has("loyaltyMessageType"))
                            return;

                        JSONArray getMessageArray = response.getJSONArray("loyaltyMessageType");
                        if (getMessageArray != null) {

                            for (int i = 0; i < getMessageArray.length(); i++) {
                                JSONObject myAreaObj = getMessageArray.getJSONObject(i);
                                MessageType messageListObj = new MessageType(myAreaObj);
                                list.add(messageListObj.getMessageType());
                                allMessageListfromServer.add(messageListObj);
                                allmessageoId.put(messageListObj.getMessageType(), messageListObj.getId());

                            }
                            p.dismiss();
                            adapter1.notifyDataSetChanged();
                        }
                    } else {
                        FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                        p.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    public void getLoyaltyAreaType() {
        p.show();
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getLoyaltyAreaType(object, new FB_LY_UserService.FBLoyaltyAreaTypeCallback() {
            public void onLoyaltyAreaTypeCallback(JSONObject response, Exception error) {
                try {
                    if (response != null && error == null) {
                        if (!response.has("loyaltyAreaType"))
                            return;

                        JSONArray getAreaArray = response.getJSONArray("loyaltyAreaType");
                        if (getAreaArray != null) {

                            for (int i = 0; i < getAreaArray.length(); i++) {
                                JSONObject myAreaObj = getAreaArray.getJSONObject(i);
                                AreaType areaListObj = new AreaType(myAreaObj);
                                list1.add(areaListObj.getAreaType());
                                allAreaListfromServer.add(areaListObj);
                                allareaToId.put(areaListObj.getAreaType(), areaListObj.getId());
                            }
                            p.dismiss();
                            adapter2.notifyDataSetChanged();
                        }
                    } else {
                        FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                        p.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        });
    }


    public boolean checkValidation() {
        if (!FBUtils.isValidString(subject.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Message Subject is Empty ");
            return false;
        }
        if (!FBUtils.isValidString(messageSpinner.getSelectedItem().toString()) || messageSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Option")) {
            FBUtils.showAlert(getActivity(), "Message Type is Empty ");
            return false;
        }
        if (!FBUtils.isValidString(areaSpinner.getSelectedItem().toString()) || areaSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Option")) {
            FBUtils.showAlert(getActivity(), "Area Type is Empty ");
            return false;
        }
        if (!FBUtils.isValidString(message.getText().toString())) {
            FBUtils.showAlert(getActivity(), "Message is Empty");
            return false;
        }

        return true;
    }


}