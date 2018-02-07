package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ProfileMenu.UpdateProfile;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.State.StateActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Store.SearchStoreActivity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.States;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtility;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Models.FBThemeRegistrationField;
import com.fishbowl.loyaltymodule.Models.Member;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.fishbowl.LoyaltyTabletApp.Fragments.FragmentThemeSignUpDynamic.PICK_STORE_REQUEST;
import static com.fishbowl.LoyaltyTabletApp.Fragments.FragmentThemeSignUpDynamic.PICK_State_REQUEST;


/**
 * Created by schaudhary_ic on 24-Nov-16.
 */

public class FragmentUpdateProfileThemeDynamicFields extends Fragment {
    public static String statecode;
    public EditText FirstName;
    public EditText LastName;
    public EditText Address;
    public EditText ZipCode;
    public EditText FavoriteStore;
    public EditText City;
    public RadioGroup Gender;
    public EditText EmailAddress;
    public CheckBox SMSOptIn;
    public CheckBox PushOptin;
    public CheckBox EmailOptIn;
    public EditText DOB;
    public EditText State;
    public EditText PhoneNumber;
    public EditText CustomField;
    ProgressBarHandler progressBarHandler;
    LinearLayout scrollLinearLayout;
    Member member = null;
    Button bt_save, bt_edit;
    com.fishbowl.loyaltymodule.Models.FBStoresItem selectedStore;
    LinearLayout logout_layout;
    Button registration_button;
    TextView registxt_name;
    String StateName;
    public static Spinner messageSpinner;
    public CheckBox customeFieldRadio;
    ArrayAdapter<String> adapter1;
    List<EditText> allEds = new ArrayList<EditText>();
    List<Spinner> allspns = new ArrayList<Spinner>();
    List<CheckBox> allchecks = new ArrayList<CheckBox>();
    List<EditText> alldps = new ArrayList<EditText>();
    public EditText CustomDate;
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener customlistenerdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updatecustomLabel();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_profile_new, container, false);
        progressBarHandler = new ProgressBarHandler(getContext());
        registration_button = (Button) v.findViewById(R.id.registration_start_okbutton);
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMember();
            }
        });
        registxt_name = (TextView) v.findViewById(R.id.registxt_name);
        registxt_name.setText("My Profile");
        scrollLinearLayout = (LinearLayout) v.findViewById(R.id.scrollLinearLayout);
        member = FB_LY_UserService.sharedInstance().member;
        bt_edit = (Button) v.findViewById(R.id.bt_edit);
        bt_save = (Button) v.findViewById(R.id.bt_save);
        configureField();
        return v;

    }
    private void
    updatecustomLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        CustomDate.setText(sdf.format(myCalendar.getTime()));


    }


    @Override
    public void onResume() {
        super.onResume();


        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<FBThemeRegistrationField> registerFields = FBThemeMobileSettingsService.sharedInstance().registerFields;


                for (FBThemeRegistrationField registerfield : registerFields) {

                    if (registerfield.name.equalsIgnoreCase("FirstName")) {
                        if (StringUtilities.isValidString(member.firstName)) {
                            FirstName.setFocusableInTouchMode(true);
                            FirstName.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                            FirstName.requestFocus();
                        }
                    }
                    else if (registerfield.name.equalsIgnoreCase("LastName")) {

                        if (StringUtilities.isValidString(member.lastName)) {

                            LastName.setFocusableInTouchMode(true);
                            LastName.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                            LastName.requestFocus();
                        }
                    }
                    else if (registerfield.name.equalsIgnoreCase("City")) {
                        if (StringUtilities.isValidString(member.addressCity)) {

                            City.setFocusableInTouchMode(true);
                            City.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                            City.requestFocus();
                        }
                    }
                    else if (registerfield.name.equalsIgnoreCase("ZipCode")) {
                        if (StringUtilities.isValidString(member.addressZip)) {

                            ZipCode.setFocusableInTouchMode(true);
                            ZipCode.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                            ZipCode.requestFocus();
                            ZipCode.setEnabled(true);

                        }
                    }
                    else if (registerfield.name.equalsIgnoreCase("Address")) {
                        if (StringUtilities.isValidString(member.addressLine1)) {

                            Address.setFocusableInTouchMode(true);
                            Address.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                            Address.requestFocus();
                        }
                    }
                    else if (registerfield.name.equalsIgnoreCase("State")) {
                        if (StringUtilities.isValidString(member.addressState)) {

                            State.setFocusableInTouchMode(false);
                            State.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                            State.requestFocus();
                            State.setEnabled(true);
                            State.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getContext(), StateActivity.class);
                                    startActivityForResult(i, PICK_State_REQUEST);
                                }
                            });

                        }
                    }
                    else if (registerfield.name.equalsIgnoreCase("Gender")) {
                        if (StringUtilities.isValidString(member.customerGender)) {

                            int radioButtonID = Gender.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
                            for (int i = 0; i < Gender.getChildCount(); i++) {
                                Gender.getChildAt(i).setEnabled(true);

                            }

                        }}
                    else if (registerfield.name.equalsIgnoreCase("FavoriteStore")) {
                        if (StringUtilities.isValidString(member.homeStoreID)) {


                            FavoriteStore.setFocusableInTouchMode(true);
                            FavoriteStore.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                            FavoriteStore.requestFocus();
                            FavoriteStore.setEnabled(true);
                            FavoriteStore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getContext(), SearchStoreActivity.class);
                                    startActivityForResult(i, PICK_STORE_REQUEST);
                                }
                            });

                        }
                    }

                    else if (registerfield.name.equalsIgnoreCase("EmailOptIn")) {
                        if (StringUtilities.isValidString(member.emailOpted)) {
                            EmailOptIn.setEnabled(true);
                        }

                    }
                    else if (registerfield.name.equalsIgnoreCase("SMSOptIn")) {
                        if (StringUtilities.isValidString(member.smsOpted)) {
                            SMSOptIn.setEnabled(true);
                        }
                    } else if (registerfield.name.equalsIgnoreCase("PushOptin")) {
                        if (StringUtilities.isValidString(member.pushOpted)) {
                            PushOptin.setEnabled(true);
                        }
                    }
                    else {
                        if (registerfield.customField) {
                            if (allEds.size() > 0) {
                                String[] items = new String[allEds.size()];
                                for (int i = 0; i < allEds.size(); i++) {
                                    CustomField = allEds.get(i);
                                    if (CustomField.getTag().toString().equalsIgnoreCase(registerfield.databaseName)) {
                                        CustomField.setFocusableInTouchMode(true);
                                        CustomField.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                                        CustomField.requestFocus();
                                        CustomField.setEnabled(true);
                                    }
                                }
                            }

                            if (allspns.size() > 0) {
                                String[] items = new String[allspns.size()];
                                for (int i = 0; i < allspns.size(); i++) {
                                    messageSpinner = allspns.get(i);
                                    if (messageSpinner.getTag().toString().equalsIgnoreCase(registerfield.databaseName)) {


                                        messageSpinner.setFocusableInTouchMode(true);
                                        messageSpinner.requestFocus();
                                        messageSpinner.setEnabled(true);


                                        adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, registerfield.optionListdisplayname) {
//                                            @Override
//                                            public boolean isEnabled(int position) {
//                                                if (position == 0) {
//                                                    return false;
//                                                } else {
//                                                    return true;
//                                                }
//                                            }

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
                                    }
                                }
                            }
                            if (allchecks.size() > 0) {
                                Boolean [] items=new Boolean[allchecks.size()];
                                for (int i = 0; i < allchecks.size(); i++) {

                                    customeFieldRadio = allchecks.get(i);
                                    if (customeFieldRadio.getTag().toString().equalsIgnoreCase(registerfield.databaseName)) {
                                        customeFieldRadio.setFocusableInTouchMode(true);
                                        customeFieldRadio.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                                        customeFieldRadio.requestFocus();
                                        customeFieldRadio.setEnabled(true);
                                    }
                                }
                            }
                            if(alldps.size() >0)
                            {
                                String [] items=new String[alldps.size()];
                                for (int i = 0; i < alldps.size(); i++)
                                {
                                    final EditText  CustomDate = alldps.get(i);
                                    if(CustomDate.getTag().toString().equalsIgnoreCase(registerfield.databaseName))
                                    {

                                        CustomDate.setFocusableInTouchMode(false);
                                        CustomDate.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                                        //CustomDate.requestFocus();
                                        CustomDate.setEnabled(true);
                                        CustomDate.setFocusable(false);

                                        CustomDate.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {

                                                // TODO Auto-generated method stub
                                                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                                                    @Override
                                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                        myCalendar.set(Calendar.YEAR, year);
                                                        myCalendar.set(Calendar.MONTH, monthOfYear);
                                                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                                                        String myFormat = "MM/dd/yyyy"; //In which you need put here
                                                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                                        CustomDate.setText(sdf.format(myCalendar.getTime()));
                                                    }

                                                }, myCalendar
                                                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                                        myCalendar.get(Calendar.DAY_OF_MONTH));

                                                mDatePicker.getDatePicker().setMaxDate((System.currentTimeMillis()));
                                                mDatePicker.show();


                                            }
                                        });
                                    }
                                }
                            }

                        }
                    }


                    bt_edit.setVisibility(View.GONE);
                    bt_save.setVisibility(View.VISIBLE);
                    registxt_name.setText("Update Profile");

                }
            } });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (checkValidation()) {
                        updateMember();

                        ArrayList<FBThemeRegistrationField> registerFields = FBThemeMobileSettingsService.sharedInstance().registerFields;


                        for (FBThemeRegistrationField registerfield : registerFields) {
                            if (registerfield.name.equalsIgnoreCase("FirstName")) {
                                if (StringUtilities.isValidString(member.firstName)) {
                                    FirstName.setFocusable(false);
                                    FirstName.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                }
                            }
                            else if (registerfield.name.equalsIgnoreCase("LastName")) {
                                if (StringUtilities.isValidString(member.lastName)) {
                                    LastName.setFocusable(false);
                                    LastName.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                }
                            }
                            else if (registerfield.name.equalsIgnoreCase("City")) {
                                if (StringUtilities.isValidString(member.addressCity)) {
                                    City.setFocusable(false);
                                    City.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                }
                            }
                            else if (registerfield.name.equalsIgnoreCase("ZipCode"))
                            {
                                if (StringUtilities.isValidString(member.addressZip)) {
                                    ZipCode.setFocusableInTouchMode(false);
                                    ZipCode.setEnabled(false);
                                    ZipCode.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));


                                }
                            }
                            else if (registerfield.name.equalsIgnoreCase("Address")) {
                                if (StringUtilities.isValidString(member.addressLine1)) {

                                    Address.setFocusableInTouchMode(false);
                                    Address.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                    Address.requestFocus();
                                }
                            }
                            else if (registerfield.name.equalsIgnoreCase("State")) {
                                if (StringUtilities.isValidString(member.addressState)) {
                                    State.setFocusableInTouchMode(false);
                                    State.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                    State.requestFocus();
                                    State.setEnabled(false);
                                }}
                            else if (registerfield.name.equalsIgnoreCase("FavoriteStore")) {
                                if (StringUtilities.isValidString(member.homeStoreID)) {

                                    FavoriteStore.setFocusableInTouchMode(false);
                                    FavoriteStore.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                    FavoriteStore.setFocusable(false);
                                    FavoriteStore.setEnabled(false);


                                }}

                            else if (registerfield.name.equalsIgnoreCase("Gender")) {
                                for (int i = 0; i < Gender.getChildCount(); i++) {
                                    Gender.getChildAt(i).setEnabled(false);
                                }
                            }
                            else if (registerfield.name.equalsIgnoreCase("EmailOptIn")) {
                                if (StringUtilities.isValidString(member.emailOpted)) {
                                    EmailOptIn.setEnabled(false);
                                }
                            }
                            else if (registerfield.name.equalsIgnoreCase("SMSOptIn")) {
                                if (StringUtilities.isValidString(member.smsOpted)) {
                                    SMSOptIn.setEnabled(false);
                                }
                            } else if (registerfield.name.equalsIgnoreCase("PushOptin")) {
                                if (StringUtilities.isValidString(member.pushOpted)) {
                                    PushOptin.setEnabled(false);
                                }
                            }

                            else  if (registerfield.customField ) {
                                if (allEds.size() > 0) {
                                    String[] items = new String[allEds.size()];
                                    for (int i = 0; i < allEds.size(); i++) {
                                        CustomField = allEds.get(i);
                                        if (CustomField.getTag().toString().equalsIgnoreCase(registerfield.databaseName)) {
                                            CustomField.setFocusableInTouchMode(false);
                                            CustomField.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                            CustomField.requestFocus();
                                            CustomField.setEnabled(false);
                                        }
                                    }
                                }

                                if(allspns.size() >0)
                                {
                                    String [] items=new String[allspns.size()];
                                    for (int i = 0; i < allspns.size(); i++)
                                    {
                                        messageSpinner = allspns.get(i);
                                        if(messageSpinner.getTag().toString().equalsIgnoreCase(registerfield.databaseName))
                                        {
                                            messageSpinner.setFocusableInTouchMode(false);
                                            messageSpinner.requestFocus();
                                            messageSpinner.setEnabled(false);
                                        }
                                    }
                                }

                                if (allchecks.size() > 0) {
                                    Boolean [] items=new Boolean[allchecks.size()];
                                    for (int i = 0; i < allchecks.size(); i++) {

                                        customeFieldRadio = allchecks.get(i);
                                        if (customeFieldRadio.getTag().toString().equalsIgnoreCase(registerfield.databaseName)) {
                                            customeFieldRadio.setFocusableInTouchMode(false);
                                            customeFieldRadio.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                            customeFieldRadio.requestFocus();
                                            customeFieldRadio.setEnabled(false);
                                        }
                                    }
                                }
                                if(alldps.size() >0)
                                {
                                    String [] items=new String[alldps.size()];
                                    for (int i = 0; i < alldps.size(); i++)
                                    {
                                        CustomDate = alldps.get(i);
                                        if(CustomDate.getTag().toString().equalsIgnoreCase(registerfield.databaseName))
                                        {

                                            CustomDate.setFocusableInTouchMode(false);
                                            CustomDate.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                                            CustomDate.requestFocus();
                                            CustomDate.setEnabled(false);
                                        }
                                    }
                                }


                            }

                            bt_save.setVisibility(View.GONE);
                            bt_edit.setVisibility(View.VISIBLE);
                            registxt_name.setText("My Profile");
                        }}
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void configureField() {

        ArrayList<FBThemeRegistrationField> registerFields = FBThemeMobileSettingsService.sharedInstance().registerFields;


        for (FBThemeRegistrationField registerfield : registerFields) {

            if (registerfield.name.equalsIgnoreCase("FirstName")) {
                if (StringUtilities.isValidString(member.firstName)) {
                    creatTextField("firstName", registerfield.displayName, registerfield);

                }
            } else if (registerfield.name.equalsIgnoreCase("LastName")) {
                if (StringUtilities.isValidString(member.lastName)) {
                    creatTextField("lastName", registerfield.displayName, registerfield);
                }
            } else if (registerfield.name.equalsIgnoreCase("ZipCode")) {
                if (StringUtilities.isValidString(member.addressZip)) {
                    creatTextField("addressZip", registerfield.displayName, registerfield);

                }
            }
            else if (registerfield.name.equalsIgnoreCase("City")) {

                if (StringUtilities.isValidString(member.addressCity)) {
                    creatTextField("addressCity", registerfield.displayName, registerfield);
                }
            } else if (registerfield.name.equalsIgnoreCase("EmailAddress")) {
                if (StringUtilities.isValidString(member.emailID)) {
                    creatTextField("emailID", registerfield.displayName, registerfield);
                }
            } else if (registerfield.name.equalsIgnoreCase("DOB")) {
                if (StringUtilities.isValidString(member.dateOfBirth)) {
                    creatTextField("dateOfBirth", registerfield.displayName, registerfield);
                }
            } else if (registerfield.name.equalsIgnoreCase("Address")) {
                if (StringUtilities.isValidString(member.addressLine1)) {
                    creatTextField("address", registerfield.displayName, registerfield);
                }
            }
            else if (registerfield.name.equalsIgnoreCase("State")) {
                if (StringUtilities.isValidString(member.addressState)) {
                    creatTextField("addressState", registerfield.displayName, registerfield);
                }
            }
            else if (registerfield.name.equalsIgnoreCase("FavoriteStore")) {
                if (StringUtilities.isValidString(member.homeStoreID)) {
                    creatTextField("homeStoreID", registerfield.displayName, registerfield);
                }
            } else if (registerfield.name.equalsIgnoreCase("Gender")) {
                if (StringUtilities.isValidString(member.customerGender)) {
                    creatRadioGroup(member.customerGender);
                }
            } else if (registerfield.name.equalsIgnoreCase("PhoneNumber")) {

                if (StringUtilities.isValidString(member.cellPhone)) {
                    final EditText editText = creatTextField("cellPhone", registerfield.displayName, registerfield);


                    editText.addTextChangedListener(new TextWatcher() {
                        private static final char space = ' ';

                        public void afterTextChanged(Editable s) {
                            PhoneNumber.setSelection(PhoneNumber.getText().length());
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            FBUtils.setUsDashPhone(s, before, PhoneNumber);
                        }
                    });

                }
            } else if (registerfield.name.equalsIgnoreCase("EmailOptIn")) {
                if (StringUtilities.isValidString(member.emailOpted)) {
                    creatRadioButton("Opt-in to receive VIP email offers",registerfield.displayName,registerfield);
                }
            } else if (registerfield.name.equalsIgnoreCase("SMSOptIn")) {
                if (StringUtilities.isValidString(member.smsOpted)) {
                    creatRadioButton("SMSOptIn",registerfield.displayName,registerfield);
                }
            } else if (registerfield.name.equalsIgnoreCase("PushOptin")) {
                if (StringUtilities.isValidString(member.pushOpted)) {
                    creatRadioButton("PushOptin",registerfield.displayName,registerfield);
                }
            } else if (registerfield.customField) {

                if (StringUtilities.isValidString(registerfield.profileFieldType)) {
                    if (registerfield.profileFieldType.equalsIgnoreCase("text")) {
                        creatTextField("customeField", registerfield.displayName, registerfield);
                    } else if (registerfield.profileFieldType.equalsIgnoreCase("textarea")) {
                        creatTextField("customeField", registerfield.displayName, registerfield);
                    } else if (registerfield.profileFieldType.equalsIgnoreCase("legend")) {
                        creatTextField("customeField", registerfield.displayName, registerfield);
                    } else if (registerfield.profileFieldType.equalsIgnoreCase("dropdown")) {
                        creatDropDown("customeField", registerfield.displayName, new ArrayList<String>(), registerfield);
                    }
                    else if (registerfield.profileFieldType.equalsIgnoreCase("checkbox")) {
                        creatRadioButton("customeFieldRadio", registerfield.displayName,registerfield);
                    }
                    else if (registerfield.profileFieldType.equalsIgnoreCase("date")) {
                        creatCustomTimePicker("customeFieldDatePicker", registerfield.displayName,registerfield);
                    }
                }
            }

        }

    }

    public void creatCustomTimePicker(String name,String desc,FBThemeRegistrationField registerfield) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.update_customdatepicker, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
        view.setLayoutParams(layoutParams);

        EditText editText = (EditText) view.findViewById(R.id.editText);
        TextView textview = (TextView) view.findViewById(R.id.field_text);
        // editText.setHint(desc);
        textview.setHint(desc);
        CustomDate = editText;
        CustomDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        CustomDate.setFocusable(false);
        CustomDate.setTag(registerfield.databaseName);
        alldps.add(CustomDate);
        CustomDate.setText(member.map.get(registerfield.databaseName.toLowerCase()));

//        editText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                // TODO Auto-generated method stub
//                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), customlistenerdate, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                mDatePicker.getDatePicker().setMaxDate((System.currentTimeMillis()));
//                mDatePicker.show();
//
//
//            }
//        });




        scrollLinearLayout.addView(view);
    }

    public void creatRadioGroup(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.radio_buttons, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
        view.setLayoutParams(layoutParams);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        RadioButton radioMale = (RadioButton) view.findViewById(R.id.maleRadio);
        RadioButton radiofemale = (RadioButton) view.findViewById(R.id.femaleRadio);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("", "");
            }
        });


        TextView editText = (TextView) view.findViewById(R.id.textView);
        editText.setText(name);

        if (name.equals("M")) {
            Gender = radioGroup;
            editText.setText("Gender");
            radioGroup.check(radioMale.getId());

        } else if (name.equals("F")) {
            Gender = radioGroup;
            editText.setText("Gender");
            radioGroup.check(radiofemale.getId());

        }
        for (int i = 0; i < Gender.getChildCount(); i++) {
            Gender.getChildAt(i).setEnabled(false);
        }

        scrollLinearLayout.addView(view);
    }

    public void creatRadioButton(String name,String desc,FBThemeRegistrationField registerfield) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.checkbox_buttons, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
        view.setLayoutParams(layoutParams);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);


        if (name.equals("SMSOptIn")) {
            SMSOptIn = checkBox;
            SMSOptIn.setChecked(Boolean.valueOf(member.smsOpted));
            SMSOptIn.setEnabled(false);

        }

        if (name.equals("Opt-in to receive VIP email offers")) {
            EmailOptIn = checkBox;
            EmailOptIn.setChecked(Boolean.valueOf(member.emailOpted));
            EmailOptIn.setEnabled(false);
        }

        if (name.equals("PushOptin")) {
            PushOptin = checkBox;
            PushOptin.setChecked(Boolean.valueOf(member.pushOpted));
            PushOptin.setEnabled(false);

        }
        if (name.equals("customeFieldRadio")) {
            customeFieldRadio = checkBox;
            allchecks.add(customeFieldRadio);
            customeFieldRadio.setTag(registerfield.databaseName);
            customeFieldRadio.setChecked(Boolean.valueOf(member.map.get(registerfield.databaseName.toLowerCase())));
            customeFieldRadio.setEnabled(false);
            //    textview.setText(desc);
        }


        TextView editText = (TextView) view.findViewById(R.id.textView);
        editText.setText(desc);




        scrollLinearLayout.addView(view);
    }

    public EditText creatTextField(String name, String desc, FBThemeRegistrationField registerfield) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.edittext_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
        view.setLayoutParams(layoutParams);


        EditText editText = (EditText) view.findViewById(R.id.editText);
        TextView textview = (TextView) view.findViewById(R.id.field_text);


        if (name.equals("firstName")) {


            textview.setText(desc);
            editText.setTag(member.firstName);
            editText.setText(member.firstName);
            FirstName = editText;
            FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
            FirstName.setMaxLines(1);
            FirstName.setFocusable(false);

            //imageView.setBackgroundResource(R.drawable.logo);
        }
        if (name.equals("lastName")) {
            textview.setText(desc);
            editText.setTag(member.lastName);
            editText.setText(member.lastName);
            LastName = editText;
            LastName.setInputType(InputType.TYPE_CLASS_TEXT);
            LastName.setMaxLines(1);
            LastName.setFocusable(false);

        }

        if (name.equals("addressCity")) {
            textview.setText(desc);
            editText.setTag(member.addressCity);
            editText.setText(member.addressCity);
            City = editText;
            City.setInputType(InputType.TYPE_CLASS_TEXT);
            City.setMaxLines(1);
            City.setFocusable(false);

        }
        if (name.equals("emailID")) {
            textview.setText(desc);
            editText.setTag(member.emailID);
            editText.setText(member.emailID);
            EmailAddress = editText;
            EmailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EmailAddress.setMaxLines(1);
            EmailAddress.setFocusable(false);
        }


        if (name.equals("cellPhone")) {

            textview.setText(desc);
            editText.setTag(member.cellPhone);
            editText.setText(member.cellPhone);
            PhoneNumber = editText;
            PhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            PhoneNumber.setMaxLines(1);
            PhoneNumber.setEnabled(false);


        }
        if (name.equals("addressZip")) {
            textview.setText(desc);
            editText.setTag(member.addressZip);
            editText.setText(member.addressZip);
            ZipCode = editText;
            ZipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
            ZipCode.setMaxLines(1);
            ZipCode.setFocusable(false);
            ZipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        }


        if (name.equals("homeStoreID")) {
            textview.setText(desc);
            editText.setTag(member.homeStoreID);
            editText.setText(member.homeStoreID);
            FavoriteStore = editText;
            FavoriteStore.setInputType(InputType.TYPE_CLASS_TEXT);
            FavoriteStore.setMaxLines(1);
            FavoriteStore.setFocusable(false);

            selectedStore = FB_LY_UserService.sharedInstance().mapIdToStore.get(Integer.valueOf(member.homeStoreID));
            if(selectedStore!=null)
            {
                if(StringUtilities.isValidString(selectedStore.getStoreName()))
                    FavoriteStore.setText(selectedStore.getStoreName());
            }
        }


        if (name.equals("dateOfBirth")) {
            if (member.dateOfBirth != null && FBUtils.isValidString(member.dateOfBirth)) {
                textview.setText(desc);

                try {
                    String currentDate = member.dateOfBirth;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date tempDate = simpleDateFormat.parse(currentDate);
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    //System.out.println("Output date is = "+outputDateFormat.format(tempDate));
                    String output = outputDateFormat.format(tempDate);
                    if (output != null)
                        editText.setText(output);
                    else
                        editText.setText(currentDate);
                } catch (ParseException ex) {
                    System.out.println("Parse Exception");
                }
                editText.setTag(member.dateOfBirth);

                DOB = editText;
                DOB.setInputType(InputType.TYPE_CLASS_DATETIME);
                DOB.setFocusable(false);
                DOB.setMaxLines(1);
                DOB.setFocusable(false);
            }

        }
        if (name.equals("address")) {
            textview.setText(desc);
            editText.setTag(member.addressLine1);
            editText.setText(member.addressLine1);
            Address = editText;
            Address.setInputType(InputType.TYPE_CLASS_TEXT);
            Address.setMaxLines(1);
            Address.setFocusable(false);

        }

        if (name.equals("addressState")) {

            textview.setText(desc);
            editText.setTag(member.addressState);
            editText.setText(member.addressState);
            State = editText;
            State.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            State.setMaxLines(1);
            State.setFocusable(false);
            State.setFocusable(false);


            StateName = FB_LY_UserService.sharedInstance().mapStateNumToId.get(member.addressState);
            if(StateName!=null)
            {
                if(StringUtilities.isValidString(StateName))
                    State.setText(StateName);
            }
        }

        if (name.equals("customeField")) {
            textview.setText(desc);
            editText.setTag(registerfield.databaseName);
            editText.setText(member.map.get(registerfield.databaseName.toLowerCase()));
            CustomField = editText;
            CustomField.setInputType(InputType.TYPE_CLASS_TEXT);
            CustomField.setMaxLines(1);
            CustomField.setFocusable(false);
            allEds.add(CustomField);
        }

        scrollLinearLayout.addView(view);

        return editText;
    }

    public void updateMember() {

        enableScreen(false);
        JSONObject object = new JSONObject();
        ArrayList<FBThemeRegistrationField> registerFields = FBThemeMobileSettingsService.sharedInstance().registerFields;


        for (FBThemeRegistrationField registerfield : registerFields) {

            try {
                if (registerfield.name.equalsIgnoreCase("FirstName")) {
                    if (StringUtilities.isValidString(member.firstName))
                        object.put("firstName", FirstName.getText().toString());
                }
                else if (registerfield.name.equalsIgnoreCase("LastName")) {
                    if (StringUtilities.isValidString(member.lastName))
                        object.put("lastName", LastName.getText().toString());
                }
                else if (registerfield.name.equalsIgnoreCase("EmailAddress")) {
                    if (StringUtilities.isValidString(member.emailID))
                        object.put("email", EmailAddress.getText().toString());
                }
                else if (registerfield.name.equalsIgnoreCase("EmailOptIn")) {
                    if (StringUtilities.isValidString(member.emailOpted))
                        object.put("emailOptIn", EmailOptIn.isChecked());
                }
                else if (registerfield.name.equalsIgnoreCase("SMSOptIn")) {
                    if (StringUtilities.isValidString(member.smsOpted)) {
                        object.put("smsOptIn", SMSOptIn.isChecked());
                    }
                }
                else if (registerfield.name.equalsIgnoreCase("PushOptin")) {
                    if (StringUtilities.isValidString(member.pushOpted)) {
                        object.put("pushOptIn", PushOptin.isChecked());
                    }
                }

                else if (registerfield.name.equalsIgnoreCase("PhoneNumber")){

                    if (StringUtilities.isValidString(member.cellPhone))
                        object.put("phone", PhoneNumber.getText().toString());

                }
                else if (registerfield.name.equalsIgnoreCase("Address")) {
                    if (StringUtilities.isValidString(member.addressLine1))
                        object.put("addressStreet", Address.getText().toString());
                }

                else if (registerfield.name.equalsIgnoreCase("State")) {
                    if (StringUtilities.isValidString(member.addressState))
                        object.put("addressState", statecode);
                }
                else if (registerfield.name.equalsIgnoreCase("City")) {
                    if (StringUtilities.isValidString(member.addressCity))
                        object.put("addressCity", City.getText().toString());
                }
                else if (registerfield.name.equalsIgnoreCase("ZipCode")) {
                    if (StringUtilities.isValidString(member.addressZip))
                        object.put("addressZipCode", ZipCode.getText().toString());
                }
                else if (registerfield.name.equalsIgnoreCase("DOB")) {
                    if (StringUtilities.isValidString(member.dateOfBirth))
                        object.put("dob", DOB.getText().toString());
                }
                else if (registerfield.name.equalsIgnoreCase("Gender")) {
                    if (StringUtilities.isValidString(member.customerGender)) {
                        int radioButtonID = Gender.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
                        object.put("gender", radioButton.getText());

                    }
                }
                else if (registerfield.name.equalsIgnoreCase("FavoriteStore")) {
                    if (StringUtilities.isValidString(member.homeStoreID))
                        object.put("favoriteStore", selectedStore.getStoreNumber());
                }
                else  if (registerfield.customField ) {
                    if (allEds.size() >0) {
                        String [] items=new String[allEds.size()];
                        for (int i = 0; i < allEds.size(); i++)
                        {
                            EditText ed = allEds.get(i);
                            if(ed.getTag().toString().equalsIgnoreCase(registerfield.databaseName))
                            {
                                items[i]=allEds.get(i).getText().toString();
                                object.put(registerfield.databaseName.toLowerCase(),items[i]);
                            }
                        }
                    }
                    if(allspns.size() >0)
                    {
                        String [] items=new String[allEds.size()];
                        for (int i = 0; i < allspns.size(); i++)
                        {
                            Spinner sp = allspns.get(i);
                            if(sp.getTag().toString().equalsIgnoreCase(registerfield.databaseName))
                            {
                                items[i]=allspns.get(i).getSelectedItem().toString();
                                object.put(registerfield.databaseName.toLowerCase(),items[i]);
                            }
                        }
                    }


                    if (allchecks.size() > 0) {
                        Boolean [] items=new Boolean[allchecks.size()];
                        for (int i = 0; i < allchecks.size(); i++) {

                            customeFieldRadio = allchecks.get(i);
                            if (customeFieldRadio.getTag().toString().equalsIgnoreCase(registerfield.databaseName)) {
                                items[i]=allchecks.get(i).isChecked();
                                object.put(registerfield.databaseName.toLowerCase(),items[i]);
                            }
                        }
                    }
                    if(alldps.size() >0)
                    {
                        String [] items=new String[alldps.size()];
                        for (int i = 0; i < alldps.size(); i++)
                        {
                            CustomDate = alldps.get(i);
                            if(CustomDate.getTag().toString().equalsIgnoreCase(registerfield.databaseName))
                            {
                                items[i]=alldps.get(i).getText().toString();
                                object.put(registerfield.databaseName.toLowerCase(),items[i]);
                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FB_LY_UserService.sharedInstance().memberUpdate(object, new FB_LY_UserService.FBMemberUpdateCallback() {
            @Override
            public void onMemberUpdateCallback(JSONObject response, Exception error) {
                if (response != null) {
                    FBUtils.showAlert(getContext(), "Member Update Successfully");
                    //progressBarHandler.hide();
                    enableScreen(true);
                    getMember();
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    //progressBarHandler.hide();
                    enableScreen(true);
                }
            }
        });
    }

//    public boolean checkValidation() {
//        //  fname, lname, email, phone, address,password;
//        if (StringUtilities.isValidString(member.firstName)) {
//            if (!FBUtils.isValidString(FirstName.getText().toString())) {
//                FBUtils.showAlert(getActivity(), "Empty First Name");
//                return false;
//            }
//        }
//        if (StringUtilities.isValidString(member.lastName)) {
//            if (!FBUtils.isValidString(LastName.getText().toString())) {
//                FBUtils.showAlert(getActivity(), "Empty Last Name");
//                return false;
//            }
//        }
//
//
//        return true;
//    }


    public boolean checkValidation() throws java.text.ParseException {
        //  fname, lname, email, phone, address,password;
        ArrayList<FBThemeRegistrationField> registerFields = FBThemeMobileSettingsService.sharedInstance().registerFields;
        for (FBThemeRegistrationField registerfield : registerFields) {

            if (registerfield.required && registerfield.name.equalsIgnoreCase("FirstName")) {
                if (!FBUtils.isValidString(FirstName.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty First Name");
                    return false;
                }
            } else if (registerfield.required && registerfield.name.equalsIgnoreCase("EmailAddress")) {
                if (!FBUtils.isValidString(EmailAddress.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty EmailAddress");
                    return false;
                }
            } else if (registerfield.required && registerfield.name.equalsIgnoreCase("ZipCode")) {
                if (!FBUtils.isValidString(ZipCode.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty ZipCode");
                    return false;
                }
            } else if (registerfield.required && registerfield.name.equalsIgnoreCase("FavoriteStore")) {
                if (!FBUtils.isValidString(FavoriteStore.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty FavoriteStore");
                    return false;
                }
            } else if (registerfield.required && registerfield.name.equalsIgnoreCase("Address")) {
                if (!FBUtils.isValidString(Address.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty Address");
                    return false;
                }
            } else if (registerfield.required && registerfield.name.equalsIgnoreCase("LastName")) {
                if (!FBUtils.isValidString(LastName.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty Last Name");
                    return false;
                }
            } else if (registerfield.required && registerfield.name.equalsIgnoreCase("State")) {
                if (!FBUtils.isValidString(State.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty State");
                    return false;
                }
            } else if (registerfield.required && registerfield.name.equalsIgnoreCase("City")) {
                if (!FBUtils.isValidString(City.getText().toString())) {
                    FBUtils.showAlert(getActivity(), "Empty City");
                    return false;
                }
            }


     /*   if (FBUtils.isValidEmail(email.getText().toString()))
        {
            FBUtils.showAlert(getActivity(),"Invalid Email");
            return false;
        }*/
            else if (registerfield.required && registerfield.name.equalsIgnoreCase("PhoneNumber")) {
                if (!(FBUtils.isValidString(PhoneNumber.getText().toString()) || FBUtils.isValidPhoneNumber(PhoneNumber.getText().toString()))) {
                    FBUtils.showAlert(getActivity(), "Empty Phone Number");
                    return false;
                }
            }
//        if(!FBUtils.isValidString(Address.getText().toString()))
//        {
//            FBUtils.showAlert(getActivity(),"Empty Address");
//            return false;
//        }

            else if (registerfield.required && registerfield.name.equalsIgnoreCase("DOB")) {

                {
                    return CheckDOB();
                }

            }
        }

        return true;
    }

    public boolean CheckDOB() throws java.text.ParseException {

        if (FBUtils.isValidString(DOB.getText().toString())) {
            if (FBUtils.isValidDateFormat(DOB.getText().toString())) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date birthDate = sdf.parse(DOB.getText().toString());//Yeh !! It's my date of birth :-)
                try {
                    if (yearFormatCheck(birthDate) == true) {
                        int age = calculateAge(birthDate);
                        if (age >= 21) {
                            if (age > 100) {
                                FBUtils.showAlert(getActivity(), "Maximum age exceeded");
                                DOB.getText().clear();
                                return false;
                            }
                            return true;
                        } else {
                            FBUtils.showAlert(getActivity(), "Ineligible, Age not over 21");
                            DOB.getText().clear();
                            return false;
                        }
                    } else
                        FBUtils.showAlert(getActivity(), "Please provide all digits for birth date");
                    return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                FBUtils.showAlert(getActivity(), "Please enter a valid birth date mm/dd/yyyy");
                return false;
            }

        } else

        {
            FBUtils.showAlert(getActivity(), "Empty date of birth field");
            return false;

        }
        return false;
    }

    public boolean yearFormatCheck(Date birthdate) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date theDate = null;
        try {
            theDate = format.parse(DOB.getText().toString());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Calendar myCal = new GregorianCalendar();
        myCal.setTime(theDate);
        int year = myCal.get(Calendar.YEAR);
        int yyyy;
        try {
            yyyy = year;

            if (yyyy <= 0) {
                return false;
                //  throw new NumberFormatException("zero is an invalid year.");
            }
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(year + " is an invalid year.");
        }
        if (yyyy < 999) {
            return false;
            //throw new NumberFormatException("Please provide all digits for the year (" + year + ").");
        }
        return true;
    }

    private int calculateAge(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;
        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());
        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        //Get difference between months
        months = currMonth - birthMonth;
        //if month difference is in negative then reduce years by one and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }
        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return years;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        //signup button color
        //String buttoncolor =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
        String generalButtonBGColorNormal =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonBGColorNormal");
        if (generalButtonBGColorNormal != null) {
            bt_edit.setBackgroundColor(Color.parseColor(generalButtonBGColorNormal));

        }

        String generalButtonBGColorSelected =  FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonBGColorSelected");
        if (generalButtonBGColorSelected != null) {

            bt_save.setBackgroundColor(Color.parseColor(generalButtonBGColorSelected));


        }


    }

    public void creatDropDown(String name, String desc, ArrayList<String> optionListdisplayname, FBThemeRegistrationField registerfield) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.custom_dropdown, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, getContext()), 0, FBUtility.inDp(8, getContext()));
        view.setLayoutParams(layoutParams);

        TextView textview = (TextView) view.findViewById(R.id.field_text);

        messageSpinner = (Spinner) view.findViewById(R.id.signup_spinner);
        messageSpinner.setFocusable(false);
        messageSpinner.setTag(registerfield.databaseName);
        allspns.add(messageSpinner);
        textview.setText(desc);
        // messageSpinner.setTag(registerfield.databaseName);
        optionListdisplayname.add(member.map.get(registerfield.databaseName.toLowerCase()));

        adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, optionListdisplayname) {
//            @Override
//            public boolean isEnabled(int position) {
//                if (position == 0) {
//                    return false;
//                } else {
//                    return true;
//                }
//            }

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

        scrollLinearLayout.addView(view);
    }




    public void getMember() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getMember(object, new FB_LY_UserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    progressBarHandler.dismiss();
                    RelativeLayout toolbarTextView = (RelativeLayout) ((UpdateProfile) getActivity()).findViewById(R.id.tool_bar);
                    if (toolbarTextView != null) {
                        TextView title = (TextView) toolbarTextView.findViewById(R.id.title_textb);
                        String firstName = FB_LY_UserService.sharedInstance().member.firstName;
                        title.setText(firstName);

                    }
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) getContext(), error);
                    progressBarHandler.dismiss();
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_STORE_REQUEST) {
            if (resultCode == RESULT_OK) {

                selectedStore = (com.fishbowl.loyaltymodule.Models.FBStoresItem) data.getSerializableExtra("CLPSTORE");
                FavoriteStore.setText(selectedStore.getStoreName());

            }

        }
        if (requestCode == PICK_State_REQUEST) {
            if (resultCode == RESULT_OK) {

                String statename = (String) data.getSerializableExtra("State");
                statecode = (String) data.getSerializableExtra("StateCode");
                State.setText(statename);

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


    public void getState() {


        if (States.allmapstorecode.size() > 0) {
            progressBarHandler.show();
            JSONObject object = new JSONObject();
            FB_LY_UserService.sharedInstance().getState(object, new FB_LY_UserService.FBStateCallback() {
                @Override
                public void onStateCallback(JSONObject response, Exception error) {

                    try {
                        if (error == null && response != null) {
                            if (!response.has("stateList"))
                                return;

                            JSONArray getArrayStates = response.getJSONArray("stateList");
                            if (getArrayStates != null) {

                                for (int i = 0; i < getArrayStates.length(); i++) {
                                    JSONObject myStoresObj = getArrayStates.getJSONObject(i);
                                    States statesListObj = new States(myStoresObj);


                                }
                            }
                            progressBarHandler.dismiss();
                        }
                    } catch (Exception e) {
                    }
                }


            });
        }

    }

}
