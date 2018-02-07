package com.raleys.app.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;

import com.raleys.libandroid.ProgressDialog;
import com.raleys.libandroid.TextDialog;
import com.raleys.libandroid.TickDialog;

/**
 * Base class for activities that use modal dialogs. Primarily used to handles
 * dialog allocation semantics.
 */
public class BaseActivity extends Activity {
	private RaleysApplication _app;
	private TextDialog _textDialog;
	private ProgressDialog _progressDialog;
	protected Typeface _normalFont;
	protected Typeface _boldFont;
	protected Typeface _fixedFont;
	private boolean _progressDialogActive = false;
	private TickDialog _tickDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_app = (RaleysApplication) getApplication();
		_normalFont = _app.getNormalFont();
		_boldFont = _app.getBoldFont();
		_fixedFont = _app.getFixedFont();

		// heavily used bitmaps should only be scaled once and stored in the
		// app...
		if (_app._dialogBackground == null)
			_app._dialogBackground = _app.getAppBitmap("app_dialog_background",
					R.drawable.dialog_background,
					(int) (_app.getScreenWidth() * .8),
					(int) (_app.getScreenWidth() * .48));

		if (_app._progressBackground == null)
			_app._progressBackground = _app.getAppBitmap(
					"app_progress_background", R.drawable.progress_background,
					(int) (_app.getScreenWidth() * .8),
					(int) (_app.getScreenWidth() * .48));

	}

	@Override
	protected void onPause() {
		super.onPause();
		dismissActiveDialog();
		// this.getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// CLP SDK
		try {
			// CLP SDK App Close
		} catch (Exception e) {
			Log.e(_app.CLP_TRACK_ERROR, "APP_CLOSE:" + e.getMessage());
		}
		// CLP
	}

	// show text dialog with one button that dismisses itself, use default fonts
	public void showTextDialog(Context context, String headerText,
			String message) {
		_textDialog = new TextDialog(context, _app._dialogBackground,
				_boldFont, _normalFont, headerText, message);
		_textDialog.show();
	}

	// show text dialog with one button that dismisses itself, use custom fonts
	public void showTextDialog(Context context, Typeface titleFont,
			Typeface messageFont, String headerText, String message) {
		_textDialog = new TextDialog(context, _app._dialogBackground,
				titleFont, messageFont, headerText, message);
		_textDialog.show();
	}

	// show text dialog with one button that uses a callback method, use default
	// fonts
	public void showTextDialog(Context context, String headerText,
			String message, String callback) {
		_textDialog = new TextDialog(context, _app._dialogBackground,
				_boldFont, _normalFont, headerText, message, callback);
		_textDialog.show();
	}

	// show text dialog with one button that uses a callback method, use custom
	// fonts
	public void showTextDialog(Context context, Typeface titleFont,
			Typeface messageFont, String headerText, String message,
			String callback) {
		_textDialog = new TextDialog(context, _app._dialogBackground,
				titleFont, messageFont, headerText, message, callback);
		_textDialog.show();
	}

	// show text dialog with two buttons that each have their own callback
	// method, use default fonts
	public void showTextDialog(Context context, String headerText,
			String message, String leftCallback, String rightCallback) {
		_textDialog = new TextDialog(context, _app._dialogBackground,
				_boldFont, _normalFont, headerText, message, leftCallback,
				rightCallback);
		_textDialog.show();
	}

	// show text dialog with two buttons that each have their own callback
	// method, use custom fonts
	public void showTextDialog(Context context, Typeface titleFont,
			Typeface messageFont, String headerText, String message,
			String leftCallback, String rightCallback) {
		_textDialog = new TextDialog(context, _app._dialogBackground,
				titleFont, messageFont, headerText, message, leftCallback,
				rightCallback);
		_textDialog.show();
	}

	public void dismissTextDialog() {
		if (_textDialog != null) {
			_textDialog.dismiss();
		}
	}

	public void showProgressDialog(String message) {
		if (_progressDialog == null)
			_progressDialog = new ProgressDialog(BaseActivity.this,
					_app._progressBackground, _normalFont, message);
		else
			_progressDialog.setMessage(message);

		_progressDialogActive = true;
		_progressDialog.show();
	}

	public void changeProgressText(String text) {
		_progressDialog.setMessage(text);
	}

	public void dismissActiveDialog() {
		if (_progressDialogActive == true) {
			_progressDialog.dismiss();
			_progressDialogActive = false;
		}
	}

	// show text dialog with one button that dismisses itself
	public void showNetworkUnavailableDialog(Context context) {
		_textDialog = new TextDialog(
				context,
				_app._dialogBackground,
				_boldFont,
				_normalFont,
				"Network Connection Error",
				"Your internet connection is unavailable. Please check settings before continuing.");
		_textDialog.show();
	}

	public Bitmap getDialogBitmap() {
		return _app._dialogBackground;
	}

	public void showTickImage(Context context) {

		try {
			_tickDialog = new TickDialog(context);
			_tickDialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
