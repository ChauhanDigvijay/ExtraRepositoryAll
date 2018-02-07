package com.hbh.honeybaked.supportingfiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.TextView;
import com.hbh.honeybaked.R;

public class CircularTextView extends TextView {
    boolean selected = false;
    private float strokeWidth;

    public CircularTextView(Context context) {
        super(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void draw(Canvas canvas) {
        int diameter;
        Paint circlePaint = new Paint();
        Paint strokePaint = new Paint();
        if (this.selected) {
            circlePaint.setColor(getColor(R.color.white));
            strokePaint.setColor(getColor(R.color.white));
        } else {
            circlePaint.setColor(getColor(R.color.ham_offer_yellow));
            strokePaint.setColor(getColor(R.color.ham_offer_yellow));
        }
        circlePaint.setFlags(1);
        strokePaint.setFlags(1);
        int h = getHeight();
        int w = getWidth();
        if (h > w) {
            diameter = h;
        } else {
            diameter = w;
        }
        int radius = diameter / 2;
        setHeight(diameter);
        setWidth(diameter);
        canvas.drawCircle((float) (diameter / 2), (float) (diameter / 2), (float) radius, strokePaint);
        canvas.drawCircle((float) (diameter / 2), (float) (diameter / 2), ((float) radius) - this.strokeWidth, circlePaint);
        super.draw(canvas);
    }

    private int getColor(int colorValue) {
        if (VERSION.SDK_INT >= 23) {
            return getContext().getResources().getColor(colorValue, null);
        }
        return getContext().getResources().getColor(colorValue);
    }

    public void setStrokeWidth(int dp) {
        this.strokeWidth = ((float) dp) * getContext().getResources().getDisplayMetrics().density;
    }

    public void setSelection(boolean selected) {
        this.selected = selected;
        invalidate();
    }
}
