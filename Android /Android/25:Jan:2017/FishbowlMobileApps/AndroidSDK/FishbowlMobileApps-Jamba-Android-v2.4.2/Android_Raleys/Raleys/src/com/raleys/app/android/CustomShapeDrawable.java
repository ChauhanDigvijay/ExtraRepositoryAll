package com.raleys.app.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;

public class CustomShapeDrawable extends ShapeDrawable {

	Paint fillpaint, strokepaint;

	public CustomShapeDrawable(RoundRectShape rect, int gray, int white, int i) {

		super(rect);
		fillpaint = new Paint(this.getPaint());
		fillpaint.setColor(white);
		strokepaint = new Paint(fillpaint);
		strokepaint.setStyle(Paint.Style.STROKE);
		strokepaint.setStrokeWidth(i);
		strokepaint.setColor(gray);

	}

	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
		shape.draw(canvas, fillpaint);
		shape.draw(canvas, strokepaint);
	}

}
