package com.danzoye.lib.view;

import android.content.Context;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {

    private int mDuration = 500;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context,
                              android.view.animation.Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    public FixedSpeedScroller(Context context,
                              android.view.animation.Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
