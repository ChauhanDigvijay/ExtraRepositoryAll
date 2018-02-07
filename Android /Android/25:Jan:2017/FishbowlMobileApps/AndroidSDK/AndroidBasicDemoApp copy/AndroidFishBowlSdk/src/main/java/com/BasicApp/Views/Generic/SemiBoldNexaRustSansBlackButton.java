package com.BasicApp.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.BasicApp.Utils.FontsManager;


/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class SemiBoldNexaRustSansBlackButton extends android.support.v7.widget.AppCompatButton {

    public SemiBoldNexaRustSansBlackButton(Context context) {
        super(context);
        setFont();
    }

    public SemiBoldNexaRustSansBlackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public SemiBoldNexaRustSansBlackButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
