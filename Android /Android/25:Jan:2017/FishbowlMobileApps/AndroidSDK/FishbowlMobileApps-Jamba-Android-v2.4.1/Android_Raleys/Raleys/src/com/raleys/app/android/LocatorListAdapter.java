package com.raleys.app.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.Store;
import com.raleys.libandroid.SizedImageButton;
import com.raleys.libandroid.SizedImageTextButton;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SmartTextView;

public class LocatorListAdapter extends BaseAdapter {
	private RaleysApplication _app;
	private Context _context;
	private Object _handler;
	private String _callback;
	private int _width;
	private int _height;
	private int _textWidth;
	private ArrayList<Store> _storeList;
	private LayoutInflater _inflater;
	private Typeface _normalFont;
	int imagePadding;;
	int imageSize;
	int cornerSize;;
	float[] CornerRadius;
	Store globalStore;
	Handler _newHandler;
	Runnable runnable;

	public LocatorListAdapter(Context context, ArrayList<Store> storeList,
			int width, int height, String callback) {
		_app = (RaleysApplication) context.getApplicationContext();
		_context = context;
		_handler = context;
		_callback = callback;
		_width = width;
		_height = height;
		_textWidth = (int) (_width * .6);
		_storeList = storeList;
		_inflater = LayoutInflater.from(context);
		_normalFont = _app.getNormalFont();
		imagePadding = (int) (_height * .35);
		imageSize = _height - imagePadding;

		if (_app._storeButtonBitmap == null)
			_app._storeButtonBitmap = _app.getAppBitmap("store_list_button",
					R.drawable.button_red_plain, (int) (imageSize * 1.5),
					(int) (imageSize * .75));

		_newHandler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				myStoreButtonPressed(globalStore);
			}
		};

	}

	@Override
	public int getCount() {
		return _storeList.size();
	}

	@Override
	public Object getItem(int position) {
		return _storeList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Store store = _storeList.get(position);
		Login login = _app.getLogin();

		final StoreCell cell;
		RelativeLayout.LayoutParams layoutParams;

		try {
			if (convertView == null) {
				convertView = _inflater.inflate(
						R.layout.store_convert_view_layout, null);
				convertView.setBackgroundColor(Color.WHITE);

				cell = new StoreCell();
				cell._layout = (RelativeLayout) convertView
						.findViewById(R.id.holder_layout);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.height = _height;
				layoutParams.width = _width;
				cell._layout.setLayoutParams(layoutParams);

				cornerSize = (int) (imagePadding * .25);
				CornerRadius = new float[] { cornerSize, cornerSize,
						cornerSize, cornerSize, cornerSize, cornerSize,
						cornerSize, cornerSize };

				cell._chainImageView = new SizedImageView(_context, imageSize,
						imageSize);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int) (imagePadding * .75);
				layoutParams.topMargin = (int) (imagePadding * .5);
				cell._layout.addView(cell._chainImageView, layoutParams);

				cell.myStoreButton = new Button(_context);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.rightMargin = (int) (imagePadding * .5);
				layoutParams.topMargin = (int) (imagePadding * .5);
				layoutParams.width = (int) (imageSize * 1.75);
				layoutParams.height = (int) (imageSize * .75);
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				cell._layout.addView(cell.myStoreButton, layoutParams);

				cell.myStoreSelectedButton = new Button(_context);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.rightMargin = (int) (imagePadding * .5);
				layoutParams.topMargin = (int) (imagePadding * .5);
				layoutParams.height = (int) (imageSize * .75);
				layoutParams.width = (int) (imageSize * 1.75);
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				cell._layout.addView(cell.myStoreSelectedButton, layoutParams);

				cell._addressTextView = new SmartTextView(_context, _textWidth,
						(int) (_height * .30), 1, Color.TRANSPARENT,
						Color.DKGRAY, _normalFont, Align.LEFT);

				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = imagePadding
						+ (int) (imageSize * 1.5);
				layoutParams.topMargin = (int) (_height * .20);
				cell._layout.addView(cell._addressTextView, layoutParams);

				cell._cityStateZipTextView = new SmartTextView(_context,
						_textWidth, (int) (_height * .25), 1,
						Color.TRANSPARENT, Color.DKGRAY, _normalFont,
						Align.LEFT);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = imagePadding
						+ (int) (imageSize * 1.5);
				layoutParams.topMargin = (int) (_height * .60);
				cell._layout.addView(cell._cityStateZipTextView, layoutParams);

				cell._ecartImageView = new SizedImageView(_context, imageSize,
						(int) (imageSize * .5));
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int) (imagePadding * 1.75);
				layoutParams.topMargin = (int) (imagePadding * .15);
				cell._layout.addView(cell._ecartImageView, layoutParams);

				cell._distanceTextView = new SmartTextView(_context,
						(int) (imageSize * 1.5), (int) (_height * .2), 1,
						Color.TRANSPARENT, Color.RED, _normalFont, Align.CENTER);
				layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.rightMargin = (int) (imagePadding * .5);
				layoutParams.topMargin = (int) (imageSize * 1.05);
				cell._distanceTextView.setGravity(Gravity.CENTER);
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				cell._layout.addView(cell._distanceTextView, layoutParams);

				convertView.setTag(cell);
			} else {
				cell = (StoreCell) convertView.getTag();
			}

			String storeName = store.chain;
			String eCartEnabled = store.ecart;

			// Store Logo's
			if (storeName.equals("Raley's")) {
				cell._chainImageView
						.setBackgroundResource(R.drawable.raley_logo);
			} else if (storeName.equals("Bel Air")) {
				cell._chainImageView.setBackgroundResource(R.drawable.bel_logo);
			} else if (storeName.equals("Nob Hill Foods")) {
				cell._chainImageView.setBackgroundResource(R.drawable.nob_logo);
			}

			cell._addressTextView.writeText(store.address);
			cell._cityStateZipTextView.writeText(store.city + " " + store.state
					+ " " + store.zip);

			// E-Cart Logo
			if (eCartEnabled.equals("Yes")) {// E-cart Stores
				cell._ecartImageView.setVisibility(View.VISIBLE);
				cell._ecartImageView
						.setBackgroundResource(R.drawable.ecart_button);
			} else if (eCartEnabled.equals("No")) {// Non-Ecart Stores
				cell._ecartImageView.setVisibility(View.GONE);
			}
			cell._distanceTextView.writeText(String.format("%.1f",
					store._distanceFromLocation / _app.METERS_PER_MILE)
					+ " mi.");

			int textFieldHeight = (int) (_height * .25);

			int textFieldSize = (int) (textFieldHeight * .75);
			try {
				if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
					textFieldHeight = (int) (_height*.25);
					textFieldSize = (int) (textFieldHeight * .75);//v2.3 fix for Lollipop
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (login.storeNumber == store.storeNumber) {

				// Dynamic layout creation Part
				RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
						new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0,
								0, 0 });
				ShapeDrawable top_layer = new CustomShapeDrawable(bottom_rect,
						Color.DKGRAY, Color.rgb(24, 169, 55), 0);
				LayerDrawable ld = new LayerDrawable(
						new Drawable[] { top_layer });
				ld.setLayerInset(0, 0, 0, 0, 0);

				cell.myStoreSelectedButton.setTextColor(Color.WHITE);
				cell.myStoreSelectedButton.setText("My\nStore");
				
				//v2.3 fix
				try {
					if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
//						cell.myStoreSelectedButton.setText("My Store");
						cell.myStoreSelectedButton.setPadding(0, 0, 0, 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//v2.3 fix
				cell.myStoreSelectedButton.setTypeface(_normalFont);
				cell.myStoreSelectedButton.setTextSize(
						TypedValue.COMPLEX_UNIT_PX, textFieldSize);
				cell.myStoreSelectedButton.setMaxLines(2);
				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					cell.myStoreSelectedButton.setBackgroundDrawable(ld);
				} else {
					cell.myStoreSelectedButton.setBackground(ld);
				}
				cell.myStoreButton.setVisibility(View.GONE);
				cell.myStoreSelectedButton.setVisibility(View.VISIBLE);

				cell._layout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// myStoreButtonPressed(store);
					}
				});

				cell.myStoreSelectedButton
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// myStoreButtonPressed(store);
							}
						});

				// cell.myStoreSelectedButton
				// .setOnTouchListener(new OnTouchListener() {
				// @Override
				// public boolean onTouch(View v, MotionEvent event) {
				// // TODO Auto-generated method stub
				// return false;
				//
				// }
				// });
				//
				// cell._layout.setOnTouchListener(new OnTouchListener() {
				// @Override
				// public boolean onTouch(View v, MotionEvent event) {
				// // TODO Auto-generated method stub
				// return false;
				//
				// }
				// });

			} else {

				// Dynamic layout creation Part
				RoundRectShape bottom_rect = new RoundRectShape(CornerRadius,
						new RectF(0, 0, 0, 0), new float[] { 0, 0, 0, 0, 0, 0,
								0, 0 });
				ShapeDrawable top_layer = new CustomShapeDrawable(bottom_rect,
						Color.DKGRAY, Color.WHITE, 0);
				LayerDrawable ld = new LayerDrawable(
						new Drawable[] { top_layer });
				ld.setLayerInset(0, 0, 0, 0, 0);

				cell.myStoreButton.setTextColor(Color.DKGRAY);
				cell.myStoreButton.setText("Make\nmy store");
				//v2.3 fix
				try {
					if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
//						cell.myStoreButton.setText("Make my store");
						cell.myStoreButton.setPadding(0, 0, 0, 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//v2.3 fix
				cell.myStoreButton.setTypeface(_normalFont);
				cell.myStoreButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						textFieldSize);
				cell.myStoreButton.setMaxLines(2);
				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					cell.myStoreButton.setBackgroundDrawable(ld);
				} else {
					cell.myStoreButton.setBackground(ld);
				}
				cell.myStoreSelectedButton.setVisibility(View.GONE);
				cell.myStoreButton.setVisibility(View.VISIBLE);

				// Avoid multiple alerts
				cell.myStoreButton
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								globalStore = store;
								_newHandler.removeCallbacks(runnable);
								_newHandler.postDelayed(runnable, 500);
							}
						});

				cell._layout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						globalStore = store;
						_newHandler.removeCallbacks(runnable);
						_newHandler.postDelayed(runnable, 500);
					}
				});

				// cell.myStoreButton.setOnTouchListener(new OnTouchListener() {
				// long downTime;
				// long upTime;
				//
				// @Override
				// public boolean onTouch(View v, MotionEvent event) {
				// // TODO Auto-generated method stub
				// int action = event.getAction();
				// if (action == MotionEvent.ACTION_DOWN) {
				// downTime = event.getEventTime();
				// cell._layout.setAlpha(.5f);
				// return true;
				// } else if (action == MotionEvent.ACTION_UP
				// || action == MotionEvent.ACTION_CANCEL) {
				// cell._layout.setAlpha(1.0f);
				// upTime = event.getEventTime();
				//
				// if (upTime - downTime > 150) {
				// try {
				// globalStore = store;
				// _newHandler.removeCallbacks(runnable);
				// _newHandler.postDelayed(runnable, 500);
				// // myStoreButtonPressed(store);
				// } catch (Exception ex) {
				// ex.printStackTrace();
				// }
				// }
				//
				// return true;
				// } else {
				// return false;
				// }
				// }
				// });
				//
				// cell._layout.setOnTouchListener(new OnTouchListener() {
				// long downTime;
				// long upTime;
				//
				// @Override
				// public boolean onTouch(View v, MotionEvent event) {
				// // TODO Auto-generated method stub
				// int action = event.getAction();
				// if (action == MotionEvent.ACTION_DOWN) {
				// downTime = event.getEventTime();
				// cell._layout.setAlpha(.5f);
				// return true;
				// } else if (action == MotionEvent.ACTION_UP
				// || action == MotionEvent.ACTION_CANCEL) {
				// cell._layout.setAlpha(1.0f);
				// upTime = event.getEventTime();
				//
				// if (upTime - downTime > 150) {
				// try {
				// globalStore = store;
				// _newHandler.removeCallbacks(runnable);
				// _newHandler.postDelayed(runnable, 500);
				// // myStoreButtonPressed(store);
				// } catch (Exception ex) {
				// ex.printStackTrace();
				// }
				// }
				//
				// return true;
				// } else {
				// return false;
				// }
				// }
				// });

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return convertView;
	}

	private void myStoreButtonPressed(Store store) {
		try {
			_handler.getClass().getMethod(_callback, Store.class)
					.invoke(_handler, store);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static class StoreCell {

		Button myStoreButton;
		Button myStoreSelectedButton;
		RelativeLayout _layout;
		SizedImageButton _background;
		SmartTextView _chainTextView;
		SizedImageView _chainImageView;
		SizedImageView _ecartImageView;
		SmartTextView _addressTextView;
		SmartTextView _cityStateZipTextView;
		SmartTextView _ecartTextView;
		SmartTextView _distanceTextView;
		SmartTextView _myStoreTextView;
		SizedImageTextButton _myStoreButton;
	}

}
