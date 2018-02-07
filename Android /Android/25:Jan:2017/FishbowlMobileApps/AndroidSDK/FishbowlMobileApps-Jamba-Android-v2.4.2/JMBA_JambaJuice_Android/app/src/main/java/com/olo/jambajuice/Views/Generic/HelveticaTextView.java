package com.olo.jambajuice.Views.Generic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.olo.jambajuice.Utils.FontsManager;

/**
 * Created by vt021 on 08/06/17.
 */

public class HelveticaTextView extends TextView{

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
