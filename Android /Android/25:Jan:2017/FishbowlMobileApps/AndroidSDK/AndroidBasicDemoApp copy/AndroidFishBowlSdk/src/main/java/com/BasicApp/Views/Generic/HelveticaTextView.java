package com.BasicApp.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.BasicApp.Utils.FontsManager;

;

/**
 * Created by digvijay(dj)
 */

public class HelveticaTextView extends android.support.v7.widget.AppCompatTextView {

    public HelveticaTextView(Context context) {
        super(context);
        setFont();
    }

    public HelveticaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public HelveticaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            Typeface typeface = FontsManager.getInstance().getHelveticaFont();
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
