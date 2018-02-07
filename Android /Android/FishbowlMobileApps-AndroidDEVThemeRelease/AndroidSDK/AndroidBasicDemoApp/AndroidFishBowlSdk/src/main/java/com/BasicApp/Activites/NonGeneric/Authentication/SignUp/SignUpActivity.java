package com.BasicApp.Activites.NonGeneric.Authentication.SignUp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.BasicApp.Activites.NonGeneric.Country.CountryActivity;
import com.BasicApp.Activites.NonGeneric.Settings.PrivacyLink;
import com.BasicApp.Activites.NonGeneric.Settings.TermsConditionActivity;
import com.BasicApp.Activites.NonGeneric.State.ActivityState;
import com.BasicApp.Activites.NonGeneric.Store.SearchStoreActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.Bonus;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Services.Field;
import com.fishbowl.basicmodule.Services.RegisterField;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by schaudhary_ic on 19-May-16.
 */
public class SignUpActivity extends Activity implements View.OnClickListener {
    private NetworkImageView background;
    ImageView backbutton;

    static final int PICK_STORE_REQUEST = 1;// The request code
    static final int PICK_State_REQUEST = 2;// The request code
    static final int PICK_BONUS_REQUEST = 4;// The request code

    static final int PICK_Country_REQUEST = 3;
    private ImageLoader mImageLoader;
    FBStoresItem selectedStore;
    CheckBox check_button;
    Button bt1;

    EditText name, email, phone, address, password;

    TextView firstText, tx1, tx2, skipsign,textviewbonus;

     ProgressBarHandler progressBarHandler;
    // TransparentProgressDialog pd;
    LinearLayout scrollViewLinearLayout;
    Calendar myCalendar = Calendar.getInstance();
    String countrycode;

    public EditText FirstName;

    public EditText LastName;

    public EditText Address;

    public EditText ZipCode;

    public EditText FavoriteStore;

    public EditText City;

    public EditText Bonus;

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
    NetworkImageView imlogo;

    RelativeLayout mtoolbar;
    String rewardRule, ruleId;
    ArrayList<Bonus> bonusRuleList = new ArrayList<Bonus>();
    Button editText, editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // imlogo = (NetworkImageView) findViewById(R.id.im_logo);


        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });


        bt1 = (Button) findViewById(R.id.getStarted);
        bt1.setOnClickListener(SignUpActivity.this);

        background = (NetworkImageView) findViewById(R.id.img_Back);

        scrollViewLinearLayout = (LinearLayout) findViewById(R.id.scrollViewLinearLayout);
        firstText = (TextView) findViewById(R.id.firstText);
        tx1 = (TextView) findViewById(R.id.text_terms);
        tx1.setOnClickListener(this);
        tx2 = (TextView) findViewById(R.id.text_privacy);
        tx2.setOnClickListener(this);

        skipsign = (TextView) findViewById(R.id.skipsign);
        skipsign.setOnClickListener(this);
          progressBarHandler=new ProgressBarHandler(this);
        // pd = new TransparentProgressDialog(this);
        configureField();
        FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.SIGN_UP_START);


       /* check_button = (CheckBox) findViewById(R.id.check_button);
        check_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(buttonView.isChecked())
                {
                    bt1.setEnabled(true);

                }
                else{
                    bt1.setEnabled(true);
                }

            }
        }
        );
*/
//        backbutton = (ImageView) findViewById(R.id.backbutton);
//        backbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignUpActivity.this.finish();
//            }
//        });
    }


    public void onCustomBackPressed() {
        SignUpActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {

            //   bt1.setBackgroundColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));

//            bt1.setBackgroundResource(R.drawable.normal);
//            GradientDrawable gd = (GradientDrawable) bt1.getBackground().getCurrent();
//            gd.setColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));
//            gd.setCornerRadii(new float[]{100, 100, 100, 100, 100, 100, 100, 100});
//            gd.setStroke(1, Color.parseColor("#444444"), 0, 0);
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
            //Image URL - This can point to any image file supported by Android
            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
            //   background.setImageUrl(url, mImageLoader);

//            final String url2 = "http://" + FBViewMobileSettingsService.sharedInstance().companyLogoImageUrl;
//            mImageLoader.get(url2, ImageLoader.getImageListener(imlogo, R.drawable.logo, android.R.drawable
//                    .ic_dialog_alert));
//
//            imlogo.setImageUrl(url2, mImageLoader);
        }

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }

    };


    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DOB.setText(sdf.format(myCalendar.getTime()));
    }

    public void configureField() {

      Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;
        ArrayList<RegisterField> registerFields = null;

        for (RegisterField registerfield : registerFields) {

            if (registerfield.visible && registerfield.field.equalsIgnoreCase("FirstName"))
            {
                creatTextField("FirstName", registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("LastName"))
            {
                creatTextField("LastName", registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Password")) {
                creatTextField("Password", registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Address"))
            {
                creatTextField("Address", registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("ZipCode"))
            {
                creatTextField("ZipCode", registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("FavoriteStore"))
            {
                creatTextField("Favourite Location", registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("City"))
            {
                creatTextField("City", registerfield.configDisplayLabel);
            }

            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Gender"))
            {
                creatRadioGroup("Gender");
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("EmailAddress")) {
                creatTextField("EmailAddress", registerfield.configDisplayLabel);
            }



            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("DOB")) {
                final EditText editText = creatTextField("DOB", registerfield.configDisplayLabel);
                editText.setHint("MM/dd/yyyy");
                editText.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        DatePickerDialog mDatePicker = new DatePickerDialog(SignUpActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH));

                        mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                        mDatePicker.show();
                    }
                });
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("State")) {
                creatTextField("State", registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Country")) {
                creatTextField("Country", registerfield.configDisplayLabel);
            }

            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("PhoneNumber")) {
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

            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("SMSOptIn")) {
                creatRadioButton("SMSOptIn",registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("EmailOptIn")) {
                creatRadioButton("EmailOptIn",registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("EmailVerify")) {
                creatRadioButton("EmailVerify",registerfield.configDisplayLabel);
            }
            else if (registerfield.visible && registerfield.field.equalsIgnoreCase("PushOptin")) {
                creatRadioButton("PushOptin",registerfield.configDisplayLabel);
            }
           /* else if (registerfield.visible && registerfield.field.equalsIgnoreCase("Bonus")) {
                getBonusRuleList("Bonus");

            }*/
            /*if (activeFields.Bonus)
                creatTextField("Bonus");*/
        }
    }


    public EditText creatTextField(String name, String desc) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, this), 0, FBUtility.inDp(8, this));
        view.setLayoutParams(layoutParams);

        // ImageView imageName = (ImageView) view.findViewById(R.id.imageView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textview = (TextView) view.findViewById(R.id.field_text);

        EditText editText = (EditText) view.findViewById(R.id.editText);
        if (name.equals("LastName")) {
            textview.setText(desc);
            LastName = editText;
            LastName.setInputType(InputType.TYPE_CLASS_TEXT);
            LastName.setMaxLines(1);

        }
        if (name.equals("FirstName")) {
            textview.setText(desc);
            FirstName = editText;
            FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
            FirstName.setMaxLines(1);
        }

        if (name.equals("Address")) {
            textview.setText(desc);
            Address = editText;
            Address.setInputType(InputType.TYPE_CLASS_TEXT);
            Address.setMaxLines(1);

        }
        if (name.equals("ZipCode")) {
            textview.setText(desc);
            ZipCode = editText;
            ZipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
            ZipCode.setMaxLines(1);

        }
        if (name.equals("Favourite Location")) {
            textview.setText(desc);
            FavoriteStore = editText;
            FavoriteStore.setInputType(InputType.TYPE_CLASS_TEXT);
            FavoriteStore.setMaxLines(1);

            FavoriteStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SignUpActivity.this, SearchStoreActivity.class);
                    startActivityForResult(i, PICK_STORE_REQUEST);
                }
            });
        }
        if (name.equals("City")) {
            textview.setText(desc);
            City = editText;
            City.setInputType(InputType.TYPE_CLASS_TEXT);
            City.setMaxLines(1);

        }
        if (name.equals("EmailAddress")) {
            textview.setText(desc);
            EmailAddress = editText;
            EmailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EmailAddress.setMaxLines(1);

        }
        if (name.equals("DOB")) {
            textview.setText(desc);
            DOB = editText;
            DOB.setInputType(InputType.TYPE_CLASS_DATETIME);
            DOB.setFocusable(false);
            DOB.setMaxLines(1);
        }

        if (name.equals("Country")) {
            textview.setText(desc);
            Country = editText;
            Country.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            Country.setMaxLines(1);
            Country.setFocusable(false);
            Country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SignUpActivity.this, CountryActivity.class);
                    startActivityForResult(i, PICK_Country_REQUEST);
                }
            });
        }
        if (name.equals("State")) {
            textview.setText(desc);
            State = editText;
            State.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            State.setMaxLines(1);
            State.setFocusable(false);
            State.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtilities.isValidString(countrycode)) {
                        Intent i = new Intent(SignUpActivity.this, ActivityState.class);
                        i.putExtra("CountryCode", countrycode);
                        startActivityForResult(i, PICK_State_REQUEST);
                    } else {
                        logout(SignUpActivity.this);
                    }
                }
            });
        }


        if (name.equals("PhoneNumber")) {
            textview.setText(desc);

            PhoneNumber = editText;
            PhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            PhoneNumber.setMaxLines(1);

        }
        if (name.equals("Bonus")) {
            textview.setText(desc);
            Bonus = editText;
            Bonus.setInputType(InputType.TYPE_CLASS_TEXT);
            Bonus.setMaxLines(1);
            Bonus.setFocusable(false);

            Bonus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SignUpActivity.this, BonusActivity.class);
                    //startActivity(i);
                    startActivityForResult(i, PICK_BONUS_REQUEST);
                }
            });
        }

        if (name.equals("Password")) {
            textview.setText(desc);
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


    public void setImage(ImageView image, String mDrawableName) {
        mDrawableName = mDrawableName.toLowerCase();
        Resources res = getResources();
        int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        image.setImageDrawable(drawable);
    }





/*
    public void creatSpiner(String name){

        LinearLayout view;
        LayoutInflater  mInflater = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout)mInflater.inflate(R.layout.spinnerfield, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8,this),0,FBUtility.inDp(8,this));
        view.setLayoutParams(layoutParams);

        Spinner spinner=(Spinner)view.findViewById(R.id.spinner);
        final FBStoreService all= FBStoreService.sharedInstance();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(all.allStoresAfterSort.size()>0&&all.allStoresAfterSort!=null){
                    selectedStore=all.allStoresAfterSort.get(position);
                }
                Intent i=new Intent(SignUp_Activity.this,SearchStore_Activity.class);
                startActivity(i);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List categories = new ArrayList();

        if(all.allStoresAfterSort.size()>0) {
            for (int i = 0; i < 70; i++) {
                FBStoresItem store = all.allStoresAfterSort.get(i);
                categories.add(store.storeName);
            }
        }

        FavoriteStore=spinner;

        // Creating adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        TextView editText= (TextView)view.findViewById(R.id.textView);
        editText.setText(name);

        scrollViewLinearLayout.addView(view);
    }
*/


    public void creatRadioGroup(String name) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.male_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, this), 0, FBUtility.inDp(8, this));
        view.setLayoutParams(layoutParams);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);


        if (name.equals("Gender")) {
            Gender = radioGroup;
        }

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("", "");
            }
        });


        TextView editText = (TextView) view.findViewById(R.id.textView);
        editText.setText(name);

        scrollViewLinearLayout.addView(view);
    }

    public void creatRadioButton(String name,String desc) {

        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.radio_field, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, this), 0, FBUtility.inDp(8, this));
        view.setLayoutParams(layoutParams);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        TextView textview = (TextView) view.findViewById(R.id.field_text);

        if (name.equals("SMSOptIn")) {
            SMSOptIn = checkBox;
            textview.setText("SMSOptIn");

        }

        if (name.equals("EmailOptIn")) {
            EmailOptIn = checkBox;
            textview.setText("EmailOptIn");
        }
        if (name.equals("EmailVerify")) {
            EmailVerify = checkBox;
            textview.setText("EmailVerify");
        }
        if (name.equals("PushOptin")) {
            PushOptin = checkBox;
            textview.setText("PushOptin");
        }


        TextView editText = (TextView) view.findViewById(R.id.textView);
        editText.setText(name);

        scrollViewLinearLayout.addView(view);
    }


    @Override
    public void onClick(View btnClk) {

        if (btnClk.getId() == R.id.getStarted) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Registration");

            // Setting Dialog Message
            alertDialog.setMessage("Press ok to submit your detail.");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_launcher);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    createMember();
                }
            });

            // Showing Alert Message
            alertDialog.show();


        }
        if (btnClk.getId() == R.id.text_terms) {
            Intent i = new Intent(this, TermsConditionActivity.class);
            startActivity(i);
        }
        if (btnClk.getId() == R.id.text_privacy) {
            Intent i = new Intent(this, PrivacyLink.class);
            startActivity(i);
        }

        if (btnClk.getId() == R.id.skipsign) {
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
        }
    }


    public boolean checkValidation() {


        if (Password.getText().toString().length() < 6) {
            FBUtils.showAlert(this, "Minimum digit required six.");
            return false;
        }

        return true;
    }

    /*public void createMemberPreet() {

        FBCustomerItem customer=new FBCustomerItem();

        try {

            Field activeFields= FBViewMobileSettingsService.sharedInstance().activeFields;

            if(activeFields.FirstName)
                customer.firstName=FirstName.getText().toString();

            if(activeFields.LastName)
                customer.lastName=LastName.getText().toString();

            if(activeFields.EmailAddress)

                customer.emailID=EmailAddress.getText().toString();

            if(activeFields.EmailOptIn) {
                if(EmailOptIn.isChecked())
                customer.emailOpted = "true";
                else
                    customer.emailOpted = "false";
            }

           // if(activeFields.EmailVerify)
             //   object.put("emailOptIn",EmailVerify.isChecked());

            if(activeFields.PushOptin)
            {
                if(PushOptin.isChecked())
                    customer.pushOpted = "true";
                else
                    customer.pushOpted = "false";
            }


            if(activeFields.PhoneNumber)
                customer.cellPhone=PhoneNumber.getText().toString();

            if(activeFields.SMSOptIn){
                if(SMSOptIn.isChecked())
                    customer.smsOpted = "true";
                else
                    customer.smsOpted = "false";
            }

            if(activeFields.Address)
                customer.addressLine1 =Address.getText().toString();

            if(activeFields.FavoriteStore)
                customer.favoriteDepartment=FavoriteStore.getText().toString();

            if(activeFields.State)
                customer.addressState=State.getText().toString();

            if(activeFields.City)
                customer.addressCity=City.getText().toString();

            if(activeFields.ZipCode)
                customer.addressZip=ZipCode.getText().toString();

            if(activeFields.DOB)
                customer.dateOfBirth=DOB.getText().toString();

            if(activeFields.Gender){
                int radioButtonID = Gender.getCheckedRadioButtonId();
                RadioButton radioButton =(RadioButton) Gender.findViewById(radioButtonID);
                customer.customerGender=radioButton.getText().toString();

            }

            if(activeFields.Password)
                customer.loginPassword=Password.getText().toString();
            customer.deviceId=FBUtility.getAndroidDeviceID(this);


            //object.put("sendWelcomeEmail","ss");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

*/

    public void createMember() {
        //  checkValidation();
        JSONObject object = new JSONObject();
        try {

            com.fishbowl.basicmodule.Services.Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;


            if (activeFields.LastName)
                object.put("lastName", LastName.getText().toString());

            if (activeFields.FirstName)
                object.put("firstName", FirstName.getText().toString());

            if (activeFields.EmailAddress)
                object.put("email", EmailAddress.getText().toString());

            if (activeFields.EmailOptIn)
                object.put("emailOptIn", EmailOptIn.isChecked());

            if (activeFields.EmailVerify)
                object.put("emailOptIn", EmailVerify.isChecked());
            if (activeFields.PushOptin)
                object.put("emailOptIn", PushOptin.isChecked());

            if (activeFields.PhoneNumber)
                object.put("phone", PhoneNumber.getText().toString());

            if (activeFields.SMSOptIn)
                object.put("smsOptIn", SMSOptIn.isChecked());

            if (activeFields.Address)
                object.put("addressStreet", Address.getText().toString());

            if (activeFields.FavoriteStore)
                object.put("storeCode", selectedStore.getStoreNumber());

            //object.put("favoriteStore",selectedStore.getStoreID());


            if (activeFields.Country)
                object.put("country", Country.getText().toString());

            if (activeFields.State)
                object.put("addressState", State.getText().toString());

            if (activeFields.City)
                object.put("addressCity", City.getText().toString());

            if (activeFields.ZipCode)
                object.put("addressZipCode", ZipCode.getText().toString());

            if (activeFields.DOB)
                object.put("birthDate", DOB.getText().toString());

            if (activeFields.Gender) {
                int radioButtonID = Gender.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
                object.put("gender", radioButton.getText());
            }

            if (activeFields.Password)
                object.put("password", Password.getText().toString());

            object.put("deviceId", FBUtility.getAndroidDeviceID(this));

         /*   if (activeFields.Bonus) {
                object.put("rewardRule", rewardRule);
                object.put("ruleId", ruleId);
            }*/

            Date d = Calendar.getInstance().getTime();

            String format = null;
            if (format == null)
                format = "yyyy-MM-dd'T'hh:mm:ss";

            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String currentData = formatter.format(d);

            object.put("eventDateTime", currentData);


            //object.put("sendWelcomeEmail","ss");
        } catch (Exception e) {
            e.printStackTrace();
        }

             progressBarHandler.show();
        //  pd.show();
        FBUserService.sharedInstance().createMember(object, new FBUserService.FBCreateMemberCallback() {
            @Override
            public void onCreateMemberCallback(JSONObject response, Exception error) {
                  progressBarHandler.hide();
                //  pd.dismiss();
                if (response != null) {
                    try {
                        //FBUserService.sharedInstance().access_token = response.getString("access_token");
                        //    String secratekey=response.getString("accessToken");
                        //   FBPreferences.sharedInstance(SignUpActivity.this).setAccessTokenforapp(secratekey);
                        //   FBUserService.sharedInstance().access_token = response.getString("accessToken");
                        Intent ii = new Intent(SignUpActivity.this, SignInActivity.class);
                        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(ii);
                        FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.SIGN_UP_COMPLETE);
                        SignUpActivity.this.finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    FBUtils.tryHandleTokenExpiry(SignUpActivity.this, error);
                }

            }
        });
    }

    public static void logout(final Activity activity) {


        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity).create();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                rewardRule = (String) data.getSerializableExtra("rewardRule");
                ruleId = (String) data.getSerializableExtra("ruleId");
                String desc = (String) data.getSerializableExtra("desc");
                Bonus.setText(desc);

            }


        }
    }
    public void getBonusRuleList(final String editText) {
        //  progressBarHandler.show();
        final JSONArray object = new JSONArray();
        FBUserService.sharedInstance().getBonusRuleList(object, new FBUserService.FBBonusRuleListCallback() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onBonusRuleListCallback(JSONArray response, Exception error) {
                try {
                    if (response != null) {
                        if (response.length() > 0) {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject myBonusObj = response.getJSONObject(i);
                         Bonus bonusObj = new Bonus(myBonusObj);
                                bonusRuleList.add(bonusObj);

                            }

                            if (bonusRuleList.size() > 1) {
                                creatTextFieldforbonus();
                            }
                        }
                    } else {
                        FBUtils.tryHandleTokenExpiry(SignUpActivity.this, error);

                    }
                } catch (Exception e) {

                }
            }
        });
    }
    public Button creatTextFieldforbonus() {

        final LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.signup_button, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, FBUtility.inDp(8, this), 0, FBUtility.inDp(8, this));
        view.setLayoutParams(layoutParams);

        editText = (Button) view.findViewById(R.id.btn_Text);
        textviewbonus = (TextView) view.findViewById(R.id.bonusText);
        editText.setOnClickListener(this);
        editText1 = (Button) view.findViewById(R.id.btn_Text1);
        editText1.setOnClickListener(this);

        if (bonusRuleList != null) {
            if (bonusRuleList.size() > 0) {

                if (bonusRuleList.size() > 0 && bonusRuleList.size() == 1) {

                } else {
                    textviewbonus.setText("Select your Signup Bonus");
                    editText.setText(bonusRuleList.get(0).getDescription());
                    editText1.setText(bonusRuleList.get(1).getDescription());

                }
            }
        }


        scrollViewLinearLayout.addView(view);

        return editText;

    }

}


