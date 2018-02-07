package com.fishbowl.LoyaltyTabletApp.View.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.fishbowl.LoyaltyTabletApp.Utils.FontsManager;


/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class SemiBoldNexaRustSansBlackButton extends Button {

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
