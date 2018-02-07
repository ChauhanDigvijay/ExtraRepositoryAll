package com.olo.jambajuice.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.olo.jambajuice.Utils.FontsManager;


public class SemiBoldNexaRustSansBlackTextView extends TextView {
    public SemiBoldNexaRustSansBlackTextView(Context context) {
        super(context);
        setFont();
    }

    public SemiBoldNexaRustSansBlackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public SemiBoldNexaRustSansBlackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            Typeface typeface = FontsManager.getInstance().getNexaRustFont();
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
