package com.raleys.libandroid;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * This abstract class implements a basic modal dialog with no content. NOTE:
 * Instances of any subclass derived from this class must call the
 * setContentView method in this class instead of the normal setContentView(View
 * v) method in order to bypass a bug in the Dialog class where the dialog width
 * is not sized correctly on large screen devices.
 */
public abstract class ModalDialog extends Dialog implements OnTouchListener {
	protected int _width;
	protected int _height;
	protected int _dialogWidth;
	protected int _dialogHeight;
	protected int _buttonHeight;
	protected int _buttonYOrigin;
	protected RelativeLayout _mainLayout;
	protected RelativeLayout _dialogLayout;

	@SuppressWarnings("deprecation")
	public ModalDialog(Context context, Bitmap background, Typeface font,
			String title) {
		super(context, R.style.ModalDialog);

		try {
			title = "";
			setCancelable(false); // disables the back button while dialog is
									// active
			setCanceledOnTouchOutside(false);
			DisplayMetrics displayMetrics = context.getResources()
					.getDisplayMetrics(); // metrics calculated for portrait
											// mode
			Display display = ((WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();

			if (display.getHeight() > display.getWidth()) // portrait mode
			{
				_width = displayMetrics.widthPixels;
				_height = displayMetrics.heightPixels;
				;
			} else // landscape mode
			{
				_width = displayMetrics.heightPixels;
				_height = displayMetrics.widthPixels;
			}

			_mainLayout = new RelativeLayout(context);
			_mainLayout.setOnTouchListener(this);

			int backgroundWidth = (int) (_width * .8);
			int backgroundHeight = (int) (backgroundWidth * .6); // match the
																	// iOS
																	// dialog
																	// aspect
																	// ratio
			_dialogWidth = backgroundWidth;
			_dialogHeight = backgroundHeight;
			int dialogXOrigin = (_width - backgroundWidth) / 2;
			int dialogYOrigin = (_height - backgroundHeight) / 2;
			// _buttonHeight = (int)(_dialogHeight * .14);
			// _buttonYOrigin = (int)(_dialogHeight * .80);

			// new height
			_buttonHeight = (int) (_dialogHeight * .174);
			_buttonYOrigin = (int) (_dialogHeight * .762);

			SizedImageView dialogImageView = new SizedImageView(context,
					backgroundWidth, backgroundHeight);
			dialogImageView.setImageBitmap(background);
			// dialogImageView.setAlpha(.9f);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = (_width - backgroundWidth) / 2;
			layoutParams.topMargin = (_height - backgroundHeight) / 2;
			_mainLayout.addView(dialogImageView, layoutParams);

			// dialog layout is used by inheriting classes for button placement
			_dialogLayout = new RelativeLayout(context);
			layoutParams.leftMargin = dialogXOrigin;
			layoutParams.topMargin = dialogYOrigin;
			layoutParams.width = backgroundWidth;
			layoutParams.height = backgroundHeight;
			_mainLayout.addView(_dialogLayout, layoutParams);

			// SizedTextView titleTextView = new SizedTextView(context,
			// (int)(_dialogWidth * .8), (int)(_dialogHeight * .15),
			// Color.TRANSPARENT, Color.BLACK, font, title);
			// layoutParams = new
			// RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = (int)(_dialogWidth * .1);
			// layoutParams.topMargin = (int)(_dialogHeight * .05);
			// _dialogLayout.addView(titleTextView, layoutParams);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setContentView();
	}

	public void setContentView() {
		setContentView(_mainLayout);
		getWindow().setLayout(_width, _height);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// handles all touches that aren't handled by other UI components
		return true;
	}
}
