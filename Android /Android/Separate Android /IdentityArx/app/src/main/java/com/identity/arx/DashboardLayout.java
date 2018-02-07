package com.identity.arx;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class DashboardLayout extends ViewGroup {
    private static final int UNEVEN_GRID_PENALTY_MULTIPLIER = 10;
    private int mMaxChildHeight = 0;
    private int mMaxChildWidth = 0;

    public DashboardLayout(Context context) {
        super(context, null);
    }

    public DashboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DashboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        this.mMaxChildWidth = 0;
        this.mMaxChildHeight = 0;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), Integer.MIN_VALUE);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), Integer.MIN_VALUE);
        int count = getChildCount();
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                this.mMaxChildWidth = Math.max(this.mMaxChildWidth, child.getMeasuredWidth());
                this.mMaxChildHeight = Math.max(this.mMaxChildHeight, child.getMeasuredHeight());
            }
        }
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(this.mMaxChildWidth, 1073741824);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(this.mMaxChildHeight, 1073741824);
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
        setMeasuredDimension(resolveSize(this.mMaxChildWidth, widthMeasureSpec), resolveSize(this.mMaxChildHeight, heightMeasureSpec));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onLayout(boolean r22, int r23, int r24, int r25, int r26) {
        /*
        r21 = this;
        r18 = r25 - r23;
        r8 = r26 - r24;
        r6 = r21.getChildCount();
        r16 = 0;
        r9 = 0;
    L_0x000b:
        if (r9 >= r6) goto L_0x0025;
    L_0x000d:
        r0 = r21;
        r3 = r0.getChildAt(r9);
        r19 = r3.getVisibility();
        r20 = 8;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x0022;
    L_0x001f:
        r9 = r9 + 1;
        goto L_0x000b;
    L_0x0022:
        r16 = r16 + 1;
        goto L_0x001f;
    L_0x0025:
        if (r16 != 0) goto L_0x0028;
    L_0x0027:
        return;
    L_0x0028:
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r7 = 0;
        r15 = 0;
        r5 = 1;
    L_0x002e:
        r19 = r16 + -1;
        r19 = r19 / r5;
        r12 = r19 + 1;
        r0 = r21;
        r0 = r0.mMaxChildWidth;
        r19 = r0;
        r19 = r19 * r5;
        r19 = r18 - r19;
        r20 = r5 + 1;
        r7 = r19 / r20;
        r0 = r21;
        r0 = r0.mMaxChildHeight;
        r19 = r0;
        r19 = r19 * r12;
        r19 = r8 - r19;
        r20 = r12 + 1;
        r15 = r19 / r20;
        r19 = r15 - r7;
        r13 = java.lang.Math.abs(r19);
        r19 = r12 * r5;
        r0 = r19;
        r1 = r16;
        if (r0 == r1) goto L_0x0060;
    L_0x005e:
        r13 = r13 * 10;
    L_0x0060:
        if (r13 >= r2) goto L_0x00a3;
    L_0x0062:
        r2 = r13;
        r19 = 1;
        r0 = r19;
        if (r12 != r0) goto L_0x00c8;
    L_0x0069:
        r19 = 0;
        r0 = r19;
        r7 = java.lang.Math.max(r0, r7);
        r19 = 0;
        r0 = r19;
        r15 = java.lang.Math.max(r0, r15);
        r19 = r5 + 1;
        r19 = r19 * r7;
        r19 = r18 - r19;
        r18 = r19 / r5;
        r19 = r12 + 1;
        r19 = r19 * r15;
        r19 = r8 - r19;
        r8 = r19 / r12;
        r17 = 0;
        r9 = 0;
    L_0x008c:
        if (r9 >= r6) goto L_0x0027;
    L_0x008e:
        r0 = r21;
        r3 = r0.getChildAt(r9);
        r19 = r3.getVisibility();
        r20 = 8;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x00cc;
    L_0x00a0:
        r9 = r9 + 1;
        goto L_0x008c;
    L_0x00a3:
        r5 = r5 + -1;
        r19 = r16 + -1;
        r19 = r19 / r5;
        r12 = r19 + 1;
        r0 = r21;
        r0 = r0.mMaxChildWidth;
        r19 = r0;
        r19 = r19 * r5;
        r19 = r18 - r19;
        r20 = r5 + 1;
        r7 = r19 / r20;
        r0 = r21;
        r0 = r0.mMaxChildHeight;
        r19 = r0;
        r19 = r19 * r12;
        r19 = r8 - r19;
        r20 = r12 + 1;
        r15 = r19 / r20;
        goto L_0x0069;
    L_0x00c8:
        r5 = r5 + 1;
        goto L_0x002e;
    L_0x00cc:
        r11 = r17 / r5;
        r4 = r17 % r5;
        r19 = r4 + 1;
        r19 = r19 * r7;
        r20 = r18 * r4;
        r10 = r19 + r20;
        r19 = r11 + 1;
        r19 = r19 * r15;
        r20 = r8 * r11;
        r14 = r19 + r20;
        if (r7 != 0) goto L_0x00fe;
    L_0x00e2:
        r19 = r5 + -1;
        r0 = r19;
        if (r4 != r0) goto L_0x00fe;
    L_0x00e8:
        r20 = r25;
    L_0x00ea:
        if (r15 != 0) goto L_0x0103;
    L_0x00ec:
        r19 = r12 + -1;
        r0 = r19;
        if (r11 != r0) goto L_0x0103;
    L_0x00f2:
        r19 = r26;
    L_0x00f4:
        r0 = r20;
        r1 = r19;
        r3.layout(r10, r14, r0, r1);
        r17 = r17 + 1;
        goto L_0x00a0;
    L_0x00fe:
        r19 = r10 + r18;
        r20 = r19;
        goto L_0x00ea;
    L_0x0103:
        r19 = r14 + r8;
        goto L_0x00f4;
        */
        throw new UnsupportedOperationException("Method not decompiled: identity.arx.axs.com.identityaxs.DashboardLayout.onLayout(boolean, int, int, int, int):void");
    }
}
