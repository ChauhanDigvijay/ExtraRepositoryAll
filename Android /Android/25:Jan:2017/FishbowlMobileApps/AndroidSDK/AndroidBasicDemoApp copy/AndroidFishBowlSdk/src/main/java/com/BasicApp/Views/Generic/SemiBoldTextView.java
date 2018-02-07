package com.BasicApp.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.BasicApp.Utils.FontsManager;


/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class SemiBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public SemiBoldTextView(Context context) {
        super(context);
        setFont();
    }

    public SemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public SemiBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            Typeface typeface = FontsManager.getInstance().getArcherSemiBoldFont();
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
