package com.raleys.app.android;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.ListAddItemRequest;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Product;
import com.raleys.app.android.models.ShoppingList;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SizedTextView;
import com.raleys.libandroid.SmartScrollView;
import com.raleys.libandroid.SmartTextView;
import com.raleys.libandroid.TickDialog;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceListener;

public class ShowItemDetails extends BaseScreen implements WebServiceListener {

	private RaleysApplication _app;
	private String _callback;
	private int _width;
	private int _height;
	private int _detailType;
	private float _productQuantity;
//	private float _prevQty;//v2.3 fix
//	private float _prevLb;//v2.3 fix

	private Typeface _normalFont;
	private Typeface _boldFont;
	private SmartTextView _quantityValueText;
	private SmartTextView _quantityTextViewContent;
	private SmartTextView _totalPriceValue;
	Button quantityCheckBox;
	Button weightCheckBox;
	private EditText _instructionEditText;
	private RelativeLayout _currentProductLayout;
	private Product product;
	Context context;

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
	protected int _headerButtonPad;
	protected Bitmap _navBarButtonBitmap;
	protected Bitmap _navBarLastButtonBitmap;
	protected RelativeLayout _itemLayout;
	protected RelativeLayout.LayoutParams _headerLayoutParams;
	protected RelativeLayout.LayoutParams _backButtonLayoutParams;
	protected RelativeLayout.LayoutParams _footerLayoutParams;
	protected SizedTextView _listNameTextView;
	protected SizedTextView _listTotalTextView;

	private static final String SET_ACTIVE_LIST_TEXT = "No active list to add into. Goto lists and make one active.";
	Gson _gson;
	WebService _service;
	Product _productToAdd;
	String _currentPage = "";
	Runnable runnable;
	Handler _newHandler;
	String _globalIngredients;
	Boolean dialogOpen = false;
	int footerYOrigin, footerTextYOrigin, fontSizefooter, sdk;
	FrameLayout footerImageViewLayout;
	int cornerSize;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		try {
			context = this;
			_CreateAccountButton.setVisibility(View.GONE); // Hiding Create
															// button

			_app = (RaleysApplication) context.getApplicationContext();
			_app._showItemsPageContext = this;
			_app._checkChange = false;
			Intent new_intent = getIntent();
			if (new_intent != null) {
				if (new_intent.hasExtra("currentpage")) {
					_currentPage = new_intent.getExtras().getString(
							"currentpage");
				}
				if (_currentPage.equals("productpage")) {
					_detailType = new_intent.getExtras().getInt(
							"productDetaiType");
				} else if (_currentPage.equals("listpage")) {
					_detailType = new_intent.getExtras().getInt(
							"productDetaiType");
				}
			}

			_gson = new GsonBuilder().disableHtmlEscaping().create();
			_screenWidth = _app.getScreenWidth();
			_screenHeight = _app.getScreenHeight();
			_headerHeight = _app.getHeaderHeight();
			_footerHeight = _app.getFooterHeight();
			_headerButtonPad = (int) (_screenWidth * .01);
			_navBarHeight = _app.getNavBarHeight();
			_navBarWidth = _screenWidth;
			_contentViewHeight = _screenHeight - _headerHeight - _footerHeight;
			_contentViewWidth = _screenWidth;
			product = _app._currentProductDetail;
			_normalFont = _app.getNormalFont();
			_boldFont = _app.getBoldFont();
			_width = _app.getScreenWidth();
			_height = _app.getScreenHeight();
			_mainLayout.setBackgroundColor(Color.rgb(214, 213, 213));

			_newHandler = new Handler();
			runnable = new Runnable() {

				@Override
				public void run() {
					if (dialogOpen == true) {
						return;
					} else if (dialogOpen == false) {
						dialogOpen = true;
						openIncredientsPopUp(_globalIngredients);
					}
				}
			};

			// CLP SDK PRODUCT VIEWED
			try {
				JSONObject data = new JSONObject();
				if (product != null) {
					if (product.brand == null) {
						product.brand = "";
					}
					if (product.description == null) {
						product.description = "";
					}
					data.put("SKU", product.sku);
					data.put("item_id", product.upc);
					data.put("brand", product.brand);
					data.put("item_name", product.description);

					data.put("price", String.valueOf(product.regPrice));
					data.put("promo_price", String.valueOf(product.promoPrice));
					data.put("category", String.valueOf(product.mainCategory));

					data.put("event_name", "ProductViewed");
					data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
					_app.clpsdkObj.updateAppEvent(data);

					//
					// if (_app._currentProductCategory != null) {
					// if (_app._currentProductCategory.parentCategoryName ==
					// null) {
					// _app._currentProductCategory.parentCategoryName = "";
					// }
					// if (_app._currentProductCategory.grandParentCategoryName
					// == null) {
					// _app._currentProductCategory.grandParentCategoryName =
					// "";
					// }
					// if (_app._currentProductCategory.name == null) {
					// _app._currentProductCategory.name = "";
					// }
					// // data.put(
					// // "Category_ID",
					// //
					// String.valueOf(_app._currentProductCategory.productCategoryId));
					// // data.put(
					// // "Parent_Category_ID",
					// //
					// String.valueOf(_app._currentProductCategory.parentCategoryId));
					// // data.put(
					// // "Grand_Parent_Category_ID",
					// //
					// String.valueOf(_app._currentProductCategory.grandParentCategoryId));
					// // data.put("Category_Name", String
					// // .valueOf(_app._currentProductCategory.name));
					// // data.put(
					// // "Parent_Category_Name",
					// //
					// String.valueOf(_app._currentProductCategory.parentCategoryName));
					// // data.put(
					// // "Grand_Parent_Category_Name",
					// //
					// String.valueOf(_app._currentProductCategory.grandParentCategoryName));
					// }
				}

			} catch (Exception e) {
				Log.e(_app.CLP_TRACK_ERROR, "PRODUCT_OPEN:" + e.getMessage());
			}
			// /CLP ANALYTICS END

			sdk = android.os.Build.VERSION.SDK_INT;

			// Show Progress Dialog
			showProgressDialog("");
			Handler _showItemDetailHandler = new Handler();
			_showItemDetailHandler.postDelayed(new Runnable() {

				@Override
				public void run() {

					// footer
					footerYOrigin = _screenHeight - _footerHeight;
					int footerTextHeight = (int) (_footerHeight * .7);
					if (_app.getDeviceType() == Utils.DEVICE_PHONE)
						footerTextHeight = (int) (_footerHeight * .7);
					else
						footerTextHeight = (int) (_footerHeight * .8);

					footerTextYOrigin = footerYOrigin
							+ ((_footerHeight - footerTextHeight) / 2);
					fontSizefooter = Utils.getFontSizeByHeight(
							footerTextHeight, _normalFont);

					footerImageViewLayout = new FrameLayout(context);
					_footerLayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					footerImageViewLayout.setBackgroundColor(Color.WHITE);
					_footerLayoutParams.topMargin = footerYOrigin;
					_footerLayoutParams.leftMargin = 0;
					_footerLayoutParams.width = _screenWidth;
					_footerLayoutParams.height = _footerHeight;
					_mainLayout.addView(footerImageViewLayout,
							_footerLayoutParams);
					// Footer end

					// Scroll View
					int XPadding = (int) (_width * .025);
					SmartScrollView scroll = new SmartScrollView(context);
					RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					scrollParams.topMargin = _headerHeight;
					scrollParams.leftMargin = 0;
					// scrollParams.width = _contentViewWidth;
					scrollParams.height = _contentViewHeight;
					scroll.setSmoothScrollingEnabled(true);
					_mainLayout.addView(scroll, scrollParams);

					// Scroll View end

					RelativeLayout newLayout = new RelativeLayout(context);
					RelativeLayout.LayoutParams newLayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					newLayoutParams.topMargin = _headerHeight;
					newLayoutParams.leftMargin = 0;
					newLayoutParams.width = _contentViewWidth;
					scroll.addView(newLayout, newLayoutParams);

					float[] CornerRadius;
					cornerSize = (int) (XPadding * .75);
					CornerRadius = new float[] { cornerSize, cornerSize,
							cornerSize, cornerSize, cornerSize, cornerSize,
							cornerSize, cornerSize };
					// Dynamic layout creation Part
					RoundRectShape bottom_rect = new RoundRectShape(
							CornerRadius, new RectF(0, 0, 0, 0), new float[] {
									0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable top_layer = new CustomShapeDrawable(
							bottom_rect, Color.DKGRAY, Color.WHITE, 0);
					LayerDrawable _layoutBackground = new LayerDrawable(
							new Drawable[] { top_layer });
					_layoutBackground.setLayerInset(0, 0, 0, 0, 0);

					// Content View Layout
					_currentProductLayout = new RelativeLayout(context);
					RelativeLayout.LayoutParams _contentlayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					_contentlayoutParams.topMargin = (int) (XPadding + (XPadding * .5));
					_contentlayoutParams.bottomMargin = (int) (XPadding + (XPadding * .5));
					_contentlayoutParams.rightMargin = (int) (XPadding + (XPadding * .5));
					_contentlayoutParams.leftMargin = (int) (XPadding + (XPadding * .5));
					_contentlayoutParams
							.addRule(RelativeLayout.CENTER_HORIZONTAL);

					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_currentProductLayout
								.setBackgroundDrawable(_layoutBackground);
					} else {
						_currentProductLayout.setBackground(_layoutBackground);
					}
					newLayout.addView(_currentProductLayout,
							_contentlayoutParams);
					// Content View Layout end

					// Image View Of Product
					int _productImageSize = (int) (_contentViewWidth * .925);
					// End of Image view of product

					int xPad = (int) (_width * .06);
					int yPad = (int) (_height * .01);
					int headerHeight = _app.getHeaderHeight();
					int footerHeight = _app.getFooterHeight();
					int navBarHeight = _app.getNavBarHeight();

					int dialogWidth = _width - (xPad * 2);
					int dialogHeight = _height - headerHeight - navBarHeight
							- footerHeight - (yPad * 2);

					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);

					// dialog layout
					SizedImageView dialogBackground = new SizedImageView(
							context, dialogWidth, dialogHeight);
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						dialogBackground
								.setBackgroundDrawable(new BitmapDrawable(
										context.getResources(),
										_app.getAppBitmap(
												"product_detail_background",
												R.drawable.product_detail,
												dialogWidth, dialogHeight)));
					} else {

						dialogBackground.setBackground(new BitmapDrawable(
								context.getResources(), _app.getAppBitmap(
										"product_detail_background",
										R.drawable.product_detail, dialogWidth,
										dialogHeight)));
					}

					// dialogLayout.addView(dialogBackground);

					// product image
					Bitmap productBitmap = _app
							.getCachedImage(product.imagePath);
					if (productBitmap != null) {
						productBitmap = Bitmap.createScaledBitmap(
								productBitmap, _productImageSize,
								_productImageSize, true);
					} else {
						productBitmap = _app.getAppBitmap(
								"product_detail_shopping_bag",
								R.drawable.shopping_bag, _productImageSize,
								_productImageSize);
					}
					SizedImageView productImageView = new SizedImageView(
							context, _productImageSize, _productImageSize);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = 0;
					layoutParams.leftMargin = 0;
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_currentProductLayout.getId());
					_currentProductLayout.addView(productImageView,
							layoutParams);

					productImageView
							.setImageBitmap(getRoundedCornerBitmap(productBitmap));

					int incredientsImagSize = (int) (_productImageSize * .25);
					ImageView _incredientsImageView = new ImageView(context);
					_incredientsImageView
							.setBackgroundResource(R.drawable.ingridents);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = (int) (_productImageSize * .85);
					layoutParams.leftMargin = (int) (_contentViewWidth * .70);
					layoutParams.rightMargin = XPadding;
					layoutParams.width = incredientsImagSize;
					layoutParams.height = (int) (incredientsImagSize * 0.8);
					layoutParams.addRule(RelativeLayout.RIGHT_OF,
							_currentProductLayout.getId());
					_currentProductLayout.addView(_incredientsImageView,
							layoutParams);

					int textWidth = (int) (dialogWidth * .98);
					int textHeight = (int) (dialogHeight * .055);

					// extended text(size)
					SizedTextView extendedText = new SizedTextView(context,
							textWidth, (int) (textHeight * .9),
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							product.extendedDisplay, Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = _productImageSize + XPadding;
					layoutParams.leftMargin = XPadding;
					_currentProductLayout.addView(extendedText, layoutParams);
					// extended text(size) end

					// regular price text fields
					int regPriceYOrigin = (int) (_productImageSize + (XPadding * 4.5));
					int priceWidth = (int) (dialogWidth * .4);

					SizedTextView regPriceText = new SizedTextView(context,
							priceWidth, textHeight, Color.TRANSPARENT,
							Color.DKGRAY, _normalFont, "Regular Price : ",
							Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = regPriceYOrigin;
					layoutParams.leftMargin = XPadding;
					_currentProductLayout.addView(regPriceText, layoutParams);
					// Regular price end

					// Regular Price Value
					SizedTextView regPriceValue = new SizedTextView(context,
							priceWidth, (int) (textHeight * .9),
							Color.TRANSPARENT, Color.DKGRAY, _normalFont, " $"
									+ String.format("%.2f", product.regPrice),
							Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = regPriceYOrigin;
					layoutParams.leftMargin = priceWidth;
					_currentProductLayout.addView(regPriceValue, layoutParams);
					// Regular Price Value End

					// Promo price layout start
					int priceLayoutYOrigin;
					if (product.promoType > 0) {
						priceLayoutYOrigin = _productImageSize + (XPadding * 8);
					} else {
						priceLayoutYOrigin = _productImageSize + (XPadding * 8);
					}
					RelativeLayout _priceandTotalLayout = new RelativeLayout(
							context);
					RelativeLayout.LayoutParams _priceandTotallayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					_priceandTotallayoutParams.topMargin = priceLayoutYOrigin;
					_priceandTotallayoutParams.leftMargin = XPadding;
					_priceandTotallayoutParams.bottomMargin = XPadding;
					_currentProductLayout.addView(_priceandTotalLayout,
							_priceandTotallayoutParams);

					// Price Tag Start
					int _priceImageSize = (int) (_productImageSize * .1);
					if (product.promoType > 0) {

						SizedImageView _priceImage = new SizedImageView(
								context, _priceImageSize, _priceImageSize);
						_priceImage.setImageBitmap(_app.getAppBitmap(
								"sale_icon", R.drawable.sale_icon,
								_priceImageSize, _priceImageSize));
						RelativeLayout.LayoutParams _pricelayoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						_pricelayoutParams.topMargin = 0;
						_pricelayoutParams.leftMargin = 0;
						_pricelayoutParams.addRule(RelativeLayout.LEFT_OF,
								_priceImage.getId());
						_pricelayoutParams.addRule(
								RelativeLayout.CENTER_VERTICAL,
								_priceImage.getId());
						_priceandTotalLayout.addView(_priceImage,
								_pricelayoutParams);
						// Price Tag End

						// total amount start
						_totalPriceValue = new SmartTextView(context,
								(int) (dialogWidth * .40), textHeight, 1,
								Color.TRANSPARENT, Color.rgb(187, 0, 0),
								_boldFont, "", Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = 0;
						layoutParams.leftMargin = _priceImageSize + XPadding;
						layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
								_totalPriceValue.getId());
						_priceandTotalLayout.addView(_totalPriceValue,
								layoutParams);
						// Total Amount End
					} else {

						// total amount start
						_totalPriceValue = new SmartTextView(context,
								(int) (dialogWidth * .40), textHeight, 1,
								Color.TRANSPARENT, Color.BLACK, _boldFont, "",
								Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = 0;
						layoutParams.leftMargin = 0;
						layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
								_totalPriceValue.getId());
						_priceandTotalLayout.addView(_totalPriceValue,
								layoutParams);
						// Total Amount End
					}

					// product description text
					String descriptionString;
					if (product.brand != null && product.brand.length() > 0)
						descriptionString = product.brand + " "
								+ product.description;
					else
						descriptionString = product.description;

					int descriptionYOrigin;
					if (product.promoType > 0) {
						descriptionYOrigin = (int) (_productImageSize + (XPadding * 12.5));
					} else {
						descriptionYOrigin = _productImageSize
								+ (XPadding * 12);
					}
					SmartTextView description = new SmartTextView(context,
							textWidth, (int) (textHeight * 1.75), 2,
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							descriptionString, Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = descriptionYOrigin;
					layoutParams.leftMargin = XPadding;
					_currentProductLayout.addView(description, layoutParams);

					// promo stuff
					int promoPriceYOrigin = (int) (_productImageSize + +(XPadding * 17.5));
					if (product.promoType > 0) {
						SizedTextView promoPriceText = new SizedTextView(
								context, priceWidth, (int) (textHeight * .90),
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								"Promo Price : ", Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = promoPriceYOrigin;
						layoutParams.leftMargin = XPadding;
						_currentProductLayout.addView(promoPriceText,
								layoutParams);

						String valueString;
						if (product.promoType > 0 && product.promoForQty >= 1)
							valueString = " " + product.promoForQty + " for $"
									+ String.format("%.2f", product.promoPrice);
						else
							valueString = " $"
									+ String.format("%.2f", product.promoPrice);

						SizedTextView promoPriceValue = new SizedTextView(
								context, priceWidth, (int) (textHeight * .90),
								Color.TRANSPARENT, Color.rgb(187, 0, 0),
								_normalFont, valueString, Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = promoPriceYOrigin;
						layoutParams.leftMargin = priceWidth;
						_currentProductLayout.addView(promoPriceValue,
								layoutParams);

						int promoTextYOrigin = (int) (_productImageSize + (XPadding * 20.5));
						SizedTextView promoDescription = new SizedTextView(
								context, textWidth, (int) (textHeight * .9),
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								product.promoPriceText, Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = promoTextYOrigin;
						layoutParams.leftMargin = XPadding;
						_currentProductLayout.addView(promoDescription,
								layoutParams);
					}

					// quantity/weight choice stuff
					int quantityHeight = (int) (dialogHeight * .07);
					if (product.approxAvgWgt > 0.0) {

						int buttonRowHeight = (int) (dialogHeight * .04);
						int buttonRowTextHeight = buttonRowHeight * 2;

						RelativeLayout _checkBoxFirstLayout = new RelativeLayout(
								context);
						RelativeLayout.LayoutParams _checkBoxFirstlayoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						if (product.promoType > 0) {
							_checkBoxFirstlayoutParams.topMargin = _productImageSize
									+ (XPadding * 24);
						} else {
							_checkBoxFirstlayoutParams.topMargin = (int) (_productImageSize + (XPadding * 17.5)); // 29.5));
						}
						_checkBoxFirstlayoutParams.leftMargin = XPadding;
						_checkBoxFirstlayoutParams.height = (int) (_priceImageSize + (XPadding * .5));
						_currentProductLayout.addView(_checkBoxFirstLayout,
								_checkBoxFirstlayoutParams);

						RelativeLayout _checkBoxSecondLayout = new RelativeLayout(
								context);
						RelativeLayout.LayoutParams _checkBoxSecondlayoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);

						if (product.promoType > 0) {
							_checkBoxSecondlayoutParams.topMargin = _productImageSize
									+ (XPadding * 24);
						} else {
							_checkBoxSecondlayoutParams.topMargin = (int) (_productImageSize + (XPadding * 17.5)); // 29.5));
						}
						_checkBoxSecondlayoutParams.leftMargin = (int) (textWidth * .5);
						_checkBoxSecondlayoutParams.height = (int) (_priceImageSize + (XPadding * .5));
						_currentProductLayout.addView(_checkBoxSecondLayout,
								_checkBoxSecondlayoutParams);

						quantityCheckBox = new Button(context);
						quantityCheckBox
								.setOnTouchListener(new OnTouchListener() {
									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										if (event.getAction() == MotionEvent.ACTION_DOWN)
											setPricingByQuantity();

										return true;
									}

								});

						RelativeLayout.LayoutParams qtyChecklayoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						qtyChecklayoutParams.leftMargin = 0;
						qtyChecklayoutParams.topMargin = 0;
						qtyChecklayoutParams.width = _priceImageSize;
						qtyChecklayoutParams.height = _priceImageSize;
						quantityCheckBox.setSelected(false);
						qtyChecklayoutParams.addRule(
								RelativeLayout.CENTER_VERTICAL,
								quantityCheckBox.getId());
						quantityCheckBox
								.setBackgroundResource(R.drawable.product_unchecked_box);
						_checkBoxFirstLayout.addView(quantityCheckBox,
								qtyChecklayoutParams);

						SizedTextView quantityText = new SizedTextView(context,
								(int) (dialogWidth * .325),
								buttonRowTextHeight, Color.TRANSPARENT,
								Color.DKGRAY, _normalFont, "Show by Quantity",
								Align.LEFT);
						RelativeLayout.LayoutParams qtyTextlayoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						qtyTextlayoutParams.topMargin = 0;
						qtyTextlayoutParams.leftMargin = _priceImageSize
								+ XPadding;
						qtyTextlayoutParams.addRule(
								RelativeLayout.CENTER_VERTICAL,
								quantityText.getId());
						_checkBoxFirstLayout.addView(quantityText,
								qtyTextlayoutParams);

						weightCheckBox = new Button(context);
						weightCheckBox
								.setOnTouchListener(new OnTouchListener() {
									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										if (event.getAction() == MotionEvent.ACTION_DOWN)
											setPricingByWeight();

										return true;
									}
								});

						RelativeLayout.LayoutParams wgtChecklayoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						wgtChecklayoutParams.leftMargin = 0;
						wgtChecklayoutParams.topMargin = 0;
						wgtChecklayoutParams.width = _priceImageSize;
						wgtChecklayoutParams.height = _priceImageSize;
						weightCheckBox.setSelected(false);
						wgtChecklayoutParams.addRule(
								RelativeLayout.CENTER_VERTICAL,
								weightCheckBox.getId());
						weightCheckBox
								.setBackgroundResource(R.drawable.product_checked_box);
						_checkBoxSecondLayout.addView(weightCheckBox,
								wgtChecklayoutParams);

						SizedTextView weightText = new SizedTextView(context,
								(int) (dialogWidth * .30), buttonRowTextHeight,
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								"Show by Weight", Align.LEFT);
						RelativeLayout.LayoutParams wgtTextlayoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						wgtTextlayoutParams.topMargin = 0;
						wgtTextlayoutParams.leftMargin = _priceImageSize
								+ XPadding;
						wgtTextlayoutParams.addRule(
								RelativeLayout.CENTER_VERTICAL,
								weightText.getId());
						_checkBoxSecondLayout.addView(weightText,
								wgtTextlayoutParams);

					}

					// instruction stuff
					int textCornerSize = (int) (XPadding * .5);
					float[] textCornerRadius = new float[] { textCornerSize,
							textCornerSize, textCornerSize, textCornerSize,
							textCornerSize, textCornerSize, textCornerSize,
							textCornerSize };
					RoundRectShape _instructionEditTextRect = new RoundRectShape(
							textCornerRadius, new RectF(0, 0, 0, 0),
							new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable _instructionEditTextLayer = new CustomShapeDrawable(
							_instructionEditTextRect, Color.TRANSPARENT,
							Color.DKGRAY, 0);
					RoundRectShape _instructionTop = new RoundRectShape(
							textCornerRadius, new RectF(0, 0, 0, 0),
							new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable _instructionLayerTop = new CustomShapeDrawable(
							_instructionTop, Color.TRANSPARENT, Color.WHITE, 0);

					LayerDrawable _instructionEditTextBackground = new LayerDrawable(
							new Drawable[] { _instructionEditTextLayer,
									_instructionLayerTop });

					_instructionEditTextBackground.setLayerInset(0, 0, 0, 0, 0);
					_instructionEditTextBackground.setLayerInset(1, (int) 2.0f,
							(int) 2.0f, (int) 2.0f, (int) 2.0f);

					RelativeLayout instructionsBackground = new RelativeLayout(
							context);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					if (product.promoType > 0) {
						if (product.approxAvgWgt > 0.0) {
							layoutParams.topMargin = _productImageSize
									+ (XPadding * 30);
						} else {
							layoutParams.topMargin = _productImageSize
									+ (XPadding * 24);
						}
					} else {
						if (product.approxAvgWgt > 0.0) {
							layoutParams.topMargin = (int) (_productImageSize + (XPadding * 23.5)); // 29.5));
						} else {
							layoutParams.topMargin = (int) (_productImageSize + (XPadding * 18.5)); // 29.5));
						}
					}
					layoutParams.leftMargin = XPadding;
					layoutParams.rightMargin = XPadding;
					layoutParams.bottomMargin = XPadding;
					layoutParams.width = textWidth;
					layoutParams.height = (int) (_priceImageSize * .8);
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						instructionsBackground
								.setBackgroundDrawable(_instructionEditTextBackground);

					} else {
						instructionsBackground
								.setBackground(_instructionEditTextBackground);

					}
					_currentProductLayout.addView(instructionsBackground,
							layoutParams);

					int instructionHeight = (int) (dialogHeight * .05);
					_instructionEditText = new EditText(context);
					_instructionEditText
							.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
									_app.instructionTextLength) });// added in
																	// version
																	// 2.3
					_instructionEditText.setGravity(Gravity.TOP | Gravity.LEFT);
					_instructionEditText.setBackgroundColor(Color.TRANSPARENT);
					_instructionEditText.setTextColor(Color.BLACK);
					_instructionEditText.setTypeface(_normalFont);
					_instructionEditText.setPadding(0,
							-(int) (instructionHeight * .05), 0, 0);
					_instructionEditText.setTextSize(
							TypedValue.COMPLEX_UNIT_PX,
							(int) (instructionHeight * .6));
					_instructionEditText
							.setImeOptions(EditorInfo.IME_ACTION_DONE);
					_instructionEditText.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

					if (product.customerComment != null
							&& product.customerComment.length() > 0)
						_instructionEditText.setText(product.customerComment);
					else
						_instructionEditText
								.setHint("Personal Shopper Instructions...");
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = 0;
					layoutParams.leftMargin = (int) (XPadding * .5);
					layoutParams.width = textWidth;
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
							_instructionEditText.getId());
					instructionsBackground.addView(_instructionEditText,
							layoutParams);

					// Separator line Layout start
					RelativeLayout _lineLayout = new RelativeLayout(context);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					if (product.promoType > 0) {
						if (product.approxAvgWgt > 0.0) {
							layoutParams.topMargin = _productImageSize
									+ (XPadding * 34);
						} else {
							layoutParams.topMargin = _productImageSize
									+ (XPadding * 28);
						}
					} else {
						if (product.approxAvgWgt > 0.0) {
							layoutParams.topMargin = (int) (_productImageSize + (XPadding * 27.5)); // 29.5));
						} else {
							layoutParams.topMargin = (int) (_productImageSize + (XPadding * 22.5)); // 29.5));
						}
					}
					layoutParams.leftMargin = XPadding;
					layoutParams.rightMargin = XPadding;
					layoutParams.width = textWidth;
					layoutParams.height = (int) (_priceImageSize * .025);
					_lineLayout.setBackgroundColor(Color.rgb(187, 0, 0));
					_currentProductLayout.addView(_lineLayout, layoutParams);
					// line layout end

					// newly added
					String MyCurrentListCount = new String();
					ShoppingList shoppinglist = null;
					if (_currentPage.equals("productpage")) {
						shoppinglist = _app.getActiveShoppingList();
					} else if (_currentPage.equals("listpage")) {
						shoppinglist = _app._currentShoppingList;
					}

					if (shoppinglist == null) {
						MyCurrentListCount = "You do not have an active list"; // return
																				// null
																				// value
					} else {

						if (shoppinglist.productList == null) {
							MyCurrentListCount = "No Items in the List"; // return
							// null
							// value
						} else if (shoppinglist.productList != null) {
							String sName = shoppinglist.name;
							int arrSize = shoppinglist.productList.size();
							MyCurrentListCount = sName + " ("
									+ String.valueOf(arrSize) + " items)";
						}
					}

					SmartTextView myActiveList_txt = new SmartTextView(context,
							textWidth, (int) (textHeight * .95), 1,
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							MyCurrentListCount, Align.CENTER);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					if (product.promoType > 0) {
						if (product.approxAvgWgt > 0.0) {
							layoutParams.topMargin = (int) (_productImageSize + (XPadding * 35.5));
						} else {
							layoutParams.topMargin = (int) (_productImageSize + (XPadding * 29.5));
						}
					} else {
						if (product.approxAvgWgt > 0.0) {
							layoutParams.topMargin = _productImageSize
									+ (XPadding * 29);
						} else {
							layoutParams.topMargin = _productImageSize
									+ (XPadding * 24);
						}
					}
					layoutParams.leftMargin = XPadding;
					layoutParams.bottomMargin = XPadding * 2;
					_currentProductLayout.addView(myActiveList_txt,
							layoutParams);
					// active list end

					// footer layout design
					int _plusMinusTotalWdth = (int) (_width * .6);
					int _buttonWidth = (int) (_width * .4);
					int _currHeight = _footerHeight;

					RelativeLayout _footerContentLayout = new RelativeLayout(
							context);
					RelativeLayout.LayoutParams footerContentParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					footerContentParams.topMargin = footerYOrigin;
					footerContentParams.leftMargin = 0;
					footerContentParams.width = _width;
					footerContentParams.height = _currHeight;
					footerImageViewLayout.addView(_footerContentLayout,
							footerContentParams);

					RoundRectShape footRect = new RoundRectShape(CornerRadius,
							new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0,
									0, 0, 0 });
					ShapeDrawable footRectlayer = new CustomShapeDrawable(
							footRect, Color.TRANSPARENT, Color.DKGRAY, 0);

					RoundRectShape footRectTop = new RoundRectShape(
							CornerRadius, new RectF(0, 0, 0, 0), new float[] {
									0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable footRectlayerTop = new CustomShapeDrawable(
							footRectTop, Color.TRANSPARENT, Color.WHITE, 0);

					LayerDrawable addfootRectlayer = new LayerDrawable(
							new Drawable[] { footRectlayer, footRectlayerTop });

					addfootRectlayer.setLayerInset(0, 0, 0, 0, 0);
					addfootRectlayer.setLayerInset(1, (int) 2.0f, (int) 2.0f,
							(int) 2.0f, (int) 2.0f);

					RelativeLayout _plusMinusTotalLyout = new RelativeLayout(
							context);
					RelativeLayout.LayoutParams footerParamsLeft = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					footerParamsLeft.topMargin = (int) (XPadding * .5);
					footerParamsLeft.leftMargin = XPadding;
					footerParamsLeft.bottomMargin = (int) (XPadding * .5);
					footerParamsLeft.rightMargin = XPadding;
					footerParamsLeft.width = _plusMinusTotalWdth;
					footerParamsLeft.height = _currHeight;
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_plusMinusTotalLyout
								.setBackgroundDrawable(addfootRectlayer);
					} else {
						_plusMinusTotalLyout.setBackground(addfootRectlayer);
					}
					_footerContentLayout.addView(_plusMinusTotalLyout,
							footerParamsLeft);

					RelativeLayout _buttonLyout = new RelativeLayout(context);
					RelativeLayout.LayoutParams footerParamsRight = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					footerParamsRight.topMargin = (int) (XPadding * .5);
					footerParamsRight.leftMargin = _plusMinusTotalWdth
							+ XPadding;
					footerParamsRight.bottomMargin = (int) (XPadding * .5);
					footerParamsRight.width = _buttonWidth;
					footerParamsRight.height = _currHeight;
					_footerContentLayout.addView(_buttonLyout,
							footerParamsRight);

					int textPriceSize = (int) (_priceImageSize * .85);
					int _leftPanelWidth = _plusMinusTotalWdth;
					int _symbolTextWidth = (int) (_leftPanelWidth * .6);
					int _plusWidth = (int) (_leftPanelWidth * .2);
					int _minusWidth = (int) (_leftPanelWidth * .2);

					// Minus Button

					RoundRectShape minusrect = new RoundRectShape(new float[] {
							cornerSize, cornerSize, 0, 0, 0, 0, cornerSize,
							cornerSize }, new RectF(0, 0, 0, 0), new float[] {
							0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable minusrectlayer = new CustomShapeDrawable(
							minusrect, Color.rgb(145, 145, 145), Color.rgb(187,
									0, 0), 0);
					LayerDrawable minusBG = new LayerDrawable(
							new Drawable[] { minusrectlayer });
					minusBG.setLayerInset(0, 0, 0, 0, 0);

					RelativeLayout _minusLayout = new RelativeLayout(context);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 0;
					layoutParams.topMargin = 0;
					layoutParams.width = _plusWidth;
					layoutParams.height = _currHeight;
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_minusLayout.setBackgroundDrawable(minusBG);
					} else {
						_minusLayout.setBackground(minusBG);
					}
					_plusMinusTotalLyout.addView(_minusLayout, layoutParams);

					Button minusButton = new Button(context);
					minusButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							subtractFromItemCount();
						}
					});
					minusButton.setText("-");
					minusButton.setTextColor(Color.WHITE);
					minusButton.setTypeface(_normalFont);
					minusButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							(float) (textPriceSize * .75));
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 0;
					layoutParams.topMargin = 0;
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_minusLayout.getId());
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
							_minusLayout.getId());
					minusButton.setBackgroundColor(Color.TRANSPARENT);
					_minusLayout.addView(minusButton, layoutParams);

					// Plus Button

					RoundRectShape plusrect = new RoundRectShape(new float[] {
							0, 0, cornerSize, cornerSize, cornerSize,
							cornerSize, 0, 0 }, new RectF(0, 0, 0, 0),
							new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable plusrectlayer = new CustomShapeDrawable(
							plusrect, Color.rgb(145, 145, 145), Color.rgb(187,
									0, 0), 0);
					LayerDrawable plusBG = new LayerDrawable(
							new Drawable[] { plusrectlayer });
					plusBG.setLayerInset(0, 0, 0, 0, 0);

					RelativeLayout _plusLayout = new RelativeLayout(context);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = _minusWidth + _symbolTextWidth;
					layoutParams.topMargin = 0;
					layoutParams.width = _plusWidth;
					layoutParams.height = _currHeight;
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_plusLayout.setBackgroundDrawable(plusBG);
					} else {
						_plusLayout.setBackground(plusBG);
					}
					_plusMinusTotalLyout.addView(_plusLayout, layoutParams);

					Button plusButton = new Button(context);
					plusButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							addToItemCount();
						}
					});
					plusButton.setText("+");
					plusButton.setTextColor(Color.WHITE);
					plusButton.setTypeface(_normalFont);
					plusButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							(float) (textPriceSize * .75));
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 0;
					layoutParams.topMargin = 0;
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_plusLayout.getId());
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
							_plusLayout.getId());
					plusButton.setBackgroundColor(Color.TRANSPARENT);
					_plusLayout.addView(plusButton, layoutParams);

					// Add Button
					Button _addButton = new Button(context);
					if (_detailType == Product.PRODUCT_ADD) {
						_addButton.setText("Add");
					} else if (_detailType == Product.PRODUCT_MODIFY) {
						_addButton.setText("Update");
					}
					_addButton.setTextColor(Color.WHITE);
					_addButton.setTypeface(_normalFont);
					_addButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							(float) (textPriceSize * .65));
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = XPadding;
					layoutParams.topMargin = 0;
					layoutParams.rightMargin = XPadding;
					layoutParams.width = _buttonWidth;
					layoutParams.height = _currHeight;

					RoundRectShape add_rect = new RoundRectShape(CornerRadius,
							new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0,
									0, 0, 0 });
					ShapeDrawable add_layer = new CustomShapeDrawable(add_rect,
							Color.TRANSPARENT, Color.DKGRAY, 0);

					RoundRectShape add_rect_top = new RoundRectShape(
							CornerRadius, new RectF(0, 0, 0, 0), new float[] {
									0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable add_layer_top = new CustomShapeDrawable(
							add_rect_top, Color.TRANSPARENT, Color.rgb(187, 0,
									0), 0);

					LayerDrawable addLD = new LayerDrawable(new Drawable[] {
							add_layer, add_layer_top });
					addLD.setLayerInset(0, 0, 0, 0, 0);
					addLD.setLayerInset(1, (int) 2.0f, (int) 2.0f, (int) 2.0f,
							(int) 2.0f);

					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_addButton.setBackgroundDrawable(addLD);
					} else {
						_addButton.setBackground(addLD);
					}
					_buttonLyout.addView(_addButton, layoutParams);

					_addButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							addProductToList();
						}
					});

					// Footer layout design end
					int quantityTextHeight = (int) (quantityHeight * .8);
					// quantity text fields
					if (product.weight > 0) {
						_productQuantity = product.weight;
//						_prevLb = _productQuantity;
					} else {
						_productQuantity = product.qty == 0 ? 1 : product.qty; // for
																				// products
																				// retrieved
																				// in
																				// product
																				// search
																				// the
																				// qty
																				// is
																				// 0
																				// so
																				// set
																				// it
																				// to
																				// 1
																				// here
//						_prevQty = _productQuantity;
//						_prevLb = 0.25f;

					}

					// Text Field
					RelativeLayout _textLyout = new RelativeLayout(context);
					RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					textParams.topMargin = 0;
					textParams.leftMargin = _minusWidth;
					textParams.width = _symbolTextWidth;
					textParams.height = _currHeight; // layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
					_plusMinusTotalLyout.addView(_textLyout, textParams);

					_quantityValueText = new SmartTextView(context,
							(int) (_symbolTextWidth * 0.7),
							(int) (quantityTextHeight * 0.8),
							1, // .49
							Color.TRANSPARENT, Color.BLACK, _normalFont,
							Align.CENTER);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
							_plusMinusTotalLyout.getId());
					layoutParams.topMargin = 0;
					_textLyout.addView(_quantityValueText, layoutParams);

					_quantityTextViewContent = new SmartTextView(context,
							(int) (_symbolTextWidth * 0.3),
							(int) (quantityTextHeight * 0.8), 1,
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
							_plusMinusTotalLyout.getId());
					layoutParams.leftMargin = (int) (_symbolTextWidth * 0.7);
					layoutParams.topMargin = 0;
					_textLyout.addView(_quantityTextViewContent, layoutParams);

					if (product.weight > 0)
						setPricingByWeight();
					else {
						if (product.unitOfMeasure.equalsIgnoreCase("LB")) {
							if (quantityCheckBox != null
									&& (product.weight <= 0))// Quantity
							{
								setPricingByQuantity();
							} else {
								setPricingByWeight();
							}

						} else {
							setPricingByQuantity();
						}
					}

					if (product.ingredients == null) {
						_incredientsImageView.setVisibility(View.GONE);
					} else if (product.ingredients.isEmpty()) {
						_incredientsImageView.setVisibility(View.GONE);
					} else {
						_incredientsImageView.setVisibility(View.VISIBLE);
					}
					if ((product.ingredients == null)
							|| (product.ingredients.isEmpty())) {
						productImageView
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// openIncredientsPopUp("");
									}
								});
					} else {
						productImageView
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// openIncredientsPopUp(product.ingredients);
										_globalIngredients = product.ingredients;
										_newHandler.removeCallbacks(runnable);
										_newHandler.postDelayed(runnable, 1000);
									}
								});

						_incredientsImageView
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// openIncredientsPopUp(product.ingredients);

										_globalIngredients = product.ingredients;
										_newHandler.removeCallbacks(runnable);
										_newHandler.postDelayed(runnable, 1000);
									}
								});
					}

					if (_detailType == Product.PRODUCT_ADD) {
						if (product.approxAvgWgt > 0)
							setPricingByWeight();
						else {
							if (product.unitOfMeasure.equalsIgnoreCase("LB")) {
								if (quantityCheckBox != null
										&& (product.weight <= 0))// Quantity{
								{
									setPricingByQuantity();
								} else {
									setPricingByWeight();
								}

							} else {
								setPricingByQuantity();
							}
						}
					} else {
						if (product.weight > 0)
							setPricingByWeight();
						else {
							if (product.unitOfMeasure.equalsIgnoreCase("LB")) {
								if (quantityCheckBox != null
										&& (product.weight <= 0))// Quantity{
								{
									setPricingByQuantity();
								} else {
									setPricingByWeight();
								}
							} else {
								setPricingByQuantity();
							}
						}
					}
					// if (quantityCheckBox != null
					// && quantityCheckBox.isSelected() == true) {
					// _quantityValueText.writeText(String
					// .valueOf((int) _productQuantity));
					// _quantityTextViewContent.writeText("Qty");
					// } else if (weightCheckBox != null
					// && weightCheckBox.isSelected() == true) {
					// _quantityValueText.writeText(String.format("%.2f",
					// _productQuantity));
					// _quantityTextViewContent.writeText("Lbs");
					// }

					dismissActiveDialog();
					changeFlag();
				}

			}, 2000);

			setContentView(_mainLayout);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void changeFlag() {
		// multiple open issue
		_app._productItemsActivityopen = false;
	}

	public void checkChanges() {
		if (_app._checkChange == true) {
			addProductToList();
			_app._checkChange = false;
		} else {
			this.finish();
		}
	}

	protected void deleteProductFromList() {

		product.qty = (int) _productQuantity;
		try {
			context.getClass().getMethod(_callback, int.class, Product.class)
					.invoke(context, Product.PRODUCT_DELETE, product);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	protected void addProductToList() {

		if (weightCheckBox != null && weightCheckBox.isSelected() == true) {
			product.weight = _productQuantity;
			product.qty = 0;
		} else {
			if (product.unitOfMeasure.equalsIgnoreCase("LB")) {

				if (quantityCheckBox != null
						&& quantityCheckBox.isSelected() == true)// Quantity
				{
					product.qty = (int) _productQuantity;
					product.weight = 0.0f;
				} else {
					product.weight = _productQuantity;
					product.qty = 0;

				}

			} else {
				product.qty = (int) _productQuantity;
				product.weight = 0.0f;
			}

		}
		product.customerComment = _instructionEditText.getText().toString();
		try {
			// context.getClass().getMethod(_callback, int.class, Product.class)
			// .invoke(context, _detailType, product);

			_app._ProductDetailsScreenContext = context;
			Product _productToAdd = product;
 			updateShoppingList(_detailType, _productToAdd);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	protected void subtractFromItemCount() {
		if (quantityCheckBox == null) { // default
										// is
			if (product.unitOfMeasure.equalsIgnoreCase("LB")) {
				if (_productQuantity == 0.25)
					return;
				_productQuantity -= 0.25;
//				_prevLb = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String.format("%.2f",
						_productQuantity));
			} else {
				if (_productQuantity == 1)
					return;
				_productQuantity -= 1;
//				_prevQty = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String
						.valueOf((int) _productQuantity));
			}
		} else {
			if (quantityCheckBox != null
					&& quantityCheckBox.isSelected() == true)// Quantity
			{
				if ((int) _productQuantity == 1)
					return;
				_productQuantity -= 1;
//				_prevQty = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String
						.valueOf((int) _productQuantity));
			} else {
				if (_productQuantity == 0.25)
					return;
				_productQuantity -= 0.25;
//				_prevLb = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String.format("%.2f",
						_productQuantity));
			}
		}
		_app._checkChange = true; // flag for back button - updating product
									// quantity.
		setTotalPriceText();

	}

	protected void addToItemCount() {
		if (_productQuantity >= 100.0)
			return;
		if (quantityCheckBox == null) { // default is quantity
			if (product.unitOfMeasure.equalsIgnoreCase("lb")) {
				_productQuantity += 0.25;
//				_prevLb = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String.format("%.2f",
						_productQuantity));
			} else {
				_productQuantity += 1;
//				_prevQty = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String
						.valueOf((int) _productQuantity));
			}
		} else {
			if (quantityCheckBox != null
					&& quantityCheckBox.isSelected() == true)// Quantity
			{
				_productQuantity += 1;
//				_prevQty = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String
						.valueOf((int) _productQuantity));
			} else {
				_productQuantity += 0.25;
//				_prevLb = _productQuantity;//v2.3 fix
				_quantityValueText.writeText(String.format("%.2f",
						_productQuantity));
			}
		}
		_app._checkChange = true; // flag for back button - updating product
									// quantity.
		setTotalPriceText();
	}

	private void setPricingByQuantity() {

		try {
			// _productQuantity = (int) _productQuantity;//v2.3 fix
//			_productQuantity = (int) _prevQty;//v2.3 fix
			if (product.approxAvgWgt > 0.0) {
				if (quantityCheckBox.isSelected() == true)
					return;
				if (quantityCheckBox != null) {
					if ((int) _productQuantity == 0) {
						_productQuantity = 1;
//						_prevQty=1;
					}
				}
				quantityCheckBox
						.setBackgroundResource(R.drawable.product_checked_box);
				quantityCheckBox.setSelected(true);
				weightCheckBox
						.setBackgroundResource(R.drawable.product_unchecked_box);
				weightCheckBox.setSelected(false);

			}
			_quantityTextViewContent.writeText("Qty");
			_quantityValueText
					.writeText(String.valueOf((int) _productQuantity));
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTotalPriceText();

	}

	private void setPricingByWeight() {
		try {
//			_productQuantity = _prevLb;//v2.3 fix
			if (product.approxAvgWgt > 0.0) {

				if (weightCheckBox.isSelected() == true)
					// if (_weightCheckBox.isSelected() == true)
					return;

				weightCheckBox
						.setBackgroundResource(R.drawable.product_checked_box);
				weightCheckBox.setSelected(true);
				quantityCheckBox
						.setBackgroundResource(R.drawable.product_unchecked_box);
				quantityCheckBox.setSelected(false);

			}
			_quantityTextViewContent.writeText("Lbs");
			_quantityValueText.writeText(String
					.format("%.2f", _productQuantity));

		} catch (Exception e) {
			e.printStackTrace();
		}

		setTotalPriceText();

	}

	private void setTotalPriceText() {

		try {
			float unitPrice;

			if (product.promoType > 0)
				unitPrice = product.promoPrice / product.promoForQty;
			else
				unitPrice = product.regPrice;

			if (weightCheckBox != null && weightCheckBox.isSelected() == false
					&& product.approxAvgWgt > 0)
				// if (_weightCheckBox != null
				// && _weightCheckBox.isSelected() == false
				// && product.approxAvgWgt > 0)
				unitPrice *= product.approxAvgWgt;

			_totalPriceValue.writeText("$"
					+ String.format("%.2f", unitPrice * _productQuantity));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		_app.resetScreenHeight(this);
		_app.adjustScreenHeight(this);
		overridePendingTransition(com.raleys.libandroid.R.anim.slide_in_left,
				com.raleys.libandroid.R.anim.slide_out_right);
	}

	public void updateShoppingList(int updateType, Product product) {

		int _productUpdateType = updateType;
		if (product == null) // should be null when the close button in the
								// detail view is use
			return;

		ShoppingList list = null;
		if (_currentPage.equals("productpage")) {
			list = _app.getActiveShoppingList();
			String listid = _app.getActiveListId();
			_app.setCurrentListId(listid);
		} else if (_currentPage.equals("listpage")) {
			list = _app._currentShoppingList;
		}

		if (list == null) {
			showTextDialog(this, "List Error", SET_ACTIVE_LIST_TEXT);
			return;
		}
		String currentListid = _app.getCurrentListId();
		if (_productUpdateType == Product.PRODUCT_ADD
				|| _productUpdateType == Product.PRODUCT_MODIFY) {
			Login _login = _app.getLogin();
			_productToAdd = product; // product gets added later if the
										// service
			// is successful and the list is not
			// returned because it is current
			ListAddItemRequest request = new ListAddItemRequest();
			request.accountId = _login.accountId;
			request.listId = currentListid;
			request.sku = product.sku;
			request.qty = product.qty;
			request.customerComment = product.customerComment;
			request.appListUpdateTime = list.serverUpdateTime;

			if (product.weight > 0) {
				request.purchaseBy = "W";
				request.weight = product.weight;
			} else {
				request.purchaseBy = "E";
				request.qty = product.qty;
			}

			String requestBody = _gson.toJson(request);
			if (!Utils.isNetworkAvailable(this)) {
				showNetworkUnavailableDialog(this);
				return;
			}

			showProgressDialog("Updating your list...");
			_service = new WebService(this, ShoppingList.class, requestBody,
					_login.authKey);
			_service.execute(_app.LIST_ADD_ITEM_URL);
			// handled by handleListServiceResponse() below
		}

	}

	@Override
	public void onServiceResponse(Object responseObject) {
		if (responseObject instanceof ShoppingList) {
			handleListServiceResponse();
		}
	}

	private void handleListServiceResponse() {
		try {

			dismissActiveDialog();
			int status = _service.getHttpStatusCode();
			if (status == 200) {
				if (_productToAdd != null) {

					ShoppingList response = (ShoppingList) _service
							.getResponseObject();

					if (response == null) {
						showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
						// showTextDialog(this, "Server Error",
						// "Unable to parse data returned from server.");
						return;
					}

					// choose list
					ShoppingList list;
					boolean activelist = false;
					if (_app.getActiveListId()
							.equalsIgnoreCase(response.listId)) {
						list = _app.getActiveShoppingList();
						activelist = true;
					} else {
						list = _app._currentShoppingList;
					}
					if (list != null) {
						boolean productAlreadyExist = false;
						for (int i = 0; i < list.productList.size(); i++) {
							Product product = list.productList.get(i);

							if (product.upc.compareTo(_productToAdd.upc) == 0) {
								list.productList.remove(i);
								list.productList.add(_productToAdd);

								productAlreadyExist = true;

								// update current list also if current list is
								// active
								if (_currentPage.equals("listpage")
										&& activelist) {
									_app._currentShoppingList.productList
											.remove(i);
									_app._currentShoppingList.productList
											.add(_productToAdd);
								}
								break;
							}
						}
						if (productAlreadyExist == false) {
							list.productList.add(_productToAdd);
						}
						// update active list
						if (_app.getActiveListId().equalsIgnoreCase(
								response.listId)) {
							_app.setActiveShoppingList(list);
						}
						//
						JSONObject data = new JSONObject();
						data.put("SKU", _productToAdd.sku);
						data.put("item_id", _productToAdd.upc);
						data.put("brand", _productToAdd.brand);
						data.put("item_name", _productToAdd.description);

						data.put("price",
								String.valueOf(_productToAdd.regPrice));
						data.put("promo_price",
								String.valueOf(_productToAdd.promoPrice));
						data.put("category", _productToAdd.mainCategory);
						data.put("quantity", String.valueOf(_productQuantity));

						if (productAlreadyExist) {
							// CLP SDK Product Quantity Modified
							data.put("event_name", "ProductQuantityModified");
							data.put("event_time",
									_app.clpsdkObj.formatedCurrentDate());
							_app.clpsdkObj.updateAppEvent(data);
						} else {
							// CLP SDK Product Added
							data.put("event_name", "ProductAdded");
							data.put("event_time",
									_app.clpsdkObj.formatedCurrentDate());
							_app.clpsdkObj.updateAppEvent(data);
						} //
							// STOP

					}
					showTickImage(context);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							dismissCurrentActivity();
						}
					}, TickDialog.animationDelay);

					if (_currentPage.equals("listpage")) {
						((ShoppingScreen) _app._shoppingScreenContext)
								.refreshCurrentShoppingList();
					}
					return;

				}

			} else {
				showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openIncredientsPopUp(String ingredients) {

		try {

			// custom dialog
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			int dialog_height = _contentViewHeight / 2;
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = dialog_height;
			lp.gravity = Gravity.BOTTOM;
			dialog.getWindow().setAttributes(lp);

			RelativeLayout _mainDialog = new RelativeLayout(this);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = 0;
			layoutParams.height = dialog_height;
			layoutParams.width = _contentViewWidth;
			dialog.addContentView(_mainDialog, layoutParams);

			int closeImgaeSize = (int) (dialog_height * 0.12);
			int PaddingSize = (int) (dialog_height * 0.01);

			RelativeLayout topLayout = new RelativeLayout(this);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = 0;
			layoutParams.height = closeImgaeSize;
			layoutParams.width = _contentViewWidth;
			_mainDialog.addView(topLayout, layoutParams);

			Button closeButton = new Button(this);
			closeButton.setBackgroundResource(R.drawable.close_picker_button);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = PaddingSize;
			layoutParams.topMargin = PaddingSize;
			layoutParams.height = closeImgaeSize;
			layoutParams.width = closeImgaeSize;
			topLayout.addView(closeButton, layoutParams);

			TextView textHeading = new TextView(this);
			textHeading.setText("Ingredients"); // set text in text view
			textHeading.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (dialog_height * .085)); // (float) (dialog_height *
													// .04)
			// textHeading.setTextSize(22); // (float) (dialog_height * .05)
			textHeading.setTypeface(_boldFont);
			textHeading.setTextColor(Color.rgb(187, 0, 0));
			textHeading.setGravity(Gravity.CENTER);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = closeImgaeSize;
			layoutParams.topMargin = 0;
			layoutParams.height = closeImgaeSize;
			layoutParams.width = _contentViewWidth - closeImgaeSize;
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
					_mainDialog.getId());
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_mainDialog.getId());
			topLayout.addView(textHeading, layoutParams);

			SmartScrollView scroll = new SmartScrollView(this);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = closeImgaeSize + PaddingSize;
			layoutParams.height = dialog_height - closeImgaeSize;
			layoutParams.width = _contentViewWidth;
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_mainDialog.getId());
			scroll.setSmoothScrollingEnabled(true);
			scroll.setHorizontalScrollBarEnabled(false);
			scroll.setVerticalScrollBarEnabled(false);
			_mainDialog.addView(scroll, layoutParams);

			TextView textIncredients = new TextView(this);
			textIncredients.setText(ingredients); // set text in text view
			textIncredients.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (dialog_height * .055)); // (float) (dialog_height *
													// .04)
			textIncredients.setSingleLine(false);
			textIncredients.setTypeface(_normalFont);
			textIncredients.setTextColor(Color.DKGRAY);
			layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			textIncredients.setPadding(closeImgaeSize / 2, 0, closeImgaeSize,
					closeImgaeSize / 2);
			scroll.addView(textIncredients, layoutParams);
			_mainDialog.setBackgroundColor(Color.WHITE);

			dialog.show();

			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialogdismiss) {
					dialogOpen = false;
					dialogdismiss.dismiss();
				}
			});

			closeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogOpen = false;
					dialog.dismiss();
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void dismissCurrentActivity() {
		this.finish();
	}

	@Override
	public void onBackPressed() {
		checkChanges();
		// do nothing, make the user hit the home button to background the app
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		canvas.drawRect(
				new RectF(0, cornerSize, bitmap.getWidth(), bitmap.getHeight()),
				paint);
		paint.setAntiAlias(true);
		canvas.drawRoundRect(rectF, cornerSize, cornerSize, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rectF, paint);

		return output;
	}

}
