package com.raleys.app.android;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TermsAndConditions extends BaseScreen {

	private RaleysApplication _app;
	private InputMethodManager _inputManager;
	private int _textColor;
	private int _hintColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// RelativeLayout
		//getActionBar().hide();
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// hides
																			// the
																			// keyboard
																			// on
																			// startup

		_app = (RaleysApplication) getApplication();
		_inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		_CreateAccountButton.setVisibility(View.GONE); // Hiding Create button

		// override base class listener so we can remove the keyboard if it's
		// visible when the button is pressed
		_backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_inputManager.hideSoftInputFromWindow(
						_contentLayout.getWindowToken(), 0);
				finish();
			}
		});

		// setContentView(R.layout.activity_terms_and_conditions);

		RelativeLayout.LayoutParams layoutParams;
		_textColor = Color.argb(255, 90, 60, 20);
		_hintColor = Color.argb(255, 161, 136, 98);

		// header content
		LinearLayout headerContainer = new LinearLayout(this);
		LinearLayout.LayoutParams headerLayout = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		headerLayout.topMargin = 0;
		headerLayout.height = _headerHeight;
		headerLayout.weight = _contentViewWidth;
		headerContainer.setLayoutParams(headerLayout);

		_mainLayout.addView(headerContainer);

		RelativeLayout _TermsBgLayout = new RelativeLayout(this);
		_TermsBgLayout.setBackgroundColor(Color.WHITE);
		layoutParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = _headerHeight;
		layoutParams.leftMargin = 0;
		_TermsBgLayout.setId(2);
		layoutParams.height = _contentViewHeight;
		layoutParams.width = _contentViewWidth;
		_mainLayout.addView(_TermsBgLayout, layoutParams);

		// Header text
		TextView HeadertermsText = new TextView(this);
		HeadertermsText.setId((int) System.currentTimeMillis());
		RelativeLayout.LayoutParams header_layparams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// layparams.addRule(RelativeLayout.ABOVE, 2);
		header_layparams.leftMargin = 50;
		header_layparams.rightMargin = 50;
		header_layparams.topMargin = (int) (_headerHeight * 1.25);
		HeadertermsText.setTextColor(Color.RED);
		HeadertermsText.setTextSize(20);
		HeadertermsText.setGravity(Gravity.CENTER);
		// HeadertermsText.setTextAlignment()

		HeadertermsText.setText("Terms of Services");
		_mainLayout.addView(HeadertermsText, header_layparams);

		// Paragarph text
		WebView termsText = new WebView(this);
		termsText.setId((int) System.currentTimeMillis());
		RelativeLayout.LayoutParams layparams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layparams.leftMargin = 30;
		layparams.rightMargin = 30;
		layparams.topMargin = _headerHeight * 2;
		termsText
				.loadData(getString(R.string.html_terms), "text/html", "utf-8");

		termsText.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		termsText.setLongClickable(false);

		_mainLayout.addView(termsText, layparams);

		setContentView(_mainLayout);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		finish();
		// do nothing, make the user hit the home button to background the app
	}

}
