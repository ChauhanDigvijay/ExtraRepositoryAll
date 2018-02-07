package com.olo.jambajuice.Views.Generic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.olo.jambajuice.R;

/**
 * Created by Nauman Afzaal on 10/07/15.
 */
public class MyRewardHeaderImageView extends ImageView {
    public MyRewardHeaderImageView(Context context) {
        super(context);
    }

    public MyRewardHeaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRewardHeaderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.myRewardHeaderAspectRatio, outValue, true);
        float myRewardHeaderAspectRatio = outValue.getFloat();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (int) (widthSize / myRewardHeaderAspectRatio);
        int newHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightSpec);
    }
}
