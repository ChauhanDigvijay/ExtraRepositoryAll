package com.donatos.phoenix.ui.checkout;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import com.facebook.stetho.websocket.CloseCodes;

public class SnappyRecyclerView extends RecyclerView {
    public SnappyRecyclerView(Context context) {
        super(context);
    }

    public SnappyRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SnappyRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public final boolean mo1587b(int i, int i2) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        int i3 = Resources.getSystem().getDisplayMetrics().widthPixels;
        View a = linearLayoutManager.mo780a(linearLayoutManager.m3444o());
        View a2 = linearLayoutManager.mo780a(linearLayoutManager.m3442m());
        int width = (i3 - a.getWidth()) / 2;
        int width2 = ((i3 - a2.getWidth()) / 2) + a2.getWidth();
        int left = a.getLeft();
        int right = a2.getRight();
        width = left - width;
        width2 -= right;
        if (Math.abs(i) < CloseCodes.NORMAL_CLOSURE) {
            if (left > i3 / 2) {
                m122a(-width2, 0);
            } else if (right < i3 / 2 || i <= 0) {
                m122a(width, 0);
            } else {
                m122a(-width2, 0);
            }
        } else if (i > 0) {
            m122a(width, 0);
        } else {
            m122a(-width2, 0);
        }
        return true;
    }

    public final void mo1588c(int i) {
        super.mo1588c(i);
        if (i == 0) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            int i2 = Resources.getSystem().getDisplayMetrics().widthPixels;
            View a = linearLayoutManager.mo780a(linearLayoutManager.m3444o());
            View a2 = linearLayoutManager.mo780a(linearLayoutManager.m3442m());
            int width = (i2 - a.getWidth()) / 2;
            int width2 = ((i2 - a2.getWidth()) / 2) + a2.getWidth();
            int left = a.getLeft();
            int right = a2.getRight();
            width = left - width;
            width2 -= right;
            if (left > i2 / 2) {
                m122a(-width2, 0);
            } else if (right < i2 / 2) {
                m122a(width, 0);
            }
        }
    }
}
