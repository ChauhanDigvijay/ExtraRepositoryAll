package com.olo.jambajuice.Views.Generic;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Nauman Afzaal on 15/05/15.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final GestureDetector gestureDetector;
    private ScrollView scrollView;
    private ViewGroup pager;

    public OnSwipeTouchListener(Context ctx, ScrollView view, ViewGroup pager) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.scrollView = view;
        this.pager = pager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return pager.onTouchEvent(event);
    }

    public void onSwipeRight() {
        scrollView.requestDisallowInterceptTouchEvent(true);
    }

    public void onSwipeLeft() {
        scrollView.requestDisallowInterceptTouchEvent(true);
    }

    public void onSwipeTop() {
        scrollView.requestDisallowInterceptTouchEvent(false);
    }

    public void onSwipeBottom() {
        scrollView.requestDisallowInterceptTouchEvent(false);
    }

    public void onSingleTap() {
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                if (e1 == null || e2 == null) {
                    return false;
                }
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onSingleTap();
            return super.onSingleTapConfirmed(e);
        }
    }
}