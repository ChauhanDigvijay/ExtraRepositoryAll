package com.BasicApp.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.BasicApp.Utils.FontsManager;


/**
 * Created by vt021 on 08/06/17.
 */

public class SanSerifTextView extends android.support.v7.widget.AppCompatTextView {

    public SanSerifTextView(Context context) {
        super(context);
        setFont();
    }

    public SanSerifTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public SanSerifTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            Typeface typeface = FontsManager.getInstance().getSanSerifFont();
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}