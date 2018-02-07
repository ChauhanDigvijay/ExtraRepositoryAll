package com.raleys.app.android;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.raleys.libandroid.SizedImageButton;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SizedTextView;
import com.raleys.libandroid.TextDialog;

public class BaseScreen extends BaseActivity {
	protected RaleysApplication _app;
	protected int _screenWidth;
	protected int _screenHeight;
	protected int _headerHeight;
	protected int _footerHeight;
	protected int _contentViewHeight;
	protected int _contentViewWidth;
	protected int _navBarHeight;
	protected int _navBarWidth;
	protected int _navBarButtonCount;
	protected int _navBarButtonWidth;
	protected int _navBarLastButtonWidth;
	protected int _navBarButtonHeight;
	protected int _navBarStoreLocatorButtonHeight;
	protected int _headerButtonPad;
	protected Bitmap _navBarButtonBitmap;
	protected Bitmap _navBarLastButtonBitmap;
	protected RelativeLayout _mainLayout;
	protected RelativeLayout _contentLayout;
	protected RelativeLayout.LayoutParams _headerLayoutParams;
	protected RelativeLayout.LayoutParams _backButtonLayoutParams;
	protected SizedTextView _listNameTextView;
	protected SizedTextView _listTotalTextView;
	protected SizedImageView _headerImageView;
	protected TextDialog _textDialog;
	protected SizedImageButton _backButton;
	protected Button _CreateAccountButton;
	protected RelativeLayout.LayoutParams _CreateAccountLayoutParams;

	private final int BACKGROUND_COLOR = Color.argb(255, 241, 241, 241);

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			overridePendingTransition(
					com.raleys.libandroid.R.anim.slide_in_right,
					com.raleys.libandroid.R.anim.slide_out_left);
			_app = (RaleysApplication) getApplication();
			_screenWidth = _app.getScreenWidth();
			_screenHeight = _app.getScreenHeight();
			_headerHeight = _app.getHeaderHeight();
			_footerHeight = _app.getFooterHeight();
			_headerButtonPad = (int) (_screenWidth * .01);
			_navBarHeight = _app.getNavBarHeight();
			_navBarWidth = _screenWidth;
			_contentViewHeight = _screenHeight - _navBarHeight;
			_contentViewWidth = _screenWidth;

			RelativeLayout.LayoutParams layoutParams;

			// build the screen
			_mainLayout = new RelativeLayout(this);
			_mainLayout.setBackgroundColor(BACKGROUND_COLOR);

			// header
			_headerImageView = new SizedImageView(this, _screenWidth,
					_headerHeight);
			_headerImageView.setBackgroundDrawable(new BitmapDrawable(_app
					.getAppBitmap("base_header", R.drawable.header,
							_screenWidth, _headerHeight)));
			_headerLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			_headerLayoutParams.topMargin = 0;
			_headerLayoutParams.leftMargin = 0;
			// add header image to main layout in setContentView() so subclass
			// menus slide up underneath

			int backButtonSize = (int) (_headerHeight * .6);
			Bitmap backButtonBitmap = _app.getAppBitmap(
					"base_screen_back_button", R.drawable.back_arrow,
					backButtonSize * 2, backButtonSize);
			Bitmap backButtonSelectedBitmap = _app.getAppBitmap(
					"base_screen_back_button_selected",
					R.drawable.back_arrow_selected, backButtonSize * 2,
					backButtonSize);

			// back button
			_backButton = new SizedImageButton(this, backButtonBitmap,
					backButtonSelectedBitmap);
			_backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (_app._checkChange == true) {
						((ShowItemDetails) _app._showItemsPageContext)
								.checkChanges();
					} else {
						finish();
					}
				}
			});
			_backButtonLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			_backButtonLayoutParams.topMargin = (_headerHeight - backButtonSize) / 2;
			_backButtonLayoutParams.leftMargin = _headerButtonPad;
			// add back button to main layout in setContentView() so subclass
			// menus slide up underneath

			// Create / Update button
			int CreateButtonheight = (int) (_contentViewWidth * .10);
			int CreateButtonwidth = (int) (_contentViewWidth * .19);
			int _textSize = (int) (_screenHeight * .02);
			_CreateAccountButton = new Button(this);
			_CreateAccountButton.setText("Create");
			_CreateAccountButton.setTextSize(_textSize);
			_CreateAccountButton.setHeight(CreateButtonheight);
			_CreateAccountButton.setTextColor(Color.BLACK);
			_CreateAccountButton.setWidth(CreateButtonwidth);
			int sdk = android.os.Build.VERSION.SDK_INT;
			int _local_defaultCornerRadius = (int) (_screenWidth * .02);

			// if () {
			// _CreateAccountButton.setBackgroundDrawable(this.getResources()
			// .getDrawable(R.drawable.shop_setactive));
			// } else {
			// _CreateAccountButton.setBackground(this.getResources()
			// .getDrawable(R.drawable.shop_setactive));
			// }

			if (sdk > android.os.Build.VERSION_CODES.JELLY_BEAN) {
				_CreateAccountButton
						.setBackground(getCustomCornerBg(
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius));
			} else {
				_CreateAccountButton
						.setBackgroundDrawable(getCustomCornerBg(
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius,
								_local_defaultCornerRadius));
			}

			_CreateAccountButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// moreButtonPressed();
				}
			});
			_CreateAccountLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			_CreateAccountLayoutParams.leftMargin = (int) (_screenWidth
					- CreateButtonwidth - (_headerButtonPad * 1.5)); // 5
			_CreateAccountLayoutParams.width = CreateButtonwidth;
			_CreateAccountLayoutParams.height = backButtonSize;
			_CreateAccountLayoutParams.topMargin = (_headerHeight - backButtonSize) / 2;
			_CreateAccountLayoutParams.rightMargin = (int) ((CreateButtonwidth / _headerButtonPad) * 0.4);

			// content area for subclasses
			_contentLayout = new RelativeLayout(this);

			// _contentLayout.setBackgroundColor(Color.rgb(225, 225, 225)); //
			// lighter gray color
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = _headerHeight;
			layoutParams.leftMargin = 0;
			layoutParams.height = _contentViewHeight;
			layoutParams.width = _contentViewWidth;

			_mainLayout.addView(_contentLayout, layoutParams);

			// // footer
			// int footerYOrigin = _screenHeight - _footerHeight;
			// int footerTextHeight = (int) (_footerHeight * .7);
			//
			// if (_app.getDeviceType() == Utils.DEVICE_PHONE)
			// footerTextHeight = (int) (_footerHeight * .7);
			// else
			// footerTextHeight = (int) (_footerHeight * .8);
			//
			// int footerTextYOrigin = footerYOrigin
			// + ((_footerHeight - footerTextHeight) / 2);
			// int fontSize = Utils.getFontSizeByHeight(footerTextHeight,
			// _normalFont);
			//
			// SizedImageView footerImageView = new SizedImageView(this,
			// _screenWidth, _footerHeight);
			// footerImageView.setBackgroundDrawable(new BitmapDrawable(_app
			// .getAppBitmap("base_footer", R.drawable.footer,
			// _screenWidth, _footerHeight)));
			// layoutParams = new RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.topMargin = footerYOrigin;
			// layoutParams.leftMargin = 0;
			// _mainLayout.addView(footerImageView, layoutParams);

			//
			// int shoppingListImageSize = (int) (_footerHeight * .6);
			// SizedImageView shoppingListImageView = new SizedImageView(this,
			// shoppingListImageSize, shoppingListImageSize);
			// shoppingListImageView.setBackgroundDrawable(new
			// BitmapDrawable(_app
			// .getAppBitmap("base_footer_shopping_list",
			// R.drawable.footer_listicon, shoppingListImageSize,
			// shoppingListImageSize)));
			// layoutParams = new RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = 0;
			// layoutParams.topMargin = footerYOrigin
			// + (_footerHeight - shoppingListImageSize) / 2;
			// _mainLayout.addView(shoppingListImageView, layoutParams);
			//
			// int footerDivider = (int) (_screenWidth * .60);
			// _listNameTextView = new SizedTextView(this, footerDivider
			// - shoppingListImageSize - _headerButtonPad,
			// footerTextHeight, Color.TRANSPARENT, Color.WHITE,
			// _normalFont, fontSize, "", Align.LEFT);
			// layoutParams = new RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = shoppingListImageSize +
			// _headerButtonPad;
			// layoutParams.topMargin = footerTextYOrigin;
			// _mainLayout.addView(_listNameTextView, layoutParams);
			//
			// _listTotalTextView = new SizedTextView(this, footerDivider,
			// footerTextHeight, Color.TRANSPARENT, Color.WHITE,
			// _normalFont, fontSize, "", Align.RIGHT);
			// layoutParams = new RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = (int) (_screenWidth * .39);
			// layoutParams.topMargin = footerTextYOrigin;
			// _mainLayout.addView(_listTotalTextView, layoutParams);

			// setFooterDetails();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setContentView(View view) {
		// add header image and back button here so subclass menus slide up
		// underneath
		_mainLayout.addView(_headerImageView, _headerLayoutParams);
		_mainLayout.addView(_backButton, _backButtonLayoutParams);
		_mainLayout.addView(_CreateAccountButton, _CreateAccountLayoutParams);
		super.setContentView(view);
	}

	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(com.raleys.libandroid.R.anim.slide_in_left,
				com.raleys.libandroid.R.anim.slide_out_right);
	}

	@Override
	public void onBackPressed() {
		// do nothing, make the user hit the home button to background the app
	}

	// void setFooterDetails() {
	// if (_app._currentShoppingList == null) {
	// _listNameTextView.changeText("");
	// _listTotalTextView.changeText("");
	// } else {
	// _listNameTextView.changeText(_app._currentShoppingList.name);
	// _listTotalTextView.changeText("$"
	// + String.format("%.2f",
	// _app._currentShoppingList.totalPrice));
	// }
	// }

	void hideFooterDetails() {
		// _listNameTextView.setVisibility(View.GONE);
		// _listTotalTextView.setVisibility(View.GONE);
	}

	void hideBackButton() {
		if (_backButton != null)
			_backButton.setVisibility(View.GONE);
	}

	void restoreBackground() {
		_mainLayout.setBackgroundColor(BACKGROUND_COLOR);
	}

	void setBackgroundColor(int color) {
		_mainLayout.setBackgroundColor(color);
	}

	void setNavBarButtonAppearance(int count, Typeface font) {
		_navBarButtonCount = count;
		_navBarButtonWidth = _navBarWidth / _navBarButtonCount;
		_navBarLastButtonWidth = _navBarButtonWidth
				+ (_navBarWidth - (_navBarButtonCount * _navBarButtonWidth));
		_navBarButtonHeight = _navBarHeight;
		_navBarStoreLocatorButtonHeight = (int) (_navBarHeight * .75);
	}

	public LayerDrawable getCustomCornerBg(int topleft1, int topleft2,
			int topright1, int topright2, int bottomright1, int bottomright2,
			int bottomleft1, int bottomleft2) {
		// int cornerRadius = 24;
		// (topleft,0,0)
		float[] CornerRadius = new float[] { topleft1, topleft2, topright1,
				topright2, bottomright1, bottomright2, bottomleft1, bottomleft2 };
		// float[] CornerRadius = new float[] { cornerRadius, cornerRadius,
		// cornerRadius,cornerRadius,cornerRadius, cornerRadius,
		// cornerRadius,cornerRadius
		// };
		RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
				new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		// CustomShapeDrawable params:
		// second - border color
		// third - inner fill color
		// fourth - border stroke radius

		ShapeDrawable top_layer = new CustomShapeDrawable(bottom_rect,
				Color.WHITE, Color.rgb(255, 255, 255), 0);
		LayerDrawable ld = new LayerDrawable(new Drawable[] { top_layer });
		ld.setLayerInset(0, 0, 0, 0, 0);

		return ld;
	}

	@Override
	protected void onResume() {
		try{
			_app.customerRegistration(_app.regid);
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onResume();
	}

}
