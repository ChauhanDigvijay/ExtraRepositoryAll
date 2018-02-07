package com.raleys.app.android;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.LoginRequest;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceError;
import com.raleys.libandroid.WebServiceListener;

public class LoginScreen extends BaseActivity implements WebServiceListener {

	private WebService _service;
	private int _width;
	private int _height;
	private RaleysApplication _app;
	RelativeLayout _mainLayout, _mainLayoutBottom, Register;
	private SizedImageView _animateLeft;
	// private SizedImageView _animateTop;
	private SizedImageView _raleysLogo, _raleysTrilLogo;

	RelativeLayout.LayoutParams emaillayoutParams, LogoParams,
			registerLayoutLeft, registerLayoutRight, registerLayout,
			layoutParams;
	String comm_string = "screen";
	Context con;
	boolean launched = false;
	int i = 0;
	TranslateAnimation anim;
	StringBuilder app_str;
	int logo_width, logo_height, bott_logo_width, bott_logo_height;
	ImageView logo_image;
	FrameLayout fl;

	int splash_img_array[] = { R.drawable.screen1, R.drawable.screen2,
			R.drawable.screen3, R.drawable.screen4, R.drawable.screen5 };

	String msg_array[] = { "Everyday Special", "Fine Desserts",
			"Inspired Meals", "Local & Organic" };
	TextView show_text, _donthaveAccoun, _registerNow;
	EditText _emailEditText, _passwordEditText;
	Button _loginButton;
	private InputMethodManager _inputManager;
	Typeface type_olivier;
	public boolean newscheduled = false;
	FrameLayout.LayoutParams raleysLogoParams;
	Handler newHandler, _newHandler;

	int textFieldWidth, textFieldHeight, textFieldSize, textFieldXOrigin,
			textXPad, textYPad;
	ScrollView _scrollView;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			con = this;
			// Get Screen Height and Width
			_app = (RaleysApplication) getApplication();
			_width = _app.getScreenWidth();
			_height = _app.getScreenHeight() - Utils.getStatusBarHeight(this);
			_inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
					WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
			// Main Layout

			fl = new FrameLayout(con);
			// Main Layout Params
			raleysLogoParams = new FrameLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);

			_animateLeft = new SizedImageView(con, _width
					+ (int) (_width * .10), _height + (int) (_height * .10));// increase
																				// the
																				// height
																				// and
																				// width
																				// of
																				// images
																				// for
																				// animation
			// First Image as background
			_animateLeft.setImageBitmap(_app.getAppBitmap("screen1",
					R.drawable.screen1, _width + (int) (_width * .10), _height
							+ (int) (_height * .10)));
			FrameLayout.LayoutParams flayoutParams = new FrameLayout.LayoutParams(
					_width, _height);
			fl.addView(_animateLeft, flayoutParams);

			textFieldWidth = (int) (_width * .8);
			textFieldHeight = (int) (_height * .1);
			textFieldSize = (int) (textFieldHeight * .4);
			textFieldXOrigin = (_width - textFieldWidth) / 2;
			textXPad = (int) (textFieldWidth * .02);
			textYPad = (int) (textFieldHeight * .07);

			showProgressDialog("");
			_newHandler = new Handler();
			_newHandler.postDelayed(new Runnable() {

				@Override
				public void run() {

					logo_width = (int) (_width * .65);
					logo_height = (int) (logo_width * .41);

					_raleysLogo = new SizedImageView(con, logo_width,
							logo_height);
					_raleysLogo.setLayoutParams(new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

					_raleysLogo.setImageBitmap(_app.getAppBitmap("raleys_logo",
							R.drawable.raleys_logo, logo_width, logo_height));

					_scrollView = new ScrollView(con);
					_scrollView.setLayoutParams(new LayoutParams(_width,
							_height));

					// Raley's Logo Display
					_mainLayout = new RelativeLayout(con);
					RelativeLayout.LayoutParams main_layout_params = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT, _height);

					RelativeLayout.LayoutParams lLogoParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					lLogoParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_raleysLogo.getId());
					lLogoParams.topMargin = (int) (_height * 0.05);
					_mainLayout.addView(_raleysLogo, lLogoParams);

					type_olivier = Typeface.createFromAsset(getAssets(),
							"fonts/olivier.ttf");

					// Text View Contents
					show_text = new TextView(con);
					RelativeLayout.LayoutParams msg_params = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					msg_params.addRule(RelativeLayout.CENTER_HORIZONTAL,
							show_text.getId());
					msg_params.addRule(RelativeLayout.BELOW, show_text.getId());
					show_text.setTextColor(Color.WHITE);
					show_text.setAlpha((float) 1.0);
					show_text.setTypeface(type_olivier);
					show_text.setText("Everyday Special");
					show_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							(int) ((_height * .1) * .5));
					show_text.setGravity(Gravity.CENTER_HORIZONTAL);
					show_text.setY((float) (_height * 0.25));
					_mainLayout.addView(show_text, msg_params);

					// Show text End;

					bott_logo_width = (int) (_width * .55);
					bott_logo_height = (int) (bott_logo_width * .13);

					_raleysTrilLogo = new SizedImageView(con, bott_logo_width,
							bott_logo_height);
					_raleysTrilLogo.setLayoutParams(new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
					_raleysTrilLogo.setImageBitmap(_app.getAppBitmap(
							"raleys_trilogo_white",
							R.drawable.raleys_trilogo_white, bott_logo_width,
							bott_logo_height));
					RelativeLayout.LayoutParams BottLogoParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					BottLogoParams.topMargin = (int) (_height * 0.93);
					BottLogoParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_raleysTrilLogo.getId());
					_mainLayout.addView(_raleysTrilLogo, BottLogoParams);

					int sdk = android.os.Build.VERSION.SDK_INT;

					// // email text field
					float[] CornerRadius;
					int cornerSize = (int) (_width * .0125);
					CornerRadius = new float[] { cornerSize, cornerSize,
							cornerSize, cornerSize, cornerSize, cornerSize,
							cornerSize, cornerSize };

					RoundRectShape innerRect = new RoundRectShape(CornerRadius,
							new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0,
									0, 0, 0 });
					RoundRectShape textOuter = new RoundRectShape(CornerRadius,
							new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0,
									0, 0, 0 });

					ShapeDrawable top_layer = new CustomShapeDrawable(
							innerRect, Color.TRANSPARENT, Color.rgb(216, 215,
									215), 0);// 238,238,238
					ShapeDrawable bottomlayer = new CustomShapeDrawable(
							textOuter, Color.TRANSPARENT, Color.rgb(238, 238,
									238), 0);

					LayerDrawable _layoutBackground = new LayerDrawable(
							new Drawable[] { bottomlayer, top_layer });
					_layoutBackground.setLayerInset(0, 0, 0, 0, 0);
					_layoutBackground.setLayerInset(1, (int) 3.0f, (int) 3.0f,
							(int) 3.0f, (int) 3.0f);

					_emailEditText = new EditText(con);
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_emailEditText.setBackgroundDrawable(_layoutBackground);
					} else {
						_emailEditText.setBackground(_layoutBackground);
					}
					_emailEditText.setPadding(textXPad, textYPad, 0, 0);
					_emailEditText.setTextColor(Color.BLACK);
					_emailEditText.setTypeface(_normalFont);
					_emailEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textFieldSize);
					_emailEditText.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
					_emailEditText.setHint("Email");
					layoutParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = (int) (_height * .5);
					layoutParams.leftMargin = textFieldXOrigin;
					layoutParams.width = textFieldWidth;
					layoutParams.height = textFieldHeight;
					_emailEditText.setAlpha((float) 0.65);
					_mainLayout.addView(_emailEditText, layoutParams);

					// Email Text field End

					// password text field
					_passwordEditText = new EditText(con);
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_passwordEditText
								.setBackgroundDrawable(_layoutBackground);
					} else {
						_passwordEditText.setBackground(_layoutBackground);
					}
					_passwordEditText.setPadding(textXPad, textYPad, 0, 0);
					_passwordEditText.setTextColor(Color.BLACK);
					_passwordEditText.setTypeface(_normalFont);
					_passwordEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textFieldSize);
					_passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					_passwordEditText.setHint("Password");

					if (_app.getDeviceType() == Utils.DEVICE_PHONE) {
						_passwordEditText
								.setOnEditorActionListener(new OnEditorActionListener() {
									@Override
									public boolean onEditorAction(TextView v,
											int actionId, KeyEvent event) {
										if (actionId == EditorInfo.IME_ACTION_DONE) {
											_inputManager.hideSoftInputFromWindow(
													_mainLayout
															.getWindowToken(),
													0);
											return true;
										}
										return false;
									}
								});
					}

					layoutParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = (int) (_height * 0.61);
					_passwordEditText.setAlpha((float) 0.65);
					layoutParams.leftMargin = textFieldXOrigin;
					layoutParams.width = textFieldWidth;
					layoutParams.height = textFieldHeight;
					_mainLayout.addView(_passwordEditText, layoutParams);

					// Password text field End

					// login button
					RoundRectShape buttonRect = new RoundRectShape(
							CornerRadius, new RectF(0, 0, 0, 0), new float[] {
									0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable layerTop = new CustomShapeDrawable(
							buttonRect, Color.TRANSPARENT,
							Color.rgb(187, 0, 0), 0);// 238,238,238

					RoundRectShape buttonRectOuter = new RoundRectShape(
							CornerRadius, new RectF(0, 0, 0, 0), new float[] {
									0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable bottom_layer = new CustomShapeDrawable(
							buttonRectOuter, Color.TRANSPARENT, Color.rgb(238,
									238, 238), 0);

					LayerDrawable _buttonBackground = new LayerDrawable(
							new Drawable[] { bottom_layer, layerTop });
					_buttonBackground.setLayerInset(0, 0, 0, 0, 0);
					_buttonBackground.setLayerInset(1, (int) 3.0f, (int) 3.0f,
							(int) 3.0f, (int) 3.0f);

					Button _loginButton = new Button(con);
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_loginButton.setBackgroundDrawable(_buttonBackground);
					} else {
						_loginButton.setBackground(_buttonBackground);
					}
					_loginButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							loginButtonPressed();
						}
					});
					_loginButton.setText("Login");
					_loginButton.setTextColor(Color.WHITE);
					_loginButton.setTypeface(_normalFont);
					_loginButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							textFieldSize);
					layoutParams = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = textFieldXOrigin;
					layoutParams.topMargin = (int) (_height * 0.73);
					_loginButton.setAlpha((float) 0.65);
					layoutParams.width = textFieldWidth;
					layoutParams.height = textFieldHeight;
					_mainLayout.addView(_loginButton, layoutParams);

					// Login Button End
					// Register Now Start

					_donthaveAccoun = new TextView(con);
					_registerNow = new TextView(con);

					_donthaveAccoun.setTextColor(Color.WHITE);
					_donthaveAccoun.setTypeface(_normalFont);
					_registerNow.setTextColor(Color.WHITE);
					_registerNow.setTypeface(_boldFont);
					_donthaveAccoun.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							(float) (textFieldSize * 0.6));
					_registerNow.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							(float) (textFieldSize * 0.6));
					_donthaveAccoun.setText("Don't have an account?");
					_registerNow.setText("Register Now");

					_donthaveAccoun.setPadding(0, 5, 0, 5);
					_registerNow.setPadding(0, 5, 0, 5);

					_registerNow.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							registrationButtonPressed();
						}
					});

					Register = new RelativeLayout(con);
					registerLayout = new RelativeLayout.LayoutParams(
							textFieldWidth,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

					registerLayoutLeft = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					registerLayoutLeft.addRule(
							RelativeLayout.ALIGN_PARENT_LEFT,
							_donthaveAccoun.getId());
					registerLayoutRight = new RelativeLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					registerLayoutRight.addRule(
							RelativeLayout.ALIGN_PARENT_RIGHT,
							_registerNow.getId());

					Register.addView(_donthaveAccoun, registerLayoutLeft);
					Register.addView(_registerNow, registerLayoutRight);
					Register.setY((float) (_height * 0.85));
					registerLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
					_mainLayout.addView(Register, registerLayout);
					// Register Now End

					_raleysLogo.setImageBitmap(_app.getAppBitmap("raleys_logo",
							R.drawable.raleys_logo, logo_width, logo_height));// Set
																				// raley's
																				// logo
					_scrollView.addView(_mainLayout, main_layout_params);
					fl.addView(_scrollView);
					fl.bringChildToFront(_scrollView);

					dismissActiveDialog();
					_newHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							startAnimating();
						}
					}, Raleys.SLIDE_TRANSITION_DELAY);
				}
			}, Raleys.SLIDE_TRANSITION_DELAY);
			setContentView(fl);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void startAnimating() {

		try {

			app_str = new StringBuilder();
			int resID = splash_img_array[i];
			if (i != 4) {

				// Append the string for image name
				app_str = app_str.append(comm_string);
				app_str = app_str.append(i + 1);// like screen1

				_animateLeft.setImageBitmap(_app.getAppBitmap(
						app_str.toString(), resID, _width
								+ (int) (_width * .10), _height
								+ (int) (_height * .10)));

				_animateLeft.setX((float) 0.0);
				_animateLeft.setY((float) 0.0);

				overridePendingTransition(com.raleys.libandroid.R.anim.fadein,
						com.raleys.libandroid.R.anim.fadeout);

				show_text.setTypeface(type_olivier);
				show_text.setText(msg_array[i]);
				show_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						(int) ((_height * .1) * .5)); // 25
				anim = new TranslateAnimation(0, -(int) (_width * 0.1), 0, 0);
				anim.setDuration(2000);
				_animateLeft.startAnimation(anim);
				i++;
				app_str.delete(0, app_str.length());
				_newHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						startAnimating();
					}
				}, 2000);

			} else if (i == 4) {

				app_str = app_str.append(comm_string);
				app_str = app_str.append(i + 1);

				_animateLeft.setImageBitmap(_app.getAppBitmap(
						app_str.toString(), resID, _width
								+ (int) (_width * .10), _height
								+ (int) (_height * .10)));

				_animateLeft.setX((float) 0.0);
				_animateLeft.setY((float) 0.0);

				show_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						(int) ((_height * .1) * .3)); // 17
				show_text.setTypeface(_normalFont);
				show_text.setText("For Food. For Family. For you.");
				anim = new TranslateAnimation(0, 0, 0, -(int) (_height * 0.10));
				anim.setDuration(2000);
				_animateLeft.startAnimation(anim);
				launched = true;
				app_str.delete(0, app_str.length());
				_animateLeft.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Then just use the following:
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								_animateLeft.getWindowToken(), 0);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void registrationButtonPressed() {

		if (_app._registerActivityopen == true) {
			return;
		} else if (_app._registerActivityopen == false) {
			_app._registerActivityopen = true;
			Intent intent = new Intent(LoginScreen.this,
					AccountScreen_new.class);
			intent.putExtra("registrationPage", true);
			startActivity(intent);
		}

		// CLP SDK
		try {
//			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
//			data.add(new BasicNameValuePair("time", _app.getCurrentTime()));
//			data.add(new BasicNameValuePair("Event_Name", "Register_Now"));
//			// CLP SDK Register click
		} catch (Exception e) {
			Log.e(_app.CLP_TRACK_ERROR, "EVENT_CLICK:" + e.getMessage());
		}
		// CLP

		// this.finish();
	}

	public void loginButtonPressed() {
		String email = _emailEditText.getText().toString();
		String password = _passwordEditText.getText().toString();

		if (email == null || email.equalsIgnoreCase("")) {
			showTextDialog(this, "Input Error", "Email field can't be blank");
			return;
		}

		if (Utils.validateEmail(email) == false) {
			showTextDialog(this, "Input Error", "Incorrect email format");
			return;
		}

		if (password == null || password.equalsIgnoreCase("")) {
			showTextDialog(this, "Input Error", "Password field can't be blank");
			return;
		}

		String platform = "Android";
		String platformVersion = android.os.Build.VERSION.RELEASE;

		LoginRequest request = new LoginRequest();
		request.email = email;
		request.password = password;
		request.platform = platform;
		request.platformVersion = platformVersion;

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String requestBody = gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}
		// CLP SDK Login button click action
		// To do

		showProgressDialog("Authenticating User...");
		_service = new WebService(this, Login.class, requestBody, null);
		_service.execute(_app.LOGIN_URL);
		// handled by handleLoginServiceResponse() directly below
	}

	public void handleLoginServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status != 200) {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Login Failed", error.errorMessage);
				else
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error", "Http Status code: "
				// + status);

				return;
			}
			try {
				JSONObject data = new JSONObject();
				// CLP SDK SignedIn
				data.put("event_name", "SignedIn");
				data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
				_app.clpsdkObj.updateAppEvent(data);
			} catch (Exception e) {
				Log.e(_app.CLP_TRACK_ERROR, "EVENT_CLICK:" + e.getMessage());
			}

			Login login = (Login) _service.getResponseObject();

			if (login == null || login.accountId == null
					|| login.crmNumber == null) {
				showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error",
				// "Unable to parse data returned from server.");
				return;
			}

			_app._newLogin = true;
			_app.setLogin(login);
			Intent rIntent = getIntent();
			Intent intent = new Intent(LoginScreen.this, ShoppingScreen.class);
			if (rIntent.hasExtra("clpnid")) {
				intent.putExtras(rIntent.getExtras());
			}

			startActivity(intent);
			finish();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onServiceResponse(Object responseObject) {
		if (responseObject instanceof Login)
			handleLoginServiceResponse();
	}

	@Override
	public void onBackPressed() {
		// finish();
		// do nothing, make the user hit the home button to background the app
	}

}
