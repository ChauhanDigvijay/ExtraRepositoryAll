package com.BasicApp.Activites.NonGeneric.Authentication.SignUp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.SignInModelActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardModelActivity;
import com.BasicApp.Activites.NonGeneric.Settings.PrivacyLink;
import com.BasicApp.Activites.NonGeneric.Settings.TermsConditionActivity;
import com.BasicApp.Activites.NonGeneric.Store.SearchStoreModelActivity;
import com.BasicApp.BusinessLogic.Interfaces.FBASessionServiceCallback;
import com.BasicApp.BusinessLogic.Models.Bonus;
import com.BasicApp.BusinessLogic.Services.FBARewardService;
import com.BasicApp.BusinessLogic.Services.FBASessionService;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.TransitionManager;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBRestaurantItem;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Services.Field;
import com.fishbowl.basicmodule.Services.RegisterField;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by digvijay(dj)
 */
public class SignUpNewModelActivity extends BaseActivity implements View.OnClickListener, FBASessionServiceCallback {
    static final int PICK_STORE_REQUEST = 1;
    static final int PICK_State_REQUEST = 2;
    static final int PICK_Country_REQUEST = 3;
    static final int PICK_BONUS_REQUEST = 4;
    private static final String DATE_PATTERN_FOR_MONTH =
            "(0?[1-9]|1[012])";
    private static final String DATE_PATTERN_FOR_MONTHDATE =
            "(0?[1-9]|[12][0-9]|3[01])";
    private static final String DATE_PATTERN_FOR_YEAR =
            "((19|20)\\\\d\\\\d)";
    public static boolean checktermandconditon;
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
    public EditText Country;
    public EditText State;
    public EditText PhoneNumber;
    public EditText Password;
    public CheckBox PushOptin;
    public EditText Bonus;
    public TextView registxt_name;
    Button registration_button;
    TextView terms_text, privacy_text, signin;
    CheckBox checkbox_terms;
    LinearLayout scrollViewLinearLayout;
    Calendar myCalendar = Calendar.getInstance();
    String countrycode;
    FBRestaurantItem selectedStore;
    Button editText, editText1;
    Boolean rewardRule;
    int ruleId;
    ArrayList<Bonus> bonusRuleList = new ArrayList<Bonus>();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }

    };
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        setContentView(R.layout.fragment_signup_dynamic_fields);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        scrollViewLinearLayout = (LinearLayout) findViewById(R.id.scrollViewLinearLayout);
        configureField();
        checkbox_terms = (CheckBox) findViewById(R.id.checkbox_terms);
        checkbox_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked() == true) {
                    // registration_button.setEnabled(true);
                    checktermandconditon = true;
                } else {
                    // registration_button.setEnabled(false);
                    checktermandconditon = false;
                }
            }
        });
        terms_text = (TextView) findViewById(R.id.terms_text);
        terms_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.transitFrom(SignUpNewModelActivity.this, TermsConditionActivity.class);
            }
        });
        privacy_text = (TextView) findViewById(R.id.privacy_text);
        privacy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.transitFrom(SignUpNewModelActivity.this, PrivacyLink.class);
            }
        });

        signin = (TextView) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.transitFrom(SignUpNewModelActivity.this, SignInModelActivity.class);
            }
        });

        registxt_name = (TextView) findViewById(R.id.registxt_name);
        registration_button = (Button) findViewById(R.id.registration_start_okbutton);
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    createMemberModel();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        setUpToolBar(true, true);
        setTitle("Sign up and get VIP rewards");
        setBackButton(false, false);
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DOB.setText(sdf.format(myCalendar.getTime()));


    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_STORE_REQUEST) {

            if (resultCode == RESULT_OK) {

                selectedStore = (FBRestaurantItem) data.getSerializableExtra("CLPSTORE");
                FavoriteStore.setText(selectedStore.getStoreName());

            }

        }


        if (requestCode == PICK_Country_REQUEST) {
            if (resultCode == RESULT_OK) {

                String countryname = (String) data.getSerializableExtra("Country");
                countrycode = (String) data.getSerializableExtra("CountryCode");
                Country.setText(countryname);

            }


        }
        if (requestCode == PICK_BONUS_REQUEST) {
            if (resultCode == RESULT_OK) {


                String desc = (String) data.getSerializableExtra("desc");
                Bonus.setText(desc);

            }
        }
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_Text) {// do your code
            editText1.setBackgroundDrawable(getResources().getDrawable(R.drawable.square_cornner_stroke));
            editText.setBackgroundColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));
            editText.setTextColor(Color.parseColor("#ffffff"));
            editText1.setTextColor(Color.parseColor("#333333"));
            rewardRule = bonusRuleList.get(0).getRewardRule();
            ruleId = bonusRuleList.get(0).getId();

            // editText1.setBackgroundColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));

        } else if (i == R.id.btn_Text1) {// do your code
            editText.setBackgroundDrawable(getResources().getDrawable(R.drawable.square_cornner_stroke));
            editText1.setBackgroundColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));
            editText.setTextColor(Color.parseColor("#333333"));
            editText1.setTextColor(Color.parseColor("#ffffff"));
            rewardRule = bonusRuleList.get(1).getRewardRule();
            ruleId = bonusRuleList.get(1).getId();

        } else {
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try {
            Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;

            if (activeFields.FirstName) {
                if (FBUtils.isValidString(FirstName.getText().toString())) {
                    //FirstName =FirstName.setText().toString();
                    outState.putString("FirstName", FirstName.getText().toString());
                }
            }

            if (activeFields.LastName) {
                if (FBUtils.isValidString(LastName.getText().toString())) {
                    outState.putString("LastName", LastName.getText().toString());
                }
            }

            if (activeFields.EmailAddress) {
                if (FBUtils.isValidString(EmailAddress.getText().toString())) {
                    outState.putString("EmailAddress", EmailAddress.getText().toString());
                }
            }

            if (activeFields.PhoneNumber) {
                if (FBUtils.isValidString(PhoneNumber.getText().toString())) {
                    outState.putString("PhoneNumber", PhoneNumber.getText().toString());
                }
            }

            if (activeFields.DOB) {
                if (FBUtils.isValidString(DOB.getText().toString())) {
                    outState.putString("DOB", DOB.getText().toString());
                }
            }

            if (activeFields.Password) {
                if (FBUtils.isValidString(Password.getText().toString())) {
                    outState.putString("Password", Password.getText().toString());
                }
            }


            if (activeFields.FavoriteStore) {
                if (FBUtils.isValidString(FavoriteStore.getText().toString())) {
                    outState.putString("SelectedStore", FavoriteStore.getText().toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;
        if (savedInstanceState != null) {
            if (activeFields.FirstName) {

                if (FBUtils.isValidString(savedInstanceState.getString("FirstName"))) {
                    FirstName.setText(savedInstanceState.getString("FirstName"));
                }
            }
            if (activeFields.LastName) {
                if (FBUtils.isValidString(savedInstanceState.getString("LastName"))) {
                    LastName.setText(savedInstanceState.getString("LastName"));
                }
            }

            if (activeFields.EmailAddress) {
                if (FBUtils.isValidString(savedInstanceState.getString("EmailAddress"))) {
                    EmailAddress.setText(savedInstanceState.getString("EmailAddress"));
                }

                if (activeFields.PhoneNumber) {
                    if (FBUtils.isValidString(savedInstanceState.getString("PhoneNumber"))) {
                        PhoneNumber.setText(savedInstanceState.getString("PhoneNumber"));
                    }
                }


                if (activeFields.DOB) {
                    if (FBUtils.isValidString(savedInstanceState.getString("DOB"))) {
                        DOB.setText(savedInstanceState.getString("DOB"));

                    }
                }

                if (activeFields.Password) {
                    if (FBUtils.isValidString(savedInstanceState.getString("Password"))) {
                        Password.setText(savedInstanceState.getString("Password"));
                    }
                }


                if (activeFields.FavoriteStore) {
                    if (FBUtils.isValidString(savedInstanceState.getString("SelectedStore"))) {
                        FavoriteStore.setText(savedInstanceState.getString("SelectedStore"));
                    }
                }


            }
        }

    }

    public void configureField() {


        ArrayList<RegisterField> registerFields = FBViewMobileSettingsService.sharedInstance().activeFields.registerFields;

        for (RegisterField registerfield : registerFields) {
            if (registerfield.visible && registerfield.field.equalsIgnoreCase("FirstName")) {
                creatTextField("FirstName", registerfield.configDisplayLabel);

            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("LastName")) {
                creatTextField("LastName", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Address")) {
                creatTextField("Address", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("ZipCode")) {
                creatTextField("ZipCode", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("FavoriteStore")) {
                creatTextField("Favourite Location", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("City")) {
                creatTextField("City", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Gender")) {
                creatRadioGroup("Gender");
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("EmailAddress")) {
                creatTextField("EmailAddress", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("DOB")) {
                creatTextFieldforcalendra("DOB", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("PhoneNumber")) {
                final EditText editText = creatTextField("PhoneNumber", registerfield.configDisplayLabel);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

                editText.addTextChangedListener(new TextWatcher() {
                    private static final char space = ' ';

                    public void afterTextChanged(Editable s) {
                        editText.setSelection(editText.getText().length());
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        FBUtils.setUsDashPhone(s, before, editText);
                    }
                });

            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("SMSOptIn")) {
                creatRadioButton("SMSOptIn");
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("EmailOptIn")) {
                creatRadioButton("Opt-in to receive VIP email offers");
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Password")) {
                creatTextField("Password", registerfield.configDisplayLabel);
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("PushOptin")) {
                creatRadioButton("PushOptin");
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Bonus")) {


            }
        }
    }

    public EditText creatTextFieldforcalendra(String name, String desc) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewModelActivity.this.getSystemService(SignUpNewModelActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.calendar_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewModelActivity.this), 0, FBUtility.inDp(8, SignUpNewModelActivity.this));
        view.setLayoutParams(layoutParams);

        final EditText editText = (EditText) view.findViewById(R.id.editText);


        editText.setHint(desc);

        Button calbtn = (Button) view.findViewById(R.id.btn_Text1);
        if (name.equals("DOB")) {

            DOB = editText;
            DOB.setInputType(InputType.TYPE_CLASS_DATETIME);
            DOB.setMaxLines(1);
        }

        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editText.getText().length() < 10) {
                        FBUtils.showAlert(SignUpNewModelActivity.this, "Please provide all digits for birth date");
                    }
                }
                // TODO: the editText has just been left
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            public void afterTextChanged(Editable s) {


                editText.setSelection(editText.getText().length());


                if (editText.getText().length() == 2) {
                    {

                        CheckDOBforparticualr(editText.getText().toString());
                    }
                } else if (editText.getText().length() == 5) {
                    {

                        // CheckDOBforparticualrmonthdate(editText.getText().toString());

                        String date = editText.getText().toString();
                        String[] parts = date.split("/");
                        CheckDOBforparticualrmonthdate(parts[1]);
                    }
                } else if (editText.getText().length() == 10) {
                    try {

                        CheckDOB();


                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {


                    FBUtils.setUsDatePhone(s, before, editText);

                }
            }

        });


        calbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                DatePickerDialog mDatePicker = new DatePickerDialog(SignUpNewModelActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                mDatePicker.getDatePicker().setMaxDate((System.currentTimeMillis()));
                mDatePicker.show();


            }
        });


        scrollViewLinearLayout.addView(view);

        return editText;

    }


    public EditText creatTextField(String name, String desc) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewModelActivity.this.getSystemService(SignUpNewModelActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_fields, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewModelActivity.this), 0, FBUtility.inDp(8, SignUpNewModelActivity.this));
        view.setLayoutParams(layoutParams);


        EditText editText = (EditText) view.findViewById(R.id.editText);
        if (name.equals("FirstName")) {
            FirstName = editText;
            FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
            FirstName.setMaxLines(1);
        }
        if (name.equals("LastName")) {

            LastName = editText;
            LastName.setInputType(InputType.TYPE_CLASS_TEXT);
            LastName.setMaxLines(1);

        }
        if (name.equals("Address")) {

            Address = editText;
            Address.setInputType(InputType.TYPE_CLASS_TEXT);
            Address.setMaxLines(1);

        }
        if (name.equals("ZipCode")) {

            ZipCode = editText;
            ZipCode.setInputType(InputType.TYPE_CLASS_PHONE);
            ZipCode.setMaxLines(1);
            ZipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        }
        if (name.equals("Favourite Location")) {

            FavoriteStore = editText;
            FavoriteStore.setInputType(InputType.TYPE_CLASS_TEXT);
            FavoriteStore.setMaxLines(1);
            FavoriteStore.setFocusable(false);

            FavoriteStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SignUpNewModelActivity.this, SearchStoreModelActivity.class);
                    startActivityForResult(i, PICK_STORE_REQUEST);
                }
            });
        }
        if (name.equals("City")) {

            City = editText;
            City.setInputType(InputType.TYPE_CLASS_TEXT);
            City.setMaxLines(1);

        }
        if (name.equals("EmailAddress")) {

            EmailAddress = editText;
            EmailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EmailAddress.setMaxLines(1);

        }


        if (name.equals("PhoneNumber")) {
            PhoneNumber = editText;
            PhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            PhoneNumber.setMaxLines(1);

        }

        if (name.equals("Password")) {
            Password = editText;
            Password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            Password.setMaxLines(1);

        }


        editText.setTag(name);
        editText.setHint(desc);


        scrollViewLinearLayout.addView(view);

        return editText;
    }

    public void creatRadioGroup(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewModelActivity.this.getSystemService(SignUpNewModelActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_male_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewModelActivity.this), 0, FBUtility.inDp(8, SignUpNewModelActivity.this));
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

        if (name.equals("Gender")) {
            Gender = radioGroup;
            editText.setText("Gender");
            radioGroup.check(radioMale.getId());

        } else {
            Gender = radioGroup;
            editText.setText("Gender");
            radioGroup.check(radiofemale.getId());

        }
        for (int i = 0; i < Gender.getChildCount(); i++) {
            Gender.getChildAt(i).setEnabled(true);

        }

        scrollViewLinearLayout.addView(view);
    }

    public void creatRadioButton(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewModelActivity.this.getSystemService(SignUpNewModelActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_checkbox_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewModelActivity.this), 0, FBUtility.inDp(8, SignUpNewModelActivity.this));
        view.setLayoutParams(layoutParams);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        TextView textview = (TextView) view.findViewById(R.id.textView);

        if (name.equals("SMSOptIn")) {
            SMSOptIn = checkBox;


        }

        if (name.equals("Opt-in to receive VIP email offers")) {
            EmailOptIn = checkBox;
            textview.setText("Opt-in to receive VIP email offers");
            EmailOptIn.setChecked(true);
        }

        if (name.equals("PushOptin")) {
            PushOptin = checkBox;
            textview.setText("PushOptin");
        }


        TextView editText = (TextView) view.findViewById(R.id.textView);
        editText.setText(name);

        scrollViewLinearLayout.addView(view);
    }

    public void createMemberModel() throws java.text.ParseException {

        if (checkValidation()) {
            if (checkValidationforprivacy()) {
                enableScreen(false);
                setButtonEnabled(false);
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

                    if (activeFields.PushOptin)
                        customer.setPushOptIn(PushOptin.isChecked());

                    if (activeFields.PhoneNumber) {
                        if (FBUtils.isValidString(PhoneNumber.getText().toString())) {
                            //  object.put("phone", PhoneNumber.getText().toString());

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
                    if (activeFields.FavoriteStore) {
                        if (FBUtils.isValidString(selectedStore.getStoreNumber().toString())) {

                            customer.setStoreCode(selectedStore.getStoreNumber());
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


                        if (activeFields.Password) {
                            if (FBUtils.isValidString(Password.getText().toString())) {
                                customer.setPassword(Password.getText().toString());
                            }
                        }


                        customer.setDeviceId(FBUtility.getAndroidDeviceID(SignUpNewModelActivity.this));

                        Date d = Calendar.getInstance().getTime();

                        String format = null;
                        if (format == null)
                            format = "yyyy-MM-dd'T'hh:mm:ss";

                        SimpleDateFormat formatter = new SimpleDateFormat(format);
                        String currentData = formatter.format(d);

                        Calendar cal = Calendar.getInstance();

                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(cal.get(Calendar.MONTH) + 1);
                        int date = cal.get(Calendar.DATE);

                        customer.setDate(String.valueOf(date));
                        customer.setMonth(String.valueOf(month));
                        customer.setYear(String.valueOf(year));


                        createUser(customer);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void createUser(FBMember user) {

        FBSessionService.createMember(user, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem response, Exception error) {
                if (response != null) {


                    try {
                        if (true) {
                            FBUtils.showAlert(SignUpNewModelActivity.this, "Registration successful! You are now a member of the Sea Island VIP Club. Start earning points every time you dine with us by showing your VIP card or giving you phone number to the cashier.");

                            if (EmailOptIn.isChecked()) {

                                Login();
                            } else {
                                Intent ii = new Intent(SignUpNewModelActivity.this, SignInModelActivity.class);
                                startActivity(ii);
                                checktermandconditon = false;
                                enableScreen(true);
                                setButtonEnabled(true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    enableScreen(true);
                    setButtonEnabled(true);
                    FBUtils.tryHandleTokenExpiry(SignUpNewModelActivity.this, error);
                }


            }


        });
    }

    public void Login() {

        Date d = Calendar.getInstance().getTime();
        String format = null;
        if (format == null) {
            format = "yyyy-MM-dd'T'hh:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentData = formatter.format(d);

        //  object.put("eventDateTime", currentData);
        FBASessionService.sharedInstance(this).signInUser(PhoneNumber.getText().toString(), Password.getText().toString(), SignUpNewModelActivity.this);

    }


    private void fetchRewardPoint()
    {
        FBARewardService.sharedInstance(this).getUserFBPointBankOffer();
        GotoDashboard();
    }

    private void GotoDashboard() {
        TransitionManager.transitFrom(SignUpNewModelActivity.this, DashboardModelActivity.class);
    }


    public boolean checkValidation() throws java.text.ParseException {
        ArrayList<RegisterField> registerFields = FBViewMobileSettingsService.sharedInstance().activeFields.registerFields;
        for (RegisterField registerfield : registerFields) {

            if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("FirstName")) {
                if (!FBUtils.isValidString(FirstName.getText().toString())||!FBUtils.isValidName(FirstName.getText().toString())) {
                    FBUtils.showAlert(SignUpNewModelActivity.this, "Empty First Name");
                    return false;
                }
            } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("EmailAddress")) {
                if (!FBUtils.isValidString(EmailAddress.getText().toString())||!FBUtils.isValidEmail(EmailAddress.getText().toString())) {
                    FBUtils.showAlert(SignUpNewModelActivity.this, "Empty EmailAddress");
                    return false;
                }
            } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("ZipCode")) {
                if (!FBUtils.isValidString(ZipCode.getText().toString())) {
                    FBUtils.showAlert(SignUpNewModelActivity.this, "Empty ZipCode");
                    return false;
                }
            } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("FavoriteStore")) {
                if (!FBUtils.isValidString(FavoriteStore.getText().toString())) {
                    FBUtils.showAlert(SignUpNewModelActivity.this, "Empty FavoriteStore");
                    return false;
                }
            } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("Address")) {
                if (!FBUtils.isValidString(Address.getText().toString())) {
                    FBUtils.showAlert(SignUpNewModelActivity.this, "Empty Address");
                    return false;
                }
            } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("LastName")) {
                if (!FBUtils.isValidString(LastName.getText().toString())||!FBUtils.isValidName(LastName.getText().toString())) {
                    FBUtils.showAlert(SignUpNewModelActivity.this, "Empty Last Name");
                    return false;
                } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("City")) {
                    if (!FBUtils.isValidString(City.getText().toString())) {
                        FBUtils.showAlert(SignUpNewModelActivity.this, "Empty City");
                        return false;
                    }
                } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("PhoneNumber")) {
                    if (!(FBUtils.isValidString(PhoneNumber.getText().toString()) || FBUtils.isValidPhoneNumber(PhoneNumber.getText().toString()))) {
                        FBUtils.showAlert(SignUpNewModelActivity.this, "Empty Phone Number");
                        return false;
                    }
                } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("DOB")) {

                    {
                        return CheckDOB();
                    }

                } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("Password")) {
                    if (!FBUtils.isValidString(Password.getText().toString())||!FBUtils.isValidPassword(Password.getText().toString())) {
                        FBUtils.showAlert(SignUpNewModelActivity.this, "Empty Password");
                        return false;
                    }
                    if (Password.getText().toString().length() < 6) {
                        FBUtils.showAlert(SignUpNewModelActivity.this, "Minimum digit required six.");
                        return false;
                    }
                }
            }


        }
        return true;
    }

    public boolean checkValidationforprivacy() {
        if (checktermandconditon == false)

        {
            FBUtils.showAlert(SignUpNewModelActivity.this, "Please confirm you have read the Terms Of Use  and Privacy Policy by selecting the appropriate box .");
            return false;
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
                                FBUtils.showAlert(SignUpNewModelActivity.this, "Maximum age exceeded");
                                DOB.getText().clear();
                                return false;
                            }
                            return true;
                        } else {
                            FBUtils.showAlert(SignUpNewModelActivity.this, "Ineligible, Age not over 21");
                            DOB.getText().clear();
                            return false;
                        }
                    } else
                        FBUtils.showAlert(SignUpNewModelActivity.this, "Please provide all digits for birth date");
                    return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                FBUtils.showAlert(SignUpNewModelActivity.this, "Please enter a valid birth date mm/dd/yyyy");
                return false;
            }

        } else

        {
            FBUtils.showAlert(SignUpNewModelActivity.this, "Empty date of birth field");
            return false;

        }
        return false;
    }

    public void CheckDOBforparticualr(final String date) {

        pattern = Pattern.compile(DATE_PATTERN_FOR_MONTH);

        matcher = pattern.matcher(date);

        if (matcher.matches()) {


        } else {
            FBUtils.showAlert(SignUpNewModelActivity.this, "Invalide Month");
        }
    }

    public void CheckDOBforparticualrmonthdate(final String date) {

        pattern = Pattern.compile(DATE_PATTERN_FOR_MONTHDATE);

        matcher = pattern.matcher(date);

        if (matcher.matches()) {


        } else {
            FBUtils.showAlert(SignUpNewModelActivity.this, "Invalide Date");
        }
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

            }
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(year + " is an invalid year.");
        }
        if (yyyy < 999) {
            return false;

        }
        return true;
    }

    private int calculateAge(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        months = currMonth - birthMonth;

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
        return years;
    }

    private void setButtonEnabled(boolean isenabled) {
        isBackButtonEnabled = isenabled;
        registration_button.setEnabled(isenabled);


        if (isenabled) {
            registration_button.setText("GET STARTED");
        } else {
            registration_button.setText("Please wait...");
        }
    }

    @Override
    public void onUserServiceCallback(FBSessionItem spendGoSession, Exception exception) {
        fetchRewardPoint();
    }
}

