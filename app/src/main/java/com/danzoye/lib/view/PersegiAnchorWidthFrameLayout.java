package com.danzoye.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class PersegiAnchorWidthFrameLayout extends FrameLayout {

    public PersegiAnchorWidthFrameLayout(Context context) {
        super(context);
    }

    public PersegiAnchorWidthFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersegiAnchorWidthFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeightSize = measuredWidthSize;

        int desiredWidthSpec = MeasureSpec.makeMeasureSpec(measuredWidthSize,
                MeasureSpec.EXACTLY);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(desiredWidthSpec, desiredHeightSpec);
    }
}
