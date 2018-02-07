package com.fishbowl.cbc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

public class HexagonMaskView extends View {
    private Path mHexagonPath;
    private float mRadius;
    private float mWidth, mHeight;
    private int mMaskColor;
    private Paint mPaint;


    public HexagonMaskView(Context context) {
        super(context);
        init();
    }

    public HexagonMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HexagonMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init() {
        mHexagonPath = new Path();
        mMaskColor = Color.WHITE;

    }

    public void setRadius(float r) {
        this.mRadius = r;
        calculatePath();
    }

    public void setMaskColor(int color) {
        this.mMaskColor = color;
        invalidate();
    }

    private void calculatePath() {
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        float centerX = mWidth / 2;
        float centerY = mHeight / 2;
        float radius = (float) mHeight * (float) 0.15;

        mHexagonPath.moveTo(0, radius);
        mHexagonPath.lineTo(centerX, 0);
        mHexagonPath.lineTo(mWidth, radius);
        mHexagonPath.lineTo(mWidth, mHeight - radius);
        mHexagonPath.lineTo(centerX, mHeight);
        mHexagonPath.lineTo(0, mHeight - radius);
        mHexagonPath.lineTo(0, radius);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.TRANSPARENT);      // transparent to show the background of hexagonmaskview
        mPaint.setStrokeWidth(5);               // set the size
        mPaint.setDither(true);                // set the dither to true
        mPaint.setStyle(Paint.Style.STROKE);    // set the style for stroke
        mPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mPaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        mPaint.setPathEffect(new CornerPathEffect(10));   // set the path effect when they join.
        mPaint.setAntiAlias(true);
//        System.out.println(mWidth);
//        System.out.println(mHeight);
//        System.out.println(centerX);
//        System.out.println(centerY);

        invalidate();
    }

    @Override
    public void onDraw(Canvas c) {
        clearAnimation();
        super.onDraw(c);
//        c.clipPath(hexagonBorderPath, Region.Op.DIFFERENCE);
//        c.drawColor(Color.WHITE);
//        c.save();
        c.drawPath(mHexagonPath, mPaint);
        c.clipPath(mHexagonPath, Region.Op.DIFFERENCE);
        c.drawColor(mMaskColor);
        c.save();
    }

    // getting the view size and default radius
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculatePath();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculatePath();
    }
}