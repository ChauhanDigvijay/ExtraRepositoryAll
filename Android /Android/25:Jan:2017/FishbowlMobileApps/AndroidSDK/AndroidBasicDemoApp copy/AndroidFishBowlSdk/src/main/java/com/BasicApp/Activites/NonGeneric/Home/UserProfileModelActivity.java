package com.BasicApp.Activites.NonGeneric.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.ChangePasswordModelActivity;
import com.BasicApp.Activites.NonGeneric.Store.SearchStoreModelActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Services.Field;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.gson.Gson;

import java.text.ParseException;

import static com.basicmodule.sdk.R.id.imageView;


/**
 * Created by digvijay(dj)
 */
public class UserProfileModelActivity extends BaseActivity implements View.OnClickListener

{
    static final int PICK_STORE_REQUEST = 1;
    static final int PICK_State_REQUEST = 2;
    public EditText FirstName;
    public EditText LastName;
    public EditText Address;
    public EditText ZipCode;
    public EditText FavoriteStore;
    public EditText City;
    public RadioGroup Gender;
    public EditText EmailAddress;
    public CheckBox SMSOptIn;
    public CheckBox EmailOptIn;
    public EditText DOB;
    public EditText State;
    public EditText PhoneNumber;
    RelativeLayout mtoolbar;
    TextView change;
    FBStoresItem selectedStore;
    Button save;
    ProgressBarHandler progressBarHandler;
    LinearLayout scrollLinearLayout;
    FBMember member = null;

    NetworkImageView backgroundProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        backgroundProfile = (NetworkImageView) findViewById(R.id.backgroundProfile);

        change = (TextView) findViewById(R.id.changepass);
        change.setOnClickListener(this);
        scrollLinearLayout = (LinearLayout) findViewById(R.id.scrollLinearLayout);

        save = (Button) findViewById(R.id.save_profile);
        save.setOnClickListener(this);
        progressBarHandler = new ProgressBarHandler(this);
      //  member = FBSessionService.member;

        Gson gson = new Gson();
        String json = FBPreferences.sharedInstance(UserProfileModelActivity.this).mSharedPreferences.getString("FBUser", "");
         member = gson.fromJson(json, FBMember.class);

        configureField();


        if (EmailAddress != null) {
            EmailAddress.setFocusable(false);
            EmailAddress.setHint("EmailAddress");
        }

        if (PhoneNumber != null)
            PhoneNumber.setFocusable(false);

        if (FirstName != null) {
            FirstName.setMaxLines(1);
            FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
            FirstName.setHint("FirstName");
        }
        if (LastName != null) {
            LastName.setMaxLines(1);
            LastName.setInputType(InputType.TYPE_CLASS_TEXT);
            LastName.setHint("LastName");
        }
        if (Address != null) {
            Address.setInputType(InputType.TYPE_CLASS_TEXT);
            Address.setMaxLines(1);
            Address.setHint("Address");
        }
        if (ZipCode != null) {
            ZipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
            ZipCode.setMaxLines(1);
            ZipCode.setHint("ZipCode");
        }
        if (FavoriteStore != null) {
            FavoriteStore.setInputType(InputType.TYPE_CLASS_TEXT);
            FavoriteStore.setMaxLines(1);
            FavoriteStore.setHint("FavoriteStore");

            FavoriteStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(UserProfileModelActivity.this, SearchStoreModelActivity.class);
                    startActivityForResult(i, PICK_STORE_REQUEST);
                }
            });
        }
        if (State != null) {
            State.setInputType(InputType.TYPE_CLASS_TEXT);
            State.setMaxLines(1);
            State.setHint("State");
        }
        if (City != null) {
            City.setInputType(InputType.TYPE_CLASS_TEXT);
            City.setMaxLines(1);
            City.setHint("City");
        }
        if (DOB != null) {
            DOB.setInputType(InputType.TYPE_CLASS_TEXT);
            DOB.setMaxLines(1);
            DOB.setHint("Date of Birth");
            DOB.setFocusable(false);
        }

        setUpToolBar(true, true);
        setTitle("UpdateProfile");
        setBackButton(false, false);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;

            final String url2 = "http://" + FBViewMobileSettingsService.sharedInstance().companyLogoImageUrl;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_STORE_REQUEST) {
            if (resultCode == RESULT_OK) {

                selectedStore = (FBStoresItem) data.getSerializableExtra("CLPSTORE");
                FavoriteStore.setText(selectedStore.getStoreName());

            }
        }
        if (requestCode == PICK_State_REQUEST) {
            if (resultCode == RESULT_OK) {

                String statename = (String) data.getSerializableExtra("State");
                State.setText(statename);

            }
        }


    }


    public void configureField() {

        if (member.getFirstName() != null)
            creatTextField(member.getFirstName());

        if (member.getLastName() != null)
            creatTextField(member.getLastName());

        if (member.getAddressStreet() != null)
            creatTextField(member.getAddressStreet());

        if (member.getAddressZipCode() != null)
            creatTextField(member.getAddressZipCode());

        if (member.getFavoriteStore() != null)
            creatTextField(member.getFavoriteStore());

        if (member.getAddressCity() != null)
            creatTextField(member.getAddressCity());

        if (member.getGender() != null)
            creatRadioGroup(member.getGender());

        if (member.getEmailAddress() != null)
            creatTextField(member.getEmailAddress());

        if (member.getdOB() != null)
            creatTextField(member.getdOB());

        if (member.getAddressState() != null)
            creatTextField(member.getAddressState());

        if (member.getPhoneNumber() != null) {
            final EditText editText = creatTextField(member.getPhoneNumber());


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

        if (member.getsMSOptIn() != null)
            creatRadioButton("SMSOptIn");

        if (member.getEmailOptIn() != null)
            creatRadioButton("EmailOptIn");


    }

    public EditText creatTextField(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, this), 0, FBUtility.inDp(8, this));
        view.setLayoutParams(layoutParams);

        ImageView imageName = (ImageView) view.findViewById(imageView);


        EditText editText = (EditText) view.findViewById(R.id.editText);
        TextView textview = (TextView) view.findViewById(R.id.field_text);


        editText.setTag(name);
        editText.setText(name);

        if (name.equals(member.getFirstName())) {


            textview.setText("FirstName");
            FirstName = editText;
            FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
            FirstName.setMaxLines(1);
            //imageView.setBackgroundResource(R.drawable.logo);
        }
        if (name.equals(member.getLastName())) {
            textview.setText("LastName");
            LastName = editText;
            LastName.setInputType(InputType.TYPE_CLASS_TEXT);
            LastName.setMaxLines(1);

        }
        if (name.equals(member.getAddressStreet())) {


            textview.setText("Address");
            Address = editText;
            Address.setInputType(InputType.TYPE_CLASS_TEXT);
            Address.setMaxLines(1);

        }
        if (name.equals(member.getAddressZipCode())) {
            textview.setText("ZipCode");
            ZipCode = editText;
            ZipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
            ZipCode.setMaxLines(1);

        }
        if (name.equals(member.getFavoriteStore())) {
            textview.setText("FavoriteStore");
            FavoriteStore = editText;
            FavoriteStore.setInputType(InputType.TYPE_CLASS_TEXT);
            FavoriteStore.setMaxLines(1);

        }

        if (name.equals(member.getAddressCity())) {
            textview.setText("City");
            City = editText;
            City.setInputType(InputType.TYPE_CLASS_TEXT);
            City.setMaxLines(1);

        }
        if (name.equals(member.getEmailAddress())) {
            textview.setText("EmailAddress");
            EmailAddress = editText;
            EmailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EmailAddress.setMaxLines(1);
        }
        if (name.equals(member.getdOB())) {
            textview.setText("BirthDay");
            DOB = editText;
            DOB.setInputType(InputType.TYPE_CLASS_DATETIME);
            DOB.setFocusable(false);
            DOB.setMaxLines(1);

        }

        if (name.equals(member.getPhoneNumber())) {

            textview.setText("PhoneNumber");

            PhoneNumber = editText;
            PhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            PhoneNumber.setMaxLines(1);


        }

        scrollLinearLayout.addView(view);

        return editText;
    }


    public void creatRadioGroup(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.male_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, this), 0, FBUtility.inDp(8, this));
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

        scrollLinearLayout.addView(view);
    }

    public void creatRadioButton(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.radio_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, this), 0, FBUtility.inDp(8, this));
        view.setLayoutParams(layoutParams);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);


        if (name.equals("SMSOptIn")) {
            SMSOptIn = checkBox;
            TextView editText = (TextView) view.findViewById(R.id.textView);
            editText.setText("SMSOptIn");
            SMSOptIn.setChecked(Boolean.valueOf(member.getsMSOptIn()));
        }

        if (name.equals("EmailOptIn")) {
            EmailOptIn = checkBox;
            TextView editText = (TextView) view.findViewById(R.id.textView);
            editText.setText("EmailOptIn");
            EmailOptIn.setChecked(Boolean.valueOf(member.getEmailOptIn()));
        }


        scrollLinearLayout.addView(view);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.changepass) {
            Intent i = new Intent(this, ChangePasswordModelActivity.class);
            startActivity(i);
        }

        if (v.getId() == R.id.save_profile) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle("Update");

            alertDialog.setMessage("Member Updated Successfully");

            alertDialog.setIcon(R.drawable.logo);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        createUpdateModel();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            alertDialog.show();

        }

    }


    public void createUpdateModel() throws java.text.ParseException {

        progressBarHandler.show();
        FBMember customer = new FBMember();
        try {
            Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;

            if (activeFields.FirstName) {
                if (FBUtils.isValidString(FirstName.getText().toString())) {

                    customer.setFirstName(FirstName.getText().toString());
                }
            }

            if (activeFields.LastName) {
                if (FBUtils.isValidString(LastName.getText().toString())) {

                    customer.setLastName(LastName.getText().toString());
                }
            }

            if (activeFields.EmailAddress) {
                if (FBUtils.isValidString(EmailAddress.getText().toString())) {


                    customer.setEmailAddress(EmailAddress.getText().toString());
                }
            }

            if (activeFields.EmailOptIn)
                customer.setEmailOptIn(EmailOptIn.isChecked());


            if (activeFields.PhoneNumber) {
                if (FBUtils.isValidString(PhoneNumber.getText().toString())) {


                    customer.setPhoneNumber(PhoneNumber.getText().toString());
                }
            }
            if (activeFields.SMSOptIn)
                customer.setsMSOptIn(SMSOptIn.isChecked());

            if (activeFields.Address) {
                if (FBUtils.isValidString(Address.getText().toString())) {
                    customer.setAddressStreet(Address.getText().toString());
                }
            }

            if (activeFields.City) {
                if (FBUtils.isValidString(City.getText().toString())) {

                    customer.setAddressCity(ZipCode.getText().toString());
                }
            }
            if (activeFields.ZipCode) {
                if (FBUtils.isValidString(ZipCode.getText().toString())) {
                    customer.setAddressZipCode(ZipCode.getText().toString());

                }
            }
            if (activeFields.DOB) {
                if (FBUtils.isValidString(DOB.getText().toString())) {
                    customer.setdOB(DOB.getText().toString());
                }
            }
            if (activeFields.Gender) {
                int radioButtonID = Gender.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
                customer.setGender(radioButton.getText().toString());
            }


            UpdateMember(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void UpdateMember(FBMember user) {

        FBSessionService.memberUpdate(user, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (spendGoSession != null)

                {
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE_IMAGE);
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE);
                    GetMember();

                } else {


                }
            }
        });
    }


    public void GetMember() {

        FBSessionService.getMember(new FBUserServiceCallback() {
            @Override
            public void onUserServiceCallback(FBMember user, Exception error) {


                if (user != null) {
                    progressBarHandler.dismiss();
                    finish();
                    Intent i = new Intent(UserProfileModelActivity.this, DashboardModelActivity.class);
                    startActivity(i);


                } else {
                    progressBarHandler.dismiss();
                    FBUtils.tryHandleTokenExpiry(UserProfileModelActivity.this, error);
                }
            }
        });
    }


}
