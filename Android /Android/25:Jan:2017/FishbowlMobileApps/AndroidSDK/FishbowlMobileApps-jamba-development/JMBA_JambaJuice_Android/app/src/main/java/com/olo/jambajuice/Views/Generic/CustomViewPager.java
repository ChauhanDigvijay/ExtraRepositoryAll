package com.olo.jambajuice.Views.Generic;

/**
 * Created by Ihsanulhaq on 6/18/2015.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductDetailViewPagerActivity;
import com.olo.jambajuice.R;

public class CustomViewPager extends ViewPager {
    private Float mAspectRatio = null;
    private boolean enabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if (this.enabled) {
                return super.onInterceptTouchEvent(event);
            }
        }catch(IllegalArgumentException e){}
        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAspectRatio == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = (int) (widthSize / mAspectRatio);
            int newHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, newHeightSpec);
        }
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomViewPager,
                0, 0);

        try {
            float sentinel = -9.0f;
            mAspectRatio = a.getFloat(R.styleable.CustomViewPager_pagerAspectRatio, sentinel);
            if (mAspectRatio == sentinel) {
                mAspectRatio = null;
            }
        } finally {
            a.recycle();
        }
    }
}