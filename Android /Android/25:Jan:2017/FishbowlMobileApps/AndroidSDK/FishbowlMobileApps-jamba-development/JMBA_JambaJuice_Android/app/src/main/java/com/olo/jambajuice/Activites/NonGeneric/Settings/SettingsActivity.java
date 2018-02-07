package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.animation.ArgbEvaluator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.journeyapps.barcodescanner.Util;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreDetail.StoreDetailActivity;
import com.olo.jambajuice.Adapters.AvatarPagerAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserLogoutCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UserUpdateCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.OfferService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.SharedPreferenceHandler;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CapitalizeTextWatcher;
import com.viewpagerindicator.CirclePageIndicator;
import com.wearehathway.apps.incomm.Services.InCommService;

import org.json.JSONObject;

import java.util.Calendar;

import static com.olo.jambajuice.Utils.Constants.AVATAR_ICONS_BG;
import static com.olo.jambajuice.Utils.Constants.BROADCAST_UPDATE_HOME_ACTIVITY;
import static com.olo.jambajuice.Utils.Constants.B_IS_STORE_DETAIL_ONLY;
import static com.olo.jambajuice.Utils.Constants.B_STORE;
import static com.olo.jambajuice.Utils.Constants.SPENDGO_SIGNUP_AGE_LIMIT;

public class SettingsActivity extends BaseActivity implements UserLogoutCallback, View.OnClickListener, View.OnFocusChangeListener, TextWatcher, ViewPager.OnPageChangeListener, UserUpdateCallback, CompoundButton.OnCheckedChangeListener {
    View signedView;
    Button saveButton;
    int yearNow, monthNow, dayNow;
    DatePickerDialog datePickerDialog;
    ViewPager pager;
    boolean isChanged = false;
    ArgbEvaluator argbEvaluator;
    Boolean pushOpt = false;
    private SwitchCompat pushSwitch;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;
        setUpToolBar(true);
        isSlideDown = true;
        setUpView();
        isShowBasketIcon = false;
        setTitle("Settings");
        setBackButton(false, false);
        TextView copyRightText = (TextView) findViewById(R.id.copyRightText);
        Calendar calendar = Calendar.getInstance();
        copyRightText.setText("Copyright \u00a9 " + calendar.get(Calendar.YEAR) + " Jamba\u00AE, Inc.");
        if (UserService.isUserAuthenticated()) {
            setUpSignedView();
        }
    }

    private void setUpView() {
        Button tos = (Button) findViewById(R.id.termandprivacy);
        saveButton = (Button) findViewById(R.id.saveButton);
        if (!UserService.isUserAuthenticated()) {
            saveButton.setVisibility(View.GONE);
        }
        Button sendFeedback = (Button) findViewById(R.id.sendFeedback);
        enableSaveButton(false);
        sendFeedback.setOnClickListener(this);
        tos.setOnClickListener(this);
        TextView versionName = (TextView) findViewById(R.id.versionName);
        versionName.setText("Version: " + Utils.getVersionNumber());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Do not show setting button on this screen.
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logOutBtn:
                trackButton(v);
                showBasketAlertIfAvailableOrLogout();
                break;
            case R.id.changePasswordBtn:
                trackButton(v);
                TransitionManager.transitFrom(this, ChangePasswordActivity.class);
                break;
            case R.id.email:
                trackButtonWithName("Update Email");
                TransitionManager.transitFrom(this, ChangeEmailActivity.class);
                break;
            case R.id.dob:
                trackButtonWithName("Update DOB");
                openDatePickerDialog();
                break;
            case R.id.preferredStoreView:
                trackButtonWithName("Preferred Store Detail");
                showPreferredStoreView();
                break;
            case R.id.manageCardView:
                showCardsOnFile();
                break;
            case R.id.saveButton:
                trackButtonWithName("Save");
                saveButtonPressed();
                break;
            case R.id.contactNumber:
                trackButtonWithName("Update Contact");
                TransitionManager.transitFrom(this, ChangePhoneNumberActivity.class);
                break;
            case R.id.termandprivacy:
                trackButton(v);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.B_IS_OPENING_FROM_SETTING, true);
                TransitionManager.transitFrom(this, TermsAndPrivacyActivity.class, bundle);
                break;
            case R.id.sendFeedback:
                TransitionManager.transitFrom(this, FeedbackTypeActivity.class);
                break;
        }
    }

    private void showBasketAlertIfAvailableOrLogout() {
        final Context ctx = this;
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setCancelable(false);
        alertdialog.setTitle("Hold on a second...");
        alertdialog.setNegativeButton("Cancel", null).setIcon(android.R.drawable.ic_dialog_alert);
        if (DataManager.getInstance().isBasketPresent()) {
            alertdialog.setMessage("Your existing basket will be lost upon logging out.").setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
        } else {
            alertdialog.setMessage("Are you sure you want to log out?").setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
        }
        alertdialog.show();
    }

    private void logout() {
        JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.LOGOUT);
        disableUserInteraction(true);
        BasketFlagViewManager.getInstance().removeBasketFlag();
        UserService.logout(this);
        User user = UserService.getUser();

    }

    private void saveButtonPressed() {
        hideKeyboard();
        updateImage();
        saveUserInformation();
    }

    private void showPreferredStoreView() {
        User user = UserService.getUser();
        Store store = new Store();
        store.setName(user.getStoreName());
        store.setLatitude(user.getLatitude());
        store.setLongitude(user.getLongitude());
        store.setStreetAddress(user.getStoreAddress());
        store.setTelephone(user.getStoreTelephoneNumber());
        int restId = user.getOloFavoriteRestaurantId();
        store.setRestaurantId(restId);
//        if (restId > 0) {
//            store.setSupportsOrderAhead(true);
//        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(B_IS_STORE_DETAIL_ONLY, true);
        bundle.putSerializable(B_STORE, store);
        bundle.putBoolean(Constants.B_IS_SHOW_BASKET, true);
        bundle.putBoolean(Constants.B_IS_FROM_SETTINGS, true);
        TransitionManager.transitFrom(this, StoreDetailActivity.class, bundle);
    }

    private void showCardsOnFile() {
        TransitionManager.slideUp(SettingsActivity.this, BillingAccountsActivity.class);
    }

    @Override
    public void onBackPressed() {
        TransitionManager.slideUp(SettingsActivity.this, HomeActivity.class, true);
    }

    @Override
    public void onUserLogoutCallback(Exception exception) {
        UserService.setUser(null);
        DataManager.getInstance().resetDataManager();
        GiftCardDataManager.getInstance().resetGiftCardDataManager();
        InCommService.clearSession();
        SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, "");
        BasketFlagViewManager.getInstance().removeBasketFlag();
        disableUserInteraction(false);
        Utils.notifyHomeScreenUpdateAndTransitBack(this); // No need to notify any exception. Clear user session
        OfferService.clearOfferList();
        JSONObject jsonObject = new JSONObject();
        FBUserService.sharedInstance().logout(jsonObject, new FBUserService.FBLogoutCallback() {
            @Override
            public void onLogoutCallback(JSONObject response, Exception error) {
                JambaApplication.getAppContext().clearMemberId();
                FBPreferences.sharedInstance(getApplicationContext()).clearPreferences();
            }
        });
    }

    @Override
    public void onUserUpdateCallback(Exception exception) {
        if (exception != null) {
            Utils.showErrorAlert(this, exception);
        } else {
            //Update CLP Customer on updating information
            User user = UserService.getUser();
            user.setEnablePushOpt(pushOpt);
            updateUserData();
            Toast.makeText(this, "Information updated successfully", Toast.LENGTH_SHORT).show();
            ((JambaApplication) this.getApplication()).updateCustomerInfo(user);
        }
        disableUserInteraction(false);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        TextView editText = (TextView) v;
        if (editText.getHint() != null) {
            String text = editText.getText().toString();
            String hintText = editText.getHint().toString();
            if (text.equals("") || text.equals(hintText)) {
                if (hasFocus) {
                    editText.setText(hintText);
                } else {
                    editText.setText("");
                }
            } else {
                setSaveItemState();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setSaveItemState();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (argbEvaluator == null) {
            argbEvaluator = new ArgbEvaluator();
        }
        if (position < (pager.getAdapter().getCount() - 1) && position < (AVATAR_ICONS_BG.length - 1)) {
            Integer color1 = getResources().getColor(AVATAR_ICONS_BG[position]);
            Integer color2 = getResources().getColor(AVATAR_ICONS_BG[position + 1]);
            pager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, color1, color2));
        } else {
            Integer color1 = getResources().getColor(AVATAR_ICONS_BG[AVATAR_ICONS_BG.length - 1]);
            pager.setBackgroundColor(color1);
        }
        setSaveItemState();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserData();// Incase user has changed his email address update UI.
    }

    private void updateImage() {
        ViewPager pager = (ViewPager) signedView.findViewById(R.id.viewpager);
        if (UserService.getUser().getAvatarId() != pager.getCurrentItem()) {
            UserService.updateAvatarId(pager.getCurrentItem());
            notifyHomeScreenUpdate();
        }
        setSaveItemState();
    }

    private void saveUserInformation() {
        if (isChanged) {
            EditText fName = (EditText) signedView.findViewById(R.id.firstName);
            EditText lName = (EditText) signedView.findViewById(R.id.lastName);
            SwitchCompat emailSwitch = (SwitchCompat) signedView.findViewById(R.id.emailSwitch);
            SwitchCompat smsSwitch = (SwitchCompat) signedView.findViewById(R.id.smsSwitch);
            SwitchCompat pushSwitch = (SwitchCompat) signedView.findViewById(R.id.pushSwitch);

            final String firstName = fName.getText().toString();
            final String lastName = lName.getText().toString();
            String dob = "";
            if (yearNow > 0) // User has changed his dob
            {
                dob = Utils.getBirthdayInSpendGoFormat(yearNow, monthNow, dayNow);
            }
            if (fName.getText().length() < 3) {
                if (fName.getText().length() != 0) {
                    fName.setError("Enter your first name");
                    fName.requestFocus();
                    return;
                }
            }
            if (lName.getText().length() < 3) {
                if (lName.getText().length() != 0) {
                    lName.setError("Enter your last name");
                    lName.requestFocus();
                    return;
                }
            }
            disableUserInteraction(true);
            UserService.updateUserInformation(firstName, lastName, smsSwitch.isChecked(), emailSwitch.isChecked(), dob, this);
        }
    }

    private void setUpSignedView() {
        FrameLayout containerView = (FrameLayout) findViewById(R.id.containerView);
        LayoutInflater inflater = LayoutInflater.from(this);
        signedView = inflater.inflate(R.layout.setting_signed_in, containerView, false);
        containerView.addView(signedView);
        Button logOutBtn = (Button) findViewById(R.id.logOutBtn);
        Button changePassword = (Button) findViewById(R.id.changePasswordBtn);
        Button changeEmail = (Button) findViewById(R.id.email);
        Button termandprivacy = (Button) findViewById(R.id.termandprivacy);

        SwitchCompat emailSwitch = (SwitchCompat) signedView.findViewById(R.id.emailSwitch);
        SwitchCompat smsSwitch = (SwitchCompat) signedView.findViewById(R.id.smsSwitch);
        SwitchCompat pushSwitch = (SwitchCompat) signedView.findViewById(R.id.pushSwitch);
        RelativeLayout preferredStoreView = (RelativeLayout) signedView.findViewById(R.id.preferredStoreView);
        RelativeLayout manageCardView = (RelativeLayout) signedView.findViewById(R.id.manageCardView);
        pager = (ViewPager) signedView.findViewById(R.id.viewpager);
        EditText fName = (EditText) signedView.findViewById(R.id.firstName);
        EditText lName = (EditText) signedView.findViewById(R.id.lastName);
        fName.addTextChangedListener(new CapitalizeTextWatcher(fName));
        lName.addTextChangedListener(new CapitalizeTextWatcher(lName));

        logOutBtn.setVisibility(View.VISIBLE);
        logOutBtn.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        changeEmail.setOnClickListener(this);
        preferredStoreView.setOnClickListener(this);
        manageCardView.setOnClickListener(this);
        termandprivacy.setOnClickListener(this);
        emailSwitch.setOnCheckedChangeListener(this);
        smsSwitch.setOnCheckedChangeListener(this);
        pushSwitch.setOnCheckedChangeListener(this);
        //updateUserData();
        setUpUserAvatar();
        fetchUserProfile();
    }

    private void fetchUserProfile() {
        UserService.getUserProfileInformation(this, new UserServiceCallback() {
            @Override
            public void onUserServiceCallback(User user, Exception exception) {
                if (exception == null) {
                    user.setEnablePushOpt(FBPreferences.sharedInstance(getApplicationContext()).IsPushOptIn());
                    updateUserData();
                } else {
                    Utils.tryHandlingAuthTokenExpiry(SettingsActivity.this, exception);
                }
            }
        });
    }

    private void updateUserData() {
        if (!UserService.isUserAuthenticated()) {
            return;
        }
        EditText fName = (EditText) signedView.findViewById(R.id.firstName);
        EditText lName = (EditText) signedView.findViewById(R.id.lastName);
        Button email = (Button) signedView.findViewById(R.id.email);
        SwitchCompat emailSwitch = (SwitchCompat) signedView.findViewById(R.id.emailSwitch);
        SwitchCompat smsSwitch = (SwitchCompat) signedView.findViewById(R.id.smsSwitch);
        SwitchCompat pushLocalSwitch = (SwitchCompat) signedView.findViewById(R.id.pushSwitch);
        TextView storeName = (TextView) signedView.findViewById(R.id.storeName);
        TextView storeAddress = (TextView) signedView.findViewById(R.id.storeAddress);

        fName.setOnFocusChangeListener(this);
        lName.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);

        fName.addTextChangedListener(this);
        lName.addTextChangedListener(this);
        email.addTextChangedListener(this);

        fName.setText("");
        lName.setText("");
        email.setText("");

        User user = UserService.getUser();
        fName.setHint(user.getFirstname());
        lName.setHint(user.getLastname());
        email.setHint(user.getEmailaddress());

        emailSwitch.setChecked(user.isEnableEmailOpt());
        smsSwitch.setChecked(user.isEnableSmsOpt());
        pushLocalSwitch.setChecked(user.isEnablePushOpt());
        if (user.getOloFavoriteRestaurantId() > 0) {
            if (DataManager.getInstance().isDebug) {
                storeName.setText(Utils.getDemoStoreName());
            } else {
                storeName.setText(user.getStoreName().replace("Jamba Juice ", ""));
            }
            storeAddress.setText(user.getStoreAddress());
        } else {
            storeName.setText("No preferred store selected");
            storeAddress.setVisibility(View.INVISIBLE);
        }
        updateDob();
        updateContactNumber();
        setSaveItemState();
        notifyHomeScreenUpdate();
    }

    private void openDatePickerDialog() {
        if (datePickerDialog != null) {
            datePickerDialog.cancel();
        }
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setViews(year, monthOfYear, dayOfMonth);
                datePickerDialog = null;
            }
        }, yearNow, monthNow, dayNow);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -SPENDGO_SIGNUP_AGE_LIMIT);
        datePickerDialog.getDatePicker().setMaxDate(now.getTimeInMillis());
        datePickerDialog.show();
    }

    private void setViews(int year, int monthOfYear, int dayOfMonth) {
        yearNow = year;
        monthNow = monthOfYear;
        dayNow = dayOfMonth;
        TextView dob = (TextView) findViewById(R.id.dob);
        dob.setText(Utils.getFormattedBirthdayString(yearNow, monthNow, dayNow));
    }

    private void updateDob() {
        User user = UserService.getUser();
        TextView dob = (TextView) signedView.findViewById(R.id.dob);
        dob.addTextChangedListener(this);
        dob.setOnClickListener(this);
        dob.setText("");
        if (user.getDob() != null) {
            String[] splitDob = user.getDob().split("-");
            yearNow = Integer.parseInt(splitDob[0]);
            monthNow = Integer.parseInt(splitDob[1]) - 1;
            dayNow = Integer.parseInt(splitDob[2]);
            dob.setHint(Utils.getFormattedBirthdayString(yearNow, monthNow, dayNow));
        }
    }

    private void updateContactNumber() {
        User user = UserService.getUser();
        Button contact = (Button) signedView.findViewById(R.id.contactNumber);
        contact.setOnClickListener(this);
        contact.setText("");
        contact.setHint(Utils.getFormattedPhoneNumber(user.getContactnumber()));
    }

    private void notifyHomeScreenUpdate() {
        Intent intent = new Intent(BROADCAST_UPDATE_HOME_ACTIVITY);
        intent.putExtra(Constants.B_IS_IT_FROM_SETTINGS_SCREEN, true);
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).sendBroadcast(intent);
    }

    private void disableUserInteraction(boolean isDisabled) {
        enableScreen(!isDisabled);
        isBackButtonEnabled = !isDisabled;
    }

    private void setUpUserAvatar() {
        AvatarPagerAdapter adapter = new AvatarPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) signedView.findViewById(R.id.viewpager);
        pager.setAdapter(adapter);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) signedView.findViewById(R.id.circlePageIndicator);
        circlePageIndicator.setViewPager(pager);
        circlePageIndicator.setOnPageChangeListener(this);
        pager.setCurrentItem(UserService.getUser().getAvatarId());
    }

    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        if (signedView != null) {
            EditText hiddenView = (EditText) signedView.findViewById(R.id.hiddenView);
            hiddenView.setFocusableInTouchMode(true);
            hiddenView.requestFocus();
        }
    }

    private boolean isChanged(TextView textView) {
        if (textView == null) {
            return false;
        } else if (textView.getHint() == null || !Utils.isEmptyString(textView.getText().toString()) && !textView.getText().toString().equals(textView.getHint().toString())) {
            return true;
        }
        return false;
    }

    private void setSaveItemState() {
        if (signedView == null || saveButton == null) {
            return;
        }
        EditText fName = (EditText) signedView.findViewById(R.id.firstName);
        EditText lName = (EditText) signedView.findViewById(R.id.lastName);
        TextView dob = (TextView) signedView.findViewById(R.id.dob);
        ViewPager pager = (ViewPager) signedView.findViewById(R.id.viewpager);
        SwitchCompat emailSwitch = (SwitchCompat) signedView.findViewById(R.id.emailSwitch);
        SwitchCompat smsSwitch = (SwitchCompat) signedView.findViewById(R.id.smsSwitch);
        pushSwitch = (SwitchCompat) signedView.findViewById(R.id.pushSwitch);

        pushOpt = pushSwitch.isChecked();

        if (isChanged(fName) || isChanged(lName) || isChanged(dob) || UserService.getUser().getAvatarId() != pager.getCurrentItem() || UserService.getUser().isEnableEmailOpt() != emailSwitch.isChecked() || UserService.getUser().isEnableSmsOpt() != smsSwitch.isChecked() || UserService.getUser().isEnablePushOpt() != pushSwitch.isChecked()) {
            enableSaveButton(true);
            isChanged = true;
        } else {
            enableSaveButton(false);
            isChanged = false;
        }
    }

    private void enableSaveButton(boolean isEnabled) {
        if (isEnabled) {
            saveButton.setTextColor(getResources().getColor(R.color.red_color));
            saveButton.setClickable(true);
        } else {

            saveButton.setTextColor(getResources().getColor(R.color.darker_gray));
            saveButton.setClickable(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        setSaveItemState();
    }

}
