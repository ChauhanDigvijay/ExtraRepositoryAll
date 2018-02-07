package com.raleys.app.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.AccountRequest;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Store;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceError;
import com.raleys.libandroid.WebServiceListener;

public class AccountScreen_new extends BaseScreen implements WebServiceListener {
	private RaleysApplication _app;
	private boolean _registrationPage;
	private AccountRequest _accountRequest;
	private InputMethodManager _inputManager;
	private int _textColor;
	private int _defaultCornerRadius;
	private int _defaultPadding;
	private RelativeLayout _accountLayout;
	private WebService _service;

	private EditText _firstname;
	private EditText _lastname;
	private EditText _email;
	private EditText _password;
	private EditText _confirm_password;
	private ImageView _confirm_loyalty_check;
	private EditText _loyalty_number;
	private TextView _points_balance;
	private EditText _mobilephone;
	private EditText _homephone;
	private EditText _address;
	private EditText _city;
	private TextView _state;
	private EditText _zip;
	private ImageView _SE_rewards_check;
	private ImageView _NO_thanks_check;
	private TextView _FL_text;
	private TextView _FD_text;
	private ImageView _New_Offer_check;
	private ImageView _Hot_Offer_check;
	private ImageView _TC_rewards_check;

	public String DEFAULT_POINTS_TEXT = "Points Balance: ";

	final String INPUT_ERROR_TITLE_TEXT = "Input Error";
	final String STATE_DEFAULT_TEXT = "State";
	final String STORE_DEFAULT_TEXT = "Select Your Favorite Location";
	final String DEPARTMENT_DEFAULT_TEXT = "Select Your Favorite Department";

	final String[] _states = new String[] { "Alabama", "Alaska", "Arizona",
			"Arkansas", "California", "Colorado", "Connecticut", "Delaware",
			"DC", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois",
			"Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine",
			"Maryland", "Massachusetts", "Michigan", "Minnesota",
			"Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
			"New Hampshire", "New Jersey", "New Mexico", "New York",
			"North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon",
			"Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
			"Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
			"West Virginia", "Wisconsin", "Wyoming" };

	final String[] _departments = new String[] { "Fine Produce", "Fine Meats",
			"Fine Seafood", "Grocery & Household", "Wine, Beer & Spirits",
			"Recipes", "Deli Prepared Foods", "Fine Bakery", "New Items",
			"Natural & Organic Foods", "Pharmacy & Wellness" };
	// Newly Added
	RelativeLayout.LayoutParams layoutParams;
	int text_height, element_gap, marginGap, textSize;
	Context _accountContext;

	@Override
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		try {
			_accountContext = this;
			_app = (RaleysApplication) getApplication();
			int _width = _app.getScreenWidth();
			int _height = _app.getScreenHeight();

			_defaultCornerRadius = (int) (_width * .02);
			_defaultPadding = (int) (_width * .03);
			textSize = (int) (_headerHeight * 0.3);

			_registrationPage = this.getIntent().getBooleanExtra(
					"registrationPage", false);
			_accountRequest = new AccountRequest();
			_inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			_CreateAccountButton.setVisibility(View.VISIBLE); // Enabling Create
																// button
			// Get all store
			if (_app.getStoreList() == null || _app.getStoreList().size() == 0) {
				_app.getAllStores();
			}
			if (_registrationPage == false) {
				_accountRequest = _app._currentAccountRequest;
				_CreateAccountButton.setText("Update");
				_CreateAccountButton.setPadding(0, 0, 0, 0);//v2.3 fix for Lollipop
				_CreateAccountButton.setTextColor(Color.rgb(187, 0, 0));
				_CreateAccountButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						textSize);
				_CreateAccountButton.setTypeface(_normalFont);
				_headerImageView.setBackgroundDrawable(new BitmapDrawable(_app
						.getAppBitmap("myaccount", R.drawable.myaccount,
								_screenWidth, _headerHeight)));
			} else {
				_CreateAccountButton.setText("Create");
				_CreateAccountButton.setTextColor(Color.rgb(187, 0, 0));
				_CreateAccountButton.setPadding(0, 0, 0, 0);//v2.3 fix for Lollipop
				_CreateAccountButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						textSize);
				_CreateAccountButton.setTypeface(_normalFont);
				_headerImageView.setBackgroundDrawable(new BitmapDrawable(_app
						.getAppBitmap("signup", R.drawable.signup,
								_screenWidth, _headerHeight)));
			}
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// hides
																				// the
																				// keyboard
																				// on
																				// startup

			_CreateAccountButton.setVisibility(View.VISIBLE); // Enabling Create
																// button

			_CreateAccountButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					accountSubmission();
				}
			});

			// override base class listener so we can remove the keyboard if
			// it's visible when the button is pressed
			_backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
					finish();
				}
			});

			_textColor = Color.rgb(0, 0, 0);
			text_height = (int) (_headerHeight * 0.89);
			element_gap = 3;
			marginGap = (int) (_height * .02);

			// show progress dialog
			showProgressDialog("");
			Handler _accountHandler = new Handler();
			_accountHandler.postDelayed(new Runnable() {

				@Override
				public void run() {

					// don't use base class content area
					_accountLayout = new RelativeLayout(_accountContext);
					_accountLayout.setBackgroundColor(Color.LTGRAY);
					layoutParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = _headerHeight;
					layoutParams.leftMargin = 0;
					layoutParams.height = _contentViewHeight;
					layoutParams.width = _contentViewWidth;
					_mainLayout.addView(_accountLayout, layoutParams);

					ScrollView _scrollview = new ScrollView(_accountContext);
					_scrollview.setBackgroundColor(Color.LTGRAY);
					_scrollview.setSmoothScrollingEnabled(true);
					RelativeLayout.LayoutParams slp = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_accountLayout.addView(_scrollview, slp);

					LinearLayout _accountHolder_parent = new LinearLayout(
							_accountContext);
					_accountHolder_parent.setOrientation(LinearLayout.VERTICAL);
					_accountHolder_parent.setBackgroundColor(Color.LTGRAY);
					_accountHolder_parent.setGravity(Gravity.CENTER_HORIZONTAL);
					_accountHolder_parent.setLayoutParams(new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT));
					_scrollview.addView(_accountHolder_parent);

					// holder for all the account elements
					LinearLayout _accountholder = new LinearLayout(
							_accountContext);
					_accountholder.setOrientation(LinearLayout.VERTICAL);
					_accountholder.setBackgroundColor(Color.LTGRAY);
					layoutParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = (int) (_headerHeight * 0.89);
					int _acholder_width = (int) (_contentViewWidth * 0.92);
					layoutParams.leftMargin = (_contentViewWidth - _acholder_width) / 2;
					layoutParams.width = _acholder_width;

					// creating viewholder1
					LinearLayout _viewholder1 = new LinearLayout(
							_accountContext);
					_viewholder1.setOrientation(LinearLayout.VERTICAL);
					_viewholder1.setBackgroundColor(Color.LTGRAY);
					LayoutParams _viewholder1_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_viewholder1_param.topMargin = marginGap * 2;
					_viewholder1.setLayoutParams(_viewholder1_param);
					// viewholder1 elements

					// creating holder for name elements
					LinearLayout _nameholder = new LinearLayout(_accountContext);
					_nameholder.setOrientation(LinearLayout.HORIZONTAL);
					_nameholder.setBackgroundColor(Color.TRANSPARENT);
					LayoutParams _nameholder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_nameholder_param.height = text_height;

					_nameholder.setLayoutParams(_nameholder_param);

					// name holder elements
					_firstname = new EditText(_accountContext);
					LayoutParams _firstname_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_firstname_param.width = _acholder_width / 2;
					_firstname.setLayoutParams(_firstname_param);
					_firstname.setHint("First Name");
					_firstname
							.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_firstname.setTypeface(_normalFont);
					_firstname.setSingleLine(true);
					_firstname_param.height = text_height;

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_firstname.setBackground(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0, 0, 0));
					} else {
						_firstname.setBackgroundDrawable(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0, 0, 0));
					}
					_firstname.setPadding(_defaultPadding, 0, 0, 0);
					_firstname.setTypeface(_normalFont);
					_nameholder.addView(_firstname);

					_lastname = new EditText(_accountContext);
					LayoutParams _lastname_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_lastname_param.width = (_acholder_width / 2) - element_gap;
					_lastname_param.leftMargin = element_gap;
					_lastname_param.height = text_height;
					_lastname.setLayoutParams(_lastname_param);
					_lastname.setHint("Last Name");
					_lastname.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_lastname.setTypeface(_normalFont);
					_lastname.setSingleLine(true);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_lastname.setBackground(getCustomCornerBg(0, 0,
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0));
					} else {
						_lastname.setBackgroundDrawable(getCustomCornerBg(0, 0,
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0));
					}
					_lastname.setPadding(_defaultPadding, 0, 0, 0);
					_lastname.setTypeface(_normalFont);
					_nameholder.addView(_lastname);

					_viewholder1.addView(_nameholder);// adding nameholder to
														// view
														// holder

					_email = new EditText(_accountContext);
					LayoutParams _email_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_email_param.height = text_height;
					_email_param.topMargin = element_gap;
					_email.setLayoutParams(_email_param);
					_email.setHint("Your Email");
					_email.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_email.setTypeface(_normalFont);
					_email.setSingleLine(true);
					_email.setPadding(_defaultPadding, 0, 0, 0);
					_email.setBackgroundColor(Color.WHITE);
					_email.setTypeface(_normalFont);
					_viewholder1.addView(_email);

					_password = new EditText(_accountContext);
					LayoutParams _password_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_password_param.height = text_height;
					_password_param.topMargin = element_gap;
					_password.setLayoutParams(_password_param);
					_password.setHint("Password");
					_password.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_password.setTypeface(_normalFont);
					_password.setSingleLine(true);
					_password.setPadding(_defaultPadding, 0, 0, 0);
					_password.setBackgroundColor(Color.WHITE);
					_password.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);// set as
																		// password
					_viewholder1.addView(_password);

					_confirm_password = new EditText(_accountContext);
					LayoutParams _confirm_password_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_confirm_password_param.height = text_height;
					_confirm_password_param.topMargin = element_gap;
					_confirm_password.setLayoutParams(_confirm_password_param);
					_confirm_password.setHint("Reconfirm Password");
					_confirm_password.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textSize);
					_confirm_password.setTypeface(_normalFont);
					_confirm_password.setSingleLine(true);
					_confirm_password.setPadding(_defaultPadding, 0, 0, 0);
					// _confirm_password.setTypeface(_normalFont);

					// _confirm_password.setBackgroundColor(Color.WHITE);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_confirm_password.setBackground(getCustomCornerBg(0, 0,
								0, 0, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius));
					} else {
						_confirm_password
								.setBackgroundDrawable(getCustomCornerBg(0, 0,
										0, 0, _defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius));
					}
					_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);// set as
																		// password
					_confirm_password.setPadding(_defaultPadding, 0, 0, 0);

					_viewholder1.addView(_confirm_password);
					_accountholder.addView(_viewholder1);

					// creating viewholder2
					LinearLayout _viewholder2 = new LinearLayout(
							_accountContext);
					_viewholder2.setOrientation(LinearLayout.HORIZONTAL);
					_viewholder2.setBackgroundColor(Color.LTGRAY);
					LayoutParams _viewholder2_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					if (_registrationPage == false) {
						_viewholder2_param.topMargin = 0;
					} else {
						_viewholder2_param.topMargin = marginGap;
					}
					_viewholder2.setPadding(_defaultPadding, 0, 0, 0);
					_viewholder2.setLayoutParams(_viewholder2_param);

					TextView _confirm_loyalty = new TextView(_accountContext);
					if (_registrationPage == false) {
						_confirm_loyalty.setVisibility(View.GONE);
					} else {
						_confirm_loyalty.setVisibility(View.VISIBLE);
						_confirm_loyalty
								.setText("Have you already enrolled in Something Extra at our store, and have a Card or Loyalty Number?");
					}
					_confirm_loyalty.setLines(3);
					_confirm_loyalty.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textSize);
					_confirm_loyalty.setTypeface(_normalFont);

					_confirm_loyalty.setTextColor(Color.BLACK);
					LayoutParams _confirm_loyalty_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_confirm_loyalty_param.width = (int) (_acholder_width * 0.8);
					_confirm_loyalty.setLayoutParams(_confirm_loyalty_param);
					_confirm_loyalty.setGravity(Gravity.CENTER_VERTICAL);
					_confirm_loyalty.setPadding(0, _defaultPadding, 0,
							_defaultPadding);
					_confirm_loyalty.setTypeface(_normalFont);
					_viewholder2.addView(_confirm_loyalty);

					LinearLayout _confirm_loyalty_check_holder = new LinearLayout(
							_accountContext);
					LayoutParams _confirm_loyalty_check_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_confirm_loyalty_check_holder
							.setLayoutParams(_confirm_loyalty_check_holder_param);
					_confirm_loyalty_check_holder
							.setGravity(Gravity.CENTER_VERTICAL
									| Gravity.CENTER_HORIZONTAL
									| Gravity.CENTER);
					_confirm_loyalty_check = new ImageView(_accountContext);
					if (_registrationPage == false) {
						_confirm_loyalty_check.setVisibility(View.GONE);
					} else {
						_confirm_loyalty_check.setVisibility(View.VISIBLE);
						_confirm_loyalty_check
								.setBackgroundResource(R.drawable.product_unchecked_box);
						_confirm_loyalty_check.setSelected(false);
					}
					_confirm_loyalty_check
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (((ImageView) v).isSelected()) {
										_confirm_loyalty_check
												.setSelected(false);
										_confirm_loyalty_check
												.setBackgroundResource(R.drawable.product_unchecked_box);
										_loyalty_number
												.setHint("Create a 8 - 12 digits Loyalty Number");
									} else {
										_confirm_loyalty_check
												.setSelected(true);
										_confirm_loyalty_check
												.setBackgroundResource(R.drawable.product_checked_box);
										_loyalty_number
												.setHint("Enter Your Something Extra or Loyalty Number");
									}

								}
							});
					LayoutParams _confirm_loyalty_check_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_confirm_loyalty_check_param.width = (int) (_acholder_width * 0.1);
					_confirm_loyalty_check_param.height = (int) (_acholder_width * 0.1);
					_confirm_loyalty_check
							.setLayoutParams(_confirm_loyalty_check_param);
					_confirm_loyalty_check_holder
							.addView(_confirm_loyalty_check);
					_viewholder2.addView(_confirm_loyalty_check_holder);
					_accountholder.addView(_viewholder2);

					_loyalty_number = new EditText(_accountContext);
					LayoutParams _loyalty_number_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_loyalty_number_param.height = text_height;
					if (_registrationPage == false) {
						_loyalty_number_param.topMargin = marginGap;
					} else {
						_loyalty_number_param.topMargin = marginGap;
					}
					_loyalty_number.setLayoutParams(_loyalty_number_param);
					_loyalty_number
							.setHint("Create a 8 - 12 digits Loyalty Number");
					_loyalty_number.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textSize);
					_loyalty_number.setTypeface(_normalFont);
					_loyalty_number.setEllipsize(TextUtils.TruncateAt.END);
					_loyalty_number.setSingleLine(true);
					_loyalty_number.setInputType(InputType.TYPE_CLASS_NUMBER);
					_loyalty_number.setTypeface(_normalFont);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_loyalty_number.setBackground(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					} else {
						_loyalty_number
								.setBackgroundDrawable(getCustomCornerBg(
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius));
					}
					_loyalty_number.setPadding(_defaultPadding, 0, 0, 0);
					_accountholder.addView(_loyalty_number);

					if (_registrationPage == false) {
						_points_balance = new TextView(_accountContext);
						LayoutParams _points_balance_param = new LayoutParams(
								android.view.ViewGroup.LayoutParams.MATCH_PARENT,
								android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
						_points_balance_param.height = text_height;
						_points_balance_param.topMargin = marginGap;
						_points_balance.setLayoutParams(_points_balance_param);
						_points_balance.setGravity(Gravity.CENTER_VERTICAL);
						_points_balance.setText(DEFAULT_POINTS_TEXT + "0");
						_points_balance.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								textSize);
						_points_balance.setTypeface(_normalFont);
						_points_balance.setTextColor(Color.BLACK);
						int sdk = android.os.Build.VERSION.SDK_INT;
						if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
							_points_balance
									.setBackgroundDrawable(getCustomCornerBg(
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius));
						} else {
							_points_balance
									.setBackground(getCustomCornerBg(
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius,
											_defaultCornerRadius));
						}
						_points_balance.setPadding(_defaultPadding, 0, 5, 0);
						_points_balance.setTypeface(_normalFont);
						_accountholder.addView(_points_balance);
					}

					// Creating View Holder 3

					LinearLayout _viewholder3 = new LinearLayout(
							_accountContext);
					_viewholder3.setOrientation(LinearLayout.VERTICAL);
					_viewholder3.setBackgroundColor(Color.LTGRAY);
					LayoutParams _viewholder3_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_viewholder3_param.topMargin = marginGap;
					_viewholder3.setLayoutParams(_viewholder3_param);

					// viewholder3 elements

					// creating holder for name elements
					LinearLayout _phoneholder = new LinearLayout(
							_accountContext);
					_phoneholder.setOrientation(LinearLayout.HORIZONTAL);
					_phoneholder.setBackgroundColor(Color.LTGRAY);
					LayoutParams _phoneholder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_phoneholder_param.height = text_height;
					_phoneholder.setLayoutParams(_phoneholder_param);

					// phoneholder elements
					_mobilephone = new EditText(_accountContext);
					LayoutParams _mobilephone_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_mobilephone_param.width = _acholder_width / 2;
					_mobilephone_param.height = text_height;
					_mobilephone.setLayoutParams(_mobilephone_param);
					_mobilephone.setHint("Mobile Phone");
					_mobilephone.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textSize);
					_mobilephone.setTypeface(_normalFont);
					_mobilephone.setSingleLine(true);
					_mobilephone.setTypeface(_normalFont);
					_mobilephone.setInputType(InputType.TYPE_CLASS_PHONE);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_mobilephone.setBackground(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0, 0, 0));
					} else {
						_mobilephone.setBackgroundDrawable(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0, 0, 0));
					}
					_mobilephone.setPadding(_defaultPadding, 0, 0, 0);
					_phoneholder.addView(_mobilephone);

					_homephone = new EditText(_accountContext);
					LayoutParams _homephone_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_homephone_param.width = (_acholder_width / 2)
							- element_gap;
					_homephone_param.leftMargin = element_gap;
					_homephone_param.height = text_height;
					_homephone.setLayoutParams(_homephone_param);
					_homephone.setHint("Home Phone");
					_homephone
							.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_homephone.setTypeface(_normalFont);
					_homephone.setSingleLine(true);
					_homephone.setInputType(InputType.TYPE_CLASS_PHONE);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_homephone.setBackground(getCustomCornerBg(0, 0,
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0));
					} else {
						_homephone.setBackgroundDrawable(getCustomCornerBg(0,
								0, _defaultCornerRadius, _defaultCornerRadius,
								0, 0, 0, 0));
					}
					_homephone.setTypeface(_normalFont);
					_homephone.setPadding(_defaultPadding, 0, 0, 0);
					_phoneholder.addView(_homephone);
					_viewholder3.addView(_phoneholder);// adding phoneholder to
														// view
														// holder

					_address = new EditText(_accountContext);
					LayoutParams _address_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_address_param.height = text_height;
					_address_param.topMargin = element_gap;
					_address_param.height = text_height;
					_address.setLayoutParams(_address_param);
					_address.setHint("Address");
					_address.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_address.setTypeface(_normalFont);
					_address.setSingleLine(true);
					_address.setBackgroundColor(Color.WHITE);
					_address.setPadding(_defaultPadding, 0, 0, 0);
					_address.setTypeface(_normalFont);
					_viewholder3.addView(_address);

					_city = new EditText(_accountContext);
					LayoutParams _city_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_city_param.height = text_height;
					_city_param.topMargin = element_gap;
					_city_param.height = text_height;
					_city.setLayoutParams(_city_param);
					_city.setHint("City");
					_city.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_city.setTypeface(_normalFont);
					_city.setSingleLine(true);
					_city.setTypeface(_normalFont);
					_city.setBackgroundColor(Color.WHITE);
					_city.setPadding(_defaultPadding, 0, 0, 0);
					_viewholder3.addView(_city);

					// creating holder for State and Zip elements
					LinearLayout _statezipholder = new LinearLayout(
							_accountContext);
					_statezipholder.setOrientation(LinearLayout.HORIZONTAL);
					_statezipholder.setBackgroundColor(Color.LTGRAY);
					LayoutParams _statezipholder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_statezipholder_param.topMargin = element_gap;
					_statezipholder_param.height = text_height;
					_statezipholder.setLayoutParams(_statezipholder_param);

					// statezipholder elements
					_state = new TextView(_accountContext);
					LayoutParams _state_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_state_param.width = _acholder_width / 2;
					// _state_param.height = text_height;
					_state.setLayoutParams(_state_param);
					_state.setHint("State");
					_state.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_state.setTypeface(_normalFont);
					_state.setSingleLine(true);
					_state.setGravity(Gravity.CENTER_VERTICAL);
					// _state.setBackgroundColor(Color.WHITE);
					_state.setTypeface(_normalFont);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_state.setBackground(getCustomCornerBg(0, 0, 0, 0, 0,
								0, _defaultCornerRadius, _defaultCornerRadius));
					} else {
						_state.setBackgroundDrawable(getCustomCornerBg(0, 0, 0,
								0, 0, 0, _defaultCornerRadius,
								_defaultCornerRadius));
					}
					_state.setPadding(_defaultPadding, 0, 0, 0);
					_state.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							openStateDialog();
						}
					});
					_statezipholder.addView(_state);

					_zip = new EditText(_accountContext);
					LayoutParams _zip_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_zip_param.width = (_acholder_width / 2) - element_gap;
					_zip_param.leftMargin = element_gap;
					_zip.setLayoutParams(_zip_param);
					_zip.setHint("Zip");
					_zip.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_zip.setTypeface(_normalFont);
					_zip.setSingleLine(true);
					_zip.setInputType(InputType.TYPE_CLASS_NUMBER);
					_zip.setTypeface(_normalFont);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_zip.setBackground(getCustomCornerBg(0, 0, 0, 0,
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0));
					} else {
						_zip.setBackgroundDrawable(getCustomCornerBg(0, 0, 0,
								0, _defaultCornerRadius, _defaultCornerRadius,
								0, 0));
					}
					_zip.setPadding(_defaultPadding, 0, 0, 0);
					_statezipholder.addView(_zip);
					_viewholder3.addView(_statezipholder);// adding
															// statezipholder to
															// view holder

					_accountholder.addView(_viewholder3);

					// Creating View Holder 4
					LinearLayout _viewholder4 = new LinearLayout(
							_accountContext);
					_viewholder4.setOrientation(LinearLayout.VERTICAL);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_viewholder4.setBackground(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					} else {
						_viewholder4.setBackgroundDrawable(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					}
					LayoutParams _viewholder4_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_viewholder4_param.setMargins(0, marginGap, 0, 0);
					_viewholder4.setLayoutParams(_viewholder4_param);

					// Something Extra Rewords Holder
					LinearLayout _SE_holder = new LinearLayout(_accountContext);
					_SE_holder.setOrientation(LinearLayout.HORIZONTAL);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_SE_holder.setBackground(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0));
					} else {
						_SE_holder.setBackgroundDrawable(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius, 0,
								0, 0, 0));
					}
					LayoutParams _SE_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_SE_holder_param.topMargin = marginGap;
					_SE_holder.setLayoutParams(_SE_holder_param);

					LinearLayout _SE_rewards_check_holder = new LinearLayout(
							_accountContext);
					_SE_rewards_check_holder.setBackgroundColor(Color.WHITE);//
					LayoutParams _SE_rewards_check_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_SE_rewards_check_holder_param.setMargins(_defaultPadding,
							0, 0, 0);
					_SE_rewards_check_holder
							.setLayoutParams(_SE_rewards_check_holder_param);
					_SE_rewards_check_holder.setGravity(Gravity.TOP);

					// Something Extra Rewords Check
					_SE_rewards_check = new ImageView(_accountContext);
					_SE_rewards_check
							.setBackgroundResource(R.drawable.product_unchecked_box);
					LayoutParams _SE_rewards_check_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_SE_rewards_check_param.width = (int) (_acholder_width * 0.1);
					_SE_rewards_check_param.height = (int) (_acholder_width * 0.1);
					_SE_rewards_check.setLayoutParams(_SE_rewards_check_param);
					_SE_rewards_check.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							_SE_rewards_check.setSelected(true);
							_NO_thanks_check.setSelected(false);
							_SE_rewards_check
									.setBackgroundResource(R.drawable.product_checked_box);
							_NO_thanks_check
									.setBackgroundResource(R.drawable.product_unchecked_box);
						}
					});
					_SE_rewards_check_holder.addView(_SE_rewards_check);
					_SE_holder.addView(_SE_rewards_check_holder);

					int textareaWidth = (int) (_acholder_width * 0.75);

					TextView _SE_rewards = new TextView(_accountContext);
					_SE_rewards.setBackgroundColor(Color.WHITE);//
					_SE_rewards
							.setText("I'd like to request a Something Extra rewards card.");
					_SE_rewards.setLines(3);
					_SE_rewards.setTextColor(Color.BLACK);
					_SE_rewards.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textSize);// 17
					_SE_rewards.setTypeface(_normalFont);
					_SE_rewards.setPadding(_defaultPadding, 0, 0,
							_defaultPadding);
					_SE_rewards.setGravity(Gravity.TOP);
					LayoutParams _SE_rewards_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_SE_rewards_param.width = textareaWidth;
					_SE_rewards.setLayoutParams(_SE_rewards_param);
					_SE_holder.addView(_SE_rewards);
					_viewholder4.addView(_SE_holder);// adding SE holder

					// No thanks Holder
					LinearLayout _NT_holder = new LinearLayout(_accountContext);
					_NT_holder.setOrientation(LinearLayout.HORIZONTAL);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_NT_holder.setBackground(getCustomCornerBg(0, 0, 0, 0,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					} else {
						_NT_holder.setBackgroundDrawable(getCustomCornerBg(0,
								0, 0, 0, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius));
					}
					LayoutParams _NT_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_NT_holder.setLayoutParams(_NT_holder_param);

					LinearLayout _NO_thanks_check_holder = new LinearLayout(
							_accountContext);
					_NO_thanks_check_holder.setBackgroundColor(Color.WHITE);//
					LayoutParams _NO_thanks_check_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_NO_thanks_check_holder_param.setMargins(_defaultPadding,
							0, 0, 0);
					_NO_thanks_check_holder
							.setLayoutParams(_NO_thanks_check_holder_param);
					_NO_thanks_check_holder.setGravity(Gravity.TOP);

					// No thanks Check
					_NO_thanks_check = new ImageView(_accountContext);
					_NO_thanks_check
							.setBackgroundResource(R.drawable.product_unchecked_box);
					LayoutParams _NO_thanks_check_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_NO_thanks_check_param.width = (int) (_acholder_width * 0.1);
					_NO_thanks_check_param.height = (int) (_acholder_width * 0.1);
					_NO_thanks_check.setLayoutParams(_NO_thanks_check_param);
					_NO_thanks_check.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							_SE_rewards_check.setSelected(false);
							_NO_thanks_check.setSelected(true);
							_SE_rewards_check
									.setBackgroundResource(R.drawable.product_unchecked_box);
							_NO_thanks_check
									.setBackgroundResource(R.drawable.product_checked_box);
						}
					});
					_NO_thanks_check_holder.addView(_NO_thanks_check);
					_NT_holder.addView(_NO_thanks_check_holder);
					TextView _NO_thanks = new TextView(_accountContext);
					_NO_thanks.setBackgroundColor(Color.WHITE);//
					_NO_thanks
							.setText("No thanks, I'll just shop with my Loyalty Number.");
					_NO_thanks.setLines(3);
					_NO_thanks.setTextColor(Color.BLACK);
					_NO_thanks
							.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);// 17
					_NO_thanks.setTypeface(_normalFont);
					_NO_thanks.setPadding(_defaultPadding, 0, 0,
							_defaultPadding);
					_NO_thanks.setGravity(Gravity.TOP);
					LayoutParams _NO_thanks_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_NO_thanks_param.width = textareaWidth;
					_NO_thanks.setLayoutParams(_NO_thanks_param);
					_NT_holder.addView(_NO_thanks);
					_viewholder4.addView(_NT_holder);// adding viewholder4
														// holder
					_accountholder.addView(_viewholder4);

					// Creating ViewHolder 5
					LinearLayout _viewholder5 = new LinearLayout(
							_accountContext);
					_viewholder5.setOrientation(LinearLayout.VERTICAL);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_viewholder5.setBackground(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					} else {
						_viewholder5.setBackgroundDrawable(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					}
					LayoutParams _viewholder5_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					if (_registrationPage == false) {
						_viewholder5_param.setMargins(0, marginGap, 0,
								marginGap * 2);
					} else {
						_viewholder5_param.setMargins(0, marginGap, 0, 0);
					}
					_viewholder5.setLayoutParams(_viewholder5_param);

					// Select Your Favorite Location Holder
					RelativeLayout _FL_holder = new RelativeLayout(
							_accountContext);
					LayoutParams _FL_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_FL_holder_param.height = _headerHeight;
					_FL_holder_param.setMargins(_defaultPadding,
							_defaultPadding, _defaultPadding, _defaultPadding);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_FL_holder.setBackground(getCustomCornerInsideBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					} else {
						_FL_holder
								.setBackgroundDrawable(getCustomCornerInsideBg(
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius));
					}

					_FL_holder.setLayoutParams(_FL_holder_param);

					_FL_text = new TextView(_accountContext);
					_FL_text.setHint("Select Your Favorite Location");
					_FL_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_FL_text.setTypeface(_normalFont);
					_FL_text.setGravity(Gravity.CENTER_VERTICAL);
					_FL_text.setPadding(_defaultPadding, 0, 0, 0);
					_FL_text.setTextColor(Color.BLACK);
					_FL_text.setSingleLine(true);
					_FL_holder.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							chooseStore();
						}
					});
					RelativeLayout.LayoutParams _FL_text_param = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_FL_text_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
							_FL_holder.getId());
					_FL_text_param.addRule(RelativeLayout.CENTER_VERTICAL,
							_FL_holder.getId());
					int _FL_text_width = (int) (_acholder_width * 0.76);
					_FL_text_param.width = _FL_text_width;
					_FL_text.setLayoutParams(_FL_text_param);
					_FL_holder.addView(_FL_text);

					ImageView _FL_Button = new ImageView(_accountContext);
					// Button _FL_Button = new Button(_accountContext);
					_FL_Button
							.setBackgroundResource(R.drawable.account_favlocation_button);
					RelativeLayout.LayoutParams _FL_Button_param = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_FL_Button_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
							_FL_holder.getId());
					_FL_Button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// changeStore();
							chooseStore();
						}
					});

					int buttonSize = _headerHeight;
					_FL_Button_param.width = buttonSize;
					_FL_Button_param.height = buttonSize;
					_FL_Button.setLayoutParams(_FL_Button_param);
					_FL_holder.addView(_FL_Button);

					_viewholder5.addView(_FL_holder);

					// Select Your Favorite Department Holder
					RelativeLayout _FD_holder = new RelativeLayout(
							_accountContext);
					_FD_holder.setBackgroundResource(R.drawable.round_corners);
					LayoutParams _FD_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_FD_holder_param.height = _headerHeight;
					_FD_holder_param.setMargins(_defaultPadding,
							_defaultPadding, _defaultPadding, _defaultPadding);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_FD_holder.setBackground(getCustomCornerInsideBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					} else {
						_FD_holder
								.setBackgroundDrawable(getCustomCornerInsideBg(
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius));
					}
					_FD_holder.setLayoutParams(_FD_holder_param);

					_FD_text = new TextView(_accountContext);
					_FD_text.setHint("Select Your Favorite Department");
					_FD_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_FD_text.setTypeface(_normalFont);
					_FD_text.setGravity(Gravity.CENTER_VERTICAL);
					_FD_text.setPadding(_defaultPadding, 0, 0, 0);
					_FD_text.setTextColor(Color.BLACK);
					_FD_text.setSingleLine(true);

					_FD_text.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							openDeptDialog();
						}
					});

					RelativeLayout.LayoutParams _FD_text_param = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_FD_text_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
							_FD_holder.getId());
					_FD_text_param.addRule(RelativeLayout.CENTER_VERTICAL,
							_FD_holder.getId());
					int _FD_text_width = (int) (_acholder_width * 0.76);
					_FD_text_param.width = _FD_text_width;
					_FD_text.setLayoutParams(_FD_text_param);
					_FD_holder.addView(_FD_text);

					ImageView _FD_Button = new ImageView(_accountContext);
					_FD_Button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							openDeptDialog();
						}
					});
					_FD_Button
							.setBackgroundResource(R.drawable.account_updown_button);
					RelativeLayout.LayoutParams _FD_Button_param = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_FD_Button_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
							_FD_holder.getId());

					_FD_Button_param.width = buttonSize;
					_FD_Button_param.height = buttonSize;
					_FD_Button.setLayoutParams(_FD_Button_param);
					_FD_holder.addView(_FD_Button);
					_viewholder5.addView(_FD_holder);

					// New Offers Holder
					LinearLayout _New_Offer_holder = new LinearLayout(
							_accountContext);
					_New_Offer_holder.setOrientation(LinearLayout.HORIZONTAL);
					_New_Offer_holder.setBackgroundColor(Color.WHITE);
					LayoutParams _New_Offer_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_New_Offer_holder.setLayoutParams(_New_Offer_holder_param);

					LinearLayout _New_Offer_check_holder = new LinearLayout(
							_accountContext);
					_New_Offer_check_holder.setBackgroundColor(Color.WHITE);//
					LayoutParams _New_Offer_check_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_New_Offer_check_holder_param.setMargins(_defaultPadding,
							0, 0, 0);
					_New_Offer_check_holder
							.setLayoutParams(_New_Offer_check_holder_param);
					_New_Offer_check_holder.setGravity(Gravity.TOP);

					// New Offer Check
					_New_Offer_check = new ImageView(_accountContext);
					LayoutParams _New_Offer_check_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_New_Offer_check_param.width = (int) (_acholder_width * 0.1);
					_New_Offer_check_param.height = (int) (_acholder_width * 0.1);
					_New_Offer_check.setLayoutParams(_New_Offer_check_param);
					_New_Offer_check.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (((ImageView) v).isSelected()) {
								_New_Offer_check.setSelected(false);
								_New_Offer_check
										.setBackgroundResource(R.drawable.product_unchecked_box);

							} else {
								_New_Offer_check.setSelected(true);
								_New_Offer_check
										.setBackgroundResource(R.drawable.product_checked_box);
							}

						}
					});
					_New_Offer_check_holder.addView(_New_Offer_check);
					_New_Offer_holder.addView(_New_Offer_check_holder);

					TextView _New_Offer = new TextView(_accountContext);
					_New_Offer.setBackgroundColor(Color.WHITE);//
					_New_Offer
							.setText("When my new offers are ready, Please email me!");
					_New_Offer.setLines(3);
					_New_Offer.setTextColor(Color.BLACK);
					_New_Offer
							.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_New_Offer.setTypeface(_normalFont);
					LayoutParams _New_Offer_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_New_Offer_param.width = textareaWidth;
					_New_Offer.setLayoutParams(_New_Offer_param);
					_New_Offer.setBackgroundColor(Color.TRANSPARENT);
					_New_Offer.setGravity(Gravity.TOP);
					_New_Offer.setPadding(_defaultPadding, 0, 0,
							_defaultPadding);
					_New_Offer_holder.addView(_New_Offer);
					_viewholder5.addView(_New_Offer_holder);// adding
															// viewholder4
															// holder

					// Hot Offers Holder
					LinearLayout _Hot_Offer_holder = new LinearLayout(
							_accountContext);
					_Hot_Offer_holder.setOrientation(LinearLayout.HORIZONTAL);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_Hot_Offer_holder.setBackground(getCustomCornerBg(0, 0,
								0, 0, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius));
					} else {
						_Hot_Offer_holder
								.setBackgroundDrawable(getCustomCornerBg(0, 0,
										0, 0, _defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius,
										_defaultCornerRadius));
					}
					LayoutParams _Hot_Offer_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_Hot_Offer_holder.setLayoutParams(_Hot_Offer_holder_param);
					LinearLayout _Hot_Offer_check_holder = new LinearLayout(
							_accountContext);
					_Hot_Offer_check_holder.setBackgroundColor(Color.WHITE);//
					LayoutParams _Hot_Offer_check_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_Hot_Offer_check_holder_param.setMargins(_defaultPadding,
							0, 0, 0);
					_Hot_Offer_check_holder
							.setLayoutParams(_Hot_Offer_check_holder_param);
					_Hot_Offer_check_holder.setGravity(Gravity.TOP);

					// No thanks Check
					_Hot_Offer_check = new ImageView(_accountContext);
					LayoutParams _Hot_Offer_check_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_Hot_Offer_check_param.width = (int) (_acholder_width * 0.1);
					_Hot_Offer_check_param.height = (int) (_acholder_width * 0.1);
					_Hot_Offer_check.setLayoutParams(_Hot_Offer_check_param);
					_Hot_Offer_check.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (((ImageView) v).isSelected()) {
								_Hot_Offer_check.setSelected(false);
								_Hot_Offer_check
										.setBackgroundResource(R.drawable.product_unchecked_box);

							} else {
								_Hot_Offer_check.setSelected(true);
								_Hot_Offer_check
										.setBackgroundResource(R.drawable.product_checked_box);
							}

						}
					});
					_Hot_Offer_check_holder.addView(_Hot_Offer_check);
					_Hot_Offer_holder.addView(_Hot_Offer_check_holder);

					TextView _Hot_Offer = new TextView(_accountContext);
					_Hot_Offer.setBackgroundColor(Color.WHITE);//
					_Hot_Offer
							.setText("When there's a really hot offer for me, text me on my phone");
					_Hot_Offer.setLines(3);
					_Hot_Offer.setTextColor(Color.BLACK);
					_Hot_Offer
							.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					_Hot_Offer.setTypeface(_normalFont);
					_Hot_Offer.setPadding(_defaultPadding, 0, 0,
							_defaultPadding);
					_Hot_Offer.setGravity(Gravity.TOP);
					LayoutParams _Hot_Offer_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_Hot_Offer_param.width = textareaWidth;
					_Hot_Offer.setLayoutParams(_Hot_Offer_param);
					_Hot_Offer.setBackgroundColor(Color.TRANSPARENT);
					_Hot_Offer_holder.addView(_Hot_Offer);
					_viewholder5.addView(_Hot_Offer_holder);// adding Hot Offer
															// holder
					_accountholder.addView(_viewholder5);

					// Creating ViewHolder 6
					LinearLayout _viewholder6 = new LinearLayout(
							_accountContext);
					_viewholder6.setOrientation(LinearLayout.VERTICAL);
					_viewholder6.setBackgroundColor(Color.WHITE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						_viewholder6.setBackground(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					} else {
						_viewholder6.setBackgroundDrawable(getCustomCornerBg(
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius,
								_defaultCornerRadius, _defaultCornerRadius));
					}
					LayoutParams _viewholder6_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_viewholder6_param.setMargins(0, marginGap, 0,
							marginGap * 2);
					_viewholder6.setLayoutParams(_viewholder6_param);

					// Terms and Conditions Holder
					LinearLayout _TC_holder = new LinearLayout(_accountContext);
					_TC_holder.setOrientation(LinearLayout.HORIZONTAL);
					LayoutParams _TC_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_TC_holder_param.setMargins(5, 5, 5, 5);
					_TC_holder.setLayoutParams(_TC_holder_param);

					LinearLayout _TC_rewards_check_holder = new LinearLayout(
							_accountContext);
					_TC_rewards_check_holder.setBackgroundColor(Color.WHITE);//
					LayoutParams _TC_rewards_check_holder_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT);
					_TC_rewards_check_holder_param.setMargins(_defaultPadding,
							5, 5, 5);
					_TC_rewards_check_holder
							.setLayoutParams(_TC_rewards_check_holder_param);
					_TC_rewards_check_holder.setGravity(Gravity.CENTER_VERTICAL
							| Gravity.CENTER_HORIZONTAL | Gravity.CENTER);

					// Terms and Conditions Check
					_TC_rewards_check = new ImageView(_accountContext);
					_TC_rewards_check
							.setBackgroundResource(R.drawable.product_unchecked_box);
					LayoutParams _TC_rewards_check_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_TC_rewards_check_param.width = (int) (_acholder_width * 0.1);
					_TC_rewards_check_param.height = (int) (_acholder_width * 0.1);
					_TC_rewards_check.setLayoutParams(_TC_rewards_check_param);
					_TC_rewards_check.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (((ImageView) v).isSelected()) {
								_TC_rewards_check.setSelected(false);
								_TC_rewards_check
										.setBackgroundResource(R.drawable.product_unchecked_box);

							} else {
								_TC_rewards_check.setSelected(true);
								_TC_rewards_check
										.setBackgroundResource(R.drawable.product_checked_box);
							}

						}
					});

					_TC_rewards_check_holder.addView(_TC_rewards_check);
					_TC_holder.addView(_TC_rewards_check_holder);

					Spannable wordtoSpan = new SpannableString(
							"I agree to the Terms and Conditions of this program. Terms and Service");
					wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
							54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 52,
							70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

					TextView _TC_rewards = new TextView(_accountContext);
					_TC_rewards.setBackgroundColor(Color.WHITE);//
					_TC_rewards.setText(wordtoSpan);
					_TC_rewards.setLines(3);
					_TC_rewards.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textSize);
					_TC_rewards.setTypeface(_normalFont);
					_TC_rewards.setTextColor(Color.BLACK);
					_TC_rewards.setPadding(5, _defaultPadding, 5,
							_defaultPadding);
					LayoutParams _TC_rewards_param = new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					_TC_rewards_param.setMargins(5, 5, 5, 5);
					_TC_rewards_param.width = (int) (_acholder_width * 0.8);
					_TC_rewards.setLayoutParams(_TC_rewards_param);

					_TC_rewards.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							openTermsPage();

						}
					});

					_TC_holder.addView(_TC_rewards);
					_viewholder6.addView(_TC_holder);// adding SE holder
					_accountholder.addView(_viewholder6);
					_accountHolder_parent.addView(_accountholder, layoutParams);

					// Setting values
					if (_registrationPage == false) {
						_firstname.setText(_accountRequest.firstName);
						_lastname.setText(_accountRequest.lastName);
						_email.setText(_accountRequest.email);
						_password.setText(_accountRequest.password);

						_loyalty_number.setText(_accountRequest.loyaltyNumber);

						_homephone.setText(_accountRequest.phone);
						_mobilephone.setText(_accountRequest.mobilePhone);
						_address.setText(_accountRequest.address);
						_city.setText(_accountRequest.city);
						_state.setText(getStateNameByCode(_accountRequest.state));
						_state.setTextColor(Color.BLACK);
						_zip.setText(_accountRequest.zip);

						// setting favorite location
						if (_app._allStoresList.size() > 0) {

							for (Store store : _app._allStoresList) {
								if (store != null) {
									if (store.storeNumber == _accountRequest.storeNumber) {
										String str_store = store.chain + " "
												+ store.address;
										_FL_text.setText(str_store);
										break;
									}
								}
							}

						}

						Login _login = _app.getLogin();
						_password.setText(_login.password);
						_points_balance.setText(DEFAULT_POINTS_TEXT
								+ _login.pointsBalance);

						_FD_text.setText(_accountRequest.favoriteDept);
						boolean checkIssueCard = _accountRequest.issueCardFlag;
						if (checkIssueCard == true) {
							_SE_rewards_check
									.setBackgroundResource(R.drawable.product_checked_box);
							_SE_rewards_check.setSelected(true);
							_NO_thanks_check.setSelected(false);

							_NO_thanks_check
									.setBackgroundResource(R.drawable.product_unchecked_box);

						} else {

							_SE_rewards_check
									.setBackgroundResource(R.drawable.product_unchecked_box);
							_NO_thanks_check
									.setBackgroundResource(R.drawable.product_checked_box);
							_NO_thanks_check.setSelected(true);
							_SE_rewards_check.setSelected(false);
						}

						if (_accountRequest.sendEmailsFlag) {
							_New_Offer_check.setSelected(true);
							_New_Offer_check
									.setBackgroundResource(R.drawable.product_checked_box);
						} else {
							_New_Offer_check.setSelected(false);
							_New_Offer_check
									.setBackgroundResource(R.drawable.product_unchecked_box);
						}

						if (_accountRequest.sendTextsFlag) {
							_Hot_Offer_check.setSelected(true);
							_Hot_Offer_check
									.setBackgroundResource(R.drawable.product_checked_box);
						} else {
							_Hot_Offer_check.setSelected(false);
							_Hot_Offer_check
									.setBackgroundResource(R.drawable.product_unchecked_box);
						}

						_viewholder6.setVisibility(View.GONE);// hides the terms
																// and
																// conditions

					} else {

						// Default check option

						_NO_thanks_check.setSelected(true);
						_New_Offer_check.setSelected(true);
						_Hot_Offer_check.setSelected(true);

						_NO_thanks_check
								.setBackgroundResource(R.drawable.product_checked_box);
						_New_Offer_check
								.setBackgroundResource(R.drawable.product_checked_box);
						_Hot_Offer_check
								.setBackgroundResource(R.drawable.product_checked_box);

					}

					dismissActiveDialog();
					changeFlag();
				}
			}, 2000);

			setContentView(_mainLayout);

		} catch (Exception ex) {
			ex.printStackTrace();
			// Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void changeFlag() {
		_app._registerActivityopen = false;
	}

	public void openTermsPage() {
		Intent intent = new Intent(this, TermsAndConditions.class);
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();

		try {
			if (_app._locateForAccount == true) {
				_app._locateForAccount = false;
				_accountRequest.storeNumber = _app._storeForAccount.storeNumber;
				_FL_text.setText(_app._storeForAccount.chain + ", "
						+ _app._storeForAccount.address);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void chooseStore() {
		try {
			if (_app.getStoresList() == null
					|| _app.getStoresList().size() == 0) {
				_app.getAllStores();
				showTextDialog(this, "Server Error",
						"Sorry, the store locator is not available at this time. Please try again.");
				return;
			}

			_app._locateForAccount = true;
			// _FL_text.setTextColor(_textColor);
			Intent intent = null;
			if (_registrationPage == true) {
				intent = new Intent(this, StoreLocatorScreen.class);
				intent.putExtra("registration", true);
			} else {
				intent = new Intent(this, StoreLocatorScreen.class);
				intent.putExtra("registration", false);
			}
			startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public LayerDrawable getCustomCornerBg(int topleft1, int topleft2,
			int topright1, int topright2, int bottomright1, int bottomright2,
			int bottomleft1, int bottomleft2) {
		float[] CornerRadius = new float[] { topleft1, topleft2, topright1,
				topright2, bottomright1, bottomright2, bottomleft1, bottomleft2 };
		RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
				new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		ShapeDrawable top_layer = new CustomShapeDrawable(bottom_rect,
				Color.WHITE, Color.rgb(255, 255, 255), 0);
		LayerDrawable ld = new LayerDrawable(new Drawable[] { top_layer });
		ld.setLayerInset(0, 0, 0, 0, 0);

		return ld;
	}

	public LayerDrawable getCustomCornerInsideBg(int topleft1, int topleft2,
			int topright1, int topright2, int bottomright1, int bottomright2,
			int bottomleft1, int bottomleft2) {
		float[] CornerRadius = new float[] { topleft1, topleft2, topright1,
				topright2, bottomright1, bottomright2, bottomleft1, bottomleft2 };
		RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
				new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		ShapeDrawable bottom_layer = new CustomShapeDrawable(bottom_rect,
				Color.TRANSPARENT, Color.WHITE, 0);

		RoundRectShape top_rect = new RoundRectShape(CornerRadius, new RectF(0,
				0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		ShapeDrawable top_layer = new CustomShapeDrawable(top_rect,
				Color.TRANSPARENT, Color.DKGRAY, 0);

		LayerDrawable ld = new LayerDrawable(new Drawable[] { top_layer,
				bottom_layer });
		ld.setLayerInset(0, 0, 0, 0, 0);
		ld.setLayerInset(1, (int) 2.0f, (int) 2.0f, (int) 2.0f, (int) 2.0f);

		return ld;
	}

	public boolean validateAllFields() {

		// ViewHolder 1
		_accountRequest.firstName = _firstname.getText().toString();
		_accountRequest.lastName = _lastname.getText().toString();
		_accountRequest.email = _email.getText().toString();
		_accountRequest.password = _password.getText().toString();
		String confirmPassword = _confirm_password.getText().toString();

		// ViewHolder 2

		// _accountRequest.phone = _homePhoneTextField.getText().toString();
		// _accountRequest.mobilePhone =
		// _cellPhoneTextField.getText().toString();

		// *********************************************************
		// ViewHolder 1
		// ********************************************************

		if (_accountRequest.firstName.length() == 0) {
			_firstname.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"First Name can't be blank");
			return false;
		}

		if (_accountRequest.lastName.length() == 0) {
			_lastname.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Last Name can't be blank");
			return false;
		}

		if (_accountRequest.email.length() == 0) {
			_email.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Email Address can't be blank");
			return false;
		}

		if (Utils.validateEmail(_accountRequest.email) == false) {
			_email.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Invalid Email Address");
			return false;
		}

		if (_registrationPage == false
				&& _accountRequest.password.length() == 0
				&& confirmPassword.length() == 0)
			return true;

		if (_accountRequest.password.length() == 0) {
			_email.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Password can't be blank");
			return false;
		}

		if (confirmPassword.length() == 0) {
			_confirm_password.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Confirm password can't be blank");
			return false;
		}

		if (!_accountRequest.password.equals(confirmPassword)) {
			_confirm_password.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Passwords do not match");
			return false;
		}

		if (_accountRequest.password.length() < 6
				|| confirmPassword.length() < 6
				|| _accountRequest.password.equals(_accountRequest.password
						.toLowerCase(Locale.US))) {
			_password.requestFocus();
			showTextDialog(
					this,
					INPUT_ERROR_TITLE_TEXT,
					"Passwords must be at least 6 characters and contain at least one uppercase letter");
			return false;
		}

		// *********************************************************
		// ViewHolder 2
		// ********************************************************

		if (_confirm_loyalty_check.isSelected() && _registrationPage == true) {
			_accountRequest.loyaltyNumber = _loyalty_number.getText()
					.toString();
			// _accountRequest.issueCardFlag = true;
		} else {
			_accountRequest.loyaltyNumber = _loyalty_number.getText()
					.toString();
			// _accountRequest.issueCardFlag = false;
		}

		if (_NO_thanks_check.isSelected()) {
			_accountRequest.issueCardFlag = false;
		} else {
			_accountRequest.issueCardFlag = true;
		}

		if (_accountRequest.loyaltyNumber.length() == 0) {
			_loyalty_number.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Loyalty Number can't be blank");
			return false;
		}

		if ((_accountRequest.loyaltyNumber.length() < 8 || _accountRequest.loyaltyNumber
				.length() > 12)) {
			_loyalty_number.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Loyalty Numbers must contain 8 to 12 digits");
			return false;
		}

		try {
			Long.parseLong(_accountRequest.loyaltyNumber);
		} catch (NumberFormatException ex) {
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Loyalty Numbers must contain 8 to 12 digits");
			return false;
		}

		// *********************************************************
		// ViewHolder 3
		// ********************************************************

		_accountRequest.phone = _homephone.getText().toString();
		_accountRequest.mobilePhone = _mobilephone.getText().toString();
		_accountRequest.address = _address.getText().toString();
		_accountRequest.city = _city.getText().toString();
		_accountRequest.state = getStateCodeByName(_state.getText().toString());
		_accountRequest.zip = _zip.getText().toString();

		if (_accountRequest.mobilePhone.length() == 0) {
			_mobilephone.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Cell Phone can't be blank");
			return false;
		}

		try {

			if (_accountRequest.mobilePhone.length() > 0) {
				if ((_accountRequest.mobilePhone.length() > 0 && _accountRequest.mobilePhone
						.length() != 12)
						|| !_accountRequest.mobilePhone.substring(3, 4).equals(
								"-")
						|| !_accountRequest.mobilePhone.substring(7, 8).equals(
								"-")
						|| (Integer.parseInt(_accountRequest.mobilePhone
								.substring(0, 3)) < 0)
						|| (Integer.parseInt(_accountRequest.mobilePhone
								.substring(4, 7)) < 0)
						|| (Integer.parseInt(_accountRequest.mobilePhone
								.substring(8, 12)) < 0)) {
					_mobilephone.requestFocus();
					if (_accountRequest.mobilePhone.length() != 10) {
						// showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
						// "Cell Phone must be in xxx-xxx-xxxx format");
						showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
								"Mobile No. must be formatted 999-999-9999");
						return false;
					} else {
						if (Long.parseLong(_accountRequest.mobilePhone) < 0) {
							showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
									"Mobile No. must be formatted 999-999-9999");
							return false;
						}
					}
				}
			}
			// if (_accountRequest.mobilePhone.length() > 0) {
			// if ((_accountRequest.mobilePhone.length() > 0 &&
			// _accountRequest.mobilePhone
			// .length() != 12)
			// || !_accountRequest.mobilePhone.substring(3, 4).equals(
			// "-")
			// || !_accountRequest.mobilePhone.substring(7, 8).equals(
			// "-")
			// || (Integer.parseInt(_accountRequest.mobilePhone
			// .substring(0, 3)) < 0)
			// || (Integer.parseInt(_accountRequest.mobilePhone
			// .substring(4, 7)) < 0)
			// || (Integer.parseInt(_accountRequest.mobilePhone
			// .substring(8, 12)) < 0)) {
			// _mobilephone.requestFocus();
			// showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
			// "Cell Phone must be in xxx-xxx-xxxx format");
			// return false;
			// }
			// }
		} catch (NumberFormatException nfex) {
			_mobilephone.requestFocus();
			// showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
			// "Mobile Phone must be in xxx-xxx-xxxx format");
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Mobile No. must be formatted 999-999-9999");
			return false;
		}

		if (_accountRequest.phone.length() == 0) {
			_homephone.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Home Phone can't be blank");
			return false;
		}

		try {

			if (_accountRequest.phone.length() > 0) {
				if (_accountRequest.phone.length() != 12
						|| !_accountRequest.phone.substring(3, 4).equals("-")
						|| !_accountRequest.phone.substring(7, 8).equals("-")
						|| (Integer.parseInt(_accountRequest.phone.substring(0,
								3)) < 0)
						|| (Integer.parseInt(_accountRequest.phone.substring(4,
								7)) < 0)
						|| (Integer.parseInt(_accountRequest.phone.substring(8,
								12)) < 0)) {
					_homephone.requestFocus();
					if (_accountRequest.phone.length() != 10) {
						// showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
						// "Home Phone must be in xxx-xxx-xxxx format");
						showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
								"Phone No. must be formatted 999-999-9999");
						return false;
					} else {
						if (Long.parseLong(_accountRequest.phone) < 0) {
							showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
									"Phone No. must be formatted 999-999-9999");
							return false;
						}
					}
				}
			}
			// if (_accountRequest.phone.length() > 0) {
			// if (_accountRequest.phone.length() != 12
			// || !_accountRequest.phone.substring(3, 4).equals("-")
			// || !_accountRequest.phone.substring(7, 8).equals("-")
			// || (Integer.parseInt(_accountRequest.phone.substring(0,
			// 3)) < 0)
			// || (Integer.parseInt(_accountRequest.phone.substring(4,
			// 7)) < 0)
			// || (Integer.parseInt(_accountRequest.phone.substring(8,
			// 12)) < 0)) {
			// _homephone.requestFocus();
			// showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
			// "Home Phone must be in xxx-xxx-xxxx format");
			// return false;
			// }
			// }
		} catch (NumberFormatException nfex) {
			_homephone.requestFocus();
			// showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
			// "Home Phone must be in xxx-xxx-xxxx format");
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Phone No. must be formatted 999-999-9999");
			return false;
		}

		if (_accountRequest.address.length() == 0) {
			_address.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Mailing Address can't be blank");
			return false;
		}

		if (_accountRequest.city.length() == 0) {
			_city.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT, "City can't be blank");
			return false;
		}

		if (_accountRequest.state.equals(STATE_DEFAULT_TEXT)) {
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT, "State can't be blank");
			return false;
		}

		if (_accountRequest.zip.length() == 0) {
			_zip.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Zip Code can't be blank");
			return false;
		}

		try {
			if (_accountRequest.zip.length() != 5
					|| (Integer.parseInt(_accountRequest.zip.substring(0, 5)) < 0)) {
				_zip.requestFocus();
				showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
						"Zip Code must contain 5 digits");
				return false;
			}
		} catch (NumberFormatException nfex) {
			_zip.requestFocus();
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Zip Code must contain 5 digits");
			return false;
		}

		// *********************************************************
		// ViewHolder 4
		// ********************************************************

		// if(_SE_rewards_check.isSelected() && !_NO_thanks_check.isSelected()){
		// _accountRequest.issueCardFlag = true;
		// }
		// else{
		// _accountRequest.issueCardFlag = false;
		// }

		// *********************************************************
		// ViewHolder 5
		// ********************************************************
		_accountRequest.favoriteDept = _FD_text.getText().toString();
		if (_registrationPage == true && _FL_text.getText().equals("")) {
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Please choose a Favorite Location");
			return false;
		}

		if (_accountRequest.favoriteDept.equals("")) {
			showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
					"Please choose a Favorite Department");
			return false;
		}

		if (_New_Offer_check.isSelected()) {
			_accountRequest.sendEmailsFlag = true;
		} else {
			_accountRequest.sendEmailsFlag = false;
		}

		if (_Hot_Offer_check.isSelected()) {
			_accountRequest.sendTextsFlag = true;
		} else {
			_accountRequest.sendTextsFlag = false;
		}

		// *********************************************************
		// ViewHolder 6
		// ********************************************************

		if (_registrationPage == true) {
			if (!_TC_rewards_check.isSelected()) {
				showTextDialog(this, INPUT_ERROR_TITLE_TEXT,
						"You must agree to the Terms and Conditions in order to register.");
				return false;
			}

			if (_TC_rewards_check.isSelected()) {
				_accountRequest.termsAcceptedFlag = true;
			} else {
				_accountRequest.termsAcceptedFlag = false;
			}
		}
		return true;
	}

	public void accountSubmission() {

		if (validateAllFields() == false) {
			return;
		}

		if (_registrationPage == true) {
			makeRequest(_app.ACCOUNT_REGISTRATION_URL);
		} else {
			makeRequest(_app.ACCOUNT_UPDATE_URL);
		}
	}

	public void makeRequest(String url) {
		try {
			String auth = null;
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			String requestBody = gson.toJson(_accountRequest);

			if (!Utils.isNetworkAvailable(this)) {
				showNetworkUnavailableDialog(this);
				return;
			}

			if (url.equalsIgnoreCase(_app.ACCOUNT_UPDATE_URL)) {
				Login login = _app.getLogin();
				auth = login.authKey;

				showProgressDialog("Updating Account...");
			} else {
				showProgressDialog("Creating Account...");
			}

			_service = new WebService(this, Login.class, requestBody, auth);
			_service.execute(url);
			// handled by handleAccountServiceResponse() directly below
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void handleAccountServiceResponse() {
		try {
			int status = _service.getHttpStatusCode();
			dismissActiveDialog();

			if (status == 200) {
				String dialogText;

				if (_registrationPage == false)
					dialogText = "Account Updated.";
				else
					dialogText = "Account Created.";

				showTextDialog(this, "Request Succeeded", dialogText,
						"changeComplete");
				_app._currentAccountRequest = _accountRequest;
				_app.setPersistentDataAccount(_accountRequest);
				_app.savePersistentData();// save account information in
											// persistant file
				// CLP SDK
				try {
					if (_registrationPage == true) {
//						ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
//						data.add(new BasicNameValuePair("time", _app
//								.getCurrentTime()));
//						data.add(new BasicNameValuePair("event_name",
//								"Create_Account"));
						// EVENT_CLICK
					} else {
						Login _login;
						_login = _app.getLogin();
						_login.storeNumber = _accountRequest.storeNumber;
						_app.setLogin(_login);
//						ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
//						data.add(new BasicNameValuePair("time", _app
//								.getCurrentTime()));
//						data.add(new BasicNameValuePair("Event_Name",
//								"Update_Account"));
						// EVENT_CLICK
					}

				} catch (Exception e) {
					Log.e(_app.CLP_TRACK_ERROR, "EVENT_CLICK:" + e.getMessage());
				}
				// CLP
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
				{
					if (_registrationPage == false)
						showTextDialog(this, "Account Create Failed",
								error.errorMessage);
					else
						showTextDialog(this, "Account Update Failed",
								error.errorMessage);
				} else {

					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error", "Http Status code: "
					// + status);
				}

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void changeComplete() {
		dismissTextDialog();
		finish();
	}

	public void openStateDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(this);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.account_dialog);
		// dialog.setTitle("Choose One State...");

		int dialog_height = _contentViewHeight / 2;

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = dialog_height;
		lp.gravity = Gravity.BOTTOM;
		dialog.getWindow().setAttributes(lp);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(""); // Choose One State..
		text.setTextColor(Color.BLACK);
		// text.setBackgroundColor(Color.RED);

		ListView _stateList = (ListView) dialog.findViewById(R.id.listview1);
		// _stateList.setBackgroundColor(Color.GREEN);

		ArrayList<String> stateList = new ArrayList<String>();
		stateList.addAll(Arrays.asList(_states));

		// Create ArrayAdapter using the planet list.
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
				R.layout.account_listrow, stateList);

		// Set the ArrayAdapter as the ListView's adapter.
		_stateList.setAdapter(listAdapter);
		_stateList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, int position, long id) {
						final String item = (String) parent
								.getItemAtPosition(position);
						_state.setTextColor(_textColor);
						_state.setText(item);
						dialog.dismiss();
					}
				});

		dialog.show();
	}

	public void openDeptDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(this);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.account_dialog);
		// dialog.setTitle("Choose One Department...");

		int dialog_height = _contentViewHeight / 2;

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = dialog_height;
		lp.gravity = Gravity.BOTTOM;
		dialog.getWindow().setAttributes(lp);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(""); // Choose One Department..
		text.setTextColor(Color.BLACK);
		// text.setBackgroundColor(Color.RED);

		ListView _deptList = (ListView) dialog.findViewById(R.id.listview1);
		// _stateList.setBackgroundColor(Color.GREEN);

		ArrayList<String> deptList = new ArrayList<String>();
		deptList.addAll(Arrays.asList(_departments));

		// Create ArrayAdapter using the planet list.
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
				R.layout.account_listrow, deptList);

		// Set the ArrayAdapter as the ListView's adapter.
		_deptList.setAdapter(listAdapter);
		_deptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				_FD_text.setTextColor(_textColor);
				_FD_text.setText(item);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public String getStateCodeByName(String StateName) {
		String code = "";
		if (StateName.equalsIgnoreCase("Alabama")) {
			code = "AL";
		} else if (StateName.equalsIgnoreCase("Alaska")) {
			code = "AK";
		} else if (StateName.equalsIgnoreCase("Arizona")) {
			code = "AZ";
		} else if (StateName.equalsIgnoreCase("Arkansas")) {
			code = "AR";
		} else if (StateName.equalsIgnoreCase("California")) {
			code = "CA";
		} else if (StateName.equalsIgnoreCase("Colorado")) {
			code = "CO";
		} else if (StateName.equalsIgnoreCase("Connecticut")) {
			code = "CT";
		} else if (StateName.equalsIgnoreCase("Delaware")) {
			code = "DE";
		} else if (StateName.equalsIgnoreCase("Florida")) {
			code = "FL";
		} else if (StateName.equalsIgnoreCase("Georgia")) {
			code = "GA";
		} else if (StateName.equalsIgnoreCase("Hawaii")) {
			code = "HI";
		} else if (StateName.equalsIgnoreCase("Idaho")) {
			code = "ID";
		} else if (StateName.equalsIgnoreCase("Illinois")) {
			code = "IL";
		} else if (StateName.equalsIgnoreCase("Indiana")) {
			code = "IN";
		} else if (StateName.equalsIgnoreCase("Iowa")) {
			code = "IA";
		} else if (StateName.equalsIgnoreCase("Kansas")) {
			code = "KS";
		} else if (StateName.equalsIgnoreCase("Kentucky")) {
			code = "KY";
		} else if (StateName.equalsIgnoreCase("Louisiana")) {
			code = "LA";
		} else if (StateName.equalsIgnoreCase("Maine")) {
			code = "ME";
		} else if (StateName.equalsIgnoreCase("Maryland")) {
			code = "MD";
		} else if (StateName.equalsIgnoreCase("Massachusetts")) {
			code = "MA";
		} else if (StateName.equalsIgnoreCase("Michigan")) {
			code = "MI";
		} else if (StateName.equalsIgnoreCase("Minnesota")) {
			code = "MN";
		} else if (StateName.equalsIgnoreCase("Mississippi")) {
			code = "MS";
		} else if (StateName.equalsIgnoreCase("Missouri")) {
			code = "MO";
		} else if (StateName.equalsIgnoreCase("Montana")) {
			code = "MT";
		} else if (StateName.equalsIgnoreCase("Nebraska")) {
			code = "NE";
		} else if (StateName.equalsIgnoreCase("Nevada")) {
			code = "NV";
		} else if (StateName.equalsIgnoreCase("New Hampshire")) {
			code = "NH";
		} else if (StateName.equalsIgnoreCase("New Jersey")) {
			code = "NJ";
		} else if (StateName.equalsIgnoreCase("New Mexico")) {
			code = "NM";
		} else if (StateName.equalsIgnoreCase("New York")) {
			code = "NY";
		} else if (StateName.equalsIgnoreCase("North Carolina")) {
			code = "NC";
		} else if (StateName.equalsIgnoreCase("North Dakota")) {
			code = "ND";
		} else if (StateName.equalsIgnoreCase("Ohio")) {
			code = "OH";
		} else if (StateName.equalsIgnoreCase("Oklahoma")) {
			code = "OK";
		} else if (StateName.equalsIgnoreCase("Oregon")) {
			code = "OR";
		} else if (StateName.equalsIgnoreCase("Pennsylvania")) {
			code = "PA";
		} else if (StateName.equalsIgnoreCase("Rhode Island")) {
			code = "RI";
		} else if (StateName.equalsIgnoreCase("South Carolina")) {
			code = "SC";
		} else if (StateName.equalsIgnoreCase("South Dakota")) {
			code = "SD";
		} else if (StateName.equalsIgnoreCase("Tennessee")) {
			code = "TN";
		} else if (StateName.equalsIgnoreCase("Texas")) {
			code = "TX";
		} else if (StateName.equalsIgnoreCase("Utah")) {
			code = "UT";
		} else if (StateName.equalsIgnoreCase("Vermont")) {
			code = "VT";
		} else if (StateName.equalsIgnoreCase("Virginia")) {
			code = "VA";
		} else if (StateName.equalsIgnoreCase("Washington")) {
			code = "WA";
		} else if (StateName.equalsIgnoreCase("West Virginia")) {
			code = "WV";
		} else if (StateName.equalsIgnoreCase("Wisconsin")) {
			code = "WI";
		} else if (StateName.equalsIgnoreCase("Wyoming")) {
			code = "WY";
		}
		return code;
	}

	public String getStateNameByCode(String StateCode) {
		String name = "";

		if (StateCode.equalsIgnoreCase("AL")) {
			name = "Alabama";
		} else if (StateCode.equalsIgnoreCase("AK")) {
			name = "Alaska";
		} else if (StateCode.equalsIgnoreCase("AZ")) {
			name = "Arizona";
		} else if (StateCode.equalsIgnoreCase("AR")) {
			name = "Arkansas";
		} else if (StateCode.equalsIgnoreCase("CA")) {
			name = "California";
		} else if (StateCode.equalsIgnoreCase("CO")) {
			name = "Colorado";
		} else if (StateCode.equalsIgnoreCase("CT")) {
			name = "Connecticut";
		} else if (StateCode.equalsIgnoreCase("DE")) {
			name = "Delaware";
		} else if (StateCode.equalsIgnoreCase("FL")) {
			name = "Florida";
		} else if (StateCode.equalsIgnoreCase("GA")) {
			name = "Georgia";
		} else if (StateCode.equalsIgnoreCase("HI")) {
			name = "Hawaii";
		} else if (StateCode.equalsIgnoreCase("ID")) {
			name = "Idaho";
		} else if (StateCode.equalsIgnoreCase("IL")) {
			name = "Illinois";
		} else if (StateCode.equalsIgnoreCase("IN")) {
			name = "Indiana";
		} else if (StateCode.equalsIgnoreCase("IA")) {
			name = "Iowa";
		} else if (StateCode.equalsIgnoreCase("KS")) {
			name = "Kansas";
		} else if (StateCode.equalsIgnoreCase("KY")) {
			name = "Kentucky";
		} else if (StateCode.equalsIgnoreCase("LA")) {
			name = "Louisiana";
		} else if (StateCode.equalsIgnoreCase("ME")) {
			name = "Maine";
		} else if (StateCode.equalsIgnoreCase("MD")) {
			name = "Maryland";
		} else if (StateCode.equalsIgnoreCase("MA")) {
			name = "Massachusetts";
		} else if (StateCode.equalsIgnoreCase("MI")) {
			name = "Michigan";
		} else if (StateCode.equalsIgnoreCase("MN")) {
			name = "Minnesota";
		} else if (StateCode.equalsIgnoreCase("MS")) {
			name = "Mississippi";
		} else if (StateCode.equalsIgnoreCase("MO")) {
			name = "Missouri";
		} else if (StateCode.equalsIgnoreCase("MT")) {
			name = "Montana";
		} else if (StateCode.equalsIgnoreCase("NE")) {
			name = "Nebraska";
		} else if (StateCode.equalsIgnoreCase("NV")) {
			name = "Nevada";
		} else if (StateCode.equalsIgnoreCase("NH")) {
			name = "New Hampshire";
		} else if (StateCode.equalsIgnoreCase("NJ")) {
			name = "New Jersey";
		} else if (StateCode.equalsIgnoreCase("NM")) {
			name = "New Mexico";
		} else if (StateCode.equalsIgnoreCase("NY")) {
			name = "New York";
		} else if (StateCode.equalsIgnoreCase("NC")) {
			name = "North Carolina";
		} else if (StateCode.equalsIgnoreCase("ND")) {
			name = "North Dakota";
		} else if (StateCode.equalsIgnoreCase("OH")) {
			name = "Ohio";
		} else if (StateCode.equalsIgnoreCase("OK")) {
			name = "Oklahoma";
		} else if (StateCode.equalsIgnoreCase("OR")) {
			name = "Oregon";
		} else if (StateCode.equalsIgnoreCase("PA")) {
			name = "Pennsylvania";
		} else if (StateCode.equalsIgnoreCase("RI")) {
			name = "Rhode Island";
		} else if (StateCode.equalsIgnoreCase("SC")) {
			name = "South Carolina";
		} else if (StateCode.equalsIgnoreCase("SD")) {
			name = "South Dakota";
		} else if (StateCode.equalsIgnoreCase("TN")) {
			name = "Tennessee";
		} else if (StateCode.equalsIgnoreCase("TX")) {
			name = "Texas";
		} else if (StateCode.equalsIgnoreCase("UT")) {
			name = "Utah";
		} else if (StateCode.equalsIgnoreCase("VT")) {
			name = "Vermont";
		} else if (StateCode.equalsIgnoreCase("VA")) {
			name = "Virginia";
		} else if (StateCode.equalsIgnoreCase("WA")) {
			name = "Washington";
		} else if (StateCode.equalsIgnoreCase("WV")) {
			name = "West Virginia";
		} else if (StateCode.equalsIgnoreCase("WI")) {
			name = "Wisconsin";
		} else if (StateCode.equalsIgnoreCase("WY")) {
			name = "Wyoming";
		}

		return name;
	}

	public void toggleStateTable() {
		try {
			// if(_stateList.getVisibility() == View.VISIBLE)
			// {
			// _stateList.setVisibility(View.GONE);
			// }
			// else
			// {
			// _stateList.setVisibility(View.VISIBLE);
			// _inputManager.hideSoftInputFromWindow(_view3.getWindowToken(),
			// 0);
			// }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void toggleDepartmentTable() {
		try {
			// _departmentText.setTextColor(_textColor);
			//
			// if(_departmentList.getVisibility() == View.VISIBLE)
			// _departmentList.setVisibility(View.GONE);
			// else
			// _departmentList.setVisibility(View.VISIBLE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onServiceResponse(Object responseObject) {
		if (responseObject instanceof Login) {
			handleAccountServiceResponse();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		// do nothing, make the user hit the home button to background the app
	}

}
