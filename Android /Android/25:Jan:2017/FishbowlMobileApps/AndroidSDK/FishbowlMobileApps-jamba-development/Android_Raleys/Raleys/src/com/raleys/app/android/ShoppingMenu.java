package com.raleys.app.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.raleys.libandroid.SizedImageTextButton;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.Utils;

@SuppressLint("ViewConstructor")
public class ShoppingMenu extends RelativeLayout {

	private RaleysApplication _app;
	private Object _handler;
	private int _width;
	private int _height;
	private int _animationDuration;
	public boolean _isAnimating;
	public RelativeLayout.LayoutParams _hiddenLayoutParams;
	public RelativeLayout.LayoutParams _visibleLayoutParams;
	public InputMethodManager inputManager;

	public ShoppingMenu(Context context, int width, int height,
			ArrayList<String> items, int animationDuration,
			InputMethodManager _inputManager) {
		super(context);

		try {

			_app = (RaleysApplication) context.getApplicationContext();
			_handler = context;
			_width = width;
			_height = height;
			inputManager = _inputManager;
			_animationDuration = animationDuration;
			RelativeLayout.LayoutParams layoutParams;
			Typeface menuFont = _app.getNormalFont();
			int menuButtonFontSize = Utils.getFontSize((int) (_width * .8),
					(int) (_height * .8), menuFont, "Accepted Offers"); // longest
																		// text
																		// currently
																		// used
																		// in
																		// drop
																		// menus

			Bitmap topButtonBitmap = _app.getAppBitmap(
					"shopping_menu_top_button",
					R.drawable.drop_menu_top_unselected, _width, _height);
			Bitmap topButtonSelectedBitmap = _app.getAppBitmap(
					"shopping_menu_top_selected_button",
					R.drawable.drop_menu_top_selected, _width, _height);
			Bitmap middleButtonBitmap = _app.getAppBitmap(
					"shopping_menu_middle_button",
					R.drawable.drop_menu_unselected, _width, _height);
			Bitmap middleButtonSelectedBitmap = _app.getAppBitmap(
					"shopping_menu_middle_selected_button",
					R.drawable.drop_menu_selected, _width, _height);
			SizedImageTextButton menuButton;

			for (int i = 0; i < items.size(); i++) {
				String itemString = items.get(i);
				final String buttonText = itemString.substring(0,
						itemString.indexOf(":"));
				final String callbackText = itemString.substring(
						itemString.lastIndexOf(":") + 1, itemString.length());

				if (i == 0) {
					if (topButtonBitmap == null
							|| topButtonSelectedBitmap == null)
						continue;

					menuButton = new SizedImageTextButton(context,
							topButtonBitmap, topButtonSelectedBitmap,
							Color.BLACK, menuFont, menuButtonFontSize,
							buttonText);
				} else {
					if (middleButtonBitmap == null
							|| middleButtonSelectedBitmap == null)
						continue;

					menuButton = new SizedImageTextButton(context,
							middleButtonBitmap, middleButtonSelectedBitmap,
							Color.BLACK, menuFont, menuButtonFontSize,
							buttonText);
				}

				menuButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							_handler.getClass().getMethod(callbackText)
									.invoke(_handler);
							inputManager.hideSoftInputFromWindow(
									getWindowToken(), 0);

						} catch (Exception ex) {
							// Log.e("Error", "menuButton.setOnClickListener");
							// ex.printStackTrace();
						}
					}
				});

				layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = height * i;
				layoutParams.leftMargin = 0;
				addView(menuButton, layoutParams);
			}

			int baselineHeight = (int) (_height * .05);
			SizedImageView baselineImage = new SizedImageView(context, _width,
					baselineHeight);
			baselineImage.setImageBitmap(_app.getAppBitmap(
					"shopping_menu_baseline", R.drawable.drop_menu_baseline,
					_width, baselineHeight));
			layoutParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = _height * items.size();
			layoutParams.leftMargin = 0;
			addView(baselineImage, layoutParams);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		_isAnimating = false;
	}

	public void showMenu() {

		_isAnimating = true;
		setVisibility(View.VISIBLE);
		TranslateAnimation slideDown = new TranslateAnimation(0, 0, 0,
				getHeight());
		slideDown.setDuration(_animationDuration);
		slideDown.setFillEnabled(true);
		slideDown.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						getWidth(), getHeight());
				layoutParams.setMargins(_visibleLayoutParams.leftMargin,
						_visibleLayoutParams.topMargin, 0, 0);
				setLayoutParams(layoutParams);
			}
		});

		startAnimation(slideDown);
	}

	public void hideMenu() {
		if (getVisibility() == (View.VISIBLE)) {
			_isAnimating = true;
			TranslateAnimation slideUp = new TranslateAnimation(0, 0, 0,
					-getHeight());
			slideUp.setDuration(_animationDuration);
			slideUp.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							getWidth(), getHeight());
					layoutParams.setMargins(_hiddenLayoutParams.leftMargin,
							_hiddenLayoutParams.topMargin, 0, 0);
					setLayoutParams(layoutParams);
				}
			});

			startAnimation(slideUp);
			setVisibility(View.GONE);
		}
	}
}
