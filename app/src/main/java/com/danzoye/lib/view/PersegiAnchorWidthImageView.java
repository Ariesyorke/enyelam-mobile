package com.danzoye.lib.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class PersegiAnchorWidthImageView extends AppCompatImageView {

    public PersegiAnchorWidthImageView(Context context) {
        super(context);
    }

    public PersegiAnchorWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersegiAnchorWidthImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
