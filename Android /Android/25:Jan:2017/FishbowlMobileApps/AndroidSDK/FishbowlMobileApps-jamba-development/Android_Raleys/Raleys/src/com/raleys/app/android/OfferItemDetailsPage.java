package com.raleys.app.android;

import java.sql.Date;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Offer;
import com.raleys.app.android.models.OfferAcceptRequest;
import com.raleys.libandroid.SizedImageButton;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SizedTextView;
import com.raleys.libandroid.SmartScrollView;
import com.raleys.libandroid.SmartTextView;
import com.raleys.libandroid.Utils;
import com.raleys.libandroid.WebService;
import com.raleys.libandroid.WebServiceError;
import com.raleys.libandroid.WebServiceListener;

public class OfferItemDetailsPage extends BaseScreen implements
		WebServiceListener {

	private RaleysApplication _app;
	private RelativeLayout _currentProductLayout;
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
	protected SizedImageView _headerImageView;
	protected SizedImageButton _backButton;

	String _offerTitle, _offerConsumerText, _endDate, _offer_ID, _offerLimit;
	Bitmap _offerBitmap;
	Bitmap _offerNewBitmap;
	ImageView _alwaysAvailableImageView;
	TextView _acceptOffer;
	Offer _currentOfferData;
	WebService _service;
	Gson _gson;

	@SuppressLint("NewApi")
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(com.raleys.libandroid.R.anim.slide_in_right,
				com.raleys.libandroid.R.anim.slide_out_left);

		try {

			context = this;
			_app = (RaleysApplication) context.getApplicationContext();

			_CreateAccountButton.setVisibility(View.GONE); // Hiding Create
															// button
			_gson = new GsonBuilder().disableHtmlEscaping().create();
			_alwaysAvailableImageView = new ImageView(context);
			Intent new_intent = getIntent();
			_currentOfferData = (Offer) new_intent.getExtras().getSerializable(
					"offerdatas");

			_offer_ID = _currentOfferData.offerId;
			_offerTitle = _currentOfferData.consumerTitle;
			_offerConsumerText = _currentOfferData.consumerDesc;
			_offerLimit = _currentOfferData.offerLimit;
			String endDate = _currentOfferData.endDate;
			_endDate = "Good thru "
					+ DateFormat.format("M/d/yy",
							new Date(Long.parseLong(endDate))).toString();
			_offerBitmap = _app
					.getCachedImage(_currentOfferData.offerProductImageFile);

			_screenWidth = _app.getScreenWidth();
			_screenHeight = _app.getScreenHeight();
			_headerHeight = _app.getHeaderHeight();
			_footerHeight = _app.getFooterHeight();
			_headerButtonPad = (int) (_screenWidth * .01);
			_contentViewHeight = _screenHeight - _headerHeight;
			_contentViewWidth = _screenWidth;

			// CLP SDK
			try {
				if (_currentOfferData != null) {

					JSONObject data = new JSONObject();
					if (_currentOfferData.offerId != null)
						data.put("item_id", _currentOfferData.offerId);

					if (_currentOfferData.consumerTitle != null)
						data.put("item_name", _currentOfferData.consumerTitle);

					if (_currentOfferData.promoCode != null)
						data.put("promo_code", _currentOfferData.promoCode);

					if (_currentOfferData.consumerDesc != null)
						data.put("promo_title", _currentOfferData.consumerDesc);

					data.put("price",
							String.valueOf(_currentOfferData.offerPrice));

					if (_currentOfferData.acceptGroup != null)
						data.put("accept_group", _currentOfferData.acceptGroup);

					if (_currentOfferData.startDate != null)
						data.put("start_date", _currentOfferData.startDate);

					if (_currentOfferData.endDate != null)
						data.put("end_date", _currentOfferData.endDate);

					data.put("event_name", "OpenOffer");
					data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
					_app.clpsdkObj.updateAppEvent(data);

					// CLP SDK Offer open
				}
			} catch (Exception e) {
				Log.e(_app.CLP_TRACK_ERROR, "OFFER_OPEN:" + e.getMessage());
			}
			// CLP
			_mainLayout.setBackgroundColor(Color.rgb(225, 225, 225));

			showProgressDialog("");
			Handler _offerPageHanler = new Handler();
			_offerPageHanler.postDelayed(new Runnable() {

				@Override
				public void run() {

					// Scroll View
					int XPadding = (int) (_screenWidth * .025);
					SmartScrollView scroll = new SmartScrollView(context);
					RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					scrollParams.topMargin = _headerHeight;
					scrollParams.leftMargin = 0;
					scrollParams.width = _contentViewWidth;
					scrollParams.height = _contentViewHeight;
					scroll.setSmoothScrollingEnabled(true);
					scroll.setScrollbarFadingEnabled(true);
					_mainLayout.addView(scroll, scrollParams);

					// Scroll View end

					RelativeLayout newLayout = new RelativeLayout(context);
					RelativeLayout.LayoutParams newLayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					newLayoutParams.topMargin = _headerHeight;
					newLayoutParams.leftMargin = 0;
					newLayoutParams.width = _contentViewWidth;
					newLayoutParams.height = _contentViewHeight;
					scroll.addView(newLayout, newLayoutParams);

					float[] CornerRadius;
					int cornerSize = (int) (XPadding * .75);
					CornerRadius = new float[] { cornerSize, cornerSize,
							cornerSize, cornerSize, cornerSize, cornerSize,
							cornerSize, cornerSize };
					// Dynamic layout creation Part
					RoundRectShape bottom_rect = new RoundRectShape(
							CornerRadius, new RectF(0, 0, 0, 0), new float[] {
									0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable top_layer = new CustomShapeDrawable(
							bottom_rect, Color.TRANSPARENT, Color.WHITE, 0);
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
					_contentlayoutParams.width = _contentViewWidth;
					if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_currentProductLayout
								.setBackgroundDrawable(_layoutBackground);
					} else {
						_currentProductLayout.setBackground(_layoutBackground);
					}
					newLayout.addView(_currentProductLayout,
							_contentlayoutParams);
					// Content View Layout end

					// Image View Of Product
					int _offerImageSize = (int) (_contentViewWidth * .65);

					if (_offerBitmap != null)
						_offerNewBitmap = Bitmap.createScaledBitmap(
								_offerBitmap, _offerImageSize, _offerImageSize,
								true);
					else
						_offerNewBitmap = _app.getAppBitmap(
								"product_detail_shopping_bag",
								R.drawable.shopping_bag, _offerImageSize,
								_offerImageSize);

					// _offerNewBitmap = Bitmap.createScaledBitmap(_offerBitmap,
					// _offerImageSize, _offerImageSize, true);

					SizedImageView _offerImageView = new SizedImageView(
							context, _offerImageSize, _offerImageSize);
					// ImageView _offerImageView = new ImageView(context);
					_offerImageView.setImageBitmap(_offerNewBitmap);
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = XPadding * 2;
					layoutParams.leftMargin = 0;
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_currentProductLayout.getId());
					_currentProductLayout
							.addView(_offerImageView, layoutParams);

					int textWidth = (int) (_contentViewWidth * .8);
					int textHeight = (int) (_contentViewHeight * .065);

					int _secondLayoutMargin = (int) (_offerImageSize + _offerImageSize * .05)
							+ (XPadding * 2);
					SmartTextView _consumerTitleTextView = new SmartTextView(
							context, textWidth, textHeight, 2,
							Color.TRANSPARENT, Color.BLACK, _normalFont,
							Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = XPadding;
					layoutParams.topMargin = _secondLayoutMargin;

					_currentProductLayout.addView(_consumerTitleTextView,
							layoutParams);

					int _thirdLayoutMargin = (int) (_offerImageSize + _offerImageSize * .15)
							+ (XPadding * 2);
					SmartTextView _consumerDescTextView = new SmartTextView(
							context, textWidth, (int) (textHeight * 2.5), 5,
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = XPadding;
					layoutParams.topMargin = _thirdLayoutMargin + XPadding;
					_currentProductLayout.addView(_consumerDescTextView,
							layoutParams);

					// Missed this offerLimit. Newly added
					float _thirdSublayoutMarginTop = 0;
					if (_currentOfferData.offerLimit != null
							&& _currentOfferData.offerLimit.length() > 0) {
						int _thirdSubLayoutMargin = (int) (_offerImageSize + _offerImageSize * .6)
								+ (XPadding * 2);
						_thirdSublayoutMarginTop = 0.2f;
						SmartTextView _consumerOfferLimit = new SmartTextView(
								context, textWidth, (int) (textHeight * .5), 1,
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = XPadding;
						layoutParams.topMargin = _thirdSubLayoutMargin
								+ XPadding;
						_currentProductLayout.addView(_consumerOfferLimit,
								layoutParams);
						_consumerOfferLimit.writeText(_offerLimit);

					}

					int _fourthLayoutMargin = (int) (_offerImageSize + _offerImageSize
							* (.6 + _thirdSublayoutMarginTop))
							+ (XPadding * 2);
					SmartTextView _endDateTextView = new SmartTextView(context,
							textWidth, (int) (textHeight * .5), 1,
							Color.TRANSPARENT, Color.DKGRAY, _normalFont,
							Align.LEFT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = XPadding;
					layoutParams.topMargin = _fourthLayoutMargin;
					_currentProductLayout.addView(_endDateTextView,
							layoutParams);

					int _bottomImageHeight = (int) (_contentViewHeight * .063);
					int _bottomImageWidth = (int) (_contentViewWidth * .6);

					// Bottom Image
					int _fifthLayoutMargin = (int) (_offerImageSize + (_offerImageSize * (.8 + _thirdSublayoutMarginTop)))
							+ (XPadding * 2);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = _fifthLayoutMargin;
					layoutParams.leftMargin = XPadding;
					layoutParams.rightMargin = XPadding;
					layoutParams.bottomMargin = XPadding * 2;
					layoutParams.width = _bottomImageWidth;
					layoutParams.height = _bottomImageHeight;
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_currentProductLayout.getId());
					_currentProductLayout.addView(_alwaysAvailableImageView,
							layoutParams);

					RoundRectShape buttonRect = new RoundRectShape(
							CornerRadius, new RectF(0, 0, 0, 0), new float[] {
									0, 0, 0, 0, 0, 0, 0, 0 });
					ShapeDrawable buttonRectLayer = new CustomShapeDrawable(
							buttonRect, Color.rgb(145, 145, 145), Color.rgb(
									187, 0, 0), 0);
					LayerDrawable buttonLD = new LayerDrawable(
							new Drawable[] { buttonRectLayer });
					buttonLD.setLayerInset(0, 0, 0, 0, 0);

					_acceptOffer = new TextView(context);
					_acceptOffer.setText("Accept This Offer");
					_acceptOffer.setTextColor(Color.WHITE);
					_acceptOffer.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							(int) (_bottomImageHeight * .5));
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin = _fifthLayoutMargin;
					layoutParams.leftMargin = XPadding;
					layoutParams.rightMargin = XPadding;
					layoutParams.bottomMargin = XPadding * 2;
					layoutParams.width = _bottomImageWidth;
					layoutParams.height = _bottomImageHeight;
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
							_currentProductLayout.getId());
					_acceptOffer.setGravity(Gravity.CENTER);
					if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						_acceptOffer.setBackgroundDrawable(buttonLD);
					} else {
						_acceptOffer.setBackground(buttonLD);
					}
					_currentProductLayout.addView(_acceptOffer, layoutParams);

					_consumerTitleTextView.writeText(_offerTitle);
					_consumerDescTextView.writeText(_offerConsumerText);
					_endDateTextView.writeText(_endDate);

					if (_currentOfferData._acceptedOffer == true) {
						_acceptOffer.setVisibility(View.GONE);
						_alwaysAvailableImageView.setVisibility(View.VISIBLE);
						_alwaysAvailableImageView
								.setBackgroundResource(R.drawable.offer_accepted);
					} else if (_currentOfferData._acceptableOffer == true) {
						_alwaysAvailableImageView.setVisibility(View.GONE);
						_acceptOffer.setVisibility(View.VISIBLE);

						_acceptOffer.setOnTouchListener(new OnTouchListener() {
							long downTime;
							long upTime;

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								int action = event.getAction();

								if (action == MotionEvent.ACTION_DOWN) {
									downTime = event.getEventTime();
									_acceptOffer.setAlpha(.5f);
									return true;
								} else if (action == MotionEvent.ACTION_UP
										|| action == MotionEvent.ACTION_CANCEL) {
									_acceptOffer.setAlpha(1.0f);
									upTime = event.getEventTime();

									// if (upTime - downTime > 150) {
									if (upTime - downTime > 0) {
										try {
											acceptOffer(_currentOfferData);
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}

									return true;
								} else {
									return false;
								}
							}
						});
					} else if (_currentOfferData._acceptableOffer == false) {
						_acceptOffer.setVisibility(View.GONE);
						_alwaysAvailableImageView.setVisibility(View.VISIBLE);
						_alwaysAvailableImageView
								.setBackgroundResource(R.drawable.offer_always_available);
					}
					dismissActiveDialog();
					changeFlag();
				}
			}, 2000);
			setContentView(_mainLayout);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void changeFlag() {
		// Multiple open issue
		_app._offerItemsActivityopen = false;
	}

	@Override
	public void onBackPressed() {
		finish();
		// do nothing, make the user hit the home button to background the app
	}

	public void acceptOffer(Offer offer) {
		Login login = _app.getLogin();
		OfferAcceptRequest request = new OfferAcceptRequest();
		request.crmNumber = login.crmNumber;
		request.acceptGroup = offer.acceptGroup;
		// new for accepting push offer
		request.offerId = offer.offerId;
		if (offer.endDate != null) {
			request.endDate = offer.endDate;
		}
		if (offer.consumerTitle != null) {
			request.title = offer.consumerTitle;
		}
		if (offer.promoCode != null) {
			request.promoCode = offer.promoCode;
		}
		//
		String requestBody = _gson.toJson(request);

		if (!Utils.isNetworkAvailable(this)) {
			showNetworkUnavailableDialog(this);
			return;
		}

		// CLP SDK Offer accept
		try {
			if (offer != null && offer.offerId != null
					&& offer.consumerTitle != null && offer.promoCode != null
					&& offer.consumerDesc != null && offer.acceptGroup != null
					&& offer.startDate != null && offer.endDate != null) {

				JSONObject data = new JSONObject();
				if (offer.offerId != null)
					data.put("item_id", offer.offerId);
				if (offer.consumerTitle != null)
					data.put("item_name", offer.consumerTitle);
				if (offer.promoCode != null)
					data.put("promo_code", offer.promoCode);
				if (offer.consumerDesc != null)
					data.put("promo_title", offer.consumerDesc);

				data.put("price", String.valueOf(offer.offerPrice));
				if (offer.acceptGroup != null)
					data.put("accept_group", offer.acceptGroup);
				if (offer.startDate != null)
					data.put("start_date", offer.startDate);
				if (offer.endDate != null)
					data.put("end_date", offer.endDate);

				data.put("event_name", "AcceptOffer");
				data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
				_app.clpsdkObj.updateAppEvent(data);
			}
		} catch (Exception e) {
			Log.e(_app.CLP_TRACK_ERROR, "OFFER_OPEN:" + e.getMessage());
		}
		// CLP SDK

		showProgressDialog("Sending accept request...");
		_service = new WebService(this, OfferAcceptRequest.class, requestBody,
				login.authKey);
		_service.execute(_app.ACCEPT_OFFER_URL);
		// handled by handleOfferAcceptServiceResponse() directly below
	}

	public void handleOfferAcceptServiceResponse() {
		try {
			dismissActiveDialog();
			int status = _service.getHttpStatusCode();

			if (status == 200) {
				OfferAcceptRequest response = (OfferAcceptRequest) _service
						.getResponseObject();

				if (response == null || response.acceptGroup == null) {
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
					// showTextDialog(this, "Server Error",
					// "Unable to parse data returned from server.");
					return;
				}

				for (int i = 0; i < _app._personalizedOffersList.size(); i++) {
					Offer offer = _app._personalizedOffersList.get(i);

					if (offer.acceptGroup.compareTo(response.acceptGroup) == 0) {
						_app._acceptedOffersList.add(offer);
						_app._personalizedOffersList.remove(i);
						break;
					}
				}

				for (int i = 0; i < _app._extraFriendzyOffersList.size(); i++) {
					Offer offer = _app._extraFriendzyOffersList.get(i);

					if (offer.acceptGroup.compareTo(response.acceptGroup) == 0) {
						_app._acceptedOffersList.add(offer);
						_app._extraFriendzyOffersList.remove(i);
						break;
					}
				}
				// show tick mark for success
				showTickImage(context);
				finish();
			} else {
				WebServiceError error = _service.getError();

				if (status == 422) // backend or http error
					showTextDialog(this, "Accept Offer Failed",
							error.errorMessage);
				else
					showTextDialog(this, "", Raleys.COMMON_ERROR_MSG);
				// showTextDialog(this, "Server Error", "Http Status code: "
				// + status);

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onServiceResponse(Object responseObject) {
		// TODO Auto-generated method stub
		if (responseObject instanceof OfferAcceptRequest) {
			handleOfferAcceptServiceResponse();
		}
	}
}
