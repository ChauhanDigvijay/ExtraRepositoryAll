package com.raleys.app.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.widget.Button;

import com.raleys.app.android.models.ProductCategory;
import com.raleys.libandroid.Utils;

@SuppressLint("ViewConstructor")
public class ProductCategoryButton extends Button {

	private RaleysApplication _app;
	private Paint _paint;
	private int _width;
	private int _height;
	private int _imageSize;
	private Bitmap _expandedStateImage;
	private Bitmap _background;
	private Typeface _font;
	public ProductCategory _category;
	public int _yOrigin;
	public boolean _expanded;
	private Rect _textBounds;
	private Rect _backgroundRect;
	int textXOrigin = 0;
	String arrayString = "";
	String Curr_cat_name = "";

	public ProductCategoryButton(Context context, int width, int height,
			Typeface font, ProductCategory category) {
		super(context);
		_app = (RaleysApplication) context.getApplicationContext();
		_paint = new Paint();
		_expanded = false;
		_width = width;
		_height = height;
		_imageSize = (int) (_height * .4);
		_font = font;
		_category = category;
		_textBounds = new Rect();
		_backgroundRect = new Rect(0, 0, _width, _height);

		if (_app._productCategoryButtonBitmap == null)
			_app._productCategoryButtonBitmap = _app.getAppBitmap(
					"product_category_background", R.drawable.cat_list_cell,
					_width, _height);

		if (_app._productCategorySelectedButtonBitmap == null)
			_app._productCategorySelectedButtonBitmap = _app.getAppBitmap(
					"product_category_selected_background",
					R.drawable.cat_list_cell_selected, _width, _height);

		_background = _app._productCategoryButtonBitmap;

		// heavily used bitmaps should only be scaled once and stored in the
		// app...
		if (_app._categoryForwardBitmap == null)
			_app._categoryForwardBitmap = _app.getAppBitmap("forward_button",
					R.drawable.forward_button, _imageSize, _imageSize);

		if (_app._categoryDownBitmap == null)
			_app._categoryDownBitmap = _app.getAppBitmap("down_button",
					R.drawable.down_button, _imageSize, _imageSize);

		// if (_app._categoryGoBitmap == null)
		// _app._categoryGoBitmap = _app.getAppBitmap("category_go_button",
		// R.drawable.button_go, _imageSize, _imageSize);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		this.setMeasuredDimension(_width, _height);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		try {

			int inset = _category.level == 1 ? (int) (_width * .9)
					: (int) (_width * .8)
							+ ((_category.level - 1) * (int) (_width * .1));
			int imageYOrigin = (_height - _imageSize) / 2;
			int textPad = (int) (_width * .01);
			// int textXOrigin = inset + + textPad;
			if (_category.level == 1) {
				textXOrigin = _imageSize + textPad;
			} else if (_category.level == 2) {
				textXOrigin = (_imageSize + textPad) * 2;
			} else if (_category.level == 3) {
				textXOrigin = (_imageSize + textPad) * 3;
			}
			String categoryCount = "";

			if (_category.subCategoryList != null
					&& _category.subCategoryList.size() > 0) {
				categoryCount = String
						.valueOf(_category.subCategoryList.size());

				if (_expanded == true)
					_expandedStateImage = _app._categoryDownBitmap;
				else
					_expandedStateImage = _app._categoryForwardBitmap;
			} else {
				Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf
																// types
				Bitmap bmp = Bitmap.createBitmap(_imageSize, _imageSize, conf); // this
																				// creates
																				// a
																				// MUTABLE
																				// bitmap
				_expandedStateImage = bmp;
			}
			super.onDraw(canvas);
			// draw background
			try {
				canvas.drawBitmap(_background, null, _backgroundRect, _paint);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			_paint.setStyle(Paint.Style.FILL);
			_paint.setColor(Color.TRANSPARENT);
			canvas.drawPaint(_paint);
			// draw text
			_paint.setColor(Color.DKGRAY); // txt color
			_paint.setAntiAlias(true); // ???
			_paint.setTypeface(_font);
			_paint.setTextAlign(Align.LEFT);
			_paint.setTextSize((Utils.getFontSizeByHeight(
					(int) (_height * .38), _font))); // 4
			_paint.getTextBounds(_category.name, 0, _category.name.length(),
					_textBounds);
			Paint.FontMetrics fontMetrics = _paint.getFontMetrics();
			float fontMetricsHeight = ((fontMetrics.top * -1) + fontMetrics.bottom);
			float unusedVerticalSpace = _height - fontMetricsHeight;
			float yOrigin = _height - (_height + fontMetrics.top)
					+ (unusedVerticalSpace / 2);
			Curr_cat_name = _category.name;
			// check if count available for category
			if (categoryCount == "") {
				Curr_cat_name = Curr_cat_name.concat(" ");
			} else {
				Curr_cat_name = Curr_cat_name
						.concat(" (" + categoryCount + ")");
			}
			int _lines = 1;
			int lineHeight = _height / _lines;
			if (Curr_cat_name == null)
				arrayString = (String) getText();
			else
				arrayString = Curr_cat_name;

			ArrayList<String> textArray = Utils.wrapText(arrayString,
					(int) (_width * .75), _paint); // allow a small pad on each
													// side
			int arrayCount = textArray.size();
			int lastLineIndex = _lines - 1;

			Rect[] _lineRects = new Rect[_lines];
			for (int i = 0; i < _lines; i++)
				_lineRects[i] = new Rect();

			if (arrayCount > _lines) {
				String lastLine = textArray.get(_lines - 1);
				String adjustedLastLine = lastLine.substring(0,
						lastLine.length() - 1)
						+ "..."; // truncate the last
									// allowable line

				for (int i = lastLineIndex; i < arrayCount; i++)
					// remove the last allowable line and beyond
					textArray.remove(lastLineIndex);

				textArray.add(adjustedLastLine); // put the adjusted lastLine
													// back
			}

			int actualLines = textArray.size() < _lines ? textArray.size()
					: _lines;
			int yPadding = actualLines < _lines ? ((_lines - actualLines) * lineHeight) / 2
					: 0;
			String text = null;
			for (int i = 0; i < actualLines; i++) {
				_lineRects[i].left = 0;
				_lineRects[i].top = (lineHeight * i) + yPadding;
				_lineRects[i].right = _width;
				_lineRects[i].bottom = (lineHeight * i) + lineHeight;
				text = textArray.get(i);
				canvas.drawText(text, textXOrigin, yOrigin, _paint);
			}
			// draw image
			canvas.drawBitmap(_expandedStateImage, inset, imageYOrigin, _paint);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN)
			_background = _app._productCategorySelectedButtonBitmap;
		else if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_CANCEL)
			_background = _app._productCategoryButtonBitmap;

		invalidate();
		return true;
	}
	public void setExpandedState(boolean expanded) {
		_expanded = expanded;
	}

	public void toggleExpandedState() {
		if (_expanded == true)
			_expanded = false;
		else
			_expanded = true;
	}
}
