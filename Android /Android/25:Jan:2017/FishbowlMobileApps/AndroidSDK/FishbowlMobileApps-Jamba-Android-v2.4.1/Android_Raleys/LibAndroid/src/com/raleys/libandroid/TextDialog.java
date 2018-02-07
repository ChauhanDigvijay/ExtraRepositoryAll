package com.raleys.libandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TextDialog extends ModalDialog {
	private Object _handler;
	private Context _context;
	private Typeface _messageFont;
	private String _message;
	private String _callback;
	private String _leftCallback;
	private String _rightCallback;
	private int _buttons;

	public TextDialog(Context context, Bitmap background, Typeface titleFont,
			Typeface messageFont, String title, String message) {
		super(context, background, titleFont, title);

		_context = context;
		_message = message;
		_messageFont = messageFont;
		_callback = null;
		_leftCallback = null;
		_rightCallback = null;
		_buttons = 1;
		buildDialog();
	}

	public TextDialog(Context context, Bitmap background, Typeface titleFont,
			Typeface messageFont, String title, String message, String callback) {
		super(context, background, titleFont, title);

		_context = context;
		_messageFont = messageFont;
		_message = message;
		_callback = callback;
		_leftCallback = null;
		_rightCallback = null;
		_buttons = 1;
		buildDialog();
	}

	public TextDialog(Context context, Bitmap background, Typeface titleFont,
			Typeface messageFont, String title, String message,
			String leftCallback, String rightCallback) {
		super(context, background, titleFont, title);

		_context = context;
		_messageFont = messageFont;
		_message = message;
		_callback = null;
		_leftCallback = leftCallback;
		_rightCallback = rightCallback;
		_buttons = 2;
		buildDialog();
	}

	void buildDialog() {
		CenteredTextView text = new CenteredTextView(_context,
				(int) (_dialogWidth * .92), (int) (_dialogHeight * .48), 4,
				Color.TRANSPARENT, Color.WHITE, _messageFont, _message);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = (int) (_dialogHeight * .16);
		layoutParams.leftMargin = (int) (_dialogWidth * .04);

		RelativeLayout text_container = new RelativeLayout(_context);
		RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// text_container.setBackgroundColor(Color.BLUE);
		text_container.addView(text, layoutParams1);

		_dialogLayout.addView(text_container, layoutParams);

		buildButtons();
		setContentView();
	}

	@SuppressWarnings("deprecation")
	void buildButtons() {
		_handler = _context;

		try {
			float[] CornerRadius;
			int cornerSize = (int) (_buttonHeight * .2);
			int textSize = (int) (_buttonHeight * .4);
			CornerRadius = new float[] { cornerSize, cornerSize, cornerSize,
					cornerSize, cornerSize, cornerSize, cornerSize, cornerSize };
			// Dynamic layout creation Part
			RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
					new RectF(0, 0, 0, 0),
					new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable top_layer = new CustomShapeDrawable(bottom_rect,
					Color.LTGRAY, Color.LTGRAY, 0);
			LayerDrawable _layoutBackground = new LayerDrawable(
					new Drawable[] { top_layer });
			_layoutBackground.setLayerInset(0, 0, 0, 0, 0);

			if (_buttons == 1) {
				int buttonWidth = (int) (_width * .3);
				Button okButton = new Button(_context);
				okButton.setText("OK");
				okButton.setTextColor(Color.BLACK);
				okButton.setTypeface(null, Typeface.BOLD);
				okButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
				okButton.setBackgroundDrawable(_layoutBackground);

				if (_callback == null) {
					okButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							dismiss();
						}
					});
				} else {
					okButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							try {
								_handler.getClass().getMethod(_callback)
										.invoke(_handler);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				}

				RelativeLayout button_container = new RelativeLayout(_context);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.width = buttonWidth;
				layoutParams.height = _buttonHeight;
				layoutParams.leftMargin = (int) (buttonWidth * .85);
				layoutParams.topMargin = _buttonYOrigin;
				RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);

				// button_container.setBackgroundColor(Color.DKGRAY);
				button_container.addView(okButton, layoutParams);
				// layoutParams2.addView(layoutParams, layoutParams);

				_dialogLayout.addView(button_container, layoutParams2);

			} else if (_buttons == 2) {
				int buttonWidth = (int) (_width * .2);
				// Bitmap buttonBitmap =
				// BitmapFactory.decodeResource(_context.getResources(),
				// R.drawable.button_white_plain);
				// buttonBitmap = Bitmap.createScaledBitmap(buttonBitmap,
				// buttonWidth, _buttonHeight, true);
				// SizedImageTextButton leftButton = new
				// SizedImageTextButton(_context, buttonBitmap, .9f,
				// Color.BLACK, _messageFont, "Yes");
				Button leftButton = new Button(_context);
				leftButton.setText("Yes");
				leftButton.setTextColor(Color.BLACK);
				leftButton.setTypeface(null, Typeface.BOLD);
				leftButton.setTextSize(textSize);
				leftButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
				leftButton.setBackgroundDrawable(_layoutBackground);
				leftButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Log.i(getClass().getSimpleName(), "handler class = "
								+ _handler.getClass() + ", method = "
								+ _leftCallback);
						try {
							_handler.getClass().getMethod(_leftCallback)
									.invoke(_handler);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

				// SizedImageTextButton rightButton = new
				// SizedImageTextButton(_context, buttonBitmap, .9f,
				// Color.BLACK, _messageFont, "No");
				Button rightButton = new Button(_context);
				rightButton.setText("No");
				rightButton.setTextColor(Color.BLACK);
				rightButton.setTypeface(null, Typeface.BOLD);
				rightButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
				rightButton.setBackgroundDrawable(_layoutBackground);
				rightButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Log.i(getClass().getSimpleName(), "handler class = "
								+ _handler.getClass() + ", method = "
								+ _rightCallback);
						try {
							_handler.getClass().getMethod(_rightCallback)
									.invoke(_handler);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

				int xSpacing = (_dialogWidth - (buttonWidth * 2)) / 3;

				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = xSpacing;
				layoutParams.topMargin = _buttonYOrigin;
				layoutParams.width = buttonWidth;
				layoutParams.height = _buttonHeight;
				_dialogLayout.addView(leftButton, layoutParams);

				layoutParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = buttonWidth + (xSpacing * 2);
				layoutParams.topMargin = _buttonYOrigin;
				layoutParams.width = buttonWidth;
				layoutParams.height = _buttonHeight;
				_dialogLayout.addView(rightButton, layoutParams);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
