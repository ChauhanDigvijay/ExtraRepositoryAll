package com.BasicApp.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.BasicApp.Utils.FontsManager;


/**
 * Created by digvijay(dj)
 */
public class BoldTextView extends android.support.v7.widget.AppCompatTextView {
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
