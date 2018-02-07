package com.BasicApp.Activites.NonGeneric.Authentication.SignUp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardActivity;
import com.BasicApp.Activites.NonGeneric.Settings.PrivacyLink;
import com.BasicApp.Activites.NonGeneric.Settings.TermsConditionActivity;
import com.BasicApp.Activites.NonGeneric.State.StateActivity;
import com.BasicApp.Activites.NonGeneric.Store.SearchStoreActivity;
import com.BasicApp.BusinessLogic.Models.Bonus;
import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummaryPointCallback;
import com.BasicApp.BusinessLogic.Services.RewardPointService;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Services.Field;
import com.fishbowl.basicmodule.Services.RegisterField;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONException;
import org.json.JSONObject;
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
 * Created by schaudhary_ic on 19-May-16.
 */
public class SignUpNewActivity extends Activity implements View.OnClickListener {
    static final int PICK_STORE_REQUEST = 1;// The request code
    static final int PICK_State_REQUEST = 2;// The request code
    static final int PICK_Country_REQUEST = 3;
    static final int PICK_BONUS_REQUEST = 4;// The request code
    private static final String DATE_PATTERN =
            "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
    private static final String DATE_PATTERN_FOR_MONTH =
            "(0?[1-9]|1[012])";
    private static final String DATE_PATTERN_FOR_MONTHDATE =
            "(0?[1-9]|[12][0-9]|3[01])";
    private static final String DATE_PATTERN_FOR_YEAR =
            "((19|20)\\\\d\\\\d)";
    public static boolean checktermandconditon;
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
    public CheckBox EmailOptIn;
    public EditText DOB;
    public EditText Country;
    public EditText State;
    public EditText PhoneNumber;
    public EditText Password;
    //Two add new
    public CheckBox EmailVerify;
    public CheckBox PushOptin;
    public EditText Bonus;
    public Button Bonus1;
    public TextView registxt_name;
    Button registration_button;
    ImageView imageView2;
    ProgressBarHandler progressBarHandler;
    TextView terms_text, privacy_text, signin, textviewbonus;
    String fbm;
    CheckBox checkbox_terms;
    LinearLayout scrollViewLinearLayout;
    Calendar myCalendar = Calendar.getInstance();
    String countrycode;
    FBStoresItem selectedStore;
    Button editText, editText1;
    Boolean button1, button2;
    Boolean rewardRule;
    int ruleId;
    RelativeLayout mtoolbar;
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
    private ImageLoader mImageLoader;
    private Pattern pattern;
    private Matcher matcher;

    public static void logout(final Activity activity) {


        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        // Setting Dialog Title

        // Setting Dialog Message
        alertDialog.setMessage("Please select country first ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }
        });


        alertDialog.show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        setContentView(R.layout.fragment_signup_dynamic_fields);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // imlogo = (NetworkImageView) findViewById(R.id.im_logo);


//        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
//        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onCustomBackPressed();
//            }
//        });

        progressBarHandler = new ProgressBarHandler(SignUpNewActivity.this);
        scrollViewLinearLayout = (LinearLayout) findViewById(R.id.scrollViewLinearLayout);


        // Your first time start-up code here, will not run on orientation change
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
                Intent ii = new Intent( SignUpNewActivity.this, TermsConditionActivity.class);
                startActivity(ii);
            }
        });
        privacy_text = (TextView) findViewById(R.id.privacy_text);
        privacy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent( SignUpNewActivity.this, PrivacyLink.class);
                startActivity(ii);
            }
        });

        signin = (TextView) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent( SignUpNewActivity.this, SignInActivity.class);
                startActivity(ii);
            }
        });

        registxt_name = (TextView) findViewById(R.id.registxt_name);
        registration_button = (Button) findViewById(R.id.registration_start_okbutton);
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //      getToken();
                    createMember();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //  registration_button.setEnabled(false);


        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SignUpNewActivity.this.finish();
            }
        });






    }

    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DOB.setText(sdf.format(myCalendar.getTime()));


    }
    public void onCustomBackPressed() {
        SignUpNewActivity.this.finish();
    }

    public void configureField() {

        Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;
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
            }


//            else if (registerfield.visible&&registerfield.field.equalsIgnoreCase("Country")) {
//                creatTextField("Country");
//            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("State")) {
                creatTextField("State", registerfield.configDisplayLabel);
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
            }
//            else if (registerfield.visible&&registerfield.field.equalsIgnoreCase("EmailVerify")) {
//                creatRadioButton("EmailVerify");
//            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("PushOptin")) {
                creatRadioButton("PushOptin");
            } else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Bonus")) {
              //  getBonusRuleList("Bonus");

            }
        }
    }

    public Button creatTextFieldforbonus() {

        final LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewActivity.this.getSystemService(SignUpNewActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_button, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewActivity.this), 0, FBUtility.inDp(8, SignUpNewActivity.this));
        view.setLayoutParams(layoutParams);

        editText = (Button) view.findViewById(R.id.btn_Text);
        textviewbonus = (TextView) view.findViewById(R.id.bonusText);
        editText.setOnClickListener(this);
        editText1 = (Button) view.findViewById(R.id.btn_Text1);
        editText1.setOnClickListener(this);

        if (bonusRuleList != null) {
            if (bonusRuleList.size() > 0) {

                if (bonusRuleList.size() > 0 && bonusRuleList.size() == 1) {
//                    textviewbonus.setText("Get your Signup Bonus");
//                    editText.setText(bonusRuleList.get(0).getDescription());
//                    editText.setGravity(Gravity.CENTER);
//                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
//                    editText.setLayoutParams(param);
//                    //  editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    editText1.setVisibility(View.INVISIBLE);
//                    rewardRule = bonusRuleList.get(0).getRewardRule();
//                    ruleId = bonusRuleList.get(0).getId();
                } else {
                    textviewbonus.setText("Select your Signup Bonus");
                    editText.setText(bonusRuleList.get(0).getDescription());
                    editText1.setText(bonusRuleList.get(1).getDescription());
                    rewardRule = bonusRuleList.get(0).getRewardRule();
                    ruleId = bonusRuleList.get(0).getId();
                    String buttoncolor = FBViewMobileSettingsService.sharedInstance().checkInButtonColor;
                    if (buttoncolor != null) {
                        String btncolor = "#" + buttoncolor;
                        editText.setBackgroundColor(Color.parseColor(btncolor));
                    }

                }
            }
        }
//            for ( int i= 0; i<2; i++) {
//
//                if (bo != null) {
//
//
//                Bonus1 = editText;
//                Bonus1.setText(bo.getDescription());
//            ;
//                Bonus1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view1) {
//
//                        if (view.isSelected()) {
//
//                            view.setBackgroundColor(Color.parseColor("#ffffff"));
//                            view.setSelected(false);
//                        } else {
//                            view.setBackgroundColor(Color.parseColor("#" + FB_LY_MobileSettingService.sharedInstance().checkInButtonColor));
//                            view.setSelected(true);
//                        }
//                        rewardRule = bo.getRewardRule();
//                        ruleId = bo.getId();
//                    }
//
//                });
//            }}
//

        scrollViewLinearLayout.addView(view);

        return editText;

    }

    public EditText creatTextFieldforcalendra(String name, String desc) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewActivity.this.getSystemService(SignUpNewActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.calendar_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewActivity.this), 0, FBUtility.inDp(8, SignUpNewActivity.this));
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

//        editText.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    if (editText.getText().length() <2 )
//                    {
//                       FBUtils.showAlert(SignUpNewActivity.this(),"sdk");
//                    }
//                    else
//                    {
//
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editText.getText().length() < 10) {
                        FBUtils.showAlert(SignUpNewActivity.this, "Please provide all digits for birth date");
                    }
                }
                // TODO: the editText has just been left
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            public void afterTextChanged(Editable s) {

//                String ss = editText.getText().toString();
//                int o = 0;
//
//                if ((ss.charAt(2) == '/') && (ss.charAt(4) == '/')) {
//
//
//                } else {
//                    editText.setTextColor(Color.RED);
//                    editText.setText("Invalid Format");
//
//
//                }

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
                DatePickerDialog mDatePicker = new DatePickerDialog(SignUpNewActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                mDatePicker.getDatePicker().setMaxDate((System.currentTimeMillis()));
                mDatePicker.show();


            }
        });


        scrollViewLinearLayout.addView(view);

        return editText;

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
//                if (activeFields.EmailOptIn)
//                    object.put("emailOptIn", EmailOptIn.isChecked());
//
////                if (activeFields.EmailVerify)
////                    object.put("emailOptIn", EmailVerify.isChecked());
//                if (activeFields.PushOptin)
//                    object.put("emailOptIn", PushOptin.isChecked());
//
            if (activeFields.PhoneNumber) {
                if (FBUtils.isValidString(PhoneNumber.getText().toString())) {
                    outState.putString("PhoneNumber", PhoneNumber.getText().toString());
                }
            }
//                if (activeFields.SMSOptIn)
//                    object.put("smsOptIn", SMSOptIn.isChecked());
//
//                if (activeFields.Address) {
//                    if (FBUtils.isValidString(Address.getText().toString())) {
//                        object.put("addressStreet", Address.getText().toString());
//                    }
//                }
//                if (activeFields.FavoriteStore){
//                    if( FBUtils.isValidString(selectedStore.getStoreNumber().toString())) {
//                        object.put("storeCode", selectedStore.getStoreNumber());
//                    }
//                }
//
//                if (activeFields.State){
//                    if( FBUtils.isValidString(statecode)) {
//                        object.put("addressState", statecode);
//                    }
//                }
//                if (activeFields.City) {
//                    if( FBUtils.isValidString(City.getText().toString())) {
//                        object.put("addressCity", City.getText().toString());}
//                }
//                if (activeFields.ZipCode){
//                    if( FBUtils.isValidString(ZipCode.getText().toString())) {
//                        object.put("addressZipCode", ZipCode.getText().toString());
//                    }
//                }
            if (activeFields.DOB) {
                if (FBUtils.isValidString(DOB.getText().toString())) {
                    outState.putString("DOB", DOB.getText().toString());
                }
            }
//                if (activeFields.Gender) {
//                    int radioButtonID = Gender.getCheckedRadioButtonId();
//                    RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
//                    object.put("gender", radioButton.getText());
//                }
//
//                if (activeFields.Bonus) {
//                    if (bonusRuleList != null) {
//                        if (bonusRuleList.size() > 1) {
//                            object.put("rewardRule", rewardRule);
//                            object.put("ruleId", ruleId);
//                        }
//                    }
//                }
//
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

                //    someVarB = savedInstanceState.getString("someVarB");

            }
        }

    }

    public EditText creatTextField(String name, String desc) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewActivity.this.getSystemService(SignUpNewActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_fields, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewActivity.this), 0, FBUtility.inDp(8, SignUpNewActivity.this));
        view.setLayoutParams(layoutParams);

        // ImageView imageName = (ImageView) view.findViewById(R.id.imageView);
        // ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //  TextView textview = (TextView) view.findViewById(R.id.field_text);

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
                    Intent i = new Intent(SignUpNewActivity.this, SearchStoreActivity.class);
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
//        if (name.equals("DOB")) {
//
//            DOB = editText;
//            DOB.setInputType(InputType.TYPE_CLASS_DATETIME);
//            DOB.setFocusable(false);
//            DOB.setMaxLines(1);
//        }

//        if (name.equals("Country")) {
//
//            Country = editText;
//            Country.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//            Country.setMaxLines(1);
//            Country.setFocusable(false);
//            Country.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(SignUpNewActivity.this(), CountryActivity.class);
//                    startActivityForResult(i, PICK_Country_REQUEST);
//                }
//            });
//        }
        if (name.equals("State")) {

            State = editText;
            State.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            State.setMaxLines(1);
            State.setFocusable(false);
            State.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(SignUpNewActivity.this, StateActivity.class);
                    i.putExtra("CountryCode", countrycode);
                    startActivityForResult(i, PICK_State_REQUEST);

                }
            });
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
        //setImage(imageName,name);

        scrollViewLinearLayout.addView(view);

        return editText;
    }

    public void creatRadioGroup(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) SignUpNewActivity.this.getSystemService(SignUpNewActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_male_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewActivity.this), 0, FBUtility.inDp(8, SignUpNewActivity.this));
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
        LayoutInflater mInflater = (LayoutInflater) SignUpNewActivity.this.getSystemService(SignUpNewActivity.this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_checkbox_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, SignUpNewActivity.this), 0, FBUtility.inDp(8, SignUpNewActivity.this));
        view.setLayoutParams(layoutParams);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        TextView textview = (TextView) view.findViewById(R.id.textView);

        if (name.equals("SMSOptIn")) {
            SMSOptIn = checkBox;
            //  textview.setText("SMSOptIn");

        }

        if (name.equals("Opt-in to receive VIP email offers")) {
            EmailOptIn = checkBox;
            textview.setText("Opt-in to receive VIP email offers");
            EmailOptIn.setChecked(true);
        }
//        if (name.equals("EmailVerify")) {
//            EmailVerify = checkBox;
//            textview.setText("EmailVerify");
//        }
        if (name.equals("PushOptin")) {
            PushOptin = checkBox;
            textview.setText("PushOptin");
        }


        TextView editText = (TextView) view.findViewById(R.id.textView);
        editText.setText(name);

        scrollViewLinearLayout.addView(view);
    }

    public void createMember() throws java.text.ParseException {

        if (checkValidation()) {
            if (checkValidationforprivacy()) {
                progressBarHandler.show();
                JSONObject object = new JSONObject();
                try {
                    Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;

                    if (activeFields.FirstName) {
                        if (FBUtils.isValidString(FirstName.getText().toString())) {
                            object.put("firstName", FirstName.getText().toString());
                        }
                    }

                    if (activeFields.LastName) {
                        if (FBUtils.isValidString(LastName.getText().toString())) {
                            object.put("lastName", LastName.getText().toString());
                        }
                    }

                    if (activeFields.EmailAddress) {
                        if (FBUtils.isValidString(EmailAddress.getText().toString())) {
                            object.put("email", EmailAddress.getText().toString());
                        }
                    }
                    if (activeFields.EmailOptIn)
                        object.put("emailOptIn", EmailOptIn.isChecked());

//                if (activeFields.EmailVerify)
//                    object.put("emailOptIn", EmailVerify.isChecked());
                    if (activeFields.PushOptin)
                        object.put("emailOptIn", PushOptin.isChecked());

                    if (activeFields.PhoneNumber) {
                        if (FBUtils.isValidString(PhoneNumber.getText().toString())) {
                            object.put("phone", PhoneNumber.getText().toString());
                        }
                    }
                    if (activeFields.SMSOptIn)
                        object.put("smsOptIn", SMSOptIn.isChecked());

                    if (activeFields.Address) {
                        if (FBUtils.isValidString(Address.getText().toString())) {
                            object.put("addressStreet", Address.getText().toString());
                        }
                    }
                    if (activeFields.FavoriteStore) {
                        if (FBUtils.isValidString(selectedStore.getStoreNumber().toString())) {
                            object.put("storeCode", selectedStore.getStoreNumber());
                        }
                    }
                    //object.put("favoriteStore",selectedStore.getStoreID());


//
//                if (activeFields.Country)
//                    object.put("country",Country.getText().toString());

                    if (activeFields.State) {
                        if (FBUtils.isValidString(statecode)) {
                            object.put("addressState", statecode);
                        }
                    }
                    if (activeFields.City) {
                        if (FBUtils.isValidString(City.getText().toString())) {
                            object.put("addressCity", City.getText().toString());
                        }
                    }
                    if (activeFields.ZipCode) {
                        if (FBUtils.isValidString(ZipCode.getText().toString())) {
                            object.put("addressZipCode", ZipCode.getText().toString());
                        }
                    }
                    if (activeFields.DOB) {
                        if (FBUtils.isValidString(DOB.getText().toString())) {
                            object.put("birthDate", DOB.getText().toString());
//                            String[] parts =  DOB.getText().toString().split("/");
//                            object.put("month", parts[0]);
//                            object.put("date", parts[1]);
//                            object.put("year",parts[2]);

                        }
                    }
                    if (activeFields.Gender) {
                        int radioButtonID = Gender.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
                        object.put("gender", radioButton.getText());
                    }

//                    if (activeFields.Bonus) {
//                        if (bonusRuleList != null) {
//                            if (bonusRuleList.size() > 1) {
//                                object.put("rewardRule", rewardRule);
//                                object.put("ruleId", ruleId);
//                            }
//                        }
//                    }

                    if (activeFields.Password) {
                        if (FBUtils.isValidString(Password.getText().toString())) {
                            object.put("password", Password.getText().toString());
                        }
                    }
                    object.put("deviceId", FBUtility.getAndroidDeviceID(SignUpNewActivity.this));

                    Date d = Calendar.getInstance().getTime();

                    String format = null;
                    if (format == null)
                        format = "yyyy-MM-dd'T'hh:mm:ss";

                    SimpleDateFormat formatter = new SimpleDateFormat(format);
                    String currentData = formatter.format(d);

                    object.put("eventDateTime", currentData);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                FBUserService.sharedInstance().createMember(object, new FBUserService.FBCreateMemberCallback() {
                    @Override
                    public void onCreateMemberCallback(JSONObject response, Exception error) {
                        //  progressBarHandler.hide();
                        //  pd.dismiss();
                        if (response != null) {


                            try {
                                if (response.getString("successFlag").equalsIgnoreCase("true")) {
                                    FBUtils.showAlert(SignUpNewActivity.this, "Registration successful! You are now a member of the Sea Island VIP Club. Start earning points every time you dine with us by showing your VIP card or giving you phone number to the cashier.");

                                 if(EmailOptIn.isChecked()) {

                                     loginMemberforpassword();
                                 }else
                                     {
                                         Intent ii = new Intent(SignUpNewActivity.this, SignInActivity.class);
                                         startActivity(ii);
                                         checktermandconditon = false;
                                         progressBarHandler.dismiss();
                                     }
                                } else {
                                    progressBarHandler.dismiss();
                                    FBUtils.tryHandleTokenExpiry(SignUpNewActivity.this, error);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            progressBarHandler.dismiss();
                            FBUtils.tryHandleTokenExpiry(SignUpNewActivity.this, error);
                        }

                    }
                });
            }
        }
    }

//    public void getBonusRuleList(final String editText) {
//        progressBarHandler.show();
//        final JSONArray object = new JSONArray();
//        FBUserOfferService.sharedInstance().getBonusRuleList(object, new FBUserOfferService.FBBonusRuleListCallback() {
//            @TargetApi(Build.VERSION_CODES.KITKAT)
//            public void onBonusRuleListCallback(JSONArray response, Exception error) {
//                try {
//                    if (response != null) {
//                        progressBarHandler.dismiss();
//                        if (response.length() > 0) {
//
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject myBonusObj = response.getJSONObject(i);
//                              Bonus bonusObj = new Bonus(myBonusObj);
//                                bonusRuleList.add(bonusObj);
//
//                            }
//
//                            if (bonusRuleList.size() > 1) {
//                                creatTextFieldforbonus();
//                            }
//                        }
//                    } else {
//                        FBUtils.tryHandleTokenExpiry(SignUpNewActivity.this, error);
//                        progressBarHandler.dismiss();
//
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
//    }

    public void loginMember() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        try {
            String email = PhoneNumber.getText().toString();
            String pass = Password.getText().toString();

            object.put("username", email);
            object.put("password", pass);


        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    try {
                        String secratekey = response.getString("accessToken");
                        FBPreferences.sharedInstance(SignUpNewActivity.this).setAccessTokenforapp(secratekey);
                        //  FB_LY_UserService.sharedInstance().access_token = response.getString("accessToken");
                        //   FBPreferences.sharedInstance(SignUpNewActivity.this()).setSignin(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getMember();
                    // progressBarHandler.hide();
                } else {
                    FBUtils.tryHandleTokenExpiry((Activity) SignUpNewActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }

    public void getMember() {

        JSONObject object = new JSONObject();

        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {
                if (response != null) {


                    String homeStoreID= null;
                    try {
                        homeStoreID = response.getString("homeStoreID");
                        FBPreferences.sharedInstance(SignUpNewActivity.this).setStoreCode(homeStoreID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    fetchRewardPoint();

                } else {
                    FBUtils.tryHandleTokenExpiry((Activity)SignUpNewActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }

        });
    }

    @Override
    public void onResume(){
        super.onResume();


//        String buttoncolor = FBViewMobileSettingsService.sharedInstance().checkInButtonColor;
//        if (buttoncolor != null) {
//            String btncolor = "#" + buttoncolor;
//            registration_button.setBackgroundColor(Color.parseColor(btncolor));
//            registxt_name.setTextColor(Color.parseColor(btncolor));
//            signin.setTextColor(Color.parseColor(btncolor));
//            terms_text.setTextColor(Color.parseColor(btncolor));
//            privacy_text.setTextColor(Color.parseColor(btncolor));
//
//
//        }
//
//        if (StringUtilities.isValidString(FBViewMobileSettingsService.sharedInstance().signUpPageTagLine)) {
//            registxt_name.setText(FBViewMobileSettingsService.sharedInstance().signUpPageTagLine);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_STORE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                selectedStore = (FBStoresItem) data.getSerializableExtra("CLPSTORE");
                FavoriteStore.setText(selectedStore.getStoreName());

                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }

        }
        if (requestCode == PICK_State_REQUEST) {
            if (resultCode == RESULT_OK) {

                String statename = (String) data.getSerializableExtra("State");
                statecode = (String) data.getSerializableExtra("StateCode");

                State.setText(statename);

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

                // rewardRule = (String) data.getSerializableExtra("rewardRule");
                // ruleId = (String) data.getSerializableExtra("ruleId");
                String desc = (String) data.getSerializableExtra("desc");
                Bonus.setText(desc);

            }
        }
    }

    public boolean checkValidation() throws java.text.ParseException {
        //  fname, lname, email, phone, address,password;
        ArrayList<RegisterField> registerFields = FBViewMobileSettingsService.sharedInstance().activeFields.registerFields;
        for (RegisterField registerfield : registerFields) {

            if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("FirstName")) {
                if (!FBUtils.isValidString(FirstName.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty First Name");
                    return false;
                }
            } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("EmailAddress")) {
                if (!FBUtils.isValidString(EmailAddress.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty EmailAddress");
                    return false;
                }
            } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("ZipCode")) {
                if (!FBUtils.isValidString(ZipCode.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty ZipCode");
                    return false;
                }
            } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("FavoriteStore")) {
                if (!FBUtils.isValidString(FavoriteStore.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty FavoriteStore");
                    return false;
                }
            } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("Address")) {
                if (!FBUtils.isValidString(Address.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty Address");
                    return false;
                }
            } else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("LastName")) {
                if (!FBUtils.isValidString(LastName.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty Last Name");
                    return false;
                }
            } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("State")) {
                if (!FBUtils.isValidString(State.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty State");
                    return false;
                }
            } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("City")) {
                if (!FBUtils.isValidString(City.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty City");
                    return false;
                }
            }


     /*   if (FBUtils.isValidEmail(email.getText().toString()))
        {
            FBUtils.showAlert(SignUpNewActivity.this(),"Invalid Email");
            return false;
        }*/
            else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("PhoneNumber")) {
                if (!(FBUtils.isValidString(PhoneNumber.getText().toString()) || FBUtils.isValidPhoneNumber(PhoneNumber.getText().toString()))) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty Phone Number");
                    return false;
                }
            }
//        if(!FBUtils.isValidString(Address.getText().toString()))
//        {
//            FBUtils.showAlert(SignUpNewActivity.this(),"Empty Address");
//            return false;
//        }

            else if (registerfield.visible && registerfield.mandatory && registerfield.field.equalsIgnoreCase("DOB")) {

                {
                    return CheckDOB();
                }

            } else if (registerfield.mandatory && registerfield.visible && registerfield.field.equalsIgnoreCase("Password")) {
                if (!FBUtils.isValidString(Password.getText().toString())) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Empty Password");
                    return false;
                }
                if (Password.getText().toString().length() < 6) {
                    FBUtils.showAlert(SignUpNewActivity.this, "Minimum digit required six.");
                    return false;
                }
            }
        }

        return true;
    }

    public boolean checkValidationforprivacy() {
        if (checktermandconditon == false)

        {
            FBUtils.showAlert(SignUpNewActivity.this, "Please confirm you have read the Terms Of Use  and Privacy Policy by selecting the appropriate box .");
            return false;
        }
        return true;
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
                                FBUtils.showAlert(SignUpNewActivity.this, "Maximum age exceeded");
                                DOB.getText().clear();
                                return false;
                            }
                            return true;
                        } else {
                            FBUtils.showAlert(SignUpNewActivity.this, "Ineligible, Age not over 21");
                            DOB.getText().clear();
                            return false;
                        }
                    } else
                        FBUtils.showAlert(SignUpNewActivity.this, "Please provide all digits for birth date");
                    return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                FBUtils.showAlert(SignUpNewActivity.this, "Please enter a valid birth date mm/dd/yyyy");
                return false;
            }

        } else

        {
            FBUtils.showAlert(SignUpNewActivity.this, "Empty date of birth field");
            return false;

        }
        return false;
    }

    public void CheckDOBforparticualr(final String date) {

        pattern = Pattern.compile(DATE_PATTERN_FOR_MONTH);

        matcher = pattern.matcher(date);

        if (matcher.matches()) {


        } else {
            FBUtils.showAlert(SignUpNewActivity.this, "Invalide Month");
        }
    }


    public void CheckDOBforparticualrmonthdate(final String date) {

        pattern = Pattern.compile(DATE_PATTERN_FOR_MONTHDATE);

        matcher = pattern.matcher(date);

        if (matcher.matches()) {


        } else {
            FBUtils.showAlert(SignUpNewActivity.this, "Invalide Date");
        }
    }


    public void CheckDOBforyear(final String date) {

        pattern = Pattern.compile(DATE_PATTERN_FOR_YEAR);

        matcher = pattern.matcher(date);

        if (matcher.matches()) {


        } else {
            FBUtils.showAlert(SignUpNewActivity.this, "Invalide Year");
        }
    }

//
//    public static boolean isDateValid(int year, int month, int day) {
//        boolean dateIsValid = true;
//        try {
//            LocalDate.of(year, month, day);
//        } catch (DateTimeException e) {
//            dateIsValid = false;
//        }
//        return dateIsValid;
//    }

    public boolean CheckDOBwithoutmandatorycheck() throws java.text.ParseException {

        if (FBUtils.isValidString(DOB.getText().toString())) {
            if (FBUtils.isValidDateFormat(DOB.getText().toString())) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date birthDate = sdf.parse(DOB.getText().toString());//Yeh !! It's my date of birth :-)
                try {
                    if (yearFormatCheck(birthDate) == true) {
                        int age = calculateAge(birthDate);
                        if (age >= 21) {
                            if (age > 100) {
                                FBUtils.showAlert(SignUpNewActivity.this, "Maximum age exceeded");
                                return false;
                            }
                            return true;
                        } else {
                            FBUtils.showAlert(SignUpNewActivity.this, "Ineligible, Age not over 21");
                            return false;
                        }
                    } else
                        FBUtils.showAlert(SignUpNewActivity.this, "Please provide all digits for birth date");
                    return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                FBUtils.showAlert(SignUpNewActivity.this, "Please enter a valid birth date mm/dd/yyyy");
                return false;
            }

        } else

        {
            FBUtils.showAlert(SignUpNewActivity.this, "Empty date of birth field");
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

    public void getToken() {


        JSONObject object = new JSONObject();
        try {
            object.put("clientId", FBConstant.client_id);
            object.put("clientSecret", FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(SignUpNewActivity.this));
            object.put("tenantId", FBConstant.client_tenantid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback() {


            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if (response != null) {

                    try {
                        String secratekey = response.getString("message");
                        FBPreferences.sharedInstance(SignUpNewActivity.this).setAccessToken(secratekey);

                        createMember();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }

            }
        });
    }


    public void loginMemberforpassword() {
        JSONObject object = new JSONObject();
        String email = PhoneNumber.getText().toString();
        Date d = Calendar.getInstance().getTime();

        String format = null;
        if (format == null)
            format = "yyyy-MM-dd'T'hh:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentData = formatter.format(d);

        try {
           // fbm = FBViewMobileSettingsService.sharedInstance().passwordEnable;
            Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;
            String pass = Password.getText().toString();
            object.put("username", email);
            object.put("password", pass);
            object.put("eventDateTime", currentData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        FBUserService.sharedInstance().loginMember(object, new FBUserService.FBLoginMemberCallback() {
            public void onLoginMemberCallback(JSONObject response, Exception error) {
                if (response != null) {
                    try {
                        String secratekey = response.getString("accessToken");
                        FBPreferences.sharedInstance(SignUpNewActivity.this).setAccessTokenforapp(secratekey);
//                        FB_LY_UserService.sharedInstance().access_token = response.getString("accessToken");
//                        FBPreferences.sharedInstance(SignUpNewActivity.this()).setSignin(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getMember();
                } else {
                    FBUtils.tryHandleTokenExpiry(SignUpNewActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }

        });
    }

    private void fetchRewardPoint() {

        RewardPointService.getUserRewardPoint(SignUpNewActivity.this, new RewardSummaryPointCallback() {
            @Override
            public void onRewardSummaryPointCallback(RewardPointSummary rewardSummary, Exception error) {
                if (rewardSummary != null) {
                    Intent ii = new Intent(SignUpNewActivity.this, DashboardActivity.class);
                    startActivity(ii);
                    checktermandconditon = false;
                    progressBarHandler.dismiss();
                } else {
                    FBUtils.tryHandleTokenExpiry(SignUpNewActivity.this, error);
                    progressBarHandler.dismiss();
                }
            }
        });
    }
}

