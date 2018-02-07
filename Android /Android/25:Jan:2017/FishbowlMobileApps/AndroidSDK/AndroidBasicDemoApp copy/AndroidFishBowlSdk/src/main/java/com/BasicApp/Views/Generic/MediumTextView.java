package com.BasicApp.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.BasicApp.Utils.FontsManager;
/**
 * Created by digvijay(dj)
 */

public class MediumTextView extends android.support.v7.widget.AppCompatTextView {

    public MediumTextView(Context context) {
        super(context);
        setFont();
    }

    public MediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public MediumTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            Typeface typeface = FontsManager.getInstance().getArcherMediumFont();
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
