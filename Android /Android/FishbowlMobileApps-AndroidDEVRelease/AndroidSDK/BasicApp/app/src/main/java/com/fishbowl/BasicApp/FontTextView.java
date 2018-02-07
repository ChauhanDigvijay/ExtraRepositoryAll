package com.fishbowl.BasicApp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by digvijay(dj)
 */

public class FontTextView extends TextView
{
    public FontTextView(Context context)
    {
        super(context);
        setFont();
    }

    public FontTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setFont();
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont()
    {
        if(!isInEditMode())
        {
            Typeface typeface = FontUtil.getInstance().getArcherSemiBoldFont();
            if (typeface != null)
            {
                setTypeface(typeface);
            }
        }
    }
}
