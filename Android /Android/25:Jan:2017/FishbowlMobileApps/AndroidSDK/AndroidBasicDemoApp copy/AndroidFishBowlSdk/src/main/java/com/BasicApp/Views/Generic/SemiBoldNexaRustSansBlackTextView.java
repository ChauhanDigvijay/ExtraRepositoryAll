package com.BasicApp.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.BasicApp.Utils.FontsManager;


public class SemiBoldNexaRustSansBlackTextView extends android.support.v7.widget.AppCompatTextView {
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
