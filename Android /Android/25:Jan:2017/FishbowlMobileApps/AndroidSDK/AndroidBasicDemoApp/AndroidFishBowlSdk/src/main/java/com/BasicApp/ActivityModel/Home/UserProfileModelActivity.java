package com.BasicApp.ActivityModel.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

import com.BasicApp.Activites.NonGeneric.Authentication.SignIn.SignInActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardActivity;
import com.BasicApp.Activites.NonGeneric.State.ActivityState;
import com.BasicApp.Activites.NonGeneric.Store.SearchStoreActivity;
import com.BasicApp.ActivityModel.Authentication.SignIn.ChangePasswordModelActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.OfferSummary;
import com.BasicApp.BusinessLogic.Models.RewardSummary;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBParseMember;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBMemberUpdateCallback;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Services.Field;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONObject;

import static com.basicmodule.sdk.R.id.imageView;


/**
 * Created by digvijay(dj)
 */
public class UserProfileModelActivity extends Activity implements View.OnClickListener

{
    static final int PICK_STORE_REQUEST = 1;  // The request code
    static final int PICK_State_REQUEST = 2;// The request code
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
    EditText lname, fname, email, dob;
    Button save;
    ProgressBarHandler progressBarHandler;
    LinearLayout scrollLinearLayout;
    FBParseMember member = null;
    NetworkImageView imlogo;
    NetworkImageView backgroundProfile, headerImage;
    LinearLayout logout_layout;
    //  Button logout;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // logout_layout=(LinearLayout) findViewById(R.id.logout_layout);
        backgroundProfile = (NetworkImageView) findViewById(R.id.backgroundProfile);
        //     logout_layout.setOnClickListener(this);

        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });
        mtoolbar.findViewById(R.id.logout_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


        change = (TextView) findViewById(R.id.changepass);
        change.setOnClickListener(this);
        scrollLinearLayout = (LinearLayout) findViewById(R.id.scrollLinearLayout);
        //  headerImage = (NetworkImageView) findViewById(R.id.headerImage);
        save = (Button) findViewById(R.id.save_profile);
        save.setOnClickListener(this);
        progressBarHandler = new ProgressBarHandler(this);
        member = FBSessionService.member;
      //  member = FBUserService.sharedInstance().member;
        configureField();
        //   imlogo = (NetworkImageView) findViewById(R.id.iv_logo);


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
                    Intent i = new Intent(UserProfileModelActivity.this, SearchStoreActivity.class);
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


    }

    public void onCustomBackPressed() {
        UserProfileModelActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;

            final String url2 = "http://" + FBViewMobileSettingsService.sharedInstance().companyLogoImageUrl;


        }

    }


    public void configureField() {

        if (member.firstName != null)
            creatTextField(member.firstName);

        if (member.lastName != null)
            creatTextField(member.lastName);

        if (member.addressLine1 != null)
            creatTextField(member.addressLine1);

        if (member.addressZip != null)
            creatTextField(member.addressZip);

        if (member.homeStoreID != null)
            creatTextField(member.homeStoreID);

        if (member.addressCity != null)
            creatTextField(member.addressCity);

        if (member.customerGender != null)
            creatRadioGroup(member.customerGender);

        if (member.emailID != null)
            creatTextField(member.emailID);

        if (member.dateOfBirth != null)
            creatTextField(member.dateOfBirth);

        if (member.addressState != null)
            creatTextField(member.addressState);

        if (member.cellPhone != null) {
            final EditText editText = creatTextField(member.cellPhone);


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

        if (member.smsOpted != null)
            creatRadioButton("SMSOptIn");

        if (member.emailOpted != null)
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

        if (name.equals(member.firstName)) {


            textview.setText("FirstName");
            FirstName = editText;
            FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
            FirstName.setMaxLines(1);
            //imageView.setBackgroundResource(R.drawable.logo);
        }
        if (name.equals(member.lastName)) {
            textview.setText("LastName");
            LastName = editText;
            LastName.setInputType(InputType.TYPE_CLASS_TEXT);
            LastName.setMaxLines(1);

        }
        if (name.equals(member.addressLine1)) {


            textview.setText("Address");
            Address = editText;
            Address.setInputType(InputType.TYPE_CLASS_TEXT);
            Address.setMaxLines(1);

        }
        if (name.equals(member.addressZip)) {
            textview.setText("ZipCode");
            ZipCode = editText;
            ZipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
            ZipCode.setMaxLines(1);

        }
        if (name.equals(member.homeStoreID)) {
            textview.setText("FavoriteStore");
            FavoriteStore = editText;
            FavoriteStore.setInputType(InputType.TYPE_CLASS_TEXT);
            FavoriteStore.setMaxLines(1);
            // int StoreId=FBStoreService.sharedInstance().mapNumToId.get(member.homeStoreID);
//            selectedStore = FBStoreService.sharedInstance().mapIdToStore.get(Integer.valueOf(member.homeStoreID));
//            FavoriteStore.setText(selectedStore.getStoreName());

        }
        if (name.equals(member.addressCity)) {
            textview.setText("City");
            City = editText;
            City.setInputType(InputType.TYPE_CLASS_TEXT);
            City.setMaxLines(1);

        }
        if (name.equals(member.emailID)) {
            textview.setText("EmailAddress");
            EmailAddress = editText;
            EmailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EmailAddress.setMaxLines(1);
        }
        if (name.equals(member.dateOfBirth)) {
            textview.setText("BirthDay");
            DOB = editText;
            DOB.setInputType(InputType.TYPE_CLASS_DATETIME);
            DOB.setFocusable(false);
            DOB.setMaxLines(1);

        }
        if (name.equals(member.addressState)) {

            textview.setText("State");
            State = editText;
            State.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            State.setMaxLines(1);
            State.setFocusable(false);
            State.setFocusable(false);
            State.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(UserProfileModelActivity.this, ActivityState.class);
                    startActivityForResult(i, PICK_State_REQUEST);
                }
            });

        }
        if (name.equals(member.cellPhone)) {

            textview.setText("PhoneNumber");

            PhoneNumber = editText;
            PhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            PhoneNumber.setMaxLines(1);


        }


        //setImage(imageName,name);

        scrollLinearLayout.addView(view);

        return editText;
    }


    public void setImage(ImageView image, String mDrawableName) {
        mDrawableName = mDrawableName.toLowerCase();
        Resources res = getResources();
        int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        image.setImageDrawable(drawable);
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
            SMSOptIn.setChecked(Boolean.valueOf(member.smsOpted));
        }

        if (name.equals("EmailOptIn")) {
            EmailOptIn = checkBox;
            TextView editText = (TextView) view.findViewById(R.id.textView);
            editText.setText("EmailOptIn");
            EmailOptIn.setChecked(Boolean.valueOf(member.emailOpted));
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

            // Setting Dialog Title
            alertDialog.setTitle("Update");

            // Setting Dialog Message
            alertDialog.setMessage("Member Updated Successfully");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.logo);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    memberUpdate();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }

    }

    public void memberUpdate() {

        JSONObject object = new JSONObject();
        try {
            Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;

            if (member.firstName != null)
                object.put("firstName", FirstName.getText().toString());

            if (member.lastName != null)
                object.put("lastName", LastName.getText().toString());

            if (member.emailID != null)
                object.put("email", EmailAddress.getText().toString());


            if (member.emailOpted != null)
                object.put("emailOptIn", EmailOptIn.isChecked());

            if (member.cellPhone != null)
                object.put("phone", PhoneNumber.getText().toString());

            if (member.smsOpted != null)
                object.put("smsOptIn", SMSOptIn.isChecked());


            if (member.addressLine1 != null)
                object.put("addressStreet", Address.getText().toString());

            if (member.favoriteDepartment != null)
                object.put("favoriteStore", FavoriteStore.getText().toString());

            if (member.homeStoreID != null) {
                if (selectedStore != null)
                    object.put("storeCode", selectedStore.getStoreNumber());
            }


            if (member.addressState != null)
                object.put("addressState", State.getText().toString());

            if (member.addressCity != null)
                object.put("addressCity", City.getText().toString());

            if (member.addressZip != null)
                object.put("addressZipCode", ZipCode.getText().toString());

            if (member.dateOfBirth != null)
                object.put("dob", DOB.getText().toString());


            if (member.customerGender != null) {
                int radioButtonID = Gender.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) Gender.findViewById(radioButtonID);
                object.put("gender", radioButton.getText());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        progressBarHandler.show();
        FBUserService.sharedInstance().memberUpdate(object, new FBMemberUpdateCallback() {
            @Override
            public void onMemberUpdateCallback(JSONObject response, Exception error) {
                progressBarHandler.hide();
                if (response != null) {
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE_IMAGE);
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.UPDATE_PROFILE);
                    getMember();
                } else {
                    FBUtils.tryHandleTokenExpiry(UserProfileModelActivity.this, error);

                }
            }
        });
    }




    public void createMemberModel() throws java.text.ParseException {

                progressBarHandler.show();
                FBMember customer = new FBMember();
                try {
                    Field activeFields = FBViewMobileSettingsService.sharedInstance().activeFields;

                    if (activeFields.FirstName) {
                        if (FBUtils.isValidString(FirstName.getText().toString())) {
                            // customer.put("firstName", FirstName.getText().toString());
                            customer.setFirstName(FirstName.getText().toString());
                        }
                    }

                    if (activeFields.LastName) {
                        if (FBUtils.isValidString(LastName.getText().toString())) {
                            // object.put("lastName", LastName.getText().toString());
                            customer.setLastName(LastName.getText().toString());
                        }
                    }

                    if (activeFields.EmailAddress) {
                        if (FBUtils.isValidString(EmailAddress.getText().toString())) {

                            // object.put("email", EmailAddress.getText().toString());
                            customer.setEmailAddress(EmailAddress.getText().toString());
                        }
                    }

                    if (activeFields.EmailOptIn)
                        customer.setEmailOptIn(EmailOptIn.isChecked());

//                if (activeFields.EmailVerify)
//                    object.put("emailOptIn", EmailVerify.isChecked());


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




//                    customer.setDeviceId(FBUtility.getAndroidDeviceID(SignUpNewActivity.this));
//
//                    Date d = Calendar.getInstance().getTime();
//
//                    String format = null;
//                    if (format == null)
//                        format = "yyyy-MM-dd'T'hh:mm:ss";
//
//                    SimpleDateFormat formatter = new SimpleDateFormat(format);
//                    String currentData = formatter.format(d);
//
//                    Calendar cal = Calendar.getInstance();
//
//                    int year= cal.get(Calendar.YEAR);
//                    int month= cal.get(cal.get(Calendar.MONTH)+1);
//                    int date= cal.get(Calendar.DATE);
//
//                    customer.setDate(String.valueOf(date));
//                    customer.setMonth(String.valueOf(month));
//                    customer.setYear(String.valueOf(year));


                    UpdateMember(customer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }










    public void UpdateMember(FBMember user) {

        FBSessionService.memberUpdate(user, new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(final FBSessionItem spendGoSession, Exception error) {
                if (error != null)

                {

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
                    String homeStoreID = user.getStoreCode();
                    FBPreferences.sharedInstance(UserProfileModelActivity.this).setStoreCode(homeStoreID);
                    FBPreferences.sharedInstance(UserProfileModelActivity.this).setStoreCode("47197");
                    Intent ii = new Intent(UserProfileModelActivity.this, DashboardActivity.class);
                    startActivity(ii);
                    FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.LOGIN);


                }
            }
        });
    }



    public void getMember() {

        JSONObject object = new JSONObject();
        progressBarHandler.show();
        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback() {
            public void onGetMemberCallback(JSONObject response, Exception error) {
                progressBarHandler.hide();
                if (response != null) {
                    finish();
                    Intent i = new Intent(UserProfileModelActivity.this, DashboardActivity.class);
                    startActivity(i);

                } else {
                    FBUtils.tryHandleTokenExpiry(UserProfileModelActivity.this, error);
                }
            }

        });

    }

    public void logout() {


        AlertDialog alertDialog = new AlertDialog.Builder(UserProfileModelActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Logout ");

        // Setting Dialog Message
        alertDialog.setMessage("Press Ok to Logout ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                JSONObject object = new JSONObject();
                try {
                    object.put("Application", "mobilesdk");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressBarHandler.show();
                FBUserService.sharedInstance().logout(object, new FBUserService.FBLogoutCallback() {
                    @Override
                    public void onLogoutCallback(JSONObject response, Exception error) {
                        progressBarHandler.hide();
                        if (response != null) {

                            Bundle extras = new Bundle();
                            Intent i = new Intent(UserProfileModelActivity.this, SignInActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                            i.putExtras(extras);

                            startActivity(i);
                            if (RewardSummary.rewardList != null) {
                                if (RewardSummary.rewardList.size() > 0) {
                                    RewardSummary.rewardList.clear();

                                }
                            }
                            if (OfferSummary.offerList != null) {
                                if (OfferSummary.offerList.size() > 0) {

                                    OfferSummary.offerList.clear();
                                }
                            }
                            FBPreferences.sharedInstance(UserProfileModelActivity.this).setSignin(false);
                            UserProfileModelActivity.this.finish();


                        } else {
                            FBUtils.tryHandleTokenExpiry(UserProfileModelActivity.this, error);

                        }
                    }
                });
            }
        });

        alertDialog.show();

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





}
