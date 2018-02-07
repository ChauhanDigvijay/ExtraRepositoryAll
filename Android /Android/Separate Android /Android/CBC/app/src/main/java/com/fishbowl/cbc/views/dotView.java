package com.fishbowl.cbc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by VT027 on 4/22/2017.
 */

public class dotView extends View {

    public dotView(Context context) {
        super(context);
    }

    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
        Paint paint = new Paint();
        Path path = new Path();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);
        c.drawPaint(paint);
        for (int i = 50; i < 100; i++) {
            path.moveTo(i, i - 1);
            path.lineTo(i, i);
        }
        path.close();
        paint.setStrokeWidth(3);
        paint.setPathEffect(null);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        c.drawPath(path, paint);
    }
}
