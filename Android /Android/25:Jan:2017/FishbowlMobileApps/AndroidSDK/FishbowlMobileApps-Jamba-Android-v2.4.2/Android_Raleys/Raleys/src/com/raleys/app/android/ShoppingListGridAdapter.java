package com.raleys.app.android;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.raleys.app.android.models.Product;
import com.raleys.app.android.models.ShoppingList;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SmartTextView;
import com.raleys.libandroid.Utils;

public class ShoppingListGridAdapter extends BaseAdapter {
	private RaleysApplication _app;
	private Context _context;
	private Object _handler;
	private String _callback;
	private int _width;
	private int _headerWidth;
	private int _headerHeight;
	private int _cellHeight;
	private int _columns;
	private int _cellSpacing;
	private int _cellWidth;
	private int _productImageSize;
	private int _promoImageSize;
	private int _textXOrigin;
	private int _textWidth;
	private int _textHalfWidth;
	private LayoutInflater _inflater;
	private Typeface _normalFont;
	private ArrayList<ArrayList<Product>> _rows;
	private AlphaAnimation _touchAnimation;

	private final int BLANK_ROW = 0;
	private final int HEADER_ROW = 1;
	private final int PRODUCT_ROW = 2;

	public ShoppingListGridAdapter(Context context,
			ArrayList<ArrayList<Product>> rows, int columns, int width,
			int headerHeight, int cellHeight, String callback) {
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
		_cellSpacing = (int) (_width * .01);
		_cellWidth = (_width - ((_columns + 1) * _cellSpacing)) / _columns;
		_textXOrigin = (int) (_cellWidth * .03);
		_textWidth = _cellWidth - (_textXOrigin * 2);
		_textHalfWidth = (_width - (_textXOrigin * 2)) / 2;
		_productImageSize = (int) (_cellHeight * .9);
		_promoImageSize = (int) (_cellHeight * .9);
		_inflater = LayoutInflater.from(context);
		_normalFont = _app.getNormalFont();

		// heavily used bitmaps should only be scaled once and stored in the
		// app...
		if (_app._shoppingListGridHeaderBitmap == null)
			_app._shoppingListGridHeaderBitmap = _app.getAppBitmap(
					"shopping_list_header", R.drawable.list_section_bg,
					_headerWidth, _headerHeight);

		if (_app._shoppingListGridCellBitmap == null)
			_app._shoppingListGridCellBitmap = _app.getAppBitmap(
					"shopping_list_grid_cell", R.drawable.product_cell,
					_cellWidth, _cellHeight);

		if (_app._shoppingListGridPromoBitmap == null)
			_app._shoppingListGridPromoBitmap = _app.getAppBitmap(
					"shopping_list_grid_promo", R.drawable.sale_tag,
					_promoImageSize, _promoImageSize);

		if (_app._shoppingListDefaultBitmap == null)
			_app._shoppingListDefaultBitmap = _app.getAppBitmap(
					"shopping_list_grid_default", R.drawable.shopping_bag,
					_productImageSize, _productImageSize);

		_touchAnimation = new AlphaAnimation(1.0f, .5f);
		_touchAnimation.setDuration(100);
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
		ArrayList<Product> currentRow = _rows.get(position);
		Product product = currentRow.get(0);

		if (product.mainCategory == null)
			return PRODUCT_ROW;

		if (product.mainCategory.equalsIgnoreCase("CONTENT_VIEW_BLANK"))
			return BLANK_ROW;
		if (currentRow.get(0).mainCategory.equalsIgnoreCase("GRID_HEADER"))
			return HEADER_ROW;
		else
			return PRODUCT_ROW;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArrayList<Product> products = _rows.get(position);
		BlankRow blankRow = null;
		HeaderRow headerRow = null;
		ProductRow productRow = null;
		RelativeLayout.LayoutParams layoutParams = null;

		try {
			if (convertView == null) {
				convertView = _inflater.inflate(R.layout.convert_view_layout,
						null);
				convertView.setBackgroundColor(Color.TRANSPARENT);

				if (getItemViewType(position) == BLANK_ROW) {
					blankRow = new BlankRow();
					blankRow._layout = (RelativeLayout) convertView
							.findViewById(R.id.holder_layout);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.height = _app.getDeviceType() == Utils.DEVICE_PHONE ? (int) (_headerHeight * .75)
							: (int) (_headerHeight * .90);
					layoutParams.width = _width;
					blankRow._layout.setBackgroundColor(Color
							.rgb(215, 215, 215)); //
					// Menu's
					// header
					// bottom
					// border

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
				} else if (getItemViewType(position) == HEADER_ROW) {
					headerRow = new HeaderRow();
					headerRow._layout = (RelativeLayout) convertView
							.findViewById(R.id.holder_layout);
					headerRow._layout.setBackgroundColor(Color.rgb(215, 215,
							215));// (R.color.ListSectionGray);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.width = _width;
					layoutParams.height = _headerHeight;
					headerRow._layout.setLayoutParams(layoutParams);
					convertView.setTag(headerRow);
					/*
					 * headerRow._background = new SizedImageView(_context,
					 * _headerWidth, _headerHeight); headerRow._background
					 * .setImageBitmap(_app._shoppingListGridHeaderBitmap);
					 * layoutParams = new RelativeLayout.LayoutParams(
					 * RelativeLayout.LayoutParams.WRAP_CONTENT,
					 * RelativeLayout.LayoutParams.WRAP_CONTENT);
					 * layoutParams.leftMargin = 0; layoutParams.topMargin =0;
					 * headerRow._layout.addView(headerRow._background,
					 * layoutParams);
					 */

					int textWidth = (int) (_headerWidth * .9);
					headerRow._headerTextView = new SmartTextView(_context,
							textWidth, (int) (_headerHeight * .55), 1,
							Color.TRANSPARENT, Color.BLACK, _normalFont,
							Align.CENTER);
					headerRow._headerTextView
							.setBackgroundColor(Color.TRANSPARENT);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = (_width - textWidth) / 2;
					layoutParams.topMargin = (int) (_headerHeight * .2);
					headerRow._layout.addView(headerRow._headerTextView,
							layoutParams);
				} else // PRODUCT_ROW
				{
					productRow = new ProductRow();
					productRow._layout = (RelativeLayout) convertView
							.findViewById(R.id.holder_layout);
					layoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					layoutParams.width = _width;
					layoutParams.height = _cellHeight;
					productRow._layout.setLayoutParams(layoutParams);
					convertView.setTag(productRow);

					productRow._cells = new ProductCell[_columns];

					for (int i = 0; i < _columns; i++) {
						productRow._cells[i] = new ProductCell();
						productRow._cells[i]._layout = new RelativeLayout(
								_context);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = (_cellSpacing * (i + 1))
								+ (_cellWidth * i);
						layoutParams.topMargin = 0;
						layoutParams.width = _cellWidth;
						layoutParams.height = _cellHeight;
						productRow._cells[i]._layout
								.setLayoutParams(layoutParams);

						// productRow._cells[i].

						productRow._cells[i]._backgroundLayout = new RelativeLayout(
								_context);
						productRow._cells[i]._backgroundLayout
								.setBackgroundColor(Color.WHITE);

						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = 0;
						layoutParams.topMargin = 0;
						layoutParams.width = _cellWidth;
						layoutParams.height = _cellHeight;
						productRow._cells[i]._layout.addView(
								productRow._cells[i]._backgroundLayout,
								layoutParams);

						int productImagePad = (int) (_cellWidth * .01);
						productRow._cells[i]._productImageView = new SizedImageView(
								_context, _productImageSize, _productImageSize);
						// productRow._cells[i]._productImageView
						// .setImageBitmap(_app._shoppingListGridPromoBitmap);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = productImagePad;
						layoutParams.topMargin = productImagePad;
						productRow._cells[i]._layout.addView(
								productRow._cells[i]._productImageView,
								layoutParams);

						int new_textWidth = (int) Math.round(_textWidth * 0.55);
						productRow._cells[i]._descriptionTextView = new SmartTextView(
								_context, new_textWidth,
								(int) (_cellHeight * .20), 1,
								Color.TRANSPARENT, Color.BLACK, _normalFont,
								Align.LEFT);

						productRow._cells[i]._descriptionTextView
								.setBackgroundColor(Color.TRANSPARENT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = _textXOrigin * 2
								+ _productImageSize;
						layoutParams.topMargin = (int) (_cellHeight * .21); // .41
						productRow._cells[i]._layout.addView(
								productRow._cells[i]._descriptionTextView,
								layoutParams);

						productRow._cells[i]._extendedTextView = new SmartTextView(
								_context, _textHalfWidth,
								(int) (_cellHeight * 0.21), 1,
								Color.TRANSPARENT, Color.BLACK, _normalFont,
								Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = _textXOrigin;
						layoutParams.topMargin = (int) (_cellHeight * .83);

						productRow._cells[i]._quantityTextView = new SmartTextView(
								_context, _textHalfWidth,
								(int) (_cellHeight * 0.21), 1,
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								Align.LEFT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = _textXOrigin * 2
								+ _productImageSize;
						layoutParams.topMargin = (int) (_cellHeight * .60); // 0.90
						productRow._cells[i]._layout.addView(
								productRow._cells[i]._quantityTextView,
								layoutParams);

						productRow._cells[i]._totalPriceTextView = new SmartTextView(
								_context, (int) (_cellWidth * .46),
								(int) (_cellHeight * 0.21), 1,
								Color.TRANSPARENT, Color.DKGRAY, _normalFont,
								Align.RIGHT);
						layoutParams = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin = (int) (_cellWidth * .30); // .42
						// layoutParams.leftMargin = (int) (_textXOrigin*2) +
						// (_productImageSize)+_textHalfWidth;
						layoutParams.topMargin = (int) (_cellHeight * .60);
						productRow._cells[i]._layout.addView(
								productRow._cells[i]._totalPriceTextView,
								layoutParams);

						// check active or not
						int Delete_button_size = (int) (_cellHeight * 0.40);
						String activeListId = _app.getActiveListId();
						if (_app._currentShoppingList.listId
								.equals(activeListId)) {
							// delete button
							productRow._cells[i]._deleteButton = new Button(
									_context);
							productRow._cells[i]._deleteButton.setText("");
							int sdk = android.os.Build.VERSION.SDK_INT;

							if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
								productRow._cells[i]._deleteButton
										.setBackgroundDrawable(_context
												.getResources()
												.getDrawable(
														R.drawable.delete_inactive));
							} else {
								productRow._cells[i]._deleteButton
										.setBackground(_context
												.getResources()
												.getDrawable(
														R.drawable.delete_inactive));
							}

							productRow._cells[i]._deleteButton
									.setHeight(Delete_button_size);
							productRow._cells[i]._deleteButton
									.setWidth(Delete_button_size);

							layoutParams = new RelativeLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);

							layoutParams.width = Delete_button_size;
							layoutParams.height = Delete_button_size;
							layoutParams.leftMargin = (int) (_cellWidth * .84);
							layoutParams.topMargin = (int) (_cellHeight * .28);

							productRow._cells[i]._layout.addView(
									productRow._cells[i]._deleteButton,
									layoutParams);
						} else {

							// move delete layout
							int layoutheight = (int) (_cellHeight * .8);
							int layoutwidth = (int) (_cellWidth * .125);

							productRow._cells[i]._moveDeleteLayout = new RelativeLayout(
									_context);
							layoutParams = new RelativeLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							layoutParams.width = layoutwidth;
							layoutParams.height = layoutheight;
							layoutParams.leftMargin = (int) (_cellWidth * .825);
							layoutParams.topMargin = (int) (_cellHeight * .1);
							productRow._cells[i]._layout.addView(
									productRow._cells[i]._moveDeleteLayout,
									layoutParams);

							// move button
							int moveHgt = (int) (layoutheight * .5);
							productRow._cells[i]._moveButton = new Button(
									_context);
							productRow._cells[i]._moveButton.setText("Move");
							productRow._cells[i]._moveButton.setPadding(6, 6,
									6, 6);
							productRow._cells[i]._moveButton.setTextSize(
									TypedValue.COMPLEX_UNIT_PX,
									(int) (_cellHeight * .15));
							int sdk = android.os.Build.VERSION.SDK_INT;
							if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
								productRow._cells[i]._moveButton
										.setBackgroundDrawable(_context
												.getResources().getDrawable(
														R.drawable.shop_delete));
							} else {
								productRow._cells[i]._moveButton
										.setBackground(_context.getResources()
												.getDrawable(
														R.drawable.shop_delete));
							}

							productRow._cells[i]._moveButton
									.setTextColor(Color.WHITE);
							layoutParams = new RelativeLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							layoutParams.width = layoutwidth;
							layoutParams.height = moveHgt;
							layoutParams.leftMargin = 0;
							layoutParams.topMargin = 0;
							layoutParams.addRule(
									RelativeLayout.CENTER_HORIZONTAL,
									productRow._cells[i]._moveDeleteLayout
											.getId());
							layoutParams.addRule(
									RelativeLayout.ALIGN_PARENT_TOP,
									productRow._cells[i]._moveDeleteLayout
											.getId());
							productRow._cells[i]._moveDeleteLayout.addView(
									productRow._cells[i]._moveButton,
									layoutParams);

							// delete button
							productRow._cells[i]._deleteButton = new Button(
									_context);
							productRow._cells[i]._deleteButton.setText("");
							if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
								productRow._cells[i]._deleteButton
										.setBackgroundDrawable(_context
												.getResources()
												.getDrawable(
														R.drawable.delete_inactive));
							} else {
								productRow._cells[i]._deleteButton
										.setBackground(_context
												.getResources()
												.getDrawable(
														R.drawable.delete_inactive));
							}

							productRow._cells[i]._deleteButton
									.setHeight(Delete_button_size);
							productRow._cells[i]._deleteButton
									.setWidth(Delete_button_size);

							layoutParams = new RelativeLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							layoutParams.width = moveHgt;
							layoutParams.height = moveHgt;
							layoutParams.topMargin = moveHgt
									+ (int) (_cellHeight * .05);
							layoutParams.addRule(
									RelativeLayout.ALIGN_PARENT_BOTTOM,
									productRow._cells[i]._moveDeleteLayout
											.getId());
							layoutParams.addRule(
									RelativeLayout.CENTER_HORIZONTAL,
									productRow._cells[i]._moveDeleteLayout
											.getId());
							productRow._cells[i]._moveDeleteLayout.addView(
									productRow._cells[i]._deleteButton,
									layoutParams);
						}

						productRow._layout
								.addView(productRow._cells[i]._layout);

					}
				}
			} else {
				if (getItemViewType(position) == 0) // BlankRow
					blankRow = (BlankRow) convertView.getTag();
				else if (getItemViewType(position) == 1) // HeaderRow
					headerRow = (HeaderRow) convertView.getTag();
				else
					// OfferRow
					productRow = (ProductRow) convertView.getTag();
			}

			for (int i = 0; i < _columns; i++) {
				if (getItemViewType(position) == 0)
					break;

				if (getItemViewType(position) == 1) {
					headerRow._headerTextView
							.writeText(products.get(0).description);
					break;
				}

				if (i < products.size()) {
					final Product product = products.get(i);

					// product bitmap
					Bitmap productBitmap = _app
							.getCachedImage(product.imagePath); // doesn't need
																// scaling as
																// drawRect()
																// handles that
																// nicely

					if (productBitmap == null)
						productBitmap = _app._shoppingListDefaultBitmap;

					productRow._cells[i]._productImageView
							.setImageBitmap(productBitmap);

					// // promo bitmap
					// if (product.promoType > 0)
					// productRow._cells[i]._promoImageView
					// .setVisibility(View.VISIBLE);
					// else
					// productRow._cells[i]._promoImageView
					// .setVisibility(View.GONE);

					// description text
					String description = null;

					if (product.description != null
							&& product.description.length() > 0) {
						if (product.brand != null && product.brand.length() > 0)
							description = product.brand + " "
									+ product.description;
						else
							description = product.description;
					}

					productRow._cells[i]._descriptionTextView
							.writeText(description);

					// extended text
					productRow._cells[i]._extendedTextView
							.writeText(product.extendedDisplay);

					// // quantity text
					// int quantity;
					//
					// if (product.weight > 0)
					// quantity = (int) product.weight;
					// else
					// quantity = product.qty;

					try {
						// new code{
						if (product.weight > 0) {
							productRow._cells[i]._quantityTextView
									.writeText("Lbs: "
											+ Float.toString(product.weight));
						} else {
							productRow._cells[i]._quantityTextView
									.writeText("Qty: "
											+ Integer.toString(product.qty));
						}
						// new code}
						// productRow._cells[i]._quantityTextView
						// .writeText("Qty: " + Integer.toString(quantity));

					} catch (NumberFormatException nfex) {
						nfex.printStackTrace();
					}

					// total price
					float unitPrice;
					int totalPriceColor;

					if (product.promoType > 0) {
						unitPrice = product.promoPrice / product.promoForQty;
						// totalPriceColor = Color.RED;
					} else {
						unitPrice = product.regPrice;
						// totalPriceColor = Color.argb(255, 53, 139, 66);
					}

					totalPriceColor = Color.DKGRAY;

					if (product.qty > 0 && product.approxAvgWgt > 0)
						unitPrice *= product.approxAvgWgt;

					// Log.e("DEBUG", "ShoppingListGridAdapter: weight=" +
					// product.weight + ", qty=" + product.qty + ", quantity=" +
					// quantity + ", totalPriceValue=" + ("$" +
					// String.format("%.2f", unitPrice * quantity)));
					productRow._cells[i]._totalPriceTextView
							.setTextColor(totalPriceColor);
					// new code{
					if (product.weight > 0) {
						productRow._cells[i]._totalPriceTextView
								.writeText("Price: $"
										+ String.format("%.2f", unitPrice
												* product.weight));
					} else {
						productRow._cells[i]._totalPriceTextView
								.writeText("Price: $"
										+ String.format("%.2f", unitPrice
												* product.qty));
					}
					// new code}
					productRow._cells[i]._layout.setVisibility(View.VISIBLE);
					final ProductCell cell = productRow._cells[i];

					productRow._cells[i]._layout
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									((ShoppingScreen) _handler)
											.dismiss_offermenu();
									changeList(product);
								}
							});

					/*
					 * productRow._cells[i]._layout .setOnTouchListener(new
					 * OnTouchListener() { long downTime; long upTime;
					 * 
					 * @Override public boolean onTouch(View v, MotionEvent
					 * event) { int action = event.getAction();
					 * 
					 * if (action == MotionEvent.ACTION_DOWN) { downTime =
					 * event.getEventTime(); cell._layout.setAlpha(.5f); return
					 * true; } else if (action == MotionEvent.ACTION_UP ||
					 * action == MotionEvent.ACTION_CANCEL) {
					 * cell._layout.setAlpha(1.0f); upTime =
					 * event.getEventTime();
					 * 
					 * if (upTime - downTime > 150) changeList(product);
					 * 
					 * return true; } else { return false; } }
					 * 
					 * });
					 */
					// move button
					if (productRow._cells[i]._moveButton != null) {
						productRow._cells[i]._moveButton
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// ((ShoppingScreen)_context);
										moveProductFromList(product);
									}
								});
					}

					// delete button
					if (productRow._cells[i]._deleteButton != null) {
						productRow._cells[i]._deleteButton
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// ((ShoppingScreen)_context);
										deleteProductFromList(product);
									}
								});
					}

				} else {
					productRow._cells[i]._layout.setVisibility(View.GONE);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return convertView;
	}

	void deleteProductFromList(Product _product) {
		// _product.qty = _productQuantity;
		// try {
		// if (_product != null) {
		// ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		// param.add(new BasicNameValuePair("Product_SKU", _product.sku));
		// param.add(new BasicNameValuePair("Product_UPC", _product.upc));
		// param.add(new BasicNameValuePair("Product_Brand",
		// _product.brand));
		// param.add(new BasicNameValuePair("Product_Name",
		// _product.description));
		// param.add(new BasicNameValuePair("Product_QTY", String
		// .valueOf(_product.qty)));
		//
		// CLP SDK Product Delete
		// }
		// } catch (Exception e) {
		// Log.e(_app.ERROR, "REMOVE_PRODUCT:" + e.getMessage());
		// }
		try {
			// _handler.getClass()
			// .getMethod("updateShoppingList", int.class, Product.class)
			// .invoke(_handler, Product.PRODUCT_DELETE, _product);
			((ShoppingScreen) _handler).updateShoppingList(
					Product.PRODUCT_DELETE, _product, _app.getCurrentListId());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void moveProductFromList(Product _product) {

		try {
			if (_app.getActiveListId().isEmpty()) {
				// no active list
				((ShoppingScreen) _handler)
						.showTextDialog(_context, "No Active List",
								"No active list to add into. Goto lists and make one active.");
				return;
			}
			// _handler.getClass()
			// .getMethod("updateShoppingList", int.class, Product.class,
			// String.class)
			// .invoke(_handler, Product.PRODUCT_ADD, _product,
			// _app.getActiveListId());

			// check product already exists in the active shopping list
			if (checkProudctIsAlreadyExistsInActiveList(_product) == false) {

				// 
				try {
					// CLP SDK Product add in tiles
					JSONObject data = new JSONObject();
//					data.put("product_sku", _product.sku);
					data.put("product_upc", _product.upc);
//					data.put("product_brand", _product.brand);
					data.put("product_name", _product.description);
//					data.put("product_qty", String.valueOf(_product.qty));

					data.put("event_name", "Product Added");
					data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
					_app.clpsdkObj.updateAppEvent(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// CLP ANALYTICS END
				((ShoppingScreen) _handler).updateShoppingList(
						Product.PRODUCT_ADD, _product, _app.getActiveListId());
			}
			_app._showShoppingList = false;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Boolean checkProudctIsAlreadyExistsInActiveList(Product _product) {
		Boolean exists = false;
		ShoppingList list = _app.getActiveShoppingList();
		for (int i = 0; i < list.productList.size(); i++) {
			Product prod = list.productList.get(i);
			if (prod.sku.equals(_product.sku)) {
				exists = true;
				// product already exists
				break;
			}
		}
		if (exists == true) {
			String message = "This item already Exists in ".concat(list.name);
			((ShoppingScreen) _handler).showTextDialog(_context,
					"Already Exists", message);
		}
		return exists;
	}

	public void changeList(Product product) {
		_app._currentProductDetail = product;

		try {
			_handler.getClass().getMethod(_callback, int.class, String.class)
					.invoke(_handler, Product.PRODUCT_MODIFY, "listpage");
		} catch (Exception ex) {
			Log.e("Error", "listpage");
		}
	}

	static class BlankRow {
		RelativeLayout _layout;
		SizedImageView _background;
	}

	static class HeaderRow {
		RelativeLayout _layout;
		SizedImageView _background;
		SmartTextView _headerTextView;
	}

	static class ProductRow {
		RelativeLayout _layout;
		ProductCell[] _cells;
	}

	static class ProductCell {
		RelativeLayout _layout;
		RelativeLayout _moveDeleteLayout;
		// SizedImageView _background;
		SizedImageView _productImageView;
		RelativeLayout _backgroundLayout;
		// SizedImageView _promoImageView;
		SmartTextView _descriptionTextView;
		SmartTextView _extendedTextView;
		SmartTextView _quantityTextView;
		SmartTextView _totalPriceTextView;
		Button _moveButton;
		Button _deleteButton;
	}
}
