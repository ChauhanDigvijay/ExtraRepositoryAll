package com.olo.jambajuice.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.olo.jambajuice.Utils.FontsManager;

public class MediumTextView extends TextView {

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
