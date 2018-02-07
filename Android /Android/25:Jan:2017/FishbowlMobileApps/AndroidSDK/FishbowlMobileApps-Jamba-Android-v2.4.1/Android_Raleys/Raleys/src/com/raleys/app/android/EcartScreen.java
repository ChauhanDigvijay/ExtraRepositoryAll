package com.raleys.app.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.EcartAppointment;
import com.raleys.app.android.models.EcartOrderRequest;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Store;
import com.raleys.libandroid.SizedImageTextButton;
import com.raleys.libandroid.SmartScrollView;
import com.raleys.libandroid.SmartTextView;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceError;
import com.raleys.libandroid.WebServiceListener;

public class EcartScreen extends BaseScreen implements WebServiceListener {
	private RaleysApplication _app;
	private int _textColor;
	private int _hintColor;
	private String _substitutionPreference;
	private String _bagPreference;
	private String _appointmentDay;
	private String _appointmentTime;
	private EditText _instructionEditText;
	private SizedImageTextButton _submitButton;
	private SmartTextView _appointmentDayText;
	private SmartTextView _appointmentTimeText;
	private ListView _appointmentDayList;
	private ListView _appointmentTimeList;
	private AccountDropdownAdapter _appointmentDayAdapter;
	private AccountDropdownAdapter _appointmentTimeAdapter;
	private EcartConfirmView _ecartConfirmView;
	private EcartOrderRequest _ecartOrderRequest;
	private InputMethodManager _inputManager;
	private Login _login;
	private WebService _service;

	final String SUB_PREF_1 = "at Personal Shopper's discretion.";
	final String SUB_PREF_2 = "with different size but same brand.";
	final String SUB_PREF_3 = "with same size but different brand.";
	final String SUB_PREF_4 = "with similar item that has Extra Points.";
	final String SUB_PREF_5 = "Don't substitute.  Omit if unavailable.";
	final String BAG_PREF_1 = "Use paper bags.*";
	final String BAG_PREF_2 = "Use reusable bags.";

	final int VAL_SUB_PREF_1 = 1;
	final int VAL_SUB_PREF_2 = 2;
	final int VAL_SUB_PREF_3 = 3;
	final int VAL_SUB_PREF_4 = 4;
	final int VAL_SUB_PREF_5 = 5;
	final int VAL_BAG_PREF_1 = 1;
	final int VAL_BAG_PREF_2 = 2;

	private int _substitutionPreferenceValue;
	private int _bagPreferenceValue;

	private String[] _appointmentDays;
	private String[] _appointmentTimes;
	RelativeLayout.LayoutParams layoutParams;
	ImageView _shopperCheckBox, _differentCheckBox, _sameCheckBox,
			_similarCheckBox, _noSubstituteCheckBox;
	ImageView _paperBagCheckBox;
	ImageView _reusableBagCheckBox;
	RelativeLayout _ecartMainLayout;
	SmartScrollView _ecartScrollView;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			_app = (RaleysApplication) getApplication();
			_screenWidth = _app.getScreenWidth();
			_screenHeight = _app.getScreenHeight();
			_headerHeight = _app.getHeaderHeight();
			_footerHeight = _app.getFooterHeight();
			_contentViewHeight = _screenHeight - _navBarHeight;
			_contentViewWidth = _screenWidth;
			int textWidth = (int) (_contentViewWidth * .85);
			_CreateAccountButton.setVisibility(View.GONE); // Hiding Create
															// button
			_inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			_appointmentDays = new String[_app._currentEcartPreOrderResponse.appointmentList
					.size()];
			_appointmentTimes = new String[0];

			for (int i = 0; i < _app._currentEcartPreOrderResponse.appointmentList
					.size(); i++) {
				EcartAppointment appointment = _app._currentEcartPreOrderResponse.appointmentList
						.get(i);
				_appointmentDays[i] = appointment.appointmentDate;
			}

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
			_textColor = Color.argb(255, 55, 55, 55);
			_hintColor = Color.argb(255, 55, 55, 55);
			_mainLayout.setBackgroundColor(Color.rgb(225, 225, 225));

			int editWidth = (int) (_contentViewWidth * .96);
			int editXOrigin = (int) (_contentViewWidth * .02);
			int promptHeight = (int) (_contentViewHeight * .05);
			int promptTextBottomPad = (int) (_contentViewHeight * .005);
			int checkBoxSize = (int) (_contentViewHeight * .05);
			int checkBoxTextWidth = editWidth - checkBoxSize
					- (editXOrigin * 2);
			int checkBoxTextXOrigin = checkBoxSize + (editXOrigin * 2);
			int checkBoxTextHeight = (int) (checkBoxSize * .6);
			int checkBoxTextYOffset = (checkBoxSize - checkBoxTextHeight) / 2;
			int checkBoxVerticalPad = (int) (_contentViewHeight * .01);
			_substitutionPreference = SUB_PREF_1;
			_substitutionPreferenceValue = VAL_SUB_PREF_1;
			_bagPreference = BAG_PREF_1;
			_bagPreferenceValue = VAL_BAG_PREF_1;

			_appointmentDay = "";
			_appointmentTime = "";

			int layoutPadding = (int) (_contentViewWidth * .04);

			RelativeLayout _accountLayout = new RelativeLayout(this);
			_accountLayout.setBackgroundColor(Color.LTGRAY);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = _headerHeight;
			layoutParams.leftMargin = 0;
			layoutParams.height = _contentViewHeight;
			layoutParams.width = _contentViewWidth;
			_mainLayout.addView(_accountLayout, layoutParams);

			ScrollView _scrollview = new ScrollView(this);
			_scrollview.setBackgroundColor(Color.LTGRAY);
			_scrollview.setSmoothScrollingEnabled(true);
			RelativeLayout.LayoutParams slp = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			_accountLayout.addView(_scrollview, slp);

			// _ecartScrollView = new SmartScrollView(this);
			// layoutParams = new RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.MATCH_PARENT,
			// RelativeLayout.LayoutParams.MATCH_PARENT);
			// _ecartScrollView.setVerticalScrollBarEnabled(false);
			// _ecartScrollView.setHorizontalScrollBarEnabled(false);
			// _ecartScrollView.setSmoothScrollingEnabled(true);
			// layoutParams.topMargin = _headerHeight;
			// layoutParams.leftMargin = 0;
			// _accountLayout.addView(_ecartScrollView, layoutParams);

			LinearLayout _accountHolder_parent = new LinearLayout(this);
			_accountHolder_parent.setOrientation(LinearLayout.VERTICAL);
			_accountHolder_parent.setBackgroundColor(Color.LTGRAY);
			_accountHolder_parent.setGravity(Gravity.CENTER_HORIZONTAL);
			_accountHolder_parent.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT));
			_scrollview.addView(_accountHolder_parent);

			// holder for all the account elements
			LinearLayout _accountholder = new LinearLayout(this);
			_accountholder.setOrientation(LinearLayout.VERTICAL);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			layoutParams.width = _contentViewWidth;
			_accountholder.setPadding(layoutPadding, layoutPadding,
					layoutPadding, layoutPadding);
			_accountHolder_parent.addView(_accountholder, layoutParams);

			int sdk = android.os.Build.VERSION.SDK_INT;
			float[] CornerRadius;
			float[] innerCornerRadius;
			int cornerSize = (int) (layoutPadding * .5);
			int innercornerSize = (int) (layoutPadding * .3);

			CornerRadius = new float[] { cornerSize, cornerSize, cornerSize,
					cornerSize, cornerSize, cornerSize, cornerSize, cornerSize };
			innerCornerRadius = new float[] { innercornerSize, innercornerSize,
					innercornerSize, innercornerSize, innercornerSize,
					innercornerSize, innercornerSize, innercornerSize };

			// Dynamic layout creation Part
			RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
					new RectF(0, 0, 0, 0),
					new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable top_layer = new CustomShapeDrawable(bottom_rect,
					Color.TRANSPARENT, Color.WHITE, 0);
			LayerDrawable _layoutBackground = new LayerDrawable(
					new Drawable[] { top_layer });
			_layoutBackground.setLayerInset(0, 0, 0, 0, 0);

			_ecartMainLayout = new RelativeLayout(this);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, layoutPadding, 0, layoutPadding);
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				_ecartMainLayout.setBackgroundDrawable(_layoutBackground);
			} else {
				_ecartMainLayout.setBackground(_layoutBackground);
			}
			_accountholder.addView(_ecartMainLayout, layoutParams);

			SmartTextView headerText = new SmartTextView(this, textWidth,
					(int) (_contentViewHeight * .035), 1, Color.TRANSPARENT,
					Color.BLACK, _boldFont, "Welcome to E-cart!", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = layoutPadding;
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			_ecartMainLayout.addView(headerText, layoutParams);

			_login = _app.getLogin();
			String storeNameText = null;
			String storeAddressText = null;
			List<Store> storeList = _app.getStoresList();

			if (storeList != null && storeList.size() > 0) {
				for (Store store : storeList) {
					if (_login.storeNumber == store.storeNumber) {
						storeNameText = store.chain;
						storeAddressText = store.address + ", " + store.city
								+ ", " + store.state + ", " + store.zip;
						break;
					}
				}
			} else // this shouldn't happen, but just in case
			{
				storeNameText = String.valueOf(_login.storeNumber);
				storeAddressText = "Store " + _login.storeNumber
						+ " additional details unavailable at this time";
			}

			SmartTextView locationPromptText = new SmartTextView(this,
					textWidth, (int) (_contentViewHeight * .035), 1,
					Color.TRANSPARENT, Color.BLACK, _boldFont,
					"You are ordering at: " + storeNameText, Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = (int) (_contentViewHeight * .07);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			_ecartMainLayout.addView(locationPromptText, layoutParams);

			SmartTextView locationText = new SmartTextView(this, textWidth,
					(int) (_contentViewHeight * .05), 2, Color.TRANSPARENT,
					Color.DKGRAY, _normalFont, storeAddressText, Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = (int) (_contentViewHeight * .13);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			_ecartMainLayout.addView(locationText, layoutParams);

			// ------------------------------------------------------------------------------------------
			// pickup date section begin. NOTE: add this last so it's list
			// covers other fields when exposed
			// ------------------------------------------------------------------------------------------
			int pickupDateYOrigin = (int) (_contentViewHeight * .16);

			int tablesYOrigin = pickupDateYOrigin + promptHeight;
			int tableHeight = (int) (_contentViewHeight * .06);
			int tablesTextSize = (int) (tableHeight * .7);
			int tablesTextYOrigin = tablesYOrigin
					+ (tableHeight - tablesTextSize) / 2;
			int dayWidth = (int) (_contentViewWidth * .35);
			int timeWidth = (int) (_contentViewWidth * .52);
			int timeXOrigin = dayWidth + (editXOrigin * 2);

			// Dynamic layout creation Part
			RoundRectShape appointmentDayRect = new RoundRectShape(
					innerCornerRadius, new RectF(0, 0, 0, 0), new float[] { 0,
							0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable appointmentDayBack = new CustomShapeDrawable(
					appointmentDayRect, Color.DKGRAY, Color.WHITE, 0);

			LayerDrawable appointmentDay = new LayerDrawable(
					new Drawable[] { appointmentDayBack });
			appointmentDay.setLayerInset(0, 0, 0, 0, 0);

			Bitmap appointmentDayBackground = _app.getAppBitmap(
					"ecart_appointment_day_dropdown_item",
					R.drawable.account_cell_medium, dayWidth, tableHeight);
			Bitmap appointmentTimeBackground = _app.getAppBitmap(
					"ecart_appointment_time_dropdown_item",
					R.drawable.account_cell_medium, timeWidth, tableHeight);

			// appointment day
			ImageView appointmentDayView = new ImageView(this);
			appointmentDayView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleAppointmentDayTable();
				}
			});
			// SizedImageView appointmentDayView = new SizedImageView(this,
			// dayWidth, tableHeight);
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				appointmentDayView.setBackgroundDrawable(appointmentDay);
			} else {
				appointmentDayView.setBackground(appointmentDay);
			}
			// appointmentDayView.setImageBitmap(_app.getAppBitmap(
			// "ecart_appointment_day_background",
			// R.drawable.account_cell_medium, dayWidth, tableHeight));
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = tablesYOrigin;
			layoutParams.width = dayWidth;
			layoutParams.height = tableHeight;
			_ecartMainLayout.addView(appointmentDayView, layoutParams);

			ImageView _appointmentDayButton = new ImageView(this);
			_appointmentDayButton
					.setBackgroundResource(R.drawable.account_updown_button);
			// _appointmentDayButton = new SizedImageButton(this,
			// _app.getAppBitmap("ecart_appointment_day_button",
			// R.drawable.account_updown_button, tableHeight,
			// tableHeight));
			_appointmentDayButton
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							toggleAppointmentDayTable();
						}
					});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin + dayWidth - tableHeight;
			layoutParams.topMargin = tablesYOrigin;
			layoutParams.width = tableHeight;
			layoutParams.height = tableHeight;
			_ecartMainLayout.addView(_appointmentDayButton, layoutParams);

			_appointmentDayText = new SmartTextView(this, dayWidth,
					(int) (tablesTextSize * 0.65), 1, Color.TRANSPARENT,
					_hintColor, _normalFont, Align.LEFT);
			_appointmentDayText.setPadding(timeXOrigin, 0, 0, 0);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin + (int) (dayWidth * .02);
			layoutParams.topMargin = tablesTextYOrigin;
			_ecartMainLayout.addView(_appointmentDayText, layoutParams);

			_appointmentDayList = new ListView(this);
			_appointmentDayAdapter = new AccountDropdownAdapter(this,
					_appointmentDays, dayWidth, tableHeight,
					appointmentDayBackground, _textColor);
			_appointmentDayList.setAdapter(_appointmentDayAdapter);
			_appointmentDayList.setOnTouchListener(new OnTouchListener() {
				// Setting on Touch Listener for handling the touch inside
				// ScrollView
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// Disallow the touch request for parent scroll on touch of
					// child view
					_ecartScrollView.requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});

			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.width = dayWidth;
			layoutParams.topMargin = tablesYOrigin + tableHeight; // offset for
																	// button
																	// transparency
			layoutParams.height = tableHeight * 4;
			_ecartMainLayout.addView(_appointmentDayList, layoutParams);

			_appointmentDayList.setVisibility(View.GONE);
			/*
			 * _appointmentDayList .setOnItemClickListener(new
			 * AdapterView.OnItemClickListener() {
			 * 
			 * @Override public void onItemClick(AdapterView<?> parent, final
			 * View view, int position, long id) { String selectedDay = (String)
			 * parent .getItemAtPosition(position);
			 * 
			 * if (selectedDay.compareTo(_appointmentDay) != 0) {
			 * _appointmentDay = (String) parent .getItemAtPosition(position);
			 * _appointmentDayText.setTextColor(_textColor);
			 * _appointmentDayText.writeText(_appointmentDay);
			 * 
			 * // reset the appointment time components when // the day is
			 * selected _appointmentTime = "";
			 * _appointmentTimeText.writeText("Time");
			 * _appointmentTimeAdapter._dropdownList = null; EcartAppointment
			 * appointment = _app._currentEcartPreOrderResponse.appointmentList
			 * .get(position); _appointmentTimeAdapter._dropdownList = new
			 * String[appointment.appointmentTimeList .size()];
			 * 
			 * for (int i = 0; i < appointment.appointmentTimeList .size(); i++)
			 * _appointmentTimeAdapter._dropdownList[i] =
			 * appointment.appointmentTimeList .get(i); }
			 * 
			 * _appointmentTimeAdapter.notifyDataSetChanged();
			 * _appointmentDayList.setVisibility(View.GONE); } });
			 */

			// appointment time
			ImageView appointmentTimeView = new ImageView(this);
			appointmentTimeView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleAppointmentTimeTable();
				}
			});
			// SizedImageView appointmentTimeView = new SizedImageView(this,
			// timeWidth, tableHeight);
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				appointmentTimeView.setBackgroundDrawable(appointmentDay);
			} else {
				appointmentTimeView.setBackground(appointmentDay);
			}
			// appointmentTimeView.setImageBitmap(_app.getAppBitmap(
			// "ecart_appointment_time_background",
			// R.drawable.account_cell_medium, timeWidth, tableHeight));
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = timeXOrigin;
			layoutParams.topMargin = tablesYOrigin;
			layoutParams.width = timeWidth;
			layoutParams.height = tableHeight;
			_ecartMainLayout.addView(appointmentTimeView, layoutParams);

			ImageView _appointmentTimeButton = new ImageView(this);
			_appointmentTimeButton
					.setBackgroundResource(R.drawable.account_updown_button);
			// _appointmentTimeButton = new SizedImageButton(this,
			// _app.getAppBitmap("ecart_appointment_time_button",
			// R.drawable.account_updown_button, tableHeight,
			// tableHeight));
			_appointmentTimeButton
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							toggleAppointmentTimeTable();
						}
					});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = timeXOrigin + timeWidth - tableHeight;
			layoutParams.topMargin = tablesYOrigin;
			layoutParams.width = tableHeight;
			layoutParams.height = tableHeight;
			_ecartMainLayout.addView(_appointmentTimeButton, layoutParams);

			_appointmentTimeText = new SmartTextView(this, timeWidth,
					(int) (tablesTextSize * 0.65), 1, Color.TRANSPARENT,
					_hintColor, _normalFont, "Time", Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			_appointmentTimeText.setPadding(timeXOrigin, 0, 0, 0);
			layoutParams.leftMargin = timeXOrigin + (int) (timeWidth * .02);
			layoutParams.topMargin = tablesTextYOrigin;
			_ecartMainLayout.addView(_appointmentTimeText, layoutParams);

			_appointmentTimeList = new ListView(this);
			_appointmentTimeAdapter = new AccountDropdownAdapter(this,
					_appointmentTimes, timeWidth, tableHeight,
					appointmentTimeBackground, _textColor);
			_appointmentTimeList.setAdapter(_appointmentTimeAdapter);
			_appointmentTimeList.setOnTouchListener(new OnTouchListener() {
				// Setting on Touch Listener for handling the touch inside
				// ScrollView
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// Disallow the touch request for parent scroll on touch of
					// child view
					_ecartScrollView.requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = timeXOrigin;
			layoutParams.width = timeWidth;
			layoutParams.topMargin = tablesYOrigin + tableHeight; // offset for
																	// button
																	// transparency
			layoutParams.height = tableHeight * 4;
			_ecartMainLayout.addView(_appointmentTimeList, layoutParams);
			_appointmentTimeList.setVisibility(View.GONE);

			_appointmentTimeList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								final View view, int position, long id) {
							_appointmentTime = (String) parent
									.getItemAtPosition(position);
							_appointmentTimeText.setTextColor(_textColor);
							_appointmentTimeText.writeText(_appointmentTime);
							_appointmentTimeList.setVisibility(View.GONE);
						}
					});

			// ------------------------------------------------------------------------------------------
			// Date and Time default value populated start
			// ------------------------------------------------------------------------------------------

			// By default first value selected

			// _appointmentDay = String.valueOf(_appointmentDays[0]);
			_appointmentDay = "";
			_appointmentDayText.writeText("Date");

			if (_appointmentTimeAdapter._dropdownList.length == 0) {

				// Creating time list array
				// the day is selected
				_appointmentTime = "";
				_appointmentTimeText.writeText("Time");
				_appointmentTimeAdapter._dropdownList = null;
				EcartAppointment appointment = _app._currentEcartPreOrderResponse.appointmentList
						.get(0);
				_appointmentTimeAdapter._dropdownList = new String[appointment.appointmentTimeList
						.size()];

				for (int i = 0; i < appointment.appointmentTimeList.size(); i++)
					_appointmentTimeAdapter._dropdownList[i] = appointment.appointmentTimeList
							.get(i);

				// By default first value selected

				if (_appointmentDay.length() == 0) {
					_appointmentTime = "";
					_appointmentTimeText.writeText("Time");
				} else {
					_appointmentTime = String
							.valueOf(_appointmentTimeAdapter._dropdownList[0]);
					_appointmentTimeText.writeText(_appointmentTime);
				}
			}

			// ------------------------------------------------------------------------------------------
			// Date and Time default value populated start
			// ------------------------------------------------------------------------------------------

			// ------------------------------------------------------------------------------------------
			// pickup date section end
			// ------------------------------------------------------------------------------------------

			// Shopper INstruction start
			int instructionsYOrigin = (int) (_contentViewHeight * .30);
			int textFieldHeight = (int) (promptHeight * 1.9);

			// Dynamic layout creation Part
			RoundRectShape instructionEditText = new RoundRectShape(
					innerCornerRadius, new RectF(0, 0, 0, 0), new float[] { 0,
							0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable topinstructionEditText = new CustomShapeDrawable(
					instructionEditText, Color.DKGRAY, Color.WHITE, 0);

			LayerDrawable _layoutinstructionEditText = new LayerDrawable(
					new Drawable[] { topinstructionEditText });
			_layoutinstructionEditText.setLayerInset(0, 0, 0, 0, 0);

			// instruction text field
			_instructionEditText = new EditText(this);
			_instructionEditText.setPadding(0, 0, 0, 0);//v2.3 fix for Lollipop
			_instructionEditText.setTextColor(Color.DKGRAY);
			_instructionEditText.setGravity(Gravity.TOP | Gravity.LEFT);
			_instructionEditText.setTypeface(_normalFont);
			_instructionEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (_contentViewHeight * .025));
			// _instructionEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
			_instructionEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			_instructionEditText.setSingleLine(false);
			_instructionEditText.setHintTextColor(Color.DKGRAY);
			_instructionEditText.setHint("Personal Shopper Instructions");
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = instructionsYOrigin - promptTextBottomPad;
			layoutParams.width = textWidth;
			layoutParams.height = textFieldHeight;
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				_instructionEditText
						.setBackgroundDrawable(_layoutinstructionEditText);
			} else {
				_instructionEditText.setBackground(_layoutinstructionEditText);
			}
			_ecartMainLayout.addView(_instructionEditText, layoutParams);

			_instructionEditText
					.setOnEditorActionListener(new OnEditorActionListener() {

						@Override
						public boolean onEditorAction(TextView v, int actionId,
								KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										_mainLayout.getWindowToken(), 0);
								return true;
							}
							return false;
						}
					});

			// ------------------------------------------------------------------------------------------
			// submit button
			// ------------------------------------------------------------------------------------------
			int submitButtonWidth = (int) (_contentViewWidth * .25);
			int submitButtonHeight = (int) (_contentViewHeight * .06);

			int buttonYOrigin = (int) (_contentViewHeight * .325)
					+ textFieldHeight;
			// Dynamic layout creation Part
			RoundRectShape submitButton = new RoundRectShape(CornerRadius,
					new RectF(0, 0, 0, 0),
					new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable submitDraw = new CustomShapeDrawable(submitButton,
					Color.DKGRAY, Color.rgb(187, 3, 3), 0);
			LayerDrawable submitLayerBackground = new LayerDrawable(
					new Drawable[] { submitDraw });
			submitLayerBackground.setLayerInset(0, 0, 0, 0, 0);

			Button SubmitButton = new Button(this);
			SubmitButton.setText("Submit");
			SubmitButton.setGravity(Gravity.CENTER);
			SubmitButton.setTypeface(_boldFont);
			SubmitButton.setTextColor(Color.WHITE);
			SubmitButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (submitButtonHeight * .35));
			SubmitButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					confirmOrder();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = (_contentViewWidth - submitButtonWidth) / 2;
			layoutParams.topMargin = buttonYOrigin;
			layoutParams.width = submitButtonWidth;
			layoutParams.height = submitButtonHeight;
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				SubmitButton.setBackgroundDrawable(submitLayerBackground);
			} else {
				SubmitButton.setBackground(submitLayerBackground);
			}
			_ecartMainLayout.addView(SubmitButton, layoutParams);

			// ------------------------------------------------------------------------------------------
			// substitution preference section begin
			// ------------------------------------------------------------------------------------------
			int substitutePreferenceYOrigin = (int) (_contentViewHeight * .5);

			SmartTextView substitutePreferencePromptText = new SmartTextView(
					this, textWidth, (int) (_contentViewHeight * .035), 1,
					Color.TRANSPARENT, Color.BLACK, _boldFont,
					"Substitute any item", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = substitutePreferenceYOrigin
					- promptTextBottomPad;
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			_ecartMainLayout.addView(substitutePreferencePromptText,
					layoutParams);

			// Separator Layout start
			int spaceYOrigin = substitutePreferenceYOrigin + promptHeight
					+ checkBoxVerticalPad;
			RelativeLayout _seperatorLayout = new RelativeLayout(this);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = layoutPadding;
			layoutParams.rightMargin = layoutPadding;
			layoutParams.topMargin = spaceYOrigin - promptTextBottomPad;
			layoutParams.width = editWidth;
			layoutParams.height = (int) (textFieldHeight * .015);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			_seperatorLayout.setBackgroundColor(Color.DKGRAY);
			_ecartMainLayout.addView(_seperatorLayout, layoutParams);
			// Separator Layout end

			// shopper discretion line
			int shopperDiscretionYOrigin = spaceYOrigin + checkBoxVerticalPad;

			_shopperCheckBox = new ImageView(this);
			_shopperCheckBox
					.setBackgroundResource(R.drawable.product_checked_box);
			_shopperCheckBox.setSelected(true);
			_shopperCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					_substitutionPreference = SUB_PREF_1;
					_substitutionPreferenceValue = VAL_SUB_PREF_1;
					_shopperCheckBox.setSelected(true);
					_shopperCheckBox
							.setBackgroundResource(R.drawable.product_checked_box);
					_differentCheckBox.setSelected(false);
					_differentCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_sameCheckBox.setSelected(false);
					_sameCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_similarCheckBox.setSelected(false);
					_similarCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_noSubstituteCheckBox.setSelected(false);
					_noSubstituteCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = shopperDiscretionYOrigin;
			layoutParams.width = checkBoxSize;
			layoutParams.height = checkBoxSize;
			_ecartMainLayout.addView(_shopperCheckBox, layoutParams);

			SmartTextView shopperDiscretionText = new SmartTextView(this,
					checkBoxTextWidth, checkBoxTextHeight, 1,
					Color.TRANSPARENT, _textColor, _normalFont, SUB_PREF_1,
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = checkBoxTextXOrigin;
			layoutParams.topMargin = shopperDiscretionYOrigin
					+ checkBoxTextYOffset;
			_ecartMainLayout.addView(shopperDiscretionText, layoutParams);

			// different size line
			int differentSizeYOrigin = shopperDiscretionYOrigin + promptHeight
					+ checkBoxVerticalPad;

			_differentCheckBox = new ImageView(this);
			_differentCheckBox
					.setBackgroundResource(R.drawable.product_unchecked_box);
			_differentCheckBox.setSelected(false);
			_differentCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					_substitutionPreference = SUB_PREF_2;
					_substitutionPreferenceValue = VAL_SUB_PREF_2;
					_shopperCheckBox.setSelected(false);
					_shopperCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_differentCheckBox.setSelected(true);
					_differentCheckBox
							.setBackgroundResource(R.drawable.product_checked_box);
					_sameCheckBox.setSelected(false);
					_sameCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_similarCheckBox.setSelected(false);
					_similarCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_noSubstituteCheckBox.setSelected(false);
					_noSubstituteCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = differentSizeYOrigin;
			layoutParams.width = checkBoxSize;
			layoutParams.height = checkBoxSize;
			_ecartMainLayout.addView(_differentCheckBox, layoutParams);

			SmartTextView differentSizeText = new SmartTextView(this,
					checkBoxTextWidth, checkBoxTextHeight, 1,
					Color.TRANSPARENT, _textColor, _normalFont, SUB_PREF_2,
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = checkBoxTextXOrigin;
			layoutParams.topMargin = differentSizeYOrigin + checkBoxTextYOffset;
			_ecartMainLayout.addView(differentSizeText, layoutParams);

			// same size line
			int sameSizeYOrigin = differentSizeYOrigin + promptHeight
					+ checkBoxVerticalPad;

			_sameCheckBox = new ImageView(this);
			_sameCheckBox
					.setBackgroundResource(R.drawable.product_unchecked_box);
			_sameCheckBox.setSelected(false);
			_sameCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					_substitutionPreference = SUB_PREF_3;
					_substitutionPreferenceValue = VAL_SUB_PREF_3;
					_shopperCheckBox.setSelected(false);
					_shopperCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_differentCheckBox.setSelected(false);
					_differentCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_sameCheckBox.setSelected(true);
					_sameCheckBox
							.setBackgroundResource(R.drawable.product_checked_box);
					_similarCheckBox.setSelected(false);
					_similarCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_noSubstituteCheckBox.setSelected(false);
					_noSubstituteCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = sameSizeYOrigin;
			layoutParams.width = checkBoxSize;
			layoutParams.height = checkBoxSize;
			_ecartMainLayout.addView(_sameCheckBox, layoutParams);

			SmartTextView sameSizeText = new SmartTextView(this,
					checkBoxTextWidth, checkBoxTextHeight, 1,
					Color.TRANSPARENT, _textColor, _normalFont, SUB_PREF_3,
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = checkBoxTextXOrigin;
			layoutParams.topMargin = sameSizeYOrigin + checkBoxTextYOffset;
			_ecartMainLayout.addView(sameSizeText, layoutParams);

			// similar item line
			int similarItemYOrigin = sameSizeYOrigin + promptHeight
					+ checkBoxVerticalPad;

			_similarCheckBox = new ImageView(this);
			_similarCheckBox
					.setBackgroundResource(R.drawable.product_unchecked_box);
			_similarCheckBox.setSelected(false);
			_similarCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					_substitutionPreference = SUB_PREF_4;
					_substitutionPreferenceValue = VAL_SUB_PREF_4;
					_shopperCheckBox.setSelected(false);
					_shopperCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_differentCheckBox.setSelected(false);
					_differentCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_sameCheckBox.setSelected(false);
					_sameCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_similarCheckBox.setSelected(true);
					_similarCheckBox
							.setBackgroundResource(R.drawable.product_checked_box);
					_noSubstituteCheckBox.setSelected(false);
					_noSubstituteCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = similarItemYOrigin;
			layoutParams.width = checkBoxSize;
			layoutParams.height = checkBoxSize;
			_ecartMainLayout.addView(_similarCheckBox, layoutParams);

			SmartTextView similarItemText = new SmartTextView(this,
					checkBoxTextWidth, checkBoxTextHeight, 1,
					Color.TRANSPARENT, _textColor, _normalFont, SUB_PREF_4,
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = checkBoxTextXOrigin;
			layoutParams.topMargin = similarItemYOrigin + checkBoxTextYOffset;
			_ecartMainLayout.addView(similarItemText, layoutParams);

			// similar item line
			int noSubstitutionYOrigin = similarItemYOrigin + promptHeight
					+ checkBoxVerticalPad;

			_noSubstituteCheckBox = new ImageView(this);
			_noSubstituteCheckBox
					.setBackgroundResource(R.drawable.product_unchecked_box);
			_noSubstituteCheckBox.setSelected(false);
			_noSubstituteCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					_substitutionPreference = SUB_PREF_5;
					_substitutionPreferenceValue = VAL_SUB_PREF_5;
					_shopperCheckBox.setSelected(false);
					_shopperCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_differentCheckBox.setSelected(false);
					_differentCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_sameCheckBox.setSelected(false);
					_sameCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_similarCheckBox.setSelected(false);
					_similarCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_noSubstituteCheckBox.setSelected(true);
					_noSubstituteCheckBox
							.setBackgroundResource(R.drawable.product_checked_box);
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = noSubstitutionYOrigin;
			layoutParams.width = checkBoxSize;
			layoutParams.height = checkBoxSize;
			_ecartMainLayout.addView(_noSubstituteCheckBox, layoutParams);

			SmartTextView noSubstitutionText = new SmartTextView(this,
					checkBoxTextWidth, checkBoxTextHeight, 1,
					Color.TRANSPARENT, _textColor, _normalFont, SUB_PREF_5,
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = checkBoxTextXOrigin;
			layoutParams.topMargin = noSubstitutionYOrigin
					+ checkBoxTextYOffset;
			_ecartMainLayout.addView(noSubstitutionText, layoutParams);

			// ------------------------------------------------------------------------------------------
			// substitution preference section end
			// ------------------------------------------------------------------------------------------

			// ------------------------------------------------------------------------------------------
			// bag preference section begin
			// ------------------------------------------------------------------------------------------
			int bagPreferenceYOrigin = (int) (_contentViewHeight * .9);

			SmartTextView bagPreferencePromptText = new SmartTextView(this,
					textWidth, (int) (_contentViewHeight * .035), 1,
					Color.TRANSPARENT, _textColor, _boldFont,
					"Specify Bag Preference", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = bagPreferenceYOrigin - promptTextBottomPad;
			_ecartMainLayout.addView(bagPreferencePromptText, layoutParams);

			// Separator Layout start
			int bagspaceYOrigin = bagPreferenceYOrigin + promptHeight
					+ checkBoxVerticalPad;
			_seperatorLayout = new RelativeLayout(this);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = layoutPadding;
			layoutParams.rightMargin = layoutPadding;
			layoutParams.topMargin = bagspaceYOrigin - promptTextBottomPad;
			layoutParams.width = editWidth;
			layoutParams.height = (int) (textFieldHeight * .015);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_ecartMainLayout.getId());
			_seperatorLayout.setBackgroundColor(Color.DKGRAY);
			_ecartMainLayout.addView(_seperatorLayout, layoutParams);
			// Separator Layout end

			// shopper discretion line
			int paperBagYOrigin = bagspaceYOrigin + checkBoxVerticalPad;

			_paperBagCheckBox = new ImageView(this);
			_paperBagCheckBox
					.setBackgroundResource(R.drawable.product_checked_box);
			_paperBagCheckBox.setSelected(true);
			_paperBagCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					_bagPreference = BAG_PREF_1;
					_bagPreferenceValue = VAL_BAG_PREF_1;

					_paperBagCheckBox.setSelected(true);
					_paperBagCheckBox
							.setBackgroundResource(R.drawable.product_checked_box);
					_reusableBagCheckBox.setSelected(false);
					_reusableBagCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = paperBagYOrigin;
			layoutParams.width = checkBoxSize;
			layoutParams.height = checkBoxSize;
			_ecartMainLayout.addView(_paperBagCheckBox, layoutParams);

			SmartTextView paperBagText = new SmartTextView(this,
					checkBoxTextWidth, checkBoxTextHeight, 1,
					Color.TRANSPARENT, _textColor, _normalFont, BAG_PREF_1,
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = checkBoxTextXOrigin;
			layoutParams.topMargin = paperBagYOrigin + checkBoxTextYOffset;
			_ecartMainLayout.addView(paperBagText, layoutParams);

			// shopper discretion line
			int reusableBagYOrigin = paperBagYOrigin + checkBoxSize
					+ checkBoxVerticalPad;

			_reusableBagCheckBox = new ImageView(this);
			_reusableBagCheckBox
					.setBackgroundResource(R.drawable.product_unchecked_box);
			_reusableBagCheckBox.setSelected(false);
			_reusableBagCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					_bagPreference = BAG_PREF_2;
					_bagPreferenceValue = VAL_BAG_PREF_2;

					_paperBagCheckBox.setSelected(false);
					_paperBagCheckBox
							.setBackgroundResource(R.drawable.product_unchecked_box);
					_reusableBagCheckBox.setSelected(true);
					_reusableBagCheckBox
							.setBackgroundResource(R.drawable.product_checked_box);
					_inputManager.hideSoftInputFromWindow(
							_contentLayout.getWindowToken(), 0);
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = editXOrigin;
			layoutParams.topMargin = reusableBagYOrigin;
			layoutParams.width = checkBoxSize;
			layoutParams.height = checkBoxSize;
			_ecartMainLayout.addView(_reusableBagCheckBox, layoutParams);

			SmartTextView reusableBagText = new SmartTextView(this,
					checkBoxTextWidth, checkBoxTextHeight, 1,
					Color.TRANSPARENT, _textColor, _normalFont, BAG_PREF_2,
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = checkBoxTextXOrigin;
			layoutParams.topMargin = reusableBagYOrigin + checkBoxTextYOffset;
			_ecartMainLayout.addView(reusableBagText, layoutParams);

			int bottomtextYPad = reusableBagYOrigin + checkBoxSize
					+ checkBoxVerticalPad;
			SmartTextView bagDisclosurePromptText = new SmartTextView(this,
					textWidth, (int) (_contentViewHeight * .05), 2,
					Color.TRANSPARENT, _textColor, _normalFont,
					"*Based on local ordinances, you may be charged per bag.",
					Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = layoutPadding;
			layoutParams.topMargin = bottomtextYPad;
			layoutParams.bottomMargin = promptHeight;
			_ecartMainLayout.addView(bagDisclosurePromptText, layoutParams);

			// ------------------------------------------------------------------------------------------
			// bag preference section end
			// ------------------------------------------------------------------------------------------

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setContentView(_mainLayout);
	}

	@Override
	public void onResume() {
		super.onResume();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void toggleAppointmentDayTable() {

		try {
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

			ListView _deptList = (ListView) dialog.findViewById(R.id.listview1);

			ArrayList<String> deptList = new ArrayList<String>();

			// ArrayList<Array> customArray = new ArrayList<Array>();
			// By default first value selected
			// _appointmentDayText.setHint(_appointmentDays[0]);

			deptList.addAll(Arrays.asList(_appointmentDays));

			// Create ArrayAdapter using the planet list.
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
					R.layout.account_listrow, deptList);

			// Set the ArrayAdapter as the ListView's adapter.
			_deptList.setAdapter(listAdapter);
			_deptList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								final View view, int position, long id) {

							// new code
							final String item = (String) parent
									.getItemAtPosition(position);

							try {

								if (item.compareTo(_appointmentDay) != 0) {

									// Creating time list array
									_appointmentDay = (String) parent
											.getItemAtPosition(position);
									_appointmentDayText
											.setTextColor(_textColor);
									_appointmentDayText
											.writeText(_appointmentDay);

									// reset the appointment time components
									// when
									// the day is selected
									_appointmentTime = "";
									_appointmentTimeText.writeText("Time");
									_appointmentTimeAdapter._dropdownList = null;
									EcartAppointment appointment = _app._currentEcartPreOrderResponse.appointmentList
											.get(position);
									_appointmentTimeAdapter._dropdownList = new String[appointment.appointmentTimeList
											.size()];

									for (int i = 0; i < appointment.appointmentTimeList
											.size(); i++)
										_appointmentTimeAdapter._dropdownList[i] = appointment.appointmentTimeList
												.get(i);
								}

								// By default first value selected
								_appointmentTimeText
										.writeText(_appointmentTimes[0]);
								_appointmentTimeAdapter.notifyDataSetChanged();

							} catch (Exception e) {
								Log.e("Error", "Time Adapter");
							}
							dialog.dismiss();
						}
					});

			if (_appointmentDays.length > 0) {
				dialog.show();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void toggleAppointmentTimeTable() {

		try {
			String _text = _appointmentDayText.currentText().toString();
			if (_text == "Date") {
				showTextDialog(this, "Input Error",
						"Please select a pickup date.");
				return;
			}
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

			ListView _deptList = (ListView) dialog.findViewById(R.id.listview1);

			ArrayList<String> deptList = new ArrayList<String>();

			deptList.addAll(Arrays
					.asList(_appointmentTimeAdapter._dropdownList));

			// Create ArrayAdapter using the planet list.
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
					R.layout.account_listrow, deptList);

			// Set the ArrayAdapter as the ListView's adapter.
			_deptList.setAdapter(listAdapter);
			_deptList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								final View view, int position, long id) {
							final String item = (String) parent
									.getItemAtPosition(position);
							// _appointmentTimeText.setHint("");
							_appointmentTimeText.writeText(item);
							_appointmentTime = item;
							dialog.dismiss();
						}
					});

			if (_appointmentTimeAdapter._dropdownList.length > 0) {
				dialog.show();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * public void toggleAppointmentDayTable() { try {
	 * _ecartMainLayout.bringChildToFront(_appointmentDayList);
	 * 
	 * if (_appointmentDayList.getVisibility() == View.VISIBLE) {
	 * _appointmentDayList.setVisibility(View.GONE); } else { if
	 * (_appointmentTimeList.getVisibility() == View.VISIBLE)
	 * _appointmentTimeList.setVisibility(View.GONE);
	 * 
	 * _appointmentDayList.setVisibility(View.VISIBLE);
	 * _inputManager.hideSoftInputFromWindow( _contentLayout.getWindowToken(),
	 * 0); } } catch (Exception ex) { ex.printStackTrace(); } }
	 * 
	 * public void toggleAppointmentTimeTable() { try {
	 * _ecartMainLayout.bringChildToFront(_appointmentTimeList);
	 * 
	 * if (_appointmentTimeAdapter._dropdownList.length == 0) // don't show //
	 * an // invisible // list... return;
	 * 
	 * if (_appointmentTimeList.getVisibility() == View.VISIBLE) {
	 * _appointmentTimeList.setVisibility(View.GONE); } else { if
	 * (_appointmentDayList.getVisibility() == View.VISIBLE)
	 * _appointmentDayList.setVisibility(View.GONE);
	 * 
	 * _appointmentTimeList.setVisibility(View.VISIBLE);
	 * _inputManager.hideSoftInputFromWindow( _contentLayout.getWindowToken(),
	 * 0);
	 * 
	 * } } catch (Exception ex) { ex.printStackTrace(); } }
	 */

	public void confirmOrder() {
		try {
			if (_appointmentDay.compareTo("") == 0) {
				showTextDialog(this, "Input Error",
						"Please select a pickup date.");
				return;
			} else if (_appointmentTime.compareTo("") == 0) {
				showTextDialog(this, "Input Error",
						"Please select a pickup time.");
				return;
			}

			Login login = _app.getLogin();

			_ecartOrderRequest = new EcartOrderRequest();
			_ecartOrderRequest.accountId = login.accountId;
			_ecartOrderRequest.storeNumber = login.storeNumber;
			_ecartOrderRequest.listId = _app._currentShoppingList.listId;
			_ecartOrderRequest.substitutionPreferenceName = _substitutionPreference;
			_ecartOrderRequest.bagPreferenceName = _bagPreference;
			_ecartOrderRequest.substitutionPreference = _substitutionPreferenceValue;
			_ecartOrderRequest.bagPreference = _bagPreferenceValue;
			_ecartOrderRequest.appointmentDay = _appointmentDay;
			_ecartOrderRequest.appointmentTime = _appointmentTime;
			_ecartOrderRequest.instructions = _instructionEditText.getText()
					.toString();

			// _submitButton.setVisibility(View.GONE);
			_ecartConfirmView = new EcartConfirmView(this,
					_app._currentEcartPreOrderResponse, _ecartOrderRequest,
					"submitOrder");

			int sdk = android.os.Build.VERSION.SDK_INT;
			float[] CornerRadius;
			int cornerSize = (10);
			CornerRadius = new float[] { cornerSize, cornerSize, cornerSize,
					cornerSize, cornerSize, cornerSize, cornerSize, cornerSize };
			// Dynamic layout creation Part
			RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
					new RectF(0, 0, 0, 0),
					new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable top_layer = new CustomShapeDrawable(bottom_rect,
					Color.TRANSPARENT, Color.WHITE, 0);
			LayerDrawable _layoutBackground = new LayerDrawable(
					new Drawable[] { top_layer });
			_layoutBackground.setLayerInset(0, 0, 0, 0, 0);

			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				_ecartConfirmView.setBackgroundDrawable(_layoutBackground);
			} else {
				_ecartConfirmView.setBackground(_layoutBackground);
			}

			_ecartConfirmView.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void submitOrder() {
		try {
			// ecart
			_ecartConfirmView.dismiss();
			Log.i(getClass().getSimpleName(), "submitting ecart order");
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			String requestBody = gson.toJson(_ecartOrderRequest);
			Log.i(getClass().getSimpleName(), "EcartOrderRequest: "
					+ requestBody);

			if (!Utils.isNetworkAvailable(this)) {
				showNetworkUnavailableDialog(this);
				return;
			}

			showProgressDialog("Submitting your Ecart Order...");
			_service = new WebService(this, EcartOrderRequest.class,
					requestBody, _login.authKey);
			_service.execute(_app.ECART_ORDER_URL);
			// ecart
			// handled by handleEcartOrderServiceResponse() below

			// // START
			try {
				// CLP SDK ecart button pressed
				JSONObject data = new JSONObject();
				data.put("Ecart_AccountID", _ecartOrderRequest.accountId);
				data.put("Ecart_ListID", _ecartOrderRequest.listId);
				data.put("Ecart_AppointmentDay",
						_ecartOrderRequest.appointmentDay);
				data.put("Ecart_appointmentTime",
						_ecartOrderRequest.appointmentTime);
				data.put("store", _ecartOrderRequest.storeNumber);
				data.put("bag_preference", _ecartOrderRequest.bagPreference);
				data.put("instructions", _ecartOrderRequest.instructions);
				data.put("price", String
						.valueOf(_app._currentEcartPreOrderResponse.totalPrice));

				data.put("event_name", "ListSubmitted");
				data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
				_app.clpsdkObj.updateAppEvent(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// // END

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void handleEcartOrderServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				EcartOrderRequest response = (EcartOrderRequest) _service
						.getResponseObject();

				if (response == null) {

					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					return;
				}

				showTextDialog(
						this,
						"Request Succeeded",
						"Ecart Order submitted successfully. Your order id is "
								+ response.orderId
								+ ". Please check your email for confirmation.",
						"orderDone");
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Ecart Submit Failed",
							error.errorMessage);
				else
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error", "Http Status code: "
				// + status);

				_submitButton.setVisibility(View.VISIBLE);
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void orderDone() {
		finish();
	}

	@Override
	public void onServiceResponse(Object responseObject) {
		if (responseObject instanceof EcartOrderRequest)
			handleEcartOrderServiceResponse();
	}

	@Override
	public void onBackPressed() {
		finish();
		// do nothing, make the user hit the home button to background the app
	}

}
