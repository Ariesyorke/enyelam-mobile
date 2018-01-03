package com.nyelam.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/3/2018.
 */

public class NYCustomViewPager extends ViewPager {
    private boolean isPagingEnabled = true;
    public NYCustomViewPager(Context context) {
        super(context);
    }

    public NYCustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isPagingEnabled && super.onTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        this.isPagingEnabled = pagingEnabled;
    }
}
