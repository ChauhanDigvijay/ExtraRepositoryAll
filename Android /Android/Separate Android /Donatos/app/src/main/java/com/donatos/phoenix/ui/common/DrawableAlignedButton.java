package com.donatos.phoenix.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class DrawableAlignedButton extends AppCompatButton {
    private Drawable f8534a;

    public DrawableAlignedButton(Context context) {
        super(context);
    }

    public DrawableAlignedButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DrawableAlignedButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    protected void onDraw(Canvas canvas) {
        if (this.f8534a != null) {
            canvas.save();
            canvas.translate((float) ((this.f8534a.getIntrinsicWidth() / 2) + 2), 0.0f);
        }
        super.onDraw(canvas);
        if (this.f8534a != null) {
            canvas.restore();
            canvas.save();
            canvas.translate((float) ((int) (((((float) getWidth()) / 2.0f) - (((float) ((int) getPaint().measureText(getText().toString()))) / 2.0f)) - ((float) this.f8534a.getIntrinsicWidth()))), (float) ((getHeight() - this.f8534a.getIntrinsicHeight()) / 2));
            this.f8534a.draw(canvas);
            canvas.restore();
        }
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.f8534a != null) {
            setMeasuredDimension(getMeasuredWidth(), Math.max(getMeasuredHeight(), (this.f8534a.getIntrinsicHeight() + getPaddingTop()) + getPaddingBottom()));
        }
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            this.f8534a = drawable;
        }
    }
}
