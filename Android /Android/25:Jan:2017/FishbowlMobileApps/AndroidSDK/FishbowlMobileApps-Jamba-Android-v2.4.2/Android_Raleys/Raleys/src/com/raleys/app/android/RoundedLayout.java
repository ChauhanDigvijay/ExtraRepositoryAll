package com.raleys.app.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RoundedLayout extends LinearLayout {

	private RectF rect;
	private Paint paint;
	BitmapShader bitmapShader;
	int width;
	int height;
	Context context;

	public RoundedLayout(Context _context, int _width, int _height) {
		super(_context);
		width = _width;
		height = _height;
		context = _context;
		init();
	}

	public RoundedLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.parseColor("#7EB5D6"));
		Bitmap b = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.back_transparent);
		bitmapShader = new BitmapShader(b, TileMode.CLAMP, TileMode.CLAMP);
		paint.setShader(bitmapShader);
		rect = new RectF(0.0f, 0.0f, width, height);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRoundRect(rect, 20, 20, paint);
	}
}
