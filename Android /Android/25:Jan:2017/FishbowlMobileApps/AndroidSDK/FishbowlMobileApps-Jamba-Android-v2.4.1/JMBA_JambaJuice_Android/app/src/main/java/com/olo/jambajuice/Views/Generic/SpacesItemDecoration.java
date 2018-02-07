package com.olo.jambajuice.Views.Generic;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ihsanulhaq on 6/30/2015.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean hasHeader;

    public SpacesItemDecoration(int space, boolean hasHeader) {
        this.space = space;
        this.hasHeader = hasHeader;
    }

    public SpacesItemDecoration(boolean hasHeader) {
        this.space = 1;
        this.hasHeader = hasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (hasHeader) {
            if (parent.getChildPosition(view) == 0) {
                return;
            }
        }

        outRect.top = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) % 2 == 0) {
            if (hasHeader) {
                outRect.left = space;
            } else {
                outRect.right = space;
            }
        } else {
            if (hasHeader) {
                outRect.right = space;
            } else {
                outRect.left = space;
            }
        }
    }
}