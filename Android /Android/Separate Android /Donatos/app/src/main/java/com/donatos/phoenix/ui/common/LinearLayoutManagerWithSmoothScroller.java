package com.donatos.phoenix.ui.common;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.C0869r;
import android.support.v7.widget.ap;

public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {

    private class C2724a extends ap {
        final /* synthetic */ LinearLayoutManagerWithSmoothScroller f8535l;

        public C2724a(LinearLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller, Context context) {
            this.f8535l = linearLayoutManagerWithSmoothScroller;
            super(context);
        }

        public final PointF mo1596a(int i) {
            return this.f8535l.mo792b(i);
        }

        protected final int mo1597b() {
            return -1;
        }
    }

    public LinearLayoutManagerWithSmoothScroller(Context context) {
        super(context, 1, false);
    }

    public LinearLayoutManagerWithSmoothScroller(Context context, int i, boolean z) {
        super(context, i, z);
    }

    public final void mo786a(RecyclerView recyclerView, int i) {
        C0869r c2724a = new C2724a(this, recyclerView.getContext());
        c2724a.m3623b(i);
        m3320a(c2724a);
    }
}
