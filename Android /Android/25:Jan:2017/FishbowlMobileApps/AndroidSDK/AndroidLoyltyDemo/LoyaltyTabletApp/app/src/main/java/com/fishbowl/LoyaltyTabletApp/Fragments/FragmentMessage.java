package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.Adapters.MyMessageAdapter;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Interfaces.LoyaltyActivitySummaryCallback;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyActivityList;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyActivityListItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyMessages;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services.ActivityService;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.LoyaltyActivityList.getLoyaltyList;

/**
 * Created by schaudhary_ic on 10-Nov-16.
 */

public class FragmentMessage extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static Spinner spinner1;
    public ArrayList<LoyaltyMessages> myList = new ArrayList<LoyaltyMessages>();
    ListView messageList;
    Context context = getActivity();
    ArrayList<LoyaltyActivityListItem> mylist;
    Button submit;
    LoyaltyMessages messageListObj;
    ArrayAdapter<String> dataAdapter;
    HashSet<Integer> checkedValue = new HashSet<Integer>();
    MyMessageAdapter messageadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        messageList = (ListView) v.findViewById(R.id.messageList);
        //    p = new ProgressBarHandler(getActivity());
        submit = (Button) v.findViewById(R.id.btn_apply);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showalertdelete();

            }
        });
        getLoyaltyMessages();

        if (myList != null) {

            messageadapter = new MyMessageAdapter(getContext(), myList);
            messageList.setAdapter(messageadapter);
            messageList.setOnItemClickListener(this);
        }


        spinner1 = (Spinner) v.findViewById(R.id.spinner1);
        final List<String> categories = new ArrayList<String>();

        categories.add("Mark as Read");
        categories.add("Mark as UnRead");
        categories.add(0, "More");
        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories) {

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
                    // Set the hint text color gray
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(dataAdapter);
        return v;
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        String generalButtonBGColorSelected =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonBGColorNormal");
        if (generalButtonBGColorSelected != null) {

            submit.setBackgroundColor(Color.parseColor(generalButtonBGColorSelected));


        }



    }

    @Override
    public void onResume() {
        super.onResume();
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                //    TextView tv = (TextView) selectedItemView;
                String label = parentView.getItemAtPosition(position).toString();
                if (label.equalsIgnoreCase("Mark as Read")) {
                    getLoyaltyMarkMessageStatus();
                    //  tv.setBackgroundColor(Color.parseColor("#FFC9A3FF"));
                    TextView selectedText = (TextView) parentView.getChildAt(0);
                    if (selectedText != null) {
                        selectedText.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                    }
                } else if (label.equalsIgnoreCase("Mark as UnRead")) {
                    getLoyaltyUnMarkedMessageStatus();
                    TextView selectedText = (TextView) parentView.getChildAt(0);
                    if (selectedText != null) {
                        selectedText.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    public void getLoyaltyMessages() {
        enableScreen(false);
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getLoyaltyMessage(object, new FB_LY_UserService.FBLoyaltyMessageCallback() {
            public void onLoyaltyMessageCallback(JSONObject response, Exception error) {
                try {
                    if (response != null && error == null) {
                        if (!response.has("loyaltyCutomerMessageArray"))
                            return;

                        if (response.has("successFlag")) {

                            if (response.getString("successFlag").equalsIgnoreCase("true")) {

                                JSONArray getMessageArray = response.getJSONArray("loyaltyCutomerMessageArray");
                                if (getMessageArray != null) {

                                    for (int i = 0; i < getMessageArray.length(); i++) {
                                        JSONObject myAreaObj = getMessageArray.getJSONObject(i);
                                        messageListObj = new LoyaltyMessages(myAreaObj);
                                        myList.add(messageListObj);
                                        messageadapter.notifyDataSetChanged();

                                    }
                                    messageadapter.notifyDataSetChanged();

                                    enableScreen(true);
                                } else {
                                    enableScreen(true);
                                }
                            } else {
                                enableScreen(true);
                                messageadapter.notifyDataSetChanged();
                            }


                        }
                    } else {
                        FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);

                        enableScreen(true);
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    public void getloyaltyaMessagesStatus() {
        String area = spinner1.getSelectedItem().toString();
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        arr.put("value1");
        arr.put("value2");
        try {
            obj.put("areaType", area);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActivityService.getActivity(getActivity(), obj, new LoyaltyActivitySummaryCallback() {


            @Override
            public void onLoyaltyActivitySummaryCallback(LoyaltyActivityList rewardSummary, Exception error) {

                //   p.show();

                if (rewardSummary != null) {
                    if (getLoyaltyList() != null) {
                        if (getLoyaltyList().size() > 0) {
                            mylist = LoyaltyActivityList.getLoyaltyList();
                            messageadapter.notifyDataSetChanged();
                        } else {

                            mylist.clear();
                            messageadapter.notifyDataSetChanged();


                        }
                    } else {
                        mylist.clear();

                    }

                } else {

                    FBUtils.tryHandleTokenExpiry(getActivity(), error);
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView arg0, View v, int position, long arg3) {
        // TODO Auto-generated method stub
        CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
        cb.performClick();
        if (cb.isChecked()) {
            LoyaltyMessages loyaltyitem = myList.get(position);
            loyaltyitem.setRead(true);
            checkedValue.add(loyaltyitem.getId());
        } else if (!cb.isChecked()) {
            LoyaltyMessages loyaltyitem = myList.get(position);
            loyaltyitem.setRead(false);
            checkedValue.remove(loyaltyitem.getId());

        }

    }

    public void getLoyaltyDeleteMessageStatus() {
        if (checkedValue != null) {
            if (checkedValue.size() > 0) {
                enableScreen(false);
                JSONObject obj = new JSONObject();

                JSONArray jsArray = new JSONArray(checkedValue);

                FB_LY_UserService.getLoyaltyMessageStatus(obj, jsArray, new FB_LY_UserService.FBLoyaltyMessageCallback() {


                    @Override
                    public void onLoyaltyMessageCallback(JSONObject response, Exception error) {

                        if (response != null) {
                            enableScreen(true);
                            myList.clear();
                            getLoyaltyMessages();
                        } else {
                            enableScreen(true);
                        }
                    }
                });
            }
        }
    }

    public void getLoyaltyMarkMessageStatus() {
        if (checkedValue != null) {
            if (checkedValue.size() > 0) {
                enableScreen(false);
                JSONObject obj = new JSONObject();
                JSONArray jsArray = new JSONArray(checkedValue);

                FB_LY_UserService.getLoyaltyMarkMessageStatus(obj, jsArray, new FB_LY_UserService.FBLoyaltyMessageCallback() {


                    @Override
                    public void onLoyaltyMessageCallback(JSONObject response, Exception error) {

                        if (response != null) {

                            enableScreen(true);
                            myList.clear();
                            checkedValue.clear();
                            getLoyaltyMessages();
                        } else {

                            enableScreen(true);
                        }
                    }
                });
            }
        }
    }


    public void getLoyaltyUnMarkedMessageStatus() {

        if (checkedValue != null) {
            if (checkedValue.size() > 0) {
                enableScreen(false);
                JSONObject obj = new JSONObject();

                JSONArray jsArray = new JSONArray(checkedValue);

                FB_LY_UserService.getLoyaltyUnMarkMessageStatus(obj, jsArray, new FB_LY_UserService.FBLoyaltyMessageCallback() {

                    @Override
                    public void onLoyaltyMessageCallback(JSONObject response, Exception error) {

                        if (response != null) {

                            //          p.hide();
                            enableScreen(true);
                            myList.clear();
                            checkedValue.clear();
                            getLoyaltyMessages();
                        } else {
                            //        p.hide();
                            enableScreen(true);
                        }
                    }
                });
            }
        }
    }

    public void enableScreen(boolean isEnabled) {
        RelativeLayout screenDisableView = (RelativeLayout) getActivity().findViewById(R.id.screenDisableView);
        if (screenDisableView != null) {
            if (!isEnabled) {
                screenDisableView.setVisibility(View.VISIBLE);
            } else {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }

    public void showalertdelete() {
        if (checkedValue != null) {
            if (checkedValue.size() > 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setMessage("Are you sure you want to delete message?");
                alertDialog.setIcon(R.drawable.logomain);
                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getLoyaltyDeleteMessageStatus();
                    }
                });
                alertDialog.show();
            }
        }
    }

}








