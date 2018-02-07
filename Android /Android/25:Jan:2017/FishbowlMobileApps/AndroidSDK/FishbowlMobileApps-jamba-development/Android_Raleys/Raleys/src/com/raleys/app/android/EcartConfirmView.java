package com.raleys.app.android;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raleys.app.android.models.EcartOrderRequest;
import com.raleys.app.android.models.EcartPreOrderResponse;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Store;
import com.raleys.libandroid.SmartTextView;

public class EcartConfirmView extends Dialog implements OnTouchListener {
	private RaleysApplication _app;
	private Object _handler;
	private String _callback;
	private int _width;
	private int _height;
	private Typeface _normalFont;
	private Typeface _boldFont;
	private Typeface _fixedFont;
	private RelativeLayout _mainLayout;
	private EcartPreOrderResponse _ecartPreOrderResponse;
	private EcartOrderRequest _ecartOrderRequest;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public EcartConfirmView(Context context, EcartPreOrderResponse response,
			EcartOrderRequest request, String callback) {
		super(context, R.style.ModalDialog);

		try {
			_app = (RaleysApplication) context.getApplicationContext();
			_handler = context;
			_callback = callback;
			_ecartPreOrderResponse = response;
			_ecartOrderRequest = request;
			_normalFont = _app.getNormalFont();
			_boldFont = _app.getBoldFont();
			_fixedFont = _app.getFixedFont();
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
			_mainLayout.setBackgroundColor(Color.argb(150, 0, 0, 0));

			int _screenWidth = _app.getScreenWidth();
			int _screenHeight = _app.getScreenHeight();
			// int dialogTopPad = (int) (_screenWidth * .08);
			int dialogTopPad = (int) (_screenWidth * .08);
			int dialogSidePad = (int) (_screenWidth * .04);
			int dialogLeft = dialogSidePad;
			int dialogTop = _app.getHeaderHeight() + dialogTopPad;
			int dialogWidth = _screenWidth - (dialogSidePad * 2);
			int dialogHeight = _screenHeight - _app.getFooterHeight()
					- dialogTop - dialogTopPad;
			int textWidth = (int) (dialogWidth * .9);
			int textXOrigin = (dialogLeft + (dialogWidth - textWidth) / 2);
			// int textColor = Color.argb(255, 90, 60, 20);
			int textColor = Color.DKGRAY;
			int splitTextWidth = textWidth / 2;
			int splitTextRightXOrigin = dialogLeft + (int) (dialogWidth * .51);
			int headingHeight = (int) (dialogHeight * .04);
			int textHeight = (int) (dialogHeight * .035);

			int corRadius = (int) (dialogWidth * .025);
			float[] WhiteCornerRadius;
			int newcornerSize = (int) (corRadius * .8);
			WhiteCornerRadius = new float[] { newcornerSize, newcornerSize,
					newcornerSize, newcornerSize, newcornerSize, newcornerSize,
					newcornerSize, newcornerSize };
			// Dynamic layout creation Part
			RoundRectShape whiteBackground = new RoundRectShape(
					WhiteCornerRadius, new RectF(0, 0, 0, 0), new float[] { 0,
							0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable Whiteback = new CustomShapeDrawable(whiteBackground,
					Color.DKGRAY, Color.WHITE, 0);
			LayerDrawable whiteLayer = new LayerDrawable(
					new Drawable[] { Whiteback });
			whiteLayer.setLayerInset(0, 0, 0, 0, 0);

			// dialog layout
			LinearLayout dialog = new LinearLayout(context);
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				dialog.setBackgroundDrawable(whiteLayer);
			} else {
				dialog.setBackground(whiteLayer);
			}
			dialog.setOrientation(LinearLayout.VERTICAL);

			// SizedImageView dialog = new SizedImageView(context, dialogWidth,
			// dialogHeight);
			// dialog.setBackgroundDrawable(new
			// BitmapDrawable(_app.getAppBitmap(
			// "ecart_confirm_background", R.drawable.account_background,
			// dialogWidth, dialogHeight)));

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = dialogTop;
			layoutParams.leftMargin = dialogLeft;
			layoutParams.width = dialogWidth;
			layoutParams.height = dialogHeight;
			_mainLayout.addView(dialog, layoutParams);

			SmartTextView headerText = new SmartTextView(context, textWidth,
					headingHeight, 1, Color.TRANSPARENT, Color.rgb(187, 0, 0),
					_boldFont, "Your Pickup Information:", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .005);
			_mainLayout.addView(headerText, layoutParams);

			Login login = _app.getLogin();
			String storeName = null;
			String storeAddress = null;
			String storeCityStateZip = null;
			String storePhone = null;

			List<Store> storeList = _app.getStoresList();

			if (storeList != null && storeList.size() > 0) {
				for (Store store : storeList) {
					if (login.storeNumber == store.storeNumber) {
						storeName = store.chain;
						storeAddress = store.address;
						storeCityStateZip = store.city + ", " + store.state
								+ ", " + store.zip;
						storePhone = store.groceryPhoneNo;
						break;
					}
				}
			} else // this shouldn't happen, but just in case
			{
				storeName = String.valueOf(login.storeNumber);
				storeAddress = "Store " + login.storeNumber
						+ " additional details unavailable at this time";
				storeCityStateZip = "";
				storePhone = "";
			}

			SmartTextView orderingAtText = new SmartTextView(context,
					textWidth, headingHeight, 1, Color.TRANSPARENT, textColor,
					_boldFont, "You are ordering at: " + storeName,
					Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .07);
			_mainLayout.addView(orderingAtText, layoutParams);

			SmartTextView addressText = new SmartTextView(context, textWidth,
					textHeight, 1, Color.TRANSPARENT, textColor, _normalFont,
					storeAddress, Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .115);
			_mainLayout.addView(addressText, layoutParams);

			SmartTextView cityStateZipText = new SmartTextView(context,
					textWidth, textHeight, 1, Color.TRANSPARENT, textColor,
					_normalFont, storeCityStateZip, Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .155);
			_mainLayout.addView(cityStateZipText, layoutParams);

			SmartTextView phoneText = new SmartTextView(context, textWidth,
					textHeight, 1, Color.TRANSPARENT, textColor, _normalFont,
					storePhone, Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .195);
			_mainLayout.addView(phoneText, layoutParams);

			SmartTextView substitutionHeaderText = new SmartTextView(context,
					textWidth, headingHeight, 1, Color.TRANSPARENT, textColor,
					_boldFont, "Substitutions: ", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .245);
			_mainLayout.addView(substitutionHeaderText, layoutParams);

			SmartTextView substitutionText = new SmartTextView(context,
					textWidth, textHeight, 1, Color.TRANSPARENT, textColor,
					_normalFont, _ecartOrderRequest.substitutionPreferenceName,
					Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .29);
			_mainLayout.addView(substitutionText, layoutParams);

			SmartTextView bagPreferenceHeaderText = new SmartTextView(context,
					textWidth, headingHeight, 1, Color.TRANSPARENT, textColor,
					_boldFont, "Bag Preference: ", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .34);
			_mainLayout.addView(bagPreferenceHeaderText, layoutParams);

			SmartTextView bagPreferenceText = new SmartTextView(context,
					textWidth, textHeight, 1, Color.TRANSPARENT, textColor,
					_normalFont, _ecartOrderRequest.bagPreferenceName,
					Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .385);
			_mainLayout.addView(bagPreferenceText, layoutParams);

			SmartTextView pickupHeaderText = new SmartTextView(context,
					textWidth, headingHeight, 1, Color.TRANSPARENT, textColor,
					_boldFont, "Pickup Date And Time: ", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .435);
			_mainLayout.addView(pickupHeaderText, layoutParams);

			SmartTextView pickupText = new SmartTextView(context, textWidth,
					textHeight, 1, Color.TRANSPARENT, textColor, _normalFont,
					_ecartOrderRequest.appointmentDay + "  "
							+ _ecartOrderRequest.appointmentTime, Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .48);
			_mainLayout.addView(pickupText, layoutParams);

			SmartTextView instructionHeaderText = new SmartTextView(context,
					textWidth, headingHeight, 1, Color.TRANSPARENT, textColor,
					_boldFont, "Special Instructions: ", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .53);
			_mainLayout.addView(instructionHeaderText, layoutParams);

			if (_ecartOrderRequest.instructions != null
					&& _ecartOrderRequest.instructions.length() > 0) {
				TextView instructionText = new TextView(context);
				instructionText.setMaxLines(10);
				instructionText
						.setMovementMethod(new ScrollingMovementMethod());
				instructionText.setGravity(Gravity.TOP | Gravity.LEFT);
				instructionText.setBackgroundColor(Color.TRANSPARENT);
				instructionText.setTextColor(textColor);
				instructionText.setTypeface(_normalFont);
				instructionText.setPadding((int) (textWidth * .005),
						(int) (textHeight * .1), (int) (textWidth * .005),
						(int) (textHeight * .1));
				 instructionText.setText(_ecartOrderRequest.instructions);
				instructionText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						(int) (textHeight * .7));
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = textXOrigin;
				layoutParams.width = textWidth;
				layoutParams.topMargin = dialogTop
						+ (int) (dialogHeight * .575);
				layoutParams.height = textHeight * 2;
				_mainLayout.addView(instructionText, layoutParams);
			}

			SmartTextView orderTotalText = new SmartTextView(context,
					textWidth, headingHeight, 1, Color.TRANSPARENT, textColor,
					_boldFont, "Your Order Totals: ", Align.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = dialogTop + (int) (dialogHeight * .665);
			_mainLayout.addView(orderTotalText, layoutParams);

			int sePointsYOrigin = dialogTop + (int) (dialogHeight * .71);
			SmartTextView sePointsPrompt = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _normalFont, "Estimated Points:", Align.RIGHT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = sePointsYOrigin;
			_mainLayout.addView(sePointsPrompt, layoutParams);

			SmartTextView sePointsText = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _fixedFont,
					String.valueOf(_ecartPreOrderResponse.sePoints), Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = splitTextRightXOrigin;
			layoutParams.topMargin = sePointsYOrigin;
			_mainLayout.addView(sePointsText, layoutParams);

			int subtotalYOrigin = dialogTop + (int) (dialogHeight * .75);
			SmartTextView subtotalPrompt = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _normalFont, "Subtotal:", Align.RIGHT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = subtotalYOrigin;
			_mainLayout.addView(subtotalPrompt, layoutParams);

			SmartTextView subtotalText = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _fixedFont, String.format("$%7.2f",
							_ecartPreOrderResponse.productPrice), Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = splitTextRightXOrigin;
			layoutParams.topMargin = subtotalYOrigin;
			_mainLayout.addView(subtotalText, layoutParams);

			int taxYOrigin = dialogTop + (int) (dialogHeight * .79);
			SmartTextView taxPrompt = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _normalFont, "Tax + CRV:", Align.RIGHT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = taxYOrigin;
			_mainLayout.addView(taxPrompt, layoutParams);

			SmartTextView taxText = new SmartTextView(context, splitTextWidth,
					textHeight, 1, Color.TRANSPARENT, textColor, _fixedFont,
					String.format("$%7.2f", _ecartPreOrderResponse.salesTax
							+ _ecartPreOrderResponse.crv), Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = splitTextRightXOrigin;
			layoutParams.topMargin = taxYOrigin;
			_mainLayout.addView(taxText, layoutParams);

			int serviceFeeYOrigin = dialogTop + (int) (dialogHeight * .83);
			SmartTextView serviceFeePrompt = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _normalFont, "Service Fee:", Align.RIGHT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = serviceFeeYOrigin;
			_mainLayout.addView(serviceFeePrompt, layoutParams);

			SmartTextView serviceFeeText = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _fixedFont, String.format("$%7.2f",
							_ecartPreOrderResponse.fees), Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = splitTextRightXOrigin;
			layoutParams.topMargin = serviceFeeYOrigin;
			_mainLayout.addView(serviceFeeText, layoutParams);

			int totalPriceYOrigin = dialogTop + (int) (dialogHeight * .87);
			SmartTextView totalPricePrompt = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _normalFont, "Estimated Total:", Align.RIGHT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = textXOrigin;
			layoutParams.topMargin = totalPriceYOrigin;
			_mainLayout.addView(totalPricePrompt, layoutParams);

			SmartTextView totalPriceText = new SmartTextView(context,
					splitTextWidth, textHeight, 1, Color.TRANSPARENT,
					textColor, _fixedFont, String.format("$%7.2f",
							_ecartPreOrderResponse.totalPrice
									+ _ecartPreOrderResponse.crv), Align.LEFT);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = splitTextRightXOrigin;
			layoutParams.topMargin = totalPriceYOrigin;
			_mainLayout.addView(totalPriceText, layoutParams);

			// action buttons
			int buttonPad = (int) (dialogWidth * .025);
			int buttonWidth = (int) (dialogWidth * .45);
			int buttonHeight = (int) (dialogHeight * .07);
			int buttonYOrigin = dialogTop + (int) (dialogHeight * .92);
			buttonPad = (int) (dialogWidth * .02);

			// Typeface buttonFont = _normalFont;
			// Bitmap submitBitmap = _app.getAppBitmap("ecart_submit_button",
			// R.drawable.map_forward_button, buttonWidth, buttonHeight);
			// Bitmap submitBitmap = _app.getAppBitmap("ecart_submit_button",
			// R.drawable.button_red_plain, buttonWidth, buttonHeight);
			// Bitmap cancelBitmap = _app.getAppBitmap("ecart_cancel_button",
			// R.drawable.button_red_plain, buttonWidth, buttonHeight);

			float[] CornerRadius;
			int cornerSize = (int) (buttonPad * .8);
			CornerRadius = new float[] { cornerSize, cornerSize, cornerSize,
					cornerSize, cornerSize, cornerSize, cornerSize, cornerSize };

			// Dynamic layout creation Part
			RoundRectShape submitButtonRect = new RoundRectShape(CornerRadius,
					new RectF(0, 0, 0, 0),
					new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			ShapeDrawable submitDraw = new CustomShapeDrawable(
					submitButtonRect, Color.DKGRAY, Color.rgb(187, 0, 0), 0);
			LayerDrawable submitLayerBackground = new LayerDrawable(
					new Drawable[] { submitDraw });
			submitLayerBackground.setLayerInset(0, 0, 0, 0, 0);

			String buttonText = "Submit Order";
			Button submitButton = new Button(context);
			submitButton.setPadding(0, 0, 0, 0);//v2.3 fix for Lollipop
			submitButton.setText(buttonText);
			submitButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (buttonHeight * .5));
			submitButton.setTypeface(_normalFont);
			submitButton.setTextColor(Color.WHITE);

			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				submitButton.setBackgroundDrawable(submitLayerBackground);
			} else {
				submitButton.setBackground(submitLayerBackground);
			}

			// SizedImageTextButton submitButton = new SizedImageTextButton(
			// context, submitBitmap, Color.WHITE, buttonFont,
			// Utils.getFontSize(buttonWidth, (int) (buttonHeight * .6),
			// buttonFont, buttonText), buttonText);

			submitButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					placeEcartOrder();
				}
			});

			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = buttonYOrigin;
			layoutParams.leftMargin = dialogLeft + (int) (dialogWidth * .5)
					- buttonWidth - buttonPad;
			layoutParams.width = buttonWidth;
			layoutParams.height = buttonHeight;
			_mainLayout.addView(submitButton, layoutParams);

			buttonText = "Cancel Order";
			Button cancelButton = new Button(context);
			cancelButton.setText(buttonText);
			cancelButton.setPadding(0, 0, 0, 0);
			cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (buttonHeight * .5));
			cancelButton.setTypeface(_normalFont);
			cancelButton.setTextColor(Color.WHITE);

			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				cancelButton.setBackgroundDrawable(submitLayerBackground);
			} else {
				cancelButton.setBackground(submitLayerBackground);
			}
			// SizedImageTextButton cancelButton = new SizedImageTextButton(
			// context, cancelBitmap, Color.WHITE, buttonFont,
			// Utils.getFontSize(buttonWidth, (int) (buttonHeight * .6),
			// buttonFont, buttonText), buttonText);
			cancelButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = buttonYOrigin;
			layoutParams.leftMargin = dialogLeft + (int) (dialogWidth * .5)
					+ buttonPad;
			layoutParams.width = buttonWidth;
			layoutParams.height = buttonHeight;
			_mainLayout.addView(cancelButton, layoutParams);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setContentView(_mainLayout);
		getWindow().setLayout(_width, _height);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// handles all touches that aren't handled by other UI components
		return false;
	}

	void placeEcartOrder() {
		try {
			_handler.getClass().getMethod(_callback).invoke(_handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setBackgroundDrawable(LayerDrawable _layoutBackground) {

	}

	public void setBackground(LayerDrawable _layoutBackground) {

	}
}
