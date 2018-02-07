package com.olo.jambajuice.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.olo.jambajuice.Utils.FontsManager;

/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class BoldTextView extends TextView {
    public BoldTextView(Context context) {
        super(context);
        setFont();
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            Typeface typeface = FontsManager.getInstance().getArcherBoldFont();
            if (typeface != null) {
                setTypeface(typeface);

            }
        }
    }
}
