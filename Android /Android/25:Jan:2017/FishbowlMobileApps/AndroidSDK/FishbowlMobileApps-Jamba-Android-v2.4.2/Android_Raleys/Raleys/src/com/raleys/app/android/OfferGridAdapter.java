package com.raleys.app.android;

import java.sql.Date;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raleys.app.android.models.Offer;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SmartTextView;
import com.raleys.libandroid.Utils;

public class OfferGridAdapter extends BaseAdapter {

	public RaleysApplication _app;
	public Context _context;
	public Object _handler;
	public String _callback;
	public int _width;
	public int _headerWidth;
	public int _headerHeight;
	public int _cellHeight;
	public int _columns;
	public int _cellSpacing;
	public int _cellWidth;
	public int _textXOrigin;
	public int _textWidth;
	public int _acceptTextHeight;
	public int _acceptTextFontSize;
	public LayoutInflater _inflater;
	public ArrayList<ArrayList<Offer>> _rows;
	public Typeface _normalFont;
	public Drawable _containerButton;

	// Multiple open
	Runnable runnable;
	Handler _newHandler;
	Bitmap globalOfferBitmap;
	Offer globalOffer;

	@SuppressWarnings("deprecation")
	public OfferGridAdapter(Context context, ArrayList<ArrayList<Offer>> rows,
			int columns, int width, int headerHeight, int cellHeight,
			String callback) {
		_app = (RaleysApplication) context.getApplicationContext();
		_context = context;
		_handler = context;
		_callback = callback;
		_rows = rows;
		_columns = columns;
		_width = width;
		_headerHeight = headerHeight;
		_headerWidth = _width;
		_cellHeight = cellHeight;
		_cellSpacing = (int) (_width * .02);
		_cellWidth = (_width - ((_columns + 1) * _cellSpacing)) / _columns;
		_textXOrigin = (int) (_cellWidth * .04);
		_textWidth = _cellWidth - (_cellSpacing * 2);
		_acceptTextHeight = (int) (_cellHeight * .11);
		_acceptTextFontSize = Utils.getFontSizeByHeight(
				(int) (_acceptTextHeight * .5), _normalFont);
		_inflater = LayoutInflater.from(context);
		_normalFont = _app.getNormalFont();
		_containerButton = new BitmapDrawable(_app.getAppBitmap(
				"offer_container_button", R.drawable.offer_container_button,
				_textWidth, _acceptTextHeight));

		// heavily used bitmaps should only be scaled once and stored in the
		// app...
		if (_app._offerGridCellBitmap == null)
			_app._offerGridCellBitmap = _app.getAppBitmap("offer_grid_cell",
					R.drawable.product_cell, _cellWidth, cellHeight);

		if (_app._offerGridHeaderBitmap == null)
			_app._offerGridHeaderBitmap = _app.getAppBitmap(
					"offer_grid_header", R.drawable.section_header,
					_headerWidth, _headerHeight);

		_newHandler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				try {
					_handler.getClass().getMethod("dismiss_offermenu")
							.invoke(_handler);
				} catch (Exception e) {
					Log.e("Error", "dismiss_offermenu");
				}
				Intent intent = null;
				if (_app._offerItemsActivityopen == true) {
					return;
				} else if (_app._offerItemsActivityopen == false) {
					_app._offerItemsActivityopen = true;
					intent = new Intent(_context, OfferItemDetailsPage.class);
					intent.putExtra("offerdatas", globalOffer);
				}
				_context.startActivity(intent);

			}
		};
	}

	@Override
	public int getCount() {
		return _rows.size();
	}

	@Override
	public Object getItem(int position) {
		return _rows.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		ArrayList<Offer> currentRow = _rows.get(position);

		if (currentRow.get(0).consumerTitle
				.equalsIgnoreCase("CONTENT_VIEW_BLANK"))
			return 0;
		else if (currentRow.get(0).consumerTitle
				.equalsIgnoreCase("GRID_HEADER"))
			return 1;
		else
			return 2;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ArrayList<Offer> offers = _rows.get(position);
		BlankRow blankRow = null;
		HeaderRow headerRow = null;
		OfferRow offerRow = null;
		RelativeLayout.LayoutParams layoutParams = null;

		try {
			if (convertView == null) {
				convertView = _inflater.inflate(R.layout.convert_view_layout,
						null);
				convertView.setBackgroundColor(Color.TRANSPARENT);

				if (getItemViewType(position) == 0) // BlankRow
				{
					blankRow = new BlankRow();

					blankRow._layout = (RelativeLayout) convertView
							.findViewById(R.id.holder_layout);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.height = _app.getDeviceType() == Utils.DEVICE_PHONE ? (int) (_headerHeight * .75)
							: (int) (_headerHeight * .90);
					layoutParams.width = _width;
					blankRow._layout.setLayoutParams(layoutParams);
					convertView.setTag(blankRow);

					blankRow._background = new SizedImageView(_context,
							_headerWidth, _headerHeight);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = (_width - _headerWidth) / 2;
					layoutParams.topMargin = 0;
					blankRow._layout
							.addView(blankRow._background, layoutParams);
				} else if (getItemViewType(position) == 1) // HeaderRow
				{
					headerRow = new HeaderRow();
					headerRow._layout = (RelativeLayout) convertView
							.findViewById(R.id.holder_layout);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.height = _headerHeight + _cellSpacing;
					layoutParams.width = _width;
					headerRow._layout.setLayoutParams(layoutParams);
					convertView.setTag(headerRow);

					headerRow._background = new SizedImageView(_context,
							_headerWidth, _headerHeight);
					headerRow._background
							.setImageBitmap(_app._offerGridHeaderBitmap);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 0;
					layoutParams.topMargin = 0;
					headerRow._layout.addView(headerRow._background,
							layoutParams);

					int textWidth = (int) (_headerWidth * .9);
					headerRow._headerTextView = new SmartTextView(_context,
							textWidth, (int) (_headerHeight * .50), 1,
							Color.TRANSPARENT, Color.BLACK, _normalFont);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = (_width - textWidth) / 2;
					layoutParams.topMargin = (int) (_headerHeight * .2);
					headerRow._layout.addView(headerRow._headerTextView,
							layoutParams);

					headerRow._seperatorLineView = new View(_context);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 0;
					layoutParams.topMargin = _headerHeight;
					layoutParams.height = (int) (_cellSpacing * .1);
					layoutParams.width = _headerWidth;
					headerRow._seperatorLineView
							.setBackgroundColor(Color.DKGRAY);
					headerRow._layout.addView(headerRow._seperatorLineView,
							layoutParams);

					// Space View
					headerRow._spaceView = new View(_context);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 0;
					layoutParams.topMargin = _headerHeight;
					layoutParams.height = _cellSpacing;
					layoutParams.width = _headerWidth;
					headerRow._layout.addView(headerRow._spaceView,
							layoutParams);

				} else // OfferRow
				{
					offerRow = new OfferRow();

					offerRow._layout = (RelativeLayout) convertView
							.findViewById(R.id.holder_layout);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.height = _cellHeight;
					layoutParams.width = _width;
					offerRow._layout.setLayoutParams(layoutParams);
					convertView.setTag(offerRow);

					offerRow._cells = new OfferCell[_columns];

					for (int i = 0; i < _columns; i++) {

						offerRow._cells[i] = new OfferCell();

						int cornerSize = _cellSpacing;
						float[] cornerRadius = new float[] { cornerSize,
								cornerSize, cornerSize, cornerSize, cornerSize,
								cornerSize, cornerSize, cornerSize };

						RoundRectShape rect = new RoundRectShape(cornerRadius,
								new RectF(0, 0, 0, 0), new float[] { 0, 0, 0,
										0, 0, 0, 0, 0 });

						RoundRectShape bottom_rect = new RoundRectShape(
								cornerRadius, new RectF(0, 0, 0, 0),
								new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });

						ShapeDrawable bottom_layer = new CustomShapeDrawable(
								bottom_rect, Color.rgb(145, 145, 145),
								Color.rgb(145, 145, 145), 0);

						ShapeDrawable top_layer = new CustomShapeDrawable(rect,
								Color.DKGRAY, Color.WHITE, 0);

						LayerDrawable ld = new LayerDrawable(new Drawable[] {
								bottom_layer, top_layer });

						ld.setLayerInset(0, 0, (int) 1.0f, 0, 0);
						ld.setLayerInset(1, 0, 0, 0, (int) 2.0f);

						offerRow._cells[i]._layout = new RelativeLayout(
								_context);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = (_cellSpacing * (i + 1))
								+ (_cellWidth * i);
						layoutParams.topMargin = 0;
						layoutParams.bottomMargin = _cellSpacing;
						layoutParams.width = _cellWidth;
						layoutParams.height = _cellHeight;

						if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
							offerRow._cells[i]._layout
									.setBackgroundDrawable(ld);
						} else {
							offerRow._cells[i]._layout.setBackground(ld);
						}

						offerRow._cells[i]._layout
								.setLayoutParams(layoutParams);

						offerRow._cells[i]._offerImageView = new SizedImageView(
								_context, _cellWidth, _cellWidth);
						// offerRow._cells[i]._offerImageView
						// .setScaleType(SizedImageView.ScaleType.MATRIX);
						// offerRow._cells[i]._offerImageView
						// .setAdjustViewBounds(true);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = 0;
						layoutParams.topMargin = 0;
						layoutParams.rightMargin = 0;
						layoutParams.width = _cellWidth;

						layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
								offerRow._cells[i]._layout.getId());
						offerRow._cells[i]._layout.addView(
								offerRow._cells[i]._offerImageView,
								layoutParams);

						offerRow._cells[i]._consumerTitleTextView = new SmartTextView(
								_context, _textWidth,
								(int) (_cellHeight * .10), 2,
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = _cellSpacing;
						layoutParams.topMargin = (int) (_cellHeight * .625);
						offerRow._cells[i]._layout.addView(
								offerRow._cells[i]._consumerTitleTextView,
								layoutParams);

						offerRow._cells[i]._consumerDescTextView = new SmartTextView(
								_context, _textWidth,
								(int) (_cellHeight * .225), 3,
								Color.TRANSPARENT, Color.DKGRAY, _normalFont);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = _textXOrigin;
						layoutParams.topMargin = (int) (_cellHeight * .50);
						// offerRow._cells[i]._layout.addView(
						// offerRow._cells[i]._consumerDescTextView,
						// layoutParams);

						offerRow._cells[i]._offerLimitTextView = new SmartTextView(
								_context, _textWidth,
								(int) (_cellHeight * .005), 1,
								Color.TRANSPARENT, Color.GRAY, _normalFont);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = _textXOrigin;
						layoutParams.topMargin = (int) (_cellHeight * .74);
						// offerRow._cells[i]._layout.addView(
						// offerRow._cells[i]._offerLimitTextView,
						// layoutParams);

						offerRow._cells[i]._endDateTextView = new SmartTextView(
								_context, _textWidth,
								(int) (_cellHeight * .05), 1,
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = _cellSpacing;
						layoutParams.topMargin = (int) (_cellHeight * .75);
						offerRow._cells[i]._layout.addView(
								offerRow._cells[i]._endDateTextView,
								layoutParams);

						// Always Available Image start

						int _bottomImageWidtht = _cellWidth
								- (_cellSpacing * 4);
						int _bottomImageHeight = (int) (_cellHeight * .082);

						offerRow._cells[i]._availableImageLayout = new RelativeLayout(
								_context);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = (int) (_cellHeight * .85);
						layoutParams.leftMargin = _cellSpacing * 2;
						layoutParams.rightMargin = _cellSpacing * 2;
						layoutParams.width = _bottomImageWidtht;
						layoutParams.height = _bottomImageHeight;
						offerRow._cells[i]._layout.addView(
								offerRow._cells[i]._availableImageLayout,
								layoutParams);

						// offerRow._cells[i]._acceptThisOffer = new Button(
						// _context);
						// layoutParams = new RelativeLayout.LayoutParams(
						// RelativeLayout.LayoutParams.WRAP_CONTENT,
						// RelativeLayout.LayoutParams.WRAP_CONTENT);
						// layoutParams.topMargin = (int) (_cellHeight * .85);
						// layoutParams.leftMargin = _cellSpacing * 2;
						// layoutParams.rightMargin = _cellSpacing * 2;
						// layoutParams.width = _bottomImageWidtht;
						// layoutParams.height = _bottomImageHeight;
						// offerRow._cells[i]._layout.addView(
						// offerRow._cells[i]._acceptThisOffer,
						// layoutParams);

						// Always Available Image end
						RoundRectShape buttonRect = new RoundRectShape(
								cornerRadius, new RectF(0, 0, 0, 0),
								new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
						ShapeDrawable buttonRectLayer = new CustomShapeDrawable(
								buttonRect, Color.rgb(145, 145, 145),
								Color.rgb(187, 0, 0), 0);
						LayerDrawable buttonLD = new LayerDrawable(
								new Drawable[] { buttonRectLayer });
						buttonLD.setLayerInset(0, 0, 0, 0, 0);

						offerRow._cells[i]._acceptThisOffer = new TextView(
								_context) {
							@Override
							protected void onMeasure(int widthMeasureSpec,
									int heightMeasureSpec) {
								this.setMeasuredDimension(
										(int) (_cellWidth * .8),
										_acceptTextHeight);
							}
						};
						if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
							offerRow._cells[i]._acceptThisOffer
									.setBackgroundDrawable(buttonLD);
						} else {
							offerRow._cells[i]._acceptThisOffer
									.setBackground(buttonLD);
						}
						offerRow._cells[i]._acceptThisOffer
								.setTypeface(_normalFont);
						offerRow._cells[i]._acceptThisOffer
								.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										_acceptTextFontSize);
						offerRow._cells[i]._acceptThisOffer
								.setGravity(Gravity.CENTER);
						offerRow._cells[i]._acceptThisOffer
								.setTextColor(Color.WHITE);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = (int) (_cellHeight * .85);
						layoutParams.leftMargin = _cellSpacing * 2;
						layoutParams.rightMargin = _cellSpacing * 2;
						layoutParams.width = _bottomImageWidtht;
						layoutParams.height = _bottomImageHeight;
						offerRow._cells[i]._layout.addView(
								offerRow._cells[i]._acceptThisOffer,
								layoutParams);

						offerRow._layout.addView(offerRow._cells[i]._layout);

					}
				}
			} else {
				if (getItemViewType(position) == 0) // BlankRow
					blankRow = (BlankRow) convertView.getTag();
				else if (getItemViewType(position) == 1) // HeaderRow
					headerRow = (HeaderRow) convertView.getTag();
				else
					// OfferRow
					offerRow = (OfferRow) convertView.getTag();
			}

			for (int i = 0; i < _columns; i++) {
				if (getItemViewType(position) == 0)
					break;

				if (getItemViewType(position) == 1) {
					headerRow._headerTextView
							.writeText(offers.get(0).consumerDesc);
					break;
				}

				if (i < offers.size()) {

					final Offer offer = offers.get(i);
					Bitmap offerBitmap = _app
							.getCachedImage(offer.offerProductImageFile); // doesn't
																			// need
																			// scaling
																			// as
																			// drawRect()
																			// handles
																			// that
																			// nicely

					if (offerBitmap != null) {
						offerRow._cells[i]._offerImageView
								.setImageBitmap(getRoundedCornerBitmap(offerBitmap));
						offerRow._cells[i]._offerImageView
								.setVisibility(View.VISIBLE);
					} else {

						Bitmap _offerNewBitmap = _app.getAppBitmap(
								"product_detail_shopping_bag",
								R.drawable.shopping_bag, _cellWidth,
								_cellWidth);

						// _offerNewBitmap =
						// Bitmap.createScaledBitmap(_offerBitmap,
						// _offerImageSize, _offerImageSize, true);

						// ImageView _offerImageView = new ImageView(context);
						offerRow._cells[i]._offerImageView
								.setImageBitmap(getRoundedCornerBitmap(_offerNewBitmap));
						offerRow._cells[i]._offerImageView
								.setVisibility(View.VISIBLE);

					}

					// if (Build.VERSION.SDK_INT >=
					// Build.VERSION_CODES.JELLY_BEAN) {
					// offerRow._cells[i]._offerImageView.setBackground(getCustomCornerBg(_cellSpacing,
					// _cellSpacing, _cellSpacing,_cellSpacing, 0, 0, 0, 0));
					// } else {
					// offerRow._cells[i]._offerImageView.setBackgroundDrawable(getCustomCornerBg(_cellSpacing,
					// _cellSpacing, _cellSpacing, _cellSpacing, 0, 0, 0, 0));
					// }

					offerRow._cells[i]._consumerTitleTextView
							.writeText(offer.consumerTitle);

					if (offer.consumerTitle != null
							&& offer.consumerTitle.length() > 0)
						offerRow._cells[i]._consumerDescTextView
								.writeText(offer.consumerDesc);
					else
						offerRow._cells[i]._consumerDescTextView.writeText("");

					if (offer.offerLimit != null
							&& offer.offerLimit.length() > 0)
						offerRow._cells[i]._offerLimitTextView
								.writeText(offer.offerLimit);
					else
						offerRow._cells[i]._offerLimitTextView.writeText("");

					if (offer.endDate != null && offer.endDate.length() > 0) {
						String timeString = "";

						try {
							timeString = "Good thru "
									+ DateFormat.format(
											"M/d/yy",
											new Date(Long
													.parseLong(offer.endDate)))
											.toString();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						offerRow._cells[i]._endDateTextView
								.writeText(timeString);
					} else {
						offerRow._cells[i]._endDateTextView.writeText("");
					}

					if (offer._acceptableOffer == true) {

						offerRow._cells[i]._acceptThisOffer
								.setVisibility(View.VISIBLE);
						offerRow._cells[i]._availableImageLayout
								.setVisibility(View.GONE);
						offerRow._cells[i]._acceptThisOffer
								.setText("Accept This Offer");

						final TextView _buttonlayout = offerRow._cells[i]._acceptThisOffer;
						offerRow._cells[i]._acceptThisOffer
								.setOnTouchListener(new OnTouchListener() {
									long downTime;
									long upTime;

									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										int action = event.getAction();

										if (action == MotionEvent.ACTION_DOWN) {
											downTime = event.getEventTime();
											_buttonlayout.setAlpha(.5f);
											return true;
										} else if (action == MotionEvent.ACTION_UP
												|| action == MotionEvent.ACTION_CANCEL) {
											_buttonlayout.setAlpha(1.0f);
											upTime = event.getEventTime();

//											if (upTime - downTime > 150) {
											if (upTime - downTime > 0) {
												try {
													_handler.getClass()
															.getMethod(
																	_callback,
																	Offer.class)
															.invoke(_handler,
																	offer);
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
					} else if (offer._acceptedOffer == true) {

						offerRow._cells[i]._acceptThisOffer
								.setVisibility(View.GONE);
						offerRow._cells[i]._availableImageLayout
								.setVisibility(View.VISIBLE);

						offerRow._cells[i]._availableImageLayout
								.setBackgroundResource(R.drawable.offer_accepted);

						// offerRow._cells[i]._acceptTextView
						// .setText("Offer Accepted!");
					} else if (offer._acceptableOffer == false) {

						offerRow._cells[i]._acceptThisOffer
								.setVisibility(View.GONE);
						offerRow._cells[i]._availableImageLayout
								.setVisibility(View.VISIBLE);

						offerRow._cells[i]._availableImageLayout
								.setBackgroundResource(R.drawable.offer_always_available);

						// offerRow._cells[i]._acceptTextView
						// .setText("Always Available");
					}

					offerRow._cells[i]._layout.setVisibility(View.VISIBLE);
				} else {
					offerRow._cells[i]._layout.setVisibility(View.GONE);
				}

				if (i < offers.size()) {

					final Offer offer = offers.get(i);
					offerRow._cells[i]._layout
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									try {
										globalOffer = offer;
										_newHandler.removeCallbacks(runnable);
										_newHandler.postDelayed(runnable, 1000);

									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return convertView;
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

	static class BlankRow {
		RelativeLayout _layout;
		SizedImageView _background;
	}

	static class HeaderRow {
		RelativeLayout _layout;
		SizedImageView _background;
		SmartTextView _headerTextView;
		View _spaceView;
		View _seperatorLineView;
	}

	static class OfferRow {
		RelativeLayout _offerMainLayout;
		RelativeLayout _layout;
		OfferCell[] _cells;
	}

	static class OfferCell {
		RelativeLayout _layout;
		SizedImageView _background;
		SizedImageView _offerImageView;
		SmartTextView _consumerTitleTextView;
		SmartTextView _consumerDescTextView;
		SmartTextView _offerLimitTextView;
		SmartTextView _endDateTextView;
		// TextView _acceptTextView;
		RelativeLayout _availableImageLayout;
		TextView _acceptThisOffer;
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		canvas.drawRect(
				new RectF(0, _cellSpacing, bitmap.getWidth(), bitmap
						.getHeight()), paint);
		paint.setAntiAlias(true);
		canvas.drawRoundRect(rectF, (float) (_cellSpacing * .5),
				(float) (_cellSpacing * .5), paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rectF, paint);

		return output;
	}
}
