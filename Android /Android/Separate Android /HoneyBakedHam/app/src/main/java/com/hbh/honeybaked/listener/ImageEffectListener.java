package com.hbh.honeybaked.listener;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageEffectListener implements OnTouchListener {
    int colorValue = 0;

    public ImageEffectListener(int color) {
        this.colorValue = color;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof ImageView) {
            ImageView v1 = (ImageView) v;
            int maskId = event.getActionMasked();
            if (maskId == 0) {
                if (v1.getDrawable() != null) {
                    v1.getDrawable().setColorFilter(this.colorValue, Mode.SRC_ATOP);
                    v1.invalidate();
                }
            } else if ((maskId == 3 || maskId == 1) && v1.getDrawable() != null) {
                v1.getDrawable().clearColorFilter();
                v1.invalidate();
            }
        }
        return false;
    }
}
