package com.danzoye.lib.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.lang.reflect.Field;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class ViewPagerAdjustableDuration extends ViewPager {
    public ViewPagerAdjustableDuration(Context context) {
        super(context);
        init();
    }

    public ViewPagerAdjustableDuration(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(),
                    new AccelerateDecelerateInterpolator());
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }
}
