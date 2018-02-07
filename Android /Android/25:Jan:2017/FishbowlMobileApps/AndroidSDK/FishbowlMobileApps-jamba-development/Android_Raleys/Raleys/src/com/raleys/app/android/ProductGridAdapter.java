package com.raleys.app.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raleys.app.android.models.Product;
import com.raleys.app.android.models.ProductCategory;
import com.raleys.app.android.models.ShoppingList;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SmartTextView;

public class ProductGridAdapter extends BaseAdapter {

	private RaleysApplication _app;
	private Context _context;
	private Object _handler;
	private String _callback;
	private int _width;
	private int _height;
	private int _productImageSize;
	private int _promoImageSize;
	private int _textWidth;
	private int _textXOrigin;
	private ArrayList<Product> _productList;
	private LayoutInflater _inflater;
	private Typeface _normalFont;
	int cornerSize;
	float[] CornerRadius;

	public ProductGridAdapter(Context context, ArrayList<Product> productList,
			ProductCategory _currentProductCategory, int width, int height,
			String callback) {
		_productList = productList;

		_app = (RaleysApplication) context.getApplicationContext();
		_context = context;
		_handler = context;
		_callback = callback;
		_width = width;
		_height = height;
		_productImageSize = _width;
		_promoImageSize = (int) (_height * .15);
		_textXOrigin = (int) (_width * .04);
		_textWidth = _width - (_textXOrigin * 2);
		_context = context;
		_productList = productList;
		_inflater = LayoutInflater.from(context);
		_normalFont = _app.getNormalFont();
		_app._currentProductCategory = _currentProductCategory;

		// heavily used bitmaps should only be scaled once and stored in the
		// app...
		if (_app._productGridCellBitmap == null)
			_app._productGridCellBitmap = _app.getAppBitmap(
					"product_grid_cell", R.drawable.product_cell, _width,
					_height);

		if (_app._productGridPromoBitmap == null)
			_app._productGridPromoBitmap = _app.getAppBitmap(
					"product_grid_promo", R.drawable.sale_tag, _promoImageSize,
					_promoImageSize);

		if (_app._productListDefaultBitmap == null)
			_app._productListDefaultBitmap = _app.getAppBitmap(
					"product_grid_default", R.drawable.shopping_bag,
					_productImageSize, _productImageSize);

		cornerSize = (int) (_width * .05);
		CornerRadius = new float[] { cornerSize, cornerSize, cornerSize,
				cornerSize, cornerSize, cornerSize, cornerSize, cornerSize };
	}

	@Override
	public int getCount() {
		return _productList.size();
	}

	@Override
	public Object getItem(int position) {
		return _productList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Product product = _productList.get(position);
		ProductCell cell;
		RelativeLayout.LayoutParams layoutParams;

		try {
			if (convertView == null) {

				convertView = _inflater.inflate(R.layout.convert_view_layout,
						null);
				convertView.setBackgroundColor(Color.TRANSPARENT);

				cell = new ProductCell();
				cell._layout = (RelativeLayout) convertView
						.findViewById(R.id.holder_layout);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.height = _height;
				layoutParams.width = _width;
				cell._layout.setLayoutParams(layoutParams);
				convertView.setTag(cell);

				// Dynamic layout creation Part
				RoundRectShape rect = new RoundRectShape(CornerRadius,
						new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0,
								0, 0 });
				RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
						new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0,
								0, 0 });
				ShapeDrawable bottom_layer = new CustomShapeDrawable(
						bottom_rect, Color.rgb(145, 145, 145), Color.rgb(145,
								145, 145), 0);
				ShapeDrawable top_layer = new CustomShapeDrawable(rect,
						Color.rgb(175, 175, 175), Color.WHITE, 0);
				LayerDrawable ld = new LayerDrawable(new Drawable[] {
						bottom_layer, top_layer });
				ld.setLayerInset(0, 0, (int) 1.0f, 0, 0);
				ld.setLayerInset(1, 0, 0, 0, (int) 2.0f);

				cell._backgroundlayout = new RelativeLayout(_context);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 0;
				layoutParams.topMargin = 0;
				layoutParams.width = _width;
				layoutParams.height = _height;
				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					cell._backgroundlayout.setBackgroundDrawable(ld);
				} else {
					cell._backgroundlayout.setBackground(ld);
				}
				cell._layout.addView(cell._backgroundlayout, layoutParams);

				int _productImageWidth = _width;
				int _productImageHeight = _width;

				// Product Image View portion
				cell._productImageView = new SizedImageView(_context,
						_productImageWidth, _productImageHeight);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 0;
				layoutParams.topMargin = 0;
				layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
						cell._layout.getId());
				cell._layout.addView(cell._productImageView, layoutParams);

				int textFieldWidth = (int) (_width * .81);
				int textFieldHeight = (int) (_height * .1);
				int textXPad = (int) (textFieldWidth * .02);

				cell._descriptionTextView = new SmartTextView(_context,
						textFieldWidth, textFieldHeight, 2, Color.TRANSPARENT,
						Color.BLACK, _normalFont, Align.LEFT);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = _textXOrigin;
				layoutParams.topMargin = (int) (_height * .625);
				cell._layout.addView(cell._descriptionTextView, layoutParams);

				cell._extendedTextView = new SmartTextView(_context,
						_textWidth, (int) (_height * .05), 1,
						Color.TRANSPARENT, Color.BLACK, _normalFont, Align.LEFT);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = _textXOrigin;
				layoutParams.topMargin = (int) (_height * .75);
				cell._layout.addView(cell._extendedTextView, layoutParams);

				// newly added code{
				cell._seeDetailsTextView = new SmartTextView(_context,
						_textWidth, (int) (_height * .05), 1,
						Color.TRANSPARENT, Color.rgb(187, 0, 0), _normalFont,
						Align.RIGHT);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				layoutParams.leftMargin = _textWidth / 2;
				layoutParams.rightMargin = _textXOrigin;
				layoutParams.topMargin = (int) (_height * .75);
				cell._seeDetailsTextView.writeText("See Details");
				cell._seeDetailsTextView.setTag(position);// newfix
				//cell._layout.addView(cell._seeDetailsTextView, layoutParams);//v2.3 fix
				// if (product.promoType > 0) {
				// cell._layout
				// .addView(cell._seeDetailsTextView, layoutParams);
				// }
				// newly added code}

				// small separator layout portion
				cell.spaceLayout = new RelativeLayout(_context);
				cell.spaceLayout.setMinimumHeight((int) (_height * .005));
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 0;
				layoutParams.topMargin = (int) (_height * .82);
				layoutParams.width = _width;
				cell.spaceLayout.setBackgroundColor(Color.rgb(225, 225, 225));
				cell._layout.addView(cell.spaceLayout, layoutParams);

				// Display price icon amount and add button
				cell._addButtonLayout = new RelativeLayout(_context);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = (int) (_height * .85);
				layoutParams.leftMargin = textXPad;
				layoutParams.rightMargin = textXPad;
				layoutParams.bottomMargin = textXPad;
				layoutParams.width = _width;
				layoutParams.height = (int) (_height * .175);
				cell._layout.addView(cell._addButtonLayout, layoutParams);

				int curr_width = _width - (textXPad * 2);
				int _priceImageHeight = (int) (curr_width * .2);
				int _priceImageWidth = (int) (curr_width * .2);

				int _balanceWidth = (int) (_width * .4);
				int textPriceSize = (int) (_priceImageHeight * .5);

				// if (product.promoType > 0) {

				// Price Icon
				cell._priceImage = new SizedImageView(_context,
						_priceImageWidth, _priceImageHeight);
				cell._priceImage.setImageBitmap(_app.getAppBitmap("sale_icon",
						R.drawable.sale_icon, _priceImageWidth,
						_priceImageHeight));
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = textXPad * 2;
				layoutParams.topMargin = 0;
				layoutParams.width = _priceImageWidth;
				layoutParams.height = _priceImageHeight;
				layoutParams.addRule(RelativeLayout.LEFT_OF,
						cell._priceImage.getId());
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
						cell._priceImage.getId());
				cell._addButtonLayout.addView(cell._priceImage, layoutParams);

				// Price text
				cell._priceAmount = new TextView(_context);
				// cell._priceAmount.setPadding(textXPad, textYPad, 0, 0);
				cell._priceAmount.setTextColor(Color.rgb(206, 31, 39));
				cell._priceAmount.setTypeface(_normalFont);
				cell._priceAmount.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						textPriceSize);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = _priceImageWidth;
				layoutParams.topMargin = 0;
				layoutParams.width = _balanceWidth;
				layoutParams.height = _priceImageHeight;
				cell._priceAmount.setGravity(Gravity.CENTER);
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
						cell._priceAmount.getId());
				cell._addButtonLayout.addView(cell._priceAmount, layoutParams);

				// } else {
				//
				// // Price text
				// cell._priceAmount = new TextView(_context);
				// // cell._priceAmount.setPadding(textXPad, textYPad, 0, 0);
				// cell._priceAmount.setTextColor(Color.BLACK);
				// cell._priceAmount.setTypeface(_normalFont);
				// cell._priceAmount.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				// textPriceSize);
				// String regPriceText = "";
				// if (product.regPrice > 0)
				// regPriceText = "$"
				// + String.format("%.2f", product.regPrice);
				// cell._priceAmount.setText(regPriceText);
				// layoutParams = new RelativeLayout.LayoutParams(
				// RelativeLayout.LayoutParams.WRAP_CONTENT,
				// RelativeLayout.LayoutParams.WRAP_CONTENT);
				// layoutParams.leftMargin = textXPad;
				// layoutParams.topMargin = 0;
				// layoutParams.width = _balanceWidth;
				// layoutParams.height = _priceImageHeight;
				// cell._priceAmount.setGravity(Gravity.CENTER);
				// layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
				// cell._priceAmount.getId());
				// cell._addButtonLayout.addView(cell._priceAmount,
				// layoutParams);
				// }

				// Add Button
				cell._addButton = new Button(_context);
				cell._addButton.setText("Add");
				cell._addButton.setPadding(0, 0, 0, 0);//v2.3 fix for Lollipop
				cell._addButton.setTextColor(Color.WHITE);
				cell._addButton.setTypeface(_normalFont);
				cell._addButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						textPriceSize);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = _priceImageWidth + _balanceWidth;
				layoutParams.topMargin = 0;
				layoutParams.rightMargin = textXPad * 2;
				layoutParams.width = _balanceWidth;
				layoutParams.height = _priceImageHeight;
				layoutParams.addRule(RelativeLayout.RIGHT_OF,
						cell._addButton.getId());
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
						cell._addButton.getId());
				RoundRectShape add_rect = new RoundRectShape(CornerRadius,
						new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0,
								0, 0 });
				ShapeDrawable add_layer = new CustomShapeDrawable(add_rect,
						Color.rgb(145, 145, 145), Color.rgb(187, 0, 0), 0);
				LayerDrawable addLD = new LayerDrawable(
						new Drawable[] { add_layer });
				ld.setLayerInset(0, 0, 0, 0, 0);
				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					cell._addButton.setBackgroundDrawable(addLD);
				} else {
					cell._addButton.setBackground(addLD);
				}
				cell._addButtonLayout.addView(cell._addButton, layoutParams);
				convertView.setTag(cell);
			} else {
				cell = (ProductCell) convertView.getTag();
				// if (product.promoType > 0) {
				// cell._seeDetailsTextView.setVisibility(View.VISIBLE);
				// cell._priceImage.setVisibility(View.VISIBLE);
				// cell._priceAmount.setTextColor(Color.rgb(206, 31, 39));
				// } else {
				// int textFieldWidth = (int) (_width * .81);
				// int textXPad = (int) (textFieldWidth * .02);
				// cell._seeDetailsTextView.setVisibility(View.GONE);
				// cell._priceAmount.setLeft(textXPad);
				//
				// cell._priceImage.setVisibility(View.GONE);
				// cell._priceAmount.setTextColor(Color.BLACK);
				// }
			}
			//
			// cell = (ProductCell) convertView.getTag();

			if (product.promoType > 0) {
				// int textFieldWidth = (int) (_width * .81);
				// int textXPad = (int) (textFieldWidth * .02);
				// int curr_width = _width - (textXPad * 2);
				// int _priceImageWidth = (int) (curr_width * .2);
				// cell._priceAmount.setLeft(_priceImageWidth);

				cell._seeDetailsTextView.setVisibility(View.VISIBLE);
				cell._priceImage.setVisibility(View.VISIBLE);
				cell._priceAmount.setTextColor(Color.rgb(206, 31, 39));
			} else {
				// int textFieldWidth = (int) (_width * .81);
				// int textXPad = (int) (textFieldWidth * .02);
				// cell._priceAmount.setLeft(textXPad);

				cell._seeDetailsTextView.setVisibility(View.GONE);
				cell._priceImage.setVisibility(View.GONE);
				cell._priceAmount.setTextColor(Color.BLACK);
				// Log.i(">> ", ">>" + product.description + " moved "
				// + String.valueOf(textXPad));
			}

			// product bitmap
			Bitmap productBitmap = _app.getCachedImage(product.imagePath); // doesn't
																			// need
																			// scaling
																			// as
																			// drawRect()
																			// handles
																			// that
																			// nicely

			if (productBitmap == null)
				productBitmap = _app._productListDefaultBitmap;
			cell._productImageView.setImageBitmap(getRoundedCornerBitmap(
					productBitmap, false, false, true, true));

			// description text
			String description = null;

			if (product.description != null && product.description.length() > 0) {
				if (product.brand != null && product.brand.length() > 0)
					description = product.brand + " " + product.description;
				else
					description = product.description;
			}
			cell._descriptionTextView.writeText(description);

			// extended text
			cell._extendedTextView.writeText(product.extendedDisplay);

			// reg price
			String regPriceText = "";
			if (product.regPrice > 0)
				regPriceText = "$" + String.format("%.2f", product.regPrice);
			cell._priceAmount.setText(regPriceText);

			final int cellPosition = position;
			cell._addButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						int detailType = Product.PRODUCT_ADD;
						String listid = _app.getActiveListId();
						Product prod = _productList.get(cellPosition);
						_app._ProductDetailsScreenContext = _context;
						if (listid.isEmpty()) {
							((ShoppingScreen) _handler).updateShoppingList(
									detailType, prod, listid);
						} else {
							ShoppingList list = _app.getActiveShoppingList();
							Product currentProduct = null;
							if (list != null && list.productList != null) {
								for (int i = 0; i < list.productList.size(); i++) {
									Product obj = list.productList.get(i);
									if (obj.sku.equalsIgnoreCase(prod.sku)) {
										currentProduct = obj;
										break;
									}
								}
							}

							if (currentProduct != null) {
								if (currentProduct.weight > 0) {
									if (currentProduct.weight >= 100.0)
										return;
									currentProduct.weight += 0.25;
								} else {
									if (currentProduct.unitOfMeasure
											.equalsIgnoreCase("LB")
											&& currentProduct.approxAvgWgt <= 0) {
										if (currentProduct.weight >= 100.0)
											return;
										currentProduct.weight += 0.25;

									} else {
										if (currentProduct.qty >= 100.0)
											return;
										currentProduct.qty += 1;
									}
								}
								// currentProduct.qty += 1;
								detailType = Product.PRODUCT_MODIFY;
								((ShoppingScreen) _handler).updateShoppingList(
										detailType, currentProduct, listid);
							} else {
								if (prod.weight > 0) {
									if (prod.weight >= 100.0)
										return;
									prod.weight += 0.25;
								} else {
									if (prod.unitOfMeasure
											.equalsIgnoreCase("LB")
											&& prod.approxAvgWgt <= 0) {
										if (prod.weight >= 100.0)
											return;
										prod.weight += 0.25;

									} else {
										if (prod.qty >= 100.0)
											return;
										prod.qty += 1;
									}

								}
								((ShoppingScreen) _handler).updateShoppingList(
										detailType, prod, listid);
							}

						}

					} catch (Exception ex) {
						Log.e("Error", "productpage");
					}
				}
			});

			cell._layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int productDetaiType = 0;
					boolean found = false;
					Product product = _productList.get(cellPosition);

					ShoppingList list = _app.getActiveShoppingList();
					if (list != null && list.productList != null) {
						for (Product listProduct : list.productList) {
							if (listProduct.sku.equals(product.sku)) {
								found = true;
								_app._currentProductDetail = listProduct;
								productDetaiType = Product.PRODUCT_MODIFY;
								break;
							}
						}
					}

					if (found == false) {
						_app._currentProductDetail = product;
						productDetaiType = Product.PRODUCT_ADD;
					}

					try {
						_handler.getClass()
								.getMethod(_callback, int.class, String.class)
								.invoke(_handler, productDetaiType,
										"productpage");
					} catch (Exception ex) {
						Log.e("Error", "productpage");
					}

				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return convertView;
	}

	static class ProductCell {
		RelativeLayout _layout;
		RelativeLayout spaceLayout;
		RelativeLayout _addButtonLayout;
		RelativeLayout _backgroundlayout;
		SizedImageView _background;
		SizedImageView _priceImage;
		SizedImageView _productImageView;
		SizedImageView _promoImageView;
		SmartTextView _descriptionTextView;
		SmartTextView _extendedTextView;
		SmartTextView _regPriceTextTextView;
		SmartTextView _regPriceValueTextView;
		SmartTextView _promoPriceTextTextView;
		SmartTextView _promoPriceValueTextView;
		TextView _priceAmount;
		Button _addButton; // new code
		SmartTextView _seeDetailsTextView; // new code
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean squareTL,
			boolean squareTR, boolean squareBL, boolean squareBR) {

		// link

		// http://stackoverflow.com/questions/2459916/how-to-make-an-imageview-with-rounded-corners

		// Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		// bitmap.getHeight(), Config.ARGB_8888);
		// Canvas canvas = new Canvas(output);
		// Paint paint = new Paint();
		// RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		// Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		// canvas.drawRect(new RectF(0, cornerSize, 0, 0), paint);
		// paint.setAntiAlias(true);
		// canvas.drawRoundRect(rectF, cornerSize, cornerSize, paint);
		//
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas.drawBitmap(bitmap, rect, rectF, paint);
		//
		// return output;

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawRoundRect(rectF, cornerSize, cornerSize, paint);

		if (squareTL) {
			canvas.drawRect(0, 0, cornerSize / 2, cornerSize / 2, paint);
		}
		if (squareTR) {
			canvas.drawRect(cornerSize / 2, 0, cornerSize, cornerSize / 2,
					paint);
		}
		if (squareBL) {
			canvas.drawRect(0, cornerSize / 2, cornerSize / 2, cornerSize,
					paint);
		}
		if (squareBR) {
			canvas.drawRect(cornerSize / 2, cornerSize / 2, cornerSize,
					cornerSize, paint);
		}

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rectF, paint);

		return output;
	}
}
