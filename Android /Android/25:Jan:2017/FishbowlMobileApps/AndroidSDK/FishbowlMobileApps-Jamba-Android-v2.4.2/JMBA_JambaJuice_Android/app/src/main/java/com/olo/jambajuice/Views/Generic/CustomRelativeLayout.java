package com.olo.jambajuice.Views.Generic;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;

/**
 * Created by Nauman on 01/07/15.
 */
public class CustomRelativeLayout extends RelativeLayout {
    private Float mAspectRatio = null;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(context, attrs);
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
                R.styleable.CustomRelativeLayout,
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